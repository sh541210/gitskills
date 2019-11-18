package com.iyin.sign.system.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @Title: SignImageUtil
 * @Description: 处理图片
 * @author tdf
 * @date 2016/10/24 14:33
 * @version v1.0
 */
public class ImageUtil {

    private static Logger log = LoggerFactory.getLogger(ImageUtil.class);

    public static void newAllResizeImage(String path, String imageFormat) throws IOException {
        File srcFile = new File(path);
        InputStream is = new FileInputStream(srcFile);
        BufferedImage prevImage = ImageIO.read(is);
        double width = prevImage.getWidth();
        double height = prevImage.getHeight();
        double percent = 631/width;
        int newWidth = (int)(width * percent);
        int newHeight = (int)(height * percent);
        Image srcImg = ImageIO.read(srcFile);
        BufferedImage buffImg = null;
        //BufferedImage.TYPE_INT_ARGB设置为透明
        buffImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB_PRE);
        buffImg.getGraphics().drawImage(
                srcImg.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0,
                0, null);
        ImageIO.write(buffImg, imageFormat, new File(path));
        is.close();
    }

    /**

     * 文件转化成byte数组
     * @Author: 刘志
     * @CreateDate: 2018/8/9 18:45
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/8/9 18:45
     * @Version: 0.0.1
     * @param filePath
     * @return byte[]
     */
    public static byte[] fileToBytes(String filePath) {
        byte[] buffer = null;
        File file = new File(filePath);

        try(
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream()
        ) {
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (FileNotFoundException ex) {
            log.error("找不到该文件：{}",ex.getMessage());
            // throw new BusinessException(EsealCertificationResponseCode.BIS_7004);
        } catch (IOException ex) {
            log.error("文件处理异常：{}",ex.getMessage());
            // throw new BusinessException(EsealCertificationResponseCode.BIS_7004);
        }
        return buffer;
    }

    /**

     * 添加水印，最后输出jpg格式图片
     * @Author: 刘志
     * @CreateDate: 2018/11/27 15:19
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/11/27 15:19
     * @Version: 0.0.1
     * @param srcImgPath 源图片路径
     * @param targetImgPath 目标图片路径
     * @param text 水印文字
     * @param radian 旋转角度
     * @return void
     */
    public static void addWaterMark(String srcImgPath, String targetImgPath, String text, Integer radian) throws IOException{
        // 读取原图片信息
        File srcImgFile = new File(srcImgPath);
        Image srcImg = ImageIO.read(srcImgFile);
        int srcImgWidth = srcImg.getWidth(null);
        int srcImgHeight = srcImg.getHeight(null);
        BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufImg.createGraphics();
        //画原图
        g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
        //水印色彩以及透明度
        Color color = new Color(0, 0, 0, 40);
        g.setColor(color);
        //设置字体
        Font font = new Font("微软雅黑", Font.BOLD + Font.ITALIC, 40);
        g.setFont(font);
        //设置倾斜
        g.rotate(Math.toRadians(radian), bufImg.getWidth() / 2.0, bufImg.getHeight() / 2.0);
        //水印内容
        String waterMarkContent = text;
        //设置水印的坐标
        int x = (srcImgWidth - getWatermarkLength(waterMarkContent, g)) / 4;
        int y = srcImgHeight / 4;
        //1/4位置画出水印
        g.drawString(waterMarkContent, x, y);
        int x1 = x * 2;
        int y1 = y * 2;
        //2/4位置画出水印
        g.drawString(waterMarkContent, x1, y1);
        int x2 = x * 3;
        int y2 = y * 3;
        //3/4位置画出水印
        g.drawString(waterMarkContent, x2, y2);
        g.dispose();
        // 输出图片
        FileOutputStream outImgStream = new FileOutputStream(targetImgPath);
        ImageIO.write(bufImg, "jpg", outImgStream);
        outImgStream.flush();
        outImgStream.close();
    }

    /**

     * 获取水印长度
     * @Author: 刘志
     * @CreateDate: 2018/11/27 15:34
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/11/27 15:34
     * @Version: 0.0.1
     * @param waterMarkContent
     * @param g
     * @return int
     */
    public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

    /**

     * 抠图移除白色背景，转png
     * @Author: 刘志
     * @CreateDate: 2018/11/28 14:19
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/11/28 14:19
     * @Version: 0.0.1
     * @param srcImgPath
     * @param targetImgPath
     * @return void
     */
    public static void transferAlpha(String srcImgPath, String targetImgPath) throws IOException{
        // 读取原图片信息
        File srcImgFile = new File(srcImgPath);
        Image srcImg = ImageIO.read(srcImgFile);
        int srcImgWidth = srcImg.getWidth(null);
        int srcImgHeight = srcImg.getHeight(null);
        BufferedImage bufferedImage = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2D =  bufferedImage.createGraphics();
        //画原图
        g2D.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
        //抠图
        int alpha = 0;
        for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
            for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                int rgb = bufferedImage.getRGB(j2, j1);

                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);
                if (((255 - r) < 30) && ((255 - g) < 30) && ((255 - b) < 30)) {
                    rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                }
                bufferedImage.setRGB(j2, j1, rgb);
            }
        }
        //画出抠完的图
        g2D.drawImage(bufferedImage, 0, 0, null);
        g2D.dispose();
        //输出图片
        FileOutputStream outImgStream = new FileOutputStream(targetImgPath);
        ImageIO.write(bufferedImage, "png", outImgStream);
        outImgStream.flush();
        outImgStream.close();
    }

    public static void main(String[] args) {
        try {
            addWaterMark("C:\\localSealFile\\temp\\A1.bmp", "C:\\localSealFile\\temp\\AA.jpg", "安印科技", 45);
            transferAlpha("C:\\localSealFile\\temp\\AA.jpg", "C:\\localSealFile\\temp\\AA.png");
        } catch (IOException e) {
            log.error("The error is {}" , e);
        }
    }

    /**
     * 删除文件的方法抽取
     *
     * @param imageLocalFile 待删除文件
     * @return void
     * @Author: 刘志
     * @CreateDate: 2018/9/1 10:40
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/9/1 10:40
     * @Version: 0.0.1
     */
    public static void deleteFileIfExists(File imageLocalFile) {
        Path path = imageLocalFile.toPath();
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("临时文件删除失败:{}", e);
//            throw new BusinessException(ApiResponseCode.BIS_5011);
        }
    }
}
