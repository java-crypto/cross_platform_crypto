package main

import (
	"bytes"
  "strings"
	"crypto/aes"
	"crypto/cipher"
	"crypto/rand"
  "encoding/base64"
	"fmt"
	"io"
)

func main() {

  fmt.Printf("AES CBC 256 String encryption with random key full\n")

  plaintext := string("The quick brown fox jumps over the lazy dog")
  fmt.Printf("plaintext: " + plaintext + "\n")

  // generate random key
  encryptionKey := []byte(GenerateRandomAesKey())
  encryptionKeyBase64 := string(Base64Encoding(encryptionKey))
  fmt.Printf("encryptionKey (Base64): " + encryptionKeyBase64 + "\n")

  fmt.Printf("\n* * * Encryption * * *\n");
  ciphertextBase64 := string(AesCbcEncryptToBase64(encryptionKey, plaintext))
  fmt.Printf("ciphertext: " + ciphertextBase64 + "\n");
  fmt.Printf("output is (Base64) iv : (Base64) ciphertext\n");

  fmt.Printf("\n* * * Decryption * * *\n");
  decryptionKeyBase64 := string(encryptionKeyBase64);
  ciphertextDecryptionBase64 := string(ciphertextBase64);
  fmt.Printf("decryptionkey (Base64): " + decryptionKeyBase64 + "\n")
  decryptionKey := []byte(Base64Decoding(decryptionKeyBase64))
  fmt.Printf("ciphertext: " + ciphertextDecryptionBase64 + "\n");
  fmt.Printf("input is (Base64) iv : (Base64) ciphertext\n");
  decryptedtext := string(aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64))
  fmt.Printf("decryptedtext: " + decryptedtext + "\n")
}

func AesCbcEncryptToBase64(key []byte, data string)(string) {
	plaintext := []byte(data)
  plaintext = PKCS5Padding(plaintext, 16)
  iv := []byte(GenerateRandomInitvector())
	block, err := aes.NewCipher(key)
	if err != nil {
		panic(err)
	}
  ciphertext := make([]byte, len(plaintext))
  mode := cipher.NewCBCEncrypter(block, iv)
	mode.CryptBlocks(ciphertext, plaintext)
  ciphertextBase64 := string(Base64Encoding(ciphertext))
  ivBase64 := string(Base64Encoding(iv))
  ciphertextCompleteBase64 := string(ivBase64 + ":" + ciphertextBase64)
  return ciphertextCompleteBase64
}

func aesCbcDecryptFromBase64(encryptionKey []byte, ciphertextCompleteBase64 string)(string) {
  data := strings.Split(ciphertextCompleteBase64, ":") 
  iv := []byte(Base64Decoding(data[0]))
  ciphertext := []byte(Base64Decoding(data[1]))
  block, err := aes.NewCipher(encryptionKey)
	if err != nil {
		panic(err)
	}
	if len(ciphertext) < aes.BlockSize {
		panic("ciphertext too short")
	}
	if len(ciphertext)%aes.BlockSize != 0 {
		panic("ciphertext is not a multiple of the block size")
	}
	mode := cipher.NewCBCDecrypter(block, iv)
	mode.CryptBlocks(ciphertext, ciphertext)
  return string(ciphertext)
}

func PKCS5Padding(ciphertext []byte, blockSize int) []byte {
  padding := blockSize - len(ciphertext)%blockSize
  padtext := bytes.Repeat([]byte{byte(padding)}, padding)
  return append(ciphertext, padtext...)
}

func PKCS5Trimming(encrypt []byte) []byte {
  padding := encrypt[len(encrypt)-1]
  return encrypt[:len(encrypt)-int(padding)]
}

func GenerateRandomAesKey()([]byte) {
  key := make([]byte, 32)
	if _, err := io.ReadFull(rand.Reader, key); err != nil {
	  panic(err.Error())
	}
  return key
}

func GenerateRandomInitvector()([]byte) {
  iv := make([]byte, 16)
	if _, err := io.ReadFull(rand.Reader, iv); err != nil {
	  panic(err.Error())
	}
  return iv
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
