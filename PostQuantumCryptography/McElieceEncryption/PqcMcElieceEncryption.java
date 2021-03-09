package Post_Quantum_Cryptography.PQC_McEliece_Encryption;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.mceliece.*;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePrivateKey;
import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePublicKey;
import org.bouncycastle.pqc.jcajce.provider.mceliece.McElieceKeysToParams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class PqcMcElieceEncryption {
    public static void main(String[] args) throws IOException, InvalidCipherTextException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, InvalidKeyException {
        System.out.println("PQC McEliece public key encryption");
        //Security.addProvider(new BouncyCastleProvider());
        // we do need the regular Bouncy Castle file that includes the PQC provider
        // get Bouncy Castle here: https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on
        // tested with BC version 1.68
        if (Security.getProvider("BCPQC") == null) {
            Security.addProvider(new BouncyCastlePQCProvider());
        }
        System.out.println("\n************************************\n" +
                "* # # SERIOUS SECURITY WARNING # # *\n" +
                "* This program is a CONCEPT STUDY  *\n" +
                "* for the algorithm                *\n" +
                "* McEliece [public key encryption] *\n" +
                "* The program is using an OUTDATED *\n" +
                "* parameter set and I cannot       *\n" +
                "* check for the correctness of the *\n" +
                "* output and other details         *\n" +
                "*                                  *\n" +
                "*     DO NOT USE THE PROGRAM IN    *\n" +
                "*     ANY PRODUCTION ENVIRONENT    *\n" +
                "************************************");


        // data to encrypt is usually a 32 bytes long (randomly generated) AES key
        String keyToEncryptString = "1234567890ABCDEF1122334455667788";
        byte[] keyToEncrypt = keyToEncryptString.getBytes(StandardCharsets.UTF_8);

        // generation of the McElice key pair
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = generateMcEliceKeyPair();

        // get private and public key
        AsymmetricKeyParameter privateKeyMcElice = asymmetricCipherKeyPair.getPrivate();
        AsymmetricKeyParameter publicKeyMcElice = asymmetricCipherKeyPair.getPublic();

        // storing the key as byte array
        byte[] privateKeyMcEliceByte = (new BCMcEliecePrivateKey((McEliecePrivateKeyParameters) asymmetricCipherKeyPair.getPrivate())).getEncoded();
        byte[] publicKeyMcEliceByte = (new BCMcEliecePublicKey((McEliecePublicKeyParameters) asymmetricCipherKeyPair.getPublic())).getEncoded();
        System.out.println("\ngenerated private key length: " + privateKeyMcEliceByte.length);
        System.out.println("generated public key length:  " + publicKeyMcEliceByte.length);

        // generate the keys from a byte array
        AsymmetricKeyParameter privateKeyMcEliceLoad = getPrivateKeyMcEliceFromByteArray(privateKeyMcEliceByte);
        AsymmetricKeyParameter publicKeyMcEliceLoad = getPublicKeyMcEliceFromByteArray(publicKeyMcEliceByte);

        System.out.println("\n* * * encrypt the keyToEncrypt with the public key * * *");
        byte[] ciphertext = pqcMcEliceEncrypt(publicKeyMcEliceLoad, keyToEncrypt);
        System.out.println("ciphertext length: " + ciphertext.length + " data:\n"  + bytesToHex(ciphertext));

        System.out.println("\n* * * decrypt the ciphertext with the private key * * *");
        byte[] decryptedtext = pqcMcEliceDecrypt(privateKeyMcEliceLoad, ciphertext);
        System.out.println("decryptedtext: " + new String(decryptedtext));
        System.out.println("decryptedtext equals to keyToEncrypt: " + Arrays.equals(keyToEncrypt, decryptedtext));
    }

    private static AsymmetricCipherKeyPair generateMcEliceKeyPair() {
        SecureRandom secureRandom = new SecureRandom();
        McElieceParameters mcElieceParameters = new McElieceParameters();
        McElieceKeyPairGenerator mcElieceKeyPairGenerator = new McElieceKeyPairGenerator();
        McElieceKeyGenerationParameters mcElieceKeyGenerationParameters = new McElieceKeyGenerationParameters(secureRandom, mcElieceParameters);
        mcElieceKeyPairGenerator.init(mcElieceKeyGenerationParameters);
        return mcElieceKeyPairGenerator.generateKeyPair();
    }

    private static AsymmetricKeyParameter getPrivateKeyMcEliceFromByteArray(byte[] privateKeyEncoded) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        KeyFactory keyFactory = KeyFactory.getInstance("McEliece", "BCPQC");
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyEncoded);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        return McElieceKeysToParams.generatePrivateKeyParameter(privateKey);
    }

    private static AsymmetricKeyParameter getPublicKeyMcEliceFromByteArray(byte[] publicKeyEncoded) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        KeyFactory keyFactory = KeyFactory.getInstance("McEliece", "BCPQC");
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyEncoded);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        return McElieceKeysToParams.generatePublicKeyParameter(publicKey);
    }

    private static byte[] pqcMcEliceEncrypt(AsymmetricKeyParameter publicKeyMcElice, byte[] keyToEncrypt){
        McElieceCipher mcEliecePKCSDigestCipher = new McElieceCipher();
        SecureRandom secureRandom = new SecureRandom();
        ParametersWithRandom parametersWithRandom = new ParametersWithRandom(publicKeyMcElice, secureRandom);
        mcEliecePKCSDigestCipher.init(true, parametersWithRandom);
        return mcEliecePKCSDigestCipher.messageEncrypt(keyToEncrypt);
    }

    private static byte[] pqcMcEliceDecrypt(AsymmetricKeyParameter privateKeyMcElice, byte[] ciphertext) throws InvalidCipherTextException {
        McElieceCipher mcEliecePKCSDigestCipher = new McElieceCipher();
        mcEliecePKCSDigestCipher.init(false, privateKeyMcElice);
        return mcEliecePKCSDigestCipher.messageDecrypt(ciphertext);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
