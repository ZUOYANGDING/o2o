package zuoyang.o2o.util.wechat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import zuoyang.o2o.dto.WeChatUserAccessToken;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;


@Slf4j
@Component
public class WeChatUtil {
    private static String WECHATAPPID;
    private static String WECHATAPPSECRET;

    @Value("${wechat.appID}")
    public void setWeChatAppId(String weChatAppId) {
        WECHATAPPID = weChatAppId;
    }

    @Value("${wechat.appsecret}")
    public void setWeChatAppSecret(String weChatAppSecret) {
        WECHATAPPSECRET = weChatAppSecret;
    }

    public static WeChatUserAccessToken getUserAccessToken(String code) {
        log.debug("wechat_appid: " + WECHATAPPID);
        log.debug("wechat_appsecret: " + WECHATAPPSECRET);
        // set the url for wechat pre-defined
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WECHATAPPID + "&secret=" +
                WECHATAPPSECRET + "&code=" + code + "&grant_type=authorization_code";
        // send https request with the code, appid and wechatsecret, get token string
        String tokenStr = sendHttpsRequest(url, "GET", null);

        log.debug("userAccessToken: " + tokenStr);

        // user the returned json accessToken map a WeChatUserAccessToken
        WeChatUserAccessToken weChatUserAccessToken = new WeChatUserAccessToken();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            weChatUserAccessToken = objectMapper.readValue(tokenStr, WeChatUserAccessToken.class);
        } catch (JsonParseException e) {
            log.error("Get user access token failed " + e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            log.error("Map token to object failed " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("io error throw out when get access token " + e.getMessage());
            e.printStackTrace();
        }
        if (weChatUserAccessToken == null) {
            log.error("user access token is empty ");
            return null;
        }
        return weChatUserAccessToken;
    }


    /**
     * create the https request to get the access token in json pattern from weChat
     * @param url
     * @param requestMethod
     * @param outputStr
     * @return
     */
    private static String sendHttpsRequest(String url, String requestMethod, String outputStr) {
        StringBuffer buffer = new StringBuffer();
        try {
            // create X509 certification manager
            TrustManager[] trustManagers = {(TrustManager) new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            // build the java URL connection and send it
            URL requestUrl = new URL(url);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) requestUrl.openConnection();
            httpsURLConnection.setSSLSocketFactory(ssf);
            httpsURLConnection.setRequestMethod(requestMethod);
            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpsURLConnection.connect();
            }

            // send data if needed
            if (outputStr != null) {
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // get the return token and transfer it to string
            InputStream inputStream = httpsURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String tempStr = null;
            while ((tempStr = bufferedReader.readLine())!=null) {
                buffer.append(tempStr);
            }

            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            httpsURLConnection.disconnect();
        } catch (ConnectException ce) {
            log.error("WeChat server connection timed out......." + ce.getMessage());
        } catch (Exception e) {
            log.error("https request error: {}", e.getMessage());
        }

        log.debug("https buffer: " + buffer.toString());
        String result = buffer.toString();
        buffer = null;
        return result;

    }
}
