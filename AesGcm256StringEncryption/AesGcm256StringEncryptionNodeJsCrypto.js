var crypto = require('crypto');

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
  const cipher = crypto.createCipheriv('aes-256-gcm', key, nonce);
  let encryptedBase64 = '';
  cipher.setEncoding('base64');
  cipher.on('data', (chunk) => encryptedBase64 += chunk);
  cipher.on('end', () => {
  // do nothing console.log(encryptedBase64);
  // Prints: some clear text data
  });
  cipher.write(data);
  cipher.end();
  var nonceBase64 = base64Encoding(nonce);
  var gcmTagBase64 = base64Encoding(cipher.getAuthTag());
  return nonceBase64 + ':' + encryptedBase64 + ':' + gcmTagBase64;
}

function aesGcmDecryptFromBase64(key, data) {
  var dataSplit = data.split(":");
  var nonce = base64Decoding(dataSplit[0]);
  var ciphertext = dataSplit[1];
  var gcmTag = base64Decoding(dataSplit[2]);
  const decipher = crypto.createDecipheriv('aes-256-gcm', key, nonce);
  decipher.setAuthTag(gcmTag);
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

function generateRandomAesKey() {
  return crypto.randomBytes(32);
}

function generateRandomNonce() {
  return crypto.randomBytes(12);
}

function base64Encoding(input) {
  return input.toString('base64');
}

function base64Decoding(input) {
  return Buffer.from(input, 'base64')
}
