# Cross-platform cryptography

## DES ECB mode string encryption

Before **AES** was selected as "standard encryption algorithm" everyone used **DES** ("Data Encryption Standard") as "secure encryption algorithm". Although never confirmed it should be named as **broken** and should be nevermore in use, but up to now there are still questions about "migration" of DES functions.

Let's concentrate on the implementation details - there are 3 main parameters that need to be equal when crossing the platforms to decrypt the ciphertext.

**First parameter: the length of the key**

A DES key has just one "allowed" key length: 64 bits = 8 byte. There is a variant of DES called **TripleDES** still in use, but that's another [article](tdes_ecc_string_encryption.md). The main problem is that on each oktet (8 bit) ONE byte is reserved for a parity check, so in the end the real key length is only 56 bit = 7 byte and that's much too small for todays computing power.

**Second parameter: the mode of operation**

There are several DES modes defined and here we are using a common one - the **ECB mode** (Electronic Code Book mode). Another common mode is the **CBC mode** [which is **UNSECURE** as well], a sample implementation can be found here [DES CBC mode string encryption](des_cbc_string_encryption.md)

**Third parameter: the padding of the data**

The used ECB mode is a block algorithm means that the encryption (and decryption) is done in blocks of 8 bytes of data. There are only a few scenarios where you have to handle exact 8 bytes of data - in all other cases you have to fill up the plaintext to a length of multiple of 8. There are some padding algorithms available and our programs will use the PKCS#5 or PKCS#7 padding (Java names this padding PKCS#5, most other languages use PKCS#7).

### Is this encryption secure?
The answer is simple - **DES encryption is BROKEN and UNSECURE**, please do not use it any longer.

### Is there a more secure version of DES available?

Beneath of changing the algorithm to e.g. AES or Chacha20, there is a variant of DES still in use that is named **TripleDES** or short **TDES** and it is named as unbroken (I heared from Verizone that they still encrypt credit card data for payments with TDES). For an implementation of TDES see my separate article [TripleDES ECB mode string encryption](tdes_ecb_string_encryption.md).

### steps in the program

The program follows the usual sequence:
1. for reasons of comparison it uses a fixed 8 characters/bytes long key (in production generate a random encryption key) and show the key in Base64 encoding for later usage
2. convert the plaintext to a binary format (e.g. a byte array)
3. starts the encryption process
4. set the encryption parameters
5. encrypt the plaintext and show the result (ciphertext) in Base64 encoding
6. start the decryption process
7. Base64 decoding of the encryption key and the ciphertext
8. set the decryption parameters (same as used for encryption)
9. decrypt the ciphertext and show the resulting plaintext

### **Serious notice: although the program looks like simple there is NO CHANCE for recovering the original plaintext without the key used for encryption! (except you try all possible keys = brute forcing).**

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compiler that runs the code. **The codes contain DES and TDES algorithms with ECB mode**.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../DesEcbStringEncryption/DesEcbStringEncryption.java) | :white_check_mark: | [replit.com CpcJavaDesEcbStringEncryption](https://replit.com/@javacrypto/CpcJavaDesEcbStringEncryption#Main.java/)
| [PHP](../DesEcbStringEncryption/DesEcbStringEncryption.php) | :white_check_mark: | [replit.com  CpcPhpDesEcbStringEncryption](https://replit.com/@javacrypto/CpcPhpDesEcbStringEncryption#main.php/)
| [C#](../DesEcbStringEncryption/DesEcbStringEncryption.cs) | :white_check_mark: | [replit.com CpcCsharpDesEcbStringEncryption](https://replit.com/@javacrypto/CpcCsharpDesEcbStringEncryption#main.cs/)
| [Javascript CryptoJs](../DesEcbStringEncryption/DesEcbStringEncryptionCryptoJs.js) | :white_check_mark: | [replit.com CpcCryptoJsDesEcbStringEncryption](https://replit.com/@javacrypto/CpcCryptoJsDesEcbStringEncryption#index.js/)
| [NodeJS Crypto](../DesEcbStringEncryption/DesEcbStringEncryptionNodeJsCrypto.js) | :white_check_mark: | [replit.com CpcNodeJsCryptoDesEcbStringEncryption](https://replit.com/@javacrypto/CpcNodeJsCryptoDesEcbStringEncryption#index.js/)
| [NodeJS node-forge](../DesEcbStringEncryption/DesEcbStringEncryptionNodeJs.js) | :white_check_mark: | [replit.com CpcNodeJsDesEcbStringEncryption](https://replit.com/@javacrypto/CpcNodeJsDesEcbStringEncryption#index.js/)
| [Python](../DesEcbStringEncryption/DesEcbStringEncryption.py) *1) | :white_check_mark: | [replit.com CpcPythonDesEcbStringEncryption](https://replit.com/@javacrypto/CpcPythonDesEcbStringEncryption#main.py/)
| [Go](../DesEcbStringEncryption/DesEcbStringEncryption.go) | :white_check_mark: | [replit.com CpcGoDesEcbStringEncryption](https://replit.com/@javacrypto/CpcGoDesEcbStringEncryption#main.go/)
| [Dart](../DesEcbStringEncryption/DesEcbStringEncryption.dart) *2) | :white_check_mark: | no online compiler available

*1) you need the external library pycryptodome, version 3.9.9

*2) you need the external library dart_des, version 1.0.0

Note on some implementations: as the ECB mode should be avoided for security reasons some frameworks do not provide a cipher instantiation without an initialization vector (IV). Sometimes you will find the usage of an IV (e.g. the C# one) that is named as dummy - you have to provide somethings to run the code but the IV is not used.

This is an output (as I'm using a fixed key the output will be equal):

```plaintext
# # # SECURITY WARNING: This code is provided for achieve    # # #
# # # compatibility between different programming languages. # # #
# # # It is not necessarily fully secure.                    # # #
# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #
# # # It uses FIXED key that should NEVER used,              # # #
# # # instead use random generated keys.                     # # #
# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #

plaintext:  The quick brown fox jumps over the lazy dog

DES ECB String encryption with fixed key
desEncryptionKey (Base64): MTIzNDU2Nzg=

* * * Encryption * * *
ciphertext: XokzhoQlYFGG7ZfTNqdvr0QGMsFF24oSZ5v+vsPDNlPA+GbJ2peAY6s6292zd+MV
output is (Base64) ciphertext

* * * Decryption * * *
ciphertext (Base64): XokzhoQlYFGG7ZfTNqdvr0QGMsFF24oSZ5v+vsPDNlPA+GbJ2peAY6s6292zd+MV
input is (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog

Triple DES ECB String encryption with fixed key (16 bytes)
des2EncryptionKey (Base64): MTIzNDU2Nzg5MDEyMzQ1Ng==

* * * Encryption * * *
ciphertext: IcEIhBfEuD+9dLVoZ6hk4p3T0SONRedJqP009xKlM5m51ulyvH8P/99NiVL5LD9s
output is (Base64) ciphertext

* * * Decryption * * *
ciphertext (Base64): IcEIhBfEuD+9dLVoZ6hk4p3T0SONRedJqP009xKlM5m51ulyvH8P/99NiVL5LD9s
input is (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog

Triple DES ECB String encryption with fixed key (24 bytes)
des3EncryptionKey (Base64): MTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0

* * * Encryption * * *
ciphertext: Ad/BM+OfIMzndzKiu0WDjuQCAkjOiGa23U3jZO0GAt95CNiC1SIXcX9z+HVrEQHB
output is (Base64) ciphertext

* * * Decryption * * *
ciphertext (Base64): Ad/BM+OfIMzndzKiu0WDjuQCAkjOiGa23U3jZO0GAt95CNiC1SIXcX9z+HVrEQHB
input is (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog
```

Last update: Aug. 07th 2021

Back to the main page: [readme.md](../readme.md)