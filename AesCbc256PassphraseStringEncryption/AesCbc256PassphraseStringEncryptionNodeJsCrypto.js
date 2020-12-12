var crypto = require('crypto');

console.log('AES CBC 256 String encryption with passphrase');

console.log('\n# # # SECURITY WARNING: This code is provided for achieve    # # #');
console.log('# # # compatibility between different programming languages. # # #');
console.log('# # # It is not necessarily fully secure.                    # # #');
console.log('# # # Its security depends on the complexity and length      # # #');
console.log('# # # of the password, because of only one iteration and     # # #');
console.log('# # # the use of MD5.                                        # # #');
console.log('# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n');

var plaintext = 'The quick brown fox jumps over the lazy dog';
var passphrase = 'my secret passphrase';
console.log('passphrase: ', passphrase);

console.log('\n* * * Encryption * * *');
var ciphertextBase64 = aesCbcPassphraseEncryptToBase64(passphrase, plaintext);
console.log('ciphertext (Base64): ' + ciphertextBase64);
console.log('output is (Base64) ciphertext');

console.log('\n* * * Decryption * * *');
var ciphertextDecryptionBase64 = ciphertextBase64;
console.log('passphrase: ', passphrase);
console.log('ciphertext (Base64): ', ciphertextDecryptionBase64);
console.log('input is (Base64) ciphertext');

var decryptedtext = aesCbcPassphraseDecryptFromBase64(passphrase, ciphertextBase64);
console.log('plaintext: ', decryptedtext);

function aesCbcPassphraseEncryptToBase64(secret, data) {
    const TRANSFORM_ROUNDS = 3;
    //const cypher = Buffer.from(message, "base64");
    const salt = generateRandomSalt8Byte();
    const password = Buffer.concat([Buffer.from(secret, "binary"), salt]);
    const md5Hashes = [];
    let digest = password;
    for (let i = 0; i < TRANSFORM_ROUNDS; i++) {
        md5Hashes[i] = crypto.createHash("md5")
            .update(digest)
            .digest();
        digest = Buffer.concat([md5Hashes[i], password]);
    }
    const key = Buffer.concat([md5Hashes[0], md5Hashes[1]]);
    const iv = md5Hashes[2];
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
    const salted = Buffer.from('Salted__', 'utf8');
    const encrypted = base64Decoding(encryptedBase64);
    const ciphertextCompleteLength = salted.length + salt.length + encrypted.length;
    const ciphertextComplete = Buffer.concat([salted, salt, encrypted], ciphertextCompleteLength);
    return base64Encoding(ciphertextComplete);
}

function aesCbcPassphraseDecryptFromBase64(secret, message) {
    const TRANSFORM_ROUNDS = 3;
    const cypher = Buffer.from(message, "base64");
    const salt = cypher.slice(8, 16);
    const password = Buffer.concat([Buffer.from(secret, "binary"), salt]);
    const md5Hashes = [];
    let digest = password;
    for (let i = 0; i < TRANSFORM_ROUNDS; i++) {
        md5Hashes[i] = crypto.createHash("md5")
            .update(digest)
            .digest();
        digest = Buffer.concat([md5Hashes[i], password]);
    }
    const key = Buffer.concat([md5Hashes[0], md5Hashes[1]]);
    const iv = md5Hashes[2];
    const contents = cypher.slice(16);
    const decipher = crypto.createDecipheriv("aes-256-cbc", key, iv);
    return decipher.update(contents) + decipher.final();
}

function generateRandomSalt8Byte() {
  return crypto.randomBytes(8);
}

function base64Encoding(input) {
  return input.toString('base64');
}

function base64Decoding(input) {
  return Buffer.from(input, 'base64')
}
