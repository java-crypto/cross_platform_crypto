using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;

public class Program {
	public static void Main() {
		Console.WriteLine("AES CBC 256 String Decryption only");

		// ### place your data here:
        String decryptionKeyBase64 = "d3+KZq3lsdy5Hgd1VRVQFUhA9jU0PaBHTG2LG2PazQM=";
        String ciphertextDecryptionBase64 = "8GhkycnbzDo5WbRol5zfCQ==:gQrTOJdobbI4fuCHCfu5eqKFYCaw/AM6xMK2o+xCXB0MeAQ7rFbXAbAE3Ex+bD8c";

		// decryption
		Console.WriteLine("\n* * * Decryption * * *");
		byte[] decryptionKey = Base64Decoding(decryptionKeyBase64);
		Console.WriteLine("ciphertext (Base64): " + ciphertextDecryptionBase64);
        Console.WriteLine("input is (Base64) iv : (Base64) ciphertext");
		String decryptedtext = aesCbcDecryptFromBase64(decryptionKey, ciphertextDecryptionBase64);
		Console.WriteLine("plaintext: " + decryptedtext);
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

  static byte[] Base64Decoding(String input) {
    return Convert.FromBase64String(input);
  }
}