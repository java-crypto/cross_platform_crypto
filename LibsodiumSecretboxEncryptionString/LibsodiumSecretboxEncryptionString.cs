using System;
using System.Security.Cryptography;
using Sodium;


public static class Program
{
	public static void Main() {
	Console.WriteLine("Libsodium secret box random key string encryption");
    // you need the library https://www.nuget.org/packages/Sodium.Core/ version 1.2.3

    string plaintext = "The quick brown fox jumps over the lazy dog";
	Console.WriteLine("plaintext: " + plaintext);

	string keyBase64 = Base64Encoding(GenerateRandomKey());
	Console.WriteLine("keyBase64): " + keyBase64);

    // encrypt
	string ciphertextBase64 = SecretboxEncryptionToBase64(keyBase64, plaintext);
    Console.WriteLine("\n* * * encryption * * *");
    Console.WriteLine("all data are in Base64 encoding");
    Console.WriteLine("ciphertext:  " + ciphertextBase64);
    Console.WriteLine("output is (Base64) nonce : (Base64) ciphertext");

    // decrypt
    string ciphertextReceivedBase64 = ciphertextBase64;
	string decryptedtext = SecretboxDecryptionFromBase64(keyBase64, ciphertextReceivedBase64);
    Console.WriteLine("\n* * * decryption * * *");
    Console.WriteLine("ciphertext:  " + ciphertextReceivedBase64);
    Console.WriteLine("input is (Base64) nonce : (Base64) ciphertext");
    Console.WriteLine("decrypt.text:" + decryptedtext);
}

	static string SecretboxEncryptionToBase64(string keyBase64, string plaintext){
	  	byte[] nonce = GenerateRandomNonce();
		byte[] data = System.Text.Encoding.UTF8.GetBytes(plaintext);
		byte[] ciphertext = SecretBox.Create(plaintext, nonce, Base64Decoding(keyBase64));
	  return Base64Encoding(nonce) + ":" + Base64Encoding(ciphertext);
  	}

  	static string SecretboxDecryptionFromBase64(string keyBase64, string ciphertextReceivedBase64){
	  String[] parts = ciphertextReceivedBase64.Split(':');
	  byte[] nonce = Base64Decoding(parts[0]);
	  byte[] ciphertext = Base64Decoding(parts[1]);
	  var decrypted = SecretBox.Open(ciphertext, nonce, Base64Decoding(keyBase64));
	  return System.Text.Encoding.UTF8.GetString(decrypted, 0, decrypted.Length);
  }

	static byte[] GenerateRandomKey() {
    	RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    	byte[] key = new byte[32];
    	rngCsp.GetBytes(key);
    	return key;
	}

    static byte[] GenerateRandomNonce() {
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