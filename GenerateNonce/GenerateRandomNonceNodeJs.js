console.log("Generate a 12 byte long nonce for AES GCM");
var forge = require('node-forge');
var nonce = generateRandomNonce();
var nonceBase64 = base64Encoding(nonce);
console.log('generated nonce length: ',
  nonce.length, ' data: ', nonceBase64);

function generateRandomNonce() {
  return forge.random.getBytesSync(12);
}

function base64Encoding(input) {
  return forge.util.encode64(input);
}
