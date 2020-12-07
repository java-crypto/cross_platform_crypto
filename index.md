# Cross-platform cryptography

## General information and overview

A lot of websites provide examples for all kind of cryptographic requirements, e.g. encrypting a string with AES mode CBC. Many of them will give direct usable solutions but there are less websites and examples when crossing the platforms.

One example is the string **encryption** with AES CBC on a Java system, followed by the **decryption** on a PHP platform. Another example could by a digital signature with RSA - the **signature** is generated on a C#-system and the **verification** will be done on Javascript-platform.

I decided to setup some articles regarding this topic and I recommend to visit the readme-page on [https://github.com/java-crypto/cross_platform_crypto/blob/main/readme.md ](https://github.com/java-crypto/cross_platform_crypto/blob/main/readme.md/) for a more detailed information.

### Which platforms are supported at the moment?

As I'm a Java developer I support platforms that are similar to Java (more or less) here is a list together with the versions they are running. 

I'm using an IDE for Java and PHP but for the other ones I'm using online compiler like [https://repl.it/](https://repl.it/). Please don't ask I the programs will run on your system with a lower or higher version - just try it on your own.

A lot of solutions run with the built-in cryptographic modules but especially the Javascript-ones may need external libraries (pure Javascript will need e.g. "CryptoJs", NodeJs has a built-in "Crypto" or you can use "node-forge").

The few Javascript Webcrypto examples will run directly in your browser so you will need an actual one and Javascript is enabled.

| Language |  Online-compiler | framework version
| ------ | :---: | :----: |
| Java |  repl.it | OpenJDK Runtime Environment (build 11.0.6+10-post-Ubuntu-1ubuntu118.04.1)
| PHP |  repl.it | PHP CLI 7.2.17-0ubuntu0.18.04.1 (cli) (built: Apr 18 2019 14:12:38)
| C# |  repl.it | Mono C# compiler version 6.8.0.123
| Javascript CryptoJs |  repl.it | node v12.16.1, crypto-js version 4.0.0
| NodeJS Crypto |  repl.it | node v12.16.1, openssl 1.1.1g
| NodeJS node-forge |  repl.it | node v12.16.1, node-forge version 0.10.0 
| Webcrypto | modern browser | tested in Firefox 83 (x64), Google Chrome 84.0.4147.135 (x86), Opera 70.0.3728.119

### Which cryptographic problems are solved platform-wide?

**General routines:** generate a random AES-key, an initialization vector and a random nonce, HMAC 256 calculating, PBKDF2 key derivation, generate a RSA key pair, generate a Curve 25519 key pair

**AES string encryption:** AES 256 CBC mode, AES 256 CBC mode with PBKDF2, AES 256 CBC mode with PBKDF2 and HMAC 256 

**RSA string encryption:** RSA OAEP SHA1 padding encryption

**RSA string signature:** RSA 2048 signature

**Curve 25519 key exchange:** Curve 25519 key exchange & AES CBC mode 256 string encryption

### Which solutions are planned for the future?

**General routines:** Base64 encoding & decoding, byte array to hex string & retour , SHA256 hashing, PBKDF2 key derivation

**AES string encryption:** AES 256 GCM mode string encryption, AES 256 GCM mode with PBKDF2 string encryption

**AES file encryption:** AES 256 in modes CBC and GCM (and with or without PBKDF2 key derivation)

**Elliptic curve encryption** not for the near future, sorry

**Elliptic curve signature** not for the near future, sorry

**Diffie-Hellman key exchange** not for the near future, sorry

Last update: Dec. 07th. 2020

The website is published under:  [https://java-crypto.github.io/cross_platform_crypto/index.html](https://java-crypto.github.io/cross_platform_crypto//)

If you are interested in much more information on cryptography in Java (and in German language) then visit my webpages [http://javacrypto.bplaced.net](http://javacrypto.bplaced.net/) and [http://java-crypto.bplaced.net](http://java-crypto.bplaced.net/).
