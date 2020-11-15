# Cross-platform cryptography

## RSA key conversion

When comparing the programs you will notice that the RSA keys for C# looking like different as they are not in the "PEM"-format but in a XML-format. 

This is how a (private) key in PEM-format is looking like:

```plaintext
-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDwSZYlRn86zPi9
e1RTZL7QzgE/36zjbeCMyOhf6o/WIKeVxFwVbG2FAY3YJZIxnBH+9j1XS6f+ewjG
...
3PIW4/CddNs8mCSBOqTnoaxh
-----END PRIVATE KEY-----
```

A XML- (private) key is looking like this:

```plaintext
<RSAKeyValue>
<Modulus>8EmWJUZ/Osz4vXtUU2S...+ERa5MfoIV09Zb/LJ2aW2gQw==</Modulus> 
<Exponent>AQAB</Exponent> 
<P>/8atV5DmNxFrxF1PODDjdJPNb...VRoA9RQiVHhuJBSDPYJPoP34k=</P> 
<Q>8H9wLE5L8raUn4NYYRuUVMa+...FnoIBlfuo02rZpC5o5zebaDIms=</Q> 
<DP>BPXecL9Pp6u/0kwY+DcCgkVHi67...P50iB/e9j+3EmZm/7iVzDk=</DP> 
<DQ>rBWkAC/uLDf01Ma5AJMpahfk...QqQn2kAOh2jXXqFOstOZQc9Q0=</DQ> 
<InverseQ>BtiIiTnpBkd6hkqJSo...s8pJGns6KFsPThtJGuH6c6KVwQ==</D> 
</RSAKeyValue>
```

Although we clearly see variables and their data within the XML-file it is not as easy to convert these data into the PEM-format and vice versa. We would have to parse or encode a lot.

### So how to convert keys between XML- and PEM-format? 

Of course you will find a lot of websites where you can do the conversion online. The question is - is it **secure** to do it this way? The answer is: "it depends" on the kind of keys. 

#### Convert a public key online

As the key name already states - it is a key in public. For that reason I could present my **public key** online on my web site or this Github repository. That given **you can convert between the two formats online** (except for data that have to stay very secret).

#### Convert a private key online

Except for demonstration purpose like this web site the answer is easy: **never ever convert a private key online**.

### Locally convert a PEM-key to a XML-key with a C#-program

I found a C#-program "running out of the box" and it runs in the online-compiler as well. **Security warning: do not convert your own private key online!** For your private key: run the program in your environment and convert the private key locally.

Run the [C# Pem to XML key converter online](https://repl.it/@javacrypto/CpcCsharpRsaKeyConverterPemToXml#main.cs).

After you press "Run" you have to type in filename (e.g. private.pem or public.pem) and the conversion is done within a second. 

```plaintext
RSA public, private or PKCS #8  key file to decode: private.pem
Trying to decode and parse as PEM PKCS #8 PrivateKeyInfo ..
showing components ..

Created an RSACryptoServiceProvider instance

XML RSA private key:  2048 bits
<RSAKeyValue><Modulus>8EmWJUZ/...6c6KVwQ==</D></RSAKeyValue>
```

If you change the verbose-constant to true in the code you get all of the key data... try it your own.

The source code is available as well in my Github repository: [CsharpRsaKeyConverterPemToXml.cs](RsaKeyConversion/CsharpRsaKeyConverterPemToXml.cs)

Go to [RSA string signature](rsa_signature_string.md) and [RSA string signature verification only](rsa_signature_string_verification_only.md).

Last update: Nov. 15th 2020

Back to the main page: [readme.md](readme.md)