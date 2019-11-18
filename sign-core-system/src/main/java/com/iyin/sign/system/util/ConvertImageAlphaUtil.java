package com.iyin.sign.system.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName: ConvertImageAlphaUtil
 * @Description: 转图片Alpha工具
 * @Author: 唐德繁
 * @CreateDate: 2018/11/28 0028 下午 2:44
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/11/28 0028 下午 2:44
 * @Version: 0.0.1
 */
@Slf4j
public class ConvertImageAlphaUtil {

    private ConvertImageAlphaUtil() {}

    /**
     * 色差范围在0~255之间, 建议在255 - (10 ~ 50)之间
     */
    public static final int COLOR_RANGE = 210;

    public static byte[] convertByte(InputStream path) {
        long time=System.currentTimeMillis();
        byte[] b = null;
        try {
            BufferedImage image = ImageIO.read(path);
            ImageIcon imageIcon = new ImageIcon(image);
            int width = imageIcon.getIconWidth();
            int height = imageIcon.getIconHeight();
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, null);
            handleBufferImage(bufferedImage, height, width);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", out);
            b = out.toByteArray();
            out.close();
        } catch (IOException e) {
            log.error("图片处理异常：{}", e.getMessage());
        }
        log.info("convertByteTime:"+(System.currentTimeMillis()-time));
        return b;
    }

    public static BufferedImage convertBufferedImage(BufferedImage image) {
        BufferedImage bufferedImage = null;
        try {
            ImageIcon imageIcon = new ImageIcon(image);
            int w = imageIcon.getIconWidth();
            int h = imageIcon.getIconHeight();
            bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR_PRE);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(image.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING), 0,
                    0, null);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            handleBufferImage(bufferedImage, height, width);
        } catch (Exception e) {
            log.error("处理章模图片异常：{}", e.getMessage());
        }
        return bufferedImage;
    }

    private static void handleBufferImage(BufferedImage bufferedImage, int height, int width) {
        int alpha;
        for (int j1 = 0; j1 < height; j1++) {
            for (int j2 = 0; j2 < width; j2++) {
                int rgb = bufferedImage.getRGB(j2, j1);
                if (!colorInRange(rgb)) {
                    alpha = 230;
                }else{
                    alpha = 0;
                }
                rgb = (alpha << 24) | (rgb & 0x00ffffff);
                bufferedImage.setRGB(j2, j1, rgb);
            }
        }
    }

    private static boolean colorInRange(int color) {
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        return (red >= COLOR_RANGE && green >= COLOR_RANGE && blue >= COLOR_RANGE)||color == 0;
    }
}
