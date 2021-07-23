var CryptoJS = require("crypto-js");

console.log('\n# # # SECURITY WARNING: This code is provided for achieve    # # #');
console.log('# # # compatibility between different programming languages. # # #');
console.log('# # # It is not necessarily fully secure.                    # # #');
console.log('# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #');
console.log('# # # It uses FIXED key and IV\'s that should NEVER used,     # # #');
console.log('# # # instead use random generated keys and IVs.             # # #');
console.log('# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #');

const plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('\nplaintext: ' + plaintext);

console.log('\nDES CBC String encryption with fixed key & iv');

// fixed encryption key 8 bytes long
var desEncryptionKey = CryptoJS.enc.Utf8.parse("12345678");
var desEncryptionKeyBase64 = base64Encoding(desEncryptionKey);
console.log('desEncryptionKey (Base64): ', desEncryptionKeyBase64);

console.log('\n* * * Encryption * * *');
let desCiphertextBase64 = desCbcEncryptToBase64(desEncryptionKey, plaintext);
console.log('ciphertext (Base64): ' + desCiphertextBase64);
console.log('output is (Base64) iv : (Base64) ciphertext');
console.log('\n* * * Decryption * * *');
console.log('ciphertext (Base64): ', desCiphertextBase64);
console.log('input is (Base64) iv : (Base64) ciphertext');
var desDecryptedtext = desCbcDecryptFromBase64(desEncryptionKey, desCiphertextBase64);
console.log('plaintext: ', desDecryptedtext);

console.log('\nTriple DES CBC String encryption with fixed key (16 bytes) & iv');
var des2EncryptionKey = CryptoJS.enc.Utf8.parse("1234567890123456");
console.log('des2EncryptionKey (Base64): ', base64Encoding(des2EncryptionKey));

console.log('\n* * * Encryption * * *');
let des2CiphertextBase64 = des3CbcEncryptToBase64(des2EncryptionKey, plaintext);
console.log('ciphertext (Base64): ' + des2CiphertextBase64);
console.log('output is (Base64) iv : (Base64) ciphertext');
console.log('\n* * * Decryption * * *');
console.log('ciphertext (Base64): ', des2CiphertextBase64);
console.log('input is (Base64) iv : (Base64) ciphertext');
var des2Decryptedtext = des3CbcDecryptFromBase64(des2EncryptionKey, des2CiphertextBase64);
console.log('plaintext: ', des2Decryptedtext);

console.log('\nTriple DES CBC String encryption with fixed key (24 bytes) & iv');
var des3EncryptionKey = CryptoJS.enc.Utf8.parse("123456789012345678901234");
console.log('des3EncryptionKey (Base64): ', base64Encoding(des3EncryptionKey));

console.log('\n* * * Encryption * * *');
let des3CiphertextBase64 = des3CbcEncryptToBase64(des3EncryptionKey, plaintext);
console.log('ciphertext (Base64): ' + des3CiphertextBase64);
console.log('output is (Base64) iv : (Base64) ciphertext');
console.log('\n* * * Decryption * * *');
console.log('ciphertext (Base64): ', des3CiphertextBase64);
console.log('input is (Base64) iv : (Base64) ciphertext');
var des3Decryptedtext = des3CbcDecryptFromBase64(des3EncryptionKey, des3CiphertextBase64);
console.log('plaintext: ', des3Decryptedtext);

function desCbcEncryptToBase64(key, data) {
  // don't use this in production
  var iv = generateFixedDesInitvector();
  //var iv = generateRandomDesInitvector();
  const cipher = CryptoJS.DES.encrypt(data, key,
  {
    iv: iv,
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.CBC
  })
  return base64Encoding(iv) + ':' + cipher.toString();
}

function desCbcDecryptFromBase64(key, data) {
  var dataSplit = data.split(":");
  var iv = base64Decoding(dataSplit[0]);
  var ciphertextDecryption = dataSplit[1];
  const cipherDecryption = CryptoJS.DES.decrypt(ciphertextDecryption, key,
  {
    iv: iv,
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.CBC
  });
  return CryptoJS.enc.Utf8.stringify(cipherDecryption).toString();
}

function des3CbcEncryptToBase64(key, data) {
  // don't use this in production
  var iv = generateFixedDesInitvector();
  //var iv = generateRandomDesInitvector();
  const cipher = CryptoJS.TripleDES.encrypt(data, key,
  {
    iv: iv,
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.CBC
  })
  return base64Encoding(iv) + ':' + cipher.toString();
}

function des3CbcDecryptFromBase64(key, data) {
  var dataSplit = data.split(":");
  var iv = base64Decoding(dataSplit[0]);
  var ciphertextDecryption = dataSplit[1];
  const cipherDecryption = CryptoJS.TripleDES.decrypt(ciphertextDecryption, key,
  {
    iv: iv,
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.CBC
  });
  return CryptoJS.enc.Utf8.stringify(cipherDecryption).toString();
}

function generateRandomDesKey() {
  return CryptoJS.lib.WordArray.random(64/8);
}

function generateRandomDesInitvector() {
  return CryptoJS.lib.WordArray.random(64/8);
}

function generateFixedDesInitvector() {
  return CryptoJS.enc.Utf8.parse("12345678");
}

function base64Encoding(input) {
  return CryptoJS.enc.Base64.stringify(input);
}

function base64Decoding(input) {
  return CryptoJS.enc.Base64.parse(input);
}
