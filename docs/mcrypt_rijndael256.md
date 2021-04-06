# Cross-platform cryptography

## MCRYPT migration of Rijndael-256

This page informs you about the migration of **Rijndael-256** in **ECB, CBC and CTR mode** from **MCRYPT** to **phpseclib**. Unfortunately **OpenSSL doesn't support Rijndael-256**.

Rijndael-256 has a block length of 256 bit (= 32 byte) and the key length has to be exact 16, 24 or 32 bytes. The ECB mode does not require an initialization vector (IV) but CBC and CTR mode do.

If you need a MCRYPT migration using Rijndael-128 visit the page [MCRYPT migration of Rijndael-128](mcrypt_rijndael128.md).

I'm supporting the two padding modes **zero padding** (MCRYPT default) and **PKCS#7 padding** (phpseclib default).

### Codes for ECB mode ZERO PADDING

###########################


MCRYPT code for Rijndael-256 ECB mode zero padding:

```plaintext
encryption:
$ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_256, $key, $plaintext, MCRYPT_MODE_ECB);
decryption:
$decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key, $ciphertext, MCRYPT_MODE_ECB);
```

phpseclib 3 code for Rijndael-256 ECB mode zero padding:

```plaintext
function zeroPadding($data, $size) {
    $oversize = strlen($data) % $size;
    return $oversize == 0 ? $data : ($data . str_repeat("\0", $size - $oversize));
}
encryption:
$cipher = new \phpseclib3\Crypt\Rijndael('ecb');
$cipher->setKey($key);
$cipher->setBlockLength(256);
$cipher->disablePadding();
$ciphertext = $cipher->encrypt(zeroPadding($plaintext,32));
decryption:
$decryptedtext = $cipher->decrypt($ciphertext);
$decryptedtext = rtrim($decryptedtext);
```

### Codes for ECB mode PKCS#7 PADDING

MCRYPT code for Rijndael-256 ECB mode PKCS#7 padding:

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
$ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_256, $key, pkcs7Padding($plaintext), MCRYPT_MODE_ECB);
decryption:
$decryptedtext = pkcs7Unpadding(mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key, $ciphertext, MCRYPT_MODE_ECB));
```

phpseclib 3 code for Rijndael-256 ECB mode PKCS#7 padding:

```plaintext
encryption:
$cipher = new \phpseclib3\Crypt\Rijndael('ecb');
$cipher->setKey($key);
$cipher->setBlockLength(256);
$ciphertext = $cipher->encrypt($plaintext);
decryption:
$decryptedtext = $cipher->decrypt($ciphertext);
$decryptedtext = rtrim($decryptedtext);
```

### Codes for CBC mode, zero padding and PKCS#7 padding

The following code lines are a substitute for the ECB mode code lines, the code for additional padding is not shown (see ECB mode):

MCRYPT code for Rijndael-256 CBC mode zero padding and PKCS#7 padding:

```plaintext
zero padding:
$ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_256, $key, $plaintext, MCRYPT_MODE_CBC, iv);
$decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key, $ciphertext, MCRYPT_MODE_CBC, iv);

PKCS#7 padding:
$ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_256, $key, pkcs7Padding($plaintext, 32), MCRYPT_MODE_CBC, iv);
$decryptedtext = pkcs7Unpadding(mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key, $ciphertext, MCRYPT_MODE_CBC, iv));
```

phpseclib 3 code for Rijndael-256 CBC mode zero padding and PKCS#7 padding:

```plaintext
zero padding:
$cipher = new \phpseclib3\Crypt\Rijndael('cbc');
$cipher->setKey($key);
$cipher->setBlockLength(256);
$cipher->setIV(iv);
$cipher->disablePadding();
$ciphertext = $cipher->encrypt(zeroPadding($plaintext,32));
$decryptedtext = $cipher->decrypt($ciphertext);
$decryptedtext = rtrim($decryptedtext);

PKCS#7 padding:
$cipher = new \phpseclib3\Crypt\Rijndael('cbc');
$cipher->setKey($key);
$cipher->setBlockLength(256);
$cipher->setIV(iv);
$ciphertext = $cipher->encrypt($plaintext);
$decryptedtext = $cipher->decrypt($ciphertext);
```

### Codes for CTR mode no padding

The following code lines are a substitute for the ECB mode code lines, the CTR mode does not require any padding. Kindly note that the **CTR mode is not available in OpenSSL**.

MCRYPT code for Rijndael-256 CTR mode no padding:

```plaintext
$ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_256, $key, $plaintext, "ctr", iv);
$decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key, $ciphertext, "ctr", iv);
```

phpseclib code for Rijndael-256 CTR mode no padding:

```plaintext
$cipher = new \phpseclib3\Crypt\Rijndael('ctr');
$cipher->setKey($key);
$cipher->setBlockLength(256);
$cipher->setIV(iv);
$ciphertext = $cipher->encrypt($plaintext);
$decryptedtext = $cipher->decrypt($ciphertext);
```

### Do you provide example data?

Yes - for each algorithm, possible key length, mode and padding I run the encryption and decryption with data of different lengths.

I do not provide separate programs for the different modes and paddings but one file that runs all tests. The examples run [Rijndael-128](mcrypt_rijndael128.md) and Rijndael-256 codes.

**Security warning: the codes are using fixed keys and fixed initialization vectors - this is done for comparison reasons. Never ever do that in your codes - you have to use randomly generated initialization vectors.**

| Solution | modes | codes | results | online compiler | 
| ------ | :------: | :--: | :--: | :--: |
| MCRYPT Rijndael-256 | ECB, CBC and CTR | [Mcrypt_Rijndael_Ecb_Cbc_Ctr_Encryption.php](../McryptMigration/Mcrypt_Rijndael_Ecb_Cbc_Ctr_Encryption.php) | [MCRYPT results](../McryptMigration/Mcrypt_Rijndael_Ecb_Cbc_Ctr_Encryption.txt) | [Sandbox MCRYPT  Rijndael](http://sandbox.onlinephpfunctions.com/code/71b1ec629897965ecac805a3eb86c8613607bf12/) |
| OpenSSL Rijndael-256 | ECB and CBC | [Openssl_Rijndael_Ecb_Cbc_Encryption.php](../McryptMigration/Openssl_Rijndael_Ecb_Cbc_Encryption.php) | [OpenSSL results](../McryptMigration/Openssl_Rijndael_Ecb_Cbc_Encryption.txt) | [Replit OpenSSL  Rijndael](https://replit.com/@javacrypto/McryptMigrationRijndael#main.php/) |
| phpseclic 3 Rijndael-256 | ECB, CBC and CTR | [Phpseclib_Rijndael_Ecb_Cbc_Ctr_Encryption.php](../McryptMigration/Phpseclib_Rijndael_Ecb_Cbc_Ctr_Encryption.php) | [phpseclib results](../McryptMigration/Phpseclib_Rijndael_Ecb_Cbc_Ctr_Encryption.txt) | [Replit phpseclib  Rijndael](https://replit.com/@javacrypto/McryptMigrationPhpseclibRijndael#main.php/) |

### What additional libraries do I need to get the stuff working?

In PHP >= 7.x the OpenSSL library is the default cryptography engine, for support of Rijndael-128 CTR mode, Rijndael-256 (all modes), DES CTR mode and TDES CTR mode you need phpseclib (I'm using version 3). Get the library here: [https://github.com/phpseclib/phpseclib](https://github.com/phpseclib/phpseclib/), the documentation with example codes is here: [https://phpseclib.com/](https://phpseclib.com/).

Last update: Apr. 06th 2021

Back to the [MCRYPT migration overview page](mcrypt_overview.md) or the main page: [readme.md](../readme.md)
