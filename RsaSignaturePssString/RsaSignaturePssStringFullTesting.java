package RSA_Signature_PSS_2048;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.PSSParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaSignaturePssStringFullTesting {
    public static void main(String[] args) throws GeneralSecurityException, IOException {
        System.out.println("RSA signature PSS string"); // Probabilistic Signature Scheme

        String dataToSignString = "The quick brown fox jumps over the lazy dog";
        byte[] dataToSign = dataToSignString.getBytes(StandardCharsets.UTF_8);
        System.out.println("dataToSign: " + dataToSignString);

        // usually we would load the private and public key from a file or keystore
        // here we use hardcoded keys for demonstration - don't do this in real programs

        System.out.println("\n* * * sign the plaintext with the RSA private key * * *");
        String privateRsaKeyPem = loadRsaPrivateKeyPem();
        PrivateKey rsaPrivateKey = getPrivateKeyFromString(privateRsaKeyPem);
        System.out.println("used private key:\n" + privateRsaKeyPem);
        String signatureBase64 = rsaSignToBase64(rsaPrivateKey, dataToSign);
        System.out.println("\nsignature (Base64): " + signatureBase64);

        System.out.println("\n* * * verify the signature against the plaintext with the RSA public key * * *");
        String publicRsaKeyPem = loadRsaPublicKeyPem();
        PublicKey rsaPublicKey = getPublicKeyFromString(publicRsaKeyPem);
        System.out.println("used public key:\n" + publicRsaKeyPem);
        signatureBase64 = "6PHRis3htErseOqxoCby0z1/5n14nLjj/+2bwd5piZ4O2//cJfxjMxcaQ/0DxModN+W98IA3YE5GaHucUk5Wa4nu82k5jj0x6HPTSt14xQ0u3+SzN6QgvKHcKdgn5lpvbbXaVaTYDIVBCDCnXuakrCf7rUIbBjcQNYQg1/hXQmlzot3xnyLpmc6Hq1aWBww6+vXkOKcMBgBOODJ8HhICJqkSdgLR640RICrPJSL60s6rhmM1ZHYW24EE7J1ZtotiIabbe+Ys+fFDpnmXkwfiR8zLqqoWu+iE8WVfhvoyzELefpbTnynMNGSpmAbVkqamPnd4hoWktZ3Lq2KxjmoGIA==";
        System.out.println("\nsignature (Base64): " + signatureBase64);
        boolean signatureVerified = rsaVerifySignatureFromBase64(rsaPublicKey, dataToSign, signatureBase64);
        System.out.println("\nsignature (Base64) verified: " + signatureVerified);
    }

    private static String rsaSignToBase64(PrivateKey privateKey, byte[] messageByte)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        Signature signature = Signature.getInstance("RSASSA-PSS");
        //PSSParameterSpec params = new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1);
        signature.initSign(privateKey);
        signature.setParameter(new PSSParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), 32, 1));
        signature.update(messageByte);
        return base64Encoding(signature.sign());
    }

    private static Boolean rsaVerifySignatureFromBase64(PublicKey publicKey, byte[] messageByte, String signatureBase64)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        Signature publicSignature = Signature.getInstance("RSASSA-PSS");
        publicSignature.initVerify(publicKey);
        publicSignature.setParameter(new PSSParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), 32, 1));
        publicSignature.update(messageByte);
        return publicSignature.verify(base64Decoding(signatureBase64));
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }

    private static String loadRsaPrivateKeyPem() {
        // this is a sample key - don't worry !
        return "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDwSZYlRn86zPi9\n" +
                "e1RTZL7QzgE/36zjbeCMyOhf6o/WIKeVxFwVbG2FAY3YJZIxnBH+9j1XS6f+ewjG\n" +
                "FlJY4f2IrOpS1kPiO3fmOo5N4nc8JKvjwmKtUM0t63uFFPfs69+7mKJ4w3tk2mSN\n" +
                "4gb8J9P9BCXtH6Q78SdOYvdCMspA1X8eERsdLb/jjHs8+gepKqQ6+XwZbSq0vf2B\n" +
                "MtaAB7zTX/Dk+ZxDfwIobShPaB0mYmojE2YAQeRq1gYdwwO1dEGk6E5J2toWPpKY\n" +
                "/IcSYsGKyFqrsmbw0880r1BwRDer4RFrkzp4zvY+kX3eDanlyMqDLPN+ghXT1lv8\n" +
                "snZpbaBDAgMBAAECggEBAIVxmHzjBc11/73bPB2EGaSEg5UhdzZm0wncmZCLB453\n" +
                "XBqEjk8nhDsVfdzIIMSEVEowHijYz1c4pMq9osXR26eHwCp47AI73H5zjowadPVl\n" +
                "uEAot/xgn1IdMN/boURmSj44qiI/DcwYrTdOi2qGA+jD4PwrUl4nsxiJRZ/x7PjL\n" +
                "hMzRbvDxQ4/Q4ThYXwoEGiIBBK/iB3Z5eR7lFa8E5yAaxM2QP9PENBr/OqkGXLWV\n" +
                "qA/YTxs3gAvkUjMhlScOi7PMwRX9HsrAeLKbLuC1KJv1p2THUtZbOHqrAF/uwHaj\n" +
                "ygUblFaa/BTckTN7PKSVIhp7OihbD04bSRrh+nOilcECgYEA/8atV5DmNxFrxF1P\n" +
                "ODDjdJPNb9pzNrDF03TiFBZWS4Q+2JazyLGjZzhg5Vv9RJ7VcIjPAbMy2Cy5BUff\n" +
                "EFE+8ryKVWfdpPxpPYOwHCJSw4Bqqdj0Pmp/xw928ebrnUoCzdkUqYYpRWx0T7YV\n" +
                "RoA9RiBfQiVHhuJBSDPYJPoP34kCgYEA8H9wLE5L8raUn4NYYRuUVMa+1k4Q1N3X\n" +
                "Bixm5cccc/Ja4LVvrnWqmFOmfFgpVd8BcTGaPSsqfA4j/oEQp7tmjZqggVFqiM2m\n" +
                "J2YEv18cY/5kiDUVYR7VWSkpqVOkgiX3lK3UkIngnVMGGFnoIBlfBFF9uo02rZpC\n" +
                "5o5zebaDImsCgYAE9d5wv0+nq7/STBj4NwKCRUeLrsnjOqRriG3GA/TifAsX+jw8\n" +
                "XS2VF+PRLuqHhSkQiKazGr2Wsa9Y6d7qmxjEbmGkbGJBC+AioEYvFX9TaU8oQhvi\n" +
                "hgA6ZRNid58EKuZJBbe/3ek4/nR3A0oAVwZZMNGIH972P7cSZmb/uJXMOQKBgQCs\n" +
                "FaQAL+4sN/TUxrkAkylqF+QJmEZ26l2nrzHZjMWROYNJcsn8/XkaEhD4vGSnazCu\n" +
                "/B0vU6nMppmezF9Mhc112YSrw8QFK5GOc3NGNBoueqMYy1MG8Xcbm1aSMKVv8xba\n" +
                "rh+BZQbxy6x61CpCfaT9hAoA6HaNdeoU6y05lBz1DQKBgAbYiIk56QZHeoZKiZxy\n" +
                "4eicQS0sVKKRb24ZUd+04cNSTfeIuuXZrYJ48Jbr0fzjIM3EfHvLgh9rAZ+aHe/L\n" +
                "84Ig17KiExe+qyYHjut/SC0wODDtzM/jtrpqyYa5JoEpPIaUSgPuTH/WhO3cDsx6\n" +
                "3PIW4/CddNs8mCSBOqTnoaxh\n" +
                "-----END PRIVATE KEY-----";
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

    public static PrivateKey getPrivateKeyFromString(String key) throws GeneralSecurityException {
        String privateKeyPEM = key;
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replaceAll("[\\r\\n]+", "");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        PrivateKey privKey = (PrivateKey) kf.generatePrivate(keySpec);
        return privKey;
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
}
