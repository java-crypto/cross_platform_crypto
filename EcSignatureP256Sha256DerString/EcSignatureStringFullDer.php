<?php
function ecSignToBase64 ($privateKey, $dataToSign) {
    openssl_sign($dataToSign, $signature, $privateKey, OPENSSL_ALGO_SHA256);
    return base64_encode($signature);
}

function ecVerifySignatureFromBase64 ($publicKey, $dataToSign, $signatureBase64) {
    $ok = openssl_verify($dataToSign, base64_decode($signatureBase64), $publicKey, OPENSSL_ALGO_SHA256);
    if ($ok == 1) {
        return true;
    } elseif ($ok == 0) {
        return false;
    } else {
        return false;
    }
    return false;
}

function loadEcPrivateKeyPem() {
    // this is a sample key - don't worry !
    return '
-----BEGIN PRIVATE KEY-----
MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgciPt/gulzw7/Xe12
YOu/vLUgIUZ+7gGo5VkmU0B+gUWhRANCAAQxkee3UPW110s0aUQdcS0TDkr8blAe
SBouL4hXziiJX5Me/8OobFgNfYXkk6R/K/fqJhJ/mV8gLur16XhgueXA
-----END PRIVATE KEY-----
';
}

function loadEcPublicKeyPem() {
    // this is a sample key - don't worry !
    return '
-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEMZHnt1D1tddLNGlEHXEtEw5K/G5Q
HkgaLi+IV84oiV+THv/DqGxYDX2F5JOkfyv36iYSf5lfIC7q9el4YLnlwA==
-----END PUBLIC KEY-----
';
}

echo 'EC signature string (ECDSA with SHA256) DER-encoding' . PHP_EOL;

$dataToSign = "The quick brown fox jumps over the lazy dog";
echo 'dataToSign: ' . $dataToSign . PHP_EOL;

// usually we would load the private and public key from a file or keystore
// here we use hardcoded keys for demonstration - don't do this in real programs

echo PHP_EOL . '* * * sign the plaintext with the EC private key * * *' . PHP_EOL;
$privateEcKeyPem = loadEcPrivateKeyPem();
$ecPrivateKey = openssl_pkey_get_private($privateEcKeyPem);
echo 'used private key:' . $privateEcKeyPem . PHP_EOL;
$signatureBase64 = ecSignToBase64($ecPrivateKey, $dataToSign);
echo 'signature (Base64): ' . $signatureBase64 . PHP_EOL;

echo PHP_EOL . '* * * verify the signature against the plaintext with the EC public key * * *' . PHP_EOL;
$publicEcKeyPem = loadEcPublicKeyPem();
$ecPublicKey = openssl_pkey_get_public($publicEcKeyPem);
echo 'used public key:' . $publicEcKeyPem . PHP_EOL;
$signatureVerified = ecVerifySignatureFromBase64($ecPublicKey, $dataToSign, $signatureBase64);
//$signatureVerified = rsaVerifySignatureFromBase64($rsaPublicKey, 'test', $signatureBase64);
if ($signatureVerified == 1) {
    echo 'signature (Base64) verified: ' . 'true'. PHP_EOL;
} else {
    echo 'signature (Base64) verified: ' . 'false'. PHP_EOL;
}
?>
