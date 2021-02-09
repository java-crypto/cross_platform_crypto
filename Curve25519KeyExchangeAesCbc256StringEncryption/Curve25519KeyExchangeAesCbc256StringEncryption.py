from donna25519 import PrivateKey
from donna25519 import PublicKey
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad
from Crypto.Util.Padding import unpad
import base64
# uses https://github.com/Muterra/donna25519
# uses https://www.pycryptodome.org

def base64Encoding(input):
  dataBase64 = base64.b64encode(input)
  dataBase64P = dataBase64.decode("UTF-8")
  return dataBase64P

def base64Decoding(input):
    return base64.decodebytes(input.encode("ascii"))

def generateRandomAesKey():
  return get_random_bytes(32)

def curve25519GenerateKeyAgreement(privateKey, publicKey):
  exchangepartner = PrivateKey(privateKey)
  pubKey = PublicKey(publicKey) 
  return exchangepartner.do_exchange(pubKey)

def aesCbcEncryptToBase64(encryptionKey, plaintext):
  cipher = AES.new(encryptionKey, AES.MODE_CBC)
  ciphertext = cipher.encrypt(pad(plaintext.encode("ascii"), AES.block_size))
  ivBase64 = base64Encoding(cipher.iv)
  ciphertextBase64 = base64Encoding(ciphertext)
  return ivBase64 + ":" + ciphertextBase64

def aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64):
  data = ciphertextDecryptionBase64.split(":")
  iv = base64Decoding(data[0])
  ciphertext = base64Decoding(data[1])
  cipher = AES.new(decryptionKey, AES.MODE_CBC, iv)
  decryptedtext = unpad(cipher.decrypt(ciphertext), AES.block_size)
  decryptedtextP = decryptedtext.decode("UTF-8")
  return decryptedtextP
  
print("Curve25519 key exchange and AES CBC 256 string encryption")

plaintext = "The quick brown fox jumps over the lazy dog"
print("plaintext: " + plaintext)

# for encryption you need your private key (aPrivateKey) and the public key from other party (bPublicKey)
aPrivateKeyBase64 = "yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU="
bPublicKeyBase64  = "jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg="
# convert the base64 encoded keys
aPrivateKey = base64.b64decode(aPrivateKeyBase64)
bPublicKey = base64.b64decode(bPublicKeyBase64)
# generate shared key
aSharedKey = curve25519GenerateKeyAgreement(aPrivateKey, bPublicKey)

print("\n* * * Encryption * * *") 
ciphertextBase64 = aesCbcEncryptToBase64(aSharedKey, plaintext)
print("aPrivateKey: " + aPrivateKeyBase64)
print("bPublicKey:  " + bPublicKeyBase64)
print("aSharedKey:  " + base64Encoding(aSharedKey))
print("ciphertext: " + ciphertextBase64)
print("output is (Base64) iv : (Base64) ciphertext")

# for decryption you need your private key (bPrivateKey) and the public key from other party (aPublicKey)
print("\n* * * Decryption * * *") 
ciphertextDecryptionBase64 = ciphertextBase64
bPrivateKeyBase64 = "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM="
aPublicKeyBase64  = "b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0="
bPrivateKey = base64.b64decode(bPrivateKeyBase64)
aPublicKey = base64.b64decode(aPublicKeyBase64)
# generate shared key
bSharedKey = curve25519GenerateKeyAgreement(bPrivateKey, aPublicKey)
print("bPrivateKey: " + bPrivateKeyBase64)
print("aPublicKey:  " + aPublicKeyBase64)
print("bSharedKey:  " + base64Encoding(bSharedKey))
print("ciphertext (Base64): " + ciphertextDecryptionBase64)
print("input is (Base64) iv : (Base64) ciphertext")
decryptedtext = aesCbcDecryptFromBase64(bSharedKey, ciphertextBase64)
print("plaintext:  " + decryptedtext)
