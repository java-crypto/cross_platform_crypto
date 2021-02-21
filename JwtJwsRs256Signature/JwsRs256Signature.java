package JWT_RSA_Signature;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
// https://mvnrepository.com/artifact/com.nimbusds/nimbus-jose-jwt version 9.4.1 nimbus-jose-jwt-9.4.1.jar
// https://mvnrepository.com/artifact/com.github.stephenc.jcip/jcip-annotations version 1.0-1 jcip-annotations-1.0-1.jar
// https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on version 1.68 bcprov-jdk15on-1.68.jar
// https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk15on version 1.68 bcpkix-jdk15on-1.68.jar

public class JwsRs256Signature {
    public static void main(String[] args) throws JOSEException, ParseException {
        System.out.println("JWT JWS RS256 RSA PKCS#1.5 signature with SHA-256");
        Security.addProvider(new BouncyCastleProvider());

        JWSAlgorithm jwsAlgorithm = JWSAlgorithm.RS256;

        // usually we would load the private and public key from a file or keystore
        // here we use hardcoded keys for demonstration - don't do this in real programs

        System.out.println("\n* * * sign the payload object with the RSA private key * * *");
        // payload data
        String issuer = "https://java-crypto.github.io/cross_platform_crypto/";
        String subject = "JWT RS256 signature";
        int tokenExpirationInSeconds = 120;

        // build a JWT payload object
        JWTClaimsSet jwtPayloadObject = buildPayloadObject(subject, issuer, tokenExpirationInSeconds);
        System.out.println("jwtClaimsSet: " + jwtPayloadObject);

        // read RSA private key in a JWK object
        JWK jwkRsaPrivateKey = JWK.parseFromPEMEncodedObjects(loadRsaPrivateKeyPem());
        System.out.println("privateKey in jwk format:\n" + jwkRsaPrivateKey);

        // sign the payload
        String jwsSignedToken = jwsSignatureToString(jwsAlgorithm, jwkRsaPrivateKey, jwtPayloadObject);
        System.out.println("signed jwsToken:\n" + jwsSignedToken);

        // send the token to the recipient
        System.out.println("\n* * * verify the jwsToken with the RSA public key * * *");
        String jwsSignedTokenReceived = jwsSignedToken;

        // read the RSA public key in a JWK object
        JWK jwkRsaPublicKey = JWK.parseFromPEMEncodedObjects(loadRsaPublicKeyPem());
        System.out.println("publicKey in jwk format:\n" + jwkRsaPublicKey);

        // show payloadObject
        System.out.println("payloadObject: " + printPayloadObject(jwsSignedTokenReceived));

        // check for correct signature
        System.out.println("\ncheck signature with matching public key");
        boolean tokenVerified = jwsSignatureVerification(jwkRsaPublicKey, jwsSignedTokenReceived);
        System.out.println("the token's signature is verified: " + tokenVerified);
        System.out.println("\ncheck signature with not matching public key");
        JWK jwkRsaPublicKey2 = JWK.parseFromPEMEncodedObjects(loadRsaPublicKeyPem2());
        boolean tokenNotVerified = jwsSignatureVerification(jwkRsaPublicKey2, jwsSignedTokenReceived);
        System.out.println("the token's signature is verified: " + tokenNotVerified);

        // check for expiration
        System.out.println("\ncheck expiration that is valid");
        boolean tokenValid = checkExpiration(jwsSignedTokenReceived);
        System.out.println("the token is valid and not expired: " + tokenValid);
        System.out.println("\ncheck expiration that is invalid");
        String jwsSignedTokenReceivedExpired = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvamF2YS1jcnlwdG8uZ2l0aHViLmlvXC9jcm9zc19wbGF0Zm9ybV9jcnlwdG9cLyIsInN1YiI6IkpXVCBSUzI1NiBzaWduYXR1cmUiLCJleHAiOjE2MTMzODY4MTMsImlhdCI6MTYxMzM4NjY5M30.2ZrDOCWk7QUKfNd78exKDlDR5Z2_IgBxhmu8J6AjOLmYw7kzZ_kPR2FvuLI_yHnOWomw9KdD2lcH1sGTNhm4M-_D1hlKL3AyWSoujHqTV0beGRcmazuEGBSy2T4-ZLpmn6lruxrHnuhJP_IzwUQXgTMVz90eVtvEGgs9sgKm8EvmS5UTwiXwvNpzjPUgxTmHNwVafToTVS-e8hvdLVPKvWpFxG2JluIIsGpoX5Uv37eS4DCf0IQH9cx08VMifFHgnzFUTJlnSJ0IOzhG00AwqKNsfH0EhwreNPArxjMerfNRYYJsx3FQxzLrslVlhLBL4S5WkEUfCdaWm3F1ptAVcw";
        boolean tokenValidExpired = checkExpiration(jwsSignedTokenReceivedExpired);
        System.out.println("the token is valid and not expired: " + tokenValidExpired);
    }

    private static JWTClaimsSet buildPayloadObject(String subject, String issuer, int tokenExpirationInSeconds) {
        Date dateNow = new Date(new Date().getTime());
        Date dateExpiration = addSeconds(dateNow, tokenExpirationInSeconds);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(issuer)
                .issueTime(dateNow)
                .expirationTime(dateExpiration)
                .build();
        return claimsSet;
    }

    private static Date addSeconds(Date date, Integer seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    private static String jwsSignatureToString(JWSAlgorithm jwsAlgorithm, JWK privateKey, JWTClaimsSet payloadObject)
            throws JOSEException {
        JWSSigner signer = new RSASSASigner((RSAKey) privateKey);
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader
                        .Builder(jwsAlgorithm)
                        .type(JOSEObjectType.JWT)
                        .keyID(privateKey.getKeyID())
                        .build(),
                payloadObject);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    private static boolean jwsSignatureVerification(JWK publicKey, String signedToken)
            throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(signedToken);
        JWSVerifier verifier = new RSASSAVerifier((RSAKey) publicKey);
        return signedJWT.verify(verifier);
    }

    private static Map<String, Object> printPayloadObject(String signedToken)
            throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(signedToken);
        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
        return jwtClaimsSet.toJSONObject();
    }

    private static boolean checkExpiration(String signedToken)
            throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(signedToken);
        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
        return new Date().before(jwtClaimsSet.getExpirationTime());
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

    // not matching public key for checking verification
    private static String loadRsaPublicKeyPem2() {
        // this is a sample key - don't worry !
        return "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+\n" +
                "0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9\n" +
                "iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT\n" +
                "/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8\n" +
                "01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB\n" +
                "ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW3g\n" +
                "QwIDAQAB\n" +
                "-----END PUBLIC KEY-----";
    }
}
