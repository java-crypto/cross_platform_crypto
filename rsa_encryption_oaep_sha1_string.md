# Cross-platform cryptography

## RSA string encryption with OAEP SHA1 padding

You maybe surprised about the long and unhandy title of the article but the RSA encryption has many (different) encryption types so it is important to name them correctly.

The general "problem" with RSA encryption is the limited ability to encrypt large sized plaintext. The size depends on the key size and can get calculated with this general formula

```plaintext
maximum plaintext to encrypt = key size in bytes - padding
```
Taking our RSA sample key of 2048 bit = 256 byte we get:

```plaintext
maximum plaintext to encrypt = 256 - 42 = 214 byte
```

This size is good for short strings or - that is the way it should be done for larger sizes - using a **hybrid encryption scheme**. In short it uses a randomly created AES key of 32 byte, encrypts the data with this key in AES CBC- or (better) GCM- mode and encrypts the key with the RSA public key (and vice versa with the RSA private key for decryption).

Using the RSA encryption there are three padding modes: "no padding", "PKCS1 padding" or "OAEP padding". As the first two named are very vulnerable please don't use them, instead use an **OAEP padding** because it adds a random parameter so the ciphertext will differ each time you encrypt (even with an equal key and plaintext).

The OAEP-padding includes a hashing algorithm that is not always named by the algorithm name so there is a high chance that the Cross platform cryptography will fail. For this example I'm using the "OAEP with SHA-1" as it is available on all platforms (except for CryptoJs where RSA is not available at all). 

The <u>encryption</u> is performed with the **Public key** of the recipient. For the <u>decryption</u> the **Private Key** of the recipient is used - so in a typical environment the Public Key is provided from the recipient to the encryptor(s) of the data and only the recipient is been able to decrypt the data.

**Key generation:** All examples use pre-generated keys that are described on the page [RSA sample keys](rsa_sample_keypair.md). If you want to see how my keys got generated visit the page [RSA key generation](rsa_key_generation.md). 

When comparing the programs you will notice that the keys for C# looking like different as they are not in the "PEM"-format ("---Begin...") but in a XML-format. As it is a little bit tricky to convert the keys between XML- and PEM-format I setup an own page for this point: [RSA  key conversion](rsa_key_conversion.md)

One note about the **key management** in my programs: usually the keys are (securely) stored in files or a key store and may have additional password protection. Both scenarios are unhandy in demonstration programs running in an online compiler. That's why I'm using <u>static, hard-coded</u> keys in my programs - **please do not do this in production environment! Never ever store a Private Key in the source!** The minimum is to load the keys from a secured device; simply uncomment a line in the code to load the key from a file.

### steps in the program

The program follows the usual sequence:
1. generate a RSA key pair - here we are using a static, hard-coded key pair in form of a PEM encoded string (Java, PHP and NodeJs) or a XML-file (C#)
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

If you like to see the **decryption part only** see my separate article [RSA string decryption OAEP SHA 1 padding](rsa_decryption_oaep_sha1_string.md).

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](RsaEncryptionOaepSha1String/RsaEncryptionOaepSha1.java) | :white_check_mark: | [repl.it CpcJavaRsaEncryptionOaepSha1String](https://repl.it/@javacrypto/CpcJavaRsaEncryptionOaepSha1String/)
| [PHP](RsaEncryptionOaepSha1String/RsaEncryptionOaepSha1.php) | :white_check_mark: | [repl.it CpcPhpRsaEncryptionOaepSha1String](https://repl.it/@javacrypto/CpcPhpRsaEncryptionOaepSha1String#main.php/)
| [C#](RsaEncryptionOaepSha1String/RsaEncryptionOaepSha1.cs) | :white_check_mark: | [repl.it CpcCsharpRsaEncryptionOaepSha1String](https://repl.it/@javacrypto/CpcCsharpRsaEncryptionOaepSha1String#main.cs/)
| Javascript CryptoJs | :x: | the encryption functionality is not available in CryptoJs
| [NodeJS Crypto](RsaEncryptionOaepSha1String/RsaEncryptionOaepSha1NodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoRsaEncryptionOaepSha1String](https://repl.it/@javacrypto/CpcNodeJsCryptoRsaEncryptionOaepSha1String#index.js/)
| [NodeJS forge](RsaEncryptionOaepSha1String/RsaEncryptionOaepSha1NodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsRsaEncryptionOaepSha1String](https://repl.it/@javacrypto/CpcNodeJsRsaEncryptionOaepSha1String#index.js/)

This is an output (your will differ because a random element):

```plaintext
RSA 2048 encryption OAEP SHA-1 string
plaintext: The quick brown fox jumps over the lazy dog

* * * encrypt the plaintext with the RSA public key * * *
ciphertextBase64: SPB30DVhNgQIcptfuSKqgT6Mf6etLm7yXiMaORrEqB9vAUsnz27o/i1iLtmpHb1skaGEeNCiCDUhGazBaspnhU0xtynftr2ZB/SunBl14FiRu1e7qP9NKqj7vKduqeOBzqVQb1eTg46eyzsBE7Fhb+pv24fScEiBakqHCeCHSWq21KOrqQHUt1xmNjY8Kooyzl01r1065hF5YvDuvtQms8zObaHoKszmzQL5wap/uyoRQmFv53TW2AUWzrjRntO2OaP+7NBSvM5GGoJ2vmi0gkQcpflmgvkMTeWH25+zf8ihsbxeLyy6r7dnGQYosypS603SmzpH+7YsOD0wWYklSw==

* * * decrypt the plaintext with the RSA private key * * *
ciphertextReceivedBase64: SPB30DVhNgQIcptfuSKqgT6Mf6etLm7yXiMaORrEqB9vAUsnz27o/i1iLtmpHb1skaGEeNCiCDUhGazBaspnhU0xtynftr2ZB/SunBl14FiRu1e7qP9NKqj7vKduqeOBzqVQb1eTg46eyzsBE7Fhb+pv24fScEiBakqHCeCHSWq21KOrqQHUt1xmNjY8Kooyzl01r1065hF5YvDuvtQms8zObaHoKszmzQL5wap/uyoRQmFv53TW2AUWzrjRntO2OaP+7NBSvM5GGoJ2vmi0gkQcpflmgvkMTeWH25+zf8ihsbxeLyy6r7dnGQYosypS603SmzpH+7YsOD0wWYklSw==
decryptedtext: The quick brown fox jumps over the lazy dog

```

Last update: Nov. 15th 2020

Back to the main page: [readme.md](readme.md)