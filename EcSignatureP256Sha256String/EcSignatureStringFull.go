package main

import (
	"crypto/rand"
  "crypto/ecdsa"
	"crypto/sha256"
	"encoding/base64"
	"fmt"
  "crypto/x509"
  "math/big"
  "encoding/pem"
)

func main() {

  fmt.Println("EC signature string (ECDSA with SHA256)")

  dataToSignString := string("The quick brown fox jumps over the lazy dog")
  dataToSign := []byte(dataToSignString)
  fmt.Println("dataToSign: " + dataToSignString)

  // usually we would load the private and public key from a file or keystore
  // here we use hardcoded keys for demonstration - don't do this in real programs

  fmt.Println("\n* * * sign the plaintext with the EC private key * * *")
  // load private key
  privateKeyByte := []byte(LoadEcPrivateKeyPem());
  fmt.Println("private key loaded")
  signatureBase64 := EcSignToBase64(privateKeyByte, dataToSign);
  fmt.Println("\nsignature (Base64): " + signatureBase64)

  fmt.Println("\n* * * verify the signature against the plaintext with the EC public key * * *")
  // load public key
  publicKeyByte := []byte(LoadEcPublicKeyPem());
  fmt.Println("public key loaded")
  signatureVerified := bool(EcVerifySignatureFromBase64(publicKeyByte, dataToSign, signatureBase64))
  //signatureVerified := bool(EcVerifySignatureFromBase64(publicKeyByte, dataToSign, signatureBase64))
	fmt.Println("\nsignature (Base64) verified: ", signatureVerified)
  //fmt.Println("\nsignature (Base64) verified: " + signatureVerified)

}

func EcSignToBase64(privateKeyByte []byte, dataToSign []byte)(string){
  block, _ := pem.Decode(privateKeyByte)
  parseResult, _ := x509.ParsePKCS8PrivateKey(block.Bytes)
  privateKey := parseResult.(*ecdsa.PrivateKey)
  msgHash := sha256.New()
  _, err := msgHash.Write(dataToSign)
  if err != nil {
	  panic(err)
  }
  msgHashSum := msgHash.Sum(nil)
  r, s, err := ecdsa.Sign(rand.Reader, privateKey, msgHashSum)
  if err != nil {
	  panic(err)
  }
  // combine r|sByte
  signature := make([]byte, 64)
  signature = append(r.Bytes()[:], s.Bytes()[:]...)
  return Base64Encoding(signature)
}

func EcVerifySignatureFromBase64(publicKeyByte []byte, dataToSign []byte, signatureBase64 string)(bool){
  block, _ :=pem.Decode(publicKeyByte)
  parseResultPublicKey,err := x509.ParsePKIXPublicKey(block.Bytes)
  publicKey := parseResultPublicKey.(*ecdsa.PublicKey)
  msgHash := sha256.New()
  _, err2 := msgHash.Write(dataToSign)
  if err2 != nil {
	  panic(err2)
  }
  msgHashSum := msgHash.Sum(nil)
  signature := []byte(Base64Decoding(signatureBase64))
  // split signature in r and s
  rByte, sByte := signature[:32], signature[32:]
  r := new(big.Int)
  r.SetBytes(rByte)
  s := new(big.Int)
  s.SetBytes(sByte)
  valid := ecdsa.Verify(publicKey, msgHashSum, r, s)
  if err != nil {
	  fmt.Println("could not import public key: ", err)
	  return false
  }
  return valid
}  

func LoadEcPrivateKeyPem()([]byte) {
  return []byte(`-----BEGIN PRIVATE KEY-----
MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCAU2f8tzo99Z1HoxJlY
96yXUhFY5vppVjw1iPKRfk1wHA==
-----END PRIVATE KEY-----`)
}

func LoadEcPublicKeyPem()([]byte) {
  return []byte(`-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEzb7yAFWup6iDqJiEq764rAumsV2M
rspZxaP3WGpwHaC4Uff3N4UbJZF7Zac1c6W7KJl0eeCP0205Q3UEpwxndQ==
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
