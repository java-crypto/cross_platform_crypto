console.log("Generate a 32 byte long AES key");
var CryptoJS = require("crypto-js");
var aesKey = generateRandomAesKey();
var aesKeyBase64 = base64Encoding(aesKey);
console.log('generated key length: ',
  aesKey.sigBytes, ' data: ', aesKeyBase64);

function generateRandomAesKey() {
  return CryptoJS.lib.WordArray.random(256/8);
}

function base64Encoding(input) {
  return CryptoJS.enc.Base64.stringify(input);
}
