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
  printC("# # # It uses FIXED key and IV's that should NEVER used,     # # #");
  printC("# # # instead use random generated keys and IVs.             # # #");
  printC("# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n");

  final plaintext = 'The quick brown fox jumps over the lazy dog';
  printC("plaintext: " + plaintext);
  
  printC("\nDES CBC String encryption with fixed key & iv");

  // fixed encryption key 8 bytes long
  String desEncryptionKey = "12345678";
  printC("desEncryptionKey: " + desEncryptionKey);

  // encryption
  printC("\n* * * Encryption * * *");
  String desCiphertextBase64 = desCbcEncryptToBase64(desEncryptionKey, plaintext);
  printC("ciphertext: " + desCiphertextBase64);
  printC("output is (Base64) iv : (Base64) ciphertext");
  printC("\n* * * Decryption * * *");
  printC("ciphertext: " + desCiphertextBase64);
  printC("input is (Base64) iv : (Base64) ciphertext");
  String desDecryptedtext = desCbcDecryptFromBase64(desEncryptionKey, desCiphertextBase64);
  printC("plaintext: " + desDecryptedtext);

  printC("\nTriple DES CBC String encryption with fixed key (16 bytes) & iv");

  // fixed encryption key 16 bytes long
  String des2EncryptionKey = "1234567890123456";
  printC("des2EncryptionKey: " + des2EncryptionKey);

  // encryption
  printC("\n* * * Encryption * * *");
  String des2CiphertextBase64 = des3CbcEncryptToBase64(des2EncryptionKey, plaintext);
  printC("ciphertext: " + des2CiphertextBase64);
  printC("output is (Base64) iv : (Base64) ciphertext");
  printC("\n* * * Decryption * * *");
  printC("ciphertext: " + des2CiphertextBase64);
  printC("input is (Base64) iv : (Base64) ciphertext");
  String des2Decryptedtext = des3CbcDecryptFromBase64(des2EncryptionKey, des2CiphertextBase64);
  printC("plaintext: " + des2Decryptedtext);

  printC("\nTriple DES CBC String encryption with fixed key (24 bytes) & iv");

  // fixed encryption key 24 bytes long
  String des3EncryptionKey = "123456789012345678901234";
  printC("des3EncryptionKey: " + des3EncryptionKey);

  // encryption
  printC("\n* * * Encryption * * *");
  String des3CiphertextBase64 = des3CbcEncryptToBase64(des3EncryptionKey, plaintext);
  printC("ciphertext: " + des3CiphertextBase64);
  printC("output is (Base64) iv : (Base64) ciphertext");
  printC("\n* * * Decryption * * *");
  printC("ciphertext: " + des3CiphertextBase64);
  printC("input is (Base64) iv : (Base64) ciphertext");
  String des3Decryptedtext = des3CbcDecryptFromBase64(des3EncryptionKey, des3CiphertextBase64);
  printC("plaintext: " + des3Decryptedtext);
}

String desCbcEncryptToBase64(String key, String plaintext) {
  // don't use this in production
  List<int> iv = generateFixedDesInitvector();
  // List<int> iv = generateRandomDesInitvector();
  DES desCBC = DES(
      key: key.codeUnits,
      mode: DESMode.CBC,
      iv: iv,
      paddingType: DESPaddingType.PKCS7);
  List<int> encrypted = desCBC.encrypt(plaintext.codeUnits);
  return base64Encoding(iv) + ":" + base64Encoding(encrypted);
}

String desCbcDecryptFromBase64(String key, String data) {
  var parts = data.split(':');
  var iv = base64Decoding(parts[0]);
  var ciphertext = base64Decoding(parts[1]);
  DES desCBC = DES(
      key: key.codeUnits,
      mode: DESMode.CBC,
      iv: iv,
      paddingType: DESPaddingType.PKCS7);
  List<int> decrypted = desCBC.decrypt(ciphertext);
  return utf8.decode(decrypted);
}

String des3CbcEncryptToBase64(String key, String plaintext) {
  // don't use this in production
  List<int> iv = generateFixedDesInitvector();
  // List<int> iv = generateRandomDesInitvector();
  DES3 desCBC = DES3(
      key: key.codeUnits,
      mode: DESMode.CBC,
      iv: iv,
      paddingType: DESPaddingType.PKCS7);
  List<int> encrypted = desCBC.encrypt(plaintext.codeUnits);
  return base64Encoding(iv) + ":" + base64Encoding(encrypted);
}

String des3CbcDecryptFromBase64(String key, String data) {
  var parts = data.split(':');
  var iv = base64Decoding(parts[0]);
  var ciphertext = base64Decoding(parts[1]);
  DES3 desCBC = DES3(
      key: key.codeUnits,
      mode: DESMode.CBC,
      iv: iv,
      paddingType: DESPaddingType.PKCS7);
  List<int> decrypted = desCBC.decrypt(ciphertext);
  return utf8.decode(decrypted);
}

List<int> generateRandomDesKey() {
  var random = Random.secure();
  // 8 bytes for des, 16 bytes for TDES 2 and 24 bytes for TDES 3
  return List<int>.generate(8, (i) => random.nextInt(256));
}

List<int> generateRandomDesInitvector() {
  var random = Random.secure();
  return List<int>.generate(8, (i) => random.nextInt(256));
}

List<int> generateFixedDesInitvector() {
  return createUint8ListFromString('12345678');
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
