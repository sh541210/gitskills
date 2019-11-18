package com.iyin.sign.system.vo.DefineSeal;

import java.io.Serializable;

/**
 * @author:liushuqiao
 * @title: SealPicDTO
 * @description:生成印章图片的父类，用于各种圆形、椭圆形、方形章模的继承用
 * @date: 14:51 2017/9/28
 * @version: v1.0.0
 */
public class SealPicDTO implements Serializable {

    /**
     * 企业编码
     */
    private String enterpriseCode;

    /**
     * 印章编码
     */
    private String sealCode;
    /**
     * 印章的形状：0圆形，1椭圆形，2方形
     */
    private Integer shape;
    /**
     * 中心图案:0五角星，1三角形，2矩形，3十字形，99无图案
     */
    private Integer centerDesign;
    /**
     * 宽的像素
     */
    private Integer widthPX;
    /**
     * 高的像素
     */
    private Integer heightPX;
    /**
     * 实际的宽度
     */
    private Integer widthReal;
    /**
     * 实际的高度
     */
    private Integer heightReal;
    /**
     * 存放印模样章的根目录
     */
    private String dirPath1;
    /**
     * 存放印模样章的相对目录
     */
    private String dirPath2;
    /**
     * RGB位深颜色
     */
    private String colorRgb;

    /**
     * dpi分辨率
     */
    private Integer dpi;
    /**
     * 可使用的年限
     */
    private Integer year;

    public Integer getShape() {
        return shape;
    }

    public void setShape(Integer shape) {
        this.shape = shape;
    }

    public Integer getCenterDesign() {
        return centerDesign;
    }

    public void setCenterDesign(Integer centerDesign) {
        this.centerDesign = centerDesign;
    }

    public Integer getWidthPX() {
        return widthPX;
    }

    public void setWidthPX(Integer widthPX) {
        this.widthPX = widthPX;
    }

    public Integer getHeightPX() {
        return heightPX;
    }

    public void setHeightPX(Integer heightPX) {
        this.heightPX = heightPX;
    }

    public Integer getWidthReal() {
        return widthReal;
    }

    public void setWidthReal(Integer widthReal) {
        this.widthReal = widthReal;
    }

    public Integer getHeightReal() {
        return heightReal;
    }

    public void setHeightReal(Integer heightReal) {
        this.heightReal = heightReal;
    }

    public String getDirPath1() {
        return dirPath1;
    }

    public void setDirPath1(String dirPath1) {
        this.dirPath1 = dirPath1;
    }

    public String getDirPath2() {
        return dirPath2;
    }

    public void setDirPath2(String dirPath2) {
        this.dirPath2 = dirPath2;
    }

    public String getColorRgb() {
        return colorRgb;
    }

    public void setColorRgb(String colorRgb) {
        this.colorRgb = colorRgb;
    }

    public Integer getDpi() {
        return dpi;
    }

    public void setDpi(Integer dpi) {
        this.dpi = dpi;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public String getSealCode() {
        return sealCode;
    }

    public void setSealCode(String sealCode) {
        this.sealCode = sealCode;
    }

    @Override
    public String toString() {
        return "SealPicDTO{" +
                "enterpriseCode='" + enterpriseCode + '\'' +
                ", sealCode='" + sealCode + '\'' +
                ", shape=" + shape +
                ", centerDesign=" + centerDesign +
                ", widthPX=" + widthPX +
                ", heightPX=" + heightPX +
                ", widthReal=" + widthReal +
                ", heightReal=" + heightReal +
                ", dirPath1='" + dirPath1 + '\'' +
                ", dirPath2='" + dirPath2 + '\'' +
                ", colorRgb='" + colorRgb + '\'' +
                ", dpi=" + dpi +
                ", year=" + year +
                '}';
    }
}
