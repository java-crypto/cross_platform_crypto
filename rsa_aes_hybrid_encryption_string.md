# Cross-platform cryptography

## RSA AES CBC hybrid encryption

This article is a combined solution, based on [RSA OEAP padding SHA-1 String](rsa_encryption_oaep_sha1_string.md) and [AES CBC-mode String Encryption](aes_cbc_256_string_encryption.md). Here I'm leaving out details to the encryption schemes, kindly check the information on the linked articles.

### What is the workflow of a hybrid encryption?

When doing a **symmetric** encryption it is important to exchange the encryption key on a secure channel, what is not easy when thinking of Email or other electronic media. A better way is the usage of **asymmetric** encryption like RSA but there is the limitation to some (hundreds) of byte plaintext that can get encrypted.

The solution is to combine both encryption schemes into a (more) secure one - the **hybrid** encryption.

The encryption of the plaintext is done with e.g. AES CBC-mode encryption and results in ciphertext. The key is generated randomly but **not** exchanged unencrypted. We encrypt the 32 bytes long AES key with the Public key of the recipient and send the encrypted AES key along with the ciphertext to the recipient.

The recipient will first decrypt the AES key with his Private key, resulting in the unencrypted AES key. The second step is the decryption of the ciphertext to the decryptedtext by using the AES encryption key.

My example programs will do all the steps in a combined program. As my examples are programmed for an easier exchange on different frameworks they do have some unnecessary conversions.

I do not provide a "decryption only" version as all functions are available in the full version.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](RsaAesCbcHybridEncryptionString/RsaAesCbcHybridEncryption.java) | :white_check_mark: | [repl.it CpcJavaRsaAesCbcHybridStringEncryption](https://repl.it/@javacrypto/CpcJavaRsaAesCbcHybridStringEncryption#Main.java/)
| [PHP](RsaAesCbcHybridEncryptionString/RsaAesCbcHybridEncryption.php) | :white_check_mark: | [repl.it CpcPhpRsaAesCbcHybridStringEncryption](https://repl.it/@javacrypto/CpcPhpRsaAesCbcHybridStringEncryption#main.php/)
| [C#](RsaAesCbcHybridEncryptionString/RsaAesCbcHybridEncryption.cs) | :white_check_mark: | [repl.it CpcCsharpRsaAesCbcHybridStringEncryption](https://repl.it/@javacrypto/CpcCsharpRsaAesCbcHybridStringEncryption#main.cs/)
| Javascript CryptoJs | :x: | the encryption functionality is not available in CryptoJs
| [NodeJS Crypto](RsaAesCbcHybridEncryptionString/RsaAesCbcHybridEncryptionNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoRsaAesCbcHybridStringEncryption](https://repl.it/@javacrypto/CpcNodeJsCryptoRsaAesCbcHybridStringEncryption#index.js/)
| [NodeJS forge](RsaAesCbcHybridEncryptionString/RsaAesCbcHybridEncryptionNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsRsaAesCbcHybridStringEncryption](https://repl.it/@javacrypto/CpcNodeJsRsaAesCbcHybridStringEncryption#index.js/)

This is an output (your will differ because a random element):

```plaintext
RSA AES CBC hybrid encryption

uses AES CBC 256 random key for the plaintext encryption
and RSA OAEP SHA 1 2048 bit for the encryption of the random key

plaintext: The quick brown fox jumps over the lazy dog

* * * Encryption * * *
complete ciphertext: V+SEAPM94C8p7bMDqT913pouj3BJngEzGYQC6mkthdjUSC8gq1cW1oTlUjcGYz3O8MAqsHmuUIB/xanTcwKwojA75dMgIzlkZBkmYLL4LpCRdOCLjG8M02AUlSkyYP0GgTiGPjDzEDfhO04wHsFTHXFEj5JQI+Vbm6BMxy2E9yv1b1Gy2M6xg4Tem6LtsXFEEo3WOjeaa+ZBCuIoaiEK9stZdPcjCu8knfLlfaEQhTX4FteBc5/zqNXqxSGF51g130BsLdCOuSuPkMCWH2CZorwZEW/JyHrnkZIt5v5udfa9KH4iLsKAQSH/m7JJeK58hH4NxtfknVV6k5cT7GfRvA==:42v3tcSdVkTFPa7s0yZhjA==:60pvj3Q+F21GA34JhWdmEpIh4Vv87KhP2bJJZdLHKfTCNOG5SXZB3ZSni7W2+sVQ
output is (Base64) encryptionKey : (Base64) ciphertext

* * * Decryption * * *
completeCiphertextReceived: V+SEAPM94C8p7bMDqT913pouj3BJngEzGYQC6mkthdjUSC8gq1cW1oTlUjcGYz3O8MAqsHmuUIB/xanTcwKwojA75dMgIzlkZBkmYLL4LpCRdOCLjG8M02AUlSkyYP0GgTiGPjDzEDfhO04wHsFTHXFEj5JQI+Vbm6BMxy2E9yv1b1Gy2M6xg4Tem6LtsXFEEo3WOjeaa+ZBCuIoaiEK9stZdPcjCu8knfLlfaEQhTX4FteBc5/zqNXqxSGF51g130BsLdCOuSuPkMCWH2CZorwZEW/JyHrnkZIt5v5udfa9KH4iLsKAQSH/m7JJeK58hH4NxtfknVV6k5cT7GfRvA==:42v3tcSdVkTFPa7s0yZhjA==:60pvj3Q+F21GA34JhWdmEpIh4Vv87KhP2bJJZdLHKfTCNOG5SXZB3ZSni7W2+sVQ
plaintext:  The quick brown fox jumps over the lazy dog

```

Last update: Dec. 16th 2020

Back to the main page: [readme.md](readme.md)