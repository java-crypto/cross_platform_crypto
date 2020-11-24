using System;
using System.Security.Cryptography;
using Elliptic;

class MainClass {
  public static void Main (string[] args) {

    Console.WriteLine("Generate a key pair for Curve25519");
    // you this library: https://github.com/hanswolff/curve25519

    byte[] privateKey = GenerateCurve25519PrivateKey();
    byte[] publicKey = CalculateCurve25519PublicKey(privateKey);
    Console.WriteLine("base64 encoded key data:");
    Console.WriteLine("Curve25519 PrivateKey: " + Base64Encoding(privateKey));
    Console.WriteLine("Curve25519 PublicKey:  " + Base64Encoding(publicKey));
  }

  static byte[] GenerateCurve25519PrivateKey() {
    RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    byte[] key = new byte[32];
    rngCsp.GetBytes(key);
    return Curve25519.ClampPrivateKey(key);
  }

  static byte[] CalculateCurve25519PublicKey(byte[] secret) {
    return Curve25519.GetPublicKey(secret);
  }

  static string Base64Encoding(byte[] input) {
    return Convert.ToBase64String(input);
  }
}
