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

function convertSignatureDerToP1363Base64($signatureDer)
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

function base64Encoding($input)
{
    return base64_encode($input);
}

function base64Decoding($input)
{
    return base64_decode($input);
}

echo 'EC signature converter DER to P1363 encoding' . PHP_EOL;
echo 'Please note that all values are in Base64 encoded form' . PHP_EOL;

// insert the ecdsa signature in DER encoding
$signatureDerBase64 = "MEUCIQCW98vDD+c/Wx3WX3T+Ph9ZDf1s5nuDThz2xrhSOjwGFAIgNpUg5SygNK7W+zzw4eNZkivizpU/UUXMMR2zPw2D7J4=";

$signatureP1363Base64 = convertSignatureDerToP1363Base64($signatureDerBase64);

echo 'signature in DER encoding:   ' . $signatureDerBase64 . PHP_EOL;
echo 'signature in P1363 encoding: ' . $signatureP1363Base64 . PHP_EOL;

?>
