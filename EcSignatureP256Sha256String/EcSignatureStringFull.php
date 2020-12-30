<?php

/*
 * ASN.1 <-> DER converter taken from https://github.com/firebase/php-jwt/blob/master/src/JWT.php
 * author   Neuman Vong <neuman@twilio.com>
 * author   Anant Narayanan <anant@php.net>
 * license  http://opensource.org/licenses/BSD-3-Clause 3-clause BSD
 */
const ASN1_INTEGER = 0x02;
const ASN1_SEQUENCE = 0x10;
const ASN1_BIT_STRING = 0x03;

/**
 * Convert an ECDSA signature to an ASN.1 DER sequence
 *
 * @param string $sig The ECDSA signature to convert
 * @return  string The encoded DER object
 */
function signatureToDER($sig)
{
    // Separate the signature into r-value and s-value
    list($r, $s) = \str_split($sig, (int)(\strlen($sig) / 2));

    // Trim leading zeros
    $r = \ltrim($r, "\x00");
    $s = \ltrim($s, "\x00");

    // Convert r-value and s-value from unsigned big-endian integers to
    // signed two's complement
    if (\ord($r[0]) > 0x7f) {
        $r = "\x00" . $r;
    }
    if (\ord($s[0]) > 0x7f) {
        $s = "\x00" . $s;
    }

    return encodeDER(
        ASN1_SEQUENCE,
        encodeDER(ASN1_INTEGER, $r) .
        encodeDER(ASN1_INTEGER, $s)
    );
}

/**
 * Encodes a value into a DER object.
 *
 * @param int $type DER tag
 * @param string $value the value to encode
 * @return  string  the encoded object
 */
function encodeDER($type, $value)
{
    $tag_header = 0;
    if ($type === ASN1_SEQUENCE) {
        $tag_header |= 0x20;
    }

    // Type
    $der = \chr($tag_header | $type);

    // Length
    $der .= \chr(\strlen($value));

    return $der . $value;
}

/**
 * Encodes signature from a DER object.
 *
 * @param string $der binary signature in DER format
 * @param int $keySize the number of bits in the key
 * @return  string  the signature
 */
function signatureFromDER($der, $keySize)
{
    // OpenSSL returns the ECDSA signatures as a binary ASN.1 DER SEQUENCE
    list($offset, $_) = readDER($der);
    list($offset, $r) = readDER($der, $offset);
    list($offset, $s) = readDER($der, $offset);

    // Convert r-value and s-value from signed two's compliment to unsigned
    // big-endian integers
    $r = \ltrim($r, "\x00");
    $s = \ltrim($s, "\x00");

    // Pad out r and s so that they are $keySize bits long
    $r = \str_pad($r, $keySize / 8, "\x00", STR_PAD_LEFT);
    $s = \str_pad($s, $keySize / 8, "\x00", STR_PAD_LEFT);

    return $r . $s;
}

/**
 * Reads binary DER-encoded data and decodes into a single object
 *
 * @param string $der the binary data in DER format
 * @param int $offset the offset of the data stream containing the object
 * to decode
 * @return array [$offset, $data] the new offset and the decoded object
 */
function readDER($der, $offset = 0)
{
    $pos = $offset;
    $size = \strlen($der);
    $constructed = (\ord($der[$pos]) >> 5) & 0x01;
    $type = \ord($der[$pos++]) & 0x1f;

    // Length
    $len = \ord($der[$pos++]);
    if ($len & 0x80) {
        $n = $len & 0x1f;
        $len = 0;
        while ($n-- && $pos < $size) {
            $len = ($len << 8) | \ord($der[$pos++]);
        }
    }

    // Value
    if ($type == ASN1_BIT_STRING) {
        $pos++; // Skip the first contents octet (padding indicator)
        $data = \substr($der, $pos, $len - 1);
        $pos += $len - 1;
    } elseif (!$constructed) {
        $data = \substr($der, $pos, $len);
        $pos += $len;
    } else {
        $data = null;
    }

    return array($pos, $data);
}

// own functions

function convertSignatureDerToP1353Base64($signatureDer)
{
    $signature = base64Decoding($signatureDer);
    return base64Encoding(signatureFromDER($signature, 256));
    $sigToJavaBase64 = base64Encoding($sigToJava);
}

function convertSignatureP1363ToDerBase64($signatureP1363)
{
    $signature = base64Decoding($signatureP1363);
    return base64Encoding(signatureToDER($signature));
}


function ecSignToBase64($privateKey, $dataToSign)
{
    openssl_sign($dataToSign, $signature, $privateKey, OPENSSL_ALGO_SHA256);
    return base64_encode($signature);
}

function ecVerifySignatureFromBase64($publicKey, $dataToSign, $signatureBase64)
{
    $ok = openssl_verify($dataToSign, base64_decode($signatureBase64), $publicKey, OPENSSL_ALGO_SHA256);
    if ($ok == 1) {
        return true;
    } elseif ($ok == 0) {
        return false;
    } else {
        return false;
    }
    return false;
}

function loadEcPrivateKeyPem()
{
    // this is a sample key - don't worry !
    return '
-----BEGIN EC PRIVATE KEY-----
MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCAU2f8tzo99Z1HoxJlY
96yXUhFY5vppVjw1iPKRfk1wHA==
-----END EC PRIVATE KEY-----
';
}

function loadEcPublicKeyPem()
{
    // this is a sample key - don't worry !
    return '
-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEzb7yAFWup6iDqJiEq764rAumsV2M
rspZxaP3WGpwHaC4Uff3N4UbJZF7Zac1c6W7KJl0eeCP0205Q3UEpwxndQ==
-----END PUBLIC KEY-----
';
}

function base64Encoding($input)
{
    return base64_encode($input);
}

function base64Decoding($input)
{
    return base64_decode($input);
}

echo 'EC signature string (ECDSA with SHA256)' . PHP_EOL;

$dataToSign = "The quick brown fox jumps over the lazy dog";
echo 'dataToSign: ' . $dataToSign . PHP_EOL;

// usually we would load the private and public key from a file or keystore
// here we use hardcoded keys for demonstration - don't do this in real programs

echo PHP_EOL . '* * * sign the plaintext with the EC private key * * *' . PHP_EOL;
$privateEcKeyPem = loadEcPrivateKeyPem();
$ecPrivateKey = openssl_pkey_get_private($privateEcKeyPem);
echo 'used private key:' . $privateEcKeyPem . PHP_EOL;
$signatureDerBase64 = ecSignToBase64($ecPrivateKey, $dataToSign);
$signatureBase64 = convertSignatureDerToP1353Base64($signatureDerBase64);
echo 'signature (Base64): ' . $signatureBase64 . PHP_EOL;

echo PHP_EOL . '* * * verify the signature against the plaintext with the EC public key * * *' . PHP_EOL;
$signatureReceivedBase64 = $signatureBase64;
$publicEcKeyPem = loadEcPublicKeyPem();
$ecPublicKey = openssl_pkey_get_public($publicEcKeyPem);
echo 'used public key:' . $publicEcKeyPem . PHP_EOL;
$signatureReceivedDerBase64 = convertSignatureP1363ToDerBase64($signatureReceivedBase64);
$signatureVerified = ecVerifySignatureFromBase64($ecPublicKey, $dataToSign, $signatureReceivedDerBase64);
if ($signatureVerified == 1) {
    echo 'signature (Base64) verified: ' . 'true' . PHP_EOL;
} else {
    echo 'signature (Base64) verified: ' . 'false' . PHP_EOL;
}
?>
