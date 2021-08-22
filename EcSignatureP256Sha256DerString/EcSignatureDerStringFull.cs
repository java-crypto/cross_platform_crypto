using System;
using Org.BouncyCastle.Asn1.X9;
using Org.BouncyCastle.Security;

public class EcSignatureString {
	public static void Main() {
		// running with .NET 4.7.2 and BouncyCastle 1.89

		Console.WriteLine("EC signature string (ECDSA with SHA256) DER-encoding");
		var dataToSignString = "The quick brown fox jumps over the lazy dog";
		var dataToSign = System.Text.Encoding.UTF8.GetBytes(dataToSignString);
		
		Console.WriteLine("dataToSign: " + dataToSignString);
		try {
			Console.WriteLine("\n* * * sign the plaintext with the EC private key * * *");
			string ecPrivateKeyPem = loadEcPrivateKeyPem();
			string signatureBase64 = ecSignDerToBase64(ecPrivateKeyPem, dataToSign);
			Console.WriteLine("used private key:");
			Console.WriteLine(ecPrivateKeyPem);
			Console.WriteLine("\nsignature (Base64): " + signatureBase64);	
			
			// verify
			Console.WriteLine("\n* * * verify the signature against hash of plaintext with the EC public key * * *");
			string signatureReceivedBase64 = signatureBase64;
			string ecPublicKeyPem = loadEcPublicKeyPem();
			Console.WriteLine("used public key:");
			Console.WriteLine(ecPublicKeyPem);
			//bool signatureVerified = false;
			bool signatureVerified = ecVerifySignatureDerFromBase64(ecPublicKeyPem, dataToSign, signatureReceivedBase64);
			Console.WriteLine("\nsignature verified: " + signatureVerified);
		}
		catch(ArgumentNullException) {
			Console.WriteLine("The data was not signed or verified");
		}
	}
	
	private static string ecSignDerToBase64(string ecPrivateKeyPem, byte[] messageByte) {
		var privateKey = PrivateKeyFactory.CreateKey(Convert.FromBase64String(getEcPrivateKeyFromPemStripped(ecPrivateKeyPem)));
		var signer = SignerUtilities.GetSigner(X9ObjectIdentifiers.ECDsaWithSha256.Id);
		signer.Init(true, privateKey);
		signer.BlockUpdate(messageByte, 0, messageByte.Length);
		var signature = signer.GenerateSignature();
		return Base64Encoding(signature);
	}	
	
	private static bool ecVerifySignatureDerFromBase64(string ecPublicKeyPem, byte[] messageByte, String signatureBase64) {
		var publicKey = PublicKeyFactory.CreateKey(Convert.FromBase64String(getEcPublicKeyFromPemStripped(ecPublicKeyPem)));
		var verifier = SignerUtilities.GetSigner(X9ObjectIdentifiers.ECDsaWithSha256.Id);
		verifier.Init(false, publicKey);
		verifier.BlockUpdate(messageByte, 0, messageByte.Length);
		return verifier.VerifySignature(Convert.FromBase64String(signatureBase64));
	}
		
	private static string getEcPrivateKeyFromPemStripped(string ecPrivateKeyPem) {
		string ecPrivateKeyHeaderPem = "-----BEGIN PRIVATE KEY-----\n";
		string ecPrivateKeyFooterPem = "-----END PRIVATE KEY-----";
		string ecPrivateKeyDataPem = ecPrivateKeyPem.Replace(ecPrivateKeyHeaderPem, "").Replace(ecPrivateKeyFooterPem, "").Replace("\n", "");
		return ecPrivateKeyDataPem;
	}
	
	private static string getEcPublicKeyFromPemStripped(string ecPublicKeyPem) {
		string ecPublicKeyHeaderPem = "-----BEGIN PUBLIC KEY-----\n";
		string ecPublicKeyFooterPem = "-----END PUBLIC KEY-----";
		string ecPublicKeyDataPem = ecPublicKeyPem.Replace(ecPublicKeyHeaderPem, "").Replace(ecPublicKeyFooterPem, "").Replace("\n", "");
		return ecPublicKeyDataPem;
	}
	
	private static byte[] getEcPrivateKeyEncodedFromPem(string ecPrivateKeyPem) {
		string ecPrivateKeyHeaderPem = "-----BEGIN PRIVATE KEY-----\n";
		string ecPrivateKeyFooterPem = "-----END PRIVATE KEY-----";
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
		return "-----BEGIN PRIVATE KEY-----\n"
			+ "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgciPt/gulzw7/Xe12\n"  
			+ "YOu/vLUgIUZ+7gGo5VkmU0B+gUWhRANCAAQxkee3UPW110s0aUQdcS0TDkr8blAe\n"
			+ "SBouL4hXziiJX5Me/8OobFgNfYXkk6R/K/fqJhJ/mV8gLur16XhgueXA\n"
			+ "-----END PRIVATE KEY-----";
	}

	private static string loadEcPublicKeyPem() {
		// this is a sample key - don't worry !
		return "-----BEGIN PUBLIC KEY-----\n"
			+ "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEMZHnt1D1tddLNGlEHXEtEw5K/G5Q\n"
			+ "HkgaLi+IV84oiV+THv/DqGxYDX2F5JOkfyv36iYSf5lfIC7q9el4YLnlwA==\n"
			+ "-----END PUBLIC KEY-----";
	}

	static string Base64Encoding(byte[] input) {
		return Convert.ToBase64String(input);
	}

	static byte[] Base64Decoding(String input) {
		return Convert.FromBase64String(input);
	}
}
