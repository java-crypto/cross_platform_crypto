import nacl.utils
from nacl.public import PrivateKey, SealedBox

# https://github.com/pyca/pynacl/
# PyNaCl version 1.4.0
# https://pynacl.readthedocs.io/en/latest/

def sealedCryptoboxEncryptionToBase64(publicKey, plaintext):
  pk = nacl.public.PublicKey(publicKey)
  box = SealedBox(pk)
  encrypted = box.encrypt(plaintext.encode("ascii"))
  return base64Encoding(encrypted)

def sealedCryptoboxDecryptionFromBase64(privateKey,  ciphertextDecryptionBase64):
  ciphertext = base64Decoding(ciphertextBase64)
  sk = nacl.public.PrivateKey(privateKey)
  box = SealedBox(sk)
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

print("Libsodium sealed crypto box hybrid string encryption")

plaintext = "The quick brown fox jumps over the lazy dog"
print("plaintext: " + plaintext)

# encryption
print("\n* * * encryption * * *")
print("all data are in Base64 encoding")

# for encryption you need the public key from other party (publicKeyB)
publicKeyBBase64 = "jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg="
# convert the base64 encoded keys
publicKeyB = base64Decoding(publicKeyBBase64)
ciphertextBase64 = sealedCryptoboxEncryptionToBase64(publicKeyB, plaintext)
print("publicKeyB:  " + publicKeyBBase64)
print("ciphertext:  " + ciphertextBase64)
print("output is (Base64) ciphertext")

print("\n* * * decryption * * *");
# received ciphertext
ciphertextReceivedBase64 = ciphertextBase64;
# for decryption you need your private key (privateKeyB)
privateKeyBBase64 = "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM="
privateKeyB = nacl.encoding.Base64Encoder.decode(privateKeyBBase64)

decryptedtext = sealedCryptoboxDecryptionFromBase64(privateKeyB, ciphertextReceivedBase64);
print("ciphertext:  " + ciphertextReceivedBase64);
print("input is (Base64) ciphertext");
print("privateKeyB: " + privateKeyBBase64);
print("decrypt.text:" + decryptedtext);
