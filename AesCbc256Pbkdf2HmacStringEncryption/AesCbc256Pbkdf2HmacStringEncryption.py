from Crypto.Protocol.KDF import PBKDF2
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Hash import SHA256
from Crypto.Util.Padding import pad
from Crypto.Util.Padding import unpad
from Crypto.Hash import HMAC, SHA256
import base64

def base64Encoding(input):
  dataBase64 = base64.b64encode(input)
  dataBase64P = dataBase64.decode("UTF-8")
  return dataBase64P

def base64Decoding(input):
    return base64.decodebytes(input.encode("ascii"))

def hmac256Calculation(keyHmac, data):
  h = HMAC.new(keyHmac, digestmod=SHA256)
  h.update(data.encode("ascii"))
  return h.digest()

def generateSalt32Byte():
  return get_random_bytes(32)

def aesCbcPbkdf2EncryptToBase64(password, plaintext):
  passwordBytes = password.encode("ascii")
  salt = generateSalt32Byte()
  PBKDF2_ITERATIONS = 15000
  encryptionKey = PBKDF2(passwordBytes, salt, 64, count=PBKDF2_ITERATIONS, hmac_hash_module=SHA256) # 2 * 32 for aes + hmac
  keyAes = encryptionKey[32:]
  keyHmac = encryptionKey[:32]
  cipher = AES.new(keyAes, AES.MODE_CBC)
  ciphertext = cipher.encrypt(pad(plaintext.encode("ascii"), AES.block_size))
  ivBase64 = base64Encoding(cipher.iv)
  saltBase64 = base64Encoding(salt)
  ciphertextBase64 = base64Encoding(ciphertext)
  ciphertextWithoutHmac = saltBase64 + ":" + ivBase64 + ":" + ciphertextBase64
  hmacBase64 = base64Encoding(hmac256Calculation(keyHmac, ciphertextWithoutHmac))
  return saltBase64 + ":" + ivBase64 + ":" + ciphertextBase64 + ":" + hmacBase64

def aesCbcPbkdf2DecryptFromBase64(password, ciphertextBase64):
  passwordBytes = password.encode("ascii")
  data = ciphertextDecryptionBase64.split(":")
  saltBase64 = data[0]
  ivBase64 = data[1]
  ciphertextBase64 = data[2]
  salt = base64Decoding(saltBase64)
  iv = base64Decoding(ivBase64)
  ciphertext = base64Decoding(ciphertextBase64)
  hmacReceived = base64Decoding(data[3])

  PBKDF2_ITERATIONS = 15000
  decryptionKey = PBKDF2(passwordBytes, salt, 64, count=PBKDF2_ITERATIONS, hmac_hash_module=SHA256) # 2 * 32 for aes + hmac
  keyAes = decryptionKey[32:]
  keyHmac = decryptionKey[:32]
  # calculate the hmac over salt, iv and cyphertext
  ciphertextWithoutHmac = saltBase64 + ":" + ivBase64 + ":" + ciphertextBase64
  hmacDecryption = hmac256Calculation(keyHmac, ciphertextWithoutHmac)
  # compare received hmac and calculated hmac
  if (hmacReceived != hmacDecryption):
    print("Error: HMAC-check failed, no decryption possible")
    return ""
  cipher = AES.new(keyAes, AES.MODE_CBC, iv)
  decryptedtext = unpad(cipher.decrypt(ciphertext), AES.block_size)
  decryptedtextP = decryptedtext.decode("UTF-8")
  return decryptedtextP

print("AES CBC 256 String encryption with PBKDF2 derived key and HMAC check")

plaintext = "The quick brown fox jumps over the lazy dog"
print("plaintext: " + plaintext)
password = "secret password"

print("\n* * * Encryption * * *")
ciphertextBase64 = aesCbcPbkdf2EncryptToBase64(password, plaintext)
print("ciphertext: " + ciphertextBase64)
print("output is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac")

print("\n* * * Decryption * * *")
ciphertextDecryptionBase64 = ciphertextBase64
print("ciphertext (Base64): " + ciphertextDecryptionBase64)
print("input is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac")
decryptedtext = aesCbcPbkdf2DecryptFromBase64(password, ciphertextBase64)
print("plaintext:  " + decryptedtext)
