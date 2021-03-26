package main

import (
  "golang.org/x/crypto/nacl/sign"
  "encoding/base64"
	"fmt"
)

func main() {

  fmt.Printf("Libsodium detached signature string\n")

  dataToSign := string("The quick brown fox jumps over the lazy dog")
  fmt.Printf("dataToSign: " + dataToSign + "\n")

  // usually we would load the private and public key from a file or keystore
  // here we use hardcoded keys for demonstration - don't do this in real programs

  fmt.Printf("\n* * * sign the plaintext with the ED25519 private key * * *\n");
  fmt.Printf("all data are in Base64 encoding\n")
  privateKey := []byte(loadEd25519PrivateKey())
  signatureBase64 := string(LibsodiumSignDetachedToBase64(privateKey, dataToSign))
  fmt.Printf("signature (Base64): " + signatureBase64 + "\n")

  fmt.Printf("\n* * * verify the signature against the plaintext with the ED25519 public key * * *\n");
  publicKey := []byte(loadEd25519PublicKey())
  verified := bool(LibsodiumVerifyDetachedFromBase64(publicKey, dataToSign, signatureBase64))

  fmt.Printf("\nsignature (Base64) verified: ")
  if (verified == true) {
    fmt.Printf("true\n")  
  } else {
  fmt.Printf("false\n")  
  }

}

func LibsodiumSignDetachedToBase64(privateKey []byte, data string)(string) {
  plaintext := []byte(data)
  var privateKey64 [64]byte
	copy(privateKey64[:], privateKey)
  signatureComplete := sign.Sign(nil, plaintext, &privateKey64)
  // signatureComplete is signature|plaintext
  return Base64Encoding(signatureComplete[:64])
}

func LibsodiumVerifyDetachedFromBase64(publicKey []byte, data string, signatureBase64 string)(bool) {
  dataToSign := []byte(data)
  fmt.Printf(string(dataToSign))
  signature := Base64Decoding(signatureBase64)
  signatureComplete := make([]byte, (64 + len(dataToSign)))
  signatureComplete = append(signature[:], dataToSign[:]...)
  var publicKey32 [32]byte
	copy(publicKey32[:], publicKey)
  _ , ok := sign.Open(nil, signatureComplete, &publicKey32)  
  if !ok {  
      fmt.Printf("failed to verify signed message")  
      return false 
  }  
  return true
}

func loadEd25519PrivateKey()([]byte) {
  return Base64Decoding(
`Gjho7dILimbXJY8RvmQZEBczDzCYbZonGt/e5QQ3Vro2sjTp95EMxOrgEaWiprPduR/KJFBRA+RlDDpkvmhVfw==`)
}

func loadEd25519PublicKey()([]byte) {
  return Base64Decoding(`NrI06feRDMTq4BGloqaz3bkfyiRQUQPkZQw6ZL5oVX8=`)
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
