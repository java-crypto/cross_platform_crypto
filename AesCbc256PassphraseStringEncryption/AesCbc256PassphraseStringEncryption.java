package AES_CBC_256_Passphrase_String_Encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class AesCbc256PassphraseStringEncryption {
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        System.out.println("AES CBC 256 String encryption with passphrase");

        System.out.println("\n# # # SECURITY WARNING: This code is provided for achieve    # # #");
        System.out.println("# # # compatibility between different programming languages. # # #");
        System.out.println("# # # It is not necessarily fully secure.                    # # #");
        System.out.println("# # # Its security depends on the complexity and length      # # #");
        System.out.println("# # # of the password, because of only one iteration and     # # #");
        System.out.println("# # # the use of MD5.                                        # # #");
        System.out.println("# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n");

        String plaintext = "The quick brown fox jumps over the lazy dog";
        String passphase = "my secret passphrase";
        System.out.println("passphrase: " + passphase);

        // encryption
        System.out.println("\n* * * Encryption * * *");
        String ciphertextBase64 = aesCbcPassphraseEncryptToBase64(passphase, plaintext);
        System.out.println("ciphertext: " + ciphertextBase64);
        System.out.println("output is (Base64) ciphertext");

        // decryption
        System.out.println("\n* * * Decryption * * *");
        String ciphertextDecryptionBase64 = ciphertextBase64;
        System.out.println("passphrase: " + passphase);
        System.out.println("ciphertext (Base64): " + ciphertextDecryptionBase64);
        System.out.println("input is (Base64) ciphertext");
        String decryptedtext = aesCbcPassphraseDecryptFromBase64(passphase, ciphertextDecryptionBase64);
        System.out.println("plaintext:  " + decryptedtext);
    }

    /**
     * Source: https://stackoverflow.com/a/41434590/8166854
     * author: Codo
     * Generates a key and an initialization vector (IV) with the given salt and password.
     * <p>
     * This method is equivalent to OpenSSL's EVP_BytesToKey function
     * (see https://github.com/openssl/openssl/blob/master/crypto/evp/evp_key.c).
     * By default, OpenSSL uses a single iteration, MD5 as the algorithm and UTF-8 encoded password data.
     * </p>
     * @param keyLength the length of the generated key (in bytes)
     * @param ivLength the length of the generated IV (in bytes)
     * @param iterations the number of digestion rounds
     * @param salt the salt data (8 bytes of data or <code>null</code>)
     * @param password the password data (optional)
     * @param md the message digest algorithm to use
     * @return an two-element array with the generated key and IV
     */
    public static byte[][] GenerateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password, MessageDigest md) {
        int digestLength = md.getDigestLength();
        int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
        byte[] generatedData = new byte[requiredLength];
        int generatedLength = 0;
        try {
            md.reset();
            // Repeat process until sufficient data has been generated
            while (generatedLength < keyLength + ivLength) {
                // Digest data (last digest if available, password data, salt if available)
                if (generatedLength > 0)
                    md.update(generatedData, generatedLength - digestLength, digestLength);
                md.update(password);
                if (salt != null)
                    md.update(salt, 0, 8);
                md.digest(generatedData, generatedLength, digestLength);
                // additional rounds
                for (int i = 1; i < iterations; i++) {
                    md.update(generatedData, generatedLength, digestLength);
                    md.digest(generatedData, generatedLength, digestLength);
                }
                generatedLength += digestLength;
            }
            // Copy key and IV into separate byte arrays
            byte[][] result = new byte[2][];
            result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
            if (ivLength > 0)
                result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);
            return result;
        } catch (DigestException e) {
            throw new RuntimeException(e);
        } finally {
            // Clean out temporary data
            Arrays.fill(generatedData, (byte)0);
        }
    }

    private static byte[] generateRandomSalt8Byte() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[8];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private static String aesCbcPassphraseEncryptToBase64(String passphrase, String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] saltData = generateRandomSalt8Byte();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        final byte[][] keyAndIV = GenerateKeyAndIV(32, 16, 1, saltData, passphrase.getBytes(StandardCharsets.UTF_8), md5);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyAndIV[0], "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(keyAndIV[1]);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] ciphertext = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        byte[] salted = "Salted__".getBytes(StandardCharsets.UTF_8);
        return base64Encoding(concatenate3ByteArrays(salted, saltData, ciphertext));
    }

    private static String aesCbcPassphraseDecryptFromBase64(String passphrase, String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] cipherData = base64Decoding(data);
        byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        final byte[][] keyAndIV = GenerateKeyAndIV(32, 16, 1, saltData, passphrase.getBytes(StandardCharsets.UTF_8), md5);
        SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
        IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);
        byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
        Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decryptedData = aesCBC.doFinal(encrypted);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }

    public static byte[] concatenate3ByteArrays(byte[] a, byte[] b, byte[] c) {
        return ByteBuffer
                .allocate(a.length + b.length + c.length)
                .put(a).put(b).put(c)
                .array();
    }
}
