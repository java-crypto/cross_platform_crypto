package Libsodium_Secretbox_Encrypt_String;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class LibsodiumSecretboxEncryptionString {
    // uses TweetNacl https://github.com/InstantWebP2P/tweetnacl-java
    public static void main(String[] args) {
        System.out.println("Libsodium secret box random key string encryption");

        String plaintext = "The quick brown fox jumps over the lazy dog";
        System.out.println("plaintext: " + plaintext);

        // generate random key
        String keyBase64 = base64Encoding(generateRandomKey());
        System.out.println("encryptionKey (Base64): " + keyBase64);

        // encryption
        System.out.println("\n* * * encryption * * *");
        System.out.println("all data are in Base64 encoding");
        String ciphertextBase64 = secretboxEncryptionToBase64(keyBase64, plaintext);
        System.out.println("ciphertext:  " + ciphertextBase64);
        System.out.println("output is (Base64) nonce : (Base64) ciphertext");

        System.out.println("\n* * * decryption * * *");
        // received ciphertext
        String ciphertextReceivedBase64 = ciphertextBase64;
        String decryptedtext = secretboxDecryptionFromBase64(keyBase64, ciphertextReceivedBase64);
        System.out.println("ciphertext:  " + ciphertextReceivedBase64);
        System.out.println("input is (Base64) nonce : (Base64) ciphertext");
        System.out.println("decrypt.text:" + decryptedtext);
    }

    private static String secretboxEncryptionToBase64(String keyBase64, String plaintext){
        byte[] nonce = generateRandomNonce();
        TweetNaclFast.SecretBox sbox = new TweetNaclFast.SecretBox(base64Decoding(keyBase64));
        byte[] ciphertext = sbox.box(plaintext.getBytes(StandardCharsets.UTF_8), nonce);
        //decryption: message = sbox.open(cipher, nonce);
        return base64Encoding(nonce) + ":" + base64Encoding(ciphertext);
    }

    private static String secretboxDecryptionFromBase64(String keyBase64, String ciphertextBase64){
        String[] parts = ciphertextBase64.split(":", 0);
        byte[] nonce = base64Decoding(parts[0]);
        byte[] ciphertext = base64Decoding(parts[1]);
        TweetNaclFast.SecretBox sbox = new TweetNaclFast.SecretBox(base64Decoding(keyBase64));
        return new String(sbox.open(ciphertext, nonce));
    }

    private static byte[] generateRandomKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);
        return key;
    }

    private static byte[] generateRandomNonce() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] nonce = new byte[24];
        secureRandom.nextBytes(nonce);
        return nonce;
    }

    private static String base64Encoding(byte[] input) {return Base64.getEncoder().encodeToString(input);
    }
    private static byte[] base64Decoding(String input) {return Base64.getDecoder().decode(input);
    }
}
