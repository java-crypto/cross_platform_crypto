# Cross-platform cryptography

## PQC Rainbow signature

The Rainbow algorithm is a **signature algorithm** that is used to sign and verify data with a Private- / Public key pair. The **Rainbow algorithm** is part of round 3 "Post-Quantum Cryptography competition", held by the NIST (National Institute of Standards and Technology, USA) to find an algorithm that could get a successor of RSA- or Elliptic curve signature schemes.

**Before going on it is important that you understand that this implementation is running but not tested against "official" values (e.g. test vectors). For that reason the program will print out a "serious warning" - kindly take this program as case study to see how the algorithm is working.**

### What are the steps in the program?

1. the program generates a Rainbow key pair
2. to check that the keys could get stored in binary (byte array) form I'm converting the keys to a byte array and vice versa (you need this for publishing or sending the public key to the person who has to verify the signature)
3. a dataToSign string gets signed with the private key
4. the signature gets verified with the public key and the result is printed out

### What are the key lengths of the private and public key?

The private and public keys from Rainbow algorithm are **much longer** than their RSA- and EC pendants - see the comparison table below.

### What is the length of the signature?

The signature is shorter than RSA- or EC signature schemes - see the table below.

### What is the speed of this algorithm?

I'm sorry, but as there is no "official" Java implementation I do not want to run any benchmark tests. 

### Can you give an overview about the key lengths and the signature length?

The following table gives an overview about the key- and signature lengths for RSA-, EC- and Rainbow signature schemes (all data are for "PKCS#8 encoded data") when signing the dataToSign "The quick brown fox jumps over the lazy dog": 

| Encryption scheme | key parameter | Private key length | Public key length | signature length |
| ------ | :---: | :----: | :---: | :----: |
| RSA PKCS#1.5 padding| 2.048 bit | 1218 | 294 | 256 |
| RSA PSS padding| 2.048 bit | 1218 | 294 | 256 |
| ECIES  | curve P-256 | 67 | 91 | 71 |
| Rainbow | 1.024 | 15576 | 16277 | 33 |


### Do I need an additional library?

Yes, you need the Bouncy Castle library (tested with version 1.68), get it here (Maven): [https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on](https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on)

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

View the source code: [PqcRainbowSignature.java](../PostQuantumCryptography/RainbowSignature/PqcRainbowSignature.java)

Run the program online: [repl.it PqcRainbowSignature](https://repl.it/@javacrypto/PqcRainbowSignature#Main.java/)

This is an output (your will differ because of a random element):

```plaintext
PQC Rainbow signature

************************************
* # # SERIOUS SECURITY WARNING # # *
* This program is a CONCEPT STUDY  *
* for the algorithm                *
* Rainbow [signature]              *
* The program is using an OUTDATED *
* parameter set and I cannot       *
* check for the correctness of the *
* output and other details         *
*                                  *
*     DO NOT USE THE PROGRAM IN    *
*     ANY PRODUCTION ENVIRONENT    *
************************************

generated private key length: 15576
generated public key length:  16277

* * * sign the dataToSign with the private key * * *
signature length: 33 data:
0b0613324852cd5890208920209c362851cf40fb5b9fd78edb2a6e92fe8ac96046

* * * verify the signature with the public key * * *
the signature is verified: true

```

Last update: Mar. 09th 2021

Back to the [PQC overview page](pqc_overview.md) or the main page: [readme.md](../readme.md)