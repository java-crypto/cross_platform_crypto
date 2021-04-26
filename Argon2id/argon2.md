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

#### What parameters are the input of an Argon 2 algorithm?

As there are seven parameters kindly see my separate article about the [Argon 2 parameter](argon2_parameter.md).

#### What should you avoid?

Don't rely on default parameters when working with Argon 2-functions on different platforms. They can change when framework updates are made and different frameworks may have different default  parameter.

#### Serious security note

To get a comparable output I'm using a function called "generateFixedSalt16Byte" that is providing an output of 16 "x00" values for the salt. **Never ever** use this in production but the correct "generateSalt16Byte" function.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solution in code and within an online compiler that runs the code. Please note that due to restrictions (e.g. maximum running time for a script) the run get aborted - **there is no error in code**. I could have left out the critical functions but the code in my GitHub repository should be the same as "online".

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../Argon2id/Argon2id.java) | :white_check_mark: | [repl.it CpcJavaArgon2id](https://repl.it/@javacrypto/CpcJavaArgon2id#Main.java/)
| [PHP](../Argon2id/Argon2id.php) | :white_check_mark: | [repl.it CpcPhpArgon2id](https://repl.it/@javacrypto/CpcPhpArgon2id#main.php/)
| [C#](../Argon2id/Argon2id.cs) | :white_check_mark: | [repl.it CpcCsharpArgon2id](https://repl.it/@javacrypto/CpcCsharpArgon2id#main.cs/)
| NodeJs CryptoJs | :x: | use below version with NodeJs built-in Crypto module
| [NodeJS Crypto](../Argon2id/Argon2idNodeJsCrypto.js) | :white_check_mark: | [repl.it CpcNodeJsCryptoArgon2id](https://repl.it/@javacrypto/CpcNodeJsCryptoArgon2id#index.js/)
| NodeJS forge | :x: | use version with NodeJs built-in Crypto module
| [Browser](../Argon2id/argon2id.html) | :white_check_mark: | [your browser](http://javacrypto.bplaced.net/cpcjs/argon2id/argon2id.html)

This is an output generated with "generateFixedSalt16Byte" for comparison reasons - don't do this in production!:

```plaintext
Generate a 32 byte long encryption key with Argon2id
password: secret password
salt (Base64): AAAAAAAAAAAAAAAAAAAAAA==
encryptionKeyArgon2id (Base64) minimal:     e9G7+HHmftUaCEP2O1NwCSJkfyAT0QBzod3Szm1elf0=
encryptionKeyArgon2id (Base64) interactive: FZcsUwo7wf7V24qWTwKeSN9//+Pxy2gCKN35KZX2hXs=
encryptionKeyArgon2id (Base64) moderate:    gdizE6kia1W/CgTA3bRKKjtaf8cgZL1BIe6jeDegg0c=
encryptionKeyArgon2id (Base64) sensitive:   19Uym9wI6e/l5f0NocZmNEaouoHvsSyVfrp9iRYl/C8=
```

Below is the output of the browser version that looks like different to the other ones. The output provides the "full argon 2id hash" that incorporates some more information as just the hash. It gives an information about the used algorithm (argon2id), version number (in decimals: 19), the memory limit (66536), the ops limit (2), the parallelism (1) and hash in use (in Base64 encoding: AAAAAAAAAAAAAAAAAAAAAA). Note that after the hash the next value is separated with a "$" character and that is the "encryptionKeyArgon2id" (FZcsUwo7wf7V24qWTwKeSN9//+Pxy2gCKN35KZX2hXs) with a skipped "=" at the end. The value "hash" is the encryption key in hex string encoding (15972c530a3bc1fed5db8a964f029e48df7fffe3f1cb680228ddf92995f6857b). 

```plaintext
generateHash interactive
...
$argon2id$v=19$m=66536,t=2,p=1$AAAAAAAAAAAAAAAAAAAAAA$FZcsUwo7wf7V24qWTwKeSN9//+Pxy2gCKN35KZX2hXs
Hash (hex encoding): 15972c530a3bc1fed5db8a964f029e48df7fffe3f1cb680228ddf92995f6857b
```

Last update: Jan. 22nd 2021

Back to the main page: [readme.md](../readme.md)