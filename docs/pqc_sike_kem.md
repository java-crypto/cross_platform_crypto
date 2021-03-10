# Cross-platform cryptography

## PQC Sike key exchange mechanism (KEM)

The Sike algorithm is a **key exchange** algorithm that is used to generate a key that is later used for the encryption with a symmetric encryption algorithm like AES. The **Sike algorithm** is part of round 3 "Post-Quantum Cryptography competition", held by the NIST (National Institute of Standards and Technology, USA) to find an algorithm that could get a successor of RSA- or Elliptic curve encryption schemes.

**Before going on it is important that you understand that this implementation is running but not tested against "official" values (e.g. test vectors). For that reason the program will print out a "serious warning" - kindly take this program as case study to see how the algorithm is working.**

### What are the steps in the program?

1. the program generates a Sike key pair
2. to check that the keys could get stored in text (Base64 encoded) form I'm converting the keys to a Base64 encoded string and vice versa (you need this for publishing or sending the public key to the person who has to encrypt data for you)
3. on sender's side a 32 byte long encryption key is generated (this could be used for encryption with AES, not shown here with the public key
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

### Do I need an additional library?

Yes, you need 2 libraries:

1. the Bouncy Castle library (tested with version 1.68), get it here (Maven): [https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on](https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on)

2. the Sike library, get it here (GitHub):  [https://github.com/wultra/sike-java](https://github.com/wultra/sike-java).

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

View the source code: [PqcJavaSikeKem.java](../PostQuantumCryptography/SikeKem/PqcJavaSikeKem.java)

Run the program online: [repl.it PqcJavaSikeKemKem](https://repl.it/@javacrypto/PqcJavaSikeKem#Main.java/)

This is an output (your will differ because of a random element):

```plaintext
PQC Sike key encapsulation mechanism (KEM)

************************************
* # # SERIOUS SECURITY WARNING # # *
* This program is a CONCEPT STUDY  *
* for the algorithm                *
* Sike [public key encryption]     *
* The program is using an OUTDATED *
* parameter set and I cannot       *
* check for the correctness of the *
* output and other details         *
*                                  *
*    DO NOT USE THE PROGRAM IN     *
*    ANY PRODUCTION ENVIRONMENT    *
************************************

generated private key length: 126
generated public key length : 564

* * * generate the keyToEncrypt with the public key of the recipient * * *
sharedSecretSender length:    32 data: 3e5b719dba9a8f7c9f1f1dc2a5942280e86e6925fbe0e68173bfc38e6dcd4a77
encryptedMessageFromSender length: 596 data (Base64): FqmEckNgcLj2lAc7idJskcpQ4mIxaogCRj636letVust7cMaJmtQULciOFq27jyntiprccv7OLuMJJlSuMpYrcIUeUGon7A+9/I+vK+meJyiTQnoY7y6EJ4SJwoDCTEvBjYsfGFx+T98KQ61uGtMXCtCSVt/sprPkGLFEneyD0UINu4tZGgy3aN8OTYD4E3UmFG6v/1Q0acwSFTqJWigCy4FEmF2Sd//rvFPay4B2KOS3D3App2XtoNgvQfpuSpQtccFxXp8t3hIkQKR6QmeQcc5OtdFeuvtBftzb6yfAjPWIaxQiO9Wf1B1P352jjyxsmKDF/DD4cXMbu9U3olLKn77H+Y/3Wdbtt/czX47+vA3gnC/bO1Slz4I02AfEO1VEnCSsCcBGKYp5K53ckJo7LnHIXU0dE/ZQazIxOLsa+MkccCMicFH+FYUFNu1gCwzOHxpmT5Y5+JKzCoo6mKao92GVXltKWqufD33o6RPhJjovnEzxuxdYjsT1na8JJp6+M2t3AAijOHpjoTcIl8nnS9LBSJ1qCz8XXDh1TMEEkDOpMFgA5J2w0uehO97q3MSlg0bXfJR5ae2xJnTV31mvG7lp6AFV67xNb/T1Wf0/dt3vv0KCiZPJ/sboxiw/K+2XV7TvYBjEc+RJNVp3jTi6cTC/QAdedeI/JL7iwldy6GPo+aVemudSkt460dYMIV+bChAsVCJgWLwVONcF7Jsyd4wXiO6oO9WBh6jIcRAqYDiZZxtlmwZPdR2I0L0v3DKhOx5iwTbWMeGzdNgZ5qBhkzKq90=

* * * decapsulate the keyToEncrypt with the private key of the recipient * * *
sharedSecretRecipient length: 32 data: 3e5b719dba9a8f7c9f1f1dc2a5942280e86e6925fbe0e68173bfc38e6dcd4a77

```

Last update: Mar. 10th 2021

Back to the [PQC overview page](pqc_overview.md) or the main page: [readme.md](../readme.md)