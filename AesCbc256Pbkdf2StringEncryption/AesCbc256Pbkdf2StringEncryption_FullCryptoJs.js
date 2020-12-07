var CryptoJS = require("crypto-js");
console.log('AES CBC 256 String encryption with PBKDF2 derived key');
const plaintext = 'The quick brown fox jumps over the lazy dog';
var password = "secret password";

console.log('\n* * * Encryption * * *');
let ciphertextBase64 = aesCbcPbkdf2EncryptToBase64(password, plaintext);
console.log('ciphertext (Base64): ' + ciphertextBase64);
console.log('output is (Base64) salt : (Base64) iv : (Base64) ciphertext');

console.log('\n* * * Decryption * * *');
var ciphertextDecryptionBase64 = ciphertextBase64;

console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) salt : (Base64) iv : (Base64) ciphertext');
var decryptedtext = aesCbcPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64);
console.log('plaintext: ', decryptedtext);

function aesCbcPbkdf2EncryptToBase64(key, data) {
  var PBKDF2_ITERATIONS = 15000;
  var salt = generateSalt32Byte();
  var key = CryptoJS.PBKDF2(password, salt, {
    hasher:CryptoJS.algo.SHA256,
    keySize: 256 / 32,
    iterations: PBKDF2_ITERATIONS
  });
  var iv = generateRandomInitvector();
  const cipher = CryptoJS.AES.encrypt(data, key,
  {
    iv: iv,
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.CBC
  })
  return base64Encoding(salt) + ':' + base64Encoding(iv) + ':' + cipher.toString();
}

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

function generateRandomInitvector() {
  return CryptoJS.lib.WordArray.random(128/8);
}

function base64Encoding(input) {
  return CryptoJS.enc.Base64.stringify(input);
}

function base64Decoding(input) {
  return CryptoJS.enc.Base64.parse(input);
}
