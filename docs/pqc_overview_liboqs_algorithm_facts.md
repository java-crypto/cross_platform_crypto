# Cross-platform cryptography

## Post Quantum Cryptography (PQC) Liboqs algorithm facts

This article just gives an overview about the algorithm facts from the library **OpenQuantumSafe liboqs-java**. The values were generated during running the JUnit-tests.

For details see my article [Post Quantum Cryptography (PQC) with library Liboqs overview](pqc_liqoqs_overview.md).

| type | Encryption scheme | Private key length | Public key length | ciphertext length |
| :----: | ------ | :---: | :----: | :---: | 
| | **classic algorithms:** | | |
| public key encryption | RSA 2.048 |  1218 | 294 | 256 |
| public key encryption | ECIES curve P-256 | 67 | 91 | 128 |
| | **PQC algorithms ("own"):** | | |
| public key encryption | McEliece | 289997 | 383540 | 256 |
| public key encryption | NTRU APR2011_743_FAST | 1128 | 1022 | 1022 |
| key exchange (KEM) | BIKE L1-CPA | 3110 | 2542 | 2542 |
| key exchange (KEM) | BIKE L1-FO | 6460| 2946 | 2946 |
| key exchange (KEM) | BIKE L3-CPA | 5788 | 4964 | 4964 |
| key exchange (KEM) | BIKE L3-FO | 13236 | 6206 | 6206 |
| key exchange (KEM) | Sike P751 | 126 | 564 | 596 |
| | **PQC algorithms from Liboqs:** | | |
| key exchange (KEM) | BIKE1-L1-CPA | 3110 | 2542 | 2542 |
| key exchange (KEM) | BIKE1-L3-CPA | 5788 | 4964 | 4964 |
| key exchange (KEM) | BIKE1-L1-FO | 6460 | 2946 | 2946 |
| key exchange (KEM) | BIKE1-L3-FO | 13236 | 6206 | 6206 |
| key exchange (KEM) | Classic-McEliece-348864 | 6452 | 261120 | 128 |
| key exchange (KEM) | Classic-McEliece-348864f | 6452 | 261120 | 128 |
| key exchange (KEM) | Classic-McEliece-460896 | 13568 | 524160 | 188 |
| key exchange (KEM) | Classic-McEliece-460896f | 13568 | 524160 | 188 |
| key exchange (KEM) | Classic-McEliece-6688128 | 13892 | 1044992 | 240 |
| key exchange (KEM) | Classic-McEliece-6688128f | 13892 | 1044992 | 240 |
| key exchange (KEM) | Classic-McEliece-6960119 | 13908 | 1047319 | 226 |
| key exchange (KEM) | Classic-McEliece-6960119f | 13908 | 1047319 | 226 |
| key exchange (KEM) | Classic-McEliece-8192128 | 14080 | 1357824 | 240 |
| key exchange (KEM) | Classic-McEliece-8192128f | 14080 | 1357824 | 240 |
| key exchange (KEM) | HQC-128 | 2289 | 2249 | 4481 |
| key exchange (KEM) | HQC-192 | 4562 | 4522 | 9026 |
| key exchange (KEM) | HQC-256 | 7285 | 7245 | 14469 |
| key exchange (KEM) | Kyber512 | 1632 | 800 | 768 |
| key exchange (KEM) | Kyber768 | 2400 | 1184 | 1088 |
| key exchange (KEM) | Kyber1024 | 3168 | 1568 | 1568 |
| key exchange (KEM) | Kyber512-90s | 1632 | 800 | 768 |
| key exchange (KEM) | Kyber768-90s | 2400 | 1184 | 1088 |
| key exchange (KEM) | Kyber1024-90s | 3168 | 1568 | 1568 |
| key exchange (KEM) | NTRU-HPS-2048-509 | 935 | 699 | 699 |
| key exchange (KEM) | NTRU-HPS-2048-677 | 1234 | 930 | 930 |
| key exchange (KEM) | NTRU-HPS-4096-821 | 1590 | 1230 | 1230 |
| key exchange (KEM) | NTRU-HRSS-701 | 1450 | 1138 | 1138 |
| key exchange (KEM) | ntrulpr653 | 1125 | 897 | 1025 |
| key exchange (KEM) | ntrulpr761 | 1294 | 1039 | 1167 |
| key exchange (KEM) | ntrulpr857 | 1463 | 1184 | 1312 |
| key exchange (KEM) | sntrup653 | 1518 | 994 | 897 |
| key exchange (KEM) | sntrup761 | 1763 | 1158 | 1039 |
| key exchange (KEM) | sntrup857 | 1999 | 1322 | 1184 |
| key exchange (KEM) | LightSaber-KEM | 1568 | 672 | 736 |
| key exchange (KEM) | Saber-KEM | 2304 | 992 | 1088 |
| key exchange (KEM) | FireSaber-KEM | 3040 | 1312 | 1472 |
| key exchange (KEM) | FrodoKEM-640-AES | 19888 | 9616 | 9720 |
| key exchange (KEM) | FrodoKEM-640-SHAKE | 19888 | 9616 | 9720 |
| key exchange (KEM) | FrodoKEM-976-AES | 31296 | 15632 | 15744 |
| key exchange (KEM) | FrodoKEM-976-SHAKE | 31296 | 15632 | 15744 |
| key exchange (KEM) | FrodoKEM-1344-AES | 43088 | 21520 | 21632 |
| key exchange (KEM) | FrodoKEM-1344-SHAKE | 43088 | 21520 | 21632 |
| key exchange (KEM) | SIDH-p434 | 28 | 330 | 330 |
| key exchange (KEM) | SIDH-p503 | 32 | 378 | 378 |
| key exchange (KEM) | SIDH-p610 | 39 | 462 | 462 |
| key exchange (KEM) | SIDH-p751 | 48 | 564 | 564 |
| key exchange (KEM) | SIDH-p434-compressed | 28 | 197 | 197 |
| key exchange (KEM) | SIDH-p503-compressed | 32 | 225 | 225 |
| key exchange (KEM) | SIDH-p610-compressed | 39 | 274 | 274 |
| key exchange (KEM) | SIDH-p751-compressed | 48 | 335 | 335 |
| key exchange (KEM) | SIKE-p434 | 374 | 330 | 346 |
| key exchange (KEM) | SIKE-p503 | 434 | 378 | 402 |
| key exchange (KEM) | SIKE-p610 | 524 | 462 | 486 |
| key exchange (KEM) | SIKE-p751 | 644 | 564 | 596 |
| key exchange (KEM) | SIKE-p434-compressed | 350 | 197 | 236 |
| key exchange (KEM) | SIKE-p503-compressed | 407 | 225 | 280 |
| key exchange (KEM) | SIKE-p610-compressed | 491 | 274 | 336 |
| key exchange (KEM) | SIKE-p751-compressed | 602 | 335 | 410 |
| key exchange (KEM) | DEFAULT | 19888 | 9616 | 9720 |


| type| Signature scheme | Private key length | Public key length | signature length |
| :----: | ------ | :---: | :---: | :----: |
| | **classic algorithms:** | | |
| signature | RSA PKCS#1.5 padding 2.048 bit | 1218 | 294 | 256 |
| signature | RSA PSS padding 2.048 bit | 1218 | 294 | 256 |
| | **PQC algorithms ("own"):** | | |
| signature | ECDSA curve P-256 | 67 | 91 | 72 |
| signature | FALCON 512 | 180240 | 180347 | 666 |
| signature | FALCON 1024 | 384743 | 384850 | 1280 |
| signature | Rainbow 1.024 | 15576 | 16277 | 33 |
| signature | Sphincs SHA3-256 | 1088 | 1056 | 41000 |
| | **PQC algorithms from Liboqs:** | | |
| signature | Dilithium2 | 2528 | 1312 | 2420 |
| signature | Dilithium3 | 4000 | 1952 | 3293 |
| signature | Dilithium5 | 4864 | 2592 | 4595 |
| signature | Dilithium2-AES | 2528 | 1312 | 2420 |
| signature | Dilithium3-AES | 4000 | 1952 | 3293 |
| signature | Dilithium5-AES | 4864 | 2592 | 4595 |
| signature | Falcon-512 | 1281 | 897 | 659 |
| signature | Falcon-1024 | 2305 | 1793 | 1271 |
| signature | Rainbow-I-Classic | 103648 | 161600 | 66 |
| signature | Rainbow-I-Circumzenithal | 103648 | 60192 | 66 |
| signature | Rainbow-I-Compressed | 64 | 60192 | 66 |
| signature | Rainbow-III-Classic | 626048 | 882080 | 164 |
| signature | Rainbow-III-Circumzenithal | 626048 | 264608 | 164 |
| signature | Rainbow-III-Compressed | 64 | 264608 | 164 |
| signature | Rainbow-V-Classic | 1408736 | 1930600 | 212 |
| signature | Rainbow-V-Circumzenithal | 1408736 | 536136 | 212 |
| signature | Rainbow-V-Compressed | 64 | 536136 | 212 |
| signature | SPHINCS+-Haraka-128f-robust | 64 | 32 | 16976 |
| signature | SPHINCS+-Haraka-128f-simple | 64 | 32 | 16976 |
| signature | SPHINCS+-Haraka-128s-robust | 64 | 32 | 8080 |
| signature | SPHINCS+-Haraka-128s-simple | 64 | 32 | 8080 |
| signature | SPHINCS+-Haraka-192f-robust | 96 | 48 | 35664 |
| signature | SPHINCS+-Haraka-192f-simple | 96 | 48 | 35664 |
| signature | SPHINCS+-Haraka-192s-robust | 96 | 48 | 17064 |
| signature | SPHINCS+-Haraka-192s-simple | 96 | 48 | 17064 |
| signature | SPHINCS+-Haraka-256f-robust | 128 | 64 | 49216 |
| signature | SPHINCS+-Haraka-256f-simple | 128 | 64 | 49216 |
| signature | SPHINCS+-Haraka-256s-robust | 128 | 64 | 29792 |
| signature | SPHINCS+-Haraka-256s-simple | 128 | 64 | 29792 |
| signature | SPHINCS+-SHA256-128f-robust | 64 | 32 | 16976 |
| signature | SPHINCS+-SHA256-128f-simple | 64 | 32 | 16976 |
| signature | SPHINCS+-SHA256-128s-robust | 64 | 32 | 8080 |
| signature | SPHINCS+-SHA256-128s-simple | 64 | 32 | 8080 |
| signature | SPHINCS+-SHA256-192f-robust | 96 | 48 | 35664 |
| signature | SPHINCS+-SHA256-192f-simple | 96 | 48 | 35664 |
| signature | SPHINCS+-SHA256-192s-robust | 96 | 48 | 17064 |
| signature | SPHINCS+-SHA256-192s-simple | 96 | 48 | 17064 |
| signature | SPHINCS+-SHA256-256f-robust | 128 | 64 | 49216 |
| signature | SPHINCS+-SHA256-256f-simple | 128 | 64 | 49216 |
| signature | SPHINCS+-SHA256-256s-robust | 128 | 64 | 29792 |
| signature | SPHINCS+-SHA256-256s-simple | 128 | 64 | 29792 |
| signature | SPHINCS+-SHAKE256-128f-robust | 64 | 32 | 16976 |
| signature | SPHINCS+-SHAKE256-128f-simple | 64 | 32 | 16976 |
| signature | SPHINCS+-SHAKE256-128s-robust | 64 | 32 | 8080 |
| signature | SPHINCS+-SHAKE256-128s-simple | 64 | 32 | 8080 |
| signature | SPHINCS+-SHAKE256-192f-robust | 96 | 48 | 35664 |
| signature | SPHINCS+-SHAKE256-192f-simple | 96 | 48 | 35664 |
| signature | SPHINCS+-SHAKE256-192s-robust | 96 | 48 | 17064 |
| signature | SPHINCS+-SHAKE256-192s-simple | 96 | 48 | 17064 |
| signature | SPHINCS+-SHAKE256-256f-robust | 128 | 64 | 49216 |
| signature | SPHINCS+-SHAKE256-256f-simple | 128 | 64 | 49216 |
| signature | SPHINCS+-SHAKE256-256s-robust | 128 | 64 | 29792 |
| signature | SPHINCS+-SHAKE256-256s-simple | 128 | 64 | 29792 |
| signature | picnic_L1_FS | 49 | 33 | 32880 |
| signature | picnic_L1_UR | 49 | 33 | 53961 |
| signature | picnic_L1_full | 52 | 35 | 30922 |
| signature | picnic_L3_FS | 73 | 49 | 74348 |
| signature | picnic_L3_UR | 73 | 49 | 121845 |
| signature | picnic_L3_full | 73 | 49 | 68347 |
| signature | picnic_L5_FS | 97 | 65 | 128344 |
| signature | picnic_L5_UR | 97 | 65 | 209506 |
| signature | picnic_L5_full | 97 | 65 | 121294 |
| signature | picnic3_L1 | 52 | 35 | 12392 |
| signature | picnic3_L3 | 73 | 49 | 27800 |
| signature | picnic3_L5 | 97 | 65 | 48416 |
| signature | DEFAULT | 2528 | 1312 | 2420 |

### Do you provide your source files to let me get the details as well?

In my GitHub repository [PostQuantumCryptography with Liboqs: JavaChanges](https://github.com/java-crypto/cross_platform_crypto/tree/main/PostQuantumCryptography/Liboqs/JavaChanges) I'm providing the changed and appended files so you are been able to run this as well. 

For each algorithm I created a log file with all details, get all log files in my GitHub repository [PostQuantumCryptography with Liboqs: LogFiles](https://github.com/java-crypto/cross_platform_crypto/tree/main/PostQuantumCryptography/Liboqs/LogFiles).

Last update: Mar. 19th 2021

Back to the [PQC with library Liboqs overview](pqc_liqoqs_overview.md), to the [PQC overview page](pqc_overview.md) or the main page: [readme.md](../readme.md)

