package Post_Quantum_Cryptography.PQC_SPHINCS_Signature;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.pqc.crypto.MessageSigner;
import org.bouncycastle.pqc.crypto.sphincs.*;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;

public class PqcSphincsSignature {

    public static void main(String[] args) {
        System.out.println("PQC SPHINCS signature");

        if (Security.getProvider("BCPQC") == null) {
            Security.addProvider(new BouncyCastlePQCProvider());
        }
        // we do need the regular Bouncy Castle file that includes the PQC provider
        // get Bouncy Castle here: https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on
        // tested with BC version 1.68

        System.out.println("\n************************************\n" +
                "* # # SERIOUS SECURITY WARNING # # *\n" +
                "* This program is a CONCEPT STUDY  *\n" +
                "* for the algorithm                *\n" +
                "* SPHINCS [signature]              *\n" +
                "* The program is using an OUTDATED *\n" +
                "* parameter set and I cannot       *\n" +
                "* check for the correctness of the *\n" +
                "* output and other details         *\n" +
                "*                                  *\n" +
                "*    DO NOT USE THE PROGRAM IN     *\n" +
                "*    ANY PRODUCTION ENVIRONMENT    *\n" +
                "************************************");

        String dataToSignString = "The quick brown fox jumps over the lazy dog";
        byte[] dataToSign = dataToSignString.getBytes(StandardCharsets.UTF_8);

        // generation of the Sphincs key pair
        // uses SHA3-256 for key generation
        AsymmetricCipherKeyPair keyPair = generateSphincsKeyPairSha3();

        // get private and public key
        SPHINCSPrivateKeyParameters privateKey = (SPHINCSPrivateKeyParameters) keyPair.getPrivate();
        SPHINCSPublicKeyParameters publicKey = (SPHINCSPublicKeyParameters) keyPair.getPublic();

        // storing the key as byte array
        byte[] privateKeyEncoded = privateKey.getKeyData();
        byte[] publicKeyEncoded = publicKey.getKeyData();
        System.out.println("\ngenerated private key length: " + privateKeyEncoded.length + " key algorithm: " + privateKey.getTreeDigest());
        System.out.println("generated public key length:  " + publicKeyEncoded.length + " key algorithm: " + publicKey.getTreeDigest());

        // generate the keys from a byte array
        SPHINCSPrivateKeyParameters privateKeyLoad = new SPHINCSPrivateKeyParameters(privateKeyEncoded);
        SPHINCSPublicKeyParameters publicKeyLoad = new SPHINCSPublicKeyParameters(publicKeyEncoded);

        System.out.println("\n* * * sign the dataToSign with the private key * * *");
        byte[] signature = pqcSphincsSignatureSha3(privateKeyLoad, dataToSign);
        System.out.println("signature length: " + signature.length + " data: ** not shown due to length");

        System.out.println("\n* * * verify the signature with the public key * * *");
        boolean signatureVerified = pqcSphincsVerificationSha3(publicKeyLoad, dataToSign, signature);
        System.out.println("the signature is verified: " + signatureVerified);
    }

    public static AsymmetricCipherKeyPair generateSphincsKeyPairSha3() {
        SPHINCS256KeyPairGenerator generator = new SPHINCS256KeyPairGenerator();
        generator.init(new SPHINCS256KeyGenerationParameters(new SecureRandom(), new SHA3Digest(256)));
        AsymmetricCipherKeyPair kp = generator.generateKeyPair();
        return kp;
    }

    public static byte[] pqcSphincsSignatureSha3 (SPHINCSPrivateKeyParameters priv, byte[] messageByte) {
        MessageSigner sphincsSigner = new SPHINCS256Signer(new SHA3Digest(256), new SHA3Digest(512));
        sphincsSigner.init(true, priv);
        byte[] sig = sphincsSigner.generateSignature(messageByte);
        return sig;
    }

    public static Boolean pqcSphincsVerificationSha3 (SPHINCSPublicKeyParameters pub, byte[] messageByte, byte[] signatureByte)  {
        MessageSigner sphincsSigner = new SPHINCS256Signer(new SHA3Digest(256), new SHA3Digest(512));
        sphincsSigner.init(false, pub);
        return sphincsSigner.verifySignature(messageByte, signatureByte);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
