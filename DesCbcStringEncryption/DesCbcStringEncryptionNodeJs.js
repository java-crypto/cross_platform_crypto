var forge = require('node-forge');

console.log('\n# # # SECURITY WARNING: This code is provided for achieve    # # #');
console.log('# # # compatibility between different programming languages. # # #');
console.log('# # # It is not necessarily fully secure.                    # # #');
console.log('# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #');
console.log('# # # It uses FIXED key and IV\'s that should NEVER used,     # # #');
console.log('# # # instead use random generated keys and IVs.             # # #');
console.log('# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #');

var plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('plaintext: ', plaintext);

console.log('\nDES CBC String encryption with fixed key & iv');

// fixed encryption key 8 bytes long
var desEncryptionKey = forge.util.encodeUtf8('12345678');
console.log('desEncryptionKey (Base64): ' + base64Encoding(desEncryptionKey));

console.log('\n* * * Encryption * * *');
var desCiphertextBase64 = desCbcEncryptToBase64(desEncryptionKey, plaintext);
console.log('ciphertext (Base64): ' + desCiphertextBase64);
console.log('output is (Base64) iv : (Base64) ciphertext');
console.log('\n* * * Decryption * * *');
console.log('ciphertext (Base64): ', desCiphertextBase64);
console.log('input is (Base64) iv : (Base64) ciphertext');
var desDecryptedtext = desCbcDecryptFromBase64(desEncryptionKey, desCiphertextBase64);
console.log('plaintext: ', desDecryptedtext);

console.log('\nTriple DES CBC String encryption with fixed key (16 bytes) & iv');
var des2EncryptionKey = forge.util.encodeUtf8('1234567890123456');
console.log('des2EncryptionKey (Base64): ' + base64Encoding(des2EncryptionKey));

console.log('\n* * * Encryption * * *');
var des2CiphertextBase64 = des2CbcEncryptToBase64(des2EncryptionKey, plaintext);
console.log('ciphertext (Base64): ' + des2CiphertextBase64);
console.log('output is (Base64) iv : (Base64) ciphertext');
console.log('\n* * * Decryption * * *');
console.log('ciphertext (Base64): ', des2CiphertextBase64);
console.log('input is (Base64) iv : (Base64) ciphertext');
var des2Decryptedtext = des2CbcDecryptFromBase64(des2EncryptionKey, des2CiphertextBase64);
console.log('plaintext: ', des2Decryptedtext);

console.log('\nTriple DES CBC String encryption with fixed key (24 bytes) & iv');
var des3EncryptionKey = forge.util.encodeUtf8('123456789012345678901234');
console.log('des3EncryptionKey (Base64): ' + base64Encoding(des3EncryptionKey));

console.log('\n* * * Encryption * * *');
var des3CiphertextBase64 = des3CbcEncryptToBase64(des3EncryptionKey, plaintext);
console.log('ciphertext (Base64): ' + des3CiphertextBase64);
console.log('output is (Base64) iv : (Base64) ciphertext');
console.log('\n* * * Decryption * * *');
console.log('ciphertext (Base64): ', des3CiphertextBase64);
console.log('input is (Base64) iv : (Base64) ciphertext');
var des3Decryptedtext = des3CbcDecryptFromBase64(des3EncryptionKey, des3CiphertextBase64);
console.log('plaintext: ', des3Decryptedtext);

function desCbcEncryptToBase64(key, data) {
  // don't use this in production
  var iv = generateFixedDesInitvector();
  //var iv = generateRandomInitvector();
  var cipher = forge.cipher.createCipher('DES-CBC', key);
  cipher.start({iv: iv});
  cipher.update(forge.util.createBuffer(forge.util.encodeUtf8(data)));
  cipher.finish();
  var encrypted = cipher.output;
  var ivBase64 = base64Encoding(iv);
  var encryptedBase64 = base64Encoding(encrypted.getBytes());
  return ivBase64 + ':' + encryptedBase64;
}

function desCbcDecryptFromBase64(key, data) {
  var dataSplit = data.split(":");
  var iv = base64Decoding(dataSplit[0]);
  var ciphertext = base64Decoding(dataSplit[1]);
  var decCipher = forge.cipher.createDecipher('DES-CBC', key);
  decCipher.start({iv: iv});
  decCipher.update(forge.util.createBuffer(ciphertext));
  decCipher.finish();
  var aesDec = decCipher.output;
  return forge.util.decodeUtf8(aesDec);
} 

function des2CbcEncryptToBase64(key, data) {
  var tdes2Key = forge.util.createBuffer('1234567890123456', 'utf8');
  var tdes3Key = tdes2ToTdes3Key(tdes2Key);
  // don't use this in production
  var iv = generateFixedDesInitvector();
  //var iv = generateRandomInitvector();
  var cipher = forge.cipher.createCipher('3DES-CBC', tdes3Key);
  cipher.start({iv: iv});
  cipher.update(forge.util.createBuffer(forge.util.encodeUtf8(data)));
  cipher.finish();
  var encrypted = cipher.output;
  var ivBase64 = base64Encoding(iv);
  var encryptedBase64 = base64Encoding(encrypted.getBytes());
  return ivBase64 + ':' + encryptedBase64;
}

function des2CbcDecryptFromBase64(tdes2Key, data) {
  var tdes3Key = tdes2ToTdes3Key(tdes2Key);
  var dataSplit = data.split(":");
  var iv = base64Decoding(dataSplit[0]);
  var ciphertext = base64Decoding(dataSplit[1]);
  var decCipher = forge.cipher.createDecipher('3DES-CBC', tdes3Key);
  decCipher.start({iv: iv});
  decCipher.update(forge.util.createBuffer(ciphertext));
  decCipher.finish();
  var aesDec = decCipher.output;
  return forge.util.decodeUtf8(aesDec);
} 

function des3CbcEncryptToBase64(key, data) {
  // don't use this in production
  var iv = generateFixedDesInitvector();
  //var iv = generateRandomInitvector();
  var cipher = forge.cipher.createCipher('3DES-CBC', key);
  cipher.start({iv: iv});
  cipher.update(forge.util.createBuffer(forge.util.encodeUtf8(data)));
  cipher.finish();
  var encrypted = cipher.output;
  var ivBase64 = base64Encoding(iv);
  var encryptedBase64 = base64Encoding(encrypted.getBytes());
  return ivBase64 + ':' + encryptedBase64;
}

function des3CbcDecryptFromBase64(key, data) {
  var dataSplit = data.split(":");
  var iv = base64Decoding(dataSplit[0]);
  var ciphertext = base64Decoding(dataSplit[1]);
  var decCipher = forge.cipher.createDecipher('3DES-CBC', key);
  decCipher.start({iv: iv});
  decCipher.update(forge.util.createBuffer(ciphertext));
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

function generateRandomDesInitvector() {
  return forge.random.getBytesSync(8);
}

// don't use this in production
function generateFixedDesInitvector() {
  return forge.util.encodeUtf8('12345678');
}

function base64Encoding(input) {
  return forge.util.encode64(input);
}

function base64Decoding(input) {
  return forge.util.decode64(input);
}
