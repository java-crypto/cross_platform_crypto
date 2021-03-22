from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
import base64

def base64Encoding(input):
  dataBase64 = base64.b64encode(input)
  dataBase64P = dataBase64.decode("UTF-8")
  return dataBase64P

def base64Decoding(input):
    return base64.decodebytes(input.encode("ascii"))

def generateRandomAesKey():
  return get_random_bytes(32)    

def generateRandomNonce():  
  return get_random_bytes(12)  

def rsaOaepSha1AesGcm256HybridStringEncryption(publicKeyPem, message):
  # generate random key
  encryptionKey = generateRandomAesKey()
  # encrypt plaintext
  ciphertextBase64 = aesGcmEncryptToBase64(encryptionKey, message)
  # encrypt the aes encryption key with the rsa public key
  encryptionKeyBase64 = base64Encoding(rsaEncryptionOaepSha1(publicKeyPem, encryptionKey))
  # complete output
  return encryptionKeyBase64 + ":" + ciphertextBase64

def rsaOaepSha1AesGcm256HybridStringDecryption(privateKeyPem, ciphertextDecryptionBase64):
  data = ciphertextDecryptionBase64.split(":")
  encryptionkeyReceived = base64Decoding(data[0])
  nonce = base64Decoding(data[1])
  encryptedData = base64Decoding(data[2])
  gcmTag = base64Decoding(data[3])
  decryptionKey = rsaDecryptionOaepSha1(privateKeyPem, encryptionkeyReceived)
  return aesGcmDecrypt(decryptionKey, nonce, encryptedData, gcmTag)

def aesGcmEncryptToBase64(encryptionKey, plaintext):
  nonce = generateRandomNonce()
  aad = b""
  cipher = AES.new(encryptionKey, AES.MODE_GCM, nonce=nonce)
  cipher.update(aad)
  ciphertext, tag = cipher.encrypt_and_digest(plaintext.encode("ascii"))
  nonceBase64 = base64Encoding(nonce)
  ciphertextBase64 = base64Encoding(ciphertext)
  gcmTagBase64 = base64Encoding(tag)
  return nonceBase64 + ":" + ciphertextBase64 + ":" + gcmTagBase64

def aesGcmDecrypt(decryptionKey, nonce, ciphertextDecryption, gcmTag):
  aad = b""
  cipher = AES.new(decryptionKey, AES.MODE_GCM, nonce=nonce)
  cipher.update(aad)
  decryptedtext = cipher.decrypt_and_verify(ciphertextDecryption, gcmTag)
  decryptedtextP = decryptedtext.decode("UTF-8")
  return decryptedtextP

def rsaEncryptionOaepSha1(publicKeyPem, aesKey):
  rsaPublicKey = RSA.import_key(publicKeyPem)
  cipher = PKCS1_OAEP.new(rsaPublicKey)
  ciphertext = cipher.encrypt(aesKey)
  return ciphertext

def rsaDecryptionOaepSha1(privateKeyPem, ciphertextBytes):
  rsaPrivateKey = RSA.import_key(privateKeyPem)
  cipher = PKCS1_OAEP.new(rsaPrivateKey)
  try:
    message = cipher.decrypt(ciphertextBytes)
    return message
  except (ValueError):
    return ""

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

print("RSA AES GCM hybrid encryption")

print("\nuses AES GCM 256 random key for the plaintext encryption")
print("and RSA OAEP SHA 1 2048 bit for the encryption of the random key")

dataToEncrypt = "The quick brown fox jumps over the lazy dog"
print("\nplaintext: " + dataToEncrypt)

# # # usually we would load the private and public key from a file or keystore # # #
# # # here we use hardcoded keys for demonstration - don't do this in real programs # # #

print("\n* * * Encryption * * *")
publicRsaKeyPem = loadRsaPublicKeyPem()

completeCiphertextBase64 = rsaOaepSha1AesGcm256HybridStringEncryption(publicRsaKeyPem, dataToEncrypt)

print("completeCiphertextBase64: " + completeCiphertextBase64)

# transport the encrypted data to recipient

# receiving the encrypted data, decryption
print("\n* * * Decryption * * *")
completeCiphertextReceivedBase64 = completeCiphertextBase64
privateRsaKeyPem = loadRsaPrivateKeyPem()
decryptedtext = rsaOaepSha1AesGcm256HybridStringDecryption(privateRsaKeyPem, completeCiphertextReceivedBase64)
print("decryptedtext: " + decryptedtext)
