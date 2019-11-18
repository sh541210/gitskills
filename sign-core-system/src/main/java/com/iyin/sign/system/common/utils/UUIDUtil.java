package com.iyin.sign.system.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.UUID;

/**
 * @ClassName UUIDUtil
 * @Desscription: UUIDUtil
 * @Author wdf
 * @Date 2019/2/25 18:32
 * @Version 1.0
 **/
@Slf4j
public class UUIDUtil {

    private UUIDUtil(){}

    public static String getFormatUUID(){
        StringBuilder sb=new StringBuilder();
        String str=UUID.randomUUID().toString().toUpperCase().replace("-","").substring(0,16);
        sb.append(str.substring(0,4));
        sb.append("-");
        sb.append(str.substring(5,9));
        sb.append("-");
        sb.append(str.substring(10));
        return sb.toString();
    }

    public static void main(String[] args) {
        String filePath ="D://"+UUID.randomUUID()+".txt";
        //lic文件保存到本地
        try(OutputStream os= new FileOutputStream(filePath);){
            PrintStream ps=new PrintStream(os);
            ps.println(UUID.randomUUID());
        }catch (IOException e){
            log.error("生成文件异常"+e.getMessage());
        }
    }
}
