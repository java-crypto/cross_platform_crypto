<?php
function hmac256Calculation($keyHmac, $data)
{
    return hash_hmac("sha256" , $data, $keyHmac, true);
}

function base64Encoding($input)
{
    return base64_encode($input);
}

echo 'HMAC 256 calculation' . PHP_EOL;

$hmac256KeyString = 'hmac256ForAesEncryption';
$plaintextString = 'The quick brown fox jumps over the lazy dog';
echo 'hmac256Key: ' . $hmac256KeyString . PHP_EOL;
echo 'plaintext:  ' . $plaintextString . PHP_EOL;

// calculate hmac
$hmac256 = hmac256Calculation($hmac256KeyString, $plaintextString);
echo 'hmac256 length: ' . strlen($hmac256) . ' (Base64) data: ' . base64Encoding($hmac256) . PHP_EOL;
?>
