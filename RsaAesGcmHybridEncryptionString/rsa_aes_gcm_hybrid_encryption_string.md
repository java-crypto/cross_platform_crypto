# Cross-platform cryptography

## RSA AES GCM hybrid encryption

This article is a combined solution, based on [RSA OEAP padding SHA-1 String](rsa_encryption_oaep_sha1_string.md) and [AES GCM-mode String Encryption](aes_gcm_256_string_encryption.md). Here I'm leaving out details to the encryption schemes, kindly check the information on the linked articles.

### What is the workflow of a hybrid encryption?

When doing a **symmetric** encryption it is important to exchange the encryption key on a secure channel, what is not easy when thinking of Email or other electronic media. A better way is the usage of **asymmetric** encryption like RSA but there is the limitation to some (hundreds) of byte plaintext that can get encrypted.

The solution is to combine both encryption schemes into a (more) secure one - the **hybrid** encryption.

The encryption of the plaintext is done with e.g. AES Gcm-mode encryption and results in ciphertext. The key is generated randomly but **not** exchanged unencrypted. We encrypt the 32 bytes long AES key with the Public key of the recipient and send the encrypted AES key along with the ciphertext to the recipient.

The recipient will first decrypt the AES key with his Private key, resulting in the unencrypted AES key. The second step is the decryption of the ciphertext to the decryptedtext by using the AES encryption key.

My example programs will do all the steps in a combined program. As my examples are programmed for an easier exchange on different frameworks they do have some unnecessary conversions.

I do not provide a "decryption only" version as all functions are available in the full version.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../RsaAesGcmHybridEncryptionString/RsaAesGcmHybridEncryption.java) | :white_check_mark: | [repl.it CpcJavaRsaAesGcmHybridStringEncryption](https://repl.it/@javacrypto/CpcJavaRsaAesGcmHybridStringEncryption#Main.java/)
| [PHP](../RsaAesGcmHybridEncryptionString/RsaAesGcmHybridEncryption.php) | :white_check_mark: | [repl.it CpcPhpRsaAesGcmHybridStringEncryption](https://repl.it/@javacrypto/CpcPhpRsaAesGcmHybridStringEncryption#main.php/)
| [C#](../RsaAesGcmHybridEncryptionString/RsaAesGcmHybridEncryptionPem.cs) using PEM keys | :white_check_mark: | [dotnetfiddle.net CpcPhpRsaAesGcmHybridStringEncryptionPem](https://dotnetfiddle.net/Sh02O0/) |
| [C#](../RsaAesGcmHybridEncryptionString/RsaAesGcmHybridEncryptionXml.cs) using XML keys | :white_check_mark: | [dotnetfiddle.net CpcPhpRsaAesGcmHybridStringEncryptionXml](https://dotnetfiddle.net/5ft6sd/) |
| Javascript CryptoJs | :x: | the encryption functionality is not available in CryptoJs
| [NodeJS Crypto](../RsaAesGcmHybridEncryptionString/RsaAesGcmHybridEncryptionNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoRsaAesGcmHybridStringEncryption](https://replit.com/@javacrypto/CpcNodeJsCryptoRsaAesGcmHybridStringEncryption#index.js/)
| [NodeJS forge](../RsaAesGcmHybridEncryptionString/RsaAesGcmHybridEncryptionNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsRsaAesGcmHybridStringEncryption](https://repl.it/@javacrypto/CpcNodeJsRsaAesGcmHybridStringEncryption#index.js/)
| [Python](../RsaAesGcmHybridEncryptionString/RsaAesGcmHybridEncryption.py) | :white_check_mark: | [repl.it CpcPythonRsaAesGcmHybridStringEncryption](https://replit.com/@javacrypto/CpcPythonRsaAesGcmHybridStringEncryption#Main.py/)
| [Go](../RsaAesGcmHybridEncryptionString/RsaAesGcmHybridEncryption.go) | :white_check_mark: | [repl.it CpcGoRsaAesGcmHybridStringEncryption](https://repl.it/@javacrypto/CpcGoRsaAesGcmHybridStringEncryption#main.go/)

This is an output (your will differ because a random element):

```plaintext
RSA AES GCM hybrid encryption

uses AES GCM 256 random key for the plaintext encryption
and RSA OAEP SHA 1 2048 bit for the encryption of the random key

plaintext: The quick brown fox jumps over the lazy dog

* * * Encryption * * *
complete ciphertext: 72q+3+X/TKcNtXU09r6LdkHQM4ENVLkwN/4EGM+iW2xg6x98RgEFiOIl2T9Mbp/kf8/Y33mZdQ+Eq3wqjG/XuWakTpkaqI1W3SFZyHTeiVwhqpT1cYvEKAXDN31jy687BPs5QN6BVM2hfMcZ9YdQdHG+/ee9jdNs2MNroQfBH2NUYF3MXEfd6L3MB+OSMcZN6wmpBENhb/Xz4QChIhn7ztEm5HJwzZtDTulM/juzs2v9TEE4kDOBAu8tave+iGfajkQeZg8zWcD5nW/pi3++P5S/9hFjZ/3Cyi6xHRdGvv81J7WupDDoQlB8nNol+WzQTfiP48PFaNg65QzvylLIXw==:7Bf0B6HGWyCaCJqr:crqPzG0RqPbqR/dbwgOsyOt/VNxhEoAzvbByLwmb5jIz9TZ0JAVc6Gj8Kg==:r9UcANJ0P4fC5CnbnUXR4A==
output is (Base64) encryptionKey : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag

* * * Decryption * * *
completeCiphertextReceived: 72q+3+X/TKcNtXU09r6LdkHQM4ENVLkwN/4EGM+iW2xg6x98RgEFiOIl2T9Mbp/kf8/Y33mZdQ+Eq3wqjG/XuWakTpkaqI1W3SFZyHTeiVwhqpT1cYvEKAXDN31jy687BPs5QN6BVM2hfMcZ9YdQdHG+/ee9jdNs2MNroQfBH2NUYF3MXEfd6L3MB+OSMcZN6wmpBENhb/Xz4QChIhn7ztEm5HJwzZtDTulM/juzs2v9TEE4kDOBAu8tave+iGfajkQeZg8zWcD5nW/pi3++P5S/9hFjZ/3Cyi6xHRdGvv81J7WupDDoQlB8nNol+WzQTfiP48PFaNg65QzvylLIXw==:7Bf0B6HGWyCaCJqr:crqPzG0RqPbqR/dbwgOsyOt/VNxhEoAzvbByLwmb5jIz9TZ0JAVc6Gj8Kg==:r9UcANJ0P4fC5CnbnUXR4A==
plaintext:  The quick brown fox jumps over the lazy dog

```

Last update: Mar. 23rd 2021

Back to the main page: [readme.md](../readme.md)