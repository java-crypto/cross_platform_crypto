# Cross-platform cryptography

## RSA string decryption with OAEP SHA1 padding

This is the shortened version of [RSA string encryption with OAEP SHA1 padding](rsaencryptionoaepsha1string.md). Receiving encrypted data requires a **decryption** of the ciphertext. For the <u>decryption</u> the **Private key** of the recipient is used.

One note about the **key management** in my programs: usually the keys are (securely) stored in files or a key store and may have additional password protection. Both scenarios are unhandy in demonstration programs running in an online compiler. That's why I'm using <u>static, hard-coded</u> keys in my programs - **please do not do this in production environment! Never ever store a Private Key in the source!** The minimum is to load the keys from a secured device; simply uncomment a line in the code to load the key from a file.

### steps in the program

The program follows the usual sequence:
1. receive encrypted data in Base64 encoding
2. start the decryption process
3. load the Private key
4. Base64 decoding of the ciphertext
5. set the decryption parameters (same as used for encrypting)
11. decrypt the ciphertext to the "decryptedtext" and show the result.

If you like to see the **full encryption with decryption** see my separate article [RSA string encryption with OAEP SHA1 padding](rsaencryptionoaepsha1string.md).

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](RsaEncryptionOaepSha1String/RsaDecryptionOaepSha1.java) | :white_check_mark: | [repl.it CpcJavaRsaDecryptionOaepSha1String](https://repl.it/@javacrypto/CpcJavaRsaDecryptionOaepSha1String/)
| [PHP](RsaEncryptionOaepSha1String/RsaDecryptionOaepSha1.php) | :white_check_mark: | [repl.it CpcPhpRsaDecryptionOaepSha1String](https://repl.it/@javacrypto/CpcPhpRsaDecryptionOaepSha1String#main.php/)
| [C#](RsaEncryptionOaepSha1String/RsaDecryptionOaepSha1.cs) | :white_check_mark: | [repl.it CpcCsharpRsaDecryptionOaepSha1String](https://repl.it/@javacrypto/CpcCsharpRsaDecryptionOaepSha1String#main.cs/)
| Javascript CryptoJs | :x: | the encryption functionality is not available in CryptoJs
| [NodeJS Crypto](RsaEncryptionOaepSha1String/RsaDecryptionOaepSha1NodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoRsaDecryptionOaepSha1String](https://repl.it/@javacrypto/CpcNodeJsCryptoRsaDecryptionOaepSha1String#index.js/)
| [NodeJS forge](RsaEncryptionOaepSha1String/RsaDecryptionOaepSha1NodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsRsaDecryptionOaepSha1String](https://repl.it/@javacrypto/CpcNodeJsRsaDecryptionOaepSha1String#index.js/)

This is an output (your will differ because a random element):

```plaintext
RSA 2048 decryption OAEP SHA-1 string

* * * decrypt the ciphertext with the RSA private key * * *
ciphertextReceivedBase64: In0CE7VSJlr6PT+fl9SCejWhNZYioVn1UzYaqYUms7Y63oi4xK/2Qrgyi5a3CnvappM1MHdDaZVc+bDzl/iBiUslkHcF8lWGD4YdKuJgtfSS8WBRuIxz78EFhU3TfMU3y0v0bhUUj1/6ZdO/p9j8KcNbnpEB8A9YW+bZC1qOO8ibnTNbb3RHQekafz6r9oAYsILNga1pi9ZlyXQPYi7VpWAeZmUOq+MHEgD/Nkq/oxkyMo/yf5SVk1ig8M5Lr6arj22r2ePpQ8j3onmN7aOTCWUhZ7FdLm7IiRYebZ3MWPWozt9O6BSNjALLDXm7NfKaYcgQ+bVXrA4k1M9f4M9OEA==
decryptedtext: The quick brown fox jumps over the lazy dog

```

Last update: Nov. 11th 2020

Back to the main page: [readme.md](readme.md)