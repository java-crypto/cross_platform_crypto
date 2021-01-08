# Cross-platform cryptography

## RSA key formats

As there are many formats or encodings in use with Private- / Public key cryptography I will explain a lot of them for better understanding.

The second reason is - using the wrong key format on one or both sides will cause the cryptography to fail.

I will present a sample key pair for each format and on my Github-repository ["RSA sample keys"](RsaSampleKeys) you will find all keys in separate files.

Let's start with the format that is used in my examples - a PKCS8 key pair in PEM encoding:

### RSA PKCS8 key pair in PEM encoding

The most important identifier for this format is "BEGIN PRIVATE KEY" - please do not mix it with "BEGIN RSA PRIVATE KEY" that sounds better but will not work.

Private key:

```terminal
-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDwSZYlRn86zPi9
...
3PIW4/CddNs8mCSBOqTnoaxh
-----END PRIVATE KEY-----
```
Public key:

```terminal
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
...
QwIDAQAB
-----END PUBLIC KEY-----
```

### RSA PKCS1 key pair in PEM encoding

Although the identifier "BEGIN RSA PRIVATE KEY" sounds better this format is not usable in Java and some other frameworks. It contains the "pure" RSA key but should be avoided to use in Cross-platform usage.

Private key: 

```terminal
-----BEGIN RSA PRIVATE KEY-----
MIIEpAIBAAKCAQEA8EmWJUZ/Osz4vXtUU2S+0M4BP9+s423gjMjoX+qP1iCnlcRc
...
asmGuSaBKTyGlEoD7kx/1oTt3A7MetzyFuPwnXTbPJgkgTqk56GsYQ==
-----END RSA PRIVATE KEY-----
```
Public key: 

```terminal
-----BEGIN RSA PUBLIC KEY-----
MIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+0M4BP9+s423gjMjoX+qP1iCnlcRcFWxt
...
PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2gQwIDAQAB
-----END RSA PUBLIC KEY-----
```

### RSA key pair in XML encoding

The XML encoding is the "state of the art" format used for C# cryptography. The good news are: you can clearly see the values a key consists of. The bad news are: those keys are not compatible with the key import routines the other frameworks provide.

In the end it essential that you convert a key with a program or online service. My advice is: generate a key pair on C# and later convert the **public key** with an online service to get a PEM encoded public key. 

Or - for the other way round - convert a PEM encoded public key with the PemToXmlConverter, see my detailed information on [RSA key conversion](rsa_key_conversion.md).

Private key: 

```terminal
<RSAKeyValue>
    <Modulus>8EmWJUZ...LJ2aW2gQw==</Modulus>
    <Exponent>AQAB</Exponent>
    <P>/8atV5Dm...oP34k=</P>
    <Q>8H9wLE5L...aDIms=</Q>
    <DP>BPXecL9...7iVzDk=</DP>
    <DQ>rBWkAC/u...Qc9Q0=</DQ>
    <InverseQ>BtiIi...pOehrGE=</InverseQ>
    <D>hXGYfO...c6KVwQ==</D>
</RSAKeyValue>
```
Public key: 

```terminal
<RSAKeyValue>
    <Modulus>8EmWJUZ...LJ2aW2gQw==</Modulus>
    <Exponent>AQAB</Exponent>
</RSAKeyValue>
```

Let's examine some other formats.

### RSA PKCS8 encrypted key pair in PEM encoding

The **Private key** contain information that should kept secret when the key is on a device that is accessible. The key information can get encrypted with several algorithms like **AES-256** or **Triple DES**. The password for opening the key runs through a key derivation method and the encryption gets authenticated by a HMAC.

As some frameworks does not support the reading of such encrypted keys "out of the box" I left it out to use such keys in my examples. **When working in production environment I strongly advise you to use the encrypted ones**.

This example is a private key that has a **PBKDF2** key derivation, encryption with **AES 256 in mode CBC** and **hmacWithSHA256** for authentication or in short: a newer encryption scheme.

The **public key is not encrypted** and is the same as above. 

Private key: 

```terminal
-----BEGIN ENCRYPTED PRIVATE KEY-----
MIIFLTBXBgkqhkiG9w0BBQ0wSjApBgkqhkiG9w0BBQwwHAQIUo/aHb7/5jUCAggA
...
SwyNkvstftK35nfeI3FNP0R94+kSKzGmiQh5yl3ufAVi
-----END ENCRYPTED PRIVATE KEY-----
```

Public key:
```terminal
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
...
QwIDAQAB
-----END PUBLIC KEY-----
```

### RSA PKCS1 encrypted key pair in PEM encoding

This **private key** is encrypted with AES 256 in mode CBC as well but in PKCS1 encoding, the **public key is not encrypted** and is the same as above.:

Private key: 

```terminal
-----BEGIN RSA PRIVATE KEY-----
Proc-Type: 4,ENCRYPTED
DEK-Info: AES-256-CBC,ED3CD1EB2BAA063B54F172EC3E950EEC

zpeS37rw01+DT4Z/PHCpw6m4PFBfBlPaqqDkK8+F7j/OPebvB0x+qaadl+kh86VG
...
8QO0VdBji+lN/PbFtjtXoxw3Nr9s3gT8D3aJDo1YEq7saMC+oaGRgWMZo1kh60Ax
-----END RSA PRIVATE KEY-----
```
Public key: 

```terminal
-----BEGIN RSA PUBLIC KEY-----
MIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+0M4BP9+s423gjMjoX+qP1iCnlcRcFWxt
...
PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2gQwIDAQAB
-----END RSA PUBLIC KEY-----
```
### RSA PKCS1 DES encrypted key pair in PEM encoding

This **private key** is encrypted with the "old" **Triple DES** encryption:

Private key: 

```terminal
-----BEGIN RSA PRIVATE KEY-----
Proc-Type: 4,ENCRYPTED
DEK-Info: DES-EDE3-CBC,0183419ABA7D8BF2

JpFOA+6nsyT8WcDNgWPpbu7p420nWVlMrWIFnmlFFsMAZy+k3iJrdUlgOlQbhjUj
...
jdn911gPpy0XkmZEdNIoahw40qASCGc+vfjP99AnV66CLPXSbSO24ilWUMGAgseI
-----END RSA PRIVATE KEY-----
```
the **Public key is not encrypted** and is the same as above.: 

```terminal
-----BEGIN RSA PUBLIC KEY-----
MIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+0M4BP9+s423gjMjoX+qP1iCnlcRcFWxt
...
PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2gQwIDAQAB
-----END RSA PUBLIC KEY-----
```

### RSA PKCS8 key pair in DER encoding

The **DER** encoding is the binary form of a key. It is suitable when using it as a byte array within one system but will cause problems when exchanging them, so in most cases the above described **PEM** encodings are in use.

Private key in hex-viewing: 

```terminal
00000000 30 82 04 A4:02 01 00 02|82 01 01 00:F0 49 96 25
00000010 46 7F 3A CC:F8 BD 7B 54|53 64 BE D0:CE 01 3F DF
00000020 AC E3 6D E0:8C C8 E8 5F|EA 8F D6 20:A7 95 C4 5C
...
00000490 84 ED DC 0E:CC 7A DC F2|16 E3 F0 9D:74 DB 3C 98
000004A0 24 81 3A A4:E7 A1 AC 61
```
The Public key is not shown as its only binary data.


Last update: Jan. 08th 2021

Back to the main page: [readme.md](../readme.md)