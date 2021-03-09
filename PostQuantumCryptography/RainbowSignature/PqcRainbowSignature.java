package Post_Quantum_Cryptography.PQC_RainbowBc_Signature;

import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPrivateKey;
import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPublicKey;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class PqcRainbowSignature {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException, InvalidKeySpecException {
        //Security.addProvider(new BouncyCastleProvider());
        // we do need the regular Bouncy Castle file that includes the PQC provider
        // get Bouncy Castle here: https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on
        // tested with BC version 1.68
        if (Security.getProvider("BCPQC") == null) {
            Security.addProvider(new BouncyCastlePQCProvider());
        }
        System.out.println("PQC Rainbow signature");

        System.out.println("\n************************************\n" +
                "* # # SERIOUS SECURITY WARNING # # *\n" +
                "* This program is a CONCEPT STUDY  *\n" +
                "* for the algorithm                *\n" +
                "* Rainbow [signature]              *\n" +
                "* The program is using an OUTDATED *\n" +
                "* parameter set and I cannot       *\n" +
                "* check for the correctness of the *\n" +
                "* output and other details         *\n" +
                "*                                  *\n" +
                "*    DO NOT USE THE PROGRAM IN     *\n" +
                "*    ANY PRODUCTION ENVIRONENT     *\n" +
                "************************************");

        String dataToSignString = "The quick brown fox jumps over the lazy dog";
        byte[] dataToSign = dataToSignString.getBytes(StandardCharsets.UTF_8);

        // generation of the Rainbox key pair
        KeyPair keyPair = generateRainboxKeyPair();

        // get private and public key
        PrivateKey privateKeyRainbow = keyPair.getPrivate();
        PublicKey publicKeyRainbow = keyPair.getPublic();

        // storing the key as byte array
        byte[] privateKeyRainbowByte = privateKeyRainbow.getEncoded();
        byte[] publicKeyRainbowByte = publicKeyRainbow.getEncoded();
        System.out.println("\ngenerated private key length: " + privateKeyRainbowByte.length);
        System.out.println("generated public key length:  " + publicKeyRainbowByte.length);

        // generate the keys from a byte array
        PrivateKey privateKeyRainbowLoad = getBCRainbowPrivateKeyFromEncoded(privateKeyRainbowByte);
        PublicKey publicKeyRainbowLoad = getBCRainbowPublicKeyFromEncoded(publicKeyRainbowByte);

        System.out.println("\n* * * sign the dataToSign with the private key * * *");
        byte[] signature = pqcRainbowSignature(privateKeyRainbowLoad, dataToSign);
        System.out.println("signature length: " + signature.length + " data:\n"  + bytesToHex(signature));

        System.out.println("\n* * * verify the signature with the public key * * *");
        boolean signatureVerified = pqcRainbowVerification(publicKeyRainbowLoad, dataToSign, signature);
        System.out.println("the signature is verified: " + signatureVerified);
    }

    private static KeyPair generateRainboxKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("Rainbow");
        kpg.initialize(1024);
        return kpg.genKeyPair();
    }

    private static BCRainbowPrivateKey getBCRainbowPrivateKeyFromEncoded(byte[] encodedKey)
            throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("Rainbow", "BCPQC");
        return (BCRainbowPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    private static BCRainbowPublicKey getBCRainbowPublicKeyFromEncoded(byte[] encodedKey)
            throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("Rainbow", "BCPQC");
        return (BCRainbowPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
    }

    private static byte[] pqcRainbowSignature(PrivateKey privateKey, byte[] dataToSign) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA256WITHRainbow");
        sig.initSign(privateKey, new SecureRandom());
        sig.update(dataToSign, 0, dataToSign.length);
        return sig.sign();
    }

    private static boolean pqcRainbowVerification(PublicKey publicKey, byte[] dataToSign, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sigVerify = Signature.getInstance("SHA256WITHRainbow");
        sigVerify.initVerify(publicKey);
        sigVerify.update(dataToSign, 0, dataToSign.length);
        return sigVerify.verify(signature);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
