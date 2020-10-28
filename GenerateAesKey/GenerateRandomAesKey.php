<?php
function generateRandomAesKey()
{
    return openssl_random_pseudo_bytes(32, $crypto_strong);
}

function base64Encoding($input)
{
    return base64_encode($input);
}

echo 'Generate a 32 byte long AES key' . PHP_EOL;
$aesKey = generateRandomAesKey();
$aesKeyBase64 = base64Encoding($aesKey);
echo 'generated key length: ' . strlen($aesKey) . ' data: '. $aesKeyBase64 . PHP_EOL;
?>
