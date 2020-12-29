# Cross-platform cryptography

## AES CBC mode 256 passphrase string encryption

This article describes a specialized form of AES password based encryption - the "old fashioned" OpenSSL password derivation. Up to version 1.1.0 of OpenSSL this method was in use and I'm giving the Cross platform examples just for historic reasons **and** for migrations to a (much) better password derivation functions (using e.g. PBKDF2 as described here: [AES CBC 256 PBKDF2 string encryption](aes_cbc_256_pbkdf2_string_encryption.md)).

### Is this password derivation secure?

As you could imaging a secure one wouldn't be replaced by OpenSSL so the answer is: **no, this password derivation is UNSECURE**. There are two main resons for this: 

1. the password derivation is based on the hash algorithm MD5 that should no longer be in use
2. compared to PBKDF2 with a variable iterations number (minimum should be 10000 iterations, better more) this function does make exact **ONE** iteration.

## :warning: Security warning :warning:

**Please use this function only for migration and not for actual (new written) software projects as it is UNSECURE**.

If you want read more about the encryption kindly see my article [AES CBC mode 256 string encryption](aes_cbc_256_string_encryption.md).

### Is this encryption secure?
Beneath the issue with the password derivation the answer is "it depends on...". The encryption algorithm itself is secure but you should keep in mind that an attacker may been able to modify the ciphertext on transport. One example for this is the so called **tampering** - you can find a simple [example here](aes_cbc_tampering.md).

### steps in the program

The program follows the usual sequence:
1. input a password
2. convert the plaintext to a binary format (e.g. a byte array)
3. starts the encryption process
4. generate a random salt (8 bytes long) for password derivation
5. calculate the key and initialization vector
6. set the encryption parameters
7. encrypt the plaintext, prepends the text 'Salted__' and salt to the ciphertext and show the result (ciphertext) in Base64 encoding
8. start the decryption process
9. Base64-decode the complete ciphertext
10. split the complete ciphertext-string into 'Salted__', salt and ciphertext
11. calculate the key and iv
12. set the decryption parameters (same as used for encryption)
13. decrypt the ciphertext and show the resulting plaintext

### **Serious notice: although the program looks like simple there is NO CHANCE for recovering the original plaintext without the key used for encryption!**

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](AesCbc256PassphraseStringEncryption/AesCbc256PassphraseStringEncryption.java) | :white_check_mark: | [repl.it CpcJavaAesCbc256PassphraseStringEncryption](https://repl.it/@javacrypto/CpcJavaAesCbc256PassphraseStringEncryption#Main.java/)
| [PHP](AesCbc256PassphraseStringEncryption/AesCbc256PassphraseStringEncryption.php) | :white_check_mark: | [repl.it CpcPhpAesCbc256PassphraseStringEncryption](https://repl.it/@javacrypto/CpcPhpAesCbc256PassphraseStringEncryption#main.php/)
| [C#](AesCbc256PassphraseStringEncryption/AesCbc256PassphraseStringEncryption.cs) | :white_check_mark: | [repl.it CpcCsharpAesCbc256PassphraseStringEncryption](https://repl.it/@javacrypto/CpcCsharpAesCbc256PassphraseStringEncryption#main.cs/)
| [Javascript CryptoJs](AesCbc256PassphraseStringEncryption/AesCbc256PassphraseStringEncryptionCryptoJs.js) | :white_check_mark: | [repl.it CpcCryptoJsAesCbc256PassphraseStringEncryption](https://repl.it/@javacrypto/CpcCryptoJsAesCbc256PassphraseStringEncryption#index.js/)
| [NodeJS Crypto](AesCbc256PassphraseStringEncryption/AesCbc256PassphraseStringEncryptionNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoAesCbc256PassphraseStringEncryption](https://repl.it/@javacrypto/CpcNodeJsCryptoAesCbc256PassphraseStringEncryption#index.js/)
| [NodeJS node-forge](AesCbc256PassphraseStringEncryption/AesCbc256PassphraseStringEncryptionNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsAesCbc256PassphraseStringEncryption](https://repl.it/@javacrypto/CpcNodeJsAesCbc256PassphraseStringEncryption#index.js/)

This is an output (as there are random elements your output will differ):

```plaintext
AES CBC 256 String encryption with passphrase

# # # SECURITY WARNING: This code is provided for achieve    # # #
# # # compatibility between different programming languages. # # #
# # # It is not necessarily fully secure.                    # # #
# # # Its security depends on the complexity and length      # # #
# # # of the password, because of only one iteration and     # # #
# # # the use of MD5.                                        # # #
# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #

passphrase: my secret passphrase

* * * Encryption * * *
ciphertext: U2FsdGVkX1/+hqfCdNaZ36kZzPzwvFXkr4nEySMEgdVdPnPrGUj2Gi1t2pspAH2WHVEVgKHKWfR2Gc0sKnTlLg==
output is (Base64) ciphertext

* * * Decryption * * *
passphrase: my secret passphrase
ciphertext (Base64): U2FsdGVkX19rV+JOoeF72K54jlyJ47tNEsMjih7gDnFUysZFaw+WnYTB5L/hc2+rssFj1hXw3N8kkikHwClB2w==
input is (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog

```

Last update: Dec. 12th 2020

Back to the main page: [readme.md](readme.md)