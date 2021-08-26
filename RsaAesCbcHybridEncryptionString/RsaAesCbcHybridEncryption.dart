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
  // https://pub.dev/packages/pointycastle
  // https://github.com/bcgit/pc-dart/
  // https://pub.dev/packages/basic_utils
  // https://github.com/Ephenodrom/Dart-Basic-Utils

  printC('RSA AES CBC hybrid encryption');
  printC('\nuses AES CBC 256 random key for the plaintext encryption');
  printC('and RSA OAEP SHA 1 2048 bit for the encryption of the random key\n');

  final dataToEncryptString = 'The quick brown fox jumps over the lazy dog';
  printC('plaintext: ' + dataToEncryptString);

  // # # # usually we would load the private and public key from a file or keystore # # #
  // # # # here we use hardcoded keys for demonstration - don't do this in real programs # # #

  // encryption
  printC('\n* * * Encryption * * *');
  final publicKeyPem = loadRsaPublicKeyPem();
  RSAPublicKey publicKey =
      CryptoUtils.rsaPublicKeyFromPem(publicKeyPem) as RSAPublicKey;
  var completeCiphertextBase64 = rsaOaepSha1AesCbc256HybridStringEncryption(
      publicKey, dataToEncryptString);
  printC('completeCiphertext (Base64): ' + completeCiphertextBase64);
  printC('output is (Base64) encryptionKey : (Base64) ciphertext');

  // transport the encrypted data to recipient

  // decryption
  printC('\n* * * Decryption * * *');
  final privateKeyPem = loadRsaPrivateKeyPem();
  final privateKey = CryptoUtils.rsaPrivateKeyFromPem(privateKeyPem);
  final completeCiphertextDecryptionBase64 = completeCiphertextBase64;
  printC('completeCiphertext (Base64): ' + completeCiphertextDecryptionBase64);
  printC('input is (Base64) encryptionKey : (Base64) ciphertext');
  var decryptedtext = rsaOaepSha1AesCbc256HybridStringDecryption(
      privateKey, completeCiphertextDecryptionBase64);
  printC('plaintext:  ' + decryptedtext);
}

String rsaOaepSha1AesCbc256HybridStringEncryption(
    RSAPublicKey publicKey, String plaintext) {
  // generate random key
  Uint8List encryptionKey = generateRandomAesKey();
  // encrypt plaintext
  String ciphertextBase64 = aesCbcEncryptToBase64(encryptionKey, plaintext);
  // encrypt the aes encryption key with the rsa public key
  String encryptionKeyBase64 =
      base64Encoding(rsaEncryptionOaepSha1(publicKey, encryptionKey));
  // complete output
  return encryptionKeyBase64 + ':' + ciphertextBase64;
}

String rsaOaepSha1AesCbc256HybridStringDecryption(
    RSAPrivateKey privateKey, String completeCiphertext) {
  var parts = completeCiphertext.split(':');
  Uint8List encryptionKeyReceived = base64Decoding(parts[0]);
  Uint8List iv = base64Decoding(parts[1]);
  Uint8List encryptedData = base64Decoding(parts[2]);
  // decrypt the encryption key with the rsa private key
  Uint8List decryptionKey =
      rsaDecryptionOaepSha1(privateKey, encryptionKeyReceived);
  return aesCbcDecrypt(decryptionKey, iv, encryptedData);
}

String aesCbcEncryptToBase64(Uint8List key, String plaintext) {
  var plaintextUint8 = createUint8ListFromString(plaintext);
  final iv = generateRandomIv();
  final CBCBlockCipher cipher = new CBCBlockCipher(new AESFastEngine());
  ParametersWithIV<KeyParameter> cbcParams =
      new ParametersWithIV<KeyParameter>(new KeyParameter(key), iv);
  PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null>
      paddingParams =
      new PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null>(
          cbcParams, null);
  PaddedBlockCipherImpl paddingCipher =
      new PaddedBlockCipherImpl(new PKCS7Padding(), cipher);
  paddingCipher.init(true, paddingParams);
  final ciphertext = paddingCipher.process(plaintextUint8);
  final ivBase64 = base64Encoding(iv);
  final ciphertextBase64 = base64Encoding(ciphertext);
  return ivBase64 + ':' + ciphertextBase64;
}

String aesCbcDecrypt(Uint8List key, Uint8List iv, Uint8List encryptedData) {
  CBCBlockCipher cipher = new CBCBlockCipher(new AESFastEngine());
  ParametersWithIV<KeyParameter> cbcParams =
      new ParametersWithIV<KeyParameter>(new KeyParameter(key), iv);
  PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null>
      paddingParams =
      new PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null>(
          cbcParams, null);
  PaddedBlockCipherImpl paddingCipher =
      new PaddedBlockCipherImpl(new PKCS7Padding(), cipher);
  paddingCipher.init(false, paddingParams);
  return new String.fromCharCodes(paddingCipher.process(encryptedData));
}

Uint8List generateRandomAesKey() {
  final _sGen = Random.secure();
  final _seed =
      Uint8List.fromList(List.generate(32, (n) => _sGen.nextInt(255)));
  SecureRandom sec = SecureRandom("Fortuna")..seed(KeyParameter(_seed));
  return sec.nextBytes(32);
}

Uint8List generateRandomIv() {
  final _sGen = Random.secure();
  final _seed =
      Uint8List.fromList(List.generate(32, (n) => _sGen.nextInt(255)));
  SecureRandom sec = SecureRandom("Fortuna")..seed(KeyParameter(_seed));
  return sec.nextBytes(16);
}

Uint8List rsaEncryptionOaepSha1(
    RSAPublicKey publicKey, Uint8List dataToEncrypt) {
  final encryptor = OAEPEncoding(RSAEngine())
    ..init(true, PublicKeyParameter<RSAPublicKey>(publicKey)); // true=encrypt
  return _processInBlocks(encryptor, dataToEncrypt);
}

Uint8List rsaDecryptionOaepSha1(
    RSAPrivateKey privateKey, Uint8List ciphertext) {
  final decryptor = OAEPEncoding(RSAEngine())
    ..init(
        false, PrivateKeyParameter<RSAPrivateKey>(privateKey)); // false=decrypt
  return _processInBlocks(decryptor, ciphertext);
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

String loadRsaPublicKeyPem() {
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
