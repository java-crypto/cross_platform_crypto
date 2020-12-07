# Cross-platform cryptography

## AES GCM mode 256 string encryption

The standard encryption algorithm is **AES** after it got choosen and standardized by NIST (National Institute of Standards and Technology). There are 3 main parameters that need to be equal when crossing the platforms to decrypt the ciphertext.

**First parameter: the length of the key**

An AES key has 3 "allowed" key lengths: 128 bit = 16 byte, 192 bit = 24 byte and 256 bits = 32 byte. Our programs will run with a fixed length of 32 byte for a maximum of security.

**Second parameter: the mode of operation**

There are several AES modes defined and here we are using the most secure one - the GCM mode (Galois Counter mode). This mode includes an authentication check (like a HMAC) encryption and detects any kind of tampering (see my article on [AES CBC mode tampering](aes_cbc_tampering.md)). More information about GCM mode can be found on [Wikipedia](https://en.wikipedia.org/wiki/Galois/Counter_Mode).

**Third parameter: the padding of the data**

The used CBC mode is a block algorithm means that the encryption (and decryption) is done in blocks of 16 bytes of data. There are only a few scenarios where you have to handle exact 16 bytes of data - in all other cases you have to fill up the plaintext to a length of multiple of 16. There are some padding algorithms available and our programs will use the PKCS#5 or PKCS#7 padding (Java names this padding PKCS#5, most other languages use PKCS#7).

### Is this encryption secure?

The answer is easy - it is most secure AES mode available. It calculates a checksum (like a HMAC) and is often named as "tag" or "gcmTag". Some frameworks like Java append the tag to the ciphertext, others like PHP get the tag directly from the cipher engine. My programs always separate the tag from the ciphertext and transport it a an own (base64 encoded) variable.

### steps in the program

The program follows the usual sequence:
1. generate a random encryption key and show the key in Base64 encoding for later usage
2. convert the plaintext to a binary format (e.g. a byte array)
3. starts the encryption process
4. generate a random initialization vector (iv)
5. set the encryption parameters
6. encrypt the plaintext, prepends the iv to the ciphertext, appends the tag and show the result ( iv:ciphertext:tag) in Base64 encoding
7. start the decryption process
8. Base64 decoding of the encryption key and the ciphertext
9. split the complete ciphertext-string into iv, ciphertext and tag
10. set the decryption parameters (same as used for encryption)
11. decrypt the ciphertext and show the resulting plaintext

I don't provide a stand alone decryption only example because all parts are available in the full programs..

### **Serious notice: although the program looks like simple there is NO CHANCE for recovering the original plaintext without the key used for encryption!**

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code. Unfortunately this mode is not so widely supported as the CBC mode. If you like to get a pure Javascript solution running in your browser kindly see my Webcrypto example in the article [AES GCM mode PBKDF2 string encryption](aes_gcm_256_pbkdf2_string_encryption.md).

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](AesGcm256StringEncryption/AesGcm256StringEncryption.java) | :white_check_mark: | [repl.it CpcJavaAesGCM256StringEncryption](https://repl.it/@javacrypto/CpcJavaAesGcm256StringEncryption#Main.java/)
| [PHP](AesGcm256StringEncryption/AesGcm256StringEncryption.php) | :white_check_mark: | [repl.it CpcPhpAesGCM256StringEncryption](https://repl.it/@javacrypto/CpcPhpAesGcm256StringEncryption#main.php/)
| [C#](AesGcm256StringEncryption/AesGcm256StringEncryption.cs) *1) | :white_check_mark: | [dotnetfiddle.net  CpcCsharpAesGCM256StringEncryption](https://dotnetfiddle.net/c91C0t/)
| Javascript CryptoJs | :x: | not available
| [NodeJS Crypto](AesGcm256StringEncryption/AesGcm256StringEncryptionNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoAesGCM256StringEncryption](https://repl.it/@javacrypto/CpcNodeJsCryptoAesGcm256StringEncryption#index.js/)
| [NodeJS node-forge](AesGcm256StringEncryption/AesGcm256StringEncryptionNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsAesGCM256StringEncryption](https://repl.it/@javacrypto/CpcNodeJsAesGcm256StringEncryption#index.js/)

*1) C# needs **dot.net 5** to run, so I changed my online compiler for this example.

This is an output (as there are random elements your output will differ):

```plaintext
AES GCM 256 String encryption with random key
plaintext:  The quick brown fox jumps over the lazy dog
encryptionKey (Base64): 9EpOtX94bHDiEl3n32oBS2Q3g6PK706mxsxZeWgF7nc=

* * * Encryption * * *
ciphertext: MS6WBc0jteo/WlSv:IhnNAdqi/uff3WePSasi4EH6qSm5Dd38ZtuU4c1yEEeFg+hqImxqiyDY/g==:WxoZfgL539r+0JvBfn+DbQ==
output is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag

* * * Decryption * * *
decryptionKey (Base64): 9EpOtX94bHDiEl3n32oBS2Q3g6PK706mxsxZeWgF7nc=
ciphertext (Base64): MS6WBc0jteo/WlSv:IhnNAdqi/uff3WePSasi4EH6qSm5Dd38ZtuU4c1yEEeFg+hqImxqiyDY/g==:WxoZfgL539r+0JvBfn+DbQ==
input is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag
plaintext:  The quick brown fox jumps over the lazy dog

```

Last update: Dec. 07th 2020

Back to the main page: [readme.md](readme.md)