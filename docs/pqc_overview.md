# Cross-platform cryptography

## Post Quantum Cryptography (PQC) overview

The articles regarding **Post Quantum Cryptography (PQC)** are special as I cannot provide running implementations "cross platform wide" because the libraries for the new algorithms are not widely available (e.g. like for Libsodium or JWT) or need a lot of additional work to get them run.

Instead I'm concentrating on selected algorithms and frameworks to show how the "new world of cryptography" looks like. That may sound pompous but after reading this article you may think as me.

Before we are going into details I give a short overview about the general theme.

### What is a quantum computer (QC)?

Wikipedia gives a short explanation: "*Computers that perform quantum computations are known as quantum computers.*" That sounds harmless but as they get a result on a formula for all inputs it sound possible that a quantum computer is been able to factorize large prime numbers or solve the elliptic curve discrete logarithm problem.

That means in fact: **all cryptographic functions that use RSA- or Elliptic Curve-based algorithms may become UNSECURE** at the moment a QC is on the market.

### What algorithm should I choose to be quantum computer safe?

There are several algorithms on the market that propose to be quantum computer safe but I recommend to have a close look to a running competition that is held by the [National Institute of Standards and Technology (NIST)](https://www.nist.gov/). In 2017 they started a "Call for Proposals" and actually (March 2021) we are in round 3 of the selection process. In the end they will propose one or two "winner algorithms" that will become the de facto standard algorithms (like "AES" some years before). The website [NIST Post-Quantum Cryptography PQC Round 3  Submissions](https://csrc.nist.gov/projects/post-quantum-cryptography/round-3-submissions) informs you about the algorithms that are in the final round.

A general and platform-wide availability of the algorithms will be given when the winners are named (for e.g. the well known Bouncy Castle cryptographic library [version 1.68] does not cover most of the final candidates).

### Can you give an overview about the affected systems?

The following table just gives a very <u>shortened overview</u> and I strongly recommend that you do a self study.

| Purpose | algorithm | safe before QC | safe after QC | some informations |
| ------ | :------: | :--: | :--: | --- |
| hashing | MD5 | :x: | :x: | the algorithm is **broken** so do not use it |
| hashing | SHA-1 | :x: | :x: | the algorithm was **broken** in one case so do not use it |
| hashing | SHA-256 | :white_check_mark: | :white_check_mark: | better use SHA3-256 |
| | | | | |
| symmetric encryption | DES | :x: | :x: | the algorithm is **deprecated** so do not use it |
| symmetric encryption | TDES | :x: | :x: | the algorithm is **deprecated** in next future so do not use it |
| symmetric encryption | AES 128 | :white_check_mark: | :x: | the algorithm **will get unsecure** with QC so change to AES 256 |
| symmetric encryption | AES 256 | :white_check_mark: | :white_check_mark: | the usage of HMAC secured mode like GCM is recommended |
| symmetric encryption | ChaCha20 | :white_check_mark: | :question: | no information available |
| symmetric encryption | XSalsa20 stream cipher | :white_check_mark: | :question: | no information available |
| | | | | |
| signature | RSA PKCS#1.5 padding | :white_check_mark: | :x: | as the signature is deterministic better use RSA PSS padding |
| signature | RSA PSS padding | :white_check_mark: | :x: | the signature **will get broken** with QC |
| signature | ECDSA | :white_check_mark: | :x: | the signature **will get broken** with QC |
| signature | Ed25519 curve | :white_check_mark: | :question: | no information available |
| signature | CRYSTALS-DILITHIUM | :white_check_mark: | :white_check_mark: | NIST round 3 candidate |
| signature | FALCON | :white_check_mark: | :white_check_mark: | NIST round 3 candidate |
| signature | Rainbow | :white_check_mark: | :white_check_mark: | NIST round 3 candidate |
| | | | | |
| public key encryption | RSA PKCS#1.5 padding | :white_check_mark: | :x: | as the signature is deterministic better use RSA PSS padding |
| public key encryption | RSA OAEP padding | :white_check_mark: | :x: | the signature **will get broken** with QC |
| public key encryption | ECIES | :white_check_mark: | :x: | the signature **will get broken** with QC |
| public key encryption | Classic McEliece | :white_check_mark: | :white_check_mark: | NIST round 3 candidate |
| public key encryption | NTRU | :white_check_mark: | :white_check_mark: | NIST round 3 candidate |
| public key encryption | SABER | :white_check_mark: | :white_check_mark: | NIST round 3 candidate |
| | | | | |
| key exchange (KEM) | Diffie-Hellman RSA | :white_check_mark: | :x: | the KEM **will get broken** with QC |
| key exchange (KEM) | Diffie-Hellman EC | :white_check_mark: | :x: | the KEM **will get broken** with QC |
| key exchange (KEM) | X25519 curve | :white_check_mark: | :question: | no information available |
| key exchange (KEM) | CRYSTALS-KYBER | :white_check_mark: | :white_check_mark: | NIST round 3 candidate |
| key exchange (KEM) | Sike | :white_check_mark: | :white_check_mark: | NIST round 3 candidate |

### What algorithms and information do you offer here?

| Purpose | algorithm description | Language | source code | Online compiler |
| ------ | :------: | :--: | :--: | :--: |
| [algorithms facts overview](pqc_overview_algorithm_facts.md) | information about key, ciphertext & signature sizes | | note: own algorithm implementations | |
| [PQC with Liboqs library overview](pqc_liboqs_overview.md)| general overview about the OpenQuantumSafe library | | |
| [liboqs algorithms facts overview](pqc_overview_algorithm_facts.md) | information about key, ciphertext & signature sizes | | note: liboqs algorithm implementations | |
| | | |
| Public key encryption | [McEliece](pqc_mcelice_encryption.md) | Java | [PqcMcElieceEncryption.java](../PostQuantumCryptography/McElieceEncryption/PqcMcElieceEncryption.java) | [repl.it PqcJavaMcElieceEncryption](https://repl.it/@javacrypto/PqcJavaMcElieceEncryption#Main.java/) |
| Public key encryption | [NTRU](pqc_ntru_encryption.md) | Java | [PqcNtruEncryption.java](../PostQuantumCryptography/NtruEncryption/PqcNtruEncryption.java) |  [repl.it PqcJavaNtruEncryption](https://repl.it/@javacrypto/PqcJavaNtruEncryption#Main.java/) |
| | | |
| key exchange (KEM) | [Chrystals-Kyber](pqc_chrystalskyber_kem.md) | NodeJs | [PqcChrystalsKyberKemNodeJs.js](../PostQuantumCryptography/ChrystalsKyberKem/PqcChrystalsKyberKemNodeJs.js)  | [repl.it PqcNodeJsChrystalsKyberKem ](https://repl.it/@javacrypto/PqcNodeJsChrystalsKyberKem#index.js) |
| key exchange (KEM) | [Sike](pqc_sike_kem.md) | Java | [PqcSikeKem.java](../PostQuantumCryptography/SikeKem/PqcJavaSikeKem.java) | [repl.it PqcJavaSikeKem](https://repl.it/@javacrypto/PqcJavaSikeKem#Main.java/)
| key exchange (KEM) | [Sike with Liboqs](pqc_sike_liboqs_kem.md) | Java | n.a. | n.a. |
| | | |
| signature | [FALCON](pqc_falcon_signature.md) | Python | [PqcFalconSignature.py](../PostQuantumCryptography/FalconSignature/PqcFalconSignature.py) | [repl.it PqcFalconSignature](https://repl.it/@javacrypto/PQCPythonFalconSignature#main.py/)
| signature | [FALCON with Liboqs](pqc_falcon_liboqs_signature.md) | Java | n.a. | n.a. |
| signature | [Rainbow](pqc_rainbow_signature.md) | Java | [PqcRainbowSignature.java](../PostQuantumCryptography/RainbowSignature/PqcRainbowSignature.java)| [repl.it PqcRainbowSignature](https://repl.it/@javacrypto/PqcJavaRainbowSignature#Main.java/) |
| signature | [Sphincs](pqc_sphincs_signature.md) | Java | [PqcSphincsSignature.java](../PostQuantumCryptography/SphincsSignature/PqcSphincsSignature.java)| [repl.it PqcSphincsSignature](https://repl.it/@javacrypto/PqcJavaSphincsSignature#Main.java/) |

For all of them you find an online running implementation - take them as a **case study**. Keep in mind that I could not check the correctness of the algorithms and results so **please do not rely on the programs**.

### Why don't you use the Open Quantum Safe (OQS) library?

If you are looking for a cross-platform library that is capable of all round 3 candidates then I'm recommending to visit the [Open Quantum Safe (OQS) project](https://openquantumsafe.org/) and the GitHub repository [https://github.com/open-quantum-safe](https://github.com/open-quantum-safe). The libraries could be perfect for my Cross platform cryptography project but unfortunately they do not provide any libraries in compiled form. My limited resources did not allow me to investigate in the compiling and binding technology so I have to leave out this great opportunity. 

**Update:** In the meantime I could get the Open Quantum Safe library with Java binding running so I'm been able to provide some (rough) information - kindly visit the page [Post Quantum Cryptography (PQC) with library Liboqs overview](pqc_liboqs_overview.md).

Last update: Mar. 19th 2021

Back to the main page: [readme.md](../readme.md)
