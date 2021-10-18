// https://replit.com/@javacrypto/CpcGoGenerateSha256Base64Hex#main.go
package main

import (
  "crypto/sha256"
  "encoding/hex"
  "encoding/base64"
	"fmt"
  "strconv"
)

func main() {

  fmt.Println("Generate a SHA-256 hash, Base64 en- and decoding and hex conversions")

  plaintext := string("The quick brown fox jumps over the lazy dog")
  fmt.Println("plaintext: " + plaintext)

  sha256Value := CalculateSha256(plaintext)
  sha256Length := len(sha256Value)
  fmt.Println("sha256Value (hex) length: " + strconv.Itoa(sha256Length) + " data: " + BytesToHex(sha256Value))

  sha256Base64 := Base64Encoding(sha256Value)
  fmt.Println("sha256Value (base64) " + sha256Base64)

  sha256ValueDecoded := Base64Decoding(sha256Base64)
  sha256ValueDecodedLength := len(sha256ValueDecoded)
  fmt.Println("sha256Base64 decoded to a byte array:")
  fmt.Println("sha256Value (hex) length: " + strconv.Itoa(sha256ValueDecodedLength) + " data: " + BytesToHex(sha256ValueDecoded))

  sha256HexString := "d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592"
  sha256Hex := HexStringToByteArray(sha256HexString)
  sha256HexLength := len(sha256Hex)
  fmt.Println("sha256HexString converted to a byte array:")
  fmt.Println("sha256Value (hex) length: " + strconv.Itoa(sha256HexLength) + " data: " + BytesToHex(sha256Hex))
}

func CalculateSha256(dataToHash string)([]byte){
  msgHash := sha256.New()
  _, err := msgHash.Write([]byte(dataToHash))
  if err != nil {
	  panic(err)
  }
  msgHashSum := msgHash.Sum(nil)
  return msgHashSum
}

func BytesToHex(input []byte)(string){
  return hex.EncodeToString(input)
}

func HexStringToByteArray(input string)([]byte){
  data, err := hex.DecodeString(input)
  if err != nil {
    panic(err)
  }
  return data;
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
