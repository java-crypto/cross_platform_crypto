var tweetnacl = require('tweetnacl');
// https://www.npmjs.com/package/tweetnacl
// https://github.com/dchest/tweetnacl-js
tweetnacl.util = require('tweetnacl-util');
// https://github.com/dchest/tweetnacl-util-js#documentation

console.log('Libsodium crypto box hybrid string encryption');

var plaintext = 'The quick brown fox jumps over the lazy dog';
console.log ('plaintext: ' + plaintext);

// encryption
console.log ('\n* * * encryption * * *');
// for encryption you need your private key (privateKeyA) and the public key from other party (publicKeyB)
var privateKeyABase64 = 'yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU=';
var publicKeyBBase64 =  'jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=';
// convert the base64 encoded keys
var privateKeyA = base64Decoding(privateKeyABase64);
var publicKeyB = base64Decoding(publicKeyBBase64);
var ciphertextBase64 = secretboxEncryptionToBase64(privateKeyA, publicKeyB, plaintext);
console.log('all data are in Base64 encoding');
console.log('privateKeyA: ' + privateKeyABase64);
console.log('publicKeyB:  ' + publicKeyBBase64);
console.log('ciphertext:  ' + ciphertextBase64);
console.log('output is (Base64) nonce : (Base64) ciphertext');

// decryption
console.log ('\n* * * decryption * * *');
var ciphertextReceivedBase64 = ciphertextBase64;
// for decryption you need your private key (privateKeyB) and the public key from other party (publicKeyA)
var privateKeyBBase64 = 'yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=';
var publicKeyABase64 =  'b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0=';
// convert the base64 encoded keys
var privateKeyB = base64Decoding(privateKeyBBase64);
var publicKeyA = base64Decoding(publicKeyABase64);
console.log('privateKeyB: ' + privateKeyBBase64);
console.log('publicKeyA:  ' + publicKeyABase64);
console.log('ciphertext:  ' + ciphertextReceivedBase64);
console.log('input is (Base64) nonce : (Base64) ciphertext');
var decryptedtext = secretboxDecryptionToBase64(privateKeyA, publicKeyB, ciphertextReceivedBase64);
console.log('decrypt.text:' + decryptedtext);

function secretboxEncryptionToBase64(secretKey, publicKey, data) {
  var nonce = generateRandomNonce();
  var plaintext = tweetnacl.util.decodeUTF8(data);
  var ciphertext = tweetnacl.box(plaintext, nonce, publicKey, secretKey);
	var ciphertextBase64 = base64Encoding(ciphertext);
  return base64Encoding(nonce)  + ':' + ciphertextBase64;
}

function secretboxDecryptionToBase64(secretKey, publicKey, dataBase64) {
  var dataSplit = dataBase64.split(":");
	var nonce = base64Decoding(dataSplit[0]);
	var ciphertext = base64Decoding(dataSplit[1]);
  var decryptedtext = tweetnacl.box.open(ciphertext, nonce, publicKey, secretKey);
	return tweetnacl.util.encodeUTF8(decryptedtext);
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
