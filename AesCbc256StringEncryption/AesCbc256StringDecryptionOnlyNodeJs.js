var forge = require('node-forge');

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
  var ciphertext = base64Decoding(dataSplit[1]);
  var decCipher = forge.cipher.createDecipher('AES-CBC', key);
  decCipher.start({iv: iv});
  decCipher.update(forge.util.createBuffer(ciphertext));
  decCipher.finish();
  var aesDec = decCipher.output;
  return forge.util.decodeUtf8(aesDec);
}

function base64Decoding(input) {
  return forge.util.decode64(input);
}
