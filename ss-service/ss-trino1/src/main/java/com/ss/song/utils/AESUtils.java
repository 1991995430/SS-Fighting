package com.ss.song.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * AES加解密工具类
 * @author suwenbao
 * @since 2022/10/24
 */
@Data
@Slf4j
public class AESUtils {
    /**
     * 指定加密算法
     */
    private static final String AES = "AES";

    /**
     * 声明完整算法 用于密码构造器
     */
    private static final String AES_CIPHER = "AES/ECB/PKCS5Padding";

    /**
     * 指定生成AESKey的种子
     */
    private static final String KEY = "DataStudio";

    /**
     * 加密
     * @param content 需要加密的内容
     * @return 加密后的内容
     */
    public static String encrypt(String content) {
        try {
            //AES密钥
            String keyStr = getAESKey();
            //根据自定义的密钥构建SecretKeySpec对象
            SecretKeySpec keySpec = new SecretKeySpec(Hex.decodeHex(keyStr.toCharArray()), AES);
            //创建密码器，声明为AES算法
            Cipher cipher = Cipher.getInstance(AES_CIPHER);
            //初始化
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] result = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encodeHex(result));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param content 加密后的密文
     * @return 解密后的内容
     */
    public static String decrypt(String content) {
        try {
            //AES密钥
            String keyStr = getAESKey();
            SecretKeySpec keySpec = new SecretKeySpec(Hex.decodeHex(keyStr.toCharArray()), AES);
            Cipher cipher = Cipher.getInstance(AES_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] result = cipher.doFinal(Hex.decodeHex(content.toCharArray()));
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据自定义的key得到128bit的密钥
     */
    public static String getAESKey() throws Exception {
//        SecureRandom random = new SecureRandom(KEY.getBytes());
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(KEY.getBytes());
        KeyGenerator generator;
        generator = KeyGenerator.getInstance(AES);
        generator.init(128, random);
        byte[] keyBytes = generator.generateKey().getEncoded();
//        log.info("加密后的字符串：{}", new String(keyBytes));
        return new String(Hex.encodeHex(keyBytes));
    }
}
