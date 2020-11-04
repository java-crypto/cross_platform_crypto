<?php
function base64Decoding($input)
{
    return base64_decode($input);
}

function aesCbcDecryptFromBase64($key, $data)
{
    list($iv, $encryptedData) = explode(':', $data, 2);
    return openssl_decrypt(base64_decode($encryptedData), 'aes-256-cbc', $key, OPENSSL_RAW_DATA, base64_decode($iv));
}

echo 'AES CBC 256 String Decryption only' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';
echo 'plaintext: ' . $plaintext . PHP_EOL;

// ### place your data here:
$decryptionKeyBase64 = 'JJu2xyi4sP7lTfxVi8iPvKIIOWiwkgr7spyUEhsnjek=';
$ciphertextDecryptionBase64 = '+KOM4ASOY0cMNrM9zV/qvw==:HuoyDiusSplYfd04XSNLOqtcWyOFwmeHNzXS5ywmqRkgAXi8do/6dKppo2U3ZoKl';

// decryption
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
echo 'decryptionKey (Base64): ' . $decryptionKeyBase64 . PHP_EOL;
echo 'ciphertextDecryption (Base64): ' . $ciphertextDecryptionBase64 . PHP_EOL;
echo 'input is (Base64) iv : (Base64) ciphertext' .PHP_EOL;
$decryptionKey = base64Decoding($decryptionKeyBase64);
$decryptedtext = aesCbcDecryptFromBase64($decryptionKey, $ciphertextDecryptionBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;
?>
