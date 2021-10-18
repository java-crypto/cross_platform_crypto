# Cross-platform cryptography

## Generate a secure random AES key

One of the most important fact for secure cryptography is to use an encryption key that is generated randomly and **not** by taking a passwordphrase and simply convert it to a key.

The codes will generate an AES key with size of 32 byte = 256 bit that is the maximum key size available for AES encryption.

The key output format is a byte array but when sending this key to another system or persons you often need another (string based) format - so I'm providing the key also as a Base64 encoded string.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solution in code and within an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../GenerateAesKey/GenerateRandomAesKey.java) | :white_check_mark: | [replit.com CpcJavaGenerateRandomAesKey#Main.java](https://replit.com/@javacrypto/CpcJavaGenerateRandomAesKey#Main.java/)
| [PHP](../GenerateAesKey/GenerateRandomAesKey.php) | :white_check_mark: | [replit.com CspPhpGenerateRandomAesKey](https://replit.com/@javacrypto/CspPhpGenerateRandomAesKey/)
| [C#](../GenerateAesKey/GenerateRandomAesKey.cs) | :white_check_mark: | [replit.com CpcCsharpGenerateRandomAesKey#main.cs](https://replit.com/@javacrypto/CpcCsharpGenerateRandomAesKey#main.cs/)
| [Javascript CryptoJs](../GenerateAesKey/GenerateRandomAesKeyCryptoJs.js) | :white_check_mark: | [replit.com CpcCryptoJsGenerateRandomAesKey](https://replit.com/@javacrypto/CpcCryptoJsGenerateRandomAesKey/)
| [NodeJS Crypto](../GenerateAesKey/GenerateRandomAesKeyNodeJsCrypto.js) | :white_check_mark: | [replit.com CpcNodeJsGenerateRandomAesKey#index.js](https://replit.com/@javacrypto/CpcNodeJsGenerateRandomAesKey#index.js/)
| [NodeJS forge](../GenerateAesKey/GenerateRandomAesKeyNodeJs.js) | :white_check_mark: | [replit.com CpcNodeJsGenerateRandomAesKey#index.js](https://replit.com/@javacrypto/CpcNodeJsGenerateRandomAesKey#index.js/)
| [Python](../GenerateAesKey/GenerateRandomKeyIvNonce.py) | :white_check_mark: | [replit.com CpcPythonGenerateRandomKeyIvNonce#main.py](https://replit.com/@javacrypto/CpcPythonGenerateRandomKeyIvNonce#main.py/)
| [Go](../GenerateAesKey/GenerateAesKeyIvNonce.go) *2) | :white_check_mark: | [replit.com CpcGoGenerateRandomAesKey#Main.go](https://replit.com/@javacrypto/CpcGoGenerateKeyIvNonce#main.go/)
| [Dart](../GenerateAesKey/GenerateRandomKeyIvNonce.dart) *1) | :white_check_mark: | no online compiler available

*1) you need the external library pointycastle version 3.1.1

*2) the Go version is a combined one: generate AES key, IV and nonce in one program

This is an output (as there is a random element your output will differ):

```plaintext
Generate a 32 byte long AES key
generated key length: 32 base64: a9VVidAcHk6mv09zGSfH6ZiWNWSsjt/gryi6UcnMdAg=
```

Last update: Oct. 18th 2021

Back to the main page: [readme.md](../readme.md)