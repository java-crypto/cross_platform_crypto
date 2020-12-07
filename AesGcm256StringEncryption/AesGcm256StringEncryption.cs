using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;

public class Program {
	public static void Main() {
		Console.WriteLine("AES GCM 256 String encryption with random key full");

		string plaintext = "The quick brown fox jumps over the lazy dog";
    Console.WriteLine("plaintext: " + plaintext);

		// generate random key
    byte[] encryptionKey = GenerateRandomAesKey();
    String encryptionKeyBase64 = Base64Encoding(encryptionKey);
		Console.WriteLine("encryptionKey (Base64): " + encryptionKeyBase64);

		// encryption
    Console.WriteLine("\n* * * Encryption * * *");
    String ciphertextBase64 = aesGcmEncryptToBase64(encryptionKey, plaintext);
    Console.WriteLine("ciphertext (Base64): " + ciphertextBase64);
    Console.WriteLine("output is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag");

		// decryption
		Console.WriteLine("\n* * * Decryption * * *");
		//String decryptionKeyBase64 = "JJu2xyi4sP7lTfxVi8iPvKIIOWiwkgr7spyUEhsnjek="; // from Java
    String decryptionKeyBase64 = encryptionKeyBase64; // full
		//String ciphertextDecryptionBase64 = "+KOM4ASOY0cMNrM9zV/qvw==:HuoyDiusSplYfd04XSNLOqtcWyOFwmeHNzXS5ywmqRkgAXi8do/6dKppo2U3ZoKl";
    String ciphertextDecryptionBase64 = ciphertextBase64;

		byte[] decryptionKey = Base64Decoding(decryptionKeyBase64);
		Console.WriteLine("ciphertext (Base64): " + ciphertextDecryptionBase64);
        Console.WriteLine("input is (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag");
		String decryptedtext = aesGcmDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64);
		Console.WriteLine("plaintext: " + decryptedtext);
	}

	static string aesGcmEncryptToBase64(byte[] key, string data) {
    	byte[] plaintext = Encoding.UTF8.GetBytes(data);
    	byte[] gcmTag = new byte[16];
    	byte[] nonce = GenerateRandomNonce();
    	byte[] cipherText = new byte[plaintext.Length];
    	byte[] associatedData = new byte[0];
		using (var cipher = new AesGcm(key)) {
        	cipher.Encrypt(nonce, plaintext, cipherText, gcmTag, associatedData);
        	return Base64Encoding(nonce) + ":" + Base64Encoding(cipherText) + ":" + Base64Encoding(gcmTag);
    	}
	}

	static string aesGcmDecryptFromBase64(byte[] key, string data) {
		string decryptedtext;
		byte[] associatedData = new byte[0];
		String[] parts = data.Split(':');
		byte[] ciphertext = Base64Decoding(parts[1]);
		byte[] decryptedData = new byte[ciphertext.Length];
		using (var cipher = new AesGcm(key)) {
        	cipher.Decrypt(Base64Decoding(parts[0]), ciphertext, Base64Decoding(parts[2]), decryptedData, associatedData);
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

  static string Base64Encoding(byte[] input) {
    return Convert.ToBase64String(input);
  }

  static byte[] Base64Decoding(String input) {
    return Convert.FromBase64String(input);
  }
}
