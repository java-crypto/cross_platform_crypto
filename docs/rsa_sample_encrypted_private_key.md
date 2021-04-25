# Cross-platform cryptography

## RSA encrypted private key

All my RSA encryption and signature examples are running with **unencrypted** PEM- or XML-formatted keys. In production that may be good for **public keys** but **never for private keys**. I did it in my examples because a lot of frameworks (like Java or Go) do not support the usage of encrypted private keys "out of the box".

This article describes the structure of an encrypted RSA private key and in other articles I will present solutions for all frameworks.

If you need to get the basics I recommend to visit my page [RSA sample key pair](rsa_sample_keypair.md). If you want to know how you can decrypt such an encrypted RSA key programmatically then I'm recommending my article [RSA encrypted private key - decrypt manual](RSA encrypted private key - decrypt manual.md).

### How can I generate an encrypted RSA key?

In the article [RSA key generation](rsa_key_generation.md) I showed how to generate the **unencrypted** RSA private key using this command line with OpenSSL:

```plaintext
openssl genpkey -out privatekey2048.pem -algorithm RSA -pkeyopt rsa_keygen_bits:2048
```

To get the encrypted version of the key I used this additional command line:

```plaintext
openssl pkcs8 -v2 aes-256-cbc -iter 2048 -topk8 -in privatekey2048.pem -out privatekey_pkcs8_2048_aes256_123456.pem
```

The parameter "-v2" forces OpenSSL to use the newer password encryption scheme version 2, next you find the encryption algorithm ("aes-256-cbc") and the number of iterations ("2048") to derive the key. The parameter "-topk8" gives a PKCS#8 encoded key. After that you are aked to type in the password for the encrypted key twice (I used 123456 as passphrase for my sample key).

### How does an encrypted private key looks like? 

I'm using again the PEM-format and this is my sample private key ("privatekey_pkcs8_2048_aes256_123456.pem"):

```plaintext
-----BEGIN ENCRYPTED PRIVATE KEY-----
MIIFLTBXBgkqhkiG9w0BBQ0wSjApBgkqhkiG9w0BBQwwHAQIUo/aHb7/5jUCAggA
MAwGCCqGSIb3DQIJBQAwHQYJYIZIAWUDBAEqBBCbpiqeplyyDN85uu5yYqpFBIIE
0Iqvy2v42HeASj6B25llJjNOWvT5djjDiAg7BFsuOhMq10mTC7gWduXBai5llUBH
qF4+GgqCeedwT24YkdR4wAqtc2shcmq+2xC70N/XrSg9geGehmNwDx35byhPjouk
XwA5tgVsKtxTob/xjPYhjL5J1KpWOPQhdnHPGfGlWedTyTKboRfgkZ/Yds5EW6k7
mO9wt/iXcym6QgaJTswa1ntGPVaKMYvXVRtNnphsI9M7njXBYO52vfujUaeKn8Xe
qmuEAKGFhhu+r7/WWyQl+Zq2KIoD5ImOxuXScCZVH3+76AxXSwDJzB6ZG1SowSy+
YHbfkmvK2H6YdFshEfI/coMmu9E3wDKuCY7rgb70lHLcpvbSMa88vCFl3d/ZfmFl
w4TwG3vEVR9wfbXAuAw6XTSLBODcF4ifZnyXI2Dr+fQbQd/4o8w05hDiPkXRgnYY
e/4nFbTGGKvatZ7LOLa62isiGtQ4nprDpTapLMeJCFSgQN1jlZMVvV6mKUfrCJ69
3bESlyYGAN0MAx4KeMcCRwuEXWhOkLMFW0gvRp1udiMxJ8SnWfseYTr2FPUFQlFk
2ZhHsSO0Pg/cszUSuCX0q2ZW0ZZOiihJsmiQNXlbsRog9m8vaxUfA00v/q6iryGE
ApCiQd653ikukRhk/nQEY6OV4byLyI9zESF/pIdsrFnEtGSd0tggGsREtn11D8+7
SW0F0Es4P4UUPjiaYzyB58gcZJWxcoq63NOTUaicvTjNbzQu1+4y+2xhFe89e09w
PFSGerjSV/uVtVTH2hb79mAmlyLvaikQfQq2nvpCuZy7wZjhPA3D03WFTCbrYS7P
PfckAICCBCIB97vPsWVAviOVOrG5NDbIag+/3YhZb2gyuR2/E0XI1NdXO63NqU9R
B6VUUNdSvv4djA30nVjARlh3LOZmTy50OIj/QDOEbBrtFH79eHHxHRiK0+Q0A8Ch
EoDDz7bMKBcpiS3vXnI2UsrZX/QIuRjypc003uVm4MvdUwv0j3UGZr9zRGctqWf0
YBRkoIELRA45hQn0Ln3+wjAcjLRPpeuPgVAq7tDDLDmP0i/m8UbjNorhdv4CrT/u
P+keTW7v9Nuov1IXG4W6wKipYRzMjiYrTL5TaXPi9z63b6RQVShAEqSTtsPg5nDy
CZkt5NPBltq+3InrsTlbKSQGYn6wyEQeBYezD3CLkIliDn7UM4BDn+k5YonNynU4
emxbU4wyBEXoMD8+aBMf+bKKQXU97Ts9p7is+Ogqyop2mbMOebIxCKayizlb7/hp
Qs2ThBP84OrniUEeKVAkdykZyrdCoatgXVJngDwwEkSwOrEYZ0sJ48qPQMTN3kVq
r5XJjX3m8mpXdz0s9VbhT/rWSdQMX82msof4c6+RghEbiHFG4B0el5vrJaAROb69
3OnoU8mYKBlFjbO1rJJpGS5h/v+ysqWxDxfPPLD86UsgDAjr5C7W3K8eYB1UD/wP
n91TuTdrLKhLE93VsCyXnsLm44Td3MBv89Fr2CMF/Jz15Nfrg3yvR7bKvkNs3uVM
FVO3kqtmUMC/RsUr3NIE/RZ2sS+cjLMDE2zBUgHnDpNQB6WSuwqEI9prAWxEc1jS
SwyNkvstftK35nfeI3FNP0R94+kSKzGmiQh5yl3ufAVi
-----END ENCRYPTED PRIVATE KEY-----
```

The technical data for this key are:
* algorithm scheme: RSA private key
* key format: PKCS#8
* key encoding: PEM encoded
* key size: 2048 bit
* password encryption scheme: PBES2 [OpenSSL -v2]
* password derivation method: PBKDF2 with HMAC SHA-256
* salt (8 byte): 52 8F DA 1D BE FF E6 35
* iterations: 2048
* passphrase: 123456
* encryption algorithm: AES-256-CBC
* initialization vector (16 byte): 9B A6 2A 9E A6 5C B2 0C DF 39 BA EE 72 62 AA 45

When analyzing this key in an ASN1 decoder you see this structure:

```plaintext
   0 1325: SEQUENCE {
   4   87:   SEQUENCE {
   6    9:     OBJECT IDENTIFIER pkcs5PBES2 (1 2 840 113549 1 5 13)
  17   74:     SEQUENCE {
  19   41:       SEQUENCE {
  21    9:         OBJECT IDENTIFIER pkcs5PBKDF2 (1 2 840 113549 1 5 12)
  32   28:         SEQUENCE {
  34    8:           OCTET STRING 52 8F DA 1D BE FF E6 35
  44    2:           INTEGER 2048
  48   12:           SEQUENCE {
  50    8:             OBJECT IDENTIFIER hmacWithSHA256 (1 2 840 113549 2 9)
  60    0:             NULL
         :             }
         :           }
         :         }
  62   29:       SEQUENCE {
  64    9:         OBJECT IDENTIFIER aes256-CBC (2 16 840 1 101 3 4 1 42)
  75   16:         OCTET STRING 9B A6 2A 9E A6 5C B2 0C DF 39 BA EE 72 62 AA 45
         :         }
         :       }
         :     }
  93 1232:   OCTET STRING
         :     8A AF CB 6B F8 D8 77 80 4A 3E 81 DB 99 65 26 33
         :     ...
         :     49 D4 AA 56 38 F4 21 76 71 CF 19 F1 A5 59 E7 53
         :             [ Another 1104 bytes skipped ]
         :   }
```

and the same with (hex encoded) bytes:

```plaintext
0000: 30 82 05 2d                               ; SEQUENCE (52d Bytes)
0004:    30 57                                  ; SEQUENCE (57 Bytes)
0006:    |  06 09                               ; OBJECT_IDENTIFIER (9 Bytes)
0008:    |  |  2a 86 48 86 f7 0d 01 05  0d
         |  |     ; "1.2.840.113549.1.5.13"
0011:    |  30 4a                               ; SEQUENCE (4a Bytes)
0013:    |     30 29                            ; SEQUENCE (29 Bytes)
0015:    |     |  06 09                         ; OBJECT_IDENTIFIER (9 Bytes)
0017:    |     |  |  2a 86 48 86 f7 0d 01 05  0c
         |     |  |     ; "1.2.840.113549.1.5.12"
0020:    |     |  30 1c                         ; SEQUENCE (1c Bytes)
0022:    |     |     04 08                      ; OCTET_STRING (8 Bytes)
0024:    |     |     |  52 8f da 1d be ff e6 35                           ; R......5
002c:    |     |     02 02                      ; INTEGER (2 Bytes)
002e:    |     |     |  08 00
0030:    |     |     30 0c                      ; SEQUENCE (c Bytes)
0032:    |     |        06 08                   ; OBJECT_IDENTIFIER (8 Bytes)
0034:    |     |        |  2a 86 48 86 f7 0d 02 09
         |     |        |     ; "1.2.840.113549.2.9"
003c:    |     |        05 00                   ; NULL (0 Bytes)
003e:    |     30 1d                            ; SEQUENCE (1d Bytes)
0040:    |        06 09                         ; OBJECT_IDENTIFIER (9 Bytes)
0042:    |        |  60 86 48 01 65 03 04 01  2a
         |        |     ; "aes256 (2.16.840.1.101.3.4.1.42)"
004b:    |        04 10                         ; OCTET_STRING (10 Bytes)
004d:    |           9b a6 2a 9e a6 5c b2 0c  df 39 ba ee 72 62 aa 45  ; ..*..\...9..rb.E
005d:    04 82 04 d0                            ; OCTET_STRING (4d0 Bytes)
0061:       8a af cb 6b f8 d8 77 80  4a 3e 81 db 99 65 26 33  ; ...k..w.J>...e&3
            ...
0521:       7d e3 e9 12 2b 31 a6 89  08 79 ca 5d ee 7c 05 62  ; }...+1...y.].|.b
```

The encrypted key is full compatible to the unencrypted key, means you can sign or decrypt like you would with the unencrypted one.

The public key remains unencrypted as it is "public".

Public Key 2048 bit ("publickey2048.pem"):

```plaintext
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9
iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT
/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8
01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB
ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g
QwIDAQAB
-----END PUBLIC KEY-----
```

One note regarding XML-keys: there is no format available for "encrypted XML keys" but it's up to you to encrypt (and later decrypt) the complete string data with one of my AES string encryption methods (e.g. using [AES GCM mode 256 PBKDF2 string encryption](aes_gcm_256_pbkdf2_string_encryption.md)).

Last update: Apr. 25th 2021

Back to the main page: [readme.md](../readme.md)