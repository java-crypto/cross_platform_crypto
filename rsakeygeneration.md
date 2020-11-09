# Cross-platform cryptography

## RSA key generation

There are several way to generate RSA key, even on each framework and operation system. Usually you will generate the keys within your main  framework and just convert them on demand to other formats.

Here I'm showing you a different tool that is very usefull in all cryptographic question - it the **OpenSSL** software. OpenSSL is available on a lot of platforms and sometimes "built-in" as in modern PHP-frameworks. As OpenSSL has a lot of functions it is often called "Swiss knife".

The **key generation** takes place in two steps - first we generate the RSA <u>private key</u> and then we generate the belonging <u>public key</u>:

```plaintext
private key generation:
openssl genpkey -out privatekey2048.pem -algorithm RSA -pkeyopt rsa_keygen_bits:2048
public key generation:
openssl rsa -in privatekey2048.pem -outform PEM -pubout -out publickey2048.pem
```
If you like to see more details on the generated keys use OpenSSL again for information:

```plaintext
openssl pkey -in privatekey2048.pem -text
openssl pkey -in publickey2048.pem -pubin -text
```

A last note on RSA key pair generation: Especially the private key contains very sensitive data so it need to get protected. Getting the private key from a 3rd party you can sign in the name of him or decrypt data that is send to him. So what are the typical protection schemes:

## Protection Schemes

a) do not store the key on the device but e.g. an USB-stick. This isn't usable on server systems that need to run without human input
b) store the key in a key store. They are widely available and especially when using a webserver you come into direct contact with the stores. The store itself is protected with a password and the access to the key itself can get protected with an additional, different password. 
c) store the key in a password protected zip- or rar- archive. This isn't less usefull as well in automated environments.
d) use password protected ("encrypted") private keys. They are <u>my favorite</u> but using them exchangeable in cross-platform cryptography demand some more lines of code and sometimes external libraries.

**All of my examples use unprotected = unsecure private keys!**

Go to [RSA string signature](rsasignaturestring.md) and [RSA string signature verification only](rsasignaturestringverificationonly.md).

Last update: Nov. 9th 2020

Back to the main page: [readme.md](readme.md)