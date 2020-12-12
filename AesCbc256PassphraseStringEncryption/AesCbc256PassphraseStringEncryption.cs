using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using System.Collections.Generic;

public class Program {
	public static void Main() {
		Console.WriteLine("AES CBC 256 String encryption with passphrase");

		Console.WriteLine("\n# # # SECURITY WARNING: This code is provided for achieve    # # #");
		Console.WriteLine("# # # compatibility between different programming languages. # # #");
		Console.WriteLine("# # # It is not necessarily fully secure.                    # # #");
		Console.WriteLine("# # # Its security depends on the complexity and length      # # #");
		Console.WriteLine("# # # of the password, because of only one iteration and     # # #");
		Console.WriteLine("# # # the use of MD5.                                        # # #");
		Console.WriteLine("# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n");

		string plaintext = "The quick brown fox jumps over the lazy dog";
		string passphrase = "my secret passphrase";
		Console.WriteLine("passphrase: " + passphrase);

		// encryption
		Console.WriteLine("\n* * * Encryption * * *");
		String ciphertextBase64 = aesCbcPassphraseEncryptToBase64(passphrase, plaintext);
		Console.WriteLine("ciphertext (Base64): " + ciphertextBase64);
		Console.WriteLine("output is (Base64) ciphertext");

		// decryption
		Console.WriteLine("\n* * * Decryption * * *");
		String ciphertextDecryptionBase64 = ciphertextBase64;
		Console.WriteLine("ciphertext (Base64): " + ciphertextDecryptionBase64);
		Console.WriteLine("input is (Base64) ciphertext");
		String decryptedtext = aesCbcPassphraseDecryptFromBase64(passphrase, ciphertextDecryptionBase64);
		Console.WriteLine("plaintext: " + decryptedtext);

	}

	static string aesCbcPassphraseEncryptToBase64(string passphrase, string data) {
		byte[] encrypted;
		byte[] key,
		iv;
		byte[] salt = GenerateSalt8Byte();
		DeriveKeyAndIV(passphrase, salt, out key, out iv);
		using(Aes aesAlg = Aes.Create()) {
			aesAlg.Key = key;
			aesAlg.IV = iv;
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
		// add salt as first 8 bytes
		byte[] encryptedBytesWithSalt = new byte[salt.Length + encrypted.Length + 8];
		Buffer.BlockCopy(Encoding.ASCII.GetBytes("Salted__"), 0, encryptedBytesWithSalt, 0, 8);
		Buffer.BlockCopy(salt, 0, encryptedBytesWithSalt, 8, salt.Length);
		Buffer.BlockCopy(encrypted, 0, encryptedBytesWithSalt, salt.Length + 8, encrypted.Length);
		// base64 encode
		return Base64Encoding(encryptedBytesWithSalt);
	}

	static string aesCbcPassphraseDecryptFromBase64(string passphrase, string data) {
		string decryptedtext;
		// base 64 decode
		byte[] encryptedBytesWithSalt = Convert.FromBase64String(data);
		// extract salt (first 8 bytes of encrypted)
		byte[] salt = new byte[8];
		byte[] encryptedBytes = new byte[encryptedBytesWithSalt.Length - salt.Length - 8];
		Buffer.BlockCopy(encryptedBytesWithSalt, 8, salt, 0, salt.Length);
		Buffer.BlockCopy(encryptedBytesWithSalt, salt.Length + 8, encryptedBytes, 0, encryptedBytes.Length);
		// get key and iv
		byte[] key,
		iv;
		DeriveKeyAndIV(passphrase, salt, out key, out iv);
		using(Aes aesAlg = Aes.Create()) {
			aesAlg.Key = key;
			byte[] IV = iv;
			aesAlg.IV = IV;
			aesAlg.Mode = CipherMode.CBC;
			ICryptoTransform decryptor = aesAlg.CreateDecryptor(aesAlg.Key, aesAlg.IV);
			using(var msDecrypt = new MemoryStream(encryptedBytes)) {
				using(var csDecrypt = new CryptoStream(msDecrypt, decryptor, CryptoStreamMode.Read)) {
					using(var srDecrypt = new StreamReader(csDecrypt)) {
						decryptedtext = srDecrypt.ReadToEnd();
					}
				}
			}
		}
		return decryptedtext;
	}

	// source: https://asecuritysite.com/encryption/open_aes?val1=hello&val2=qwerty&val3=241fa86763b85341
	// author: https://asecuritysite.com
	static void DeriveKeyAndIV(string passphrase, byte[] salt, out byte[] key, out byte[] iv) {
		// generate key and iv
		List < byte > concatenatedHashes = new List < byte > (48);
		byte[] password = Encoding.UTF8.GetBytes(passphrase);
		byte[] currentHash = new byte[0];
		MD5 md5 = MD5.Create();
		bool enoughBytesForKey = false;
		// See http://www.openssl.org/docs/crypto/EVP_BytesToKey.html#KEY_DERIVATION_ALGORITHM
		while (!enoughBytesForKey) {
			int preHashLength = currentHash.Length + password.Length + salt.Length;
			byte[] preHash = new byte[preHashLength];
			Buffer.BlockCopy(currentHash, 0, preHash, 0, currentHash.Length);
			Buffer.BlockCopy(password, 0, preHash, currentHash.Length, password.Length);
			Buffer.BlockCopy(salt, 0, preHash, currentHash.Length + password.Length, salt.Length);
			currentHash = md5.ComputeHash(preHash);
			concatenatedHashes.AddRange(currentHash);
			if (concatenatedHashes.Count >= 48) enoughBytesForKey = true;
		}
		key = new byte[32];
		iv = new byte[16];
		concatenatedHashes.CopyTo(0, key, 0, 32);
		concatenatedHashes.CopyTo(32, iv, 0, 16);
		md5.Clear();
		md5 = null;
	}

	static byte[] GenerateSalt8Byte() {
		RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
		byte[] salt = new byte[8];
		rngCsp.GetBytes(salt);
		return salt;
	}

	static string Base64Encoding(byte[] input) {
		return Convert.ToBase64String(input);
	}

	static byte[] Base64Decoding(String input) {
		return Convert.FromBase64String(input);
	}
}
