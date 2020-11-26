using System;
using System.Security.Cryptography;
using System.IO;
using System.Text;
using Elliptic;

class MainClass {
  public static void Main (string[] args) {

    Console.WriteLine("Curve25519 key exchange and AES CBC 256 string encryption");
    // // you need the library https://github.com/hanswolff/curve25519

    string plaintext = "The quick brown fox jumps over the lazy dog";

    // for encryption you need your private key (aPrivateKey) and the public key from other party (bPublicKey)
    string aPrivateKeyBase64 = "yJIzp7IueQOu8l202fwI21/aNXUxXBcg3jJoLFJATlU=";
    string bPublicKeyBase64 =  "jVSuHjVH47PMbMaAxL5ziBS9/Z0HQK6TJLw9X3Jw6yg=";

    // convert the base64 encoded keys
    byte[] aPrivateKey = Base64Decoding(aPrivateKeyBase64);
    byte[] bPublicKey = Base64Decoding(bPublicKeyBase64);

    // generate shared key
    byte[] aSharedKey = curve25519GenerateKeyAgreement(aPrivateKey, bPublicKey);
    // encrypt
    string ciphertextBase64 = aesCbcEncryptToBase64(aSharedKey, plaintext);

    Console.WriteLine("\n* * * encryption * * *");
    Console.WriteLine("all data are in Base64 encoding");
    Console.WriteLine("aPrivateKey: " + aPrivateKeyBase64);
    Console.WriteLine("bPublicKey:  " + bPublicKeyBase64);
    Console.WriteLine("aSharedKey:  " + Base64Encoding(aSharedKey));
    Console.WriteLine("ciphertext:  " + ciphertextBase64);
    Console.WriteLine("output is    (Base64) iv : (Base64) ciphertext");

    // for decryption you need your private key (bPrivateKey) and the public key from other party (aPublicKey)
    // received ciphertext
    string ciphertextReceivedBase64 = ciphertextBase64;
    // received public key
    string aPublicKeyBase64 = "b+Z6ajj7wI6pKAK5N28Hzp0Lyhv2PvHofwGY3WSm7W0=";
    // own private key
    string bPrivateKeyBase64 = "yNmXR5tfBXA/uZjanND+IYgGXlrFnrdUiUXesI4fOlM=";
    // convert the base64 encoded keys
    byte[] aPublicKey = Base64Decoding(aPublicKeyBase64);
    byte[] bPrivateKey = Base64Decoding(bPrivateKeyBase64);
    // generate shared key
    byte[] bSharedKey = curve25519GenerateKeyAgreement(bPrivateKey, aPublicKey);
    // decrypt
    string decryptedtext = aesCbcDecryptFromBase64(bSharedKey, ciphertextReceivedBase64);
    Console.WriteLine("\n* * * decryption * * *");
    Console.WriteLine("ciphertext:  " + ciphertextReceivedBase64);
    Console.WriteLine("input is     (Base64) iv : (Base64) ciphertext");
    Console.WriteLine("bPrivateKey: " + bPrivateKeyBase64);
    Console.WriteLine("aPublicKey:  " + aPublicKeyBase64);
    Console.WriteLine("bSharedKey:  " + Base64Encoding(bSharedKey));
    Console.WriteLine("decrypt.text:" + decryptedtext);
  }

  static byte[] curve25519GenerateKeyAgreement(byte[] privateKey, byte[] publicKey) {
    return Curve25519.GetSharedSecret(privateKey, publicKey);
  }

  static string aesCbcEncryptToBase64(byte[] key, string data) {
		byte[] encrypted;
		byte[] IV = GenerateRandomInitvector();
		using(Aes aesAlg = Aes.Create()) {
			aesAlg.Key = key;
      aesAlg.IV = IV;
			aesAlg.Mode = CipherMode.CBC;
			var encryptor = aesAlg.CreateEncryptor(aesAlg.Key, aesAlg.IV);
			using(var msEncrypt = new MemoryStream()) {
				using(var csEncrypt = new CryptoStream(msEncrypt, encryptor, CryptoStreamMode.Write)) {
					using(var swEncrypt = new StreamWriter(csEncrypt)) {
						swEncrypt.Write(data);
					}
					encrypted = msEncrypt.ToArray();
				}
			}
		}
    return Base64Encoding(IV) + ":" + Base64Encoding(encrypted);
	}

	static string aesCbcDecryptFromBase64(byte[] key, string data) {
		string decryptedtext;
		String[] parts = data.Split(':');
		using(Aes aesAlg = Aes.Create()) {
			aesAlg.Key = key;
			byte[] IV = Base64Decoding(parts[0]);
			string encryptedData = parts[1];
			byte[] cipherText = Base64Decoding(encryptedData);
			aesAlg.IV = IV;
			aesAlg.Mode = CipherMode.CBC;
			ICryptoTransform decryptor = aesAlg.CreateDecryptor(aesAlg.Key, aesAlg.IV);
			using(var msDecrypt = new MemoryStream(cipherText)) {
				using(var csDecrypt = new CryptoStream(msDecrypt, decryptor, CryptoStreamMode.Read)) {
					using(var srDecrypt = new StreamReader(csDecrypt)) {
						decryptedtext = srDecrypt.ReadToEnd();
					}
				}
			}
		}
		return decryptedtext;
	}

  static string Base64Encoding(byte[] input) {
    return Convert.ToBase64String(input);
  }

  static byte[] Base64Decoding(String input) {
    return Convert.FromBase64String(input);
  }

  static byte[] GenerateRandomInitvector() {
    RNGCryptoServiceProvider rngCsp = new RNGCryptoServiceProvider();
    byte[] iv = new byte[16];
    rngCsp.GetBytes(iv);
    return iv;
  }
}
