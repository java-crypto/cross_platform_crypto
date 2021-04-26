# Cross-platform cryptography

## Curve 25519 key generation

The programs will show how to generate a curve 25519 key pair that later will be used for a secure key exchange between two parties. For a more detailed information about the curve kindly see the [Wikipedia Curve 25519 article](https://en.wikipedia.org/wiki/Curve25519).

### What are the benefits to use curve 25519 and not (classic) Diffie Hellman?

There are several "pro's" to use th algorithms based on curve 25519:

* It is one of the fastest ECC curves and is not covered by any known patents.
* The keys are short (just 32 bytes for the private and public key)
* the key exchange is supported on all platforms (Java, PHP, C# and Javascript) with some small libraries

### How do you use the generated keys?

In the usual workflow you generate the key pair once, publish the public key (e.g. on a website, email chat) and keep the private key secret. When receiving some encrypted data from someone you get his public key as well (e.g. accompanied with the encrypted message, on his website, via chat) to decrypt the message.

The complete workflow for an key exchange with AES encryption is shown here: [curve25519 key exchange aes cbc 256 string encryption](curve25519_key_exchange_aes_cbc_256_string_encryption.md).

### What additional libraries do I need to run the examples?

| Language | library | link
| ------ | :---: | :---- |
| Java | curve25519-java-0.5.0.jar | [https://mvnrepository.com/artifact/org.whispersystems/curve25519-java](https://mvnrepository.com/artifact/org.whispersystems/curve25519-java/) |
| PHP | Curve25519.php | [https://github.com/lt/PHP-Curve25519](https://github.com/lt/PHP-Curve25519/) |
| C# | Curve25519.cs | [https://github.com/hanswolff/curve25519](https://github.com/hanswolff/curve25519/) |
| Javascript | axlsign.js | [https://github.com/wavesplatform/curve25519-js](https://github.com/wavesplatform/curve25519-js/) |
| Javascript | buffer-to-uint8array.js | [https://github.com/substack/buffer-to-uint8array](https://github.com/substack/buffer-to-uint8array/) |

The following links provide the solution in code and within an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../Curve25519KeyGeneration/GenerateCurve25519Keypair.java) | :white_check_mark: | [jdoodle.com  CpcJavaGenerateCurve25519Keypair](https://jdoodle.com/a/2EHr/)
| [PHP](../Curve25519KeyGeneration/GenerateCurve25519Keypair.php) | :white_check_mark: | [repl.it CpcPhpCurve25519KeyGeneration](https://repl.it/@javacrypto/CpcPhpCurve25519KeyGeneration/)
| [C#](../Curve25519KeyGeneration/GenerateCurve25519Keypair.cs) | :white_check_mark: | [repl.it CpcCsharpCurve25519KeyGeneration#main.css](https://repl.it/@javacrypto/CpcCsharpCurve25519KeyGeneration#main.cs/)
| [Javascript CryptoJs](../Curve25519KeyGeneration/GenerateCurve25519KeypairCryptoJs.js) | :white_check_mark: | [repl.it CpcCryptoJsCurve25519KeyGeneration#index.js](https://repl.it/@javacrypto/CpcCryptoJsCurve25519KeyGeneration#index.js/)
| [NodeJS Crypto](../Curve25519KeyGeneration/GenerateCurve25519KeypairNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoCurve25519KeyGeneration#index.js](https://repl.it/@javacrypto/CpcNodeJsCryptoCurve25519KeyGeneration#index.js/)
| NodeJS forge | :x: | please use the built-in Node Crypto-solution above

This is the output with the (Base64 encoded) Curve 25519 <u>private key</u> and  the belonging <u>public key</u>:

```plaintext
Generate a key pair for Curve25519
base64 encoded key data:
Curve25519 PrivateKey: KOrUWVTgb7F+1KdpLn+2lRQDeeCuDaMkrhQ5ke6P4HM=
Curve25519 PublicKey:  BwMqVfr1myxqX8tikIPYCyNtpHgMLIg/2nUE+pLQnTE=
```

A last note on Curve 25519 key pair generation: Especially the private key contains very sensitive data so it need to get protected. 

Go to the complete workflow for an key exchange with AES encryption: [curve25519 key exchange aes cbc 256 string encryption](curve25519_key_exchange_aes_cbc_256_string_encryption.md).

Last update: Jan. 08th 2021

Back to the main page: [readme.md](../readme.md)
