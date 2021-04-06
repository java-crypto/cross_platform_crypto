# Cross-platform cryptography

## MCRYPT migration overview

This chapter is not about a cross platform project but a migration project on **PHP**. The standard cryptography library for symmetric encryption was **MCRYPT** and it was included in all major distributions. 

As Mcrypt was easy to use it was included in many projects but starting with PHP version 7.x it was marked as **deprecated** and got removed. A lot of people are now asking for help on [Stackoverflow.com](https://stackoverflow.com/search?tab=newest&q=mcrypt) when they updating their website server to PHP 7 or 8.

I'm trying to help in a lot of cases, the following algorithms and modes are directly supported by my article series:

* **encryption algorithms**: Rijndael-128 (a.k.a. "AES"), Rijndael-256, DES and Triple DES ("TDES")
* **algorithm modes**: ECB, CBC and CTR
* **padding modes**: zero padding (MCRYPT default) and PKCS#5/PKCS#7

### What are the parameters for a successful migration?

When examine the source code it is easy to see what parameters are in use in your special case, let's see a simple example:

```plaintext
encryption:
$ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key, $plaintext, MCRYPT_MODE_ECB);
decryption:
$decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key, $ciphertext, MCRYPT_MODE_ECB);
```

### Encryption algorithm and algorithm mode:

The choosen encryption algorithm is "**Rijndael-128**" (parameter 1) and the algorithm mode is "**ECB**" (parameter 4).

### Padding mode:

The padding is not visible or selectable by a parameter but it is <u>always</u> a **zero padding** when using MCRYPT, means that a plaintext is always filled up with "x00" characters up to the block length or a multiple (e.g. Rijndael-128 has a block length of 128 bit = 16 byte, DES uses a blocklength of 8 bytes).

If the "original" plaintext is directly input to the encryption function then any replacement has to deal with a manual zero padding. If you should find methods looking like these and they are  interposed between the original plaintext and the encryption function (or after the decryption function):

```plaintext
function pkcs7Padding($data, $blocklen) {
    $len = strlen($data);
    $padding = $blocklen - ($len % $blocklen);
    $data .= str_repeat(chr($padding),$padding);
    return $data;
}

function pkcs7Unpadding($data, $blocklen) {
    $packing = ord($data{strlen($data) - 1});
    if($packing and ($packing < $blocklen)){
        for($P = strlen($data) - 1; $P >= strlen($data) - $packing; $P--){
            if(ord($data{$P}) != $packing){
                $packing = 0;
            }
        }
    }
    return substr($data,0,strlen($data) - $packing);
}
```

then a **PKCS#7 padding** (or in Java PKCS#5) is in use.

### Can you show example data for encryption and decryption ?

For this example we are using Rijndael-128 encryption in ECB mode - here are the outputs of encryption and decryption functions, using a 15 bytes long plaintext and a 16 bytes long key (for Rijndael-128 / AES-128):

```plaintext
plaintext (15 byte): 123456789012345
key (16 byte):       12345678abcdefgh
Enc R128 ECB Key 16 B Pt 15 B length: 16 hex: 69f567233e8370691c2d7debf3335373 base64: afVnIz6DcGkcLX3r8zNTcw==
Dec R128 ECB Key 16 B Pt 15 B length: 16 hex: 31323334353637383930313233343500 string: 123456789012345#
Dec R128 ECB Key 16 B Pt 15 B length: 15 hex: 313233343536373839303132333435 string: 123456789012345#

```

The encryption result is a Base64 encoded string ("afVnIz6DcGkcLX3r8zNTcw==") and this ciphertext is used as input for the decryption function. But why do we have two result lines for decryption?

In the first "Dec" line the decyption result is seemingly the string "123456789012345" and thats the plaintext but when printing out the decryption result as hex encoded string we see that there is a "x00" at the end that is not printed out. The "x00" is the result of the "zero padding" on encryption that fills up the plaintext to the block length of our Rijndael-/AES- algorithm (16 bytes). This is the <u>direct output</u> of the MCRYPT decryption function.

The second "Dec" line removes all "x00" at the end of the decryptedtext (via the rtrim-function) and now we receive the original string.

For that reason you should **never use "zero padding" when working with binary data** that could have a "x00" character at the end of the plaintext (think of a picture or video with binary data and the chance is 1/256 or 4 per thousand that there is a "x00" at the end...).

Here are my articles regarding MCRYPT migration themes:

| Solution | mode | PHP OpenSSL | PHP phpseclib 3 | 
| ------ | :------: | :--: | :--: | :--: | :--: | :--: | :--: | :--: |
|[MCRYPT Rijndael-128 ECB](mcrypt_rijndael128.md) | ECB mode | :white_check_mark: | :white_check_mark: |
|[MCRYPT Rijndael-128 CBC](mcrypt_rijndael128.md) | CBC mode | :white_check_mark: | :white_check_mark: |
|[MCRYPT Rijndael-128 CTR](mcrypt_rijndael128.md) | CTR mode | :white_check_mark: | :white_check_mark: |
| | | |
|[MCRYPT Rijndael-256 ECB](mcrypt_rijndael256.md) | ECB mode | :x: | :white_check_mark: |
|[MCRYPT Rijndael-256 CBC](mcrypt_rijndael256.md) | CBC mode | :x: | :white_check_mark: |
|[MCRYPT Rijndael-256 CTR](mcrypt_rijndael256.md) | CTR mode | :x: | :white_check_mark: |
| | | |
|[MCRYPT DES ECB](mcrypt_des.md) | ECB mode | :white_check_mark: | :white_check_mark: |
|[MCRYPT DES CBC](mcrypt_des.md) | CBC mode | :white_check_mark: | :white_check_mark: |
|[MCRYPT DES CTR](mcrypt_des.md) | CTR mode | :x: | :white_check_mark: |
| | | |
|[MCRYPT TDES ECB](mcrypt_tdes.md) | ECB mode | :white_check_mark: | :white_check_mark: |
|[MCRYPT TDES CBC](mcrypt_tdes.md) | CBC mode | :white_check_mark: | :white_check_mark: |
|[MCRYPT TDES CTR](mcrypt_tdes.md) | CTR mode | :x: | :white_check_mark: |

### What additional libraries do I need to get the stuff working?

In PHP >= 7.x the OpenSSL library is the default cryptography engine, for support of Rijndael-128 CTR mode, Rijndael-256 (all modes), DES CTR mode and TDES CTR mode you need phpseclib (I'm using version 3). Get the library here: [https://github.com/phpseclib/phpseclib](https://github.com/phpseclib/phpseclib/), the documentation with example codes is here: [https://phpseclib.com/](https://phpseclib.com/).

Last update: Apr. 06th 2021

Back to the main page: [readme.md](../readme.md)
