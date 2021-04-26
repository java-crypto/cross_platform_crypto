# Cross-platform cryptography

## PQC NTRU encryption

The NTRU algorithm is a **public key encryption** algorithm that is used to encrypt small amounts of data, primary for the encryption of symmetric keys. The **NTRU algorithm** is part of round 3 "Post-Quantum Cryptography competition", held by the NIST (National Institute of Standards and Technology, USA) to find an algorithm that could get a successor of RSA- or Elliptic curve encryption schemes.

**Important information: This code is using an outdated parameter set** ("APR2011_743_FAST") so please do not rely on the results and wait for an "official" implementation when the competition is over.

**Before going on it is important that you understand that this implementation is running but not tested against "official" values (e.g. test vectors). For that reason the program will print out a "serious warning" - kindly take this program as case study to see how the algorithm is working.**

### What are the steps in the program?

1. the program generates a NTRU key pair
2. to check that the keys could get stored in binary (byte array) form I'm converting the keys to a byte array and vice versa (you need this for publishing or sending the public key to the person who has to encrypt data for you)
3. a plaintext (here 32 chars as a replacement for a 32 byte long encryption key) gets encrypted to the ciphertext with the public key
4. the ciphertext gets decrypted with the private key and printed out

### What are the key lengths of the private and public key?

The private and public keys from NTRU algorithm are **longer** than their RSA- and EC pendants - see the comparison table below.

### What is the length of the ciphertext?

The ciphertext is **longer** than the RSA- or EC encryption schemes - see the table below.

### What is the speed of this algorithm?

I'm sorry, but as there is no "official" Java implementation I do not want to run any benchmark tests. 

### Can you give an overview about the key lengths and the ciphertext length?

The following table gives an overview about the key- and ciphertext lengths for RSA-, EC- and NTRU encryption schemes (all data are for "PKCS#8 encoded data") when encrypting the plaintext "1234567890ABCDEF1122334455667788": 

| Encryption scheme | key parameter | Private key length | Public key length | ciphertext length |
| ------ | :---: | :----: | :---: | :----: |
| RSA | 2.048 bit | 1218 | 294 | 256 |
| ECIES  | curve P-256 | 67 | 91 | 128 |
| NTRU | APR2011_743_FAST | 1128 | 1022 | 1022 |

If you like to get a general overview about the sizes compared to other algorithms visit my page [PQC algorithms facts overview](pqc_overview_algorithm_facts.md).

### Do I need an additional library?

Yes, you need the **<u>extended</u>** Bouncy Castle library (tested with version 1.68), get it here (Maven): [https://mvnrepository.com/artifact/org.bouncycastle/bcprov-ext-jdk15on](https://mvnrepository.com/artifact/org.bouncycastle/bcprov-ext-jdk15on)

### I read that NTRU is broken?

Don't mix the "NTRU encryption" with "NTRU signing" - the signing algorithm was broken some year ago but the encryption algorithm is not!

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

View the source code: [PqcNtruEncryption.java](../PostQuantumCryptography/NtruEncryption/PqcNtruEncryption.java)

Run the program online: [repl.it PqcJavaNtruEncryption](https://repl.it/@javacrypto/PqcJavaNtruEncryption#Main.java/)

This is an output (your will differ because of a random element):

```plaintext
PQC NTRU key encapsulation mechanism (KEM)

************************************
* # # SERIOUS SECURITY WARNING # # *
* This program is a CONCEPT STUDY  *
* for the algorithm                *
* NTRU [public key encryption]     *
* The program is using an OUTDATED *
* parameter set and I cannot       *
* check for the correctness of the *
* output and other details         *
*                                  *
*    DO NOT USE THE PROGRAM IN     *
*    ANY PRODUCTION ENVIRONMENT    *
************************************

generated private key length: 1128
generated public key length:  1022

* * * encrypt the keyToEncrypt with the public key * * *
ciphertext length: 1022 data: ** not shown due to length

* * * decrypt the ciphertext with the private key * * *
decryptedtext: 1234567890ABCDEF1122334455667788
decryptedtext equals to keyToEncrypt: true

used algorithm parameter: EncryptionParameters(N=743 q=2048 polyType=PRODUCT df1=11 df2=11 df3=15 dm0=220 db=256 c=10 minCallsR=27 minCallsMask=14 hashSeed=true hashAlg=org.bouncycastle.crypto.digests.SHA512Digest@1c72da34 oid=[0, 7, 105] sparse=false)

```

Last update: Mar. 11th 2021

Back to the [PQC overview page](pqc_overview.md) or the main page: [readme.md](../readme.md)