package AES_CBC_256_String;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AesCbc256StringDecryptionOnly {
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        System.out.println("AES CBC 256 String Decryption only");
        // ### place your data here:
        String decryptionKeyBase64 = "d3+KZq3lsdy5Hgd1VRVQFUhA9jU0PaBHTG2LG2PazQM=";
        String ciphertextDecryptionBase64 = "8GhkycnbzDo5WbRol5zfCQ==:gQrTOJdobbI4fuCHCfu5eqKFYCaw/AM6xMK2o+xCXB0MeAQ7rFbXAbAE3Ex+bD8c";

        // decryption
        System.out.println("\n* * * Decryption * * *");
        System.out.println("decryptionKey (Base64): " + decryptionKeyBase64);
        byte[] decryptionKey = base64Decoding(decryptionKeyBase64);
        System.out.println("ciphertext (Base64): " + ciphertextDecryptionBase64);
        System.out.println("input is (Base64) iv : (Base64) ciphertext");
        String decryptedtext = aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64);
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

    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }
}

/*
AES CBC 256 String Decryption only

* * * Decryption * * *
decryptionKey (Base64): d3+KZq3lsdy5Hgd1VRVQFUhA9jU0PaBHTG2LG2PazQM=
ciphertext (Base64): 8GhkycnbzDo5WbRol5zfCQ==:gQrTOJdobbI4fuCHCfu5eqKFYCaw/AM6xMK2o+xCXB0MeAQ7rFbXAbAE3Ex+bD8c
input is (Base64) iv : (Base64) ciphertext
plaintext:  The quick brown fox jumps over the lazy dog
 */
