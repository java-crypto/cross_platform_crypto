# https://github.com/tprest/falcon.py
# https://pycryptodome.readthedocs.io/en/latest/src/hash/shake256.html
import falcon


import base64
import pickle
import os

def base64Encoding(input):
  dataBase64 = base64.b64encode(input)
  dataBase64P = dataBase64.decode("UTF-8")
  return dataBase64P

def base64Decoding(input):
    return base64.decodebytes(input.encode("ascii"))

print ("PQC FALCON signature")

print ("\n************************************\n" +
  "* # # SERIOUS SECURITY WARNING # # *\n" +
  "* This program is a CONCEPT STUDY  *\n" +
  "* for the algorithm                *\n" +
  "* FALCON [signature]               *\n" +
  "* The program is using an OUTDATED *\n" +
  "* parameter set and I cannot       *\n" +
  "* check for the correctness of the *\n" +
  "* output and other details         *\n" +
  "*                                  *\n" +
  "*    DO NOT USE THE PROGRAM IN     *\n" +
  "*    ANY PRODUCTION ENVIRONMENT    *\n" +
  "************************************")

dataToSignString = "The quick brown fox jumps over the lazy dog"

# get private and public key
print("\nthe generation of the private key will take some seconds... please wait...")
# the generation of a 1024er key will take too long here
privateKey = falcon.SecretKey(512)
publicKey = falcon.PublicKey(privateKey)

# save the private key as dumped object for later usage
with open("privateKey.file", "wb") as f:
    pickle.dump(privateKey, f, pickle.HIGHEST_PROTOCOL)
# save the public key as dumped object
with open("publicKey.file", "wb") as f:
    pickle.dump(publicKey, f, pickle.HIGHEST_PROTOCOL)
print("generated private key length: ", end='')
print(os.path.getsize('privateKey.file'))
print("generated public key length: ", end='')
print(os.path.getsize('publicKey.file'))

# load the private and public key
with open("privateKey.file", "rb") as f:
    dump = pickle.load(f)
privateKeyLoad = dump
with open("publicKey.file", "rb") as f:
    dump = pickle.load(f)
publicKeyLoad = dump

print("\n* * * sign the dataToSign with the private key * * *")
#signature = privateKeyLoad.sign(b"Hello2")
signature = privateKeyLoad.sign(dataToSignString.encode("ascii"))
print("signature length: ", end='')
print(str(len(signature)))
signatureBase64 = base64Encoding(signature)
print("\nsignature (Base64): " + signatureBase64)

print("\n* * * verify the signature with the public key * * *")
signatureVerification = base64Decoding(signatureBase64)
signatureVerified = publicKeyLoad.verify(dataToSignString.encode("ascii"), signatureVerification)
print("signature verified: ", end="")
print(signatureVerified)
