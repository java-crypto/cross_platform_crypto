# Cross-platform cryptography

## EC curve P-256 sample key pair

Private-/Public cryptography needs a key pair for encryption and signing. This key pair is usually generated and then stored in files or a key store before the keys are used for encryption or signing.

As I'm working with online-compiler I'm using use static, hard-coded keys instead of loading them from files. This is for demonstration purpose only - **do not do this in production environment!**

I generated an Elliptic Curve key pair with a length of 256 bit. An EC key is defined by the curve that is the basis for the calculations so you need to know what curve was used for signing or encryption. My example key pair was created with the curve **SECP256R1** that is known with these names as well (depending on the platform): **NIST P-256**, **PRIME256V1** or with the O.I.D. identifier **1.2.840.10045.3.1.7**. 

The keys are saved the keys in PEM-encoding (for a usage in **Java**, **PHP**, **C#**, **CryptoJs**, **NodeJs**, Python, WebCrypto, Go and Dart). There are 2 PEM encodings that are widely used - all of my programs run with the **PKCS#8** encoding. The "original" and "older" format is the **SEC1** encoding.

This is my sample EC key pair in **PKCS#8 PEM encoding**:

EC Private Key 256 bit curve SECP256R1 ("ecprivatekey_secp256r1.pem"):

```plaintext
-----BEGIN PRIVATE KEY-----
MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgciPt/gulzw7/Xe12
YOu/vLUgIUZ+7gGo5VkmU0B+gUWhRANCAAQxkee3UPW110s0aUQdcS0TDkr8blAe
SBouL4hXziiJX5Me/8OobFgNfYXkk6R/K/fqJhJ/mV8gLur16XhgueXA
-----END PRIVATE KEY-----
```

EC Public Key 256 bit curve SECP256R1 ("ecpublickey_secp256r1.pem"):

```plaintext
-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEMZHnt1D1tddLNGlEHXEtEw5K/G5Q
HkgaLi+IV84oiV+THv/DqGxYDX2F5JOkfyv36iYSf5lfIC7q9el4YLnlwA==
-----END PUBLIC KEY-----
```

If you want to know how this key pair was created with OpenSSL visit the page [EC key pair generation](ec_key_generation.md).

Last update: Aug. 22nd 2021

Back to the main page: [readme.md](../readme.md)