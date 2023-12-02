package top.kthirty.core.secure.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;

import java.nio.charset.StandardCharsets;

public class PasswordUtil {
    public static final byte[] BASE_SALT = "dSGWCHBNFcaa172".getBytes(StandardCharsets.UTF_8);

    /**
     * 密码加密 SHA1(MD5(salt,plaintext))
     * @param plaintext 明文
     * @return ciphertext
     */
    public static String encrypt(String plaintext){
        return SecureUtil.sha1(new MD5(BASE_SALT).digestHex(plaintext));
    }

    /**
     * 验证密码
     * @param plaintext 明文
     * @param ciphertext 密文
     * @return 是否通过
     */
    public static boolean validate(String plaintext,String ciphertext){
        return encrypt(plaintext).equals(ciphertext);
    }
}
