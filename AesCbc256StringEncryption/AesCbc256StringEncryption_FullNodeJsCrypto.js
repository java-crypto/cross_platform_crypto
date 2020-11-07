var crypto = require('crypto');

console.log('AES CBC 256 String encryption with random key full');

var plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('plaintext: ', plaintext);

// generate random key
var encryptionKey = generateRandomAesKey();
var encryptionKeyBase64 = base64Encoding(encryptionKey);
console.log('encryptionKey (Base64): ', encryptionKeyBase64);

console.log('\n* * * Encryption * * *');

var ciphertextBase64 = aesCbcEncryptToBase64(encryptionKey, plaintext);
console.log('ciphertext (Base64): ' + ciphertextBase64);
console.log('output is (Base64) iv : (Base64) ciphertext');

console.log('\n* * * Decryption * * *');
var decryptionKeyBase64 = encryptionKeyBase64;
var ciphertextDecryptionBase64 = ciphertextBase64;
console.log('decryptionKey (Base64): ', decryptionKeyBase64);
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) iv : (Base64) ciphertext');
var decryptedtext = aesCbcDecryptFromBase64(encryptionKey, ciphertextBase64);
console.log('plaintext: ', decryptedtext);

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

function generateRandomAesKey() {
  return crypto.randomBytes(32);
}

function generateRandomInitvector() {
  return crypto.randomBytes(16);
}

function base64Encoding(input) {
  return input.toString('base64');
}

function base64Decoding(input) {
  return Buffer.from(input, 'base64')
}
