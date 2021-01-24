var tweetnacl = require('tweetnacl');
// https://www.npmjs.com/package/tweetnacl
// https://github.com/dchest/tweetnacl-js
tweetnacl.util = require('tweetnacl-util');
// https://github.com/dchest/tweetnacl-util-js#documentation

console.log('Libsodium detached signature string');

var dataToSign = 'The quick brown fox jumps over the lazy dog';
console.log ('plaintext: ' + dataToSign);

// usually we would load the private and public key from a file or keystore
// here we use hardcoded keys for demonstration - don't do this in real programs

console.log ('\n* * * sign the plaintext with the ED25519 private key * * *');
var privateKey = loadEd25519PrivateKey();
var signatureBase64 = libsodiumSignDetachedToBase64(privateKey, dataToSign);
console.log ('signature (Base64): ' + signatureBase64);
console.log ('signature (Base64): ' + 'x41mufah/9VO347W+nPXu5FYeSJOI894YClbbTiX0pwRrvfAPMymxEvyMMsDFMI0R0sulCnuCRSgN0WOKnZBDg==');

console.log ('\n* * * verify the signature against the plaintext with the ED25519 public key * * *');
var publicKey = loadEd25519PublicKey();
var signatureVerified = libsodiumVerifyDetachedFromBase64(publicKey, dataToSign, signatureBase64);
console.log ('signature (Base64): ' + signatureBase64);
console.log ('signature (Base64) verified: ' + signatureVerified);


function libsodiumSignDetachedToBase64(privateKey, data){
  var plaintext = tweetnacl.util.decodeUTF8(data);
  return base64Encoding(tweetnacl.sign.detached(plaintext, privateKey));
}

function libsodiumVerifyDetachedFromBase64(publicKey, data, signatureBase64){
  var plaintext = tweetnacl.util.decodeUTF8(data);
  return tweetnacl.sign.detached.verify(plaintext, base64Decoding(signatureBase64), publicKey)
}

function loadEd25519PrivateKey() {
  // this is a sample key - don't worry !
  return base64Decoding("Gjho7dILimbXJY8RvmQZEBczDzCYbZonGt/e5QQ3Vro2sjTp95EMxOrgEaWiprPduR/KJFBRA+RlDDpkvmhVfw==");
}

function loadEd25519PublicKey() {
  // this is a sample key - don't worry !
  return base64Decoding("NrI06feRDMTq4BGloqaz3bkfyiRQUQPkZQw6ZL5oVX8=");
}

function base64Encoding(input) {
  return tweetnacl.util.encodeBase64(input);
}

function base64Decoding(input) {
  return tweetnacl.util.decodeBase64(input);
}
