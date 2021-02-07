import nacl.utils
from nacl.signing import SigningKey

# https://github.com/pyca/pynacl/
# PyNaCl version 1.4.0
# https://pynacl.readthedocs.io/en/latest/

def generateRandomEd25519PrivateKey():
  return nacl.utils.random(64)

def deriveEd25519PublicKey(privateKeyBase64):
  # build signing_key from seed
  privateKey = base64Decoding(privateKeyBase64)
  seed = nacl.bindings.crypto_sign_ed25519_sk_to_seed(privateKey)
  signing_key = SigningKey(seed)
  return signing_key.verify_key.__bytes__()

def base64Encoding(input):
    dataBase64 = nacl.encoding.Base64Encoder.encode(input)
    dataBase64P = dataBase64.decode("UTF-8")
    return dataBase64P

def base64Decoding(input):
    return nacl.encoding.Base64Encoder.decode(input.encode("ascii"))

print("Generate ED25519 private and public key and derive public key from private key")

privateKeyBase64 = base64Encoding(generateRandomEd25519PrivateKey())
publicKey = deriveEd25519PublicKey(privateKeyBase64)
print("privateKey (Base64): " + privateKeyBase64)
print("publicKey (Base64):  " + base64Encoding(publicKey))
