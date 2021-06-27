package AES_CBC_256_Pbkdf2_Hmac_String;

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
import java.util.Arrays;
import java.util.Base64;

public class AesCbc256Pbkdf2HmacStringEncryption {
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeySpecException {
        System.out.println("AES CBC 256 String encryption with PBKDF2 derived key and HMAC check");

        String plaintext = "The quick brown fox jumps over the lazy dog";
        System.out.println("plaintext:  " + plaintext);
        char[] password = "secret password".toCharArray();

        // encryption
        System.out.println("\n* * * Encryption * * *");
        String ciphertextHmacBase64 = aesCbcPbkdf2HmacEncryptToBase64(password, plaintext);
        System.out.println("ciphertextHmac (Base64): " + ciphertextHmacBase64);
        System.out.println("output is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac");

        // decryption
        System.out.println("\n* * * Decryption * * *");
        String ciphertextHmacDecryptionBase64 = ciphertextHmacBase64;
        System.out.println("ciphertextHmacDecryption (Base64): " + ciphertextHmacDecryptionBase64);
        System.out.println("input is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac");
        String decryptedtextHmac = aesCbcPbkdf2HmacDecryptFromBase64(password, ciphertextHmacDecryptionBase64);
        System.out.println("plaintext:  " + decryptedtextHmac);
    }

    private static String aesCbcPbkdf2HmacEncryptToBase64(char[] password, String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        int PBKDF2_ITERATIONS = 15000;
        byte[] salt = generateSalt32Byte();
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySpec = new PBEKeySpec(password, salt, PBKDF2_ITERATIONS, 64 * 8); // 2 * 32 for aes + hmac
        byte[] keyAesHmac = secretKeyFactory.generateSecret(keySpec).getEncoded();
        byte[] keyAes = new byte[32];
        byte[] keyHmac = new byte[32];
        System.arraycopy(keyAesHmac, 0, keyAes, 0, 32); // copy aes key
        System.arraycopy(keyAesHmac, 32, keyHmac, 0, 32); // copy hmac key
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyAes, "AES");
        byte[] iv = generateRandomInitvector();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        String ciphertextBase64 = base64Encoding(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        String saltBase64 = base64Encoding(salt);
        String ivBase64 = base64Encoding(iv);
        // calculate hmac over salt, iv and ciphertext
        String ciphertextWithoutHmac = saltBase64 + ":" + ivBase64 + ":" + ciphertextBase64;
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(keyHmac, "HmacSHA256");
        sha256_HMAC.init(secret_key);
        String hmacBase64 = base64Encoding((sha256_HMAC.doFinal(ciphertextWithoutHmac.getBytes(StandardCharsets.UTF_8))));
        return saltBase64 + ":" + ivBase64 + ":" + ciphertextBase64 + ":" + hmacBase64;
    }

    private static String aesCbcPbkdf2HmacDecryptFromBase64(char[] password, String data) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeySpecException {
        String[] parts = data.split(":", 0);
        byte[] salt = base64Decoding(parts[0]);
        byte[] iv = base64Decoding(parts[1]);
        byte[] encryptedData = base64Decoding(parts[2]);
        byte[] hmac = base64Decoding(parts[3]);
        int PBKDF2_ITERATIONS = 15000;
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySpec = new PBEKeySpec(password, salt, PBKDF2_ITERATIONS, 64 * 8); // 2 * 32 for aes + hmac
        byte[] keyAesHmac = secretKeyFactory.generateSecret(keySpec).getEncoded();
        byte[] keyAes = new byte[32];
        byte[] keyHmac = new byte[32];
        System.arraycopy(keyAesHmac, 0, keyAes, 0, 32); // copy aes key
        System.arraycopy(keyAesHmac, 32, keyHmac, 0, 32); // copy hmac key
        // before we decrypt we have to check the hmac
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(keyHmac, "HmacSHA256");
        sha256_HMAC.init(secret_key);
        // calculate the hmac over salt, iv and cyphertext
        String hmacToCheck = base64Encoding(salt) + ":" + base64Encoding(iv) + ":" + base64Encoding(encryptedData);
        byte[] hmacCalculated = sha256_HMAC.doFinal(hmacToCheck.getBytes(StandardCharsets.UTF_8));
        if (Arrays.equals(hmac, hmacCalculated) == false) {
            System.out.println("Error: HMAC-check failed, no decryption possible");
            return "";
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyAes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return new String(cipher.doFinal(encryptedData));
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

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }
}
