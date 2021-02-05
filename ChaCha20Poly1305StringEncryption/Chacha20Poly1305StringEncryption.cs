using System;
using System.Security.Cryptography;
using NSec.Cryptography;

public static class Program
{
	public static void Main() {
	Console.WriteLine("ChaCha20-Poly1305 String encryption with random key full");
    // you need the library https://www.nuget.org/packages/NSec.Cryptography version 20.2.0
	// documentation: https://nsec.rocks/
	// https://github.com/ektrah/nsec

    string plaintext = "The quick brown fox jumps over the lazy dog";
	Console.WriteLine("plaintext: " + plaintext);

	byte[] encryptionKey = GenerateRandomKey();
	string encryptionKeyBase64 = Base64Encoding(encryptionKey);
	Console.WriteLine("encryptionKey (Base64): " + encryptionKeyBase64);

    // encrypt

	string ciphertextBase64 = Chacha20Poly1305EncryptToBase64(encryptionKey, plaintext);
	//string ciphertextBase64 = SecretboxEncryptionToBase64(encryptionKeyBase64, plaintext);
    Console.WriteLine("\n* * * encryption * * *");
    Console.WriteLine("all data are in Base64 encoding");
    Console.WriteLine("ciphertext:  " + ciphertextBase64);
    Console.WriteLine("output is (Base64) nonce : (Base64) ciphertext");

    // decrypt
    string ciphertextReceivedBase64 = ciphertextBase64;
	string decryptionKeyBase64 = encryptionKeyBase64;
	byte[] decryptionKey = Base64Decoding(decryptionKeyBase64);
	string decryptedtext = Chacha20Poly1305DecryptFromBase64(decryptionKey, ciphertextReceivedBase64);
    Console.WriteLine("\n* * * decryption * * *");
    Console.WriteLine("ciphertext:  " + ciphertextReceivedBase64);
    Console.WriteLine("input is (Base64) nonce : (Base64) ciphertext");
    Console.WriteLine("decrypt.text:" + decryptedtext);
}

	static string Chacha20Poly1305EncryptToBase64(byte[] encryptionKey, string plaintext) {
		byte[] nonce = GenerateRandomNonce();
		//byte[] nonce = new byte[12];
		byte[] data = System.Text.Encoding.UTF8.GetBytes(plaintext);
		byte[] aad = new byte[0];
		// Create the primitive
		// select the Ed25519 signature algorithm
		var alg = AeadAlgorithm.ChaCha20Poly1305;
		using var key = Key.Import(alg, encryptionKey, KeyBlobFormat.RawSymmetricKey);
		var ciphertextWithTag = alg.Encrypt(key, new Nonce(nonce, 0), aad, data);
		// split ciphertextWitTag in ciphertext and poly1305Tag
		int ciphertextWithTagLength = ciphertextWithTag.Length;
		byte[] ciphertext = new byte[ciphertextWithTagLength - 16];
		byte[] poly1305Tag = new byte[16];
		Array.Copy(ciphertextWithTag, 0, ciphertext, 0, (ciphertextWithTagLength - 16));
		Array.Copy(ciphertextWithTag, (ciphertextWithTagLength - 16), poly1305Tag, 0, 16);
		return Base64Encoding(nonce) + ":" + Base64Encoding(ciphertext) + ":" + Base64Encoding(poly1305Tag);
	}

	static string Chacha20Poly1305DecryptFromBase64(byte[] encryptionKey, string ciphertextReceivedBase64) {
		String[] parts = ciphertextReceivedBase64.Split(':');
	  	byte[] nonce = Base64Decoding(parts[0]);
	  	byte[] ciphertext = Base64Decoding(parts[1]);
		byte[] poly1305Tag = Base64Decoding(parts[2]);
		byte[] ciphertextWithTag = new byte[(ciphertext.Length + poly1305Tag.Length)];
		Array.Copy(ciphertext, 0, ciphertextWithTag, 0, ciphertext.Length);
		Array.Copy(poly1305Tag, 0, ciphertextWithTag, ciphertext.Length, poly1305Tag.Length);
		byte[] aad = new byte[0];
		var alg = AeadAlgorithm.ChaCha20Poly1305;
		using var key = Key.Import(alg, encryptionKey, KeyBlobFormat.RawSymmetricKey);
		byte[] decrypted = new byte[ciphertext.Length];
		bool success = alg.Decrypt(key, new Nonce(nonce, 0), aad, ciphertextWithTag, decrypted);
		return System.Text.Encoding.UTF8.GetString(decrypted, 0, decrypted.Length);;
	}

	static byte[] GenerateRandomKey() {
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

	private static string Base64Encoding(byte[] input) {
    	return Convert.ToBase64String(input);
  	}

  	private static byte[] Base64Decoding(String input) {
    	return Convert.FromBase64String(input);
  	}
}
