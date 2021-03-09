# Cross-platform cryptography

## PQC McEliece encryption

The McEliece algorithm is a **public key encryption** algorithm that is used to encrypt small amounts of data, primary for the encryption of symmetric keys. The **Classic McEliece algorithm** is part of round 3 "Post-Quantum Cryptography competition", held by the NIST (National Institute of Standards and Technology, USA) to find an algorithm that could get a successor of RSA- or Elliptic curve encryption schemes.

**Before going on it is important that you understand that this implementation is running but not tested against "official" values (e.g. test vectors). For that reason the program will print out a "serious warning" - kindly take this program as case study to see how the algorithm is working.**

### What are the steps in the program?

1. the program generates a McEliece key pair
2. to check that the keys could get stored in binary (byte array) form I'm converting the keys to a byte array and vice versa (you need this for publishing or sending the public key to the person who has to encrypt data for you)
3. a plaintext (here 32 chars as a replacement for a 32 byte long encryption key) gets encrypted to the ciphertext with the public key
4. the ciphertext gets decrypted with the private key and printed out

### What are the key lengths of the private and public key?

The private and public keys from McEliece algorithm are **much much longer** than their RSA- and EC pendants - see the comparison table below.

### What is the length of the ciphertext?

The ciphertext is of the same length as RSA- or EC encryption schemes - see the table below.

### What is the speed of this algorithm?

I'm sorry, but as there is no "official" Java implementation I do not want to run any benchmark tests. 

### Can you give an overview about the key lengths and the ciphertext length?

The following table gives an overview about the key- and ciphertext lengths for RSA-, EC- and McEliece encryption schemes (all data are for "PKCS#8 encoded data") when encrypting the plaintext "1234567890ABCDEF1122334455667788": 

| Encryption scheme | key parameter | Private key length | Public key length | ciphertext length |
| ------ | :---: | :----: | :---: | :----: |
| RSA | 2.048 bit | 1218 | 294 | 256 |
| ECIES  | curve P-256 | 67 | 91 | 128 |
| McEliece | not specified | 289997 | 383540 | 256 |

### Do I need an additional library?

Yes, you need the Bouncy Castle library (tested with version 1.68), get it here (Maven): [https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on](https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on)

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

View the source code: [PqcMcElieceEncryption.java](../PostQuantumCryptography/McElieceEncryption/PqcMcElieceEncryption.java)

Run the program online: [repl.it PqcJavaMcElieceEncryption](https://repl.it/@javacrypto/PqcJavaMcElieceEncryption#Main.java/)

This is an output (your will differ because of a random element):

```plaintext
PQC McEliece public key encryption

************************************
* # # SERIOUS SECURITY WARNING # # *
* This program is a CONCEPT STUDY  *
* for the algorithm                *
* McEliece [public key encryption] *
* The program is using an OUTDATED *
* parameter set and I cannot       *
* check for the correctness of the *
* output and other details         *
*                                  *
*     DO NOT USE THE PROGRAM IN    *
*     ANY PRODUCTION ENVIRONENT    *
************************************

generated private key length: 289997
generated public key length:  383540

* * * encrypt the keyToEncrypt with the public key * * *
ciphertext length: 256 data:
71a1fd88353edbe2aa3ef6df5a468bfa8da9dafe6b9235cad6d29e4e76f2b64bfc5910ec0fbc6c77b9128366779e6e5ff1df16cfeef47540c6cf95810aef9efe3eeb5bb7e4a3b91170d5c76ea7795f30f588f1ffba01a1e301d80e449d04c00a79696f5a052848e03e7b06299e200cf85e1f2535b797c12c6563847252042532d399df0b58fd211c03001b9102ca46b436f69d2008dd9b31e9d105c68d894f99e64aaa9fa9313d07ce58ae616a178a8198c585f8c285ab1ef0e788a8440dfa18aad52221d6dab9ee2ad4aca6274721a9279d90dd865be70b8e7d946ffaa0b832a4e39d58a058ea581d41053e202a98d9fb6ada59a3b7dea760c72fec9e9bf03b

* * * decrypt the ciphertext with the private key * * *
decryptedtext: 1234567890ABCDEF1122334455667788
decryptedtext equals to keyToEncrypt: true

```

Last update: Mar. 08th 2021

Back to the [PQC overview page](pqc_overview.md) or the main page: [readme.md](../readme.md)