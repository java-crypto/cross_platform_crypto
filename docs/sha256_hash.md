# Cross-platform cryptography

## SHA-256 hash of a string

Hashing of data is one of the most function used when working with cryptographic.

### What is a hash?

The hash of data is often described as "fingerprint" of the data as the result is a kind of "checksum" that is characteristic for the data. An other name for a hash is a trapdoor as there should be no way back meaning: with having a hash value it should not been able to get the data back or even think what data was used as input.

### How secure is a hash?

Very import for hash function is it to change a lot of bytes even when just 1 byte of the plaintext changes. As hashes are very often used in combination with digital signatures it is most important that you cannot create a changed or second document that gets the same hash. One example for an **unsecure hash algorithm is MD5** - please don't use it ever! Visit the website [https://natmchugh.blogspot.com/2015/02/create-your-own-md5-collisions.html](https://natmchugh.blogspot.com/2015/02/create-your-own-md5-collisions.html) and see the two pictures - one is a crashing plane and the other is the wrack of a sunken ship - both have the **same MD5 hash**. This of second contract that was digitally signed but is total different to that you have signed... horrible.

The second **unsecure hash algorithm is SHA-1** - please use it not more longer in new programs. A team of developers tryed to find a second PDF-document that has the same SHA-1 value as the first one. With the kindly help of Google's machine power they find it - read the complete story on [http://shattered.it/](http://shattered.it/). Same to MD5 - do not use SHA-1 in new programs.

### What is a secure hash algorithm?

As date of writing a SHA-256 is secure enough but if possible you should change to the SHA-3-variants as they are more secure and faster.

### What hash algorithm are we using?

My programs will use SHA-256 as default algorithm but in rare cases I'm using a signature algorithm with a SHA-1 hashing.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solution in code and within an online compiler that runs the code. Kindly note that three articles use the same example: [Base64 encoding & decoding](base64_encoding_decoding.md), [binary data to a hex string & back](binary_data_hex_string.md) and [SHA256 hashing](sha256_hash.md).

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../GenerateSha256Base64Hex/GenerateSha256Base64Hex.java) | :white_check_mark: | [repl.it CpcJavaGenerateSha256Base64Hex#Main.java](https://repl.it/@javacrypto/CpcJavaGenerateRandomAesKey#Main.java/)
| [PHP](../GenerateSha256Base64Hex/GenerateSha256Base64Hex.php) | :white_check_mark: | [repl.it CspPhpGenerateSha256Base64Hex](https://repl.it/@javacrypto/CpcPhpGenerateSha256Base64Hex#main.php/)
| [C#](../GenerateSha256Base64Hex/GenerateSha256Base64Hex.cs) | :white_check_mark: | [repl.it CpcCsharpGenerateSha256Base64Hex#main.cs](https://repl.it/@javacrypto/CpcCsharpGenerateSha256Base64Hex#main.cs/)
| [Javascript CryptoJs](../GenerateSha256Base64Hex/GenerateSha256Base64HexCryptoJs.js) | :white_check_mark: | [repl.it CpcCryptoJsGenerateSha256Base64Hex](https://repl.it/@javacrypto/CpcCryptoJsGenerateSha256Base64Hex#index.js/)
| [NodeJS Crypto](../GenerateSha256Base64Hex/GenerateSha256Base64HexNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsGenerateSha256Base64Hex#index.js](https://repl.it/@javacrypto/CpcNodeJsCryptoGenerateSha256Base64Hex#index.js/)
| [NodeJS forge](../GenerateSha256Base64Hex/GenerateSha256Base64HexNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsGenerateSha256Base64Hex#index.js](https://repl.it/@javacrypto/CpcNodeJsGenerateSha256Base64Hex#index.js/)
| [Webcrypto](../GenerateSha256Base64Hex/generatesha256base64hex.html) | :white_check_mark: | [your browser WebcryptoGenerateSha256Base64Hex.html](https://java-crypto.github.io/cross_platform_crypto/GenerateSha256Base64Hex/generatesha256base64hex.html)
| [Python](../GenerateSha256Base64Hex/GenerateSha256Base64Hex.py) | :white_check_mark: | [repl.it CpcPythonGenerateSha256Base64Hex#main.java](https://repl.it/@javacrypto/CpcPythonGenerateSha256Base64Hex#main.py/)
| [Dart](../GenerateSha256Base64Hex/GenerateSha256Base64Hex.dart) *1) | :white_check_mark: | no online compiler available

*1) you need the external library pointycastle version 3.1.1

This is an output:

```plaintext
Generate a SHA-256 hash, Base64 en- and decoding and hex conversions
plaintext:                The quick brown fox jumps over the lazy dog
sha256Value (hex) length: 32 data: d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592
sha256Value (base64):     16j7swfXgJRpypq8sAguT41WUeRtPNt2LQLQvzfJ5ZI=
sha256Base64 decoded to a byte array:
sha256Value (hex) length: 32 data: d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592
sha256HexString converted to a byte array:
sha256Value (hex) length: 32 data: d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592
```

Last update: Aug. 24th 2021

Back to the main page: [readme.md](../readme.md)