package Post_Quantum_Cryptography.PQC_NTRU_encryption;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.ntru.*;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Security;

public class PqcNtruEncryption {


    public static void main(String[] args) throws InvalidCipherTextException, IOException {
        System.out.println("PQC NTRU key encapsulation mechanism (KEM)");

        if (Security.getProvider("BCPQC") == null) {
            Security.addProvider(new BouncyCastlePQCProvider());
        }
        // we do need the _extended_ Bouncy Castle file that includes the PQC provider
        // get Bouncy Castle here: https://mvnrepository.com/artifact/org.bouncycastle/bcprov-ext-jdk15on
        // tested with BC version 1.68

        System.out.println("\n************************************\n" +
                "* # # SERIOUS SECURITY WARNING # # *\n" +
                "* This program is a CONCEPT STUDY  *\n" +
                "* for the algorithm                *\n" +
                "* NTRU [public key encryption]     *\n" +
                "* The program is using an OUTDATED *\n" +
                "* parameter set and I cannot       *\n" +
                "* check for the correctness of the *\n" +
                "* output and other details         *\n" +
                "*                                  *\n" +
                "*    DO NOT USE THE PROGRAM IN     *\n" +
                "*    ANY PRODUCTION ENVIRONMENT    *\n" +
                "************************************");

        // data to encrypt is usually a 32 bytes long (randomly generated) AES key
        String keyToEncryptString = "1234567890ABCDEF1122334455667788";
        byte[] keyToEncrypt = keyToEncryptString.getBytes(StandardCharsets.UTF_8);

        // generation of the NTRU key pair using OUTDATED APR2011_743_FAST parameter set
        NTRUEncryptionKeyPairGenerator ntruEncryptionKeyPairGenerator = new NTRUEncryptionKeyPairGenerator();
        NTRUEncryptionKeyGenerationParameters ntruEncryptionKeyGenerationParameters = NTRUEncryptionKeyGenerationParameters.APR2011_743_FAST.clone();
        ntruEncryptionKeyPairGenerator.init(ntruEncryptionKeyGenerationParameters);
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = ntruEncryptionKeyPairGenerator.generateKeyPair();

        // get private and public key
        AsymmetricKeyParameter privateKeyNtru = asymmetricCipherKeyPair.getPrivate();
        AsymmetricKeyParameter publicKeyNtru = asymmetricCipherKeyPair.getPublic();

        // storing the key as byte array
        byte[] privateKeyEncoded = ((NTRUEncryptionPrivateKeyParameters) privateKeyNtru).getEncoded();
        byte[] publicKeyEncoded = ((NTRUEncryptionPublicKeyParameters) publicKeyNtru).getEncoded();
        System.out.println("\ngenerated private key length: " + privateKeyEncoded.length);
        System.out.println("generated public key length:  " + publicKeyEncoded.length);

        // generate the keys from a byte array
        NTRUEncryptionParameters ntruEncryptionParameters = NTRUEncryptionKeyGenerationParameters.APR2011_743_FAST.getEncryptionParameters();
        NTRUEncryptionPrivateKeyParameters ntruEncryptionPrivateKeyParameters = new NTRUEncryptionPrivateKeyParameters(privateKeyEncoded, ntruEncryptionParameters);
        NTRUEncryptionPublicKeyParameters ntruEncryptionPublicKeyParameters = new NTRUEncryptionPublicKeyParameters(publicKeyEncoded, ntruEncryptionParameters);

        System.out.println("\n* * * encrypt the keyToEncrypt with the public key * * *");
        //byte[] ciphertext = pqcNtruEncrypt(publicKeyNtru, keyToEncrypt);
        byte[] ciphertext = pqcNtruEncrypt(ntruEncryptionPublicKeyParameters, keyToEncrypt);
        System.out.println("ciphertext length: " + ciphertext.length + " data: ** not shown due to length");

        System.out.println("\n* * * decrypt the ciphertext with the private key * * *");
        byte[] decryptedtext = pqcNtruDecrypt(ntruEncryptionPrivateKeyParameters, ciphertext);
        //byte[] decryptedtext = pqcNtruDecrypt(privateKeyNtru, ciphertext);
        System.out.println("decryptedtext: " + new String(decryptedtext));
        System.out.println("decryptedtext equals to keyToEncrypt: " + java.util.Arrays.equals(keyToEncrypt, decryptedtext));

        System.out.println("\nused algorithm parameter: " + ntruEncryptionKeyGenerationParameters.getEncryptionParameters().toString());
    }

    private static byte[] pqcNtruEncrypt(AsymmetricKeyParameter publicKey, byte[] keyToEncrypt) throws InvalidCipherTextException {
        NTRUEngine ntru = new NTRUEngine();
        ntru.init(true, publicKey);
        return ntru.processBlock(keyToEncrypt, 0, keyToEncrypt.length);
    }

    private static byte[] pqcNtruDecrypt(AsymmetricKeyParameter privateKey, byte[] ciphertext) throws InvalidCipherTextException {
        NTRUEngine ntru = new NTRUEngine();
        ntru.init(false, privateKey);
        return ntru.processBlock(ciphertext, 0, ciphertext.length);
    }
}
