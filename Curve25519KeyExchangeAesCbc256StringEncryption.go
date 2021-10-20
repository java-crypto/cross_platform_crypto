package main

import (
	"bytes"
  "strings"
	"crypto/aes"
	"crypto/cipher"
  "golang.org/x/crypto/curve25519"
	"crypto/rand"
  "encoding/base64"
	"fmt"
	"io"
)

func main() {

  fmt.Printf("Curve25519 key exchange and AES CBC 256 string encryption\n")

  plaintext := string("The quick brown fox jumps over the lazy dog")
  fmt.Printf("plaintext: " + plaintext + "\n")

  // for encryption you need your private key (aPrivateKey) and the public key from other party (bPublicKey)
  aPrivateKeyBase64 := "yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU="
  bPublicKeyBase64 :=  "jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg="

  // convert the base64 encoded keys
  aPrivateKey := Base64Decoding(aPrivateKeyBase64)
  bPublicKey := Base64Decoding(bPublicKeyBase64)

  aSharedKey := Curve25519GenerateKeyAgreement(aPrivateKey, bPublicKey)

  fmt.Printf("\n* * * Encryption * * *\n")
  fmt.Printf("all data are in Base64 encoding")
  fmt.Printf("\naPrivateKey: " + aPrivateKeyBase64)
  fmt.Printf("\nbPublicKey:  " + bPublicKeyBase64)
  fmt.Printf("\naSharedKey:  " + Base64Encoding(aSharedKey))

  ciphertextBase64 := string(AesCbcEncryptToBase64(aSharedKey, plaintext))
  fmt.Printf("\nciphertext: " + ciphertextBase64 + "\n")
  fmt.Printf("output is (Base64) iv : (Base64) ciphertext\n")

  // for decryption you need your private key (bPrivateKey) and the public key from other party (aPublicKey)
  // received ciphertext
  ciphertextReceivedBase64 := ciphertextBase64
  // received public key
  aPublicKeyBase64 := "b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0="
  // own private key
  bPrivateKeyBase64 := "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM="
  // convert the base64 encoded keys
  aPublicKey := Base64Decoding(aPublicKeyBase64)
  bPrivateKey := Base64Decoding(bPrivateKeyBase64)
  // generate shared key
  bSharedKey := Curve25519GenerateKeyAgreement(bPrivateKey, aPublicKey)

  fmt.Printf("\n* * * Decryption * * *\n")

  decryptionKey := bSharedKey
  fmt.Printf("ciphertext: " + ciphertextReceivedBase64 + "\n")
  fmt.Printf("input is (Base64) iv : (Base64) ciphertext\n")
  decryptedtext := string(aesCbcDecryptFromBase64(decryptionKey, ciphertextReceivedBase64))
  fmt.Printf("bPrivateKey: " + bPrivateKeyBase64)
  fmt.Printf("\naPublicKey:  " + aPublicKeyBase64)
  fmt.Printf("\nbSharedKey:  " + Base64Encoding(bSharedKey))
  fmt.Printf("\ndecrypt.text:" + decryptedtext)
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

func Curve25519GenerateKeyAgreement(privateKey, publicKey []byte)([]byte) {
  data, err := curve25519.X25519(privateKey, publicKey)
	if err != nil {
		return data
	}
  return data
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

