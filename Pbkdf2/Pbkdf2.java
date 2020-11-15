package PBKDF2;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

public class Pbkdf2 {
    public static void main(String[] args) throws Exception {
        System.out.println("Generate a 32 byte long AES key with PBKDF2");

        // get the password as char array
        char[] passwordChar = "secret password".toCharArray();
        final int PBKDF2_ITERATIONS = 15000; // number of iterations, higher is better but slower
        // ### security warning - never use a fixed salt in production, this is for compare reasons only
        byte[] salt = generateFixedSalt32Byte();
        // please use below generateSalt32Byte()
        //byte[] salt = generateSalt32Byte();
        byte[] aesKeySha512 = generateAes256KeyPbkdf2Sha512(passwordChar, PBKDF2_ITERATIONS, salt);
        System.out.println("aesKeySha512 length: " + aesKeySha512.length + " data: " + bytesToHex(aesKeySha512));
        byte[] aesKeySha256 = generateAes256KeyPbkdf2Sha256(passwordChar, PBKDF2_ITERATIONS, salt);
        System.out.println("aesKeySha256 length: " + aesKeySha256.length + " data: " + bytesToHex(aesKeySha256));
        byte[] aesKeySha1 = generateAes256KeyPbkdf2Sha1(passwordChar, PBKDF2_ITERATIONS, salt);
        System.out.println("aesKeySha1   length: " + aesKeySha1.length + " data: " + bytesToHex(aesKeySha1));
    }

    public static byte[] generateAes256KeyPbkdf2Sha512(char[] password, int iterations, byte[] salt) throws Exception {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        KeySpec keySpec = new PBEKeySpec(password, salt, iterations, 32 * 8);
        return secretKeyFactory.generateSecret(keySpec).getEncoded();
    }

    public static byte[] generateAes256KeyPbkdf2Sha256(char[] password, int iterations, byte[] salt) throws Exception {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySpec = new PBEKeySpec(password, salt, iterations, 32 * 8);
        return secretKeyFactory.generateSecret(keySpec).getEncoded();
    }

    public static byte[] generateAes256KeyPbkdf2Sha1(char[] password, int iterations, byte[] salt) throws Exception {
        if (iterations < 15000) iterations = 15000; // minimum number of iterations
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(password, salt, iterations, 32 * 8);
        return secretKeyFactory.generateSecret(keySpec).getEncoded();
    }

    private static byte[] generateSalt32Byte() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[32];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private static byte[] generateFixedSalt32Byte() {
        // ### security warning - never use this in production ###
        byte[] salt = new byte[32]; // 32 x0's
        return salt;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
