<?php

require_once ('Curve25519/Curve25519.php');

function generateCurve25519PrivateKey()
{
    return openssl_random_pseudo_bytes(32, $crypto_strong);
}

function calculateCurve25519PublicKey($secret)
{
    return (new Curve25519\Curve25519)->publicKey($secret);
}

function base64Encoding($input)
{
    return base64_encode($input);
}

echo 'Generate a key pair for Curve25519' . PHP_EOL;
// you need this library: https://github.com/lt/PHP-Curve25519

$privateKey = generateCurve25519PrivateKey();
$publicKey = calculateCurve25519PublicKey($privateKey);
echo 'base64 encoded key data:' . PHP_EOL;
echo 'Curve25519 PrivateKey: ' . base64Encoding($privateKey) . PHP_EOL;
echo 'Curve25519 PublicKey:  ' . base64Encoding($publicKey) . PHP_EOL;
?>
