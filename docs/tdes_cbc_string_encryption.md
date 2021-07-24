# Cross-platform cryptography

## Triple DES CBC mode string encryption

Before **AES** was selected as "standard encryption algorithm" everyone used **DES** ("Data Encryption Standard") as "secure encryption algorithm". Although never confirmed it should be named as **broken** and should be nevermore in use, but up to now there are still questions about "migration" of DES functions.

This article is about a variant of the original DES algorithm called **Triple DES algorithm**. It's so called because it applies the DES cipher algorithm three times to each data block. The key length is of 3 * 8 = 24 bytes in total.

Let's concentrate on the implementation details - there are 3 main parameters that need to be equal when crossing the platforms to decrypt the ciphertext.

**First parameter: the length of the key**

A TDES key has a key length of 192 bits = 24 byte (so called "3 keys DES"). There is a variant available with a key length of 16 byte and it is called "2 keys DES" but the key is enhanced to 24 bytes this way:

```plaintext
16 bytes key: 12345678abcdefgh
enhancement takes the 8 starting bytes and appends it to the end
24 bytes key: 12345678abcdefgh12345678
```

**Second parameter: the mode of operation**

There are several DES modes defined and here we are using the most common one - the CBC mode (Cipher Block Chaining mode). 

**Third parameter: the padding of the data**

The used CBC mode is a block algorithm means that the encryption (and decryption) is done in blocks of 8 bytes of data. There are only a few scenarios where you have to handle exact 8 bytes of data - in all other cases you have to fill up the plaintext to a length of multiple of 8. There are some padding algorithms available and our programs will use the PKCS#5 or PKCS#7 padding (Java names this padding PKCS#5, most other languages use PKCS#7).

### Is this encryption secure?
The answer is simple - **DES encryption is BROKEN and UNSECURE**, but the longer TDES algorithm seems to be unbroken, but please do not use DES nor TDES any longer.

### Is there a less secure version of TDES available?

Beneath of changing the algorithm to e.g. AES or Chacha20, there is a variant of TDES (the "basic one") available called **DES**, see my separate article [DES CBC mode string encryption](des_cbc_string_encryption.md).

### steps in the program

The program follows the usual sequence:
1. for reasons of comparison it uses a fixed 8 characters/bytes long key (in production generate a random encryption key) and show the key in Base64 encoding for later usage
2. convert the plaintext to a binary format (e.g. a byte array)
3. starts the encryption process
4. again for reasons of comparison it uses a fixed IV (generate a random initialization vector in production)
5. set the encryption parameters
5b. Note: some frameworks accept a 16 byte long key and enhance them automatically (like e.g. Python), other frameworks need a dedicated enhancement function (like e.g. Java)
6. encrypt the plaintext, prepends the iv to the ciphertext and show the result ( iv:ciphertext) in Base64 encoding
7. start the decryption process
8. Base64 decoding of the encryption key and the ciphertext
9. split the complete ciphertext-string into iv and ciphertext
10. set the decryption parameters (same as used for encryption)
11. decrypt the ciphertext and show the resulting plaintext

### **Serious notice: although the program looks like simple there is NO CHANCE for recovering the original plaintext without the key used for encryption! (except you try all possible keys = brute forcing).**

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compiler that runs the code. **The codes contain DES and TDES algorithms with CBC mode**.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../DesCbcStringEncryption/DesCbcStringEncryption.java) | :white_check_mark: | [replit.com CpcJavaDesCbcStringEncryption](https://replit.com/@javacrypto/CpcJavaDesCbcStringEncryption#Main.java/)
| [PHP](../DesCbcStringEncryption/DesCbcStringEncryption.php) | :white_check_mark: | [replit.com  CpcPhpDesCbcStringEncryption](https://replit.com/@javacrypto/CpcPhpDesCbcStringEncryption#main.php/)
| [C#](../DesCbcStringEncryption/DesCbcStringEncryption.cs) | :white_check_mark: | [replit.com CpcCsharpDesCbcStringEncryption](https://replit.com/@javacrypto/CpcCsharpDesCbcStringEncryption#main.cs/)
| [Javascript CryptoJs](../DesCbcStringEncryption/DesCbcStringEncryptionCryptoJs.js) | :white_check_mark: | [replit.com CpcCryptoJsDesCbcStringEncryption](https://replit.com/@javacrypto/CpcCryptoJsDesCbcStringEncryption#index.js/)
| [NodeJS Crypto](../DesCbcStringEncryption/DesCbcStringEncryptionNodeJsCrypto.js) | :white_check_mark: | [replit.com CpcNodeJsCryptoDesCbcStringEncryption](https://replit.com/@javacrypto/CpcNodeJsCryptoDesCbcStringEncryption#index.js/)
| [NodeJS node-forge](../DesCbcStringEncryption/DesCbcStringEncryptionNodeJs.js) | :white_check_mark: | [replit.com CpcNodeJsDesCbcStringEncryption](https://replit.com/@javacrypto/CpcNodeJsDesCbcStringEncryption#index.js/)
| [Python](../DesCbcStringEncryption/DesCbcStringEncryption.py) *1) | :white_check_mark: | [replit.com CpcPythonDesCbcStringEncryption](https://replit.com/@javacrypto/CpcPythonDesCbcStringEncryption#main.py/)
| [Go](../DesCbcStringEncryption/DesCbcStringEncryption.go) | :white_check_mark: | [replit.com CpcGoDesCbcStringEncryption](https://replit.com/@javacrypto/CpcGoDesCbcStringEncryption#main.go/)
| [Dart](../DesCbcStringEncryption/DesCbcStringEncryption.dart) *2) | :white_check_mark: | no online compiler available

*1) you need the external library pycryptodome, version 3.9.9

*2) you need the external library dart_des, version 1.0.0

This is an output (as I'm using a fixed key and iv the output will be equal):

```plaintext
# # # SECURITY WARNING: This code is provided for achieve    # # #
# # # compatibility between different programming languages. # # #
# # # It is not necessarily fully secure.                    # # #
# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #
# # # It uses FIXED key and IV's that should NEVER used,     # # #
# # # instead use random generated keys and IVs.             # # #
# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #

plaintext:  The quick brown fox jumps over the lazy dog
DES CBC String encryption with fixed key & iv
desEncryptionKey (Base64): MTIzNDU2Nzg=

* * * Encryption * * *
ciphertext: MTIzNDU2Nzg=:o2KnpBGw3w8QXBeNFGQB8JKsTEOhXm9d58IbXw6guWVZbhUnG4WcUEQpGbIah9wy
output is (Base64) iv : (Base64) ciphertext

* * * Decryption * * *
ciphertext (Base64): MTIzNDU2Nzg=:o2KnpBGw3w8QXBeNFGQB8JKsTEOhXm9d58IbXw6guWVZbhUnG4WcUEQpGbIah9wy
input is (Base64) iv : (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog

Triple DES CBC String encryption with fixed key (16 bytes) & iv
des2EncryptionKey (Base64): MTIzNDU2Nzg5MDEyMzQ1Ng==

* * * Encryption * * *
ciphertext: MTIzNDU2Nzg=:wWF78iQlYiesXpnjwFR3g+/RXO/KwG9MjZpc/WFAkX+QeD+iMNIpe2leqm2FdDYz
output is (Base64) iv : (Base64) ciphertext

* * * Decryption * * *
ciphertext (Base64): MTIzNDU2Nzg=:wWF78iQlYiesXpnjwFR3g+/RXO/KwG9MjZpc/WFAkX+QeD+iMNIpe2leqm2FdDYz
input is (Base64) iv : (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog

Triple DES CBC String encryption with fixed key (24 bytes) & iv
des3EncryptionKey (Base64): MTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0

* * * Encryption * * *
ciphertext: MTIzNDU2Nzg=:KNCFU2KIWNTgds6F01lAHr64m3OQxncUme+81dWu9WWfVoCaPIc4GpGZuO7xvR+g
output is (Base64) iv : (Base64) ciphertext

* * * Decryption * * *
ciphertext (Base64): MTIzNDU2Nzg=:KNCFU2KIWNTgds6F01lAHr64m3OQxncUme+81dWu9WWfVoCaPIc4GpGZuO7xvR+g
input is (Base64) iv : (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog
```

Last update: Jul. 24rd 2021

Back to the main page: [readme.md](../readme.md)