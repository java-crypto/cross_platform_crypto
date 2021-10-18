# Cross-platform cryptography

## Generate a secure random initialization vector (IV)

Some AES encryption modes (e.g. CBC) need a random generated initialization vector (iv). The codes will generate an iv with size of 16 byte = 128 bit.

The iv output format is a byte array but when sending this iv to another system or persons you often need another (string based) format - so I'm providing the iv also as a Base64 encoded string.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solution in code and within an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../GenerateInitvector/GenerateRandomInitvector.java) | :white_check_mark: | [replit.com CpcJavaGenerateRandomInitvector#Main.java](https://replit.com/@javacrypto/CpcJavaGenerateRandomInitvector#Main.java/)
| [PHP](../GenerateInitvector/GenerateRandomInitvector.php) | :white_check_mark: | [replit.com CpcPhpGenerateInitvector#main.php](https://replit.com/@javacrypto/CpcPhpGenerateInitvector#main.php/)
| [C#](../GenerateInitvector/GenerateRandomInitvector.cs) | :white_check_mark: | [replit.com CpcCsharpGenerateRandomInitvector#main.cs](https://replit.com/@javacrypto/CpcCsharpGenerateRandomInitvector#main.cs/)
| [Javascript CryptoJs](../GenerateInitvector/GenerateRandomInitvectorCryptoJs.js) | :white_check_mark: | [replit.com CpcCryptoJsGenerateRandomInitvector](https://replit.com/@javacrypto/CpcCryptoJsGenerateRandomInitvector/)
| [NodeJS CryptoJs](../GenerateInitvector/GenerateRandomInitvectorNodeJsCrypto.js) | :white_check_mark: | [replit.com CpcNodeJsGenerateRandomInitvector](https://replit.com/@javacrypto/CpcNodeJsGenerateRandomInitvector/)
| [NodeJS forge](../GenerateInitvector/GenerateRandomInitvectorNodeJs.js) | :white_check_mark: | [replit.com CpcNodeJsGenerateRandomInitvector](https://replit.com/@javacrypto/CpcNodeJsGenerateRandomInitvector/)
| [Python](../GenerateInitvector/GenerateRandomKeyIvNonce.py) | :white_check_mark: | [replit.com CpcPythonGenerateRandomKeyIvNonce#main.py](https://replit.com/@javacrypto/CpcPythonGenerateRandomKeyIvNonce#main.py/)
| [Go](../GenerateInitvector/GenerateAesKeyIvNonce.go) *2) | :white_check_mark: | [replit.com CpcGoGenerateRandomAesKey#Main.go](https://replit.com/@javacrypto/CpcGoGenerateKeyIvNonce#main.go/)
| [Dart](../GenerateAesKey/GenerateRandomKeyIvNonce.dart) *1) | :white_check_mark: | no online compiler available

*1) you need the external library pointycastle version 3.1.1

*2) the Go version is a combined one: generate AES key, IV and nonce in one program

This is an output (as there is a random element your output will differ):

```plaintext
Generate a 16 byte long Initialization vector (IV)
generated iv length: 16 base64: PnujuygIgaMxwdQRWz9wvA==
```

Last update: Oct. 18th 2021

Back to the main page: [readme.md](../readme.md)