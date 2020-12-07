var CryptoJS = require("crypto-js");
console.log('AES CBC 256 String encryption with PBKDF2 derived key and HMAC check');
const plaintext = 'The quick brown fox jumps over the lazy dog';
var password = "secret password";

console.log('\n* * * Encryption * * *');
let ciphertextBase64 = aesCbcPbkdf2HmacEncryptToBase64(password, plaintext);
console.log('ciphertext (Base64): ' + ciphertextBase64);
console.log('output is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac');

console.log('\n* * * Decryption * * *');
var ciphertextDecryptionBase64 = ciphertextBase64;

console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac');
var decryptedtext = aesCbcPbkdf2HmacDecryptFromBase64(password, ciphertextDecryptionBase64);
console.log('plaintext: ', decryptedtext);

function aesCbcPbkdf2HmacEncryptToBase64(key, data) {
  var PBKDF2_ITERATIONS = 15000;
  var salt = generateSalt32Byte();
  var keyAesHmac = CryptoJS.PBKDF2(password, salt, {
    hasher:CryptoJS.algo.SHA256,
    keySize: 512 / 32, 
    iterations: PBKDF2_ITERATIONS
  });
  const keyAesHmacString = keyAesHmac.toString();
  const keyAesString = keyAesHmacString.slice(0,keyAesHmacString.length/2);
  const keyHmacString = keyAesHmacString.slice(keyAesHmacString.length/2,keyAesHmacString.length);
  var keyAes = CryptoJS.enc.Hex.parse(keyAesString);
  var iv = generateRandomInitvector();
  console.log('iv: ' + iv);
  const cipher = CryptoJS.AES.encrypt(data, keyAes,
  {
    iv: iv,
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.CBC
  })
  // calculate hmac over salt, iv and ciphertext
  const ciphertext =  base64Encoding(salt) + ':' + base64Encoding(iv) + ':' + cipher.toString();
  var keyHmac = CryptoJS.enc.Hex.parse(keyHmacString);
  var hmacBase64 = base64Encoding(CryptoJS.HmacSHA256(ciphertext, keyHmac));
  return ciphertext + ':' + hmacBase64;
}

function aesCbcPbkdf2HmacDecryptFromBase64(key, data) {
  var PBKDF2_ITERATIONS = 15000;
  var dataSplit = data.split(":");
  var salt = base64Decoding(dataSplit[0]);
  var hmac = base64Decoding(dataSplit[3]);
  var keyAesHmac = CryptoJS.PBKDF2(password, salt, {
    hasher:CryptoJS.algo.SHA256,
    keySize: 512 / 32, 
    iterations: PBKDF2_ITERATIONS
  });
  const keyAesHmacString = keyAesHmac.toString();
  const keyAesString = keyAesHmacString.slice(0,keyAesHmacString.length/2);
  const keyHmacString = keyAesHmacString.slice(keyAesHmacString.length/2,keyAesHmacString.length);
  // before we decrypt we have to check the hmac
  var keyHmac = CryptoJS.enc.Hex.parse(keyHmacString);
  var hmacToCheck = dataSplit[0] + ':' + dataSplit[1] + ':' + dataSplit[2];
  var hmacCalculated = CryptoJS.HmacSHA256(hmacToCheck, keyHmac);
  if (hmac.toString() != hmacCalculated.toString()) {
    console.log("Error: HMAC-check failed, no decryption possible");
    return "";
  }
  var keyAes = CryptoJS.enc.Hex.parse(keyAesString);
  var iv = base64Decoding(dataSplit[1]);
  var ciphertextDecryption = dataSplit[2];
  const cipherDecryption = CryptoJS.AES.decrypt(ciphertextDecryption, keyAes,
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
