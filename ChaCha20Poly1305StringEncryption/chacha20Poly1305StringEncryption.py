from Crypto.Cipher import ChaCha20_Poly1305
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

def chacha20Poly1305EncryptToBase64(encryptionKey, plaintext):
  nonce = generateRandomNonce()
  aad = b""
  cipher = ChaCha20_Poly1305.new(key=encryptionKey, nonce=nonce)
  cipher.update(aad)
  ciphertext, tag = cipher.encrypt_and_digest(plaintext.encode("ascii"))
  nonceBase64 = base64Encoding(nonce)
  ciphertextBase64 = base64Encoding(ciphertext)
  poly1305TagBase64 = base64Encoding(tag)
  return nonceBase64 + ":" + ciphertextBase64 + ":" + poly1305TagBase64

def chacha20Poly1305DecryptFromBase64(decryptionKey, ciphertextDecryptionBase64):
  data = ciphertextDecryptionBase64.split(":")
  nonce = base64Decoding(data[0])
  ciphertext = base64Decoding(data[1])
  poly1305Tag = base64Decoding(data[2])
  aad = b""
  decryptedtext = ""
  try:
    cipher = ChaCha20_Poly1305.new(key=decryptionKey, nonce=nonce)
    cipher.update(aad)
    decryptedtext = cipher.decrypt_and_verify(ciphertext, poly1305Tag)
    return decryptedtext.decode("UTF-8")
  except ValueError:
    return ""
  return ""

print("ChaCha20-Poly1305 String encryption with random key full")

plaintext = "The quick brown fox jumps over the lazy dog"
print("plaintext: " + plaintext)

encryptionKey = generateRandomAesKey()
encryptionKeyBase64 = base64Encoding(encryptionKey)
print("encryptionKey (Base64): " + encryptionKeyBase64)

print("\n* * * Encryption * * *")
ciphertextBase64 = chacha2020Poly1305EncryptToBase64(encryptionKey, plaintext)
print("ciphertext: " + ciphertextBase64)
print("output is (Base64) nonce : (Base64) ciphertext : (Base64) poly1305Tag")

print("\n* * * Decryption * * *")
ciphertextDecryptionBase64 = ciphertextBase64
decryptionKeyBase64 = encryptionKeyBase64
decryptionKey = base64Decoding(decryptionKeyBase64)
print("decryptionKey (Base64): " + decryptionKeyBase64)
print("ciphertext (Base64): " + ciphertextDecryptionBase64)
print("input is (Base64) nonce : (Base64) ciphertext : (Base64) poly1305Tag")
decryptedtext = chacha2020Poly1305DecryptFromBase64(decryptionKey, ciphertextBase64)
print("plaintext:  " + decryptedtext)
