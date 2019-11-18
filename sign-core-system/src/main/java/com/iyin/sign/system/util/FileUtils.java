package com.iyin.sign.system.util;

import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author tdf
 * @version v1.0
 * @Title FileUtils
 * @Description 文件工具类
 * @date 2017/9/28 16:09
 */
@Slf4j
public class FileUtils {

    private FileUtils(){}

    private static final int BLOCK_SIZE = 4096;

    public static void  getFilePath(InputStream inputStream,OutputStream out){
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            inputStream.close();
        }catch (Exception e) {
            log.error("获取文件路径异常：{}", e.getMessage());
        }
    }

    public static String getFileExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return null;
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws ServiceException {
        try {
            byte[] buffer = new byte[BLOCK_SIZE];
            int n;
            while ((n = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, n);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(ErrorCode.SERVER_50401);
        }
    }

    /**
    	* @Description 流转字节
    	* @Author: wdf
        * @CreateDate: 2019/3/4 16:45
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/3/4 16:45
    	* @Version: 0.0.1
        * @param is
        * @return byte[]
        */
    public static byte[] toBytesArray(InputStream is) throws ServiceException  {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] b = new byte[BLOCK_SIZE];
            int n;
            while ((n = is.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(ErrorCode.SERVER_50401);
        }
    }

    public static InputStream fileUrl(String path){
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try{
            URL url =  new URL(path);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200){
                inputStream = httpURLConnection.getInputStream();
            }
        }catch (Exception e){
            log.error("文件链接异常：{}", e.getMessage());
        }
        return inputStream;
    }

}
