using System;
using System.Security.Cryptography;
using Sodium;

public static class Program
{
	public static void Main()
	{
		// you need this library libsodium-core/sodium.core version 1.2.3
		// get it from here: https://www.nuget.org/packages/Sodium.Core/ - https://github.com/tabrath/libsodium-core/ libsodium-core
		Console.WriteLine("Libsodium crypto box hybrid string encryption");
    	string plaintext = "The quick brown fox jumps over the lazy dog";
		Console.WriteLine("plaintext: " + plaintext);

		// encryption
		Console.WriteLine("\n* * * encryption * * *");
    	Console.WriteLine("all data are in Base64 encoding");
		// for encryption you need your private key (privateKeyA) and the public key from other party (publicKeyB)
        string privateKeyABase64 = "yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU=";
        string publicKeyBBase64 =  "jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=";
        // convert the base64 encoded keys
        byte[] privateKeyA = Base64Decoding(privateKeyABase64);
        byte[] publicKeyB = Base64Decoding(publicKeyBBase64);
        string ciphertextBase64 = SecretboxEncryptionToBase64(privateKeyA, publicKeyB, plaintext);
    	Console.WriteLine("privateKeyA: " + privateKeyABase64);
    	Console.WriteLine("publicKeyB:  " + publicKeyBBase64);
    	Console.WriteLine("ciphertext:  " + ciphertextBase64);
    	Console.WriteLine("output is (Base64) nonce : (Base64) ciphertext");

		// decryption
		Console.WriteLine("\n* * * decryption * * *");
		string ciphertextReceivedBase64 = ciphertextBase64;
		// for decryption you need your private key (privateKeyB) and the public key from other party (publicKeyA)
        string privateKeyBBase64 = "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=";
        string publicKeyABase64 =  "b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0=";
        // convert the base64 encoded keys
        byte[] privateKeyB = Base64Decoding(privateKeyBBase64);
        byte[] publicKeyA = Base64Decoding(publicKeyABase64);
		string decryptedtext = SecretboxDecryptionFromBase64(privateKeyA, publicKeyB, ciphertextReceivedBase64);
    	Console.WriteLine("ciphertext:  " + ciphertextReceivedBase64);
    	Console.WriteLine("input is (Base64) nonce : (Base64) ciphertext");
    	Console.WriteLine("privateKeyB: " + privateKeyBBase64);
    	Console.WriteLine("publicKeyA:  " + publicKeyBBase64);
    	Console.WriteLine("decrypt.text:" + decryptedtext);


	}
	private static string SecretboxEncryptionToBase64(byte[] privateKey, byte[] publicKey, string plaintext){
		byte[] nonce = GenerateRandomNonce();
		return Base64Encoding(nonce) + ":" + Base64Encoding(PublicKeyBox.Create(plaintext, nonce, privateKey, publicKey));
	}

	private static string SecretboxDecryptionFromBase64(byte[] privateKey, byte[] publicKey, string ciphertextBase64) {
		string[] parts = ciphertextBase64.Split(':');
		byte[] nonce = Base64Decoding(parts[0]);
		byte[] ciphertext = Base64Decoding(parts[1]);
		var decrypted = PublicKeyBox.Open(ciphertext, nonce, privateKey, publicKey);
		return System.Text.Encoding.UTF8.GetString(decrypted, 0, decrypted.Length);
	}

	private static byte[] GenerateRandomNonce() {
    	RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    	byte[] nonce = new byte[24];
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
