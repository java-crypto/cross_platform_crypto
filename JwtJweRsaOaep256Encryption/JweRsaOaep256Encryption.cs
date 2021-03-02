using System;
using System.Security.Cryptography;
using System.Collections.Generic;
using Jose;
using System.Text.Json;

class RrsaSignatureString {
	public static void Main() {
		Console.WriteLine("JWT JWE RSA-OAEP-256 AES GCM 256 encryption");
		// https://www.nuget.org/packages/jose-jwt/
		// https://github.com/dvsekhvalnov/jose-jwt

		string jweKeyAlgorithm = "RSA-OAEP-256"; // supported key algorithms: RSA-OAEP, RSA-OAEP-256, RSA1_5
        //string jweKeyAlgorithm = "RSA-OAEP";
		//string jweKeyAlgorithm = "RSA1_5";

		string jweEncryptionAlgorithm = "A256GCM"; // supported encryption algorithms: A128GCM, A192GCM, A256GCM
        //string jweEncryptionAlgorithm = "A192GCM";
		//string jweEncryptionAlgorithm = "A128GCM";

		Console.WriteLine("jwe key algorithm:        " + jweKeyAlgorithm);
        Console.WriteLine("jwe encryption algorithm: " + jweEncryptionAlgorithm);

		// build the payload to encrypt
        string dataToEncrypt = "The quick brown fox jumps over the lazy dog";
        string issuer = "https://java-crypto.github.io/cross_platform_crypto/";
        string subject = "JWE RSA-OAEP & AES GCM encryption";
        int tokenExpirationInSeconds = 120;

        // build a JWT payload object
        var jwtPayloadObject = buildPayloadObject(dataToEncrypt, subject, issuer, tokenExpirationInSeconds);
        Console.WriteLine("jwtPayloadObject generated");

		Console.WriteLine("\n* * * encrypt the payload with recipient\'s public key * * *");
		string rsaPublicKeyFromRecipient = loadRsaPublicKeyPem();
		Console.WriteLine("publicKey loaded");
		// encrypt the data and build the encrypted jwe token
        string jweTokenBase64Url = jweRsaEncryptToBase64UrlToken(rsaPublicKeyFromRecipient, jweKeyAlgorithm, jweEncryptionAlgorithm, jwtPayloadObject);
        Console.WriteLine("jweToken (Base64Url encoded):\n" + jweTokenBase64Url);

		Console.WriteLine("\n* * * decrypt the payload with recipient\'s private key * * *");
		string rsaPrivateKey = loadRsaPrivateKeyPem();
		Console.WriteLine("privateKey loaded");
		string jweDecryptedPayload = jweRsaDecryptFromBase64UrlToken(rsaPrivateKey, jweTokenBase64Url);
        Console.WriteLine("jweDecryptedPayload: " + jweDecryptedPayload);

		// check for expired
		Console.WriteLine("\ncheck for expired token");
		Console.WriteLine("actual time:     " + DateTime.Now);
		var payloadData = JsonSerializer.Deserialize<PayloadObject>(jweDecryptedPayload);
		long payloadExp = payloadData.exp;
		DateTime dateExp = UnixTimestampToDateTime(payloadData.exp);
		Console.WriteLine("expiration time: " + dateExp);
		long dateTimeUnixNow = DateTimeOffset.Now.ToUnixTimeSeconds();
		if (payloadExp < dateTimeUnixNow) {
			Console.WriteLine("payload is expired");
		} else {
			Console.WriteLine("payload is NOT expired");
		}
	}

	public static string jweRsaEncryptToBase64UrlToken(string rsaPublicKeyFromRecipient, string jweKeyAlgorithm, string jweEncryptionAlgorithm, object jwtPayloadObject){
		RSA rsaAlg = RSA.Create();
		rsaAlg.ImportFromPem(rsaPublicKeyFromRecipient);
		JweAlgorithm jweAlg;
		switch (jweKeyAlgorithm)
      	{
        	case "RSA-OAEP-256":
				jweAlg = JweAlgorithm.RSA_OAEP_256;
              	break;
          	case "RSA-OAEP":
              	jweAlg = JweAlgorithm.RSA_OAEP;
              	break;
			case "RSA1_5":
				jweAlg = JweAlgorithm.RSA1_5;
				break;
          	default:
              	return "*** not supported algorithm";
              	break;
      	}
		JweEncryption jweEnc;
		switch (jweEncryptionAlgorithm)
      	{
        	case "A128GCM":
				jweEnc = JweEncryption.A128GCM;
              	break;
			case "A192GCM":
				jweEnc = JweEncryption.A192GCM;
              	break;
			case "A256GCM":
				jweEnc = JweEncryption.A256GCM;
              	break;
          	default:
              	return "*** not supported algorithm";
              	break;
      	}
		return Jose.JWT.Encode(jwtPayloadObject, rsaAlg, jweAlg, jweEnc);
	}

	public static string jweRsaDecryptFromBase64UrlToken(string rsaPrivateKey, string jweTokenBase64Url){
		RSA rsaAlg = RSA.Create();
		byte[] privateKeyByte = getRsaPrivateKeyEncodedFromPem(rsaPrivateKey);
		int _out;
		rsaAlg.ImportPkcs8PrivateKey(privateKeyByte, out _out);
		string json = "";
		try {
			json = Jose.JWT.Decode(jweTokenBase64Url, rsaAlg);
		} catch(Jose.EncryptionException) {
			Console.WriteLine("*** Error: payload corrupted or wrong private key ***");
			// throws: Jose.EncryptionException: Unable to decrypt content or authentication tag do not match.
		}
		return json;
	}

	private static Dictionary<string, object> buildPayloadObject(string dataToEncrypt, string subject, string issuer, int tokenExpirationInSeconds){
		long dateNow = DateTimeOffset.Now.ToUnixTimeSeconds();
		long dateExpiration = dateNow + tokenExpirationInSeconds;
		return new Dictionary<string, object>()
		{
			{"iss", issuer},
    		{"sub", subject},
			{"iat", dateNow},
    		{ "exp", dateExpiration}
		};
	}

	static string Base64Encoding(byte[] input) {
		return Convert.ToBase64String(input);
	}

	static byte[] Base64Decoding(String input) {
		return Convert.FromBase64String(input);
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

	private static string loadRsaPublicKeyPem2() { // corrupted
		// this is a sample key - don't worry !
		return "-----BEGIN PUBLIC KEY-----\n" +
			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+\n" +
			"0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9\n" +
			"iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT\n" +
			"/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8\n" +
			"01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB\n" +
			"sshaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g\n" +
			"QwIDAQAB\n" +
			"-----END PUBLIC KEY-----";
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

	public static DateTime UnixTimestampToDateTime(double unixTime)
	{
    	DateTime unixStart = new DateTime(1970, 1, 1, 0, 0, 0, 0, System.DateTimeKind.Utc);
    	long unixTimeStampInTicks = (long) (unixTime * TimeSpan.TicksPerSecond);
    	return new DateTime(unixStart.Ticks + unixTimeStampInTicks, System.DateTimeKind.Utc);
	}
}

public class PayloadObject
{
	public string sub {get; set;}
	public long exp {get; set;}
}
