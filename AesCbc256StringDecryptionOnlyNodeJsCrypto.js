var crypto = require('crypto');

console.log('AES CBC 256 String Decryption only');

// ### place your data here:
var decryptionKeyBase64 = 'd3+KZq3lsdy5Hgd1VRVQFUhA9jU0PaBHTG2LG2PazQM=';
var ciphertextDecryptionBase64 = '8GhkycnbzDo5WbRol5zfCQ==:gQrTOJdobbI4fuCHCfu5eqKFYCaw/AM6xMK2o+xCXB0MeAQ7rFbXAbAE3Ex+bD8c';

console.log('\n* * * Decryption * * *');
console.log('decryptionKey (Base64): ', decryptionKeyBase64);
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) iv : (Base64) ciphertext');
var decryptionKey = base64Decoding(decryptionKeyBase64);
var decryptedtext = aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64);
console.log('plaintext: ', decryptedtext);

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

function base64Decoding(input) {
  return Buffer.from(input, 'base64')
}
