# Cross-platform cryptography

## RSA file signature with PKCS#1.5 padding and OpenSSL

This article will show you how to generate a RSA key pair, sign a file with the private key and verify the signature with the public key. All you need for this task is an installed OpenSSL program on your machine.

Best of all: when working as explained you get a full compatibility to the elliptic curve signatures described in the article [RSA string signature with PKCS#1.5 padding](rsa_signature_string.md).

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

A last note on EC key pair generation: Especially the private key contains very sensitive data so it need to get **protected**. More information can be found in the article [RSA key generation](rsa_key_generation.md).

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

Now we are signing this file:

```plaintext
openssl dgst -sha256 -sign privatekey2048.pem -out rsamessage.signature.bin rsamessage.txt
```
As the signature is in binary encoding we need to take a hex view to the signature:
```plaintext
BC 24 81 E3 BE 38 A7 7D 08 06 5F EC 08 B8 EF 2A |.....8..........  
...
2F F3 D2 F0 24 46 E6 5C 61 61 80 19 A9 37 3E 56 |.....F..aa...7.V 
```
If you stay with OpenSSL for verification on your machine you do not need to run the next two steps and proceed directly to task five.

### Third task: convert the binary signature to Base64 encoding

With just one line you can convert the binary signature to a Base64 encoded signature:

```plaintext
openssl base64 -in rsamessage.signature.bin -out rsamessage.signature.base64.txt
```
Here we see the result - please note that OpenSSL is adding a "new line character" after 64 characters:

```plaintext
vCSB4744p30IBl/sCLjvKm2nix7wfScQn99YX9tVIDNQIvU3QnSCLc2cF+J6R9WM
...
L/PS8CRG5lxhYYAZqTc+Vg==
```

The encoding is perfect for transmitting via Email or chat purposes. The signature can be as well provided to the other programs for verifying - try it with the Browser based verification tool: 

### Fourth task: convert the Base64 encoded signature to a binary encoded signature

When verifying this signature with OpenSSL or getting a signature from third party you need to Base64 decode the signature back to a binary format:

```plaintext
openssl base64 -d -in rsamessage.signature.base64.txt -out rsamessage.signature.dec.bin
```

### Fifth task: verify the signature of a file with the private key using OpenSSL

To verify the digital signature we do need to have 3 files available: the file itself, the signature and the **public key** of the signer. Now we verify the signature with one line of code:

```plaintext
openssl dgst -sha256 -verify publickey2048.pem -signature rsamessage.signature.bin rsamessage.txt
when using the Base64 decoded signature from task 4 use this command:
openssl dgst -sha256 -verify publickey2048.pem -signature rsamessage.signature.dec.bin rsamessage.txt
```
Both commands will successfully run with a positive result - when the signature is correct:
```plaintext
Verified OK
```


## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

Last update: Jan. 09th 2021

Back to the main page: [readme.md](../readme.md)