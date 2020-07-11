package zuoyang.o2o.util.wechat;

import java.security.MessageDigest;
import java.util.Arrays;

public class SignUtil {
    private static String token = "o2o";

    public static boolean checkUserSignature(String signature, String timeStamp, String nonce) {
        String[] signatureArray = new String[] {token, timeStamp, nonce};

        Arrays.sort(signatureArray);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i=0; i<signatureArray.length; i++) {
            stringBuffer.append(signatureArray[i]);
        }

        MessageDigest md = null;
        String result = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(stringBuffer.toString().getBytes());
            result = byteToString(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stringBuffer = null;
        return (result != null) && result.equals(signature.toUpperCase());
    }

    private static String byteToString(byte[] digest) {
        String tempStr = "";
        for (int i=0; i<digest.length; i++) {
            tempStr += byteToHexStr(digest[i]);
        }
        return tempStr;
    }

    private static String byteToHexStr(byte b) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempChar = new char[2];
        tempChar[0] = Digit[(b>>>4) & 0X0F];
        tempChar[1] = Digit[b & 0X0F];
        String tempStr = new String(tempChar);
        return tempStr;
    }
}
