from Crypto.Hash import SHA256
import base64

def base64Encoding(input):
  dataBase64 = base64.b64encode(input)
  dataBase64P = dataBase64.decode("UTF-8")
  return dataBase64P

def base64Decoding(input):
    return base64.decodebytes(input.encode("ascii"))

def calculateSha256(input):
  h = SHA256.new()
  h.update(input.encode("ascii"))
  #return h.hexdigest()
  return h.digest()

def bytesToHex(input):
  return input.hex()

def hexStringToByteArray(input):
  return bytes.fromhex(input)

print("Generate a SHA-256 hash, Base64 en- and decoding and hex conversions")

plaintext = "The quick brown fox jumps over the lazy dog"
print("plaintext:               " +  plaintext)

sha256Value = calculateSha256(plaintext)
print("sha256Value (hex) length: " + str(len(sha256Value)) + " data: " + bytesToHex(sha256Value))

sha256Base64 = base64Encoding(sha256Value)
print("sha256Value (base64):     " + sha256Base64)

sha256ValueDecoded = base64Decoding(sha256Base64)
print("sha256Base64 decoded to a byte array:")
print("sha256Value (hex) length: " + str(len(sha256ValueDecoded)) + " data: " + bytesToHex(sha256ValueDecoded))

sha256HexString = "d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592"
sha256Hex = hexStringToByteArray(sha256HexString)
print("sha256HexString converted to a byte array:")
print("sha256Value (hex) length: " + str(len(sha256Hex)) + " data: " + bytesToHex(sha256Hex))
