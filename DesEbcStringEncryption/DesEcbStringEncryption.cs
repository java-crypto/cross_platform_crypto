using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;

public class Program {
	public static void Main() {

    Console.WriteLine("\n# # # SECURITY WARNING: This code is provided for achieve    # # #");
		Console.WriteLine("# # # compatibility between different programming languages. # # #");
		Console.WriteLine("# # # It is not necessarily fully secure.                    # # #");
		Console.WriteLine("# # # It uses the OUTDATED and UNSECURE algorithm DES.       # # #");
		Console.WriteLine("# # # It uses FIXED key that should NEVER used,              # # #");
		Console.WriteLine("# # # instead use random generated keys.                     # # #");
		Console.WriteLine("# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n");

		string plaintext = "The quick brown fox jumps over the lazy dog";
    Console.WriteLine("plaintext: " + plaintext);

    Console.WriteLine("DES EBC String encryption with fixed key");
    // fixed encryption key 8 bytes long
    string desKey = "12345678";
		UTF8Encoding utf8 = new UTF8Encoding();
    byte[] desEncryptionKey = utf8.GetBytes(desKey);
    Console.WriteLine("desEncryptionKey (Base64): " + Base64Encoding(desEncryptionKey));

    Console.WriteLine("\n* * * Encryption * * *");
    String desCiphertextBase64 = desEcbEncryptToBase64(desEncryptionKey, plaintext);
    Console.WriteLine("ciphertext (Base64): " + desCiphertextBase64);
    Console.WriteLine("output is (Base64) ciphertext");
    // decryption
		Console.WriteLine("\n* * * Decryption * * *");
		Console.WriteLine("ciphertext (Base64): " + desCiphertextBase64);
    Console.WriteLine("input is (Base64) ciphertext");
    String desDecryptedtext = desEcbDecryptFromBase64(desEncryptionKey, desCiphertextBase64);
    Console.WriteLine("plaintext: " + desDecryptedtext);

    Console.WriteLine("\nTriple DES ECB String encryption with fixed key (16 bytes)");
    // fixed encryption key 16 bytes long
    string des2Key = "1234567890123456";
    byte[] des2EncryptionKeyShort = utf8.GetBytes(des2Key);
    byte[] des2EncryptionKey = tdes2ToDes3Key(des2EncryptionKeyShort);
    Console.WriteLine("des2EncryptionKey (Base64): " + Base64Encoding(des2EncryptionKey));

    Console.WriteLine("\n* * * Encryption * * *");
    String des2CiphertextBase64 = des3EcbEncryptToBase64(des2EncryptionKey, plaintext);
    Console.WriteLine("ciphertext (Base64): " + des2CiphertextBase64);
    Console.WriteLine("output is (Base64) ciphertext");
    // decryption
		Console.WriteLine("\n* * * Decryption * * *");
		Console.WriteLine("ciphertext (Base64): " + des2CiphertextBase64);
    Console.WriteLine("input is (Base64) ciphertext");
    String des2Decryptedtext = des3EcbDecryptFromBase64(des2EncryptionKey, des2CiphertextBase64);
    Console.WriteLine("plaintext: " + des2Decryptedtext);

    // TripleDES
    Console.WriteLine("\nTriple DES ECB String encryption with fixed key (24 bytes)");
    // fixed encryption key 24 bytes long
    byte[] des3EncryptionKey = Encoding.ASCII.GetBytes("123456789012345678901234");
        
    Console.WriteLine("\n* * * Encryption * * *");
    String des3CiphertextBase64 = des3EcbEncryptToBase64(des3EncryptionKey, plaintext);
    Console.WriteLine("ciphertext (Base64): " + des3CiphertextBase64);
    Console.WriteLine("output is (Base64) ciphertext");
    Console.WriteLine("\n* * * Decryption * * *");
		Console.WriteLine("ciphertext (Base64): " + des3CiphertextBase64);
    Console.WriteLine("input is (Base64) ciphertext");
    String des3Decryptedtext = des3EcbDecryptFromBase64(des3EncryptionKey, des3CiphertextBase64);
		Console.WriteLine("plaintext: " + des3Decryptedtext);
	}

  static string desEcbEncryptToBase64(byte[] key, string data) {
		byte[] encrypted;
    // this is just a dummy value, the ECB mode does not require an IV
    byte[] IV = GenerateFixedDesInitvector();
    using(DES desAlg = DES.Create()) {
      desAlg.Key = key;
      desAlg.IV = IV;
			desAlg.Mode = CipherMode.ECB;
			var encryptor = desAlg.CreateEncryptor(desAlg.Key, desAlg.IV);
			using(var msEncrypt = new MemoryStream()) {
				using(var csEncrypt = new CryptoStream(msEncrypt, encryptor, CryptoStreamMode.Write)) {
					using(var swEncrypt = new StreamWriter(csEncrypt)) {
						swEncrypt.Write(data);
					}
					encrypted = msEncrypt.ToArray();
				}
			}
    }
    return Base64Encoding(encrypted);
	}

  static string desEcbDecryptFromBase64(byte[] key, string data) {
		string decryptedtext;
		using(DES desAlg = DES.Create()) {
			desAlg.Key = key;
			byte[] IV = GenerateFixedDesInitvector();
			byte[] cipherText = Base64Decoding(data);
			desAlg.IV = IV;
			desAlg.Mode = CipherMode.ECB;
			ICryptoTransform decryptor = desAlg.CreateDecryptor(desAlg.Key, desAlg.IV);
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

  static string des3EcbEncryptToBase64(byte[] key, string data) {
		byte[] encrypted;
    // this is just a dummy value, the ECB mode does not require an IV
    byte[] IV = GenerateFixedDesInitvector();
    using(TripleDES desAlg = TripleDES.Create()) {
      desAlg.Key = key;
      desAlg.IV = IV;
			desAlg.Mode = CipherMode.ECB;
			var encryptor = desAlg.CreateEncryptor(desAlg.Key, desAlg.IV);
			using(var msEncrypt = new MemoryStream()) {
				using(var csEncrypt = new CryptoStream(msEncrypt, encryptor, CryptoStreamMode.Write)) {
					using(var swEncrypt = new StreamWriter(csEncrypt)) {
						swEncrypt.Write(data);
					}
					encrypted = msEncrypt.ToArray();
				}
			}
    }
    return Base64Encoding(encrypted);
	}

  static string des3EcbDecryptFromBase64(byte[] key, string data) {
		string decryptedtext;
		using(TripleDES desAlg = TripleDES.Create()) {
			desAlg.Key = key;
			byte[] IV = GenerateFixedDesInitvector();
			byte[] cipherText = Base64Decoding(data);
			desAlg.IV = IV;
			desAlg.Mode = CipherMode.ECB;
			ICryptoTransform decryptor = desAlg.CreateDecryptor(desAlg.Key, desAlg.IV);
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

  static byte[] tdes2ToDes3Key(byte[] tdes2Key) {
    byte[] tdes3Key = new byte[24];
    Array.Copy(tdes2Key, 0, tdes3Key, 0, 16);
    Array.Copy(tdes2Key, 0, tdes3Key, 16, 8);
    return tdes3Key;
  }

  static byte[] GenerateRandomDesKey() {
    RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    byte[] key = new byte[8]; // 8 bytes for des, 16 bytes for TDES 2 and 24 bytes for TDES 3
    rngCsp.GetBytes(key);
    return key;
  }

  // this is just a dummy value
  static byte[] GenerateFixedDesInitvector() {
    return Encoding.ASCII.GetBytes("00000000");
  }

  static string Base64Encoding(byte[] input) {
    return Convert.ToBase64String(input);
  }

  static byte[] Base64Decoding(String input) {
    return Convert.FromBase64String(input);
  }
}
