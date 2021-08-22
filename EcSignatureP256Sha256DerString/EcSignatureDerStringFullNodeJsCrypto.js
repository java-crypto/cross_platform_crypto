const crypto = require('crypto')

console.log('EC signature string (ECDSA with SHA256) DER-encoding');
const dataToSign = 'The quick brown fox jumps over the lazy dog';
console.log('dataToSign:  ', dataToSign);

// usually we would load the private and public key from a file or keystore
// here we use hardcoded keys for demonstration - don't do this in real programs

console.log('\n* * * sign the plaintext with the EC private key * * *');
const private_key = getEcPrivateKeyPem();
console.log('used private key:\n', private_key);
const signatureBase64 = ecSignToBase64(private_key, dataToSign);
console.log('signature (Base64): ', signatureBase64);

console.log('\n* * * verify the signature against the plaintext with the EC public key * * *');
const public_key = getEcPublicKeyPem();
console.log('used public_key:\n', public_key);
const signatureVerified = ecVerifySignatureFromBase64(public_key, dataToSign, signatureBase64);
console.log('\nsignature (Base64) verified: ', signatureVerified)

function ecSignToBase64(privateKey, message) {
  const signer = crypto.createSign('sha256');
  signer.update(message);
  signer.end();
  const signature = signer.sign(
    {key:private_key, 
    dsaEncoding:'der'
    });
  return signature.toString('Base64')
}

function ecVerifySignatureFromBase64(publicKey, message, signatureBase64) {
  const signature = base64Decoding(signatureBase64);
  const verifier = crypto.createVerify('sha256');
  verifier.update(message);
  verifier.end();
  return verifier.verify(
    {key:publicKey, 
    dsaEncoding:'der'
    },
    signature);
}

function getEcPrivateKeyPem() {
    const key = '-----BEGIN PRIVATE KEY-----\n' +
                'MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgciPt/gulzw7/Xe12\n' +
                'YOu/vLUgIUZ+7gGo5VkmU0B+gUWhRANCAAQxkee3UPW110s0aUQdcS0TDkr8blAe\n' +
                'SBouL4hXziiJX5Me/8OobFgNfYXkk6R/K/fqJhJ/mV8gLur16XhgueXA\n' +
                '-----END PRIVATE KEY-----\n'; // important last crlf
    return key;
}

function getEcPublicKeyPem() {
    const key = '-----BEGIN PUBLIC KEY-----\n' +
                'MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEMZHnt1D1tddLNGlEHXEtEw5K/G5Q\n' +
                'HkgaLi+IV84oiV+THv/DqGxYDX2F5JOkfyv36iYSf5lfIC7q9el4YLnlwA==\n' +
                '-----END PUBLIC KEY-----\n'; // important last crlf
    return key;
}

function base64Decoding(input) {
  return Buffer.from(input, 'base64')
}
