package zuoyang.o2o.service;

import zuoyang.o2o.dto.WeChatAuthExecution;
import zuoyang.o2o.entity.WeChatAuth;
import zuoyang.o2o.exception.WeChatAuthOperationException;

public interface WeChatAuthService {
    WeChatAuth getWeChatAuthByOpenId(String openId) throws WeChatAuthOperationException;

    WeChatAuthExecution addWeChatAuth(WeChatAuth weChatAuth) throws WeChatAuthOperationException;
}
