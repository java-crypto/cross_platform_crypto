var crypto = require('crypto');

console.log('HMAC 256 calculation');

const hmac256Key = 'hmac256ForAesEncryption';
const plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('hmac256Key: ', hmac256Key);
console.log('plaintext:  ', plaintext);

// calculate hmac
var hmac256 = hmac256Calculation(hmac256Key, plaintext);
console.log('hmac256 length: ' + base64Decoding(hmac256).length + ' (Base64) data: ' + hmac256);

function hmac256Calculation(keyHmac, data) {
  return crypto.createHmac('sha256', keyHmac)
      .update(data)
      .digest('base64');
} 

function base64Decoding(input) {
  return Buffer.from(input, 'base64')
}
