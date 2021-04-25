package main

import (
	"crypto"
	"crypto/rand"
	"crypto/rsa"
	"crypto/sha256"
  "crypto/aes"
	"crypto/cipher"
	"encoding/base64"
  "encoding/hex"
  "bytes"
  "strings"
	"fmt"
  "crypto/x509"
  "encoding/pem"
  "golang.org/x/crypto/pbkdf2"
  "os"
)

func main() {

  fmt.Println("Load RSA PKCS8 encrypted private key AES256-CBC PBKDF2 HMAC with SHA-256")

  dataToSignString := string("The quick brown fox jumps over the lazy dog")
  dataToSign := []byte(dataToSignString)
  fmt.Println("dataToSign: " + dataToSignString)

  passphrase := string("123456")

  // usually we would load the private and public key from a file or keystore
  // here we use hardcoded keys for demonstration - don't do this in real programs

  fmt.Println("\n* * * sign the plaintext with the encrypted RSA private key * * *")
  // first we load the RSA private key encrypted pem
  privateKeyEncryptedPem := string(LoadRsaPrivateKeyEncryptedPem())
  // then we decrypt the data and receive the DER encoded key
  rsaPrivateKeyByte := []byte(ReadRsaPrivateKeyEncryptedPbes2Aes256Cbc(privateKeyEncryptedPem, passphrase))
  // test with signature
  signatureBase64 := RsaSignToBase64Der(rsaPrivateKeyByte, dataToSign);
  fmt.Println("\nsignature (Base64): " + signatureBase64)

  fmt.Println("\n* * * verify the signature against the plaintext with the RSA public key * * *")
  // load public key
  publicKeyByte := []byte(LoadRsaPublicKeyPem());
  fmt.Println("public key loaded")
  signatureVerified := string(RsaVerifySignatureFromBase64(publicKeyByte, dataToSign, signatureBase64))
  fmt.Println("\nsignature (Base64) verified: " + signatureVerified)
}

func ReadRsaPrivateKeyEncryptedPbes2Aes256Cbc(privateKeyEncryptedPem string, passphrase string)([]byte) {
  // note: this function is hardcoded to read an encrypted RSA private key with the following spcifications:
  // key format: PKCS#8, key encoding: PEM encoded, password encryption scheme: PBES2 [OpenSSL -v2], password derivation method: PBKDF2 with HMAC SHA-256, maximum iterations: 8388607, encryption algorithm: AES-256-CBC
  privateKeyEncryptedString := strings.Replace(privateKeyEncryptedPem,"-----BEGIN ENCRYPTED PRIVATE KEY-----", "", 1)
  privateKeyEncryptedString = strings.Replace(privateKeyEncryptedString,"-----END ENCRYPTED PRIVATE KEY-----", "", 1)
  privateKeyEncByte := Base64Decoding(privateKeyEncryptedString)
  // fixed identifier
  IDENTIFIER_PKCS5PBES2          := string("2A864886F70D01050D")
  IDENTIFIER_PKCS5PBKDF2         := string("2A864886F70D01050C")
  IDENTIFIER_PKCS5HMACWITHSHA256 := string("2A864886F70D020905")
  IDENTIFIER_AES256CBC           := string("60864801650304012a")
  IDENTIFIER_PKCS5PBES2_byte := []byte(HexStringToByteArray(IDENTIFIER_PKCS5PBES2))
  IDENTIFIER_PKCS5PBKDF2_byte := []byte(HexStringToByteArray(IDENTIFIER_PKCS5PBKDF2))
  IDENTIFIER_PKCS5HMACWITHSHA256_byte := []byte(HexStringToByteArray(IDENTIFIER_PKCS5HMACWITHSHA256))
  IDENTIFIER_AES256CBC_byte := []byte(HexStringToByteArray(IDENTIFIER_AES256CBC))
  posIDENTIFIER_PKCS5PBES2 := int(IndexOf(privateKeyEncByte, IDENTIFIER_PKCS5PBES2_byte))
  posIDENTIFIER_PKCS5PBKDF2 := int(IndexOf(privateKeyEncByte, IDENTIFIER_PKCS5PBKDF2_byte))
  posIDENTIFIER_PKCS5HMACWITHSHA256 := int(IndexOf(privateKeyEncByte, IDENTIFIER_PKCS5HMACWITHSHA256_byte))
  posIDENTIFIER_AES256CBC := int(IndexOf(privateKeyEncByte, IDENTIFIER_AES256CBC_byte))
  // vanity check - all values are > 0
  systemExit := false
  if (posIDENTIFIER_PKCS5PBES2 < 1) {systemExit = true}
  if (posIDENTIFIER_PKCS5PBKDF2 < 1) {systemExit = true}
  if (posIDENTIFIER_PKCS5HMACWITHSHA256 < 1) {systemExit = true}
  if (posIDENTIFIER_AES256CBC < 1) {systemExit = true}
  if (systemExit == true) {
    fmt.Println("*** SYSTEM ERROR **")
    fmt.Println("One or more identifier not found in header.")
    fmt.Println("This function ONLY works with PBES2 encryption scheme, PBKDF2 key derivation, HMACwithSHA256 hashing and AES256CBC encryption.")
    fmt.Println("System halted.")
    os.Exit(0)
  }
    // find salt and iterations after posIDENTIFIER_PKCS5PBKDF2 1c = length can differ depending on iterations length
  posSALT := int(posIDENTIFIER_PKCS5PBKDF2 + (9 + 4)) // pos is at the beginning of identifier
  SALT := make([]byte, 8)
  SALT = copyOfRange(privateKeyEncByte, posSALT, (posSALT + 8))
  // iteration starts 3 bytes after end of salt but length may differ, so we need to check length tag + 2 after end of salt
  iterationLength := int(privateKeyEncByte[(posSALT + 8 + 1)])
  // maximum iterationLength supported: 3
  if (iterationLength > 3) {
    fmt.Println()
    fmt.Println("*** SYSTEM ERROR **")
    fmt.Println("maximum IterationLength supported: 3")
    fmt.Println("System halted.")
    os.Exit(0)
  }
  iterationByte := copyOfRange(privateKeyEncByte, (posSALT + 8 + 2),  (posSALT + 8 + 2 + iterationLength))
  //iterations = new BigInteger(iterationByte).intValueExact()
  iterations := int(0)
  if (iterationLength == 1) {
    iterations = int(uint(iterationByte[0]))
  }
  if (iterationLength == 2) {
    iterations = int(uint(iterationByte[1]) | uint(iterationByte[0])<<8)
  }
  if (iterationLength == 3) {
    iterations = int(uint(iterationByte[2]) | uint(iterationByte[1])<<8 | uint(iterationByte[0])<<16)
  }
  // find iv after end of IDENTIFIER_AES256CBC
  ivByte := copyOfRange(privateKeyEncByte, (posIDENTIFIER_AES256CBC + 9 + 2), (posIDENTIFIER_AES256CBC + 9 + 2 + 16));
        // now everything is available for decryption
  key := []byte(pbkdf2.Key([]byte(passphrase), SALT, iterations, 32, sha256.New))
  // get the encrypted key material = ciphertext
  privateKeyEncByteLength := len(privateKeyEncByte)
  headerLength := posIDENTIFIER_AES256CBC + 9 + 2 + 16 + 4
  encryptedMaterialLength := privateKeyEncByteLength - headerLength
  ciphertext := copyOfRange(privateKeyEncByte, headerLength, (headerLength + encryptedMaterialLength))
  // decrypt the encrypted key material
  block, err := aes.NewCipher(key)
	if err != nil {
		panic(err)
	}
	if len(ciphertext) < aes.BlockSize {
		panic("ciphertext too short")
	}
	if len(ciphertext)%aes.BlockSize != 0 {
		panic("ciphertext is not a multiple of the block size")
	}
	mode := cipher.NewCBCDecrypter(block, ivByte)
	mode.CryptBlocks(ciphertext, ciphertext)
  return ciphertext
}

func IndexOf(outerArray []byte, innerArray []byte)(int) {
  return bytes.Index(outerArray, innerArray)
}

func copyOfRange(src []byte, from, to int) []byte {
    return append([]byte(nil), src[from:to]...)
}

func HexStringToByteArray(s string) ([]byte){
  data, err := hex.DecodeString(s)
  if err != nil {
    panic(err)
  }
  return data
}

func RsaSignToBase64Der(privateKeyByte []byte, dataToSign []byte)(string){
  parseResult, _ := x509.ParsePKCS8PrivateKey(privateKeyByte)
  privateKey := parseResult.(*rsa.PrivateKey)
  msgHash := sha256.New()
  _, err := msgHash.Write(dataToSign)
  if err != nil {
	  panic(err)
  }
  msgHashSum := msgHash.Sum(nil)
  signature, err := rsa.SignPKCS1v15(rand.Reader, privateKey, crypto.SHA256, msgHashSum)
  if err != nil {
	  panic(err)
  }
  return Base64Encoding(signature)
}

func RsaVerifySignatureFromBase64(publicKeyByte []byte, dataToSign []byte, signatureBase64 string)(string){
  block, _ :=pem.Decode(publicKeyByte)
  parseResultPublicKey,err := x509.ParsePKIXPublicKey(block.Bytes)
  publicKey := parseResultPublicKey.(*rsa.PublicKey)
  msgHash := sha256.New()
  _, err2 := msgHash.Write(dataToSign)
  if err2 != nil {
	  panic(err2)
  }
  msgHashSum := msgHash.Sum(nil)
  signature := []byte(Base64Decoding(signatureBase64))
  err = rsa.VerifyPKCS1v15(publicKey, crypto.SHA256, msgHashSum, signature)
  if err != nil {
	  fmt.Println("could not verify signature: ", err)
	  return "false"
  }
  return "true"
}

func LoadRsaPrivateKeyEncryptedPem()(string) {
  // this is a sample key - don't worry !
  // passphrase for decryption: 123456
  // passphrase scheme: PBES2
  // password based key derivation: PBKDF2 with HMAC and SHA-256
  // encryption algorithm: AES256-CBC
  return string(`-----BEGIN ENCRYPTED PRIVATE KEY-----
MIIFLTBXBgkqhkiG9w0BBQ0wSjApBgkqhkiG9w0BBQwwHAQIUo/aHb7/5jUCAggA
MAwGCCqGSIb3DQIJBQAwHQYJYIZIAWUDBAEqBBCbpiqeplyyDN85uu5yYqpFBIIE
0Iqvy2v42HeASj6B25llJjNOWvT5djjDiAg7BFsuOhMq10mTC7gWduXBai5llUBH
qF4+GgqCeedwT24YkdR4wAqtc2shcmq+2xC70N/XrSg9geGehmNwDx35byhPjouk
XwA5tgVsKtxTob/xjPYhjL5J1KpWOPQhdnHPGfGlWedTyTKboRfgkZ/Yds5EW6k7
mO9wt/iXcym6QgaJTswa1ntGPVaKMYvXVRtNnphsI9M7njXBYO52vfujUaeKn8Xe
qmuEAKGFhhu+r7/WWyQl+Zq2KIoD5ImOxuXScCZVH3+76AxXSwDJzB6ZG1SowSy+
YHbfkmvK2H6YdFshEfI/coMmu9E3wDKuCY7rgb70lHLcpvbSMa88vCFl3d/ZfmFl
w4TwG3vEVR9wfbXAuAw6XTSLBODcF4ifZnyXI2Dr+fQbQd/4o8w05hDiPkXRgnYY
e/4nFbTGGKvatZ7LOLa62isiGtQ4nprDpTapLMeJCFSgQN1jlZMVvV6mKUfrCJ69
3bESlyYGAN0MAx4KeMcCRwuEXWhOkLMFW0gvRp1udiMxJ8SnWfseYTr2FPUFQlFk
2ZhHsSO0Pg/cszUSuCX0q2ZW0ZZOiihJsmiQNXlbsRog9m8vaxUfA00v/q6iryGE
ApCiQd653ikukRhk/nQEY6OV4byLyI9zESF/pIdsrFnEtGSd0tggGsREtn11D8+7
SW0F0Es4P4UUPjiaYzyB58gcZJWxcoq63NOTUaicvTjNbzQu1+4y+2xhFe89e09w
PFSGerjSV/uVtVTH2hb79mAmlyLvaikQfQq2nvpCuZy7wZjhPA3D03WFTCbrYS7P
PfckAICCBCIB97vPsWVAviOVOrG5NDbIag+/3YhZb2gyuR2/E0XI1NdXO63NqU9R
B6VUUNdSvv4djA30nVjARlh3LOZmTy50OIj/QDOEbBrtFH79eHHxHRiK0+Q0A8Ch
EoDDz7bMKBcpiS3vXnI2UsrZX/QIuRjypc003uVm4MvdUwv0j3UGZr9zRGctqWf0
YBRkoIELRA45hQn0Ln3+wjAcjLRPpeuPgVAq7tDDLDmP0i/m8UbjNorhdv4CrT/u
P+keTW7v9Nuov1IXG4W6wKipYRzMjiYrTL5TaXPi9z63b6RQVShAEqSTtsPg5nDy
CZkt5NPBltq+3InrsTlbKSQGYn6wyEQeBYezD3CLkIliDn7UM4BDn+k5YonNynU4
emxbU4wyBEXoMD8+aBMf+bKKQXU97Ts9p7is+Ogqyop2mbMOebIxCKayizlb7/hp
Qs2ThBP84OrniUEeKVAkdykZyrdCoatgXVJngDwwEkSwOrEYZ0sJ48qPQMTN3kVq
r5XJjX3m8mpXdz0s9VbhT/rWSdQMX82msof4c6+RghEbiHFG4B0el5vrJaAROb69
3OnoU8mYKBlFjbO1rJJpGS5h/v+ysqWxDxfPPLD86UsgDAjr5C7W3K8eYB1UD/wP
n91TuTdrLKhLE93VsCyXnsLm44Td3MBv89Fr2CMF/Jz15Nfrg3yvR7bKvkNs3uVM
FVO3kqtmUMC/RsUr3NIE/RZ2sS+cjLMDE2zBUgHnDpNQB6WSuwqEI9prAWxEc1jS
SwyNkvstftK35nfeI3FNP0R94+kSKzGmiQh5yl3ufAVi
-----END ENCRYPTED PRIVATE KEY-----`)}

func LoadRsaPublicKeyPem()([]byte) {
  // this is a sample key - don't worry !
  return []byte(`-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9
iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT
/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8
01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB
ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g
QwIDAQAB
-----END PUBLIC KEY-----`)
}

func Base64Encoding(input []byte)(string) {
  return base64.StdEncoding.EncodeToString(input)
}

func Base64Decoding(input string)([]byte) {
  data, err := base64.StdEncoding.DecodeString(input)
	if err != nil {
		return data
	}
  return data
}
