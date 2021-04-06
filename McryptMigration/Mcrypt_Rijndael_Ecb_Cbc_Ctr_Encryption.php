<?php
function getFixedIv16Byte()
{ // ### security warning - never use this in production ###
    return "\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00";}
function getFixedIv32Byte()
{ // ### security warning - never use this in production ###
    return "\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00";}
function base64Encoding($input){return base64_encode($input);}
function base64Decoding($input){return base64_decode($input);}
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

function mcrypt_rijndael128_ecb_zeroPadding($key, $plaintext) {
    $ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key, $plaintext, MCRYPT_MODE_ECB);
    $name = 'R128 ECB Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key, $ciphertext, MCRYPT_MODE_ECB);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function mcrypt_rijndael128_ecb_pkcs7Padding($key, $plaintext) {
    $ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key, pkcs7Padding($plaintext, 16), MCRYPT_MODE_ECB);
    $name = 'R128 ECB Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key, $ciphertext, MCRYPT_MODE_ECB);
    printStringData(('Dec ' . $name), pkcs7Unpadding($decryptedtext, 16));
}

function mcrypt_rijndael256_ecb_zeroPadding($key, $plaintext) {
    $ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_256, $key, $plaintext, MCRYPT_MODE_ECB);
    $name = 'R256 ECB Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key, $ciphertext, MCRYPT_MODE_ECB);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function mcrypt_rijndael256_ecb_pkcs7Padding($key, $plaintext) {
    $ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_256, $key, pkcs7Padding($plaintext, 32), MCRYPT_MODE_ECB);
    $name = 'R256 ECB Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key, $ciphertext, MCRYPT_MODE_ECB);
    printStringData(('Dec ' . $name), pkcs7Unpadding($decryptedtext, 32));
}

function mcrypt_rijndael128_cbc_zeroPadding($key, $plaintext) {
    $ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key, $plaintext, MCRYPT_MODE_CBC, getFixedIv16Byte());
    $name = 'R128 CBC Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key, $ciphertext, MCRYPT_MODE_CBC, getFixedIv16Byte());
    printStringData(('Dec ' . $name), $decryptedtext);
}

function mcrypt_rijndael128_cbc_pkcs7Padding($key, $plaintext) {
    $ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key, pkcs7Padding($plaintext, 16), MCRYPT_MODE_CBC, getFixedIv16Byte());
    $name = 'R128 CBC Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key, $ciphertext, MCRYPT_MODE_CBC, getFixedIv16Byte());
    printStringData(('Dec ' . $name), pkcs7Unpadding($decryptedtext, 16));
}

function mcrypt_rijndael256_cbc_zeroPadding($key, $plaintext) {
    $ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_256, $key, $plaintext, MCRYPT_MODE_CBC, getFixedIv32Byte());
    $name = 'R256 CBC Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key, $ciphertext, MCRYPT_MODE_CBC, getFixedIv32Byte());
    printStringData(('Dec ' . $name), $decryptedtext);
}

function mcrypt_rijndael256_cbc_pkcs7Padding($key, $plaintext) {
    $ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_256, $key, pkcs7Padding($plaintext, 32),MCRYPT_MODE_CBC, getFixedIv32Byte());
    $name = 'R256 CBC Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key, $ciphertext, MCRYPT_MODE_CBC, getFixedIv32Byte());
    printStringData(('Dec ' . $name), pkcs7Unpadding($decryptedtext, 32));
}

function mcrypt_rijndael128_ctr_noPadding($key, $plaintext) {
    $ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key, $plaintext, "ctr", getFixedIv16Byte());
    $name = 'R128 CTR Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key, $ciphertext, "ctr", getFixedIv16Byte());
    printStringData(('Dec ' . $name), $decryptedtext);
}

function mcrypt_rijndael256_ctr_noPadding($key, $plaintext) {
    $ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_256, $key, $plaintext, "ctr", getFixedIv32Byte());
    $name = 'R256 CTR Key ' . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key, $ciphertext, "ctr", getFixedIv32Byte());
    printStringData(('Dec ' . $name), $decryptedtext);
}

echo 'MCRYPT example program for PHP up to 5.6.x' . PHP_EOL;
echo 'This program encrypts and decrypts strings of 15/16/17/31/32/33 bytes length.' . PHP_EOL;
echo 'The encryption algorithms are Rijndael-128 = AES and Rijndael-256' . PHP_EOL;
echo 'The tested key lengths are 16/24/32 bytes, modes are ECB/CBC/CTR' . PHP_EOL;
echo 'All tests are done with ZERO PADDING (MCRYPT default) and PKCS7 padding' . PHP_EOL;
echo 'except CTR mode that has no padding' . PHP_EOL;

$keys = array("12345678abcdefgh", "12345678abcdefgh12345678", "12345678abcdefgh12345678abcdefgh"); // 16/24/32 byte
$plaintexts = array("123456789012345", "1234567890123456", "12345678901234567",
    "1234567890123456789012345678901", "12345678901234567890123456789012", "123456789012345678901234567890123");

echo PHP_EOL . 'Rijndael 128 ECB encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        mcrypt_rijndael128_ecb_zeroPadding($key, $plaintext);
    }
}

echo PHP_EOL . 'Rijndael 128 ECB encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        mcrypt_rijndael128_ecb_pkcs7Padding($key, $plaintext);
    }
}

echo PHP_EOL . 'Rijndael 128 CBC encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        mcrypt_rijndael128_cbc_zeroPadding($key, $plaintext);
    }
}

echo PHP_EOL . 'Rijndael 128 CBC encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        mcrypt_rijndael128_cbc_pkcs7Padding($key, $plaintext);
    }
}

echo PHP_EOL . 'Rijndael 128 CTR encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext no padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        mcrypt_rijndael128_ctr_noPadding($key, $plaintext);
    }
}

echo PHP_EOL . 'Rijndael 256 ECB encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        mcrypt_rijndael256_ecb_zeroPadding($key, $plaintext);
    }
}

echo PHP_EOL . 'Rijndael 256 ECB encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        mcrypt_rijndael256_ecb_pkcs7Padding($key, $plaintext);
    }
}

echo PHP_EOL . 'Rijndael 256 CBC encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        mcrypt_rijndael256_cbc_zeroPadding($key, $plaintext);
    }
}

echo PHP_EOL . 'Rijndael 256 CBC encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        mcrypt_rijndael256_cbc_pkcs7Padding($key, $plaintext);
    }
}

echo PHP_EOL . 'Rijndael 256 CTR encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext no padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        mcrypt_rijndael256_ctr_noPadding($key, $plaintext);
    }
}
?>