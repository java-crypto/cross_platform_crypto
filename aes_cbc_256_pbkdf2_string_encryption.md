# Cross-platform cryptography

## AES CBC mode 256 PBKDF2 string encryption

The standard encryption algorithm is **AES** after it got choosen and standardized by NIST (National Institute of Standards and Technology). There are 3 main parameters that need to be equal when crossing the platforms to decrypt the ciphertext. 

This version is the advanced version of [AES CBC mode 256 string encryption](aes_cbc_256_string_encryption.md) because it does not use a randomly generated AES key but a key derived from a password using the **PBKDF2** key derivation. More details about this can be found in my [PBKDF2 article](pbkdf2.md).

#### First parameter: the password or passphrase

This is a string (or char array in Java) and in a program it gets filled by a user input like "input passwort".

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

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_Full.java) | :white_check_mark: | [repl.it CpcJavaAesCbc256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcJavaAesCbc256Pbkdf2StringEncryptionFull#Main.java/)
| [PHP](AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_Full.php) | :white_check_mark: | [repl.it CpcPhpAesCbc256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcPhpAesCbc256Pbkdf2StringEncryptionFull/)
| [C#](AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_Full.cs) | :white_check_mark: | [repl.it CpcCsharpAesCbc256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcCsharpAesCbc256Pbkdf2StringEncryptionFull#main.cs/)
| [Javascript CryptoJs](AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_FullCryptoJs.js) | :white_check_mark: | [repl.it CpcCryptoJsAesCbc256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcCryptoJsAesCbc256Pbkdf2StringEncryptionFull#index.js/)
| [NodeJS Crypto](AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_FullNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoAesCbc256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcNodeJsCryptoAesCbc256Pbkdf2StringEncryptionFull#index.js/)
| [NodeJS node-forge](AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_FullNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsAesCbc256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcNodeJsAesCbc256Pbkdf2StringEncryptionFull#index.js/)
| [Webcrypto](aes_.html) | :white_check_mark: | [your browser](aes_.html)

This is an output (as there are random elements your output will differ):

```plaintext
AES CBC 256 String encryption with PBKDF2 derived key
plaintext: The quick brown fox jumps over the lazy dog

* * * Encryption * * *
ciphertext (Base64): zQAobC3IWzxIN4bVs936hM5Jc7uFQIzJ1lg4qtWPnXw=:ZZsPdYbPlI6nTF6iRgP5lg==:Tpski19GwNnAvB113A6yS+ty9R9LQdc8mKxiRvGD0/C0+l615bOj3tITAoGUkXOf
output is (Base64) salt : (Base64) iv : (Base64) ciphertext

* * * Decryption * * *
ciphertext (Base64): zQAobC3IWzxIN4bVs936hM5Jc7uFQIzJ1lg4qtWPnXw=:ZZsPdYbPlI6nTF6iRgP5lg==:Tpski19GwNnAvB113A6yS+ty9R9LQdc8mKxiRvGD0/C0+l615bOj3tITAoGUkXOf
input is (Base64) salt : (Base64) iv : (Base64) ciphertext
plaintext: The quick brown fox jumps over the lazy dog
```

Last update: Dec. 07th 2020

Back to the main page: [readme.md](readme.md)