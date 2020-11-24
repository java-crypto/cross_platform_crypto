var crypto = require('crypto');
var axlsign = require('./axlsign.js'); //

console.log('Generate a key pair for Curve25519');
// you need the library axlsign.js: 
// https://github.com/wavesplatform/curve25519-js
// you need the library buffer-to-uint8array
// https://github.com/substack/buffer-to-uint8array

var curve25519Keypair = generateCurve25519KeyPair();
var privateKey = curve25519Keypair.private;
var publicKey = curve25519Keypair.public;
console.log('base64 encoded key data:');
console.log('Curve25519 PrivateKey: ' + Uint8ToBase64(privateKey));
console.log('Curve25519 PublicKey:  ' + Uint8ToBase64(publicKey));

function generateCurve25519KeyPair() {
  var seed = crypto.randomBytes(32);
  return axlsign.generateKeyPair(seed);
}

function Uint8ToBase64 (u8) {
    return Buffer.from(u8, 'binary').toString('base64')
}