<?php
// source: https://github.com/blocktrail/cryptojs-aes-php/blob/master/src/CryptoJSAES.php
// author: BlockTrail
function aesCbcPassphraseEncryptToBase64($passphrase, $data, $salt = null) {
    $salt = $salt ?: openssl_random_pseudo_bytes(8);
    list($key, $iv) = evpkdf($passphrase, $salt);
    $ct = openssl_encrypt($data, 'aes-256-cbc', $key, true, $iv);
    return encode($ct, $salt);
}

function aesCbcPassphraseDecryptFromBase64($passphrase, $base64) {
    list($ct, $salt) = decode($base64);
    list($key, $iv) = evpkdf($passphrase, $salt);
    $data = openssl_decrypt($ct, 'aes-256-cbc', $key, true, $iv);
    return $data;
}

function evpkdf($passphrase, $salt) {
    $salted = '';
    $dx = '';
    while (strlen($salted) < 48) {
        $dx = md5($dx . $passphrase . $salt, true);
        $salted .= $dx;
    }
    $key = substr($salted, 0, 32);
    $iv = substr($salted, 32, 16);
    return [$key, $iv];
}

function decode($base64) {
    $data = base64_decode($base64);
    if (substr($data, 0, 8) !== "Salted__") {
        return "";
    }
    $salt = substr($data, 8, 8);
    $ct = substr($data, 16);
    return [$ct, $salt];
}

function encode($ct, $salt) {
    return base64_encode("Salted__" . $salt . $ct);
}

echo 'AES CBC 256 String encryption with passphrase' . PHP_EOL;

echo PHP_EOL . '# # # SECURITY WARNING: This code is provided for achieve    # # #' . PHP_EOL;
echo '# # # compatibility between different programming languages. # # #' . PHP_EOL;
echo '# # # It is not necessarily fully secure.                    # # #' . PHP_EOL;
echo '# # # Its security depends on the complexity and length      # # #' . PHP_EOL;
echo '# # # of the password, because of only one iteration and     # # #' . PHP_EOL;
echo '# # # the use of MD5.                                        # # #' . PHP_EOL;
echo '# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #' . PHP_EOL;

$plaintext = 'The quick brown fox jumps over the lazy dog';
$passphrase = 'my secret passphrase';
echo PHP_EOL . 'passphrase: ' . $passphrase . PHP_EOL;

echo PHP_EOL . '* * * Encryption * * *' . PHP_EOL;
$ciphertextBase64 = aesCbcPassphraseEncryptToBase64($passphrase, $plaintext);
echo 'ciphertext (Base64): ' . $ciphertextBase64 . PHP_EOL;
echo 'output is (Base64) ciphertext' . PHP_EOL;

echo PHP_EOL . '* * * Decryption * * *' . PHP_EOL;
$decryptionPassphrase = $passphrase;
$ciphertextDecryptionBase64 = $ciphertextBase64;
echo 'decryptionPassphrase: ' . $decryptionPassphrase . PHP_EOL;
echo 'ciphertext (Base64): ' . $ciphertextDecryptionBase64 . PHP_EOL;
echo 'input is (Base64) ciphertext' . PHP_EOL;
$decryptedtext = aesCbcPassphraseDecryptFromBase64($decryptionPassphrase, $ciphertextDecryptionBase64);
echo 'plaintext: ' . $decryptedtext . PHP_EOL;
?>