# Cross-platform cryptography

## Base64 encoding & decoding

When using my programs you will see these conversions are in nearly every source code.

### Why is Base64 encoding so important?

The answer is very simple: because all types of cryptography are working with binary data but when trying to transport the data (e.g. a ciphertext) from one system to another system you encounter the problem - "how should I transport binary data?".

As binary data can consist of bytes that are not directly printable as a string (e.g. value hex x00) we need to convert the data to a better (string) encoding. In general there are two encodings on the market - the **Base64 encoding** and the **hex string representation** of the binary data.

This article focuses on the Base64 encoding, if you like to know more about the hex string representation of binary data see my article [binary data to a hex string & back](binary_data_hex_string.md).

### How does Base64 encoding work?

A byte can hold up to 256 values that need to find a string representation. When trying to use only the letters a..z, A..Z and numbers 0..9 and 3 extra symbols like "/", "+" and "=" we get a character set of (in total) 65 characters - much to low for the necessary 256 characters. So trick is not to use the 8 bit of byte but only 6 bit of the byte sequence, convert this 6 bit chunk into a character of the set and in the end all bytes get converted.

This procedure will exand the length of encoded data about 33% but those characters are available on nearly every western operating system. The decoding goes the other way round and there is the "problem" with a padding that is done with the "=" character because not all byte sequences are divisible by "6".

If need more detailed information about this process kindly see the [Wikipedia Base64 article](https://en.wikipedia.org/wiki/Base64).

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