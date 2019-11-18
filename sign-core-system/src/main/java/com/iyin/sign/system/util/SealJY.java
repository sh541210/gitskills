package com.iyin.sign.system.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tdf
 * @version v1.0
 * @Title SealJY
 * @Description 解压缩
 * @date 2017/10/16 11:07
 */
public class SealJY {

    private static final Logger logger = LoggerFactory.getLogger(SealJY.class);

    public static int[] initImageDate(int[] byteSealBasicTX, int width, int height) {
        int[] result = new int[byteSealBasicTX.length];
        for (int y = 0; y <= (height - 1); y++) {
            for (int x = 0; x <= (width - 1); x++) {
                int cc = 22 + x + y * width;
                int dd = byteSealBasicTX[cc];
                if (dd == 0x3) {//防标志
                    result[cc] = 0x0;
                } else {
                    result[cc] = byteSealBasicTX[cc];
                }
            }
        }
        return result;
    }

    public static BufferedImage getTX(int[] byteTX, int width, int height, String type, String filePath) {
        int[] byteSealBasicTX = initImageDate(byteTX, width, height);
        BufferedImage bmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y <= (height - 1); y++) {
            for (int x = 0; x <= (width - 1); x++) {
                int cc = 22 + x + y * width;

                int dd = byteSealBasicTX[cc];
                Color sColor = new Color(255, 255, 255);
                if (type == "0") {
                    if (dd == 0x0) {//前景红色
                        sColor = new Color(255, 0, 0);
                    } else if (dd == 0x1) {//背景白色
                        sColor = new Color(255, 255, 255);
                    } else if (dd == 0x2) {//数字蓝色
                        sColor = new Color(0, 0, 255);
                    } else if (dd == 0x3) {//防标志
                        sColor = new Color(0, 255, 0);
                    }
                } else {
                    if (dd == 0x0) {
                        sColor = new Color(0, 0, 0);
                    } else if (dd == 0x1) {
                        sColor = new Color(255, 255, 255);
                    } else if (dd == 0x2) {
                        sColor = new Color(0, 0, 0);
                    } else if (dd == 0x3) {
                        sColor = new Color(255, 255, 255);
                    }
                }
                Graphics g = bmp.getGraphics();
                g.setColor(sColor);
                g.drawLine(x, y, x, y);
            }
        }
            File f = new File(filePath);
            try {
                ImageIO.write(bmp, "png", f);
                bmp.flush();
            } catch (IOException e) {

                logger.error("The error is {}" , e);
            }
        return bmp;
    }

    public static int[] yzjys(int[] BinByte, int width, int height) {
        int[] ImgByte;
        int n, m;
        int i, k, nLine;
        int j, w;
        byte TZ = 0x0;
        boolean wxy;
        int BinLen;
        final byte ImgBJ = 0x20;//32
        final byte ImgBM = 0x40;//64
        final byte ImgQJTZ = 0x60;//96
        final byte ImgBJTZ = (byte) 0x80;//128
        int[] Result;



        ImgByte = new int[22 + width * height + 10000];

        for (k = 0; k <= 21; k++) {
            ImgByte[k] = BinByte[k];
        }
        for (i = 22; i <= 22 + width * height; i++) {
            ImgByte[i] = 0x1;
        }
        ImgByte[21] = 50;
        BinLen = BinByte.length;
        i = 0;
        j = 0;
        k = 22;
        w = 22;
        nLine = 0;
        try {
            while (k < BinLen) {
                wxy = true;
                if ((BinByte[k] < 32) && (BinByte[k] > 0x0)) {
                    TZ = 0x0;
                    i = BinByte[k];
                } else if ((BinByte[k] < 225) && (BinByte[k] >= 192)) {
                    TZ = 0x0;
                    m = BinByte[k] - 192;
                    k = k + 1;
                    n = BinByte[k];
                    i = m * 256 + n;
                } else if ((BinByte[k] < 64) && (BinByte[k] > 32)) {
                    TZ = 0x1;
                    i = BinByte[k] - ImgBJ;
                } else if ((BinByte[k] < 192) && (BinByte[k] >= 160)) {
                    TZ = 0x1;
                    m = BinByte[k] - 160;
                    k = k + 1;
                    n = BinByte[k];
                    i = m * 256 + n;
                } else if ((BinByte[k] < 96) && (BinByte[k] > 64)) {
                    TZ = 0x2;
                    i = BinByte[k] - ImgBM;
                } else if ((BinByte[k] < 128) && (BinByte[k] > 96)) {
                    TZ = 0x3;
                    i = BinByte[k] - ImgQJTZ;
                } else if ((BinByte[k] < 160) && (BinByte[k] > 128)) {
                    TZ = 0x4;
                    i = BinByte[k] - ImgBJTZ;
                } else if (BinByte[k] == 0) {
                    nLine = nLine + 1;
                    w = 22 + nLine * width;
                    wxy = false;
                }

                if ((w > 22 + width * height)) {
                    wxy = false;
                }

                if (wxy) {
                    for (j = w; j <= w + i - 1; j++) {
                        ImgByte[j] = TZ;
                    }
                    w = w + i;
                }

                k = k + 1;
            }
        } catch (Exception e) {
            logger.error("The error is {}" , e);
        }
        Result = ImgByte;
        return Result;
    }

    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(String filePath) {
        File file = new File(filePath);
        byte[] buffer = null;
        try(
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1000)
        ) {
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            logger.error("The error msg is1 {}" ,e);
        } catch (IOException e) {
            logger.error("The error msg is2 {}" ,e);
        }
        return buffer;
    }

    /**
     * //解压缩
     *
     * @param seal_ys_byte 压缩后的印模数据
     */
    public static void jyTest(byte[] seal_ys_byte, String filePath) {
        int nWidth = 0, nHeight = 0;
        byte[] ysByteSealBasicTX = seal_ys_byte;
        String nWidthStr = String.valueOf((char) ((int) (ysByteSealBasicTX[13]))) + String.valueOf((char) ((int) (ysByteSealBasicTX[14]))) + String.valueOf((char) ((int) (ysByteSealBasicTX[15])));
        String nHeightStr = String.valueOf((char) ((int) (ysByteSealBasicTX[17]))) + String.valueOf((char) ((int) (ysByteSealBasicTX[18]))) + String.valueOf((char) ((int) (ysByteSealBasicTX[19])));
        try {
            nWidth = Integer.parseInt(nWidthStr);
            nHeight = Integer.parseInt(nHeightStr);
        } catch (RuntimeException e) {
            nWidth = 0;
            nHeight = 0;
        }
        if (nWidth != 0 && nHeight != 0) {
            int data[] = new int[ysByteSealBasicTX.length];
            for (int i = 0; i < ysByteSealBasicTX.length; i++) {
                data[i] = ysByteSealBasicTX[i] & 0xff;
            }
            int[] byteSealBasicTX = yzjys(data, nWidth, nHeight);
            int data_byte[] = new int[byteSealBasicTX.length];
            for (int i = 0; i < byteSealBasicTX.length; i++) {
                data_byte[i] = byteSealBasicTX[i];
            }
            getTX(data_byte, nWidth, nHeight, "0", filePath);
        }
    }


    /**
     * //解压缩章模图片获取宽高
     */
    public static Map<String, Integer> jyWHPicData(String picData) {
        byte[] seal_ys_byte = Base64.decodeBase64(picData);
        HashMap<String, Integer> resultMap = new HashMap<>();

        int nWidth = 0, nHeight = 0;
        byte[] ysByteSealBasicTX = seal_ys_byte;
        String nWidthStr = String.valueOf((char) ((int) (ysByteSealBasicTX[13]))) + String.valueOf((char) ((int) (ysByteSealBasicTX[14]))) + String.valueOf((char) ((int) (ysByteSealBasicTX[15])));
        String nHeightStr = String.valueOf((char) ((int) (ysByteSealBasicTX[17]))) + String.valueOf((char) ((int) (ysByteSealBasicTX[18]))) + String.valueOf((char) ((int) (ysByteSealBasicTX[19])));
        try {
            nWidth = Integer.parseInt(nWidthStr);
            nHeight = Integer.parseInt(nHeightStr);
        } catch (RuntimeException e) {
            nWidth = 0;
            nHeight = 0;
        }
        resultMap.put("nWidth", (int) (nWidth / 400.0 * 25.4));
        resultMap.put("nHeight", (int) (nHeight / 400.0 * 25.4));
        return resultMap;
    }


    public static void main(String args[]) throws Exception {
        //base64后的印模数据（压缩过后存在数据库的数据）
        String decode = "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";
        //解压印模并生成图片
//        SealJY.jyTest(Base64Util.decode(decode),"D://image//201608181125.png");
        Map<String, Integer> stringIntegerMap = SealJY.jyWHPicData(decode);
        logger.info("info = {}" , stringIntegerMap);
    }
}
