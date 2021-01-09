# Cross-platform cryptography

## OpenSSL overview

One of the first cryptographic libraries on the market is **OpenSSL** that is available for all major operation systems and is included in some frameworks as well (e.g. PHP OpenSSL is included since 7.x PHP versions by default).

You can use OpenSSL as "stand-alone" program or it is used in command line batch files. As the library is grown over the years there are a lot of subsystems with similar looking commands so for the beginner it might be not so easy to keep track of things.

This articles and sub-articles will focus on common tasks and sometimes the results are "cross-platform" wide usable.

A perfect example of a full working task is the **Elliptic curve signature** - I will present you the commands that will enable you to generate EC keys, sign a data file with the private key file and verify the signature against the data file with the public key file and the cream topping is the cross platform property.

Here are my articles regarding OpenSSL themes:

| Solution | Description |
| ------ | :------: |
[Generate a RSA key pair](rsa_key_generation.md) | generate a RSA private key and public key in PEM encoding |
[Generate an EC key pair](ec_key_generation.md) | generate an Elliptic curve (EC) private key and public key in PEM encoding |
[RSA file signature with PKCS#1.5 padding](rsa_signature_file_openssl.md) |  full example for RSA key generation, signing and verifying a file. The results are interchangable with [RSA string signature with PKCS#1.5 padding](rsa_signature_string.md)|
[Elliptic curve (ECDSA) file signature DER format](ecdsa_signature_file_openssl.md) | full example for EC key generation, signing and verifying a file. The results are interchangable with [Elliptic key string signature (ECDSA) with DER encoding](ecdsa_signature_der_string.md) |

Last update: Jan. 09th 2021

Back to the main page: [readme.md](../readme.md)

