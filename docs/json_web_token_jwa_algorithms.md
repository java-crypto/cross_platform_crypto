# Cross-platform cryptography

## JSON web token JWA algorithms

This page gives you an overview about the different standardized algorithms when using a JSON web token (JWT).

For a complete overview with much more explanations I recommend you to read the original docs - [JSON Web Algorithms (JWA)](https://tools.ietf.org/html/rfc7518). 

In the following tables "CPC solution" says if I'm providing a solution in one of my articles.

### Algorithms for JWS JSON web signature

| short name | long name | CPC solution |  
| ------ | :--: | :--: |
| HS256 | HMAC using SHA-256 | :x: |
| HS384 | HMAC using SHA-384 | :x: |
| HS512 | HMAC using SHA-512 | :x: |
| RS256 | RSASSA-PKCS1-v1_5 using SHA-256 | :white_check_mark: |
| RS384 | RSASSA-PKCS1-v1_5 using SHA-384 | :x: |
| RS512 | RSASSA-PKCS1-v1_5 using SHA-512 | :x: |
| ES256 | ECDSA using P-256 and SHA-256 | :x: |
| ES384 | ECDSA using P-384 and SHA-384 | :x: |
| ES512 | ECDSA using P-521 and SHA-512 | :x: |
| PS256 | RSASSA-PSS using SHA-256 and MGF1 with SHA-256 | :white_check_mark: |
| PS384 | RSASSA-PSS using SHA-384 and MGF1 with SHA-384 | :x: |
| PS512 | RSASSA-PSS using SHA-512 and MGF1 with SHA-512 | :x: |
| none  | No digital signature or MAC   | :x: |

### Algorithms for JWE JSON web encryption

#### "alg" (Algorithm) Header Parameter Values for JWE

| short name | long name | CPC solution | 
| ------ | :--: | :--: |
| RSA1_5      | RSAES-PKCS1-v1_5 | :white_check_mark: |
| RSA-OAEP | RSAES OAEP using default parameters | :x: |
| RSA-OAEP-256 | RSAES OAEP using SHA-256 and MGF1 with SHA-256 | :white_check_mark: |
| ... | much more algorithms | :x: |

#### "enc" (Encryption Algorithm) Header Parameter Values for JWE
| short name | long name | CPC solution | 
| ------ | :--: | :--: |
| A128CBC-HS256 | AES_128_CBC_HMAC_SHA_256 | :x: |
| A192CBC-HS384 | AES_192_CBC_HMAC_SHA_384 | :x: |
| A256CBC-HS512 | AES_256_CBC_HMAC_SHA_512 | :x: |
| A128GCM       | AES GCM using 128-bit key | :x: |
| A192GCM       | AES GCM using 192-bit key | :x: |
| A256GCM       | AES GCM using 256-bit key | :white_check_mark: |

 
Last update: Feb. 22nd 2021

Back to the main page: [readme.md](../readme.md) or back to the [JSON web token overview](json_web_token_overview.md) page.