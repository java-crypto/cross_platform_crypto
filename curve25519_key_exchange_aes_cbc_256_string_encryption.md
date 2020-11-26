# Cross-platform cryptography

## Curve 25519 key exchange with AES CBC mode 256 string encryption

This solution is a combination of an key exchange on basis of an **Curve25519** algorithm and a standard **AES CBC mode 256** string encryption.

The AES encryption part is completely described in [AES CBC mode 256 string encryption](aes_cbc_256_string_encryption.md) so I don't repeat it here.

### Why do we use a key exchange protocol for encryption?

Most communication channels are unsecure as anybody with access to the data can grab the data. The best way to exchange an encryption key is to do in personal, so you have to meet the other party.

That is not a problem in a personal environment as you meet e.g. your friends or family but secure exchanging the keys between persons in other cities, countries or continents can get problematic. One of the best ways to do I by telephone, but sometimes there are language barriers or other problems.

So some persons invented a computerized key exchange and one algorithm is widely used up to date and that's the **Diffie Hellman key exchange** or "DH". With coming up of elliptic curve cryptography the "DHE" algorithm was invented.

### Why are we using Curve25519 and not Diffie Hellmann?

My programs work on a specialized elliptic curve - **Curve25519**. This curve has the advantage that the key length is "handy" (a 32 byte long private and public key), fast (compared to DH-algorithms) and in the end you receive a 32 bytes long "shared key" - perfect for modern AES cryptography.

Another reason for using Curve25519 is - it is available on all of my platforms with some, small libraries.

You can find more information about key change in this [Wikipedia article](https://en.wikipedia.org/wiki/Key_exchange).

### How doe the key exchange work in general?

Each party is generating its own key pair. The private key should be stored in a safe environment (e.g. in a password secured key store), the public key can be published on the own website or send by Email (think about a "man in the middle attack" and secure this transport with a digital signature...) to the other party.

Both parties now do the same steps:

1. take the own private key and the others public key
2. generate their **shared secret key**
3. use this shared secret key as key for an encryption

Due to the mathematical mystery both parties will generate the same shared secret key so both parties are been able to decrypt the encrypted message received from other party.

### How to generate the Curve25519 keys?

As in most cases the key generation takes place before the encrypted messages will be exchanged I sourced this part out to a separate program, visit [Curve 25519 key generation](curve25519_key_generation.md) for this task.

### steps in the program

The program follows the usual sequence:
1. copy your own private key and others public key to the source
2. copy your plaintext message to the program
3. generate the shared secret key and show the key in Base64 encoding
4. convert the plaintext to a binary format (e.g. a byte array)
5. start the encryption process
6. I'm leaving out the AES part as it is described in the named article
7. send the encrypted message to the other party
8. for decryption (that will happen on the other parties side) input other parties private key and "foreign" public key
9. copy the received encrypted message to the program
10. generate the shared secret key and show the key in Base64 encoding
11. start the decryption process
12. I'm leaving out the AES part as it is described in the named article
13. decrypt the ciphertext and show the resulting plaintext

### **Serious notice: although the program looks like simple there is NO CHANCE for recovering the original plaintext without the keys used for encryption!**

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](Curve25519KeyExchangeAesCbc256StringEncryption/Curve25519KeyExchangeAesCbc256StringEncryption.java) | :white_check_mark: | [repl.it CpcJavaCurve25519KeyExchangeAesCbc256StringEncryption](https://repl.it/@javacrypto/CpcJavaCurve25519KeyExchangeAesCbc256StringEncryption#Main.java/)
| [PHP](Curve25519KeyExchangeAesCbc256StringEncryption/Curve25519KeyExchangeAesCbc256StringEncryption.php) | :white_check_mark: | [repl.it CpcPhpCurve25519KeyExchangeAesCbc256StringEncryption](https://repl.it/@javacrypto/CpcPhpCurve25519KeyExchangeAesCbc256StringEncryption#main.php/)
| [C#](Curve25519KeyExchangeAesCbc256StringEncryption/Curve25519KeyExchangeAesCbc256StringEncryption.cs) | :white_check_mark: | [repl.it CpcCsharpCurve25519KeyExchangeAesCbc256StringEncryption](https://repl.it/@javacrypto/CpcCsharpCurve25519KeyExchangeAesCbc256StringEncryption#main.cs/)
| [Javascript CryptoJs](Curve25519KeyExchangeAesCbc256StringEncryption/Curve25519KeyExchangeAesCbc256StringEncryptionCryptoJs.js) | :white_check_mark: | [repl.it CpcCryptoJsCurve25519KeyExchangeAesCbc256StringEncryption](https://repl.it/@javacrypto/CpcCryptoJsCurve25519KeyExchangeAesCbc256StringEncryption#index.js/)
| [NodeJS Crypto](Curve25519KeyExchangeAesCbc256StringEncryption/Curve25519KeyExchangeAesCbc256StringEncryptionNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoCurve25519KeyExchangeAesCbc256StringEncryption](https://repl.it/@javacrypto/CpcNodeJsCryptoCurve25519KeyExchangeAesCbc256StringEncryption/#index.js/)
| NodeJS node-forge | :x: | please use the built-in Node Crypto-solution above

### What additional libraries do I need to run the examples?

| Language | library | link
| ------ | :---: | :---- |
| Java | curve25519-java-0.5.0.jar | [https://mvnrepository.com/artifact/org.whispersystems/curve25519-java](https://mvnrepository.com/artifact/org.whispersystems/curve25519-java/) and online on https://repl.it https://github.com/wavesplatform/curve25519-java/|
| PHP | Curve25519.php | [https://github.com/lt/PHP-Curve25519](https://github.com/lt/PHP-Curve25519/) |
| C# | Curve25519.cs | [https://github.com/hanswolff/curve25519](https://github.com/hanswolff/curve25519/) |
| Javascript | axlsign.js | [https://github.com/wavesplatform/curve25519-js](https://github.com/wavesplatform/curve25519-js/) |
| Javascript | buffer-to-uint8array.js | [https://github.com/substack/buffer-to-uint8array](https://github.com/substack/buffer-to-uint8array/) |

### Java 11+ supports Curve25519, why do we use an external library?

The Curve25519 key exchange is running with Java 11+ support but the encoded keys are not compatible to the keys on other platforms (e.g. the Java 11+ encoded public key is 46 bytes long). The rason for this is simple - the other libraries are using "raw" key but Java 11+ is adding some data. While the explanation is simple the conversion of the public key between the two encodings is not and it is more easy to use a compatible library. 


This is an output (as there are random elements your output will differ):

```plaintext
Curve25519 key exchange and AES CBC 256 string encryption

* * * encryption * * *
all data are in Base64 encoding
aPrivateKey: yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU=
bPublicKey:  jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=
aSharedKey:  WEALguIF70iwU84lkJiqV14cvABDxYuETiAF4DHGYBg=
ciphertext:  VYjzzn9QtyqqC2HZx4UAPw==:Lh7ESuKTjOfC5jPDq5D6f1siD/bzKNS/lh26BfmGdHkK4jeD88Aoyrx99mSHOO/F
output is    (Base64) iv : (Base64) ciphertext

* * * decryption * * *
ciphertext:  VYjzzn9QtyqqC2HZx4UAPw==:Lh7ESuKTjOfC5jPDq5D6f1siD/bzKNS/lh26BfmGdHkK4jeD88Aoyrx99mSHOO/F
input is     (Base64) iv : (Base64) ciphertext
bPrivateKey: yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=
aPublicKey:  b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0=
bSharedKey:  WEALguIF70iwU84lkJiqV14cvABDxYuETiAF4DHGYBg=
decrypt.text:The quick brown fox jumps over the lazy dog

```

Last update: Nov. 26th 2020

Back to the main page: [readme.md](readme.md)