using System;
using System.Security.Cryptography;

public class GenerateAesKey {
	public static void Main() {
		Console.WriteLine("Generate a 32 byte long AES key");
    byte[] aesKey = GenerateRandomAesKey();
    string aesKeyBase64 = Base64Encoding(aesKey);
    Console.WriteLine("generated key length: " + aesKey.Length + " data: " + aesKeyBase64);
	}

  static byte[] GenerateRandomAesKey() {
    RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    byte[] key = new byte[32];
    rngCsp.GetBytes(key);
    return key;
  }

  static string Base64Encoding(byte[] input) {
    return Convert.ToBase64String(input);
  }
}

