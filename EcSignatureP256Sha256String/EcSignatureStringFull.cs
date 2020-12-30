using System;
using System.Security.Cryptography;

class EcSignatureString {
	static void Main() {

		Console.WriteLine("EC signature string (ECDSA with SHA256)");
		var dataToSignString = "The quick brown fox jumps over the lazy dog";
		var dataToSign = System.Text.Encoding.UTF8.GetBytes(dataToSignString);

		Console.WriteLine("dataToSign: " + dataToSignString);

		try {
			Console.WriteLine("\n* * * sign the plaintext with the EC private key * * *");
			string ecPrivateKeyPem = loadEcPrivateKeyPem();
			string signatureBase64 = ecSignP1363ToBase64(ecPrivateKeyPem, dataToSign);
			Console.WriteLine("used private key:");
			Console.WriteLine(ecPrivateKeyPem);
			Console.WriteLine("\nsignature (Base64): " + signatureBase64);

			// verify
			Console.WriteLine("\n* * *verify the signature against hash of plaintext with the EC public key * * *");
			string signatureReceivedBase64 = signatureBase64;
			string ecPublicKeyPem = loadEcPublicKeyPem();
			Console.WriteLine("used public key:");
			Console.WriteLine(ecPublicKeyPem);
			bool signatureVerified = ecVerifySignatureFromBase64(ecPublicKeyPem, dataToSign, signatureReceivedBase64);
			Console.WriteLine("\nsignature verified: " + signatureVerified);
		}
		catch(ArgumentNullException) {
			Console.WriteLine("The data was not signed or verified");
		}
	}

	private static string ecSignP1363ToBase64(string ecPrivateKeyPem, byte[] messageByte) {
		byte[] ecPrivateKeyByte = getEcPrivateKeyEncodedFromPem(ecPrivateKeyPem);
		var ecDsaKeypair = ECDsa.Create(ECCurve.NamedCurves.nistP256);
		int _out;
		ecDsaKeypair.ImportPkcs8PrivateKey(ecPrivateKeyByte, out _out);
		byte[] hashedData = null;
		byte[] signature = null;
		HashAlgorithm hashAlgo = new SHA256Managed();
		hashedData = hashAlgo.ComputeHash(messageByte);
		signature = ecDsaKeypair.SignHash(hashedData);
		return Base64Encoding(signature);
	}

	private static bool ecVerifySignatureFromBase64(string ecPublicKeyPem, byte[] messageByte, String signatureBase64) {
		byte[] hashedData = null;
		HashAlgorithm hashAlgo = new SHA256Managed();
		hashedData = hashAlgo.ComputeHash(messageByte);
		var ecDsaVerify = ECDsa.Create(ECCurve.NamedCurves.nistP256);
		byte[] ecPublicKeyByte = getEcPublicKeyFromPem(ecPublicKeyPem);
		ecDsaVerify.ImportSubjectPublicKeyInfo(ecPublicKeyByte, out _);
		return ecDsaVerify.VerifyHash(hashedData, Base64Decoding(signatureBase64));
	}

	private static byte[] getEcPrivateKeyEncodedFromPem(string ecPrivateKeyPem) {
		string ecPrivateKeyHeaderPem = "-----BEGIN EC PRIVATE KEY-----\n";
		string ecPrivateKeyFooterPem = "-----END EC PRIVATE KEY-----";
		string ecPrivateKeyDataPem = ecPrivateKeyPem.Replace(ecPrivateKeyHeaderPem, "").Replace(ecPrivateKeyFooterPem, "").Replace("\n", "");
		return Base64Decoding(ecPrivateKeyDataPem);
	}

	private static byte[] getEcPublicKeyFromPem(string ecPublicKeyPem) {
		string ecPublicKeyHeaderPem = "-----BEGIN PUBLIC KEY-----\n";
		string ecPublicKeyFooterPem = "-----END PUBLIC KEY-----";
		string ecPublicKeyDataPem = ecPublicKeyPem.Replace(ecPublicKeyHeaderPem, "").Replace(ecPublicKeyFooterPem, "").Replace("\n", "");
		return Base64Decoding(ecPublicKeyDataPem);
	}

	private static string loadEcPrivateKeyPem() {
		// this is a sample key - don't worry !
		return "-----BEGIN EC PRIVATE KEY-----\n"
			+ "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCAU2f8tzo99Z1HoxJlY\n"
			+ "96yXUhFY5vppVjw1iPKRfk1wHA==\n"
			+ "-----END EC PRIVATE KEY-----";
	}

	private static string loadEcPublicKeyPem() {
		// this is a sample key - don't worry !
		return "-----BEGIN PUBLIC KEY-----\n"
			+ "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEzb7yAFWup6iDqJiEq764rAumsV2M\n"
			+ "rspZxaP3WGpwHaC4Uff3N4UbJZF7Zac1c6W7KJl0eeCP0205Q3UEpwxndQ==\n"
			+ "-----END PUBLIC KEY-----";
	}

	static string Base64Encoding(byte[] input) {
		return Convert.ToBase64String(input);
	}

	static byte[] Base64Decoding(String input) {
		return Convert.FromBase64String(input);
	}
}