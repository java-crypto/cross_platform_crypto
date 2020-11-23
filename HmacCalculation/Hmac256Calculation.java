package HMAC_Calculation;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Hmac256Calculation {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        System.out.println("HMAC 256 calculation");

        String hmac256KeyString = "hmac256ForAesEncryption";
        String plaintextString = "The quick brown fox jumps over the lazy dog";
        System.out.println("hmac256Key:    " + hmac256KeyString);
        System.out.println("plaintext:  " + plaintextString);

        // get binary data
        byte[] hmacKey = hmac256KeyString.getBytes(StandardCharsets.UTF_8);
        byte[] plaintext = plaintextString.getBytes(StandardCharsets.UTF_8);
        // calculate hmac
        byte[] hmac256 = hmac256Calculation(hmacKey, plaintext);
        System.out.println("hmac256 length: " + hmac256.length + " (Base64) data: " + base64Encoding(hmac256));
    }

    private static byte[] hmac256Calculation(byte[] keyHmac, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(keyHmac, "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return sha256_HMAC.doFinal(data);
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
}

