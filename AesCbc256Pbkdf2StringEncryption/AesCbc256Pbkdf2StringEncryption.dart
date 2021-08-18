import 'dart:convert';
import 'dart:math';
import 'dart:typed_data';
import "package:pointycastle/export.dart";

main() {
/* add in pubspec.yaml:
dependencies:
  pointycastle: ^3.1.1
 */

  printC('AES CBC 256 String encryption with PBKDF2 derived key');
  final plaintext = 'The quick brown fox jumps over the lazy dog';
  printC('plaintext: ' + plaintext);
  final password = 'secret password';

  // encryption
  printC('\n* * * Encryption * * *');
  String ciphertextBase64 = aesCbcPbkdf2EncryptToBase64(password, plaintext);
  printC('ciphertext (Base64): ' + ciphertextBase64);
  printC('output is (Base64) salt : (Base64) iv : (Base64) ciphertext');

  printC('\n* * * Decryption * * *');
  var ciphertextDecryptionBase64 = ciphertextBase64;
  printC('ciphertext (Base64): ' + ciphertextDecryptionBase64);
  printC('input is (Base64) salt : (Base64) iv : (Base64) ciphertext');
  var decryptedtext = aesCbcPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64);
  printC('plaintext:  ' + decryptedtext);
}

String aesCbcPbkdf2EncryptToBase64(String password, String plaintext) {
  var plaintextUint8 = createUint8ListFromString(plaintext);
  var passphrase =  createUint8ListFromString(password);
  final PBKDF2_ITERATIONS = 15000;
  final salt = generateSalt32Byte();
  KeyDerivator derivator = new PBKDF2KeyDerivator(new HMac(new SHA256Digest(), 64));
  Pbkdf2Parameters params = new Pbkdf2Parameters(salt, PBKDF2_ITERATIONS, 32);
  derivator.init(params);
  final key = derivator.process(passphrase);
  final iv = generateRandomIv();
  final CBCBlockCipher cipher = new CBCBlockCipher(new AESFastEngine());
  ParametersWithIV<KeyParameter> cbcParams = new ParametersWithIV<KeyParameter>(new KeyParameter(key), iv);
  PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null> paddingParams = new PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null>(cbcParams, null);
  PaddedBlockCipherImpl paddingCipher = new PaddedBlockCipherImpl(new PKCS7Padding(), cipher);
  paddingCipher.init(true, paddingParams);
  final ciphertext = paddingCipher.process(plaintextUint8);
  final saltBase64 = base64Encoding(salt);
  final ivBase64 = base64Encoding(iv);
  final ciphertextBase64 = base64Encoding(ciphertext);
  return saltBase64 + ':' + ivBase64 + ':' + ciphertextBase64;
}

String aesCbcPbkdf2DecryptFromBase64(String password, String data) {
  var parts = data.split(':');
  var salt = base64Decoding(parts[0]);
  var iv = base64Decoding(parts[1]);
  var ciphertext = base64Decoding(parts[2]);
  var passphrase =  createUint8ListFromString(password);
  final PBKDF2_ITERATIONS = 15000;
  KeyDerivator derivator = new PBKDF2KeyDerivator(new HMac(new SHA256Digest(), 64));
  Pbkdf2Parameters params = new Pbkdf2Parameters(salt, PBKDF2_ITERATIONS, 32);
  derivator.init(params);
  final key = derivator.process(passphrase);
  CBCBlockCipher cipher = new CBCBlockCipher(new AESFastEngine());
  ParametersWithIV<KeyParameter> cbcParams = new ParametersWithIV<KeyParameter>(new KeyParameter(key), iv);
  PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null> paddingParams = new PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null>(cbcParams, null);
  PaddedBlockCipherImpl paddingCipher = new PaddedBlockCipherImpl(new PKCS7Padding(), cipher);
  paddingCipher.init(false, paddingParams);
  return new String.fromCharCodes(paddingCipher.process(ciphertext));
}

Uint8List generateSalt32Byte() {
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
