import nacl.utils
from nacl.public import PrivateKey, Box

# https://github.com/pyca/pynacl/
# PyNaCl version 1.4.0
# https://pynacl.readthedocs.io/en/latest/

def cryptoboxEncryptionToBase64(privateKey, publicKey, plaintext):
  sk = nacl.public.PrivateKey(privateKey)
  pk = nacl.public.PublicKey(publicKey)
  box = Box(sk, pk)
  nonce = generateRandomNonce()
  encrypted = box.encrypt(plaintext.encode("ascii"), nonce)
  # output is (24 byte) nonce + ciphertext
  ciphertextBase64 = base64Encoding(encrypted)
  return insert_colon(ciphertextBase64, 32)

def cryptoboxDecryptionFromBase64(privateKey, publicKey, ciphertextDecryptionBase64):
  ciphertext = base64Decoding(remove_colon(ciphertextBase64))
  sk = nacl.public.PrivateKey(privateKey)
  pk = nacl.public.PublicKey(publicKey)
  box = Box(sk, pk)
  decryptedtext = box.decrypt(ciphertext)
  return decryptedtext.decode('utf-8')

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

print("Libsodium crypto box hybrid string encryption")

plaintext = "The quick brown fox jumps over the lazy dog"
print("plaintext: " + plaintext)

# encryption
print("\n* * * encryption * * *")
print("all data are in Base64 encoding")

# for encryption you need your private key (privateKeyA) and the public key from other party (publicKeyB)
privateKeyABase64 = "yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU="
publicKeyBBase64 = "jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg="
# convert the base64 encoded keys
privateKeyA = base64Decoding(privateKeyABase64)
publicKeyB = base64Decoding(publicKeyBBase64)
ciphertextBase64 = cryptoboxEncryptionToBase64(privateKeyA, publicKeyB, plaintext)
print("privateKeyA: " + privateKeyABase64)
print("publicKeyB:  " + publicKeyBBase64)
print("ciphertext:  " + ciphertextBase64)
print("output is  (Base64) nonce : (Base64) ciphertext")

print("\n* * * decryption * * *")
# received ciphertext
ciphertextReceivedBase64 = ciphertextBase64
# for decryption you need your private key (privateKeyB) and the public key from other party (publicKeyA)
privateKeyBBase64 = "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM="
publicKeyABase64 = "b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0="
privateKeyB = nacl.encoding.Base64Encoder.decode(privateKeyBBase64)
publicKeyA = nacl.encoding.Base64Encoder.decode(publicKeyABase64)

decryptedtext = cryptoboxDecryptionFromBase64(privateKeyB, publicKeyA, ciphertextReceivedBase64)
print("ciphertext:  " + ciphertextReceivedBase64)
print("input is (Base64) nonce : (Base64) ciphertext")
print("privateKeyB: " + privateKeyBBase64)
print("publicKeyA:  " + publicKeyABase64)
print("decrypt.text:" + decryptedtext)