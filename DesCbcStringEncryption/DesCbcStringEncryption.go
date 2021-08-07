package main

import (
	"bytes"
  "strings"
  "crypto/des"
	"crypto/cipher"
	"crypto/rand"
  "encoding/base64"
	"fmt"
	"io"
)

func main() {

  fmt.Printf("\n# # # SECURITY WARNING: This code is provided for achieve    # # #\n");
  fmt.Printf("# # # compatibility between different programming languages. # # #\n");
  fmt.Printf("# # # It is not necessarily fully secure.                    # # #\n");
  fmt.Printf("# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #\n");
  fmt.Printf("# # # It uses FIXED key and IVs that should NEVER used,      # # #\n");
  fmt.Printf("# # # instead use random generated keys and IVs.             # # #\n");
  fmt.Printf("# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n");

  plaintext := string("The quick brown fox jumps over the lazy dog")
  fmt.Printf("\nplaintext: " + plaintext + "\n")

  fmt.Printf("\nDES CBC String encryption with fixed key & iv\n")

  desEncryptionKey := []byte("12345678");
  fmt.Printf("desEncryptionKey: " + Base64Encoding(desEncryptionKey));

  fmt.Printf("\n* * * Encryption * * *\n");
  desCiphertextBase64 := string(DesCbcEncryptToBase64(desEncryptionKey, plaintext))
  fmt.Printf("ciphertext: " + desCiphertextBase64 + "\n");
  fmt.Printf("output is (Base64) iv : (Base64) ciphertext\n");
  fmt.Printf("\n* * * Decryption * * *\n");
  fmt.Printf("ciphertext: " + desCiphertextBase64 + "\n");
  fmt.Printf("input is (Base64) iv : (Base64) ciphertext\n");
  desDecryptedtext := string(DesCbcDecryptFromBase64(desEncryptionKey, desCiphertextBase64))
  fmt.Printf("decryptedtext: " + desDecryptedtext + "\n")

  fmt.Printf("\nTriple DES CBC String encryption with fixed key (16 bytes) & iv\n");

  des2EncryptionKey := []byte("1234567890123456");
  fmt.Printf("des2EncryptionKey: " + Base64Encoding(des2EncryptionKey));

  fmt.Printf("\n* * * Encryption * * *\n");
  // convert tdes2 to tdes3 key
  var tripleDESKey []byte
	tripleDESKey = append(tripleDESKey, des2EncryptionKey[:16]...)
	tripleDESKey = append(tripleDESKey, des2EncryptionKey[:8]...)

  des2CiphertextBase64 := string(Des3CbcEncryptToBase64(tripleDESKey, plaintext))
  fmt.Printf("ciphertext: " + des2CiphertextBase64 + "\n");
  fmt.Printf("output is (Base64) iv : (Base64) ciphertext\n");
  fmt.Printf("\n* * * Decryption * * *\n");
  fmt.Printf("ciphertext: " + des2CiphertextBase64 + "\n");
  fmt.Printf("input is (Base64) iv : (Base64) ciphertext\n");
  des2Decryptedtext := string(Des3CbcDecryptFromBase64(tripleDESKey, des2CiphertextBase64))
  fmt.Printf("decryptedtext: " + des2Decryptedtext + "\n")

  fmt.Printf("\nTriple DES CBC String encryption with fixed key (24 bytes) & iv\n");

  des3EncryptionKey := []byte("123456789012345678901234");
  fmt.Printf("des3EncryptionKey: " + Base64Encoding(des3EncryptionKey));
  
  fmt.Printf("\n* * * Encryption * * *\n");
  des3CiphertextBase64 := string(Des3CbcEncryptToBase64(des3EncryptionKey, plaintext))
  fmt.Printf("ciphertext: " + des3CiphertextBase64 + "\n");
  fmt.Printf("output is (Base64) iv : (Base64) ciphertext\n");
  fmt.Printf("\n* * * Decryption * * *\n");
  fmt.Printf("ciphertext: " + des3CiphertextBase64 + "\n");
  fmt.Printf("input is (Base64) iv : (Base64) ciphertext\n");
  des3Decryptedtext := string(Des3CbcDecryptFromBase64(des3EncryptionKey, des3CiphertextBase64))
  fmt.Printf("decryptedtext: " + des3Decryptedtext + "\n")

}

func DesCbcEncryptToBase64(key []byte, data string)(string) {
	plaintext := []byte(data)
  plaintext = PKCS5Padding(plaintext, 8)
  // don't use this in production
  iv := []byte(GenerateFixedDesInitvector())
  //iv := []byte(GenerateRandomInitvector())
	block, err := des.NewCipher(key)
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

func DesCbcDecryptFromBase64(encryptionKey []byte, ciphertextCompleteBase64 string)(string) {
  data := strings.Split(ciphertextCompleteBase64, ":") 
  iv := []byte(Base64Decoding(data[0]))
  ciphertext := []byte(Base64Decoding(data[1]))
  block, err := des.NewCipher(encryptionKey)
	if err != nil {
		panic(err)
	}
	if len(ciphertext) < des.BlockSize {
		panic("ciphertext too short")
	}
	if len(ciphertext)%des.BlockSize != 0 {
		panic("ciphertext is not a multiple of the block size")
	}
	mode := cipher.NewCBCDecrypter(block, iv)
	mode.CryptBlocks(ciphertext, ciphertext)
  return string(ciphertext)
}

func Des3CbcEncryptToBase64(key []byte, data string)(string) {
	plaintext := []byte(data)
  plaintext = PKCS5Padding(plaintext, 8)
  // don't use this in production
  iv := []byte(GenerateFixedDesInitvector())
  //iv := []byte(GenerateRandomInitvector())
	block, err := des.NewTripleDESCipher(key)
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

func Des3CbcDecryptFromBase64(encryptionKey []byte, ciphertextCompleteBase64 string)(string) {
  data := strings.Split(ciphertextCompleteBase64, ":") 
  iv := []byte(Base64Decoding(data[0]))
  ciphertext := []byte(Base64Decoding(data[1]))
  block, err := des.NewTripleDESCipher(encryptionKey)
	if err != nil {
		panic(err)
	}
	if len(ciphertext) < des.BlockSize {
		panic("ciphertext too short")
	}
	if len(ciphertext)%des.BlockSize != 0 {
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

func GenerateRandomDesKey()([]byte) {
  key := make([]byte, 8) // 8 bytes for des, 16 bytes for TDES 2 and 24 bytes for TDES 3
	if _, err := io.ReadFull(rand.Reader, key); err != nil {
	  panic(err.Error())
	}
  return key
}

func GenerateRandomDesInitvector()([]byte) {
  iv := make([]byte, 8)
	if _, err := io.ReadFull(rand.Reader, iv); err != nil {
	  panic(err.Error())
	}
  return iv
}

func GenerateFixedDesInitvector()([]byte) {
  return []byte("12345678")
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
