<?php
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

function openssl_aes_ecb_zeroPadding($key, $plaintext) {
    // choose algorithm name from key length
    $algorithmName = "";
    $aesName = "";
    switch (strlen($key)) {
        case 16:
        {
            $algorithmName = "aes-128-ecb";
            $aesName = "A128 ECB Key ";
            break;
        }
        case 24:
        {
            $algorithmName = "aes-192-ecb";
            $aesName = "A192 ECB Key ";
            break;
        }
        case 32:
        {
            $algorithmName = "aes-256-ecb";
            $aesName = "A256 ECB Key ";
            break;
        }
        default:
            {
                // not supported key length
                return '*** not supported key length ***';
            }
    }
    $ciphertext = openssl_encrypt(zeroPadding($plaintext,16), $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING);
    $name = $aesName . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING);
    printStringData(('Dec ' . $name), $decryptedtext);
}

function openssl_aes_ecb_pkcs7Padding($key, $plaintext) {
    // choose algorithm name from key length
    $algorithmName = "";
    $aesName = "";
    switch (strlen($key)) {
        case 16:
        {
            $algorithmName = "aes-128-ecb";
            $aesName = "A128 ECB Key ";
            break;
        }
        case 24:
        {
            $algorithmName = "aes-192-ecb";
            $aesName = "A192 ECB Key ";
            break;
        }
        case 32:
        {
            $algorithmName = "aes-256-ecb";
            $aesName = "A256 ECB Key ";
            break;
        }
        default:
        {
            // not supported key length
            return '*** not supported key length ***';
        }
    }
    $ciphertext = openssl_encrypt($plaintext, $algorithmName, $key, OPENSSL_RAW_DATA);
    $name = $aesName . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA);
    printStringData(('Dec ' . $name), $decryptedtext);
}


function openssl_aes_cbc_zeroPadding($key, $plaintext) {
    // choose algorithm name from key length
    $algorithmName = "";
    $aesName = "";
    switch (strlen($key)) {
        case 16:
        {
            $algorithmName = "aes-128-cbc";
            $aesName = "A128 CBC Key ";
            break;
        }
        case 24:
        {
            $algorithmName = "aes-192-cbc";
            $aesName = "A192 CBC Key ";
            break;
        }
        case 32:
        {
            $algorithmName = "aes-256-cbc";
            $aesName = "A256 CBC Key ";
            break;
        }
        default:
        {
            // not supported key length
            return '*** not supported key length ***';
        }
    }
    $ciphertext = openssl_encrypt(zeroPadding($plaintext,16), $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING, getFixedIv16Byte());
    $name = $aesName . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING, getFixedIv16Byte());
    printStringData(('Dec ' . $name), $decryptedtext);
}

function openssl_aes_cbc_pkcs7Padding($key, $plaintext) {
    // choose algorithm name from key length
    $algorithmName = "";
    $aesName = "";
    switch (strlen($key)) {
        case 16:
        {
            $algorithmName = "aes-128-cbc";
            $aesName = "A128 CBC Key ";
            break;
        }
        case 24:
        {
            $algorithmName = "aes-192-cbc";
            $aesName = "A192 CBC Key ";
            break;
        }
        case 32:
        {
            $algorithmName = "aes-256-cbc";
            $aesName = "A256 CBC Key ";
            break;
        }
        default:
        {
            // not supported key length
            return '*** not supported key length ***';
        }
    }
    $ciphertext = openssl_encrypt($plaintext, $algorithmName, $key, OPENSSL_RAW_DATA, getFixedIv16Byte());
    $name = $aesName . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA, getFixedIv16Byte());
    printStringData(('Dec ' . $name), $decryptedtext);
}

function openssl_aes_ctr_noPadding($key, $plaintext) {
    // choose algorithm name from key length
    $algorithmName = "";
    $aesName = "";
    switch (strlen($key)) {
        case 16:
        {
            $algorithmName = "aes-128-ctr";
            $aesName = "A128 CTR Key ";
            break;
        }
        case 24:
        {
            $algorithmName = "aes-192-ctr";
            $aesName = "A192 CTR Key ";
            break;
        }
        case 32:
        {
            $algorithmName = "aes-256-ctr";
            $aesName = "A256 CTR Key ";
            break;
        }
        default:
        {
            // not supported key length
            return '*** not supported key length ***';
        }
    }
    $ciphertext = openssl_encrypt($plaintext, $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING, getFixedIv16Byte());
    $name = $aesName . strlen($key) . ' B Pt ' . strlen($plaintext) . ' B';
    printData(('Enc ' . $name), $ciphertext);
    $decryptedtext = openssl_decrypt($ciphertext, $algorithmName, $key, OPENSSL_RAW_DATA|OPENSSL_ZERO_PADDING, getFixedIv16Byte());
    printStringData(('Dec ' . $name), $decryptedtext);
}

echo 'OPENSSL example program for PHP from 7.0.x' . PHP_EOL;
echo 'This program encrypts and decrypts strings of 15/16/17/31/32/33 bytes length.' . PHP_EOL;
echo 'The encryption algorithm is Rijndael-128 = AES' . PHP_EOL;
echo 'The tested key lengths are 16/24/32 bytes, modes are ECB/CBC/CTR' . PHP_EOL;
echo 'All tests are done with ZERO PADDING and PKCS7 padding (OpenSSL default)' . PHP_EOL;
echo 'except CTR mode that has no padding' . PHP_EOL;

$keys = array("12345678abcdefgh", "12345678abcdefgh12345678", "12345678abcdefgh12345678abcdefgh"); // 16/24/32 byte
$plaintexts = array("123456789012345", "1234567890123456", "12345678901234567",
    "1234567890123456789012345678901", "12345678901234567890123456789012", "123456789012345678901234567890123");

echo PHP_EOL . 'AES ECB encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        openssl_aes_ecb_zeroPadding($key, $plaintext);
    }
}

echo PHP_EOL . 'AES ECB encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        openssl_aes_ecb_pkcs7Padding($key, $plaintext);
    }
}

echo PHP_EOL . 'AES CBC encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext zero padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        openssl_aes_cbc_zeroPadding($key, $plaintext);
    }
}

echo PHP_EOL . 'AES CBC encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext PKCS7 padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        openssl_aes_cbc_pkcs7Padding($key, $plaintext);
    }
}

echo PHP_EOL . 'AES CTR encryption with 16/24/32 keys and 15/16/17/31/32/33 plaintext noo padding' . PHP_EOL;
foreach ($keys as $key) {
    foreach ($plaintexts as $plaintext) {
        openssl_aes_ctr_noPadding($key, $plaintext);
    }
}
?>