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
  fmt.Println("JWT JWS RS256 RSA PKCS#1.5 signature with SHA-256")

  jwsAlgorithm := string("RS256")

  fmt.Println("\n* * * sign the payload object with the RSA private key * * *")
  // build a JWT payload object
  issuer := string("https://java-crypto.github.io/cross_platform_crypto/")
  subject := string("JWT RS256 signature")
  tokenExpirationInSeconds := 120
  payload := BuildPayloadObject(subject, issuer, tokenExpirationInSeconds)
  
  fmt.Println("jwtClaimsSet: " + payload)

  // // read RSA private key as PEM object
  privateKeyByte := []byte(LoadRsaPrivateKeyPem());
  fmt.Println("private key loaded")
  // sign the payload
  jwsSignedToken := JwsSignatureToString(jwsAlgorithm, privateKeyByte, payload)
  fmt.Println("\nsigned jwsToken:\n" + jwsSignedToken)

  // send the token to the recipient

  fmt.Println("\n* * * verify the jwsToken with the RSA public key * * *")
  jwsSignedTokenReceived := jwsSignedToken
  // load public key
  publicKeyByte := []byte(LoadRsaPublicKeyPem())

  tokenVerified := JwsSignatureVerification(publicKeyByte, jwsSignedTokenReceived);
  fmt.Print("\nthe tokens signature is verified: ")
  fmt.Println(tokenVerified)

  fmt.Println("\ncheck signature with not matching public key")
  publicKeyByte2 := []byte(LoadRsaPublicKeyPem2());
  tokenVerified2 := JwsSignatureVerification(publicKeyByte2, jwsSignedTokenReceived)
  fmt.Print("the tokens signature is verified: ")
  fmt.Println(tokenVerified2)

  // check for expiration
  fmt.Println("\ncheck expiration that is valid")
  decodedPayload := JwsGetPayload(publicKeyByte, jwsSignedTokenReceived); 
  tokenValid := CheckExpiration(decodedPayload)
  fmt.Print("the token is valid and not expired: ")
  fmt.Println(tokenValid)
  // with expired token
  fmt.Println("\ncheck expiration that is invalid")
  jwsSignedTokenReceivedExpired := `eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2phdmEtY3J5cHRvLmdpdGh1Yi5pby9jcm9zc19wbGF0Zm9ybV9jcnlwdG8vIiwic3ViIjoiSldUIFJTMjU2IHNpZ25hdHVyZSIsImlhdCI6MTYxMzQ3NDQ4MSwiZXhwIjoxNjEzNDc0NDgzfQ.pWw0U6nMwTikn1WImAi8phL6kxrVqwpyNAH7u1DVL26DyBbmtEFLTqAU3szrrVbB6vZirZx8fvJTMRaze3ExGZOzEgH4sUQkKz3HdUymz2LKWWpzlucRCB5CCMm5uVRf_f6MNsfAw3Fx41DryXU7BBoV3fp8Qt8rzdCPwabHSlCyNTC69AI46OmS25IUXzBpuhxa8-M9hh97YnSNwOYOfow3evJDNa9dUsO3ZLgUNTPQuxct3f0Bv95zmWzoMJYY-YsOSnBncSnac1t0GlhcwQCceata1fBrBqIc3a-r7hhrFmwv3Bam0LdCYTiqH5UU83osCIabFb3qlnQcRdLO0Q`
  decodedPayload = JwsGetPayload(publicKeyByte, jwsSignedTokenReceivedExpired); 
  tokenValid = CheckExpiration(decodedPayload)
  fmt.Print("the token is valid and not expired: ")
  fmt.Println(tokenValid)

  fmt.Printf("\nprint payload = %v\n",JwsGetPayload(publicKeyByte, jwsSignedTokenReceived))

  fmt.Printf("\nheader = %v\n",JwsGetHeader(publicKeyByte, jwsSignedTokenReceived))
}

func JwsSignatureToString(jwsAlgorithm string, privateKeyByte []byte, payload string)(string) {
  privateKey,e:=Rsa.ReadPrivate(privateKeyByte)
	if(e!=nil) {
		panic("invalid key format")
	}
	token,err := jose.Sign(payload,jose.RS256, privateKey)
	if(err==nil) {
		//go use token
		return token
	}
  return "" // error
}

func JwsSignatureVerification(publicKeyByte []byte, jwsSignedTokenReceived string)(bool) {
  publicKey, e:=Rsa.ReadPublic(publicKeyByte)
  if(e!=nil) {
      panic("invalid key format")
  }
  payload, headers, err := jose.Decode(jwsSignedTokenReceived, publicKey)
  if(err==nil) {
    //go use token
    fmt.Printf("\npayload = %v\n",payload)
    //and/or use headers
    fmt.Printf("headers = %v\n",headers)
    return true
  }
  if(e!=nil) {
		panic("error in verification")
	}
  return false;
}

func JwsGetPayload(publicKeyByte []byte, jwsSignedTokenReceived string)(string) {
  publicKey, e:=Rsa.ReadPublic(publicKeyByte)
  if(e!=nil) {
      panic("invalid key format")
  }
  payload, _, err := jose.Decode(jwsSignedTokenReceived, publicKey)
  if(err==nil) {
    return payload
  }
  if(e!=nil) {
		panic("error in verification")
	}
  return "error";
}

func JwsGetHeader(publicKeyByte []byte, jwsSignedTokenReceived string)(string) {
  publicKey, e:=Rsa.ReadPublic(publicKeyByte)
  if(e!=nil) {
      panic("invalid key format")
  }
  _, header, err := jose.Decode(jwsSignedTokenReceived, publicKey)
  if(err==nil) {
    b, _ := json.Marshal(header)
    //fmt.Println(string(b))
    headerString := string(b)
    return string(headerString)
  }
  if(e!=nil) {
		panic("error in verification")
	}
  return "error";
}

func CheckExpiration(jwsPayload string)(bool) {
  iat := time.Now().Unix() // actual time
  expString := ExtractValue(jwsPayload, "exp")
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

func BuildPayloadObject(subject string, issuer string, tokenExpirationInSeconds int)(string) {
  iat := time.Now().Unix()
  exp := iat + int64(tokenExpirationInSeconds)
  return string(`{"iss":"`) + string(issuer) + string(`","sub":"`) + string(subject) + string(`","iat":`) + strconv.FormatInt(iat, 10) + string(`,"exp":`) + strconv.FormatInt(exp, 10) + string(`}`)
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
