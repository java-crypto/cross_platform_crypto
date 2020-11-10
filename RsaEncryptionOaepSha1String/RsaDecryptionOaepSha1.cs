using System;
using System.Security.Cryptography;
using System.Text;
using System.IO;

class RsaEncryptionOaepSha1 {
	static void Main() {
    Console.WriteLine("RSA 2048 decryption OAEP SHA-1 string");

    // # # # usually we would load the private key from a file or keystore # # #
    // # # # here we use a hardcoded key for demonstration - don't do this in real programs # # #
    string filenamePrivateKeyXml = "privatekey2048.xml";

		try {
        // receiving the encrypted data, decryption
        Console.WriteLine("\n* * * decrypt the ciphertext with the RSA private key * * *");
        string ciphertextReceivedBase64 = "In0CE7VSJlr6PT+fl9SCejWhNZYioVn1UzYaqYUms7Y63oi4xK/2Qrgyi5a3CnvappM1MHdDaZVc+bDzl/iBiUslkHcF8lWGD4YdKuJgtfSS8WBRuIxz78EFhU3TfMU3y0v0bhUUj1/6ZdO/p9j8KcNbnpEB8A9YW+bZC1qOO8ibnTNbb3RHQekafz6r9oAYsILNga1pi9ZlyXQPYi7VpWAeZmUOq+MHEgD/Nkq/oxkyMo/yf5SVk1ig8M5Lr6arj22r2ePpQ8j3onmN7aOTCWUhZ7FdLm7IiRYebZ3MWPWozt9O6BSNjALLDXm7NfKaYcgQ+bVXrA4k1M9f4M9OEA==";
        Console.WriteLine("ciphertextReceivedBase64: " + ciphertextReceivedBase64);
        //string privateKeyLoad = loadRsaPrivateKeyPem();
        // use this in production
        string privateKeyLoad = File.ReadAllText(filenamePrivateKeyXml);
        byte[] ciphertextReceived = Base64Decoding(ciphertextReceivedBase64);
        byte[] decryptedtextByte = rsaDecryptionOaepSha1(privateKeyLoad, ciphertextReceived);
        Console.WriteLine("decryptedData: " + Encoding.UTF8.GetString(decryptedtextByte, 0, decryptedtextByte.Length));
		}
		catch(ArgumentNullException) {
			Console.WriteLine("The data was not RSA encrypted");
		}
	}

  public static byte[] rsaDecryptionOaepSha1(string privateKeyXml, byte[] ciphertext) {
    RSACryptoServiceProvider RSAalg = new RSACryptoServiceProvider(2048);
		RSAalg.PersistKeyInCsp = false;
		RSAalg.FromXmlString(privateKeyXml);
    return RSAalg.Decrypt(ciphertext, true);
  }

  static byte[] Base64Decoding(String input) {
    return Convert.FromBase64String(input);
  }

  public static string loadRsaPrivateKeyPem() {
    return "<RSAKeyValue><Modulus>8EmWJUZ/Osz4vXtUU2S+0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe801/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLBishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2gQw==</Modulus><Exponent>AQAB</Exponent><P>/8atV5DmNxFrxF1PODDjdJPNb9pzNrDF03TiFBZWS4Q+2JazyLGjZzhg5Vv9RJ7VcIjPAbMy2Cy5BUffEFE+8ryKVWfdpPxpPYOwHCJSw4Bqqdj0Pmp/xw928ebrnUoCzdkUqYYpRWx0T7YVRoA9RiBfQiVHhuJBSDPYJPoP34k=</P><Q>8H9wLE5L8raUn4NYYRuUVMa+1k4Q1N3XBixm5cccc/Ja4LVvrnWqmFOmfFgpVd8BcTGaPSsqfA4j/oEQp7tmjZqggVFqiM2mJ2YEv18cY/5kiDUVYR7VWSkpqVOkgiX3lK3UkIngnVMGGFnoIBlfBFF9uo02rZpC5o5zebaDIms=</Q><DP>BPXecL9Pp6u/0kwY+DcCgkVHi67J4zqka4htxgP04nwLF/o8PF0tlRfj0S7qh4UpEIimsxq9lrGvWOne6psYxG5hpGxiQQvgIqBGLxV/U2lPKEIb4oYAOmUTYnefBCrmSQW3v93pOP50dwNKAFcGWTDRiB/e9j+3EmZm/7iVzDk=</DP><DQ>rBWkAC/uLDf01Ma5AJMpahfkCZhGdupdp68x2YzFkTmDSXLJ/P15GhIQ+Lxkp2swrvwdL1OpzKaZnsxfTIXNddmEq8PEBSuRjnNzRjQaLnqjGMtTBvF3G5tWkjClb/MW2q4fgWUG8cusetQqQn2k/YQKAOh2jXXqFOstOZQc9Q0=</DQ><InverseQ>BtiIiTnpBkd6hkqJnHLh6JxBLSxUopFvbhlR37Thw1JN94i65dmtgnjwluvR/OMgzcR8e8uCH2sBn5od78vzgiDXsqITF76rJgeO639ILTA4MO3Mz+O2umrJhrkmgSk8hpRKA+5Mf9aE7dwOzHrc8hbj8J102zyYJIE6pOehrGE=</InverseQ><D>hXGYfOMFzXX/vds8HYQZpISDlSF3NmbTCdyZkIsHjndcGoSOTyeEOxV93MggxIRUSjAeKNjPVzikyr2ixdHbp4fAKnjsAjvcfnOOjBp09WW4QCi3/GCfUh0w39uhRGZKPjiqIj8NzBitN06LaoYD6MPg/CtSXiezGIlFn/Hs+MuEzNFu8PFDj9DhOFhfCgQaIgEEr+IHdnl5HuUVrwTnIBrEzZA/08Q0Gv86qQZctZWoD9hPGzeAC+RSMyGVJw6Ls8zBFf0eysB4spsu4LUom/WnZMdS1ls4eqsAX+7AdqPKBRuUVpr8FNyRM3s8pJUiGns6KFsPThtJGuH6c6KVwQ==</D></RSAKeyValue>";
  }
}
