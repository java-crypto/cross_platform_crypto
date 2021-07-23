var crypto = require('crypto');

console.log('\n# # # SECURITY WARNING: This code is provided for achieve    # # #');
console.log('# # # compatibility between different programming languages. # # #');
console.log('# # # It is not necessarily fully secure.                    # # #');
console.log('# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #');
console.log('# # # It uses FIXED key and IV\'s that should NEVER used,     # # #');
console.log('# # # instead use random generated keys and IVs.             # # #');
console.log('# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #');

const plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('\nplaintext: ' + plaintext);

console.log('\nDES CBC String encryption with fixed key & iv');

// fixed encryption key 8 bytes long
var desEncryptionKey = Buffer.from('12345678');
console.log('desEncryptionKey (Base64): ', base64Encoding(desEncryptionKey));

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
var des2EncryptionKey = Buffer.from('1234567890123456');
console.log('des2EncryptionKey (Base64): ', base64Encoding(des2EncryptionKey));

console.log('\n* * * Encryption * * *');
var des2CiphertextBase64 = des2CbcEncryptToBase64(des2EncryptionKey, plaintext);
console.log('output is (Base64) iv : (Base64) ciphertext');
console.log('\n* * * Decryption * * *');
console.log('ciphertext (Base64): ', des2CiphertextBase64);
console.log('input is (Base64) iv : (Base64) ciphertext');
var des2Decryptedtext = des2CbcDecryptFromBase64(des2EncryptionKey, des2CiphertextBase64);
console.log('plaintext: ', des2Decryptedtext);

console.log('\nTriple DES CBC String encryption with fixed key (24 bytes) & iv');
var des3EncryptionKey = Buffer.from('123456789012345678901234');
console.log('des3EncryptionKey (Base64): ', base64Encoding(des3EncryptionKey));

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
  const cipher = crypto.createCipheriv('des-cbc', key, iv);
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

function desCbcDecryptFromBase64(key, data) {
  var dataSplit = data.split(":");
  var iv = base64Decoding(dataSplit[0]);
  var ciphertext = dataSplit[1];
  const decipher = crypto.createDecipheriv('des-cbc', key, iv);
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

function des2CbcEncryptToBase64(key, data) {
  // don't use this in production 
  var iv = generateFixedDesInitvector();
  //var iv = generateRandomInitvector();
  const cipher = crypto.createCipheriv('des-ede-cbc', key, iv);
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

function des2CbcDecryptFromBase64(key, data) {
  var dataSplit = data.split(":");
  var iv = base64Decoding(dataSplit[0]);
  var ciphertext = dataSplit[1];
  const decipher = crypto.createDecipheriv('des-ede-cbc', key, iv);
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

function des3CbcEncryptToBase64(key, data) {
  // don't use this in production 
  var iv = generateFixedDesInitvector();
  //var iv = generateRandomInitvector();
  const cipher = crypto.createCipheriv('des-ede3-cbc', key, iv);
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

function des3CbcDecryptFromBase64(key, data) {
  var dataSplit = data.split(":");
  var iv = base64Decoding(dataSplit[0]);
  var ciphertext = dataSplit[1];
  const decipher = crypto.createDecipheriv('des-ede3-cbc', key, iv);
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

function generateRandomDesKey() {
  return crypto.randomBytes(8); // 8 bytes for des, 16 bytes for TDES 2 and 24 bytes for TDES 3
}

function generateRandomDesInitvector() {
  return crypto.randomBytes(8);
}

// don't use this in production
function generateFixedDesInitvector() {
  return Buffer.from('12345678');
}

function base64Encoding(input) {
  return input.toString('base64');
}

function base64Decoding(input) {
  return Buffer.from(input, 'base64')
}
