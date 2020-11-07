package GenerateInitVector;

import java.security.SecureRandom;
import java.util.Base64;

public class GenerateRandomInitvector {
    public static void main(String[] args) {
        System.out.println("Generate a 16 byte long Initialization vector (IV)");
        byte[] iv = generateRandomInitvector();
        String ivBase64 = base64Encoding(iv);
        System.out.println("generated iv length: " + iv.length + " base64: " + ivBase64);
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
}
