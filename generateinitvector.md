# Cross-platform cryptography

## Generate a secure random initialization vector (IV)

Some AES encryption modes (e.g. CBC) need a random generated initialization vector (iv). The codes will generate an iv with size of 16 byte = 128 bit.

The iv output format is a byte array but when sending this iv to another system or persons you often need another (string based) format - so I'm providing the iv also as a Base64 encoded string.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

The following links provide the solution in code and within an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](GenerateInitvector/Main.java) | :white_check_mark: | [repl.it CpcJavaGenerateRandomInitvector#Main.java](https://repl.it/@javacrypto/CpcJavaGenerateRandomInitvector#Main.java/)
| [PHP](GenerateInitvector/GenerateRandomInitvector.php) | :white_check_mark: | [repl.it CpcPhpGenerateInitvector#main.php](https://repl.it/@javacrypto/CpcPhpGenerateInitvector#main.php/)
| [C#](GenerateInitvector/GenerateRandomInitvector.cs) | :white_check_mark: | [repl.it CpcCsharpGenerateRandomInitvector#main.cs](https://repl.it/@javacrypto/CpcCsharpGenerateRandomInitvector#main.cs/)
| [CryptoJs](GenerateInitvector/GenerateRandomInitvector.cryptoJs) | :white_check_mark: | [repl.it CpcCryptoJsGenerateRandomInitvector](https://repl.it/@javacrypto/CpcCryptoJsGenerateRandomInitvector/)
| [NodeJS](GenerateInitvector/GenerateRandomInitvector.nodeJs) | :white_check_mark: | [repl.it CpcNodeJsGenerateRandomInitvector](https://repl.it/@javacrypto/CpcNodeJsGenerateRandomInitvector/)

This is an output (as there is a random element your output will differ):

```plaintext
Generate a 16 byte long Initialization vector (IV)
generated iv length: 16 base64: PnujuygIgaMxwdQRWz9wvA==
```


Last update: Nov. 4th 2020

Back to the main page: [readme.md](readme.md)