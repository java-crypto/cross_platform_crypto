<?php
function aesCbcPbkdf2HmacEncryptToBase64($password, $data)
{
    $PBKDF2_ITERATIONS = 15000;
    $salt = generateSalt32Byte();
    $keyAesHmac = hash_pbkdf2("sha256", $password, $salt, $PBKDF2_ITERATIONS, 64, $raw_output = true);
    $keyAes = substr($keyAesHmac, 0, 32);
    $keyHmac = substr($keyAesHmac, 32, 32);
    $iv = generateRandomInitvector();
    $ciphertext = openssl_encrypt($data, 'aes-256-cbc', $keyAes, OPENSSL_RAW_DATA, $iv);
    $ciphertextWithoutHmac = base64_encode($salt) . ':' . base64_encode($iv) . ':' . base64_encode($ciphertext);
    $hmacBase64 =  base64_encode(hash_hmac("sha256" , $ciphertextWithoutHmac, $keyHmac, true));
    return $ciphertextWithoutHmac . ':' . $hmacBase64;
}

function generateSalt32Byte()
{
    return openssl_random_pseudo_bytes(32, $crypto_strong);
}

function aesCbcPbkdf2HmacDecryptFromBase64($password, $data)
{
    $PBKDF2_ITERATIONS = 15000;
    list($salt, $iv, $encryptedData, $hmac) = explode(':', $data, 4);
    $keyAesHmac = hash_pbkdf2("sha256", $password, base64_decode($salt), $PBKDF2_ITERATIONS, 64, $raw_output = true);
    $keyAes = substr($keyAesHmac, 0, 32);
    $keyHmac = substr($keyAesHmac, 32, 32);
    // before we decrypt we have to check the hmac
    $hmacToCheck = $salt . ':' . $iv . ':' . $encryptedData;
    $hmacCalculated = hash_hmac("sha256" , $hmacToCheck, $keyHmac, true);
    $hmac = base64_decode($hmac);
    if (!hash_equals($hmacCalculated, $hmac)) {
        echo 'Error: HMAC-check failed, no decryption possible' . PHP_EOL;
        return "";
    }
    return openssl_decrypt(base64_decode($encryptedData), 'aes-256-cbc', $keyAes, OPENSSL_RAW_DATA, base64_decode($iv));
}

function generateRandomInitvector()
{
    return openssl_random_pseudo_bytes(16, $crypto_strong);
}

echo 'AES CBC 256 String encryption with PBKDF2 derived key and HMAC check' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';
echo 'plaintext: ' . $plaintext . PHP_EOL;
$password = "secret password";

// encryption
echo PHP_EOL . '* * * Encryption * * *' . PHP_EOL;
$ciphertextBase64 = aesCbcPbkdf2HmacEncryptToBase64($password, $plaintext);
echo 'ciphertext: ' . $ciphertextBase64 . PHP_EOL;
echo 'output is (Base64) salt : (Base64) iv : (Base64) ciphertext' .PHP_EOL;

// decryption
echo PHP_EOL;
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
$ciphertextDecryptionBase64 = $ciphertextBase64;
echo 'ciphertextDecryption (Base64): ' . $ciphertextDecryptionBase64 . PHP_EOL;
echo 'input is (Base64) salt : (Base64) iv : (Base64) ciphertext' .PHP_EOL;
$decryptedtext = aesCbcPbkdf2HmacDecryptFromBase64($password, $ciphertextDecryptionBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;
?>
