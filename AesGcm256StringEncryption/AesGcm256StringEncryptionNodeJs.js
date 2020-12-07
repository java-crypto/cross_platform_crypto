var forge = require('node-forge');

console.log('AES GCM 256 String encryption with random key full');

var plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('plaintext: ', plaintext);

// generate random key
var encryptionKey = generateRandomAesKey();
var encryptionKeyBase64 = base64Encoding(encryptionKey);
console.log('encryptionKey (Base64): ', encryptionKeyBase64);

console.log('\n* * * Encryption * * *');

var ciphertextBase64 = aesGcmEncryptToBase64(encryptionKey, plaintext);
console.log('ciphertext (Base64): ' + ciphertextBase64);
console.log('output is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag');

console.log('\n* * * Decryption * * *');
var decryptionKeyBase64 = encryptionKeyBase64;
var ciphertextDecryptionBase64 = ciphertextBase64;
console.log('decryptionKey (Base64): ', decryptionKeyBase64);
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag');
var decryptedtext = aesGcmDecryptFromBase64(encryptionKey, ciphertextBase64);
console.log('plaintext: ', decryptedtext);

function aesGcmEncryptToBase64(key, data) {
  var nonce = generateRandomNonce();
  var cipher = forge.cipher.createCipher('AES-GCM', key);
  cipher.start({iv: nonce,
    tagLength: 128 // optional, defaults to 128 bits});
  });  
  cipher.update(forge.util.createBuffer(forge.util.encodeUtf8(data)));
  cipher.finish();
  var encrypted = cipher.output;
  var nonceBase64 = base64Encoding(nonce);
  var gcmTagBase64 = base64Encoding(cipher.mode.tag.data);
  var encryptedBase64 = base64Encoding(encrypted.getBytes());
  return nonceBase64 + ':' + encryptedBase64 + ':' + gcmTagBase64;
}

function aesGcmDecryptFromBase64(key, data) {
  var dataSplit = data.split(":");
  var nonce = base64Decoding(dataSplit[0]);
  var ciphertext = base64Decoding(dataSplit[1]);
  var gcmTag = base64Decoding(dataSplit[2]);
  var decCipher = forge.cipher.createDecipher('AES-GCM', key);
  decCipher.start({iv: nonce,
    tagLength: 128, // optional, defaults to 128 bits});
    tag: gcmTag // authentication tag from encryption
});  
  decCipher.update(forge.util.createBuffer(ciphertext));
  decCipher.finish();
  var aesDec = decCipher.output;
  return forge.util.decodeUtf8(aesDec);
}  

function generateRandomAesKey() {
  return forge.random.getBytesSync(32);
}

function generateRandomNonce() {
  return forge.random.getBytesSync(12);
}

function base64Encoding(input) {
  return forge.util.encode64(input);
}

function base64Decoding(input) {
  return forge.util.decode64(input);
}
