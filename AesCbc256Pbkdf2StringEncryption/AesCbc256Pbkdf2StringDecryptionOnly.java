package AES_CBC_256_Pbkdf2_String;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AesCbc256Pbkdf2StringDecryptionOnly {
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeySpecException {
        System.out.println("AES CBC 256 String decryption with PBKDF2 derived key only");

        char[] password = "secret password".toCharArray();

        // decryption
        String ciphertextDecryptionBase64 = "ETpVa06FG6pKzDRtc4OFHMWk5SGLx0Bg1E3+fAmtsuA=:rsvmFmJZBT+smIE5lFKDhA==:BWpupA3D/ciXB/tN+w0SguYYfLCINhP3ofkNTeWQiADPSaB+AJAy4hU8ITazMWff";
        System.out.println("ciphertext (Base64): " + ciphertextDecryptionBase64);
        System.out.println("input is (Base64) salt : (Base64) iv : (Base64) ciphertext");
        String decryptedtext = aesCbcPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64);
        System.out.println("plaintext:  " + decryptedtext);
    }

    private static String aesCbcPbkdf2DecryptFromBase64(char[] password, String data) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeySpecException {
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

    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }
}

/*
complete run data

 */
