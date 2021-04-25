from Crypto.PublicKey import RSA
from Crypto.Signature import pkcs1_15
from Crypto import Hash
import base64

def base64Encoding(input):
  dataBase64 = base64.b64encode(input)
  dataBase64P = dataBase64.decode("UTF-8")
  return dataBase64P

def base64Decoding(input):
    return base64.decodebytes(input.encode("ascii"))

def rsaSignToBase64(privateKeyPem, passphrase, message):
  rsaPrivateKey = RSA.import_key(privateKeyPem, passphrase)
  hashMessage = Hash.SHA256.new(message.encode("ascii"))
  signature = pkcs1_15.new(rsaPrivateKey).sign(hashMessage)
  return base64Encoding(signature)

def rsaVerifySignatureFromBase64(publicKeyPem, message, signatureBase64):
    rsaPublicKey = RSA.import_key(publicKeyPem)
    signatureBytes = base64Decoding(signatureBase64)
    hashMessage = Hash.SHA256.new(message.encode("ascii"))
    try:
      verifier = pkcs1_15.new(rsaPublicKey).verify(hashMessage, signatureBytes)
      return True
    except (ValueError):
      return False

def loadRsaPrivateKeyEncryptedPem():
  return """-----BEGIN ENCRYPTED PRIVATE KEY-----
MIIFLTBXBgkqhkiG9w0BBQ0wSjApBgkqhkiG9w0BBQwwHAQIUo/aHb7/5jUCAggA
MAwGCCqGSIb3DQIJBQAwHQYJYIZIAWUDBAEqBBCbpiqeplyyDN85uu5yYqpFBIIE
0Iqvy2v42HeASj6B25llJjNOWvT5djjDiAg7BFsuOhMq10mTC7gWduXBai5llUBH
qF4+GgqCeedwT24YkdR4wAqtc2shcmq+2xC70N/XrSg9geGehmNwDx35byhPjouk
XwA5tgVsKtxTob/xjPYhjL5J1KpWOPQhdnHPGfGlWedTyTKboRfgkZ/Yds5EW6k7
mO9wt/iXcym6QgaJTswa1ntGPVaKMYvXVRtNnphsI9M7njXBYO52vfujUaeKn8Xe
qmuEAKGFhhu+r7/WWyQl+Zq2KIoD5ImOxuXScCZVH3+76AxXSwDJzB6ZG1SowSy+
YHbfkmvK2H6YdFshEfI/coMmu9E3wDKuCY7rgb70lHLcpvbSMa88vCFl3d/ZfmFl
w4TwG3vEVR9wfbXAuAw6XTSLBODcF4ifZnyXI2Dr+fQbQd/4o8w05hDiPkXRgnYY
e/4nFbTGGKvatZ7LOLa62isiGtQ4nprDpTapLMeJCFSgQN1jlZMVvV6mKUfrCJ69
3bESlyYGAN0MAx4KeMcCRwuEXWhOkLMFW0gvRp1udiMxJ8SnWfseYTr2FPUFQlFk
2ZhHsSO0Pg/cszUSuCX0q2ZW0ZZOiihJsmiQNXlbsRog9m8vaxUfA00v/q6iryGE
ApCiQd653ikukRhk/nQEY6OV4byLyI9zESF/pIdsrFnEtGSd0tggGsREtn11D8+7
SW0F0Es4P4UUPjiaYzyB58gcZJWxcoq63NOTUaicvTjNbzQu1+4y+2xhFe89e09w
PFSGerjSV/uVtVTH2hb79mAmlyLvaikQfQq2nvpCuZy7wZjhPA3D03WFTCbrYS7P
PfckAICCBCIB97vPsWVAviOVOrG5NDbIag+/3YhZb2gyuR2/E0XI1NdXO63NqU9R
B6VUUNdSvv4djA30nVjARlh3LOZmTy50OIj/QDOEbBrtFH79eHHxHRiK0+Q0A8Ch
EoDDz7bMKBcpiS3vXnI2UsrZX/QIuRjypc003uVm4MvdUwv0j3UGZr9zRGctqWf0
YBRkoIELRA45hQn0Ln3+wjAcjLRPpeuPgVAq7tDDLDmP0i/m8UbjNorhdv4CrT/u
P+keTW7v9Nuov1IXG4W6wKipYRzMjiYrTL5TaXPi9z63b6RQVShAEqSTtsPg5nDy
CZkt5NPBltq+3InrsTlbKSQGYn6wyEQeBYezD3CLkIliDn7UM4BDn+k5YonNynU4
emxbU4wyBEXoMD8+aBMf+bKKQXU97Ts9p7is+Ogqyop2mbMOebIxCKayizlb7/hp
Qs2ThBP84OrniUEeKVAkdykZyrdCoatgXVJngDwwEkSwOrEYZ0sJ48qPQMTN3kVq
r5XJjX3m8mpXdz0s9VbhT/rWSdQMX82msof4c6+RghEbiHFG4B0el5vrJaAROb69
3OnoU8mYKBlFjbO1rJJpGS5h/v+ysqWxDxfPPLD86UsgDAjr5C7W3K8eYB1UD/wP
n91TuTdrLKhLE93VsCyXnsLm44Td3MBv89Fr2CMF/Jz15Nfrg3yvR7bKvkNs3uVM
FVO3kqtmUMC/RsUr3NIE/RZ2sS+cjLMDE2zBUgHnDpNQB6WSuwqEI9prAWxEc1jS
SwyNkvstftK35nfeI3FNP0R94+kSKzGmiQh5yl3ufAVi
-----END ENCRYPTED PRIVATE KEY-----
"""

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

print("Load RSA PKCS8 encrypted private key AES256-CBC PBKDF2 HMAC with SHA-256")

dataToSign = "The quick brown fox jumps over the lazy dog"
print("dataToSign: " + dataToSign)

passphrase = "123456"

print("\n* * * sign the plaintext with the encrypted RSA private key * * *")
privateRsaKeyPem = loadRsaPrivateKeyEncryptedPem()
print("used private key:\n" + privateRsaKeyPem)
signatureBase64 = rsaSignToBase64(privateRsaKeyPem, passphrase, dataToSign)
print("signature (Base64): " + signatureBase64)

print("\n* * * verify the signature against the plaintext with the RSA public key * * *")
publicRsaKeyPem = loadRsaPublicKeyPem()
print("used public key:\n" + publicRsaKeyPem)
signatureVerified = rsaVerifySignatureFromBase64(publicRsaKeyPem, dataToSign, signatureBase64)
print("\nsignature (Base64) verified: ")
print(signatureVerified)
