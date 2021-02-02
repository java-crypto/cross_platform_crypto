from Crypto.Hash import SHA256
from Crypto.PublicKey import ECC
from Crypto.Signature import DSS
import base64

def base64Encoding(input):
  dataBase64 = base64.b64encode(input)
  dataBase64P = dataBase64.decode("UTF-8")
  return dataBase64P

def base64Decoding(input):
    return base64.decodebytes(input.encode("ascii"))

def ecSignToBase64(privateKeyPem, message):
  privateKeyDer = base64Decoding(privateKeyPem)
  ecPrivateKey = ECC.import_key(privateKeyDer)
  hashMessage = SHA256.new(message.encode("ascii"))
  signer = DSS.new(ecPrivateKey, 'fips-186-3', 'binary')
  signature = signer.sign(hashMessage)
  return base64Encoding(signature)

def ecVerifySignatureFromBase64(publicKeyPem, message, signatureBase64):
  ecPublicKey = ECC.import_key(publicKeyPem)
  signatureBytes = base64Decoding(signatureBase64)
  hashMessage = SHA256.new(message.encode("ascii"))
  verifier = DSS.new(ecPublicKey, 'fips-186-3', 'binary')
  try:
    verifier.verify(hashMessage, signatureBytes)
    print("The message is authentic.")
    return True
  except ValueError:
    print("The message is not authentic.")
    return False


def loadEcPrivateKeyPem():
  return """-----BEGIN PRIVATE KEY-----
MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCAU2f8tzo99Z1HoxJlY
96yXUhFY5vppVjw1iPKRfk1wHA==
-----END PRIVATE KEY-----
"""

def loadEcPublicKeyPem():
  return """-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEzb7yAFWup6iDqJiEq764rAumsV2M
rspZxaP3WGpwHaC4Uff3N4UbJZF7Zac1c6W7KJl0eeCP0205Q3UEpwxndQ==
-----END PUBLIC KEY-----"""

def loadEcPrivateKeyDerBase64():
  return """MDECAQEEIBTZ/y3Oj31nUejEmVj3rJdSEVjm+mlWPDWI8pF+TXAcoAoGCCqGSM49AwEH"""

print("EC signature string (ECDSA with SHA256)")

dataToSign = "The quick brown fox jumps over the lazy dog"
print("dataToSign: " + dataToSign)

print("\n* * * sign the plaintext with the EC private key * * *")
privateEcKeyPem = loadEcPrivateKeyDerBase64()
print("used private key:\n" + privateEcKeyPem)
signatureBase64 = ecSignToBase64(privateEcKeyPem, dataToSign)
print("\nsignature (Base64): " + signatureBase64)

print("\n* * * verify the signature against the plaintext with the EC public key * * *")
publicEcKeyPem = loadEcPublicKeyPem()
print("used public key:\n" + publicEcKeyPem)
signatureVerified = ecVerifySignatureFromBase64(publicEcKeyPem, dataToSign, signatureBase64)
print("\nsignature (Base64) verified: ")
print(signatureVerified)
