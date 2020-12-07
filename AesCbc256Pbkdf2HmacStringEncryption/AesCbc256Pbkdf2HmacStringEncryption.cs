using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using System.Collections;

public class Program {
	public static void Main() {
		Console.WriteLine("AES CBC 256 String encryption with PBKDF2 derived key and HMAC check");
		
		string plaintext = "The quick brown fox jumps over the lazy dog";
    Console.WriteLine("plaintext: " + plaintext);
		string password = "secret password";

		// encryption
    Console.WriteLine("\n* * * Encryption * * *");
    String ciphertextBase64 = aesCbcPbkdf2HmacEncryptToBase64(password, plaintext);
    Console.WriteLine("ciphertext (Base64): " + ciphertextBase64);
    Console.WriteLine("output is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac");
		
		// decryption
		Console.WriteLine("\n* * * Decryption * * *");
    String ciphertextDecryptionBase64 = ciphertextBase64;
		Console.WriteLine("ciphertext (Base64): " + ciphertextDecryptionBase64);
    Console.WriteLine("input is (Base64) salt : (Base64) iv : (Base64) ciphertext : (Base64) hmac");
    String decryptedtext = aesCbcPbkdf2HmacDecryptFromBase64(password, ciphertextDecryptionBase64);
		Console.WriteLine("plaintext: " + decryptedtext);
	}

  static string aesCbcPbkdf2HmacEncryptToBase64(string password, string data) {
    int PBKDF2_ITERATIONS = 15000;
    byte[] salt = GenerateSalt32Byte();
    byte[] keyAesHmac = new byte[64];
    using (var deriveBytes = new Rfc2898DeriveBytes(password, salt, PBKDF2_ITERATIONS, HashAlgorithmName.SHA256))
    {
      keyAesHmac = deriveBytes.GetBytes(64);
    }
		byte[] keyAes = new byte[32];
    byte[] keyHmac = new byte[32];
    Buffer.BlockCopy(keyAesHmac, 0, keyAes, 0, 32);
    Buffer.BlockCopy(keyAesHmac, 32, keyHmac, 0, 32);
    byte[] encrypted;
		byte[] IV = GenerateRandomInitvector();
		using(Aes aesAlg = Aes.Create()) {
			aesAlg.Key = keyAes;
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
    string ciphertext = Base64Encoding(salt) + ":" + Base64Encoding(IV) + ":" + Base64Encoding(encrypted);
    HMACSHA256 sha256Hmac = new HMACSHA256(keyHmac);
    byte[] hmac = sha256Hmac.ComputeHash(Encoding.UTF8.GetBytes(ciphertext));
    return ciphertext + ":" + Base64Encoding(hmac);
	}
  
	static string aesCbcPbkdf2HmacDecryptFromBase64(string password, string data) {
    int PBKDF2_ITERATIONS = 15000;
    String[] parts = data.Split(':');
    byte[] salt = Base64Decoding(parts[0]);
    byte[] hmac = Base64Decoding(parts[3]);    
    byte[] keyAesHmac = new byte[64];
    using (var deriveBytes = new Rfc2898DeriveBytes(password, salt, PBKDF2_ITERATIONS, HashAlgorithmName.SHA256))
    {
      keyAesHmac = deriveBytes.GetBytes(64);
    }
    byte[] keyAes = new byte[32];
    byte[] keyHmac = new byte[32];
    Buffer.BlockCopy(keyAesHmac, 0, keyAes, 0, 32);
    Buffer.BlockCopy(keyAesHmac, 32, keyHmac, 0, 32);
    // before we decrypt we have to check the hmac
    String hmacToCheck = parts[0] + ':' + parts[1] + ':' + parts[2];
    HMACSHA256 sha256Hmac = new HMACSHA256(keyHmac);
    byte[] hmacCalculated = sha256Hmac.ComputeHash(Encoding.UTF8.GetBytes(hmacToCheck));
    bool equal = ByteArrayCompare(hmac, hmacCalculated);
    if (!equal) {
      Console.WriteLine("Error: HMAC-check failed, no decryption possible");
      return "";
    }
		string decryptedtext;
		using(Aes aesAlg = Aes.Create()) {
			aesAlg.Key = keyAes;
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

  static bool ByteArrayCompare(byte[] a1, byte[] a2) 
  {
    IStructuralEquatable eqa1 = a1;
    return eqa1.Equals(a2, StructuralComparisons.StructuralEqualityComparer);
  }

  static string Base64Encoding(byte[] input) {
    return Convert.ToBase64String(input);
  }

  static byte[] Base64Decoding(String input) {
    return Convert.FromBase64String(input);
  }
}
