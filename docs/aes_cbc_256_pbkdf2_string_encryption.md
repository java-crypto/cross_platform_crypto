# Cross-platform cryptography

## AES CBC mode 256 PBKDF2 string encryption

The standard encryption algorithm is **AES** after it got choosen and standardized by NIST (National Institute of Standards and Technology). There are 3 main parameters that need to be equal when crossing the platforms to decrypt the ciphertext. 

This version is the advanced version of [AES CBC mode 256 string encryption](aes_cbc_256_string_encryption.md) because it does not use a randomly generated AES key but a key derived from a password using the **PBKDF2** key derivation. More details about this can be found in my [PBKDF2 article](pbkdf2.md).

#### First parameter: the password or passphrase

This is a string (or char array in Java) and in a program it gets filled by a user input like "input password".

#### Second parameter: the data to encrypt (or decrypt)

This is the plaintext string (on encryption side) or ciphertext string (on decryption side). As we do need the same **salt** and the **initialization vector (iv)** on decryption side the ciphertext is a concationation of

```terminal
(Base64) salt : (Base64) iv : (Base64) ciphertext
```

All other parameters (number of PBKDF2 iterations, AES mode and padding) are hard-coded in the encryption and decryption functions.

### Is this encryption secure?
The answer is "it depends on...". The algorithm itself is secure but you should keep in mind that an attacker may been able to modify the ciphertext on transport. One example for this is the so called **tampering** - you can find a simple [example here](aes_cbc_tampering.md).

### steps in the program

The program follows the usual sequence:
1. input a password
2. convert the plaintext to a binary format (e.g. a byte array)
3. starts the encryption process
4. generate a random salt for PBKDF2
5. calculate the key with PBKDF2
6. generate a random initialization vector (iv)
7. set the encryption parameters
8. encrypt the plaintext, prepends the salt and iv to the ciphertext and show the result (salt:iv:ciphertext) in Base64 encoding
9. start the decryption process
10. split the complete ciphertext-string into salt, iv and ciphertext
11. Base64 decoding of the salt, iv and the ciphertext
12. calculate the key with PBKDF2
13. set the decryption parameters (same as used for encryption)
14. decrypt the ciphertext and show the resulting plaintext

If you like to see the **decryption part only** see my separate article [AES CBC mode 256 PBKDF2 string decryption only](aes_cbc_256_pbkdf2_string_decryption_only.md).

### **Serious notice: although the program looks like simple there is NO CHANCE for recovering the original plaintext without the password used for encryption!**

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_Full.java) | :white_check_mark: | [repl.it CpcJavaAesCbc256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcJavaAesCbc256Pbkdf2StringEncryptionFull#Main.java/)
| [PHP](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_Full.php) | :white_check_mark: | [repl.it CpcPhpAesCbc256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcPhpAesCbc256Pbkdf2StringEncryptionFull/)
| [C#](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_Full.cs) | :white_check_mark: | [repl.it CpcCsharpAesCbc256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcCsharpAesCbc256Pbkdf2StringEncryptionFull#main.cs/)
| [Javascript CryptoJs](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_FullCryptoJs.js) | :white_check_mark: | [repl.it CpcCryptoJsAesCbc256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcCryptoJsAesCbc256Pbkdf2StringEncryptionFull#index.js/)
| [NodeJS Crypto](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_FullNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoAesCbc256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcNodeJsCryptoAesCbc256Pbkdf2StringEncryptionFull#index.js/)
| [NodeJS node-forge](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_FullNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsAesCbc256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcNodeJsAesCbc256Pbkdf2StringEncryptionFull#index.js/)
| [Webcrypto](../AesCbc256Pbkdf2StringEncryption/aescbc256pbkdf2stringencryptionwebcrypto.html) | :white_check_mark: | [your browser AesCbc256Pbkdf2StringEncryptionWebcrypto.html](https://java-crypto.github.io/cross_platform_crypto/AesCbc256Pbkdf2StringEncryption/aescbc256pbkdf2stringencryptionwebcrypto.html)
| [Python](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_Full.py) *1) | :white_check_mark: | [repl.it CpcPythonAesCbc256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcPythonAesCbc256Pbkdf2StringEncryptionFull#main.py/)

*1) you need the external library pycryptodome, version 3.9.9

An important note about the WebCrypto-program: the program is of **very poor quality** and should be used for demonstration purpose only. It has a lot of unnecessary conversions - **never ever use it as basis for your own (business) programs**.

This is an output (as there are random elements your output will differ):

```plaintext
AES CBC 256 String encryption with PBKDF2 derived key
plaintext:  The quick brown fox jumps over the lazy dog

* * * Encryption * * *
ciphertext (Base64): mZa29HurA54RaYjXQ67UyFSAJXm5KPkXb720TERXvrQ=:jmmpanDGdObdsD163bxSnQ==:ZYgY4j8I1P3EL4lrbPnIPrUtijYr2uRac7nIvxFi7wx0oTJ3/SwHRsGfbYX1Ghsd
output is (Base64) salt : (Base64) iv : (Base64) ciphertext

* * * Decryption * * *
AES CBC 256 String decryption with PBKDF2 derived key
ciphertext (Base64): mZa29HurA54RaYjXQ67UyFSAJXm5KPkXb720TERXvrQ=:jmmpanDGdObdsD163bxSnQ==:ZYgY4j8I1P3EL4lrbPnIPrUtijYr2uRac7nIvxFi7wx0oTJ3/SwHRsGfbYX1Ghsd
input is (Base64) salt : (Base64) iv : (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog

```

This is the output of the (browser based) Webcrypto:

```plaintext
AES CBC 256 String encryption with PBKDF2 derived key

Important note: this program is doing what it promises but the programming itself is of very poor quality and for demonstration purposes only. Never ever use this program as source for your own programs because there are a lot of conversions to get it run.

Plaintext: The quick brown fox jumps over the lazy dog

Secret password: secret password

CiphertextCpc: 0hjPdiaEjzCYpYbfXWb47U9Yz2vcqTbo+2ixn6dYZxQ=:hx2yzgjkpB0oB+qj4HK1pg==:obDSutcfyv4mNn6SIpR/5cP+rccR0lEwi27lXxWDadOmfC51zi2fECXN3mjCb7pF

Ciphertext is (Base64) salt : (Base64) iv : (Base64) ciphertext

DecryptedtextCpc: The quick brown fox jumps over the lazy dog
```

Last update: Feb. 02nd 2021

Back to the main page: [readme.md](../readme.md)