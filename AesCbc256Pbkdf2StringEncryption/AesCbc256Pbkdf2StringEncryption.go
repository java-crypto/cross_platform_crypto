package main

import (
	"crypto/aes"
	"crypto/cipher"
	"crypto/rand"
	"crypto/sha256"
  "encoding/base64"
	"fmt"
  "io"
	"strings"
  "bytes"
	"golang.org/x/crypto/pbkdf2"
)

func main() {
  fmt.Printf("AES CBC 256 String encryption with PBKDF2 derived key\n")

  plaintext := string("The quick brown fox jumps over the lazy dog")
  fmt.Printf("plaintext: " + plaintext + "\n")
  password := string("secret password")

  fmt.Printf("\n* * * Encryption * * *\n");
  ciphertextBase64 := string(AesCbcPbkdf2EncryptToBase64(password, plaintext))
  fmt.Printf("ciphertext: " + ciphertextBase64 + "\n");
  fmt.Printf("output is (Base64) salt : (Base64) nonce : (Base64) ciphertext\n");
	
  fmt.Printf("\n* * * Decryption * * *\n");
  ciphertextDecryptionBase64 := string(ciphertextBase64);
  fmt.Printf("ciphertext: " + ciphertextDecryptionBase64 + "\n");
  fmt.Printf("input is (Base64) salt : (Base64) nonce : (Base64) ciphertext\n");
  decryptedtext := string(AesCbcPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64))
  fmt.Printf("decryptedtext: " + decryptedtext)
}

func AesCbcPbkdf2EncryptToBase64(passphrase string, data string)(string) {
  PBKDF2_ITERATIONS := int(15000)
  salt := []byte(GenerateSalt32Byte())
  // derive key
  key := []byte(pbkdf2.Key([]byte(passphrase), salt, PBKDF2_ITERATIONS, 32, sha256.New))
  // encrypt
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
  saltbase64 := string(Base64Encoding(salt))
  ciphertextCompleteBase64 := string(saltbase64 + ":" + ivBase64 + ":" + ciphertextBase64) 
  return ciphertextCompleteBase64
}

func AesCbcPbkdf2DecryptFromBase64(passphrase string, ciphertextCompleteBase64 string)(string) {
  PBKDF2_ITERATIONS := int(15000)
  data := strings.Split(ciphertextCompleteBase64, ":") 
  salt := []byte(Base64Decoding(data[0]))
  iv := []byte(Base64Decoding(data[1]))
  ciphertext := []byte(Base64Decoding(data[2]))
  // derive key
  key := []byte(pbkdf2.Key([]byte(passphrase), salt, PBKDF2_ITERATIONS, 32, sha256.New))
  // decrypt
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

func GenerateSalt32Byte()([]byte) {
  salt := make([]byte, 32)
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
