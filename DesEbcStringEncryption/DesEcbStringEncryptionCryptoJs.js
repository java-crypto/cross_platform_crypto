var CryptoJS = require("crypto-js");

console.log('\n# # # SECURITY WARNING: This code is provided for achieve    # # #');
console.log('# # # compatibility between different programming languages. # # #');
console.log('# # # It is not necessarily fully secure.                    # # #');
console.log('# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #');
console.log('# # # It uses FIXED key that should NEVER used,              # # #');
console.log('# # # instead use random generated keys.                     # # #');
console.log('# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #');

const plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('\nplaintext: ' + plaintext);

console.log('\nDES ECB String encryption with fixed key');

// fixed encryption key 8 bytes long
var desEncryptionKey = CryptoJS.enc.Utf8.parse("12345678");
var desEncryptionKeyBase64 = base64Encoding(desEncryptionKey);
console.log('desEncryptionKey (Base64): ', desEncryptionKeyBase64);

console.log('\n* * * Encryption * * *');
let desCiphertextBase64 = desEcbEncryptToBase64(desEncryptionKey, plaintext);
console.log('ciphertext (Base64): ' + desCiphertextBase64);
console.log('output is (Base64) ciphertext');
console.log('\n* * * Decryption * * *');
console.log('ciphertext (Base64): ', desCiphertextBase64);
console.log('input is (Base64) ciphertext');
var desDecryptedtext = desEcbDecryptFromBase64(desEncryptionKey, desCiphertextBase64);
console.log('plaintext: ', desDecryptedtext);

console.log('\nTriple DES ECB String encryption with fixed key (16 bytes)');
var des2EncryptionKey = CryptoJS.enc.Utf8.parse("1234567890123456");
console.log('des2EncryptionKey (Base64): ', base64Encoding(des2EncryptionKey));

console.log('\n* * * Encryption * * *');
let des2CiphertextBase64 = des3EcbEncryptToBase64(des2EncryptionKey, plaintext);
console.log('ciphertext (Base64): ' + des2CiphertextBase64);
console.log('output is (Base64) ciphertext');
console.log('\n* * * Decryption * * *');
console.log('ciphertext (Base64): ', des2CiphertextBase64);
console.log('input is (Base64) ciphertext');
var des2Decryptedtext = des3EcbDecryptFromBase64(des2EncryptionKey, des2CiphertextBase64);
console.log('plaintext: ', des2Decryptedtext);

console.log('\nTriple DES ECB String encryption with fixed key (24 bytes)');
var des3EncryptionKey = CryptoJS.enc.Utf8.parse("123456789012345678901234");
console.log('des3EncryptionKey (Base64): ', base64Encoding(des3EncryptionKey));

console.log('\n* * * Encryption * * *');
let des3CiphertextBase64 = des3EcbEncryptToBase64(des3EncryptionKey, plaintext);
console.log('ciphertext (Base64): ' + des3CiphertextBase64);
console.log('output is (Base64) ciphertext');
console.log('\n* * * Decryption * * *');
console.log('ciphertext (Base64): ', des3CiphertextBase64);
console.log('input is (Base64) ciphertext');
var des3Decryptedtext = des3EcbDecryptFromBase64(des3EncryptionKey, des3CiphertextBase64);
console.log('plaintext: ', des3Decryptedtext);

function desEcbEncryptToBase64(key, data) {
  const cipher = CryptoJS.DES.encrypt(data, key,
  {
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.ECB
  })
  return cipher.toString();
}

function desEcbDecryptFromBase64(key, data) {
  const cipherDecryption = CryptoJS.DES.decrypt(data, key,
  {
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.ECB
  });
  return CryptoJS.enc.Utf8.stringify(cipherDecryption).toString();
}

function des3EcbEncryptToBase64(key, data) {
  const cipher = CryptoJS.TripleDES.encrypt(data, key,
  {
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.ECB
  })
  return cipher.toString();
}

function des3EcbDecryptFromBase64(key, data) {
  const cipherDecryption = CryptoJS.TripleDES.decrypt(data, key,
  {
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.ECB
  });
  return CryptoJS.enc.Utf8.stringify(cipherDecryption).toString();
}

function generateRandomDesKey() {
  return CryptoJS.lib.WordArray.random(64/8); // 8 bytes for des, 16 bytes for TDES 2 and 24 bytes for TDES 3
}

function base64Encoding(input) {
  return CryptoJS.enc.Base64.stringify(input);
}

function base64Decoding(input) {
  return CryptoJS.enc.Base64.parse(input);
}
