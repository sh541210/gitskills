package com.iyin.sign.system.util.picUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author --wzz--
 * @version v1.0
 * @Title: CustomFontUtil
 * @Description: 取自定义字体
 * @date 2016/11/12
 */

public class CustomFontUtil {

    private static Logger logger = LoggerFactory.getLogger(CustomFontUtil.class);

    public static Font getCustomFont(String fileName){
        /*String filePathOfTTF = getFilePathByFileName(fileName);
        Font font;
        try{
            font = getCustomFontByFilePath(filePathOfTTF);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return font;*/

        Font font;
        try{
            String filePath = "config/fonts/"+fileName;
            logger.info("CustomFontUtil filePath={}", filePath);
            InputStream isInputStream = CustomFontUtil.class.getClassLoader().getResourceAsStream(filePath);
            font = getCustomFontByInputStream(isInputStream);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return font;
    }

    /**
     * wzz 1112 1.取文件路径
     * @param fileName TTF文件名
     * @return TTF文件路径
     */
    public static String getFilePathByFileName(String fileName){
       return new File(
                        CustomFontUtil.class.getClassLoader()
                        .getResource("config"+"/fonts/"+fileName).getFile()
                        ).getAbsolutePath();
        /*return new File(SourceUtils.sourcesDir+"fonts/"+fileName).getAbsolutePath();*/
    }

    /**
     * zjh 1202 1.取文件路径
     * @param fileName TTF文件名
     * @return TTF文件路径
     */
    public static String getFilePathByFileNameForFont(String fileName){
        URL fileUrl=CustomFontUtil.class.getClassLoader().getResource("config" + "/fonts/" + fileName);
        if(null != fileUrl){
            File file = new File(fileUrl.getFile());
            if(file.exists()){
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    /**
     * wzz 1112 2.取Font
     * @param filePath
     * @return
     * @throws IOException
     * @throws FontFormatException
     */
    public static Font getCustomFontByFilePath(String filePath) throws IOException, FontFormatException {
        File file = new File(filePath);
        Font myFont = Font.createFont(Font.TRUETYPE_FONT, file);
//        myFont = myFont.deriveFont(fontSize);
        return myFont;
    }

    /**
     * 根据文件流生成字体样式
     * @param inputStream
     * @return
     * @throws IOException
     * @throws FontFormatException
     */
    public static Font getCustomFontByInputStream(InputStream inputStream) throws IOException, FontFormatException {
        Font myFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        return myFont;
    }
}
