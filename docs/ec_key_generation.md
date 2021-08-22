# Cross-platform cryptography

## Elliptic Curve key generation

There are several ways to generate Elliptic Curve keys, even on each framework and operation system. Usually you will generate the keys within your main  framework and just convert them on demand to other formats.

Here I'm showing you a different tool that is very useful in all cryptographic question - it is the **OpenSSL** software. OpenSSL is available on a lot of platforms and sometimes "built-in" as in modern PHP-frameworks. As OpenSSL has a lot of functions it is often called "Swiss knife".

My example key pair was created with the curve **SECP256R1** that is known with these names as well (depending on the platform): **NIST P-256**, **PRIME256V1** or with the O.I.D. identifier **1.2.840.10045.3.1.7**. 

The **key generation** takes place in tthree steps - first we generate the EC <u>private key in SEC1 encoding</u>, then we convert the private key into a <u>PKCS#8 encoding</u> and then we generate the belonging <u>public key</u>:

### Step 1: generate the EC private key in SEC1 encoding

```plaintext
ecdsa private key generation:
openssl ecparam -name prime256v1 -genkey -noout -out ecprivatekey_secp256r1.pem
```

### Step 2: convert the private key into PKCS#8 encoding

```plaintext
ecdsa private key conversion:
openssl pkcs8 -topk8 -nocrypt -in ecprivatekey_secp256r1.pem -out ecprivatekey_secp256r1_pkcs8.pem
```

### Step 3: get the EC public key from the private key:

```plaintext
ecdsa public key derivation:
openssl ec -in ecprivatekey_secp256r1.pem -pubout -out ecpublickey_secp256r1.pem
```

If you like to see more details on the generated keys use OpenSSL again for information:

```plaintext
openssl pkey -in ecprivatekey_secp256r1.pem -text
openssl pkey -in ecpublickey_secp256r1.pem -pubin -text
```

Here are the results:
```plaintext
EC private key in SEC1 encoding:

-----BEGIN EC PRIVATE KEY-----
MHcCAQEEIHIj7f4Lpc8O/13tdmDrv7y1ICFGfu4BqOVZJlNAfoFFoAoGCCqGSM49
AwEHoUQDQgAEMZHnt1D1tddLNGlEHXEtEw5K/G5QHkgaLi+IV84oiV+THv/DqGxY
DX2F5JOkfyv36iYSf5lfIC7q9el4YLnlwA==
-----END EC PRIVATE KEY-----

dump:
-----BEGIN PRIVATE KEY-----
MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgciPt/gulzw7/Xe12
YOu/vLUgIUZ+7gGo5VkmU0B+gUWhRANCAAQxkee3UPW110s0aUQdcS0TDkr8blAe
SBouL4hXziiJX5Me/8OobFgNfYXkk6R/K/fqJhJ/mV8gLur16XhgueXA
-----END PRIVATE KEY-----
Private-Key: (256 bit)
priv:
    72:23:ed:fe:0b:a5:cf:0e:ff:5d:ed:76:60:eb:bf:
    bc:b5:20:21:46:7e:ee:01:a8:e5:59:26:53:40:7e:
    81:45
pub:
    04:31:91:e7:b7:50:f5:b5:d7:4b:34:69:44:1d:71:
    2d:13:0e:4a:fc:6e:50:1e:48:1a:2e:2f:88:57:ce:
    28:89:5f:93:1e:ff:c3:a8:6c:58:0d:7d:85:e4:93:
    a4:7f:2b:f7:ea:26:12:7f:99:5f:20:2e:ea:f5:e9:
    78:60:b9:e5:c0
ASN1 OID: prime256v1
NIST CURVE: P-256
```

```plaintext
EC private key in PKCS#8 encoding:

-----BEGIN PRIVATE KEY-----
MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgciPt/gulzw7/Xe12
YOu/vLUgIUZ+7gGo5VkmU0B+gUWhRANCAAQxkee3UPW110s0aUQdcS0TDkr8blAe
SBouL4hXziiJX5Me/8OobFgNfYXkk6R/K/fqJhJ/mV8gLur16XhgueXA
-----END PRIVATE KEY-----
Private-Key: (256 bit)
priv:
    72:23:ed:fe:0b:a5:cf:0e:ff:5d:ed:76:60:eb:bf:
    bc:b5:20:21:46:7e:ee:01:a8:e5:59:26:53:40:7e:
    81:45
pub:
    04:31:91:e7:b7:50:f5:b5:d7:4b:34:69:44:1d:71:
    2d:13:0e:4a:fc:6e:50:1e:48:1a:2e:2f:88:57:ce:
    28:89:5f:93:1e:ff:c3:a8:6c:58:0d:7d:85:e4:93:
    a4:7f:2b:f7:ea:26:12:7f:99:5f:20:2e:ea:f5:e9:
    78:60:b9:e5:c0
ASN1 OID: prime256v1
NIST CURVE: P-256
```

```plaintext
EC public key:

-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEMZHnt1D1tddLNGlEHXEtEw5K/G5Q
HkgaLi+IV84oiV+THv/DqGxYDX2F5JOkfyv36iYSf5lfIC7q9el4YLnlwA==
-----END PUBLIC KEY-----
Public-Key: (256 bit)
pub:
    04:31:91:e7:b7:50:f5:b5:d7:4b:34:69:44:1d:71:
    2d:13:0e:4a:fc:6e:50:1e:48:1a:2e:2f:88:57:ce:
    28:89:5f:93:1e:ff:c3:a8:6c:58:0d:7d:85:e4:93:
    a4:7f:2b:f7:ea:26:12:7f:99:5f:20:2e:ea:f5:e9:
    78:60:b9:e5:c0
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

Last update: Aug. 22nd 2021

Back to the main page: [readme.md](../readme.md)