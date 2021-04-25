package RSA_Key_Loading_encrypted;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.pkcs.jcajce.JcePKCSPBEInputDecryptorProviderBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaLoadEncryptedPrivateKeyBc {
    // you need the two libraries from Bouncy Castle:
    // bcprov-jdk15on-1.68 & bcpkix-jdk15on-1.68.jar
    public static void main(String[] args) throws IOException, GeneralSecurityException, PKCSException {
        Security.addProvider(new BouncyCastleProvider());
        System.out.println("Load RSA PKCS8 encrypted private key AES256-CBC PBKDF2 HMAC with SHA-256\n");

        String dataToSignString = "The quick brown fox jumps over the lazy dog";
        byte[] dataToSign = dataToSignString.getBytes(StandardCharsets.UTF_8);
        System.out.println("dataToSign: " + dataToSignString);

        String passphrase = "123456"; // passphrase for encrypted RSA private key

        // usually we would load the private and public key from a file or keystore
        // here we use hardcoded keys for demonstration - don't do this in real programs

        System.out.println("\n* * * sign the plaintext with the encrypted RSA private key * * *");
        String privateRsaKeyPem = loadRsaPrivateKeyEncryptedPem();
        PrivateKey rsaPrivateKey = readRsaPrivateKeyEncryptedPbes2Aes256CbcBc(privateRsaKeyPem, passphrase);
        String signatureBase64 = rsaSignToBase64(rsaPrivateKey, dataToSign);
        System.out.println("\nsignature (Base64): " + signatureBase64);

        System.out.println("\n* * * verify the signature against the plaintext with the RSA public key * * *");
        String publicRsaKeyPem = loadRsaPublicKeyPem();
        PublicKey rsaPublicKey = getPublicKeyFromString(publicRsaKeyPem);
        boolean signatureVerified = rsaVerifySignatureFromBase64(rsaPublicKey, dataToSign, signatureBase64);
        System.out.println("\nsignature (Base64) verified: " + signatureVerified);
    }

    private static String rsaSignToBase64(PrivateKey privateKey, byte[] messageByte)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(messageByte);
        return base64Encoding(signature.sign());
    }

    private static Boolean rsaVerifySignatureFromBase64(PublicKey publicKey, byte[] messageByte, String signatureBase64)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(messageByte);
        return publicSignature.verify(base64Decoding(signatureBase64));
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }

    private static String loadRsaPrivateKeyEncryptedPem() {
        // this is a sample key - don't worry !
        // passphrase for decryption: 123456
        // passphrase scheme: PBES2
        // password based key derivation: PBKDF2 with HMAC and SHA-256
        // encryption algorithm: AES256-CBC
        return "-----BEGIN ENCRYPTED PRIVATE KEY-----\n" +
                "MIIFLTBXBgkqhkiG9w0BBQ0wSjApBgkqhkiG9w0BBQwwHAQIUo/aHb7/5jUCAggA\n" +
                "MAwGCCqGSIb3DQIJBQAwHQYJYIZIAWUDBAEqBBCbpiqeplyyDN85uu5yYqpFBIIE\n" +
                "0Iqvy2v42HeASj6B25llJjNOWvT5djjDiAg7BFsuOhMq10mTC7gWduXBai5llUBH\n" +
                "qF4+GgqCeedwT24YkdR4wAqtc2shcmq+2xC70N/XrSg9geGehmNwDx35byhPjouk\n" +
                "XwA5tgVsKtxTob/xjPYhjL5J1KpWOPQhdnHPGfGlWedTyTKboRfgkZ/Yds5EW6k7\n" +
                "mO9wt/iXcym6QgaJTswa1ntGPVaKMYvXVRtNnphsI9M7njXBYO52vfujUaeKn8Xe\n" +
                "qmuEAKGFhhu+r7/WWyQl+Zq2KIoD5ImOxuXScCZVH3+76AxXSwDJzB6ZG1SowSy+\n" +
                "YHbfkmvK2H6YdFshEfI/coMmu9E3wDKuCY7rgb70lHLcpvbSMa88vCFl3d/ZfmFl\n" +
                "w4TwG3vEVR9wfbXAuAw6XTSLBODcF4ifZnyXI2Dr+fQbQd/4o8w05hDiPkXRgnYY\n" +
                "e/4nFbTGGKvatZ7LOLa62isiGtQ4nprDpTapLMeJCFSgQN1jlZMVvV6mKUfrCJ69\n" +
                "3bESlyYGAN0MAx4KeMcCRwuEXWhOkLMFW0gvRp1udiMxJ8SnWfseYTr2FPUFQlFk\n" +
                "2ZhHsSO0Pg/cszUSuCX0q2ZW0ZZOiihJsmiQNXlbsRog9m8vaxUfA00v/q6iryGE\n" +
                "ApCiQd653ikukRhk/nQEY6OV4byLyI9zESF/pIdsrFnEtGSd0tggGsREtn11D8+7\n" +
                "SW0F0Es4P4UUPjiaYzyB58gcZJWxcoq63NOTUaicvTjNbzQu1+4y+2xhFe89e09w\n" +
                "PFSGerjSV/uVtVTH2hb79mAmlyLvaikQfQq2nvpCuZy7wZjhPA3D03WFTCbrYS7P\n" +
                "PfckAICCBCIB97vPsWVAviOVOrG5NDbIag+/3YhZb2gyuR2/E0XI1NdXO63NqU9R\n" +
                "B6VUUNdSvv4djA30nVjARlh3LOZmTy50OIj/QDOEbBrtFH79eHHxHRiK0+Q0A8Ch\n" +
                "EoDDz7bMKBcpiS3vXnI2UsrZX/QIuRjypc003uVm4MvdUwv0j3UGZr9zRGctqWf0\n" +
                "YBRkoIELRA45hQn0Ln3+wjAcjLRPpeuPgVAq7tDDLDmP0i/m8UbjNorhdv4CrT/u\n" +
                "P+keTW7v9Nuov1IXG4W6wKipYRzMjiYrTL5TaXPi9z63b6RQVShAEqSTtsPg5nDy\n" +
                "CZkt5NPBltq+3InrsTlbKSQGYn6wyEQeBYezD3CLkIliDn7UM4BDn+k5YonNynU4\n" +
                "emxbU4wyBEXoMD8+aBMf+bKKQXU97Ts9p7is+Ogqyop2mbMOebIxCKayizlb7/hp\n" +
                "Qs2ThBP84OrniUEeKVAkdykZyrdCoatgXVJngDwwEkSwOrEYZ0sJ48qPQMTN3kVq\n" +
                "r5XJjX3m8mpXdz0s9VbhT/rWSdQMX82msof4c6+RghEbiHFG4B0el5vrJaAROb69\n" +
                "3OnoU8mYKBlFjbO1rJJpGS5h/v+ysqWxDxfPPLD86UsgDAjr5C7W3K8eYB1UD/wP\n" +
                "n91TuTdrLKhLE93VsCyXnsLm44Td3MBv89Fr2CMF/Jz15Nfrg3yvR7bKvkNs3uVM\n" +
                "FVO3kqtmUMC/RsUr3NIE/RZ2sS+cjLMDE2zBUgHnDpNQB6WSuwqEI9prAWxEc1jS\n" +
                "SwyNkvstftK35nfeI3FNP0R94+kSKzGmiQh5yl3ufAVi\n" +
                "-----END ENCRYPTED PRIVATE KEY-----";
    }

    private static String loadRsaPublicKeyPem() {
        // this is a sample key - don't worry !
        return "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+\n" +
                "0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9\n" +
                "iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT\n" +
                "/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8\n" +
                "01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB\n" +
                "ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g\n" +
                "QwIDAQAB\n" +
                "-----END PUBLIC KEY-----";
    }

    public static PublicKey getPublicKeyFromString(String key) throws GeneralSecurityException {
        String publicKeyPEM = key;
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replaceAll("[\\r\\n]+", "");
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pubKey = (PublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        return pubKey;
    }

    static public PrivateKey readRsaPrivateKeyEncryptedPbes2Aes256CbcBc(String pemString, String password)
            throws IOException, PKCSException {
        PrivateKeyInfo pki;
        try (PEMParser pemParser = new PEMParser(new StringReader(pemString))) {
            Object o = pemParser.readObject();
            if (o instanceof PKCS8EncryptedPrivateKeyInfo) { // encrypted private key in pkcs8-format
                //System.out.println("key in pkcs8 encoding");
                PKCS8EncryptedPrivateKeyInfo epki = (PKCS8EncryptedPrivateKeyInfo) o;
                //System.out.println("epki:" + epki.getEncryptionAlgorithm().getAlgorithm());
                JcePKCSPBEInputDecryptorProviderBuilder builder =
                        new JcePKCSPBEInputDecryptorProviderBuilder().setProvider("BC");
                InputDecryptorProvider idp = builder.build(password.toCharArray());
                pki = epki.decryptPrivateKeyInfo(idp);
            } else {
                throw new PKCSException("Invalid encrypted private key class: " + o.getClass().getName());
            }
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            return converter.getPrivateKey(pki);
        }
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
}
