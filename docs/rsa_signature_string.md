# Cross-platform cryptography

## RSA string signature with PKCS#1.5 padding

The standard signature algorithm is **RSA** using a (RSA) Private-/ Public key pair. The <u>signature</u> is generated with the **Private key** of the signer. For the <u>verification</u> the **Public Key** of the signer is used - so in a typical environment the Public Key is provided to the recipient(s) of the signed data and the recipient is been able to check = verify the signature.

To get this to work in cross platform systems there are three parameters that have to been equal - that is the hash algorithm that is used. My programs are using **SHA256** for this task.

The second important data are the **keys** itself - it's easy not to recognize that "- Begin Private Key -" isn't the same as "- Begin RSA Private Key - ". Using the wrong keys will cause the the verification to fail.

The third parameter is hidden in most implementations, it is the **padding**. The programs in this example use the **PKCS#1.5-padding** that is available platform wide but **not as secure as modern ones**.

### Why is the PKCS#1.5 padding not so secure?

An important parameter in cryptography is **randomness** - this prevents from easy textbook attacks. For a digital signature it means: the signature of the the same string and same (private) key results in the same value. Running my codes will always result in a (Base64 encoded) string beginning with "vCSB4744p30..", so an attacker may been able to "see" that a previous used signature is in use again.

### What would be a (more) secure padding?

A modern padding is the use of **PSS padding** (Probabilistic Signature Scheme) but this is not available "out of the box" and it will take some time to publish a solution here.

### Key generation: 

All examples use pre-generated keys that are described on the page [RSA sample keys](rsa_sample_keypair.md). If you want to see how my keys got generated visit the page [RSA key generation](rsa_key_generation.md). 

When comparing the programs you will notice that the keys for C# looking like different as they are not in the "PEM"-format ("---Begin...") but in a XML-format. As it is a little bit tricky to convert the keys between XML- and PEM-format I setup an own page for this point: [rsa_key_conversion.md](rsa_key_conversion.md)

One note about the **key management** in my programs: usually the keys are (securely) stored in files or a key store and may have additional password protection. Both scenarios are unhandy in demonstration programs running in an online compiler. That's why I'm using <u>static, hard-coded</u> keys in my programs - **please do not do this in production environment! Never ever store a Private Key in the source!** The minimum is to load the keys from a secured device.

### steps in the program

The program follows the usual sequence:
1. generate a RSA key pair - here we are using a static, hard-coded key pair in form of a PEM encoded string (Java, PHP, CryptoJs and NodeJs) or a XML-file (C#)
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

If you like to see the **verification part only** see my separate article [RSA signature string verification only](rsa_signature_string_verification_only.md) (coming soon).

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../RsaSignatureString/RsaSignatureStringFull.java) | :white_check_mark: | [repl.it CpcJavaRsaStringSignatureFull](https://repl.it/@javacrypto/CpcJavaRsaSignatureStringFull#Main.java/)
| [PHP](../RsaSignatureString/RsaSignatureStringFull.php) | :white_check_mark: | [repl.it CpcPhpRsaSignatureStringFull](https://repl.it/@javacrypto/CpcPhpRsaSignatureStringFull#main.php/)
| [C#](../RsaSignatureString/RsaSignatureStringFull.cs) | :white_check_mark: | [repl.it CpcCsharpRsaSignatureStringFull](https://repl.it/@javacrypto/CpcCsharpRsaSignatureStringFull#main.cs/)
| Javascript CryptoJs | :x: | the signature functionality is not available in CryptoJs
| [NodeJS Crypto](../RsaSignatureString/RsaSignatureStringFullNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoRsaSignatureStringFull](https://repl.it/@javacrypto/CpcNodeJsCryptoRsaSignatureStringFull#index.js/)
| [NodeJS forge](../RsaSignatureString/RsaSignatureStringFullNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsRsaSignatureStringFull](https://repl.it/@javacrypto/CpcNodeJsRsaSignatureStringFull#index.js/)
| [Webcrypto sign only](../RsaSignatureString/rsasignaturestringsign.html) | :white_check_mark: | [your browser WebcryptoRsaSignString.html](https://java-crypto.github.io/cross_platform_crypto/RsaSignatureString/rsasignaturestringsign.html)
| [Webcrypto verify only](../RsaSignatureString/rsasignaturestringverification.html) | :white_check_mark: | [your browser WebcryptoRsaVerifyString.html](https://java-crypto.github.io/cross_platform_crypto/RsaSignatureString/rsasignaturestringverification.html)

This is an output:

```plaintext
RSA signature string
dataToSign: The quick brown fox jumps over the lazy dog

* * * sign the plaintext with the RSA private key * * *
used private key:
-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDwSZYlRn86zPi9
...
84Ig17KiExe+qyYHjut/SC0wODDtzM/jtrpqyYa5JoEpPIaUSgPuTH/WhO3cDsx6
3PIW4/CddNs8mCSBOqTnoaxh
-----END PRIVATE KEY-----

signature (Base64): vCSB4744p30IBl/sCLjvKm2nix7wfScQn99YX9tVIDNQIvU3QnSCLc2cF+J6R9WMRIXOsY94MxjKCQANW0CuaSs+w31ePHaounFVnmXyY092SicZrtpwlxw2CHqJ0NSyciDpxlRId1vjKlp9E5IJmYtVMtL2hfb711P+nb+m+1sPplNXPpJpdnWIzfLsDMVxCkplAdrcoH2HuWgOtOCHAf3vWbUC/vkvi388NT1UXJRPoERM0m1v11ogP9DiycMdoJxg3fbdH3HknbR02MLNEr7q4ZMzlrKxnYChwp2hnnBvJDcXpPDnQz7sG8zrim1nL/PS8CRG5lxhYYAZqTc+Vg==

* * * verify the signature against the plaintext with the RSA public key * * *
used public key:
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
...
ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g
QwIDAQAB
-----END PUBLIC KEY-----

signature (Base64) verified: true

```

Last update: Jan. 08th 2021

Back to the main page: [readme.md](../readme.md)