var forge = require('node-forge');

console.log('AES CBC 256 String encryption with PBKDF2 derived key');

var plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('plaintext: ', plaintext);
var password = "secret password";

console.log('\n* * * Encryption * * *');

var ciphertextBase64 = aesCbcPbkdf2EncryptToBase64(password, plaintext);
console.log('ciphertext (Base64): ' + ciphertextBase64);
console.log('output is (Base64) salt : (Base64) iv : (Base64) ciphertext');

console.log('\n* * * Decryption * * *');
var ciphertextDecryptionBase64 = ciphertextBase64;
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) salt : (Base64) iv : (Base64) ciphertext');
var decryptedtext = aesCbcPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64);
console.log('plaintext: ', decryptedtext);

function aesCbcPbkdf2EncryptToBase64(password, data) {
  var PBKDF2_ITERATIONS = 15000;
  var salt = generateSalt32Byte();
  var key = forge.pkcs5.pbkdf2(password, salt, PBKDF2_ITERATIONS, 32, 'sha256');
  var iv = generateRandomInitvector();
  var cipher = forge.cipher.createCipher('AES-CBC', key);
  cipher.start({iv: iv});
  cipher.update(forge.util.createBuffer(forge.util.encodeUtf8(data)));
  cipher.finish();
  var encrypted = cipher.output;
  var saltBase64 = base64Encoding(salt);
  var ivBase64 = base64Encoding(iv);
  var encryptedBase64 = base64Encoding(encrypted.getBytes());
  return saltBase64 + ':' + ivBase64 + ':' + encryptedBase64;
}

function aesCbcPbkdf2DecryptFromBase64(password, data) {
  var PBKDF2_ITERATIONS = 15000;
  var dataSplit = data.split(":");
  var salt = base64Decoding(dataSplit[0]);
  var key = forge.pkcs5.pbkdf2(password, salt, PBKDF2_ITERATIONS, 32, 'sha256');
  var iv = base64Decoding(dataSplit[1]);
  var ciphertext = base64Decoding(dataSplit[2]);
  var decCipher = forge.cipher.createDecipher('AES-CBC', key);
  decCipher.start({iv: iv});
  decCipher.update(forge.util.createBuffer(ciphertext));
  decCipher.finish();
  var aesDec = decCipher.output;
  return forge.util.decodeUtf8(aesDec);
}

function generateSalt32Byte() {
  return forge.random.getBytesSync(32);
}

function generateRandomInitvector() {
  return forge.random.getBytesSync(16);
}

function base64Encoding(input) {
  return forge.util.encode64(input);
}

function base64Decoding(input) {
  return forge.util.decode64(input);
}
