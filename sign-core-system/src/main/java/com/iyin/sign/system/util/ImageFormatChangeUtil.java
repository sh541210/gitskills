package com.iyin.sign.system.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author chenchunliang
 * @version v1.0.0
 * @Title: ImageFormatChangeUtil
 * @Description: 图片格式转换格式
 * @date 2018/3/22 15:24
 */
@Slf4j
public class ImageFormatChangeUtil {

    private ImageFormatChangeUtil(){}

    /**
     * 将非bmp图片转换为bmp图片
     * @param srcPath 源文件路径
     * @param targetPath 转换生成目标文件路径
     * @throws Exception
     */
    public static boolean changeToBmp(String srcPath, String targetPath) throws IOException{
        BufferedImage bufferedImage;

        //使用系统缓存
        ImageIO.setUseCache(false);
        //read image file
        bufferedImage = ImageIO.read(new File(srcPath));
        // create a blank, RGB, same width and height, and a white background
        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        //TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
        // write to jpeg file
        return ImageIO.write(newBufferedImage, "bmp", new File(targetPath));
    }

    /**
     * 将非png图片转换为png图片
     * @param srcPath
     * @param targetPath
     */
    public static void changeToPng(String srcPath, String targetPath){
        BufferedImage bufferedImage;
        try {
            //read image file
            bufferedImage = ImageIO.read(new File(srcPath));
            // create a blank, RGB, same width and height, and a white background
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            //TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            // write to jpeg file
            ImageIO.write(newBufferedImage, "png", new File(targetPath));

            log.info("转换png成功");

        } catch (IOException e) {
            log.error("The error msg is1 {}" ,e);
        }
    }

    /**
     * 将非bmp图片转换为bmp图片
     * @param srcPath 源文件路径
     * @param targetPath 转换生成目标文件路径
     * @throws Exception
     */
    public static boolean changeToJpg(String srcPath, String targetPath) throws IOException{
        BufferedImage bufferedImage;

        //使用系统缓存
        ImageIO.setUseCache(false);
        //read image file
        bufferedImage = ImageIO.read(new File(srcPath));
        // create a blank, RGB, same width and height, and a white background
        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        //TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
        // write to jpeg file
        return ImageIO.write(newBufferedImage, "jpg", new File(targetPath));
    }
}
