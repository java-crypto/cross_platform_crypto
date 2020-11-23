var CryptoJS = require("crypto-js");
console.log('HMAC 256 calculation');

const hmac256Key = 'hmac256ForAesEncryption';
const plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('hmac256Key: ', hmac256Key);
console.log('plaintext:  ', plaintext);

// calculate hmac
var hmac256 = hmac256Calculation(hmac256Key, plaintext);
console.log('hmac256 length: ' + hmac256.sigBytes + ' (Base64) data: ' + base64Encoding(hmac256));

function hmac256Calculation(keyHmac, data) {
  return CryptoJS.HmacSHA256(data, keyHmac);
}  

function base64Encoding(input) {
  return CryptoJS.enc.Base64.stringify(input);
}
