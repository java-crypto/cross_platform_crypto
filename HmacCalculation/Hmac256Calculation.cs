using System;
using System.Security.Cryptography;
using System.Text;

public class Program {
	public static void Main() {
		Console.WriteLine("HMAC 256 calculation");
		
    string hmac256KeyString = "hmac256ForAesEncryption";
		string plaintextString = "The quick brown fox jumps over the lazy dog";

    Console.WriteLine("hmac256Key: " + hmac256KeyString);
    Console.WriteLine("plaintext:  " + plaintextString);
		
    // get binary data
    byte[] hmacKey = Encoding.UTF8.GetBytes(hmac256KeyString);
    byte[] plaintext = Encoding.UTF8.GetBytes(plaintextString);
    // calculate hmac
    byte[] hmac256 = hmac256Calculation(hmacKey, plaintext);
    Console.WriteLine("hmac32 length: " + hmac256.Length + " (Base64) data: " + Base64Encoding(hmac256));
	}

  static byte[] hmac256Calculation(byte[] keyHmac, byte[] data) {
    HMACSHA256 sha256Hmac = new HMACSHA256(keyHmac);
    return sha256Hmac.ComputeHash(data);
  }

  static string Base64Encoding(byte[] input) {
    return Convert.ToBase64String(input);
  }

}
