# Cross-platform cryptography

## RSA string signature verification only

This is the shortened version of [RSA string signature](rsa_signature_string.md). Receiving data accompanied with a signature requires a **verification** of the signature. For the <u>verification</u> the **Public Key** of the signer is used.

One note about the **key management** in my programs: usually the keys are (securely) stored in files or a keystore and have additional password protection. Both scenarios are unhandy in demonstration programs running in an online compiler. That's why I'm using <u>static, hard-coded</u> keys in my programs - **please do not do this in production environment! Never ever store a Private Key in the source!** The minimum is to load the keys from secured files.

### steps in the program

The program follows the usual sequence:
1. Receive a message ("data to sign"), a signature and a RSA public key
2. convert the data to sign into a binary format (e.g. a byte array)
3. load the public key
4. start the verification process
5. Base64 decoding of the signature
6. set the verification parameters (same as used for signing)
7. verify the signature against the "data to sign" and show the result.

If you like to see the **RSA string encryption full** see my separate article [RSA signature string](rsa_signature_string.md).

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](RsaSignatureString/RsaSignatureStringVerificationOnly.java) | :white_check_mark: | [repl.it CpcJavaRsaSignatureStringVerificationOnly](https://repl.it/@javacrypto/CpcJavaRsaSignatureStringVerificationOnly/)
| [PHP](RsaSignatureString/RsaSignatureStringVerificationOnly.php) | :white_check_mark: | [repl.it CpcPhpRsaSignatureStringVerificationOnly](https://repl.it/@javacrypto/CpcPhpRsaSignatureStringVerificationOnly#main.php/)
| [C#](RsaSignatureString/RsaSignatureStringVerificationOnly.cs) | :white_check_mark: | [repl.it CpcCsharpRsaSignatureStringVerificationOnly](https://repl.it/@javacrypto/CpcCsharpRsaSignatureStringVerificationOnly#main.cs/)
| Javascript CryptoJs | :x: | the signature functionality is not available in CryptoJs
| [NodeJS Crypto](RsaSignatureString/RsaSignatureStringVerificationOnlyNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoRsaSignatureStringVerificationOnly](https://repl.it/@javacrypto/CpcNodeJsCryptoRsaSignatureStringVerificationOnly#index.js/)
| [NodeJS forge](RsaSignatureString/RsaSignatureStringVerificationOnlyNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsRsaSignatureStringVerificationOnly](https://repl.it/@javacrypto/CpcNodeJsRsaSignatureStringVerificationOnly#index.js/)

This is an output:

```plaintext
RSA signature string verification only
dataToSign: The quick brown fox jumps over the lazy dog

* * * verify the signature against the plaintext with the RSA public key * * *
used public key:
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9
iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT
/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8
01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB
ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g
QwIDAQAB
-----END PUBLIC KEY-----

signature (Base64) verified: true

```

Last update: Nov. 159th 2020

Back to the main page: [readme.md](readme.md)