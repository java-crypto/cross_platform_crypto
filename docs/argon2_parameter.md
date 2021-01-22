# Cross-platform cryptography

## Argon 2 Password-Based Key Derivation Function

This article is about the parameter for the Argon 2 algorithm. It is important to understand what they do to get a key derivation that meets your security standards. [Here is the Argon 2 algorithm in different frameworks](argon2.md).

A lot of implementations just take the parameter in a specific range but give no recommendations about the "right" value. Fortunately I found some fixed and named parameter sets in the **PHP Libsodium** documentation.

Below you find a link to a PHP program that simply outputs the predefined constants, here is the result of the run, followed by an explanation of each parameter:

```plaintext
PHP Libsodium Argon2 parameter

SODIUM_CRYPTO_PWHASH_SALTBYTES:            16
SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE: 2
SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE: 67108864
SODIUM_CRYPTO_PWHASH_OPSLIMIT_MODERATE:    3
SODIUM_CRYPTO_PWHASH_MEMLIMIT_MODERATE:    268435456
SODIUM_CRYPTO_PWHASH_OPSLIMIT_SENSITIVE:   4
SODIUM_CRYPTO_PWHASH_MEMLIMIT_SENSITIVE:   1073741824
SODIUM_CRYPTO_PWHASH_ALG_ARGON2ID      :   2
SODIUM_CRYPTO_SECRETBOX_KEYBYTES:          32
```

#### Parameter 1: length of salt

One of the most important details of an cryptographic implementation is the **randomness** of the encrypted data (e.g. to avoid "rainbow table attacks"). As a lot of frameworks use a **salt** length of 16 bytes I use this length (SALTBYTES: 16) in all my Argon2 programs.

This is the text in the [documentation for the function sodium crypto pwhash](https://www.php.net/manual/de/function.sodium-crypto-pwhash.php): "*A salt to add to the password before hashing. The salt should be unpredictable, ideally generated from a good random mumber source such as random_bytes(), and have a length of at least SODIUM_CRYPTO_PWHASH_SALTBYTES bytes.*"

#### Parameter 2: opslimit or iterations or timecost

The **opslimit** parameter is often named **number of iterations** or **timecost** and is identical to the parameter in the [PBKDF2 algorithm](pbkdf2.md) - how often does the complete circle run. Running more rounds will take more time. There are three constants available **interactive**, **moderate** and **sensitive** that should give you an expression what they are good for - the sensitive one will take much longer than the interactive one. The value for **interactive is 2**, for **moderate is 3** and for **sensitive it is 4**.

This is the text in the [documentation for the function sodium crypto pwhash](https://www.php.net/manual/de/function.sodium-crypto-pwhash.php): "*Represents a maximum amount of computations to perform. Raising this number will make the function require more CPU cycles to compute a key. There are some constants available to set the operations limit to appropriate values depending on intended use, in order of strength: SODIUM_CRYPTO_PWHASH_OPSLIMIT_INTERACTIVE, SODIUM_CRYPTO_PWHASH_OPSLIMIT_MODERATE and SODIUM_CRYPTO_PWHASH_OPSLIMIT_SENSITIVE.*"

**Parameter 3: memory limit**

As a the arithmetic operations will run in the device's memory and giving more memory will expand the internal arrays but makes the algorithm slower (what one the intentions is to avoid brute force attacks).

Same to opslimit the **memlimit** comes with 3 constants, the sensitive one grabs a lot more memory than the interactive on.

Just a note on the numbers: The internal value of the **interactive** parameter is **67108864** or **66536** * 1024 for **moderate** it is **268435456** = **262144** * 1024 and using **sensitive** it will be **1073741824** = **1048576** * 1024. What looks like a silly calculation is important when using other Argon2 libraries - some of them get the number in 1024er byte blocks.

This is the text in the [documentation for the function sodium crypto pwhash](https://www.php.net/manual/de/function.sodium-crypto-pwhash.php): "*The maximum amount of RAM that the function will use, in bytes. There are constants to help you choose an appropriate value, in order of size: SODIUM_CRYPTO_PWHASH_MEMLIMIT_INTERACTIVE, SODIUM_CRYPTO_PWHASH_MEMLIMIT_MODERATE, and SODIUM_CRYPTO_PWHASH_MEMLIMIT_SENSITIVE. Typically these should be paired with the matching opslimit values.*"

**Parameter 4: length of derived key**

A we will use the output of the Argon2 key derivation function for encryption functions like **AES 256 CBC** or Libsodium's **XSalsa20 stream cipher** we have to define the output length - in my programs I will use an **output length of 32 bytes**. 

This is the text in the [documentation for the function sodium crypto pwhash](https://www.php.net/manual/de/function.sodium-crypto-pwhash.php): "*The length of the password hash to generate, in bytes.*"

#### Parameter 5: the Argon 2 implementation

There are 3 Argon2 implementations available: **Argon2d**, **Argon2i** and **Argon2id**. As the Argon2id combines the "i" and "d" imps I'm using it in my examples.

This is the text in the [documentation for the function sodium crypto pwhash](https://www.php.net/manual/de/function.sodium-crypto-pwhash.php): "*A number indicating the hash algorithm to use. By default SODIUM_CRYPTO_PWHASH_ALG_DEFAULT (the currently recommended algorithm, which can change from one version of libsodium to another), or explicitly using SODIUM_CRYPTO_PWHASH_ALG_ARGON2ID13, representing the Argon2id algorithm version 1.3. *"

#### Parameter 6: the version number of the algorithm

This parameter is not available in PHP Libsodium but it needs to get set in other imps. The **version** I'm using **is (in hex) x13 or (decimal) 19**.

#### Parameter 7: number of parallel threads running

The **parallelism parameter** is not set in PHP but in other implementations. To get a cross platform wide solution I stay with the number **1**.

#### What should you avoid when using Argon 2 libraries?

Don't rely on default parameters when working with Argon 2- functions on different platforms. The can change when framework updates are made and different frameworks may have different parameters.

#### Serious security note

To get a comparable output I'm using a function called "generateFixedSalt16Byte" that is providing an output of 16 "x00" values for the salt. **Never ever** use this in production but the correct "generateSalt16Byte" function.

#### complete parameter set minimal ### probably UNSECURE ###

This parameter set is only in use for demonstration in my online resources. **Do not use this parameter set in production as it is probably UNSECURE!**

* Argon 2 algorithm: Argon2id
* Argon 2 version: x13 / 19
* salt length: 16 bytes
* opslimit/iterations: 2
* memlimit: 8388608 or 8192
* output length: 32 bytes
* parallelism/threads: 1

#### complete parameter set interactive

* Argon 2 algorithm: Argon2id
* Argon 2 version: x13 / 19
* salt length: 16 bytes
* opslimit/iterations: 2
* memlimit: 67108864 or 66536
* output length: 32 bytes
* parallelism/threads: 1

#### complete parameter set moderate

* Argon 2 algorithm: Argon2id
* Argon 2 version: x13 / 19
* salt length: 16 bytes
* opslimit/iterations: 3
* memlimit: 268435456 or 262144
* output length: 32 bytes
* parallelism/threads: 1

#### complete parameter set sensitive

* Argon 2 algorithm: Argon2id
* Argon 2 version: x13 / 19
* salt length: 16 bytes
* opslimit/iterations: 4
* memlimit: 1073741824 or 1048576
* output length: 32 bytes
* parallelism/threads: 1

#### How long does it take to calculate the key?

As a simple benchmark you could use a web page that offers an online calculation and benchmark, visit [https://antelle.net/argon2-browser](https://antelle.net/argon2-browser/). Using the parameter sets and entering the smaller numbers of the memlimit (e.g. 66536 as the value is in KiB) and the Argon2id algorithm you get these results: the interactive set will take 356 ms, the moderate set take 2126 ms and the sensitive set will get an example within 11521 ms (the duration times depend on the browser, machine's memory and processor speed).

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following link provide the solution in code and within an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [PHP parameter](../Argon2id/Argon2Parameter.php) | :white_check_mark: | [repl.it CpcPhpArgon2Parameter](https://repl.it/@javacrypto/CpcPhpArgon2Parameter#main.php/)

Last update: Jan. 22nd 2021

Back to the main page: [readme.md](../readme.md)