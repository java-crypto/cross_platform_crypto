import 'dart:convert';
import 'dart:math';
import 'dart:typed_data';
import "package:pointycastle/export.dart";

main() {
/* add in pubspec.yaml:
dependencies:
  pointycastle: ^3.1.1
 */

  printC('AES CBC 256 String encryption with random key');
  final plaintext = 'The quick brown fox jumps over the lazy dog';
  printC('plaintext: ' + plaintext);

  // generate random key
  var encryptionKey = generateRandomAesKey();
  var encryptionKeyBase64 = base64Encoding(encryptionKey);
  print('encryptionKey (Base64): ' + encryptionKeyBase64);

  // encryption
  printC('\n* * * Encryption * * *');
  String ciphertextBase64 = aesCbcEncryptionToBase64(encryptionKey, plaintext);
  printC('ciphertext (Base64): ' + ciphertextBase64);
  printC('output is (Base64) iv : (Base64) ciphertext');

  printC('\n* * * Decryption * * *');
  var ciphertextDecryptionBase64 = ciphertextBase64;
  printC('ciphertext (Base64): ' + ciphertextDecryptionBase64);
  printC('input is (Base64) iv : (Base64) ciphertext');
  var decryptedtext = aesCbcDecryptionFromBase64(encryptionKey, ciphertextDecryptionBase64);
  printC('plaintext:  ' + decryptedtext);
}

String aesCbcEncryptionToBase64(Uint8List key, String plaintext) {
  var plaintextUint8 = createUint8ListFromString(plaintext);
  final iv = generateRandomIv();
  final CBCBlockCipher cipher = new CBCBlockCipher(new AESFastEngine());
  ParametersWithIV<KeyParameter> cbcParams = new ParametersWithIV<KeyParameter>(new KeyParameter(key), iv);
  PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null> paddingParams = new PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null>(cbcParams, null);
  PaddedBlockCipherImpl paddingCipher = new PaddedBlockCipherImpl(new PKCS7Padding(), cipher);
  paddingCipher.init(true, paddingParams);
  final ciphertext = paddingCipher.process(plaintextUint8);
  final ivBase64 = base64Encoding(iv);
  final ciphertextBase64 = base64Encoding(ciphertext);
  return ivBase64 + ':' + ciphertextBase64;
}

String aesCbcDecryptionFromBase64(Uint8List key, String data) {
  var parts = data.split(':');
  var iv = base64Decoding(parts[0]);
  var ciphertext = base64Decoding(parts[1]);
  CBCBlockCipher cipher = new CBCBlockCipher(new AESFastEngine());
  ParametersWithIV<KeyParameter> cbcParams = new ParametersWithIV<KeyParameter>(new KeyParameter(key), iv);
  PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null> paddingParams = new PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null>(cbcParams, null);
  PaddedBlockCipherImpl paddingCipher = new PaddedBlockCipherImpl(new PKCS7Padding(), cipher);
  paddingCipher.init(false, paddingParams);
  return new String.fromCharCodes(paddingCipher.process(ciphertext));
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

void printC(String newString) {
  // for compatibility reasons to FlutterEmptyConsole
  print(newString);
}
