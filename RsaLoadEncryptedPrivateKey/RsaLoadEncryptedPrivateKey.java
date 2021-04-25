package RSA_Key_Loading_encrypted;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class RsaLoadEncryptedPrivateKey {

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        System.out.println("Load RSA PKCS8 encrypted private key AES256-CBC PBKDF2 HMAC with SHA-256\n");

        String dataToSignString = "The quick brown fox jumps over the lazy dog";
        byte[] dataToSign = dataToSignString.getBytes(StandardCharsets.UTF_8);
        System.out.println("dataToSign: " + dataToSignString);

        String passphrase = "123456"; // passphrase for encrypted RSA private key

        // usually we would load the private and public key from a file or keystore
        // here we use hardcoded keys for demonstration - don't do this in real programs

        System.out.println("\n* * * sign the plaintext with the encrypted RSA private key * * *");
        String privateRsaKeyPem = loadRsaPrivateKeyEncryptedPem();
        PrivateKey rsaPrivateKey = readRsaPrivateKeyEncryptedPbes2Aes256Cbc(privateRsaKeyPem, passphrase);
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

    private static PrivateKey readRsaPrivateKeyEncryptedPbes2Aes256Cbc (String pemString, String password)
            throws IOException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, InvalidKeySpecException,
            BadPaddingException, IllegalBlockSizeException {
        // note: this function is hardcoded to read an encrypted RSA private key with the following spcifications:
        // key format: PKCS#8, key encoding: PEM encoded, password encryption scheme: PBES2 [OpenSSL -v2],
        // password derivation method: PBKDF2 with HMAC SHA-256, maximum iterations: 8388607,
        // encryption algorithm: AES-256-CBC
        pemString = pemString.replace("-----BEGIN ENCRYPTED PRIVATE KEY-----", "");
        pemString = pemString.replace("-----END ENCRYPTED PRIVATE KEY-----", "");
        byte[] orgKeyByte = Base64.getMimeDecoder().decode(pemString);
        int orgKeyByteLength = orgKeyByte.length;
        EncryptedPrivateKeyInfo pkInfo = new EncryptedPrivateKeyInfo(orgKeyByte);
        byte[] encryptedKeyByte = pkInfo.getEncryptedData();
        int encryptedKeyByteLength = encryptedKeyByte.length;
        int headerByteLength = orgKeyByteLength - encryptedKeyByteLength;
        // all searches only in header
        byte[] headerByte = Arrays.copyOf(orgKeyByte, headerByteLength);
        // fixed identifier
        final String IDENTIFIER_PKCS5PBES2          = "2A864886F70D01050D";
        final String IDENTIFIER_PKCS5PBKDF2         = "2A864886F70D01050C";
        final String IDENTIFIER_PKCS5HMACWITHSHA256 = "2A864886F70D020905";
        final String IDENTIFIER_AES256CBC           = "60864801650304012a";
        byte[] IDENTIFIER_PKCS5PBES2_byte = hexStringToByteArray(IDENTIFIER_PKCS5PBES2);
        byte[] IDENTIFIER_PKCS5PBKDF2_byte = hexStringToByteArray(IDENTIFIER_PKCS5PBKDF2);
        byte[] IDENTIFIER_PKCS5HMACWITHSHA256_byte = hexStringToByteArray(IDENTIFIER_PKCS5HMACWITHSHA256);
        byte[] IDENTIFIER_AES256CBC_byte = hexStringToByteArray(IDENTIFIER_AES256CBC);
        int posIDENTIFIER_PKCS5PBES2 = indexOf(headerByte, IDENTIFIER_PKCS5PBES2_byte);
        int posIDENTIFIER_PKCS5PBKDF2 = indexOf(headerByte, IDENTIFIER_PKCS5PBKDF2_byte);
        int posIDENTIFIER_PKCS5HMACWITHSHA256 = indexOf(headerByte, IDENTIFIER_PKCS5HMACWITHSHA256_byte);
        int posIDENTIFIER_AES256CBC = indexOf(headerByte, IDENTIFIER_AES256CBC_byte);
        // vanity check - all values are > 0
        if (posIDENTIFIER_PKCS5PBES2 < 1 || posIDENTIFIER_PKCS5PBKDF2 < 1 || posIDENTIFIER_PKCS5HMACWITHSHA256 < 1 || posIDENTIFIER_AES256CBC < 1) {
            System.out.println("*** SYSTEM ERROR ***");
            System.out.println("One or more identifier not found in header.");
            System.out.println("This function ONLY works with PBES2 encryption scheme, PBKDF2 key derivation, HMACwithSHA256 hashing and AES256CBC encryption.");
            System.out.println("System halted.");
            System.exit(0);
        }
        // find salt and iterations after posIDENTIFIER_PKCS5PBKDF2 1c = length can differ depending on iterations length
        int posSALT = posIDENTIFIER_PKCS5PBKDF2 + (9 + 4); // pos is at the beginning of identifier
        byte[] saltByte = Arrays.copyOfRange(headerByte, posSALT, (posSALT + 8)); // salt is 8 bytes long
        // iteration starts 3 bytes after end of salt but length may differ, so we need to check length tag + 2 after end of salt
        int iterationLength = (int) headerByte[(posSALT + 8 + 1)];
        byte[] iterationByte = Arrays.copyOfRange(headerByte, (posSALT + 8 + 2),  (posSALT + 8 + 2 + iterationLength));
        int iterations = new BigInteger(iterationByte).intValueExact();
        // find iv after end of IDENTIFIER_AES256CBC
        byte[] ivByte = Arrays.copyOfRange(headerByte, (posIDENTIFIER_AES256CBC + 9 + 2), (posIDENTIFIER_AES256CBC + 9 + 2 + 16));
        // now everything is available for decryption
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), saltByte, iterations, 32 * 8);
        byte[] key = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivByte);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedKey = cipher.doFinal(encryptedKeyByte);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decryptedKey);
        PrivateKey privateKey = (PrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        return privateKey;
    }

    // https://stackoverflow.com/a/21341168/8166854 answered Jan 24 '14 at 19:44 morpheus05
    public static int indexOf(byte[] outerArray, byte[] smallerArray) {
        for(int i = 0; i < outerArray.length - smallerArray.length+1; ++i) {
            boolean found = true;
            for(int j = 0; j < smallerArray.length; ++j) {
                if (outerArray[i+j] != smallerArray[j]) {
                    found = false;
                    break;
                }
            }
            if (found) return i;
        }
        return -1;
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
