# Cross-platform cryptography

## PQC FALCON signature using Liboqs library

The FALCON algorithm is a **signature algorithm** that is used to sign and verify data with a Private- / Public key pair. The **Rainbow algorithm** is part of round 3 "Post-Quantum Cryptography competition", held by the NIST (National Institute of Standards and Technology, USA) to find an algorithm that could get a successor of RSA- or Elliptic curve signature schemes. "FALCON" stands for "FAst Fourier Lattice-based COmpact signatures over NTRU".

This implementation is a sample program taken from the "official" tests for the Java binding of Liboqs, liboqs-java. I modified the program to get a similar output like my [own implementation of FALCON](pqc_falcon_signature.md).

**Before going on it is important that you understand that this implementation is running but not tested against "official" values (e.g. test vectors). For that reason the program will print out a "serious warning" - kindly take this program as case study to see how the algorithm is working.**

### What are the steps in the program?

1. the program generates a FALCON key pair
2. to check that the keys could get stored I'm dumping the key objects and vice versa (you need this for publishing or sending the public key to the person who has to verify the signature)
3. a dataToSign string gets signed with the private key
4. the signature gets verified with the public key and the result is printed out

### What are the key lengths of the private and public key?

The private and public keys from FALCON algorithm are **longer** than their RSA- and EC pendants - see the comparison table below.

### What is the length of the signature?

The signature is longer than RSA- or EC signature schemes and depends on the key length - see the table below.

### What is the speed of this algorithm?

I'm sorry, but as there is no "official" Java implementation I do not want to run any benchmark tests. 

### Can you give an overview about the key lengths and the signature length?

The following table gives an overview about the key- and signature lengths for RSA-, EC- and FALCON signature schemes (all data are for "PKCS#8 encoded data") when signing the dataToSign "The quick brown fox jumps over the lazy dog": 

| Signature scheme | key parameter | Private key length | Public key length | signature length |
| ------ | :---: | :----: | :---: | :----: |
| RSA PKCS#1.5 padding| 2.048 bit | 1218 | 294 | 256 |
| RSA PSS padding| 2.048 bit | 1218 | 294 | 256 |
| ECDSA  | curve P-256 | 67 | 91 | 72 |
| FALCON | 512 | 1281 | 897 | 657 |
| FALCON | 1024 | 2305 | 1793 | 1266 |

If you like to get a general overview about the sizes compared to other algorithms supported by the Liboqs visit my page [PQC Liboqs algorithms facts  overview](pqc_overview_liboqs_algorithm_facts.md).

### Do I need an additional library?

Yes, you need the core library and the Java binding of OpenQuantumSafe's Libqs library:

1. the core library, get it here: [https://github.com/open-quantum-safe/liboqs](https://github.com/open-quantum-safe/liboqs) 

2. the Java binding, get it here: [https://github.com/open-quantum-safe/liboqs-java](https://github.com/open-quantum-safe/liboqs-java) 

Important note: you need to compile the core library (C framework) as there are no compiled libraries for different Operating Systems (like Windows, Mac or Linux) available!

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

This is an output (your will differ because of a random element):

```plaintext
PQC signature using
OpenQuantumSafe.org liboqs-java library
SIG algorithm: Falcon-512

*** generate a key pair for signer ***
generated private key length: 1281
generated public key length:  897
private and public key for signer saved to file

* * * sign the plaintext with the private key * * *
signature length: 657 data: in a separate file

* * * verify the signature against the plaintext with the public key * * *

signature is verified: true

information from Liboqs about used algorithm:
Signature Details:
  Name: Falcon-512
  Version: supercop-20201018 via https://github.com/jschanck/package-pqclean/tree/cea1fa5a/falcon
  Claimed NIST level: 1
  Is IND-CCA: true
  Length public key (bytes): 897
  Length secret key (bytes): 1281
  Maximum length signature (bytes): 690

```

Last update: Mar. 19th 2021

Back to the [PQC with library Liboqs overview](pqc_liqoqs_overview.md), to the [PQC overview page](pqc_overview.md) or the main page: [readme.md](../readme.md)
