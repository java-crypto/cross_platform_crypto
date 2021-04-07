# Cross-platform cryptography

## MCRYPT migration of DES

This page informs you about the migration of **DES** in **ECB, CBC and CTR mode** from **MCRYPT** to **OpenSSL** or **phpseclib**. 

DES has a block length of 64 bit (= 8 byte) and the key length has to be exact 8 bytes. The ECB mode does not require an initialization vector (IV) but CBC and CTR mode do.

If you need a MCRYPT migration using Triple DES visit the page [MCRYPT migration of TDES](mcrypt_tdes.md).

I'm supporting the two padding modes **zero padding** (MCRYPT default) and **PKCS#7 padding** (OpenSSL and phpseclib default).

### Codes for ECB mode ZERO PADDING

MCRYPT code for DES ECB mode zero padding:

```plaintext
encryption:
$ciphertext = mcrypt_encrypt(MCRYPT_DES, $key, $plaintext, MCRYPT_MODE_ECB);
decryption:
$decryptedtext = mcrypt_decrypt(MCRYPT_DES, $key, $ciphertext, MCRYPT_MODE_ECB);
```

OpenSSL code for DES ECB mode zero padding:

```plaintext
function zeroPadding($data, $size) {
    $oversize = strlen($data) % $size;
    return $oversize == 0 ? $data : ($data . str_repeat("\0", $size - $oversize));
}
encryption:
$algorithmName = "des-ecb";
$ciphertext = openssl_encrypt(zeroPadding($plaintext,8), $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING);
decryption:
$decryptedtext = rtrim(openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING));
```

phpseclib 3 code for DES ECB mode zero padding:

```plaintext
function zeroPadding($data, $size) {
    $oversize = strlen($data) % $size;
    return $oversize == 0 ? $data : ($data . str_repeat("\0", $size - $oversize));
}
encryption:
$cipher = new DES('ecb');
$cipher->setKey($key);
$cipher->disablePadding();
$ciphertext = $cipher->encrypt(zeroPadding($plaintext,8));
decryption:
$decryptedtext = $cipher->decrypt($ciphertext);
$decryptedtext = rtrim($decryptedtext);
```

### Codes for ECB mode PKCS#7 PADDING

MCRYPT code for DES ECB mode PKCS#7 padding:

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
$ciphertext = mcrypt_encrypt(MCRYPT_DES, $key, pkcs7Padding($plaintext, 8), MCRYPT_MODE_ECB);
decryption:
$decryptedtext = pkcs7Unpadding(mcrypt_decrypt(MCRYPT_DES, $key, $ciphertext, MCRYPT_MODE_ECB),8);
```

OpenSSL code for DES ECB mode PKCS#7 padding:

```plaintext
encryption:
$algorithmName = "des-ecb";
$ciphertext = openssl_encrypt($plaintext, $algorithmName, $key, OPENSSL_RAW_DATA);
decryption:
$decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA);
```

phpseclib 3 code for DES ECB mode PKCS#7 padding:

```plaintext
encryption:
$cipher = new DES('ecb');
$cipher->setKey($key);
$ciphertext = $cipher->encrypt($plaintext);
decryption:
$decryptedtext = $cipher->decrypt($ciphertext);
```

### Codes for CBC mode, zero padding and PKCS#7 padding

The following code lines are a substitute for the ECB mode code lines, the code for additional padding is not shown (see ECB mode):

MCRYPT code for DES CBC mode zero padding and PKCS#7 padding:

```plaintext
zero padding:
$ciphertext = mcrypt_encrypt(MCRYPT_DES, $key, $plaintext, MCRYPT_MODE_CBC, iv);
$decryptedtext = rtrim(mcrypt_decrypt(MCRYPT_DES, $key, $ciphertext, MCRYPT_MODE_CBC, iv));

PKCS#7 padding:
$ciphertext = mcrypt_encrypt(MCRYPT_DES, $key, pkcs7Padding($plaintext, 8), MCRYPT_MODE_CBC, iv);
$decryptedtext = pkcs7Unpadding(mcrypt_decrypt(MCRYPT_DES, $key, $ciphertext, MCRYPT_MODE_CBC, iv), 8);
```

OpenSSL code for DES CBC mode zero padding and PKCS#7 padding:

```plaintext
zero padding:
$algorithmName = "des-cbc";
$ciphertext = openssl_encrypt(zeroPadding($plaintext,8), $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING, iv);
$decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING, iv);
$decryptedtext = rtrim($decryptedtext);

PKCS#7 padding:
$algorithmName = "des-cbc";
$ciphertext = openssl_encrypt($plaintext, $algorithmName, $key, OPENSSL_RAW_DATA, iv);
$decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA, iv);
```

phpseclib 3 code for DES CBC mode zero padding and PKCS#7 padding:

```plaintext
zero padding:
$cipher = new DES('cbc');
$cipher->setIV(iv);
$cipher->setKey($key);
$cipher->disablePadding();
$ciphertext = $cipher->encrypt(zeroPadding($plaintext,8));
$decryptedtext = rtrim($cipher->decrypt($ciphertext));

PKCS#7 padding:
$cipher = new DES('cbc');
$cipher->setIV(iv);
$cipher->setKey($key);
$ciphertext = $cipher->encrypt($plaintext);
$decryptedtext = $cipher->decrypt($ciphertext);
```

### Codes for CTR mode no padding

The following code lines are a substitute for the ECB mode code lines, the CTR mode does not require any padding. Kindly note that the **CTR mode is not available in OpenSSL**.

MCRYPT code for DES CTR mode no padding:

```plaintext
$ciphertext = mcrypt_encrypt(MCRYPT_DES, $key, $plaintext, 'ctr', getFixedIv8Byte());
$decryptedtext = mcrypt_decrypt(MCRYPT_DES, $key, $ciphertext, 'ctr', getFixedIv8Byte());
```

phpseclib code for DES CTR mode no padding:

```plaintext
$cipher = new DES('ctr');
$cipher->setIV(iv);
$cipher->setKey($key);
$ciphertext = $cipher->encrypt($plaintext);
$decryptedtext = $cipher->decrypt($ciphertext);
```

### Do you provide example data?

Yes - for each algorithm, possible key length, mode and padding I run the encryption and decryption with data of different lengths.

I do not provide separate programs for the different modes and paddings but one file that runs all tests. The examples run DES and [Triple DES](mcrypt_tdes.md) codes.

**Security warning: the codes are using fixed keys and fixed initialization vectors - this is done for comparison reasons. Never ever do that in your codes - you have to use randomly generated initialization vectors.**

| Solution | modes | codes | results | online compiler | 
| ------ | :------: | :--: | :--: | :--: |
| MCRYPT DES | ECB, CBC and CTR | [Mcrypt_Des_Tdes_Ecb_Cbc_Ctr_Encryption.php](../McryptMigration/Mcrypt_Des_Tdes_Ecb_Cbc_Ctr_Encryption.php) | [MCRYPT results](../McryptMigration/Mcrypt_Des_Tdes_Ecb_Cbc_Ctr_Encryption.txt) | [Sandbox MCRYPT DES](http://sandbox.onlinephpfunctions.com/code/56dd1a166d30b8585783dfe92e42c12e3f3c9a83/) |
| OpenSSL DES | ECB and CBC | [Openssl_Des_Tdes_Ecb_Cbc_Encryption.php](../McryptMigration/Openssl_Des_Tdes_Ecb_Cbc_Encryption.php) | [OpenSSL results](../McryptMigration/Openssl_Des_Tdes_Ecb_Cbc_Encryption.txt) | [Replit OpenSSL DES](https://replit.com/@javacrypto/McryptMigrationOpensslDesTdes#main.php/) |
| phpseclic 3 DES | ECB, CBC and CTR | [Phpseclib_Des_Tdes_Ecb_Cbc_Ctr_Encryption.php](../McryptMigration/Phpseclib_Des_Tdes_Ecb_Cbc_Ctr_Encryption.php) | [phpseclib results](../McryptMigration/Phpseclib_Des_Tdes_Ecb_Cbc_Ctr_Encryption.txt) | [Replit phpseclib  DES](https://replit.com/@javacrypto/McryptMigrationPhpseclibDesTdes#main.php/) |

### What additional libraries do I need to get the stuff working?

In PHP >= 7.x the OpenSSL library is the default cryptography engine, for support of Rijndael-128 CTR mode, Rijndael-256 (all modes), DES CTR mode and TDES CTR mode you need phpseclib (I'm using version 3). Get the library here: [https://github.com/phpseclib/phpseclib](https://github.com/phpseclib/phpseclib/), the documentation with example codes is here: [https://phpseclib.com/](https://phpseclib.com/).

Last update: Apr. 07th 2021

Back to the [MCRYPT migration overview page](mcrypt_overview.md) or the main page: [readme.md](../readme.md)
