package top.kthirty.system.auth.util;

import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import top.kthirty.core.tool.redis.RedisUtil;
import top.kthirty.core.web.utils.WebUtil;

import java.util.concurrent.TimeUnit;

public class CaptchaHelper {
    private static final String KEY = "sys:auth:captcha:";
    private static final String BASE64_PREFIX = "data:image/png;base64,";

    /**
     * 存储验证码
     * 使用Header中的ClientId作为标识
     * @author Kthirty
     * @since 2023/12/2
     * @return java.lang.String
     */
    public static String generateCode(){
        String clientId = WebUtil.getClientId();
        Assert.notBlank(clientId,"未知客户端");
        LineCaptcha lineCaptcha = new LineCaptcha(100, 40, 4, 50);
        RedisUtil.set(KEY + clientId,lineCaptcha.getCode(),2, TimeUnit.MINUTES);
        return BASE64_PREFIX + lineCaptcha.getImageBase64();
    }
    /**
     * 验证传入验证码
     * 使用Header中的ClientId作为标识
     * @author Kthirty
     * @since 2023/12/2
     * @param code 输入的验证码
     * @return boolean 是否通过
     */
    public static boolean validateCode(String code){
        String clientId = WebUtil.getClientId();
        Assert.notBlank(clientId,"未知客户端");
        String redisCode = RedisUtil.get(KEY + clientId);
        return StrUtil.equalsIgnoreCase(redisCode,code);
    }
}
