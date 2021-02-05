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

function chacha2020Poly1305EncryptToBase64($key, $data)
{
    $nonce = generateRandomNonce();
    $ciphertext = sodium_crypto_aead_chacha20poly1305_ietf_encrypt ($data , "" , $nonce, $key );
    // strip the tag off
    $ciphertextLength = strlen($ciphertext);
    $ciph = substr($ciphertext,0,$ciphertextLength - 16);
    $tag = substr($ciphertext,($ciphertextLength - 16),16);
    return base64Encoding($nonce) . ':' . base64Encoding($ciph) . ':' . base64Encoding($tag);
}

function chacha2020Poly1305DecryptFromBase64($key, $data)
{
    list($nonceBase64, $ciphertextBase64, $tagBase64) = explode(':', $data, 3);
    // append tag to ciphertext
    $nonce = base64Decoding($nonceBase64);
    $ciphertext = base64Decoding($ciphertextBase64);
    $tag = base64Decoding($tagBase64);
    $ciphertextComplete = $ciphertext . $tag;
    return sodium_crypto_aead_chacha20poly1305_ietf_decrypt($ciphertextComplete, "", $nonce, $key);
}

echo 'ChaCha2020-Poly1305 String encryption with random key full' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';
echo 'plaintext: ' . $plaintext . PHP_EOL;

// generate random key
$encryptionKey = generateRandomAesKey();
$encryptionKeyBase64 = base64Encoding($encryptionKey);
echo 'encryptionKey (Base64): ' . $encryptionKeyBase64 . PHP_EOL;

// encryption
echo PHP_EOL . '* * * Encryption * * *' . PHP_EOL;
$ciphertextBase64 = chacha2020Poly1305EncryptToBase64($encryptionKey, $plaintext);
echo 'ciphertext: ' . $ciphertextBase64 . PHP_EOL;
echo 'output is (Base64) nonce : (Base64) ciphertext : (Base64) poly1305Tag' .PHP_EOL;

// decryption
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
$decryptionKeyBase64 = $encryptionKeyBase64; // full
$ciphertextDecryptionBase64 = $ciphertextBase64;
echo 'decryptionKey (Base64): ' . $decryptionKeyBase64 . PHP_EOL;

echo 'ciphertextDecryption (Base64): ' . $ciphertextDecryptionBase64 . PHP_EOL;
echo 'input is (Base64) nonce : (Base64) ciphertext : (Base64) poly1305Tag' .PHP_EOL;
$decryptionKey = base64Decoding($decryptionKeyBase64);
$decryptedtext = chacha2020Poly1305DecryptFromBase64($decryptionKey, $ciphertextDecryptionBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;
?>
