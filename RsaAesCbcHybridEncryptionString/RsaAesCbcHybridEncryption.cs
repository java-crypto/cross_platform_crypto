using System;
using System.Security.Cryptography;
using System.Text;
using System.IO;

class RsaEncryptionOaepSha1 {
	static void Main() {
    Console.WriteLine("RSA AES CBC hybrid encryption");

    Console.WriteLine("\nuses AES CBC 256 random key for the plaintext encryption");
    Console.WriteLine("and RSA OAEP SHA 1 2048 bit for the encryption of the random key\n");

    string plaintextString = "The quick brown fox jumps over the lazy dog";
    Console.WriteLine("plaintext: " + plaintextString);

    // # # # usually we would load the private and public key from a file or keystore # # #
    // # # # here we use hardcoded keys for demonstration - don't do this in real programs # # #
    string filenamePrivateKeyXml = "privatekey2048.xml";
    string filenamePublicKeyXml = "publickey2048.xml";

		try {
      // encryption
        Console.WriteLine("\n* * * Encryption * * *");
        //string publicKeyLoad = loadRsaPublicKeyPem();
        // use this in production
        string publicKeyLoad = File.ReadAllText(filenamePublicKeyXml);
        string completeCiphertextBase64 = rsaOaepSha1AesCbc256HybridStringEncryption(publicKeyLoad, plaintextString);
        Console.WriteLine("complete ciphertext: " + completeCiphertextBase64);
        Console.WriteLine("output is (Base64) encryptionKey : (Base64) ciphertext");

        // transport the encrypted data to recipient

        // receiving the encrypted data, decryption
        Console.WriteLine("\n* * * Decryption * * *");
        string completeCiphertextReceivedBase64 = completeCiphertextBase64;
        Console.WriteLine("completeCiphertextReceivedBase64: " + completeCiphertextReceivedBase64);
        //string privateKeyLoad = loadRsaPrivateKeyPem();
        // use this in production
        string privateKeyLoad = File.ReadAllText(filenamePrivateKeyXml);
        string decryptedText = rsaOaepSha1AesCbc256HybridStringDecryption(privateKeyLoad, completeCiphertextReceivedBase64);
        Console.WriteLine("plaintext: " + decryptedText);
		}
		catch(ArgumentNullException) {
			Console.WriteLine("The data was not RSA encrypted");
		}
	}

  static string rsaOaepSha1AesCbc256HybridStringEncryption (string publicKeyXml, string plaintext) {
    // generate random key
    byte[] encryptionKey = GenerateRandomAesKey();
    // encrypt plaintext
    string ciphertextBase64 = aesCbcEncryptToBase64(encryptionKey, plaintext);
    // encrypt the aes encryption key with the rsa public key
    string encryptionKeyBase64 = Base64Encoding(rsaEncryptionOaepSha1(publicKeyXml, encryptionKey));
    // complete output
    return encryptionKeyBase64 + ":" + ciphertextBase64;
  }

  static string rsaOaepSha1AesCbc256HybridStringDecryption (string privateKeyXml, string data) {
    String[] parts = data.Split(':');
    byte[] encryptedKeyReceived = Base64Decoding(parts[0]);
    byte[] iv = Base64Decoding(parts[1]);
    byte[] encryptedData = Base64Decoding(parts[2]);
    // decrypt the encryption key with the rsa private key
    byte[] decryptionKey = rsaDecryptionOaepSha1(privateKeyXml, encryptedKeyReceived);
    return aesCbcDecrypt(decryptionKey, iv, encryptedData);
  }

	static string aesCbcEncryptToBase64(byte[] key, string data) {
		byte[] encrypted;
		byte[] IV = GenerateRandomInitvector();
		using(Aes aesAlg = Aes.Create()) {
			aesAlg.Key = key;
      aesAlg.IV = IV;
			aesAlg.Mode = CipherMode.CBC;
			var encryptor = aesAlg.CreateEncryptor(aesAlg.Key, aesAlg.IV);
			using(var msEncrypt = new MemoryStream()) {
				using(var csEncrypt = new CryptoStream(msEncrypt, encryptor, CryptoStreamMode.Write)) {
					using(var swEncrypt = new StreamWriter(csEncrypt)) {
						swEncrypt.Write(data);
					}
					encrypted = msEncrypt.ToArray();
				}
			}
		}
    return Base64Encoding(IV) + ":" + Base64Encoding(encrypted);
	}

	static string aesCbcDecrypt(byte[] key, byte[] iv, byte[] encryptedData) {
		string decryptedtext;
		using(Aes aesAlg = Aes.Create()) {
			aesAlg.Key = key;
			//byte[] IV = iv;
			aesAlg.IV = iv;
			aesAlg.Mode = CipherMode.CBC;
			ICryptoTransform decryptor = aesAlg.CreateDecryptor(aesAlg.Key, aesAlg.IV);
			using(var msDecrypt = new MemoryStream(encryptedData)) {
				using(var csDecrypt = new CryptoStream(msDecrypt, decryptor, CryptoStreamMode.Read)) {
					using(var srDecrypt = new StreamReader(csDecrypt)) {
						decryptedtext = srDecrypt.ReadToEnd();
					}
				}
			}
		}
		return decryptedtext;
	}

  static byte[] GenerateRandomAesKey() {
    RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    byte[] key = new byte[32];
    rngCsp.GetBytes(key);
    return key;
  }

  static byte[] GenerateRandomInitvector() {
    RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    byte[] iv = new byte[16];
    rngCsp.GetBytes(iv);
    return iv;
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

  public static string loadRsaPublicKeyPem() {
    return "<RSAKeyValue><Modulus>8EmWJUZ/Osz4vXtUU2S+0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe801/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLBishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2gQw==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
  }

  public static string loadRsaPrivateKeyPem() {
    return "<RSAKeyValue><Modulus>8EmWJUZ/Osz4vXtUU2S+0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe801/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLBishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2gQw==</Modulus><Exponent>AQAB</Exponent><P>/8atV5DmNxFrxF1PODDjdJPNb9pzNrDF03TiFBZWS4Q+2JazyLGjZzhg5Vv9RJ7VcIjPAbMy2Cy5BUffEFE+8ryKVWfdpPxpPYOwHCJSw4Bqqdj0Pmp/xw928ebrnUoCzdkUqYYpRWx0T7YVRoA9RiBfQiVHhuJBSDPYJPoP34k=</P><Q>8H9wLE5L8raUn4NYYRuUVMa+1k4Q1N3XBixm5cccc/Ja4LVvrnWqmFOmfFgpVd8BcTGaPSsqfA4j/oEQp7tmjZqggVFqiM2mJ2YEv18cY/5kiDUVYR7VWSkpqVOkgiX3lK3UkIngnVMGGFnoIBlfBFF9uo02rZpC5o5zebaDIms=</Q><DP>BPXecL9Pp6u/0kwY+DcCgkVHi67J4zqka4htxgP04nwLF/o8PF0tlRfj0S7qh4UpEIimsxq9lrGvWOne6psYxG5hpGxiQQvgIqBGLxV/U2lPKEIb4oYAOmUTYnefBCrmSQW3v93pOP50dwNKAFcGWTDRiB/e9j+3EmZm/7iVzDk=</DP><DQ>rBWkAC/uLDf01Ma5AJMpahfkCZhGdupdp68x2YzFkTmDSXLJ/P15GhIQ+Lxkp2swrvwdL1OpzKaZnsxfTIXNddmEq8PEBSuRjnNzRjQaLnqjGMtTBvF3G5tWkjClb/MW2q4fgWUG8cusetQqQn2k/YQKAOh2jXXqFOstOZQc9Q0=</DQ><InverseQ>BtiIiTnpBkd6hkqJnHLh6JxBLSxUopFvbhlR37Thw1JN94i65dmtgnjwluvR/OMgzcR8e8uCH2sBn5od78vzgiDXsqITF76rJgeO639ILTA4MO3Mz+O2umrJhrkmgSk8hpRKA+5Mf9aE7dwOzHrc8hbj8J102zyYJIE6pOehrGE=</InverseQ><D>hXGYfOMFzXX/vds8HYQZpISDlSF3NmbTCdyZkIsHjndcGoSOTyeEOxV93MggxIRUSjAeKNjPVzikyr2ixdHbp4fAKnjsAjvcfnOOjBp09WW4QCi3/GCfUh0w39uhRGZKPjiqIj8NzBitN06LaoYD6MPg/CtSXiezGIlFn/Hs+MuEzNFu8PFDj9DhOFhfCgQaIgEEr+IHdnl5HuUVrwTnIBrEzZA/08Q0Gv86qQZctZWoD9hPGzeAC+RSMyGVJw6Ls8zBFf0eysB4spsu4LUom/WnZMdS1ls4eqsAX+7AdqPKBRuUVpr8FNyRM3s8pJUiGns6KFsPThtJGuH6c6KVwQ==</D></RSAKeyValue>";
  }
}
