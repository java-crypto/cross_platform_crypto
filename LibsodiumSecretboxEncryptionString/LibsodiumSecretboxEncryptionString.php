<?php

function secretboxEncryptionToBase64($encryptionKey, $data) {
    $nonce = generateRandomNonce();
    $ciphertext = sodium_crypto_secretbox($data, $nonce, base64Decoding($encryptionKey));
    return base64Encoding($nonce) . ':' . base64Encoding($ciphertext);
}

function secretboxDecryptionFromBase64($decryptionKey, $data) {
    $decryptionKey = base64_decode($decryptionKey);
    list($nonce, $ciphertext) = explode(':', $data, 2);
    return sodium_crypto_secretbox_open(base64Decoding($ciphertext), base64Decoding($nonce), $decryptionKey);
}

function generateRandomKey()
{
    return random_bytes(SODIUM_CRYPTO_SECRETBOX_KEYBYTES); // 32 bytes
}

function generateRandomNonce()
{
    return random_bytes(SODIUM_CRYPTO_SECRETBOX_NONCEBYTES); // 24 bytes
}

function base64Encoding($input)
{
    return base64_encode($input);
}

function base64Decoding($input)
{
    return base64_decode($input);
}

echo 'Libsodium secret box random key string encryption' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';
echo 'plaintext: ' . $plaintext . PHP_EOL;

$keyBase64 = base64Encoding(generateRandomKey());
echo 'encryption key (Base64): ' . $keyBase64 . PHP_EOL;

echo PHP_EOL . '* * * encryption * * *' . PHP_EOL;
echo 'all data are in Base64 encoding' . PHP_EOL;
$ciphertextBase64 = secretboxEncryptionToBase64($keyBase64, $plaintext);
echo 'ciphertext (Base64): ' . $ciphertextBase64 . PHP_EOL;
echo 'output is (Base64) nonce : (Base64) ciphertext' . PHP_EOL;

echo PHP_EOL . '* * * decryption * * *' . PHP_EOL;
$ciphertextReceivedBase64 = $ciphertextBase64;
echo 'ciphertext (Base64): ' . $ciphertextReceivedBase64 . PHP_EOL;
echo 'input is (Base64) nonce : (Base64) ciphertext' . PHP_EOL;
$decryptedtext = secretboxDecryptionFromBase64($keyBase64, $ciphertextReceivedBase64);
echo 'decrypt.text: ' . $decryptedtext . PHP_EOL;
?>
