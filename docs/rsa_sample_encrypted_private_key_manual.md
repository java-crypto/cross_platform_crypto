# Cross-platform cryptography

## RSA encrypted private key - decrypt manually

This article describes how to decrypt an encrypted RSA private key without additional libraries - this in known as **parsing** the key details.

A lot of frameworks do support encrypted private keys "out of the box", e.g. PHP/OpenSSL, C#, NodeJs Crypto, Node-forge or Python and it's easy to use them. But unfortunately Java or Go **do not support** them so I needed a function to do it manually/programmatically.

Let's have a look to the structure of the key - there are a lot of ASN.1 decoder available, you could use e.g. [https://lapo.it/asn1js/](https://lapo.it/asn1js/):

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

The "built in" libraries are supporting all algorithms but I'm concentrating to (only) one combination that is given by the four "OBJECT_IDENTIFIER" you see above:

```plaintext
Name of the identifier               | hex byte representation
pkcs5PBES2 (1 2 840 113549 1 5 13)   | 2a 86 48 86 f7 0d 01 05 0d
pkcs5PBKDF2 (1 2 840 113549 1 5 12)  | 2a 86 48 86 f7 0d 01 05 0c
hmacWithSHA256 (1 2 840 113549 2 9)  | 2a 86 48 86 f7 0d 02 09
aes256-CBC (2 16 840 1 101 3 4 1 42) | 60 86 48 01 65 03 04 01 2a

```

### Step 1: decode the key material to a byte array

Our encrypted key is in **PEM format** so I'm stripping off the header and footer line and **Base64-decode** the remaining content to a byte array.

### Step 2: get the position of the identifier

I'm using a simple search using the 4 byte arrays to get the position of the 4 object identifier. There is "vanity check" - if one or more identifier are not found the program is stopping.

### Step 3: get the salt and nmber of iterations for PBKDF2

This is the object identifier pointing to a PBKDF2 key derivation. The **salt** is 8 bytes long ("52 8f da 1d be ff e6 35").

The number of iterations is a bit more tricky a it is an integer that is converted from a byte array of variable length. In my example the line "002c" there is the first "02" indication that the following value will be of integer style with the length "02", the real value is (in hex) "x08 x00" that gives the decimal value of "2048" as the **number of iterations**.

I'm limiting the conversion "byte array to integer" up to 3 bytes length and so the maximum number of iterations are "8388607". 

```plaintext
0017:    |     |  |  2a 86 48 86 f7 0d 01 05  0c
         |     |  |     ; "1.2.840.113549.1.5.12"
0020:    |     |  30 1c                         ; SEQUENCE (1c Bytes)
0022:    |     |     04 08                      ; OCTET_STRING (8 Bytes)
0024:    |     |     |  52 8f da 1d be ff e6 35                           ; R......5
002c:    |     |     02 02                      ; INTEGER (2 Bytes)
002e:    |     |     |  08 00
```

### Step 4: get the PBKDF2 algorithm

As we searched (and found) for the object identifier "hmacWithSHA256 (1 2 840 113549 2 9)" the **PBKDF2 algorithm** is "PBKDF2WithHmacSHA256" (name in Java) or "sha256.New" (name in Go).

### Step 5: generate the decryption key

As our target decryption algorithm should be "AES-256" we do need a 32 bytes long key that we derive from the passphrase, salt, number of iterations using the PBKDF2 algorithm.

### Step 6: get the initialization vector for decryption of the encrypted key material

Analogue to the PBKDF2 algorithm we searched for "AES-256-CBC" as encryption algorithm identifier. The identifier is followed by the 16 bytes long initialization vector (IV) needed as parameter for decryption ("9b a6 2a 9e a6 5c b2 0c df 39 ba ee 72 62 aa 45").

```plaintext
0042:    |        |  60 86 48 01 65 03 04 01  2a
         |        |     ; "aes256 (2.16.840.1.101.3.4.1.42)"
004b:    |        04 10                         ; OCTET_STRING (10 Bytes)
004d:    |           9b a6 2a 9e a6 5c b2 0c  df 39 ba ee 72 62 aa 45  ; ..*..\...9..rb.E
```

### Step 7: decrypt the encrypted key material

Now we do need, as last step, get access to the ciphertext (= encrypted key material) that is located some bytes after the initialization vector:

```plaintext
005d:    04 82 04 d0                            ; OCTET_STRING (4d0 Bytes)
0061:       8a af cb 6b f8 d8 77 80  4a 3e 81 db 99 65 26 33  ; ...k..w.J>...e&3
            ...
0521:       7d e3 e9 12 2b 31 a6 89  08 79 ca 5d ee 7c 05 62  ; }...+1...y.].|.b
```

When decrypting this ciphertext we receive the RSA private key in PKCS#8 encoded form that can be used for any operation (decryption or signing) in the RSA algorithm world.

Here are the complete "technical data" for the sample key:
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

Last update: Apr. 25th 2021

Back to the main page: [readme.md](../readme.md)