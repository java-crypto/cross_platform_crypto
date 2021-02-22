const jws = require('jws');

function buildPayloadObject(subject, issuer, tokenExpirationInSeconds) {
  const dateNow = Math.floor(Date.now() / 1000);
  const dateExpiration = dateNow + tokenExpirationInSeconds;
  var payloadObj = {
    iss: issuer,
    sub: subject,
    iat: dateNow,
    exp: dateExpiration
  };
  return payloadObj;
}

function jwsSignatureToString(jwsAlgorithm, privateKeyPem, payload) {
  const tokenSign = jws.sign({
    header: { alg: 'PS256' },
    payload,
    privateKey: privateKeyPem,
  })
  return tokenSign;
}

function jwsSignatureVerification(publicKeyPem, jwsSignedTokenReceived) {
  const result = jws.verify(jwsSignedTokenReceived, 'PS256', publicKeyPem);
  return result;
}

function checkExpiration(jwsSignedToken) {
  const signatureDecoded = jws.decode(jwsSignedToken);
  var objectValue = JSON.parse(signatureDecoded.payload);
  const dateExpiration =  objectValue['exp'];
  const dateNow = Math.floor(Date.now() / 1000);
  if (dateExpiration > dateNow) {
    return 'true';
  } else {
    return 'false';
  }
  return 'false';
}

function printPayloadObject(jwsSignedToken) {
  const signatureDecoded = jws.decode(jwsSignedToken);
  return signatureDecoded.payload;
}

function printHeaderObject(jwsSignedToken) {
  const signatureDecoded = jws.decode(jwsSignedToken);
  return signatureDecoded.header;
}

function printSignatureObject(jwsSignedToken) {
  const signatureDecoded = jws.decode(jwsSignedToken);
  return signatureDecoded;
}

const getPrivateKeyPem = `-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDwSZYlRn86zPi9
e1RTZL7QzgE/36zjbeCMyOhf6o/WIKeVxFwVbG2FAY3YJZIxnBH+9j1XS6f+ewjG
FlJY4f2IrOpS1kPiO3fmOo5N4nc8JKvjwmKtUM0t63uFFPfs69+7mKJ4w3tk2mSN
4gb8J9P9BCXtH6Q78SdOYvdCMspA1X8eERsdLb/jjHs8+gepKqQ6+XwZbSq0vf2B
MtaAB7zTX/Dk+ZxDfwIobShPaB0mYmojE2YAQeRq1gYdwwO1dEGk6E5J2toWPpKY
/IcSYsGKyFqrsmbw0880r1BwRDer4RFrkzp4zvY+kX3eDanlyMqDLPN+ghXT1lv8
snZpbaBDAgMBAAECggEBAIVxmHzjBc11/73bPB2EGaSEg5UhdzZm0wncmZCLB453
XBqEjk8nhDsVfdzIIMSEVEowHijYz1c4pMq9osXR26eHwCp47AI73H5zjowadPVl
uEAot/xgn1IdMN/boURmSj44qiI/DcwYrTdOi2qGA+jD4PwrUl4nsxiJRZ/x7PjL
hMzRbvDxQ4/Q4ThYXwoEGiIBBK/iB3Z5eR7lFa8E5yAaxM2QP9PENBr/OqkGXLWV
qA/YTxs3gAvkUjMhlScOi7PMwRX9HsrAeLKbLuC1KJv1p2THUtZbOHqrAF/uwHaj
ygUblFaa/BTckTN7PKSVIhp7OihbD04bSRrh+nOilcECgYEA/8atV5DmNxFrxF1P
ODDjdJPNb9pzNrDF03TiFBZWS4Q+2JazyLGjZzhg5Vv9RJ7VcIjPAbMy2Cy5BUff
EFE+8ryKVWfdpPxpPYOwHCJSw4Bqqdj0Pmp/xw928ebrnUoCzdkUqYYpRWx0T7YV
RoA9RiBfQiVHhuJBSDPYJPoP34kCgYEA8H9wLE5L8raUn4NYYRuUVMa+1k4Q1N3X
Bixm5cccc/Ja4LVvrnWqmFOmfFgpVd8BcTGaPSsqfA4j/oEQp7tmjZqggVFqiM2m
J2YEv18cY/5kiDUVYR7VWSkpqVOkgiX3lK3UkIngnVMGGFnoIBlfBFF9uo02rZpC
5o5zebaDImsCgYAE9d5wv0+nq7/STBj4NwKCRUeLrsnjOqRriG3GA/TifAsX+jw8
XS2VF+PRLuqHhSkQiKazGr2Wsa9Y6d7qmxjEbmGkbGJBC+AioEYvFX9TaU8oQhvi
hgA6ZRNid58EKuZJBbe/3ek4/nR3A0oAVwZZMNGIH972P7cSZmb/uJXMOQKBgQCs
FaQAL+4sN/TUxrkAkylqF+QJmEZ26l2nrzHZjMWROYNJcsn8/XkaEhD4vGSnazCu
/B0vU6nMppmezF9Mhc112YSrw8QFK5GOc3NGNBoueqMYy1MG8Xcbm1aSMKVv8xba
rh+BZQbxy6x61CpCfaT9hAoA6HaNdeoU6y05lBz1DQKBgAbYiIk56QZHeoZKiZxy
4eicQS0sVKKRb24ZUd+04cNSTfeIuuXZrYJ48Jbr0fzjIM3EfHvLgh9rAZ+aHe/L
84Ig17KiExe+qyYHjut/SC0wODDtzM/jtrpqyYa5JoEpPIaUSgPuTH/WhO3cDsx6
3PIW4/CddNs8mCSBOqTnoaxh
-----END PRIVATE KEY-----
`;

const getPublicKeyPem = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9
iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT
/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8
01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB
ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g
QwIDAQAB
-----END PUBLIC KEY-----
`;

const getPublicKeyPem2 = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+
0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9
iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT
/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8
01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB
ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW3g
QwIDAQAB
-----END PUBLIC KEY-----
`;

console.log('JWT JWS PS256 RSA with RSASSA-PSS + MGF1 with SHA-256 padding and SHA-256 hashing');

const jwsAlgorithm = 'PS256';

// usually we would load the private and public key from a file or keystore
// here we use hardcoded keys for demonstration - don't do this in real programs

console.log('\n* * * sign the payload object with the RSA private key * * *');
// payload data
const issuer = 'https://java-crypto.github.io/cross_platform_crypto/';
const subject = 'JWT PS256 signature';
const tokenExpirationInSeconds = 120;

// build a JWT payload object
const payload = buildPayloadObject(subject, issuer, tokenExpirationInSeconds);
console.log('jwtClaimsSet: ');
console.log(payload);

// read RSA private key as PEM object
const jwkRsaPrivateKey = getPrivateKeyPem;
console.log('privateKey in PEM format:\n' + jwkRsaPrivateKey);

// sign the payload
const jwsSignedToken = jwsSignatureToString(jwsAlgorithm, jwkRsaPrivateKey, payload);
console.log('signed jwsToken:\n' + jwsSignedToken);

// send the token to the recipient
console.log('\n* * * verify the jwsToken with the RSA public key * * *');
const jwsSignedTokenReceived = jwsSignedToken;

const jwkRsaPublicKey = getPublicKeyPem;
console.log('publicKey in PEM format:\n' + jwkRsaPublicKey);

const tokenVerified = jwsSignatureVerification(jwkRsaPublicKey, jwsSignedTokenReceived);
console.log('the token\'s signature is verified: ' + tokenVerified);
console.log('\ncheck signature with not matching public key');
const jwkRsaPublicKey2 = getPublicKeyPem2;
const tokenNotVerified = jwsSignatureVerification(jwkRsaPublicKey2, jwsSignedTokenReceived);
console.log('the token\'s signature is verified: ' + tokenNotVerified);

// check for expiration
console.log('\ncheck expiration that is valid');
const tokenValid = checkExpiration(jwsSignedTokenReceived);
console.log('the token is valid and not expired: ' + tokenValid);
console.log('\ncheck expiration that is invalid');
const jwsSignedTokenReceivedExpired = `eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2phdmEtY3J5cHRvLmdpdGh1Yi5pby9jcm9zc19wbGF0Zm9ybV9jcnlwdG8vIiwic3ViIjoiSldUIFJTMjU2IHNpZ25hdHVyZSIsImlhdCI6MTYxMzQ3NDQ4MSwiZXhwIjoxNjEzNDc0NDgzfQ.pWw0U6nMwTikn1WImAi8phL6kxrVqwpyNAH7u1DVL26DyBbmtEFLTqAU3szrrVbB6vZirZx8fvJTMRaze3ExGZOzEgH4sUQkKz3HdUymz2LKWWpzlucRCB5CCMm5uVRf_f6MNsfAw3Fx41DryXU7BBoV3fp8Qt8rzdCPwabHSlCyNTC69AI46OmS25IUXzBpuhxa8-M9hh97YnSNwOYOfow3evJDNa9dUsO3ZLgUNTPQuxct3f0Bv95zmWzoMJYY-YsOSnBncSnac1t0GlhcwQCceata1fBrBqIc3a-r7hhrFmwv3Bam0LdCYTiqH5UU83osCIabFb3qlnQcRdLO0Q`;
const tokenValidExpired = checkExpiration(jwsSignedTokenReceivedExpired);
console.log('the token is valid and not expired: ' + tokenValidExpired);

// print payload
console.log('\nprint payload');
const payloadObject = printPayloadObject(jwsSignedTokenReceived);
console.log('payloadObject: ' + payloadObject);

// print header
console.log('\nprint header');
const headerObject = printHeaderObject(jwsSignedTokenReceived);
console.log('headerObject: ')
console.log(headerObject);

// print signature
console.log('\nprint signature');
const signatureObject = printSignatureObject(jwsSignedTokenReceived);
console.log('signatureObject: ')
console.log(signatureObject);
