# Cross-platform cryptography

## RSA file signature with PSS padding and OpenSSL

This article will show you how to generate a RSA key pair, sign a file with the private key and verify the signature with the public key. All you need for this task is an installed OpenSSL program on your machine.

Best of all: when working as explained you get a full compatibility to the elliptic curve signatures described in the article [RSA string signature with PSS padding](rsa_signature_pss_string.md).

This example was tested with **OpenSSL version 1.1.1g** - kindly do no use older versions (<= 1.1.0) as they may not work as expected.

### First task: generate a RSA key pair

My example key pair will be created with a key length of 2048 bit = 256 bytes. 
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
We will receive two files on your device containing the two keys:

privatekey2048.pem:
```plaintext
-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDwSZYlRn86zPi9
...
3PIW4/CddNs8mCSBOqTnoaxh
-----END PRIVATE KEY-----
```

publickey2048.pem:
```plaintext
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
...
QwIDAQAB
-----END PUBLIC KEY-----
```

A last note on RSA key pair generation: Especially the private key contains very sensitive data so it need to get **protected**. More information can be found in the article [RSA key generation](rsa_key_generation.md).

### Second task: sign a file with the private key using OpenSSL

Before we start with the signature process itself we need to create a file that gets signed. 

OpenSSL will work with each file but to get a compatibility with other programs <u>using a string</u> as source for the signature it is important that the string in your message file is the same as the string in the examples in [RSA string signature with PKCS#1.5 padding](rsa_signature_string.md). 

That means in detail: **do not use any string delimiter or new line characters or carriage return characters**. When typing a string in an editor it is normal to press the "Enter" button but that will add the "CR/LF" characters at the end. Verifying the stripped string with the other programs will fail as it is not the same data that was signed. The usage of an editor that will show you these not printable characters like **Notepad++** may help you in this case.

This is an example file ("rsamessage.txt") in hex view mode that has the "CR/LF" characters ("0D 0A") at the end - **don't use this in cross platform programs**:

```plaintext
54 68 65 20 71 75 69 63 6B 20 62 72 6F 77 6E 20 |The.quick.brown.  
66 6F 78 20 6A 75 6D 70 73 20 6F 76 65 72 20 74 |fox.jumps.over.t  
68 65 20 6C 61 7A 79 20 64 6F 67 0D 0A          |he.lazy.dog..
```

I'm providing a sample file **rsamessage.txt** that has just one row with my demonstration text to sign in my GitHub-repository.

You may have seen the example in [RSA file signature with PKCS#1.5 padding](rsa_signature_file_openssl.md) where we directly sign the message file with OpenSSL, this is **not possible** when using the PSS padding. Unfortunately we have to hash the message with the requested hash algorithm first and then we will sign this hash.

#### Sub-Task: hash the message file with SHA-256

You will encounter a minimal problem when hashing a file with OpenSSL - you can get the output (the hash value) written to file and you need to redirect the output to a file. On a Windows system it is done with ">" but I can't test this sub task on other operating systems.

Simply create a file "openssl sha256.cmd" with this content:
```plaintext
openssl dgst -sha256 -binary rsamessage.txt -out > rsamessage.hash.bin
```
and let it run, you will receive a file "rsamessage.hash.bin" with the binary hash value:
```plaintext
D7 A8 FB B3 07 D7 80 94 69 CA 9A BC B0 08 2E 4F 
8D 56 51 E4 6D 3C DB 76 2D 02 D0 BF 37 C9 E5 92 
```
Now we can proceed with the signature process.

#### Sub-Task: sign the hash file

Now we are signing the hash of our "rsamessage.txt" file. The command line is longer due to the PSS parameters passed to OpenSSL that have to been equal to the other cross platform programs.

```plaintext
openssl pkeyutl -sign -out rsamesssage.pss.signature.bin -pkeyopt digest:sha256 -pkeyopt rsa_padding_mode:pss -pkeyopt rsa_pss_saltlen:32 -in rsamessage.hash.bin -inkey privatekey2048.pem
```
As the signature is in binary encoding we need to take a hex view to the signature. You will notice that the signature is much longer (256 byte) that theRSA signature done with PKCS#1.5 padding (32 bytes):
```plaintext
62 FE E5 E0 34 9B 1E 3B 18 7B A1 E5 23 C8 2A 97 
...
33 26 CC 06 2B 03 8D 8C 12 99 8A C9 FA 2C 02 42 
```
If you stay with OpenSSL for verification on your machine you do not need to run the next two steps and proceed directly to task five.

### Third task: convert the binary signature to Base64 encoding

With just one line you can convert the binary signature to a Base64 encoded signature:

```plaintext
openssl base64 -in rsamesssage.pss.signature.bin -out rsamessage.pss.signature.base64.txt
```
Here we see the result - please note that OpenSSL is adding a "new line character" after 64 characters:

```plaintext
Yv7l4DSbHjsYe6HlI8gql49zdrAgppQXkg8SZp521TFa85ZUN4OZ8o9Rua6w9USf
...
MybMBisDjYwSmYrJ+iwCQg==
```

The encoding is perfect for transmitting via Email or chat purposes. The signature can be as well provided to the other programs for verifying - try it with the Browser based verification tool: 

### Fourth task: convert the Base64 encoded signature to a binary encoded signature

When verifying this signature with OpenSSL or getting a signature from third party you need to Base64 decode the signature back to a binary format:

```plaintext
openssl base64 -d -in rsamessage.pss.signature.base64.txt -out rsamessage.pss.signature.dec.bin
```

### Fifth task: verify the signature of a file with the private key using OpenSSL

To verify the digital signature we do need to have 3 files available: the (or better the file with the hash of the) file itself, the signature and the **public key** of the signer. Now we verify the signature with one line of code:

```plaintext
openssl pkeyutl -verify -pkeyopt rsa_padding_mode:pss -pkeyopt rsa_pss_saltlen:32 -pkeyopt digest:sha256 -in rsamessage.hash.bin -sigfile rsamesssage.pss.signature.bin -pubin -inkey publickey2048.pem
when using the Base64 decoded signature from task 4 use this command:
openssl pkeyutl -verify -pkeyopt rsa_padding_mode:pss -pkeyopt rsa_pss_saltlen:32 -pkeyopt digest:sha256 -in rsamessage.hash.bin -sigfile rsamesssage.pss.signature.dec.bin -pubin -inkey publickey2048.pem
```
Both commands will successfully run with a positive result - when the signature is correct:
```plaintext
Signature Verified Successfully
```

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

Last update: Jan. 09th 2021

Back to the main page: [readme.md](../readme.md)