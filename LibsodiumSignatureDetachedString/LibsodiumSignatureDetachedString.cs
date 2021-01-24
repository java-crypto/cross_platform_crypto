using System;
//using System.Security.Cryptography;
using Sodium;


public static class Program
{
	public static void Main() {
	Console.WriteLine("Libsodium detached signature string");
    // you need the library https://www.nuget.org/packages/Sodium.Core/ version 1.2.3

    string dataToSign = "The quick brown fox jumps over the lazy dog";
	Console.WriteLine("plaintext: " + dataToSign);

	 // usually we would load the private and public key from a file or keystore
     // here we use hardcoded keys for demonstration - don't do this in real programs

    Console.WriteLine("\n* * * sign the plaintext with the ED25519 private key * * ");
	byte[] privateKey = LoadEd25519PrivateKey();
	string signatureBase64 = LibsodiumSignDetachedToBase64(privateKey, dataToSign);
	Console.WriteLine("signature (Base64): " + signatureBase64);

	Console.WriteLine("* * * verify the signature against the plaintext with the ED25519 public key * * *");
	byte[] publicKey = LoadEd25519PublicKey();
	bool signatureVerified = LibsodiumVerifyDetachedFromBase64(publicKey, dataToSign, signatureBase64);
	Console.WriteLine("signature (Base64): " + signatureBase64);
    Console.WriteLine("signature (Base64) verified: " + signatureVerified);
}

	static string LibsodiumSignDetachedToBase64(byte[] privateKey, string dataToSign){
		byte[] data = System.Text.Encoding.UTF8.GetBytes(dataToSign);
		byte[] signature = PublicKeyAuth.SignDetached(data, privateKey);
		return Base64Encoding(signature);
  	}

	static bool LibsodiumVerifyDetachedFromBase64(byte[] publicKey, string dataToSign, string signatureBase64){
		byte[] data = System.Text.Encoding.UTF8.GetBytes(dataToSign);
		byte[] signature = Base64Decoding(signatureBase64);
		return PublicKeyAuth.VerifyDetached(signature, data, publicKey);
	}

	private static byte[] LoadEd25519PrivateKey() {
		// this is a sample key - don't worry !
		return Base64Decoding("Gjho7dILimbXJY8RvmQZEBczDzCYbZonGt/e5QQ3Vro2sjTp95EMxOrgEaWiprPduR/KJFBRA+RlDDpkvmhVfw==");
	}

	private static byte[] LoadEd25519PublicKey() {
		// this is a sample key - don't worry !
		return Base64Decoding("NrI06feRDMTq4BGloqaz3bkfyiRQUQPkZQw6ZL5oVX8=");
	}

	private static string Base64Encoding(byte[] input) {
    	return Convert.ToBase64String(input);
  	}

  	private static byte[] Base64Decoding(String input) {
    	return Convert.FromBase64String(input);
  	}
}
