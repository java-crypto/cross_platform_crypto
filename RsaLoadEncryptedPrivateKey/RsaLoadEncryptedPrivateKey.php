<?php
function rsaSignToBase64 ($privateKey, $dataToSign) {
    openssl_sign($dataToSign, $signature, $privateKey, OPENSSL_ALGO_SHA256);
    return base64_encode($signature);
}

function rsaVerifySignatureFromBase64 ($publicKey, $dataToSign, $signatureBase64) {
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

function loadRsaPrivateKeyEncryptedPem() {
    // this is a sample key - don't worry !
    // passphrase for decryption: 123456
    // passphrase scheme: PBES2
    // password based key derivation: PBKDF2 with HMAC and SHA-256
    // encryption algorithm: AES256-CBC
    return '
-----BEGIN ENCRYPTED PRIVATE KEY-----
MIIFLTBXBgkqhkiG9w0BBQ0wSjApBgkqhkiG9w0BBQwwHAQIUo/aHb7/5jUCAggA
MAwGCCqGSIb3DQIJBQAwHQYJYIZIAWUDBAEqBBCbpiqeplyyDN85uu5yYqpFBIIE
0Iqvy2v42HeASj6B25llJjNOWvT5djjDiAg7BFsuOhMq10mTC7gWduXBai5llUBH
qF4+GgqCeedwT24YkdR4wAqtc2shcmq+2xC70N/XrSg9geGehmNwDx35byhPjouk
XwA5tgVsKtxTob/xjPYhjL5J1KpWOPQhdnHPGfGlWedTyTKboRfgkZ/Yds5EW6k7
mO9wt/iXcym6QgaJTswa1ntGPVaKMYvXVRtNnphsI9M7njXBYO52vfujUaeKn8Xe
qmuEAKGFhhu+r7/WWyQl+Zq2KIoD5ImOxuXScCZVH3+76AxXSwDJzB6ZG1SowSy+
YHbfkmvK2H6YdFshEfI/coMmu9E3wDKuCY7rgb70lHLcpvbSMa88vCFl3d/ZfmFl
w4TwG3vEVR9wfbXAuAw6XTSLBODcF4ifZnyXI2Dr+fQbQd/4o8w05hDiPkXRgnYY
e/4nFbTGGKvatZ7LOLa62isiGtQ4nprDpTapLMeJCFSgQN1jlZMVvV6mKUfrCJ69
3bESlyYGAN0MAx4KeMcCRwuEXWhOkLMFW0gvRp1udiMxJ8SnWfseYTr2FPUFQlFk
2ZhHsSO0Pg/cszUSuCX0q2ZW0ZZOiihJsmiQNXlbsRog9m8vaxUfA00v/q6iryGE
ApCiQd653ikukRhk/nQEY6OV4byLyI9zESF/pIdsrFnEtGSd0tggGsREtn11D8+7
SW0F0Es4P4UUPjiaYzyB58gcZJWxcoq63NOTUaicvTjNbzQu1+4y+2xhFe89e09w
PFSGerjSV/uVtVTH2hb79mAmlyLvaikQfQq2nvpCuZy7wZjhPA3D03WFTCbrYS7P
PfckAICCBCIB97vPsWVAviOVOrG5NDbIag+/3YhZb2gyuR2/E0XI1NdXO63NqU9R
B6VUUNdSvv4djA30nVjARlh3LOZmTy50OIj/QDOEbBrtFH79eHHxHRiK0+Q0A8Ch
EoDDz7bMKBcpiS3vXnI2UsrZX/QIuRjypc003uVm4MvdUwv0j3UGZr9zRGctqWf0
YBRkoIELRA45hQn0Ln3+wjAcjLRPpeuPgVAq7tDDLDmP0i/m8UbjNorhdv4CrT/u
P+keTW7v9Nuov1IXG4W6wKipYRzMjiYrTL5TaXPi9z63b6RQVShAEqSTtsPg5nDy
CZkt5NPBltq+3InrsTlbKSQGYn6wyEQeBYezD3CLkIliDn7UM4BDn+k5YonNynU4
emxbU4wyBEXoMD8+aBMf+bKKQXU97Ts9p7is+Ogqyop2mbMOebIxCKayizlb7/hp
Qs2ThBP84OrniUEeKVAkdykZyrdCoatgXVJngDwwEkSwOrEYZ0sJ48qPQMTN3kVq
r5XJjX3m8mpXdz0s9VbhT/rWSdQMX82msof4c6+RghEbiHFG4B0el5vrJaAROb69
3OnoU8mYKBlFjbO1rJJpGS5h/v+ysqWxDxfPPLD86UsgDAjr5C7W3K8eYB1UD/wP
n91TuTdrLKhLE93VsCyXnsLm44Td3MBv89Fr2CMF/Jz15Nfrg3yvR7bKvkNs3uVM
FVO3kqtmUMC/RsUr3NIE/RZ2sS+cjLMDE2zBUgHnDpNQB6WSuwqEI9prAWxEc1jS
SwyNkvstftK35nfeI3FNP0R94+kSKzGmiQh5yl3ufAVi
-----END ENCRYPTED PRIVATE KEY-----
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

echo 'Load RSA PKCS8 encrypted private key AES256-CBC PBKDF2 HMAC with SHA-256' . PHP_EOL . PHP_EOL;

$dataToSign = "The quick brown fox jumps over the lazy dog";
echo 'dataToSign: ' . $dataToSign . PHP_EOL;

$passphrase = "123456"; // passphrase for encrypted RSA private key

// usually we would load the private and public key from a file or keystore
// here we use hardcoded keys for demonstration - don't do this in real programs

echo PHP_EOL . '* * * sign the plaintext with the encrypted RSA private key * * *' . PHP_EOL;
$privateRsaKeyPem = loadRsaPrivateKeyEncryptedPem();
$rsaPrivateKey = openssl_pkey_get_private($privateRsaKeyPem, $passphrase);
$signatureBase64 = rsaSignToBase64($rsaPrivateKey, $dataToSign);
echo 'signature (Base64): ' . $signatureBase64 . PHP_EOL;

echo PHP_EOL . '* * * verify the signature against the plaintext with the RSA public key * * *' . PHP_EOL;
$publicRsaKeyPem = loadRsaPublicKeyPem();
$rsaPublicKey = openssl_pkey_get_public($publicRsaKeyPem);
$signatureVerified = rsaVerifySignatureFromBase64($rsaPublicKey, $dataToSign, $signatureBase64);
//$signatureVerified = rsaVerifySignatureFromBase64($rsaPublicKey, 'test', $signatureBase64);
if ($signatureVerified == 1) {
    echo 'signature (Base64) verified: ' . 'true'. PHP_EOL;
} else {
    echo 'signature (Base64) verified: ' . 'false'. PHP_EOL;
}
?>
