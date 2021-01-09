console.log("Generate a 12 byte long nonce for AES GCM");
var CryptoJS = require("crypto-js");
var nonce = generateRandomNonce();
var nonceBase64 = base64Encoding(nonce);
console.log('generated nonce length: ',
  nonce.sigBytes, ' data: ', nonceBase64);

function generateRandomNonce() {
  return CryptoJS.lib.WordArray.random(96/8);
}

function base64Encoding(input) {
  return CryptoJS.enc.Base64.stringify(input);
}
