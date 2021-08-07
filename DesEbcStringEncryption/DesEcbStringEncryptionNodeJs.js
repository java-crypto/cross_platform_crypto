var forge = require('node-forge');

console.log('\n# # # SECURITY WARNING: This code is provided for achieve    # # #');
console.log('# # # compatibility between different programming languages. # # #');
console.log('# # # It is not necessarily fully secure.                    # # #');
console.log('# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #');
console.log('# # # It uses FIXED keythat should NEVER used,               # # #');
console.log('# # # instead use random generated keys.                     # # #');
console.log('# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #');

var plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('plaintext: ', plaintext);

console.log('\nDES EBC String encryption with fixed key');

// fixed encryption key 8 bytes long
var desEncryptionKey = forge.util.encodeUtf8('12345678');
console.log('desEncryptionKey (Base64): ' + base64Encoding(desEncryptionKey));

console.log('\n* * * Encryption * * *');
var desCiphertextBase64 = desEcbEncryptToBase64(desEncryptionKey, plaintext);
console.log('ciphertext (Base64): ' + desCiphertextBase64);
console.log('output is (Base64) ciphertext');
console.log('\n* * * Decryption * * *');
console.log('ciphertext (Base64): ', desCiphertextBase64);
console.log('input is (Base64) ciphertext');
var desDecryptedtext = desEcbDecryptFromBase64(desEncryptionKey, desCiphertextBase64);
console.log('plaintext: ', desDecryptedtext);

console.log('\nTriple DES ECB String encryption with fixed key (16 bytes)');
var des2EncryptionKey = forge.util.encodeUtf8('1234567890123456');
console.log('des2EncryptionKey (Base64): ' + base64Encoding(des2EncryptionKey));

console.log('\n* * * Encryption * * *');
var des2CiphertextBase64 = des2EcbEncryptToBase64(des2EncryptionKey, plaintext);
console.log('ciphertext (Base64): ' + des2CiphertextBase64);
console.log('output is (Base64) ciphertext');
console.log('\n* * * Decryption * * *');
console.log('ciphertext (Base64): ', des2CiphertextBase64);
console.log('input is (Base64) ciphertext');
var des2Decryptedtext = des2EcbDecryptFromBase64(des2EncryptionKey, des2CiphertextBase64);
console.log('plaintext: ', des2Decryptedtext);

console.log('\nTriple DES ECB String encryption with fixed key (24 bytes)');
var des3EncryptionKey = forge.util.encodeUtf8('123456789012345678901234');
console.log('des3EncryptionKey (Base64): ' + base64Encoding(des3EncryptionKey));

console.log('\n* * * Encryption * * *');
var des3CiphertextBase64 = des3EcbEncryptToBase64(des3EncryptionKey, plaintext);
console.log('ciphertext (Base64): ' + des3CiphertextBase64);
console.log('output is (Base64) ciphertext');
console.log('\n* * * Decryption * * *');
console.log('ciphertext (Base64): ', des3CiphertextBase64);
console.log('input is (Base64) ciphertext');
var des3Decryptedtext = des3EcbDecryptFromBase64(des3EncryptionKey, des3CiphertextBase64);
console.log('plaintext: ', des3Decryptedtext);

function desEcbEncryptToBase64(key, data) {
  var cipher = forge.cipher.createCipher('DES-ECB', key);
  cipher.start({iv: ''});
  cipher.update(forge.util.createBuffer(forge.util.encodeUtf8(data)));
  cipher.finish();
  var encrypted = cipher.output;
  var encryptedBase64 = base64Encoding(encrypted.getBytes());
  return encryptedBase64;
}

function desEcbDecryptFromBase64(key, data) {
  var decCipher = forge.cipher.createDecipher('DES-ECB', key);
  decCipher.start({iv: ''});
  decCipher.update(forge.util.createBuffer(base64Decoding(data)));
  decCipher.finish();
  var aesDec = decCipher.output;
  return forge.util.decodeUtf8(aesDec);
} 

function des2EcbEncryptToBase64(key, data) {
  var tdes3Key = tdes2ToTdes3Key(key);
  var cipher = forge.cipher.createCipher('3DES-ECB', tdes3Key);
  cipher.start({iv: ''});
  cipher.update(forge.util.createBuffer(forge.util.encodeUtf8(data)));
  cipher.finish();
  var encrypted = cipher.output;
  var encryptedBase64 = base64Encoding(encrypted.getBytes());
  return encryptedBase64;
}

function des2EcbDecryptFromBase64(tdes2Key, data) {
  var tdes3Key = tdes2ToTdes3Key(tdes2Key);
  var decCipher = forge.cipher.createDecipher('3DES-ECB', tdes3Key);
  decCipher.start({iv: ''});
  decCipher.update(forge.util.createBuffer(base64Decoding(data)));
  decCipher.finish();
  var aesDec = decCipher.output;
  return forge.util.decodeUtf8(aesDec);
} 

function des3EcbEncryptToBase64(key, data) {
  var cipher = forge.cipher.createCipher('3DES-ECB', key);
  cipher.start({iv: ''});
  cipher.update(forge.util.createBuffer(forge.util.encodeUtf8(data)));
  cipher.finish();
  var encrypted = cipher.output;
  var encryptedBase64 = base64Encoding(encrypted.getBytes());
  return encryptedBase64;
}

function des3EcbDecryptFromBase64(key, data) {
  var decCipher = forge.cipher.createDecipher('3DES-ECB', key);
  decCipher.start({iv: ''});
  decCipher.update(forge.util.createBuffer(base64Decoding(data)));
  decCipher.finish();
  var aesDec = decCipher.output;
  return forge.util.decodeUtf8(aesDec);
} 

function tdes2ToTdes3Key(tdes2Key) {
    var forgeBuffer = forge.util.createBuffer(tdes2Key, 'raw');
    var nodeBuffer = Buffer.from(forgeBuffer.getBytes(), 'binary');
    var nodeBufferExpanded = Buffer.concat([nodeBuffer, nodeBuffer.slice(0, 8)], 24); // properly expand 3DES key from 128 bit to 192 bit
    return forge.util.createBuffer(nodeBufferExpanded.toString('binary'));
  }

function generateRandomDesKey() {
  return forge.random.getBytesSync(8); // 8 bytes for des, 16 bytes for TDES 2 and 24 bytes for TDES 3
}

function base64Encoding(input) {
  return forge.util.encode64(input);
}

function base64Decoding(input) {
  return forge.util.decode64(input);
}
