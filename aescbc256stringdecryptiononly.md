# Cross-platform cryptography

## AES CBC mode 256 string decryption only

If you received a message that was encrypted by one of my [AES CBC 256 string encryption-programs](aescbc256stringencryption.md) here is the **decryption only** solution for one of the other platforms.


### steps in the program

The program follows the usual sequence but starting with decryption:
1. enter the random encryption key in Base64 encoding
2. enter the complete ciphertext (iv:ciphertext) in Base64 encoding
3. start the decryption process
4. split the complete ciphertext-string into iv and ciphertext
5. Base64 decoding of the encryption key and the ciphertext
6. set the decryption parameters (same as used for encryption)
7. decrypt the ciphertext and show the resulting plaintext

If you like to see the **encryption part** see my separate article [AES CBC mode 256 string encryption](aescbc256stringencryption.md).

### **Serious notice: although the program looks like simple there is NO CHANCE for recovering the original plaintext without the key used for encryption!**

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](AesCbc256StringEncryption/AesCbc256StringDecryptionOnly.java) | :white_check_mark: | [repl.it CpcJavaAesCbc256StringDecryptionOnly](https://repl.it/@javacrypto/CpcJavaAesCbc256StringDecryptionOnly#Main.java/)
| [PHP](AesCbc256StringEncryption/AesCbc256StringDecryptionOnly.php) | :white_check_mark: | [repl.it CpcPhpAesCbc256StringDecryptionOnly](https://repl.it/@javacrypto/CpcPhpAesCbc256StringDecryptionOnly/)
| [C#](AesCbc256StringEncryption/AesCbc256StringDecryptionOnly.cs) | :white_check_mark: | [repl.it CpcCsharpAesCbc256StringDecryptionOnly](https://repl.it/@javacrypto/CpcCsharpAesCbc256StringDecryptionOnly#main.cs/)
| [Javascript CryptoJs](AesCbc256StringEncryption/AesCbc256StringDecryptionOnlyCryptoJs.js) | :white_check_mark: | [repl.it AesCbc256StringDecryptionOnly](https://repl.it/@javacrypto/CpcCryptoJsAesCbc256StringDecryptionOnly#index.js/)
| [NodeJS Crypto](AesCbc256StringEncryption/AesCbc256StringDecryptionOnlyNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoAesCbc256StringDecryptionOnly](https://repl.it/@javacrypto/CpcNodeJsCryptoAesCbc256StringDecryptionOnly#index.js/)
| [NodeJS node-forge](AesCbc256StringEncryption/AesCbc256StringDecryptionOnlyNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsAesCbc256StringDecryptionOnly](https://repl.it/@javacrypto/CpcNodeJsAesCbc256StringDecryptionOnly#index.js/)

This is an output (as there are random elements your output will differ):

```plaintext
AES CBC 256 String Decryption only

* * * Decryption * * *
decryptionKey (Base64): d3+KZq3lsdy5Hgd1VRVQFUhA9jU0PaBHTG2LG2PazQM=
ciphertext (Base64): 8GhkycnbzDo5WbRol5zfCQ==:gQrTOJdobbI4fuCHCfu5eqKFYCaw/AM6xMK2o+xCXB0MeAQ7rFbXAbAE3Ex+bD8c
input is (Base64) iv : (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog
```

Last update: Nov. 7th 2020

Back to the main page: [readme.md](readme.md)