console.log("Generate a 16 byte long Initialization vector (IV)");
var CryptoJS = require("crypto-js");
var iv = generateRandomInitvector();
var ivBase64 = base64Encoding(iv);
console.log('generated iv length: ',
  iv.sigBytes, ' data: ', ivBase64);

function generateRandomInitvector() {
  return CryptoJS.lib.WordArray.random(128/8);
}

function base64Encoding(input) {
  return CryptoJS.enc.Base64.stringify(input);
}
