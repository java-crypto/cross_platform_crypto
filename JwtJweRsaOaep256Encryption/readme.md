# Cross-platform cryptography

## JSON web encryption JWE with algorithms RSA-OAEP-256 and A256GCM

This article is about a **hybrid encryption scheme** using an **AES 256 GCM** encryption for the payload and a **RSA encryption** for the AES encryption key.

As you will see in some implementations (e.g. in [NodeJs](../JwtJweRsaOaep256Encryption/JweRsaOaep256EncryptionNodeJsCrypto.js)) there is no "magic" in doing that as all necessary cryptographic routines are covered by several of my articles and are well known - it's only a clever combining of all. On the other hand there are JWT libraries that do all the heavy part and leave you with just some lines of code (e.g. [Python with Authlib library](../JwtJweRsaOaep256Encryption/JweRsaOaep256Encryption.py)).

### What key and encryption algorithms are provided with the programs?

Using a JWE encryption scheme needs to define <u>two</u> algorithms. 

The first one is the **key encryption algorithm** (that is given with the "alg" tag, see below). My programs will use the **RSA-OAEP-256** algorithm and you can easily change that to the two other algorithms **RSA-OAEP** or **RSA PKCS#1.5**, but you should note that both are marked as "deprecated" in some JWT libraries and should be no longer used for newer programs. 

The second algorithm is the **payload (content) encryption algorithm** (given by the "enc" tag, see below). I'm using **A256GCM** as preset meaning an AES 256 GCM mode encryption. You can change this to **A128GCM** or **A192GCM** very simple. 

### Are the other encryption algorithms available?

Yes, as well for the key encryption as for the payload encryption other algorithms are available but are not supported by my programs (I'm focusing on RSA keys and AES GCM mode).

### What is the structure of a JWE encrypted web token?

As the structure of an **encrypted JWT token** looks like similar to a **signed JWT token** I recommend to read the article [JSON web token (JWT) structure](json_web_token_structure.md) to get a good overview.

This is how a JWE token looks like:

```plaintext
eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAtMjU2In0.RhEKG1RrABc63reHe3dgUZXAngQBiWIU4c6WJrtxy9JINTD9mbCFC3AmOqGPmNzxDqeETF90saL5qgpLzP28Mzc6J9cxREIYqvlAfkwiiHuB9W1IhjewfTfl8isI4RPMlevZNmHeOXEfIctw0JrFFponFCDS5rrQi_wrO8atTolA-PZWcuf5mIm_V7beM3atSLs_x_HKq2Elxx1ArnP9UnlJ254QSVH1BP_nK8Dj_bElpphLMoavhFzd95i2sXGVssFT6LvUZKQ72nT3i4QfeW6-m1rvZJ3aYnlSSrfK4AvctjQE1EdnVeLSm7oaP0bG71dwfok-G8RCZEGaKW9r3g.52PmTilsPD1VygOP.Xnlbh7q_vLuhCXY4YIPPoK1dz6fFYkHA_C9tT6gp_V21LCZbpHQ13DAad81F8mos0ARP4NpI2ewe5d6FrbTfUYHJu--jCExtW_lT27nGwsNizFxd2jxbW8g-MvWSrK9jV_cQEcZcaIdYhqfqWl_m5Pq3NTyNctSnAveJsqFtcx9uHPRZNoS5YdbQHlXmgRRtY4Dz5h2QCXPtrBFHQLlvHd59CvdLvCT6phkLb0WHF6cSB74TMO81VhCiFz3fjuIQlZw.D9Al-f1ScXJnZCNI_KLRhQ
```

Let's split it into the following 5 parts:

Part 1: the Base64Url encoded **JWT header**:
```plaintext
eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAtMjU2In0
decoded: 
{"enc":"A256GCM","alg":"RSA-OAEP-256"}
```

Part 2: the **encryption key** for the payload after encryption with RSA-OAEP-256 and Base64Url encoding:
```plaintext
RhEKG1RrABc63reHe3dgUZXAngQBiWIU4c6WJrtxy9JINTD9mbCFC3AmOqGPmNzxDqeETF90saL5qgpLzP28Mzc6J9cxREIYqvlAfkwiiHuB9W1IhjewfTfl8isI4RPMlevZNmHeOXEfIctw0JrFFponFCDS5rrQi_wrO8atTolA-PZWcuf5mIm_V7beM3atSLs_x_HKq2Elxx1ArnP9UnlJ254QSVH1BP_nK8Dj_bElpphLMoavhFzd95i2sXGVssFT6LvUZKQ72nT3i4QfeW6-m1rvZJ3aYnlSSrfK4AvctjQE1EdnVeLSm7oaP0bG71dwfok-G8RCZEGaKW9r3g
```

Part 3: the Base64Url encoded **nonce**:
```plaintext
52PmTilsPD1VygOP
value in hex encoding: e763e64e296c3c3d55ca038f
```

Part 4: the Base64Url encoded **ciphertext** (the encrypted payload):
```plaintext
Xnlbh7q_vLuhCXY4YIPPoK1dz6fFYkHA_C9tT6gp_V21LCZbpHQ13DAad81F8mos0ARP4NpI2ewe5d6FrbTfUYHJu--jCExtW_lT27nGwsNizFxd2jxbW8g-MvWSrK9jV_cQEcZcaIdYhqfqWl_m5Pq3NTyNctSnAveJsqFtcx9uHPRZNoS5YdbQHlXmgRRtY4Dz5h2QCXPtrBFHQLlvHd59CvdLvCT6phkLb0WHF6cSB74TMO81VhCiFz3fjuIQlZw
```

Part 5: the Base64Url encoded **GCM tag**:
```plaintext
D9Al-f1ScXJnZCNI_KLRhQ
value in hex encoding: 0fd025f9Fd52717267642348fca2d185
```

### What are the next steps when receiving these data?

Well, it's not a complicated workflow that is described here:

1. decode the JWT header and read the "alg" and "enc" tags
2. decrypt the data in part 2 (that is the RSA encrypted AES-key and do not forget to decode the Base64Url encoded data) with the algorithm in the "alg" tag using a matching RSA private key [in our example we are using "RSA OAEP 256"]
3. now we do have the **encryption key** and we can decrypt the encrypted payload (see part 4) with the algorithm in the "enc" tag [in our example we used AES 256 GCM]; to get the correct result we need to take the **nonce** and the **gcm tag** as input for our decryption function
4. running the decryption function will fail as we forgot to give an additional parameter to the decryption function, and that's the (Base64Url encoded) JWT Header as **additional authenticated data** or in short **AAD**
5. now the decryption function gives the correct result (for a better reading I formatted the output):

```plaintext
{
 "iss":"https:\/\/java-crypto.github.io\/cross_platform_crypto\/",
 "sub":"JWE RSA-OAEP & AES GCM encryption",
 "exp":1614244772,
 "iat":1614244652,
 "dat":"The quick brown fox jumps over the lazy dog"
}
```

The opposite steps are needed to be done for encryption and - why do we sometimes use an external library for this task? The answer is: "*because the library is there*" and makes the life easier :-).


## :warning: Security warning :warning:

**This is a serious warning regarding the security of the programs shown in these article series.  Always keep in mind my disclaimer regarding my programs: All programs are for educational purposes and are not intended to use in production or any other programs where a  secure solution is needed. The programs do not have proper exceptional/error handling and in some cases they use insecure key lengths or other methods that are insecure. Never ever use the programs in real life unless checked by a qualified professional cryptographer.**

The following links provide the solutions in code and an online compiler that runs the code.

| Language | available | Alg | Enc | Online-compiler
| ------ | :---: | :----: | :---: | :----: |
| [Java](../JwtJweRsaOaep256Encryption/JweRsaOaep256Encryption.java) | :white_check_mark: | RSA-OAEP-256, RSA-OAEP, RSA PKCS1.5 | A128GCM, A192GCM, A256GCM | [repl.it CpcJavaJweRsaOaep256Encryption](https://repl.it/@javacrypto/CpcJavaJweRsaOaep256Encryption#Main.java/) ***)** |
| [PHP](../JwtJweRsaOaep256Encryption/JweRsaOaep256Encryption.php) | :white_check_mark: |  RSA-OAEP-256, RSA-OAEP, RSA PKCS1.5 | A128GCM, A192GCM, A256GCM | [repl.it CpcPhpJweRsaOaep256Encryption](https://repl.it/@javacrypto/CpcPhpJweRsaOaep256Encryption#main.php/) |
| [C#](../JwtJweRsaOaep256Encryption/JweRsaOaep256Encryption.cs) | :white_check_mark: | RSA-OAEP-256, RSA-OAEP, RSA PKCS1.5 | A128GCM, A192GCM, A256GCM | [dotnetfiddle.net CpcCsharpJweRsaOaep256Encryption](https://dotnetfiddle.net/Z7fwAQ/) |
| Javascript CryptoJs | :x: | | | kindly use the pure nodeJs implementation |
| [NodeJS Crypto](../JwtJweRsaOaep256Encryption/JweRsaOaep256EncryptionNodeJsCrypto.js) | :white_check_mark: | RSA-OAEP-256, RSA-OAEP, RSA PKCS1.5 | A128GCM, A192GCM, A256GCM | [repl.it CpcNodeJsCryptoJweRsaOaep256Encryption](https://repl.it/@javacrypto/CpcNodeJsCryptoJweRsaOaep256Encryption#index.js/) |
| NodeJS forge | :x: | | | kindly use the pure nodeJs implementation |
| [Webcrypto encryption only](../JwtJweRsaOaep256Encryption/jwersaoaep256encryption.html) | :white_check_mark: | RSA-OAEP-256 | A256GCM | [your browser WebcryptoJweRsaOaep-256Encryption.html](https://java-crypto.github.io/cross_platform_crypto/JwtJweRsaOaep256Encryption/jwersaoaep256encryption.html) |
| [Webcrypto decryption only](../JwtJweRsaOaep256Encryption/jwersaoaep256decryption.html) | :white_check_mark: | RSA-OAEP-256 | A256GCM | [your browser WebcryptoJweRsaOaep-256Decryption.html](https://java-crypto.github.io/cross_platform_crypto/JwtJweRsaOaep256Encryption/jwersaoaep256decryption.html) |
| [Python](../JwtJweRsaOaep256Encryption/JweRsaOaep256Encryption.py) | :white_check_mark: | RSA-OAEP-256, RSA-OAEP, RSA PKCS1.5 | A128GCM, A192GCM, A256GCM | [repl.it CpcPythonJweRsaOaep256Encryption](https://repl.it/@javacrypto/CpcPythonJweRsaOaep256Encryption#main.py/) ***)** |

***)** kindly note that the online compiler has to rebuild all libraries and that takes some seconds before the programs is ready to run.

This in a complete output of the Java program (the output will differ when comparing the frameworks):
```plaintext
JWT JWE RSA-OAEP-256 AES GCM 256 encryption
jwe key algorithm:        RSA-OAEP-256
jwe encryption algorithm: A256GCM
jwtPayloadObject: {"sub":"JWE RSA-OAEP & AES GCM encryption","dat":"The quick brown fox jumps over the lazy dog","iss":"https:\/\/java-crypto.github.io\/cross_platform_crypto\/","exp":1614244772,"iat":1614244652}

* * * encrypt the payload with recipient's public key * * *
rsaPublicKey in JWK format:
{"kty":"RSA","e":"AQAB","n":"8EmWJUZ_Osz4vXtUU2S-0M4BP9-s423gjMjoX-qP1iCnlcRcFWxthQGN2CWSMZwR_vY9V0un_nsIxhZSWOH9iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG_CfT_QQl7R-kO_EnTmL3QjLKQNV_HhEbHS2_44x7PPoHqSqkOvl8GW0qtL39gTLWgAe801_w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLBishaq7Jm8NPPNK9QcEQ3q-ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb_LJ2aW2gQw"}
jweToken (Base64Url encoded):
eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAtMjU2In0.RhEKG1RrABc63reHe3dgUZXAngQBiWIU4c6WJrtxy9JINTD9mbCFC3AmOqGPmNzxDqeETF90saL5qgpLzP28Mzc6J9cxREIYqvlAfkwiiHuB9W1IhjewfTfl8isI4RPMlevZNmHeOXEfIctw0JrFFponFCDS5rrQi_wrO8atTolA-PZWcuf5mIm_V7beM3atSLs_x_HKq2Elxx1ArnP9UnlJ254QSVH1BP_nK8Dj_bElpphLMoavhFzd95i2sXGVssFT6LvUZKQ72nT3i4QfeW6-m1rvZJ3aYnlSSrfK4AvctjQE1EdnVeLSm7oaP0bG71dwfok-G8RCZEGaKW9r3g.52PmTilsPD1VygOP.Xnlbh7q_vLuhCXY4YIPPoK1dz6fFYkHA_C9tT6gp_V21LCZbpHQ13DAad81F8mos0ARP4NpI2ewe5d6FrbTfUYHJu--jCExtW_lT27nGwsNizFxd2jxbW8g-MvWSrK9jV_cQEcZcaIdYhqfqWl_m5Pq3NTyNctSnAveJsqFtcx9uHPRZNoS5YdbQHlXmgRRtY4Dz5h2QCXPtrBFHQLlvHd59CvdLvCT6phkLb0WHF6cSB74TMO81VhCiFz3fjuIQlZw.D9Al-f1ScXJnZCNI_KLRhQ

* * * decrypt the payload with recipient's private key * * *
rsaPrivateKey in JWK format:
{"p":"_8atV5DmNxFrxF1PODDjdJPNb9pzNrDF03TiFBZWS4Q-2JazyLGjZzhg5Vv9RJ7VcIjPAbMy2Cy5BUffEFE-8ryKVWfdpPxpPYOwHCJSw4Bqqdj0Pmp_xw928ebrnUoCzdkUqYYpRWx0T7YVRoA9RiBfQiVHhuJBSDPYJPoP34k","kty":"RSA","q":"8H9wLE5L8raUn4NYYRuUVMa-1k4Q1N3XBixm5cccc_Ja4LVvrnWqmFOmfFgpVd8BcTGaPSsqfA4j_oEQp7tmjZqggVFqiM2mJ2YEv18cY_5kiDUVYR7VWSkpqVOkgiX3lK3UkIngnVMGGFnoIBlfBFF9uo02rZpC5o5zebaDIms","d":"hXGYfOMFzXX_vds8HYQZpISDlSF3NmbTCdyZkIsHjndcGoSOTyeEOxV93MggxIRUSjAeKNjPVzikyr2ixdHbp4fAKnjsAjvcfnOOjBp09WW4QCi3_GCfUh0w39uhRGZKPjiqIj8NzBitN06LaoYD6MPg_CtSXiezGIlFn_Hs-MuEzNFu8PFDj9DhOFhfCgQaIgEEr-IHdnl5HuUVrwTnIBrEzZA_08Q0Gv86qQZctZWoD9hPGzeAC-RSMyGVJw6Ls8zBFf0eysB4spsu4LUom_WnZMdS1ls4eqsAX-7AdqPKBRuUVpr8FNyRM3s8pJUiGns6KFsPThtJGuH6c6KVwQ","e":"AQAB","qi":"BtiIiTnpBkd6hkqJnHLh6JxBLSxUopFvbhlR37Thw1JN94i65dmtgnjwluvR_OMgzcR8e8uCH2sBn5od78vzgiDXsqITF76rJgeO639ILTA4MO3Mz-O2umrJhrkmgSk8hpRKA-5Mf9aE7dwOzHrc8hbj8J102zyYJIE6pOehrGE","dp":"BPXecL9Pp6u_0kwY-DcCgkVHi67J4zqka4htxgP04nwLF_o8PF0tlRfj0S7qh4UpEIimsxq9lrGvWOne6psYxG5hpGxiQQvgIqBGLxV_U2lPKEIb4oYAOmUTYnefBCrmSQW3v93pOP50dwNKAFcGWTDRiB_e9j-3EmZm_7iVzDk","dq":"rBWkAC_uLDf01Ma5AJMpahfkCZhGdupdp68x2YzFkTmDSXLJ_P15GhIQ-Lxkp2swrvwdL1OpzKaZnsxfTIXNddmEq8PEBSuRjnNzRjQaLnqjGMtTBvF3G5tWkjClb_MW2q4fgWUG8cusetQqQn2k_YQKAOh2jXXqFOstOZQc9Q0","n":"8EmWJUZ_Osz4vXtUU2S-0M4BP9-s423gjMjoX-qP1iCnlcRcFWxthQGN2CWSMZwR_vY9V0un_nsIxhZSWOH9iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG_CfT_QQl7R-kO_EnTmL3QjLKQNV_HhEbHS2_44x7PPoHqSqkOvl8GW0qtL39gTLWgAe801_w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLBishaq7Jm8NPPNK9QcEQ3q-ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb_LJ2aW2gQw"}
jweDecryptedPayload: {"sub":"JWE RSA-OAEP & AES GCM encryption","dat":"The quick brown fox jumps over the lazy dog","iss":"https:\/\/java-crypto.github.io\/cross_platform_crypto\/","exp":1614244772,"iat":1614244652}

example for checking token expiration
Expiration Time: Thu Feb 25 09:19:32 UTC 2021
Issue Time:      Thu Feb 25 09:17:32 UTC 2021
actual Time:     Thu Feb 25 09:17:37 UTC 2021
jweToken valid:  true

example for printing the header object
{"enc":"A256GCM","alg":"RSA-OAEP-256"}

```

Last update: Mar. 02nd 2021

Back to the main page: [readme.md](../readme.md) or back to the [JSON web token overview](json_web_token_overview.md) page.
