package com.workspace.ssoserve.service;

import com.google.gson.Gson;
import com.workspace.ssoserve.utils.VerifyCodeImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginService {

    private static final long VERIFY_CODE_TIMEOUT_MINUTE = 5;

    private static final int randomStrNum = 4; //验证码字符个数

    private static final String randomString = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWSYZ";

    private static final String[] phoneString = {"android", "iphone", "ipad", "ipod", "ios"};

    private static Random random = new Random();

    private static Gson gson = new Gson();

    private final RedisTemplate<String, String> verifyRedisTemplate;

    private final StringRedisTemplate accessTokenStringRT;

    @Autowired
    public LoginService(@Qualifier("verify-pool") RedisTemplate<String, String> verifyRedisTemplate,
                        @Qualifier("access-token") StringRedisTemplate accessTokenStringRT) {
        super();
        this.verifyRedisTemplate = verifyRedisTemplate;
        this.accessTokenStringRT = accessTokenStringRT;
    }

    public void getVerifyCodeImage(String loginStamp, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("image/png");
        response.setHeader("Expire", "0");
        response.setHeader("Pragma", "no-cache");
        try {
            String verifyCode = generateVerifyCode(loginStamp);
            log.info("[AuthController][getVerifyCodeImage]stamp:{}, verify code:{}", loginStamp, verifyCode);
            BufferedImage verifyImage = VerifyCodeImageUtil.generateImage(verifyCode);
            ImageIO.write(verifyImage, "PNG", response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateVerifyCode(String loginStamp) {
        StringBuilder sb = new StringBuilder(randomStrNum);
        for (int i = 0; i < randomStrNum; i++) {
            sb.append(randomString.charAt(random.nextInt(randomString.length())));
        }
        String verifyCode = sb.toString();
        verifyRedisTemplate.opsForValue().set(redisVerifyCodeKey(loginStamp), verifyCode,
                VERIFY_CODE_TIMEOUT_MINUTE, TimeUnit.MINUTES);
        return verifyCode;
    }

    private String redisVerifyCodeKey(String key) {
        return "VerifyKey:" + key;
    }

}
