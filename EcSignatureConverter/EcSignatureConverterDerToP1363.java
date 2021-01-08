package EC_Signature_secp256r1_conversion;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Base64;

public class EcSignatureConverterDerToP1363 {
    public static void main(String[] args) throws Exception {
        System.out.println("EC signature converter DER to P1363 encoding");
        System.out.println("Please note that all values are in Base64 encoded form");

        // insert the ecdsa signature in DER encoding
        String signatureDerBase64 = "MEUCIQCW98vDD+c/Wx3WX3T+Ph9ZDf1s5nuDThz2xrhSOjwGFAIgNpUg5SygNK7W+zzw4eNZkivizpU/UUXMMR2zPw2D7J4=";

        String signatureP1363Base64 = convertSignatureDerToP1363Base64(signatureDerBase64);
        System.out.println("signature in DER encoding:   " + signatureDerBase64);
        System.out.println("signature in P1363 encoding: " + signatureP1363Base64);
    }

    // convertPlainToDer and convertDerToPlain
    // source https://stackoverflow.com/a/61873962/8166854  answered May 18 at 16:07 dave_thompson_085
    // secp384r1 (aka P-384) has 384-bit order so use 384/8 which is 48 for n
    private static String convertSignatureP1363ToDerBase64 (String plainBase64) {
        byte[] plain = base64Decoding(plainBase64);
        int n = 32; // for example assume 256-bit-order curve like P-256
        BigInteger r = new BigInteger (+1, Arrays.copyOfRange(plain,0,n));
        BigInteger s = new BigInteger (+1, Arrays.copyOfRange(plain,n,n*2));
        byte[] x1 = r.toByteArray(), x2 = s.toByteArray();
        // already trimmed two's complement, as DER wants
        int len = x1.length + x2.length + (2+2), idx = len>=128? 3: 2;
        // the len>=128 case can only occur for curves of 488 bits or more,
        // and can be removed if you will definitely not use such curve(s)
        byte[] out = new byte[idx+len];
        out[0] = 0x30;
        if( idx==3 ){
            out[1] = (byte)0x81;
            out[2] = (byte)len; }
        else {
            out[1] = (byte)len; }
        out[idx] = 2;
        out[idx+1] = (byte)x1.length;
        System.arraycopy(x1, 0, out, idx+2, x1.length);
        idx += x1.length + 2;
        out[idx] = 2;
        out[idx+1] = (byte)x2.length;
        System.arraycopy(x2, 0, out, idx+2, x2.length);
        return base64Encoding(out);
    }

    private static String convertSignatureDerToP1363Base64 (String derBase64) throws Exception {
        byte[] der = base64Decoding(derBase64);
        int n = 32; // for example assume 256-bit-order curve like P-256
        BigInteger r, s;
        byte[] out;
        if( der[0] != 0x30 ) throw new Exception();
        int idx = der[1]==0x81? 3: 2; // the 0x81 case only occurs for curve over 488 bits
        if( der[idx] != 2 ) throw new Exception();
        r = new BigInteger (1, Arrays.copyOfRange(der,  idx+2, idx+2+der[idx+1]));
        idx += der[idx+1] + 2;
        if( der[idx] != 2 ) throw new Exception();
        s = new BigInteger (1, Arrays.copyOfRange(der,  idx+2, idx+2+der[idx+1]));
        if( idx + der[idx+1] + 2 != der.length ) throw new Exception();
        // common output
        out = new byte[2*n];
        toFixed(r, out, 0, n);
        toFixed(s, out, n, n);
        return base64Encoding(out);
    }

    static void toFixed (BigInteger x, byte[] a, int off, int len) throws Exception {
        byte[] t = x.toByteArray();
        if( t.length == len+1 && t[0] == 0 ) System.arraycopy (t,1, a,off, len);
        else if( t.length <= len ) System.arraycopy (t,0, a,off+len-t.length, t.length);
        else throw new Exception();
    }

    private static String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
    private static byte[] base64Decoding(String input) {
        return Base64.getDecoder().decode(input);
    }
}
