var crypto = require('crypto');

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

function base64Decoding(input) {
  return Buffer.from(input, 'base64')
}
