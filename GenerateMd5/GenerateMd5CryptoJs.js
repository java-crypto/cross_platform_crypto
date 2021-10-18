var CryptoJS = require("crypto-js");
console.log('Generate a MD5 hash');

console.log('# # # SECURITY WARNING: This code is provided for achieve    # # #');
console.log('# # # compatibility between different programming languages. # # #');
console.log('# # # It is NOT SECURE - DO NOT USE THIS CODE ANY LONGER !   # # #');
console.log('# # # The hash algorithm MD5 is BROKEN.                      # # #');
console.log('# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n');

const plaintext = 'The quick brown fox jumps over the lazy dog';
console.log('plaintext:               ', plaintext);

var md5Value = calculateMd5(plaintext);
console.log('md5Value (hex) length: ' + md5Value.sigBytes + ' data: ' + bytesToHex(md5Value));

function calculateMd5(input) {
  return CryptoJS.MD5(input);
}

function bytesToHex(input) {
  return input.toString(CryptoJS.enc.Hex)
}
