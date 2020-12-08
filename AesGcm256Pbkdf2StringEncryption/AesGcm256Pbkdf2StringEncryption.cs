using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;

public class Program {
	public static void Main() {
		Console.WriteLine("AES GCM 256 String encryption with PBKDF2 derived key");

		string plaintext = "The quick brown fox jumps over the lazy dog";
		Console.WriteLine("plaintext: " + plaintext);
		string password = "secret password";

		// encryption
		Console.WriteLine("\n* * * Encryption * * *");
		String ciphertextBase64 = aesGcmPbkdf2EncryptToBase64(password, plaintext);
		Console.WriteLine("ciphertext (Base64): " + ciphertextBase64);
		Console.WriteLine("output is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag");

		// decryption
		Console.WriteLine("\n* * * Decryption * * *");
		String ciphertextDecryptionBase64 = ciphertextBase64;
		Console.WriteLine("ciphertext (Base64): " + ciphertextDecryptionBase64);
		Console.WriteLine("input is (Base64) salt : (Base64) nonce : (Base64) ciphertext : (Base64) gcmTag");
		String decryptedtext = aesGcmPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64);
		Console.WriteLine("plaintext: " + decryptedtext);
	}

	static string aesGcmPbkdf2EncryptToBase64(string password, string data) {
		int PBKDF2_ITERATIONS = 15000;
		byte[] salt = GenerateSalt32Byte();
		byte[] key = new byte[32];
		using(var deriveBytes = new Rfc2898DeriveBytes(password, salt, PBKDF2_ITERATIONS, HashAlgorithmName.SHA256)) {
			key = deriveBytes.GetBytes(32);
		}
		byte[] plaintext = Encoding.UTF8.GetBytes(data);
		byte[] gcmTag = new byte[16];
		byte[] nonce = GenerateRandomNonce();
		byte[] cipherText = new byte[plaintext.Length];
		byte[] associatedData = new byte[0];
		using(var cipher = new AesGcm(key)) {
			cipher.Encrypt(nonce, plaintext, cipherText, gcmTag, associatedData);
			return Base64Encoding(salt) + ":" + Base64Encoding(nonce) + ":" + Base64Encoding(cipherText) + ":" + Base64Encoding(gcmTag);
		}
	}

	static string aesGcmPbkdf2DecryptFromBase64(string password, string data) {
		string decryptedtext;
		int PBKDF2_ITERATIONS = 15000;
		String[] parts = data.Split(':');
		byte[] salt = Base64Decoding(parts[0]);
		byte[] key = new byte[32];
		using(var deriveBytes = new Rfc2898DeriveBytes(password, salt, PBKDF2_ITERATIONS, HashAlgorithmName.SHA256)) {
			key = deriveBytes.GetBytes(32);
		}
		byte[] associatedData = new byte[0];
		byte[] ciphertext = Base64Decoding(parts[2]);
		byte[] decryptedData = new byte[ciphertext.Length];
		using(var cipher = new AesGcm(key)) {
			cipher.Decrypt(Base64Decoding(parts[1]), ciphertext, Base64Decoding(parts[3]), decryptedData, associatedData);
			decryptedtext = Encoding.UTF8.GetString(decryptedData, 0, decryptedData.Length);
			return decryptedtext;
		}
	}

	static byte[] GenerateSalt32Byte() {
		RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
		byte[] salt = new byte[32];
		rngCsp.GetBytes(salt);
		return salt;
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
