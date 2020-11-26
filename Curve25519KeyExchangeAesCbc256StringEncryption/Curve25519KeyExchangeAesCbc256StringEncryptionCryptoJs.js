var CryptoJS = require("crypto-js");
var axlsign = require('./axlsign.js'); //
var tou8 = require('buffer-to-uint8array'); // 

console.log('Curve25519 key exchange and AES CBC 256 string encryption');
// you need the library axlsign.js: 
// https://github.com/wavesplatform/curve25519-js
// you need the library buffer-to-uint8array
// https://github.com/substack/buffer-to-uint8array

var plaintext = 'The quick brown fox jumps over the lazy dog';

// for encryption you need your private key (aPrivateKey) and the public key from other party (bPublicKey)
var aPrivateKeyBase64 = 'yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU=';
var bPublicKeyBase64 =  'jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=';

// convert the base64 encoded keys
var aPrivateKey = convertWordArrayToUint8Array(base64Decoding(aPrivateKeyBase64));
var bPublicKey = convertWordArrayToUint8Array(base64Decoding(bPublicKeyBase64));

// generate shared key
var aSharedKey = curve25519GenerateKeyAgreement(aPrivateKey, bPublicKey);
// encrypt
var ciphertextBase64 = aesCbcEncryptToBase64(aSharedKey, plaintext);
console.log('\n* * * Encryption * * *');
console.log('all data are in Base64 encoding');
console.log('aPrivateKey: ' + aPrivateKeyBase64);
console.log('bPublicKey:  ' + bPublicKeyBase64);
console.log('aSharedKey:  ' + ToBase64(aSharedKey));
console.log('ciphertext:  ' + ciphertextBase64);
console.log('output is    (Base64) iv : (Base64) ciphertext');

// for decryption you need your private key (bPrivateKey) and the public key from other party (aPublicKey)
// received ciphertext
var ciphertextReceivedBase64 = ciphertextBase64;
// received public key
var aPublicKeyBase64 = "b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0=";
// own private key
var bPrivateKeyBase64 = "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=";
// convert the base64 encoded keys
var aPublicKey = convertWordArrayToUint8Array(base64Decoding(aPublicKeyBase64));
var bPrivateKey = convertWordArrayToUint8Array(base64Decoding(bPrivateKeyBase64));
// generate shared key
var bSharedKey = curve25519GenerateKeyAgreement(bPrivateKey, aPublicKey);
// decrypt
var decryptedtext = aesCbcDecryptFromBase64(bSharedKey, ciphertextReceivedBase64);
console.log('\n* * * decryption * * *');
console.log('ciphertext:  ' + ciphertextReceivedBase64);
console.log('input is     (Base64) iv : (Base64) ciphertext');
console.log('bPrivateKey: ' + bPrivateKeyBase64);
console.log('aPublicKey:  ' + aPublicKeyBase64);
console.log('bSharedKey:  ' + ToBase64(bSharedKey));
console.log('decrypt.text:' + decryptedtext);

function curve25519GenerateKeyAgreement(privateKey, publicKey) {
  return axlsign.sharedKey(privateKey, publicKey);
}

function aesCbcEncryptToBase64(key, data) {
  var iv = generateRandomInitvector();
  const cipher = CryptoJS.AES.encrypt(data, key,
  {
    iv: iv,
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.CBC
  })
  return base64Encoding(iv) + ':' + cipher.toString();
}

function aesCbcDecryptFromBase64(key, data) {
  var dataSplit = data.split(":");
  var iv = base64Decoding(dataSplit[0]);
  var ciphertextDecryption = dataSplit[1];
  const cipherDecryption = CryptoJS.AES.decrypt(ciphertextDecryption, key,
  {
    iv: iv,
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.CBC
  });
  return CryptoJS.enc.Utf8.stringify(cipherDecryption).toString();
}

function generateRandomInitvector() {
  return CryptoJS.lib.WordArray.random(128/8);
}

function base64Encoding(input) {
  return CryptoJS.enc.Base64.stringify(input);
}

function base64Decoding(input) {
  return CryptoJS.enc.Base64.parse(input);
}

function ToBase64 (u8) {
    return Buffer.from(u8, 'binary').toString('base64')
}

function FromBase64 (str) {
    return Buffer.from(str, 'base64').toString('binary');
}

// https://gist.github.com/getify/7325764
function convertWordArrayToUint8Array(wordArray) {
	var len = wordArray.words.length,
		u8_array = new Uint8Array(len << 2),
		offset = 0, word, i
	;
	for (i=0; i<len; i++) {
		word = wordArray.words[i];
		u8_array[offset++] = word >> 24;
		u8_array[offset++] = (word >> 16) & 0xff;
		u8_array[offset++] = (word >> 8) & 0xff;
		u8_array[offset++] = word & 0xff;
	}
	return u8_array;
}

//https://gist.github.com/getify/7325764
// create a wordArray that is Big-Endian (because it's used with CryptoJS which is all BE)
// From: https://gist.github.com/creationix/07856504cf4d5cede5f9#file-encode-js
function convertUint8ArrayToWordArray(u8Array) {
	var words = [], i = 0, len = u8Array.length;

	while (i < len) {
		words.push(
			(u8Array[i++] << 24) |
			(u8Array[i++] << 16) |
			(u8Array[i++] << 8)  |
			(u8Array[i++])
		);
	}

	return {
		sigBytes: words.length * 4,
		words: words
	};
}
