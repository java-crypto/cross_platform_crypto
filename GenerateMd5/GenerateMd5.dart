import 'dart:convert';
import 'dart:typed_data';
import "package:pointycastle/export.dart";
import 'package:convert/convert.dart';

main() {
/* add in pubspec.yaml:
dependencies:
  pointycastle: ^3.1.1
 */

  printC('Generate a MD5 hash');

  printC('\n# # # SECURITY WARNING: This code is provided for achieve    # # #');
  printC('# # # compatibility between different programming languages. # # #');
  printC('# # # It is NOT SECURE - DO NOT USE THIS CODE ANY LONGER !   # # #');
  printC('# # # The hash algorithm MD5 is BROKEN.                      # # #');
  printC('# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n');

  final plaintext = 'The quick brown fox jumps over the lazy dog';
  printC('plaintext: ' + plaintext);

  Uint8List md5Value = calculateMd5(plaintext);
  printC('\nmd5Value (hex) length: ' + md5Value.length.toString() + ' data: ' + bytesToHex(md5Value));
}

Uint8List calculateMd5(String data) {
  var dataToDigest = createUint8ListFromString(data);
  var d = Digest('MD5');
  return d.process(dataToDigest);
}

String bytesToHex(Uint8List data) {
  return hex.encode(data);
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