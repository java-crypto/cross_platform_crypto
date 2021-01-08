# Cross-platform cryptography

## Elliptic key string signature (ECDSA) with IEEE P1363 encoding

The most used signature algorithm is **RSA** using but there are more modern Private-Public-Key signature schemes available. Here I am writing about **Elliptic key** or in short **EC key** cryptography. The Elliptic signature is known with the naming **ECDSA** as well.

### What are the advantages of Elliptic key signatures?

Firstly, the key size of an Elliptic key is much smaller than RSA keys (a RSA key is [minimum] 2048 bit (= 256 bytes) sized, an Elliptic key is about 32 bytes long.

Secondly, the arithmetic calculations can be done more quickly than with the RSA calculations.

### Why are Elliptic key signatures not so common as RSA signatures?

The RSA signature scheme is older than the EC signature so it is longer in use, more often used and therefore not so common.

### What are the parameters for a cross platform system?

1. Let's start with the private-public key pair - we do need a key pair that is defined by a "named curve". There are several curves available and for my examples I'm using the curve **SECP256R1** that is known with these names as well (depending on the platform): **NIST P-256**, **PRIME256V1** or the O.I.D. identifier **1.2.840.10045.3.1.7**. Needless to say that we do need the same key pair for signing and verification.
 
2. The second parameter is the hash algorithm - usually the "data to sign" are not signed directly but the hash of the data. My programs will use the **SHA-256** algorithm for that.
 
3. The third parameter is in a lot of implementations not directly visible but is implicitly part of the internal implementation - it is the **encoding of the signature**. When working with Java you would choose the algorithm "SHA256withECDSA" but this won't work with WebCrypto and vice versa. My programs run with **IEEE-P1363 encoding** - the other encoding in use is **DER encoding**. Please see my notes regarding the specific implementations.

### Randomness of the signature

An important parameter in cryptography is **randomness** - this prevents from easy textbook attacks. For a digital signature it means: the signature of the the same string and same (private) key results in the same value. Running my codes will get a different signature even with the same data to sign and private key.

### Key generation: 

All examples use pre-generated keys that are described on the page [EC sample keys](ec_sample_keypair.md). If you want to see how my keys got generated visit the page [EC key generation](ec_key_generation.md). 

One note about the **key management** in my programs: usually the keys are (securely) stored in files or a key store and may have additional password protection. Both scenarios are unhandy in demonstration programs running in an online compiler. That's why I'm using <u>static, hard-coded</u> keys in my programs - **please do not do this in production environment! Never ever store a Private Key in the source!** The minimum is to load the keys from a secured device.

### steps in the program

The program follows the usual sequence:
1. generate an EC key pair - here we are using a static, hard-coded key pair in form of a PEM encoded string (Java, PHP, C#, NodeJs Crypto and WebCrypto)
2. convert the data to sign into a binary format (e.g. a byte array)
3. start the signature process
4. load the Private Key
5. set the signature parameters
6. sign the "data to sign" and show the result ("signature") in Base64 encoding
7. start the verification process
8. load the Public Key
9. Base64 decoding of the signature
10. set the verification parameters (same as used for signing)
11. verify the signature against the "data to sign" and show the result.

I do not provide a **verification part only** version as all coding is in the full signature programs available, except that the WebCrypto programs are "signature only" and "verification only".

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compiler that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](EcSignatureP256Sha256String/EcSignatureStringFull.java) | :white_check_mark: | [repl.it CpcJavaEcStringSignatureFull](https://repl.it/@javacrypto/CpcJavaEcSignatureP256StringFull#Main.java/)
| [PHP](EcSignatureP256Sha256String/EcSignatureStringFull.php) | :white_check_mark: | [repl.it CpcPhpEcSignatureStringFull](https://repl.it/@javacrypto/CpcPhpEcSignatureP256StringFull#main.php/)
| [C#](EcSignatureP256Sha256String/EcSignatureStringFull.cs) | :white_check_mark: | [dotnetfiddle.net  CpcCsharpEcSignatureStringFull](https://dotnetfiddle.net/espTT1/)
| Javascript CryptoJs | :x: | the signature functionality is not available in CryptoJs
| [NodeJS Crypto](EcSignatureP256Sha256String/EcSignatureStringFullNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoEcSignatureStringFull](https://repl.it/@javacrypto/CpcNodeJsCryptoEcSignatureP256StringFull#index.js/)
| NodeJS forge | :x: | the signature functionality is not available in node-forge, use the above NodeJs Crypto implementation
| [Webcrypto sign only](EcSignatureP256Sha256String/ecsignaturestringsign.html) *1) | :white_check_mark: | [your browser WebcryptoEcSignString.html](https://java-crypto.github.io/cross_platform_crypto/EcSignatureP256Sha256String/ecsignaturestringsign.html) - does not work with Firefox 84
| [Webcrypto verify only](EcSignatureP256Sha256String/ecsignaturestringverification.html) *1) | :white_check_mark: | [your browser WebcryptoEcVerifyString.html](https://java-crypto.github.io/cross_platform_crypto/EcSignatureP256Sha256String/ecsignaturestringverification.html)
| OpenSSL | :x: | not available

Notes regarding the implementations: to get a most cross platform wide implementation I have choosen the **IEEE-P1363 encoding** of the signature. This encoding is easy to detect as the signature is 64 bytes long and consist of 2 concatenated values (R | S) of each 32 bytes long. Some frameworks support this kind of encoding "out of the box", e.g. in Java we choose the algorithm **SHA256withECDSAinP1363format** and everything is fine. Other platforms like PHP do need a programmatical code to achive this encoding. Whenever I used sources from other people I added a note. If you like need the example with [DER encoding see this  article](ecdsa_signature_der_string.md).

If you need to convert the signature format types (DER to IEEE P1363 and vice versa) see my article [ecdsa signature conversion](ecdsa_signature_conversion.md).

The other important part is the key management. Some implementations will work with other curves, some implementations do have hard-coded import functions that will work with the named curve **SECP256R1** only.

*1) Running the WebCrypto examples in my Browser I found that the Firefox Browser (tested with Version 84, x64 Windows) cannot run the **sign** example due to a long lasting bug - please use Chrome or Opera as they will sign.

This is an output (your output will differ due to a random element when signing):

```plaintext
EC signature string (ECDSA with SHA256)
dataToSign: The quick brown fox jumps over the lazy dog

* * * sign the plaintext with the EC private key * * *
used private key:
-----BEGIN EC PRIVATE KEY-----
MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCAU2f8tzo99Z1HoxJlY
96yXUhFY5vppVjw1iPKRfk1wHA==
-----END EC PRIVATE KEY-----

signature (Base64): MEYCIQDHYuDoqKndX4zUTcz+oIacSd+JuOkBhGwmjwhX0X+NbwIhAIkdxElOpzhV7UEQcbe5nLSQSxDiCpec+UZ4BnJiVIep

* * * verify the signature against the plaintext with the EC public key * * *
used public key:
-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEzb7yAFWup6iDqJiEq764rAumsV2M
rspZxaP3WGpwHaC4Uff3N4UbJZF7Zac1c6W7KJl0eeCP0205Q3UEpwxndQ==
-----END PUBLIC KEY-----

signature (Base64) verified: true

```

Last update: Jan. 08th 2020

Back to the main page: [readme.md](readme.md)