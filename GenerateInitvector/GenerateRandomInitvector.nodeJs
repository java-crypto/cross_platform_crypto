console.log("Generate a 16 byte long Initialization vector (IV)");
var forge = require('node-forge');
var iv = generateRandomInitvector();
var ivBase64 = base64Encoding(iv);
console.log('generated iv length: ',
  iv.length, ' data: ', ivBase64);

function generateRandomInitvector() {
  return forge.random.getBytesSync(16);
}

function base64Encoding(input) {
  return forge.util.encode64(input);
}
