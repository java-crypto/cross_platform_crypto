<?php
function generateRandomAesKey()
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

function aesGcmEncryptToBase64($key, $data)
{
    $nonce = generateRandomNonce();
    $ciphertext = openssl_encrypt($data, 'aes-256-gcm', $key, OPENSSL_RAW_DATA, $nonce, $tag);
    return base64Encoding($nonce) . ':' . base64Encoding($ciphertext) . ':' . base64Encoding($tag);
}

function aesGcmDecryptFromBase64($key, $data)
{
    list($nonce, $encryptedData, $tag) = explode(':', $data, 3);
    return openssl_decrypt(base64Decoding($encryptedData), 'aes-256-gcm', $key, OPENSSL_RAW_DATA, base64Decoding($nonce), base64Decoding($tag));
}

echo 'AES GCM 256 String encryption with random key' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';
echo 'plaintext: ' . $plaintext . PHP_EOL;

// generate random key
$encryptionKey = generateRandomAesKey();
$encryptionKeyBase64 = base64Encoding($encryptionKey);
echo 'encryptionKey (Base64): ' . $encryptionKeyBase64 . PHP_EOL;

// encryption
echo PHP_EOL . '* * * Encryption * * *' . PHP_EOL;
$ciphertextBase64 = aesGcmEncryptToBase64($encryptionKey, $plaintext);
echo 'ciphertext: ' . $ciphertextBase64 . PHP_EOL;
echo 'output is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag' .PHP_EOL;

echo PHP_EOL;
echo 'AES GCM 256 String decryption with random key full' . PHP_EOL;
// decryption
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
$decryptionKeyBase64 = $encryptionKeyBase64; // full
$ciphertextDecryptionBase64 = $ciphertextBase64;
echo 'decryptionKey (Base64): ' . $decryptionKeyBase64 . PHP_EOL;

echo 'ciphertextDecryption (Base64): ' . $ciphertextDecryptionBase64 . PHP_EOL;
echo 'input is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag' .PHP_EOL;
$decryptionKey = base64Decoding($decryptionKeyBase64);
$decryptedtext = aesGcmDecryptFromBase64($decryptionKey, $ciphertextDecryptionBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;
?>
