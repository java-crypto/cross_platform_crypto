package EC_Signature_secp256r1_Der_String;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class EcSignatureDerStringFull {
    public static void main(String[] args) throws GeneralSecurityException, IOException {
        System.out.println("EC signature string (ECDSA with SHA256) DER-encoding");

        String dataToSignString = "The quick brown fox jumps over the lazy dog";
        byte[] dataToSign = dataToSignString.getBytes(StandardCharsets.UTF_8);
        System.out.println("dataToSign: " + dataToSignString);

        // usually we would load the private and public key from a file or keystore
        // here we use hardcoded keys for demonstration - don't do this in real programs

        System.out.println("\n* * * sign the plaintext with the EC private key * * *");
        String privateEcKeyPem = loadEcPrivateKeyPem();
        PrivateKey ecPrivateKey = getEcPrivateKeyFromString(privateEcKeyPem);
        System.out.println("used private key:\n" + privateEcKeyPem);
        String signatureBase64 = ecSignToBase64(ecPrivateKey, dataToSign);
        System.out.println("\nsignature (Base64): " + signatureBase64);

        System.out.println("\n* * * verify the signature against the plaintext with the EC public key * * *");
        String signatureReceivedBase64 = signatureBase64;
        String publicRsaKeyPem = loadEcPublicKeyPem();
        PublicKey rsaPublicKey = getEcPublicKeyFromString(publicRsaKeyPem);
        System.out.println("used public key:\n" + publicRsaKeyPem);
        boolean signatureVerified = ecVerifySignatureFromBase64(rsaPublicKey, dataToSign, signatureReceivedBase64);
        System.out.println("\nsignature (Base64) verified: " + signatureVerified);
    }

    private static String ecSignToBase64(PrivateKey privateKey, byte[] messageByte)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(privateKey);
        signature.update(messageByte);
        return base64Encoding(signature.sign());
    }

    private static Boolean ecVerifySignatureFromBase64(PublicKey publicKey, byte[] messageByte, String signatureBase64)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        Signature publicSignature = Signature.getInstance("SHA256withECDSA");
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

    private static String loadEcPrivateKeyPem() {
        // this is a sample key - don't worry !
        return "-----BEGIN EC PRIVATE KEY-----\n" +
                "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCAU2f8tzo99Z1HoxJlY\n" +
                "96yXUhFY5vppVjw1iPKRfk1wHA==\n" +
                "-----END EC PRIVATE KEY-----";
    }

    private static String loadEcPublicKeyPem() {
        // this is a sample key - don't worry !
        return "-----BEGIN PUBLIC KEY-----\n" +
                "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEzb7yAFWup6iDqJiEq764rAumsV2M\n" +
                "rspZxaP3WGpwHaC4Uff3N4UbJZF7Zac1c6W7KJl0eeCP0205Q3UEpwxndQ==\n" +
                "-----END PUBLIC KEY-----";
    }

    public static PrivateKey getEcPrivateKeyFromString(String key) throws GeneralSecurityException {
        String privateKeyPEM = key;
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN EC PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replace("-----END EC PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replaceAll("[\\r\\n]+", "");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("EC");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        PrivateKey privKey = (PrivateKey) kf.generatePrivate(keySpec);
        return privKey;
    }

    public static PublicKey getEcPublicKeyFromString(String key) throws GeneralSecurityException {
        String publicKeyPEM = key;
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replaceAll("[\\r\\n]+", "");
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("EC");
        PublicKey pubKey = (PublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        return pubKey;
    }
}
