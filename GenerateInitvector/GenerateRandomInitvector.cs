using System;
using System.Security.Cryptography;

public class GenerateAesKey {
	public static void Main() {
		Console.WriteLine("Generate a 16 byte long Initialization vector (IV)");
    byte[] iv = GenerateRandomInitvector();
    string ivBase64 = Base64Encoding(iv);
    Console.WriteLine("generated iv length: " + iv.Length + " data: " + ivBase64);
	}

  static byte[] GenerateRandomInitvector() {
    RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    byte[] iv = new byte[16];
    rngCsp.GetBytes(iv);
    return iv;
  }

  static string Base64Encoding(byte[] input) {
    return Convert.ToBase64String(input);
  }
}

