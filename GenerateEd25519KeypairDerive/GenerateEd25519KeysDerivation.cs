using System;
using Sodium;

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("Generate ED25519 private and public and derive public key from private key");
        // you need the library https://www.nuget.org/packages/Sodium.Core/ version 1.2.3
        // https://github.com/tabrath/libsodium-core/
        var keyPair = generateEd25519KeyPair();
        byte[] privateKey = keyPair.PrivateKey;
        byte[] publicKey = keyPair.PublicKey;
        Console.WriteLine("privateKey (Base64): " + Base64Encoding(privateKey));
        Console.WriteLine("publicKey (Base64):  " + Base64Encoding(publicKey));

        Console.WriteLine("\nderive the publicKey from the privateKey");
        byte[] publicKeyDerived = deriveEd25519PublicKey(privateKey);
        Console.WriteLine("publicKey (Base64):  " + Base64Encoding(publicKeyDerived));
    }
    static KeyPair generateEd25519KeyPair()
    {
        return PublicKeyAuth.GenerateKeyPair();
    }
    static byte[] deriveEd25519PublicKey(byte[] privateKey)
    {
        return PublicKeyAuth.ExtractEd25519PublicKeyFromEd25519SecretKey(privateKey);
    }

    private static string Base64Encoding(byte[] input)
    {
        return Convert.ToBase64String(input);
    }
}
