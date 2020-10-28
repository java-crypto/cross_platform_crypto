package GenerateAesKey;

import java.security.SecureRandom;
import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        System.out.println("Generate a 32 byte length AES key");
        byte[] aesKey = generateRandomAesKey();
        String aesKeyBase64 = base64Encoding(aesKey);
        System.out.println("generated key length: " + aesKey.length + " base64: " + aesKeyBase64);
    }
    private static byte[] generateRandomAesKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);
        return key;
    }
    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
}
