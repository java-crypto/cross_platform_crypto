console.log("Generate a 32 byte long AES key");
var forge = require('node-forge');
var aesKey = generateRandomAesKey();
var aesKeyBase64 = base64Encoding(aesKey);
console.log('generated key length: ',
  aesKey.length, ' data: ', aesKeyBase64);

function generateRandomAesKey() {
  return forge.random.getBytesSync(32);
}

function base64Encoding(input) {
  return forge.util.encode64(input);
}
