package com.iyin.sign.system.common.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 加密工具，对称加密算法，采用随机Key
 *
 * @author CodeApe
 * @version 1.0
 * @Description
 * @date 2013-04-02
 * @Copyright: Copyright (c) 2013 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class EncryptUtil {

    /**
     * 解密是否成功的校验码
     */
    private static final String CHECK_CODE = "12345678";

    /**
     * MD5加密
     *
     * @param plaintext 需要进行MD5加密的明文
     * @return ciphertext MD5加密后的密文
     * @version 1.0
     */
    public static String md5EncryptNormal(String plaintext) {// 保持编码为UTF-8
        if (StringUtils.isEmpty(plaintext)) {
            plaintext = CHECK_CODE;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plaintext.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * sha加密
     *
     * @param decript 需要进行MD5加密的明文
     * @return ciphertext MD5加密后的密文
     * @version 1.0
     */
    public static String shaEncrypt(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(decript.getBytes());
            byte[] messageDigest
                    = digest.digest();

            StringBuilder hexString = new StringBuilder();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * encrypt加密
     *
     * @param password 需要进行MD5加密的明文
     * @return 加密后的密文
     * @version 1.0
     */
    public static String encryptPass(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    /**
     * encrypt加密
     *
     * @param oldPassword   用户输入密码明文
     * @param newEncodePass 新加密密码
     * @return true or false
     * @version 1.0
     */
    public static boolean matchPass(String oldPassword, String newEncodePass) {
        return new BCryptPasswordEncoder().matches(oldPassword, newEncodePass);
    }

    public static void main(String[] args) {
        String ss = EncryptUtil.encryptPass(CHECK_CODE);
        System.out.println(ss);
        boolean f = EncryptUtil.matchPass(CHECK_CODE, ss);
        System.out.println(f);

    }
}
