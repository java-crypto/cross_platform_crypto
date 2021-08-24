import 'dart:convert';
import 'dart:typed_data';
import "package:pointycastle/export.dart";
import 'package:convert/convert.dart';

main() {
/* add in pubspec.yaml:
dependencies:
  pointycastle: ^3.1.1
 */

  printC('Generate a SHA-256 hash, Base64 en- and decoding and hex conversions');

  final plaintext = 'The quick brown fox jumps over the lazy dog';
  printC('plaintext: ' + plaintext);

  Uint8List sha256Value = calculateSha256(plaintext);
  printC('\nsha256Value (hex) length: ' + sha256Value.length.toString() + ' data: ' + bytesToHex(sha256Value));

  String sha256Base64 = base64Encoding(sha256Value);
  printC('sha256Value (base64):     ' + sha256Base64);

  Uint8List sha256ValueDecoded = base64Decoding(sha256Base64);
  printC('sha256Base64 decoded to a byte array:');
  printC('sha256Value (hex) length: ' + sha256ValueDecoded.length.toString() + ' data: ' + bytesToHex(sha256ValueDecoded));

  String sha256HexString = 'd7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592';
  Uint8List sha256Hex = hexStringToByteArray(sha256HexString);
  printC('sha256HexString converted to a byte array:');
  printC('sha256Value (hex) length: ' + sha256Hex.length.toString() + ' data: ' + bytesToHex(sha256Hex));
}

Uint8List calculateSha256(String data) {
  var dataToDigest = createUint8ListFromString(data);
  var d = Digest('SHA-256');
  return d.process(dataToDigest);
}

String bytesToHex(Uint8List data) {
  return hex.encode(data);
}

List<int> hexStringToByteArrayO(String input) {
  return hex.decode(input);
}

// If present, removes the 0x from the start of a hex-string.
String strip0x(String hex) {
  if (hex.startsWith('0x')) return hex.substring(2);
  return hex;
}

Uint8List hexStringToByteArray(String hexStr) {
  final bytes = hex.decode(strip0x(hexStr));
  if (bytes is Uint8List) return bytes;

  return Uint8List.fromList(bytes);
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
