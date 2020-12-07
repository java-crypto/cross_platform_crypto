var crypto = require('crypto');

console.log('AES CBC 256 String encryption with PBKDF2 derived key');

var plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('plaintext: ', plaintext);
var password = "secret password";

console.log('\n* * * Encryption * * *');

var ciphertextBase64 = aesCbcPbkdf2EncryptToBase64(password, plaintext);
console.log('ciphertext (Base64): ' + ciphertextBase64);
console.log('output is (Base64) salt : (Base64) iv : (Base64) ciphertext');

console.log('\n* * * Decryption * * *');
var ciphertextDecryptionBase64 = ciphertextBase64;
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) salt : (Base64) iv : (Base64) ciphertext');
var decryptedtext = aesCbcPbkdf2DecryptFromBase64(password, ciphertextBase64);
console.log('plaintext: ', decryptedtext);

function aesCbcPbkdf2EncryptToBase64(password, data) {
  var PBKDF2_ITERATIONS = 15000;
  var salt = generateSalt32Byte();
  var key = crypto.pbkdf2Sync(password, salt, PBKDF2_ITERATIONS, 32, 'sha256');
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
  var saltBase64 = base64Encoding(salt);
  var ivBase64 = base64Encoding(iv);
  return saltBase64 + ':' + ivBase64 + ':' + encryptedBase64;
}

function aesCbcPbkdf2DecryptFromBase64(password, data) {
  var PBKDF2_ITERATIONS = 15000;
  var dataSplit = data.split(":");
  var salt = base64Decoding(dataSplit[0]);
  var key = crypto.pbkdf2Sync(password, salt, PBKDF2_ITERATIONS, 32, 'sha256');
  var iv = base64Decoding(dataSplit[1]);
  var ciphertext = dataSplit[2];
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

function generateSalt32Byte() {
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
