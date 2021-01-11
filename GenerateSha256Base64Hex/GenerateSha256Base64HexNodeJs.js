var forge = require('node-forge');

console.log('Generate a SHA-256 hash, Base64 en- and decoding and hex conversions');

var plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('plaintext:               ', plaintext);

var sha256Value = calculateSha256(plaintext);
console.log('sha256Value (hex) length: ' + sha256Value.length() + ' data: ' + bytesToHex(sha256Value));

var sha256Base64 = base64Encoding(sha256Value.getBytes());
console.log('sha256Value (base64):     ' + sha256Base64);

var sha256ValueDecoded = base64Decoding(sha256Base64);
console.log('sha256Base64 decoded to a byte array:');
console.log('sha256Value (hex) length: ' + sha256ValueDecoded.length + ' data: ' + bytesToHex(sha256ValueDecoded));

var sha256HexString = 'd7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592';
var sha256Hex = hexStringToByteArray(sha256HexString);
console.log('sha256HexString converted to a byte array:');
console.log('sha256Value (hex) length: ' + sha256Hex.length + ' data: ' + bytesToHex(sha256Hex));

function calculateSha256(input) {
  var md = forge.md.sha256.create();
  md.update(input);
return md.digest();
}

function bytesToHex(input) {
  return forge.util.bytesToHex(input);
}

function hexStringToByteArray(input) {
  return forge.util.hexToBytes(input);
}

function base64Encoding(input) {
  return forge.util.encode64(input);
}

function base64Decoding(input) {
  return forge.util.decode64(input);
}
