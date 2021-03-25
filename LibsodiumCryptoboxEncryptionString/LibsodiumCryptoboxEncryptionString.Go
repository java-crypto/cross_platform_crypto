package main

import (
  "strings"
  "golang.org/x/crypto/nacl/box"
	"crypto/rand"
  "encoding/base64"
	"fmt"
	"io"
)

func main() {

  fmt.Printf("Libsodium crypto box hybrid string encryption\n")

  plaintext := string("The quick brown fox jumps over the lazy dog")
  fmt.Printf("plaintext: " + plaintext + "\n")

  fmt.Printf("\n* * * encryption * * *\n");
  fmt.Printf("all data are in Base64 encoding\n");
  // for encryption you need your private key (privateKeyA) and the public key from other party (publicKeyB)
  privateKeyABase64 := "yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU="
  publicKeyBBase64 := "jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg="
  // convert the base64 encoded keys
  privateKeyA := []byte(Base64Decoding(privateKeyABase64))
  publicKeyB := []byte(Base64Decoding(publicKeyBBase64))
  ciphertextBase64 := string(CryptoboxEncryptionToBase64(privateKeyA, publicKeyB, plaintext))
  fmt.Printf("privateKeyA: " + privateKeyABase64 + "\n")
  fmt.Printf("publicKeyB:  " + publicKeyBBase64 + "\n")
  fmt.Printf("ciphertext: " + ciphertextBase64 + "\n");
  fmt.Printf("output is (Base64) nonce : (Base64) ciphertext\n");

  fmt.Printf("\n* * * Decryption * * *\n");
  // for decryption you need your private key (privateKeyB) and the public key from other party (publicKeyA)
  privateKeyBBase64 := "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM="
  publicKeyABase64 := "b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0="
  // convert the base64 encoded keys
  privateKeyB := []byte(Base64Decoding(privateKeyBBase64))
  publicKeyA := []byte(Base64Decoding(publicKeyABase64))

  ciphertextReceivedBase64 := string(ciphertextBase64);
  fmt.Printf("ciphertext: " + ciphertextReceivedBase64 + "\n");
  fmt.Printf("input is (Base64) nonce : (Base64) ciphertext\n");
  decryptedtext := string(CryptoboxDecryptionFromBase64(privateKeyB, publicKeyA, ciphertextReceivedBase64))
  fmt.Printf("decryptedtext: " + decryptedtext + "\n")
}

func CryptoboxEncryptionToBase64(privateKey []byte, publicKey []byte, data string)(string) {
  plaintext := []byte(data)
  // generate random noncevar nonce [24]byte
  var nonce [24]byte
	if _, err := io.ReadFull(rand.Reader, nonce[:]); err != nil {
		panic(err)
	}
  var privateKey32 [32]byte
	copy(privateKey32[:], privateKey)
  var publicKey32 [32]byte
	copy(publicKey32[:], publicKey)
  encrypted := box.Seal(nonce[:], plaintext, &nonce, &publicKey32, &privateKey32)
  nonceEnc, ciphertext := encrypted[:24], encrypted[24:]
  return Base64Encoding(nonceEnc) + ":" + Base64Encoding(ciphertext)
}

func CryptoboxDecryptionFromBase64(privateKey []byte, publicKey []byte, ciphertextDecryptionBase64 string)(string) {
  data := strings.Split(ciphertextDecryptionBase64, ":") 
  nonce := []byte(Base64Decoding(data[0]))
  ciphertext := []byte(Base64Decoding(data[1]))
  var decryptNonce [24]byte
  copy(decryptNonce[:], nonce[:24])
  var privateKey32 [32]byte
	copy(privateKey32[:], privateKey)
  var publicKey32 [32]byte
	copy(publicKey32[:], publicKey)
  decrypted, ok := box.Open(nil, ciphertext, &decryptNonce, &publicKey32, &privateKey32)
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
