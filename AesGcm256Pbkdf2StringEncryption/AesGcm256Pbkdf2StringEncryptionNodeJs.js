var forge = require('node-forge');

console.log('AES GCM 256 String encryption with PBKDF2 derived key');

var plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('plaintext: ', plaintext);
var password = "secret password";

console.log('\n* * * Encryption * * *');

var ciphertextBase64 = aesGcmPbkdf2EncryptToBase64(password, plaintext);
console.log('ciphertext (Base64): ' + ciphertextBase64);
console.log('output is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag');

console.log('\n* * * Decryption * * *');
var ciphertextDecryptionBase64 = ciphertextBase64;
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag');
var decryptedtext = aesGcmPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64);
console.log('plaintext: ', decryptedtext);

function aesGcmPbkdf2EncryptToBase64(password, data) {
  var PBKDF2_ITERATIONS = 15000;
  var salt = generateSalt32Byte();
  var key = forge.pkcs5.pbkdf2(password, salt, PBKDF2_ITERATIONS, 32, 'sha256');
  var nonce = generateRandomNonce();
  var cipher = forge.cipher.createCipher('AES-GCM', key);
  cipher.start({iv: nonce});
  cipher.update(forge.util.createBuffer(forge.util.encodeUtf8(data)));
  cipher.finish();
  var encrypted = cipher.output;
  var saltBase64 = base64Encoding(salt);
  var nonceBase64 = base64Encoding(nonce);
  var encryptedBase64 = base64Encoding(encrypted.getBytes());
  var gcmTagBase64 = base64Encoding(cipher.mode.tag.data);
  return saltBase64 + ':' + nonceBase64 + ':' + encryptedBase64 + ':' + gcmTagBase64;
}

function aesGcmPbkdf2DecryptFromBase64(password, data) {
  var PBKDF2_ITERATIONS = 15000;
  var dataSplit = data.split(":");
  var salt = base64Decoding(dataSplit[0]);
  var key = forge.pkcs5.pbkdf2(password, salt, PBKDF2_ITERATIONS, 32, 'sha256');
  var nonce = base64Decoding(dataSplit[1]);
  var ciphertext = base64Decoding(dataSplit[2]);
  var gcmTag = base64Decoding(dataSplit[3]);
  var decCipher = forge.cipher.createDecipher('AES-GCM', key);
  decCipher.start({iv: nonce,
    tagLength: 128, // optional, defaults to 128 bits});
    tag: gcmTag // authentication tag from encryption});
  });
  decCipher.update(forge.util.createBuffer(ciphertext));
  decCipher.finish();
  var aesDec = decCipher.output;
  return forge.util.decodeUtf8(aesDec);
}

function generateSalt32Byte() {
  return forge.random.getBytesSync(32);
}

function generateRandomNonce() {
  return forge.random.getBytesSync(12);
}

function base64Encoding(input) {
  return forge.util.encode64(input);
}

function base64Decoding(input) {
  return forge.util.decode64(input);
}
