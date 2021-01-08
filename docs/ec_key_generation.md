# Cross-platform cryptography

## Elliptic Curve key generation

There are several ways to generate Elliptic Curve keys, even on each framework and operation system. Usually you will generate the keys within your main  framework and just convert them on demand to other formats.

Here I'm showing you a different tool that is very useful in all cryptographic question - it is the **OpenSSL** software. OpenSSL is available on a lot of platforms and sometimes "built-in" as in modern PHP-frameworks. As OpenSSL has a lot of functions it is often called "Swiss knife".

My example key pair was created with the curve **SECP256R1** that is known with these names as well (depending on the platform): **NIST P-256**, **PRIME256V1** or with the O.I.D. identifier **1.2.840.10045.3.1.7**. 

The **key generation** takes place in two steps - first we generate the EC <u>private key</u> and then we generate the belonging <u>public key</u>:

```plaintext
ecdsa private key generation:
openssl ecparam -name prime256v1 -genkey -noout -out ecprivatekey_secp256r1.pem
ecdsa public key generation:
openssl ec -in ecprivatekey_secp256r1.pem -pubout -out ecpublickey_secp256r1.pem
```
If you like to see more details on the generated keys use OpenSSL again for information:

```plaintext
openssl pkey -in ecprivatekey_secp256r1.pem -text
openssl pkey -in ecpublickey_secp256r1.pem -pubin -text
```

Here are the results:
```plaintext
-----BEGIN PRIVATE KEY-----
MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCAU2f8tzo99Z1HoxJlY
96yXUhFY5vppVjw1iPKRfk1wHA==
-----END PRIVATE KEY-----
Private-Key: (256 bit)
priv:
    14:d9:ff:2d:ce:8f:7d:67:51:e8:c4:99:58:f7:ac:
    97:52:11:58:e6:fa:69:56:3c:35:88:f2:91:7e:4d:
    70:1c
pub:
    04:cd:be:f2:00:55:ae:a7:a8:83:a8:98:84:ab:be:
    b8:ac:0b:a6:b1:5d:8c:ae:ca:59:c5:a3:f7:58:6a:
    70:1d:a0:b8:51:f7:f7:37:85:1b:25:91:7b:65:a7:
    35:73:a5:bb:28:99:74:79:e0:8f:d3:6d:39:43:75:
    04:a7:0c:67:75
ASN1 OID: prime256v1
NIST CURVE: P-256
```

```plaintext
-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEzb7yAFWup6iDqJiEq764rAumsV2M
rspZxaP3WGpwHaC4Uff3N4UbJZF7Zac1c6W7KJl0eeCP0205Q3UEpwxndQ==
-----END PUBLIC KEY-----
Public-Key: (256 bit)
pub:
    04:cd:be:f2:00:55:ae:a7:a8:83:a8:98:84:ab:be:
    b8:ac:0b:a6:b1:5d:8c:ae:ca:59:c5:a3:f7:58:6a:
    70:1d:a0:b8:51:f7:f7:37:85:1b:25:91:7b:65:a7:
    35:73:a5:bb:28:99:74:79:e0:8f:d3:6d:39:43:75:
    04:a7:0c:67:75
ASN1 OID: prime256v1
NIST CURVE: P-256
```

A last note on EC key pair generation: Especially the private key contains very sensitive data so it need to get protected. Getting the private key from a 3rd party you can sign in the name of him or decrypt data that is send to him. So what are the typical protection schemes:

## Protection Schemes

a) do not store the key on the device but e.g. an USB-stick. This isn't usable on server systems that need to run without human input

b) store the key in a key store. They are widely available and especially when using a webserver you come into direct contact with the stores. The store itself is protected with a password and the access to the key itself can get protected with an additional, different password. 

c) store the key in a password protected zip- or rar- archive. This isn't less usefull as well in automated environments.

d) use password protected ("encrypted") private keys. They are <u>my favorite</u> but using them exchangeable in cross-platform cryptography demand some more lines of code and sometimes external libraries.

**All of my examples use unprotected = unsecure private keys!**

Go to [ECDSA string signature](ecdsa_signature_string.md).

Last update: Jan. 08th 2021

Back to the main page: [readme.md](../readme.md)