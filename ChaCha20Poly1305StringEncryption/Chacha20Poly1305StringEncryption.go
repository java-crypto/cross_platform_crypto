package main

import (
  "strings"
  "golang.org/x/crypto/chacha20poly1305"
	"crypto/rand"
  "encoding/base64"
	"fmt"
	"io"
)

func main() {

  fmt.Printf("ChaCha20-Poly1305 String encryption with random key full\n")

  plaintext := string("The quick brown fox jumps over the lazy dog")
  fmt.Printf("plaintext: " + plaintext + "\n")

  // generate random key
  encryptionKey := []byte(GenerateRandomAesKey())
  encryptionKeyBase64 := string(Base64Encoding(encryptionKey))
  fmt.Printf("encryptionKey (Base64): " + encryptionKeyBase64 + "\n")

  fmt.Printf("\n* * * Encryption * * *\n");
  ciphertextBase64 := string(Chacha20Poly1305EncryptToBase64(encryptionKey, plaintext))
  fmt.Printf("ciphertext: " + ciphertextBase64 + "\n");
  fmt.Printf("output is (Base64) nonce : (Base64) ciphertext : (Base64) poly1305Tag\n");

  fmt.Printf("\n* * * Decryption * * *\n");
  decryptionKeyBase64 := string(encryptionKeyBase64);
  ciphertextDecryptionBase64 := string(ciphertextBase64);
  fmt.Printf("decryptionkey (Base64): " + decryptionKeyBase64 + "\n")
  decryptionKey := []byte(Base64Decoding(decryptionKeyBase64))
  fmt.Printf("ciphertext: " + ciphertextDecryptionBase64 + "\n");
  fmt.Printf("input is (Base64) nonce : (Base64) ciphertext : (Base64) poly1305Tag\n");
  decryptedtext := string(Chacha20Poly1305DecryptFromBase64(decryptionKey, ciphertextDecryptionBase64))
  fmt.Printf("decryptedtext: " + decryptedtext)
}

func Chacha20Poly1305EncryptToBase64(key []byte, data string)(string) {
	plaintext := []byte(data)
  nonce := []byte(GenerateRandomNonce())
	block, err := chacha20poly1305.New(key)
	if err != nil {
		panic(err.Error())
	}
  ciphertext := block.Seal(nil, nonce, plaintext, nil)
  ciphertextWithTagLength := int(len(ciphertext))
  ciphertextWithoutTag, poly1305Tag := ciphertext[:(ciphertextWithTagLength-16)], ciphertext[(ciphertextWithTagLength-16):]
  ciphertextWithoutTagBase64 := string(Base64Encoding(ciphertextWithoutTag))
  nonceBase64 := string(Base64Encoding(nonce))
  poly1305TagBase64 := string(Base64Encoding(poly1305Tag))
  ciphertextCompleteBase64 := string(nonceBase64 + ":" + ciphertextWithoutTagBase64 + ":" + poly1305TagBase64) 
  return ciphertextCompleteBase64
}

func Chacha20Poly1305DecryptFromBase64(key []byte, ciphertextCompleteBase64 string)(string) {
  data := strings.Split(ciphertextCompleteBase64, ":") 
  nonce := []byte(Base64Decoding(data[0]))
  ciphertext := []byte(Base64Decoding(data[1]))
  poly1305Tag := []byte(Base64Decoding(data[2]))
  ciphertextCompleteLength := int(len(ciphertext) + len(poly1305Tag))
  ciphertextComplete := make([]byte, ciphertextCompleteLength)
  ciphertextComplete = append(ciphertext[:], poly1305Tag[:]...)
  block, err := chacha20poly1305.New(key)
	if err != nil {
	  panic(err.Error())
	}
  plaintext, err := block.Open(nil, nonce, ciphertextComplete, nil)
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
