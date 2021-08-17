import 'dart:convert';
import 'dart:typed_data';
import 'package:flutter_sodium/flutter_sodium.dart';

main() {
/* add in pubspec.yaml:
dependencies:
  flutter_sodium: ^0.2.0
 */

  printC('Generate ED25519 private and public key and derive public key from private key');

  KeyPair keyPair = generateEd25519KeyPair();
  Uint8List privateKey = keyPair.sk;
  Uint8List publicKey = keyPair.pk;
  printC('privateKey (Base64): ' + base64Encoding(privateKey));
  printC('publicKey (Base64):  ' + base64Encoding(publicKey));

  printC('\nderive the publicKey from the privateKey');
  Uint8List publicKeyDerived = deriveEd25519PublicKey(privateKey);
  printC('publicKey (Base64):  ' + base64Encoding(publicKeyDerived));
}

KeyPair generateEd25519KeyPair(){
  return CryptoSign.randomKeys();
}

Uint8List deriveEd25519PublicKey(Uint8List privateKey) {
  return CryptoSign.extractPublicKey(privateKey);
}

String base64Encoding(Uint8List input) {
  return base64.encode(input);
}

void printC(String newString) {
  // for compatibility reasons to FlutterEmptyConsole
  print(newString);
}