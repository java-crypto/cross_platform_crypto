import 'dart:convert';
import 'dart:math';
import 'dart:typed_data';
import "package:pointycastle/export.dart";
import 'package:basic_utils/basic_utils.dart';

void main() {
/* add in pubspec.yaml:
dependencies:
  pointycastle: ^3.1.1
  basic_utils: ^3.4.0
 */
    printC('RSA signature string');

    final dataToSignString = 'The quick brown fox jumps over the lazy dog';
    final dataToSign = createUint8ListFromString(dataToSignString);
    printC('plaintext: ' + dataToSignString);

    // # # # usually we would load the private and public key from a file or keystore # # #
    // # # # here we use hardcoded keys for demonstration - don't do this in real programs # # #

    printC('\n* * * sign the plaintext with the RSA private key * * *');
    final privateKeyPem = loadRsaPrivateKeyPem();
    final rsaPrivateKey = CryptoUtils.rsaPrivateKeyFromPem(privateKeyPem);
    final signatureBase64 = rsaSignToBase64(rsaPrivateKey, dataToSign);
    printC('signature (Base64): ' + signatureBase64);

    printC('\n* * * verify the signature against the plaintext with the RSA public key * * *');
    final publicKeyPem = loadRsaPublicKeyPem();
    final publicKey = CryptoUtils.rsaPublicKeyFromPem(publicKeyPem) as RSAPublicKey;
    bool signatureVerified = rsaVerifySignatureFromBase64(publicKey, dataToSign, signatureBase64);
    printC('signature (Base64) verified: ' + signatureVerified.toString());
}

String rsaSignToBase64(RSAPrivateKey privateKey, Uint8List messageByte) {
    final signer = RSASigner(SHA256Digest(), '0609608648016503040201');
    signer.init(
        true, PrivateKeyParameter<RSAPrivateKey>(privateKey)); // true=sign
    final sig = signer.generateSignature(messageByte);
    return base64Encoding(sig.bytes);
}

bool rsaVerifySignatureFromBase64(RSAPublicKey publicKey, Uint8List messageByte, String signatureBase64) {
    final sig = RSASignature(base64Decoding(signatureBase64));
    final verifier = RSASigner(SHA256Digest(), '0609608648016503040201');
    verifier.init(
        false, PublicKeyParameter<RSAPublicKey>(publicKey)); // false=verify
    try {
        return verifier.verifySignature(messageByte, sig);
    } on ArgumentError {
        return false; // for Pointy Castle 1.0.2 when signature has been modified
    }
}

Uint8List createUint8ListFromString(String s) {
    var ret = new Uint8List(s.length);
    for (var i = 0; i < s.length; i++) {
        ret[i] = s.codeUnitAt(i);
    }
    return ret;
}

String base64Encoding(Uint8List input) {
    return base64.encode(input);
}

Uint8List base64Decoding(String input) {
    return base64.decode(input);
}

String loadRsaPublicKeyPem () {
    return ('''-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9
iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT
/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8
01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB
ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g
QwIDAQAB
-----END PUBLIC KEY-----''');
}

String loadRsaPrivateKeyPem() {
    return ('''-----BEGIN PRIVATE KEY-----
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
-----END PRIVATE KEY-----''');
}

void printC(String newString) {
    // for compatibility reasons to FlutterEmptyConsole
    print(newString);
}
