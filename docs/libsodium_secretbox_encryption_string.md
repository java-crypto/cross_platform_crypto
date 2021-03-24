# Cross-platform cryptography

## Libsodium secret box authenticated string encryption

What is so special with this **secret box** inside of **Libsodium**? The answer is easy - using this box will give you a plus on security, high speed, "state of the art" methods and a minimum implementation work - OK - it's time to say "wow"! 

### How does this encryption works?

The program is using a randomly generated encryption key. This key has to be used on encryption and decryption, that's why this encryption is named **symmetric encryption**.

Just feed the data and receive the encrypted form of the data prepared for sending via email, later these data get decrypted to the original plaintext. When someone tries to tamper the ciphertext, don't worry, there is "signature" appended to detect those changes.

### What are the algorithms used in the secret box?

There are three different tasks and here are the implementation details:

1. The **encryption key** is a randomly generated, 32 bytes long key that is encoded in Base64 for better exchange
2. for the encryption the fast **XSalsa20 stream cipher** was choosen
3. to secure the ciphertext against tampering a MAC is generated, the algorithm for this task is **Poly1305 MAC**.

To run this bundle of tasks you need exact one line of code for encryption and another line for decryption, that's it. No hassling with lines of code that could cause errors. When viewing my programs there are of course some more lines of code but most of them are service tasks (e.g. converting the binary data to Base64 encoding and vice versa). Just for fun - compare the code with the one in [AES CBC mode 256 string encryption](docs/aes_cbc_256_string_encryption.md) and you will notice the difference.

And - in my eyes - the most important fact is the compatibility of the code cross-platform wide. All Libsodium bindings work on the same basis so it is easy to exchange data across system barriers.

### Is this encryption secure?

Answered from a technical side it is **yes**. Most important is the way the encryption key is shared between sender and recipient - best in a personal exchange. 

### Is there any anonymized solution available?

The ciphertext is secured against tampering but without additional "signatures" there is just the anonymized encryption.

I do not provide a "decryption only" version as all functions are available in the full version.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../LibsodiumSecretboxEncryptionString/LibsodiumSecretboxEncryptionString.java) | :white_check_mark: | [repl.it CpcJavaLibsodiumSecretboxStringEncryption](https://repl.it/@javacrypto/CpcJavaLibsodiumSecretboxEncryptionString#Main.java/)
| [PHP](../LibsodiumSecretboxEncryptionString/LibsodiumSecretboxEncryptionString.php) | :white_check_mark: | [repl.it CpcPhpLibsodiumSecretboxStringEncryption](https://repl.it/@javacrypto/CpcPhpLibsodiumSecretboxEncryptionString#main.php/)
| [C#](../LibsodiumSecretboxEncryptionString/LibsodiumSecretboxEncryptionString.cs) | :white_check_mark: | [dotnetfiddle.net  CpcCsharpLibsodiumSecretboxStringEncryption](https://dotnetfiddle.net/ErYhvs)
| [Javascript / NodeJs](../LibsodiumSecretboxEncryptionString/LibsodiumSecretboxEncryptionStringNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsLibsodiumSecretboxStringEncryption](https://repl.it/@javacrypto/CpcNodeJsLibsodiumSecretboxEncryptionString#index.js)
| NodeJS CryptoJs | :x: | use above Javascript / NodeJs solution
| NodeJS Crypto | :x: | use above Javascript / NodeJs solution
| NodeJS forge | :x: | use above Javascript / NodeJs solution
| [Webcrypto / Browser](../LibsodiumSecretboxEncryptionString/libsodiumsecretboxencryptionstring.html) | :white_check_mark: | [your browser LibsodiumSecretboxStringEncryption.html](http://javacrypto.bplaced.net/cpcjs/secretbox/libsodiumsecretboxencryptionstring.html/)
| [Python](../LibsodiumSecretboxEncryptionString/LibsodiumSecretboxEncryptionString.py) | :white_check_mark: | [repl.it CpcPythonLibsodiumSecretboxStringEncryption](https://repl.it/@javacrypto/CpcPythonLibsodiumSecretboxEncryptionString#main.py/)
| [Go](../LibsodiumSecretboxEncryptionString/LibsodiumSecretboxEncryptionString.go) | :white_check_mark: | [repl.it CpcGoLibsodiumSecretboxStringEncryption](https://repl.it/@javacrypto/CpcGoLibsodiumSecretboxEncryptionString#main.go/)

This is an output (your will differ because a random element):

```plaintext
Libsodium secret box random key string encryption
plaintext: The quick brown fox jumps over the lazy dog
keyBase64): 2D+rQ4BiIjvktHQtFAyAa2FgcZORFB4JQHieO1YKSM4=

* * * encryption * * *
all data are in Base64 encoding
ciphertext:  DaiA7r5xBBviqULcVerR4zI9EALgC2x+:DBHu6jcfLUFdKrwePnIreaFkczcfGzT9ZWFl1gu2uAWToTjuFVg8+kelmDi3lp7eaTnftCnmkodkbi4=
output is (Base64) nonce : (Base64) ciphertext

* * * decryption * * *
ciphertext:  DaiA7r5xBBviqULcVerR4zI9EALgC2x+:DBHu6jcfLUFdKrwePnIreaFkczcfGzT9ZWFl1gu2uAWToTjuFVg8+kelmDi3lp7eaTnftCnmkodkbi4=
input is (Base64) nonce : (Base64) ciphertext
decrypt.text:The quick brown fox jumps over the lazy dog

```

Last update: Mar. 24th 2021

Back to the main page: [readme.md](../readme.md)