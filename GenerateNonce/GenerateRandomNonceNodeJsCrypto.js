console.log("Generate a 12 byte long nonce for AES GCM");
var crypto = require('crypto');
console.log('');
var nonce = generateRandomNonce();
var nonceBase64 = base64Encoding(nonce);
console.log('generated nonce length: ', nonce.length, ' data: ', nonceBase64);

function generateRandomNonce() {
  return crypto.randomBytes(12);
}

function base64Encoding(input) {
  return input.toString('base64');
}
