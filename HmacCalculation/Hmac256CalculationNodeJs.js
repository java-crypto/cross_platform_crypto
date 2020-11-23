var forge = require('node-forge');

console.log('HMAC 256 calculation');

const hmac256Key = 'hmac256ForAesEncryption';
const plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('hmac256Key: ', hmac256Key);
console.log('plaintext:  ', plaintext);

// calculate hmac
var hmac256 = hmac256Calculation(hmac256Key, plaintext);
console.log('hmac256 length: ' + base64Decoding(hmac256).length + ' (Base64) data: ' + hmac256);

function hmac256Calculation(keyHmac, data) {
  var hmac = forge.hmac.create();
  hmac.start('sha256', keyHmac);
  hmac.update(data);
  var hmacBytes = forge.util.hexToBytes(hmac.digest().toHex());
  return base64Encoding(hmacBytes);
} 

function base64Encoding(input) {
  return forge.util.encode64(input);
}

function base64Decoding(input) {
  return forge.util.decode64(input);
}
