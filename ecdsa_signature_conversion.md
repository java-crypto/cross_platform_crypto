# Cross-platform cryptography

## Elliptic key string signature conversion between DER and IEEE P1363  encoding

Please note that all following information is valid for the elliptic curve **SECP256R1** that we are using in the programs. Using a curve with a longer size will give longer signatures.

There are two ECDSA signature formats "on the market" and here are the specifications:

1. **IEEE P1363 = R|S = raw encoding**: this encoding is easy to detect - the signature in byte form is exact 64 bytes long and a simple concatenation of the two values "R" and "S".

2. **DER = ASN.1 encoding**: this encoding contains some more bytes so it is around 70 bytes long (some bytes more or less are common). The two values "R" and "S" are embedded in a structure for better reading by some frameworks. 

The conversion between the two formats sounds easy but when trying to do it on your own you will encounter a lot of hick ups. For that reason I'm providing converter from **DER to IEEE P1363** and **IEEE P1363 to DER** - unfortunately only for a limited number of frameworks.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compiler that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java DER to P1363](EcSignatureConverter/EcSignatureConverterDerToP1363.java) | :white_check_mark: | [repl.it CpcJavaEcSignatureConverterDerToP1363](https://repl.it/@javacrypto/CpcJavaEcSignatureConvertDerToP1363#Main.java/)
| [Java P1363 to DER](EcSignatureConverter/EcSignatureConverterP1363ToDer.java) | :white_check_mark: | [repl.it CpcJavaEcSignatureConverterP1363ToDer](https://repl.it/@javacrypto/CpcJavaEcSignatureConvertP1363ToDer#Main.java/)
| [PHP DER to P1363](EcSignatureConverter/EcSignatureConverterDerToP1363.php) | :white_check_mark: | [repl.it CpcPhpEcSignatureConverter](https://repl.it/@javacrypto/CpcPhpEcSignatureConvertDerToP1363#main.php/)
| [PHP P1363 to DER](EcSignatureConverter/EcSignatureConverterP1363ToDer.php) | :white_check_mark: | [repl.it CpcPhpEcSignatureConverter](https://repl.it/@javacrypto/CpcPhpEcSignatureConvertP1363ToDer#main.php/)
| C# | :x: | not available
| Javascript CryptoJs | :x: | not available
| [NodeJS Crypto DER to P1363](EcSignatureConverter/EcSignatureConverterDerToP1363NodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoEcSignatureConverterDerToP1363](https://repl.it/@javacrypto/CpcNodeJsCryptoConvertDerToP1363#index.js/)
| [NodeJS Crypto P1363 to DER](EcSignatureConverter/EcSignatureConverterP1363ToDerNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoEcSignatureConverterP1363ToDer](https://repl.it/@javacrypto/CpcNodeJsCryptoConvertP1363ToDer#index.js/)
| NodeJS forge | :x: | not available
| [Webcrypto DER to P1363](EcSignatureConverter/ecsignatureconverterdertop1363.html) | :white_check_mark: | [Your browser WebcryptoEcSignatureConverterDerToP1363.html](https://java-crypto.github.io/cross_platform_crypto/EcSignatureConverter/ecsignatureconverterdertop1363.html)
| [Webcrypto P1363 to DER](EcSignatureConverter/ecsignatureconverterp1363toder.html) | :white_check_mark: | [Your browser WebcryptoEcSignatureConverterP1363ToDer.html](https://java-crypto.github.io/cross_platform_crypto/EcSignatureConverter/ecsignatureconverterp1363toder.html)
| OpenSSL | :x: | not available

Notes regarding the implementations: for this article I have choosen the **DER encoding** of the signature. Some frameworks support this kind of encoding "out of the box", e.g. in Java we choose the algorithm **SHA256withECDSA** and everything is fine. If you like need the example with [IEEE P1363 encoding see this article](ecdsa_signature_ieee_p1363_string.md).

The other important part is the key management. Some implementations will work with other curves, some implementations do have hard-coded import functions that will work with the named curve **SECP256R1** only.

This is an output for DER to P1363 encoding:

```plaintext
EC signature converter DER to P1363 encoding
Please note that all values are in Base64 encoded form
signature in DER encoding:   MEUCIQCW98vDD+c/Wx3WX3T+Ph9ZDf1s5nuDThz2xrhSOjwGFAIgNpUg5SygNK7W+zzw4eNZkivizpU/UUXMMR2zPw2D7J4=
signature in P1363 encoding: lvfLww/nP1sd1l90/j4fWQ39bOZ7g04c9sa4Ujo8BhQ2lSDlLKA0rtb7PPDh41mSK+LOlT9RRcwxHbM/DYPsng==
```

This is an output for P1363 to DER encoding:

```plaintext
EC signature converter P1363 to DER encoding
Please note that all values are in Base64 encoded form
signature in P1363 encoding: aF1cTCPqSUuCSKMftzeE37VeaqrlQuILJsW9RBEo8yZUyZFTOkEqq2TI2Bd/kugow6bbZDfhtsB3QwbZ55aOmQ==
signature in DER encoding:   MEQCIGhdXEwj6klLgkijH7c3hN+1Xmqq5ULiCybFvUQRKPMmAiBUyZFTOkEqq2TI2Bd/kugow6bbZDfhtsB3QwbZ55aOmQ==
```

Last update: Jan. 08th 2021

Back to the main page: [readme.md](readme.md)