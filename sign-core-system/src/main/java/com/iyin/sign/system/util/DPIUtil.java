package com.iyin.sign.system.util;//package net.iyin.common.util.image;
//
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
///**
// * @ClassName:   DPIUtil
// * @Description: 设置图片文件DPI
// * @Author:      杨兰
// * @CreateDate:  2018/11/7 15:51
// * @UpdateUser:  杨兰
// * @UpdateDate:  2018/11/7 15:51
// * @Version:     0.0.1
// */
//public class DPIUtil {
//    private static int DPI = 300;
//
//    public static void main(String[] args) {
//        String path = "C:\\Users\\Rambo\\Desktop\\Temp\\test.bmp";
//        File file = new File(path);
//        handleDpi(file, DPI, DPI);
//    }
//
//    /**
//     * 改变图片DPI
//     *
//     * @param file
//     * @param xDensity
//     * @param yDensity
//     */
//    public static void handleDpi(File file, int xDensity, int yDensity) {
//        try {
//            BufferedImage image = ImageIO.read(file);
//            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(new FileOutputStream(file));
//            JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(image);
//            jpegEncodeParam.setDensityUnit(JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);
//            jpegEncoder.setJPEGEncodeParam(jpegEncodeParam);
//            jpegEncodeParam.setQuality(0.75f, false);
//            jpegEncodeParam.setXDensity(xDensity);
//            jpegEncodeParam.setYDensity(yDensity);
//            jpegEncoder.encode(image, jpegEncodeParam);
//            image.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
