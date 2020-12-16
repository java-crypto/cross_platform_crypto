const crypto = require('crypto')
const fs = require('fs');

console.log("RSA AES CBC hybrid encryption");

console.log("\nuses AES CBC 256 random key for the plaintext encryption");
console.log("and RSA OAEP SHA 1 2048 bit for the encryption of the random key\n");

var plaintextString = "The quick brown fox jumps over the lazy dog";
console.log("plaintext: ", plaintextString);

// # # # usually we would load the private and public key from a file or keystore # # #
// # # # here we use hardcoded keys for demonstration - don't do this in real programs # # #
var filenamePrivateKeyPem = "privatekey2048.pem";
var filenamePublicKeyPem = "publickey2048.pem";

// encryption
console.log("\n* * * Encryption * * *");
var publicKeyLoad = loadRsaPublicKeyPem();
// use this in production
//var publicKeyLoad = fs.readFileSync(filenamePublicKeyPem);
//var ciphertextBase64 = rsaEncryptionOaepSha1(publicKeyLoad, dataToEncrypt);
var completeCiphertextBase64 = rsaOaepSha1AesCbc256HybridStringEncryption (publicKeyLoad, plaintextString);
console.log("completeCiphertextBase64: ", completeCiphertextBase64);

// transport the encrypted data to recipient

// receiving the encrypted data, decryption
console.log("\n* * * Decryption * * *");
var completeCiphertextReceivedBase64 = completeCiphertextBase64;
console.log("completeCiphertextReceivedBase64: " + completeCiphertextReceivedBase64);
var privateKeyLoad = loadRsaPrivateKeyPem();
// use this in production
//var privateKeyLoad = fs.readFileSync(filenamePrivateKeyPem);
var decryptedtext = rsaOaepSha1AesCbc256HybridStringDecryption(privateKeyLoad, completeCiphertextReceivedBase64);
console.log("decryptedtext: " + decryptedtext);

function rsaOaepSha1AesCbc256HybridStringEncryption (publicKey, plaintext) {
  // generate random key
  var encryptionKey = generateRandomAesKey();
  // encrypt plaintext
  var ciphertextBase64 = aesCbcEncryptToBase64(encryptionKey, plaintext);
  // encrypt the aes encryption key with the rsa public key
  var encryptionKeyBase64 = rsaEncryptionOaepSha1(publicKey, encryptionKey);
  return encryptionKeyBase64 + ':' + ciphertextBase64;
}

function rsaOaepSha1AesCbc256HybridStringDecryption (privateKey, completeCiphertext) {
  var dataSplit = completeCiphertext.split(":");
  var encryptionKeyReceived = base64Decoding(dataSplit[0]);
  var iv = base64Decoding(dataSplit[1]);
  var ciphertext = dataSplit[2];
  // decrypt the encryption key with the rsa private key
  var decryptionKey = rsaDecryptionOaepSha1(privateKey, encryptionKeyReceived);
  return aesCbcDecrypt(decryptionKey, iv, ciphertext);
}

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

function aesCbcDecrypt(key, iv, ciphertext) {
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

function rsaEncryptionOaepSha1(publicKey, plaintext) {
  var encrypted = crypto.publicEncrypt(
    { key: publicKey,
      padding: crypto.constants.RSA_PKCS1_OAEP_PADDING,
      oaepHash: "sha1"}, plaintext);
  return encrypted.toString('base64')
}

function rsaDecryptionOaepSha1(privateKey, ciphertext) {
  var options = {key: privateKey,
            padding: crypto.constants.RSA_PKCS1_OAEP_PADDING,
            oaepHash: "sha1" };
  var decrypted = crypto.privateDecrypt(options, ciphertext);
  return decrypted;
}

function base64Encoding(input) {
  return input.toString('base64');
}

function base64Decoding(input) {
   return Buffer.from(input, 'base64')
}

function loadRsaPrivateKeyPem() {
    const key = '-----BEGIN PRIVATE KEY-----\n' +
        'MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDwSZYlRn86zPi9\n' +
        'e1RTZL7QzgE/36zjbeCMyOhf6o/WIKeVxFwVbG2FAY3YJZIxnBH+9j1XS6f+ewjG\n' +
        'FlJY4f2IrOpS1kPiO3fmOo5N4nc8JKvjwmKtUM0t63uFFPfs69+7mKJ4w3tk2mSN\n' +
        '4gb8J9P9BCXtH6Q78SdOYvdCMspA1X8eERsdLb/jjHs8+gepKqQ6+XwZbSq0vf2B\n' +
        'MtaAB7zTX/Dk+ZxDfwIobShPaB0mYmojE2YAQeRq1gYdwwO1dEGk6E5J2toWPpKY\n' +
        '/IcSYsGKyFqrsmbw0880r1BwRDer4RFrkzp4zvY+kX3eDanlyMqDLPN+ghXT1lv8\n' +
        'snZpbaBDAgMBAAECggEBAIVxmHzjBc11/73bPB2EGaSEg5UhdzZm0wncmZCLB453\n' +
        'XBqEjk8nhDsVfdzIIMSEVEowHijYz1c4pMq9osXR26eHwCp47AI73H5zjowadPVl\n' +
        'uEAot/xgn1IdMN/boURmSj44qiI/DcwYrTdOi2qGA+jD4PwrUl4nsxiJRZ/x7PjL\n' +
        'hMzRbvDxQ4/Q4ThYXwoEGiIBBK/iB3Z5eR7lFa8E5yAaxM2QP9PENBr/OqkGXLWV\n' +
        'qA/YTxs3gAvkUjMhlScOi7PMwRX9HsrAeLKbLuC1KJv1p2THUtZbOHqrAF/uwHaj\n' +
        'ygUblFaa/BTckTN7PKSVIhp7OihbD04bSRrh+nOilcECgYEA/8atV5DmNxFrxF1P\n' +
        'ODDjdJPNb9pzNrDF03TiFBZWS4Q+2JazyLGjZzhg5Vv9RJ7VcIjPAbMy2Cy5BUff\n' +
        'EFE+8ryKVWfdpPxpPYOwHCJSw4Bqqdj0Pmp/xw928ebrnUoCzdkUqYYpRWx0T7YV\n' +
        'RoA9RiBfQiVHhuJBSDPYJPoP34kCgYEA8H9wLE5L8raUn4NYYRuUVMa+1k4Q1N3X\n' +
        'Bixm5cccc/Ja4LVvrnWqmFOmfFgpVd8BcTGaPSsqfA4j/oEQp7tmjZqggVFqiM2m\n' +
        'J2YEv18cY/5kiDUVYR7VWSkpqVOkgiX3lK3UkIngnVMGGFnoIBlfBFF9uo02rZpC\n' +
        '5o5zebaDImsCgYAE9d5wv0+nq7/STBj4NwKCRUeLrsnjOqRriG3GA/TifAsX+jw8\n' +
        'XS2VF+PRLuqHhSkQiKazGr2Wsa9Y6d7qmxjEbmGkbGJBC+AioEYvFX9TaU8oQhvi\n' +
        'hgA6ZRNid58EKuZJBbe/3ek4/nR3A0oAVwZZMNGIH972P7cSZmb/uJXMOQKBgQCs\n' +
        'FaQAL+4sN/TUxrkAkylqF+QJmEZ26l2nrzHZjMWROYNJcsn8/XkaEhD4vGSnazCu\n' +
        '/B0vU6nMppmezF9Mhc112YSrw8QFK5GOc3NGNBoueqMYy1MG8Xcbm1aSMKVv8xba\n' +
        'rh+BZQbxy6x61CpCfaT9hAoA6HaNdeoU6y05lBz1DQKBgAbYiIk56QZHeoZKiZxy\n' +
        '4eicQS0sVKKRb24ZUd+04cNSTfeIuuXZrYJ48Jbr0fzjIM3EfHvLgh9rAZ+aHe/L\n' +
        '84Ig17KiExe+qyYHjut/SC0wODDtzM/jtrpqyYa5JoEpPIaUSgPuTH/WhO3cDsx6\n' +
        '3PIW4/CddNs8mCSBOqTnoaxh\n' +
        '-----END PRIVATE KEY-----\n'; // important last crlf
    return key;
}

function loadRsaPublicKeyPem() {
    const key = '-----BEGIN PUBLIC KEY-----\n' +
        'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+\n' +
        '0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9\n' +
        'iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT\n' +
        '/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8\n' +
        '01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB\n' +
        'ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g\n' +
        'QwIDAQAB\n' +
        '-----END PUBLIC KEY-----\n'; // important last crlf
    return key;
}
