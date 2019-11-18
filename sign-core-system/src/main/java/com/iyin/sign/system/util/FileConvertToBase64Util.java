package com.iyin.sign.system.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName: FileConvertToBase64Util
 * @Description: 文件转Base64字符串
 * @Author: 唐德繁
 * @CreateDate: 2018/12/3 0003 上午 11:12
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/12/3 0003 上午 11:12
 * @Version: 0.0.1
 */
@Slf4j
public class FileConvertToBase64Util {

    private FileConvertToBase64Util(){}

    public static void main(String[] args){
        String fileName ="T:\\400.bmp";
        String base64 = fileToBase64(fileName);
        log.info("base64:{}",base64);
        base64ToFile(base64,"T:\\402.bmp");
    }

    public static void base64ToFile(String base64Str,String filePath) {
        byte[] deByte = Base64.decodeBase64(base64Str);
        try {
            FileUtils.writeByteArrayToFile(new File(filePath), deByte);
        } catch (IOException e) {
            log.error("base64转换文件失败：{}", e);
        }
    }

    public static String fileToBase64(String fileName)  {
        File file = new File(fileName);
        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            log.error("文件转换base64失败：{}", e);
        }
        return Base64.encodeBase64String(bytes);
    }

    public static String fileToBase64(File file)  {
        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            log.error("文件转换base64失败：{}", e);
        }
        return Base64.encodeBase64String(bytes);
    }

    /**
     * MultipartFile转Base64
     * @Author: 唐德繁
     * @CreateDate: 2019/03/04 下午 3:33
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2019/03/04 下午 3:33
     * @Version: 0.0.1
     * @param file
     * @return
     */
    public static String multipartFileToBase64(MultipartFile file) {
        byte[] bytes = new byte[0];
        try {
            bytes = file.getBytes();
        }catch (Exception e) {
            log.error("MultipartFile转Base64异常：{}", e.getMessage());
        }
        return Base64.encodeBase64String(bytes);
    }
}
