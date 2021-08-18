import 'dart:convert';
import 'dart:math';
import 'dart:typed_data';
import "package:pointycastle/export.dart";

main() {
/* add in pubspec.yaml:
dependencies:
  pointycastle: ^3.1.1
 */

  printC('AES GCM 256 String encryption with random key');
  final plaintext = 'The quick brown fox jumps over the lazy dog';
  printC('plaintext: ' + plaintext);

  // generate random key
  var encryptionKey = generateRandomAesKey();
  var encryptionKeyBase64 = base64Encoding(encryptionKey);
  printC('encryptionKey (Base64): ' + encryptionKeyBase64);

  // encryption
  printC('\n* * * Encryption * * *');
  String ciphertextBase64 = aesGcmEncryptionToBase64(encryptionKey, plaintext);
  printC('ciphertext (Base64): ' + ciphertextBase64);
  printC('output is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag');

  printC('\n* * * Decryption * * *');
  var ciphertextDecryptionBase64 = ciphertextBase64;
  printC('ciphertext (Base64): ' + ciphertextDecryptionBase64);
  printC('input is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag');
  var decryptedtext = aesGcmDecryptionFromBase64(encryptionKey, ciphertextDecryptionBase64);
  printC('plaintext:  ' + decryptedtext);
}

String aesGcmEncryptionToBase64(Uint8List key, String plaintext) {
  var plaintextUint8 = createUint8ListFromString(plaintext);
  final nonce = generateRandomNonce();
  final cipher = GCMBlockCipher(AESFastEngine());
  var aeadParameters = AEADParameters(KeyParameter(key), 128, nonce, Uint8List(0));
  cipher.init(true, aeadParameters);
  var ciphertextWithTag = cipher.process(plaintextUint8);
  var ciphertextWithTagLength = ciphertextWithTag.lengthInBytes;
  var ciphertextLength = ciphertextWithTagLength - 16; // 16 bytes = 128 bit tag length
  var ciphertext = Uint8List.sublistView(ciphertextWithTag, 0, ciphertextLength);
  var gcmTag = Uint8List.sublistView(ciphertextWithTag, ciphertextLength, ciphertextWithTagLength);
  final nonceBase64 = base64.encode(nonce);
  final ciphertextBase64 = base64.encode(ciphertext);
  final gcmTagBase64 = base64.encode(gcmTag);
  return nonceBase64 + ':' + ciphertextBase64 + ':' + gcmTagBase64;
}

String aesGcmDecryptionFromBase64(Uint8List key, String data) {
  var parts = data.split(':');
  var nonce = base64.decode(parts[0]);
  var ciphertext = base64.decode(parts[1]);
  var gcmTag = base64.decode(parts[2]);
  var bb = BytesBuilder();
  bb.add(ciphertext);
  bb.add(gcmTag);
  var ciphertextWithTag = bb.toBytes();
  final cipher = GCMBlockCipher(AESFastEngine());
  var aeadParameters = AEADParameters(KeyParameter(key), 128, nonce, Uint8List(0));
  cipher.init(false, aeadParameters);
  return new String.fromCharCodes(cipher.process(ciphertextWithTag));
}

Uint8List generateRandomAesKey() {
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
