package Libsodium_Cryptobox_Encryption_String;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class LibsodiumCryptoboxEncryptionString {
    // uses TweetNacl https://github.com/InstantWebP2P/tweetnacl-java
    public static void main(String[] args) {
        System.out.println("Libsodium crypto box hybrid string encryption");

        String plaintext = "The quick brown fox jumps over the lazy dog";
        System.out.println("plaintext: " + plaintext);

        // encryption
        System.out.println("\n* * * encryption * * *");
        System.out.println("all data are in Base64 encoding");
        // for encryption you need your private key (privateKeyA) and the public key from other party (publicKeyB)
        String privateKeyABase64 = "yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU=";
        String publicKeyBBase64 =  "jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=";
        // convert the base64 encoded keys
        byte[] privateKeyA = base64Decoding(privateKeyABase64);
        byte[] publicKeyB = base64Decoding(publicKeyBBase64);
        String ciphertextBase64 = cryptoboxEncryptionToBase64(privateKeyA, publicKeyB, plaintext);
        System.out.println("privateKeyA: " + privateKeyABase64);
        System.out.println("publicKeyB:  " + publicKeyBBase64);
        System.out.println("ciphertext:  " + ciphertextBase64);
        System.out.println("output is  (Base64) nonce : (Base64) ciphertext");

        System.out.println("\n* * * decryption * * *");
        // received ciphertext
        String ciphertextReceivedBase64 = ciphertextBase64;
        // for decryption you need your private key (privateKeyB) and the public key from other party (publicKeyA)
        String privateKeyBBase64 = "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=";
        String publicKeyABase64 =  "b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0=";
        // convert the base64 encoded keys
        byte[] privateKeyB = base64Decoding(privateKeyBBase64);
        byte[] publicKeyA = base64Decoding(publicKeyABase64);
        String decryptedtext = cryptoboxDecryptionFromBase64(privateKeyB, publicKeyA, ciphertextReceivedBase64);
        System.out.println("ciphertext:  " + ciphertextReceivedBase64);
        System.out.println("input is (Base64) nonce : (Base64) ciphertext");
        System.out.println("privateKeyB: " + privateKeyBBase64);
        System.out.println("publicKeyA:  " + publicKeyABase64);
        System.out.println("decrypt.text:" + decryptedtext);
    }

    private static String cryptoboxEncryptionToBase64(byte[] privateKey, byte[] publicKey, String plaintext) {
        byte[] data = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] nonce = generateRandomNonce();
        TweetNaclFast.Box box = new TweetNaclFast.Box(publicKey, privateKey);
        return base64Encoding(nonce) + ":" + base64Encoding(box.box(data, nonce));
    }

    private static String cryptoboxDecryptionFromBase64(byte[] privateKey, byte[] publicKey, String ciphertextBase64) {
        String[] parts = ciphertextBase64.split(":", 0);
        byte[] nonce = base64Decoding(parts[0]);
        byte[] ciphertext = base64Decoding(parts[1]);
        TweetNaclFast.Box box = new TweetNaclFast.Box(publicKey, privateKey);
        return new String(box.open(ciphertext, nonce), StandardCharsets.UTF_8);
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

