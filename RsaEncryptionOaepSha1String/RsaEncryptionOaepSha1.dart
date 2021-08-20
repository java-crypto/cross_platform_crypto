import 'dart:convert';
import 'dart:typed_data';
import "package:pointycastle/export.dart";
import 'package:asn1lib/asn1lib.dart';

void main() {
/* add in pubspec.yaml:
dependencies:
  pointycastle: ^3.1.1
  asn1lib: ^1.0.2
 */
    // https://github.com/bcgit/pc-dart/blob/master/tutorials/rsa.md

    printC('RSA 2048 encryption OAEP SHA-1 string');

    final dataToEncryptString = 'The quick brown fox jumps over the lazy dog';
    final dataToEncrypt = createUint8ListFromString(dataToEncryptString);
    printC('plaintext: ' + dataToEncryptString);

    // # # # usually we would load the private and public key from a file or keystore # # #
    // # # # here we use hardcoded keys for demonstration - don't do this in real programs # # #

    // encryption
    printC('\n* * * encrypt the plaintext with the RSA public key * * *');
    final publicKeyPem = loadRsaPublicKeyPem();
    final publicKey = getPublicKey(decodePEM(publicKeyPem)) as RSAPublicKey;
    var ciphertextBase64 = rsaEncryptionOaepSha1(publicKey, dataToEncrypt);
    printC('ciphertext (Base64): ' + ciphertextBase64);
    printC('output is (Base64) ciphertext');

    // decryption
    printC('\n* * * decrypt the ciphertext with the RSA private key * * *');
    final privateKeyPem = loadRsaPrivateKeyPem();
    final privateKey = getPrivateKey(decodePEM(privateKeyPem));
    final ciphertextDecryptionBase64 = ciphertextBase64;
    printC('ciphertext (Base64): ' + ciphertextDecryptionBase64);

    printC('input is (Base64) ciphertext');
    var decryptedtext = rsaDecryptionOaepSha1(privateKey, ciphertextDecryptionBase64);
    printC('plaintext:  ' + decryptedtext);
}

String rsaEncryptionOaepSha1(RSAPublicKey publicKey, Uint8List dataToEncrypt) {
    final encryptor = OAEPEncoding(RSAEngine())
        ..init(true, PublicKeyParameter<RSAPublicKey>(publicKey)); // true=encrypt
    return base64Encoding(_processInBlocks(encryptor, dataToEncrypt));
}

String rsaDecryptionOaepSha1(RSAPrivateKey privateKey, String ciphertextBase64) {
    final decryptor = OAEPEncoding(RSAEngine())
        ..init(
            false, PrivateKeyParameter<RSAPrivateKey>(privateKey)); // false=decrypt
    return new String.fromCharCodes(_processInBlocks(decryptor, base64Decoding(ciphertextBase64)));
}

Uint8List _processInBlocks(AsymmetricBlockCipher engine, Uint8List input) {
    final numBlocks = input.length ~/ engine.inputBlockSize +
        ((input.length % engine.inputBlockSize != 0) ? 1 : 0);
    final output = Uint8List(numBlocks * engine.outputBlockSize);
    var inputOffset = 0;
    var outputOffset = 0;
    while (inputOffset < input.length) {
        final chunkSize = (inputOffset + engine.inputBlockSize <= input.length)
            ? engine.inputBlockSize
            : input.length - inputOffset;
        outputOffset += engine.processBlock(
            input, inputOffset, chunkSize, output, outputOffset);
        inputOffset += chunkSize;
    }
    return (output.length == outputOffset)
        ? output
        : output.sublist(0, outputOffset);
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

RSAPrivateKey getPrivateKey(privateKeyDer) {
    var asn1Parser = ASN1Parser(privateKeyDer);
    var topLevelSeq = asn1Parser.nextObject() as ASN1Sequence;
    var version = topLevelSeq.elements[0];
    var algorithm = topLevelSeq.elements[1];
    var privateKey = topLevelSeq.elements[2];
    asn1Parser = ASN1Parser(privateKey.contentBytes()!);
    var pkSeq = asn1Parser.nextObject() as ASN1Sequence;
    version = pkSeq.elements[0];
    var modulus = pkSeq.elements[1] as ASN1Integer;
    var publicExponent = pkSeq.elements[2] as ASN1Integer;
    var privateExponent = pkSeq.elements[3] as ASN1Integer;
    var p = pkSeq.elements[4] as ASN1Integer;
    var q = pkSeq.elements[5] as ASN1Integer;
    var exp1 = pkSeq.elements[6] as ASN1Integer;
    var exp2 = pkSeq.elements[7] as ASN1Integer;
    var co = pkSeq.elements[8] as ASN1Integer;
    var modulusBigInt = modulus.valueAsBigInteger;
    var privateExponentBigInt = privateExponent.valueAsBigInteger;
    RSAPrivateKey rsaPrivateKey = RSAPrivateKey(
        modulusBigInt!,
        //modulus.valueAsBigInteger;
        //privateExponent.valueAsBigInteger,
        privateExponentBigInt!,
        p.valueAsBigInteger,
        q.valueAsBigInteger
    );
    return rsaPrivateKey;
}

RSAPublicKey getPublicKey(publicKeyDer) {
    var asn1Parser = new ASN1Parser(publicKeyDer);
    var topLevelSeq = asn1Parser.nextObject() as ASN1Sequence;
    var publicKeyBitString = topLevelSeq.elements[1];
    var publicKeyAsn = new ASN1Parser(publicKeyBitString.contentBytes()!);
    //ASN1Sequence publicKeySeq = publicKeyAsn.nextObject();
    ASN1Sequence publicKeySeq = publicKeyAsn.nextObject() as ASN1Sequence;
    var modulus = publicKeySeq.elements[0] as ASN1Integer;
    var publicExponent = publicKeySeq.elements[1] as ASN1Integer;
    var modulusBigInt = modulus.valueAsBigInteger;
    var publicExponentBigInt = publicExponent.valueAsBigInteger;
    RSAPublicKey rsaPublicKey = RSAPublicKey(
        modulusBigInt!,
        publicExponentBigInt!
    );
    return rsaPublicKey;
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

Uint8List decodePEM(pem) {
    var startsWith = [
        '-----BEGIN PUBLIC KEY-----',
        '-----BEGIN PRIVATE KEY-----',
        '-----BEGIN CERTIFICATE-----',
    ];
    var endsWith = [
        '-----END PUBLIC KEY-----',
        '-----END PRIVATE KEY-----',
        '-----END CERTIFICATE-----'
    ];
    //HACK
    for (var s in startsWith) {
        if (pem.startsWith(s)) pem = pem.substring(s.length);
    }
    for (var s in endsWith) {
        if (pem.endsWith(s)) pem = pem.substring(0, pem.length - s.length);
    }
    //Dart base64 decoder does not support line breaks
    pem = pem.replaceAll('\n', '');
    pem = pem.replaceAll('\r', '');
    return base64.decode(pem);
}

void printC(String newString) {
    // for compatibility reasons to FlutterEmptyConsole
    print(newString);
}
