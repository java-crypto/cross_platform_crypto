<?php

function rsaDecryptionOaepSha1($privateKey, $ciphertext) {
    openssl_private_decrypt($ciphertext, $decryptedtext, $privateKey, OPENSSL_PKCS1_OAEP_PADDING);
    return $decryptedtext;
}

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

echo 'RSA 2048 decryption OAEP SHA-1 string' . PHP_EOL;

// # # # usually we would load the private from a file or keystore # # #
// # # # here we use a hardcoded key for demonstration - don't do this in real programs # # #
$filenamePrivateKeyPem = "privatekey2048.pem";

// receiving the encrypted data, decryption
echo PHP_EOL . '* * * decrypt the ciphertext with the RSA private key * * *' .PHP_EOL;
$ciphertextReceivedBase64 = 'In0CE7VSJlr6PT+fl9SCejWhNZYioVn1UzYaqYUms7Y63oi4xK/2Qrgyi5a3CnvappM1MHdDaZVc+bDzl/iBiUslkHcF8lWGD4YdKuJgtfSS8WBRuIxz78EFhU3TfMU3y0v0bhUUj1/6ZdO/p9j8KcNbnpEB8A9YW+bZC1qOO8ibnTNbb3RHQekafz6r9oAYsILNga1pi9ZlyXQPYi7VpWAeZmUOq+MHEgD/Nkq/oxkyMo/yf5SVk1ig8M5Lr6arj22r2ePpQ8j3onmN7aOTCWUhZ7FdLm7IiRYebZ3MWPWozt9O6BSNjALLDXm7NfKaYcgQ+bVXrA4k1M9f4M9OEA==';
echo 'ciphertextReceivedBase64: ' . $ciphertextReceivedBase64 . PHP_EOL;
//$privateKeyLoad = openssl_pkey_get_private(loadRsaPrivateKeyPem());
// use this in production
$privateKeyLoad = openssl_pkey_get_private(file_get_contents($filenamePrivateKeyPem));
$decryptedtext = rsaDecryptionOaepSha1($privateKeyLoad, base64Decoding($ciphertextReceivedBase64));
echo 'decryptedtext: ' . $decryptedtext . PHP_EOL;
?>
