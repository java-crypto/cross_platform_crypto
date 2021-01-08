# Cross-platform cryptography

## General information and overview

This is a series of cryptography articles that will show show how cryptography is done between different programming platforms. The sample programs do not attempt to be "secure" but explain how the crypto routines are compatible with other platforms.

I'm trying to serve a broad spectrum of programming languages and actually I have solutions for these  frameworks: **Java**, **PHP**, **C#**, **Javascript - CryptoJs**, **NodeJs Crypto** and **NodeJs node-forge**  as long the functionality is available on the platform. You can test all programs with online compilers to see what the output is and what happens when you change some parameters. For selected assignments there is a **WebCrypto** solution available, that will run in your (modern) browser. For very rare programs I provide an **OpenSSL** solution.

A lot of solutions run with the built-in cryptographic modules but especially the Javascript-ones may need external libraries (pure Javascript will need e.g. "CryptoJs", NodeJs has a built-in "Crypto" library or you can use "node-forge").

### General routines ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS | NodeJs forge |
| ------ | :---: | :----: | :---: | :--: | :--: | :--: | 
| [generate a 32 byte long AES key](generate_aes_key.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [generate a 16 byte long initvector](generate_initvector.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [generate a 12 byte long nonce](generate_nonce.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| Base64 encoding & decoding | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| byte array to a hex string & back | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| SHA256 hashing | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [HMAC 256 calculation](hmac_256_calculation.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [PBKDF2 key derivation](pbkdf2.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [Generate RSA keys](rsa_key_generation.md) | [see explanation](rsa_key_generation.md) |
[Generate Curve 25519 keys](docs/curve25519_key_generation.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| [Generate Elliptic keys](docs/ec_key_generation.md) | [see explanation](ec_key_generation.md) |

### AES string encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |NodeJs forge | WebCrypto |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: | :--: |  
| [CBC-mode String Encryption](docs/aes_cbc_256_string_encryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| [CBC-mode String Decryption only](docs/aes_cbc_256_string_decryption_only.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| [CBC-mode PBKDF2 String Encryption](docs/aes_cbc_256_pbkdf2_string_encryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:| :white_check_mark: | :white_check_mark: |
| [CBC-mode PBKDF2 String Decryption only](docs/aes_cbc_256_pbkdf2_string_decryption_only.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:| :white_check_mark: | :x: |
| [CBC-mode PBKDF2 HMAC String Encryption](docs/aes_cbc_256_pbkdf2_hmac_string_encryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:| :white_check_mark: | :x: |
| [CBC-mode passphrase String Encryption](docs/aes_cbc_256_passphrase_string_encryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:| :white_check_mark: | :x: |
| [GCM-mode String Encryption](docs/aes_gcm_256_string_encryption.md) | :white_check_mark:| :white_check_mark: | :white_check_mark: | :x: | :white_check_mark:| :white_check_mark: | :x: |
| [GCM-mode PBKDF2 String Encryption](docs/aes_gcm_256_pbkdf2_string_encryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark:| :white_check_mark: | :white_check_mark: |
| [GCM-mode PBKDF2 AAD String Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: | :x: |

#### AES CBC special: tampering
| Solution | Java | Explanation |
| ------ | :------: | :----: |
| [AES CBC-mode tampering](docs/aes_cbc_tampering.md) | :white_check_mark: | in Java as it is for demonstration only |

### RSA encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |NodeJs forge |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: | 
| [RSA OEAP padding SHA-1 String](rsa_encryption_oaep_sha1_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark:| :white_check_mark: |
| [RSA OEAP padding SHA-1 String decryption only](rsa_decryption_oaep_sha1_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark:| :white_check_mark: |
| [RSA OEAP padding SHA-256 String](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [RSA OEAP padding SHA-256 String decryption only](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |


### RSA signature with different padding modes ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS Crypto | NodeJs forge | WebCrypto |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: | :--: |
| [RSA String Signature PKSC#1.5 (full)](rsa_signature_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark:
| [RSA String Signature PKSC#1.5 Verification only](rsa_signature_string_verification_only.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark:| :white_check_mark: | :white_check_mark:
| [RSA String Signature PSS (full)](rsa_signature_pss_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark:

### RSA keys ###

Working with RSA key pairs can get tricky sometimes so I'm providing some more information about RSA keys: 

| page | information |
| ------ | :------: |
[How to generate RSA key pairs](rsa_key_generation.md) | creation of RSA key pairs with OpenSSL |
[How to convert RSA keys](rsa_key_conversion.md) | convert keys between XML- and PEM-format |
[RSA sample keys](rsa_sample_keypair.md) | for my encryption and signature examples I used these keys |
[RSA key formats](rsa_key_formats.md) | explanation of the most used key formats |

### RSA AES hybrid encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS Crypto | NodeJs forge |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: |
| [RSA AES CBC 256 hybrid encryption](rsa_aes_hybrid_encryption_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: | :white_check_mark: |

### Curve 25519 key exchange ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS | NodeJs forge |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: |
[Curve 25519 key exchange & AES CBC mode 256 string encryption](docs/curve25519_key_exchange_aes_cbc_256_string_encryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |

Note: you need external libraries for this feature!

### Elliptic curve keys ###

| page | information |
| ------ | :------: |
[EC key generation](docs/ec_key_generation.md) | creation of EC key pairs with OpenSSL
[EC sample keys](docs/ec_sample_keypair.md) | sample key pair for curve SECP256R1 = P-256 = PRIME256V1

### Elliptic curve signature ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS Crypto | NodeJs forge | WebCrypto | OpenSSL
| ------ | :------: | :----: | :---: | :--: | :--: | :--: | :--: | :--: |
| [ECDSA String Signature IEEE-P1363 encoding ](docs/ecdsa_signature_ieee_p1363_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: | :x:| :white_check_mark: | :x:|
| [ECDSA String Signature DER encoding ](docs/ecdsa_signature_der_string.md) | :white_check_mark: | :white_check_mark: | :x: | :x: | :white_check_mark: | :x:| :white_check_mark: | :white_check_mark:
| [ECDSA signature converter DER <- -> IEEE P1363 encoding ](docs/ecdsa_signature_conversion.md) | :white_check_mark: | :white_check_mark: | :x: | :x: | :white_check_mark: | :x: | :white_check_mark: | :x:|

### OpenSSL solutions ###
| Solution | Descryption |
| ------ | ------ |
[OpenSSL overview](docs/openssl_overview.md) | general information about OpenSSL cryptography |
[Generate a RSA key pair](docs/rsa_key_generation.md) | generate a RSA private key and public key in PEM encoding
[Generate an EC key pair](docs/ec_key_generation.md) | generate an Elliptic curve (EC) private key and public key in PEM encoding
[Elliptic curve file signature DER encoding](docs/ecdsa_signature_file_openssl.md) | the signature and verification is compatible with solutions in [ECDSA String Signature DER encoding ](docs/ecdsa_signature_der_string.md) |

### Elliptic curve encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |
| ------ | :------: | :----: | :---: | :--: | :--: |

planned in later future

### AES file encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |NodeJs forge |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: | 
| [CBC-mode File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [CBC-mode PBKDF2 File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode PBKDF2 File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| [GCM-mode PBKDF2 AAD File Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |

planned in future

### Which platforms are supported at the moment?

I'm using an IDE for Java and PHP but for the other ones I'm using online compiler like [https://repl.it/](https://repl.it/). Please don't ask if the programs will run on your system with a lower or higher version - just try it on your own.

A lot of solutions run with the built-in cryptographic modules but especially the Javascript-ones may need external libraries (pure Javascript will need e.g. "CryptoJs", NodeJs has a built-in "Crypto" or you can use "node-forge").

The few Javascript Webcrypto examples will run directly in your browser so you will need an actual one and Javascript is enabled.

| Language |  Online-compiler | framework version
| ------ | :---: | ---- |
| Java |  repl.it | OpenJDK Runtime Environment (build 11.0.6+10-post-Ubuntu-1ubuntu118.04.1)
| PHP |  repl.it | PHP CLI 7.2.17-0ubuntu0.18.04.1 (cli) (built: Apr 18 2019 14:12:38)
| PHP phpseclib |   | version 3
| C# |  repl.it | Mono C# compiler version 6.8.0.123
| C# |  dotnetfiddle.net | .NET 4.7.2
| C# |  Bouncy Castle | version 1.89
| Javascript CryptoJs |  repl.it | node v12.16.1, crypto-js version 4.0.0
| NodeJS Crypto |  repl.it | node v12.16.1, openssl 1.1.1g
| NodeJS node-forge |  repl.it | node v12.16.1, node-forge version 0.10.0 
| Webcrypto | modern browser | tested in Firefox 83 (x64), Google Chrome 84.0.4147.135 (x86), Opera 70.0.3728.119
| OpenSSL |  | version 1.1.1g Windows x64

Last update: Jan. 08th 2021

The website is published under:  [https://java-crypto.github.io/cross_platform_crypto//](https://java-crypto.github.io/cross_platform_crypto//)

If you are interested in much more information on cryptography in Java (and in German language) then visit my webpages [http://javacrypto.bplaced.net](http://javacrypto.bplaced.net/) and [http://java-crypto.bplaced.net](http://java-crypto.bplaced.net/).
