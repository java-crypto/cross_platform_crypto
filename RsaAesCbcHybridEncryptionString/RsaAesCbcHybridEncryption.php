<?php
function rsaOaepSha1AesCbc256HybridStringEncryption($publicKey, $plaintext) {
    // generate random key
    $encryptionKey = generateRandomAesKey();
    // encrypt plaintext
    $ciphertextBase64 = aesCbcEncryptToBase64($encryptionKey, $plaintext);
    // encrypt the aes encryption key with the rsa public key
    $encryptionKeyBase64 = base64Encoding(rsaEncryptionOaepSha1($publicKey, $encryptionKey));
    // complete output
    return $encryptionKeyBase64 . ':' . $ciphertextBase64;
}

function rsaOaepSha1AesCbc256HybridStringDecryption ($privateKey, $completeCiphertext) {
    list($encryptionkeyReceived, $iv, $encryptedData) = explode(':', $completeCiphertext, 3);
    $encryptionkeyReceived = base64Decoding($encryptionkeyReceived);
    $iv = base64Decoding($iv);
    $encryptedData = base64Decoding($encryptedData);
    // decrypt the encryption key with the rsa private key
    $decryptionKey = rsaDecryptionOaepSha1($privateKey, $encryptionkeyReceived);
    return aesCbcDecrypt($decryptionKey, $iv, $encryptedData);
}

function aesCbcEncryptToBase64($key, $data)
{
    $iv = generateRandomInitvector();
    $ciphertext = openssl_encrypt($data, 'aes-256-cbc', $key, OPENSSL_RAW_DATA, $iv);
    return base64_encode($iv) . ':' . base64_encode($ciphertext);
}

function aesCbcDecrypt($key, $iv, $data)
{
    return openssl_decrypt($data, 'aes-256-cbc', $key, OPENSSL_RAW_DATA, $iv);
}

function rsaEncryptionOaepSha1($publicKey, $plaintext) {
    openssl_public_encrypt($plaintext, $ciphertext, $publicKey, OPENSSL_PKCS1_OAEP_PADDING);
    return $ciphertext;
 }

function rsaDecryptionOaepSha1($privateKey, $ciphertext) {
    openssl_private_decrypt($ciphertext, $decryptedtext, $privateKey, OPENSSL_PKCS1_OAEP_PADDING);
    return $decryptedtext;
}

function generateRandomAesKey()
{
    return openssl_random_pseudo_bytes(32, $crypto_strong);
}

function generateRandomInitvector()
{
    return openssl_random_pseudo_bytes(16, $crypto_strong);
}

function base64Encoding($input) {return base64_encode($input);}
function base64Decoding($input){return base64_decode($input);}

function loadRsaPrivateKeyPem() {
    // this is a sample key - don't worry !
    return '
-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDwSZYlRn86zPi9
e1RTZL7QzgE/36zjbeCMyOhf6o/WIKeVxFwVbG2FAY3YJZIxnBH+9j1XS6f+ewjG
FlJY4f2IrOpS1kPiO3fmOo5N4nc8JKvjwmKtUM0t63uFFPfs69+7mKJ4w3tk2mSN
4gb8J9P9BCXtH6Q78SdOYvdCMspA1X8eERsdLb/jjHs8+gepKqQ6+XwZbSq0vf2B
MtaAB7zTX/Dk+ZxDfwIobShPaB0mYmojE2YAQeRq1gYdwwO1dEGk6E5J2toWPpKY
/IcSYsGKyFqrsmbw0880r1BwRDer4RFrkzp4zvY+kX3eDanlyMqDLPN+ghXT1lv8
snZpbaBDAgMBAAECggEBAIVxmHzjBc11/73bPB2EGaSEg5UhdzZm0wncmZCLB453
XBqEjk8nhDsVfdzIIMSEVEowHijYz1c4pMq9osXR26eHwCp47AI73H5zjowadPVl
uEAot/xgn1IdMN/boURmSj44qiI/DcwYrTdOi2qGA+jD4PwrUl4nsxiJRZ/x7PjL
hMzRbvDxQ4/Q4ThYXwoEGiIBBK/iB3Z5eR7lFa8E5yAaxM2QP9PENBr/OqkGXLWV
qA/YTxs3gAvkUjMhlScOi7PMwRX9HsrAeLKbLuC1KJv1p2THUtZbOHqrAF/uwHaj
ygUblFaa/BTckTN7PKSVIhp7OihbD04bSRrh+nOilcECgYEA/8atV5DmNxFrxF1P
ODDjdJPNb9pzNrDF03TiFBZWS4Q+2JazyLGjZzhg5Vv9RJ7VcIjPAbMy2Cy5BUff
EFE+8ryKVWfdpPxpPYOwHCJSw4Bqqdj0Pmp/xw928ebrnUoCzdkUqYYpRWx0T7YV
RoA9RiBfQiVHhuJBSDPYJPoP34kCgYEA8H9wLE5L8raUn4NYYRuUVMa+1k4Q1N3X
Bixm5cccc/Ja4LVvrnWqmFOmfFgpVd8BcTGaPSsqfA4j/oEQp7tmjZqggVFqiM2m
J2YEv18cY/5kiDUVYR7VWSkpqVOkgiX3lK3UkIngnVMGGFnoIBlfBFF9uo02rZpC
5o5zebaDImsCgYAE9d5wv0+nq7/STBj4NwKCRUeLrsnjOqRriG3GA/TifAsX+jw8
XS2VF+PRLuqHhSkQiKazGr2Wsa9Y6d7qmxjEbmGkbGJBC+AioEYvFX9TaU8oQhvi
hgA6ZRNid58EKuZJBbe/3ek4/nR3A0oAVwZZMNGIH972P7cSZmb/uJXMOQKBgQCs
FaQAL+4sN/TUxrkAkylqF+QJmEZ26l2nrzHZjMWROYNJcsn8/XkaEhD4vGSnazCu
/B0vU6nMppmezF9Mhc112YSrw8QFK5GOc3NGNBoueqMYy1MG8Xcbm1aSMKVv8xba
rh+BZQbxy6x61CpCfaT9hAoA6HaNdeoU6y05lBz1DQKBgAbYiIk56QZHeoZKiZxy
4eicQS0sVKKRb24ZUd+04cNSTfeIuuXZrYJ48Jbr0fzjIM3EfHvLgh9rAZ+aHe/L
84Ig17KiExe+qyYHjut/SC0wODDtzM/jtrpqyYa5JoEpPIaUSgPuTH/WhO3cDsx6
3PIW4/CddNs8mCSBOqTnoaxh
-----END PRIVATE KEY-----
';
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

echo 'RSA AES CBC hybrid encryption' . PHP_EOL;

echo PHP_EOL . 'uses AES CBC 256 random key for the plaintext encryption' . PHP_EOL;
echo 'and RSA OAEP SHA 1 2048 bit for the encryption of the random key' . PHP_EOL . PHP_EOL;

$dataToEncryptString = "The quick brown fox jumps over the lazy dog";
echo 'plaintext: ' . $dataToEncryptString . PHP_EOL;

// # # # usually we would load the private and public key from a file or keystore # # #
// # # # here we use hardcoded keys for demonstration - don't do this in real programs # # #
$filenamePrivateKeyPem = "privatekey2048.pem";
$filenamePublicKeyPem = "publickey2048.pem";

// encryption
echo PHP_EOL . '* * * Encryption * * *' .PHP_EOL;

$publicKeyLoad = openssl_pkey_get_public(loadRsaPublicKeyPem());
// use this in production
// $publicKeyLoad = openssl_pkey_get_public( file_get_contents($filenamePublicKeyPem));
$completeCiphertextBase64 = rsaOaepSha1AesCbc256HybridStringEncryption($publicKeyLoad, $dataToEncryptString);
echo 'completeCiphertextBase64: ' . $completeCiphertextBase64 . PHP_EOL;

// transport the encrypted data to recipient

// receiving the encrypted data, decryption
echo PHP_EOL . '* * * Decryption * * *' .PHP_EOL;
$completeCiphertextReceivedBase64 = $completeCiphertextBase64;
echo 'completeCiphertextReceivedBase64: ' . $completeCiphertextReceivedBase64 . PHP_EOL;
$privateKeyLoad = openssl_pkey_get_private(loadRsaPrivateKeyPem());
// use this in production
//$privateKeyLoad = openssl_pkey_get_private(file_get_contents($filenamePrivateKeyPem));
$decryptedtext = rsaOaepSha1AesCbc256HybridStringDecryption($privateKeyLoad, $completeCiphertextReceivedBase64);
echo 'decryptedtext: ' . $decryptedtext . PHP_EOL;
?>
