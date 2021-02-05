package Chacha20_Poly1305_String_Encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Chacha20Poly1305StringEncryption {
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        System.out.println("ChaCha20-Poly1305 String encryption with random key full");

        String plaintext = "The quick brown fox jumps over the lazy dog";
        System.out.println("plaintext:  " + plaintext);

        // generate random key
        byte[] encryptionKey = generateRandomAesKey();
        String encryptionKeyBase64 = base64Encoding(encryptionKey);
        System.out.println("encryptionKey (Base64): " + encryptionKeyBase64);

        // encryption
        System.out.println("\n* * * Encryption * * *");
        String ciphertextBase64 = chacha20Poly1305EncryptToBase64(encryptionKey, plaintext);
        System.out.println("ciphertext: " + ciphertextBase64);
        System.out.println("output is (Base64) nonce : (Base64) ciphertext : (Base64) poly1305Tag");

        // decryption
        System.out.println("\n* * * Decryption * * *");
        String decryptionKeyBase64 = encryptionKeyBase64; // full
        String ciphertextDecryptionBase64 = ciphertextBase64;
        System.out.println("decryptionKey (Base64): " + decryptionKeyBase64);
        byte[] decryptionKey = base64Decoding(decryptionKeyBase64);
        System.out.println("ciphertext (Base64): " + ciphertextDecryptionBase64);
        System.out.println("input is (Base64) nonce : (Base64) ciphertext : (Base64) poly1305Tag");
        String decryptedtext = chacha20Poly1305DecryptFromBase64(decryptionKey, ciphertextDecryptionBase64);
        System.out.println("plaintext:  " + decryptedtext);
    }

    private static byte[] generateRandomAesKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);
        return key;
    }

    private static byte[] generateRandomNonce() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] nonce = new byte[12];
        secureRandom.nextBytes(nonce);
        return nonce;
    }

    private static String chacha20Poly1305EncryptToBase64(byte[] key, String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] nonce = generateRandomNonce();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(nonce);
        Cipher cipher = Cipher.getInstance("ChaCha20-Poly1305");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] ciphertextWithTag = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        byte[] ciphertext = new byte[(ciphertextWithTag.length-16)];
        byte[] tag = new byte[16];
        System.arraycopy(ciphertextWithTag, 0, ciphertext, 0, (ciphertextWithTag.length - 16));
        System.arraycopy(ciphertextWithTag, (ciphertextWithTag.length-16), tag, 0, 16);
        String nonceBase64 = base64Encoding(nonce);
        String ciphertextBase64 = base64Encoding(ciphertext);
        String tagBase64 = base64Encoding(tag);
        return nonceBase64 + ":" + ciphertextBase64 + ":" + tagBase64;
    }

    private static String chacha20Poly1305DecryptFromBase64(byte[] key, String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String[] parts = data.split(":", 0);
        byte[] nonce = base64Decoding(parts[0]);
        byte[] ciphertextWithoutTag = base64Decoding(parts[1]);
        byte[] poly1305Tag = base64Decoding(parts[2]);
        byte[] encryptedData = concatenateByteArrays(ciphertextWithoutTag, poly1305Tag);
        Cipher cipher = Cipher.getInstance("ChaCha20-Poly1305");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(nonce);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return new String(cipher.doFinal(encryptedData));
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }

    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        return ByteBuffer
                .allocate(a.length + b.length)
                .put(a).put(b)
                .array();
    }
}


