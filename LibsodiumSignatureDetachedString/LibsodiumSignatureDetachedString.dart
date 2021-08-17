import 'dart:convert';
import 'dart:typed_data';
import 'package:flutter_sodium/flutter_sodium.dart';

main() {
/* add in pubspec.yaml:
dependencies:
  flutter_sodium: ^0.2.0
 */

  printC('Libsodium detached signature string');
  final dataToSign = 'The quick brown fox jumps over the lazy dog';
  printC('plaintext: ' + dataToSign);

  // usually we would load the private and public key from a file or keystore
  // here we use hardcoded keys for demonstration - don't do this in real programs

  // signature
  printC('\n* * * sign the plaintext with the ED25519 private key * * *');
  var privateKey = loadEd25519PrivateKey();
  var signatureBase64 = libsodiumSignDetachedToBase64(privateKey, dataToSign);
  printC('signature Base64: ' + signatureBase64);

  // verification
  printC('\n* * * verify the signature against the plaintext with the ED25519 public key * * *');
  var publicKey = loadEd25519PublicKey();
  printC('signature Base64: ' + signatureBase64);
  var signatureVerified = libsodiumVerifyDetachedFromBase64(publicKey, dataToSign, signatureBase64);
  printC('signature (Base64) verified: ' + signatureVerified.toString());

}

String libsodiumSignDetachedToBase64(Uint8List privateKey, String dataToSign) {
  var signature = CryptoSign.signStringDetached(dataToSign, privateKey);
  return base64Encoding(signature);
}

bool libsodiumVerifyDetachedFromBase64(Uint8List publicKey, String dataToSign, String signatureBase64) {
  var signature = base64Decoding(signatureBase64);
  return CryptoSign.verifyString(signature, dataToSign, publicKey);
}

Uint8List loadEd25519PrivateKey() {
  return base64Decoding("Gjho7dILimbXJY8RvmQZEBczDzCYbZonGt/e5QQ3Vro2sjTp95EMxOrgEaWiprPduR/KJFBRA+RlDDpkvmhVfw==");
}

Uint8List loadEd25519PublicKey() {
  return base64Decoding("NrI06feRDMTq4BGloqaz3bkfyiRQUQPkZQw6ZL5oVX8=");
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
