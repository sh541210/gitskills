package com.iyin.sign.system.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @ClassName: MD5Util.java
 * @Description: Md5校验工具类
 * @Author: yml
 * @CreateDate: 2019/6/27
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/27
 * @Version: 1.0.0
 */
@Slf4j
public class MD5Util {
    private MD5Util() {
    }

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Get MD5 of a file (lower case)
     * @return empty string if I/O error when get MD5
     */
    public static String getFileMD5( File file) {

        try (FileInputStream in = new FileInputStream(file)){
            FileChannel ch = in.getChannel();
            return MD5(ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length()));
        } catch (IOException e) {
            return "";
        }

    }

    /**
     * MD5校验字符串
     * @param s String to be MD5
     * @return 'null' if cannot get MessageDigest
     */

    public static String getStringMD5( String s) {
        MessageDigest mdInst;
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            mdInst = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            return "";
        }

        byte[] btInput = s.getBytes();
        // 使用指定的字节更新摘要
        mdInst.update(btInput);
        // 获得密文
        byte[] md = mdInst.digest();
        // 把密文转换成十六进制的字符串形式
        int length = md.length;
        char[] str = new char[length * 2];
        int k = 0;
        for (byte b : md) {
            str[k++] = HEX_DIGITS[b >>> 4 & 0xf];
            str[k++] = HEX_DIGITS[b & 0xf];
        }
        return new String(str);
    }


    @SuppressWarnings("unused")
    private static String getSubStr( String str, int subNu, char replace) {
        int length = str.length();
        if (length > subNu) {
            str = str.substring(length - subNu, length);
        } else if (length < subNu) {
            // NOTE: padding字符填充在字符串的右侧，和服务器的算法是一致的
            str += createPaddingString(subNu - length, replace);
        }
        return str;
    }


    private static String createPaddingString(int n, char pad) {
        if (n <= 0) {
            return "";
        }

        char[] paddingArray = new char[n];
        Arrays.fill(paddingArray, pad);
        return new String(paddingArray);
    }

    /**
     * 计算MD5校验
     * @param buffer
     * @return 空串，如果无法获得 MessageDigest实例
     */

    private static String MD5(ByteBuffer buffer) {
        String s = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buffer);
            // MD5 的计算结果是一个 128 位的长整数，
            byte[] tmp = md.digest();
            // 用字节表示就是 16 个字节 ，每个字节用 16 进制表示的话，使用两个字符，
            char[] str = new char[16 * 2];
            // 所以表示成 16 进制需要 32 个字符  表示转换结果中对应的字符位置
            int k = 0;
            // 从第一个字节开始，对 MD5 的每一个字节
            for (int i = 0; i < 16; i++) {
                // 转换成 16 进制字符的转换  取第 i 个字节
                byte byte0 = tmp[i];
                // 取字节中高 4 位的数字转换, >>>,
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                // 逻辑右移，将符号位一起右移  取字节中低 4 位的数字转换
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            // 换后的结果转换为字符串
            s = new String(str);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
        }
        return s;
    }

    /**
     * 获取文件的md5值 ，有可能不是32位
     * @param filePath	文件路径
     * @return
     * @throws FileNotFoundException
     */
    public static String md5HashCode(String filePath) throws FileNotFoundException{
        FileInputStream fis = new FileInputStream(filePath);
        return md5HashCode(fis);
    }

    /**
     * 保证文件的MD5值为32位
     * @param filePath	文件路径
     * @return
     * @throws FileNotFoundException
     */
    public static String md5HashCode32(String filePath) throws FileNotFoundException{
        FileInputStream fis = new FileInputStream(filePath);
        return md5HashCode32(fis);
    }

    /**
     * 保证文件的MD5值为32位
     * @param file	文件
     * @return
     * @throws FileNotFoundException
     */
    public static String md5HashCode32(MultipartFile file) throws IOException{
        return md5HashCode32(file.getInputStream());
    }

    /**
     * java获取文件的md5值
     * @param fis 输入流
     * @return
     */
    public static String md5HashCode(InputStream fis) {
        try {
            //拿到一个MD5转换器,如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
            MessageDigest md = MessageDigest.getInstance("MD5");

            //分多次将一个文件读入，对于大型文件而言，比较推荐这种方式，占用内存比较少。
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close();
            //转换并返回包含16个元素字节数组,返回数值范围为-128到127
            byte[] md5Bytes  = md.digest();
            //1代表绝对值
            BigInteger bigInt = new BigInteger(1, md5Bytes);
            //转换为16进制
            return bigInt.toString(16);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * java计算文件32位md5值
     * @param fis 输入流
     * @return
     */
    public static String md5HashCode32(InputStream fis) {
        try {
            //拿到一个MD5转换器,如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
            MessageDigest md = MessageDigest.getInstance("MD5");

            //分多次将一个文件读入，对于大型文件而言，比较推荐这种方式，占用内存比较少。
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close();

            //转换并返回包含16个元素字节数组,返回数值范围为-128到127
            byte[] md5Bytes  = md.digest();
            StringBuilder hexValue = new StringBuilder();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;//解释参见最下方
                if (val < 16) {
                    /**
                     * 如果小于16，那么val值的16进制形式必然为一位，
                     * 因为十进制0,1...9,10,11,12,13,14,15 对应的 16进制为 0,1...9,a,b,c,d,e,f;
                     * 此处高位补0。
                     */
                    hexValue.append("0");
                }
                //这里借助了Integer类的方法实现16进制的转换
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString().toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }

}