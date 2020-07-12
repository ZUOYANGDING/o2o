package zuoyang.o2o.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class WeChatUser implements Serializable {
    private static final long serialVersionUID = 2587051764881917286L;
    // unique user id
    @JsonProperty("openid")
    private String openId;
    // user nickname
    @JsonProperty("nickname")
    private String nickName;
    // user sex, 1 for male, 2 for female, 0 for unknown
    @JsonProperty("sex")
    private int sex;
    // user language
    @JsonProperty("language")
    private String language;
    // user city
    @JsonProperty("city")
    private String city;
    // user province
    @JsonProperty("province")
    private String province;
    // user country
    @JsonProperty("country")
    private String country;
    // user avatar
    @JsonProperty("headimgurl")
    private String headImgUrl;

    @Override
    public String toString() {
        return "openId:" + this.getOpenId() + ",nickName:" + this.getNickName();
    }
}
