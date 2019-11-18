package com.iyin.sign.system.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
	* @Description pdf转换图片
	* @Author: wdf 
    * @CreateDate: 2019/3/5 16:50
	* @UpdateUser: wdf
    * @UpdateDate: 2019/3/5 16:50
	* @Version: 0.0.1
    * @param 
    * @return 
    */
@Slf4j
public class PdfUtils {

    private PdfUtils() {
        throw new IllegalStateException("PdfUtils class");
    }

    /**
     * 删除文件
     * @author wdf
     * @date 2019/3/5
     * @param  * @param filePath
     * @return void
    */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            log.error("del file error:"+e);
        }
    }

}
