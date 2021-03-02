<?php
// uses phpseclib version 3
include 'vendor/autoload.php';

use phpseclib3\Crypt\RSA;
use phpseclib3\Crypt\PublicKeyLoader;
use phpseclib3\Crypt\Random;
use phpseclib3\Crypt\AES;

function loadRsaPrivateKeyPem()
{
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

function loadRsaPublicKeyPem()
{
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

function loadRsaPublicKeyPem2()
{
    // this is a sample key - don't worry !
    return '
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9
iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT
/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8
01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB
ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW3g
QwIDAQAB
-----END PUBLIC KEY-----
';
}

function buildJweHeaderObject($algorithm, $encryptionAlgorithm)
{
    return json_encode([
        'alg' => $algorithm,
        'enc' => $encryptionAlgorithm,
        'typ' => 'JWT',
    ]);
}

function buildJwePayloadObject($data, $subject, $issuer, $tokenExpirationInSeconds)
{
    return json_encode([
        'dat' => $data,
        'sub' => $subject,
        'iss' => $issuer,
        'iat' => time(),
        'exp' => time() + $tokenExpirationInSeconds,
    ]);
}

function generateRandomAesKey($keyLength)
{
    return Random::string($keyLength);
}

function generateRandomNonce()
{
    return Random::string(12);
}

function printJweHeaderObject($jwsSignedTokenObject)
{
    list($headerBase64Url, $payloadBase64Url, $signatureBase64Url) = explode('.', $jwsSignedTokenObject, 3);
    return decodeBase64Url($headerBase64Url);
}

function getJweHeaderKeyAlgorithm($jweHeaderBase64Url)
{
    $array = json_decode(decodeBase64Url($jweHeaderBase64Url), true);
    return $array['alg'];
}

function getJweHeaderEncryptionAlgorithm($jweHeaderBase64Url)
{
    $array = json_decode(decodeBase64Url($jweHeaderBase64Url), true);
    return $array['enc'];
}

function printPayloadObject($jwsSignedTokenObject)
{
    list($headerBase64Url, $payloadBase64Url, $signatureBase64Url) = explode('.', $jwsSignedTokenObject, 3);
    return decodeBase64Url($payloadBase64Url);
}

function printExpirationData($jwsSignedTokenObject)
{
    list($headerBase64Url, $payloadBase64Url, $signatureBase64Url) = explode('.', $jwsSignedTokenObject, 3);
    $payload = decodeBase64Url($payloadBase64Url);
    $array = json_decode($payload, true);
    $timeExpiration = $array['exp'];
    $timeNow = time();
    $unix_timestamp = $timeNow;
    $datetime = new DateTime("@$unix_timestamp");
    // Display GMT datetime
    $actualTime = $datetime->format('d-m-Y H:i:s');
    $unix_timestamp = $timeExpiration;
    $datetime = new DateTime("@$unix_timestamp");
    // Display GMT datetime
    $expirationTime = $datetime->format('d-m-Y H:i:s');
    return 'actual time:     ' . $actualTime . ' GMT' . PHP_EOL .
        'expiration time: ' . $expirationTime . ' GMT';
}

function checkExpiration($jwsSignedTokenObject)
{
    list($headerBase64Url, $payloadBase64Url, $signatureBase64Url) = explode('.', $jwsSignedTokenObject, 3);
    $payload = decodeBase64Url($payloadBase64Url);
    $array = json_decode($payload, true);
    $expirationTime = $array['exp'];
    $timeNow = time();
    if ($expirationTime > $timeNow) {
        return 'true';
    } else {
        return 'false';
    }
    return 'false';
}

/**
 * from: http://www.php.net/manual/en/function.base64-encode.php#103849
 * @param $data
 * @return string
 */
function encodeBase64Url($data)
{
    return rtrim(strtr(base64_encode($data), '+/', '-_'), '=');
}

/**
 * from: http://www.php.net/manual/en/function.base64-encode.php#103849
 * @param $data
 * @return string
 */
function decodeBase64Url($data)
{
    return base64_decode(str_pad(strtr($data, '-_', '+/'), strlen($data) % 4, '=', STR_PAD_RIGHT));
}

function base64Encoding($input)
{
    return base64_encode($input);
}

function base64Decoding($input)
{
    return base64_decode($input);
}

function rsaEncryptionOaepToBase64Url($rsaPublicKeyFromRecipient, $jweKeyAlgorithm, $jweEncryptionKey)
{
    switch ($jweKeyAlgorithm) {
        case 'RSA1_5':
        {
            $rsa = PublicKeyLoader::load($rsaPublicKeyFromRecipient)
                ->withPadding(RSA::ENCRYPTION_PKCS1);
            return encodeBase64Url($rsa->encrypt($jweEncryptionKey));
            break;
        }
        case 'RSA-OAEP':
        {
            $rsa = PublicKeyLoader::load($rsaPublicKeyFromRecipient)
                ->withHash('sha1')
                ->withMGFHash('sha1');
            return encodeBase64Url($rsa->encrypt($jweEncryptionKey));
            break;
        }
        case 'RSA-OAEP-256':
        {
            $rsa = PublicKeyLoader::load($rsaPublicKeyFromRecipient)
                ->withHash('sha256')
                ->withMGFHash('sha256');
            return encodeBase64Url($rsa->encrypt($jweEncryptionKey));
            break;
        }
        default:
        {
            // not supported algorithm
            return '*** not supported algorithm ***';
        }
    }
    // not supported algorithm
    return '*** not supported algorithm ***';
}

function jweRsaEncryptToBase64UrlToken($rsaPublicKeyFromRecipient, $jweKeyAlgorithm, $jweEncryptionAlgorithm, $jwePayloadObject)
{
    // build a header depending on the key algorithm
    $jweHeaderObject = buildJweHeaderObject($jweKeyAlgorithm, $jweEncryptionAlgorithm);
    $jweHeaderObjectBase64Url = encodeBase64Url($jweHeaderObject);
    // encrypt the payload with a random generated aes key (16 or 32 byte length
    $encryptionKey = "";
    switch ($jweEncryptionAlgorithm) {
        case 'A128GCM':
        {
            $key = generateRandomAesKey(16);
            $nonce = generateRandomNonce();
            $cipher = new AES('gcm');
            $cipher->setKey($key);
            $cipher->setNonce($nonce);
            $cipher->setAAD($jweHeaderObjectBase64Url);
            $ciphertext = $cipher->encrypt($jwePayloadObject);
            $gcmTag = $cipher->getTag();
            $encryptedKeyBase64Url = rsaEncryptionOaepToBase64Url($rsaPublicKeyFromRecipient, $jweKeyAlgorithm, $key);
            return $jweHeaderObjectBase64Url . '.' . $encryptedKeyBase64Url . '.' . encodeBase64Url($nonce) . '.' . encodeBase64Url($ciphertext) . '.' . encodeBase64Url($gcmTag);
            break;
        }
        case 'A192GCM':
        {
            $key = generateRandomAesKey(24);
            $nonce = generateRandomNonce();
            $cipher = new AES('gcm');
            $cipher->setKey($key);
            $cipher->setNonce($nonce);
            $cipher->setAAD($jweHeaderObjectBase64Url);
            $ciphertext = $cipher->encrypt($jwePayloadObject);
            $gcmTag = $cipher->getTag();
            $encryptedKeyBase64Url = rsaEncryptionOaepToBase64Url($rsaPublicKeyFromRecipient, $jweKeyAlgorithm, $key);
            return $jweHeaderObjectBase64Url . '.' . $encryptedKeyBase64Url . '.' . encodeBase64Url($nonce) . '.' . encodeBase64Url($ciphertext) . '.' . encodeBase64Url($gcmTag);
            break;
        }
        case 'A256GCM':
        {
            $key = generateRandomAesKey(32);
            $nonce = generateRandomNonce();
            $cipher = new AES('gcm');
            $cipher->setKey($key);
            $cipher->setNonce($nonce);
            $cipher->setAAD($jweHeaderObjectBase64Url);
            $ciphertext = $cipher->encrypt($jwePayloadObject);
            $gcmTag = $cipher->getTag();
            $encryptedKeyBase64Url = rsaEncryptionOaepToBase64Url($rsaPublicKeyFromRecipient, $jweKeyAlgorithm, $key);
            return $jweHeaderObjectBase64Url . '.' . $encryptedKeyBase64Url . '.' . encodeBase64Url($nonce) . '.' . encodeBase64Url($ciphertext) . '.' . encodeBase64Url($gcmTag);
            break;
        }
        default:
        {
            // not supported algorithm
            return '*** not supported algorithm ***';
        }
    }
    return '*** not supported algorithm ***';
}

function jweRsaDecryptFromBase64UrlToken($rsaPrivateKey, $jweTokenBase64Url)
{
    list($jweHeaderObjectBase64Url, $jweEncryptedKeyBase64Url, $nonceBase64Url, $encryptedDataBase64Url, $gcmTagBase64Url) = explode('.', $jweTokenBase64Url, 5);
    $keyAlgorithm = getJweHeaderKeyAlgorithm($jweHeaderObjectBase64Url);
    $encryptionAlgorithm = getJweHeaderEncryptionAlgorithm($jweHeaderObjectBase64Url);
    $aesDecryptionKey = "";
    switch ($keyAlgorithm) {
        case 'RSA1_5':
        {
            $rsa = PublicKeyLoader::load($rsaPrivateKey)
                ->withPadding(RSA::ENCRYPTION_PKCS1);
            $aesDecryptionKey = $rsa->decrypt(decodeBase64Url($jweEncryptedKeyBase64Url));
            break;
        }
        case 'RSA-OAEP':
        {
            $rsa = PublicKeyLoader::load($rsaPrivateKey)
                ->withHash('sha1')
                ->withMGFHash('sha1');
            $aesDecryptionKey = $rsa->decrypt(decodeBase64Url($jweEncryptedKeyBase64Url));
            break;
        }
        case 'RSA-OAEP-256':
        {
            $rsa = PublicKeyLoader::load($rsaPrivateKey)
                ->withHash('sha256')
                ->withMGFHash('sha256');
            $aesDecryptionKey = $rsa->decrypt(decodeBase64Url($jweEncryptedKeyBase64Url));
            break;
        }
        default:
        {
            // not supported algorithm
            return '*** not supported algorithm ***';
        }
    }
    switch ($encryptionAlgorithm) {
        case 'A128GCM' || 'A192GCM' || 'A256GCM':
        {
            $nonce = decodeBase64Url($nonceBase64Url);
            $ciphertext = decodeBase64Url($encryptedDataBase64Url);
            $gcmTag = decodeBase64Url($gcmTagBase64Url);
            $cipher = new AES('gcm');
            $cipher->setKey($aesDecryptionKey);
            $cipher->setNonce($nonce);
            $cipher->setAAD($jweHeaderObjectBase64Url);
            $cipher->setTag($gcmTag);
            return $cipher->decrypt($ciphertext);
            break;
        }
        default:
            {
                // not supported algorithm
                return '*** not supported algorithm ***';
            }
            return '*** not supported algorithm ***';
    }
}

echo PHP_EOL;

echo 'JWT JWE RSA-OAEP-256 AES GCM 256 encryption' . PHP_EOL;

$jweKeyAlgorithm = 'RSA-OAEP-256'; // supported key algorithms: RSA-OAEP, RSA-OAEP-256, RSA1_5
$jweEncryptionAlgorithm = 'A256GCM'; // supported encryption algorithms: A128GCM, A192GCM, A256GCM

echo 'jwe key algorithm:        ' . $jweKeyAlgorithm . PHP_EOL;
echo 'jwe encryption algorithm: ' . $jweEncryptionAlgorithm . PHP_EOL;

// build the payload to encrypt
$dataToEncrypt = 'The quick brown fox jumps over the lazy dog';
$issuer = 'https://java-crypto.github.io/cross_platform_crypto/';
$subject = 'JWE RSA-OAEP & AES GCM encryption';
$tokenExpirationInSeconds = 120;
$jwePayloadObject = buildJwePayloadObject($dataToEncrypt, $subject, $issuer, $tokenExpirationInSeconds);
echo 'jwePayloadObject: ' . $jwePayloadObject . PHP_EOL;

echo PHP_EOL . '* * * encrypt the payload with recipient\'s public key * * *' . PHP_EOL;
// get public key from recipient
$rsaPublicKeyFromRecipient = loadRsaPublicKeyPem();
// encrypt the data and build the encrypted jwe token
$jweTokenBase64Url = jweRsaEncryptToBase64UrlToken($rsaPublicKeyFromRecipient, $jweKeyAlgorithm, $jweEncryptionAlgorithm, $jwePayloadObject);
echo 'jweToken (Base64Url encoded):' . PHP_EOL . $jweTokenBase64Url . PHP_EOL;

echo PHP_EOL . '* * * decrypt the payload with recipient\'s private key * * *' . PHP_EOL;
// get private key
$rsaPrivateKey = loadRsaPrivateKeyPem();
$jweDecryptedPayload = jweRsaDecryptFromBase64UrlToken($rsaPrivateKey, $jweTokenBase64Url);
echo 'jweDecryptedPayload: ' . $jweDecryptedPayload . PHP_EOL;
?>
