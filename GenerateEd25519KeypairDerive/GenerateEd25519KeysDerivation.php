<?php
function generateEd25519KeyPair()
{
    return sodium_crypto_sign_keypair();
}

function deriveEd25519PublicKey($privateKey)
{
    return sodium_crypto_sign_publickey_from_secretkey ($privateKey);
}

function base64Encoding($input)
{
    return base64_encode($input);
}

echo 'Generate ED25519 private and public key and derive public key from private key' . PHP_EOL;
$keyPair = generateEd25519KeyPair();
$privateKey = sodium_crypto_sign_secretkey($keyPair);
$publicKey = sodium_crypto_sign_publickey($keyPair);
echo'privateKey (Base64): ' . base64Encoding($privateKey) . PHP_EOL;
echo'publicKey (Base64):  ' . base64Encoding($publicKey) . PHP_EOL;

echo PHP_EOL . 'derive the publicKey from the privateKey' . PHP_EOL;
$publicKeyDerived = deriveEd25519PublicKey($privateKey);
echo'publicKey (Base64):  ' . base64Encoding($publicKeyDerived) . PHP_EOL;
?>
