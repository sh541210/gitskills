package com.iyin.sign.system.util.picUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author wzz
 * @version v1.0
 * @Title: PropertiesUtil
 * @Description: 证书固定的一些属性
 * @date 2016/8/3
 */
public class SealPropConfigUtil {

    private static Logger logger = LoggerFactory.getLogger(SealPropConfigUtil.class);

    private static Properties prop = new Properties();
    private final static String file = SealPropConfigUtil.class.getClassLoader().getResource("").getPath() + "config/seal_pic.properties";

    //private final static String file = SourceUtils.sourcesDir+"seal_pic.properties";

    //private final static String FILE_PATH_OF_XDOC_FOR_OVAL_1 = new File(SealPropConfigUtil.class.getClassLoader().getResource("config/oval_1.xdoc").getFile()).getAbsolutePath();
    //private final static String FILE_PATH_OF_XDOC_FOR_OVAL_2 = new File(SealPropConfigUtil.class.getClassLoader().getResource("config/oval_2.xdoc").getFile()).getAbsolutePath();
    //private final static String DIR_PATH_OF_FONT = new File(SealPropConfigUtil.class.getClassLoader().getResource("config/fonts").getFile()).getAbsolutePath();

    static {
        try {
            //prop.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String filePath = "config/seal_pic.properties";
            logger.info("SealPropConfigUtil filePath={}", filePath);
            InputStream isInputStream = SealPropConfigUtil.class.getClassLoader().getResourceAsStream(filePath);
            prop.load(isInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPropValue(String key) {
        return prop.getProperty(key);
    }

    public static void setProper(String key, String value) {
        /**
         * 将文件加载到内存中，在内存中修改key对应的value值，再将文件保存
         */
        try {
            prop.setProperty(key, value);
            FileOutputStream fos = new FileOutputStream(file);
            prop.store(fos, null);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*public static String getFilePathOfXdocForOval1(){
        return FILE_PATH_OF_XDOC_FOR_OVAL_1;
    }

    public static String getFilePathOfXdocForOval2(){
        return FILE_PATH_OF_XDOC_FOR_OVAL_2;
    }

    public static String getDirPathOfFont(){
        return DIR_PATH_OF_FONT;
    }
*/

}
