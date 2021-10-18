using System;
using System.Security.Cryptography;
using System.Text;

public class Program {
	public static void Main() {
		Console.WriteLine("Generate a MD5 hash");

    Console.WriteLine("\n# # # SECURITY WARNING: This code is provided for achieve    # # #");
    Console.WriteLine("# # # compatibility between different programming languages. # # #");
    Console.WriteLine("# # # It is NOT SECURE - DO NOT USE THIS CODE ANY LONGER !   # # #");
    Console.WriteLine("# # # The hash algorithm MD5 is BROKEN.                      # # #");
    Console.WriteLine("# # # DO NOT USE THIS CODE IN PRODUCTION                     # # #\n");
 
    string plaintext = "The quick brown fox jumps over the lazy dog";
    Console.WriteLine("plaintext:                " + plaintext);

    byte[] md5Value = CalculateMd5(plaintext);
    Console.WriteLine("md5Value (hex) length: " + md5Value.Length + " data: " + BytesToHex(md5Value));
	}

  static byte[] CalculateMd5(string input) {
    MD5 md5 = MD5.Create();
    return md5.ComputeHash(System.Text.Encoding.UTF8.GetBytes(input));
  }

  static string BytesToHex(byte[] ba) {
    StringBuilder hex = new StringBuilder(ba.Length * 2);
    foreach (byte b in ba)
      hex.AppendFormat("{0:x2}", b);
    return hex.ToString();
  }
}
