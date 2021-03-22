package main

import (
  "strings"
	"crypto"
	"crypto/rand"
	"crypto/rsa"
  "crypto/sha1"
	"crypto/aes"
	"crypto/cipher"  
	"encoding/base64"
	"fmt"
  "crypto/x509"
  "encoding/pem"
  "io"
)

func main() {

  fmt.Println("RSA AES GCM hybrid encryption")
  fmt.Println("\nuses AES GCM 256 random key for the plaintext encryption")
  fmt.Println("and RSA OAEP SHA 1 2048 bit for the encryption of the random key\n")

  dataToEncryptString := string("The quick brown fox jumps over the lazy dog")
  fmt.Println("plaintext: " + dataToEncryptString)
  
  // usually we would load the private and public key from a file or keystore
  // here we use hardcoded keys for demonstration - don't do this in real programs

  // encryption
  fmt.Println("\n* * * Encryption * * *")
  // load public key
  publicKeyByte := []byte(LoadRsaPublicKeyPem());
  fmt.Println("public key loaded")
  completeCiphertextBase64 := RsaOaepSha1AesGcm256HybridStringEncryption(publicKeyByte, dataToEncryptString)
  fmt.Println("ciphertextBase64: " + completeCiphertextBase64)
  
  fmt.Println("\n* * * Decryption * * *")
  // load private key
  privateKeyByte := []byte(LoadRsaPrivateKeyPem());
  fmt.Println("private key loaded")
  completeCiphertextReceivedBase64 := string(completeCiphertextBase64)
  decryptedtext := string(RsaOaepSha1AesCbc256HybridStringDecryption(privateKeyByte, completeCiphertextReceivedBase64))
  fmt.Println("decryptedtext: " + decryptedtext)
}

func RsaOaepSha1AesGcm256HybridStringEncryption(publicKeyByte []byte, dataToEncrypt string) (string){
  // generate random key
  encryptionKey := []byte(GenerateRandomAesKey())
  // encrypt plaintext
  ciphertextBase64 := string(AesGcmEncryptToBase64(encryptionKey, dataToEncrypt))
  block, _ :=pem.Decode(publicKeyByte)
  parseResultPublicKey,err := x509.ParsePKIXPublicKey(block.Bytes)
  publicKey := parseResultPublicKey.(*rsa.PublicKey)
  encryptedBytes, err := rsa.EncryptOAEP(
	  sha1.New(),
	  rand.Reader,
	  publicKey,
	  []byte(encryptionKey),
	  nil)
  if err != nil {
	  panic(err)
  }
  return Base64Encoding(encryptedBytes) + ":" + ciphertextBase64
}

func RsaOaepSha1AesCbc256HybridStringDecryption(privateKeyByte []byte, ciphertextCompleteBase64 string)(string){
  data := strings.Split(ciphertextCompleteBase64, ":") 
  encryptedKey :=  []byte(Base64Decoding(data[0]))
  nonce := []byte(Base64Decoding(data[1]))
  ciphertext := []byte(Base64Decoding(data[2]))
  gcmTag :=  []byte(Base64Decoding(data[3]))
  // decrypt the encrypted key
  block, _ := pem.Decode(privateKeyByte)
  parseResult, _ := x509.ParsePKCS8PrivateKey(block.Bytes)
  privateKey := parseResult.(*rsa.PrivateKey)
  decryptedKey, err := privateKey.Decrypt(nil, encryptedKey, &rsa.OAEPOptions{Hash: crypto.SHA1})
  if err != nil {
	  panic(err)
  }
  return string(AesGcmDecrypt(decryptedKey, nonce, ciphertext, gcmTag))
}

func AesGcmEncryptToBase64(key []byte, data string)(string) {
	plaintext := []byte(data)
  nonce := []byte(GenerateRandomNonce())
	block, err := aes.NewCipher(key)
	if err != nil {
		panic(err.Error())
	}
	aesGCM, err := cipher.NewGCM(block)
	if err != nil {
		panic(err.Error())
	}
  ciphertext := aesGCM.Seal(nil, nonce, plaintext, nil)
  // ciphertext hold the ciphertext | gcmTag
  ciphertextWithTagLength := int(len(ciphertext))
  ciphertextWithoutTag, gcmTag := ciphertext[:(ciphertextWithTagLength-16)], ciphertext[(ciphertextWithTagLength-16):]
  ciphertextWithoutTagBase64 := string(Base64Encoding(ciphertextWithoutTag))
  nonceBase64 := string(Base64Encoding(nonce))
  gcmTagBase64 := string(Base64Encoding(gcmTag))
  ciphertextCompleteBase64 := string(nonceBase64 + ":" + ciphertextWithoutTagBase64 + ":" + gcmTagBase64) 
  return ciphertextCompleteBase64
}

func AesGcmDecrypt(encryptionKey []byte, nonce []byte, ciphertext []byte, gcmTag []byte)(string) {
  ciphertextCompleteLength := int(len(ciphertext) + len(gcmTag))
  ciphertextComplete := make([]byte, ciphertextCompleteLength)
  ciphertextComplete = append(ciphertext[:], gcmTag[:]...)
	block, err := aes.NewCipher(encryptionKey)
	if err != nil {
		panic(err.Error())
	}
	aesGCM, err := cipher.NewGCM(block)
	if err != nil {
		panic(err.Error())
	}
  plaintext, err := aesGCM.Open(nil, nonce, ciphertextComplete, nil)
	if err != nil {
		panic(err.Error())
	}
  return string(plaintext)
}

func GenerateRandomAesKey()([]byte) {
  key := make([]byte, 32)
	if _, err := io.ReadFull(rand.Reader, key); err != nil {
	  panic(err.Error())
	}
  return key
}

func GenerateRandomNonce()([]byte) {
  nonce := make([]byte, 12)
	if _, err := io.ReadFull(rand.Reader, nonce); err != nil {
	  panic(err.Error())
	}
  return nonce
}

func LoadRsaPrivateKeyPem()([]byte) {
  return []byte(`-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDwSZYlRn86zPi9
e1RTZL7QzgE/36zjbeCMyOhf6o/WIKeVxFwVbG2FAY3YJZIxnBH+9j1XS6f+ewjG
FlJY4f2IrOpS1kPiO3fmOo5N4nc8JKvjwmKtUM0t63uFFPfs69+7mKJ4w3tk2mSN
4gb8J9P9BCXtH6Q78SdOYvdCMspA1X8eERsdLb/jjHs8+gepKqQ6+XwZbSq0vf2B
MtaAB7zTX/Dk+ZxDfwIobShPaB0mYmojE2YAQeRq1gYdwwO1dEGk6E5J2toWPpKY
/IcSYsGKyFqrsmbw0880r1BwRDer4RFrkzp4zvY+kX3eDanlyMqDLPN+ghXT1lv8
snZpbaBDAgMBAAECggEBAIVxmHzjBc11/73bPB2EGaSEg5UhdzZm0wncmZCLB453
XBqEjk8nhDsVfdzIIMSEVEowHijYz1c4pMq9osXR26eHwCp47AI73H5zjowadPVl
uEAot/xgn1IdMN/boURmSj44qiI/DcwYrTdOi2qGA+jD4PwrUl4nsxiJRZ/x7PjL
hMzRbvDxQ4/Q4ThYXwoEGiIBBK/iB3Z5eR7lFa8E5yAaxM2QP9PENBr/OqkGXLWV
qA/YTxs3gAvkUjMhlScOi7PMwRX9HsrAeLKbLuC1KJv1p2THUtZbOHqrAF/uwHaj
ygUblFaa/BTckTN7PKSVIhp7OihbD04bSRrh+nOilcECgYEA/8atV5DmNxFrxF1P
ODDjdJPNb9pzNrDF03TiFBZWS4Q+2JazyLGjZzhg5Vv9RJ7VcIjPAbMy2Cy5BUff
EFE+8ryKVWfdpPxpPYOwHCJSw4Bqqdj0Pmp/xw928ebrnUoCzdkUqYYpRWx0T7YV
RoA9RiBfQiVHhuJBSDPYJPoP34kCgYEA8H9wLE5L8raUn4NYYRuUVMa+1k4Q1N3X
Bixm5cccc/Ja4LVvrnWqmFOmfFgpVd8BcTGaPSsqfA4j/oEQp7tmjZqggVFqiM2m
J2YEv18cY/5kiDUVYR7VWSkpqVOkgiX3lK3UkIngnVMGGFnoIBlfBFF9uo02rZpC
5o5zebaDImsCgYAE9d5wv0+nq7/STBj4NwKCRUeLrsnjOqRriG3GA/TifAsX+jw8
XS2VF+PRLuqHhSkQiKazGr2Wsa9Y6d7qmxjEbmGkbGJBC+AioEYvFX9TaU8oQhvi
hgA6ZRNid58EKuZJBbe/3ek4/nR3A0oAVwZZMNGIH972P7cSZmb/uJXMOQKBgQCs
FaQAL+4sN/TUxrkAkylqF+QJmEZ26l2nrzHZjMWROYNJcsn8/XkaEhD4vGSnazCu
/B0vU6nMppmezF9Mhc112YSrw8QFK5GOc3NGNBoueqMYy1MG8Xcbm1aSMKVv8xba
rh+BZQbxy6x61CpCfaT9hAoA6HaNdeoU6y05lBz1DQKBgAbYiIk56QZHeoZKiZxy
4eicQS0sVKKRb24ZUd+04cNSTfeIuuXZrYJ48Jbr0fzjIM3EfHvLgh9rAZ+aHe/L
84Ig17KiExe+qyYHjut/SC0wODDtzM/jtrpqyYa5JoEpPIaUSgPuTH/WhO3cDsx6
3PIW4/CddNs8mCSBOqTnoaxh
-----END PRIVATE KEY-----`)
}

func LoadRsaPublicKeyPem()([]byte) {
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
