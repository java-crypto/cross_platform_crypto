package AES_CBC_256_Tampering;

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
import java.util.Base64;

public class AesCbc256StringDecryptionOnlyTampering {
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        System.out.println("AES CBC 256 String Decryption only with tampering");
        // ### place your data here:
        String decryptionKeyBase64 = "d3+KZq3lsdy5Hgd1VRVQFUhA9jU0PaBHTG2LG2PazQM=";
        String ciphertextDecryptionBase64 = "8GhkycnbzDo5WbRol5zfCQ==:gQrTOJdobbI4fuCHCfu5eqKFYCaw/AM6xMK2o+xCXB0MeAQ7rFbXAbAE3Ex+bD8c";

        // decryption
        System.out.println("\n* * * Decryption original * * *");
        System.out.println("decryptionKey (Base64): " + decryptionKeyBase64);
        byte[] decryptionKey = base64Decoding(decryptionKeyBase64);
        System.out.println("ciphertext (Base64): " + ciphertextDecryptionBase64);
        System.out.println("input is (Base64) iv : (Base64) ciphertext");
        String decryptedtext = aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64);
        System.out.println("plaintext:  " + decryptedtext);

        // tampering
        System.out.println("\ntampering the data without encryption key");
        // ### important: all strings have to be exact 16 bytes/characters long ###
        String guessedString = "The quick brown ";
        String newString = "The happy brown ";
        System.out.println("guessedString: " + guessedString);
        System.out.println("newString:     " + newString);
        ciphertextDecryptionBase64 = aesCbcTamperingBase64(ciphertextDecryptionBase64, guessedString, newString);

        // decryption
        System.out.println("\n* * * Decryption tampered * * *");
        System.out.println("ciphertext (Base64): " + ciphertextDecryptionBase64);
        decryptedtext = aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64);
        System.out.println("plaintext:  " + decryptedtext);
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

    private static String aesCbcTamperingBase64(String data, String guessedString, String newString) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String[] parts = data.split(":", 0);
        byte[] iv = base64Decoding(parts[0]); // unused
        byte[] encryptedData = base64Decoding(parts[1]); // unused
        byte[] guessedStringByte = guessedString.getBytes(StandardCharsets.UTF_8);
        byte[] newStringByte = newString.getBytes(StandardCharsets.UTF_8);
        // tampering with guessedString and newString
        for (int i = 0; i < 16; i++) {
            iv[i] = (byte) (iv[i] ^ newStringByte[i] ^ guessedStringByte[i]);
        }
        // encode back to base64
        return base64Encoding(iv) + ":" + base64Encoding(encryptedData);
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }
}

/*
AES CBC 256 String Decryption only with tampering

* * * Decryption original * * *
decryptionKey (Base64): d3+KZq3lsdy5Hgd1VRVQFUhA9jU0PaBHTG2LG2PazQM=
ciphertext (Base64): 8GhkycnbzDo5WbRol5zfCQ==:gQrTOJdobbI4fuCHCfu5eqKFYCaw/AM6xMK2o+xCXB0MeAQ7rFbXAbAE3Ex+bD8c
input is (Base64) iv : (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog

* * * Decryption tampered * * *
ciphertext (Base64): 8GhkydDP1SkrWbRol5zfCQ==:gQrTOJdobbI4fuCHCfu5eqKFYCaw/AM6xMK2o+xCXB0MeAQ7rFbXAbAE3Ex+bD8c
plaintext:  The happy brown fox jumps over the lazy dog
 */
