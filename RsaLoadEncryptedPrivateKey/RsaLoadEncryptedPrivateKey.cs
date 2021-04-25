using System;
using System.Security.Cryptography;
using System.Text;

public class RsaEncryptionPkcs1String {
	public static void Main() {
	Console.WriteLine("RSA signature string");
    string dataToSignString = "The quick brown fox jumps over the lazy dog";
		byte[] dataToSign = System.Text.Encoding.UTF8.GetBytes(dataToSignString);
    Console.WriteLine("dataToSign: " + dataToSignString);

	string passphrase = "123456";

    try {
        Console.WriteLine("\n* * * sign the plaintext with the encrypted RSA private key * * *");
        string privateRsaKeyPem = loadRsaPrivateKeyEncryptedPem();
        Console.WriteLine("used private key:\n" + privateRsaKeyPem);
        string signatureBase64 = rsaSignToBase64(privateRsaKeyPem, passphrase, dataToSign);
        Console.WriteLine("\nsignature (Base64): " + signatureBase64);

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

	private static string rsaSignToBase64(string privateKeyPem, string passphrase, byte[] dataToSign) {
      try {
      	// getting the rsa private key from pem
		RSA rsaAlgd = RSA.Create();
		byte[] privateKeyByte = getRsaPrivateKeyEncryptedEncodedFromPem(privateKeyPem);
		int _out;
		rsaAlgd.ImportEncryptedPkcs8PrivateKey(passphrase, privateKeyByte, out _out);
		return Base64Encoding(rsaAlgd.SignData(dataToSign, HashAlgorithmName.SHA256, RSASignaturePadding.Pkcs1));
      	} catch(CryptographicException e) {
			Console.WriteLine(e.Message);
			return null;
		}
  }

  private static bool rsaVerifySignatureFromBase64(string publicKeyPem, byte[] dataToSign, string signatureBase64) {
      try {
        var signature = Base64Decoding(signatureBase64);
        // getting the rsa public key from pem
		RSA rsaAlg = RSA.Create();
		rsaAlg.ImportFromPem(publicKeyPem);
        return rsaAlg.VerifyData(dataToSign, signature, HashAlgorithmName.SHA256, RSASignaturePadding.Pkcs1);
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

	private static byte[] getRsaPrivateKeyEncryptedEncodedFromPem(string rsaPrivateKeyPem) {
		string rsaPrivateKeyHeaderPem = "-----BEGIN ENCRYPTED PRIVATE KEY-----\n";
		string rsaPrivateKeyFooterPem = "-----END ENCRYPTED PRIVATE KEY-----";
		string rsaPrivateKeyDataPem = rsaPrivateKeyPem.Replace(rsaPrivateKeyHeaderPem, "").Replace(rsaPrivateKeyFooterPem, "").Replace("\n", "");
		return Base64Decoding(rsaPrivateKeyDataPem);
	}

	private static string loadRsaPrivateKeyEncryptedPem() {
       // this is a sample key - don't worry !
        // passphrase for decryption: 123456
        // passphrase scheme: PBES2
        // password based key derivation: PBKDF2 with HMAC and SHA-256
        // encryption algorithm: AES256-CBC
        return "-----BEGIN ENCRYPTED PRIVATE KEY-----\n" +
                "MIIFLTBXBgkqhkiG9w0BBQ0wSjApBgkqhkiG9w0BBQwwHAQIUo/aHb7/5jUCAggA\n" +
                "MAwGCCqGSIb3DQIJBQAwHQYJYIZIAWUDBAEqBBCbpiqeplyyDN85uu5yYqpFBIIE\n" +
                "0Iqvy2v42HeASj6B25llJjNOWvT5djjDiAg7BFsuOhMq10mTC7gWduXBai5llUBH\n" +
                "qF4+GgqCeedwT24YkdR4wAqtc2shcmq+2xC70N/XrSg9geGehmNwDx35byhPjouk\n" +
                "XwA5tgVsKtxTob/xjPYhjL5J1KpWOPQhdnHPGfGlWedTyTKboRfgkZ/Yds5EW6k7\n" +
                "mO9wt/iXcym6QgaJTswa1ntGPVaKMYvXVRtNnphsI9M7njXBYO52vfujUaeKn8Xe\n" +
                "qmuEAKGFhhu+r7/WWyQl+Zq2KIoD5ImOxuXScCZVH3+76AxXSwDJzB6ZG1SowSy+\n" +
                "YHbfkmvK2H6YdFshEfI/coMmu9E3wDKuCY7rgb70lHLcpvbSMa88vCFl3d/ZfmFl\n" +
                "w4TwG3vEVR9wfbXAuAw6XTSLBODcF4ifZnyXI2Dr+fQbQd/4o8w05hDiPkXRgnYY\n" +
                "e/4nFbTGGKvatZ7LOLa62isiGtQ4nprDpTapLMeJCFSgQN1jlZMVvV6mKUfrCJ69\n" +
                "3bESlyYGAN0MAx4KeMcCRwuEXWhOkLMFW0gvRp1udiMxJ8SnWfseYTr2FPUFQlFk\n" +
                "2ZhHsSO0Pg/cszUSuCX0q2ZW0ZZOiihJsmiQNXlbsRog9m8vaxUfA00v/q6iryGE\n" +
                "ApCiQd653ikukRhk/nQEY6OV4byLyI9zESF/pIdsrFnEtGSd0tggGsREtn11D8+7\n" +
                "SW0F0Es4P4UUPjiaYzyB58gcZJWxcoq63NOTUaicvTjNbzQu1+4y+2xhFe89e09w\n" +
                "PFSGerjSV/uVtVTH2hb79mAmlyLvaikQfQq2nvpCuZy7wZjhPA3D03WFTCbrYS7P\n" +
                "PfckAICCBCIB97vPsWVAviOVOrG5NDbIag+/3YhZb2gyuR2/E0XI1NdXO63NqU9R\n" +
                "B6VUUNdSvv4djA30nVjARlh3LOZmTy50OIj/QDOEbBrtFH79eHHxHRiK0+Q0A8Ch\n" +
                "EoDDz7bMKBcpiS3vXnI2UsrZX/QIuRjypc003uVm4MvdUwv0j3UGZr9zRGctqWf0\n" +
                "YBRkoIELRA45hQn0Ln3+wjAcjLRPpeuPgVAq7tDDLDmP0i/m8UbjNorhdv4CrT/u\n" +
                "P+keTW7v9Nuov1IXG4W6wKipYRzMjiYrTL5TaXPi9z63b6RQVShAEqSTtsPg5nDy\n" +
                "CZkt5NPBltq+3InrsTlbKSQGYn6wyEQeBYezD3CLkIliDn7UM4BDn+k5YonNynU4\n" +
                "emxbU4wyBEXoMD8+aBMf+bKKQXU97Ts9p7is+Ogqyop2mbMOebIxCKayizlb7/hp\n" +
                "Qs2ThBP84OrniUEeKVAkdykZyrdCoatgXVJngDwwEkSwOrEYZ0sJ48qPQMTN3kVq\n" +
                "r5XJjX3m8mpXdz0s9VbhT/rWSdQMX82msof4c6+RghEbiHFG4B0el5vrJaAROb69\n" +
                "3OnoU8mYKBlFjbO1rJJpGS5h/v+ysqWxDxfPPLD86UsgDAjr5C7W3K8eYB1UD/wP\n" +
                "n91TuTdrLKhLE93VsCyXnsLm44Td3MBv89Fr2CMF/Jz15Nfrg3yvR7bKvkNs3uVM\n" +
                "FVO3kqtmUMC/RsUr3NIE/RZ2sS+cjLMDE2zBUgHnDpNQB6WSuwqEI9prAWxEc1jS\n" +
                "SwyNkvstftK35nfeI3FNP0R94+kSKzGmiQh5yl3ufAVi\n" +
                "-----END ENCRYPTED PRIVATE KEY-----";
	}

	private static string loadRsaPublicKeyPem() {
		// this is a sample key - don't worry !
		return "-----BEGIN PUBLIC KEY-----\n" +
			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8EmWJUZ/Osz4vXtUU2S+\n" +
			"0M4BP9+s423gjMjoX+qP1iCnlcRcFWxthQGN2CWSMZwR/vY9V0un/nsIxhZSWOH9\n" +
			"iKzqUtZD4jt35jqOTeJ3PCSr48JirVDNLet7hRT37Ovfu5iieMN7ZNpkjeIG/CfT\n" +
			"/QQl7R+kO/EnTmL3QjLKQNV/HhEbHS2/44x7PPoHqSqkOvl8GW0qtL39gTLWgAe8\n" +
			"01/w5PmcQ38CKG0oT2gdJmJqIxNmAEHkatYGHcMDtXRBpOhOSdraFj6SmPyHEmLB\n" +
			"ishaq7Jm8NPPNK9QcEQ3q+ERa5M6eM72PpF93g2p5cjKgyzzfoIV09Zb/LJ2aW2g\n" +
			"QwIDAQAB\n" +
			"-----END PUBLIC KEY-----";
	}
}
