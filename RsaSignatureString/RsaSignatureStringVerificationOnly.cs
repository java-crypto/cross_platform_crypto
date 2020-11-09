using System;
using System.Security.Cryptography;
using System.Text;

class RrsaSignatureString {
	static void Main() {
    Console.WriteLine("RSA signature string verification only");
    string dataToSignString = "The quick brown fox jumps over the lazy dog";
		byte[] dataToSign = System.Text.Encoding.UTF8.GetBytes(dataToSignString);
    Console.WriteLine("dataToSign: " + dataToSignString);

    // paste signature here:
    string signatureBase64 = "vCSB4744p30IBl/sCLjvKm2nix7wfScQn99YX9tVIDNQIvU3QnSCLc2cF+J6R9WMRIXOsY94MxjKCQANW0CuaSs+w31ePHaounFVnmXyY092SicZrtpwlxw2CHqJ0NSyciDpxlRId1vjKlp9E5IJmYtVMtL2hfb711P+nb+m+1sPplNXPpJpdnWIzfLsDMVxCkplAdrcoH2HuWgOtOCHAf3vWbUC/vkvi388NT1UXJRPoERM0m1v11ogP9DiycMdoJxg3fbdH3HknbR02MLNEr7q4ZMzlrKxnYChwp2hnnBvJDcXpPDnQz7sG8zrim1nL/PS8CRG5lxhYYAZqTc+Vg==";

    try {
        Console.WriteLine("\n* * * verify the signature against the plaintext with the RSA public key * * *");
        string publicRsaKeyPem = loadRsaPublicKeyPem();
        Console.WriteLine("used public key:\n" + publicRsaKeyPem);
        bool signatureVerified = rsaVerifySignatureFromBase64(publicRsaKeyPem, dataToSign, signatureBase64);
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

  private static bool rsaVerifySignatureFromBase64(string rsaPublicKey, byte[] dataToSign, string signatureBase64) {
      try {
        var signature = Base64Decoding(signatureBase64);
        // getting the rsa public key from xml
        RSACryptoServiceProvider RSAalg = new RSACryptoServiceProvider(2048);
			  RSAalg.PersistKeyInCsp = false;
			  RSAalg.FromXmlString(rsaPublicKey);
        return RSAalg.VerifyData(dataToSign, new SHA256CryptoServiceProvider(), signature);
      } catch(CryptographicException e) {
			Console.WriteLine(e.Message);
			return false;
		}
  }

  static byte[] Base64Decoding(String input) {
    return Convert.FromBase64String(input);
  }

  private static string loadRsaPublicKeyPem() {
    return "<RSAKeyValue><Modulus>8EmWJUZ/Osz4vXtUU2S+0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe801/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLBishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2gQw==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
  }
}
