using System;
using System.Security.Cryptography;
using System.Text;

public class Program {
	public static void Main() {
		Console.WriteLine("Generate a SHA-256 hash, Base64 en- and decoding and hex conversions");

    string plaintext = "The quick brown fox jumps over the lazy dog";
    Console.WriteLine("plaintext:                " + plaintext);

    byte[] sha256Value = CalculateSha256(plaintext);
    Console.WriteLine("sha256Value (hex) length: " + sha256Value.Length + " data: " + BytesToHex(sha256Value));

    string sha256Base64 = Base64Encoding(sha256Value);
    Console.WriteLine("sha256Value (base64):     " + sha256Base64);

    byte[] sha256ValueDecoded = Base64Decoding(sha256Base64);
    Console.WriteLine("sha256Base64 decoded to a byte array:");
    Console.WriteLine("sha256Value (hex) length: " + sha256ValueDecoded.Length + " data: " + BytesToHex(sha256ValueDecoded));

    string sha256HexString = "d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592";
    byte[] sha256Hex = HexStringToByteArray(sha256HexString);
    Console.WriteLine("sha256HexString converted to a byte array:");
    Console.WriteLine("sha256Value (hex) length: " + sha256Hex.Length + " data: " + BytesToHex(sha256Hex));
	}

  static byte[] CalculateSha256(string input) {
    SHA256 sha256 = SHA256.Create();
    return sha256.ComputeHash(System.Text.Encoding.UTF8.GetBytes(input));
  }

  static string BytesToHex(byte[] ba) {
    StringBuilder hex = new StringBuilder(ba.Length * 2);
    foreach (byte b in ba)
      hex.AppendFormat("{0:x2}", b);
    return hex.ToString();
  }

  static byte[] HexStringToByteArray(String hex) {
    int NumberChars = hex.Length;
    byte[] bytes = new byte[NumberChars / 2];
    for (int i = 0; i < NumberChars; i += 2)
    bytes[i / 2] = Convert.ToByte(hex.Substring(i, 2), 16);
    return bytes;
  }

	static string Base64Encoding(byte[] input) {
		return Convert.ToBase64String(input);
	}

	static byte[] Base64Decoding(String input) {
		return Convert.FromBase64String(input);
	}
}
