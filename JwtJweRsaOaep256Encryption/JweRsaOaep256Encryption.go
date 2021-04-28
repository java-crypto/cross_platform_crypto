package main

import (
	"fmt"
  "strings"
  "regexp"
  "time"
  "strconv"
  "encoding/json"
  "github.com/dvsekhvalnov/jose2go"
  Rsa "github.com/dvsekhvalnov/jose2go/keys/rsa"
)

func main() {
  // uses https://github.com/dvsekhvalnov/jose2go
  fmt.Println("JWT JWE RSA-OAEP-256 AES GCM 256 encryption")

  jweKeyAlgorithm := string("RSA-OAEP-256")
   // supported key algorithms: RSA-OAEP, RSA-OAEP-256, RS1_5
  // jweKeyAlgorithm = 'RSA-OAEP'
  // jweKeyAlgorithm = 'RSA1_5'
  jweEncryptionAlgorithm := string("A256GCM")
  // supported encryption algorithms: A128GCM, A192GCM, A256GCM
  // jweEncryptionAlgorithm = 'A128GCM'
  // jweEncryptionAlgorithm = 'A192GCM'

  // build the payload to encrypt
  dataToEncrypt := string("The quick brown fox jumps over the lazy dog")
  issuer := string("https://java-crypto.github.io/cross_platform_crypto/")
  subject := string("JWE RSA-OAEP & AES GCM encryption")
  tokenExpirationInSeconds := 120
  //payload := BuildPayloadObject(subject, issuer, tokenExpirationInSeconds)
  jwePayloadObject := BuildPayloadObject(dataToEncrypt, subject, issuer, tokenExpirationInSeconds)

  fmt.Println("\n* * * encrypt the payload with recipients public key * * *")
   
  fmt.Println("jwtClaimsSet: " + jwePayloadObject)
  // get public key from recipient
  publicKeyByte := []byte(LoadRsaPublicKeyPem())
  fmt.Println("public key loaded")
  // encrypt the data and build the encrypted jwe token
  jweTokenBase64Url := JweRsaEncryptToBase64UrlToken(publicKeyByte, jweKeyAlgorithm, jweEncryptionAlgorithm, jwePayloadObject)
  fmt.Println("jweToken (Base64Url encoded):\n" + jweTokenBase64Url)

  fmt.Println("\n* * * decrypt the payload with recipients private key * * *")
  // get private key
  privateKeyByte := []byte(LoadRsaPrivateKeyPem());
  fmt.Println("private key loaded")
  jweDecryptedPayload := JweRsaDecryptFromBase64UrlToken(privateKeyByte, jweTokenBase64Url);
  fmt.Println("jweDecryptedPayload:\n" + jweDecryptedPayload)

  // check for expiration
  fmt.Println("\ncheck expiration that is valid")
  tokenValid := JweCheckExpiration(jweDecryptedPayload)
  fmt.Print("the token is valid and not expired: ")
  fmt.Println(tokenValid)

  // get header
  fmt.Printf("\nheader: %v\n",JweGetHeader(privateKeyByte, jweTokenBase64Url))
}

func JweRsaEncryptToBase64UrlToken(rsaPublicKeyFromRecipient []byte, jweKeyAlgorithm string, jweEncryptionAlgorithm string, jwePayloadObject string)(string){
  // Creating encrypted tokens RSA-OAEP-256, RSA-OAEP and RSA1_5 key management algorithm
  publicKey,e:=Rsa.ReadPublic(rsaPublicKeyFromRecipient)
	if(e!=nil) {
		panic("invalid key format")
	}
  token,err := jose.Encrypt(jwePayloadObject, jweKeyAlgorithm, jweEncryptionAlgorithm, publicKey)
  if(err==nil) {
    return token
  }
  return "error"
}

func JweRsaDecryptFromBase64UrlToken(rsaPrivateKey []byte, jweTokenBase64Url string)(string){
  privateKey, e:=Rsa.ReadPrivate(rsaPrivateKey)
  if(e!=nil) {
    panic("invalid key format")
  }
  payload, _, err := jose.Decode(jweTokenBase64Url, privateKey)
  if(err==nil) {
     return payload
  }
  return "error"
}

func JweGetHeader(rsaPrivateKey []byte, jweTokenBase64Url string)(string){
  privateKey, e:=Rsa.ReadPrivate(rsaPrivateKey)
  if(e!=nil) {
    panic("invalid key format")
  }
  _, headers, err := jose.Decode(jweTokenBase64Url, privateKey)
  if(err==nil) {
    b, _ := json.Marshal(headers)
    headerString := string(b)
    return string(headerString)
  }
  return "error"
}

func JweCheckExpiration(jwePayload string)(bool) {
  iat := time.Now().Unix() // actual time
  expString := ExtractValue(jwePayload, "exp")
  exp, _ := strconv.ParseInt(expString, 10, 64)
  if (iat < exp) {
    return true // not expired
  }
  return false // expired
}

// extracts the value for a key from a JSON-formatted string
// body - the JSON-response as a string. Usually retrieved via the request body
// key - the key for which the value should be extracted
// returns - the value for the given key
func ExtractValue(body string, key string) string {
    keystr := "\"" + key + "\":[^,;\\]}]*"
    r, _ := regexp.Compile(keystr)
    match := r.FindString(body)
    keyValMatch := strings.Split(match, ":")
    return strings.ReplaceAll(keyValMatch[1], "\"", "")
}

func BuildPayloadObject(dataToEncrypt string, subject string, issuer string, tokenExpirationInSeconds int)(string) {
  iat := time.Now().Unix()
  exp := iat + int64(tokenExpirationInSeconds)
  return string(`{"iss":"`) + string(issuer) + string(`","sub":"`) + string(subject) + string(`", "dat":"`) + string(dataToEncrypt) + string(`","iat":`) + strconv.FormatInt(iat, 10) + string(`,"exp":`) + strconv.FormatInt(exp, 10) + string(`}`)
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

func LoadRsaPublicKeyPem2()([]byte) {
  return []byte(`-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9
iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT
/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8
01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB
ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW3g
QwIDAQAB
-----END PUBLIC KEY-----`)
}  
