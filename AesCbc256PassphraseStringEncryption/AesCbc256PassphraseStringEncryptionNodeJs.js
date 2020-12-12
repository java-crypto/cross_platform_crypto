var forge = require('node-forge');

console.log('AES CBC 256 String encryption with passphrase');

console.log('\n# # # SECURITY WARNING: This code is provided for achieve    # # #');
console.log('# # # compatibility between different programming languages. # # #');
console.log('# # # It is not necessarily fully secure.                    # # #');
console.log('# # # Its security depends on the complexity and length      # # #');
console.log('# # # of the password, because of only one iteration and     # # #');
console.log('# # # the use of MD5.                                        # # #');
console.log('# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #');

var plaintext = 'The quick brown fox jumps over the lazy dog';
var passphrase = 'my secret passphrase';
console.log('\npassphrase: ' + passphrase);

console.log('\n* * * Encryption * * *');
var ciphertextBase64 = aesCbcPassphraseEncryptToBase64(passphrase, plaintext);
console.log('ciphertext (Base64): ' + ciphertextBase64);
console.log('output is (Base64) ciphertext');

console.log('\n* * * Decryption * * *');
var ciphertextDecryptionBase64 = ciphertextBase64;
console.log('passphrase: ' + passphrase);
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) ciphertext');
var decryptedtext = aesCbcPassphraseDecryptFromBase64(passphrase, ciphertextBase64);
console.log('plaintext: ', decryptedtext);

function aesCbcPassphraseEncryptToBase64(password, data) {
  var keySize = 32;
  var ivSize = 16;
  var salt = generateRandomSalt8Byte();
  var derivedBytes = forge.pbe.opensslDeriveBytes(
    password, salt, keySize + ivSize);
  var buffer = forge.util.createBuffer(derivedBytes);
  var key = buffer.getBytes(keySize);
  var iv = buffer.getBytes(ivSize);
  var cipher = forge.cipher.createCipher('AES-CBC', key);
  cipher.start({iv: iv});
  cipher.update(forge.util.createBuffer(forge.util.encodeUtf8(data)));
  cipher.finish();
  var output = forge.util.createBuffer();
  // if using a salt, prepend this to the output:
  if(salt !== null) {
    output.putBytes('Salted__'); // (add to match openssl tool output)
    output.putBytes(salt);
  }
  output.putBuffer(cipher.output);
  return base64Encoding(output.getBytes());
}

function aesCbcPassphraseDecryptFromBase64(password, dataBase64) {
  data = forge.util.createBuffer(base64Decoding(dataBase64), 'binary');
  // skip "Salted__" (if known to be present)
  data.getBytes('Salted__'.length);
  // read 8-byte salt
  var salt = data.getBytes(8);
  var keySize = 32;
  var ivSize = 16;
  var derivedBytes = forge.pbe.opensslDeriveBytes(
    password, salt, keySize + ivSize);
  var buffer = forge.util.createBuffer(derivedBytes);
  var key = buffer.getBytes(keySize);
  var iv = buffer.getBytes(ivSize);
  var decCipher = forge.cipher.createDecipher('AES-CBC', key);
  decCipher.start({iv: iv});
  decCipher.update(forge.util.createBuffer(data));
  decCipher.finish();
  var aesDec = decCipher.output;
  return forge.util.decodeUtf8(aesDec);
}

function generateRandomSalt8Byte() {
  return forge.random.getBytesSync(8);
}

function base64Encoding(input) {
  return forge.util.encode64(input);
}

function base64Decoding(input) {
  return forge.util.decode64(input);
}
