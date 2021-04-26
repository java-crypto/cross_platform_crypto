# Cross-platform cryptography

## PQC FALCON signature

The FALCON algorithm is a **signature algorithm** that is used to sign and verify data with a Private- / Public key pair. The **Rainbow algorithm** is part of round 3 "Post-Quantum Cryptography competition", held by the NIST (National Institute of Standards and Technology, USA) to find an algorithm that could get a successor of RSA- or Elliptic curve signature schemes. "FALCON" stands for "FAst Fourier Lattice-based COmpact signatures over NTRU".

**Before going on it is important that you understand that this implementation is running but not tested against "official" values (e.g. test vectors). For that reason the program will print out a "serious warning" - kindly take this program as case study to see how the algorithm is working.**

### What are the steps in the program?

1. the program generates a FALCON key pair
2. to check that the keys could get stored I'm dumping the key objects and vice versa (you need this for publishing or sending the public key to the person who has to verify the signature)
3. a dataToSign string gets signed with the private key
4. the signature gets verified with the public key and the result is printed out

### What are the key lengths of the private and public key?

The private and public keys from FALCON algorithm are **much longer** than their RSA- and EC pendants - see the comparison table below.

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
| FALCON | 512 | 180240 | 180347 | 666 |
| FALCON | 1024 | 384743 | 384850 | 1280 |

If you like to get a general overview about the sizes compared to other algorithms visit my page [PQC algorithms facts overview](pqc_overview_algorithm_facts.md).

### Do I need an additional library?

Yes, you need two libraries: 

1. the FALCON library GitHub: [https://github.com/tprest/falcon.py](https://github.com/tprest/falcon.py)
2. the PyCryptoDome library:  [https://pypi.org/project/pycryptodome/](https://pypi.org/project/pycryptodome/)

Kindly note that I uploaded and modified the FALCON files to the online compiler to get it run there.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

View the source code: [PqcFalconSignature.py](../PostQuantumCryptography/FalconSignature/PqcFalconSignature.py)

Run the program online: [repl.it PqcFalconSignature](https://repl.it/@javacrypto/PQCPythonFalconSignature#main.py/)

This is an output (your will differ because of a random element):

```plaintext
PQC FALCON signature

************************************
* # # SERIOUS SECURITY WARNING # # *
* This program is a CONCEPT STUDY  *
* for the algorithm                *
* FALCON [signature]               *
* The program is using an OUTDATED *
* parameter set and I cannot       *
* check for the correctness of the *
* output and other details         *
*                                  *
*    DO NOT USE THE PROGRAM IN     *
*    ANY PRODUCTION ENVIRONMENT     *
************************************

the generation of the private key will take some seconds... please wait...
generated private key length: 180240
generated public key length: 180347

* * * sign the dataToSign with the private key * * *
signature length: 666

signature (Base64): OUrFbM0ilgCVb3x5Le8SUwCOhQ/sAxsB8o6L3IAqDx77pwvHZNcuBVBIzcBbL1sTBlarBc5ZtVYvzMY2wnxncEVVLexvpytuATVumFQlG3gTj17pg0CrDc5zcx+U1VcXRgOanuLUxD1JlaVbVoLHAtpOWacfY5dOuroPVxeGvyw6PsIMyDtqCwaFsvI2LUyH4WpxkwlWQ/VvjLmAMbDvSUsm9A4M5JdQbs28pxq65BDad2ILRdkktm/b0XRA9+xsYuUrpOF5d9YF2HLQEZ+uUfFtouJLGw4U1wwgqIrKq6qV9klJ59XITvf2RQl3VrDy+WcXdh8AQyRjIMIRVwbPqJfCcmbZXcUkcXsd6221JGwmGjW5uzWZ3p+5hWMY/Xp5VnWKNWH8oWjmrSsHoEY3zbmtar2JgvcJsLMMb6NBEhdXTV6Iygy/Z01RdRxXznuK0gnUt1jEm4QzKyDgo2gUHI9yGFlqOOxHWsYNMUUtUFReBaJDbXF0fM6dI06ALWoOvzEaLNMSpqYcq3c5W+7Sf2lFji2lJ7CO3b0C65BEJ91HgCC5nL6xnz0xIsXPLa2rLkAen57JIyXV/KxM1KJhzoGYZ6ZVLRrYRCV1NZWz3If4LLpFY/XSV6ebXD1I17boI6Jg1n8K46oiOAb4g/O3qNK/8pNhYvY1m1uchTEmWyGDQupoojjzRZusm5ncywoHOuUWnsdmFB2EF3CQaQ/Wf4/7isopeXS36we6ea3Lf54wR9nENkTROj5nHYNliP2PonSqdT0xK8zEvZAaXIUQMYakxa1OYUoKo0FVzOqJMeJilIxhHvNa1bfO71dxnMobWH2dgyz1Y1MYYijan7JIQWGfnEYrPY5vitxLDdt/dZkkAAAAAAAAAAAA

* * * verify the signature with the public key * * *
signature verified: True

```

Last update: Mar. 11th 2021

Back to the [PQC overview page](pqc_overview.md) or the main page: [readme.md](../readme.md)
