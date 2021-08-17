import 'dart:convert';
import 'dart:typed_data';
import 'package:flutter_sodium/flutter_sodium.dart';

main() {
/* add in pubspec.yaml:
dependencies:
  flutter_sodium: ^0.2.0
 */

  printC('Libsodium crypto box hybrid string encryption');
  final plaintext = 'The quick brown fox jumps over the lazy dog';
  printC('plaintext: ' + plaintext);

  // encryption
  printC('\n* * * Encryption * * *');
  printC('all data are in Base64 encoding');
  // for encryption you need your private key (privateKeyA) and the public key from other party (publicKeyB)
  String privateKeyABase64 = 'yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU=';
  String publicKeyBBase64 =  'jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=';
  var privateKeyA = base64Decoding(privateKeyABase64);
  var publicKeyB = base64Decoding(publicKeyBBase64);
  String ciphertextBase64 = cryptoboxEncryptionToBase64(privateKeyA, publicKeyB, plaintext);
  printC('ciphertext (Base64): ' + ciphertextBase64);
  printC('output is (Base64) nonce : (Base64) ciphertext ');

  printC('\n* * * Decryption * * *');
  var ciphertextReceivedBase64 = ciphertextBase64;
  // for decryption you need your private key (privateKeyB) and the public key from other party (publicKeyA)
  String privateKeyBBase64 = 'yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=';
  String publicKeyABase64 =  'b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0=';
  var privateKeyB = base64Decoding(privateKeyBBase64);
  var publicKeyA = base64Decoding(publicKeyABase64);
  printC('ciphertext (Base64): ' + ciphertextReceivedBase64);
  printC('input is (Base64) nonce : (Base64) ciphertext');
  var decryptedtext = cryptoboxDecryptionFromBase64(privateKeyB, publicKeyA, ciphertextReceivedBase64);
  printC('plaintext:  ' + decryptedtext);
}

String cryptoboxEncryptionToBase64(Uint8List privateKey, Uint8List publicKey, String plaintext) {
  var nonce = generateRandomNonce();
  var ciphertext = CryptoBox.encryptString(plaintext, nonce, publicKey, privateKey);
  return base64Encoding(nonce) + ':' + base64Encoding(ciphertext);
}

String cryptoboxDecryptionFromBase64(Uint8List privateKey, Uint8List publicKey, String data) {
  var parts = data.split(':');
  var nonce = base64Decoding(parts[0]);
  var ciphertext = base64Decoding(parts[1]);
  var deryptedtext = CryptoBox.decryptString(ciphertext, nonce, publicKey, privateKey);
  return deryptedtext;
}

Uint8List generateRandomNonce() {
  return CryptoBox.randomNonce();
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