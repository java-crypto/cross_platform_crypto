# Cross-platform cryptography

## HMAC 256 calculation

With this little helper function we will calculate a **HMAC** or "keyed-hash message authentication code" or "hash-based message authentication code".

Here is a very short explanation, for a more detailed answer I'm referring to the [Wikipedia article](https://en.wikipedia.org/wiki/HMAC).

#### What is a HMAC good for?

Sending encrypted data with AES in modes CBC is safe because without knowledge of the encryption key an attacker will not been able to decrypt the message and reveal the original data.

Unfortunately there are attacker outside (the so called "man in the middle") that might been able that have access to the encrypted data. If they are been able to <u>modify</u> the ciphertext then they can manipulate the encrypted data that you will receive and decrypt data with different result than to the original plaintext (but the decryption will not fail because of changed data).

This kind of manipulating is called **tampering** and the bad news are - it works without the knowledge or even breaking of the encryption key.

The only way to prevent from this manipulating is to append some data that work like a "signature" or "checksum". If you receive such secured encrypted data you <u>first</u> check for the signature and **only if the "signature" is correct you decrypt it**.

#### What are the parameters for the HMAC function?

There are 3 parameters to get a HMAC:

1. you enter a **key** that can be a string or byte array
2. we need the original **data** to calculate the HMAC
3. as the calculation is done with hash functions we need to name the used **hash algorithm**. My programs will (fixed) work with the **SHA-256** algorithm.


### steps in the program

The program follows the usual sequence:
1. input a hmac key
2. input the plaintext
3. convert the plaintext to a binary format (e.g. a byte array in some languages)
3. calculate the hmac

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](HmacCalculation/Hmac256Calculation.java) | :white_check_mark: | [repl.it CpcJavaHmac256Calculation](https://repl.it/@javacrypto/CpcJavaHmac256Calculation#Main.java/)
| [PHP](HmacCalculation/Hmac256Calculation.php) | :white_check_mark: | [repl.it CpcPhpHmac256Calculation](https://repl.it/@javacrypto/CpcPhpHmac256Calculation#main.php/)
| [C#](HmacCalculation/Hmac256Calculation.cs) | :white_check_mark: | [repl.it CpcCsharpAesCbc256Pbkdf2HmacStringEncryption](https://repl.it/@javacrypto/CpcCsharpHmac256Calculation#main.cs/)
| [Javascript CryptoJs](HmacCalculation/Hmac256CalculationCryptoJs.js) | :white_check_mark: | [repl.it CpcCryptoJsHmac256Calculation](https://repl.it/@javacrypto/CpcCryptoJsHmac256Calculation#index.js/)
| [NodeJS Crypto](HmacCalculation/Hmac256CalculationNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoHmac256Calculation](https://repl.it/@javacrypto/CpcCpcNodeJsCryptoHmac256Calculation#index.js/)
| [NodeJS node-forge](HmacCalculation/Hmac256CalculationNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsHmac256Calculation](https://repl.it/@javacrypto/CpcCpcNodeJsHmac256Calculation#index.js/)

This is an output (as there are random elements your output will differ):

```plaintext
HMAC 256 calculation
hmac256Key:    hmac256ForAesEncryption
plaintext:  The quick brown fox jumps over the lazy dog
hmac256 length: 32 (Base64) data: avMiN7NEG6L32JBmfvL9UviofJmdgLdfRP6QwF+suM8=

```

Last update: Nov. 23rd 2020

Back to the main page: [readme.md](readme.md)