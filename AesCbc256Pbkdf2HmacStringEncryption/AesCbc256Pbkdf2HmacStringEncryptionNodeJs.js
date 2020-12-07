var forge = require('node-forge');

console.log('AES CBC 256 String encryption with PBKDF2 derived key and HMAC check');

var plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('plaintext: ', plaintext);
var password = "secret password";

console.log('\n* * * Encryption * * *');

var ciphertextBase64 = aesCbcPbkdf2HmacEncryptToBase64(password, plaintext);
console.log('ciphertext (Base64): ' + ciphertextBase64);
console.log('output is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac');

console.log('\n* * * Decryption * * *');
var ciphertextDecryptionBase64 = ciphertextBase64;
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac');
var decryptedtext = aesCbcPbkdf2HmacDecryptFromBase64(password, ciphertextDecryptionBase64);
console.log('plaintext: ', decryptedtext);

function aesCbcPbkdf2HmacEncryptToBase64(password, data) {
  var PBKDF2_ITERATIONS = 15000;
  var salt = generateSalt32Byte();
  var keyAesHmac = forge.pkcs5.pbkdf2(password, salt, PBKDF2_ITERATIONS, 64, 'sha256');
  var keyAes = keyAesHmac.slice(0,keyAesHmac.length/2);
  var keyHmac = keyAesHmac.slice(keyAesHmac.length/2,keyAesHmac.length);
  var iv = generateRandomInitvector();
  var cipher = forge.cipher.createCipher('AES-CBC', keyAes);
  cipher.start({iv: iv});
  cipher.update(forge.util.createBuffer(forge.util.encodeUtf8(data)));
  cipher.finish();
  var encrypted = cipher.output;
  var saltBase64 = base64Encoding(salt);
  var ivBase64 = base64Encoding(iv);
  var encryptedBase64 = base64Encoding(encrypted.getBytes());
  var ciphertext = saltBase64 + ':' + ivBase64 + ':' + encryptedBase64;
  // calculate hmac over salt, iv and ciphertext
  var hmac = forge.hmac.create();
  hmac.start('sha256', keyHmac);
  hmac.update(ciphertext);
  var hmacBytes = forge.util.hexToBytes(hmac.digest().toHex());
  var hmacBase64 = base64Encoding(hmacBytes);
  return ciphertext + ':' + hmacBase64;
}

function aesCbcPbkdf2HmacDecryptFromBase64(password, data) {
  var PBKDF2_ITERATIONS = 15000;
  var dataSplit = data.split(":");
  var salt = base64Decoding(dataSplit[0]);
  var hmac = base64Decoding(dataSplit[3]);
  var keyAesHmac = forge.pkcs5.pbkdf2(password, salt, PBKDF2_ITERATIONS, 64, 'sha256');
  var keyAes = keyAesHmac.slice(0,keyAesHmac.length/2);
  var keyHmac = keyAesHmac.slice(keyAesHmac.length/2,keyAesHmac.length);
  // before we decrypt we have to check the hmac
  var hmacToCheck = dataSplit[0] + ':' + dataSplit[1] + ':' + dataSplit[2];
  var hmacDec = forge.hmac.create();
  hmacDec.start('sha256', keyHmac);
  hmacDec.update(hmacToCheck);
  var hmacCalculated = forge.util.hexToBytes(hmacDec.digest().toHex());
  if (hmac.toString('hex') != hmacCalculated.toString()) {
    console.log("Error: HMAC-check failed, no decryption possible");
    return "";
  }
  var iv = base64Decoding(dataSplit[1]);
  var ciphertext = base64Decoding(dataSplit[2]);
  var decCipher = forge.cipher.createDecipher('AES-CBC', keyAes);
  decCipher.start({iv: iv});
  decCipher.update(forge.util.createBuffer(ciphertext));
  decCipher.finish();
  var aesDec = decCipher.output;
  return forge.util.decodeUtf8(aesDec);
}

function generateSalt32Byte() {
  return forge.random.getBytesSync(32);
}

function generateRandomInitvector() {
  return forge.random.getBytesSync(16);
}

function base64Encoding(input) {
  return forge.util.encode64(input);
}

function base64Decoding(input) {
  return forge.util.decode64(input);
}
