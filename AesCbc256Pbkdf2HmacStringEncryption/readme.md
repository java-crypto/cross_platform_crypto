# Cross-platform cryptography

## AES CBC mode 256 PBKDF2 HMAC string encryption

The standard encryption algorithm is **AES** after it got choosen and standardized by NIST (National Institute of Standards and Technology). There are 3 main parameters that need to be equal when crossing the platforms to decrypt the ciphertext. 

This version is the advanced version of [AES CBC mode 256 PBKDF2 string encryption](aes_cbc_256_pbkdf2_string_encryption.md) because it additionally secures the ciphertext with a value called **HMAC** ("keyed-hash message authentication code"). 

#### Why is it necessary to "secure" encrypted data?  

The answer is very easy: to get it secure. Encrypting data with AES in mode CBC is so secure that nobody can decrypt them without having access to the encryption key or putting in the right password for a PBKDF2-derived key.

Unfortunately there are attacker outside (the so called "man in the middle") that might been able that have access to the encrypted data. If the are been able to <u>modify</u> the ciphertext then they can manipulate the encrypted data that you will receive and decrypt data with different result than to the original plaintext.

This kind of manipulating is called **tampering** and the bad news are - it works without the knowledge or even breaking of the encryption key.

The only way to prevent from this manipulating is to append some data that work like a "signature". If you receive such secured encrypted data you first check for the signature and **only if the "signature" is correct you decrypt it**.

#### What is name for this process?

We are working with a specialized hash function called **HMAC** and with some lines of code the complete encryption gets more secure.

As all other parameters stay the same as in "AES CBC mode 256 PBKDF2 string encryption" I don't name they again.

### steps in the program

The program follows the usual sequence:
1. input a password
2. convert the plaintext to a binary format (e.g. a byte array)
3. starts the encryption process
4. generate a random salt for PBKDF2
5. calculate the key with PBKDF2
6. calculate the HMAC key with PBKDF2
7. generate a random initialization vector (iv)
8. set the encryption parameters
9. encrypt the plaintext, prepends the salt and iv to the ciphertext
10. calculate the HMAC from data in step 9, convert it to Base64 encoding and append it to the complete ciphertext in step 9
11. show the result (salt:iv:ciphertext:hmac) in Base64 encoding
12. start the decryption process
13. split the complete ciphertext-string into salt, iv, ciphertext and hmac
14. Base64 decoding of the salt, iv, ciphertext and hmac
15. calculate the key with PBKDF2
16. calculate the HMAC key with PBKDF2
17. build the complete ciphertext string as in step 9
18. calculate the HMAC from string in step 17
19. compare the hmac from step 18 with hmac in step 13 and only if the match proceed with the decryption
20. set the decryption parameters (same as used for encryption)
21. decrypt the ciphertext and show the resulting plaintext

I don't provide a separate example for the decryption part as all functions are available in this program.

### **Serious notice: although the program looks like simple there is NO CHANCE for recovering the original plaintext without the password used for encryption!**

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compiler that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../AesCbc256Pbkdf2HmacStringEncryption/AesCbc256Pbkdf2HmacStringEncryption.java) | :white_check_mark: | [repl.it CpcJavaAesCbc256Pbkdf2HmacStringEncryption](https://repl.it/@javacrypto/CpcJavaAesCbc256Pbkdf2HmacStringEncryption#Main.java/)
| [PHP](../AesCbc256Pbkdf2HmacStringEncryption/AesCbc256Pbkdf2HmacStringEncryption.php) | :white_check_mark: | [repl.it CpcPhpAesCbc256Pbkdf2HmacStringEncryption](https://repl.it/@javacrypto/CpcPhpAesCbc256Pbkdf2HmacStringEncryption/)
| [C#](../AesCbc256Pbkdf2HmacStringEncryption/AesCbc256Pbkdf2HmacStringEncryption.cs) | :white_check_mark: | [repl.it CpcCsharpAesCbc256Pbkdf2HmacStringEncryption](https://repl.it/@javacrypto/CpcCsharpAesCbc256Pbkdf2HmacStringEncryption#main.cs/)
| [Javascript CryptoJs](../AesCbc256Pbkdf2HmacStringEncryption/AesCbc256Pbkdf2HmacStringEncryptionCryptoJs.js) | :white_check_mark: | [repl.it CpcCryptoJsAesCbc256Pbkdf2HmacStringEncryption](https://repl.it/@javacrypto/CpcCryptoJsAesCbc256Pbkdf2HmacStringEncryption#index.js/)
| [NodeJS Crypto](../AesCbc256Pbkdf2HmacStringEncryption/AesCbc256Pbkdf2HmacStringEncryptionNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoAesCbc256Pbkdf2HmacStringEncryption](https://repl.it/@javacrypto/CpcNodeJsCryptoAesCbc256Pbkdf2HmacStringEncryption#index.js/)
| [NodeJS node-forge](../AesCbc256Pbkdf2HmacStringEncryption/AesCbc256Pbkdf2HmacStringEncryptionNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsAesCbc256Pbkdf2HmacStringEncryption](https://repl.it/@javacrypto/CpcNodeJsAesCbc256Pbkdf2HmacStringEncryption#index.js/)
| [Python](../AesCbc256Pbkdf2HmacStringEncryption/AesCbc256Pbkdf2HmacStringEncryption.py) *1) | :white_check_mark: | [repl.it CpcPythonAesCbc256Pbkdf2HmacStringEncryption](https://repl.it/@javacrypto/CpcPythonAesCbc256Pbkdf2HmacStringEncryption#main.py/)
| [Dart](../AesCbc256Pbkdf2HmacStringEncryption/AesCbc256Pbkdf2HmacStringEncryption.dart) *2) | :white_check_mark: | no online compiler available

*1) you need the external library pycryptodome, version 3.9.9

*2) you need the external library pointycastle version 3.1.1

This is an output (as there are random elements your output will differ):

```plaintext
AES CBC 256 String encryption with PBKDF2 derived key and HMAC check
plaintext:  The quick brown fox jumps over the lazy dog

* * * Encryption * * *
ciphertextHmac (Base64): OPfp9A8dbuX60RQLWWbxS1DAlI1XmCNS7J672cqsoxQ=:TYaG8t14axeBAIT0rmOn7Q==:koRDiCLrdjQ3Bz20N0GZzYQVd0xcNLAP8p0Sc9UGp39NQWtJA35p0aEPWpRaaoBC:MskS4RpRkHa8PvcM1zGeN5BDFOmAvzTZFQ9QJxaWE70=
output is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac

* * * Decryption * * *
ciphertextHmacDecryption (Base64): OPfp9A8dbuX60RQLWWbxS1DAlI1XmCNS7J672cqsoxQ=:TYaG8t14axeBAIT0rmOn7Q==:koRDiCLrdjQ3Bz20N0GZzYQVd0xcNLAP8p0Sc9UGp39NQWtJA35p0aEPWpRaaoBC:MskS4RpRkHa8PvcM1zGeN5BDFOmAvzTZFQ9QJxaWE70=
input is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac
plaintext:  The quick brown fox jumps over the lazy dog

```

Last update: Aug. 18th 2021

Back to the main page: [readme.md](../readme.md)