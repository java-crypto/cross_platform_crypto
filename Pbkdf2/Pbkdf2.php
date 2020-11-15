<?php
function generateAes256KeyPbkdf2Sha512($password, $iterations, $salt)
{
    return hash_pbkdf2("sha512", $password, $salt, $iterations, 32, $raw_output = true);
}

function generateAes256KeyPbkdf2Sha256($password, $iterations, $salt)
{
    return hash_pbkdf2("sha256", $password, $salt, $iterations, 32, $raw_output = true);
}

function generateAes256KeyPbkdf2Sha1($password, $iterations, $salt)
{
    return hash_pbkdf2("sha1", $password, $salt, $iterations, 32, $raw_output = true);
}

function generateSalt32Byte()
{
    return openssl_random_pseudo_bytes(32, $crypto_strong);
}

function generateFixedSalt32Byte()
{
    // ### security warning - never use this in production ###
    return "\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00";
}

echo 'Generate a 32 byte long AES key with PBKDF2' . PHP_EOL;
$password = "secret password";
$PBKDF2_ITERATIONS = 15000;  // number of iterations, higher is better but slower

// ### security warning - never use a fixed salt in production, this is for compare reasons only
$salt = generateFixedSalt32Byte();
// please use below generateSalt32Byte()
//$salt = generateSalt32Byte();
$aesKeySha512 = generateAes256KeyPbkdf2Sha512($password, $PBKDF2_ITERATIONS, $salt);
echo 'aesKeySha512 length: ' . strlen($aesKeySha512) . ' data: '. bin2hex($aesKeySha512) . PHP_EOL;
$aesKeySha256 = generateAes256KeyPbkdf2Sha256($password, $PBKDF2_ITERATIONS, $salt);
echo 'aesKeySha256 length: ' . strlen($aesKeySha256) . ' data: '. bin2hex($aesKeySha256) . PHP_EOL;
$aesKeySha1 = generateAes256KeyPbkdf2Sha1($password, $PBKDF2_ITERATIONS, $salt);
echo 'aesKeySha1   length: ' . strlen($aesKeySha1) . ' data: '. bin2hex($aesKeySha1) . PHP_EOL;
?>
