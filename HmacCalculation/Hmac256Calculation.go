package main

import (
	"crypto/sha256"
  "crypto/hmac"
  "encoding/base64"
	"fmt"
  "strconv"
)

func main() {
  fmt.Printf("HMAC 256 calculation\n")

  hmac256KeyString := string("hmac256ForAesEncryption")
  plaintextString := string("The quick brown fox jumps over the lazy dog")
  fmt.Printf("hmac256Key: " + hmac256KeyString + "\n")
  fmt.Printf("plaintext: " + plaintextString + "\n")
   // get binary data
  plaintext := []byte(plaintextString)
  hmacKey := []byte(hmac256KeyString)

  // calculate hmac
  hmac256 := Hmac256Calculation(hmacKey, plaintext)
  hmacLength := len(hmac256)
  fmt.Printf("hmac256 length: " + strconv.Itoa(hmacLength) + " (Base64) data: " + Base64Encoding(hmac256) + "\n")
}

func Hmac256Calculation(hmacKey []byte, plaintext []byte)([]byte) {
    h := hmac.New(sha256.New, hmacKey)
    h.Write(plaintext)
    return h.Sum(nil)
}

func Base64Encoding(input []byte)(string) {
  return base64.StdEncoding.EncodeToString(input)
}
