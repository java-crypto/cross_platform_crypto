import time
import json
# Authlib version 0.15.3
# https://authlib.org/
# ttps://docs.authlib.org/en/stable/
# https://github.com/lepture/authlib
from authlib.jose import JsonWebEncryption

def buildHeaderObject(jweKeyAlgorithm, jweEncryptionAlgorithm):
  return {'alg': jweKeyAlgorithm, 'enc': jweEncryptionAlgorithm, 'typ': 'JWT'}

def buildPayloadObject(subject, issuer, data, tokenExpirationInSeconds):
  dateNow = int(time.time())
  dateExpired = dateNow + tokenExpirationInSeconds
  return {'sub': subject, 'iss': issuer, 'dat': data, 'iat': dateNow, 'exp': dateExpired}

def jweRsaEncryptToBase64UrlToken(rsaPublicKey, jweKeyAlgorithm, jweEncryptionAlgorithm, jwePayloadObject):
  jwe = JsonWebEncryption()
  jweHeaderObject = buildHeaderObject(jweKeyAlgorithm, jweEncryptionAlgorithm)
  return jwe.serialize_compact(jweHeaderObject, json.dumps(jwePayloadObject), rsaPublicKey)

def jweRsaDecryptFromBase64UrlToken(rsaPrivateKey, jweTokenBase64Url):
  jwe = JsonWebEncryption()
  return jwe.deserialize_compact(jweTokenBase64Url, rsaPrivateKey)

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

print("JWT JWE RSA-OAEP-256 AES GCM 256 encryption")

jweKeyAlgorithm = "RSA-OAEP-256" # support encryption algorithms: RSA1_5, RSA-OAEP, RSA-OAEP-256
#jweKeyAlgorithm = "RSA-OAEP"
#jweKeyAlgorithm = "RSA1_5"
jweEncryptionAlgorithm = "A256GCM" # supported key algorithms: A128GCM, A192GCM, A256GCM
#jweEncryptionAlgorithm = "A192GCM"
#jweEncryptionAlgorithm = "A128GCM"

print("jweKeyAlgorithm:        " + jweKeyAlgorithm)
print("jweEncryptionAlgorithm: " + jweEncryptionAlgorithm)

# build the payload to encrypt
subject = "JWE RSA-OAEP & AES GCM encryption"
issuer = "https://java-crypto.github.io/cross_platform_crypto/"
data = "The quick brown fox jumps over the lazy dog"
tokenExpirationInSeconds = 120
jwePayloadObject = buildPayloadObject(subject, issuer, data, tokenExpirationInSeconds);
print("\njwePayloadObject:\n" + json.dumps(jwePayloadObject))

print("\n* * * encrypt the payload with recipient\'s public key * * *")
# get public key from recipient
rsaPublicKeyFromRecipient = loadRsaPublicKeyPem()
print("public key loaded")
jweTokenBase64Url = jweRsaEncryptToBase64UrlToken(rsaPublicKeyFromRecipient, jweKeyAlgorithm, jweEncryptionAlgorithm, jwePayloadObject)
print("jweToken (Base64Url encoded):")
print(jweTokenBase64Url)

print("\n* * * decrypt the payload with recipient\'s private key * * *")
rsaPrivateKey = loadRsaPrivateKeyPem()
print("private key loaded")
jweDecryptedPayload = jweRsaDecryptFromBase64UrlToken(rsaPrivateKey, jweTokenBase64Url)

# get jweHeaderObject
jweHeaderObjectDecrypted = jweDecryptedPayload['header']
print("jweHeaderObject: ")
print (jweHeaderObjectDecrypted)

# get jwePayloadObject
jwePayloadObjectDecrypted = jweDecryptedPayload['payload']
print("jwePayloadObject")
print(jwePayloadObjectDecrypted)
