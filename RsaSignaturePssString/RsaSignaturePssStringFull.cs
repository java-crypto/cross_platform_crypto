using System;
using System.Security.Cryptography;
using System.Text;

class RrsaSignatureString {
	static void Main() {
		Console.WriteLine("RSA signature string PSS padding");
		string dataToSignString = "The quick brown fox jumps over the lazy dog";
		byte[] dataToSign = System.Text.Encoding.UTF8.GetBytes(dataToSignString);
		Console.WriteLine("dataToSign: " + dataToSignString);
		try {
			Console.WriteLine("\n* * * sign the plaintext with the RSA private key * * *");
			string privateRsaKeyPem = loadRsaPrivateKeyPem();
			Console.WriteLine("used private key:\n" + privateRsaKeyPem);
			string signatureBase64 = rsaSignPssToBase64(privateRsaKeyPem, dataToSign);
			Console.WriteLine("\nsignature (Base64): " + signatureBase64);

			Console.WriteLine("\n* * * verify the signature against the plaintext with the RSA public key * * *");
			string publicRsaKeyPem = loadRsaPublicKeyPem();
			Console.WriteLine("used public key:\n" + publicRsaKeyPem);
			bool signatureVerified = rsaVerifySignaturePssFromBase64(publicRsaKeyPem, dataToSign, signatureBase64);
			if (signatureVerified) {
				Console.WriteLine("\nsignature (Base64) verified: true");
			} else {
				Console.WriteLine("\nsignature (Base64) verified: false");
			}
		}
		catch(ArgumentNullException) {
			Console.WriteLine("The data was not signed or verified");
		}
	}

	public static string rsaSignPssToBase64(string rsaPrivateKey, byte[] data) {
		// getting the rsa private key from xml
		RSACryptoServiceProvider RSAalg = new RSACryptoServiceProvider(2048);
		RSAalg.PersistKeyInCsp = false;
		RSAalg.FromXmlString(rsaPrivateKey);
		RSAParameters rsaParams = RSAalg.ExportParameters(true);
		RSACng RSACng = new RSACng();
		RSACng.ImportParameters(rsaParams);
		byte[] signature = RSACng.SignData(data, HashAlgorithmName.SHA256, RSASignaturePadding.Pss);
		return Base64Encoding(signature);
	}

	private static bool rsaVerifySignaturePssFromBase64(string rsaPublicKey, byte[] dataToSign, string signatureBase64) {
		try {
			var signature = Base64Decoding(signatureBase64);
			// getting the rsa public key from xml
			RSACryptoServiceProvider RSAalg = new RSACryptoServiceProvider(2048);
			RSAalg.PersistKeyInCsp = false;
			RSAalg.FromXmlString(rsaPublicKey);
			RSAParameters rsaParams = RSAalg.ExportParameters(false);
			RSACng RSACng = new RSACng();
			RSACng.ImportParameters(rsaParams);
			return RSACng.VerifyData(dataToSign, signature, HashAlgorithmName.SHA256, RSASignaturePadding.Pss);
		} catch(CryptographicException e) {
			Console.WriteLine(e.Message);
			return false;
		}
	}

	static string Base64Encoding(byte[] input) {
		return Convert.ToBase64String(input);
	}

	static byte[] Base64Decoding(String input) {
		return Convert.FromBase64String(input);
	}

	private static string loadRsaPrivateKeyPem() {
		return "<RSAKeyValue><Modulus>8EmWJUZ/Osz4vXtUU2S+0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe801/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLBishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2gQw==</Modulus><Exponent>AQAB</Exponent><P>/8atV5DmNxFrxF1PODDjdJPNb9pzNrDF03TiFBZWS4Q+2JazyLGjZzhg5Vv9RJ7VcIjPAbMy2Cy5BUffEFE+8ryKVWfdpPxpPYOwHCJSw4Bqqdj0Pmp/xw928ebrnUoCzdkUqYYpRWx0T7YVRoA9RiBfQiVHhuJBSDPYJPoP34k=</P><Q>8H9wLE5L8raUn4NYYRuUVMa+1k4Q1N3XBixm5cccc/Ja4LVvrnWqmFOmfFgpVd8BcTGaPSsqfA4j/oEQp7tmjZqggVFqiM2mJ2YEv18cY/5kiDUVYR7VWSkpqVOkgiX3lK3UkIngnVMGGFnoIBlfBFF9uo02rZpC5o5zebaDIms=</Q><DP>BPXecL9Pp6u/0kwY+DcCgkVHi67J4zqka4htxgP04nwLF/o8PF0tlRfj0S7qh4UpEIimsxq9lrGvWOne6psYxG5hpGxiQQvgIqBGLxV/U2lPKEIb4oYAOmUTYnefBCrmSQW3v93pOP50dwNKAFcGWTDRiB/e9j+3EmZm/7iVzDk=</DP><DQ>rBWkAC/uLDf01Ma5AJMpahfkCZhGdupdp68x2YzFkTmDSXLJ/P15GhIQ+Lxkp2swrvwdL1OpzKaZnsxfTIXNddmEq8PEBSuRjnNzRjQaLnqjGMtTBvF3G5tWkjClb/MW2q4fgWUG8cusetQqQn2k/YQKAOh2jXXqFOstOZQc9Q0=</DQ><InverseQ>BtiIiTnpBkd6hkqJnHLh6JxBLSxUopFvbhlR37Thw1JN94i65dmtgnjwluvR/OMgzcR8e8uCH2sBn5od78vzgiDXsqITF76rJgeO639ILTA4MO3Mz+O2umrJhrkmgSk8hpRKA+5Mf9aE7dwOzHrc8hbj8J102zyYJIE6pOehrGE=</InverseQ><D>hXGYfOMFzXX/vds8HYQZpISDlSF3NmbTCdyZkIsHjndcGoSOTyeEOxV93MggxIRUSjAeKNjPVzikyr2ixdHbp4fAKnjsAjvcfnOOjBp09WW4QCi3/GCfUh0w39uhRGZKPjiqIj8NzBitN06LaoYD6MPg/CtSXiezGIlFn/Hs+MuEzNFu8PFDj9DhOFhfCgQaIgEEr+IHdnl5HuUVrwTnIBrEzZA/08Q0Gv86qQZctZWoD9hPGzeAC+RSMyGVJw6Ls8zBFf0eysB4spsu4LUom/WnZMdS1ls4eqsAX+7AdqPKBRuUVpr8FNyRM3s8pJUiGns6KFsPThtJGuH6c6KVwQ==</D></RSAKeyValue>";
	}

	private static string loadRsaPublicKeyPem() {
		return "<RSAKeyValue><Modulus>8EmWJUZ/Osz4vXtUU2S+0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe801/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLBishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2gQw==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
	}
}
