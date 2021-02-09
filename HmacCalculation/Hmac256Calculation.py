from Crypto.Hash import HMAC, SHA256
import base64

def hmac256Calculation(keyHmac, data):
  h = HMAC.new(keyHmac.encode("ascii"), digestmod=SHA256)
  h.update(data.encode("ascii"))
  return h.digest()

def base64Encoding(input):
  dataBase64 = base64.b64encode(input)
  dataBase64P = dataBase64.decode("UTF-8")
  return dataBase64P

print("HMAC 256 calculation")

hmac256KeyString = "hmac256ForAesEncryption"
plaintext = "The quick brown fox jumps over the lazy dog"
print ("hmac256Key: " + hmac256KeyString)
print("plaintext: " + plaintext)
hmac256 = hmac256Calculation(hmac256KeyString, plaintext)
hmacBase64 = base64Encoding(hmac256)
print ("hmac256 length: " + str(len(hmac256)) + " (Base64) data: " + base64Encoding(hmac256))
