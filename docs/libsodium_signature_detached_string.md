# Cross-platform cryptography

## Libsodium detached signature of a string

A cryptographic library should also offer the possibility to (digitally) sign documents and verify them later by using a private key (for the signature) and a public key (for verification). Libsosium (of course) offers this by using the **Edwards curve 25519** or in short **ED25519 algorithm**.

### How does the signature process works?

Three simple steps are necessary to run a complete sign and verify round:

1. [generate an ED25519 private-public key pair](generate_ed25519_keypair.md) (or use a pre-generated one)
2. sign a plaintext with the private key and receive a signature
3. verify the received signature against the plaintext with the public key

The best of all: the source code in most examples will just have some lines of code (I know there are more lines but in the end the "overhead" is just needed for input, output and encoding).

### How do I generate a curve ED25519 key pair?

For all frameworks I'm providing a small program to [**generate the ED25519 key pair**](generate_ed25519_keypair.md).

Here is an example in PHP:
```plaintext
<?php
echo 'PHP Libsodium minimal example for signing and verification' . PHP_EOL;
$dataToSign = "The quick brown fox jumps over the lazy dog";
echo 'dataToSign: ' . $dataToSign . PHP_EOL;
// generate the key pair
$keyPair = sodium_crypto_sign_keypair();
$privateKey = sodium_crypto_sign_secretkey($keyPair);
$publicKey = sodium_crypto_sign_publickey($keyPair);
// signature
$signature = sodium_crypto_sign_detached($dataToSign, $privateKey);
echo 'signature in Base64 encoding: ' . base64_encode($signature) . PHP_EOL;
// verification
$signatureValidation = sodium_crypto_sign_verify_detached($signature, $dataToSign, $publicKey);
if ($signatureValidation) {echo 'verification status: the signature is valid' . PHP_EOL;} else
{echo 'verification status: the signature is invalid' . PHP_EOL;}
?>
```

and the output (your output will differ as I'm generating a new key pair each run):

```plaintext
PHP Libsodium minimal example for signing and verification
dataToSign: The quick brown fox jumps over the lazy dog
signature in Base64 encoding: OESKDU7pVoqnCU4ucGuTQIms+Fqj6JDwYABeP4IVAqogIf6lndTtEms65We7CkvXaokCZgYlJQGrFxebPPiZDA==
verification status: the signature is valid
```
### What is a detached signature and are there other types?

There are two signature types available within Libsodium, the **detached signature** and the **included signature**. The "detached" one provides a separate signature analogue to the RSA- or Elliptic curve ones, so you need to send the original plaintext and the signature to the recipient. The "included signature" gives only one signature result that incorporates the plaintext within the signature. After a positive ("valid") verification the original plaintext gets revealed, when the signature is invalid the verification function will return the complete signature string. For my programs I opted the "detached" version - feel free to use the included one.

I do not provide a "verification only" version as all functions are available in the full version.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../LibsodiumSignatureDetachedString/LibsodiumSignatureDetachedString.java) | :white_check_mark: | [repl.it CpcJavaLibsodiumSignatureDetachedString](https://repl.it/@javacrypto/CpcJavaLibsodiumSignatureDetachedString#Main.java/)
| [PHP](../LibsodiumSignatureDetachedString/LibsodiumSignatureDetachedString.php) | :white_check_mark: | [repl.it CpcPhpLibsodiumSignatureDetachedString](https://repl.it/@javacrypto/CpcPhpLibsodiumSignatureDetachedString#main.php/)
| [C#](../LibsodiumSignatureDetachedString/LibsodiumSignatureDetachedString.cs) | :white_check_mark: | # note 1) # [dotnetfiddle.net  CpcCsharpLibsodiumSignatureDetachedString](https://dotnetfiddle.net/Rd2hgu)
| [Javascript / NodeJs](../LibsodiumSignatureDetachedString/LibsodiumSignatureDetachedStringNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsLibsodiumSignatureDetachedString](https://repl.it/@javacrypto/CpcNodeJsLibsodiumSignatureDetachedString#index.js)
| NodeJS CryptoJs | :x: | use above Javascript / NodeJs solution
| NodeJS Crypto | :x: | use above Javascript / NodeJs solution
| NodeJS forge | :x: | use above Javascript / NodeJs solution
| [Webcrypto / Browser](../LibsodiumSignatureDetachedString/libsodiumsignaturedetachedstring.html) | :white_check_mark: | [your browser LibsodiumSignatureDetachedString.html](http://javacrypto.bplaced.net/cpcjs/signature/libsodiumsignaturedetachedstring.html)

note 1) at the time of writing the online-compiler dotnetfiddle.net does not work properly and claims about less ressources - the code is running correctly but not online, sorry.

This is an output:

```plaintext
Libsodium detached signature string
dataToSign: The quick brown fox jumps over the lazy dog

* * * sign the plaintext with the ED25519 private key * * *
signature (Base64): x41mufah/9VO347W+nPXu5FYeSJOI894YClbbTiX0pwRrvfAPMymxEvyMMsDFMI0R0sulCnuCRSgN0WOKnZBDg==

* * * verify the signature against the plaintext with the ED25519 public key * * *
signature (Base64): x41mufah/9VO347W+nPXu5FYeSJOI894YClbbTiX0pwRrvfAPMymxEvyMMsDFMI0R0sulCnuCRSgN0WOKnZBDg==
signature (Base64) verified: true

```

Last update: Jan. 24th 2021

Back to the main page: [readme.md](../readme.md)