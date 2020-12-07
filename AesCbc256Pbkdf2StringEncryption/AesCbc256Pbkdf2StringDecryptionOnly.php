<?php
function aesCbcPbkdf2DecryptFromBase64($password, $data)
{
    $PBKDF2_ITERATIONS = 15000;
    list($salt, $iv, $encryptedData) = explode(':', $data, 3);
    $key = hash_pbkdf2("sha256", $password, base64_decode($salt), $PBKDF2_ITERATIONS, 32, $raw_output = true);
    return openssl_decrypt(base64_decode($encryptedData), 'aes-256-cbc', $key, OPENSSL_RAW_DATA, base64_decode($iv));
}

echo 'AES CBC 256 String decryption with PBKDF2 derived key only' . PHP_EOL;

$password = "secret password";

// decryption
echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
$ciphertextDecryptionBase64 = "do08Oh2DRbF38vcF2YYI/g2DLmRCUTUVDSwzmZlgCt8=:n1BFnxCo8HrsBgLnylCOhA==:Stc/VI08bGOatozFOATRwU/stE9uv/ouiBEfP7rMhUc7rufIkJkeb4NjeT4tw33M";
echo 'ciphertextDecryption (Base64): ' . $ciphertextDecryptionBase64 . PHP_EOL;
echo 'input is (Base64) salt : (Base64) iv : (Base64) ciphertext' .PHP_EOL;
$decryptedtext = aesCbcPbkdf2DecryptFromBase64($password, $ciphertextDecryptionBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;
?>
