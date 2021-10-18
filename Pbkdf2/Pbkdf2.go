// https://replit.com/@javacrypto/CpcGoPbkdf2#main.go
package main

import (
	"crypto/rand"
  "crypto/sha1"
	"crypto/sha256"
  "crypto/sha512"
	"fmt"
  "io"
	"golang.org/x/crypto/pbkdf2"
  "encoding/hex"
  "strconv"
)

func main() {
  fmt.Printf("Generate a 32 byte long AES key with PBKDF2\n")

  password := []byte("secret password")
  PBKDF2_ITERATIONS := 15000 // number of iterations, higher is better but slower
  // ### security warning - never use a fixed salt in production, this is for compare reasons only
  salt := GenerateFixedSalt32Byte()
  // please use below generateSalt32Byte()
  // salt := GenerateSalt32Byte();

  aesKeySha512 := GenerateAes256KeyPbkdf2Sha512(password, PBKDF2_ITERATIONS, salt)
  aesKeySha512Length := len(aesKeySha512)
  fmt.Println("\naesKeySha512 length: " + strconv.Itoa(aesKeySha512Length) + " data: " + BytesToHex(aesKeySha512))

  aesKeySha256 := GenerateAes256KeyPbkdf2Sha256(password, PBKDF2_ITERATIONS, salt)
  aesKeySha256Length := len(aesKeySha256)
  fmt.Println("\naesKeySha256 length: " + strconv.Itoa(aesKeySha256Length) + " data: " + BytesToHex(aesKeySha256))

  aesKeySha1 := GenerateAes256KeyPbkdf2Sha1(password, PBKDF2_ITERATIONS, salt)
  aesKeySha1Length := len(aesKeySha1)
  fmt.Println("\naesKeySha1 length: " + strconv.Itoa(aesKeySha1Length) + " data: " + BytesToHex(aesKeySha1))  
}

func GenerateAes256KeyPbkdf2Sha1(password []byte, pbkdf2Iterations int, salt []byte)([]byte) {
  key := []byte(pbkdf2.Key([]byte(password), salt, pbkdf2Iterations, 32, sha1.New))
  return key
}

func GenerateAes256KeyPbkdf2Sha256(password []byte, pbkdf2Iterations int, salt []byte)([]byte) {
  key := []byte(pbkdf2.Key([]byte(password), salt, pbkdf2Iterations, 32, sha256.New))
  return key
}

func GenerateAes256KeyPbkdf2Sha512(password []byte, pbkdf2Iterations int, salt []byte)([]byte) {
  key := []byte(pbkdf2.Key([]byte(password), salt, pbkdf2Iterations, 32, sha512.New))
  return key
}

func BytesToHex(input []byte)(string){
  return hex.EncodeToString(input)
}

func GenerateSalt32Byte()([]byte) {
  salt := make([]byte, 32)
	if _, err := io.ReadFull(rand.Reader, salt); err != nil {
	  panic(err.Error())
	}
  return salt
}

func GenerateFixedSalt32Byte()([]byte) {
  salt := make([]byte, 32)
  return salt
}

func GenerateRandomNonce()([]byte) {
  nonce := make([]byte, 12)
	if _, err := io.ReadFull(rand.Reader, nonce); err != nil {
	  panic(err.Error())
	}
  return nonce
}
