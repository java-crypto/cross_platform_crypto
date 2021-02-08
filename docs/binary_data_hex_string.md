# Cross-platform cryptography

## Binary data to a hex string & back

The hex string is second most encoding to represent or transport binary data. 

### Why is the hex string not so often in use?

The answer is easy: each byte is converted into 2 characters (character set 0..9 and a..f) so this encoding **doubles** the size of data.

### Why is a hex string so often used?

Especially when in debugging state of development phase the programmer wants to see what is in a variable and that can be better done than using a Base64 encoding or using decimals.

### How does it work?

As a byte has a range of 256 numbers (decimal 0..255) the number is pushed into a 16 number base (compared to our decimal number space of 10) so we need to hexadecimal characters to get the value of one byte.

My implementations will provide the character in low or high chars, depending on the default of the framework.

This article focuses on the hex string encoding, if you like to know more about the Base64 representation of binary data see my article [Base64 encoding & decoding](base64_encoding_decoding.md).

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

Last update: Feb. 08th 2021

Back to the main page: [readme.md](../readme.md)