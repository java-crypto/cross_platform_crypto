var crypto = require('crypto');
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
var aPrivateKey = base64Decoding(aPrivateKeyBase64);
var bPublicKey = base64Decoding(bPublicKeyBase64);

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
var aPublicKey = base64Decoding(aPublicKeyBase64);
var bPrivateKey = base64Decoding(bPrivateKeyBase64);
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
  const cipher = crypto.createCipheriv('aes-256-cbc', key, iv);
  let encryptedBase64 = '';
  cipher.setEncoding('base64');
  cipher.on('data', (chunk) => encryptedBase64 += chunk);
  cipher.on('end', () => {
  // do nothing console.log(encryptedBase64);
  // Prints: some clear text data
  });
  cipher.write(data);
  cipher.end();
  var ivBase64 = base64Encoding(iv);
  return ivBase64 + ':' + encryptedBase64;
}

function aesCbcDecryptFromBase64(key, data) {
  var dataSplit = data.split(":");
  var iv = base64Decoding(dataSplit[0]);
  var ciphertext = dataSplit[1];
  const decipher = crypto.createDecipheriv('aes-256-cbc', key, iv);
  let decrypted = '';
  decipher.on('readable', () => {
    while (null !== (chunk = decipher.read())) {
      decrypted += chunk.toString('utf8');
    }
  });
  decipher.on('end', () => {
  // do nothing console.log(decrypted);
  });
  decipher.write(ciphertext, 'base64');
  decipher.end();
  return decrypted;
}

function generateRandomInitvector() {
  return crypto.randomBytes(16);
}

function ToBase64 (u8) {
    return Buffer.from(u8, 'binary').toString('base64')
}

function FromBase64 (str) {
    return Buffer.from(str, 'base64').toString('binary');
}

function base64Encoding(input) {
  return input.toString('base64');
}

function base64Decoding(input) {
  return Buffer.from(input, 'base64')
}
