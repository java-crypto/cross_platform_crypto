const crypto = require('crypto')

console.log('EC signature string (ECDSA with SHA256)');
const dataToSign = 'The quick brown fox jumps over the lazy dog';
console.log('dataToSign:  ', dataToSign);

// usually we would load the private and public key from a file or keystore
// here we use hardcoded keys for demonstration - don't do this in real programs

console.log('\n* * * sign the plaintext with the EC private key * * *');
// the beginning of the EC PRIVATE KEY needs to get changed to
// PRIVATE KEY, same for the end
const private_key = formatEcPrivateKeyPem(getEcPrivateKeyPem());
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
    dsaEncoding:'ieee-p1363'
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
    dsaEncoding:'ieee-p1363'
    },
    signature);
}

function formatEcPrivateKeyPem(privateKeyPem) {
  var re = /EC PRIVATE KEY/gi;
  return privateKeyPem.replace(re, "PRIVATE KEY");
}

function getEcPrivateKeyPem() {
    const key = '-----BEGIN EC PRIVATE KEY-----\n' +
                'MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCAU2f8tzo99Z1HoxJlY\n' +
                '96yXUhFY5vppVjw1iPKRfk1wHA==\n' +
                '-----END EC PRIVATE KEY-----\n'; // important last crlf
    return key;
}

function getEcPublicKeyPem() {
    const key = '-----BEGIN PUBLIC KEY-----\n' +
                'MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEzb7yAFWup6iDqJiEq764rAumsV2M\n' +
                'rspZxaP3WGpwHaC4Uff3N4UbJZF7Zac1c6W7KJl0eeCP0205Q3UEpwxndQ==\n' +
                '-----END PUBLIC KEY-----\n'; // important last crlf
    return key;
}

function base64Decoding(input) {
  return Buffer.from(input, 'base64')
}
