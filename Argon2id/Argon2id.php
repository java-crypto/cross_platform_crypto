<?php
// ### the minimal parameter set is probably UNSECURE ###
function generateArgon2idMinimal($password, $salt) {
    $opsLimit= 2;
    $memLimit = 8192 * 1024;
    $outputLength =  32;
    return sodium_crypto_pwhash ($outputLength, $password, $salt,   $opsLimit, $memLimit, SODIUM_CRYPTO_PWHASH_ALG_ARGON2ID13);
}

function generateArgon2idInteractive($password, $salt) {
    $opsLimit= 2;
    $memLimit = 66536 * 1024;
    $outputLength =  32;
    return sodium_crypto_pwhash ($outputLength, $password, $salt,   $opsLimit, $memLimit, SODIUM_CRYPTO_PWHASH_ALG_ARGON2ID13);
}

function generateArgon2idModerate($password, $salt) {
    $opsLimit= 3;
    $memLimit = 262144 * 1024;
    $outputLength =  32;
    return sodium_crypto_pwhash ($outputLength, $password, $salt,   $opsLimit, $memLimit, SODIUM_CRYPTO_PWHASH_ALG_ARGON2ID13);
}

function generateArgon2idSensitive($password, $salt) {
    $opsLimit= 4;
    $memLimit = 1048576 * 1024;
    $outputLength =  32;
    return sodium_crypto_pwhash ($outputLength, $password, $salt,   $opsLimit, $memLimit, SODIUM_CRYPTO_PWHASH_ALG_ARGON2ID13);
}

function base64Encoding($input)
{
    return base64_encode($input);
}

function generateSalt16Byte()
{
    return openssl_random_pseudo_bytes(16, $crypto_strong);
}

function generateFixedSalt16Byte()
{
    // ### security warning - never use this in production ###
    return "\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00";
}

echo 'Generate a 32 byte long encryption key with Argon2id' . PHP_EOL;

$password = "secret password";
echo 'password: ' . $password . PHP_EOL;

// ### security warning - never use a fixed salt in production, this is for compare reasons only
//$salt = generateFixedSalt32Byte();
$salt = generateFixedSalt16Byte();
// please use below generateSalt16Byte()
//$salt = generateSalt16Byte();
echo 'salt (Base64): ' . base64Encoding($salt) . PHP_EOL;

// ### the minimal parameter set is probably UNSECURE ###
$encryptionKeyArgon2id = generateArgon2idMinimal($password, $salt);
echo 'encryptionKeyArgon2id (Base64) minimal:     ' . base64Encoding($encryptionKeyArgon2id) . PHP_EOL;

$encryptionKeyArgon2id = generateArgon2idInteractive($password, $salt);
echo 'encryptionKeyArgon2id (Base64) interactive: ' . base64Encoding($encryptionKeyArgon2id) . PHP_EOL;

$encryptionKeyArgon2id = generateArgon2idModerate($password, $salt);
echo 'encryptionKeyArgon2id (Base64) moderate:    ' . base64Encoding($encryptionKeyArgon2id) . PHP_EOL;

$encryptionKeyArgon2id = generateArgon2idSensitive($password, $salt);
echo 'encryptionKeyArgon2id (Base64) sensitive:   ' . base64Encoding($encryptionKeyArgon2id) . PHP_EOL;
?>
