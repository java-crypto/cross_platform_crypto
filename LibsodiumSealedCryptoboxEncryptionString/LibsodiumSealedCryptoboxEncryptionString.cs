using System;
using System.Security.Cryptography;
using Sodium;

using System.IO;

public static class Program {
	public static void Main() {
		Console.WriteLine("Libsodium sealed crypto box hybrid string encryption");
		// you need this library: https://www.nuget.org/packages/Sodium.Core/ version 1.2.3
		string plaintext = "The quick brown fox jumps over the lazy dog";

		// for encryption you need the public key from the one you are sending the data to (party B)
		string publicKeyBBase64 = "jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=";
		// convert the base64 encoded keys
		byte[] publicKeyB = Base64Decoding(publicKeyBBase64);
		// encrypt
		string ciphertextBase64 = SealedCryptoboxEncryptionToBase64(publicKeyB, plaintext);
		Console.WriteLine("\n* * * encryption * * *");
		Console.WriteLine("all data are in Base64 encoding");
		Console.WriteLine("publicKeyB:  " + publicKeyBBase64);
		Console.WriteLine("ciphertext:  " + ciphertextBase64);
		Console.WriteLine("output is (Base64) nonce : (Base64) ciphertext");

		// for decryption you need your private key (privateKeyB)
		// received ciphertext
		string ciphertextReceivedBase64 = ciphertextBase64;
		// own private key
		string privateKeyBBase64 = "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=";
		// convert the base64 encoded keys
		byte[] privateKeyB = Base64Decoding(privateKeyBBase64);

		// decrypt
		string decryptedtext = SealedCryptoboxDecryptionToBase64(privateKeyB, ciphertextReceivedBase64);
		Console.WriteLine("\n* * * decryption * * *");
		Console.WriteLine("ciphertext:  " + ciphertextReceivedBase64);
		Console.WriteLine("input is (Base64) nonce : (Base64) ciphertext");
		Console.WriteLine("privateKeyB: " + privateKeyBBase64);
		Console.WriteLine("decrypt.text:" + decryptedtext);
	}

	static string SealedCryptoboxEncryptionToBase64(byte[] publicKey, string plaintext) {
		var ciphertext = SealedPublicKeyBox.Create(plaintext, publicKey);
		return Base64Encoding(ciphertext);
	}

	static string SealedCryptoboxDecryptionToBase64(byte[] privateKey, string ciphertextReceivedBase64) {
		byte[] ciphertext = Base64Decoding(ciphertextReceivedBase64);
		KeyPair keypair = PublicKeyBox.GenerateKeyPair(privateKey);
		var decrypted = SealedPublicKeyBox.Open(ciphertext, keypair);
		return System.Text.Encoding.UTF8.GetString(decrypted, 0, decrypted.Length);
	}

	private static string Base64Encoding(byte[] input) {
		return Convert.ToBase64String(input);
	}

	private static byte[] Base64Decoding(String input) {
		return Convert.FromBase64String(input);
	}
}
