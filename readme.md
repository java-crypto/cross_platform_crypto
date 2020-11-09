# Cross-platform cryptography

## General information and overview

This is a series of cryptography articles that will show show how cryptography is done between different programming platforms. The sample programs do not attempt to be "secure" but explain how the crypto routines are compatible with other platforms.

I'm trying to serve a broad spectrum of programming languages and actually I have solutions for these  frameworks: **Java**, **PHP**, **C#**, **Javascript - CryptoJs**, **NodeJs Crypto** and **NodeJs node-forge**  as long the functionality is available on the platform. You can test all programs with online compilers to see what the output is and what happens when you change some parameters.

A lot of solutions run with the built-in cryptographic modules but especially the Javascript-ones may need external libraries (pure Javascript will need e.g. "CryptoJs", NodeJs has a built-in "Crypto" or you can use "node-forge").

### General routines ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS | NodeJs forge |
| ------ | :---: | :----: | :---: | :--: | :--: | :--: | 
| [generate a 32 byte long AES key](generateaeskey.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [generate a 16 byte long initvector](generateinitvector.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [generate a 12 byte long nonce](generatenonce.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [Base64 encoding & decoding](base64encoding.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [byte array to hexstring & retour](bytearray.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [SHA256 hashing](sha256.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [PBKDF2 key derivation](pbkdf2.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [Generate RSA keys](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [Generate Elliptic keys](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |

### AES string encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |NodeJs forge |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: | 
| [CBC-mode String Encryption](aescbc256stringencryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [CBC-mode String Decryption only](aescbc256stringdecryptiononly.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [CBC-mode PBKDF2 String Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode String Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode PBKDF2 String Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode PBKDF2 AAD String Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |

### AES file encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |NodeJs forge |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: | 
| [CBC-mode File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [CBC-mode PBKDF2 File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode PBKDF2 File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode PBKDF2 AAD File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |

planned in future

### RSA encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |NodeJs forge |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: | 
| [RSA PKCS1 padding String](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [RSA OEAP padding SHA-1 String](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [RSA OEAP padding SHA-256 String](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |

planned in future

### RSA signature ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS Crypto | NodeJs forge |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: |
| [RSA String Signature (full)](readme.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: | :white_check_mark: |
| [RSA String Signature Verification only](readme.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark:| :white_check_mark: |

### RSA keys ###

Working with RSA key pairs can get tricky sometimes so I'm providing some more information about RSA keys:  [How to generate RSA key pairs](rsakeygeneration.md) and [How to convert RSA keys](rsakeyconversion.md). My programs uses a sample key pair, information are available are here: 
[RSA sample keys](rsasamplekeypair.md).

### Elliptic curve encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |
| ------ | :------: | :----: | :---: | :--: | :--: |

planned in later future

### Elliptic curve signature ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |
| ------ | :------: | :----: | :---: | :--: | :--: |

planned in later future

### Diffie-Hellman key exchange ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |
| ------ | :------: | :----: | :---: | :--: | :--: |

planned in later future

### Which platforms are supported at the moment?

I'm using an IDE for Java and PHP but for the other ones I'm using online compiler like [https://repl.it/](https://repl.it/). Please don't ask I the programs will run on your system with a lower or higher version - just try it on your own.

A lot of solutions run with the built-in cryptographic modules but especially the Javascript-ones may need external libraries (pure Javascript will need e.g. "CryptoJs", NodeJs has a built-in "Crypto" or you can use "node-forge").

| Language |  Online-compiler | framework version
| ------ | :---: | :----: |
| Java |  repl.it | OpenJDK Runtime Environment (build 11.0.6+10-post-Ubuntu-1ubuntu118.04.1)
| PHP |  repl.it | PHP CLI 7.2.17-0ubuntu0.18.04.1 (cli) (built: Apr 18 2019 14:12:38)
| C# |  repl.it | Mono C# compiler version 6.8.0.123
| Javascript CryptoJs |  repl.it | node v12.16.1, crypto-js version 4.0.0
| NodeJS Crypto |  repl.it | node v12.16.1, openssl 1.1.1g
| NodeJS node-forge |  repl.it | node v12.16.1, node-forge version 0.10.0 

Last update: Nov. 9th 2020

The website is published under:  [https://java-crypto.github.io/cross_platform_crypto//](https://java-crypto.github.io/cross_platform_crypto//)

If you are interested in much more information on cryptography in Java (and in German language) then visit my webpages [http://javacrypto.bplaced.net](http://javacrypto.bplaced.net/) and [http://java-crypto.bplaced.net](http://java-crypto.bplaced.net/).