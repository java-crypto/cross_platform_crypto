package AES_CBC_256_Pbkdf2_String;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AesCbc256Pbkdf2StringEncryption_Full {
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeySpecException {
        System.out.println("AES CBC 256 String encryption with PBKDF2 derived key");

        String plaintext = "The quick brown fox jumps over the lazy dog";
        System.out.println("plaintext:  " + plaintext);
        char[] password = "secret password".toCharArray();

        // encryption
        System.out.println("\n* * * Encryption * * *");
        String ciphertextBase64 = aesCbcPbkdf2EncryptToBase64(password, plaintext);
        System.out.println("ciphertext (Base64): " + ciphertextBase64);
        System.out.println("output is (Base64) salt : (Base64) iv : (Base64) ciphertext");

        // decryption
        System.out.println("\n* * * Decryption * * *");
        System.out.println("AES CBC 256 String decryption with PBKDF2 derived key");
        String ciphertextDecryptionBase64 = ciphertextBase64;
        System.out.println("ciphertext (Base64): " + ciphertextDecryptionBase64);
        System.out.println("input is (Base64) salt : (Base64) iv : (Base64) ciphertext");
        String decryptedtext = aesCbcPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64);
        System.out.println("plaintext:  " + decryptedtext);
    }

    private static byte[] generateSalt32Byte() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[32];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private static byte[] generateRandomInitvector() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        return iv;
    }

    private static String aesCbcPbkdf2EncryptToBase64(char[] password, String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        int PBKDF2_ITERATIONS = 15000;
        byte[] salt = generateSalt32Byte();
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySpec = new PBEKeySpec(password, salt, PBKDF2_ITERATIONS, 32 * 8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), "AES");
        byte[] iv = generateRandomInitvector();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        String ciphertextBase64 = base64Encoding(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        String saltBase64 = base64Encoding(salt);
        String ivBase64 = base64Encoding(iv);
        return saltBase64 + ":" + ivBase64 + ":" + ciphertextBase64;
    }

    private static String aesCbcPbkdf2DecryptFromBase64(char[] password, String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        String[] parts = data.split(":", 0);
        byte[] salt = base64Decoding(parts[0]);
        byte[] iv = base64Decoding(parts[1]);
        byte[] encryptedData = base64Decoding(parts[2]);
        int PBKDF2_ITERATIONS = 15000;
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySpec = new PBEKeySpec(password, salt, PBKDF2_ITERATIONS, 32 * 8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return new String(cipher.doFinal(encryptedData));
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }
}

