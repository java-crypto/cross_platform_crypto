# Cross-platform cryptography

## PQC Sike key exchange mechanism (KEM) using Liboqs library

The Sike algorithm is a **key exchange** algorithm that is used to generate a key that is later used for the encryption with a symmetric encryption algorithm like AES. The **Sike algorithm** is part of round 3 "Post-Quantum Cryptography competition", held by the NIST (National Institute of Standards and Technology, USA) to find an algorithm that could get a successor of RSA- or Elliptic curve encryption schemes.

This implementation is a sample program taken from the "official" tests for the Java binding of Liboqs, liboqs-java. I modified the program to get a similar output like my [own implementation of Sike](pqc_sike_kem.md).

### What are the steps in the program?

1. the program generates a Sike key pair for the sender and the receiver.
2. I'm writing the keys in binary form to a file but did not implement the other way round ("importing" the private and public key into the program)
3. on sender's side a 32 byte long encryption key is generated (this could be used for encryption with AES, not shown here) with the public key
4. an encapsulated key is generated that needs to get transported to the recipient along with the ciphertext
5. the recipient generates the decryption key with the encapsulated key and his private key - with this key you can decrypt the ciphertext (not shown here)

### What are the key lengths of the private and public key?

The private and public keys from Sike algorithm are of **equal** lengths than their RSA- and EC pendants - see the comparison table below.

### What is the length of the encapsulated key?

The encapsulated key is **much longer** than the resulting decryption key - see the table below.

### What is the speed of this algorithm?

I'm sorry, but as there is no "official" Java implementation I do not want to run any benchmark tests. 

### Can you give an overview about the key lengths and the ciphertext length?

The following table gives an overview about the key- and ciphertext lengths for RSA-, EC- and Sike encryption schemes (all data are for "PKCS#8 encoded data"): 

| Encryption scheme | key parameter | Private key length | Public key length | ciphertext length |
| ------ | :---: | :----: | :---: | :----: |
| RSA | 2.048 bit | 1218 | 294 | 256 |
| ECIES  | curve P-256 | 67 | 91 | 128 |
| Sike | P751 | 126 | 564 | 596 |

If you like to get a general overview about the sizes compared to other algorithms supported by the Liboqs visit my page [PQC Liboqs algorithms facts overview](pqc_overview_liboqs_algorithm_facts.md).

### Do I need an additional library?

Yes, you need the core library and the Java binding of OpenQuantumSafe's Libqs library:

1. the core library, get it here: [https://github.com/open-quantum-safe/liboqs](https://github.com/open-quantum-safe/liboqs) 

2. the Java binding, get it here: [https://github.com/open-quantum-safe/liboqs-java](https://github.com/open-quantum-safe/liboqs-java) 

Important note: you need to compile the core library (C framework) as there are no compiled libraries for different Operating Systems (like Windows, Mac or Linux) available!

For comparison purpose I provide a second Sike implementation on basis of the **Bouncy Castle** library - visit [PQC Sike key exchange mechanism (KEM)](pqc_sike_kem.md).

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

This is an output (your will differ because of a random element):

```plaintext
PQC key encapsulation mechanism (KEM) using
OpenQuantumSafe.org liboqs-java library
KEM algorithm: SIKE-p751

*** generate a key pair for client=receiver and server=sender ***
generated private key length: 644
generated public key length:  564
private and public key for server and client saved to file

* * * generate the keyToEncrypt with the public key of the recipient * * *
length of the received clientPublicKey: 564
encryptedKey length: 596 data: 32d6eeb5b03d4b3a590d5b9a79e2051cdf4cf1f46e23ff3ea867c5000f9e4f057a44cc11bb39e936adca9e511a7932154fb2b4bee17e102b8d64d1c3240cb05b08242792085780127fd343bcfa1a46267ce12adc96e464e3eba3edf1fc36b69dca2b32975908b9033dfd36701b856ab465dc299769364382f5c32bc909ee939fae79f48990f594ceced1323696b75fcf9ddcbe4023e8289c17aa70e89ec8504d5b545ab7bfac3fd5f498b82d55a5c6f180a1e91bcb6bcb8b02714268bef63c2df7d66249fc7e668a6ec76aaf7db3d983c19e8ce97c70b0b48253900e7441e38cd042e46f1c972958f3c334add14484c51f4fea5e6bd71054b21868055f662c88dd11ca637eb5d12638e78b5659aaad848c06e63a922305b49f6c2f259b511f2989e1a2f6b5f8d9c84202dc483259d71422ed6e928e77bdefcab943018c0513105cf5ae577b806bbeb20f0f4dd63c47a5921cb6d35433a4a46234883c7d228b5fd256c74b34add97dc931910a83520933eaf5e571d8c06e3afa7b3a49b6094a32f31214d59afc8d81200d6027ee3817a1cc58fd795c578e631b83ac26f1a49b96a0d8ee7ed3fdb16e6c91901bf370ca365c8c615773ec25a73d9ef34e0a125694c32a5df2d91cb521b0571cda1ea25163697349fcae36b93663f0b5cefde41d834524e6ce50cb27f530d2dccfa9e5c7927ac58d86639cc33ea6e7b3fb1fa469d50690a939c582a9542515f3c97a7551b536dbafcf7d0069d60b1dded5b40d2db076cf9412b3003bcbd8d17e2d63f78efa017e4e5df4c928569606758a59354c2e3c4673bb857322d7811fe57462dbbb99b10ae365
sharedSecretServer length: 32 data: ba7d2dbbed92fa6d8e4d1aea377c3ec89f5ba724ad98ee98480724a7497bffc1

* * * decapsulate the keyToEncrypt with the private key of the recipient * * *
sharedSecretClient length: 32 data: ba7d2dbbed92fa6d8e4d1aea377c3ec89f5ba724ad98ee98480724a7497bffc1

information from Liboqs about used algorithm:
KEM Details:
  Name: SIKE-p751
  Version: https://github.com/microsoft/PQCrypto-SIDH/tree/v3.4
  Claimed NIST level: 5
  Is IND-CCA: true
  Length public key (bytes): 564
  Length secret key (bytes): 644
  Length ciphertext (bytes): 596
  Length shared secret (bytes): 32

```

Last update: Mar. 18th 2021

Back to the [PQC with library Liboqs overview](pqc_liqoqs_overview.md), to the [PQC overview page](pqc_overview.md) or the main page: [readme.md](../readme.md)
