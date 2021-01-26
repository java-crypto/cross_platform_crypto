var tweetnacl = require('tweetnacl');
// https://www.npmjs.com/package/tweetnacl
// https://github.com/dchest/tweetnacl-js
tweetnacl.util = require('tweetnacl-util');
// https://github.com/dchest/tweetnacl-util-js#documentation

console.log('Generate ED25519 private and public and derive public key from private key');

var keyPair = generateEd25519KeyPair();
var privateKey = keyPair.secretKey;
var publicKey = keyPair.publicKey;
console.log('privateKey (Base64): ' + base64Encoding(privateKey));
console.log('publicKey (Base64):  ' + base64Encoding(publicKey));

console.log('\nderive the publicKey from the privateKey');
var publicKeyDerived = deriveEd25519PublicKey(privateKey);
console.log('publicKey (Base64):  ' + base64Encoding(publicKeyDerived));

function generateEd25519KeyPair() {
  return tweetnacl.sign.keyPair();
}

function deriveEd25519PublicKey(privateKey) {
  return tweetnacl.sign.keyPair.fromSecretKey(privateKey).publicKey;
}

function base64Encoding(input) {
  return tweetnacl.util.encodeBase64(input);
}
