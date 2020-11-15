console.log("Generate a 32 byte long AES key with PBKDF2");
var CryptoJS = require("crypto-js");

var password = "secret password";
var PBKDF2_ITERATIONS = 15000; // number of iterations, higher is better but slower

// ### security warning - never use a fixed salt in production, this is for compare reasons only
var salt = generateFixedSalt32Byte();
// please use below generateSalt32Byte()
// var salt = generateSalt32Byte();

var aesKeySha512 = generateAes256KeyPbkdf2Sha512(password, PBKDF2_ITERATIONS, salt);
console.log('aesKeySha512 length: ',
  aesKeySha512.sigBytes, ' data: ', bytesToHex(aesKeySha512));
var aesKeySha256 = generateAes256KeyPbkdf2Sha256(password, PBKDF2_ITERATIONS, salt);
console.log('aesKeySha256 length: ',
  aesKeySha256.sigBytes, ' data: ', bytesToHex(aesKeySha256));
var aesKeySha1 = generateAes256KeyPbkdf2Sha1(password, PBKDF2_ITERATIONS, salt);
console.log('aesKeySha1   length: ',
  aesKeySha1.sigBytes, ' data: ', bytesToHex(aesKeySha1));

function generateAes256KeyPbkdf2Sha512(password, iterations, salt) {
  return CryptoJS.PBKDF2(password, salt, {
    hasher:CryptoJS.algo.SHA512,
    keySize: 256 / 32,
    iterations: iterations
  });
}

function generateAes256KeyPbkdf2Sha256(password, iterations, salt) {
  return CryptoJS.PBKDF2(password, salt, {
    hasher:CryptoJS.algo.SHA256,
    keySize: 256 / 32,
    iterations: iterations
  });
}

function generateAes256KeyPbkdf2Sha1(password, iterations, salt) {
  return CryptoJS.PBKDF2(password, salt, {
    hasher:CryptoJS.algo.SHA1,
    keySize: 256 / 32,
    iterations: iterations
  });
}

function generateSalt32Byte() {
  return CryptoJS.lib.WordArray.random(256/8);
}

function generateFixedSalt32Byte() {
  // ### security warning - never use this in production ###
  return CryptoJS.enc.Hex.parse("0000000000000000000000000000000000000000000000000000000000000000");
}

function bytesToHex(input) {
  return CryptoJS.enc.Hex.stringify(input);
}
