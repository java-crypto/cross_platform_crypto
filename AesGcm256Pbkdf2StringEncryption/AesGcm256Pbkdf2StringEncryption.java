package AES_GCM_256_PBKDF2_String_Encryption;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AesGcm256Pbkdf2StringEncryption {
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeySpecException {
        System.out.println("AES GCM 256 String encryption with PBKDF2 derived key");

        String plaintext = "The quick brown fox jumps over the lazy dog";
        System.out.println("plaintext:  " + plaintext);
        char[] password = "secret password".toCharArray();

        // encryption
        System.out.println("\n* * * Encryption * * *");
        String ciphertextBase64 = aesGcmPbkdf2EncryptToBase64(password, plaintext);
        System.out.println("ciphertext (Base64): " + ciphertextBase64);
        System.out.println("output is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag");

        // decryption
        System.out.println("\n* * * Decryption * * *");
        String ciphertextDecryptionBase64 = ciphertextBase64;
        System.out.println("ciphertext (Base64): " + ciphertextDecryptionBase64);
        System.out.println("input is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag");
        String decryptedtext = aesGcmPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64);
        System.out.println("plaintext:  " + decryptedtext);
    }

    private static byte[] generateSalt32Byte() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[32];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private static byte[] generateRandomNonce() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] nonce = new byte[12];
        secureRandom.nextBytes(nonce);
        return nonce;
    }

    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        return ByteBuffer
                .allocate(a.length + b.length)
                .put(a).put(b)
                .array();
    }

    private static String aesGcmPbkdf2EncryptToBase64(char[] password, String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        int PBKDF2_ITERATIONS = 15000;
        byte[] salt = generateSalt32Byte();
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySpec = new PBEKeySpec(password, salt, PBKDF2_ITERATIONS, 32 * 8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), "AES");
        byte[] nonce = generateRandomNonce();
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, nonce);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
        byte[] ciphertextWithTag = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        byte[] ciphertext = new byte[(ciphertextWithTag.length-16)];
        byte[] gcmTag = new byte[16];
        System.arraycopy(ciphertextWithTag, 0, ciphertext, 0, (ciphertextWithTag.length - 16));
        System.arraycopy(ciphertextWithTag, (ciphertextWithTag.length-16), gcmTag, 0, 16);
        String saltBase64 = base64Encoding(salt);
        String nonceBase64 = base64Encoding(nonce);
        String ciphertextBase64 = base64Encoding(ciphertext);
        String gcmTagBase64 = base64Encoding(gcmTag);
        return saltBase64 + ":" + nonceBase64 + ":" + ciphertextBase64 + ":" + gcmTagBase64;
    }

    private static String aesGcmPbkdf2DecryptFromBase64(char[] password, String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        String[] parts = data.split(":", 0);
        byte[] salt = base64Decoding(parts[0]);
        byte[] nonce = base64Decoding(parts[1]);
        byte[] ciphertextWithoutTag = base64Decoding(parts[2]);
        byte[] gcmTag = base64Decoding(parts[3]);
        byte[] encryptedData = concatenateByteArrays(ciphertextWithoutTag, gcmTag);
        int PBKDF2_ITERATIONS = 15000;
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySpec = new PBEKeySpec(password, salt, PBKDF2_ITERATIONS, 32 * 8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, nonce);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
        return new String(cipher.doFinal(encryptedData));
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }
}
