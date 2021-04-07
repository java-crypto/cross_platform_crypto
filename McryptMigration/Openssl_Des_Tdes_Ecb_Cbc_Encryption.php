<?php
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

function openssl_des_ecb_zeroPadding($key, $plaintext) {
    $algorithmName = "des-ecb";
    $desName = "DES ECB Key ";
    $ciphertext = openssl_encrypt(zeroPadding($plaintext,8), $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING);
    $name = $desName . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function openssl_des_ecb_pkcs7Padding($key, $plaintext) {
    $algorithmName = "des-ecb";
    $desName = "DES ECB Key ";
    $ciphertext = openssl_encrypt($plaintext, $algorithmName, $key, OPENSSL_RAW_DATA);
    $name = $desName . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function openssl_des_cbc_zeroPadding($key, $plaintext) {
    $algorithmName = "des-cbc";
    $desName = "DES CBC Key ";
    $ciphertext = openssl_encrypt(zeroPadding($plaintext,8), $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING, getFixedIv8Byte());
    $name = $desName . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING, getFixedIv8Byte());
    printStringData(('Dec ' . $name), $decryptedtext);
}

function openssl_des_cbc_pkcs7Padding($key, $plaintext) {
    $algorithmName = "des-cbc";
    $desName = "DES CBC Key ";
    $ciphertext = openssl_encrypt($plaintext, $algorithmName, $key, OPENSSL_RAW_DATA, getFixedIv8Byte());
    $name = $desName . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA, getFixedIv8Byte());
    printStringData(('Dec ' . $name), $decryptedtext);
}

function openssl_tdes_ecb_zeroPadding($key, $plaintext) {
    $algorithmName = "des-ede3-ecb";
    $desName = "TDES ECB Key ";
    $ciphertext = openssl_encrypt(zeroPadding($plaintext,8), $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING);
    $name = $desName . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function openssl_tdes_ecb_pkcs7Padding($key, $plaintext) {
    $algorithmName = "des-ede3-ecb";
    $desName = "TDES ECB Key ";
    $ciphertext = openssl_encrypt($plaintext, $algorithmName, $key, OPENSSL_RAW_DATA);
    $name = $desName . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function openssl_tdes_cbc_zeroPadding($key, $plaintext) {
    $algorithmName = "des-ede3-cbc";
    $desName = "TDES CBC Key ";
    $ciphertext = openssl_encrypt(zeroPadding($plaintext,8), $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING, getFixedIv8Byte());
    $name = $desName . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING, getFixedIv8Byte());
    printStringData(('Dec ' . $name), $decryptedtext);
}

function openssl_tdes_cbc_pkcs7Padding($key, $plaintext) {
    $algorithmName = "des-ede3-cbc";
    $desName = "TDES CBC Key ";
    $ciphertext = openssl_encrypt($plaintext, $algorithmName, $key, OPENSSL_RAW_DATA, getFixedIv8Byte());
    $name = $desName . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA, getFixedIv8Byte());
    printStringData(('Dec ' . $name), $decryptedtext);
}

echo 'OPENSSL example program for PHP from 7.0.x' . PHP_EOL;
echo 'This program encrypts and decrypts strings of 15/16/17/31/32/33 bytes length.' . PHP_EOL;
echo 'The encryption algorithms are DES and TripleDES (TDES)' . PHP_EOL;
echo 'The tested key lengths are 8 (DES) and 24 (TDES) bytes, modes are ECB/CBC' . PHP_EOL;
echo 'All tests are done with ZERO PADDING and PKCS7 padding (OpenSSL default)' . PHP_EOL;
echo 'except CTR mode that has no padding' . PHP_EOL;

$keys = array("12345678abcdefgh", "12345678abcdefgh12345678", "12345678abcdefgh12345678abcdefgh"); // 16/24/32 byte
$plaintexts = array("123456789012345", "1234567890123456", "12345678901234567",
    "1234567890123456789012345678901", "12345678901234567890123456789012", "123456789012345678901234567890123");

echo PHP_EOL . 'DES ECB encryption with 8 key and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
$key = "12345678";
foreach ($plaintexts as $plaintext) {
    openssl_des_ecb_zeroPadding($key, $plaintext);
}

echo PHP_EOL . 'DES ECB encryption with 8 key and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
$key = "12345678";
foreach ($plaintexts as $plaintext) {
    openssl_des_ecb_pkcs7Padding($key, $plaintext);
}

echo PHP_EOL . 'DES CBC encryption with 8 key and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
$key = "12345678";
foreach ($plaintexts as $plaintext) {
    openssl_des_cbc_zeroPadding($key, $plaintext);
}

echo PHP_EOL . 'DES CBC encryption with 8 key and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
$key = "12345678";
foreach ($plaintexts as $plaintext) {
    openssl_des_cbc_pkcs7Padding($key, $plaintext);
}

echo PHP_EOL . 'TDES ECB encryption with 24 key and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
$key = "123456789012345678901234";
foreach ($plaintexts as $plaintext) {
    openssl_tdes_ecb_zeroPadding($key, $plaintext);
}

echo PHP_EOL . 'TDES ECB encryption with 8 key and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
$key = "123456789012345678901234";
foreach ($plaintexts as $plaintext) {
    openssl_tdes_ecb_pkcs7Padding($key, $plaintext);
}

echo PHP_EOL . 'TDES CBC encryption with 8 key and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
$key = "123456789012345678901234";
foreach ($plaintexts as $plaintext) {
    openssl_tdes_cbc_zeroPadding($key, $plaintext);
}

echo PHP_EOL . 'TDES CBC encryption with 8 key and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
$key = "123456789012345678901234";
foreach ($plaintexts as $plaintext) {
    openssl_tdes_cbc_pkcs7Padding($key, $plaintext);
}
?>