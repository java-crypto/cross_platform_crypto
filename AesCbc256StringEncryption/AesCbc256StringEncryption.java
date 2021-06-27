package AES_CBC_256_String;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AesCbc256StringEncryption {
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        System.out.println("AES CBC 256 String encryption with random key full");

        String plaintext = "The quick brown fox jumps over the lazy dog";
        System.out.println("plaintext:  " + plaintext);

        // generate random key
        byte[] encryptionKey = generateRandomAesKey();
        String encryptionKeyBase64 = base64Encoding(encryptionKey);
        System.out.println("encryptionKey (Base64): " + encryptionKeyBase64);

        // encryption
        System.out.println("\n* * * Encryption * * *");
        String ciphertextBase64 = aesCbcEncryptToBase64(encryptionKey, plaintext);
        System.out.println("ciphertext: " + ciphertextBase64);
        System.out.println("output is (Base64) iv : (Base64) ciphertext");

        // decryption
        System.out.println("\n* * * Decryption * * *");
        String decryptionKeyBase64Node = "27YHPLw76nkx3s7G6AOSNAeNW7xOR/snCIAUVt24DBM=";
        String decryptionKeyBase64 = encryptionKeyBase64; // full
        String ciphertextDecryptionBase64 = ciphertextBase64;
        System.out.println("decryptionKey (Base64): " + decryptionKeyBase64);
        byte[] decryptionKey = base64Decoding(decryptionKeyBase64);
        System.out.println("ciphertext (Base64): " + ciphertextDecryptionBase64);
        System.out.println("input is (Base64) iv : (Base64) ciphertext");
        String decryptedtext = aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64);
        System.out.println("plaintext:  " + decryptedtext);
    }

    private static byte[] generateRandomAesKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);
        return key;
    }

    private static byte[] generateRandomInitvector() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        return iv;
    }

    private static String aesCbcEncryptToBase64(byte[] key, String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] iv = generateRandomInitvector();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        String ciphertextBase64 = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        String ivBase64 = Base64.getEncoder().encodeToString(iv);
        return ivBase64 + ":" + ciphertextBase64;
    }

    private static String aesCbcDecryptFromBase64(byte[] key, String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String[] parts = data.split(":", 0);
        byte[] iv = base64Decoding(parts[0]);
        byte[] encryptedData = base64Decoding(parts[1]);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
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

