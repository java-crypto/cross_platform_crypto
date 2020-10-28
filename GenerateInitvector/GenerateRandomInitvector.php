<?php
function generateRandomInitvector()
{
    return openssl_random_pseudo_bytes(16, $crypto_strong);
}

function base64Encoding($input)
{
    return base64_encode($input);
}

echo 'Generate a 16 byte long Initialization vector (IV)' . PHP_EOL;
$iv = generateRandomInitvector();
$ivBase64 = base64Encoding($iv);
echo 'generated iv length: ' . strlen($iv) . ' data: '. $ivBase64 . PHP_EOL;
?>
