# Cross-platform cryptography

## JSON web token JWK keys

At the moment I'm providing on this page just a sample JWK key pair. This key pair is "just" the JWK representation of the [sample RSA key pair](rsa_sample_keypair.md) used in other articles.

The keys are usually printed in one line, for better reading I put each element in a different line.


RSA private key (2048 bit key length) in JWK-encoding:
```plaintext
{
"p":"_8atV5DmNxFrxF1PODDjdJPNb9pzNrDF03TiFBZWS4Q-2JazyLGjZzhg5Vv9RJ7VcIjPAbMy2Cy5BUffEFE-8ryKVWfdpPxpPYOwHCJSw4Bqqdj0Pmp_xw928ebrnUoCzdkUqYYpRWx0T7YVRoA9RiBfQiVHhuJBSDPYJPoP34k",
"kty":"RSA",
"q":"8H9wLE5L8raUn4NYYRuUVMa-1k4Q1N3XBixm5cccc_Ja4LVvrnWqmFOmfFgpVd8BcTGaPSsqfA4j_oEQp7tmjZqggVFqiM2mJ2YEv18cY_5kiDUVYR7VWSkpqVOkgiX3lK3UkIngnVMGGFnoIBlfBFF9uo02rZpC5o5zebaDIms",
"d":"hXGYfOMFzXX_vds8HYQZpISDlSF3NmbTCdyZkIsHjndcGoSOTyeEOxV93MggxIRUSjAeKNjPVzikyr2ixdHbp4fAKnjsAjvcfnOOjBp09WW4QCi3_GCfUh0w39uhRGZKPjiqIj8NzBitN06LaoYD6MPg_CtSXiezGIlFn_Hs-MuEzNFu8PFDj9DhOFhfCgQaIgEEr-IHdnl5HuUVrwTnIBrEzZA_08Q0Gv86qQZctZWoD9hPGzeAC-RSMyGVJw6Ls8zBFf0eysB4spsu4LUom_WnZMdS1ls4eqsAX-7AdqPKBRuUVpr8FNyRM3s8pJUiGns6KFsPThtJGuH6c6KVwQ",
"e":"AQAB",
"qi":"BtiIiTnpBkd6hkqJnHLh6JxBLSxUopFvbhlR37Thw1JN94i65dmtgnjwluvR_OMgzcR8e8uCH2sBn5od78vzgiDXsqITF76rJgeO639ILTA4MO3Mz-O2umrJhrkmgSk8hpRKA-5Mf9aE7dwOzHrc8hbj8J102zyYJIE6pOehrGE",
"dp":"BPXecL9Pp6u_0kwY-DcCgkVHi67J4zqka4htxgP04nwLF_o8PF0tlRfj0S7qh4UpEIimsxq9lrGvWOne6psYxG5hpGxiQQvgIqBGLxV_U2lPKEIb4oYAOmUTYnefBCrmSQW3v93pOP50dwNKAFcGWTDRiB_e9j-3EmZm_7iVzDk",
"dq":"rBWkAC_uLDf01Ma5AJMpahfkCZhGdupdp68x2YzFkTmDSXLJ_P15GhIQ-Lxkp2swrvwdL1OpzKaZnsxfTIXNddmEq8PEBSuRjnNzRjQaLnqjGMtTBvF3G5tWkjClb_MW2q4fgWUG8cusetQqQn2k_YQKAOh2jXXqFOstOZQc9Q0",
"n":"8EmWJUZ_Osz4vXtUU2S-0M4BP9-s423gjMjoX-qP1iCnlcRcFWxthQGN2CWSMZwR_vY9V0un_nsIxhZSWOH9iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG_CfT_QQl7R-kO_EnTmL3QjLKQNV_HhEbHS2_44x7PPoHqSqkOvl8GW0qtL39gTLWgAe801_w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLBishaq7Jm8NPPNK9QcEQ3q-ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb_LJ2aW2gQw"
}
```

RSA public key (2048 bit key length) in JWK-encoding:
```plaintext
{
 "kty":"RSA",
 "e":"AQAB",
 "n":"8EmWJUZ_Osz4vXtUU2S-0M4BP9-s423gjMjoX-qP1iCnlcRcFWxthQGN2CWSMZwR_vY9V0un_nsIxhZSWOH9iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG_CfT_QQl7R-kO_EnTmL3QjLKQNV_HhEbHS2_44x7PPoHqSqkOvl8GW0qtL39gTLWgAe801_w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLBishaq7Jm8NPPNK9QcEQ3q-ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb_LJ2aW2gQw"
}

```

In this encoding it is easy to see that a RSA private key contains all the information of a public key plus some more parameter.

Last update: Feb. 21st 2021

Back to the main page: [readme.md](../readme.md) or back to the [JSON web token overview](json_web_token_overview.md) page.