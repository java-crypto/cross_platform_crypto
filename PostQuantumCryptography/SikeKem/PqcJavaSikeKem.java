import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sike.crypto.KeyGenerator;
import sike.crypto.Sike;
import sike.model.*;
import sike.param.SikeParam;
import sike.param.SikeParamP751;

import java.security.*;
import java.util.Base64;

public class PqcJavaSikeKem {
    public static void main(String[] args) throws GeneralSecurityException {
        System.out.println("PQC Sike key encapsulation mechanism (KEM)");

        Security.addProvider(new BouncyCastleProvider());
        // we do need the regular Bouncy Castle file that includes the PQC provider
        // get Bouncy Castle here: https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on
        // tested with BC version 1.68
        // and the sike library from
        // library source https://github.com/wultra/sike-java

        System.out.println("\n************************************\n" +
                "* # # SERIOUS SECURITY WARNING # # *\n" +
                "* This program is a CONCEPT STUDY  *\n" +
                "* for the algorithm                *\n" +
                "* Sike [public key encryption]     *\n" +
                "* The program is using an OUTDATED *\n" +
                "* parameter set and I cannot       *\n" +
                "* check for the correctness of the *\n" +
                "* output and other details         *\n" +
                "*                                  *\n" +
                "*    DO NOT USE THE PROGRAM IN     *\n" +
                "*    ANY PRODUCTION ENVIRONMENT    *\n" +
                "************************************");

        // data to encrypt is usually a 32 bytes long (randomly generated) AES key
        // the key will randomly generated during the encryption process

        // generation of the Sike key pair
        SikeParam sikeParam = new SikeParamP751(ImplementationType.OPTIMIZED); // gives a 32 byte long shared key
        KeyGenerator keyGenerator = new KeyGenerator(sikeParam);
        KeyPair keyPairRecipient = keyGenerator.generateKeyPair(Party.BOB); // recipient is Bob

        // test save and load keys in byte array encoding for saving and transport
        byte[] privateKeyRecipientEncoded = keyPairRecipient.getPrivate().getEncoded();
        byte[] publicKeyRecipientEncoded = keyPairRecipient.getPublic().getEncoded();
        System.out.println("\ngenerated private key length: " + privateKeyRecipientEncoded.length);
        System.out.println("generated public key length : " + publicKeyRecipientEncoded.length);
        // get keys back from encoded
        PrivateKey privateKeyRecipient = new SidhPrivateKey(sikeParam, Party.BOB, privateKeyRecipientEncoded);
        PublicKey publicKeyRecipient = new SidhPublicKey(sikeParam, publicKeyRecipientEncoded);

        System.out.println("\n* * * generate the keyToEncrypt with the public key of the recipient * * *");
        // this happens with the sender
        Sike sikeSender = new Sike(sikeParam);
        EncapsulationResult encapsulationResultSender = sikeSender.encapsulate(publicKeyRecipient);
        // sender generates the shared secret for encryption
        byte[] sharedSecretSender = encapsulationResultSender.getSecret();
        System.out.println("sharedSecretSender length:    " + sharedSecretSender.length + " data: " + bytesToHex(sharedSecretSender));
        // sender generates the EncryptedMessage = encrypted key for recipient
        EncryptedMessage encryptedMessageFromSender = encapsulationResultSender.getEncryptedMessage();
        String encryptedMessageFromSenderBase64 = base64Encoding(encryptedMessageFromSender.getEncoded());
        System.out.println("encryptedMessageFromSender length: " + encryptedMessageFromSender.getEncoded().length +
                " data (Base64): " + encryptedMessageFromSenderBase64);
        // transport the encryptedMessageFromSender along with the ciphertext to recipient

        System.out.println("\n* * * decapsulate the keyToEncrypt with the private key of the recipient * * *");
        EncryptedMessage transportedMessage = new EncryptedMessage(sikeParam, base64Decoding(encryptedMessageFromSenderBase64));
        // party B can calculate the shared secret
        Sike sikeRecipient = new Sike(sikeParam);
        byte[] sharedSecretRecipient = sikeRecipient.decapsulate(privateKeyRecipient, publicKeyRecipient, transportedMessage);
        System.out.println("sharedSecretRecipient length: " + sharedSecretRecipient.length + " data: " + bytesToHex(sharedSecretRecipient));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }
}
