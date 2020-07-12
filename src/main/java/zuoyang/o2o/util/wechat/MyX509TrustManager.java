package zuoyang.o2o.util.wechat;


import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class MyX509TrustManager {
    /**
     * check certificate of client, if not trust throw exception, here use empty body to trust any certification
     * @param chain
     * @param authType
     * @throws CertificateException
     */
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

    /**
     * check certificate of server, if not trust throw exception, here use empty body to trust any certification
     * @param chain
     * @param authType
     * @throws CertificateException
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}


    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}
