console.log("Generate a 16 byte long Initialization vector (IV)");
var crypto = require('crypto');
console.log('');
var iv = generateRandomInitvector();
var ivBase64 = base64Encoding(iv);
console.log('generated iv length: ', iv.length, ' data: ', ivBase64);

function generateRandomInitvector() {
  return crypto.randomBytes(16);
}

function base64Encoding(input) {
  return input.toString('base64');
}
