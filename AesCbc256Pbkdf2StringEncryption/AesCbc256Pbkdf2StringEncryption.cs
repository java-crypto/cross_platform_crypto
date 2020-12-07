using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;

public class Program {
	public static void Main() {
		Console.WriteLine("AES CBC 256 String encryption with PBKDF2 derived key");

		string plaintext = "The quick brown fox jumps over the lazy dog";
    Console.WriteLine("plaintext: " + plaintext);
		string password = "secret password";

		// encryption
    Console.WriteLine("\n* * * Encryption * * *");
    String ciphertextBase64 = aesCbcPbkdf2EncryptToBase64(password, plaintext);
    Console.WriteLine("ciphertext (Base64): " + ciphertextBase64);
    Console.WriteLine("output is (Base64) salt : (Base64) iv : (Base64) ciphertext");

		// decryption
		Console.WriteLine("\n* * * Decryption * * *");
    String ciphertextDecryptionBase64 = ciphertextBase64;
		Console.WriteLine("ciphertext (Base64): " + ciphertextDecryptionBase64);
    Console.WriteLine("input is (Base64) salt : (Base64) iv : (Base64) ciphertext");
    String decryptedtext = aesCbcPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64);
		Console.WriteLine("plaintext: " + decryptedtext);
	}

  static string aesCbcPbkdf2EncryptToBase64(string password, string data) {
    int PBKDF2_ITERATIONS = 15000;
    byte[] salt = GenerateSalt32Byte();
    byte[] key = new byte[32];
    using (var deriveBytes = new Rfc2898DeriveBytes(password, salt, PBKDF2_ITERATIONS, HashAlgorithmName.SHA256))
    {
      key = deriveBytes.GetBytes(32);
    }
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
    return Base64Encoding(salt) + ":" + Base64Encoding(IV) + ":" + Base64Encoding(encrypted);
	}

	static string aesCbcPbkdf2DecryptFromBase64(string password, string data) {
    int PBKDF2_ITERATIONS = 15000;
    String[] parts = data.Split(':');
    byte[] salt = Base64Decoding(parts[0]);
    byte[] key = new byte[32];
    using (var deriveBytes = new Rfc2898DeriveBytes(password, salt, PBKDF2_ITERATIONS, HashAlgorithmName.SHA256))
    {
      key = deriveBytes.GetBytes(32);
    }
		string decryptedtext;
		using(Aes aesAlg = Aes.Create()) {
			aesAlg.Key = key;
			byte[] IV = Base64Decoding(parts[1]);
			string encryptedData = parts[2];
			byte[] cipherText = Base64Decoding(encryptedData);
			aesAlg.IV = IV;
			aesAlg.Mode = CipherMode.CBC;
			ICryptoTransform decryptor = aesAlg.CreateDecryptor(aesAlg.Key, aesAlg.IV);
			using(var msDecrypt = new MemoryStream(cipherText)) {
				using(var csDecrypt = new CryptoStream(msDecrypt, decryptor, CryptoStreamMode.Read)) {
					using(var srDecrypt = new StreamReader(csDecrypt)) {
						decryptedtext = srDecrypt.ReadToEnd();
					}
				}
			}
		}
		return decryptedtext;
	}

  static byte[] GenerateSalt32Byte() {
    RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    byte[] salt = new byte[32];
    rngCsp.GetBytes(salt);
    return salt;
  }

  static byte[] GenerateRandomInitvector() {
    RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    byte[] iv = new byte[16];
    rngCsp.GetBytes(iv);
    return iv;
  }

  static string Base64Encoding(byte[] input) {
    return Convert.ToBase64String(input);
  }

  static byte[] Base64Decoding(String input) {
    return Convert.FromBase64String(input);
  }
}