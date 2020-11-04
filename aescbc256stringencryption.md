Cross-platform cryptography
===============

AES CBC mode 256 string encryption
---------------

The standard encryption algorithm is **AES** after it got choosen and standardized by NIST (National Institute of Standards and Technology). There are 3 main parameters that need to be equal when crossing the platforms to decrypt the ciphertext.

**First parameter: the length of the key**

An AES key has 3 "allowed" key lengths: 128 bit = 16 byte, 192 bit = 24 byte and 256 bits = 32 byte. Our programs will run with a fixed length of 32 byte for a maximum of .

**Second parameter: the mode of operation**

There are several AES modes defined and here we are using the most common one - the CBC mode (Cipher Block Chaining mode). Please keep in mind that there are more secure modes that could be used (e.g. GCM mode).

**Third parameter: the padding of the data**

The used CBC mode is a block algorithm means that the encryption (and decryption) is done in blocks of 16 bytes of data. There are only a few scenarios where you have to handle exact 16 bytes of data - in all other cases you have to fill up the plaintext to a length of multiple of 16. There are some padding algorithms available and our programs will use the PKCS#5 or PKCS#7 padding (Java names this padding PKCS#5, most other languages use PKCS#7).

### steps in the program

The program follows the usual sequence:
1. generate a random encryption key and show the key in Base64 encoding for later usage
2. convert the plaintext to a binary format (e.g. a byte array)
3. starts the encryption process
4. generate a random initialization vector (iv)
5. set the encryption parameters
6. encrypt the plaintext, prepends the iv to the ciphertext and show the result ( iv:ciphertext) in Base64 encoding
7. start the decryption process
8. Base64 decoding of the encryption key and the ciphertext
9. split the complete ciphertext-string into iv and ciphertext
10. set the decryption parameters (same as used for encryption)
11. decrypt the ciphertext and show the resulting plaintext

If you like to see the **decryption part only** see my separate article AES CBC mode 256 string decryption only (comming soon).

### **Serious notice: although the program looks like simple there is NO CHANCE for recovering the original plaintext without the key used for encryption!**

:warning: Security warning :warning:
---------------

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](AesCbc256StringEncryption/AesCbc256StringEncryption.java) | :white_check_mark: | [repl.it CpcJavaAesCbc256StringEncryptionFull](https://repl.it/@javacrypto/CpcJavaAesCbc256StringEncryptionFull#Main.java/)
| [PHP](AesCbc256StringEncryption/AesCbc256StringEncryption_Full.php) | :white_check_mark: | [repl.it CpcPhpAesCbc256StringEncryptionFull](https://repl.it/@javacrypto/CpcPhpAesCbc256StringEncryptionFull/)
| [C#](AesCbc256StringEncryption/AesCbc256StringEncryption_Full.cs) | :white_check_mark: | [repl.it CpcCsharpAesCbc256StringEncryptionFull](https://repl.it/@javacrypto/CpcCsharpAesCbc256StringEncryptionFull#main.cs/)
| [CryptoJs](AesCbc256StringEncryption/AesCbc256StringEncryptionFull.cryptoJs) | :white_check_mark: | [repl.it AesCbc256StringEncryption_Full](https://repl.it/@javacrypto/CpcCryptoJsAesCbc256StringEncryptionFull#index.js/)
| [Node.JS](AesCbc256StringEncryption/AesCbc256StringEncryption_Full.nodeJs) | :white_check_mark: | [repl.it CpcNodeJsAesCbc256StringEncryptionFull](https://repl.it/@javacrypto/CpcNodeJsAesCbc256StringEncryptionFull#index.js/)

This is an output (as there are random elements your output will differ):

```plaintext
AES CBC 256 String encryption with random key full
plaintext:  The quick brown fox jumps over the lazy dog
encryptionKey (Base64): DuLNOxN3BUc+htgigTcQOeJsPuMkF/aBcyVSaXhMcEw=

* * * Encryption * * *
ciphertext: XvXGCOyMrj2ohknIr8k1+A==:nPrEGko/7OFRjiRCgX0Hryz0a+Qc6A9RmRlipWl+R6vslqLBf/8EZtGsf+zwwGAV
output is (Base64) iv : (Base64) ciphertext

* * * Decryption * * *
decryptionKey (Base64): DuLNOxN3BUc+htgigTcQOeJsPuMkF/aBcyVSaXhMcEw=
ciphertext (Base64): XvXGCOyMrj2ohknIr8k1+A==:nPrEGko/7OFRjiRCgX0Hryz0a+Qc6A9RmRlipWl+R6vslqLBf/8EZtGsf+zwwGAV
input is (Base64) iv : (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog
```


Last update: Nov. 1st 2020

Back to the main page: [readme.md](readme.md)