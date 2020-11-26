<?php
require_once ('Curve25519.php');
// you need the library https://github.com/lt/PHP-Curve25519

function curve25519GenerateKeyAgreement($privateKey, $publicKey) {
    return (new Curve25519)->sharedKey($privateKey, $publicKey);
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

function base64Encoding($input)
{
    return base64_encode($input);
}

function base64Decoding($input)
{
    return base64_decode($input);
}

function generateRandomInitvector()
{
    return openssl_random_pseudo_bytes(16, $crypto_strong);
}

echo 'Curve25519 key exchange and AES CBC 256 string encryption' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';

// for encryption you need your private key (aPrivateKey) and the public key from other party (bPublicKey)

$aPrivateKeyBase64 = 'yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU=';
$bPublicKeyBase64 =  'jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=';

// convert the base64 encoded keys
$aPrivateKey = base64Decoding($aPrivateKeyBase64);
$bPublicKey = base64Decoding($bPublicKeyBase64);

// generate shared key
$aSharedKey = curve25519GenerateKeyAgreement($aPrivateKey, $bPublicKey);
// encrypt
$ciphertextBase64 = aesCbcEncryptToBase64($aSharedKey, $plaintext);

echo PHP_EOL . '* * * encryption * * *' . PHP_EOL;
echo 'all data are in Base64 encoding';
echo 'aPrivateKey: ' . $aPrivateKeyBase64 . PHP_EOL;
echo 'bPublicKey:  ' . $bPublicKeyBase64 . PHP_EOL;
echo 'aSharedKey:  ' . base64Encoding($aSharedKey) . PHP_EOL;
echo 'ciphertext:  ' . $ciphertextBase64 . PHP_EOL;
echo 'output is    (Base64) iv : (Base64) ciphertext' . PHP_EOL;

// for decryption you need your private key (bPrivateKey) and the public key from other party (aPublicKey)
// received ciphertext
$ciphertextReceivedBase64 = $ciphertextBase64;
// recieved public key
$aPublicKeyBase64 =  'b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0=';
// own private key
$bPrivateKeyBase64 = 'yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=';
// convert the base64 encoded keys
$bPrivateKey = base64Decoding($bPrivateKeyBase64);
$aPublicKey = base64Decoding($aPublicKeyBase64);
// generate shared key
$bSharedKey = curve25519GenerateKeyAgreement($bPrivateKey, $aPublicKey);
// decrypt
$decryptedtext = aesCbcDecryptFromBase64($bSharedKey, $ciphertextReceivedBase64);
echo PHP_EOL . '* * * decryption * * *' . PHP_EOL;
echo 'ciphertext:  ' . $ciphertextReceivedBase64 . PHP_EOL;
echo 'input is     (Base64) iv : (Base64) ciphertext' . PHP_EOL;
echo 'bPrivateKey: ' . $bPrivateKeyBase64 . PHP_EOL;
echo 'aPublicKey:  ' . $aPublicKeyBase64 . PHP_EOL;
echo 'bSharedKey:  ' . base64Encoding($bSharedKey) . PHP_EOL;
echo 'decrypt.text:' . $decryptedtext . PHP_EOL;
?>
