<?php

include 'vendor/autoload.php';

use phpseclib3\Crypt\DES;
use phpseclib3\Crypt\TripleDES;

function getFixedIv8Byte()
{ // ### security warning - never use this in production ###
    return "\x00\x00\x00\x00\x00\x00\x00\x00";}
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

function phpseclib_des_ecb_zeroPadding($key, $plaintext) {
    $cipher = new DES('ecb');
    $cipher->setKey($key);
    $cipher->disablePadding();
    $ciphertext = $cipher->encrypt(zeroPadding($plaintext,8));
    $name = 'DES ECB Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_des_ecb_pkcs7Padding($key, $plaintext) {
    $cipher = new DES('ecb');
    $cipher->setKey($key);
    $ciphertext = $cipher->encrypt($plaintext);
    $name = 'DES ECB Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_des_cbc_zeroPadding($key, $plaintext) {
    $cipher = new DES('cbc');
    $cipher->setIV(getFixedIv8Byte());
    $cipher->setKey($key);
    $cipher->disablePadding();
    $ciphertext = $cipher->encrypt(zeroPadding($plaintext,8));
    $name = 'DES CBC Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_des_cbc_pkcs7Padding($key, $plaintext) {
    $cipher = new DES('cbc');
    $cipher->setIV(getFixedIv8Byte());
    $cipher->setKey($key);
    $ciphertext = $cipher->encrypt($plaintext);
    $name = 'DES CBC Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_des_ctr_noPadding($key, $plaintext) {
    $cipher = new DES('ctr');
    $cipher->setIV(getFixedIv8Byte());
    $cipher->setKey($key);
    // does not need an additional padding
    $ciphertext = $cipher->encrypt($plaintext);
    $name = 'DES CTR Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_tdes_ecb_zeroPadding($key, $plaintext) {
    $cipher = new TripleDES('ecb');
    $cipher->setKey($key);
    $cipher->disablePadding();
    $ciphertext = $cipher->encrypt(zeroPadding($plaintext,8));
    $name = 'TDES ECB Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_tdes_ecb_pkcs7Padding($key, $plaintext) {
    $cipher = new TripleDES('ecb');
    $cipher->setKey($key);
    $ciphertext = $cipher->encrypt($plaintext);
    $name = 'TDES ECB Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_tdes_cbc_zeroPadding($key, $plaintext) {
    $cipher = new TripleDES('cbc');
    $cipher->setIV(getFixedIv8Byte());
    $cipher->setKey($key);
    $cipher->disablePadding();
    $ciphertext = $cipher->encrypt(zeroPadding($plaintext,8));
    $name = 'TDES CBC Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_tdes_cbc_pkcs7Padding($key, $plaintext) {
    $cipher = new TripleDES('cbc');
    $cipher->setIV(getFixedIv8Byte());
    $cipher->setKey($key);
    $ciphertext = $cipher->encrypt($plaintext);
    $name = 'TDES CBC Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function phpseclib_tdes_ctr_noPadding($key, $plaintext) {
    $cipher = new TripleDES('ctr');
    $cipher->setIV(getFixedIv8Byte());
    $cipher->setKey($key);
    // does not need an additional padding
    $ciphertext = $cipher->encrypt($plaintext);
    $name = 'TDES CTR Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = $cipher->decrypt($ciphertext);
    printStringData(('Dec ' . $name), $decryptedtext);
}

echo 'PHPSECLIB example program for phpseclib 3' . PHP_EOL;
echo 'This program encrypts and decrypts strings of 15/16/17/31/32/33 bytes length.' . PHP_EOL;
echo 'The encryption algorithms are DES and TripleDES (TDES)' . PHP_EOL;
echo 'The tested key lengths are 8 (DES) and 24 (TDES) bytes, modes are ECB/CBC/CTR' . PHP_EOL;
echo 'All tests are done with ZERO PADDING and PKCS7 padding (PHPSECLIB default)' . PHP_EOL;
echo 'except CTR mode that has no padding' . PHP_EOL;

$keys = array("12345678abcdefgh", "12345678abcdefgh12345678", "12345678abcdefgh12345678abcdefgh"); // 16/24/32 byte
$plaintexts = array("123456789012345", "1234567890123456", "12345678901234567",
    "1234567890123456789012345678901", "12345678901234567890123456789012", "123456789012345678901234567890123");

echo PHP_EOL . 'PHPSECLIB DES ECB encryption with 8 key and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
$key = "12345678";
foreach ($plaintexts as $plaintext) {
    phpseclib_des_ecb_zeroPadding($key, $plaintext);
}

echo PHP_EOL . 'PHPSECLIB DES ECB encryption with 8 key and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
$key = "12345678";
foreach ($plaintexts as $plaintext) {
    phpseclib_des_ecb_pkcs7Padding($key, $plaintext);
}

echo PHP_EOL . 'PHPSECLIB DES CBC encryption with 8 key and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
$key = "12345678";
foreach ($plaintexts as $plaintext) {
    phpseclib_des_cbc_zeroPadding($key, $plaintext);
}

echo PHP_EOL . 'PHPSECLIB DES CBC encryption with 8 key and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
$key = "12345678";
foreach ($plaintexts as $plaintext) {
    phpseclib_des_cbc_pkcs7Padding($key, $plaintext);
}

echo PHP_EOL . 'PHPSECLIB DES CTR encryption with 8 key and 15/16/17/31/32/33 plaintext no padding' . PHP_EOL;
$key = "12345678";
foreach ($plaintexts as $plaintext) {
    phpseclib_des_ctr_noPadding($key, $plaintext);
}

echo PHP_EOL . 'PHPSECLIB TDES ECB encryption with 8 key and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
$key = "123456789012345678901234";;
foreach ($plaintexts as $plaintext) {
    phpseclib_tdes_ecb_zeroPadding($key, $plaintext);
}

echo PHP_EOL . 'PHPSECLIB TDES ECB encryption with 8 key and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
$key = "123456789012345678901234";
foreach ($plaintexts as $plaintext) {
    phpseclib_tdes_ecb_pkcs7Padding($key, $plaintext);
}

echo PHP_EOL . 'PHPSECLIB TDES CBC encryption with 8 key and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
$key = "123456789012345678901234";
foreach ($plaintexts as $plaintext) {
    phpseclib_tdes_cbc_zeroPadding($key, $plaintext);
}

echo PHP_EOL . 'PHPSECLIB TDES CBC encryption with 8 key and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
$key = "123456789012345678901234";
foreach ($plaintexts as $plaintext) {
    phpseclib_tdes_cbc_pkcs7Padding($key, $plaintext);
}

echo PHP_EOL . 'PHPSECLIB TDES CTR encryption with 8 key and 15/16/17/31/32/33 plaintext no padding' . PHP_EOL;
$key = "123456789012345678901234";
foreach ($plaintexts as $plaintext) {
    phpseclib_tdes_ctr_noPadding($key, $plaintext);
}
?>