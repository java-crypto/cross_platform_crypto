<?php
function aesCbcPbkdf2EncryptToBase64($password, $data)
{
    $PBKDF2_ITERATIONS = 15000;
    $salt = generateSalt32Byte();
    $key = hash_pbkdf2("sha256", $password, $salt, $PBKDF2_ITERATIONS, 32, $raw_output = true);
    $iv = generateRandomInitvector();
    $ciphertext = openssl_encrypt($data, 'aes-256-cbc', $key, OPENSSL_RAW_DATA, $iv);
    return base64_encode($salt) . ':' . base64_encode($iv) . ':' . base64_encode($ciphertext);
}

function generateSalt32Byte()
{
    return openssl_random_pseudo_bytes(32, $crypto_strong);
}

function aesCbcPbkdf2DecryptFromBase64($password, $data)
{
    $PBKDF2_ITERATIONS = 15000;
    list($salt, $iv, $encryptedData) = explode(':', $data, 3);
    $key = hash_pbkdf2("sha256", $password, base64_decode($salt), $PBKDF2_ITERATIONS, 32, $raw_output = true);
    return openssl_decrypt(base64_decode($encryptedData), 'aes-256-cbc', $key, OPENSSL_RAW_DATA, base64_decode($iv));
}

function generateRandomInitvector()
{
    return openssl_random_pseudo_bytes(16, $crypto_strong);
}

echo 'AES CBC 256 String encryption with PBKDF2 derived key' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';
echo 'plaintext: ' . $plaintext . PHP_EOL;
$password = "secret password";

// encryption
echo PHP_EOL . '* * * Encryption * * *' . PHP_EOL;
$ciphertextBase64 = aesCbcPbkdf2EncryptToBase64($password, $plaintext);
echo 'ciphertext: ' . $ciphertextBase64 . PHP_EOL;
echo 'output is (Base64) salt : (Base64) iv : (Base64) ciphertext' .PHP_EOL;

// decryption
echo PHP_EOL;
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
$ciphertextDecryptionBase64 = $ciphertextBase64;
echo 'ciphertextDecryption (Base64): ' . $ciphertextDecryptionBase64 . PHP_EOL;
echo 'input is (Base64) salt : (Base64) iv : (Base64) ciphertext' .PHP_EOL;
$decryptedtext = aesCbcPbkdf2DecryptFromBase64($password, $ciphertextDecryptionBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;
?>
