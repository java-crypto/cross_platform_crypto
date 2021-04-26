# Cross-platform cryptography

## RSA encrypted private key example using signature with PKCS#1.5 padding

The examples in this article are a modified version of [RSA string signature with PKCS#1.5 padding](rsa_signature_string.md) but using an encrypted private key. So I don't describe the signature methods but the differences to use the encrypted key.



## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../RsaLoadEncryptedPrivateKey/RsaLoadEncryptedPrivateKey.java) | :white_check_mark: | [repl.it CpcJavaRsaStringSignatureFull](https://repl.it/@javacrypto/CpcJavaRsaSignatureStringFull#Main.java/)
| [PHP](../RsaLoadEncryptedPrivateKey/RsaLoadEncryptedPrivateKey.php) | :white_check_mark: | [repl.it CpcPhpRsaSignatureStringFull](https://repl.it/@javacrypto/CpcPhpRsaSignatureStringFull#main.php/)
| [C#](../RsaLoadEncryptedPrivateKey/RsaLoadEncryptedPrivateKey.cs) | :white_check_mark: | [repl.it CpcCsharpRsaSignatureStringFull](https://repl.it/@javacrypto/CpcCsharpRsaSignatureStringFull#main.cs/)
| Javascript CryptoJs | :x: | the signature functionality is not available in CryptoJs
| [NodeJS Crypto](../RsaLoadEncryptedPrivateKey/RsaLoadEncryptedPrivateKeyNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoRsaSignatureStringFull](https://repl.it/@javacrypto/CpcNodeJsCryptoRsaSignatureStringFull#index.js/)
| [NodeJS forge](../RsaLoadEncryptedPrivateKey/RsaLoadEncryptedPrivateKeyNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsRsaSignatureStringFull](https://repl.it/@javacrypto/CpcNodeJsRsaSignatureStringFull#index.js/)
| [Python](../RsaLoadEncryptedPrivateKey/RsaLoadEncryptedPrivateKey.py) *1) | :white_check_mark: | [repl.it CpcPythonRsaStringSignatureFull](https://repl.it/@javacrypto/CpcPythonRsaSignatureStringFull#main.py/)
| [Go](../RsaLoadEncryptedPrivateKey/RsaLoadEncryptedPrivateKey.go) | :white_check_mark: | [repl.it CpcGoRsaStringSignatureFull](https://repl.it/@javacrypto/CpcGoRsaSignatureStringFull#main.go/)

*1) you need the external library pycryptodome, version 3.9.9

This is an output:

```plaintext
Load RSA PKCS8 encrypted private key AES256-CBC PBKDF2 HMAC with SHA-256

dataToSign: The quick brown fox jumps over the lazy dog

* * * sign the plaintext with the encrypted RSA private key * * *

signature (Base64): vCSB4744p30IBl/sCLjvKm2nix7wfScQn99YX9tVIDNQIvU3QnSCLc2cF+J6R9WMRIXOsY94MxjKCQANW0CuaSs+w31ePHaounFVnmXyY092SicZrtpwlxw2CHqJ0NSyciDpxlRId1vjKlp9E5IJmYtVMtL2hfb711P+nb+m+1sPplNXPpJpdnWIzfLsDMVxCkplAdrcoH2HuWgOtOCHAf3vWbUC/vkvi388NT1UXJRPoERM0m1v11ogP9DiycMdoJxg3fbdH3HknbR02MLNEr7q4ZMzlrKxnYChwp2hnnBvJDcXpPDnQz7sG8zrim1nL/PS8CRG5lxhYYAZqTc+Vg==

* * * verify the signature against the plaintext with the RSA public key * * *

signature (Base64) verified: true

```

Last update: Apr. 25th 2021

Back to the main page: [readme.md](../readme.md)
