<?php

function generateSalt32Byte()
{
    return openssl_random_pseudo_bytes(32, $crypto_strong);
}

function generateRandomNonce()
{
    return openssl_random_pseudo_bytes(12, $crypto_strong);
}

function base64Encoding($input)
{
    return base64_encode($input);
}

function base64Decoding($input)
{
    return base64_decode($input);
}

function aesGcmPbkdf2EncryptToBase64($password, $data)
{
    $PBKDF2_ITERATIONS = 15000;
    $salt = generateSalt32Byte();
    $key = hash_pbkdf2("sha256", $password, $salt, $PBKDF2_ITERATIONS, 32, $raw_output = true);
    $nonce = generateRandomNonce();
    $ciphertext = openssl_encrypt($data, 'aes-256-gcm', $key, OPENSSL_RAW_DATA, $nonce, $tag);
    return base64Encoding($salt) . ':' . base64Encoding($nonce) . ':' . base64Encoding($ciphertext) . ':' . base64Encoding($tag);
}

function aesGcmPbkdf2DecryptFromBase64($password, $data)
{
    $PBKDF2_ITERATIONS = 15000;
    list($salt, $nonce, $encryptedData, $tag) = explode(':', $data, 4);
    $key = hash_pbkdf2("sha256", $password, base64_decode($salt), $PBKDF2_ITERATIONS, 32, $raw_output = true);
    return openssl_decrypt(base64Decoding($encryptedData), 'aes-256-gcm', $key, OPENSSL_RAW_DATA, base64Decoding($nonce), base64Decoding($tag));
}

echo 'AES GCM 256 String encryption with PBKDF2 derived key' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';
$password = 'secret password';

echo 'plaintext: ' . $plaintext . PHP_EOL;

// encryption
echo PHP_EOL . '* * * Encryption * * *' . PHP_EOL;
$ciphertextBase64 = aesGcmPbkdf2EncryptToBase64($password, $plaintext);
echo 'ciphertext (Base64): ' . $ciphertextBase64 . PHP_EOL;
echo 'output is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag' .PHP_EOL;
echo PHP_EOL;

// decryption
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
$passwordDec = 'mySecretPassword';
$ciphertextDecryptionBase64 = $ciphertextBase64;
echo 'ciphertext (Base64): ' . $ciphertextDecryptionBase64 . PHP_EOL;
echo 'input is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag' .PHP_EOL;
$decryptedtext = aesGcmPbkdf2DecryptFromBase64($password, $ciphertextDecryptionBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;
?>
