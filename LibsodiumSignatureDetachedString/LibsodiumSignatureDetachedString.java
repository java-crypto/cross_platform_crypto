package Libsodium_signature_detached_string;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LibsodiumSignatureDetachedString {
    // uses TweetNacl https://github.com/InstantWebP2P/tweetnacl-java
    public static void main(String[] args) {
        System.out.println("Libsodium detached signature string");

        String dataToSign = "The quick brown fox jumps over the lazy dog";
        System.out.println("plaintext: " + dataToSign);

        // usually we would load the private and public key from a file or keystore
        // here we use hardcoded keys for demonstration - don't do this in real programs

        System.out.println("* * * sign the plaintext with the ED25519 private key * * *");
        byte [] privateKey = loadEd25519PrivateKey();
        String signatureBase64 = libsodiumSignDetachedToBase64(privateKey, dataToSign);

        System.out.println("signature (Base64): " + signatureBase64);
        System.out.println("signature expected: x41mufah/9VO347W+nPXu5FYeSJOI894YClbbTiX0pwRrvfAPMymxEvyMMsDFMI0R0sulCnuCRSgN0WOKnZBDg==");
        System.out.println("\n* * * verify the signature against the plaintext with the ED25519 public key * * *");
        byte[] publicKey = loadEd25519PublicKey();
        System.out.println("signature (Base64): " + signatureBase64);
        boolean signatureVerified = libsodiumVerifyDetachedFromBase64(publicKey, dataToSign, signatureBase64);
        System.out.println("signature (Base64) verified: " + signatureVerified);
    }

    private static String libsodiumSignDetachedToBase64(byte[] privateKey, String dataToSign) {
        TweetNaclFast.Signature sig = new TweetNaclFast.Signature(new byte[32], privateKey);
        return base64Encoding(sig.detached(dataToSign.getBytes(StandardCharsets.UTF_8)));
    };

    private static boolean libsodiumVerifyDetachedFromBase64(byte[] publicKey, String dataToSign, String signatureBase64) {
        TweetNaclFast.Signature sig = new TweetNaclFast.Signature(publicKey, new byte[64]);
        return sig.detached_verify(dataToSign.getBytes(StandardCharsets.UTF_8), base64Decoding(signatureBase64));
    }

    private static byte[] loadEd25519PrivateKey() {
        // this is a sample key - don't worry !
        return base64Decoding("Gjho7dILimbXJY8RvmQZEBczDzCYbZonGt/e5QQ3Vro2sjTp95EMxOrgEaWiprPduR/KJFBRA+RlDDpkvmhVfw==");
    }

    private static byte[] loadEd25519PublicKey() {
        // this is a sample key - don't worry !
        return base64Decoding("NrI06feRDMTq4BGloqaz3bkfyiRQUQPkZQw6ZL5oVX8=");
    }

    private static String base64Encoding(byte[] input) {return Base64.getEncoder().encodeToString(input);
    }
    private static byte[] base64Decoding(String input) {return Base64.getDecoder().decode(input);
    }
}
