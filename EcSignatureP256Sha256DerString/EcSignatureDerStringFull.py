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
  ecPrivateKey = ECC.import_key(privateKeyPem)
  hashMessage = SHA256.new(message.encode("ascii"))
  signer = DSS.new(ecPrivateKey, 'fips-186-3', 'der')
  signature = signer.sign(hashMessage)
  return base64Encoding(signature)

def ecVerifySignatureFromBase64(publicKeyPem, message, signatureBase64):
  ecPublicKey = ECC.import_key(publicKeyPem)
  signatureBytes = base64Decoding(signatureBase64)
  hashMessage = SHA256.new(message.encode("ascii"))
  verifier = DSS.new(ecPublicKey, 'fips-186-3', 'der')
  try:
    verifier.verify(hashMessage, signatureBytes)
    return True
  except ValueError:
    return False

def loadEcPrivateKeyPem():
  return """-----BEGIN PRIVATE KEY-----
MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgciPt/gulzw7/Xe12
YOu/vLUgIUZ+7gGo5VkmU0B+gUWhRANCAAQxkee3UPW110s0aUQdcS0TDkr8blAe
SBouL4hXziiJX5Me/8OobFgNfYXkk6R/K/fqJhJ/mV8gLur16XhgueXA
-----END PRIVATE KEY-----
"""

def loadEcPublicKeyPem():
  return """-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEMZHnt1D1tddLNGlEHXEtEw5K/G5Q
HkgaLi+IV84oiV+THv/DqGxYDX2F5JOkfyv36iYSf5lfIC7q9el4YLnlwA==
-----END PUBLIC KEY-----"""

print("EC signature string (ECDSA with SHA256) DER-encoding")

dataToSign = "The quick brown fox jumps over the lazy dog"
print("dataToSign: " + dataToSign)

print("\n* * * sign the plaintext with the EC private key * * *")
privateEcKeyPem = loadEcPrivateKeyPem()
print("used private key:\n" + privateEcKeyPem)
signatureBase64 = ecSignToBase64(privateEcKeyPem, dataToSign)
print("signature (Base64): " + signatureBase64)

print("\n* * * verify the signature against the plaintext with the EC public key * * *")
publicEcKeyPem = loadEcPublicKeyPem()
print("used public key:\n" + publicEcKeyPem)
signatureVerified = ecVerifySignatureFromBase64(publicEcKeyPem, dataToSign, signatureBase64)
print("\nsignature (Base64) verified: ")
print(signatureVerified)
