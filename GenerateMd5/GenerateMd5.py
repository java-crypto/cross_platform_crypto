from Crypto.Hash import MD5
import base64

def base64Encoding(input):
  dataBase64 = base64.b64encode(input)
  dataBase64P = dataBase64.decode("UTF-8")
  return dataBase64P

def base64Decoding(input):
    return base64.decodebytes(input.encode("ascii"))

def calculateMd5(input):
  h = MD5.new()
  h.update(input.encode("ascii"))
  #return h.hexdigest()
  return h.digest()

def bytesToHex(input):
  return input.hex()

def hexStringToByteArray(input):
  return bytes.fromhex(input)

print("Generate a MD5 hash")

print("\n# # # SECURITY WARNING: This code is provided for achieve    # # #")
print("# # # compatibility between different programming languages. # # #")
print("# # # It is NOT SECURE - DO NOT USE THIS CODE ANY LONGER !   # # #")
print("# # # The hash algorithm MD5 is BROKEN.                      # # #")
print("# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n")

plaintext = "The quick brown fox jumps over the lazy dog"
print("plaintext:               " +  plaintext)

md5Value = calculateMd5(plaintext)
print("md5Value (hex) length: " + str(len(md5Value)) + " data: " + bytesToHex(md5Value))
