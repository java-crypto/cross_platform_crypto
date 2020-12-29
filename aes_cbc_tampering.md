# Cross-platform cryptography

## AES CBC mode tampering

I'm for sure you read this message a lot of times: "encryption with AES in mode CBC is secure". 

The good news are - this statement is **correct**.

The bad news are - never believe anybody who claims that the encryption is "secure" as you will see that AES encryption in mode CBC without further enhancements is **unsecure**.

Ups... whom should you believe now as both statements are written in one article?

The answer is easy: follow my article and see why both statements are correct and why you should consider an additional security level in your program.

#### Why is it necessary to "secure" encrypted data?  

The answer is very easy: to get it secure. Encrypting data with AES in mode CBC is so secure that nobody can decrypt them without having access to the encryption key or putting in the right password for a PBKDF2-derived key.

Unfortunately there are attacker outside (the so called "man in the middle") that might been able that have access to the encrypted data (think of a chat or email). If the are been able to <u>modify</u> the ciphertext then they can manipulate the encrypted data that you will receive and you will decrypt data with different result compared to the original plaintext.

This kind of manipulating is called **tampering** and the bad news are - **it works without the knowledge or even breaking of the encryption key**.

#### How to avoid the possibility of tampering?

The only way to prevent from this kind of manipulation is to append some data that work like a "signature" or "checksum". If you receive such secured encrypted data you first have to check for the signature and **only if the "signature" is correct you decrypt it**. In my article [AES CBC mode 256 PBKDF2 HMAC string encryption](aes_cbc_tampering.md) I present a solution for this security issue.

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

As the program is for a demonstration of tampering only I wrote "just" a Java version - [test it here online](https://paiza.io/projects/e/M9Qy0oQqYPFy12_yUx2T_Q/). Kindly note that the tampering method itself has no access to the encryption key. The program is based on my article [AES CBC mode 256 string encryption](aes_cbc_256_string_encryption.md/).

I show the main method here in the article:

```java
private static String aesCbcTamperingBase64(String data, String guessedString, String newString) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    String[] parts = data.split(":", 0);
    byte[] iv = base64Decoding(parts[0]); // unused
    byte[] encryptedData = base64Decoding(parts[1]); // unused
    byte[] guessedStringByte = guessedString.getBytes(StandardCharsets.UTF_8);
    byte[] newStringByte = newString.getBytes(StandardCharsets.UTF_8);
    // tampering with guessedString and newString
    for (int i = 0; i < 16; i++) {
        iv[i] = (byte) (iv[i] ^ newStringByte[i] ^ guessedStringByte[i]);
    }
    // encode back to base64
    return base64Encoding(iv) + ":" + base64Encoding(encryptedData);
}
```

In this method there is no usage of a key and the tampering is done just by guessing the original string and xor'ing it with the new string - that's all and very simple.

This is an output (as there are random elements your output will differ):

```plaintext
AES CBC 256 String Decryption only with tampering

* * * Decryption original * * *
decryptionKey (Base64): d3+KZq3lsdy5Hgd1VRVQFUhA9jU0PaBHTG2LG2PazQM=
ciphertext (Base64): 8GhkycnbzDo5WbRol5zfCQ==:gQrTOJdobbI4fuCHCfu5eqKFYCaw/AM6xMK2o+xCXB0MeAQ7rFbXAbAE3Ex+bD8c
input is (Base64) iv : (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog

tampering the data without encryption key
guessedString: The quick brown 
newString:     The happy brown 

* * * Decryption tampered * * *
ciphertext (Base64): 8GhkydDP1SkrWbRol5zfCQ==:gQrTOJdobbI4fuCHCfu5eqKFYCaw/AM6xMK2o+xCXB0MeAQ7rFbXAbAE3Ex+bD8c
plaintext:  The happy brown fox jumps over the lazy dog

```

Last update: Nov. 23rd 2020

Back to the main page: [readme.md](readme.md)