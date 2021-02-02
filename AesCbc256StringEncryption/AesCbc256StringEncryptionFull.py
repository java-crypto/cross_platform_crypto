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

def aesCbcEncryptToBase64(encryptionKey, plaintext):
  cipher = AES.new(encryptionKey, AES.MODE_CBC)
  ciphertext = cipher.encrypt(pad(plaintext.encode("ascii"), AES.block_size))
  ivBase64 = base64Encoding(cipher.iv)
  ciphertextBase64 = base64Encoding(ciphertext)
  return ivBase64 + ":" + ciphertextBase64

def aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64):
  data = ciphertextDecryptionBase64.split(":")
  iv = base64Decoding(data[0])
  ciphertext = base64Decoding(data[1])
  cipher = AES.new(decryptionKey, AES.MODE_CBC, iv)
  decryptedtext = unpad(cipher.decrypt(ciphertext), AES.block_size)
  decryptedtextP = decryptedtext.decode("UTF-8")
  return decryptedtextP

print("AES CBC 256 String encryption with random key full")

plaintext = "The quick brown fox jumps over the lazy dog"
print("plaintext: " + plaintext)

encryptionKey = generateRandomAesKey()
encryptionKeyBase64 = base64Encoding(encryptionKey)
print("encryptionKey (Base64): " + encryptionKeyBase64)

print("\n* * * Encryption * * *")
ciphertextBase64 = aesCbcEncryptToBase64(encryptionKey, plaintext)
print("ciphertext: " + ciphertextBase64)
print("output is (Base64) iv : (Base64) ciphertext")

print("\n* * * Decryption * * *")
ciphertextDecryptionBase64 = ciphertextBase64
decryptionKeyBase64 = encryptionKeyBase64
decryptionKey = base64Decoding(decryptionKeyBase64)
print("decryptionKey (Base64): " + decryptionKeyBase64)
print("ciphertext (Base64): " + ciphertextDecryptionBase64)
print("input is (Base64) iv : (Base64) ciphertext")
decryptedtext = aesCbcDecryptFromBase64(decryptionKey, ciphertextBase64)
print("plaintext:  " + decryptedtext)
