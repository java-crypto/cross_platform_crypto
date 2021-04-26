# Cross-platform cryptography

## Password-Based Key Derivation Function 2 (PBKDF2)

Many cryptographic algorithms need a (optimal random created) key, for AES 256 the key has to be 32 bytes long. When this key should come from an user input many programmers simply use a hash function like "SHA-256" to get a good looking binary key from the user input ("password").

#### What is wrong with this doing?

The key will get (in cryptographicle environments) weak as - in the end - you don't use a key of 32 bytes with a value of 0 to 255. Instead the only input would be the letters "a to z", "A to Z", "0 to 9" and some special characters e.g. "! to }" - counting them you will get about 80 different characters instead of 255 different ones when using a random key.

Talking in mathematic numbers the "key-space" is reduced from "256 to the power of 32" to "80 to the power of 32". Bring this to real numbers it's "1.1579209e+77" compared to "7.9228163e+60". That means you lower the (so called) **entrophy** to about "1.4615016e+16" what is much lower than the random one.

**So in short: do not hash your password for using it as a key!**

#### What is a better way to get a key from a password?

We are using a key derivation that uses a "salt" and run the function many times (in my example 15000 iterations) with a know hash function to get the key.

#### What are the advantages of this?

First: using a (random created) hash will produce a different key although the same password is used as input.

Second: a high number of iteration will slow down the key generation. Why is that good? Have a look to the typical scenario - the user puts in a password and press a "submit" button, waits a second and he is logged in. The other scenario is the hacker that tries to check a lot of passwords if they will work. Using a modern machine the simple hashing will take milliseconds so he is been able to check thousands of passwords in a second - with the slowed down version there is only one check per second (I think it's good).

Third: the clever algorithm will shuffle the low entrophy "password" to a high entrophy key.

#### What parameters are the input of a PBKDF2 algorithm?

My function does have three parameters as input:

1. **password**: this is the input for the user password that is often called "passphrase"
2. **iterations**: the number of iterations the function will run internally. At date of writing a minimum number of 10.000 iterations should be used, I'm using 15000 iterations. Specially on browser based Javascript environments this can slow down the user experience dramatically so you should test is to find a good number. The higher the number the better is key.
3. **salt**: to get different keys even when entering the same passphrase you need to provide a randly created salt.
4. **hash algorithm**: this is not used a parameter of the the function but in the function's name - I'm providing functions for **SHA-1**, **SHA-256** and **SHA-512**.

#### What should you avoid?

Don't rely on default parameters when working with PBKDF2-functions on different platforms. They can change when framework updates are made and different frameworks may have different parameters.

#### Serious security note

To get a comparable output I'm using a function called "generateFixedSalt32Byte" that is providing an output of 32 "x00" values for the salt. **Never ever** use this in production but the correct "generateSalt32Byte" function.


## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solution in code and within an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../Pbkdf2/Pbkdf2.java) | :white_check_mark: | [repl.it CpcJavaPbkdf2](https://repl.it/@javacrypto/CpcJavaPbkdf2#Main.java/)
| [PHP](../Pbkdf2/Pbkdf2.php) | :white_check_mark: | [repl.it CpcPhpPbkdf2](https://repl.it/@javacrypto/CpcPhpPbkdf2#main.php/)
| [C#](../Pbkdf2/Pbkdf2.cs) | :white_check_mark: | [repl.it CpcCsharpPbkdf2](https://repl.it/@javacrypto/CpcCsharpPbkdf2#main.cs/)
| [Javascript CryptoJs](../Pbkdf2/Pbkdf2CryptoJs.js) | :white_check_mark: | [repl.it CpcCryptoJsPbkdf2](https://repl.it/@javacrypto/CpcCryptoJsPbkdf2#index.js/)
| [NodeJS CryptoJs](../Pbkdf2/Pbkdf2NodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoPbkdf2](https://repl.it/@javacrypto/CpcNodeJsCryptoPbkdf2#index.js/)
| [NodeJS forge](../Pbkdf2/Pbkdf2NodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsPbkdf2](https://repl.it/@javacrypto/CpcNodeJsPbkdf2#index.js/)
| [Python](../AesCbc256Pbkdf2StringEncryption/AesCbc256Pbkdf2StringEncryption_Full.py) *1) | :white_check_mark: | [repl.it see AesCbc256Pbkdf2StringEncryption](https://repl.it/@javacrypto/CpcPythonAesCbc256Pbkdf2StringEncryptionFull#main.py/)

*1) There is not separate example in Python, see the full working code in AES CBC 256 PBKDF2  String Encryption 

This is an output generated with "generateFixedSalt32Byte" for comparison reasons - don't do this in production!:

```plaintext
aesKeySha512 length:  32  data:  d6e87030bfb04f170b43c110ec2797bf5366fdc1b99189f0185f47168fc7e560
aesKeySha256 length:  32  data:  e1ea3e4b0376c0f9bf93b94fe71719a099317297b79108aacd88c8a355d7a3d4
aesKeySha1   length:  32  data:  d6bc1ae2aea28f5098826b555d7a0fe073e5bc7d8136d232e01d422ba2dd761e
```

Last update: Feb. 08th 2021

Back to the main page: [readme.md](../readme.md)
