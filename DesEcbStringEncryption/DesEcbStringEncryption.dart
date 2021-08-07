import 'dart:convert';
import 'dart:typed_data';
import 'dart:math';

import 'package:dart_des/dart_des.dart';

main() {
/* add in pubspec.yaml:
dependencies:
  dart_des: ^1.0.0
  # https://pub.dev/packages/dart_des/
 */
  printC("\n# # # SECURITY WARNING: This code is provided for achieve    # # #");
  printC("# # # compatibility between different programming languages. # # #");
  printC("# # # It is not necessarily fully secure.                    # # #");
  printC("# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #");
  printC("# # # It uses FIXED key that should NEVER used,              # # #");
  printC("# # # instead use random generated keys.                     # # #");
  printC("# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n");

  final plaintext = 'The quick brown fox jumps over the lazy dog';
  printC("plaintext: " + plaintext);
  
  printC("\nDES ECB String encryption with fixed key");

  // fixed encryption key 8 bytes long
  String desEncryptionKey = "12345678";
  printC("desEncryptionKey: " + desEncryptionKey);

  // encryption
  printC("\n* * * Encryption * * *");
  String desCiphertextBase64 = desEcbEncryptToBase64(desEncryptionKey, plaintext);
  printC("ciphertext: " + desCiphertextBase64);
  printC("output is (Base64) ciphertext");
  printC("\n* * * Decryption * * *");
  printC("ciphertext: " + desCiphertextBase64);
  printC("input is (Base64) ciphertext");
  String desDecryptedtext = desEcbDecryptFromBase64(desEncryptionKey, desCiphertextBase64);
  printC("plaintext: " + desDecryptedtext);

  printC("\nTriple DES ECB String encryption with fixed key (16 bytes)");

  // fixed encryption key 16 bytes long
  String des2EncryptionKey = "1234567890123456";
  printC("des2EncryptionKey: " + des2EncryptionKey);

  // encryption
  printC("\n* * * Encryption * * *");
  String des2CiphertextBase64 = des3EcbEncryptToBase64(des2EncryptionKey, plaintext);
  printC("ciphertext: " + des2CiphertextBase64);
  printC("output is (Base64) ciphertext");
  printC("\n* * * Decryption * * *");
  printC("ciphertext: " + des2CiphertextBase64);
  printC("input is (Base64) ciphertext");
  String des2Decryptedtext = des3EcbDecryptFromBase64(des2EncryptionKey, des2CiphertextBase64);
  printC("plaintext: " + des2Decryptedtext);

  printC("\nTriple DES ECB String encryption with fixed key (24 bytes)");

  // fixed encryption key 24 bytes long
  String des3EncryptionKey = "123456789012345678901234";
  printC("des3EncryptionKey: " + des3EncryptionKey);

  // encryption
  printC("\n* * * Encryption * * *");
  String des3CiphertextBase64 = des3EcbEncryptToBase64(des3EncryptionKey, plaintext);
  printC("ciphertext: " + des3CiphertextBase64);
  printC("output is (Base64) ciphertext");
  printC("\n* * * Decryption * * *");
  printC("ciphertext: " + des3CiphertextBase64);
  printC("input is (Base64) ciphertext");
  String des3Decryptedtext = des3EcbDecryptFromBase64(des3EncryptionKey, des3CiphertextBase64);
  printC("plaintext: " + des3Decryptedtext);
}

String desEcbEncryptToBase64(String key, String plaintext) {
  DES desECB = DES(
      key: key.codeUnits,
      mode: DESMode.ECB,
      paddingType: DESPaddingType.PKCS7);
  List<int> encrypted = desECB.encrypt(plaintext.codeUnits);
  return base64Encoding(encrypted);
}

String desEcbDecryptFromBase64(String key, String data) {
  var ciphertext = base64Decoding(data);
  DES desECB = DES(
      key: key.codeUnits,
      mode: DESMode.ECB,
      paddingType: DESPaddingType.PKCS7);
  List<int> decrypted = desECB.decrypt(ciphertext);
  return utf8.decode(decrypted);
}

String des3EcbEncryptToBase64(String key, String plaintext) {
  DES3 desECB = DES3(
      key: key.codeUnits,
      mode: DESMode.ECB,
      paddingType: DESPaddingType.PKCS7);
  List<int> encrypted = desECB.encrypt(plaintext.codeUnits);
  return base64Encoding(encrypted);
}

String des3EcbDecryptFromBase64(String key, String data) {
  var ciphertext = base64Decoding(data);
  DES3 desECB = DES3(
      key: key.codeUnits,
      mode: DESMode.ECB,
      paddingType: DESPaddingType.PKCS7);
  List<int> decrypted = desECB.decrypt(ciphertext);
  return utf8.decode(decrypted);
}

List<int> generateRandomDesKey() {
  var random = Random.secure();
  // 8 bytes for des, 16 bytes for TDES 2 and 24 bytes for TDES 3
  return List<int>.generate(8, (i) => random.nextInt(256));
}

String base64Encoding(List<int> input){
  return base64.encode(input);
}

List<int> base64Decoding(String input){
  return base64.decode(input);
}

Uint8List createUint8ListFromString(String s) {
  var ret = new Uint8List(s.length);
  for (var i = 0; i < s.length; i++) {
    ret[i] = s.codeUnitAt(i);
  }
  return ret;
}

void printC(String newString) {
  // for compatibility reasons to FlutterEmptyConsole
  print(newString);
}
