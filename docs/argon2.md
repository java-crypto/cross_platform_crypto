# Cross-platform cryptography

## Argon 2 Password-Based Key Derivation Function

Many cryptographiclalgorithms need a (optimal random created) key, for AES 256 the key has to be 32 bytes long. When this key should come from an user input many programmers simply use a hash function like "SHA-256" to get a good looking binary key from the user input ("password") - **do not do this!**.

#### What is wrong with this doing?

The key will get (in cryptographicle environments) weak because - in the end - you don't use a key of 32 bytes with a value of 0 to 255. Instead the only input would be the letters "a to z", "A to Z", "0 to 9" and some special characters e.g. "! to }" - counting them you will get about 80 different characters instead of 255 different ones when using a randomly generated key.

Talking in mathematically numbers the "key-space" is reduced from "256 to the power of 32" to "80 to the power of 32". Bring this to real numbers it's "1.1579209e+77" compared to "7.9228163e+60". That means you lower the (so called) **entrophy** to about "1.4615016e+16" what is much lower than the random one.

**So in short: do not hash your password for using it as a key!**

#### What is a better way to get a key from a password?

We are using a key derivation that uses a "salt" and run the function many times to get the key.

#### What are the advantages of this?

First: using a (random created) hash will produce a different key although the same password is used as input.

Second: a high number of operations will slow down the key generation. Why is that good? Have a look to the typical scenario - the user puts in a password and press a "submit" button, waits a second and he is logged in. The other scenario is the hacker that tries to check a lot of passwords if they will work. Using a modern machine the simple hashing will take milliseconds so he is been able to check thousands of passwords in a second - with the slowed down version there is only one check per second (I think it's good).

Third: the clever algorithm will shuffle the low entrophy "password" to a high entrophy key.

#### I never heard about Argon 2 - what is it?

Argon 2 is the winner of a **Password Hashing Competition** (PHC) that was held from 2013 to 2015 to get a successor of the well known [**PBKDF2 algorithm**](pbkdf2.md). For a more detailed information kindly see the articles about the [Password Hashing Competition](https://www.password-hashing.net//) and [Argon 2](https://www.password-hashing.net/#argon2). Unfortunately the implementation of the algorithm is not widely available in standard libraries like NodeJs/Crypto or Java and you mostly need the help of 3rd party libraries to run my examples. The good news are: for all my frameworks I could find a solution that is working cross-platform wide.

#### What parameters are the input of an Argon2 algorithm?

As there are seven parameters kindly see my separate article about the [Argon 2 parameter](argon2_parameter.md).

#### What should you avoid?

Don't rely on default parameters when working with Argon 2-functions on different platforms. They can change when framework updates are made and different frameworks may have different parameters.

#### Serious security note

To get a comparable output I'm using a function called "generateFixedSalt16Byte" that is providing an output of 16 "x00" values for the salt. **Never ever** use this in production but the correct "generateSalt16Byte" function.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solution in code and within an online compile that runs the code. Please note that due to restrictions (e.g. maximum running time for a script) the run get aborted - there is no error in code. I could have left out the critical functions but the code in my GitHub repository should be the same "online".

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../Pbkdf2/Pbkdf2.java) | :white_check_mark: | [repl.it CpcJavaPbkdf2](https://repl.it/@javacrypto/CpcJavaPbkdf2#Main.java/)
| [PHP](../Pbkdf2/Pbkdf2.php) | :white_check_mark: | [repl.it CpcPhpPbkdf2](https://repl.it/@javacrypto/CpcPhpPbkdf2#main.php/)
| [C#](../Pbkdf2/Pbkdf2.cs) | :white_check_mark: | [repl.it CpcCsharpPbkdf2](https://repl.it/@javacrypto/CpcCsharpPbkdf2#main.cs/)
| [Javascript CryptoJs](../Pbkdf2/Pbkdf2CryptoJs.js) | :white_check_mark: | [repl.it CpcCryptoJsPbkdf2](https://repl.it/@javacrypto/CpcCryptoJsPbkdf2#index.js/)
| [NodeJS CryptoJs](../Pbkdf2/Pbkdf2NodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoPbkdf2](https://repl.it/@javacrypto/CpcNodeJsCryptoPbkdf2#index.js/)
| [NodeJS forge](../Pbkdf2/Pbkdf2NodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsPbkdf2](https://repl.it/@javacrypto/CpcNodeJsPbkdf2#index.js/)

This is an output generated with "generateFixedSalt32Byte" for comparison reasons - don't do this in production!:

```plaintext
aesKeySha512 length:  32  data:  d6e87030bfb04f170b43c110ec2797bf5366fdc1b99189f0185f47168fc7e560
aesKeySha256 length:  32  data:  e1ea3e4b0376c0f9bf93b94fe71719a099317297b79108aacd88c8a355d7a3d4
aesKeySha1   length:  32  data:  d6bc1ae2aea28f5098826b555d7a0fe073e5bc7d8136d232e01d422ba2dd761e
```

Last update: Jan. 22nd 2021

Back to the main page: [readme.md](../readme.md)