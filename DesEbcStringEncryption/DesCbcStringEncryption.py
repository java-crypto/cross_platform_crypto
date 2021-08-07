from Crypto.Cipher import DES
from Crypto.Cipher import DES3
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

def generateRandomDesKey():
  return get_random_bytes(8) # 8 bytes for des, 16 bytes for TDES 2 and 24 bytes for TDES 3

def desEcbEncryptToBase64(encryptionKey, plaintext):
  cipher = DES.new(encryptionKey, DES.MODE_ECB)
  ciphertext = cipher.encrypt(pad(plaintext.encode("ascii"),8))
  ciphertextBase64 = base64Encoding(ciphertext)
  return ciphertextBase64

def desEcbDecryptFromBase64(encryptionKey, ciphertextDecryptionBase64):
  ciphertext = base64Decoding(ciphertextDecryptionBase64)
  cipher = DES.new(encryptionKey, DES.MODE_ECB)
  decryptedtext = cipher.decrypt(ciphertext)
  decryptedtextUnpad = unpad(decryptedtext, 8)
  decryptedtextP = decryptedtextUnpad.decode("UTF-8")
  return decryptedtextP

def des3EcbEncryptToBase64(encryptionKey, plaintext):
  cipher = DES3.new(encryptionKey, DES.MODE_ECB)
  ciphertext = cipher.encrypt(pad(plaintext.encode("ascii"),8))
  ciphertextBase64 = base64Encoding(ciphertext)
  return ciphertextBase64

def des3EcbDecryptFromBase64(encryptionKey, ciphertextDecryptionBase64):
  ciphertext = base64Decoding(ciphertextDecryptionBase64)
  cipher = DES3.new(encryptionKey, DES.MODE_ECB)
  decryptedtext = cipher.decrypt(ciphertext)
  decryptedtextUnpad = unpad(decryptedtext, 8)
  decryptedtextP = decryptedtextUnpad.decode("UTF-8")
  return decryptedtextP  
  
print("\n# # # SECURITY WARNING: This code is provided for achieve    # # #")
print("# # # compatibility between different programming languages. # # #")
print("# # # It is not necessarily fully secure.                    # # #")
print("# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #")
print("# # # It uses FIXED key that should NEVER used,              # # #")
print("# # # instead use random generated key.                      # # #")
print("# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #")

plaintext = "The quick brown fox jumps over the lazy dog"
print("plaintext: " + plaintext)

print("\nDES EBC String encryption with fixed key")

desEncryptionKey = "12345678".encode("ascii")
print("desEncryptionKey (Base64): " +  base64Encoding(desEncryptionKey))

print("\n* * * Encryption * * *")
desCiphertextBase64 = desEcbEncryptToBase64(desEncryptionKey, plaintext)
print("ciphertext: " + desCiphertextBase64)
print("output is (Base64) ciphertext")
print("\n* * * Decryption * * *")
print("ciphertext: " + desCiphertextBase64)
print("input is (Base64) ciphertext")
desDecryptedtext = desEcbDecryptFromBase64(desEncryptionKey, desCiphertextBase64)
print("plaintext:  " + desDecryptedtext)

print("\nTriple DES ECB String encryption with fixed key (16 bytes)")
des2EncryptionKey = "1234567890123456".encode("ascii")
print("des2EncryptionKey (Base64): " +  base64Encoding(des2EncryptionKey))

print("\n* * * Encryption * * *")
des2CiphertextBase64 = des3EcbEncryptToBase64(des2EncryptionKey, plaintext)
print("ciphertext: " + des2CiphertextBase64)
print("output is (Base64) ciphertext")
print("\n* * * Decryption * * *")
print("ciphertext: " + des2CiphertextBase64)
print("input is (Base64) ciphertext")
des2Decryptedtext = des3EcbDecryptFromBase64(des2EncryptionKey, des2CiphertextBase64)
print("plaintext:  " + des2Decryptedtext)

print("\nTriple DES ECB String encryption with fixed key (24 bytes)")
des3EncryptionKey = "123456789012345678901234".encode("ascii")
print("des3EncryptionKey (Base64): " +  base64Encoding(des3EncryptionKey))

print("\n* * * Encryption * * *")
des3CiphertextBase64 = des3EcbEncryptToBase64(des3EncryptionKey, plaintext)
print("ciphertext: " + des3CiphertextBase64)
print("output is (Base64) ciphertext")
print("\n* * * Decryption * * *")
print("ciphertext: " + des3CiphertextBase64)
print("input is (Base64) ciphertext")
des3Decryptedtext = des3EcbDecryptFromBase64(des3EncryptionKey, des3CiphertextBase64)
print("plaintext:  " + des3Decryptedtext)
