using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using Konscious.Security.Cryptography;
// https://github.com/kmaragon/Konscious.Security.Cryptography

public class Program {
	public static void Main() {
		Console.WriteLine("Generate a 32 byte long encryption key with Argon2id");

		var password = "secret password";
    Console.WriteLine("password: " + password);

    // ### security warning - never use a fixed salt in production, this is for compare reasons only
    byte[] salt = GenerateFixedSalt16Byte();
    // please use below generateSalt16Byte()
    // byte[] salt = GenerateSalt16Byte();
    Console.WriteLine("salt (Base64): " + Base64Encoding(salt));

    // ### the minimal parameter set is probably UNSECURE ###
    string encryptionKeyArgon2id = Base64Encoding(GenerateArgon2idMinimal(password, salt));
    Console.WriteLine("encryptionKeyArgon2id (Base64) minimal:     " + encryptionKeyArgon2id);

    encryptionKeyArgon2id = Base64Encoding(GenerateArgon2idInteractive(password, salt));
    Console.WriteLine("encryptionKeyArgon2id (Base64) interactive: " + encryptionKeyArgon2id);

    encryptionKeyArgon2id = Base64Encoding(GenerateArgon2idModerate(password, salt));
    Console.WriteLine("encryptionKeyArgon2id (Base64) moderate:    " + encryptionKeyArgon2id);

    encryptionKeyArgon2id = Base64Encoding(GenerateArgon2idSensitive(password, salt));
    Console.WriteLine("encryptionKeyArgon2id (Base64) sensitive:   " + encryptionKeyArgon2id);
  }

    // ### the minimal parameter set is probably UNSECURE ###
    static byte[] GenerateArgon2idMinimal(string password, byte[] salt) {
    var argon2id = new Argon2id(Encoding.UTF8.GetBytes(password));
	  argon2id.Salt = salt;
    argon2id.DegreeOfParallelism = 1;
    argon2id.Iterations = 2; // opsLimit
    argon2id.MemorySize = 8192; // memLimit
	  return argon2id.GetBytes(32);
  }

  static byte[] GenerateArgon2idInteractive(string password, byte[] salt) {
    var argon2id = new Argon2id(Encoding.UTF8.GetBytes(password));
	  argon2id.Salt = salt;
    argon2id.DegreeOfParallelism = 1;
    argon2id.Iterations = 2; // opsLimit
    argon2id.MemorySize = 66536; // memLimit
	  return argon2id.GetBytes(32);
  }

  static byte[] GenerateArgon2idModerate(string password, byte[] salt) {
    var argon2id = new Argon2id(Encoding.UTF8.GetBytes(password));
	  argon2id.Salt = salt;
    argon2id.DegreeOfParallelism = 1;
    argon2id.Iterations = 3; // opsLimit
    argon2id.MemorySize = 262144; // memLimit
	  return argon2id.GetBytes(32);
  }

  static byte[] GenerateArgon2idSensitive(string password, byte[] salt) {
    var argon2id = new Argon2id(Encoding.UTF8.GetBytes(password));
	  argon2id.Salt = salt;
    argon2id.DegreeOfParallelism = 1;
    argon2id.Iterations = 4; // opsLimit
    argon2id.MemorySize = 1048576; // memLimit
	  return argon2id.GetBytes(32);
  }

  static byte[] GenerateSalt16Byte() {
    RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    byte[] salt = new byte[16];
    rngCsp.GetBytes(salt);
    return salt;
  }

  static byte[] GenerateFixedSalt16Byte() {
    // ### security warning - never use this in production ###
    byte[] salt = new byte[16];
    return salt;
  }

	static string Base64Encoding(byte[] input) {
		return Convert.ToBase64String(input);
	}
}
