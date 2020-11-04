<?php
function generateRandomAesKey()
{
    return openssl_random_pseudo_bytes(32, $crypto_strong);
}

function generateRandomInitvector()
{
    return openssl_random_pseudo_bytes(16, $crypto_strong);
}

function base64Encoding($input)
{
    return base64_encode($input);
}

function base64Decoding($input)
{
    return base64_decode($input);
}

function aesCbcEncryptToBase64($key, $data)
{
    $iv = generateRandomInitvector();
    $ciphertext = openssl_encrypt($data, 'aes-256-cbc', $key, OPENSSL_RAW_DATA, $iv);
    return base64_encode($iv) . ':' . base64_encode($ciphertext);
}

function aesCbcDecryptFromBase64($key, $data)
{
    list($iv, $encryptedData) = explode(':', $data, 2);
    return openssl_decrypt(base64_decode($encryptedData), 'aes-256-cbc', $key, OPENSSL_RAW_DATA, base64_decode($iv));
}

echo 'AES CBC 256 String encryption with random key full' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';
echo 'plaintext: ' . $plaintext . PHP_EOL;

// generate random key
$encryptionKey = generateRandomAesKey();
$encryptionKeyBase64 = base64Encoding($encryptionKey);
echo 'encryptionKey (Base64): ' . $encryptionKeyBase64 . PHP_EOL;

// encryption
echo PHP_EOL . '* * * Encryption * * *' . PHP_EOL;
$ciphertextBase64 = aesCbcEncryptToBase64($encryptionKey, $plaintext);
echo 'ciphertext: ' . $ciphertextBase64 . PHP_EOL;
echo 'output is (Base64) iv : (Base64) ciphertext' .PHP_EOL;

echo PHP_EOL;
echo 'Cross platform cryptography: AES CBC 256 String encryption with random key (PHP)' . PHP_EOL;
// decryption
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
$decryptionKeyBase64 = 'JJu2xyi4sP7lTfxVi8iPvKIIOWiwkgr7spyUEhsnjek='; // from Java
//$decryptionKeyBase64 = $encryptionKeyBase64; // full
$ciphertextDecryptionBase64 = '+KOM4ASOY0cMNrM9zV/qvw==:HuoyDiusSplYfd04XSNLOqtcWyOFwmeHNzXS5ywmqRkgAXi8do/6dKppo2U3ZoKl'; // from Java
//$ciphertextDecryptionBase64 = $ciphertextBase64;
echo 'decryptionKey (Base64): ' . $decryptionKeyBase64 . PHP_EOL;

echo 'ciphertextDecryption (Base64): ' . $ciphertextDecryptionBase64 . PHP_EOL;
echo 'input is (Base64) iv : (Base64) ciphertext' .PHP_EOL;
$decryptionKey = base64Decoding($decryptionKeyBase64);
$decryptedtext = aesCbcDecryptFromBase64($decryptionKey, $ciphertextDecryptionBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;
?>
