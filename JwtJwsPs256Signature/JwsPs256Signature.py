import jwt
import time
# https://pyjwt.readthedocs.io/en/stable/index.html
# PyJWT version 2.0.1
# https://github.com/jpadilla/pyjwt

def buildPayloadObject(subject, issuer, tokenExpirationInSeconds):
  dateNow = int(time.time())
  dateExpired = dateNow + tokenExpirationInSeconds
  return {"sub": subject, "iss": issuer, "iat": dateNow, "exp": dateExpired}

def jwsSignatureToString(jwsAlgorithm, jwkRsaPrivateKey, jwtPayloadObject):
  return jwt.encode(jwtPayloadObject, jwkRsaPrivateKey, algorithm=jwsAlgorithm)

def jwsSignatureVerification(jwsAlgorithm, jwkRsaPublicKey, jwsSignedToken):
  try:
    decoded = jwt.decode(jwsSignedToken, jwkRsaPublicKey, algorithms=jwsAlgorithm)
    return 'true'
  except:
    decoded = ""
    return 'false'
  return 'false'

def jwsSignatureVerificationWithoutExpCheck(jwsAlgorithm, jwkRsaPublicKey, jwsSignedToken):
  try:
    decoded = jwt.decode(jwsSignedToken, jwkRsaPublicKey, algorithms=jwsAlgorithm, options={"verify_exp": False})
    return 'true'
  except:
    decoded = ""
    return 'false'
  return 'false'

def printPayloadObject(jwsSignedToken):
  return jwt.decode(jwsSignedTokenReceived, options={"verify_signature": False})

def printHeaderObject(jwsSignedToken):
  return jwt.get_unverified_header(jwsSignedTokenReceived)

def loadRsaPrivateKeyPem():
  return """-----BEGIN PRIVATE KEY-----
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
-----END PRIVATE KEY-----
"""

def loadRsaPublicKeyPem():
  return """-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9
iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT
/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8
01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB
ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g
QwIDAQAB
-----END PUBLIC KEY-----"""

def loadRsaPublicKeyPem2():
  return """-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9
iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT
/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8
01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB
ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW3g
QwIDAQAB
-----END PUBLIC KEY-----"""

print("JWT JWS PS256 RSA with RSASSA-PSS + MGF1 with SHA-256 padding and SHA-256 hashing")

jwsAlgorithm = 'PS256'

print("\n* * * sign the payload object with the RSA private key * * *")
# payload data
issuer = "https://java-crypto.github.io/cross_platform_crypto/"
subject = "JWT PS256 signature"
tokenExpirationInSeconds = 120

# build a JWT payload object
jwtPayloadObject = buildPayloadObject(subject, issuer, tokenExpirationInSeconds)
print("jwtClaimsSet: ")
print(jwtPayloadObject)

# read RSA private key in a PEM object
jwkRsaPrivateKey = loadRsaPrivateKeyPem()
print("\nprivateKey in PEM format:\n" + jwkRsaPrivateKey)

# sign the payload
jwsSignedToken = jwsSignatureToString(jwsAlgorithm, jwkRsaPrivateKey, jwtPayloadObject)
print("signed jwsToken:\n" + jwsSignedToken)

# send the token to the recipient
print("\n* * * verify the jwsToken with the RSA public key * * *")
jwsSignedTokenReceived = jwsSignedToken

# read the RSA public key in a JWK object
jwkRsaPublicKey = loadRsaPublicKeyPem()
print("publicKey in PEM format:\n" + jwkRsaPublicKey)

# show payloadObject
print("\nprint payloadObject")
payloadObject = printPayloadObject(jwsSignedTokenReceived)
print("payloadObject: ")
print(payloadObject)

# check for correct signature
print("\ncheck signature with matching public key")
tokenVerified = jwsSignatureVerification(jwsAlgorithm, jwkRsaPublicKey, jwsSignedTokenReceived)
print("the token's signature is verified: " + tokenVerified)

print("\ncheck signature with not matching public key")
jwkRsaPublicKey2 = loadRsaPublicKeyPem2()
tokenNotVerified = jwsSignatureVerification(jwsAlgorithm, jwkRsaPublicKey2, jwsSignedTokenReceived)
print("the token's signature is verified: " + tokenNotVerified)

# check expiration
# note that the signature verification automatically checks the "exp" value
# if the token is expired the verification result will be false although
# the signature itself is valid
jwsSignedTokenReceivedExpired = """eyJ0eXAiOiJKV1QiLCJhbGciOiJQUzI1NiJ9.eyJzdWIiOiJKV1QgUFMyNTYgc2lnbmF0dXJlIiwiaXNzIjoiaHR0cHM6Ly9qYXZhLWNyeXB0by5naXRodWIuaW8vY3Jvc3NfcGxhdGZvcm1fY3J5cHRvLyIsImlhdCI6MTYxMzQ4MDE4MywiZXhwIjoxNjEzNDgwMzAzfQ.gea2Q1PbNrMmH9ixM5SfxnFRT39ux92t3xt-9uphLTuEcEFLW2Ogm4b35RU3j825dXkwK97_cnhkKKnmWydSu8Y64ekw-XtMmZV9nZAdJMwCnF7fh0ucN-And4WB5kzFTjPQ5YAJUysUueRKxDkyHW1kap7ZMIDUsdVuPrKiqE7W0Nqvn0qnW4KKtLJ8ibSYU5RPqhY1Mv7OnsELy4XabsGqYEIuali3p5Bm1EGTNxlj0JVacYC3jTdbJJC_PnTpNItlSefTqfsTX2QG2wnpl12SXdy0pLDBh6nD24AoKoNf6KDBHGFmxJueSJMSb3iYi345KnsMaRdtbnSU0zsPCw"""
print("\ncheck signature with expired token")
tokenVerified = jwsSignatureVerification(jwsAlgorithm, jwkRsaPublicKey, jwsSignedTokenReceivedExpired)
print("the token's signature is verified: " + tokenVerified)

print("\ncheck signature without check the exp value")
tokenVerified = jwsSignatureVerificationWithoutExpCheck(jwsAlgorithm, jwkRsaPublicKey, jwsSignedTokenReceivedExpired)
print("the token's signature is verified: " + tokenVerified)

# print header
print("\nprint header")
headerObject = printHeaderObject(jwsSignedTokenReceived)
print("headerObject: ")
print(headerObject)
