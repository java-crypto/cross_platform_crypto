var forge = require('node-forge');

console.log('AES CBC 256 String decryption with PBKDF2 derived key only');

var password = "secret password";

console.log('\n* * * Decryption * * *');
var ciphertextDecryptionBase64 = "ETpVa06FG6pKzDRtc4OFHMWk5SGLx0Bg1E3+fAmtsuA=:rsvmFmJZBT+smIE5lFKDhA==:BWpupA3D/ciXB/tN+w0SguYYfLCINhP3ofkNTeWQiADPSaB+AJAy4hU8ITazMWff";
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) salt : (Base64) iv : (Base64) ciphertext');
var decryptedtext = aesCbcPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64);
console.log('plaintext: ', decryptedtext);

function aesCbcPbkdf2DecryptFromBase64(password, data) {
  var PBKDF2_ITERATIONS = 15000;
  var dataSplit = data.split(":");
  var salt = base64Decoding(dataSplit[0]);
  var key = forge.pkcs5.pbkdf2(password, salt, PBKDF2_ITERATIONS, 32, 'sha256');
  var iv = base64Decoding(dataSplit[1]);
  var ciphertext = base64Decoding(dataSplit[2]);
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
