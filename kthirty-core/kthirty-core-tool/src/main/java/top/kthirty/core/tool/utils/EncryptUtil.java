package top.kthirty.core.tool.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import top.kthirty.core.tool.jackson.JsonUtil;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 加密解密工具类
 * </p>
 *
 * @author KThirty
 * @since 2023/11/19
 */
public class EncryptUtil {
    private static final String SM4_KEY = "Qf7W*geRWvM$XzZo";
    private static final SymmetricCrypto SM4 = SmUtil.sm4(SM4_KEY.getBytes(StandardCharsets.UTF_8));

    /**
     * 国密4加密
     *
     * @param plaintext 明文
     * @return 密文
     */
    public static String sm4Encrypt(String plaintext) {
        return SM4.encryptHex(plaintext);
    }

    /**
     * 国密4解密
     *
     * @param ciphertext 密文
     * @return 明文
     */
    public static String sm4Decrypt(String ciphertext) {
        return SM4.decryptStr(ciphertext);
    }

    public static void main(String[] args) {
        Dict dict = Dict.create().set("name", "测试文件.txt").set("type","minio").set("bucket", "test").set("url", "test/20211204/2020293891782381628736.txt");
        String plaintext = JsonUtil.toJson(dict);
        String ciphertext = sm4Encrypt(plaintext);
        System.out.println("加密后文本:"+ciphertext+"明文长度"+plaintext.length()+"密文长度"+ciphertext.length());
        String plaintext1 = sm4Decrypt(ciphertext);
    }


}
