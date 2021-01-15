# Cross-platform cryptography

## curve X25519 sample key pair

When working with private-public-key cryptography you always need a key pair for the specific algorithm. Below you find 2 key pairs for the **X25519 curve** that is a modern one and is often used with programs based on the  [**Libsodium**](libsodium_overview.md) library.

The curve X25519 is used for **key exchange mechanisms** or short **KEM**. Each party has it's own key pair so below you will find 2 pairs. Usual names for these parties are "Alice" and "Bob" but I will shorten it to "A" and "B" key pair.

Each key is only 32 bytes long and for better (cross platform wide) usage encoded in base64 so the string is a little bit longer. The "private key" is often called "secret key" when used in libsodium.

### Key pair for party A

```plaintext
party A X25519 private (secret) key in Base64 encoding:
yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU=
```

```plaintext
party A X25519 public key in Base64 encoding:
b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0=
```

### Key pair for party B

```plaintext
party B X25519 private (secret) key in Base64 encoding:
yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=
```

```plaintext
party B X25519 public key in Base64 encoding:
jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=
```

These key pairs are used with the programs [Curve 25519 key exchange & AES CBC mode 256 string encryption](curve25519_key_exchange_aes_cbc_256_string_encryption.md), [Libsodium secret box authenticated string hyrid encryption](libsodium_secretbox_encryption_string.md) and [Libsodium sealed box authenticated string hybrid encryption](libsodium_sealedbox_encryption_string.md)

If you want to know how this key pair was created with OpenSSL visit the page [Generate Curve 25519 keys](docs/curve25519_key_generation.md).

Last update: Jan. 14th 2021

Back to the main page: [readme.md](../readme.md)