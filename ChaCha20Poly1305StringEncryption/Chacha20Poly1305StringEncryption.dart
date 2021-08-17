import 'dart:convert';
import 'dart:math';
import 'dart:typed_data';
import "package:pointycastle/export.dart";

main() {
/* add in pubspec.yaml:
dependencies:
  pointycastle: ^3.1.1
 */

  print('ChaCha20-Poly1305 String encryption with random key full\n');
  final plaintext = 'The quick brown fox jumps over the lazy dog';
  print('plaintext: ' + plaintext);

  // generate random key
  var encryptionKey = generateRandomAesKey();
  var encryptionKeyBase64 = base64Encoding(encryptionKey);
  print('encryptionKey (Base64): ' + encryptionKeyBase64);

  print('\n* * * Encryption * * *');
  var ciphertextBase64 = chacha20Poly1305EncryptToBase64(encryptionKey, plaintext);
  print('ciphertext (Base64): ' + ciphertextBase64);
  print('output is (Base64) nonce : (Base64) ciphertext : (Base64) poly1305Tag');

  print('\n* * * Decryption * * *');
  var ciphertextDecryptionBase64 = ciphertextBase64;
  print('ciphertext (Base64): ' + ciphertextDecryptionBase64);
  print('input is (Base64) nonce : (Base64) ciphertext : (Base64) poly1305Tag');
  var decryptedtext = chacha20Poly1305DecryptFromBase64(encryptionKey, ciphertextDecryptionBase64);
  print('plaintext:  ' + decryptedtext);
}

String chacha20Poly1305EncryptToBase64(Uint8List key, String plaintext) {
  var plaintextUint8 = createUint8ListFromString(plaintext);
  final nonce = generateRandomNonce();
  var parameters = AEADParameters(KeyParameter(key), 128, nonce, Uint8List(0));
  var chaChaEngine = ChaCha20Poly1305(ChaCha7539Engine(), Poly1305())
    ..init(true, parameters);
  var enc = Uint8List(chaChaEngine.getOutputSize(plaintextUint8.length));
  var len = chaChaEngine.processBytes(plaintextUint8, 0, plaintextUint8.length, enc, 0);
  len += chaChaEngine.doFinal(enc, len);
  if (enc.length != len) {
    throw StateError('');
  }
  var ciphertextWithTag = enc;
  var ciphertextWithTagLength = ciphertextWithTag.lengthInBytes;
  var ciphertextLength = ciphertextWithTagLength - 16; // 16 bytes = 128 bit tag length
  var ciphertext = Uint8List.sublistView(ciphertextWithTag, 0, ciphertextLength);
  var macTag = Uint8List.sublistView(ciphertextWithTag, ciphertextLength, ciphertextWithTagLength);
  final nonceBase64 = base64.encode(nonce);
  final ciphertextBase64 = base64.encode(ciphertext);
  final poly1305TagBase64 = base64.encode(macTag);
  return nonceBase64 + ':' + ciphertextBase64 + ':' + poly1305TagBase64;
}

String chacha20Poly1305DecryptFromBase64(Uint8List key, String data) {
  var parts = data.split(':');
  var nonce = base64.decode(parts[0]);
  var ciphertext = base64.decode(parts[1]);
  var poly1305Tag = base64.decode(parts[2]);
  var bb = BytesBuilder();
  bb.add(ciphertext);
  bb.add(poly1305Tag);
  var ciphertextWithTag = bb.toBytes();
  var parameters = AEADParameters(KeyParameter(key), 128, nonce, Uint8List(0));
  var chaChaEngine = ChaCha20Poly1305(ChaCha7539Engine(), Poly1305())
    ..init(false, parameters);
  var dec = Uint8List(chaChaEngine.getOutputSize(ciphertextWithTag.length));
  var len = chaChaEngine.processBytes(ciphertextWithTag, 0, ciphertextWithTag.length, dec, 0);
  len += chaChaEngine.doFinal(dec, len);
  var mac = chaChaEngine.mac;
  return new String.fromCharCodes(dec);
}

Uint8List generateRandomAesKey() {
  final _sGen = Random.secure();
  final _seed =
  Uint8List.fromList(List.generate(32, (n) => _sGen.nextInt(255)));
  SecureRandom sec = SecureRandom("Fortuna")..seed(KeyParameter(_seed));
  return sec.nextBytes(32);
}

Uint8List generateRandomNonce() {
  final _sGen = Random.secure();
  final _seed =
  Uint8List.fromList(List.generate(32, (n) => _sGen.nextInt(255)));
  SecureRandom sec = SecureRandom("Fortuna")..seed(KeyParameter(_seed));
  return sec.nextBytes(12);
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
