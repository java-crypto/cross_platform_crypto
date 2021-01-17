var tweetnacl = require('tweetnacl');
// https://www.npmjs.com/package/tweetnacl
// https://github.com/dchest/tweetnacl-js
tweetnacl.util = require('tweetnacl-util');
// https://github.com/dchest/tweetnacl-util-js#documentation
tweetnacl.sealedbox = require('tweetnacl-sealedbox-js');
// https://www.jsdelivr.com/package/npm/tweetnacl-sealedbox-js

console.log('Libsodium sealed crypto box hybrid string encryption');

var plaintext = 'The quick brown fox jumps over the lazy dog';
console.log ('plaintext: ' + plaintext);

// encryption
console.log ('\n* * * encryption * * *');
// for encryption you need the public key from the one you are sending the data to (party B)
var publicKeyBBase64 =  'jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=';
// convert the base64 encoded keys
var publicKeyB = base64Decoding(publicKeyBBase64);
var ciphertextBase64 = sealedCryptoboxEncryptionToBase64(publicKeyB, plaintext);
console.log('all data are in Base64 encoding');
console.log('publicKeyB:  ' + publicKeyBBase64);
console.log('ciphertext:  ' + ciphertextBase64);
console.log('output is (Base64) ciphertext');

// decryption
console.log ('\n* * * decryption * * *');
var ciphertextReceivedBase64 = ciphertextBase64;
// for decryption you need your private key (privateKeyB)
var privateKeyBBase64 = 'yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=';
// convert the base64 encoded keys
var privateKeyB = base64Decoding(privateKeyBBase64);
console.log('privateKeyB: ' + privateKeyBBase64);
console.log('ciphertext:  ' + ciphertextReceivedBase64);
console.log('input is (Base64) ciphertext');
var decryptedtext = sealedCryptoboxDecryptionToBase64(privateKeyB, ciphertextReceivedBase64);
console.log('decrypt.text:' + decryptedtext);

function sealedCryptoboxEncryptionToBase64(publicKey, data) {
  var plaintext = tweetnacl.util.decodeUTF8(data);
  var ciphertext = tweetnacl.sealedbox.seal(plaintext, publicKey);
	return base64Encoding(ciphertext);
}

function sealedCryptoboxDecryptionToBase64(secretKey, ciphertextBase64) {
	var ciphertext = base64Decoding(ciphertextBase64);
    // get the public key from secret key via generating the key pair
	var keypair = tweetnacl.box.keyPair.fromSecretKey(secretKey);
	var publicKey = keypair.publicKey;
  var decryptedtext = tweetnacl.sealedbox.open(ciphertext, publicKey, secretKey);
	return tweetnacl.util.encodeUTF8(decryptedtext);
}

function base64Encoding(input) {
  return tweetnacl.util.encodeBase64(input);
}

function base64Decoding(input) {
  return tweetnacl.util.decodeBase64(input);
}
