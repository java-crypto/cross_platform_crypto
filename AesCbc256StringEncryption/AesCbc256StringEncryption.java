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
        //System.out.println("AES CBC 256 String decryption with random key");

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

        //System.out.println("AES CBC 256 String decryption with random key");
        // decryption
        System.out.println("\n* * * Decryption * * *");
        //String decryptionKeyBase64 = "d3+KZq3lsdy5Hgd1VRVQFUhA9jU0PaBHTG2LG2PazQM=";
        String decryptionKeyBase64Node = "8mYqSzbRsxZMmfyEIRyQzfpf26JFqHkKxk7My4yjAyQ=";
        String decryptionKeyBase64 = encryptionKeyBase64; // full
        //String ciphertextDecryptionBase64 = "8GhkycnbzDo5WbRol5zfCQ==:gQrTOJdobbI4fuCHCfu5eqKFYCaw/AM6xMK2o+xCXB0MeAQ7rFbXAbAE3Ex+bD8c";
        String ciphertextDecryptionBase64Node = "UG7Ln+KZiCboinb3qWNkzQ==:uIH+onDF+oDtOjIGVbxgCtLaPJFSZ2R0nbrP4CO5jtWiLbaNi2jwJNzi3FO/B0p3";

        String ciphertextDecryptionBase64 = ciphertextBase64;
        System.out.println("decryptionKey (Base64): " + decryptionKeyBase64);
        byte[] decryptionKey = base64Decoding(decryptionKeyBase64);
        System.out.println("ciphertext (Base64): " + ciphertextDecryptionBase64);
        System.out.println("input is (Base64) iv : (Base64) ciphertext");
        String decryptedtext = aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64);
        System.out.println("plaintext:  " + decryptedtext);
        System.out.println("\nfrom NodeJs:");
        decryptionKey = base64Decoding(decryptionKeyBase64Node);
        decryptedtext = aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64Node);
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

/*
complete run data
AES CBC 256 String encryption with random key full
plaintext:  The quick brown fox jumps over the lazy dog
encryptionKey (Base64): JJu2xyi4sP7lTfxVi8iPvKIIOWiwkgr7spyUEhsnjek=

* * * Encryption * * *
ciphertext: +KOM4ASOY0cMNrM9zV/qvw==:HuoyDiusSplYfd04XSNLOqtcWyOFwmeHNzXS5ywmqRkgAXi8do/6dKppo2U3ZoKl
output is (Base64) iv : (Base64) ciphertext

* * * Decryption * * *
decryptionKey (Base64): JJu2xyi4sP7lTfxVi8iPvKIIOWiwkgr7spyUEhsnjek=
ciphertext (Base64): +KOM4ASOY0cMNrM9zV/qvw==:HuoyDiusSplYfd04XSNLOqtcWyOFwmeHNzXS5ywmqRkgAXi8do/6dKppo2U3ZoKl
input is (Base64) iv : (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog
 */
