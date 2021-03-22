package main

import (
	"crypto"
	"crypto/rand"
	"crypto/rsa"
	"crypto/sha256"
	"encoding/base64"
	"fmt"
  "crypto/x509"
  "encoding/pem"
)

func main() {

  fmt.Println("RSA signature PSS string")

  dataToSignString := string("The quick brown fox jumps over the lazy dog")
  dataToSign := []byte(dataToSignString)
  fmt.Println("dataToSign: " + dataToSignString)

  // usually we would load the private and public key from a file or keystore
  // here we use hardcoded keys for demonstration - don't do this in real programs

  fmt.Println("\n* * * sign the plaintext with the RSA private key * * *")
  // load private key
  privateKeyByte := []byte(LoadRsaPrivateKeyPem());
  fmt.Println("private key loaded")
  signatureBase64 := RsaSignToBase64(privateKeyByte, dataToSign);
  fmt.Println("\nsignature (Base64): " + signatureBase64)

  fmt.Println("\n* * * verify the signature against the plaintext with the RSA public key * * *")
  // load public key
  publicKeyByte := []byte(LoadRsaPublicKeyPem());
  fmt.Println("public key loaded")
  signatureVerified := string(RsaVerifySignatureFromBase64(publicKeyByte, dataToSign, signatureBase64))
  fmt.Println("\nsignature (Base64) verified: " + signatureVerified)
}

func RsaSignToBase64(privateKeyByte []byte, dataToSign []byte)(string){
  block, _ := pem.Decode(privateKeyByte)
  parseResult, _ := x509.ParsePKCS8PrivateKey(block.Bytes)
  privateKey := parseResult.(*rsa.PrivateKey)
  msgHash := sha256.New()
  _, err := msgHash.Write(dataToSign)
  if err != nil {
	  panic(err)
  }
  msgHashSum := msgHash.Sum(nil)
  signature, err := rsa.SignPSS(rand.Reader, privateKey, crypto.SHA256, msgHashSum, nil)
  if err != nil {
	  panic(err)
  }
  return Base64Encoding(signature)
}

func RsaVerifySignatureFromBase64(publicKeyByte []byte, dataToSign []byte, signatureBase64 string)(string){
  block, _ :=pem.Decode(publicKeyByte)
  parseResultPublicKey,err := x509.ParsePKIXPublicKey(block.Bytes)
  publicKey := parseResultPublicKey.(*rsa.PublicKey)
  msgHash := sha256.New()
  _, err2 := msgHash.Write(dataToSign)
  if err2 != nil {
	  panic(err2)
  }
  msgHashSum := msgHash.Sum(nil)
  signature := []byte(Base64Decoding(signatureBase64))
  err = rsa.VerifyPSS(publicKey, crypto.SHA256, msgHashSum, signature, nil)
  if err != nil {
	  fmt.Println("could not verify signature: ", err)
	  return "false"
  }
  return "true"
}  

func LoadRsaPrivateKeyPem()([]byte) {
  return []byte(`-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDwSZYlRn86zPi9
e1RTZL7QzgE/36zjbeCMyOhf6o/WIKeVxFwVbG2FAY3YJZIxnBH+9j1XS6f+ewjG
FlJY4f2IrOpS1kPiO3fmOo5N4nc8JKvjwmKtUM0t63uFFPfs69+7mKJ4w3tk2mSN
4gb8J9P9BCXtH6Q78SdOYvdCMspA1X8eERsdLb/jjHs8+gepKqQ6+XwZbSq0vf2B
MtaAB7zTX/Dk+ZxDfwIobShPaB0mYmojE2YAQeRq1gYdwwO1dEGk6E5J2toWPpKY
/IcSYsGKyFqrsmbw0880r1BwRDer4RFrkzp4zvY+kX3eDanlyMqDLPN+ghXT1lv8
snZpbaBDAgMBAAECggEBAIVxmHzjBc11/73bPB2EGaSEg5UhdzZm0wncmZCLB453
XBqEjk8nhDsVfdzIIMSEVEowHijYz1c4pMq9osXR26eHwCp47AI73H5zjowadPVl
uEAot/xgn1IdMN/boURmSj44qiI/DcwYrTdOi2qGA+jD4PwrUl4nsxiJRZ/x7PjL
hMzRbvDxQ4/Q4ThYXwoEGiIBBK/iB3Z5eR7lFa8E5yAaxM2QP9PENBr/OqkGXLWV
qA/YTxs3gAvkUjMhlScOi7PMwRX9HsrAeLKbLuC1KJv1p2THUtZbOHqrAF/uwHaj
ygUblFaa/BTckTN7PKSVIhp7OihbD04bSRrh+nOilcECgYEA/8atV5DmNxFrxF1P
ODDjdJPNb9pzNrDF03TiFBZWS4Q+2JazyLGjZzhg5Vv9RJ7VcIjPAbMy2Cy5BUff
EFE+8ryKVWfdpPxpPYOwHCJSw4Bqqdj0Pmp/xw928ebrnUoCzdkUqYYpRWx0T7YV
RoA9RiBfQiVHhuJBSDPYJPoP34kCgYEA8H9wLE5L8raUn4NYYRuUVMa+1k4Q1N3X
Bixm5cccc/Ja4LVvrnWqmFOmfFgpVd8BcTGaPSsqfA4j/oEQp7tmjZqggVFqiM2m
J2YEv18cY/5kiDUVYR7VWSkpqVOkgiX3lK3UkIngnVMGGFnoIBlfBFF9uo02rZpC
5o5zebaDImsCgYAE9d5wv0+nq7/STBj4NwKCRUeLrsnjOqRriG3GA/TifAsX+jw8
XS2VF+PRLuqHhSkQiKazGr2Wsa9Y6d7qmxjEbmGkbGJBC+AioEYvFX9TaU8oQhvi
hgA6ZRNid58EKuZJBbe/3ek4/nR3A0oAVwZZMNGIH972P7cSZmb/uJXMOQKBgQCs
FaQAL+4sN/TUxrkAkylqF+QJmEZ26l2nrzHZjMWROYNJcsn8/XkaEhD4vGSnazCu
/B0vU6nMppmezF9Mhc112YSrw8QFK5GOc3NGNBoueqMYy1MG8Xcbm1aSMKVv8xba
rh+BZQbxy6x61CpCfaT9hAoA6HaNdeoU6y05lBz1DQKBgAbYiIk56QZHeoZKiZxy
4eicQS0sVKKRb24ZUd+04cNSTfeIuuXZrYJ48Jbr0fzjIM3EfHvLgh9rAZ+aHe/L
84Ig17KiExe+qyYHjut/SC0wODDtzM/jtrpqyYa5JoEpPIaUSgPuTH/WhO3cDsx6
3PIW4/CddNs8mCSBOqTnoaxh
-----END PRIVATE KEY-----`)
}

func LoadRsaPublicKeyPem()([]byte) {
  return []byte(`-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9
iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT
/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8
01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB
ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g
QwIDAQAB
-----END PUBLIC KEY-----`)
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
