package main

import (
  "strings"
	"crypto/aes"
	"crypto/cipher"
	"crypto/rand"
  "encoding/base64"
	"fmt"
	"io"
)

func main() {

  fmt.Printf("AES GCM 256 String encryption with random key\n")

  plaintext := string("The quick brown fox jumps over the lazy dog")
  fmt.Printf("plaintext: " + plaintext + "\n")

  // generate random key
  encryptionKey := []byte(GenerateRandomAesKey())
  encryptionKeyBase64 := string(Base64Encoding(encryptionKey))
  fmt.Printf("encryptionKey (Base64): " + encryptionKeyBase64 + "\n")

  fmt.Printf("\n* * * Encryption * * *\n");
  ciphertextBase64 := string(AesCbcEncryptToBase64(encryptionKey, plaintext))
  fmt.Printf("ciphertext: " + ciphertextBase64 + "\n");
  fmt.Printf("output is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag\n");

  fmt.Printf("\n* * * Decryption * * *\n");
  decryptionKeyBase64 := string(encryptionKeyBase64);
  ciphertextDecryptionBase64 := string(ciphertextBase64);
  fmt.Printf("decryptionkey (Base64): " + decryptionKeyBase64 + "\n")
  decryptionKey := []byte(Base64Decoding(decryptionKeyBase64))
  fmt.Printf("ciphertext: " + ciphertextDecryptionBase64 + "\n");
  fmt.Printf("input is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag\n");
  decryptedtext := string(aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64))
  fmt.Printf("decryptedtext: " + decryptedtext)
}

func AesCbcEncryptToBase64(key []byte, data string)(string) {
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

func aesCbcDecryptFromBase64(encryptionKey []byte, ciphertextCompleteBase64 string)(string) {
  data := strings.Split(ciphertextCompleteBase64, ":") 
  nonce := []byte(Base64Decoding(data[0]))
  ciphertext := []byte(Base64Decoding(data[1]))
  gcmTag := []byte(Base64Decoding(data[2]))
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
