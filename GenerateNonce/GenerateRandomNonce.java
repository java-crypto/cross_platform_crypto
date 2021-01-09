package GenerateNonce;

import java.security.SecureRandom;
import java.util.Base64;

public class GenerateRandomNonce {
    public static void main(String[] args) {
        System.out.println("Generate a 12 byte long nonce for AES GCM");
        byte[] nonce = generateRandomNonce();
        String nonceBase64 = base64Encoding(nonce);
        System.out.println("generated nonce length: " + nonce.length + " base64: " + nonceBase64);
    }
    private static byte[] generateRandomNonce() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] nonce = new byte[12];
        secureRandom.nextBytes(nonce);
        return nonce;
    }
    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
}
