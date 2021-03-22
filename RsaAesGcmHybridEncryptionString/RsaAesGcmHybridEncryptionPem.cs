using System;
using System.Security.Cryptography;
using System.Text;
using System.IO;

class RsaEncryptionOaepSha1 {
	static void Main() {
    Console.WriteLine("RSA AES GCM hybrid encryption");

    Console.WriteLine("\nuses AES GCM 256 random key for the plaintext encryption");
    Console.WriteLine("and RSA OAEP SHA 1 2048 bit for the encryption of the random key\n");

    string plaintextString = "The quick brown fox jumps over the lazy dog";
    Console.WriteLine("plaintext: " + plaintextString);

    // # # # usually we would load the private and public key from a file or keystore # # #
    // # # # here we use hardcoded keys for demonstration - don't do this in real programs # # #
    string filenamePrivateKeyPem = "privatekey2048.pem";
    string filenamePublicKeyPem = "publickey2048.pem";

		try {
        // encryption
        Console.WriteLine("\n* * * Encryption * * *");
        string publicKeyLoad = loadRsaPublicKeyPem();
        // use this in production
        //string publicKeyLoad = File.ReadAllText(filenamePublicKeyXml);
        string completeCiphertextBase64 = rsaOaepSha1AesGcm256HybridStringEncryption(publicKeyLoad, plaintextString);
        Console.WriteLine("complete ciphertext: " + completeCiphertextBase64);
        Console.WriteLine("output is (Base64) encryptionKey : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag");

        // transport the encrypted data to recipient

        // receiving the encrypted data, decryption
        Console.WriteLine("\n* * * Decryption * * *");
        string completeCiphertextReceivedBase64 = completeCiphertextBase64;
        Console.WriteLine("completeCiphertextReceivedBase64: " + completeCiphertextReceivedBase64);
        string privateKeyLoad = loadRsaPrivateKeyPem();
        // use this in production
        //string privateKeyLoad = File.ReadAllText(filenamePrivateKeyXml);
        string decryptedText = rsaOaepSha1AesGcm256HybridStringDecryption(privateKeyLoad, completeCiphertextReceivedBase64);
        Console.WriteLine("plaintext: " + decryptedText);
		}
		catch(ArgumentNullException) {
			Console.WriteLine("The data was not RSA encrypted");
		}
	}

  static string rsaOaepSha1AesGcm256HybridStringEncryption (string publicKeyPem, string plaintext) {
    // generate random key
    byte[] encryptionKey = GenerateRandomAesKey();
    // encrypt plaintext
    string ciphertextBase64 = aesGcmEncryptToBase64(encryptionKey, plaintext);
    // encrypt the aes encryption key with the rsa public key
    string encryptionKeyBase64 = Base64Encoding(rsaEncryptionOaepSha1(publicKeyPem, encryptionKey));
    // complete output
    return encryptionKeyBase64 + ":" + ciphertextBase64;
  }

  static string rsaOaepSha1AesGcm256HybridStringDecryption (string privateKeyPem, string data) {
    String[] parts = data.Split(':');
    byte[] encryptedKeyReceived = Base64Decoding(parts[0]);
    byte[] nonce = Base64Decoding(parts[1]);
    byte[] encryptedData = Base64Decoding(parts[2]);
	byte[] gcmTag = Base64Decoding(parts[3]);
    // decrypt the encryption key with the rsa private key
    byte[] decryptionKey = rsaDecryptionOaepSha1(privateKeyPem, encryptedKeyReceived);
    return aesGcmDecrypt(decryptionKey, nonce, encryptedData, gcmTag);
  }

	static string aesGcmEncryptToBase64(byte[] key, string data) {
		byte[] plaintext = Encoding.UTF8.GetBytes(data);
		byte[] gcmTag = new byte[16];
		byte[] nonce = GenerateRandomNonce();
		byte[] cipherText = new byte[plaintext.Length];
		byte[] associatedData = new byte[0];
		using(var cipher = new AesGcm(key)) {
			cipher.Encrypt(nonce, plaintext, cipherText, gcmTag, associatedData);
			return Base64Encoding(nonce) + ":" + Base64Encoding(cipherText) + ":" + Base64Encoding(gcmTag);
		}
	}

	static string aesGcmDecrypt(byte[] key, byte[] nonce, byte[] ciphertext, byte[] gcmTag) {
		string decryptedtext;
		byte[] associatedData = new byte[0];
		byte[] decryptedData = new byte[ciphertext.Length];
		using(var cipher = new AesGcm(key)) {
			cipher.Decrypt(nonce, ciphertext, gcmTag, decryptedData, associatedData);
			decryptedtext = Encoding.UTF8.GetString(decryptedData, 0, decryptedData.Length);
			return decryptedtext;
		}
	}	
	
  static byte[] GenerateRandomAesKey() {
    RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    byte[] key = new byte[32];
    rngCsp.GetBytes(key);
    return key;
  }

  static byte[] GenerateRandomNonce() {
		RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
		byte[] nonce = new byte[12];
		rngCsp.GetBytes(nonce);
		return nonce;
  }		

  public static byte[] rsaEncryptionOaepSha1(string publicKeyPem, byte[] plaintext) {
	  	RSA RSAalg = RSA.Create();
		RSAalg.ImportFromPem(publicKeyPem);
    //RSACryptoServiceProvider RSAalg = new RSACryptoServiceProvider(2048);
		//RSAalg.PersistKeyInCsp = false;
		//RSAalg.FromXmlString(publicKeyXml);
		return RSAalg.Encrypt(plaintext, System.Security.Cryptography.RSAEncryptionPadding.OaepSHA1);
  }

  public static byte[] rsaDecryptionOaepSha1(string privateKeyPem, byte[] ciphertext) {
	  	RSA RSAalg = RSA.Create();
		byte[] privateKeyByte = getRsaPrivateKeyEncodedFromPem(privateKeyPem);
		int _out;
		RSAalg.ImportPkcs8PrivateKey(privateKeyByte, out _out);
    return RSAalg.Decrypt(ciphertext, System.Security.Cryptography.RSAEncryptionPadding.OaepSHA1);
  }

  private static byte[] getRsaPrivateKeyEncodedFromPem(string rsaPrivateKeyPem) {
		string rsaPrivateKeyHeaderPem = "-----BEGIN PRIVATE KEY-----\n";
		string rsaPrivateKeyFooterPem = "-----END PRIVATE KEY-----";
		string rsaPrivateKeyDataPem = rsaPrivateKeyPem.Replace(rsaPrivateKeyHeaderPem, "").Replace(rsaPrivateKeyFooterPem, "").Replace("\n", "");
		return Base64Decoding(rsaPrivateKeyDataPem);
  }	
	
  static string Base64Encoding(byte[] input) {
    return Convert.ToBase64String(input);
  }

  static byte[] Base64Decoding(String input) {
    return Convert.FromBase64String(input);
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
