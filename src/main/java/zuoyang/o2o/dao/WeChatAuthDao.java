package zuoyang.o2o.dao;

import zuoyang.o2o.entity.WeChatAuth;

public interface WeChatAuthDao {
    /**
     * query WeChatAuth by openId
     * @param openId
     * @return
     */
    WeChatAuth queryWeChatAuthByOpenId(String openId);

    /**
     * add new WeChatAuth
     * @param weChatAuth
     * @return
     */
    int insertWeChatAuth(WeChatAuth weChatAuth);
}
