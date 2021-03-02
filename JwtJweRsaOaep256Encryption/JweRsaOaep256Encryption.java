package JWT_RSA_Encryption;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class JweRsaOaep256Encryption {
    public static void main(String[] args) throws ParseException, JOSEException {
        System.out.println("JWT JWE RSA-OAEP-256 AES GCM 256 encryption");
        // https://mvnrepository.com/artifact/com.nimbusds/nimbus-jose-jwt version 9.4.1 nimbus-jose-jwt-9.4.1.jar
        // https://mvnrepository.com/artifact/com.github.stephenc.jcip/jcip-annotations version 1.0-1 jcip-annotations-1.0-1.jar
        // https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on version 1.67 bcprov-jdk15on-1.67.jar
        // https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk15on version 1.67 bcpkix-jdk15on-1.67.jar

        String jweKeyAlgorithm = "RSA-OAEP-256"; // supported key algorithms: RSA-OAEP, RSA-OAEP-256, RSA1_5
        String jweEncryptionAlgorithm = "A256GCM"; // supported encryption algorithms: A128GCM, A192GCM, A256GCM
        System.out.println("jwe key algorithm:        " + jweKeyAlgorithm);
        System.out.println("jwe encryption algorithm: " + jweEncryptionAlgorithm);

        // build the payload to encrypt
        String dataToEncrypt = "The quick brown fox jumps over the lazy dog";
        String issuer = "https://java-crypto.github.io/cross_platform_crypto/";
        String subject = "JWE RSA-OAEP & AES GCM encryption";
        int tokenExpirationInSeconds = 120;

        // build a JWT payload object
        JWTClaimsSet jwtPayloadObject = buildPayloadObject(dataToEncrypt, subject, issuer, tokenExpirationInSeconds);
        System.out.println("jwtPayloadObject: " + jwtPayloadObject);

        System.out.println("\n* * * encrypt the payload with recipient\'s public key * * *");
        // get public key from recipient
        JWK rsaPublicKeyFromRecipient = JWK.parseFromPEMEncodedObjects(loadRsaPublicKeyPem());
        System.out.println("rsaPublicKey in JWK format:\n" + rsaPublicKeyFromRecipient);

        // encrypt the data and build the encrypted jwe token
        String jweTokenBase64Url = jweRsaEncryptToBase64UrlToken(rsaPublicKeyFromRecipient, jweKeyAlgorithm, jweEncryptionAlgorithm, jwtPayloadObject);
        System.out.println("jweToken (Base64Url encoded):\n" + jweTokenBase64Url);

        System.out.println("\n* * * decrypt the payload with recipient\'s private key * * *");

        // get private key
        JWK rsaPrivateKey = JWK.parseFromPEMEncodedObjects(loadRsaPrivateKeyPem());
        System.out.println("rsaPrivateKey in JWK format:\n" + rsaPrivateKey);
        EncryptedJWT jweDecryptedPayload = jweRsaDecryptFromBase64UrlToken(rsaPrivateKey, jweTokenBase64Url);
        System.out.println("jweDecryptedPayload: " + jweDecryptedPayload.getJWTClaimsSet());

        // now you could check the expiration like this:
        System.out.println("\nexample for checking token expiration");
        printJweExpirationDates(jweDecryptedPayload);
        // or print the header object
        System.out.println("\nexample for printing the header object");
        printJweHeaderObject(jweDecryptedPayload);
    }

    private static String jweRsaEncryptToBase64UrlToken(JWK rsaPublicKeyFromRecipient, String jweKeyAlgorithm, String jweEncryptionAlgorithm, JWTClaimsSet jwtPayloadObject) throws JOSEException {
        // build a header depending on the key algorithm
        JWEAlgorithm keyAlgorithm;
        switch (jweKeyAlgorithm) {
            case "RSA-OAEP":
                keyAlgorithm = JWEAlgorithm.RSA_OAEP; // deprecated
                break;
            case "RSA-OAEP-256":
                keyAlgorithm = JWEAlgorithm.RSA_OAEP_256;
                break;
            case "RSA1_5":
                keyAlgorithm = JWEAlgorithm.RSA1_5; // deprecated
                break;
            default:
                keyAlgorithm = null; // unsupported
                break;
        }
        EncryptionMethod encryptionAlgorithm;
        switch (jweEncryptionAlgorithm) {
            case "A128GCM": encryptionAlgorithm =  EncryptionMethod.A128GCM;
                break;
            case "A192GCM": encryptionAlgorithm = EncryptionMethod.A192GCM;
                break;
            case "A256GCM": encryptionAlgorithm = EncryptionMethod.A256GCM;
                break;
            default: encryptionAlgorithm = null; // unsupported
                break;
        }
        // check for nulled keyAlgorithm and encryptionAlgorithm
        if (keyAlgorithm == null || encryptionAlgorithm == null) return "*** not supported algorithm";

        // Request JWT encrypted with parameters
        JWEHeader jweHeader = new JWEHeader(keyAlgorithm, encryptionAlgorithm);
        // Create the encrypted JWT object
        EncryptedJWT encryptedJwt = new EncryptedJWT(jweHeader, jwtPayloadObject);
        // Create an encrypter with the specified public RSA key
        RSAEncrypter encrypter = new RSAEncrypter((RSAKey) rsaPublicKeyFromRecipient);
        // Do the actual encryption
        encryptedJwt.encrypt(encrypter);
        // Serialise to JWT compact form
        return encryptedJwt.serialize();
    }

    private static EncryptedJWT jweRsaDecryptFromBase64UrlToken(JWK rsaPrivateKey, String jweTokenBase64Url) throws ParseException, JOSEException {
        EncryptedJWT encryptedJWT = EncryptedJWT.parse(jweTokenBase64Url);
        // Create a decrypter with the specified private RSA key
        RSADecrypter decrypter = new RSADecrypter((RSAKey) rsaPrivateKey);
        // decrypt
        encryptedJWT.decrypt(decrypter);
        return encryptedJWT;
    }

    private static JWTClaimsSet buildPayloadObject(String data, String subject, String issuer, int tokenExpirationInSeconds) {
        Date dateNow = new Date(new Date().getTime());
        Date dateExpiration = addSeconds(dateNow, tokenExpirationInSeconds);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .claim("dat", data)
                .subject(subject)
                .issuer(issuer)
                .issueTime(dateNow)
                .expirationTime(dateExpiration)
                .build();
        return claimsSet;
    }

    private static void printJweExpirationDates(EncryptedJWT decryptedJWT) throws ParseException {
        System.out.println("Expiration Time: " + decryptedJWT.getJWTClaimsSet().getExpirationTime());
        System.out.println("Issue Time:      " + decryptedJWT.getJWTClaimsSet().getIssueTime());
        System.out.println("actual Time:     " + new Date());
        System.out.println("jweToken valid:  " + new Date().before(decryptedJWT.getJWTClaimsSet().getExpirationTime()));
    }

    private static void printJweHeaderObject(EncryptedJWT decryptedJWT) {
        System.out.println(decryptedJWT.getHeader());
    }

    private static Date addSeconds(Date date, Integer seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
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
}
