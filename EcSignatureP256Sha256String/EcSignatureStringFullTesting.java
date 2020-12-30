package EC_Signature_secp256r1_String;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class EcSignatureStringFullTesting {
    public static void main(String[] args) throws GeneralSecurityException, IOException {
        System.out.println("EC signature string");

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
        byte[] signatureJava = Base64.getDecoder().decode(signatureBase64);
        System.out.println("signatureJava length: " + signatureJava.length);
        Files.write(Paths.get("ec_sig_java.dat"), signatureJava);

        String signatureP1363Base64 = ecSignP1363ToBase64(ecPrivateKey, dataToSign);
        System.out.println("\nsignature P1363 (Base64): " + signatureP1363Base64);

        System.out.println("\n* * * verify the signature against the plaintext with the EC public key * * *");
        String publicRsaKeyPem = loadEcPublicKeyPem();
        PublicKey rsaPublicKey = getEcPublicKeyFromString(publicRsaKeyPem);
        System.out.println("used public key:\n" + publicRsaKeyPem);
        //signatureBase64 = "j6oY538oBKJ2H5xrt8ObLCjT4F2TjKt6DhAu460192PaGiDKs6O4yU3e27hKAbMzZcke2pHE+mPVEYu3CqR1sg==";

        System.out.println("\nsignature (Base64): " + signatureBase64);
        byte[] signatureWebcrypto = Base64.getDecoder().decode(signatureBase64);
        System.out.println("signatureWebcrypto length: " + signatureWebcrypto.length);
        Files.write(Paths.get("ec_sig_webcrypto.dat"), signatureWebcrypto);

        boolean signatureVerified = ecVerifySignatureFromBase64(rsaPublicKey, dataToSign, signatureBase64);
        System.out.println("\nsignature (Base64) verified: " + signatureVerified);
        System.out.println("\ndata from C#");
        String signatureCsharpBase64 = "va/umvFM6B7Ay/K2cL6Sxtc+9rW0p8gXzCpvUc9XYYXJJoW3wDxduHFYr+7D3VeDQxXf/BgoAWATWG4pDWQ+Qw==";
        boolean signatureCsharpVerified = ecVerifySignatureP1363FromBase64(rsaPublicKey, dataToSign, signatureCsharpBase64);
        System.out.println("\nsignature Csharp (Base64) verified: " + signatureCsharpVerified);

        System.out.println("\ndata from PHP");
        String signaturePhpBase64 = "LNAQC+dDAgmhcIN1Xx4iLllE0BWa42VduPxi5bO1OAZ6czwMJy/KVJrKpMDSac22WSN18oZAd8cRyWTytq01wA==";
        boolean signaturePhpVerified = ecVerifySignatureP1363FromBase64(rsaPublicKey, dataToSign, signaturePhpBase64);
        System.out.println("\nsignature PHP (Base64) verified: " + signaturePhpVerified);

        System.out.println("\ndata from NodeJs Crypto");
        String signatureNodeJsCryptoBase64 = "clF1CxteH8JZAwK0e9EllGlOl/BbbMRkYyVQo380u7CLrmQrsJpOpyM86M1BjvXgDxDsAJ+niDr9mboV8AzyHQ==";
        boolean signatureNodeJsCryptoVerified = ecVerifySignatureP1363FromBase64(rsaPublicKey, dataToSign, signatureNodeJsCryptoBase64);
        System.out.println("\nsignature NodeJs Crypto (Base64) verified: " + signatureNodeJsCryptoVerified);
    }

    private static String ecSignToBase64(PrivateKey privateKey, byte[] messageByte)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        //Signature signature = Signature.getInstance("SHA256withECDSAinP1363format");
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(privateKey);
        signature.update(messageByte);
        return base64Encoding(signature.sign());
    }

    private static String ecSignP1363ToBase64(PrivateKey privateKey, byte[] messageByte)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        Signature signature = Signature.getInstance("SHA256withECDSAinP1363format");
        //Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(privateKey);
        signature.update(messageByte);
        return base64Encoding(signature.sign());
    }

    private static Boolean ecVerifySignatureFromBase64(PublicKey publicKey, byte[] messageByte, String signatureBase64)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        //Signature publicSignature = Signature.getInstance("SHA256withECDSAinP1363format");//withECDSAinP1363format
        Signature publicSignature = Signature.getInstance("SHA256withECDSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(messageByte);
        return publicSignature.verify(base64Decoding(signatureBase64));
    }

    private static Boolean ecVerifySignatureP1363FromBase64(PublicKey publicKey, byte[] messageByte, String signatureBase64)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        Signature publicSignature = Signature.getInstance("SHA256withECDSAinP1363format");//withECDSAinP1363format
        // Signature publicSignature = Signature.getInstance("SHA256withECDSA");
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
