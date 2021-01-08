# Cross-platform cryptography

## Elliptic curve signature (ECDSA) with DER encoding and OpenSSL

This article will show you how to generate an elliptic key (EC) pair, sign a file with the private key and verify the signature with the public key. All you need for this task is an installed OpenSSL program on your machine.

Best of all: when working as explained you get a full compatibility to the elliptic curve signatures described in the article [Elliptic key string signature (ECDSA) with DER encoding](ecdsa_signature_der_string.md).

This example was tested with **OpenSSL version 1.1.1g** - kindly do no use older versions (<= 1.1.0) as they may not work as expected.

### First task: generate an elliptic curve key pair

My example key pair will be created with the curve **SECP256R1** that is known with these names as well (depending on the platform): **NIST P-256**, **PRIME256V1** or with the O.I.D. identifier **1.2.840.10045.3.1.7**. It has a key length of 256 bit = 32 byte.

The **key generation** takes place in two steps - first we generate the EC <u>private key</u> and then we generate the belonging <u>public key</u>:

```plaintext
ecdsa private key generation:
openssl ecparam -name prime256v1 -genkey -noout -out ecprivatekey_secp256r1.pem
ecdsa public key generation:
openssl ec -in ecprivatekey_secp256r1.pem -pubout -out ecpublickey_secp256r1.pem
```
We will receive two files on your device containing the two keys:

ecprivatekey_secp256r1.pem:
```plaintext
-----BEGIN PRIVATE KEY-----
MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCAU2f8tzo99Z1HoxJlY
96yXUhFY5vppVjw1iPKRfk1wHA==
-----END PRIVATE KEY-----
```

ecpublickey_secp256r1.pem:
```plaintext
-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEzb7yAFWup6iDqJiEq764rAumsV2M
rspZxaP3WGpwHaC4Uff3N4UbJZF7Zac1c6W7KJl0eeCP0205Q3UEpwxndQ==
-----END PUBLIC KEY-----
```

A last note on EC key pair generation: Especially the private key contains very sensitive data so it need to get **protected**. More information can be found in the article [EC key generation](ec_key_generation.md).

### Second task: sign a file with the private key using OpenSSL

Before we start with the signature process itself we need to create a file that gets signed. 

OpenSSL will work with each file but to get a compatibility with other programs <u>using a string</u> as source for the signature it is important that the string in your message file is the same as the string in the examples in [Elliptic key string signature (ECDSA) with DER encoding](ecdsa_signature_der_string.md). 

That means in detail: **do not use any string delimiter or new line characters or carriage return characters**. When typing a string in an editor it is normal to press the "Enter" button but that will add the "CR/LF" characters at the end. Verifying the stripped string with the other programs will fail as it is not the same data that was signed. The usage of an editor that will show you these not printable characters like **Notepad++** may help you in this case.

This is an example file in hex view mode that has the "CR/LF" characters ("0D 0A") at the end - **don't use this in cross platform programs**:

```plaintext
54 68 65 20 71 75 69 63 6B 20 62 72 6F 77 6E 20 |The.quick.brown.  
66 6F 78 20 6A 75 6D 70 73 20 6F 76 65 72 20 74 |fox.jumps.over.t  
68 65 20 6C 61 7A 79 20 64 6F 67 0D 0A          |he.lazy.dog..
```

I'm providing a sample file **ecdsamessage.txt** that has just one row with my demonstration text to sign in my GitHub-repository.

Now we are signing this file:

```plaintext
openssl dgst -sha256 -sign ecprivatekey_secp256r1.pem -out ecdsamessage.signature.bin ecdsamessage.txt
```
As the signature is in binary encoding we need to take a hex view to the signature:
```plaintext
30 46 02 21 00 90 C9 67 65 0C 53 4E 80 8E 5D 3D |0F.....ge.SN....  
C0 77 EF 7F 9A 94 47 2A CD 96 84 44 00 F1 44 95 |.w....G....D..D.  
6C 19 BF F3 E3 02 21 00 DA F8 17 68 1E 00 EA CC |l..........h....  
A1 DA EC D1 91 30 75 87 D7 6B 15 B9 0E 71 9E BD |.....0u..k...q..  
61 2C 3E F0 0A 28 43 E5                         |a.....C.   
```
If you stay with OpenSSL for verification on your machine you do not need to run the next two steps and proceed directly to task five.

### Third task: convert the binary signature to Base64 encoding

With just one line you can convert the binary signature to a Base64 encoded signature:

```plaintext
openssl base64 -in ecdsamessage.signature.bin -out ecdsamessage.signature.base64.txt
```
Here we see the result - please note that OpenSSL is adding a "new line character" after 64 characters:

```plaintext
MEYCIQCQyWdlDFNOgI5dPcB373+alEcqzZaERADxRJVsGb/z4wIhANr4F2geAOrM
odrs0ZEwdYfXaxW5DnGevWEsPvAKKEPl
```

The encoding is perfect for transmitting via Email or chat purposes. The signature can be as well provided to the other programs for verifying - try it with the Browser based verification tool: 

### Fourth task: convert the Base64 encoded signature to a binary encoded signature

When verifying this signature with OpenSSL or getting a signature from third party you need to Base64 decode the signature back to a binary format:

```plaintext
openssl base64 -d -in ecdsamessage.signature.base64.txt -out ecdsamessage.signature.dec.bin
```

### Fifth task: verify the signature of a file with the private key using OpenSSL

To verify the digital signature we do need to have 3 files available: the file itself, the signature and the **public key** of the signer. Now we verify the signature with one line of code:

```plaintext
openssl dgst -sha256 -verify ecpublickey_secp256r1.pem -signature ecdsamessage.signature.bin ecdsamessage.txt
when using the Base64 decoded signature from task 4 use this command:
openssl dgst -sha256 -verify ecpublickey_secp256r1.pem -signature ecdsamessage.signature.dec.bin ecdsamessage.txt
```
Both commands will successfully run with a positive result - when the signature is correct:
```plaintext
Verified OK
```


## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

Last update: Jan. 08th 2021

Back to the main page: [readme.md](readme.md)