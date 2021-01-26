package Generate_Ed25519_key;

import java.security.SecureRandom;
import java.util.Base64;

public class GenerateEd25519KeysDerivation {
    public static void main(String[] args) throws Exception {
        System.out.println("Generate ED25519 private and public key and derive public key from private key");

        TweetNaclFast.Signature.KeyPair keyPair = generateEd25519KeyPair();
        byte[] privateKey = keyPair.getSecretKey();
        byte[] publicKey = keyPair.getPublicKey();
        System.out.println("privateKey (Base64): " + base64Encoding(privateKey));
        System.out.println("publicKey (Base64):  " + base64Encoding(publicKey));

        System.out.println("\nderive the publicKey from the privateKey");
        byte[] publicKeyDerived = deriveEd25519PublicKey(privateKey);
        System.out.println("publicKey (Base64):  " + base64Encoding(publicKeyDerived));
    }

    private static TweetNaclFast.Signature.KeyPair generateEd25519KeyPair() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[64];
        secureRandom.nextBytes(key);
        TweetNaclFast.Signature.KeyPair keyPair = TweetNaclFast.Signature.keyPair_fromSecretKey(key);
        return keyPair;
    }

    private static byte[] deriveEd25519PublicKey(byte[] privateKey) {
        TweetNaclFast.Signature.KeyPair keyPair = TweetNaclFast.Signature.keyPair_fromSecretKey(privateKey);
        return keyPair.getPublicKey();
    }

    private static String base64Encoding(byte[] input) {return Base64.getEncoder().encodeToString(input);}
}