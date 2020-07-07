package zuoyang.o2o.util;


import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

public class DESUtil {
    private static Key key;

    // set security key
    private static String KEY_STRING = "mySecurityKeySet";
    private static String CHARSET = "UTF-8";
    private static String ALGORITHM = "DES";

    static {
        try {
            // get the DES generator object
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            // set SHA1
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            // set key seed
            secureRandom.setSeed(KEY_STRING.getBytes());
            generator.init(secureRandom);
            // set key to encrypt && decrypt
            key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getEncryptString(String str) {
        Base64 base64 = new Base64();
        try {
            byte[] bytes = str.getBytes();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(bytes);
            return base64.encodeToString(doFinal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getDecryptString(String str) {
        Base64 base64 = new Base64();
        try {
            byte[] base64decodedTokenArr = Base64.decodeBase64(str.getBytes());
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(base64decodedTokenArr);
            return new String(doFinal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

    }
}
