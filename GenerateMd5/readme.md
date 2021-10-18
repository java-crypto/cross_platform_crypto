# Cross-platform cryptography

## MD-5 hash of a string

Hashing of data is one of the most function used when working with cryptographic.

### What is a hash?

The hash of data is often described as "fingerprint" of the data as the result is a kind of "checksum" that is characteristic for the data. An other name for a hash is a trapdoor as there should be no way back meaning: with having a hash value it should not been able to get the data back or even think what data was used as input.

### How secure is a hash?

Very import for hash function is it to change a lot of bytes even when just 1 byte of the plaintext changes. As hashes are very often used in combination with digital signatures it is most important that you cannot create a changed or second document that gets the same hash. This article describes the **unsecure hash algorithm MD-5** - please don't use it ever! Visit the website [https://natmchugh.blogspot.com/2015/02/create-your-own-md5-collisions.html](https://natmchugh.blogspot.com/2015/02/create-your-own-md5-collisions.html) and see the two pictures - one is a crashing plane and the other is the wrack of a sunken ship - both have the **same MD-5 hash**. 

### Why is MD-5 still in use?

In "old days" the AES-128 algorithm was used very often and needs a 128/8 = 16 bytes long key and initialization vector and the MD-5 algorithm has an output of 16 bytes - perfect for a "poor man's key derivation".

### Is this password derivation secure?

Just a simple answer: **no, MD-5 is BROKEN and UNSECURE**.

### What is a secure hash algorithm?

As date of writing a SHA-256 is secure enough but if possible you should change to the SHA-3-variants as they are more secure and faster.

## :warning: Security warning :warning:

**Please use this function only for migration and not for actual (new written) software projects as it is UNSECURE**.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solution in code and an online compiler that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../GenerateMd5/GenerateMd5.java) | :white_check_mark: | [replit.com CpcJavaGenerateMd5#Main.java](https://replit.com/@javacrypto/CpcJavaGenerateMd5#Main.java/)
| [PHP](../GenerateMd5/GenerateMd5.php) | :white_check_mark: | [replit.com CspPhpGenerateMd5](https://replit.com/@javacrypto/CpcPhpGenerateMd5#main.php/)
| [C#](../GenerateMd5/GenerateMd5.cs) | :white_check_mark: | [replit.com CpcCsharpGenerateMd5#main.cs](https://replit.com/@javacrypto/CpcCsharpGenerateMd5#main.cs/)
| [Javascript CryptoJs](../GenerateMd5/GenerateMd5CryptoJs.js) | :white_check_mark: | [replit.com CpcCryptoJsGenerateMd5](https://replit.com/@javacrypto/CpcCryptoJsGenerateMd5#index.js/)
| [NodeJS Crypto](../GenerateMd5/GenerateMd5NodeJsCrypto.js) | :white_check_mark: | [replit.com CpcNodeJsGenerateMd5#index.js](https://replit.com/@javacrypto/CpcNodeJsCryptoGenerateMd5#index.js/)
| [NodeJS forge](../GenerateMd5/GenerateMd5NodeJs.js) | :white_check_mark: | [replit.com CpcNodeJsGenerateMd5#index.js](https://replit.com/@javacrypto/CpcNodeJsGenerateMd5#index.js/)
| [Python](../GenerateMd5/GenerateMd5.py) | :white_check_mark: | [replit.com CpcPythonGenerateMd5#main.java](https://replit.com/@javacrypto/CpcPythonGenerateMd5#main.py/)
| [Go](../GenerateMd5/GenerateMd5.go) | :white_check_mark: | [replit.com CpcGoGenerateMd5#main.java](https://replit.com/@javacrypto/CpcGoGenerateMd5#main.py/)
| [Dart](../GenerateMd5/GenerateMd5.dart) *1) | :white_check_mark: | no online compiler available

*1) you need the external library pointycastle version 3.1.1

This is an output:

```plaintext
Generate a MD5 hash

# # # SECURITY WARNING: This code is provided for achieve    # # #
# # # compatibility between different programming languages. # # #
# # # It is NOT SECURE - DO NOT USE THIS CODE ANY LONGER !   # # #
# # # The hash algorithm MD5 is BROKEN.                      # # #
# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #

plaintext:                The quick brown fox jumps over the lazy dog
md5Value (hex) length: 16 data: 9e107d9d372bb6826bd81d3542a419d6

```

Last update: Oct. 18th 2021

Back to the main page: [readme.md](../readme.md)
