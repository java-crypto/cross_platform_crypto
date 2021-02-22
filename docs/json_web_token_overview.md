# Cross-platform cryptography

## JSON web token (JWT) overview

This chapter is about a cryptography encapsulation called **JSON web token** or short **JWT**. The general idea is to hide all the cryptographic stuff and provide a simple to use library for standardized and secure data exchange between servers or server and clients.

As the "token" in the name suggests, the JWT is often used for login systems.

### What is a JWT?

Following the introduction on [jwt.io](https://jwt.io/introduction) a "*JSON Web Token (JWT) is an open standard (RFC 7519) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object. This information can be verified and trusted because it is digitally signed. JWTs can be signed using a secret (with the HMAC algorithm) or a public/private key pair using RSA or ECDSA.*"

### Are there more JSON-based modes?

The simple answer is "yes". The "**JWT**" is the generic term for two systems called **JSON web signature** ("**JWS**") and **JSON web encryption** ("**JWE**"). They get accompanied by a **JSON Web Key** ("JWK"), **JSON Web Algorithms** ("JWA"), **JSON Web Key Thumbprint** and an **Unencoded Payload Option**.

### Where do I get more information about the technical basics?

All JSON web xxx's are based on docs provided by the **Internet Engineering Task Force (IETF)**:

* JWT JSON web token: [JSON Web Token (RFC 7519)](https://tools.ietf.org/html/rfc7519)
* JWS JSON Web Signature: [JWS JSON Web Signature (RFC 7515)](https://tools.ietf.org/html/rfc7515)
* JWE JSON Web Encryption: [JWE JSON Web Encryption (RFC 7516)](http://tools.ietf.org/html/rfc7516)
* JWK JSON Web Key: [JWK JSON Web Key (RFC 7517)](http://tools.ietf.org/html/rfc7517)
* JWA JSON Web Algorithms: [JWA JSON Web Algorithms (RFC 7518)](http://tools.ietf.org/html/rfc7518)
* JSON Web Key Thumbprint: [JSON Web Key Thumbprint (RFC 7638)](https://tools.ietf.org/html/rfc7638)
* Unencoded Payload Option: [Unencoded Payload Option (RFC7797)](https://tools.ietf.org/html/rfc7797)

### Do I need external libraries to execute a JWT?

Again a simple answer: 'no', you do not need to use external libraries but sometimes they make the life much easier. Seeing the basics, a JWS is not much more than a simple RSA string signature and I have proven this by an implementation for a browser based JWT.

### What kind of JWT's are covered in the articles?

I'm starting with the signature based **JWS JSON web signatures** and later I'm programming the **JWE JSON web encryption** examples.

Kindly note that there is not just one way of using but a lot (and different) ones and I'm focusing on a sample token. Although I'm using libraries some codes do have a lot of "handcraft" to get them to run as expected, so you should take the codes just as a basis for your own programming.

To start with your JWT experience I recommend that you read the article [structure of a JSON web token JWT](json_web_token_structure.md) as I'm explaining some basics.

Here are my articles regarding JSON web token (JWT) themes:

| Solution | Description | Java | PHP | C# | NodeJs | Browser | Python |
| ------ | :------: | :--: | :--: | :--: | :--: | :--: | :--: |
|[structure of a JSON web token (JWT)](json_web_token_structure.md) | explains the general structure of a JWT | | | | | | |
|JSON web token JWA algorithms | standardized algorithms for JWT | | | | | | |
|[JSON Web JWK keys](json_web_token_jwk_keys.md) | standardized key format for JWT | | | | | | |
|[JSON web signature (JWS) using RS256 algorithm](json_web_token_jws_rs256_signature.md) | sign a JWT with a RSA key, PKCS1.5 padding and SHA-256 hashing |  :white_check_mark: |  :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |   :white_check_mark: | 
|[JSON web signature (JWS) using PS256 algorithm](json_web_token_jws_rs256_signature.md) | sign a JWT with a RSA private key, RSASSA-PSS + MGF1 with SHA-256 and SHA-256 hashing |  :white_check_mark: |  :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |   :white_check_mark: | 
|[JSON web signature (JWS) using RSxxx & PSxxx algorithms (verify only)](json_web_token_jws_rs_ps_signature.md) | verify a JWT with a RSA public key, PKCS1.5 & SSA-PSS padding and SHA-256/384/512 hashing |  :x: |  :x: | :x: | :x: | :soon: |   :x: | 
| JSON web encryption (JWE) using RSA-OAEP-256 with A256GCM algorithm |  | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |
| JSON web encryption (JWE) |  | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: |


### What additional libraries do I need to get the stuff working?

Below I'm providing the names and download links of all libraries I have used to run the examples. Please note that I did not take a deep care of the license terms of the libraries - please check them before using them in any (commercial or private) context.

| Framework | library | documentation | source and download link
| ------ | :------: | -- | -- | 
| Java | Bouncy Castle version 1.68 |  [https://github.com/bcgit/bc-java](https://github.com/bcgit/bc-java) | GitHub: [https://github.com/bcgit/bc-java](https://github.com/bcgit/bc-java) Maven: [https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on](https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on)
| Java | Bouncy Castle PKIX, CMS, EAC, TSP, PKCS, OCSP, CMP, and CRMF APIs version 1.68 |  [https://github.com/bcgit/bc-java](https://github.com/bcgit/bc-java) | GitHub: [https://github.com/bcgit/bc-java](https://github.com/bcgit/bc-java) Maven: [https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk15on](https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk15on)
| Java | Nimbus JOSE+JWT version 9.4.1 | [https://connect2id.com/products/nimbus-jose-jwt](https://connect2id.com/products/nimbus-jose-jwt) | source: [https://bitbucket.org/connect2id/nimbus-jose-jwt/src/master/](https://bitbucket.org/connect2id/nimbus-jose-jwt/src/master/) Maven: [https://mvnrepository.com/artifact/com.nimbusds/nimbus-jose-jwt](https://mvnrepository.com/artifact/com.nimbusds/nimbus-jose-jwt)
| Java | JCIP Annotations Under Apache License version 1.0.1 | [https://github.com/stephenc/jcip-annotations](https://github.com/stephenc/jcip-annotations) | Maven: [https://mvnrepository.com/artifact/com.github.stephenc.jcip/jcip-annotations](https://mvnrepository.com/artifact/com.github.stephenc.jcip/jcip-annotations)
| PHP | phpseclib version 3 | [https://phpseclib.com/](https://phpseclib.com/) | GitHub:  [https://github.com/phpseclib/phpseclib] (https://github.com/phpseclib/phpseclib) |
| C# | jose-jwt version 3.0.0 |  [https://github.com/dvsekhvalnov/jose-jwt](https://github.com/dvsekhvalnov/jose-jwt) | GitHub: [https://github.com/dvsekhvalnov/jose-jwt](https://github.com/dvsekhvalnov/jose-jwt) NugGet: [https://www.nuget.org/packages/jose-jwt/](https://www.nuget.org/packages/jose-jwt/) 
| NodeJs | node-jws version 4.0 | [https://github.com/brianloveswords/node-jws](https://github.com/brianloveswords/node-jws) | GitHub: [https://github.com/brianloveswords/node-jws](https://github.com/brianloveswords/node-jws) npmjs:  [https://www.npmjs.com/package/jws](https://www.npmjs.com/package/jws) |
| Browser | built-in Webcrypto engine | [Web Crypto API](https://developer.mozilla.org/en-US/docs/Web/API/Web_Crypto_API) | 
| Python | PyJWT version 2.0.1  | [https://pyjwt.readthedocs.io/en/stable/index.html](https://pyjwt.readthedocs.io/en/stable/index.html) | GitHub:  [https://github.com/jpadilla/pyjwt/](https://github.com/jpadilla/pyjwt/) |
| Python | pycryptodome version 3.9.9  | [https://www.pycryptodome.org/](https://www.pycryptodome.org/)  | [https://github.com/Legrandin/pycryptodome](https://github.com/Legrandin/pycryptodome) |


Last update: Feb. 22nd 2021

Back to the main page: [readme.md](../readme.md)
