<?php
function cryptoboxEncryptionToBase64($privateKey, $publicKey, $data)
{
    $nonce = generateRandomNonce();
    $encryptionKey = $privateKey . $publicKey;
    return base64Encoding($nonce) . ':' . base64Encoding(sodium_crypto_box($data, $nonce, $encryptionKey));
}

function cryptoboxDecryptionFromBase64($privateKey, $publicKey, $data)
{
    list($nonceBase64, $ciphertextBase64) = explode(':', $data, 2);
    $nonce = base64Decoding($nonceBase64);
    $ciphertext = base64Decoding($ciphertextBase64);
    $decryptionKey = $privateKey . $publicKey;
    return sodium_crypto_box_open($ciphertext, $nonce, $decryptionKey);
}

function generateRandomNonce()
{
    return random_bytes(SODIUM_CRYPTO_BOX_NONCEBYTES); // 24 bytes
}

function base64Encoding($input)
{
    return base64_encode($input);
}

function base64Decoding($input)
{
    return base64_decode($input);
}

echo 'Libsodium crypto box hybrid string encryption' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';
echo 'plaintext: ' . $plaintext . PHP_EOL;

echo PHP_EOL . '* * * encryption * * *' . PHP_EOL;
echo 'all data are in Base64 encoding' . PHP_EOL;
// for encryption you need your private key (privateKeyA) and the public key from other party (publicKeyB)
$privateKeyABase64 = 'yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU=';
$publicKeyBBase64 =  'jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=';
// convert the base64 encoded keys
$privateKeyA = base64Decoding($privateKeyABase64);
$publicKeyB = base64Decoding($publicKeyBBase64);

$ciphertextBase64 = cryptoboxEncryptionToBase64($privateKeyA, $publicKeyB, $plaintext);
echo 'privateKeyA: ' . $privateKeyABase64 . PHP_EOL;
echo 'publicKeyB:  ' . $publicKeyBBase64 . PHP_EOL;
echo 'ciphertext:  ' . $ciphertextBase64 . PHP_EOL;
echo 'output is    (Base64) nonce : (Base64) ciphertext' . PHP_EOL;

// for decryption you need your private key (privateKeyB) and the public key from other party (publicKeyA)
// received ciphertext
$ciphertextReceivedBase64 = $ciphertextBase64;
// own private key and received public key
$privateKeyBBase64 = 'yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=';
$publicKeyABase64 =  'b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0=';
// convert the base64 encoded keys
$privateKeyB = base64Decoding($privateKeyBBase64);
$publicKeyA = base64Decoding($publicKeyABase64);
// decrypt
$decryptedtext = cryptoboxDecryptionFromBase64($privateKeyB, $publicKeyA, $ciphertextReceivedBase64);
echo PHP_EOL . '* * * decryption * * *' . PHP_EOL;
echo 'ciphertext:  ' . $ciphertextReceivedBase64 . PHP_EOL;
echo 'input is     (Base64) iv : (Base64) ciphertext' . PHP_EOL;
echo 'privateKeyB: ' . $privateKeyBBase64 . PHP_EOL;
echo 'publicKeyA:  ' . $publicKeyABase64 . PHP_EOL;
echo 'decrypt.text:' . $decryptedtext . PHP_EOL;
?>
