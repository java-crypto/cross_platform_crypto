var CryptoJS = require("crypto-js");
var axlsign = require('./axlsign.js'); //

console.log('Generate a key pair for Curve25519');
// you need the library axlsign.js: 
// https://github.com/wavesplatform/curve25519-js
// you need the library buffer-to-uint8array
// https://github.com/substack/buffer-to-uint8array

var curve25519Keypair = generateCurve25519KeyPair();
var privateKey = curve25519Keypair.private;
var publicKey = curve25519Keypair.public;
console.log('base64 encoded key data:');
console.log('Curve25519 PrivateKey: ' + Uint8ToBase64(privateKey));
console.log('Curve25519 PublicKey:  ' + Uint8ToBase64(publicKey));

function generateCurve25519KeyPair() {
  var seedWord = CryptoJS.lib.WordArray.random(32);
  var seedUint8 = CryptJsWordArrayToUint8Array(seedWord);
  return axlsign.generateKeyPair(seedUint8);
}

function Uint8ToBase64 (u8) {
    return Buffer.from(u8, 'binary').toString('base64')
}

/* Converts a cryptjs WordArray to native Uint8Array */                                   
function CryptJsWordArrayToUint8Array(wordArray) {                                            const l = wordArray.sigBytes;                                                         
    const words = wordArray.words;                                                        
    const result = new Uint8Array(l);                                                     
    var i=0 /*dst*/, j=0 /*src*/;
    while(true) {
        // here i is a multiple of 4
        if (i==l)
            break;
        var w = words[j++];
        result[i++] = (w & 0xff000000) >>> 24;
        if (i==l)
            break;
        result[i++] = (w & 0x00ff0000) >>> 16;                                            
        if (i==l)                                                                         
            break;                                                                        
        result[i++] = (w & 0x0000ff00) >>> 8;
        if (i==l)
            break;
        result[i++] = (w & 0x000000ff);                                                   
    }
    return result;
}
