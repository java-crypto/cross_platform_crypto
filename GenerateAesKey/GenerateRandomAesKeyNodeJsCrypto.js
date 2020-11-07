console.log("Generate a 32 byte long AES key");
var crypto = require('crypto');
console.log('');
var aesKey = generateRandomAesKey();
var aesKeyBase64 = base64Encoding(aesKey);
console.log('generated key length: ', aesKey.length, ' data: ', aesKeyBase64);

function generateRandomAesKey() {
  return crypto.randomBytes(32);
}

function base64Encoding(input) {
  return input.toString('base64');
}
