package main

import (
  "golang.org/x/crypto/nacl/box"
	"crypto/rand"
  "encoding/base64"
	"fmt"
	"io"
)

func main() {

  fmt.Printf("Libsodium sealed crypto box hybrid string encryption\n")

  plaintext := string("The quick brown fox jumps over the lazy dog")
  fmt.Printf("plaintext: " + plaintext + "\n")

  fmt.Printf("\n* * * encryption * * *\n");
  fmt.Printf("all data are in Base64 encoding\n");
  // for encryption you need the public key from other party (publicKeyB)
  publicKeyBBase64 := "jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg="
  // convert the base64 encoded keys
  publicKeyB := []byte(Base64Decoding(publicKeyBBase64))
  ciphertextBase64 := string(SealedCryptoboxEncryptionToBase64(publicKeyB, plaintext))
  fmt.Printf("publicKeyB:  " + publicKeyBBase64 + "\n")
  fmt.Printf("ciphertext: " + ciphertextBase64 + "\n");
  fmt.Printf("output is (Base64) ciphertext\n");

  fmt.Printf("\n* * * Decryption * * *\n");
  // for decryption you need your private key (privateKeyB)
  privateKeyBBase64 := "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM="
  // convert the base64 encoded keys
  privateKeyB := []byte(Base64Decoding(privateKeyBBase64))
  ciphertextReceivedBase64 := string(ciphertextBase64);
  fmt.Printf("ciphertext: " + ciphertextReceivedBase64 + "\n");
  fmt.Printf("input is (Base64) ciphertext\n");
  decryptedtext := string(SealedCryptoboxDecryptionFromBase64(privateKeyB, publicKeyB, ciphertextReceivedBase64))
  fmt.Printf("decryptedtext: " + decryptedtext + "\n")
}

func SealedCryptoboxEncryptionToBase64(publicKey []byte, data string)(string) {
  plaintext := []byte(data)
  // generate random noncevar nonce [24]byte
  var nonce []byte
	if _, err := io.ReadFull(rand.Reader, nonce[:]); err != nil {
		panic(err)
	}
  var publicKey32 [32]byte
	copy(publicKey32[:], publicKey)
  encrypted, error := box.SealAnonymous(nil, plaintext, &publicKey32, rand.Reader)
  if (error != nil) {
		panic(error)
	}
  return Base64Encoding(encrypted)
}

func SealedCryptoboxDecryptionFromBase64(privateKey []byte, publicKey []byte, ciphertextDecryptionBase64 string)(string) {
  ciphertext := []byte(Base64Decoding(ciphertextDecryptionBase64))
  var privateKey32 [32]byte
	copy(privateKey32[:], privateKey)
  var publicKey32 [32]byte
	copy(publicKey32[:], publicKey)
  decrypted, ok := box.OpenAnonymous(nil, ciphertext, &publicKey32, &privateKey32)
	if !ok {
		panic("decryption error")
	}
  return string(decrypted)
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
