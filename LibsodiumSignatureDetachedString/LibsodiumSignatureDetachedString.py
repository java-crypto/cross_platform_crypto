import nacl.utils
from nacl.encoding import Base64Encoder
from nacl.signing import SigningKey
from nacl.signing import VerifyKey
import nacl.bindings

# https://github.com/pyca/pynacl/
# PyNaCl version 1.4.0
# https://pynacl.readthedocs.io/en/latest/

def libsodiumSignDetachedToBase64(privateKey, dataToSign):
  # build signing_key from seed
  seed = nacl.bindings.crypto_sign_ed25519_sk_to_seed(privateKey)
  signing_key = SigningKey(seed)
  signed_b64 = signing_key.sign(dataToSign.encode("ascii"), encoder=Base64Encoder).signature
  return signed_b64.decode('utf-8')

def libsodiumVerifyDetachedFromBase64(publicKey, dataToSign, signatureBase64):
  verify_key = VerifyKey(publicKey)
  result = verify_key.verify(dataToSign.encode("ascii"), base64Decoding  (signatureBase64))
  if result == dataToSign.encode("ascii"):
    return "true"
  else:
    return "false"

def loadEd25519PrivateKey():
  return base64Decoding("""Gjho7dILimbXJY8RvmQZEBczDzCYbZonGt/e5QQ3Vro2sjTp95EMxOrgEaWiprPduR/KJFBRA+RlDDpkvmhVfw==""")

def loadEd25519PublicKey():
  return base64Decoding("""NrI06feRDMTq4BGloqaz3bkfyiRQUQPkZQw6ZL5oVX8=""")

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

print("Libsodium detached signature string")

dataToSign = "The quick brown fox jumps over the lazy dog"
print("dataToSign: " + dataToSign)

# usually we would load the private and public key from a file or keystore
# here we use hardcoded keys for demonstration - don't do this in real programs

print("\n* * * sign the plaintext with the ED25519 private key * * *")
print("all data are in Base64 encoding")
privateKey = loadEd25519PrivateKey()
signatureBase64 = libsodiumSignDetachedToBase64(privateKey, dataToSign)
print("signature (Base64): " + signatureBase64)

print("\n* * * verify the signature against the plaintext with the ED25519 public key * * *")
publicKey = loadEd25519PublicKey()
signatureVerified = libsodiumVerifyDetachedFromBase64(publicKey, dataToSign, signatureBase64)
print("signature (Base64): " + signatureBase64)
print("signature (Base64) verified: " + signatureVerified)
