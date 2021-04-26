# Cross-platform cryptography

## RSA string encryption with OAEP SHA-256 padding

You maybe surprised about the long and unhandy title of the article but the RSA encryption has many (different) encryption types so it is important to name them correctly.

The general "problem" with RSA encryption is the limited ability to encrypt large sized plaintext. The size depends on the key size and can get calculated with this general formula

```plaintext
maximum plaintext to encrypt = key size in bytes - padding
```
Taking our RSA sample key of 2048 bit = 256 byte we get:

```plaintext
maximum plaintext to encrypt = 256 - 42 = 214 byte
```

This size is good for short strings or - that is the way it should be done for larger sizes - using a [**hybrid encryption scheme**](rsa_aes_hybrid_encryption_string.md). In short it uses a randomly created AES key of 32 byte, encrypts the data with this key in AES CBC- or (better) GCM- mode and encrypts the key with the RSA public key (and vice versa with the RSA private key for decryption).

Using the RSA encryption there are three padding modes: "no padding", "PKCS1.5 padding" or "OAEP padding". As the first two named are very vulnerable please don't use them, instead use an **OAEP padding** because it adds a random parameter so the ciphertext will differ each time you encrypt (even with an equal key and plaintext).

The OAEP-padding includes a hashing algorithm that is not always named by the algorithm name so there is a high chance that the Cross platform cryptography will fail. For this example I'm using the "OAEP with SHA-1" as it is available on all platforms (except for CryptoJs where RSA is not available at all). 

The <u>encryption</u> is performed with the **Public key** of the recipient. For the <u>decryption</u> the **Private Key** of the recipient is used - so in a typical environment the Public Key is provided from the recipient to the encryptor(s) of the data and only the recipient is been able to decrypt the data.

**Key generation:** All examples use pre-generated keys that are described on the page [RSA sample keys](rsa_sample_keypair.md). If you want to see how my keys got generated visit the page [RSA key generation](rsa_key_generation.md). 

When comparing the other RSA-programs you will have noticed that the keys for C# looking like different as they are not in the "PEM"-format ("---Begin...") but in a XML-format. For this program I had to use .net 5 that includes the option to directly read a PEM encoded private and public key so there is no need to use XML-formatted keys.

One note about the **key management** in my programs: usually the keys are (securely) stored in files or a key store and may have additional password protection. Both scenarios are unhandy in demonstration programs running in an online compiler. That's why I'm using <u>static, hard-coded</u> keys in my programs - **please do not do this in production environment! Never ever store a Private Key in the source!** The minimum is to load the keys from a secured device; simply uncomment a line in the code to load the key from a file.

### steps in the program

The program follows the usual sequence:
1. generate a RSA key pair - here we are using a static, hard-coded key pair in form of a PEM encoded string (Java, PHP, C#, NodeJs, Python, Go)
2. convert the data to encrypt into a binary format (e.g. a byte array)
3. start the encryption process
4. load the Public key
5. set the encryption parameters
6. encrypt the "data to encrypt" and show the result ("ciphertext") in Base64 encoding
7. start the decryption process
8. load the Private key
9. Base64 decoding of the ciphertext
10. set the decryption parameters (same as used for encrypting)
11. decrypt the ciphertext to the "decryptedtext" and show the result.

I do not provide a "decryption lony" version as all functions are available in this program.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../RsaEncryptionOaepSha256String/RsaEncryptionOaepSha256.java) | :white_check_mark: | [repl.it CpcJavaRsaEncryptionOaepSha256String](https://repl.it/@javacrypto/CpcJavaRsaEncryptionOaepSha256String/)
| [PHP](../RsaEncryptionOaepSha256String/RsaEncryptionOaepSha256.php) | :white_check_mark: | [repl.it CpcPhpRsaEncryptionOaepSha256String](https://repl.it/@javacrypto/CpcPhpRsaEncryptionOaepSha256String#main.php/)
| [C#](../RsaEncryptionOaepSha256String/RsaEncryptionOaepSha256.cs) (.net 5 needed) | :white_check_mark: | [dotnetfiddle.net  CpcCsharpRsaEncryptionOaepSha256String](https://dotnetfiddle.net/HSk0Fy/)
| Javascript CryptoJs | :x: | the encryption functionality is not available in CryptoJs
| [NodeJS Crypto](../RsaEncryptionOaepSha256String/RsaEncryptionOaepSha256NodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoRsaEncryptionOaepSha256String](https://repl.it/@javacrypto/CpcNodeJsCryptoRsaEncryptionOaepSha256String#index.js/)
| [NodeJS forge](../RsaEncryptionOaepSha256String/RsaEncryptionOaepSha256NodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsRsaEncryptionOaepSha256String](https://repl.it/@javacrypto/CpcNodeJsRsaEncryptionOaepSha256String#index.js/)
| [Webcrypto encryption only](../RsaEncryptionOaepSha256String/rsaencryptionoaepsha256.html) | :white_check_mark: | [your browser WebcryptoRsaEncryptOaepSha256String.html](https://java-crypto.github.io/cross_platform_crypto/RsaEncryptionOaepSha256String/rsaencryptionoaepsha256.html)
| [Webcrypto decryption only](../RsaEncryptionOaepSha256String/rsadecryptionoaepsha256.html) | :white_check_mark: | [your browser WebcryptoRsaDecryptOaepSha256String.html](https://java-crypto.github.io/cross_platform_crypto/RsaEncryptionOaepSha256String/rsadecryptionoaepsha256.html)
| [Python](../RsaEncryptionOaepSha256String/RsaEncryptionOaepSha256.py) *1) | :white_check_mark: | [repl.it CpcPythonRsaEncryptionOaepSha256String](https://repl.it/@javacrypto/CpcPythonRsaEncryptionOaepSha256String/#main.py)
| [Go](../RsaEncryptionOaepSha256String/RsaEncryptionOaepSha256.go) | :white_check_mark: | [repl.it CpcGoRsaEncryptionOaepSha256String](https://repl.it/@javacrypto/CpcGoRsaEncryptionOaepSha256String/#main.go/)

*1) you need the external library pycryptodome, version 3.9.9

This is an output (your will differ because a random element):

```plaintext
RSA 2048 encryption OAEP SHA-256 string
plaintext: The quick brown fox jumps over the lazy dog

* * * encrypt the plaintext with the RSA public key * * *
ciphertextBase64: Xbuere0B5jmZ3zaxKcRK+WeKwlnrSR2ftJbqhmIaZW2Sx+HMySPpLWYPkjkbhFcEKMQxNYubcsdN+U8tySbkPNytX1O9yFHdxnVK7b0GRIq98H7+fdeU5VIyalndJCliS+MD3RdWU4VZqxDxaGgnDl/NV0Nst9bH1I3eW1UtT8tSxVJwSInIWgky/Bk9UHxkwMidpV9SSss/r8VMNTR6708psxf/FmvNDQd8XxpqjAmRm4bqiPVbiI1tvSfXEReWzWzc7DQvFLFLv/tLfMavzT4BxEI2JWHCBzgtrMABGRMIJjMF64lIkLClpnG0W7dchunqbNbb3vAROajQ+rjdew==

* * * decrypt the ciphertext with the RSA private key * * *
ciphertextReceivedBase64: Xbuere0B5jmZ3zaxKcRK+WeKwlnrSR2ftJbqhmIaZW2Sx+HMySPpLWYPkjkbhFcEKMQxNYubcsdN+U8tySbkPNytX1O9yFHdxnVK7b0GRIq98H7+fdeU5VIyalndJCliS+MD3RdWU4VZqxDxaGgnDl/NV0Nst9bH1I3eW1UtT8tSxVJwSInIWgky/Bk9UHxkwMidpV9SSss/r8VMNTR6708psxf/FmvNDQd8XxpqjAmRm4bqiPVbiI1tvSfXEReWzWzc7DQvFLFLv/tLfMavzT4BxEI2JWHCBzgtrMABGRMIJjMF64lIkLClpnG0W7dchunqbNbb3vAROajQ+rjdew==
decryptedtext: The quick brown fox jumps over the lazy dog

```

Last update: Apr. 21st 2021

Back to the main page: [readme.md](../readme.md)