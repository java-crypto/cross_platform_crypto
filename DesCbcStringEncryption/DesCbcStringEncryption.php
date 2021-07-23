<?php
function generateRandomDesKey()
{
    return openssl_random_pseudo_bytes(8, $crypto_strong);
}

function generateRandomDesInitvector()
{
    return openssl_random_pseudo_bytes(8, $crypto_strong);
}

// don't do this in production
function generateFixedDesInitvector()
{
    return '12345678';
}

function base64Encoding($input)
{
    return base64_encode($input);
}

function base64Decoding($input)
{
    return base64_decode($input);
}

function desCbcEncryptToBase64($key, $data)
{
    // don't use this in production
    $iv = generateFixedDesInitvector();
    //$iv = generateRandomDesInitvector();
    $ciphertext = openssl_encrypt($data, 'des-cbc', $key, OPENSSL_RAW_DATA, $iv);
    return base64_encode($iv) . ':' . base64_encode($ciphertext);
}

function desCbcDecryptFromBase64($key, $data)
{
    list($iv, $encryptedData) = explode(':', $data, 2);
    return openssl_decrypt(base64_decode($encryptedData), 'des-cbc', $key, OPENSSL_RAW_DATA, base64_decode($iv));
}

function des2CbcEncryptToBase64($key, $data)
{
    // don't use this in production
    $iv = generateFixedDesInitvector();
    // $iv = generateRandomDesInitvector();
    $ciphertext = openssl_encrypt($data, 'des-ede-cbc', $key, OPENSSL_RAW_DATA, $iv);
    return base64_encode($iv) . ':' . base64_encode($ciphertext);
}

function des2CbcDecryptFromBase64($key, $data)
{
    list($iv, $encryptedData) = explode(':', $data, 2);
    return openssl_decrypt(base64_decode($encryptedData), 'des-ede-cbc', $key, OPENSSL_RAW_DATA, base64_decode($iv));
}

function des3CbcEncryptToBase64($key, $data)
{
    // don't use this in production
    $iv = generateFixedDesInitvector();
    // $iv = generateRandomDesInitvector();
    $ciphertext = openssl_encrypt($data, 'des-ede3-cbc', $key, OPENSSL_RAW_DATA, $iv);
    return base64_encode($iv) . ':' . base64_encode($ciphertext);
}

function des3CbcDecryptFromBase64($key, $data)
{
    list($iv, $encryptedData) = explode(':', $data, 2);
    return openssl_decrypt(base64_decode($encryptedData), 'des-ede3-cbc', $key, OPENSSL_RAW_DATA, base64_decode($iv));
}

echo PHP_EOL . '# # # SECURITY WARNING: This code is provided for achieve    # # #' . PHP_EOL;
echo '# # # compatibility between different programming languages. # # #' . PHP_EOL;
echo '# # # It is not necessarily fully secure.                    # # #' . PHP_EOL;
echo '# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #' . PHP_EOL;
echo '# # # It uses FIXED key and IV\'s that should NEVER used,    # # #' . PHP_EOL;
echo '# # # instead use random generated keys and IVs.             # # #' . PHP_EOL;
echo '# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';
echo 'plaintext: ' . $plaintext . PHP_EOL;

echo PHP_EOL . 'DES CBC String encryption with fixed key & iv' . PHP_EOL;

// fixed encryption key 8 bytes long
$desKey = '12345678';
echo 'desEncryptionKey (Base64): ' . base64_encode($desKey) . PHP_EOL;

// encryption
echo PHP_EOL . '* * * Encryption * * *' . PHP_EOL;
$desCiphertextBase64 = desCbcEncryptToBase64($desKey, $plaintext);
echo 'ciphertext: ' . $desCiphertextBase64 . PHP_EOL;
echo 'output is (Base64) iv : (Base64) ciphertext' .PHP_EOL;
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
echo 'ciphertext: ' . $desCiphertextBase64 . PHP_EOL;
echo 'input is (Base64) iv : (Base64) ciphertext' .PHP_EOL;
$decryptedtext = desCbcDecryptFromBase64($desKey, $desCiphertextBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;

echo PHP_EOL . 'Triple DES CBC String encryption with fixed key (16 bytes) & iv' . PHP_EOL;

// fixed encryption key 16 bytes long
$des2Key = '1234567890123456';
echo 'des2EncryptionKey (Base64): ' . base64_encode($des2Key) . PHP_EOL;

// encryption
echo PHP_EOL . '* * * Encryption * * *' . PHP_EOL;
$des2CiphertextBase64 = des2CbcEncryptToBase64($des2Key, $plaintext);
echo 'ciphertext: ' . $des2CiphertextBase64 . PHP_EOL;
echo 'output is (Base64) iv : (Base64) ciphertext' .PHP_EOL;
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
echo 'ciphertext: ' . $des2CiphertextBase64 . PHP_EOL;
echo 'input is (Base64) iv : (Base64) ciphertext' .PHP_EOL;
$decryptedtext = des2CbcDecryptFromBase64($des2Key, $des2CiphertextBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;

echo PHP_EOL . 'Triple DES CBC String encryption with fixed key (24 bytes) & iv' . PHP_EOL;

// fixed encryption key 24 bytes long
$des3Key = '123456789012345678901234';
echo 'des3EncryptionKey (Base64): ' . base64_encode($des3Key) . PHP_EOL;

// encryption
echo PHP_EOL . '* * * Encryption * * *' . PHP_EOL;
$des3CiphertextBase64 = des3CbcEncryptToBase64($des3Key, $plaintext);
echo 'ciphertext: ' . $des3CiphertextBase64 . PHP_EOL;
echo 'output is (Base64) iv : (Base64) ciphertext' .PHP_EOL;
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
echo 'ciphertext: ' . $des3CiphertextBase64 . PHP_EOL;
echo 'input is (Base64) iv : (Base64) ciphertext' .PHP_EOL;
$decryptedtext = des3CbcDecryptFromBase64($des3Key, $des3CiphertextBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;

?>
