var CryptoJS = require("crypto-js");
console.log('AES CBC 256 String decryption with PBKDF2 derived key only');

var password = "secret password";

console.log('\n* * * Decryption * * *');
var ciphertextDecryptionBase64 = "9W0o9S2u4aN6yFc9vDxz9hfbRGFjqmyxL/DAexXlLaI=:NoBb2Zw6fP0cpyq30bWkYA==:OUZ7v0XUXFD2wnSaI+Kja2RcTRrD0cYbDUyoa4CV3cZvJZd+xxVE/WHQsrzvwe1A";

console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) salt : (Base64) iv : (Base64) ciphertext');
var decryptedtext = aesCbcPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64);
console.log('plaintext: ', decryptedtext);

function aesCbcPbkdf2DecryptFromBase64(key, data) {
  var PBKDF2_ITERATIONS = 15000;
  var dataSplit = data.split(":");
  var salt = base64Decoding(dataSplit[0]);
  var key = CryptoJS.PBKDF2(password, salt, {
    hasher:CryptoJS.algo.SHA256,
    keySize: 256 / 32,
    iterations: PBKDF2_ITERATIONS
  });
  var iv = base64Decoding(dataSplit[1]);
  var ciphertextDecryption = dataSplit[2];
  const cipherDecryption = CryptoJS.AES.decrypt(ciphertextDecryption, key,
  {
    iv: iv,
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.CBC
  });
  return CryptoJS.enc.Utf8.stringify(cipherDecryption).toString();
}

function generateSalt32Byte() {
  return CryptoJS.lib.WordArray.random(256/8);
}

function base64Decoding(input) {
  return CryptoJS.enc.Base64.parse(input);
}
