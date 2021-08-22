package main

import (
	"crypto/ecdsa"
	"crypto/rand"
	"crypto/sha256"
	"crypto/x509"
	"encoding/base64"
	"encoding/pem"
	"fmt"
)

func main() {

	fmt.Println("EC signature string (ECDSA with SHA256)")

	dataToSignString := string("The quick brown fox jumps over the lazy dog")
	dataToSign := []byte(dataToSignString)
	fmt.Println("dataToSign: " + dataToSignString)

	// usually we would load the private and public key from a file or keystore
	// here we use hardcoded keys for demonstration - don't do this in real programs

	fmt.Println("\n* * * sign the plaintext with the EC private key * * *")
	// load private key
	privateKeyByte := []byte(LoadEcPrivateKeyPem())
	fmt.Println("private key loaded")
	signatureBase64 := EcSignToBase64(privateKeyByte, dataToSign)
	fmt.Println("\nsignature (Base64): " + signatureBase64)

	fmt.Println("\n* * * verify the signature against the plaintext with the EC public key * * *")
	// load public key
	publicKeyByte := []byte(LoadEcPublicKeyPem())
	fmt.Println("public key loaded")
	signatureVerified := bool(EcVerifySignatureFromBase64(publicKeyByte, dataToSign, signatureBase64))
	fmt.Println("\nsignature (Base64) verified: ", signatureVerified)

}

func EcSignToBase64(privateKeyByte []byte, dataToSign []byte) string {
	block, _ := pem.Decode(privateKeyByte)
	parseResult, _ := x509.ParsePKCS8PrivateKey(block.Bytes)
	privateKey := parseResult.(*ecdsa.PrivateKey)
	msgHash := sha256.New()
	_, err := msgHash.Write(dataToSign)
	if err != nil {
		panic(err)
	}
	msgHashSum := msgHash.Sum(nil)
	signature, err := ecdsa.SignASN1(rand.Reader, privateKey, msgHashSum)
	if err != nil {
		panic(err)
	}
	return Base64Encoding(signature)
}

func EcVerifySignatureFromBase64(publicKeyByte []byte, dataToSign []byte, signatureBase64 string) bool {
	block, _ := pem.Decode(publicKeyByte)
	parseResultPublicKey, err := x509.ParsePKIXPublicKey(block.Bytes)
	publicKey := parseResultPublicKey.(*ecdsa.PublicKey)
	msgHash := sha256.New()
	_, err2 := msgHash.Write(dataToSign)
	if err2 != nil {
		panic(err2)
	}
	msgHashSum := msgHash.Sum(nil)
	signature := []byte(Base64Decoding(signatureBase64))
	valid := ecdsa.VerifyASN1(publicKey, msgHashSum, signature)
	// error in reading the public key
	if err != nil {
		fmt.Println("could not load the public key: ", err)
		return false
	}
	return valid
}

func LoadEcPrivateKeyPem() []byte {
	return []byte(`-----BEGIN PRIVATE KEY-----
MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgciPt/gulzw7/Xe12
YOu/vLUgIUZ+7gGo5VkmU0B+gUWhRANCAAQxkee3UPW110s0aUQdcS0TDkr8blAe
SBouL4hXziiJX5Me/8OobFgNfYXkk6R/K/fqJhJ/mV8gLur16XhgueXA
-----END PRIVATE KEY-----`)
}

func LoadEcPublicKeyPem() []byte {
	return []byte(`-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEMZHnt1D1tddLNGlEHXEtEw5K/G5Q
HkgaLi+IV84oiV+THv/DqGxYDX2F5JOkfyv36iYSf5lfIC7q9el4YLnlwA==
-----END PUBLIC KEY-----`)
}

func Base64Encoding(input []byte) string {
	return base64.StdEncoding.EncodeToString(input)
}

func Base64Decoding(input string) []byte {
	data, err := base64.StdEncoding.DecodeString(input)
	if err != nil {
		return data
	}
	return data
}
