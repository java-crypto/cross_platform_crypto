var crypto = require('crypto');

console.log('JWT JWE RSA-OAEP-256 AES GCM 256 encryption');

var jweKeyAlgorithm = 'RSA-OAEP-256'; // supported key algorithms: RSA-OAEP, RSA-OAEP-256, RS1_5
//var jweKeyAlgorithm = 'RSA-OAEP';
//var jweKeyAlgorithm = 'RSA1_5';
var jweEncryptionAlgorithm = 'A256GCM'; // supported encryption algorithms: A128GCM, A192GCM, A256GCM
//var jweEncryptionAlgorithm = 'A128GCM';
//var jweEncryptionAlgorithm = 'A192GCM';
console.log('jwe key algorithm:        ' + jweKeyAlgorithm);
console.log('jwe encryption algorithm: ' + jweEncryptionAlgorithm);

// build the payload to encrypt
var dataToEncrypt = "The quick brown fox jumps over the lazy dog";
var issuer = "https://java-crypto.github.io/cross_platform_crypto/";
var subject = "JWE RSA-OAEP & AES GCM encryption";
var tokenExpirationInSeconds = 120;

// build a JWT payload object
var jwePayloadObject = buildPayloadObject(dataToEncrypt, subject, issuer, tokenExpirationInSeconds);
console.log('jwePayloadObject: \n' + jwePayloadObject);

console.log('\n* * * encrypt the payload with recipient\'s public key * * *');
// get public key from recipient
var rsaPublicKeyFromRecipient = loadRsaPublicKeyPem();
console.log('public key loaded');
// encrypt the data and build the encrypted jwe token
var jweTokenBase64Url = jweRsaEncryptToBase64UrlToken(rsaPublicKeyFromRecipient, jweKeyAlgorithm, jweEncryptionAlgorithm, jwePayloadObject);
console.log('jweToken (Base64Url encoded):\n' + jweTokenBase64Url);

console.log('\n* * * decrypt the payload with recipient\'s private key * * *');
// get private key
var rsaPrivateKey = loadRsaPrivateKeyPem();
console.log('private key loaded');
printJweHeaderObject(jweTokenBase64Url);
var jweDecryptedPayload = jweRsaDecryptFromBase64UrlToken(rsaPrivateKey, jweTokenBase64Url);
console.log('jweDecryptedPayload:\n' + jweDecryptedPayload);

function aesGcmEncryptAadToBase64(key, data, jweHeaderObjectBase64Url) {
  var aesGcmAlgorithm;
  if (key.length == 16) {aesGcmAlgorithm = 'aes-128-gcm';}
  if (key.length == 24) {aesGcmAlgorithm = 'aes-192-gcm';}
  if (key.length == 32) {aesGcmAlgorithm = 'aes-256-gcm';}
  var jweHeaderObjectBuffer = Buffer.from(jweHeaderObjectBase64Url, 'utf8');
  var nonce = generateRandomNonce();
  const cipher = crypto.createCipheriv(aesGcmAlgorithm, key, nonce);
  let encryptedBase64 = '';
  cipher.setAAD(jweHeaderObjectBuffer);
  cipher.setEncoding('base64');
  cipher.on('data', (chunk) => encryptedBase64 += chunk);
  cipher.on('end', () => {
    // do nothing console.log(encryptedBase64);
    // Prints: some clear text data
  });
  cipher.write(data);
  cipher.end();
  var nonceBase64 = base64Encoding(nonce);
  var gcmTagBase64 = base64Encoding(cipher.getAuthTag());
  return nonceBase64 + ':' + encryptedBase64 + ':' + gcmTagBase64;
}

function aesGcmDecryptAadFromBase64(key, data, jweHeaderObjectBase64Url) {
  var aesGcmAlgorithm;
  if (key.length == 16) {aesGcmAlgorithm = 'aes-128-gcm';}
  if (key.length == 24) {aesGcmAlgorithm = 'aes-192-gcm';}
  if (key.length == 32) {aesGcmAlgorithm = 'aes-256-gcm';}
  var jweHeaderObjectBuffer = Buffer.from(jweHeaderObjectBase64Url, 'utf8');
  var dataSplit = data.split(":");
  var nonce = base64Decoding(dataSplit[0]);
  var ciphertext = dataSplit[1];
  var gcmTag = base64Decoding(dataSplit[2]);
  const decipher = crypto.createDecipheriv(aesGcmAlgorithm, key, nonce);
  decipher.setAAD(jweHeaderObjectBuffer);
  decipher.setAuthTag(gcmTag);
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

function rsaEncryptionOaepSha256ToBase64(publicKey, plaintext) {
  var encrypted = crypto.publicEncrypt(
    { key: publicKey,
      padding: crypto.constants.RSA_PKCS1_OAEP_PADDING,
      oaepHash: "sha256"}, plaintext);
  return encrypted.toString('base64')
}

function rsaEncryptionOaepSha1ToBase64(publicKey, plaintext) {
  var encrypted = crypto.publicEncrypt(
    { key: publicKey,
      padding: crypto.constants.RSA_PKCS1_OAEP_PADDING,
      oaepHash: "sha1"}, plaintext);
  return encrypted.toString('base64')
}

function rsaEncryptionPkcs15ToBase64(publicKey, plaintext) {
  var encrypted = crypto.publicEncrypt(
    { key: publicKey,
      padding: crypto.constants.RSA_PKCS1_PADDING
    }, plaintext);
  return encrypted.toString('base64')
}

function rsaDecryptionOaepSha256Binary(privateKey, ciphertext) {
  var options = {key: privateKey,
            padding: crypto.constants.RSA_PKCS1_OAEP_PADDING,
            oaepHash: "sha256" };
  var decrypted = crypto.privateDecrypt(options, ciphertext);
  //return decrypted.toString('utf8')
  return decrypted;
}

function rsaDecryptionOaepSha1Binary(privateKey, ciphertext) {
  var options = {key: privateKey,
            padding: crypto.constants.RSA_PKCS1_OAEP_PADDING,
            oaepHash: "sha1" };
  var decrypted = crypto.privateDecrypt(options, ciphertext);
  return decrypted;
}

function rsaDecryptionPkcs15Binary(privateKey, ciphertext) {
  var options = {key: privateKey,
            padding: crypto.constants.RSA_PKCS1_PADDING};
  var decrypted = crypto.privateDecrypt(options, ciphertext);
  return decrypted;
}

function jweRsaEncryptToBase64UrlToken(rsaPublicKeyFromRecipient, jweKeyAlgorithm, jweEncryptionAlgorithm, jwePayloadObject) {
  // build a header depending on the key algorithm
  var jweHeaderObject = buildJweHeaderObject(jweKeyAlgorithm, jweEncryptionAlgorithm);
  let jweHeaderObjectBase64Url = base64ToBase64Url(Buffer.from(jweHeaderObject).toString("base64"));
  var aesGcmKeyLength;
  switch (jweEncryptionAlgorithm) {
      case 'A128GCM' :
        aesGcmKeyLength = 16;
        break;
      case 'A192GCM' :
        aesGcmKeyLength = 24;
        break;
      case 'A256GCM' :
        aesGcmKeyLength = 32;
        break;
      default:
        return '*** not supported algorithm ***';
        break;
   }
  // encrypt the payload with a random generated aes key
  var encryptionKey = generateRandomAesKey(aesGcmKeyLength);
  var encryptedPayloadBase64 = aesGcmEncryptAadToBase64(encryptionKey, jwePayloadObject, jweHeaderObjectBase64Url);
  var encryptedPayloadBase64Url = base64ToBase64Url(encryptedPayloadBase64);
  var dataSplit = encryptedPayloadBase64Url.split(":");
  var nonceBase64Url = base64ToBase64Url(dataSplit[0]);
  var ciphertextBase64Url = base64ToBase64Url(dataSplit[1]);
  var gcmTagBase64Url = base64ToBase64Url(dataSplit[2]);
  // here we need to encrypt the key with rsaPublicKeyFromRecipient
  var encryptedKeyBase64;
  switch (jweKeyAlgorithm) {
    case 'RSA1_5':
      encryptedKeyBase64 = rsaEncryptionPkcs15ToBase64(rsaPublicKeyFromRecipient, encryptionKey);
      break;
    case 'RSA-OAEP':
      encryptedKeyBase64 = rsaEncryptionOaepSha1ToBase64(rsaPublicKeyFromRecipient, encryptionKey);
      break;
    case 'RSA-OAEP-256':
      encryptedKeyBase64 = rsaEncryptionOaepSha256ToBase64(rsaPublicKeyFromRecipient, encryptionKey);
      break;
    default:
      return '*** not supported algorithm ***';
      break;
  }
  var encryptedKeyBase64Url = base64ToBase64Url(encryptedKeyBase64);
  return jweHeaderObjectBase64Url + '.'
    + encryptedKeyBase64Url + '.'
    + nonceBase64Url + '.'
    + ciphertextBase64Url + '.'
    + gcmTagBase64Url;
}

function jweRsaDecryptFromBase64UrlToken(rsaPrivateKey, jweTokenBase64Url){
  // ### split token
  var dataSplit = jweTokenBase64Url.split(".");
  var jweHeaderObjectBase64 = dataSplit[0];
  var headerObject = base64Decoding(jweHeaderObjectBase64);
  var encryptedKeyBase64Url = dataSplit[1];
  var encryptedKeyBase64 = base64UrlToBase64(encryptedKeyBase64Url);
  var jweEncryptedKey = base64Decoding(encryptedKeyBase64);
  var nonceBase64 = dataSplit[2];
  var ciphertextBase64 = dataSplit[3];
  var gcmTagBase64 = dataSplit[4];
  // decode the headerObject and get the keyAlgorithm & encryptionAlgorithm
  var jweKeyAlgorithm = getJweHeaderKeyAlgorithm(headerObject);
  var jweEncryptionAlgorithm = getJweHeaderEncryptionAlgorithm(headerObject);
  var aesDecryptionKey;
  switch (jweKeyAlgorithm) {
    case 'RSA1_5':
      aesDecryptionKey = rsaDecryptionPkcs15Binary(rsaPrivateKey, jweEncryptedKey);
      break;
    case 'RSA-OAEP':
      aesDecryptionKey = rsaDecryptionOaepSha1Binary(rsaPrivateKey, jweEncryptedKey);
      break;
    case 'RSA-OAEP-256':
      aesDecryptionKey = rsaDecryptionOaepSha256Binary(rsaPrivateKey, jweEncryptedKey);
      break;
    default:
      return '*** not supported algorithm ***';
      break;
  }
  var ciphertextComplete = nonceBase64 + ':'
    + ciphertextBase64 + ':'
    + gcmTagBase64;
  var decryptedPayload = aesGcmDecryptAadFromBase64(aesDecryptionKey, ciphertextComplete, jweHeaderObjectBase64);
  return decryptedPayload;
}

function generateRandomAesKey(keyLength) {
  return crypto.randomBytes(keyLength);
}

function generateRandomNonce() {
  return crypto.randomBytes(12);
}

function base64Encoding(input) {
  return input.toString('base64');
}

function base64Decoding(input) {
  return Buffer.from(input, 'base64')
}

function base64ToBase64Url(input){
	input = input.replace(/\=/g,"");
	input = input.replace(/\+/g,"-");
	input = input.replace(/\//g,"_");
	return input;
}

function base64UrlToBase64Old(input) {
    // Replace non-url compatible chars with base64 standard chars
    input = input
    .replace(/-/g, '+')
    .replace(/_/g, '/');
    // Pad out with standard base64 required padding characters
    var pad = input.length % 4;
    if(pad) {
		if(pad === 1) {
			throw new Error('InvalidLengthError: Input base64url string is the wrong length to determine padding');
		}
		input += new Array(5-pad).join('=');
    }
    return input;
}

function base64UrlToBase64(encoded) {
  encoded = encoded.replace('-', '+').replace('_', '/');
  while (encoded.length % 4)
    encoded += '=';
  return encoded;
};

function buildPayloadObject(dataToEncrypt, subject, issuer, tokenExpirationInSeconds) {
  var unixtime = Math.floor(Date.now() / 1000);
	var expiredunixtime = unixtime + tokenExpirationInSeconds;
	var obj = {sub:subject, iss:issuer, dat:dataToEncrypt, iat:unixtime, exp:expiredunixtime}
	return JSON.stringify(obj, null, 4);
}

function buildJweHeaderObject(jweKeyAlgorithm, jweEncryptionAlgorithm){
  var obj = {'alg': jweKeyAlgorithm,
        'enc': jweEncryptionAlgorithm,
        'typ': 'JWT'};
  return JSON.stringify(obj, null, 4);
}

function printJweHeaderObject(jweTokenBase64Url){
  var dataSplit = jweTokenBase64Url.split(".");
  var jweHeaderObjectBase64 = dataSplit[0];
  var headerObject = base64Decoding(jweHeaderObjectBase64);
  console.log('headerObject:\n' + headerObject);
}

function getJweHeaderKeyAlgorithm(jweHeaderObject){
  var objectValue = JSON.parse(jweHeaderObject.toString());
  return objectValue['alg'];
}

function getJweHeaderEncryptionAlgorithm(jweHeaderObject){
  var objectValue = JSON.parse(jweHeaderObject.toString());
  return objectValue['enc'];
}

// sample key, don't worry !
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

// sample key, don't worry !
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