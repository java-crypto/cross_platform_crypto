<?php

include 'vendor/autoload.php';

use phpseclib3\Crypt\AES;

function getFixedIv16Byte()
{ // ### security warning - never use this in production ###
    return "\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00";}
function getFixedIv32Byte()
{ // ### security warning - never use this in production ###
    return "\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00";}
function base64Encoding($input){return base64_encode($input);}
function base64Decoding($input){return base64_decode($input);}
function printData($name, $data) {
    echo $name . ' length: ' . strlen($data)
        . ' hex: ' . bin2hex($data)
        . ' base64: '. base64Encoding($data)
        . PHP_EOL;
}
function printStringData($name, $data) {
    echo $name . ' length: ' . strlen($data)
        . ' hex: ' . bin2hex($data)
        . ' string: ' . $data . '#'
        . PHP_EOL;
    $data = rtrim($data); // needed for strip off the x00s at the end
    echo $name . ' length: ' . strlen($data)
        . ' hex: ' . bin2hex($data)
        . ' string: ' . $data . '#'
        . PHP_EOL;
}
function zeroPadding($data, $size) {
    $oversize = strlen($data) % $size;
    return $oversize == 0 ? $data : ($data . str_repeat("\0", $size - $oversize));
}

function phpseclib_aes_ecb_zeroPadding($key, $plaintext) {
    $cipher = new AES('ecb');
    $cipher->setKey($key);
    $cipher->disablePadding();
    $ciphertext = $cipher->encrypt(zeroPadding($plaintext,16));
    $name = 'R128 ECB Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_aes_ecb_pkcs7Padding($key, $plaintext) {
    $cipher = new AES('ecb');
    $cipher->setKey($key);
    $ciphertext = $cipher->encrypt($plaintext);
    $name = 'R128 ECB Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_aes_cbc_zeroPadding($key, $plaintext) {
    $cipher = new AES('cbc');
    $cipher->setIV(getFixedIv16Byte());
    $cipher->setKey($key);
    $cipher->disablePadding();
    $ciphertext = $cipher->encrypt(zeroPadding($plaintext,16));
    $name = 'R128 CBC Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_aes_cbc_pkcs7Padding($key, $plaintext) {
    $cipher = new AES('cbc');
    $cipher->setIV(getFixedIv16Byte());
    $cipher->setKey($key);
    $ciphertext = $cipher->encrypt($plaintext);
    $name = 'R128 CBC Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_aes_ctr_noPadding($key, $plaintext) {
    $cipher = new AES('ctr');
    $cipher->setIV(getFixedIv16Byte());
    $cipher->setKey($key);
    //$cipher->disablePadding();
    // does not need an additional padding
    $ciphertext = $cipher->encrypt($plaintext);
    $name = 'R128 CTR Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_rijndael256_ecb_zeroPadding($key, $plaintext) {
    $cipher = new \phpseclib3\Crypt\Rijndael('ecb');
    $cipher->setKey($key);
    $cipher->setBlockLength(256);
    $cipher->disablePadding();
    $ciphertext = $cipher->encrypt(zeroPadding($plaintext,32));
    $name = 'R256 ECB Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_rijndael256_ecb_pkcs7Padding($key, $plaintext) {
    $cipher = new \phpseclib3\Crypt\Rijndael('ecb');
    $cipher->setKey($key);
    $cipher->setBlockLength(256);
    $ciphertext = $cipher->encrypt($plaintext);
    $name = 'R256 ECB Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_rijndael256_cbc_zeroPadding($key, $plaintext) {
    $cipher = new \phpseclib3\Crypt\Rijndael('cbc');
    $cipher->setKey($key);
    $cipher->setBlockLength(256);
    $cipher->setIV(getFixedIv32Byte());
    $cipher->disablePadding();
    $ciphertext = $cipher->encrypt(zeroPadding($plaintext,32));
    $name = 'R256 CBC Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_rijndael256_cbc_pkcs7Padding($key, $plaintext) {
    $cipher = new \phpseclib3\Crypt\Rijndael('cbc');
    $cipher->setKey($key);
    $cipher->setBlockLength(256);
    $cipher->setIV(getFixedIv32Byte());
    $ciphertext = $cipher->encrypt($plaintext);
    $name = 'R256 CBC Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_rijndael256_ctr_noPadding($key, $plaintext) {
    $cipher = new \phpseclib3\Crypt\Rijndael('ctr');
    $cipher->setKey($key);
    $cipher->setBlockLength(256);
    $cipher->setIV(getFixedIv32Byte());
    //$cipher->disablePadding();
    // does not need an additional padding
    $ciphertext = $cipher->encrypt($plaintext);
    $name = 'R256 CTR Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

echo 'PHPSECLIB example program for phpseclib 3' . PHP_EOL;
echo 'This program encrypts and decrypts strings of 15/16/17/31/32/33 bytes length.' . PHP_EOL;
echo 'The encryption algorithms are Rijndael-128 = AES and Rijndael-256' . PHP_EOL;
echo 'The tested key lengths are 16/24/32 bytes, modes are ECB/CBC/CTR' . PHP_EOL;
echo 'All tests are done with ZERO PADDING and PKCS7 padding (PHPSECLIB default)' . PHP_EOL;
echo 'except CTR mode that has no padding' . PHP_EOL;

$keys = array("12345678abcdefgh", "12345678abcdefgh12345678", "12345678abcdefgh12345678abcdefgh"); // 16/24/32 byte
$plaintexts = array("123456789012345", "1234567890123456", "12345678901234567",
    "1234567890123456789012345678901", "12345678901234567890123456789012", "123456789012345678901234567890123");

echo PHP_EOL . 'PHPSECLIB AES ECB encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        phpseclib_aes_ecb_zeroPadding($key, $plaintext);
    }
}

echo PHP_EOL . 'PHPSECLIB AES ECB encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        phpseclib_aes_ecb_pkcs7Padding($key, $plaintext);
    }
}

echo PHP_EOL . 'PHPSECLIB AES CBC encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        phpseclib_aes_cbc_zeroPadding($key, $plaintext);
    }
}

echo PHP_EOL . 'PHPSECLIB AES CBC encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        phpseclib_aes_cbc_pkcs7Padding($key, $plaintext);
    }
}

echo PHP_EOL . 'PHPSECLIB AES CTR encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext no padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        phpseclib_aes_ctr_noPadding($key, $plaintext);
    }
}

echo PHP_EOL . 'PHPSECLIB Rijndael-256 ECB encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        phpseclib_rijndael256_ecb_zeroPadding($key, $plaintext);
    }
}

echo PHP_EOL . 'PHPSECLIB Rijndael-256 ECB encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        phpseclib_rijndael256_ecb_pkcs7Padding($key, $plaintext);
    }
}

echo PHP_EOL . 'PHPSECLIB Rijndael-256 CBC encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        phpseclib_rijndael256_cbc_zeroPadding($key, $plaintext);
    }
}

echo PHP_EOL . 'PHPSECLIB Rijndael-256 CBC encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        phpseclib_rijndael256_cbc_pkcs7Padding($key, $plaintext);
    }
}

echo PHP_EOL . 'PHPSECLIB Rijndael-256 CTR encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext no padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        phpseclib_rijndael256_ctr_noPadding($key, $plaintext);
    }
}
?>