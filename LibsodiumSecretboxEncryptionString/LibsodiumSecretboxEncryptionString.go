package main

import (
  "strings"
  "golang.org/x/crypto/nacl/secretbox"
	"crypto/rand"
  "encoding/base64"
	"fmt"
	"io"
)

func main() {

  fmt.Printf("Libsodium secret box random key string encryption\n")

  plaintext := string("The quick brown fox jumps over the lazy dog")
  fmt.Printf("plaintext: " + plaintext + "\n")

  // generate a random key
  encryptionKey := []byte(GenerateRandomKey())
  encryptionKeyBase64 := string(Base64Encoding(encryptionKey))
  fmt.Printf("encryptionKey (Base64): " + encryptionKeyBase64 + "\n")

  fmt.Printf("\n* * * Encryption * * *\n");
  ciphertextBase64 := string(SecretboxEncryptionToBase64(encryptionKey, plaintext))
  fmt.Printf("ciphertext: " + ciphertextBase64 + "\n");
  fmt.Printf("output is (Base64) nonce : (Base64) ciphertext\n");

  fmt.Printf("\n* * * Decryption * * *\n");
  decryptionKeyBase64 := string(encryptionKeyBase64);
  ciphertextDecryptionBase64 := string(ciphertextBase64);
  fmt.Printf("decryptionkey (Base64): " + decryptionKeyBase64 + "\n")
  decryptionKey := []byte(Base64Decoding(decryptionKeyBase64))
  fmt.Printf("ciphertext: " + ciphertextDecryptionBase64 + "\n");
  fmt.Printf("input is (Base64) nonce : (Base64) ciphertext\n");
  decryptedtext := string(SecretboxDecryptionFromBase64(decryptionKey, ciphertextDecryptionBase64))
  fmt.Printf("decryptedtext: " + decryptedtext + "\n")
}

func SecretboxEncryptionToBase64(key []byte, data string)(string) {
	plaintext := []byte(data)
  var secretKey [32]byte
	copy(secretKey[:], key)
  // generate random noncevar nonce [24]byte
  var nonce [24]byte
	if _, err := io.ReadFull(rand.Reader, nonce[:]); err != nil {
		panic(err)
	}
  encrypted := secretbox.Seal(nonce[:], plaintext, &nonce, &secretKey)
  // encrypted holds the nonce | ciphertext
  nonceEnc, ciphertext := encrypted[:24], encrypted[24:]
  return Base64Encoding(nonceEnc) + ":" + Base64Encoding(ciphertext)
}

func SecretboxDecryptionFromBase64(key []byte, ciphertextCompleteBase64 string)(string) {
  data := strings.Split(ciphertextCompleteBase64, ":") 
  nonce := []byte(Base64Decoding(data[0]))
  ciphertext := []byte(Base64Decoding(data[1]))
  var decryptNonce [24]byte
  copy(decryptNonce[:], nonce[:24])
  var secretKey [32]byte
	copy(secretKey[:], key)
  decrypted, ok := secretbox.Open(nil, ciphertext, &decryptNonce, &  secretKey)
	if !ok {
		panic("decryption error")
	}
  return string(decrypted)
}

func GenerateRandomKey()([]byte) {
  key := make([]byte, 32)
	if _, err := io.ReadFull(rand.Reader, key); err != nil {
	  panic(err.Error())
	}
  return key
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
