using System;
using System.Security.Cryptography;
using System.Collections.Generic;
using Jose;
using Newtonsoft.Json.Linq;

class JwtRs256Signature {
	public static void Main() {
		Console.WriteLine("JWT JWS RS256 RSA PKCS#1.5 signature with SHA-256");
		// https://www.nuget.org/packages/jose-jwt/
		// https://github.com/dvsekhvalnov/jose-jwt

		var jwsAlgorithm = JwsAlgorithm.RS256;

		// usually we would load the private and public key from a file or keystore
		// here we use hardcoded keys for demonstration - don't do this in real programs

		Console.WriteLine("\n* * * sign the payload object with the RSA private key * * *");
		 // payload data
        string issuer = "https://java-crypto.github.io/cross_platform_crypto/";
        string subject = "JWT RS256 signature";
        int tokenExpirationInSeconds = 120;

		var jwtPayloadObject = buildPayloadObject(subject, issuer, tokenExpirationInSeconds);
        Console.WriteLine("jwtClaimsSet: ");
		jwtPayloadObject.Dump();

		// read RSA private key in a JWK object
		string jwkRsaPrivateKey = loadRsaPrivateKeyPem();
        Console.WriteLine("privateKey in PEM format:\n" + jwkRsaPrivateKey);

		// sign the payload
        string jwsSignedToken = jwsSignatureToString(jwsAlgorithm, jwkRsaPrivateKey, jwtPayloadObject);
        Console.WriteLine("signed jwsToken:\n" + jwsSignedToken);

        // send the token to the recipient
        Console.WriteLine("\n* * * verify the jwsToken with the RSA public key * * *");
        string jwsSignedTokenReceived = jwsSignedToken;

		// read the RSA public key in a JWK object
        string jwkRsaPublicKey = loadRsaPublicKeyPem();
        Console.WriteLine("publicKey in PEM format:\n" + jwkRsaPublicKey);

        // show payloadObject
        Console.WriteLine("payloadObject: " + printPayloadObject(jwsSignedTokenReceived));

		// check for correct signature
        Console.WriteLine("\ncheck signature with matching public key");
        bool tokenVerified = jwsSignatureVerification(jwkRsaPublicKey, jwsSignedTokenReceived);
        Console.WriteLine("the token's signature is verified: " + tokenVerified);
        Console.WriteLine("\ncheck signature with not matching public key");
        string jwkRsaPublicKey2 = loadRsaPublicKeyPem2();
        bool tokenNotVerified = jwsSignatureVerification(jwkRsaPublicKey2, jwsSignedTokenReceived);
        Console.WriteLine("the token's signature is verified: " + tokenNotVerified);

		// check for expiration
        Console.WriteLine("\ncheck expiration that is valid");
        bool tokenValid = checkExpiration(jwkRsaPublicKey, jwsSignedTokenReceived);
        Console.WriteLine("the token is valid and not expired: " + tokenValid);
        Console.WriteLine("\ncheck expiration that is invalid");
        string jwsSignedTokenReceivedExpired = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvamF2YS1jcnlwdG8uZ2l0aHViLmlvXC9jcm9zc19wbGF0Zm9ybV9jcnlwdG9cLyIsInN1YiI6IkpXVCBSUzI1NiBzaWduYXR1cmUiLCJleHAiOjE2MTMzODY4MTMsImlhdCI6MTYxMzM4NjY5M30.2ZrDOCWk7QUKfNd78exKDlDR5Z2_IgBxhmu8J6AjOLmYw7kzZ_kPR2FvuLI_yHnOWomw9KdD2lcH1sGTNhm4M-_D1hlKL3AyWSoujHqTV0beGRcmazuEGBSy2T4-ZLpmn6lruxrHnuhJP_IzwUQXgTMVz90eVtvEGgs9sgKm8EvmS5UTwiXwvNpzjPUgxTmHNwVafToTVS-e8hvdLVPKvWpFxG2JluIIsGpoX5Uv37eS4DCf0IQH9cx08VMifFHgnzFUTJlnSJ0IOzhG00AwqKNsfH0EhwreNPArxjMerfNRYYJsx3FQxzLrslVlhLBL4S5WkEUfCdaWm3F1ptAVcw";
        bool tokenValidExpired = checkExpiration(jwkRsaPublicKey, jwsSignedTokenReceivedExpired);
        Console.WriteLine("the token is valid and not expired: " + tokenValidExpired);

		// view header data
		Console.WriteLine("\nview header data");
		printHeaderObject(jwsSignedTokenReceived).Dump();
	}

	public static object buildPayloadObject(string subject, string issuer, int tokenExpirationInSeconds) {
		var dateNow = DateTimeOffset.Now.ToUnixTimeSeconds();
		var dateExpired = dateNow + tokenExpirationInSeconds;
		return new Dictionary<string, object>()
		{
    		{"sub", subject},
			{"iss", issuer},
			{"iat", dateNow},
    		{ "exp", dateExpired}
		};
	}

	public static string jwsSignatureToString(object jwsAlgorithm, string privateKeyPem, object payloadObject) {
		// here should be checked what algorithm is in use, at the moment it's hardcoded
		RSA rsaAlg = RSA.Create();
		byte[] privateKeyByte = getRsaPrivateKeyEncodedFromPem(privateKeyPem);
		int _out;
		rsaAlg.ImportPkcs8PrivateKey(privateKeyByte, out _out);
		return Jose.JWT.Encode(payloadObject, rsaAlg, JwsAlgorithm.RS256);
	}

	public static bool jwsSignatureVerification(string jwkRsaPublicKeyPem, string jwsSignedTokenObject){
		RSA rsaAlg = RSA.Create();
		rsaAlg.ImportFromPem(jwkRsaPublicKeyPem);
		string json = "";
		try {
			json = Jose.JWT.Decode(jwsSignedTokenObject , rsaAlg);
			return true;
		} catch(Jose.IntegrityException) {
			return false;
			// throws: Unhandled exception. Jose.IntegrityException: Invalid signature.
		}
		return false;
	}

	public static string printPayloadObject(string jwsSignedTokenObject) {
		return Jose.JWT.Payload(jwsSignedTokenObject);
	}

	public static object printHeaderObject(string jwsSignedTokenObject) {
		return Jose.JWT.Headers(jwsSignedTokenObject);
	}

	public static bool checkExpiration(string jwkRsaPublicKeyPem, string jwsSignedTokenObject) {
		RSA rsaAlg = RSA.Create();
		rsaAlg.ImportFromPem(jwkRsaPublicKeyPem);
		string json = "";
		try {
			json = Jose.JWT.Decode(jwsSignedTokenObject , rsaAlg);
			// do nothing here
		} catch(Jose.IntegrityException) {
			return false;
			// throws: Unhandled exception. Jose.IntegrityException: Invalid signature.
		}
		JObject jsonObj = JObject.Parse(json);
		var dateNow = DateTimeOffset.Now.ToUnixTimeSeconds();
		var dateExpiration = (int) jsonObj["exp"];
		if (dateExpiration > dateNow) {
			return true;
		} else {
			return false;
		}
		return false;
	}

	private static byte[] getRsaPrivateKeyEncodedFromPem(string rsaPrivateKeyPem) {
		string rsaPrivateKeyHeaderPem = "-----BEGIN PRIVATE KEY-----\n";
		string rsaPrivateKeyFooterPem = "-----END PRIVATE KEY-----";
		string rsaPrivateKeyDataPem = rsaPrivateKeyPem.Replace(rsaPrivateKeyHeaderPem, "").Replace(rsaPrivateKeyFooterPem, "").Replace("\n", "");
		return Base64Decoding(rsaPrivateKeyDataPem);
	}

	private static string loadRsaPrivateKeyPem() {
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

	private static string loadRsaPublicKeyPem() {
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
	private static string loadRsaPublicKeyPem2() {
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

	static byte[] Base64Decoding(String input) {
		return Convert.FromBase64String(input);
	}
}
