import 'dart:convert';
import 'dart:math';
import 'dart:typed_data';
import 'package:collection/collection.dart';
import "package:pointycastle/export.dart";

main() {
/* add in pubspec.yaml:
dependencies:
  pointycastle: ^3.1.1
 */

  printC('AES CBC 256 String encryption with PBKDF2 derived key and HMAC check');
  final plaintext = 'The quick brown fox jumps over the lazy dog';
  printC('plaintext: ' + plaintext);
  final password = 'secret password';

  // encryption
  printC('\n* * * Encryption * * *');
  String ciphertextHmacBase64 = aesCbcPbkdf2HmacEncryptToBase64(password, plaintext);
  printC('ciphertextHmac (Base64): ' + ciphertextHmacBase64);
  printC('output is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac');

  printC('\n* * * Decryption * * *');
  var ciphertextHmacDecryptionBase64 = ciphertextHmacBase64;
  printC('ciphertextHmac (Base64): ' + ciphertextHmacDecryptionBase64);
  printC('input is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac');
  var decryptedtext = aesCbcPbkdf2HmacDecryptFromBase64(password, ciphertextHmacDecryptionBase64);
  printC('plaintext:  ' + decryptedtext);
}

String aesCbcPbkdf2HmacEncryptToBase64(String password, String plaintext) {
  var plaintextUint8 = createUint8ListFromString(plaintext);
  var passphrase =  createUint8ListFromString(password);
  final PBKDF2_ITERATIONS = 15000;
  final salt = generateSalt32Byte();
  KeyDerivator derivator = new PBKDF2KeyDerivator(new HMac(new SHA256Digest(), 64));
  Pbkdf2Parameters params = new Pbkdf2Parameters(salt, PBKDF2_ITERATIONS, 64); // 2 * 32 for aes + hmac
  derivator.init(params);
  final keyAesHmac = derivator.process(passphrase);
  var keyAes = Uint8List.sublistView(keyAesHmac, 0, 32);
  var keyHmac = Uint8List.sublistView(keyAesHmac, 32, 64);
  final iv = generateRandomIv();
  final CBCBlockCipher cipher = new CBCBlockCipher(new AESFastEngine());
  ParametersWithIV<KeyParameter> cbcParams = new ParametersWithIV<KeyParameter>(new KeyParameter(keyAes), iv);
  PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null> paddingParams = new PaddedBlockCipherParameters<ParametersWithIV<KeyParameter>, Null>(cbcParams, null);
  PaddedBlockCipherImpl paddingCipher = new PaddedBlockCipherImpl(new PKCS7Padding(), cipher);
  paddingCipher.init(true, paddingParams);
  final ciphertext = paddingCipher.process(plaintextUint8);
  final saltBase64 = base64Encoding(salt);
  final ivBase64 = base64Encoding(iv);
  final ciphertextBase64 = base64Encoding(ciphertext);
  // calculate hmac over salt, iv and ciphertext
  final ciphertextWithoutHmac = createUint8ListFromString(saltBase64 + ':' + ivBase64 + ':' + ciphertextBase64);
  final hmac = HMac(SHA256Digest(), 64) // for HMAC SHA-256, block length must be 64
    ..init(KeyParameter(keyHmac));
  final hmacBase64 = base64Encoding(hmac.process(ciphertextWithoutHmac));
  return saltBase64 + ':' + ivBase64 + ':' + ciphertextBase64 + ':' + hmacBase64;
}

String aesCbcPbkdf2HmacDecryptFromBase64(String password, String data) {
  var parts = data.split(':');
  var salt = base64Decoding(parts[0]);
  var iv = base64Decoding(parts[1]);
  var ciphertext = base64Decoding(parts[2]);
  var hmac = base64Decoding(parts[3]);
  var passphrase =  createUint8ListFromString(password);
  final PBKDF2_ITERATIONS = 15000;
  KeyDerivator derivator = new PBKDF2KeyDerivator(new HMac(new SHA256Digest(), 64));
  Pbkdf2Parameters params = new Pbkdf2Parameters(salt, PBKDF2_ITERATIONS, 64); // 2 * 32 for aes + hmac
  derivator.init(params);
  final keyAesHmac = derivator.process(passphrase);
  var keyAes = Uint8List.sublistView(keyAesHmac, 0, 32);
  var keyHmac = Uint8List.sublistView(keyAesHmac, 32, 64);
  // before we decrypt we have to check the hmac
  final ciphertextWithoutHmac = createUint8ListFromString(base64Encoding(salt) + ':' + base64Encoding(iv) + ':' + base64Encoding(ciphertext));
  final hmacCheck = HMac(SHA256Digest(), 64) // for HMAC SHA-256, block length must be 64
    ..init(KeyParameter(keyHmac));
  final hmacCalculated = hmacCheck.process(ciphertextWithoutHmac);
  Function eq = const ListEquality().equals;
  final hmacVerified = eq(hmac, hmacCalculated);
  if (!hmacVerified) {
    printC('Error: HMAC-check failed, no decryption possible');
    return '';
  }
  CBCBlockCipher cipher = new CBCBlockCipher(new AESFastEngine());
  ParametersWithIV<KeyParameter> cbcParams = new ParametersWithIV<KeyParameter>(new KeyParameter(keyAes), iv);
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
