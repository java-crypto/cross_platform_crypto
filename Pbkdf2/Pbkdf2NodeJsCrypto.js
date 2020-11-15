console.log("Generate a 32 byte long AES key with PBKDF2");
var crypto = require('crypto');

var password = "secret password";
var PBKDF2_ITERATIONS = 15000; // number of iterations, higher is better but slower

// ### security warning - never use a fixed salt in production, this is for compare reasons only
var salt = generateFixedSalt32Byte();
// please use below generateSalt32Byte()
//var salt = generateSalt32Byte();

var aesKeySha512 = generateAes256KeyPbkdf2Sha512(password, PBKDF2_ITERATIONS, salt);
console.log('aesKeySha512 length: ',
  aesKeySha512.length, ' data: ', bytesToHex(aesKeySha512));
var aesKeySha256 = generateAes256KeyPbkdf2Sha256(password, PBKDF2_ITERATIONS, salt);
console.log('aesKeySha256 length: ',
  aesKeySha256.length, ' data: ', bytesToHex(aesKeySha256));
var aesKeySha1 = generateAes256KeyPbkdf2Sha1(password, PBKDF2_ITERATIONS, salt);
console.log('aesKeySha1   length: ',
  aesKeySha1.length, ' data: ', bytesToHex(aesKeySha1));

function generateAes256KeyPbkdf2Sha512(password, iterations, salt) {
  return crypto.pbkdf2Sync(password, salt, iterations, 32, 'sha512');
}

function generateAes256KeyPbkdf2Sha256(password, iterations, salt) {
  return crypto.pbkdf2Sync(password, salt, iterations, 32, 'sha256');
}

function generateAes256KeyPbkdf2Sha1(password, iterations, salt) {
  return crypto.pbkdf2Sync(password, salt, iterations, 32, 'sha1');
}

function generateSalt32Byte() {
  return crypto.randomBytes(32);
}

function generateFixedSalt32Byte() {
  return Buffer.from('0000000000000000000000000000000000000000000000000000000000000000', 'hex');
}

function bytesToHex(input) {
  return input.toString('hex');
}