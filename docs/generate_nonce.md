# Cross-platform cryptography

## Generate a secure random nonce

Some AES encryption modes need a random generated nonce. The codes will generate an nonce with size of 12 byte = 96 bit. The nonce is especially used for AES in mode GCM programs. 

The nonce output format is a byte array but when sending this nonce to another system or persons you often need another (string based) format - so I'm providing the nonce also as a Base64 encoded string.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solution in code and within an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../GenerateNonce/GenerateRandomNonce.java) | :white_check_mark: | [replit.com CpcJavaGenerateRandomNonce#Main.java](https://replit.com/@javacrypto/CpcJavaGenerateRandomNonce#Main.java/)
| [PHP](../GenerateNonce/GenerateRandomNonce.php) | :white_check_mark: | [replit.com CpcPhpGenerateNonce#main.php](https://replit.com/@javacrypto/CpcCsharpGenerateRandomNonce#main.php/)
| [C#](../GenerateNonce/GenerateRandomNonce.cs) | :white_check_mark: | [replit.com CpcCsharpGenerateRandomNonce#main.cs](https://replit.com/@javacrypto/CpcCsharpGenerateRandomNonce#main.cs/)
| [Javascript CryptoJs](../GenerateNonce/GenerateRandomNonceCryptoJs.js) | :white_check_mark: | [replit.com CpcCryptoJsGenerateRandomNonce](https://replit.com/@javacrypto/CpcCryptoJsGenerateRandomNonce/)
| [NodeJS Crypto](../GenerateNonce/GenerateRandomNonceNodeJsCrypto.js) | :white_check_mark: | [replit.com CpcNodeJsCryptoGenerateRandomNonce](https://replit.com/@javacrypto/CpcNodeJsCryptoGenerateRandomNonce/)
| [NodeJS forge](../GenerateNonce/GenerateRandomNonceNodeJs.js) | :white_check_mark: | [replit.com CpcNodeJsGenerateRandomNonce](https://replit.com/@javacrypto/CpcNodeJsGenerateRandomNonce/)
| [Python](../GenerateNonce/GenerateRandomKeyIvNonce.py) | :white_check_mark: | [replit.com CpcPythonGenerateRandomKeyIvNonce#main.py](https://replit.com/@javacrypto/CpcPythonGenerateRandomKeyIvNonce#main.py/)
| [Go](../GenerateNonce/GenerateAesKeyIvNonce.go) *2) | :white_check_mark: | [replit.com CpcGoGenerateRandomAesKey#Main.go](https://replit.com/@javacrypto/CpcGoGenerateKeyIvNonce#main.go/)
| [Dart](../GenerateAesKey/GenerateRandomKeyIvNonce.dart) *1) | :white_check_mark: | no online compiler available

*1) you need the external library pointycastle version 3.1.1

*2) the Go version is a combined one: generate AES key, IV and nonce in one program

This is an output (as there is a random element your output will differ):

```plaintext
Generate a 12 byte long nonce for AES GCM
generated nonce length: 12 base64: iK+wXGH26uFN+XFX
```

Last update: Oct. 18th 2021

Back to the main page: [readme.md](../readme.md)