package Curve25519_KeyGeneration;

import org.whispersystems.curve25519.Curve25519;
import org.whispersystems.curve25519.Curve25519KeyPair;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GenerateCurve25519Keypair {
    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        System.out.println("Generate a key pair for Curve25519");
        // you need this library
        // https://github.com/signalapp/curve25519-java
        // https://mvnrepository.com/artifact/org.whispersystems/curve25519-java
        Curve25519KeyPair keyPair = generateCurve25519Keypair();
        byte[] privateKey = keyPair.getPrivateKey();
        byte[] publicKey = keyPair.getPublicKey();
        System.out.println("base64 encoded key data:");
        System.out.println("Curve25519 PrivateKey: " + base64Encoding(privateKey));
        System.out.println("Curve25519 PublicKey:  " + base64Encoding(publicKey));
    }

    public static Curve25519KeyPair generateCurve25519Keypair () {
        return Curve25519.getInstance(Curve25519.BEST).generateKeyPair();
    }
    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
}
