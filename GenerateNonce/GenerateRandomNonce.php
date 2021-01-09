<?php
function generateRandomNonce()
{
    return openssl_random_pseudo_bytes(12, $crypto_strong);
}

function base64Encoding($input)
{
    return base64_encode($input);
}

echo 'Generate a 12 byte long nonce for AES GCM' . PHP_EOL;
$nonce = generateRandomNonce();
$nonceBase64 = base64Encoding($nonce);
echo 'generated nonce length: ' . strlen($nonce) . ' data: '. $nonceBase64 . PHP_EOL;
?>
