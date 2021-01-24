<?php
function libsodiumSignDetachedToBase64($privateKey, $dataToSign)
{
    return base64Encoding(sodium_crypto_sign_detached($dataToSign, $privateKey));
}

function libsodiumVerifyDetachedFromBase64($publicKey, $dataToSign, $signatureBase64)
{
    $ok = sodium_crypto_sign_verify_detached(base64Decoding($signatureBase64), $dataToSign, $publicKey);
    if ($ok == 1) {
        return true;
    } elseif ($ok == 0) {
        return false;
    } else {
        return false;
    }
    return false;
}

function loadEd25519PrivateKey()
{
    // this is a sample key - don't worry !
    return base64Decoding('Gjho7dILimbXJY8RvmQZEBczDzCYbZonGt/e5QQ3Vro2sjTp95EMxOrgEaWiprPduR/KJFBRA+RlDDpkvmhVfw==');
}

function loadEd25519PublicKey()
{
    // this is a sample key - don't worry !
    return base64Decoding('NrI06feRDMTq4BGloqaz3bkfyiRQUQPkZQw6ZL5oVX8=');
}

function base64Encoding($input)
{
    return base64_encode($input);
}

function base64Decoding($input)
{
    return base64_decode($input);
}

echo 'Libsodium detached signature string' . PHP_EOL;

$dataToSign = "The quick brown fox jumps over the lazy dog";
echo 'dataToSign: ' . $dataToSign . PHP_EOL;

// usually we would load the private and public key from a file or keystore
// here we use hardcoded keys for demonstration - don't do this in real programs

echo PHP_EOL . '* * * sign the plaintext with the ED25519 private key * * *' . PHP_EOL;
$privateKey = loadEd25519PrivateKey();
$signatureBase64 = libsodiumSignDetachedToBase64($privateKey, $dataToSign);
echo 'signature (Base64): ' . $signatureBase64 . PHP_EOL;

echo PHP_EOL . '* * * verify the signature against the plaintext with the ED25519 public key * * *' . PHP_EOL;
$publicKey = loadEd25519PublicKey();
echo 'signature (Base64): ' . $signatureBase64 . PHP_EOL;
$signatureVerified = libsodiumVerifyDetachedFromBase64($publicKey, $dataToSign, $signatureBase64);
if ($signatureVerified == 1) {
    echo 'signature (Base64) verified: ' . 'true' . PHP_EOL;
} else {
    echo 'signature (Base64) verified: ' . 'false' . PHP_EOL;
}

?>
