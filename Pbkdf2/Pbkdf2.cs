using System;
using System.Security.Cryptography;

public class Pbkdf2 {
	public static void Main() {
		Console.WriteLine("Generate a 32 byte long AES key with PBKDF2");

    // get the password as char array
    string password = "secret password";
    int PBKDF2_ITERATIONS = 15000;
    // ### security warning - never use a fixed salt in production, this is for compare reasons only
    byte[] salt = GenerateFixedSalt32Byte();
    // please use below generateSalt32Byte()
    // byte[] salt = GenerateSalt32Byte();
    byte[] aesKeySha256 = GenerateAes256KeyPbkdf2Sha256(password, PBKDF2_ITERATIONS, salt);
    Console.WriteLine("aesKeySha256 length: " + aesKeySha256.Length + " data: " + ByteArrayToHexString(aesKeySha256));
    byte[] aesKeySha1 = GenerateAes256KeyPbkdf2Sha1(password, PBKDF2_ITERATIONS, salt);
    Console.WriteLine("aesKeySha1   length: " + aesKeySha1.Length + " data: " + ByteArrayToHexString(aesKeySha1));
	}

  static byte[] GenerateAes256KeyPbkdf2Sha256(string password, int iterations, byte[] salt) {
    byte[] bytes = new byte[32];
    using (var deriveBytes = new Rfc2898DeriveBytes(password, salt, iterations, HashAlgorithmName.SHA256))
    {
      bytes = deriveBytes.GetBytes(32);
    }
    return bytes;
  }

  static byte[] GenerateAes256KeyPbkdf2Sha1(string password, int iterations, byte[] salt) {
    byte[] bytes = new byte[32];
    using (var deriveBytes = new Rfc2898DeriveBytes(password, salt, iterations, HashAlgorithmName.SHA1))
    {
      bytes = deriveBytes.GetBytes(32);
    }
    return bytes;
  }

  static byte[] GenerateSalt32Byte() {
    RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    byte[] salt = new byte[32];
    rngCsp.GetBytes(salt);
    return salt;
  }

  static byte[] GenerateFixedSalt32Byte() {
    // ### security warning - never use this in production ###
    byte[] salt = new byte[32];
    return salt;
  }

  static string ByteArrayToHexString(byte[] bytes) {
    string hex = BitConverter.ToString(bytes);
    return hex.Replace("-", "");
  }
}
