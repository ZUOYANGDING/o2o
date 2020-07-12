package zuoyang.o2o.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeChatUserAccessToken {
    // access token from wechat include all verification info
    @JsonProperty("access_token")
    private String accessToken;
    // valid time for access token, count in seconds
    @JsonProperty("expires_in")
    private String expiresIn;
    // refresh the token, refresh by mid-ware
    @JsonProperty("refresh_token")
    private String refreshToken;
    // user identity
    @JsonProperty("openid")
    private String openId;
    // user auth scope
    @JsonProperty("scope")
    private String scope;

    @Override
    public String toString() {
        return "accessToken:" + this.getAccessToken() + ",openId:" + this.getOpenId();
    }
}
