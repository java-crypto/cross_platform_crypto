# Cross-platform cryptography

## JSON web signature JWS with algorithm RS256 [RSA with PKCS#1.5 padding and SHA-256 hashing]

The standard signature algorithm is **RSA** using a (RSA) Private-/ Public key pair. The <u>signature</u> is generated with the **Private key** of the signer. For the <u>verification</u> the **Public Key** of the signer is used - so in a typical environment the Public Key is provided to the recipient(s) of the signed data and the recipient is been able to check = verify the signature.


!!!!!!!!!!!!!!! not ready !!!!!!!!!!

To get this to work in cross platform systems there are three parameters that have to been equal - that is the hash algorithm that is used. My programs are using **SHA256** for this task.

The second important data are the **keys** itself - it's easy not to recognize that "- Begin Private Key -" isn't the same as "- Begin RSA Private Key - ". Using the wrong keys will cause the the verification to fail.

The third parameter is hidden in most implementations, it is the **padding**. The programs in this example use the **PKCS#1.5-padding** that is available platform wide but **not as secure as modern ones**.

### Why is the PKCS#1.5 padding not so secure?

An important parameter in cryptography is **randomness** - this prevents from easy textbook attacks. For a digital signature it means: the signature of the the same string and same (private) key results in the same value. Running my codes will always result in a (Base64 encoded) string beginning with "vCSB4744p30..", so an attacker may been able to "see" that a previous used signature is in use again.

### What would be a (more) secure padding?

A modern padding is the use of **PSS padding** (Probabilistic Signature Scheme) but this is not available "out of the box" and it will take some time to publish a solution here.

### Key generation: 

All examples use pre-generated keys that are described on the page [RSA sample keys](rsa_sample_keypair.md). If you want to see how my keys got generated visit the page [RSA key generation](rsa_key_generation.md). 

When comparing the programs you will notice that the keys for C# looking like different as they are not in the "PEM"-format ("---Begin...") but in a XML-format. As it is a little bit tricky to convert the keys between XML- and PEM-format I setup an own page for this point: [rsa_key_conversion.md](rsa_key_conversion.md)

One note about the **key management** in my programs: usually the keys are (securely) stored in files or a key store and may have additional password protection. Both scenarios are unhandy in demonstration programs running in an online compiler. That's why I'm using <u>static, hard-coded</u> keys in my programs - **please do not do this in production environment! Never ever store a Private Key in the source!** The minimum is to load the keys from a secured device.

### steps in the program

The program follows the usual sequence:
1. generate a RSA key pair - here we are using a static, hard-coded key pair in form of a PEM encoded string (Java, PHP, CryptoJs and NodeJs) or a XML-file (C#)
2. convert the data to sign into a binary format (e.g. a byte array)
3. start the signature process
4. load the Private Key
5. set the signature parameters
6. sign the "data to sign" and show the result ("signature") in Base64 encoding
7. start the verification process
8. load the Public Key
9. Base64 decoding of the signature
10. set the verification parameters (same as used for signing)
11. verify the signature against the "data to sign" and show the result.

If you like to see the **verification part only** see my separate article [RSA signature string verification only](rsa_signature_string_verification_only.md).

## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compile that runs the code.

| Language | available | Online-compiler
| ------ | :---: | :----: |
| [Java](../JwtJwsRs256Signature/JwsRs256Signature.java) *) | :white_check_mark: | [repl.it CpcJavaJwsRs256Signature](https://repl.it/@javacrypto/CpcJavaJwsRs256Signature#Main.java/) |
| [PHP](../JwtJwsRs256Signature/JwsRs256Signature.php) *) | :white_check_mark: | [repl.it CpcPhpJwsRs256Signature](https://repl.it/@javacrypto/CpcJPhpJwsRs256Signature#main.php/) |
| [C#](../JwtJwsRs256Signature/JwsRs256Signature.cs) | :white_check_mark: | [dotnetfiddle.net CpcCsharpJwsRs256Signature](https://dotnetfiddle.net/rGnvVi/) |
| Javascript CryptoJs | :x: | kindly use the pure nodeJs implementation |
| [NodeJS Crypto](../JwtJwsRs256Signature/JwsRs256SignatureNodeJs.js) *) | :white_check_mark: | [repl.it CpcNodeJsJwsRs256Signature](CpcNodeJsJwsRs256Signature https://repl.it/@javacrypto/CpcNodeJsJwsRs256Signature#index.js/)
| NodeJS forge | :x: | kindly use the pure nodeJs implementation |
| [Webcrypto sign only](../JwtJwsRs256Signature/jwsrs256signaturessign.html) | :white_check_mark: | [your browser WebcryptoJwsRs256Sign.html](https://java-crypto.github.io/cross_platform_crypto/JwtJwsRs256Signature/jwsrs256signaturessign.html)
| [Webcrypto verify only](../JwtJwsRs256Signature/jwsrs256signatureverification.html) | :white_check_mark: | [your browser WebcryptoJwsRs256Verify.html](https://java-crypto.github.io/cross_platform_crypto/JwtJwsRs256Signature/jwsrs256signatureverification.html)
| [Python](../JwtJwsRs256Signature/JwsRs256Signature.py) *) | :white_check_mark: | [repl.it CpcPythonRsaStringSignatureFull](https://repl.it/@javacrypto/CpcPythonJwsRs256Signature#main.py/)

*) you need an external library

This is an output (Java version):

```plaintext
JWT JWS RS256 RSA PKCS#1.5 signature with SHA-256

* * * sign the payload object with the RSA private key * * *
jwtClaimsSet: {"sub":"JWT RS256 signature","iss":"https:\/\/java-crypto.github.io\/cross_platform_crypto\/","exp":1613909603,"iat":1613909483}
privateKey in jwk format:
{"p":"_8atV5DmNxFrxF1PODDjdJPNb9pzNrDF03TiFBZWS4Q-2JazyLGjZzhg5Vv9RJ7VcIjPAbMy2Cy5BUffEFE-8ryKVWfdpPxpPYOwHCJSw4Bqqdj0Pmp_xw928ebrnUoCzdkUqYYpRWx0T7YVRoA9RiBfQiVHhuJBSDPYJPoP34k","kty":"RSA","q":"8H9wLE5L8raUn4NYYRuUVMa-1k4Q1N3XBixm5cccc_Ja4LVvrnWqmFOmfFgpVd8BcTGaPSsqfA4j_oEQp7tmjZqggVFqiM2mJ2YEv18cY_5kiDUVYR7VWSkpqVOkgiX3lK3UkIngnVMGGFnoIBlfBFF9uo02rZpC5o5zebaDIms","d":"hXGYfOMFzXX_vds8HYQZpISDlSF3NmbTCdyZkIsHjndcGoSOTyeEOxV93MggxIRUSjAeKNjPVzikyr2ixdHbp4fAKnjsAjvcfnOOjBp09WW4QCi3_GCfUh0w39uhRGZKPjiqIj8NzBitN06LaoYD6MPg_CtSXiezGIlFn_Hs-MuEzNFu8PFDj9DhOFhfCgQaIgEEr-IHdnl5HuUVrwTnIBrEzZA_08Q0Gv86qQZctZWoD9hPGzeAC-RSMyGVJw6Ls8zBFf0eysB4spsu4LUom_WnZMdS1ls4eqsAX-7AdqPKBRuUVpr8FNyRM3s8pJUiGns6KFsPThtJGuH6c6KVwQ","e":"AQAB","qi":"BtiIiTnpBkd6hkqJnHLh6JxBLSxUopFvbhlR37Thw1JN94i65dmtgnjwluvR_OMgzcR8e8uCH2sBn5od78vzgiDXsqITF76rJgeO639ILTA4MO3Mz-O2umrJhrkmgSk8hpRKA-5Mf9aE7dwOzHrc8hbj8J102zyYJIE6pOehrGE","dp":"BPXecL9Pp6u_0kwY-DcCgkVHi67J4zqka4htxgP04nwLF_o8PF0tlRfj0S7qh4UpEIimsxq9lrGvWOne6psYxG5hpGxiQQvgIqBGLxV_U2lPKEIb4oYAOmUTYnefBCrmSQW3v93pOP50dwNKAFcGWTDRiB_e9j-3EmZm_7iVzDk","dq":"rBWkAC_uLDf01Ma5AJMpahfkCZhGdupdp68x2YzFkTmDSXLJ_P15GhIQ-Lxkp2swrvwdL1OpzKaZnsxfTIXNddmEq8PEBSuRjnNzRjQaLnqjGMtTBvF3G5tWkjClb_MW2q4fgWUG8cusetQqQn2k_YQKAOh2jXXqFOstOZQc9Q0","n":"8EmWJUZ_Osz4vXtUU2S-0M4BP9-s423gjMjoX-qP1iCnlcRcFWxthQGN2CWSMZwR_vY9V0un_nsIxhZSWOH9iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG_CfT_QQl7R-kO_EnTmL3QjLKQNV_HhEbHS2_44x7PPoHqSqkOvl8GW0qtL39gTLWgAe801_w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLBishaq7Jm8NPPNK9QcEQ3q-ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb_LJ2aW2gQw"}
signed jwsToken:
eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvamF2YS1jcnlwdG8uZ2l0aHViLmlvXC9jcm9zc19wbGF0Zm9ybV9jcnlwdG9cLyIsInN1YiI6IkpXVCBSUzI1NiBzaWduYXR1cmUiLCJleHAiOjE2MTM5MDk2MDMsImlhdCI6MTYxMzkwOTQ4M30.QUlazkdTZXwkRISb2j48UGzB3A1WOpWuL5oC7GvE28xVQw36oFmSYoAflJ7DWdq2HJqnl6X3R-ag-s9QyXCNGUXE3lZtHgohgfMALsLxfufcVaV7ZnJdkNpshSRy4ZO5Up5tGn0aUb8mKeUWqEketDiMa-Ny8cRyLZL-hWC9HZ7KPSh4_-kqLAvvShG0pTPQCMw0DNsQGsTJEwMiLV2ErXdBCgVg1DJ-x6PDCx7VceKpiPEVdSvvWSCsq9zHs82GZoDLk-rpmTgNnhJ1FwBB0E8sTjDAwTRQT_T6PKjgr2ZG6TU1Y1p23ptwTG22tB3XjS4cFMAXc1EmXmSqqd-Diw

* * * verify the jwsToken with the RSA public key * * *
publicKey in jwk format:
{"kty":"RSA","e":"AQAB","n":"8EmWJUZ_Osz4vXtUU2S-0M4BP9-s423gjMjoX-qP1iCnlcRcFWxthQGN2CWSMZwR_vY9V0un_nsIxhZSWOH9iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG_CfT_QQl7R-kO_EnTmL3QjLKQNV_HhEbHS2_44x7PPoHqSqkOvl8GW0qtL39gTLWgAe801_w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLBishaq7Jm8NPPNK9QcEQ3q-ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb_LJ2aW2gQw"}
payloadObject: {sub=JWT RS256 signature, iss=https://java-crypto.github.io/cross_platform_crypto/, exp=1613909603, iat=1613909483}

check signature with matching public key
the token's signature is verified: true

check signature with not matching public key
the token's signature is verified: false

check expiration that is valid
the token is valid and not expired: true

check expiration that is invalid
the token is valid and not expired: false

```

Last update: Feb. 21st 2021

Back to the main page: [readme.md](../readme.md) or back to the [JSON web token overview](json_web_token_overview.md) page.