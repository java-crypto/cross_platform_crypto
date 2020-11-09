<?php
function rsaVerifySignatureFromBase64 ($publicKey, $dataToSign, $signatureBase64) {
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

function loadRsaPublicKeyPem() {
    // this is a sample key - don't worry !
    return '
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9
iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT
/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8
01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB
ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g
QwIDAQAB
-----END PUBLIC KEY-----
';
}

echo 'RSA signature string verification only' . PHP_EOL;

$dataToSign = "The quick brown fox jumps over the lazy dog";
echo 'dataToSign: ' . $dataToSign . PHP_EOL;

// paste signature here:
$signatureBase64 = "vCSB4744p30IBl/sCLjvKm2nix7wfScQn99YX9tVIDNQIvU3QnSCLc2cF+J6R9WMRIXOsY94MxjKCQANW0CuaSs+w31ePHaounFVnmXyY092SicZrtpwlxw2CHqJ0NSyciDpxlRId1vjKlp9E5IJmYtVMtL2hfb711P+nb+m+1sPplNXPpJpdnWIzfLsDMVxCkplAdrcoH2HuWgOtOCHAf3vWbUC/vkvi388NT1UXJRPoERM0m1v11ogP9DiycMdoJxg3fbdH3HknbR02MLNEr7q4ZMzlrKxnYChwp2hnnBvJDcXpPDnQz7sG8zrim1nL/PS8CRG5lxhYYAZqTc+Vg==";

// usually we would load the public key from a file or keystore
// here we use hardcoded key for demonstration - don't do this in real programs

echo PHP_EOL . '* * * verify the signature against the plaintext with the RSA public key * * *' . PHP_EOL;
$publicRsaKeyPem = loadRsaPublicKeyPem();
$rsaPublicKey = openssl_pkey_get_public($publicRsaKeyPem);
echo 'used public key:' . $publicRsaKeyPem . PHP_EOL;
$signatureVerified = rsaVerifySignatureFromBase64($rsaPublicKey, $dataToSign, $signatureBase64);
//$signatureVerified = rsaVerifySignatureFromBase64($rsaPublicKey, 'test', $signatureBase64);
if ($signatureVerified == 1) {
    echo 'signature (Base64) verified: ' . 'true'. PHP_EOL;
} else {
    echo 'signature (Base64) verified: ' . 'false'. PHP_EOL;
}
?>
