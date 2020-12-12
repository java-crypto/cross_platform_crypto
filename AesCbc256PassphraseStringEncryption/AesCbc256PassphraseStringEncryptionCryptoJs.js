var CryptoJS = require("crypto-js");
console.log('AES CBC 256 String encryption with passphrase');

console.log('\n# # # SECURITY WARNING: This code is provided for achieve    # # #');
console.log('# # # compatibility between different programming languages. # # #');
console.log('# # # It is not necessarily fully secure.                    # # #');
console.log('# # # Its security depends on the complexity and length      # # #');
console.log('# # # of the password, because of only one iteration and     # # #');
console.log('# # # the use of MD5.                                        # # #');
console.log('# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #');

const plaintext = 'The quick brown fox jumps over the lazy dog';
var passphrase = 'my secret passphrase';
console.log('\npassphrase: ' + passphrase);

console.log('\n* * * Encryption * * *');
let ciphertextBase64 = aesCbcPassphraseEncryptToBase64(passphrase, plaintext);
console.log('ciphertext (Base64): ' + ciphertextBase64);
console.log('output is (Base64) ciphertext');

console.log('\n* * * Decryption * * *');
var decryptionPassphrase = passphrase;
var ciphertextDecryptionBase64 = ciphertextBase64;
console.log('decryptionPassphrase: ', decryptionPassphrase);
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) ciphertext');
var decryptedtext = aesCbcPassphraseDecryptFromBase64(decryptionPassphrase, ciphertextDecryptionBase64);
console.log('plaintext: ', decryptedtext);

function aesCbcPassphraseEncryptToBase64(key, data) {
  const cipher = CryptoJS.AES.encrypt(data, key,
  {
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.CBC
  })
  return cipher.toString();
}

function aesCbcPassphraseDecryptFromBase64(key, data) {
  const cipherDecryption = CryptoJS.AES.decrypt(data, key,
  {
    padding: CryptoJS.pad.Pkcs7,
    mode: CryptoJS.mode.CBC
  });
  return CryptoJS.enc.Utf8.stringify(cipherDecryption).toString();
}

function base64Encoding(input) {
  return CryptoJS.enc.Base64.stringify(input);
}

function base64Decoding(input) {
  return CryptoJS.enc.Base64.parse(input);
}
