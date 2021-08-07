<?php
function generateRandomDesKey()
{
    return openssl_random_pseudo_bytes(8, $crypto_strong);
}

function base64Encoding($input)
{
    return base64_encode($input);
}

function base64Decoding($input)
{
    return base64_decode($input);
}

function desEcbEncryptToBase64($key, $data)
{
    $ciphertext = openssl_encrypt($data, 'des-ecb', $key, OPENSSL_RAW_DATA);
    return base64_encode($ciphertext);
}

function desEcbDecryptFromBase64($key, $data)
{
    return openssl_decrypt(base64_decode($data), 'des-ecb', $key, OPENSSL_RAW_DATA);
}

function des2EcbEncryptToBase64($key, $data)
{
    $ciphertext = openssl_encrypt($data, 'des-ede-ecb', $key, OPENSSL_RAW_DATA);
    return base64_encode($ciphertext);
}

function des2EcbDecryptFromBase64($key, $data)
{
    return openssl_decrypt(base64_decode($data), 'des-ede-ecb', $key, OPENSSL_RAW_DATA);
}

function des3EcbEncryptToBase64($key, $data)
{
    $ciphertext = openssl_encrypt($data, 'des-ede3-ecb', $key, OPENSSL_RAW_DATA);
    return base64_encode($ciphertext);
}

function des3EcbDecryptFromBase64($key, $data)
{
    return openssl_decrypt(base64_decode($data), 'des-ede3-ecb', $key, OPENSSL_RAW_DATA);
}

echo PHP_EOL . '# # # SECURITY WARNING: This code is provided for achieve    # # #' . PHP_EOL;
echo '# # # compatibility between different programming languages. # # #' . PHP_EOL;
echo '# # # It is not necessarily fully secure.                    # # #' . PHP_EOL;
echo '# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #' . PHP_EOL;
echo '# # # It uses FIXED key that should NEVER used,              # # #' . PHP_EOL;
echo '# # # instead use random generated keys.                     # # #' . PHP_EOL;
echo '# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';
echo 'plaintext: ' . $plaintext . PHP_EOL;

echo PHP_EOL . 'DES ECB String encryption with fixed key' . PHP_EOL;

// fixed encryption key 8 bytes long
$desKey = '12345678';
echo 'desEncryptionKey (Base64): ' . base64_encode($desKey) . PHP_EOL;

// encryption
echo PHP_EOL . '* * * Encryption * * *' . PHP_EOL;
$desCiphertextBase64 = desEcbEncryptToBase64($desKey, $plaintext);
echo 'ciphertext: ' . $desCiphertextBase64 . PHP_EOL;
echo 'output is (Base64) ciphertext' .PHP_EOL;
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
echo 'ciphertext: ' . $desCiphertextBase64 . PHP_EOL;
echo 'input is (Base64) ciphertext' .PHP_EOL;
$decryptedtext = desEcbDecryptFromBase64($desKey, $desCiphertextBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;

echo PHP_EOL . 'Triple DES ECB String encryption with fixed key (16 bytes)' . PHP_EOL;

// fixed encryption key 16 bytes long
$des2Key = '1234567890123456';
echo 'des2EncryptionKey (Base64): ' . base64_encode($des2Key) . PHP_EOL;

// encryption
echo PHP_EOL . '* * * Encryption * * *' . PHP_EOL;
$des2CiphertextBase64 = des2EcbEncryptToBase64($des2Key, $plaintext);
echo 'ciphertext: ' . $des2CiphertextBase64 . PHP_EOL;
echo 'output is (Base64) ciphertext' .PHP_EOL;
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
echo 'ciphertext: ' . $des2CiphertextBase64 . PHP_EOL;
echo 'input is (Base64) ciphertext' .PHP_EOL;
$decryptedtext = des2EcbDecryptFromBase64($des2Key, $des2CiphertextBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;

echo PHP_EOL . 'Triple DES ECB String encryption with fixed key (24 bytes)' . PHP_EOL;

// fixed encryption key 24 bytes long
$des3Key = '123456789012345678901234';
echo 'des3EncryptionKey (Base64): ' . base64_encode($des3Key) . PHP_EOL;

// encryption
echo PHP_EOL . '* * * Encryption * * *' . PHP_EOL;
$des3CiphertextBase64 = des3EcbEncryptToBase64($des3Key, $plaintext);
echo 'ciphertext: ' . $des3CiphertextBase64 . PHP_EOL;
echo 'output is (Base64) ciphertext' .PHP_EOL;
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
echo 'ciphertext: ' . $des3CiphertextBase64 . PHP_EOL;
echo 'input is (Base64) ciphertext' .PHP_EOL;
$decryptedtext = des3EcbDecryptFromBase64($des3Key, $des3CiphertextBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;

?>
