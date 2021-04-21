# Cross-platform cryptography

## RSA string encryption with PKCS 1.5 padding

You maybe surprised about the long and unhandy title of the article but the RSA encryption has many (different) encryption types so it is important to name them correctly.

The general "problem" with RSA encryption is the limited ability to encrypt large sized plaintext. The size depends on the key size and can get calculated with this general formula

```plaintext
maximum plaintext to encrypt = key size in bytes - padding
```
Taking our RSA sample key of 2048 bit = 256 byte we get:

```plaintext
maximum plaintext to encrypt = 256 - 11 = 245 byte
```

This size is good for short strings or - that is the way it should be done for larger sizes - using a [**hybrid encryption scheme**](rsa_aes_hybrid_encryption_string.md). In short it uses a randomly created AES key of 32 byte, encrypts the data with this key in AES CBC- or (better) GCM- mode and encrypts the key with the RSA public key (and vice versa with the RSA private key for decryption).

Using the RSA encryption there are three padding modes: "no padding", "PKCS1.5 padding" or "OAEP padding". As the first two named are very vulnerable please don't use them, instead use an [**OAEP padding**](rsa_encryption_oaep_sha1.md) because it adds a random parameter so the ciphertext will differ each time you encrypt (even with an equal key and plaintext).

## **WARNING: This example uses PKCS 1.5 that is <u>vulnerable</u> and should not be used in newer programs.**

The OAEP-padding includes a hashing algorithm that is not always named by the algorithm name so there is a high chance that the Cross platform cryptography will fail. For this example I'm using the "OAEP with SHA-1" as it is available on all platforms (except for CryptoJs where RSA is not available at all). 

The <u>encryption</u> is performed with the **Public key** of the recipient. For the <u>decryption</u> the **Private Key** of the recipient is used - so in a typical environment the Public Key is provided from the recipient to the encryptor(s) of the data and only the recipient is been able to decrypt the data.

**Key generation:** All examples use pre-generated keys that are described on the page [RSA sample keys](rsa_sample_keypair.md). If you want to see how my keys got generated visit the page [RSA key generation](rsa_key_generation.md). 

When comparing the programs you will notice that the keys for C# looking like different as they are not in the "PEM"-format ("---Begin...") but in a XML-format. As it is a little bit tricky to convert the keys between XML- and PEM-format I setup an own page for this point: [RSA  key conversion](rsa_key_conversion.md)

One note about the **key management** in my programs: usually the keys are (securely) stored in files or a key store and may have additional password protection. Both scenarios are unhandy in demonstration programs running in an online compiler. That's why I'm using <u>static, hard-coded</u> keys in my programs - **please do not do this in production environment! Never ever store a Private Key in the source!** The minimum is to load the keys from a secured device; simply uncomment a line in the code to load the key from a file.

### steps in the program

The program follows the usual sequence:
1. generate a RSA key pair - here we are using a static, hard-coded key pair in form of a PEM encoded string (Java, PHP, C#, NodeJs, Python, Go) or a XML-file (C#)
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

I do not provide a **decryption part only** as all code is available in encryption code.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../RsaEncryptionPkcs15String/RsaEncryptionPkcs15.java) | :white_check_mark: | [replit.com CpcJavaRsaEncryptionPkcs15String](https://replit.com/@javacrypto/CpcJavaRsaEncryptionPkcs15String/)
| [PHP](../RsaEncryptionPkcs15String/RsaEncryptionPkcs15.php) | :white_check_mark: | [replit.com CpcPhpRsaEncryptionPkcs15String](https://replit.com/@javacrypto/CpcPhpRsaEncryptionPkcs15String#main.php/)
| [C#](../RsaEncryptionPkcs15String/RsaEncryptionPkcs15Xml.cs) XML keys | :white_check_mark: | [replit.com CpcCsharpRsaEncryptionPkcs15String XML](https://replit.com/@javacrypto/CpcCsharpRsaEncryptionPkcs15String#main.cs/)
| [C#](../RsaEncryptionPkcs15String/RsaEncryptionPkcs15Pem.cs) PEM keys | :white_check_mark: | [dotnetfiddle CpcCsharpRsaEncryptionPkcs15String PEM](https://dotnetfiddle.net/vHMU1k/)
| Javascript CryptoJs | :x: | the encryption functionality is not available in CryptoJs
| [NodeJS Crypto](../RsaEncryptionPkcs15String/RsaEncryptionPkcs15NodeJsCrypto.js) | :white_check_mark: | [replit.com CpcNodeJsCryptoRsaEncryptionPkcs15String](https://replit.com/@javacrypto/CpcNodeJsCryptoRsaEncryptionPkcs15String#index.js/)
| [NodeJS forge](../RsaEncryptionPkcs15String/RsaEncryptionOaepSha1NodeJs.js) | :white_check_mark: | [replit.com CpcNodeJsRsaEncryptionPkcs15String](https://replit.com/@javacrypto/CpcNodeJsRsaEncryptionPkcs15String#index.js/)
| [Webcrypto encryption only](../RsaEncryptionPkcs15String/rsaencryptionpkcs15.html) | :x: | the PKCS 1.5 padding is not available
| [Webcrypto decryption only](../RsaEncryptionPkcs15String/rsadecryptionpkcs15.html) | :x: | the PKCS 1.5 padding is not available
| [Python](../RsaEncryptionPkcs15String/RsaEncryptionPkcs15.py) *1) | :white_check_mark: | [replit.com CpcPythonRsaEncryptionPkcs15String](https://replit.com/@javacrypto/CpcPythonRsaEncryptionPkcs15String/#main.py)
| [Go](../RsaEncryptionPkcs15String/RsaEncryptionPkcs15.go) | :white_check_mark: | [replit.com CpcGoRsaEncryptionPkcs15String](https://replit.com/@javacrypto/CpcGoRsaEncryptionPkcs15String/#main.go/)

*1) you need the external library pycryptodome, version 3.9.9

This is an output (your will differ because a random element):

```plaintext
RSA 2048 encryption PKCS 1.5 string
plaintext: The quick brown fox jumps over the lazy dog

* * * encrypt the plaintext with the RSA public key * * *
ciphertextBase64: 4I577eCt5Zc3XhINgdoUtFG38Yv+HGHcnmj60JPqGkE9KZRr41JJoSUhWHYloSAcQhGTsLtqYlJWnijgHBZGzYhML19KpuQhwQmYPDDQW5CBEm6cgRwMu1Qx+wpbQUVtrTyGRn1OltasXTWS0MEUzfz2awwIDscbr75geTfpiIW51WBmDqVveTmWi9vDBoHlvW71sfhUvyKXRvsxZpw0jdT5BhbSmdlPHLwBS6NwrSDmwOqGSqx+3iicwtJSxJZot2yfG+WRGM3tW0bB/eyFFWiDkvh+U80jvzgf2uNjDSKSDKBlxwMNOCIffrv2iniDRciEJbLeSN1OMfqbE9/N8w==

* * * decrypt the ciphertext with the RSA private key * * *
ciphertextReceivedBase64: 4I577eCt5Zc3XhINgdoUtFG38Yv+HGHcnmj60JPqGkE9KZRr41JJoSUhWHYloSAcQhGTsLtqYlJWnijgHBZGzYhML19KpuQhwQmYPDDQW5CBEm6cgRwMu1Qx+wpbQUVtrTyGRn1OltasXTWS0MEUzfz2awwIDscbr75geTfpiIW51WBmDqVveTmWi9vDBoHlvW71sfhUvyKXRvsxZpw0jdT5BhbSmdlPHLwBS6NwrSDmwOqGSqx+3iicwtJSxJZot2yfG+WRGM3tW0bB/eyFFWiDkvh+U80jvzgf2uNjDSKSDKBlxwMNOCIffrv2iniDRciEJbLeSN1OMfqbE9/N8w==
decryptedtext: The quick brown fox jumps over the lazy dog

```

Last update: Apr. 21st 2021

Back to the main page: [readme.md](../readme.md)