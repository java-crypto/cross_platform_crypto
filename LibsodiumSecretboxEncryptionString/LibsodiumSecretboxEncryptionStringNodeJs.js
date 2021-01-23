var tweetnacl = require('tweetnacl');
// https://www.npmjs.com/package/tweetnacl
// https://github.com/dchest/tweetnacl-js
tweetnacl.util = require('tweetnacl-util');
// https://github.com/dchest/tweetnacl-util-js#documentation

console.log('Libsodium secret box random key string encryption');

var plaintext = 'The quick brown fox jumps over the lazy dog';
console.log ('plaintext: ' + plaintext);

var keyBase64 = base64Encoding(generateRandomKey());
console.log ('key (Base64): ' + keyBase64);

// encryption
console.log ('\n* * * encryption * * *');
var ciphertextBase64 = secretboxEncryptionToBase64(keyBase64, plaintext);
console.log('all data are in Base64 encoding');
console.log('ciphertext:  ' + ciphertextBase64);
console.log('output is (Base64) nonce : (Base64) ciphertext');

// decryption
console.log ('\n* * * decryption * * *');
var ciphertextReceivedBase64 = ciphertextBase64;
console.log('ciphertext:  ' + ciphertextReceivedBase64);
console.log('input is (Base64) nonce : (Base64) ciphertext');
var decryptedtext = secretboxDecryptionFromBase64(keyBase64, ciphertextReceivedBase64);
console.log('decrypt.text:' + decryptedtext);

function secretboxEncryptionToBase64(keyBase64, data) {
  var nonce = generateRandomNonce();
  var plaintext = tweetnacl.util.decodeUTF8(data);
  var ciphertext = tweetnacl.secretbox(plaintext, nonce, base64Decoding(keyBase64));
	var ciphertextBase64 = base64Encoding(ciphertext);
  return base64Encoding(nonce)  + ':' + ciphertextBase64;
}

function secretboxDecryptionFromBase64(keyBase64, dataBase64) {
  var dataSplit = dataBase64.split(":");
	var nonce = base64Decoding(dataSplit[0]);
	var ciphertext = base64Decoding(dataSplit[1]);
  var decryptedtext = tweetnacl.secretbox.open(ciphertext, nonce, base64Decoding(keyBase64));
	return tweetnacl.util.encodeUTF8(decryptedtext);
}

function generateRandomKey() {
  return tweetnacl.randomBytes(tweetnacl.secretbox.keyLength)
}

function generateRandomNonce() {
  return tweetnacl.randomBytes(tweetnacl.secretbox.nonceLength)
}

function base64Encoding(input) {
  return tweetnacl.util.encodeBase64(input);
}

function base64Decoding(input) {
  return tweetnacl.util.decodeBase64(input);
}
