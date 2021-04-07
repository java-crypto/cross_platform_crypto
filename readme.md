# Cross-platform cryptography

## General information and overview

This is a series of cryptography articles that will show show how cryptography is done between different programming platforms. The sample programs do not attempt to be "secure" but explain how the crypto routines are compatible with other platforms.

I'm trying to serve a broad spectrum of programming languages and actually I have solutions for these  frameworks: **Java**, **PHP**, **C#**, **Javascript - CryptoJs**, **NodeJs Crypto**,  **NodeJs node-forge**, **Python** (for selected programs) and **Golang** (for selected programs)  as long the functionality is available on the platform. You can test all programs with online compilers to see what the output is and what happens when you change some parameters. For selected assignments there is a **WebCrypto** solution available, that will run in your (modern) browser. For very rare programs I provide an [**OpenSSL**](#readme-openssl) solution.

Wouldn't it be nice having a library that could be used cross-platform wide to get cryptographic tasks done? Yes and the library is still there, it is the [**Libsodium**](#readme-libsodium) project and there are a lot of bindings available for all major frameworks. The algorithms in Libsodium differ from the other ones used in the cross-platform-project, therefore the Libsodium solutions are in a own section. 

Sometimes you may have read about a "JWT" or "JWT token", that is short form of a **JSON web token** or **JWT**. That are standardized signature and encryption schemes for a defined data exchange between different systems and frameworks. I provide some [**JWT web token**](#readme-jwt) solutions.

A special chapter deals with the future of cryptography; and that is the **Post Quantum Cryptography**. Although the quantum computers are not on the market at the moment they will be there and cause catastrophic impacts on cryptography as they may been able to solve the RSA- and EC-curve problems and subsequently all RSA- and EC-curve encryption and signature become worthless. Visit the chapter [**PQC Post Quantum Cryptography**](#readme-pqc) at the end.

I'm providing a special article series about the migration of the (former) standard encryption module **MCRYPT** in PHP, that was available until version 5.6.x of PHP - newer versions of PHP come with OpenSSL as standard cryptography module. I'm giving examples how to migrate the algorithms **Rijndael-128** (better known as "AES"), **Rijndael-256**, **DES** and **Triple DES** using the modes **ECB**, **CBC** and **CTR** and the **zero padding** or **PKCS#7 padding**. Visit the chapter [**MCRYPT migration**](#readme-mcrypt) at the end.

A lot of solutions run with the built-in cryptographic modules but especially the Python and  Javascript-ones may need external libraries (pure Javascript will need e.g. "CryptoJs", NodeJs has a built-in "Crypto" library or you can use "node-forge").

### General routines ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS | NodeJs forge | WebCrypto | Python | Go |
| ------ | :---: | :----: | :---: | :--: | :--: | :--: | :--: | :--: | :--: | 
| [generate a 32 byte long AES key](docs/generate_aes_key.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: |
| [generate a 16 byte long initvector](docs/generate_initvector.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: |
| [generate a 12 byte long nonce](docs/generate_nonce.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: |
| [Base64 encoding & decoding](docs/base64_encoding_decoding.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [binary data to a hex string & back](docs/binary_data_hex_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [SHA256 hashing](docs/sha256_hash.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [HMAC 256 calculation](docs/hmac_256_calculation.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: |
| [PBKDF2 key derivation](docs/pbkdf2.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: |
| [Generate RSA keys](docs/rsa_key_generation.md) | [see explanation](docs/rsa_key_generation.md) |
[Generate Curve 25519 keys](docs/curve25519_key_generation.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| [Generate Elliptic keys](docs/ec_key_generation.md) | [see explanation](docs/ec_key_generation.md) |

### AES string encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |NodeJs forge | WebCrypto | Python | Go |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: | :--: | :--: | --: |
| [CBC-mode String Encryption](docs/aes_cbc_256_string_encryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: | :white_check_mark: |
| [CBC-mode String Decryption only](docs/aes_cbc_256_string_decryption_only.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :x: | :x: |
| [CBC-mode PBKDF2 String Encryption](docs/aes_cbc_256_pbkdf2_string_encryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | 
| [CBC-mode PBKDF2 String Decryption only](docs/aes_cbc_256_pbkdf2_string_decryption_only.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:| :white_check_mark: | :x: | :x: | :x: |
| [CBC-mode PBKDF2 HMAC String Encryption](docs/aes_cbc_256_pbkdf2_hmac_string_encryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:| :white_check_mark: | :x: | :white_check_mark: |
| [CBC-mode passphrase String Encryption](docs/aes_cbc_256_passphrase_string_encryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:| :white_check_mark: | :x: | :x: |
| [GCM-mode String Encryption](docs/aes_gcm_256_string_encryption.md) | :white_check_mark:| :white_check_mark: | :white_check_mark: | :x: | :white_check_mark:| :white_check_mark: | :x: | :white_check_mark: | :white_check_mark: |
| [GCM-mode PBKDF2 String Encryption](docs/aes_gcm_256_pbkdf2_string_encryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [GCM-mode PBKDF2 AAD String Encryption](readme.md) | :soon: | :soon: | :soon: | :soon: | :soon: | :soon: | :x: |  :soon: | :soon: |

#### AES CBC special: tampering
| Solution | Java | Explanation |
| ------ | :------: | :----: |
| [AES CBC-mode tampering](docs/aes_cbc_tampering.md) | :white_check_mark: | in Java as it is for demonstration only |

### ChaCha20-Poly1305 encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |NodeJs forge | WebCrypto | Python | Go |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: |  :--: | :--: | :--: |  
| [ChaCha20-Poly1305 String encryption with random key](docs/chacha20_poly1305_string_encryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark:| :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: |


### RSA encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |NodeJs forge | WebCrypto | Python | Go |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: |  :--: | :--: | :--: |
| [RSA OEAP padding SHA-1 String](docs/rsa_encryption_oaep_sha1_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [RSA OEAP padding SHA-1 String decryption only](docs/rsa_decryption_oaep_sha1_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark:| :white_check_mark: | :white_check_mark: | |  |
| [RSA OEAP padding SHA-256 String](docs/rsa_encryption_oaep_sha256_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |


### RSA signature with different padding modes ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS Crypto | NodeJs forge | WebCrypto | OpenSSL | Python | Go |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: | :--: | :--: | :--: | :--: |
| [RSA String Signature PKSC#1.5 (full)](docs/rsa_signature_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [RSA String Signature PKSC#1.5 Verification only](docs/rsa_signature_string_verification_only.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark:| :white_check_mark: | :white_check_mark: |  |  | |
| [RSA String Signature PSS (full)](docs/rsa_signature_pss_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |

### RSA keys ###

Working with RSA key pairs can get tricky sometimes so I'm providing some more information about RSA keys: 

| page | information |
| ------ | :------: |
[How to generate RSA key pairs](docs/rsa_key_generation.md) | creation of RSA key pairs with OpenSSL |
[How to convert RSA keys](docs/rsa_key_conversion.md) | convert keys between XML- and PEM-format |
[RSA sample keys](docs/rsa_sample_keypair.md) | for my encryption and signature examples I used these keys |
[RSA key formats](docs/rsa_key_formats.md) | explanation of the most used key formats |

### RSA AES hybrid encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS Crypto | NodeJs forge | Browser | Python | Go |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: | :--: | :--: | :--: |
| [RSA AES CBC 256 hybrid encryption](docs/rsa_aes_hybrid_encryption_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: | :white_check_mark: |
| [RSA AES GCM 256 hybrid encryption](docs/rsa_aes_gcm_hybrid_encryption_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |

### Curve 25519 key exchange ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS | NodeJs forge | Python |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: | :--: |
[Curve 25519 key exchange & AES CBC mode 256 string encryption](docs/curve25519_key_exchange_aes_cbc_256_string_encryption.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: |

Note: you need external libraries for this feature!

### Elliptic curve keys ###

| page | information |
| ------ | :------: |
[EC key generation](docs/ec_key_generation.md) | creation of EC key pairs with OpenSSL
[EC sample keys](docs/ec_sample_keypair.md) | sample key pair for curve SECP256R1 = P-256 = PRIME256V1

### Elliptic curve signature ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS Crypto | NodeJs forge | WebCrypto | OpenSSL | Python | Go |
| ------ | :------: | :----: | :---: | :--: | :--: | :--: | :--: | :--: | :--: | :--: |
| [ECDSA String Signature IEEE-P1363 encoding ](docs/ecdsa_signature_ieee_p1363_string.md) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: | :white_check_mark: | :x:| :white_check_mark: | :x:| :white_check_mark: | :white_check_mark: |
| [ECDSA String Signature DER encoding ](docs/ecdsa_signature_der_string.md) | :white_check_mark: | :white_check_mark: | :x: | :x: | :white_check_mark: | :x:| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [ECDSA signature converter DER <- -> IEEE P1363 encoding ](docs/ecdsa_signature_conversion.md) | :white_check_mark: | :white_check_mark: | :x: | :x: | :white_check_mark: | :x: | :white_check_mark: | :x:| :x:|


### <a name="readme-libsodium"></a>Libsodium solutions
| Solution | Description | Java | PHP | C# | NodeJS | Browser | Python | Go |
| ------ | ------ | :--: | :--: | :--: | :--: | :--: | :--: | :--: |
|[Libsodium overview](docs/libsodium_overview.md) | general information about Libsodium ("NaCl", "Sodium") cryptography |
[generate a random key](docs/libsodium_secretbox_encryption_string.md) | see the examples in  [authenticated string encryption with a random key](docs/libsodium_secretbox_encryption_string.md) | :white_check_mark: | :white_check_mark: |  :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |  
[generate a curve 25519 key pair](docs/curve25519_key_generation.md) | generate a curve X25519 private key and public key pair in Base64 encoding | :white_check_mark: | :white_check_mark: |  :white_check_mark: | :white_check_mark: | :x: |  
[derive the curve X25519 public key from a secret key](docs/x25519_public_key_generation.md) | generate the X25519 public key from a secret key in Base64 encoding | :soon: | :soon: |  :soon: | :soon: | :white_check_mark: |  
[generate a curve ED25519 key pair](docs/generate_ed25519_keypair.md) | generate a curve ED25519 private key and public key pair in Base64 encoding | :white_check_mark: | :white_check_mark: |  :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |   
[generate a curve ED25519 public key from a private key](docs/generate_ed25519_keypair.md) | generate an ED25519 public key from a private key in Base64 encoding | :white_check_mark: |  :white_check_mark: |  :white_check_mark: |  :white_check_mark: |  :white_check_mark: |  
| [Argon 2 password derivation function](docs/argon2.md) | derives a password from a passphrase using the "modern" Argon2id algorithm | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|[Argon 2 parameter](docs/argon2_parameter.md) | describes the parameters used in Argon 2 algorithm | 
| [ChaCha20-Poly1305 String encryption with random key](docs/chacha20_poly1305_string_encryption.md) | encrypts a string on basis of a  **randomly generated encryption key**, using the **ChaCha20 cipher** for encryption and the **Poly1305 MAC** for authentication | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:| :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [Libsodium authenticated string encryption with a random key (secret box)](docs/libsodium_secretbox_encryption_string.md) | encrypts a string on basis of a **randomly generated encryption key**, using the **XSalsa20 stream cipher** for encryption and the **Poly1305 MAC** for authentication | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|[Libsodium crypto box authenticated hybrid string encryption](docs/libsodium_cryptobox_encryption_string.md) | encrypts a string on basis of a **X25519 curve** key exchange, using the **XSalsa20 stream cipher** for encryption and the **Poly1305 MAC** for authentication | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [Libsodium sealed box authenticated hybrid string encryption](docs/libsodium_sealedcryptobox_encryption_string.md) | encrypts a string on basis of a **X25519 curve** key exchange, using the **XSalsa20 stream cipher** for encryption and the **Poly1305 MAC** for authentication but without key exchange between the two partners |  :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
[Libsodium string signature (detached) with a private-public key](docs/libsodium_signature_detached_string.md) | uses an ED25519 curve signature | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |  :white_check_mark: | :white_check_mark: | :white_check_mark: |

### <a name="readme-openssl"></a>OpenSSL solutions ###
| Solution | Description |
| ------ | ------ |
[OpenSSL overview](docs/openssl_overview.md) | general information about OpenSSL cryptography |
[Generate a RSA key pair](docs/rsa_key_generation.md) | generate a RSA private key and public key in PEM encoding
[Generate an EC key pair](docs/ec_key_generation.md) | generate an Elliptic curve (EC) private key and public key in PEM encoding
[RSA file signature with PKCS#1.5 padding](docs/rsa_signature_file_openssl.md) |   the signature and verification is compatible with solutions in  [RSA string signature with PKCS#1.5 padding](docs/rsa_signature_string.md)|
[RSA file signature with PSS padding](docs/rsa_signature_pss_file_openssl.md) |   the signature and verification is compatible with solutions in  [RSA string signature with PSS padding](rsa_signature_pss_string.md)|
[Elliptic curve file signature DER encoding](docs/ecdsa_signature_file_openssl.md) | the signature and verification is compatible with solutions in [ECDSA String Signature DER encoding ](docs/ecdsa_signature_der_string.md) |

### <a name="readme-jwt"></a>JWT JSON web token solutions ###
| Solution | Description | Java | PHP | C# | NodeJS | Browser | Python |
| ------ | ------ | :--: | :--: | :--: | :--: | :--: | :--: |
|[JSON web token (JWT) overview](docs/json_web_token_overview.md) | general information about JSON web token | | | | | | |
|[structure of a JSON web token (JWT)](docs/json_web_token_structure.md) | explains the general structure of a JWT | | | | | | |
|JSON web token JWA algorithms | standardized algorithms for JWT | | | | | | |
|[JSON Web JWK keys](docs/json_web_token_jwk_keys.md) | standardized key format for JWT | | | | | | |
|[JSON web signature (JWS) using RS256 algorithm](docs/json_web_token_jws_rs256_signature.md) | sign a JWT with a RSA key, PKCS1.5 padding and SHA-256 hashing | :white_check_mark: |  :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |   :white_check_mark: | 
|[JSON web signature (JWS) using PS256 algorithm](docs/json_web_token_jws_ps256_signature.md) | sign a JWT with a RSA private key, RSASSA-PSS + MGF1 with SHA-256 and SHA-256 hashing |  :white_check_mark: |  :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |   :white_check_mark: | 
|JSON web signature (JWS) using RSxxx & PSxxx algorithms (verify only) | verify a JWT with a RSA public key, PKCS1.5 & SSA-PSS padding and SHA-256/384/512 hashing | :x: |  :x: | :x: | :x: | :soon: |   :x: | 
| [JSON web encryption (JWE) using RSA-OAEP-256 with A256GCM algorithm](docs/json_web_token_jwe_rsa_oaep_256_encryption.md) | encrypt a JWT with a RSA key RSA-OAEP-256 and AES-256-GCM algorithm | :white_check_mark: |  :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |   :white_check_mark: | 

### <a name="readme-pqc"></a>PQC Post Quantum Cryptography ###

| Purpose | algorithm | Language |
| ------ | :------: | :--: |
| [PQC overview](docs/pqc_overview.md) |  | several |
| [algorithms facts overview](docs/pqc_overview_algorithm_facts.md) |  | several |
| [PQC with Liboqs library overview](docs/pqc_liboqs_overview.md)| general overview about the OpenQuantumSafe library | 
| [liboqs algorithms facts overview](docs/pqc_overview_liboqs_algorithm_facts.md) | information about key, ciphertext & signature sizes | Java binding | 
| | |
| Public key encryption | [McEliece](docs/pqc_mceliece_encryption.md) | Java |
| Public key encryption | [NTRU](docs/pqc_ntru_encryption.md) | Java |
| | | |
| key exchange (KEM) | [Chrystals-Kyber](docs/pqc_chrystalskyber_kem.md) | NodeJs |
| key exchange (KEM) | [Sike](docs/pqc_sike_kem.md) | Java |
| key exchange (KEM) | [Sike with Liboqs](docs/pqc_sike_liboqs_kem.md) | Java |
| | | |
| signature | [FALCON](docs/pqc_falcon_signature.md) | Python |
| signature | [FALCON with Liboqs](docs/pqc_falcon_liboqs_signature.md) | Java binding |
| signature | [Rainbow](docs/pqc_rainbow_signature.md) | Java |
| signature | [Sphincs](docs/pqc_sphincs_signature.md) | Java |

### <a name="readme-mcrypt"></a>MCRYPT migration ###

| Solution | mode | PHP OpenSSL | PHP phpseclib 3 | 
| ------ | :------: | :--: | :--: | 
| [MCRYPT migration overview](docs/mcrypt_overview.md) | | | |
| | | | |
|[MCRYPT Rijndael-128 ECB](docs/mcrypt_rijndael128.md) | ECB mode | :white_check_mark: | :white_check_mark: |
|[MCRYPT Rijndael-128 CBC](docs/mcrypt_rijndael128.md) | CBC mode | :white_check_mark: | :white_check_mark: |
|[MCRYPT Rijndael-128 CTR](docs/mcrypt_rijndael128.md) | CTR mode | :white_check_mark: | :white_check_mark: |
| | | | |
|[MCRYPT Rijndael-256 ECB](docs/mcrypt_rijndael256.md) | ECB mode | :x: | :white_check_mark: |
|[MCRYPT Rijndael-256 CBC](docs/mcrypt_rijndael256.md) | CBC mode | :x: | :white_check_mark: |
|[MCRYPT Rijndael-256 CTR](docs/mcrypt_rijndael256.md) | CTR mode | :x: | :white_check_mark: |
| | | | |
|[MCRYPT DES ECB](docs/mcrypt_des.md) | ECB mode | :white_check_mark: | :white_check_mark: |
|[MCRYPT DES CBC](docs/mcrypt_des.md) | CBC mode | :white_check_mark: | :white_check_mark: |
|[MCRYPT DES CTR](docs/mcrypt_des.md) | CTR mode | :x: | :white_check_mark: |
| | | | |
|[MCRYPT TDES ECB](docs/mcrypt_tdes.md) | ECB mode | :white_check_mark: | :white_check_mark: |
|[MCRYPT TDES CBC](docs/mcrypt_tdes.md) | CBC mode | :white_check_mark: | :white_check_mark: |
|[MCRYPT TDES CTR](docs/mcrypt_tdes.md) | CTR mode | :x: | :white_check_mark: |


### Elliptic curve encryption ###

| Solution | Java | PHP | C# | CryptoJS | NodeJS |
| ------ | :------: | :----: | :---: | :--: | :--: |

not planned in future

### Which platforms are supported at the moment?

I'm using an IDE for Java and PHP but for the other ones I'm using online compiler like [https://repl.it/](https://repl.it/). Please don't ask if the programs will run on your system with a lower or higher version - just try it on your own.

A lot of solutions run with the built-in cryptographic modules but especially the Javascript-ones may need external libraries (pure Javascript will need e.g. "CryptoJs", NodeJs has a built-in "Crypto" or you can use "node-forge").

The few Javascript Webcrypto examples will run directly in your browser so you will need an actual one and Javascript is enabled.

| Language |  Online-compiler | framework version
| ------ | :---: | ---- |
| C# |  repl.it | Mono C# compiler version 6.8.0.123
| C# |  dotnetfiddle.net | .NET 4.7.2
| C# |  dotnetfiddle.net | .NET 5
| C# |  Bouncy Castle | version 1.89
| Java |  repl.it | OpenJDK Runtime Environment (build 11.0.6+10-post-Ubuntu-1ubuntu118.04.1)
| Javascript CryptoJs |  repl.it | node v12.16.1, crypto-js version 4.0.0
| NodeJS Crypto |  repl.it | node v12.16.1, openssl 1.1.1g
| NodeJS node-forge |  repl.it | node v12.16.1, node-forge version 0.10.0 
| OpenSSL |  | version 1.1.1g Windows x64
| PHP |  repl.it | PHP CLI 7.2.17-0ubuntu0.18.04.1 (cli) (built: Apr 18 2019 14:12:38)
| PHP phpseclib |   | version 3
| Python | repl.it | version 3.8.2 (default, Feb 26 2020, 02:56:10)
| Webcrypto | modern browser | tested in Firefox 83 (x64), Google Chrome 84.0.4147.135 (x86), Opera 70.0.3728.119
| Go | repl.it | version go 1.14 linux/amd64
| Go | goplay.space | version go 1.16.2 

Last update: Apr. 07th 2021

The website is published under:  [https://java-crypto.github.io/cross_platform_crypto//](https://java-crypto.github.io/cross_platform_crypto//)

If you are interested in much more information on cryptography in Java (and in German language) then visit my webpages [http://javacrypto.bplaced.net](http://javacrypto.bplaced.net/) and [http://java-crypto.bplaced.net](http://java-crypto.bplaced.net/).
