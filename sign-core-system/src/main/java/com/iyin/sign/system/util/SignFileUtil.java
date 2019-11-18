package com.iyin.sign.system.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @ClassName: SignFileUtil
 * @Description: 加签文件工具类
 * @Author: 唐德繁
 * @CreateDate: 2018/12/13 0013 下午 7:59
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/12/13 0013 下午 7:59
 * @Version: 0.0.1
 */
@Slf4j
public class SignFileUtil {

    private SignFileUtil(){}

    private static final String PDF_TERMINATOR = "%%EOF";
    private static final String ADBE_PKCS7_DETACHED = "adbe.pkcs7.detached";
    private static final String ADBE_PKCS7_SHA1 = "adbe.pkcs7.sha1";

    public static int countKeywords(InputStream inputStream, int size)throws IOException {
        int count = 0 ;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String nextLine;
            boolean b = false;
            int keywordCount = 0;
            while((nextLine=br.readLine())!=null){
                if(nextLine.indexOf(ADBE_PKCS7_DETACHED) > -1 || nextLine.indexOf(ADBE_PKCS7_SHA1) > -1){
                    b = true ;
                    keywordCount++;
                }
                if(b && nextLine.indexOf(PDF_TERMINATOR) > -1){
                    count++;
                }
                if (keywordCount == size){
                    return count;
                }
            }
        }catch (IOException e){
            log.error("IOException",e);
        }
        return count;
    }
}
