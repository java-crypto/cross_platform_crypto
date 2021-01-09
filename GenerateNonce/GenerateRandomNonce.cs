using System;
using System.Security.Cryptography;

public class GenerateNonce {
	public static void Main() {
		Console.WriteLine("Generate a 12 byte long nonce for AES GCM");
    byte[] nonce = GenerateRandomNonce();
    string nonceBase64 = Base64Encoding(nonce);
    Console.WriteLine("generated nonce length: " + nonce.Length + " data: " + nonceBase64);
	}

  static byte[] GenerateRandomNonce() {
    RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    byte[] nonce = new byte[12];
    rngCsp.GetBytes(nonce);
    return nonce;
  }

  static string Base64Encoding(byte[] input) {
    return Convert.ToBase64String(input);
  }
}

