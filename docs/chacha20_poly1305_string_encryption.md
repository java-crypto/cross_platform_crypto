# Cross-platform cryptography

## ChaCha20 - Poly1305 authenticated string encryption

Related to other encryption algorithms the **ChaCha20-Poly1305** encryption algorithm is relativly new - it was invented in 2004 by Daniel J. Bernstein. It consists of two parts: the first part "ChaCha20" is the encryption and the second "Poly1305" takes the role of a ciphertext authentication via a HMAC.

This encryption is a **symmetric** encryption that means you need a 32 bytes long key for encryption and the <u>same key</u> for decryption.

As the function runs faster compared to AES it will be used in more and more programs. If you want to implement this algorithm in your program you need one of the widely available [Libsodium](libsodium_overview.md) bindings (or special libraries explicit for this algorithm).

## What are the parameters in use for ChaCha20-Poly1305?

We do not have parameter sets that can get changed but fixed input lengths and a special one on output side as follows:

### First input: the length of the key

The ChaCha20-Poly1305 key length is **32 bytes**.

### Second input: the length of the (random) nonce

To get a different ciphtertext with same plaintext and same key we need a randomly generated nonce (or sometimes called "Initialization vector") of the length **12 bytes**.

### ciphertext output length

The output length of the encryption is identical to the plaintext length but there is an additional (HMAC) tag for the authentication part ("Poly1305"). Some frameworks like Java or PHP add this poly1305Tag to the ciphertext. The poly1305Tag has a length of **16 bytes** and in my programs I'm stripping it off the "completeCiphertext" to get the output compatible to other frameworks.

### Is this encryption secure?

The encryption is better than other unauthenticated algorithms/modes and has the same level as  the AES GCM algorithm (as far as I read the papers).

### steps in the program

The program follows the usual sequence:
1. generate a random encryption key and show the key in Base64 encoding for later usage
2. convert the plaintext to a binary format (e.g. a byte array)
3. starts the encryption process
4. generate a random nonce
5. set the encryption parameters
6. encrypt the plaintext, prepends the nonce to the ciphertext, appends the poly1305Tag and show the result ( nonce:ciphertext:poly1305Tag) in Base64 encoding
7. start the decryption process
8. Base64 decoding of the encryption key and the ciphertext
9. split the complete ciphertext-string into nonce, ciphertext and poly1305Tag
10. set the decryption parameters (same as used for encryption)
11. decrypt the ciphertext and show the resulting plaintext

I don't provide a stand alone decryption only example because all parts are available in the full program.

### **Serious notice: although the program looks like simple there is NO CHANCE for recovering the original plaintext without the key used for encryption!**

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code. .

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../ChaCha20Poly1305StringEncryption/Chacha20Poly1305StringEncryption.java) *1) | :white_check_mark: | [repl.it CpcJavaChaCha20Poly1305StringEncryption](https://repl.it/@javacrypto/CpcJavaChaCha20Poly1305StringEncryption#Main.java/)
| [PHP](../ChaCha20Poly1305StringEncryption/Chacha20Poly1305StringEncryption.php) *2) | :white_check_mark: | [repl.it CpcPhpChaCha20Poly1305StringEncryption](https://repl.it/@javacrypto/CpcPhpChaCha20Poly1305StringEncryption#main.php/)
| [C#](../ChaCha20Poly1305StringEncryption/Chacha20Poly1305StringEncryption.cs) *3) | :white_check_mark: | [dotnetfiddle.net  CpcCsharpChaCha20Poly1305StringEncryption](https://dotnetfiddle.net/LQZybS/)
| Javascript CryptoJs | :x: | not available
| [NodeJS Crypto](../ChaCha20Poly1305StringEncryption/Chacha20Poly1305StringEncryptionNodeJsCrypto.js) *4) | :white_check_mark: | [repl.it CpcNodeJsCryptoChaCha20Poly1305StringEncryption](https://repl.it/@javacrypto/CpcNodeJsCryptoChaCha20Poly1305StringEncryption#index.js/)
| NodeJS node-forge | :x: | not available
| [Webcrypto encryption & decryption](../ChaCha20Poly1305StringEncryption/chacha20poly1305encryptionstring.html) *5) | :white_check_mark: | [your browser WebcryptoChaCha20Poly105StringEncryption.html](http://javacrypto.bplaced.net/cpcjs/chacha20poly1305/chacha20poly1305encryptionstring.html)
| [Python](../ChaCha20Poly1305StringEncryption/Chacha20Poly1305StringEncryption.py) *6) | :white_check_mark: | [repl.it CpcPythonChaCha20Poly1305StringEncryption](https://repl.it/@javacrypto/CpcPythonChaCha20Poly1305StringEncryption#main.py/)
| [Go](../ChaCha20Poly1305StringEncryption/Chacha20Poly1305StringEncryption.go) | :white_check_mark: | [repl.it CpcGoChaCha20Poly1305StringEncryption](https://replit.com/@javacrypto/CpcGoChaCha20Poly1305StringEncryption#main.go/)
| [Dart](../ChaCha20Poly1305StringEncryption/Chacha20Poly1305StringEncryption.dart) *7)| :white_check_mark: | no online compiler available

* *1) Java: runs in Java 11+ without external library
* *2) PHP: runs in PHP 7.3+ without external library
* *3) C# needs **dot.net 5** to run, so I changed my online compiler for this example. For encryption you need the NSec library (documentation: https://nsec.rocks/, code  https://github.com/ektrah/nsec)
* *4) NodeJs's built in Crypto library runs the ChaCha20Poly15 algorithm
* *5) Webcrypto needs the external library "sodium.js" (documentation: https://github.com/jedisct1/libsodium.js/, code: https://github.com/jedisct1/libsodium.js/blob/master/dist/browsers/sodium.js)
* *6) Python need the external library pycryptodome, version 3.9.9 (documentation: https://pypi.org/project/pycryptodome/, code: https://github.com/Legrandin/pycryptodome/)
* *7) Dart need the external library "pointycastle" (pointycastle: ^3.1.1)

This is an output (as there are random elements your output will differ):

```plaintext
ChaCha20-Poly1305 String encryption with random key full
plaintext:  The quick brown fox jumps over the lazy dog
encryptionKey (Base64): qnEKKe7b4YuZm9K0tHp5rPiSfSER/EdoC2OKiaT2dF4=

* * * Encryption * * *
ciphertextWithoutTag length: 43
ciphertext: RqTh7HXSrF0d2A0s:UtuJ1npTKZxwKKUxCs7UDJGMZAKP5W5cvxoV5rogpIRU+/a1WN+uZlfCgg==:RSQxhrNJO+SkE1Cnzpl5Bg==
output is (Base64) nonce : (Base64) ciphertext : (Base64) poly1305Tag

* * * Decryption * * *
decryptionKey (Base64): qnEKKe7b4YuZm9K0tHp5rPiSfSER/EdoC2OKiaT2dF4=
ciphertext (Base64): RqTh7HXSrF0d2A0s:UtuJ1npTKZxwKKUxCs7UDJGMZAKP5W5cvxoV5rogpIRU+/a1WN+uZlfCgg==:RSQxhrNJO+SkE1Cnzpl5Bg==
input is (Base64) nonce : (Base64) ciphertext : (Base64) poly1305Tag
plaintext:  The quick brown fox jumps over the lazy dog

```

Last update: Aug. 17th 2021

Back to the main page: [readme.md](../readme.md)