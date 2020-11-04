# Cross-platform cryptography

## General information and overview

This is a series of cryptography articles that will show show how cryptography is done between different programming platforms. The sample programs do not attempt to be "secure" but explain how the crypto routines are compatible with other platforms.

I'm trying to serve a broad spectrum of programming languages and actually I have solutions for these  frameworks: **Java**, **PHP**, **C#**, **CryptoJs** and **Node-forge**  as long the functionality is available on the platform. You can test all programs with online compilers to see what the output is and what happens when you change some parameters.

**Please note that is actually a website with less content - I'm working on it. Please come later, thanks.**

### General routines ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS | 
| ------ | :---: | :----: | :---: | :--: | :--: | 
| [generate a 32 byte long AES key](generateaeskey.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [generate a 16 byte long initvector](generateinitvector.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [generate a 12 byte long nonce](generatenonce.md) | :soon: | :soon: | :soon: | :soon: | :soon: |
| [Base64 encoding & decoding](base64encoding.md) | :soon: | :soon: | :soon: | :soon: | :soon: |
| [byte array to hexstring & retour](bytearray.md) | :soon: | :soon: | :soon: | :soon: | :soon: |
| [SHA256 hashing](sha256.md) | :soon: | :soon: | :soon: | :soon: | :soon: |
| [PBKDF2 key derivation](pbkdf2.md) | :soon: | :soon: | :soon: | :soon: | :soon: |

### AES string encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |
| ------ | :------: | :----: | :---: | :--: | :--: |
| [CBC-mode String Encryption](aescbc256stringencryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [CBC-mode String Decryption only](aescbc256stringdecryptiononly.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [CBC-mode PBKDF2 String Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode String Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode PBKDF2 String Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode PBKDF2 AAD String Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: |

### AES file encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |
| ------ | :------: | :----: | :---: | :--: | :--: |
| [CBC-mode File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: |
| [CBC-mode PBKDF2 File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode PBKDF2 File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode PBKDF2 AAD File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: |

planned in future

### RSA encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |
| ------ | :------: | :----: | :---: | :--: | :--: |
| [RSA PKCS1 padding](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: |
| [RSA OEAP padding](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: |

planned in future

### RSA signature ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |
| ------ | :------: | :----: | :---: | :--: | :--: |


planned in future

### Elliptic curve encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |
| ------ | :------: | :----: | :---: | :--: | :--: |

planned in future

### Elliptic curve signature ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |
| ------ | :------: | :----: | :---: | :--: | :--: |


planned in future

### Diffie-Hellman key exchange ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |
| ------ | :------: | :----: | :---: | :--: | :--: |


planned in future


Last update: Nov. 4th 2020

The website is published under:  [https://java-crypto.github.io/cross_platform_crypto//](https://java-crypto.github.io/cross_platform_crypto//)
