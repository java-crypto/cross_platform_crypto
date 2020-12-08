# Cross-platform cryptography

## AES GCM mode 256 PBKDF2 string encryption

The standard encryption algorithm is **AES** after it got choosen and standardized by NIST (National Institute of Standards and Technology). There are 3 main parameters that need to be equal when crossing the platforms to decrypt the ciphertext. 

This version is the advanced version of [AES GCM mode 256 string encryption](aes_gcm_256_string_encryption.md) because it does not use a randomly generated AES key but a key derived from a password using the **PBKDF2** key derivation. More details about this can be found in my [PBKDF2 article](pbkdf2.md).

#### First parameter: the password or passphrase

This is a string (or char array in Java) and in a program it gets filled by a user input like "input password".

#### Second parameter: the data to encrypt (or decrypt)

This is the plaintext string (on encryption side) or ciphertext string (on decryption side). As we do need the same **salt**, **initialization vector (iv)** and **gcmTag** on decryption side, the ciphertext is a concationation of

```terminal
(Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag
```

All other parameters (number of PBKDF2 iterations, AES mode and padding) are hard-coded in the encryption and decryption functions.

### Is this encryption secure?

The answer is easy - it is most secure AES mode available. It calculates a checksum (like a HMAC) and is often named as "tag" or "gcmTag". Some frameworks like Java append the tag to the ciphertext, others like PHP get the tag directly from the cipher engine. My programs always separate the tag from the ciphertext and transport it in an own (Base64 encoded) variable.

### steps in the program

The program follows the usual sequence:
1. input a password
2. convert the plaintext to a binary format (e.g. a byte array)
3. starts the encryption process
4. generate a random salt for PBKDF2
5. calculate the key with PBKDF2
6. generate a random nonce
7. set the encryption parameters
8. encrypt the plaintext, prepends the salt and nonce to the ciphertext, add the gcmTag and show the result (salt:nonce:ciphertext:gcmTag) in Base64 encoding
9. start the decryption process
10. split the complete ciphertext-string into salt, nonce, ciphertext and gcmTag
11. Base64 decoding of the salt, nonce, ciphertext and the gcmTag
12. calculate the key with PBKDF2
13. set the decryption parameters (same as used for encryption)
14. decrypt the ciphertext and show the resulting plaintext

I don't provide a stand alone decryption only example because all parts are available in the full program.

### **Serious notice: although the program looks like simple there is NO CHANCE for recovering the original plaintext without the password used for encryption!**

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](AesGcm256Pbkdf2StringEncryption/AesGcm256Pbkdf2StringEncryption_Full.java) | :white_check_mark: | [repl.it CpcJavaAesGcm256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcJavaAesGcm256Pbkdf2StringEncryptionFull#Main.java/)
| [PHP](AesGcm256Pbkdf2StringEncryption/AesGcm256Pbkdf2StringEncryption_Full.php) | :white_check_mark: | [repl.it CpcPhpAesGcm256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcPhpAesGcm256Pbkdf2StringEncryptionFull/)
| [C#](AesGcm256Pbkdf2StringEncryption/AesGcm256Pbkdf2StringEncryption_Full.cs) | :white_check_mark: | [repl.it CpcCsharpAesGcm256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcCsharpAesGcm256Pbkdf2StringEncryptionFull#main.cs/)
| Javascript CryptoJs | :x: | not available
| [NodeJS Crypto](AesGcm256Pbkdf2StringEncryption/AesGcm256Pbkdf2StringEncryption_FullNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoAesGcm256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcNodeJsCryptoAesGcm256Pbkdf2StringEncryptionFull#index.js/)
| [NodeJS node-forge](AesGcm256Pbkdf2StringEncryption/AesGcm256Pbkdf2StringEncryption_FullNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsAesGcm256Pbkdf2StringEncryptionFull](https://repl.it/@javacrypto/CpcNodeJsAesGcm256Pbkdf2StringEncryptionFull#index.js/)
| [Webcrypto](AesGcm256Pbkdf2StringEncryption/aesgcm256pbkdf2stringencryptionwebcrypto.html) | :white_check_mark: | [your browser AesGcm256Pbkdf2StringEncryptionWebcrypto.html](https://java-crypto.github.io/cross_platform_crypto/AesGcm256Pbkdf2StringEncryption/aesgcm256pbkdf2stringencryptionwebcrypto.html)

An important note about the Webcrypto-program: the program is of **very poor quality** and should be used for demonstration purpose only. It has a lot of unnecessary conversions - **never ever use it as basis for your own (business) programs**.

This is an output (as there are random elements your output will differ):

```plaintext
AES GCM 256 String encryption with PBKDF2 derived key
plaintext:  The quick brown fox jumps over the lazy dog

* * * Encryption * * *
ciphertext (Base64): rGiDbmbO3BbHJFzEdxC82oNhdzyfp//ZT/F1YEdToGw=:sokNSXKF02BDnWiE:+4R9e3BE52LhiNZ4GvzcDybPmfLLY2/Bg0tT0F2JpX9tOfajy0thEkrYmw==:mmmdJ2Xkj6T6GL56x0Io2w==
output is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag

* * * Decryption * * *
ciphertext (Base64): rGiDbmbO3BbHJFzEdxC82oNhdzyfp//ZT/F1YEdToGw=:sokNSXKF02BDnWiE:+4R9e3BE52LhiNZ4GvzcDybPmfLLY2/Bg0tT0F2JpX9tOfajy0thEkrYmw==:mmmdJ2Xkj6T6GL56x0Io2w==
input is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag
plaintext:  The quick brown fox jumps over the lazy dog

```

This is the output of the (browser based) Webcrypto:

```plaintext
AES GCM 256 String encryption with PBKDF2 derived key

Important note: this program is doing what it promises but the programming itself is of very poor quality and for demonstration purposes only. Never ever use this program as source for your own programs because there are a lot of conversions to get it run.

Plaintext: The quick brown fox jumps over the lazy dog
Secret password: secret password

CiphertextCpc: 0hjPdiaEjzCYpYbfXWb47U9Yz2vcqTbo+2ixn6dYZxQ=:hx2yzgjkpB0oB+qj4HK1pg==:obDSutcfyv4mNn6SIpR/5cP+rccR0lEwi27lXxWDadOmfC51zi2fECXN3mjCb7pF
Ciphertext is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag 

DecryptedtextCpc: The quick brown fox jumps over the lazy dog
```

Last update: Dec. 08th 2020

Back to the main page: [readme.md](readme.md)