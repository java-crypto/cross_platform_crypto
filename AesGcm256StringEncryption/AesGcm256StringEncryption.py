from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad
from Crypto.Util.Padding import unpad
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

def aesGcmDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64):
  data = ciphertextDecryptionBase64.split(":")
  nonce = base64Decoding(data[0])
  ciphertext = base64Decoding(data[1])
  gcmTag = base64Decoding(data[2])
  aad = b""
  cipher = AES.new(decryptionKey, AES.MODE_GCM, nonce=nonce)
  cipher.update(aad)
  decryptedtext = cipher.decrypt_and_verify(ciphertext, gcmTag)
  decryptedtextP = decryptedtext.decode("UTF-8")
  return decryptedtextP

print("AES GCM 256 String encryption with random key")

plaintext = "The quick brown fox jumps over the lazy dog"
print("plaintext: " + plaintext)

encryptionKey = generateRandomAesKey()
encryptionKeyBase64 = base64Encoding(encryptionKey)
print("encryptionKey (Base64): " + encryptionKeyBase64)

print("\n* * * Encryption * * *")
ciphertextBase64 = aesGcmEncryptToBase64(encryptionKey, plaintext)
print("ciphertext: " + ciphertextBase64)
print("output is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag")

print("\n* * * Decryption * * *")
ciphertextDecryptionBase64 = ciphertextBase64
decryptionKeyBase64 = encryptionKeyBase64
decryptionKey = base64Decoding(decryptionKeyBase64)
print("decryptionKey (Base64): " + decryptionKeyBase64)
print("ciphertext (Base64): " + ciphertextDecryptionBase64)
print("input is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag")
decryptedtext = aesGcmDecryptFromBase64(decryptionKey, ciphertextBase64)
print("plaintext:  " + decryptedtext)
