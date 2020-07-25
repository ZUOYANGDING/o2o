package zuoyang.o2o.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
    'E', 'F'};

    public static String encryptByMD5(String source) {
        byte[] tempStr = source.getBytes();
        try {
            // apply md encrypt
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(tempStr);
            // get result of encrypted string
            byte[] mdResult = md.digest();
            char[] result = new char[mdResult.length * 2];
            int index = 0;
            for (int i=0; i<mdResult.length; i++) {
                byte b = mdResult[i];
                result[index++] = hexDigits[b >>> 4 & 0xf];
                result[index++] = hexDigits[b & 0xf];
            }
            return new String(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
//        System.out.println(encryptByMD5("123456"));
    }
}
