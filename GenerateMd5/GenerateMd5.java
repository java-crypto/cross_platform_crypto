package Generate_MD5;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GenerateMd5 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println("Generate a MD5 hash");

        System.out.println("\n# # # SECURITY WARNING: This code is provided for achieve    # # #");
        System.out.println("# # # compatibility between different programming languages. # # #");
        System.out.println("# # # It is NOT SECURE - DO NOT USE THIS CODE ANY LONGER !   # # #");
        System.out.println("# # # The hash algorithm MD5 is BROKEN.                      # # #");
        System.out.println("# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n");

        String plaintext = "The quick brown fox jumps over the lazy dog";
        System.out.println("plaintext:                " + plaintext);

        byte[] md5Value = calculateMd5(plaintext);
        System.out.println("md5Value (hex) length: " + md5Value.length + " data: " + bytesToHex(md5Value));
    }

    private static byte[] calculateMd5 (String string) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(string.getBytes(StandardCharsets.UTF_8));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

}
