<?php
function sealedCryptoboxEncryptionToBase64($publicKey, $data)
{
    return base64Encoding(sodium_crypto_box_seal($data, $publicKey));
}

function sealedCryptoboxDecryptionFromBase64($privateKey, $dataBase64)
{
    $ciphertext = base64Decoding($dataBase64);
    // get the public key from secret key
    $publicKey = sodium_crypto_box_publickey_from_secretkey($privateKey);
    $decryptionKey = $privateKey . $publicKey;
    return sodium_crypto_box_seal_open($ciphertext, $decryptionKey);
}

function base64Encoding($input)
{
    return base64_encode($input);
}

function base64Decoding($input)
{
    return base64_decode($input);
}

echo 'Libsodium sealed crypto box hybrid string encryption' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';
echo 'plaintext: ' . $plaintext . PHP_EOL;

echo PHP_EOL . '* * * encryption * * *' . PHP_EOL;
echo 'all data are in Base64 encoding' . PHP_EOL;
// for encryption you need the public key from the one you are sending the data to (party B)
$publicKeyBBase64 =  'jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=';
// convert the base64 encoded keys
$publicKeyB = base64Decoding($publicKeyBBase64);

$ciphertextBase64 = sealedCryptoboxEncryptionToBase64($publicKeyB, $plaintext);
echo 'publicKeyB:  ' . $publicKeyBBase64 . PHP_EOL;
echo 'ciphertext:  ' . $ciphertextBase64 . PHP_EOL;
echo 'output is (Base64) ciphertext' . PHP_EOL;

// for decryption you need your private key (privateKeyB)
// received ciphertext
$ciphertextReceivedBase64 = $ciphertextBase64;
// own private key and received public key
$privateKeyBBase64 = 'yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=';
// convert the base64 encoded keys
$privateKeyB = base64Decoding($privateKeyBBase64);
// decrypt
$decryptedtext = sealedCryptoboxDecryptionFromBase64($privateKeyB, $ciphertextReceivedBase64);
echo PHP_EOL . '* * * decryption * * *' . PHP_EOL;
echo 'ciphertext:  ' . $ciphertextReceivedBase64 . PHP_EOL;
echo 'input is (Base64) ciphertext' . PHP_EOL;
echo 'privateKeyB: ' . $privateKeyBBase64 . PHP_EOL;
echo 'decrypt.text:' . $decryptedtext . PHP_EOL;
?>
