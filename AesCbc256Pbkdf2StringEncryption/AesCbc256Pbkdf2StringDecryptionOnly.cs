using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;

public class Program {
	public static void Main() {
		Console.WriteLine("AES CBC 256 String decryption with PBKDF2 derived key only");

		string password = "secret password";

		// decryption
		Console.WriteLine("\n* * * Decryption * * *");
    String ciphertextDecryptionBase64 = "uzlk156G27dhUXLD4ImappTJUMG3jCT0GXiP45BlyCE=:2wRNNEejkXv59rB6Ol2BGQ==:LccJKke7VstzPjpnS93o7nXpvPKqIykAXxg4CfDbeFZ4B67W6AGIEZkT1v6PIjrT";
		Console.WriteLine("ciphertext (Base64): " + ciphertextDecryptionBase64);
    Console.WriteLine("input is (Base64) salt : (Base64) iv : (Base64) ciphertext");
    String decryptedtext = aesCbcPbkdf2DecryptFromBase64(password, ciphertextDecryptionBase64);
		Console.WriteLine("plaintext: " + decryptedtext);
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

  static byte[] Base64Decoding(String input) {
    return Convert.FromBase64String(input);
  }
}
