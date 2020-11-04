var CryptoJS = require("crypto-js");
console.log('AES CBC 256 String encryption with random key full1');
const plaintext = 'The quick brown fox jumps over the lazy dog';

var encryptionKey = generateRandomAesKey();
var encryptionKeyBase64 = base64Encoding(encryptionKey);
console.log('encryptionKey (Base64): ', encryptionKeyBase64);

console.log('\n* * * Encryption * * *');
let ciphertextBase64 = aesCbcEncryptToBase64(encryptionKey, plaintext);
console.log('ciphertext (Base64): ' + ciphertextBase64);
console.log('output is (Base64) iv : (Base64) ciphertext');

console.log('\n* * * Decryption * * *');
var decryptionKeyBase64 = encryptionKeyBase64;
//decryptionKeyBase64 = 'HmtCVrK5CLin1tEIETTCwgN9OS1a6fboOzBqj0RM9+w=';
var decryptionKey = base64Decoding(decryptionKeyBase64);
var ciphertextDecryptionBase64 = ciphertextBase64;
//var ciphertextPur = base64Decoding('c+pRNidCIPtv2GmPFMc0T+Gu+BnCIYvG6GPD9l0vB8cicUUZLFGyQgwMFjvVecUk');
console.log('decryptionKey (Base64): ', decryptionKeyBase64);
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) iv : (Base64) ciphertext');
var decryptedtext = aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64);
console.log('plaintext: ', decryptedtext);

function aesCbcEncryptToBase64(key, data) {
  var iv = generateRandomInitvector();
  const cipher = CryptoJS.AES.encrypt(data, key,
  {
    iv: iv,
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.CBC
  })
  return base64Encoding(iv) + ':' + cipher.toString();
}

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

function generateRandomAesKey() {
  return CryptoJS.lib.WordArray.random(256/8);
}

function generateRandomInitvector() {
  return CryptoJS.lib.WordArray.random(128/8);
}

function base64Encoding(input) {
  return CryptoJS.enc.Base64.stringify(input);
}

function base64Decoding(input) {
  return CryptoJS.enc.Base64.parse(input);
}
