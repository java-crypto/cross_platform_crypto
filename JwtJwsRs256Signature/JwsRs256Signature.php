<?php
// uses phpseclib version 3
include 'vendor/autoload.php';

use phpseclib3\Crypt\RSA;
use phpseclib3\Crypt\PublicKeyLoader;

function rsaSignPkcs1ToBase64Url($privateKey, $hash, $dataToSign)
{
    $rsaSign = PublicKeyLoader::load($privateKey);
    $rsaSign = $rsaSign
        ->withPadding(RSA::SIGNATURE_PKCS1)
        ->withHash($hash);
    return encodeBase64Url($rsaSign->sign($dataToSign));
}

function rsaVerifySignaturePkcs1FromBase64Url($publicKey, $hash, $dataToVerify, $signatureBase64Url)
{
    $rsaVerify = PublicKeyLoader::load($publicKey)
        ->withPadding(RSA::SIGNATURE_PKCS1)
        ->withHash($hash);
    $ok = $rsaVerify->verify($dataToVerify, decodeBase64Url($signatureBase64Url));
    if ($ok == 1) {
        return true;
    } elseif ($ok == 0) {
        return false;
    } else {
        return false;
    }
    return false;
}

function rsaSignPssToBase64Url($privateKey, $hash, $dataToSign)
{
    $rsaSign = PublicKeyLoader::load($privateKey);
    $rsaSign = $rsaSign
        ->withPadding(RSA::SIGNATURE_PSS)
        ->withHash($hash)
        ->withMGFHash('sha256')
        ->withSaltLength(32);
    return encodeBase64Url($rsaSign->sign($dataToSign));
}

function rsaVerifySignaturePssFromBase64Url($publicKey, $hash, $dataToSign, $signatureBase64)
{
    $rsaVerify = PublicKeyLoader::load($publicKey)
        ->withHash($hash)
        ->withMGFHash('sha256')
        ->withSaltLength(32);
    $ok = $rsaVerify->verify($dataToSign, decodeBase64Url($signatureBase64));
    if ($ok == 1) {
        return true;
    } elseif ($ok == 0) {
        return false;
    } else {
        return false;
    }
    return false;
}

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

function buildHeaderObject($algorithm)
{
    return json_encode([
        'alg' => $algorithm,
        'typ' => 'JWT',
    ]);
}

function buildPayloadObject($subject, $issuer, $tokenExpirationInSeconds)
{
    return json_encode([
        'sub' => $subject,
        'iss' => $issuer,
        'iat' => time(),
        'exp' => time() + $tokenExpirationInSeconds,
    ]);
}

function jwsSignatureToString($jwsAlgorithm, $jwkRsaPrivateKey, $jwtPayloadObject)
{
    $headerBase64UrlEncoded = encodeBase64Url(buildHeaderObject($jwsAlgorithm));
    $payloadBase64UrlEncoded = encodeBase64Url($jwtPayloadObject);
    $tokenToSign = $headerBase64UrlEncoded . '.' . $payloadBase64UrlEncoded;
    // depending on jwsAlgorithm we sign the payload
    switch ($jwsAlgorithm) {
        case 'RS256': {
            $signatureBase64Url = rsaSignPkcs1ToBase64Url($jwkRsaPrivateKey, 'sha256', $tokenToSign);
            return $tokenToSign . '.' . $signatureBase64Url;
            break;
        }
        case 'RS384': {
            $signatureBase64Url = rsaSignPkcs1ToBase64Url($jwkRsaPrivateKey, 'sha384', $tokenToSign);
            return $tokenToSign . '.' . $signatureBase64Url;
            break;
        }
        case 'RS512': {
            $signatureBase64Url = rsaSignPkcs1ToBase64Url($jwkRsaPrivateKey, 'sha512', $tokenToSign);
            return $tokenToSign . '.' . $signatureBase64Url;
            break;
        }
        case 'PS256': {
            $signatureBase64Url = rsaSignPssToBase64Url($jwkRsaPrivateKey, 'sha256', $tokenToSign);
            return $tokenToSign . '.' . $signatureBase64Url;
            break;
        }
        case 'PS384': {
            $signatureBase64Url = rsaSignPssToBase64Url($jwkRsaPrivateKey, 'sha384', $tokenToSign);
            return $tokenToSign . '.' . $signatureBase64Url;
            break;
        }
        case 'PS512': {
            $signatureBase64Url = rsaSignPssToBase64Url($jwkRsaPrivateKey, 'sha512', $tokenToSign);
            return $tokenToSign . '.' . $signatureBase64Url;
            break;
        }
        default: {
            // not supported algorithm
            return '*** not supported algorithm ***';
        }
    }
    return '*** not supported algorithm ***';
}

function jwsSignatureVerification($jwkRsaPublicKey, $jwsSignedTokenReceived) {
    list($headerBase64Url, $payloadBase64Url, $signatureBase64Url) = explode('.', $jwsSignedTokenReceived, 3);
    $signatureAlgorithm = getHeaderAlgorithm($headerBase64Url);
    $tokenToVerify = $headerBase64Url . '.' . $payloadBase64Url;
    switch ($signatureAlgorithm) {
        case 'RS256':
        {
            return rsaVerifySignaturePkcs1FromBase64Url($jwkRsaPublicKey, 'sha256', $tokenToVerify, $signatureBase64Url);
            break;
        }
        case 'RS384':
        {
            return rsaVerifySignaturePkcs1FromBase64Url($jwkRsaPublicKey, 'sha384', $tokenToVerify, $signatureBase64Url);
            break;
        }
        case 'RS512':
        {
            return rsaVerifySignaturePkcs1FromBase64Url($jwkRsaPublicKey, 'sha512', $tokenToVerify, $signatureBase64Url);
            break;
        }
        case 'PS256':
        {
            return rsaVerifySignaturePssFromBase64Url($jwkRsaPublicKey, 'sha256', $tokenToVerify, $signatureBase64Url);
            break;
        }
        case 'PS384':
        {
            return rsaVerifySignaturePssFromBase64Url($jwkRsaPublicKey, 'sha384', $tokenToVerify, $signatureBase64Url);
            break;
        }
        case 'PS512':
        {
            return rsaVerifySignaturePssFromBase64Url($jwkRsaPublicKey, 'sha512', $tokenToVerify);
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

function printHeaderObject($jwsSignedTokenObject) {
    list($headerBase64Url, $payloadBase64Url, $signatureBase64Url) = explode('.', $jwsSignedTokenObject, 3);
    return decodeBase64Url($headerBase64Url);
}

function getHeaderAlgorithm($headerBase64Url) {
    $array = json_decode(decodeBase64Url($headerBase64Url), true);
    return $array['alg'];
}

function printPayloadObject($jwsSignedTokenObject) {
    list($headerBase64Url, $payloadBase64Url, $signatureBase64Url) = explode('.', $jwsSignedTokenObject, 3);
    return decodeBase64Url($payloadBase64Url);
}

function printExpirationData($jwsSignedTokenObject) {
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

function checkExpiration($jwsSignedTokenObject) {
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
{ return rtrim(strtr(base64_encode($data), '+/', '-_'), '='); }

/**
 * from: http://www.php.net/manual/en/function.base64-encode.php#103849
 * @param $data
 * @return string
 */
function decodeBase64Url($data)
{ return base64_decode(str_pad(strtr($data, '-_', '+/'), strlen($data) % 4, '=', STR_PAD_RIGHT)); }

function base64Encoding($input)
{ return base64_encode($input); }

function base64Decoding($input)
{ return base64_decode($input); }

echo 'JWT JWS RS256 RSA PKCS#1.5 signature with SHA-256' . PHP_EOL;

$jwsAlgorithm = "RS256";
// these algorithms are supported:
// pkcs1.5 padding: RS256, RS384, RS512
// ssa-pss padding: PS256, PS384, PS512

// usually we would load the private and public key from a file or keystore
// here we use hardcoded keys for demonstration - don't do this in real programs

echo PHP_EOL . '* * * sign the payload object with the RSA private key * * *' . PHP_EOL;

// payload data
$issuer = 'https://java-crypto.github.io/cross_platform_crypto/';
// for this demo - the subject depends on the jwsAlgorithm
$subject = 'JWT ' . $jwsAlgorithm . ' signature'; // e.g. JWT RS256 algorithm
$tokenExpirationInSeconds = 120;

// build a JWT payload object
$jwtPayloadObject = buildPayloadObject($subject, $issuer, $tokenExpirationInSeconds);
echo 'jwtPayloadObject: ' . $jwtPayloadObject . PHP_EOL;

// build a header object
$jwtHeader = buildHeaderObject($jwsAlgorithm);
echo 'headerObject: ' . $jwtHeader . PHP_EOL;

// read RSA private key
$jwkRsaPrivateKey = loadRsaPrivateKeyPem();
//var_dump($jwkRsaPrivateKey);
echo 'private RSA key loaded' . PHP_EOL;

// sign the payload
$jwsSignedToken = jwsSignatureToString($jwsAlgorithm, $jwkRsaPrivateKey, $jwtPayloadObject);
echo 'signed jwsToken:' . PHP_EOL . $jwsSignedToken . PHP_EOL;

// send the token to the recipient
echo PHP_EOL . '* * * verify the jwsToken with the RSA public key * * *' . PHP_EOL;
$jwsSignedTokenReceived = $jwsSignedToken;

// read the RSA public key
$jwkRsaPublicKey = loadRsaPublicKeyPem();
//var_dump($jwkRsaPublicKey);
echo 'public RSA key loaded' . PHP_EOL;

// show payloadObject
echo 'payloadObject: ' . printPayloadObject($jwsSignedTokenReceived) . PHP_EOL;

// check for correct signature
echo PHP_EOL . 'check signature with matching public key' . PHP_EOL;
$tokenVerified = jwsSignatureVerification($jwkRsaPublicKey, $jwsSignedTokenReceived);
if ($tokenVerified == 1) {
    echo 'the token\'s signature is verified: ' . 'true' . PHP_EOL;
} else {
    echo 'the token\'s signature is verified: ' . 'false' . PHP_EOL;
}

echo PHP_EOL . 'check signature with not matching public key' . PHP_EOL;
$jwkRsaPublicKey2 = loadRsaPublicKeyPem2();
$tokenNotVerified = jwsSignatureVerification($jwkRsaPublicKey2, $jwsSignedTokenReceived);
if ($tokenNotVerified == 1) {
    echo 'the token\'s signature is verified: ' . 'true' . PHP_EOL;
} else {
    echo 'the token\'s signature is verified: ' . 'false' . PHP_EOL;
}

// check for expiration
echo PHP_EOL . 'check expiration that is valid' .PHP_EOL;
$tokenValid = checkExpiration($jwsSignedTokenReceived);
echo 'the token is valid and not expired: ' . $tokenValid . PHP_EOL;
echo PHP_EOL . 'check expiration that is invalid' . PHP_EOL;
$jwsSignedTokenReceivedExpired = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvamF2YS1jcnlwdG8uZ2l0aHViLmlvXC9jcm9zc19wbGF0Zm9ybV9jcnlwdG9cLyIsInN1YiI6IkpXVCBSUzI1NiBzaWduYXR1cmUiLCJleHAiOjE2MTMzODY4MTMsImlhdCI6MTYxMzM4NjY5M30.2ZrDOCWk7QUKfNd78exKDlDR5Z2_IgBxhmu8J6AjOLmYw7kzZ_kPR2FvuLI_yHnOWomw9KdD2lcH1sGTNhm4M-_D1hlKL3AyWSoujHqTV0beGRcmazuEGBSy2T4-ZLpmn6lruxrHnuhJP_IzwUQXgTMVz90eVtvEGgs9sgKm8EvmS5UTwiXwvNpzjPUgxTmHNwVafToTVS-e8hvdLVPKvWpFxG2JluIIsGpoX5Uv37eS4DCf0IQH9cx08VMifFHgnzFUTJlnSJ0IOzhG00AwqKNsfH0EhwreNPArxjMerfNRYYJsx3FQxzLrslVlhLBL4S5WkEUfCdaWm3F1ptAVcw';
$tokenValidExpired = checkExpiration($jwsSignedTokenReceivedExpired);
echo 'the token is valid and not expired: ' . $tokenValidExpired . PHP_EOL;

// print expirationData
echo PHP_EOL . 'print expiration data' .PHP_EOL;
$expirationData = printExpirationData($jwsSignedTokenReceived);
echo 'this is the expiration information in the token:' . PHP_EOL . $expirationData . PHP_EOL;

// header checking before consuming !!
echo PHP_EOL . 'header checking [optional]' . PHP_EOL;
// show payloadObject
echo 'headerObject: ' . printHeaderObject($jwsSignedTokenReceived) . PHP_EOL;
?>
