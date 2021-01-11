package Generate_SHA256_Base64_Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GenerateSha256Base64Hex {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println("Generate a SHA-256 hash, Base64 en- and decoding and hex conversions");

        String plaintext = "The quick brown fox jumps over the lazy dog";
        System.out.println("plaintext:                " + plaintext);

        byte[] sha256Value = calculateSha256(plaintext);
        System.out.println("sha256Value (hex) length: " + sha256Value.length + " data: " + bytesToHex(sha256Value));

        String sha256Base64 = base64Encoding(sha256Value);
        System.out.println("sha256Value (base64):     " + sha256Base64);

        byte[] sha256ValueDecoded = base64Decoding(sha256Base64);
        System.out.println("sha256Base64 decoded to a byte array:");
        System.out.println("sha256Value (hex) length: " + sha256ValueDecoded.length + " data: " + bytesToHex(sha256ValueDecoded));

        String sha256HexString = "d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592";
        byte[] sha256Hex = hexStringToByteArray(sha256HexString);
        System.out.println("sha256HexString converted to a byte array:");
        System.out.println("sha256Value (hex) length: " + sha256Hex.length + " data: " + bytesToHex(sha256Hex));
    }

    private static byte[] calculateSha256 (String string) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(string.getBytes(StandardCharsets.UTF_8));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }
}
