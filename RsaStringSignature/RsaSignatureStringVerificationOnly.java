package RSA_Signature_2048;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaSignatureStringVerificationOnly {
    public static void main(String[] args) throws GeneralSecurityException, IOException {
        System.out.println("RSA signature string verification only");

        String dataToSignString = "The quick brown fox jumps over the lazy dog";
        byte[] dataToSign = dataToSignString.getBytes(StandardCharsets.UTF_8);
        System.out.println("dataToSign: " + dataToSignString);

        // paste signature here:
        String signatureBase64 = "vCSB4744p30IBl/sCLjvKm2nix7wfScQn99YX9tVIDNQIvU3QnSCLc2cF+J6R9WMRIXOsY94MxjKCQANW0CuaSs+w31ePHaounFVnmXyY092SicZrtpwlxw2CHqJ0NSyciDpxlRId1vjKlp9E5IJmYtVMtL2hfb711P+nb+m+1sPplNXPpJpdnWIzfLsDMVxCkplAdrcoH2HuWgOtOCHAf3vWbUC/vkvi388NT1UXJRPoERM0m1v11ogP9DiycMdoJxg3fbdH3HknbR02MLNEr7q4ZMzlrKxnYChwp2hnnBvJDcXpPDnQz7sG8zrim1nL/PS8CRG5lxhYYAZqTc+Vg==";

        // usually we would load the public key from a file or keystore
        // here we use hardcoded key for demonstration - don't do this in real programs

        System.out.println("\n* * * verify the signature against the plaintext with the RSA public key * * *");
        String publicRsaKeyPem = loadRsaPublicKeyPem();
        PublicKey rsaPublicKey = getPublicKeyFromString(publicRsaKeyPem);
        System.out.println("used public key:\n" + publicRsaKeyPem);
        boolean signatureVerified = rsaVerifySignatureFromBase64(rsaPublicKey, dataToSign, signatureBase64);
        System.out.println("\nsignature (Base64) verified: " + signatureVerified);
    }

    private static Boolean rsaVerifySignatureFromBase64(PublicKey publicKey, byte[] messageByte, String signatureBase64)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(messageByte);
        return publicSignature.verify(base64Decoding(signatureBase64));
    }

    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
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
}
