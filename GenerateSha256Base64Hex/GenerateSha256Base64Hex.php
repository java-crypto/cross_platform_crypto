<?php

function calculateSha256($input) {
    return hash('sha256', $input, true);
}

function bytesToHex($input) {
    return bin2hex($input);
}

function hexStringToByteArray($input) {
    return hex2bin($input);
}

function base64Encoding($input)
{ return base64_encode($input); }

function base64Decoding($input)
{ return base64_decode($input); }

echo 'Generate a SHA-256 hash, Base64 en- and decoding and hex conversions' . PHP_EOL;

$plaintext = "The quick brown fox jumps over the lazy dog";
echo 'plaintext:                ' . $plaintext . PHP_EOL;

$sha256Value = calculateSha256($plaintext);
echo 'sha256Value (hex) length: ' . strlen($sha256Value) . " data: " . bytesToHex($sha256Value) . PHP_EOL;

$sha256Base64 = base64Encoding($sha256Value);
echo 'sha256Value (base64):     ' . $sha256Base64 . PHP_EOL;

$sha256ValueDecoded = base64Decoding($sha256Base64);
echo 'sha256Base64 decoded to a byte array:' . PHP_EOL;
echo 'sha256Value (hex) length: ' . strlen($sha256ValueDecoded) . " data: " . bytesToHex($sha256ValueDecoded) . PHP_EOL;

$sha256HexString = "d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592";
$sha256Hex = hexStringToByteArray($sha256HexString);
echo 'sha256HexString converted to a byte array:' . PHP_EOL;
echo 'sha256Value (hex) length: ' . strlen($sha256Hex) . " data: " . bytesToHex($sha256Hex) . PHP_EOL;
?>
