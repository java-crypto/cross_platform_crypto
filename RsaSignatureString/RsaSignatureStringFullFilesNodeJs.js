// CpcCryptoJsRsaSignatureStringFullFiles
// https://repl.it/@javacrypto/CpcCryptoJsRsaSignatureStringFullFiles
// funktioniert, signature ist mit Java identisch
const crypto = require('crypto')
const fs = require('fs')

const private_key = fs.readFileSync('privatekey.pem', 'utf-8')
const public_key = fs.readFileSync('publickey.pem', 'utf-8')
const message = fs.readFileSync('message.txt', 'utf-8')

const signer = crypto.createSign('sha256');
signer.update(message);
signer.end();

const signature = signer.sign(private_key)
const signature_hex = signature.toString('hex')
const signature_base64 = signature.toString('Base64')

const verifier = crypto.createVerify('sha256');
verifier.update(message);
verifier.end();

const verified = verifier.verify(public_key, signature);

console.log(JSON.stringify({
    message: message,
    signature: signature_hex,
    verified: verified,
}, null, 2));
console.log('signature: ', signature_base64);
