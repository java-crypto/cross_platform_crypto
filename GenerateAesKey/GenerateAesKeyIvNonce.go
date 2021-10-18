package main

import (
  "strconv"
	"crypto/rand"
  "encoding/base64"
	"fmt"
  "io"
)

func main() {
  fmt.Printf("Generate a 32 byte long AES key\n")
  aesKey := GenerateRandomAesKey()
  aesKeyBase64 := Base64Encoding(aesKey)
  aesKeyLength := len(aesKey)
  fmt.Printf("generated key length: " + strconv.Itoa(aesKeyLength) + " data: " + aesKeyBase64 + "\n\n")

  fmt.Printf("Generate a 16 byte long Initialization vector (IV)\n")
  iv := GenerateRandomInitvector()
  ivBase64 := Base64Encoding(iv)
  ivLength := len(iv)
  fmt.Printf("generated iv length: " + strconv.Itoa(ivLength) + " data: " + ivBase64 + "\n\n")

  fmt.Printf("Generate a 12 byte long nonce for AES GCM\n")
  nonce := GenerateRandomNonce()
  nonceBase64 := Base64Encoding(nonce)
  nonceLength := len(nonce)
  fmt.Printf("generated nonce length: " + strconv.Itoa(nonceLength) + " data: " + nonceBase64 + "\n")
}

func GenerateRandomNonce()([]byte) {
  salt := make([]byte, 12)
	if _, err := io.ReadFull(rand.Reader, salt); err != nil {
	  panic(err.Error())
	}
  return salt
}

func GenerateRandomInitvector()([]byte) {
  iv := make([]byte, 16)
	if _, err := io.ReadFull(rand.Reader, iv); err != nil {
	  panic(err.Error())
	}
  return iv
}

func GenerateRandomAesKey()([]byte) {
  iv := make([]byte, 32)
	if _, err := io.ReadFull(rand.Reader, iv); err != nil {
	  panic(err.Error())
	}
  return iv
}

func Base64Encoding(input []byte)(string) {
  return base64.StdEncoding.EncodeToString(input)
}

