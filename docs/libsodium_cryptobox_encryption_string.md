# Cross-platform cryptography

## Libsodium crypto box authenticated hybrid string encryption

What is so special with this **crypto box** inside of **Libsodium**? The answer is easy - using this box will give you a plus on security, high speed, "state of the art" methods and a minimum implementation work - OK - it's time to say "wow"! 

### How does this encryption works?

As the program is using a X25519 curve we do need a private-public key pair for both parties that want to exchange data. The public key from party A is provided to the other party (party B) and vice versa. The encryption function takes the (own) private key and the public key from the other party and computes a shared secret. 

The magic is: the other sides takes his private key and computes with our key the same shared secret, that is later used for encryption and decryption. In the end there is a lot of mathematical magic but that is totally hidden from the users and programmers. 

Just feed the data and receive the encrypted form of the data prepared for sending via email, later these data get decrypted to the original plaintext. When someone tries to tamper the ciphertext, don't worry, there is "signature" appended to detect those changes.

### What are the algorithms used in the secret box?

There are three different tasks and here are the implementation details:

1. The **key exchange mechanism** ("KEM"), using a private-public key pair) is done on basis of a **X25519 curve**, which has short keys (the private and public keys are 32 bytes short) and a fast processing
2. for the encryption the fast **XSalsa20 stream cipher** was choosen
3. to secure the ciphertext against tampering a MAC is generated, the algorithm for this task is **Poly1305 MAC**.

When combining the tasks one and two this is usually named **hybrid encryption** because the asymmetric encryption is not been able to encrypt more data larger than a few hundred bytes. As solution the encryption is done with a symmetric algorithm and just the encryption key is encrypted by the asymmetric algorithm.

To run this bundle of tasks you need exact one line of code for encryption and another line for decryption, that's it. No hassling with lines of code that could cause errors. When viewing my programs there are of course some more lines of code but most of them are service task (e.g. converting the binary data to Base64 encoding and vice versa). Just for fun - compare the code with the one in [RSA AES CBC 256 hybrid encryption](docs/rsa_aes_hybrid_encryption_string.md) and you will notice the difference.

And - in my eyes - the most important fact is the compatibility of the code cross-platform wide. All Libsodium bindings work on the same basis so it is easy to exchange data across system barriers.

### Is this encryption secure?

Answered from a technical side it is **yes**. When using private-public cryptography you should always keep in mind a possible "man in the middle" attack that you should be aware of. When receiving a public key from a 3rd party you definitely have to know that the key is from the person (think of an email from a friend "*hey let's start a secure encrypted communication - here is my public key, please send me yours*"). It could be good idea to check that the key (or maybe the sha-256 hash of the key) is from your friend [done via telephone or personal contact].

### Is there any anonymized solution available?

There is a way of just using recipient's public key for encryption and using the recipient's private key for decryption and it is called **sealed crypto box encryption** - here you find an example for [Libsodium sealed crypto box authenticated hybrid string encryption](libsodium_sealedcryptobox_encryption_string.md).

I do not provide a "decryption only" version as all functions are available in the full version.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

There is something I'm proud of: for the first time I did starting the programming with Java and then port it, here it all started with the Browser runnable HTML-/ Javascript code. The following links provide the solutions in code and an online compiler that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../LibsodiumCryptoboxEncryptionString/LibsodiumCryptoboxEncryptionString.java) | :white_check_mark: | [repl.it CpcJavaLibsodiumCryptoboxStringEncryption](https://repl.it/@javacrypto/CpcLibsodiumCryptoboxEncryptionString#Main.java/)
| [Java](../LibsodiumCryptoboxEncryptionString/LibsodiumCryptoboxEncryptionStringAlternativly.java) *1) alternativly | :white_check_mark: | [repl.it CpcJavaLibsodiumCryptoboxStringEncryption](https://repl.it/@javacrypto/CpcJavaLibsodiumCryptoboxEncryptionStringAlternativly#Main.java/)
| [PHP](../LibsodiumCryptoboxEncryptionString/LibsodiumCryptoboxEncryptionString.php) | :white_check_mark: | [repl.it CpcPhpLibsodiumCryptoboxStringEncryption](https://repl.it/@javacrypto/CpcPhpJavaLibsodiumCryptoboxEncryptionString#main.php/)
| [C#](../LibsodiumCryptoboxEncryptionString/LibsodiumCryptoboxEncryptionString.cs) | :white_check_mark: | [dotnetfiddle.net  CpcCsharpLibsodiumCryptoboxStringEncryption](https://dotnetfiddle.net/2U7cm5/)
| [Javascript / NodeJs](../LibsodiumCryptoboxEncryptionString/LibsodiumCryptoboxEncryptionStringNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsLibsodiumCryptoboxStringEncryption](https://repl.it/@javacrypto/CpcNodeJsLibsodiumCryptoboxEncryptionString#index.js)
| NodeJS CryptoJs | :x: | use above Javascript / NodeJs solution
| NodeJS Crypto | :x: | use above Javascript / NodeJs solution
| NodeJS forge | :x: | use above Javascript / NodeJs solution
| [Webcrypto](../LibsodiumCryptoboxEncryptionString/libsodiumcryptoboxencryptionfull.html) | :white_check_mark: | [your browser LibsodiumCryptoboxStringEncryption.html](http://javacrypto.bplaced.net/cpclibsodium/libsodiumcryptoboxencryptionfull.html/)

1) Java alternativly solution: uses the library xsalsa20poly1305-0.11.0.jar - this is not a full Libsodium compatible library so you can use it just in this case (Libsodium Cryptobox).

This is an output (your will differ because a random element):

```plaintext
Libsodium crypto box hybrid string encryption
plaintext: The quick brown fox jumps over the lazy dog

* * * encryption * * *
all data are in Base64 encoding
privateKeyA: yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU=
publicKeyB:  jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=
ciphertext:  MeImNq9oXgIDXozzOO7QuVerLOnGVa+S:6Wi521CVRmlXXqG6OmFoTt25FIXg7YExwsmEjitEbt/oZ1/tSXpIwN1AipOncQfR+BGsIJO2vETCeuM=
output is  (Base64) nonce : (Base64) ciphertext

* * * decryption * * *
ciphertext:  MeImNq9oXgIDXozzOO7QuVerLOnGVa+S:6Wi521CVRmlXXqG6OmFoTt25FIXg7YExwsmEjitEbt/oZ1/tSXpIwN1AipOncQfR+BGsIJO2vETCeuM=
input is (Base64) nonce : (Base64) ciphertext
privateKeyB: yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=
publicKeyA:  b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0=
decrypt.text:The quick brown fox jumps over the lazy dog

```

Last update: Jan. 17th 2021

Back to the main page: [readme.md](../readme.md)