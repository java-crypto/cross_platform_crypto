from Crypto.Random import get_random_bytes
import base64

def base64Encoding(input):
  dataBase64 = base64.b64encode(input)
  dataBase64P = dataBase64.decode("UTF-8")
  return dataBase64P

def generateRandomKey32Byte():
  return get_random_bytes(32)

def generateRandomIv():  
  return get_random_bytes(16)

def generateRandomNonce():  
  return get_random_bytes(12)

print("Generate a 32 byte long key, IV (16 byte), nonce (12 byte)")

aesKey = generateRandomKey32Byte()
aesKeyBase64 = base64Encoding(aesKey)
print("generated key length:    " + str(len(aesKey)) + " data: " + aesKeyBase64)

iv = generateRandomIv()
ivBase64 = base64Encoding(iv)
print("generated iv length:     " + str(len(iv)) + " data: " + ivBase64)

nonce = generateRandomNonce()
nonceBase64 = base64Encoding(nonce)
print("generated nonce length:  " + str(len(nonce)) + " data: " + nonceBase64)
