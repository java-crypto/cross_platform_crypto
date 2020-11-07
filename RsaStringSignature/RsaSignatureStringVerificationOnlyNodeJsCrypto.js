const crypto = require('crypto')

console.log('RSA signature string verification only');
const dataToSign = 'The quick brown fox jumps over the lazy dog';
console.log('dataToSign:  ', dataToSign);

// paste signature here:
const signatureBase64 = "vCSB4744p30IBl/sCLjvKm2nix7wfScQn99YX9tVIDNQIvU3QnSCLc2cF+J6R9WMRIXOsY94MxjKCQANW0CuaSs+w31ePHaounFVnmXyY092SicZrtpwlxw2CHqJ0NSyciDpxlRId1vjKlp9E5IJmYtVMtL2hfb711P+nb+m+1sPplNXPpJpdnWIzfLsDMVxCkplAdrcoH2HuWgOtOCHAf3vWbUC/vkvi388NT1UXJRPoERM0m1v11ogP9DiycMdoJxg3fbdH3HknbR02MLNEr7q4ZMzlrKxnYChwp2hnnBvJDcXpPDnQz7sG8zrim1nL/PS8CRG5lxhYYAZqTc+Vg==";

// usually we would load the public key from a file or keystore
// here we use hardcoded key for demonstration - don't do this in real programs

console.log('\n* * * verify the signature against the plaintext with the RSA public key * * *');

const public_key = getPublicKeyPem();
console.log('used public_key:\n', public_key);
const signatureVerified = rsaVerifySignatureFromBase64(public_key, dataToSign, signatureBase64);
console.log('\nsignature (Base64) verified: ', signatureVerified)

function rsaVerifySignatureFromBase64(publicKey, message, signatureBase64) {
  const signature = base64Decoding(signatureBase64);
  const verifier = crypto.createVerify('sha256');
  verifier.update(message);
  verifier.end();
  return verifier.verify(public_key, signature);
}

function getPublicKeyPem() {
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

function base64Decoding(input) {
  return Buffer.from(input, 'base64')
}
