const argon2 = require('argon2');
var crypto = require('crypto');

const {argon2id} = argon2
// package argon2
// https://github.com/ranisalt/node-argon2#readme
// version 0.27.1

console.log('Generate a 32 byte long encryption key with Argon2id');
const password = 'secret password';
console.log('password: ' + password);

// ### security warning - never use a fixed salt in production, this is for compare reasons only
const salt = generateFixedSalt16Byte();
// please use below generateSalt16Byte()
//var salt = generateSalt16Byte();
console.log('salt: ', base64Encoding(salt));

// ### the minimal parameter set is probably UNSECURE ###
generateArgon2idMinimal(password, salt);
generateArgon2idInteractive(password, salt);
generateArgon2idModerate(password, salt);
generateArgon2idSensitive(password, salt);

// ### the minimal parameter set is probably UNSECURE ###
async function generateArgon2idMinimal(password, salt) {
  try {
    const hash = await argon2.hash(password, { type: argon2id, timeCost: 2, memoryCost: 8192, salt, hashLength: 32, raw: true});
    const encryptionKeyArgon2id = base64Encoding(hash);
    console.log('encryptionKeyArgon2id (Base64) minimal:     ', encryptionKeyArgon2id);
  } catch (err) {
    console.log("ERROR " + err);
   }
}

async function generateArgon2idInteractive(password, salt) {
  try {
    const hash = await argon2.hash(password, { type: argon2id, timeCost: 2, memoryCost: 66536, salt, hashLength: 32, raw: true});
    var encryptionKeyArgon2id = base64Encoding(hash);
    console.log('encryptionKeyArgon2id (Base64) interactive: ', encryptionKeyArgon2id);
  } catch (err) {
    console.log("ERROR " + err);
   }
}

async function generateArgon2idModerate(password, salt) {
  try {
    const hash = await argon2.hash(password, { type: argon2id, timeCost: 3, memoryCost: 262144, salt, hashLength: 32, raw: true});
    var encryptionKeyArgon2id = base64Encoding(hash);
    console.log('encryptionKeyArgon2id (Base64) moderate:    ', encryptionKeyArgon2id);
  } catch (err) {
    console.log("ERROR " + err);
   }
}

async function generateArgon2idSensitive(password, salt) {
  try {
    const hash = await argon2.hash(password, { type: argon2id, timeCost: 4, memoryCost: 1048576, salt, hashLength: 32, raw: true});
    var encryptionKeyArgon2id = base64Encoding(hash);
    console.log('encryptionKeyArgon2id (Base64) sensitive:   ', encryptionKeyArgon2id);
  } catch (err) {
    console.log("ERROR " + err);
   }
}

function generateFixedSalt16Byte() {
  return Buffer.from('00000000000000000000000000000000', 'hex');
}

function generateSalt16Byte() {
  return crypto.randomBytes(16);
}

function base64Encoding(input) {
  return input.toString('base64');
}
