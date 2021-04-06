# Cross-platform cryptography

## MCRYPT migration of Rijndael-128

This page informs you about the migration of **Rijndael-128** (a.k.a. "AES") in **ECB, CBC and CTR mode** from **MCRYPT** to **OpenSSL** or **phpseclib**. 

Rijndael-128 has a block length of 128 bit (= 16 byte) and the key length has to be exact 16, 24 or 32 bytes. The ECB mode does not require an initialization vector (IV) but CBC and CTR mode do.

I'm supporting the two padding modes **zero padding** (MCRYPT default) and **PKCS#7 padding** (OpenSSL and phpseclib default).

### Codes for ECB mode ZERO PADDING

MCRYPT code for Rijndael-128 ECB mode zero padding:

```plaintext
encryption:
$ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key, $plaintext, MCRYPT_MODE_ECB);
decryption:
$decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key, $ciphertext, MCRYPT_MODE_ECB);
```

OpenSSL code for Rijndael-128 ECB mode zero padding:

```plaintext
function zeroPadding($data, $size) {
    $oversize = strlen($data) % $size;
    return $oversize == 0 ? $data : ($data . str_repeat("\0", $size - $oversize));
}
encryption:
$algorithmName = "aes-128-ecb"; // or aes-192-ecb or aes-256-ecb
$ciphertext = openssl_encrypt(zeroPadding($plaintext,16), $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING);
decryption:
$decryptedtext = rtrim(openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING));
```

phpseclib 3 code for Rijndael-128 ECB mode zero padding:

```plaintext
function zeroPadding($data, $size) {
    $oversize = strlen($data) % $size;
    return $oversize == 0 ? $data : ($data . str_repeat("\0", $size - $oversize));
}
encryption:
$cipher = new AES('ecb');
$cipher->setKey($key);
$cipher->disablePadding();
$ciphertext = $cipher->encrypt(zeroPadding($plaintext,16));
decryption:
$decryptedtext = $cipher->decrypt($ciphertext);
$decryptedtext = rtrim($decryptedtext);
```

### Codes for ECB mode PKCS#7 PADDING

MCRYPT code for Rijndael-128 ECB mode PKCS#7 padding:

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
encryption:
$ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key, $plaintext, MCRYPT_MODE_ECB);
decryption:
$decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key, $ciphertext, MCRYPT_MODE_ECB);
```

OpenSSL code for Rijndael-128 ECB mode PKCS#7 padding:

```plaintext
encryption:
$algorithmName = "aes-128-ecb"; // or aes-192-ecb or aes-256-ecb
$ciphertext = openssl_encrypt($plaintext, $algorithmName, $key, OPENSSL_RAW_DATA);
decryption:
$decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA);
```

phpseclib 3 code for Rijndael-128 ECB mode PKCS#7 padding:

```plaintext
encryption:
$cipher = new AES('ecb');
$cipher->setKey($key);
$ciphertext = $cipher->encrypt($plaintext);
$decryptedtext = $cipher->decrypt($ciphertext);
decryption:
$decryptedtext = $cipher->decrypt($ciphertext);
```

### Codes for CBC mode, zero padding and PKCS#7 padding

The following code lines are a substitute for the ECB mode code lines, the code for additional padding is not shown (see ECB mode):

MCRYPT code for Rijndael-128 CBC mode zero padding and PKCS#7 padding:

```plaintext
zero padding:
$ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key, $plaintext, MCRYPT_MODE_CBC, iv);
$decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key, $ciphertext, MCRYPT_MODE_CBC, iv);

PKCS#7 padding:
$ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key, pkcs7Padding($plaintext, 16), MCRYPT_MODE_CBC, iv);
$decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key, $ciphertext, MCRYPT_MODE_CBC, iv);
```

OpenSSL code for Rijndael-128 CBC mode zero padding and PKCS#7 padding:

```plaintext
zero padding:
$algorithmName = "aes-128-cbc"; // or aes-192-cbc or aes-256-cbc
$ciphertext = openssl_encrypt(zeroPadding($plaintext,16), $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING, iv);
$decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING, iv);

PKCS#7 padding:
$algorithmName = "aes-128-cbc"; // or aes-192-cbc or aes-256-cbc
$ciphertext = openssl_encrypt($plaintext, $algorithmName, $key, OPENSSL_RAW_DATA, iv);
$decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA, iv);
```

phpseclib 3 code for Rijndael-128 CBC mode zero padding and PKCS#7 padding:

```plaintext
zero padding:
$cipher = new AES('cbc');
$cipher->setIV(iv);
$cipher->setKey($key);
$cipher->disablePadding();
$ciphertext = $cipher->encrypt(zeroPadding($plaintext,16));
$decryptedtext = $cipher->decrypt($ciphertext);

PKCS#7 padding:
$cipher = new AES('cbc');
$cipher->setIV(iv);
$cipher->setKey($key);
$ciphertext = $cipher->encrypt($plaintext);
$decryptedtext = $cipher->decrypt($ciphertext);
```

### Codes for CTR mode no padding

The following code lines are a substitute for the ECB mode code lines, the CTR mode does not require any padding. Kindly note that the **CTR mode is not available in OpenSSL**.

MCRYPT code for Rijndael-128 CTR mode no padding:

```plaintext
$ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key, $plaintext, "ctr", iv);
$decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key, $ciphertext, "ctr", iv);
```

phpseclib code for Rijndael-128 CTR mode no padding:

```plaintext
$cipher = new AES('ctr');
$cipher->setIV(iv);
$cipher->setKey($key);
$ciphertext = $cipher->encrypt($plaintext);
$decryptedtext = $cipher->decrypt($ciphertext);
```

### Do you provide example data?

Yes - for each algorithm, possible key length, mode and padding I run the encryption and decryption with data of different lengths.

I do not provide separate programs for the different modes and paddings but one file that runs all tests. The examples run Rijndael-128 and [Rijndael-256](mcrypt_rijndael256.md) codes.

**Security warning: the codes are using fixed keys and fixed initialization vectors - this is done for comparison reasons. Never ever do that in your codes - you have to use randomly generated initialization vectors.**

| Solution | modes | codes | results | online compiler | 
| ------ | :------: | :--: | :--: | :--: |
| MCRYPT Rijndael-128 | ECB, CBC and CTR | [Mcrypt_Rijndael_Ecb_Cbc_Ctr_Encryption.php](../McryptMigration/Mcrypt_Rijndael_Ecb_Cbc_Ctr_Encryption.php) | [MCRYPT results](../McryptMigration/Mcrypt_Rijndael_Ecb_Cbc_Ctr_Encryption.txt) | [Sandbox MCRYPT  Rijndael](http://sandbox.onlinephpfunctions.com/code/71b1ec629897965ecac805a3eb86c8613607bf12/) |
| OpenSSL Rijndael-128 | ECB and CBC | [Openssl_Rijndael_Ecb_Cbc_Encryption.php](../McryptMigration/Openssl_Rijndael_Ecb_Cbc_Encryption.php) | [OpenSSL results](../McryptMigration/Openssl_Rijndael_Ecb_Cbc_Encryption.txt) | [Replit OpenSSL  Rijndael](https://replit.com/@javacrypto/McryptMigrationRijndael#main.php/) |
| phpseclic 3 Rijndael-128 | ECB, CBC and CTR | [Phpseclib_Rijndael_Ecb_Cbc_Ctr_Encryption.php](../McryptMigration/Phpseclib_Rijndael_Ecb_Cbc_Ctr_Encryption.php) | [phpseclib results](../McryptMigration/Phpseclib_Rijndael_Ecb_Cbc_Ctr_Encryption.txt) | [Replit phpseclib  Rijndael](https://replit.com/@javacrypto/McryptMigrationPhpseclibRijndael#main.php/) |

### What additional libraries do I need to get the stuff working?

In PHP >= 7.x the OpenSSL library is the default cryptography engine, for support of Rijndael-128 CTR mode, Rijndael-256 (all modes), DES CTR mode and TDES CTR mode you need phpseclib (I'm using version 3). Get the library here: [https://github.com/phpseclib/phpseclib](https://github.com/phpseclib/phpseclib/), the documentation with example codes is here: [https://phpseclib.com/](https://phpseclib.com/).

Last update: Apr. 06th 2021

Back to the [MCRYPT migration overview page](mcrypt_overview.md) or the main page: [readme.md](../readme.md)
