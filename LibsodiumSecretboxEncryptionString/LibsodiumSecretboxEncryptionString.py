import nacl.utils
import nacl.secret

# https://github.com/pyca/pynacl/
# PyNaCl version 1.4.0
# https://pynacl.readthedocs.io/en/latest/

def secretboxEncryptionToBase64(keyBase64, plaintext):
  key = base64Decoding(keyBase64)
  box = nacl.secret.SecretBox(key)
  nonce = generateRandomNonce()
  encrypted = box.encrypt(plaintext.encode("ascii"), nonce, encoder=nacl.encoding.Base64Encoder).ciphertext
  nonceBase64 = base64Encoding(nonce)
  return nonceBase64 + ":" + encrypted.decode("UTF-8")

def secretboxDecryptionFromBase64(keyBase64, ciphertextDecryptionBase64):
  ciphertext = base64Decoding(remove_colon(ciphertextDecryptionBase64))
  key = base64Decoding(keyBase64)
  box = nacl.secret.SecretBox(key)
  decryptedtext = box.decrypt(ciphertext)
  return decryptedtext.decode('utf-8')

def generateRandomKey():
    return nacl.utils.random(32)

def generateRandomNonce():
    return nacl.utils.random(24)

def base64Encoding(input):
    dataBase64 = nacl.encoding.Base64Encoder.encode(input)
    dataBase64P = dataBase64.decode("UTF-8")
    return dataBase64P

def base64Decoding(input):
    return nacl.encoding.Base64Encoder.decode(input.encode("ascii"))

def insert_colon(string, index):
    return string[:index] + ':' + string[index:]

def remove_colon(string):
    return string.replace(':', '')

print("Libsodium secret box random key string encryption")

plaintext = "The quick brown fox jumps over the lazy dog"
print("plaintext: " + plaintext)

# generate random key
keyBase64 = base64Encoding(generateRandomKey())
print("encryptionKey (Base64): " + keyBase64)

# encryption
print("\n* * * encryption * * *")
print("all data are in Base64 encoding")
ciphertextBase64 = secretboxEncryptionToBase64(keyBase64, plaintext)
print("ciphertext:  " + ciphertextBase64)
print("output is  (Base64) nonce : (Base64) ciphertext")

print("\n* * * decryption * * *")
# received ciphertext
ciphertextReceivedBase64 = ciphertextBase64
decryptedtext = secretboxDecryptionFromBase64(keyBase64, ciphertextReceivedBase64)
print("ciphertext:  " + ciphertextReceivedBase64)
print("input is (Base64) nonce : (Base64) ciphertext")
print("decrypt.text:" + decryptedtext)
