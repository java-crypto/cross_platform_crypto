import 'dart:convert';
import 'dart:typed_data';
import 'package:flutter_sodium/flutter_sodium.dart';

main() {
/* add in pubspec.yaml:
dependencies:
  flutter_sodium: ^0.2.0
 */

  printC('Libsodium secret box random key string encryption');
  final plaintext = 'The quick brown fox jumps over the lazy dog';
  printC('plaintext: ' + plaintext);

  // generate random key
  String keyBase64 = base64Encoding(generateRandomKey());
  printC('encryptionKey (Base64): ' + keyBase64);

  // encryption
  printC('\n* * * Encryption * * *');
  String ciphertextBase64 = secretboxEncryptionToBase64(keyBase64, plaintext);
  printC('ciphertext (Base64): ' + ciphertextBase64);
  printC('output is (Base64) nonce : (Base64) ciphertext');

  printC('\n* * * Decryption * * *');
  var ciphertextReceivedBase64 = ciphertextBase64;
  printC('ciphertext (Base64): ' + ciphertextReceivedBase64);
  printC('input is (Base64) nonce : (Base64) ciphertext');
  var decryptedtext = secretboxDecryptionFromBase64(keyBase64, ciphertextReceivedBase64);
  printC('plaintext:  ' + decryptedtext);
}

String secretboxEncryptionToBase64(String keyBase64, String plaintext){
  var key = base64Decoding(keyBase64);
  var nonce = generateRandomSecretboxNonce();
  var ciphertext = SecretBox.encryptString(plaintext, nonce, key);
  return base64Encoding(nonce) + ':' + base64Encoding(ciphertext);
}

String secretboxDecryptionFromBase64(String keyBase64, String data) {
  var key = base64Decoding(keyBase64);
  var parts = data.split(':');
  var nonce = base64Decoding(parts[0]);
  var ciphertext = base64Decoding(parts[1]);
  var deryptedtext = SecretBox.decryptString(ciphertext, nonce, key);
  return deryptedtext;
}

Uint8List generateRandomKey() {
  return SecretBox.randomKey();
}

Uint8List generateRandomSecretboxNonce() {
  return SecretBox.randomNonce();
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