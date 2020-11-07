// https://repl.it/@javacrypto/CpcNodeJsAesCbc256StringEncryptionFull
var forge = require('node-forge');

console.log('AES CBC 256 String encryption with random key full');

var plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('plaintext: ', plaintext);

// generate random key
var encryptionKey = generateRandomAesKey();
var encryptionKeyBase64 = base64Encoding(encryptionKey);
console.log('encryptionKey (Base64): ', encryptionKeyBase64);

console.log('\n* * * Encryption * * *');

var ciphertextBase64 = aesCbcEncryptToBase64(encryptionKey, plaintext);
console.log('ciphertext (Base64): ' + ciphertextBase64);
console.log('output is (Base64) iv : (Base64) ciphertext');

console.log('\n* * * Decryption * * *');
var decryptionKeyBase64 = encryptionKeyBase64;
var ciphertextDecryptionBase64 = ciphertextBase64;
console.log('decryptionKey (Base64): ', decryptionKeyBase64);
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) iv : (Base64) ciphertext');
var decryptedtext = aesCbcDecryptFromBase64(encryptionKey, ciphertextBase64);
console.log('plaintext: ', decryptedtext);

function aesCbcEncryptToBase64(key, data) {
  var iv = generateRandomInitvector();
  var cipher = forge.cipher.createCipher('AES-CBC', key);
  cipher.start({iv: iv});
  cipher.update(forge.util.createBuffer(forge.util.encodeUtf8(data)));
  cipher.finish();
  var encrypted = cipher.output;
  var ivBase64 = base64Encoding(iv);
  var encryptedBase64 = base64Encoding(encrypted.getBytes());
  return ivBase64 + ':' + encryptedBase64;
}

function aesCbcDecryptFromBase64(key, data) {
  var dataSplit = data.split(":");
  var iv = base64Decoding(dataSplit[0]);
  var ciphertext = base64Decoding(dataSplit[1]);
  var decCipher = forge.cipher.createDecipher('AES-CBC', key);
  decCipher.start({iv: iv});
  decCipher.update(forge.util.createBuffer(ciphertext));
  decCipher.finish();
  var aesDec = decCipher.output;
  return forge.util.decodeUtf8(aesDec);
}

function generateRandomAesKey() {
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