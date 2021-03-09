# Cross-platform cryptography

## PQC Chrystals-Kyber key exchange mechanism (KEM)

The Chrystals-Kyber algorithm is a **key exchange** algorithm that is used to generate a key that is later used for the encryption with a symmetric encryption algorithm like AES. The **Chrystals-Kyber algorithm** is part of round 3 "Post-Quantum Cryptography competition", held by the NIST (National Institute of Standards and Technology, USA) to find an algorithm that could get a successor of RSA- or Elliptic curve encryption schemes.

**Before going on it is important that you understand that this implementation is running but not tested against "official" values (e.g. test vectors). For that reason the program will print out a "serious warning" - kindly take this program as case study to see how the algorithm is working.**

### What are the steps in the program?

1. the program generates a Chrystals-Kyber key pair
2. to check that the keys could get stored in text (Base64 encoded) form I'm converting the keys to a Base64 encoded string and vice versa (you need this for publishing or sending the public key to the person who has to encrypt data for you)
3. on sender's side a 32 byte long encryption key is generated (this could be used for encryption with AES, not shown here with the public key
4. an encapsulated key is generated that needs to get transported to the recipient along with the ciphertext
5. the recipient generates the decryption key with the encapsulated key and his private key - with this key you can decrypt the ciphertext (not shown here)

### What are the key lengths of the private and public key?

The private and public keys from Chrystals-Kyber algorithm are **much longer** than their RSA- and EC pendants - see the comparison table below.

### What is the length of the encapsulated key?

The encapsulated key is **much longer** than the resulting decryption key - see the table below.

### What is the speed of this algorithm?

I'm sorry, but as there is no "official" Java implementation I do not want to run any benchmark tests. 

### Can you give an overview about the key lengths and the ciphertext length?

The following table gives an overview about the key- and ciphertext lengths for RSA-, EC- and Chrystals-Kyber encryption schemes (all data are for "PKCS#8 encoded data") when encrypting the plaintext "1234567890ABCDEF1122334455667788": 

| Encryption scheme | key parameter | Private key length | Public key length | ciphertext length |
| ------ | :---: | :----: | :---: | :----: |
| RSA | 2.048 bit | 1218 | 294 | 256 |
| ECIES  | curve P-256 | 67 | 91 | 128 |
| Chrystals-Kyber | not specified | 2400 | 1184 | 1088 |

### Do I need an additional library?

Yes, you need 2 libraries:

1. the crystals-kyber library (tested with version 2.0.2), get it here (npm): [https://www.npmjs.com/package/crystals-kyber](https://www.npmjs.com/package/crystals-kyber), GitHub: [https://github.com/antontutoveanu/crystals-kyber-javascript](https://github.com/antontutoveanu/crystals-kyber-javascript/)

2. the SHA-3 library (tested with version 2.0.2), get it here (npm): [https://www.npmjs.com/package/sha3](https://www.npmjs.com/package/sha3/), GitHub: [https://github.com/phusion/node-sha3](https://github.com/phusion/node-sha3).

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

View the source code: [PqcChrystalsKyberKemNodeJs.js](../PostQuantumCryptography/ChrystalsKyberKem/PqcChristalsKyberKemNodeJs.js)

Run the program online: [repl.it PqcNodeJsChrystalsKyberKem](https://repl.it/@javacrypto/PqcNodeJsChrystalsKyberKem#index.js/)

This is an output (your will differ because of a random element):

```plaintext
PQC Chrystals-Kyber key encapsulation mechanism (KEM)

************************************
* # # SERIOUS SECURITY WARNING # # *
* This program is a CONCEPT STUDY  *
* for the algorithm                *
* Chrystals-Kyber                  *
* [public key encryption]          *
* The program is using an OUTDATED *
* parameter set and I cannot       *
* check for the correctness of the *
* output and other details         *
*                                  *
*     DO NOT USE THE PROGRAM IN    *
*     ANY PRODUCTION ENVIRONENT    *
************************************

generated private key length: 2400
generated public key length : 1184

* * * generate the keyToEncrypt with the public key * * *
keyToEncrypt Base64: rpsVGbkwZhn/uw9JgO2inwwXAyr2VHacUpQhZWgmvBg=

encapsulatedKey length: 1088
encapsulatedKey Base64: tR8ZafOz0yIpSWzh5rjn59mn03rKZCxjBlJHWridCghTCuBr+yvL2yYu354GbFwYINEktVnjhJsGhpP3Hy/LuEDUzYiXBHC+QmV2Zci4yzTt7ClDoAZE78jNC8Glpv4n2dmAwVOmauzVZ4TyMCVjYjlJmvKjUEu1p1VpL4LERvRCAcl8xPajaBNZNHSDR799K0WEHDysOqcv4jqx6LPWiJUNH4uAttED0SoLqJui8nuvTn5WTpwfnbc5UXdcKKJ32YCXhm/REOCsMidEm8f4ehwbwZweIw6GctIMgAZgLVbOVEYzg99aqLHm6Uaz0fTawqsi28ZF5v3Xrkih/L8UIQltbj1gcF/rCS8YPcCEupufXiqDnH5eoHdgs4C4vS2OwTyy1Yk3ZLcQfhmfaZmzsVbaLrB6MLvprlLV09nx7wLKcHVXxJtUWJtvPo9V3g/yrK50PAbRG/aul+/G9zu0K2Xy/ISmgbR41ITgZZMIXL3X2q2ltyPPnYL5SrufpuEnOxI63pjFLNVTxNYrnOpPzjfaNWdZ5zjK3j8aorovYrhdddhWizbG5qUQ60V5/3oOPLMIGi8WGlfOUMlFwGDPKm0tsUppYwf6v8y8WdElYY0B83tjQLsU/fo7DOJzT7j//463TzU+SqI8G2hQXNmlWtoaDHVkyEI2e642PIb29GXk/nVx/MbQekc9RvuBJHAE055Lgj1FPmIC5MGolZwa97mD9LE/hgMUhKP+f/rZE5JQnd7adMzCFpfQt2eP88rJkvYcNCxHfXxgGlJxWECsn0qYGd4/9cQEskT5OEKV8eJhhHc0+cBiGEOH1s9/CMDmuzhgxw2bCOHzYzj+U0YAU+W0IoPQ3rq/USDpPCjt+ye8wSG7WqrxJ8Hkxgp5FU9oSUhXeRTl2Z+3BJxBiA701mwcokuqwc0vV3q00YOXytyeFlMBDwi67Yl40TN6gezwAarK29pVGe1PQsV1++vs4wLXuJuOUNbmur1o+HMiXIBUX8bXO5+21pv38vDcz8D3RW3GcE4b2e+GWKnWGk4aOm2LMRBncY7VJRD9A5VWcNsitw6/AiTc0SaOaRgwVLI43OMyBAKroWJBb8l+lGQHjuBzRBD1FqobpufN6oHJwgIpZxNLaIQMIRrAZcdCYsje+pUerwJmb3deKagWFg0r1QP4X1rPU1/go1+B86XlbNRZG1GvcOVksJMtaZW8B7n493Yj2S8ubmZwx5TYxKTFQsi2RQmUMD/e7+BsGmV7HkOJyzfjzEIcg+FRgW39Pawi2IPVoIPMorf8fXTxIYCJFqByHl95r91ghrifNkPzB08/GNKyqdTvKszQe8XsBGg8fK7bZrnTa78xW/Blag8J7qkHFs5Yhwrjr8movwCiKrteO53Zy3PIWQr4x47wycOgBmsucKMZe5IZUgXKsU4lBy9qw70Q52OZVcP12W65YiA=

* * * decapsulate the keyToEncrypt with the private key * * *
keyToDecrypt Base64: rpsVGbkwZhn/uw9JgO2inwwXAyr2VHacUpQhZWgmvBg=

```

Last update: Mar. 09th 2021

Back to the [PQC overview page](pqc_overview.md) or the main page: [readme.md](../readme.md)