using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;

public class Program {
	public static void Main() {
		Console.WriteLine("AES CBC 256 String encryption with random key full");

		string plaintext = "The quick brown fox jumps over the lazy dog";
    Console.WriteLine("plaintext: " + plaintext);

		// generate random key
    byte[] encryptionKey = GenerateRandomAesKey();
    String encryptionKeyBase64 = Base64Encoding(encryptionKey);
		Console.WriteLine("encryptionKey (Base64): " + encryptionKeyBase64);

		// encryption
    Console.WriteLine("\n* * * Encryption * * *");
    String ciphertextBase64 = aesCbcEncryptToBase64(encryptionKey, plaintext);
    Console.WriteLine("ciphertext (Base64): " + ciphertextBase64);
    Console.WriteLine("output is (Base64) iv : (Base64) ciphertext");

		// decryption
		Console.WriteLine("\n* * * Decryption * * *");
		//String decryptionKeyBase64 = "JJu2xyi4sP7lTfxVi8iPvKIIOWiwkgr7spyUEhsnjek="; // from Java
    String decryptionKeyBase64 = encryptionKeyBase64; // full
		//String ciphertextDecryptionBase64 = "+KOM4ASOY0cMNrM9zV/qvw==:HuoyDiusSplYfd04XSNLOqtcWyOFwmeHNzXS5ywmqRkgAXi8do/6dKppo2U3ZoKl";
    String ciphertextDecryptionBase64 = ciphertextBase64;

		byte[] decryptionKey = Base64Decoding(decryptionKeyBase64);
		Console.WriteLine("ciphertext (Base64): " + ciphertextDecryptionBase64);
    Console.WriteLine("input is (Base64) iv : (Base64) ciphertext");
		String decryptedtext = aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64);
		Console.WriteLine("plaintext: " + decryptedtext);
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

	static string aesCbcDecryptFromBase64(byte[] key, string data) {
		string decryptedtext;
		String[] parts = data.Split(':');
		using(Aes aesAlg = Aes.Create()) {
			aesAlg.Key = key;
			byte[] IV = Base64Decoding(parts[0]);
			string encryptedData = parts[1];
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

  static string Base64Encoding(byte[] input) {
    return Convert.ToBase64String(input);
  }

  static byte[] Base64Decoding(String input) {
    return Convert.FromBase64String(input);
  }
}