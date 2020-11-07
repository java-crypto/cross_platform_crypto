var CryptoJS = require("crypto-js");
console.log('AES CBC 256 String Decryption only');

// ### place your data here:
var decryptionKeyBase64 = 'd3+KZq3lsdy5Hgd1VRVQFUhA9jU0PaBHTG2LG2PazQM=';
var ciphertextDecryptionBase64 = "8GhkycnbzDo5WbRol5zfCQ==:gQrTOJdobbI4fuCHCfu5eqKFYCaw/AM6xMK2o+xCXB0MeAQ7rFbXAbAE3Ex+bD8c";

console.log('\n* * * Decryption * * *');
console.log('decryptionKey (Base64): ', decryptionKeyBase64);
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) iv : (Base64) ciphertext');
var decryptionKey = base64Decoding(decryptionKeyBase64);
var decryptedtext = aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64);
console.log('plaintext: ', decryptedtext);

function aesCbcDecryptFromBase64(key, data) {
  var dataSplit = data.split(":");
  var iv = base64Decoding(dataSplit[0]);
  var ciphertextDecryption = dataSplit[1];
  const cipherDecryption = CryptoJS.AES.decrypt(ciphertextDecryption, key,
  {
    iv: iv,
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.CBC
  });
  return CryptoJS.enc.Utf8.stringify(cipherDecryption).toString();
}

function base64Decoding(input) {
  return CryptoJS.enc.Base64.parse(input);
}
