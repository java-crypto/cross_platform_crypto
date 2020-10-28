Cross-platform cryptography
===============

Generate a secure random initialization vector (IV)
---------------

Some AES encryption modes need a random generated initialization vector (iv). The codes will generate an iv with size of 16 byte = 128 bit.

:warning: Security warning :warning:
---------------

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life checked by a qualified professional cryptographer.**

The following links provide the solution in code and within an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](GenerateInitvector/Main.java) | :white_check_mark: | [repl.it CpcJavaGenerateRandomInitvector#Main.java](https://repl.it/@javacrypto/CpcJavaGenerateRandom-Initvector#Main.java/)
| [PHP](generateaeskey.html) | :soon: | [repl.it](http://javacrypto.bplaced.net/)
| [C#](generateaeskey.html) | :soon: | [repl.it](http://javacrypto.bplaced.net/)
| [Javacrypto](generateaeskey.html) | :soon: | [repl.it](http://javacrypto.bplaced.net/)
| [Node.JS](generateaeskey.html) | :x: | [repl.it](http://javacrypto.bplaced.net/)

This is an output (as there is a random element your output will differ):

```plaintext
Generate a 16 byte long Initialization vector (IV)
generated iv length: 32 base64: V+zuFGNj42igh5EmYNKduOpj8pgxgbupFey8hBzO4To=
```


Last update: Oct. 28th 2020

Back to the main page: [readme.md](readme.md)