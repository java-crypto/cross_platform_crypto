package main

import (
	"bytes"
  "crypto/des"
	"crypto/rand"
  "encoding/base64"
	"fmt"
	"io"
  "errors"
)

func main() {
  fmt.Printf("\n# # # SECURITY WARNING: This code is provided for achieve    # # #\n");
  fmt.Printf("# # # compatibility between different programming languages. # # #\n");
  fmt.Printf("# # # It is not necessarily fully secure.                    # # #\n");
  fmt.Printf("# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #\n");
  fmt.Printf("# # # It uses FIXED key that should NEVER used,              # # #\n");
  fmt.Printf("# # # instead use random generated keys.                     # # #\n");
  fmt.Printf("# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n");

  plaintext := string("The quick brown fox jumps over the lazy dog")
  fmt.Printf("\nplaintext: " + plaintext + "\n")

  fmt.Printf("\nDES ECB String encryption with fixed key\n")

  desEncryptionKey := []byte("12345678");
  fmt.Printf("desEncryptionKey: " + Base64Encoding(desEncryptionKey));

  fmt.Printf("\n* * * Encryption * * *\n");
  desCiphertextBase64, err := DesEcbEncryptToBase64(desEncryptionKey, plaintext)
  if err != nil {
		panic(err)
	}
  fmt.Printf("ciphertext: " + desCiphertextBase64 + "\n");
  fmt.Printf("output is (Base64) ciphertext\n");
  fmt.Printf("\n* * * Decryption * * *\n");
  fmt.Printf("ciphertext: " + desCiphertextBase64 + "\n");
  fmt.Printf("input is (Base64) ciphertext\n");
  desDecryptedtext, err := DesEcbDecryptFromBase64(desEncryptionKey, desCiphertextBase64)
  fmt.Printf("decryptedtext: " + desDecryptedtext + "\n")

  fmt.Printf("\nTriple DES ECB String encryption with fixed key (16 bytes)\n");

  des2EncryptionKey := []byte("1234567890123456");
  fmt.Printf("des2EncryptionKey: " + Base64Encoding(des2EncryptionKey));

  fmt.Printf("\n* * * Encryption * * *\n");
  // convert tdes2 to tdes3 key
  var tripleDESKey []byte
	tripleDESKey = append(tripleDESKey, des2EncryptionKey[:16]...)
	tripleDESKey = append(tripleDESKey, des2EncryptionKey[:8]...)

  des2CiphertextBase64, err := Des3EcbEncryptToBase64(tripleDESKey, plaintext)
  fmt.Printf("ciphertext: " + des2CiphertextBase64 + "\n");
  fmt.Printf("output is (Base64) ciphertext\n");
  fmt.Printf("\n* * * Decryption * * *\n");
  fmt.Printf("ciphertext: " + des2CiphertextBase64 + "\n");
  fmt.Printf("input is (Base64) ciphertext\n");
  des2Decryptedtext, err := Des3EcbDecryptFromBase64(tripleDESKey, des2CiphertextBase64)
  fmt.Printf("decryptedtext: " + des2Decryptedtext + "\n")

  fmt.Printf("\nTriple DES ECB String encryption with fixed key (24 bytes)\n");

  des3EncryptionKey := []byte("123456789012345678901234");
  fmt.Printf("des3EncryptionKey: " + Base64Encoding(des3EncryptionKey));
  
  fmt.Printf("\n* * * Encryption * * *\n");
  des3CiphertextBase64, err := Des3EcbEncryptToBase64(des3EncryptionKey, plaintext)
  fmt.Printf("ciphertext: " + des3CiphertextBase64 + "\n");
  fmt.Printf("output is (Base64) ciphertext\n");
  fmt.Printf("\n* * * Decryption * * *\n");
  fmt.Printf("ciphertext: " + des3CiphertextBase64 + "\n");
  fmt.Printf("input is (Base64) ciphertext\n");
  des3Decryptedtext, err := Des3EcbDecryptFromBase64(des3EncryptionKey, des3CiphertextBase64)
  fmt.Printf("decryptedtext: " + des3Decryptedtext + "\n")
}

// Golang does not support DES ECB mode in crypto, used 3rd party routines
  // https://developpaper.com/aes-des-and-3des-encryption-and-decryption-in-golang-support-multiple-mode-combinations-such-as-ecb-and-cbc-and-are-compatible-with-java-php-and-other-languages/

func DesEcbEncryptToBase64(key []byte, data string) (string, error) {
  plaintext := []byte(data)
  plaintext = PKCS5Padding(plaintext, 8)
  ciphertext, err := DesEncrypt(plaintext, key)
  if err != nil {
		return "", err
	}
  return Base64Encoding(ciphertext), err
}

func DesEcbDecryptFromBase64(key []byte, ciphertextBase64 string) (string, error) {
  ciphertext := Base64Decoding(ciphertextBase64)
  decryptedtext, err := DesDecrypt(ciphertext, key)
  return string(decryptedtext), err
}

func Des3EcbEncryptToBase64(key []byte, data string)(string, error) {
  plaintext := []byte(data)
  plaintext = PKCS5Padding(plaintext, 8)
  tkey := make([]byte, 24, 24)
	copy(tkey, key)
	k1 := tkey[:8]
	k2 := tkey[8:16]
	k3 := tkey[16:]
	buf1, err := DesEncrypt(plaintext, k1)
	if err != nil {
		return "", err
	}
	buf2, err := DesDecrypt(buf1, k2)
	if err != nil {
		return "", err
	}
	out, err := DesEncrypt(buf2, k3)
	if err != nil {
		return "", err
	}
	return Base64Encoding(out), nil
}

func Des3EcbDecryptFromBase64(key []byte, data string) (string, error) {
	tkey := make([]byte, 24, 24)
	copy(tkey, key)
	k1 := tkey[:8]
	k2 := tkey[8:16]
	k3 := tkey[16:]
  ciphertext := Base64Decoding(data)
	buf1, err := DesDecrypt(ciphertext, k3)
	if err != nil {
		return "", err
	}
	buf2, err := DesEncrypt(buf1, k2)
	if err != nil {
		return "", err
	}
	out, err := DesDecrypt(buf2, k1)
	if err != nil {
		return "", err
	}
	return string(out), nil
}

func DesEncrypt(origData, key []byte) ([]byte, error) {
	if len(origData) < 1 || len(key) < 1 {
		return nil, errors.New("wrong data or key")
	}
	block, err := des.NewCipher(key)
	if err != nil {
		return nil, err
	}
	bs := block.BlockSize()
	if len(origData)%bs != 0 {
		return nil, errors.New("wrong padding")
	}
	out := make([]byte, len(origData))
	dst := out
	for len(origData) > 0 {
		block.Encrypt(dst, origData[:bs])
		origData = origData[bs:]
		dst = dst[bs:]
	}
	return out, nil
}

func DesDecrypt(crypted, key []byte) ([]byte, error) {
	if len(crypted) < 1 || len(key) < 1 {
		return nil, errors.New("wrong data or key")
	}
	block, err := des.NewCipher(key)
	if err != nil {
		return nil, err
	}
	out := make([]byte, len(crypted))
	dst := out
	bs := block.BlockSize()
	if len(crypted)%bs != 0 {
		return nil, errors.New("wrong crypted size")
	}

	for len(crypted) > 0 {
		block.Decrypt(dst, crypted[:bs])
		crypted = crypted[bs:]
		dst = dst[bs:]
	}
	return out, nil
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
