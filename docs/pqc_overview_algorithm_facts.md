# Cross-platform cryptography

## Post Quantum Cryptography (PQC) algorithm facts

This article just gives an overview about the algorithm facts I have published in several articles regarding Post Quantum Cryptography.


| type| Encryption scheme | key parameter | Private key length | Public key length | ciphertext length |
| :----: | ------ | :---: | :----: | :---: | :----: |
| public key encryption | RSA | 2.048 bit | 1218 | 294 | 256 |
| public key encryption | ECIES  | curve P-256 | 67 | 91 | 128 |
| public key encryption | McEliece | not specified | 289997 | 383540 | 256 |
| public key encryption | NTRU | APR2011_743_FAST | 1128 | 1022 | 1022 |
| key exchange (KEM) | Chrystals-Kyber | not specified | 2400 | 1184 | 1088 |
| key exchange (KEM) | Sike | P751 | 126 | 564 | 596 |

| type| Sgignature scheme | key parameter | Private key length | Public key length | signature length |
| :----: | ------ | :---: | :----: | :---: | :----: |
| signature | RSA PKCS#1.5 padding| 2.048 bit | 1218 | 294 | 256 |
| signature | RSA PSS padding| 2.048 bit | 1218 | 294 | 256 |
| signature | ECDSA  | curve P-256 | 67 | 91 | 72 |
| signature | FALCON | 512 | 180240 | 180347 | 666 |
| signature | FALCON | 1024 | 384743 | 384850 | 1280 |
| signature | Rainbow | 1.024 | 15576 | 16277 | 33 |
| signature | Sphincs | SHA3-256 | 1088 | 1056 | 41000 |

Last update: Mar. 11th 2021

Back to the [PQC overview page](pqc_overview.md) or the main page: [readme.md](../readme.md)

