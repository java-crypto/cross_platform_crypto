package DES_ECB_String;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public class DesEcbStringEncryption {
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        System.out.println("\n# # # SECURITY WARNING: This code is provided for achieve    # # #");
        System.out.println("# # # compatibility between different programming languages. # # #");
        System.out.println("# # # It is not necessarily fully secure.                    # # #");
        System.out.println("# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #");
        System.out.println("# # # It uses FIXED key that should NEVER used,              # # #");
        System.out.println("# # # instead use random generated keys.                     # # #");
        System.out.println("# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n");

        String plaintext = "The quick brown fox jumps over the lazy dog";
        System.out.println("plaintext:  " + plaintext);

        System.out.println("\nDES ECB String encryption with fixed key");

        // fixed encryption key 8 bytes long
        byte[] desEncryptionKey = "12345678".getBytes(StandardCharsets.UTF_8);
        System.out.println("desEncryptionKey (Base64): " + base64Encoding(desEncryptionKey));

        // encryption
        System.out.println("\n* * * Encryption * * *");
        String desCiphertextBase64 = desEcbEncryptToBase64(desEncryptionKey, plaintext);
        System.out.println("ciphertext: " + desCiphertextBase64);
        System.out.println("output is (Base64) ciphertext");
        // decryption
        System.out.println("\n* * * Decryption * * *");
        System.out.println("ciphertext (Base64): " + desCiphertextBase64);
        System.out.println("input is (Base64) ciphertext");
        String decryptedtext = desEcbDecryptFromBase64(desEncryptionKey, desCiphertextBase64);
        System.out.println("plaintext:  " + decryptedtext);

        System.out.println("\nTriple DES ECB String encryption with fixed key (16 bytes)");

        // fixed encryption key 16 bytes long
        byte[] des2EncryptionKey = "1234567890123456".getBytes(StandardCharsets.UTF_8);
        System.out.println("des2EncryptionKey (Base64): " + base64Encoding(des2EncryptionKey));

        // encryption
        System.out.println("\n* * * Encryption * * *");
        String des2CiphertextBase64 = des3EcbEncryptToBase64(tdes2ToDes3Key(des2EncryptionKey), plaintext);
        System.out.println("ciphertext: " + des2CiphertextBase64);
        System.out.println("output is (Base64) ciphertext");
        // decryption
        System.out.println("\n* * * Decryption * * *");
        System.out.println("ciphertext (Base64): " + des2CiphertextBase64);
        System.out.println("input is (Base64) ciphertext");
        decryptedtext = des3EcbDecryptFromBase64(tdes2ToDes3Key(des2EncryptionKey), des2CiphertextBase64);
        System.out.println("plaintext:  " + decryptedtext);

        System.out.println("\nTriple DES ECB String encryption with fixed key (24 bytes)");

        // fixed encryption key 24 bytes long
        byte[] des3EncryptionKey = "123456789012345678901234".getBytes(StandardCharsets.UTF_8);
        System.out.println("des3EncryptionKey (Base64): " + base64Encoding(des3EncryptionKey));

        // encryption
        System.out.println("\n* * * Encryption * * *");
        String des3CiphertextBase64 = des3EcbEncryptToBase64(des3EncryptionKey, plaintext);
        System.out.println("ciphertext: " + des3CiphertextBase64);
        System.out.println("output is (Base64) ciphertext");
        // decryption
        System.out.println("\n* * * Decryption * * *");
        System.out.println("ciphertext (Base64): " + des3CiphertextBase64);
        System.out.println("input is (Base64) ciphertext");
        decryptedtext = des3EcbDecryptFromBase64(des3EncryptionKey, des3CiphertextBase64);
        System.out.println("plaintext:  " + decryptedtext);
    }

    private static byte[] generateRandomDesKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[8]; // 8 bytes for des, 16 bytes for TDES 2 and 24 bytes for TDES 3
        secureRandom.nextBytes(key);
        return key;
    }

    private static String desEcbEncryptToBase64(byte[] desKey, String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(desKey, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        String ciphertextBase64 = base64Encoding(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        return ciphertextBase64;
    }

    private static String desEcbDecryptFromBase64(byte[] desKey, String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] encryptedData = base64Decoding(data);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(desKey, "DES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return new String(cipher.doFinal(encryptedData));
    }

    private static String des3EcbEncryptToBase64(byte[] des3Key, String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(des3Key, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        String ciphertextBase64 = base64Encoding(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        return ciphertextBase64;
    }

    private static String des3EcbDecryptFromBase64(byte[] des3Key, String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] encryptedData = base64Decoding(data);
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(des3Key, "DESede");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return new String(cipher.doFinal(encryptedData));
    }

    private static byte[] tdes2ToDes3Key(byte[] tdes2Key) {
        byte[] tdes3Key = new byte[24];
        System.arraycopy(tdes2Key, 0, tdes3Key, 0, 16);
        System.arraycopy(tdes2Key, 0, tdes3Key, 16, 8);
        return tdes3Key;
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }
}

