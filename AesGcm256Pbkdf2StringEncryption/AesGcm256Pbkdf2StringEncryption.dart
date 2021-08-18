import 'dart:convert';
import 'dart:math';
import 'dart:typed_data';
import "package:pointycastle/export.dart";

main() {
/* add in pubspec.yaml:
dependencies:
  pointycastle: ^3.1.1
 */

  printC('AES GCM 256 String encryption with PBKDF2 derived key');
  final plaintext = 'The quick brown fox jumps over the lazy dog';
  printC('plaintext: ' + plaintext);
  final password = 'secret password';

  // encryption
  printC('\n* * * Encryption * * *');
  String ciphertextBase64 = aesGcmPbkdf2EncryptToBase64(password, plaintext);
  printC('ciphertext (Base64): ' + ciphertextBase64);
  printC('output is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag');

  printC('\n* * * Decryption * * *');
  var ciphertextDecryptionBase64 = ciphertextBase64;
  printC('ciphertext (Base64): ' + ciphertextDecryptionBase64);
  printC('input is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag');
  var decryptedtext = aesGcmPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64);
  printC('plaintext:  ' + decryptedtext);
}

String aesGcmPbkdf2EncryptToBase64(String password, String plaintext) {
  var plaintextUint8 = createUint8ListFromString(plaintext);
  var passphrase =  createUint8ListFromString(password);
  final PBKDF2_ITERATIONS = 15000;
  final salt = generateSalt32Byte();
  KeyDerivator derivator = new PBKDF2KeyDerivator(new HMac(new SHA256Digest(), 64));
  Pbkdf2Parameters params = new Pbkdf2Parameters(salt, PBKDF2_ITERATIONS, 32);
  derivator.init(params);
  final key = derivator.process(passphrase);
  final nonce = generateRandomNonce();
  final cipher = GCMBlockCipher(AESFastEngine());
  var aeadParameters = AEADParameters(KeyParameter(key), 128, nonce, Uint8List(0));
  cipher.init(true, aeadParameters);
  var ciphertextWithTag = cipher.process(plaintextUint8);
  var ciphertextWithTagLength = ciphertextWithTag.lengthInBytes;
  var ciphertextLength = ciphertextWithTagLength - 16; // 16 bytes = 128 bit tag length
  var ciphertext = Uint8List.sublistView(ciphertextWithTag, 0, ciphertextLength);
  var gcmTag = Uint8List.sublistView(ciphertextWithTag, ciphertextLength, ciphertextWithTagLength);
  final saltBase64 = base64Encoding(salt);
  final nonceBase64 = base64Encoding(nonce);
  final ciphertextBase64 = base64Encoding(ciphertext);
  final gcmTagBase64 = base64Encoding(gcmTag);
  return saltBase64 + ':' + nonceBase64 + ':' + ciphertextBase64 + ':' + gcmTagBase64;
}

String aesGcmPbkdf2DecryptFromBase64(String password, String data) {
  var parts = data.split(':');
  var salt = base64Decoding(parts[0]);
  var nonce = base64Decoding(parts[1]);
  var ciphertext = base64Decoding(parts[2]);
  var gcmTag = base64Decoding(parts[3]);
  var bb = BytesBuilder();
  bb.add(ciphertext);
  bb.add(gcmTag);
  var ciphertextWithTag = bb.toBytes();
  var passphrase =  createUint8ListFromString(password);
  final PBKDF2_ITERATIONS = 15000;
  KeyDerivator derivator = new PBKDF2KeyDerivator(new HMac(new SHA256Digest(), 64));
  Pbkdf2Parameters params = new Pbkdf2Parameters(salt, PBKDF2_ITERATIONS, 32);
  derivator.init(params);
  final key = derivator.process(passphrase);
  final cipher = GCMBlockCipher(AESFastEngine());
  var aeadParameters = AEADParameters(KeyParameter(key), 128, nonce, Uint8List(0));
  cipher.init(false, aeadParameters);
  return new String.fromCharCodes(cipher.process(ciphertextWithTag));
}

Uint8List generateSalt32Byte() {
  final _sGen = Random.secure();
  final _seed =
  Uint8List.fromList(List.generate(32, (n) => _sGen.nextInt(255)));
  SecureRandom sec = SecureRandom("Fortuna")..seed(KeyParameter(_seed));
  return sec.nextBytes(32);
}

Uint8List generateRandomNonce() {
  final _sGen = Random.secure();
  final _seed =
  Uint8List.fromList(List.generate(32, (n) => _sGen.nextInt(255)));
  SecureRandom sec = SecureRandom("Fortuna")..seed(KeyParameter(_seed));
  return sec.nextBytes(12);
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
