const crypto = require('crypto')

console.log('EC signature converter P1363 to DER encoding');
console.log('Please note that all values are in Base64 encoded form');

// insert the ecdsa signature in P1363 encoding

const signatureP1363Base64 = 'aF1cTCPqSUuCSKMftzeE37VeaqrlQuILJsW9RBEo8yZUyZFTOkEqq2TI2Bd/kugow6bbZDfhtsB3QwbZ55aOmQ==';

var signatureDerBase64 = convertSignatureP1363ToDerBase64(signatureP1363Base64);
console.log('signature in P1363 encoding: ', signatureP1363Base64);
console.log('signature in DER encoding:   ', signatureDerBase64);

function convertSignatureP1363ToDerBase64(p1363Base64) {
    return base64Encoding(p1363ToDer(base64Decoding(p1363Base64)));
}

/**
 * Converts an ECDSA signature from P1363 to DER format
 * source: https://developer.token.io/sdk/esdoc/file/src/security/CryptoBrowser.js.html
 *
 * @param {Uint8Array} sig - P1363 signature
 * @return {Uint8Array} DER signature
 * @private
 */
function p1363ToDer(sig) {
    const signature = Array.from(sig, x => ('00' + x.toString(16)).slice(-2)).join('');
    let r = signature.substr(0, signature.length / 2);
    let s = signature.substr(signature.length / 2);
    r = r.replace(/^(00)+/, '');
    s = s.replace(/^(00)+/, '');
    if ((parseInt(r, 16) & '0x80') > 0) r = `00${r}`;
    if ((parseInt(s, 16) & '0x80') > 0) s = `00${s}`;
    const rString = `02${(r.length / 2).toString(16).padStart(2, '0')}${r}`;
    const sString = `02${(s.length / 2).toString(16).padStart(2, '0')}${s}`;
    const derSig = `30${((rString.length + sString.length) / 2)
            .toString(16).padStart(2, '0')}${rString}${sString}`;
    return new Uint8Array(derSig.match(/[\da-f]{2}/gi).map(h => parseInt(h, 16)));
}

/**
 * Converts an ECDSA signature from DER to P1363 format
 * source: https://developer.token.io/sdk/esdoc/file/src/security/CryptoBrowser.js.html
 *
 * @param {Uint8Array} sig - DER signature
 * @return {Uint8Array} P1363 signature
 * @private
 */
function derToP1363(sig) {
    const signature = Array.from(sig, x => ('00' + x.toString(16)).slice(-2)).join('');
    const rLength = parseInt(signature.substr(6, 2), 16) * 2;
    let r = signature.substr(8, rLength);
    let s = signature.substr(12 + rLength);
    r = r.length > 64 ? r.substr(-64) : r.padStart(64, '0');
    s = s.length > 64 ? s.substr(-64) : s.padStart(64, '0');
    const p1363Sig = `${r}${s}`;
    return new Uint8Array(p1363Sig.match(/[\da-f]{2}/gi).map(h => parseInt(h, 16)));
}

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

function base64Encoding(u8) {
    return Buffer.from(u8, 'binary').toString('base64')
}

function base64Decoding(input) {
  return Buffer.from(input, 'base64')
}
