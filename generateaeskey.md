Cross-platform cryptography
===============

Generate a secure random AES key
---------------

One of the most important fact for secure cryptography is to use an encryption key that is generated randomly and **not** by taking a passwordphrase and simply convert it to a key.

The codes will generate an AES key with size of 32 byte = 256 bit that is the maximum key size available for AES encryption.

:warning: Security warning :warning:
---------------

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life without they are checked by a qualified professional cryptographer.**

The following links provide the solution in code and within an online compile that runs the code.

| Solution | Link | Java | PHP | C# | JS | Node.JS
| ------ | ------ | :----: | :---: | :--: | :--: | :--: |
| generate a 32 byte long key | [Website](generatekey.html) | :soon: | :soon: | :soon: | :soon: | :soon: |

| Language | available | Online-compiler
| ------ | :---: | :----: | 
| [Java](GenerateAesKey/Main.java) | :white_check_mark: | [repl.it CpcJavaGenerateRandomAesKey#Main.java](https://repl.it/@javacrypto/CpcJavaGenerateRandomAesKey#Main.java/) |
| [PHP](generateaeskey.html) | :soon: | [repl.it](http://javacrypto.bplaced.net/) |
| [C#](generateaeskey.html) | :soon: | [repl.it](http://javacrypto.bplaced.net/) |
| [Javacrypto](generateaeskey.html) | :soon: | [repl.it](http://javacrypto.bplaced.net/) |
| [Node.JS](generateaeskey.html) | :x: | [repl.it](http://javacrypto.bplaced.net/) |

