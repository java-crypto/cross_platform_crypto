import 'dart:convert';
import 'dart:math';
import 'dart:typed_data';
import "package:pointycastle/export.dart";

main() {
/* add in pubspec.yaml:
dependencies:
  pointycastle: ^3.1.1
 */

  printC('Generate a 32 byte long AES key');
  var aesKey = generateRandomAesKey();
  printC('aesKey (Base64): ' + base64Encoding(aesKey));

  printC('\nInitialization vector (IV)');
  var iv = generateRandomAesKey();
  printC('iv (Base64): ' + base64Encoding(iv));

  printC('\nGenerate a 12 byte long nonce for AES GCM');
  var nonce = generateRandomNonce();
  printC('nonce (Base64): ' + base64Encoding(nonce));
}

Uint8List generateRandomAesKey() {
  final _sGen = Random.secure();
  final _seed =
  Uint8List.fromList(List.generate(32, (n) => _sGen.nextInt(255)));
  SecureRandom sec = SecureRandom("Fortuna")..seed(KeyParameter(_seed));
  return sec.nextBytes(32);
}

Uint8List  generateRandomInitvector() {
  final _sGen = Random.secure();
  final _seed =
  Uint8List.fromList(List.generate(32, (n) => _sGen.nextInt(255)));
  SecureRandom sec = SecureRandom("Fortuna")..seed(KeyParameter(_seed));
  return sec.nextBytes(16);
}

Uint8List generateRandomNonce() {
  final _sGen = Random.secure();
  final _seed =
  Uint8List.fromList(List.generate(32, (n) => _sGen.nextInt(255)));
  SecureRandom sec = SecureRandom("Fortuna")..seed(KeyParameter(_seed));
  return sec.nextBytes(12);
}

String base64Encoding(Uint8List input) {
  return base64.encode(input);
}

void printC(String newString) {
  // for compatibility reasons to FlutterEmptyConsole
  print(newString);
}
