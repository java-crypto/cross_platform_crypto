# Cross-platform cryptography

## AES CBC mode tampering

I'm for sure you read this message a lot of times: "encryption with AES in mode CBC is secure". 

The good news are - this statement is **correct**.

The bad news are - never believe anybody who claims that the encryption is "secure" as you will see that AES encryption in mode CBC without further enhancements is **unsecure**.

Ups... whom should you believe now as both statements are written in one article?

The answer is easy: follow my article and see why both statements are correct and why you should consider an additional security level in your program.

#### Why is it neccessary to "secure" encrypted data?  

The answer is very easy: to get it secure. Encrypting data with AES in mode CBC is so secure that nobody can decrypt them without having access to the encryption key or putting in the right password for a PBKDF2-derived key.

Unfortunately there are attacker outside (the so called "man in the middle") that might been able that have access to the encrypted data (think of a chat or email). If the are been able to <u>modify</u> the ciphertext then they can manipulate the encrypted data that you will receive and you will decrypt data with different result compared to the original plaintext.

This kind of manipulating is called **tampering** and the bad news are - **it works without the knowledge or even breaking of the encryption key**.

#### Can you show me an example how it works?

The only way to prevent from this manipulation is to append some data that work like a "signature". If you receive such secured encrypted data you first have to check for the signature and **only if the "signature" is correct you decrypt it**. In my article [AES CBC mode 256 PBKDF2 HMAC string encryption](aes_cbc_tampering.md) I present a solution for this security issue.

#### What is name for this process?

We are working with a specialized hash function called **HMAC** and with some lines of code the complete encryption gets more secure.

As all other parameters stay the same as in "AES CBC mode 256 PBKDF2 string encryption" I don't name them again.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

As the program is for a demonstration of tampering only I wrote "just" a Java version - test it here online. Kindly note that the tampering method itself has no access to the encryption key:

<iframe src="https://paiza.io/projects/e/M9Qy0oQqYPFy12_yUx2T_Q?theme=eclipse" width="100%" height="500" scrolling="no" seamless="seamless"></iframe>


This is an output (as there are random elements your output will differ):

```plaintext
AES CBC 256 String encryption with PBKDF2 derived key and HMAC check
plaintext:  The quick brown fox jumps over the lazy dog

* * * Encryption * * *
ciphertextHmac (Base64): OPfp9A8dbuX60RQLWWbxS1DAlI1XmCNS7J672cqsoxQ=:TYaG8t14axeBAIT0rmOn7Q==:koRDiCLrdjQ3Bz20N0GZzYQVd0xcNLAP8p0Sc9UGp39NQWtJA35p0aEPWpRaaoBC:MskS4RpRkHa8PvcM1zGeN5BDFOmAvzTZFQ9QJxaWE70=
output is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac

* * * Decryption * * *
ciphertextHmacDecryption (Base64): OPfp9A8dbuX60RQLWWbxS1DAlI1XmCNS7J672cqsoxQ=:TYaG8t14axeBAIT0rmOn7Q==:koRDiCLrdjQ3Bz20N0GZzYQVd0xcNLAP8p0Sc9UGp39NQWtJA35p0aEPWpRaaoBC:MskS4RpRkHa8PvcM1zGeN5BDFOmAvzTZFQ9QJxaWE70=
input is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac
plaintext:  The quick brown fox jumps over the lazy dog

```

Last update: Nov. 17th 2020

Back to the main page: [readme.md](readme.md)