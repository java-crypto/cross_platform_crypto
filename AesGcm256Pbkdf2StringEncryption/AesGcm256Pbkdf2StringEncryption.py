from Crypto.Protocol.KDF import PBKDF2
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Hash import SHA256
import base64

def base64Encoding(input):
  dataBase64 = base64.b64encode(input)
  dataBase64P = dataBase64.decode("UTF-8")
  return dataBase64P

def base64Decoding(input):
    return base64.decodebytes(input.encode("ascii"))

def generateSalt32Byte():
  return get_random_bytes(32)

def generateRandomNonce():
  return get_random_bytes(12)

def aesGcmPbkdf2EncryptToBase64(password, plaintext):
  passwordBytes = password.encode("ascii")
  salt = generateSalt32Byte()
  PBKDF2_ITERATIONS = 15000
  encryptionKey = PBKDF2(passwordBytes, salt, 32, count=PBKDF2_ITERATIONS, hmac_hash_module=SHA256)
  nonce = generateRandomNonce()
  aad = b""
  cipher = AES.new(encryptionKey, AES.MODE_GCM, nonce=nonce)
  cipher.update(aad)
  ciphertext, tag = cipher.encrypt_and_digest(plaintext.encode("ascii"))
  nonceBase64 = base64Encoding(nonce)
  saltBase64 = base64Encoding(salt)
  ciphertextBase64 = base64Encoding(ciphertext)
  gcmTagBase64 = base64Encoding(tag)
  return saltBase64 + ":" + nonceBase64 + ":" + ciphertextBase64 + ":" + gcmTagBase64

def aesGcmPbkdf2DecryptFromBase64(password, ciphertextBase64):
  passwordBytes = password.encode("ascii")
  data = ciphertextDecryptionBase64.split(":")
  salt = base64Decoding(data[0])
  nonce = base64Decoding(data[1])
  ciphertext = base64Decoding(data[2])
  gcmTag = base64Decoding(data[3])
  PBKDF2_ITERATIONS = 15000
  decryptionKey = PBKDF2(passwordBytes, salt, 32, count=PBKDF2_ITERATIONS, hmac_hash_module=SHA256)
  aad = b""
  cipher = AES.new(decryptionKey, AES.MODE_GCM, nonce=nonce)
  cipher.update(aad)
  decryptedtext = cipher.decrypt_and_verify(ciphertext, gcmTag)
  decryptedtextP = decryptedtext.decode("UTF-8")
  return decryptedtextP

print("AES GCM 256 String encryption with PBKDF2 derived key")

plaintext = "The quick brown fox jumps over the lazy dog"
print("plaintext: " + plaintext)
password = "secret password"

print("\n* * * Encryption * * *")
ciphertextBase64 = aesGcmPbkdf2EncryptToBase64(password, plaintext)
print("ciphertext: " + ciphertextBase64)
print("output is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag")

print("\n* * * Decryption * * *")
ciphertextDecryptionBase64 = ciphertextBase64

print("ciphertext (Base64): " + ciphertextDecryptionBase64)
print("input is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag")
decryptedtext = aesGcmPbkdf2DecryptFromBase64(password, ciphertextBase64)
print("plaintext:  " + decryptedtext)
