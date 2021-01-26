# Cross-platform cryptography

## Libsodium sealed crypto box authenticated hybrid string encryption

This **sealed cryptobox encryption** is a "one-way" or "anonymized" version of the [Libsodium crytobox encryption program](libsodium_cryptobox_encryption_string.md). Instead of explaining the difference I'm trying to give you a scenario where this encryption could get used.

### Can you give a practical example why I should use it?

Think of a website owner like me that want to receive encrypted information and therefore publishes his **X25519 public key** on his website - or provides a simple html web page that does the encryption with his hard coded public key: [visit my example website](http://javacrypto.bplaced.net/cpclibsodium/libsodiumsealedcryptoboxencryptionsamplesender.html/). Now anyone can enter a message on the website and receives a (Base64 encoded) encrypted string that he can email to me.

On my side I will copy the message in my [visit my locally running decryption webpage](http://javacrypto.bplaced.net/cpclibsodium/libsodiumsealedcryptoboxencryptionsampledecrypt.html/)
and receive the decrypted data.

### What is the advantage of this procedure?

You don't need to exchange any keys "upfront" - just paste the plaintext and receive the (encrypted) ciphertext for emailing the data to the recipient. 

### Are there any security issues when using the anonymized cryptobox?

Seen from the technical view, the complete encryption is secure and authenticated. The risk remaining is that you don't know if the sender of the message is really the one he is pretending. By using of the "two-way" cryptobox you are sure that the ciphertext is from the person who has a specific private key and you received the public key directly from this person.

For a general overview kindly visit the [Libsodium crypto box authenticated hybrid string encryption page](libsodium_cryptobox_encryption_string.md). 

I do not provide a "decryption only" version as all functions are available in the full version.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compiler that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../LibsodiumSealedCryptoboxEncryptionString/LibsodiumSealedCryptoboxEncryptionString.java) | :white_check_mark: | [repl.it CpcJavaSealedLibsodiumCryptoboxStringEncryption](https://repl.it/@javacrypto/CpcJavaLibsodiumSealedCryptoboxEncryptionString#Main.java/)
| [PHP](../LibsodiumSealedCryptoboxEncryptionString/LibsodiumSealedCryptoboxEncryptionString.php) | :white_check_mark: | [repl.it CpcPhpLibsodiumSealedCryptoboxStringEncryption](https://repl.it/@javacrypto/CpcPhpJavaLibsodiumCryptoboxEncryptionString#main.php/)
| [C#](../LibsodiumSealedCryptoboxEncryptionString/LibsodiumSealedCryptoboxEncryptionString.cs) | :white_check_mark: | [dotnetfiddle.net  CpcCsharpLibsodiumSealedCryptoboxStringEncryption](https://dotnetfiddle.net/2U7cm5/)
| [Javascript / NodeJs](../LibsodiumSealedCryptoboxEncryptionString/LibsodiumSealedCryptoboxEncryptionStringNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsLibsodiumSealedCryptoboxStringEncryption](https://repl.it/@javacrypto/CpcNodeJsLibsodiumSealedCryptoboxEncryptionString#index.js)
| NodeJS CryptoJs | :x: | use above Javascript / NodeJs solution
| NodeJS Crypto | :x: | use above Javascript / NodeJs solution
| NodeJS forge | :x: | use above Javascript / NodeJs solution
| [Browser](../LibsodiumSealedCryptoboxEncryptionString/libsodiumsealedcryptoboxencryptionfull.html) | :white_check_mark: | [your browser LibsodiumSealedCryptoboxStringEncryption.html](http://javacrypto.bplaced.net/cpclibsodium/libsodiumsealedcryptoboxencryptionfull.html)
| [Browser sample encryption only](../LibsodiumSealedCryptoboxEncryptionString/libsodiumsealedcryptoboxencryptionsampleencryption.html) | :white_check_mark: | [your browser LibsodiumSealedCryptoboxStringSampleEncryption.html](http://javacrypto.bplaced.net/cpcjs/sealedcryptobox/libsodiumsealedcryptoboxencryptionsampleencryption.html)
| [Browser sample decryption only](../LibsodiumSealedCryptoboxEncryptionString/libsodiumsealedcryptoboxencryptionsampledecryption.html) | :white_check_mark: | [your browser LibsodiumSealedCryptoboxStringSampleDecryption.html](http://javacrypto.bplaced.net/cpcjs/sealedcryptobox/libsodiumsealedcryptoboxencryptionsampledecryption.html/)

This is an output (your will differ because a random element):

```plaintext
Libsodium sealed crypto box hybrid string encryption
plaintext: The quick brown fox jumps over the lazy dog

* * * encryption * * *
all data are in Base64 encoding
publicKeyB:  jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=
ciphertext:  Awjxnj2/60QXQEOGA+B5K/qeyg0k/A79Q4BqgQy75SzHhDala91DypryBjkErwf5QHZH6Z4nvFnKZ4XvRgufmEiUe6UBjpBQKM385vO2DJnGrHFYJA9FEmgUTw==
output is (Base64) ciphertext

* * * decryption * * *
privateKeyB: yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=
ciphertext:  Awjxnj2/60QXQEOGA+B5K/qeyg0k/A79Q4BqgQy75SzHhDala91DypryBjkErwf5QHZH6Z4nvFnKZ4XvRgufmEiUe6UBjpBQKM385vO2DJnGrHFYJA9FEmgUTw==
input is (Base64) ciphertext
decrypt.text:The quick brown fox jumps over the lazy dog

```

Last update: Jan. 26th 2021

Back to the main page: [readme.md](../readme.md)