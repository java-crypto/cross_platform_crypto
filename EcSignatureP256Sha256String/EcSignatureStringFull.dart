import 'dart:convert';
import 'dart:typed_data';
import 'package:pointycastle/asn1.dart';
import 'package:pointycastle/asn1/object_identifiers.dart';
import "package:pointycastle/export.dart";
import 'package:basic_utils/basic_utils.dart';

void main() {
/* add in pubspec.yaml:
dependencies:
  pointycastle: ^3.1.1
  basic_utils: ^3.4.0
 */
  // https://pub.dev/packages/pointycastle
  // https://github.com/bcgit/pc-dart/
  // https://pub.dev/packages/basic_utils
  // https://github.com/Ephenodrom/Dart-Basic-Utils

  print('EC signature string (ECDSA with SHA256)');

  final dataToSignString = 'The quick brown fox jumps over the lazy dog';
  final dataToSign = createUint8ListFromString(dataToSignString);
  print('plaintext: ' + dataToSignString);

  // # # # usually we would load the private and public key from a file or keystore # # #
  // # # # here we use hardcoded keys for demonstration - don't do this in real programs # # #

  print('\n* * * sign the plaintext with the EC private key * * *');
  final privateKeyPem = loadEcPrivateKeyPem();
  // get the data from PEM
  Uint8List ecPrivateKeyPkcs8Der =
      CryptoUtils.getBytesFromPEMString(privateKeyPem);
  ECPrivateKey ecPrivateKey =
      ecPrivateKeyFromDerBytesPkcs8(ecPrivateKeyPkcs8Der);
  final signatureBase64 = ecSignToBase64(ecPrivateKey, dataToSign);
  print('signature (Base64): ' + signatureBase64);

  print(
      '\n* * * verify the signature against the plaintext with the EC public key * * *');
  final publicKeyPem = loadEcPublicKeyPem();
  final ecPublicKey =
      CryptoUtils.ecPublicKeyFromPem(publicKeyPem) as ECPublicKey;
  bool signatureVerifiedBase64 =
      ecVerifySignatureFromBase64(ecPublicKey, dataToSign, signatureBase64);
  print('signature (Base64) verified: ' + signatureVerifiedBase64.toString());
}

String ecSignToBase64(ECPrivateKey privateKey, Uint8List messageByte) {
  ECSignature ecSignature = CryptoUtils.ecSign(privateKey, messageByte,
      algorithmName: 'SHA-256/ECDSA');
  BigInt r = ecSignature.r;
  BigInt s = ecSignature.s;
  Uint8List rUint8ListV2 = encodeBigInt(r) as Uint8List;
  Uint8List sUint8ListV2 = encodeBigInt(s) as Uint8List;
  var bbV2 = BytesBuilder();
  bbV2.add(rUint8ListV2);
  bbV2.add(sUint8ListV2);
  var signature = bbV2.toBytes();
  return base64Encoding(signature);
}

bool ecVerifySignatureFromBase64(
    ECPublicKey publicKey, Uint8List messageByte, String signatureBase64) {
  var signatureValue = base64Decoding(signatureBase64);
  BigInt r = decodeBigInt(Uint8List.sublistView(signatureValue, 0, 32));
  BigInt s = decodeBigInt(Uint8List.sublistView(signatureValue, 32, 64));
  ECSignature signature = new ECSignature(r, s);
  return CryptoUtils.ecVerify(publicKey, messageByte, signature,
      algorithm: 'SHA-256/ECDSA');
}

Uint8List createUint8ListFromString(String s) {
  var ret = new Uint8List(s.length);
  for (var i = 0; i < s.length; i++) {
    ret[i] = s.codeUnitAt(i);
  }
  return ret;
}

// http://phoenix.yizimg.com/ethereumdart/rlp/blob/master/lib/src/pointycastle-utils.dart
/// Decode a BigInt from bytes in big-endian encoding.
BigInt decodeBigInt(List<int> bytes) {
  BigInt result = new BigInt.from(0);
  for (int i = 0; i < bytes.length; i++) {
    result += new BigInt.from(bytes[bytes.length - i - 1]) << (8 * i);
  }
  return result;
}

var _byteMask = new BigInt.from(0xff);

/// Encode a BigInt into bytes using big-endian encoding.
Uint8List encodeBigInt(BigInt number) {
  // Not handling negative numbers. Decide how you want to do that.
  int size = (number.bitLength + 7) >> 3;
  var result = new Uint8List(size);
  for (int i = 0; i < size; i++) {
    result[size - i - 1] = (number & _byteMask).toInt();
    number = number >> 8;
  }
  return result;
}

///
/// Decode the given [bytes] in PKCS8 encoding into an [ECPrivateKey].
///
ECPrivateKey ecPrivateKeyFromDerBytesPkcs8(Uint8List bytes) {
  ASN1Parser asn1Parser = ASN1Parser(bytes);
  ASN1Sequence topLevelSeq = asn1Parser.nextObject() as ASN1Sequence;
  ASN1Sequence innerSeq = topLevelSeq.elements!.elementAt(1) as ASN1Sequence;
  ASN1ObjectIdentifier b2 =
      innerSeq.elements!.elementAt(1) as ASN1ObjectIdentifier;
  String? b2Data = b2.objectIdentifierAsString;
  Map<String, dynamic>? b2Curvedata =
      ObjectIdentifiers.getIdentifierByIdentifier(b2Data);
  dynamic curveName;
  if (b2Curvedata != null) {
    curveName = b2Curvedata['readableName'];
  }
  // get the octet string data for the private key der data
  ASN1OctetString octetString =
      topLevelSeq.elements!.elementAt(2) as ASN1OctetString;
  asn1Parser = ASN1Parser(octetString.valueBytes);
  ASN1Sequence octetStringSeq = asn1Parser.nextObject() as ASN1Sequence;
  ASN1OctetString octetStringKeyData =
      octetStringSeq.elements!.elementAt(1) as ASN1OctetString;
  // now generate the key
  Uint8List privateKeyDer = octetStringKeyData.valueBytes!;
  return ECPrivateKey(
      decodeBigInt(privateKeyDer), ECDomainParameters(curveName));
}

// don't worry - it's a sample key
String loadEcPublicKeyPem() {
  return ('''-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEMZHnt1D1tddLNGlEHXEtEw5K/G5Q
HkgaLi+IV84oiV+THv/DqGxYDX2F5JOkfyv36iYSf5lfIC7q9el4YLnlwA==
-----END PUBLIC KEY-----''');
}

// don't worry - it's a sample key
String loadEcPrivateKeyPem() {
  return ('''-----BEGIN PRIVATE KEY-----
MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgciPt/gulzw7/Xe12
YOu/vLUgIUZ+7gGo5VkmU0B+gUWhRANCAAQxkee3UPW110s0aUQdcS0TDkr8blAe
SBouL4hXziiJX5Me/8OobFgNfYXkk6R/K/fqJhJ/mV8gLur16XhgueXA
-----END PRIVATE KEY-----''');
}

String base64Encoding(Uint8List input) {
  return base64.encode(input);
}

Uint8List base64Decoding(String input) {
  return base64.decode(input);
}
