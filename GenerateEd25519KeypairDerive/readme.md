# Cross-platform cryptography

## Elliptic Curve ED25519 key generation and key derivation

The programs below will generate an (Edwards) ED25519 curve key pair that can be used with [Libsodium's signature function](libsodium_signature_detached_string.md).

The **private key** (in the documentation named as **secret key**) is nothing else then a 64 bytes long byte array that should get generated randomly. The **public key** is a 32 bytes long byte array that needs to get derived from the private key.

My solution is available on all supported frameworks (Java, PHP, C#, NodeJs and within a browser environment).

There are two steps within the program - first you generate a fresh ED25519 key pair and later you derive a public key from the private key. 

#### Why do I need to derive the public key later ?

Usually you <u>pre-generate</u> the key pair and use it later. But what happens when you loose your public key and just have the private key stored in a secure place (see below). For this case it is usefull to derive the public key from the private key.

#### Is it possible to derive the private key from the public key ?

The short answer is: **NO**. When you ever should use your private key you need to regenerate a new key pair and publish your public key again so 3rd parties are still been able to verify your [new] signature.

Here are the results (your will differ due to random key creation):
```plaintext
Generate ED25519 private and public and derive public key from private key
privateKey (Base64): sNwj2zo5cSqurF8zjbFOm43/HlGMLuaEfnSMztnctB9a8g9uxHo+n7lDan8EQjAZ/VfWYICtGgHNrYvpBb6GrQ==
publicKey (Base64):  WvIPbsR6Pp+5Q2p/BEIwGf1X1mCArRoBza2L6QW+hq0=

derive the publicKey from the privateKey
publicKey (Base64):  WvIPbsR6Pp+5Q2p/BEIwGf1X1mCArRoBza2L6QW+hq0=
```

A last note on ED25519 key pair generation: Especially the private key contains very sensitive data so it need to get protected. Getting the private key from a 3rd party you can sign in the name of him. So what are the typical protection schemes:

### Protection Schemes

a) do not store the key on the device but e.g. an USB-stick. This isn't usable on server systems that need to run without human input

b) store the key in a key store. They are widely available and especially when using a webserver you come into direct contact with the stores. The store itself is protected with a password and the access to the key itself can get protected with an additional, different password. 

c) store the key in a password protected zip- or rar- archive. This isn't less usefull as well in automated environments.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../GenerateEd25519KeypairDerive/GenerateEd25519KeysDerivation.java) | :white_check_mark: | [repl.it CpcJavaLibsodiumGenerateEd25519KeyDerivation](https://repl.it/@javacrypto/CpcJavaLibsodiumGenerateEd25519KeyDerivation#Main.java/)
| [PHP](../GenerateEd25519KeypairDerive/GenerateEd25519KeysDerivation.php) | :white_check_mark: | [repl.it CpcPhpLibsodiumGenerateEd25519KeyDerivation](https://repl.it/@javacrypto/CpcPhpLibsodiumGenerateEd25519KeyDerivation#main.php/)
| [C#](../GenerateEd25519KeypairDerive/GenerateEd25519KeysDerivation.cs) | :white_check_mark: | # note 1) # [dotnetfiddle.net  CpcCsharpLibsodiumGenerateEd25519KeyDerivation](https://dotnetfiddle.net/9s5JFt)
| [Javascript / NodeJs](../GenerateEd25519KeypairDerive/GenerateEd25519KeysDerivationNodeJs.js) | :white_check_mark: | [repl.it CpcNodeJsLibsodiumGenerateEd25519KeyDerivation](https://repl.it/@javacrypto/CpcNodeJsLibsodiumGenerateEd25519KeyDerivation#index.js)
| NodeJS CryptoJs | :x: | use above Javascript / NodeJs solution
| NodeJS Crypto | :x: | use above Javascript / NodeJs solution
| NodeJS forge | :x: | use above Javascript / NodeJs solution
| [Webcrypto / Browser](../GenerateEd25519KeypairDerive/generateed25519keysderivation.html) | :white_check_mark: | [your browser GenerateEd25519KeysDerivation.html](http://javacrypto.bplaced.net/cpcjs/generateed25519keyderivation/generateed25519keysderivation.html)
| [Python](../GenerateEd25519KeypairDerive/GenerateEd25519KeysDerivation.py) | :white_check_mark: | [repl.it CpcPythonLibsodiumGenerateEd25519KeyDerivation](https://repl.it/@javacrypto/CpcPythonLibsodiumGenerateEd25519KeyDerivation#Main.py/)

note 1) at the time of writing the online-compiler dotnetfiddle.net does not work properly and claims about less ressources - the code is running correctly but not online, sorry.

Go to [Libsodium detached signature of a string](libsodium_signature_detached_string.md).

Last update: Feb. 07th 2021

Back to the main page: [readme.md](../readme.md)
