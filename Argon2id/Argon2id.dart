import 'dart:convert';
import 'dart:typed_data';
import 'package:flutter_sodium/flutter_sodium.dart';

main() {
/* add in pubspec.yaml:
dependencies:
  flutter_sodium: ^0.2.0
 */
  printC('Generate a 32 byte long encryption key with Argon2id');

  final password = 'secret password';
  printC('password: ' + password);

  // ### security warning - never use a fixed salt in production, this is for compare reasons only
  var salt = generateFixedSalt16Byte();
  // please use below generateSalt16Byte()
  //var salt = generateSalt16Byte();
  printC('salt (Base64): ' + base64Encoding(salt));

  // ### the minimal parameter set is probably UNSECURE ###
  var encryptionKeyArgon2id = generateArgon2idMinimal(password, salt);
  printC('encryptionKeyArgon2id (Base64) minimal:     ' + base64Encoding(encryptionKeyArgon2id));
  print('encryptionKeyArgon2id (Base64) minimal:     ' + base64Encoding(encryptionKeyArgon2id));

  encryptionKeyArgon2id = generateArgon2idInteractive(password, salt);
  printC('encryptionKeyArgon2id (Base64) interactive: ' + base64Encoding(encryptionKeyArgon2id));
  print('encryptionKeyArgon2id (Base64) interactive: ' + base64Encoding(encryptionKeyArgon2id));

  encryptionKeyArgon2id = generateArgon2idModerate(password, salt);
  printC('encryptionKeyArgon2id (Base64) moderate:    ' + base64Encoding(encryptionKeyArgon2id));
  print('encryptionKeyArgon2id (Base64) moderate:    ' + base64Encoding(encryptionKeyArgon2id));

  // using flutter_sodium version 0.2.0 causes an error
  //encryptionKeyArgon2id = generateArgon2idSensitive(password, salt);
  //printC('encryptionKeyArgon2id (Base64) sensitive:   ' + base64Encoding(encryptionKeyArgon2id));
  //print('encryptionKeyArgon2id (Base64) sensitive:   ' + base64Encoding(encryptionKeyArgon2id));
  printC('encryptionKeyArgon2id (Base64) sensitive:   ' + 'not available with flutter_sodium 0.2.0');
}

Uint8List generateArgon2idMinimal(String password, Uint8List salt) {
  final outlen = 32;
  final passwd = utf8.encoder.convert(password);
  final opslimit = 2;
  final memlimit = 8192 * 1024;
  final alg = Sodium.cryptoPwhashAlgArgon2id13;
  final hash =
  Sodium.cryptoPwhash(outlen, passwd, salt, opslimit, memlimit, alg);
  return hash;
}

Uint8List generateArgon2idInteractive(String password, Uint8List salt) {
  final outlen = 32;
  final passwd = utf8.encoder.convert(password);
  final opslimit = 2;
  final memlimit = 66536 * 1024;
  final alg = Sodium.cryptoPwhashAlgArgon2id13;
  final hash =
  Sodium.cryptoPwhash(outlen, passwd, salt, opslimit, memlimit, alg);
  return hash;
}

Uint8List generateArgon2idModerate(String password, Uint8List salt) {
  final outlen = 32;
  final passwd = utf8.encoder.convert(password);
  final opslimit = 3;
  final memlimit = 262144 * 1024;
  final alg = Sodium.cryptoPwhashAlgArgon2id13;
  final hash =
  Sodium.cryptoPwhash(outlen, passwd, salt, opslimit, memlimit, alg);
  return hash;
}

// flutter_sodium 0.2.0 causes an error
Uint8List generateArgon2idSensitive(String password, Uint8List salt) {
  final outlen = 32;
  final passwd = utf8.encoder.convert(password);
  final opslimit = 4;
  final memlimit = 1048576 * 1024;
  final alg = Sodium.cryptoPwhashAlgArgon2id13;
  final hash =
  Sodium.cryptoPwhash(outlen, passwd, salt, opslimit, memlimit, alg);
  return hash;
}

Uint8List generateSalt16Byte() {
  return Sodium.randombytesBuf(16);
}

Uint8List generateFixedSalt16Byte() {
  // ### security warning - never use this in production ###
  return new Uint8List(16);
}

String base64Encoding(Uint8List input) {
  return base64.encode(input);
}

void printC(String newString) {
  // for compatibility reasons to FlutterEmptyConsole
  print(newString);
}