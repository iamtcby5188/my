package com.sumscope.bab.quote.externalinvoke;

import com.alibaba.fastjson.JSONObject;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.util.UUIDUtils;
import com.sumscope.bab.quote.commons.util.ValidationUtil;
import com.sumscope.bab.quote.model.dto.TokenModelDto;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import com.sumscope.optimus.commons.exceptions.GeneralValidationErrorType;
import com.sumscope.optimus.commons.exceptions.ValidationExceptionDetails;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import com.sumscope.optimus.commons.redis.RedisClient;
import com.sumscope.optimus.commons.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/6.
 * redission 操作redis,用户报价的token出来
 */
@Component
public class RedisCheckHelper {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TOKEN = "token";
    private static final String FROMPROJECT = "fromProject";
    private static final String EFFECTIVEDATE = "effectiveDate";
    private static final String EXPIREDDATE = "expiredDate";

    @Autowired
    private RedisClient redisClient;

    public void setup() {
        redisClient.open();
    }

    /**
     *web端初始化获取报价token方法
     */
    public String getToken(){
        TokenModelDto tokenModelDto = getTokenModelDto();
        setTokenJedis(tokenModelDto);
        return tokenModelDto.getToken();
    }

    /**
     * 验证redis token是否过期
     * 验证完毕立即销毁
     */
    public void checkTokenModelDto(String key) {
        if(key == null){
            //我们允许前端不传token进行报价，此时报价可能出现重复。
            //允许的原因在于我们暂时需要支持性能测试等工作。
            return;
        }
        String tokenJedis = getTokenJedis(key);
        if(tokenJedis == null){
            ValidationUtil.throwExceptionWithDetails(new ValidationExceptionDetails
                    (GeneralValidationErrorType.DATA_MISSING, "TokenModelDto", "token失效!"));
        }else{
            destroyToken(key);
        }
        if (tokenJedis != null) {
            TokenModelDto tokenModelDto = JsonUtil.readValue(tokenJedis, TokenModelDto.class);
            if (tokenModelDto != null && (tokenModelDto.getEffectiveDate().getTime() >= (new Date()).getTime() ||
                    tokenModelDto.getExpiredDate().getTime() <= (new Date()).getTime())) {
                ValidationUtil.throwExceptionWithDetails(new ValidationExceptionDetails
                        (GeneralValidationErrorType.DATA_MISSING, "TokenModelDto", "token失效!"));
            }
        }
    }

    /**
     * 设置token到redis
     */
    void setTokenJedis(TokenModelDto tokenModelDto) {
        try {
            // 为给定key设置生存时间。当key过期时，它会被自动删除
            redisClient.set(tokenModelDto.getToken(), doTokenModelDto(tokenModelDto), Constant.expiredDate);
        } catch (Exception e) {
            LogStashFormatUtil.logError(logger, "将报价token存入redis中出错！", e);
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.OTHER, "redis存入token失败!" + e);
        }
    }

    /**
     *根据key值删除redis中的token数据
     */
    void destroyToken(String token){
        try {
            redisClient.delKeys(token);//删除token
        } catch (Exception e) {
            LogStashFormatUtil.logError(logger, "删除redis中token出错！", e);
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.OTHER, "删除redis中token失败!" + e);
        }
    }

    /**
     * 存入redis中的 TokenModelDto 对象
     */
    TokenModelDto getTokenModelDto() {
        long effectiveDates = new Date().getTime() / 1000 + Constant.effectiveDate;
        long expiredDates = new Date().getTime() / 1000 + Constant.expiredDate;
        TokenModelDto dto = new TokenModelDto();
        dto.setToken(Constant.fromProject+UUIDUtils.generatePrimaryKey());
        dto.setEffectiveDate(new Date(effectiveDates * 1000));
        dto.setExpiredDate(new Date(expiredDates * 1000));
        dto.setFromProject(Constant.fromProject);
        return dto;
    }

    /**
     *存入redis中对应的 key - value 中的value值
     */
    String doTokenModelDto(TokenModelDto tokenModelDto) {
        JSONObject json = new JSONObject();
        json.put(TOKEN, tokenModelDto.getToken());
        json.put(FROMPROJECT, Constant.fromProject);
        json.put(EFFECTIVEDATE, tokenModelDto.getEffectiveDate());
        json.put(EXPIREDDATE, tokenModelDto.getExpiredDate());
        return json.toJSONString();
    }

    /**
     *根据key获取redis 中的value
     */
    String getTokenJedis(String key){
        return redisClient.get(key);
    }

}
