using System;
using System.Security.Cryptography;
using System.Text;

public class RsaEncryptionPkcs1String {
	public static void Main() {
		Console.WriteLine("RSA 2048 encryption PKCS 1.5 string");
		string dataToEncryptString = "The quick brown fox jumps over the lazy dog";
		Console.WriteLine("plaintext: " + dataToEncryptString);

		// # # # usually we would load the private and public key from a file or keystore # # #
		// # # # here we use hardcoded keys for demonstration - don't do this in real programs # # #
		try {
			// encryption
			Console.WriteLine("\n* * * encrypt the plaintext with the RSA public key * * *");
			string publicKeyLoad = loadRsaPublicKeyPem();
			byte[] dataToEncrypt = Encoding.UTF8.GetBytes(dataToEncryptString);
			string ciphertextBase64 = Base64Encoding(rsaEncryptionPkcs1(publicKeyLoad, dataToEncrypt));
			Console.WriteLine("ciphertextBase64: " + ciphertextBase64);

			// transport the encrypted data to recipient
			// receiving the encrypted data, decryption
			Console.WriteLine("\n* * * decrypt the ciphertext with the RSA private key * * *");
			string ciphertextReceivedBase64 = ciphertextBase64;
			Console.WriteLine("ciphertextReceivedBase64: " + ciphertextReceivedBase64);
			string privateKeyLoad = loadRsaPrivateKeyPem();
			byte[] ciphertextReceived = Base64Decoding(ciphertextReceivedBase64);
			byte[] decryptedtextByte = rsaDecryptionPkcs1(privateKeyLoad, ciphertextReceived);
			Console.WriteLine("decryptedData: " + Encoding.UTF8.GetString(decryptedtextByte, 0, decryptedtextByte.Length));
		}
		catch(ArgumentNullException) {
			Console.WriteLine("The data was not RSA encrypted");
		}
	}

	public static byte[] rsaEncryptionPkcs1(string publicKeyPem, byte[] plaintext) {
		RSA rsaAlg = RSA.Create();
		rsaAlg.ImportFromPem(publicKeyPem);
		var encryptedData = rsaAlg.Encrypt(plaintext, RSAEncryptionPadding.Pkcs1);
		return encryptedData;
	}

	public static byte[] rsaDecryptionPkcs1(string privateKeyPem, byte[] ciphertext) {
		RSA rsaAlgd = RSA.Create();
		byte[] privateKeyByte = getRsaPrivateKeyEncodedFromPem(privateKeyPem);
		int _out;
		rsaAlgd.ImportPkcs8PrivateKey(privateKeyByte, out _out);
		var decryptedData = rsaAlgd.Decrypt(ciphertext, RSAEncryptionPadding.Pkcs1);
		return decryptedData;
	}

	public static byte[] rsaEncryptionOaepSha1(string publicKeyXml, byte[] plaintext) {
		RSACryptoServiceProvider RSAalg = new RSACryptoServiceProvider(2048);
		RSAalg.PersistKeyInCsp = false;
		RSAalg.FromXmlString(publicKeyXml);
		return RSAalg.Encrypt(plaintext, true);
	}

	public static byte[] rsaDecryptionOaepSha1(string privateKeyXml, byte[] ciphertext) {
		RSACryptoServiceProvider RSAalg = new RSACryptoServiceProvider(2048);
		RSAalg.PersistKeyInCsp = false;
		RSAalg.FromXmlString(privateKeyXml);
		return RSAalg.Decrypt(ciphertext, true);
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
}
