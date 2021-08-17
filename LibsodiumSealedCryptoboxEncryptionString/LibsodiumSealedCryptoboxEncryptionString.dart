import 'dart:convert';
import 'dart:typed_data';
import 'package:flutter_sodium/flutter_sodium.dart';

main() {
/* add in pubspec.yaml:
dependencies:
  flutter_sodium: ^0.2.0
 */

  printC('Libsodium sealed crypto box hybrid string encryption');
  final plaintext = 'The quick brown fox jumps over the lazy dog';
  printC('plaintext: ' + plaintext);

  // encryption
  printC('\n* * * Encryption * * *');
  // for encryption you need the public key from the one you are sending the data to (party B)
  String publicKeyBBase64 =  'jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=';
  var publicKeyB = base64Decoding(publicKeyBBase64);
  String ciphertextBase64 = sealedCryptoboxEncryptionToBase64(publicKeyB, plaintext);
  printC('ciphertext (Base64): ' + ciphertextBase64);
  printC('output is (Base64) ciphertext');

  printC('\n* * * Decryption * * *');
  // for decryption you need your private key (privateKeyB) and your public key (publicKeyB)
  String privateKeyBBase64 = "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=";
  var privateKeyB = base64Decoding(privateKeyBBase64);
  var ciphertextReceivedBase64 = ciphertextBase64;
  printC('ciphertext (Base64): ' + ciphertextReceivedBase64);
  printC('input is (Base64) ciphertext');
  var decryptedtext = sealedCryptoboxDecryptionFromBase64(privateKeyB, publicKeyB, ciphertextReceivedBase64);
  printC('plaintext:  ' + decryptedtext);
}

String sealedCryptoboxEncryptionToBase64(Uint8List publicKey, String plaintext) {
  var ciphertext = SealedBox.sealString(plaintext, publicKey);
  return base64Encoding(ciphertext);
}

String sealedCryptoboxDecryptionFromBase64(Uint8List privateKey, Uint8List publicKey, String data) {
  KeyPair keyPairB = new KeyPair(pk: publicKey, sk: privateKey);
  var deryptedtext = SealedBox.openString(base64Decoding(data), keyPairB);
  return deryptedtext;
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
