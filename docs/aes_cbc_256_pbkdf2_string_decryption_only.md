# Cross-platform cryptography

## AES CBC mode 256 PBKDF2 string decryption only

If you received a message that was encrypted by one of my [AES CBC 256 PBKDF2 string encryption-programs](aes_cbc_256_pbkdf2_string_encryption.md) here is the **decryption only** solution for one of the other platforms.

This version is the advanced version of [AES CBC mode 256 string decryption only](aes_cbc_256_string_decryption_only.md) because it does not use a randomly generated AES key but a key derived from a password using the **PBKDF2** key derivation. More details about this can be found in my [PBKDF2 article](pbkdf2.md).

### steps in the program

The program follows the usual sequence:
1. input a password
2. start the decryption process
3. input a complete ciphertext
4. split the complete ciphertext-string into salt, iv and ciphertext
5. Base64 decoding of the salt, iv and the ciphertext
6. calculate the key with PBKDF2
7. set the decryption parameters (same as used for encryption)
8. decrypt the ciphertext and show the resulting plaintext

If you like to see the **full encryption** see my separate article [AES CBC mode 256 PBKDF2 string encryption](aes_cbc_256_pbkdf2_string_encryption.md).

### **Serious notice: although the program looks like simple there is NO CHANCE for recovering the original plaintext without the password used for encryption!**

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compiler that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringDecryptionOnly.java) | :white_check_mark: | [repl.it CpcJavaAesCbc256Pbkdf2StringDecryptionOnly](https://repl.it/@javacrypto/CpcJavaAesCbc256Pbkdf2StringDecryptionOnly#Main.java/)
| [PHP](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringDecryptionOnly.php) | :white_check_mark: | [repl.it CpcPhpAesCbc256Pbkdf2StringDecryptionOnly](https://repl.it/@javacrypto/CpcPhpAesCbc256Pbkdf2StringDecryptionOnly/)
| [C#](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringDecryptionOnly.cs) | :white_check_mark: | [repl.it CpcCsharpAesCbc256Pbkdf2StringDecryptionOnly](https://repl.it/@javacrypto/CpcCsharpAesCbc256Pbkdf2StringDecryptionOnly#main.cs/)
| [Javascript CryptoJs](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringDecryptionOnlyCryptoJs.js) | :white_check_mark: | [repl.it CpcCryptoJsAesCbc256Pbkdf2StringDecryptionOnly](https://repl.it/@javacrypto/CpcCryptoJsAesCbc256Pbkdf2StringDecryptionOnly#index.js/)
| [NodeJS Crypto](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringDecryptionOnlyNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoAesCbc256Pbkdf2StringDecryptionOnly](https://repl.it/@javacrypto/CpcNodeJsCryptoAesCbc256Pbkdf2StringDecryptionOnly#index.js/)
| [NodeJS node-forge](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringDecryptionOnlyNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsAesCbc256Pbkdf2StringDecryptionOnly](https://repl.it/@javacrypto/CpcNodeJsAesCbc256Pbkdf2StringDecryptionOnly#index.js/)
| [Python](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_Full.py) *1) | :x: | [see the full version](https://repl.it/@javacrypto/CpcPythonAesCbc256Pbkdf2StringEncryptionFull#main.py/)
| [Dart](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption.dart) *2) | :white_check_mark: | no online compiler available

*1) you need the external library pycryptodome, version 3.9.9

*2) you need the external library pointycastle version 3.1.1

This is an output (as there are random elements your output will differ):

```plaintext
AES CBC 256 String decryption with PBKDF2 derived key only

* * * Decryption * * *
ciphertext (Base64): uzlk156G27dhUXLD4ImappTJUMG3jCT0GXiP45BlyCE=:2wRNNEejkXv59rB6Ol2BGQ==:LccJKke7VstzPjpnS93o7nXpvPKqIykAXxg4CfDbeFZ4B67W6AGIEZkT1v6PIjrT
input is (Base64) salt : (Base64) iv : (Base64) ciphertext
plaintext: The quick brown fox jumps over the lazy dog
```

Last update: Aug. 18th 2021

Back to the main page: [readme.md](../readme.md)