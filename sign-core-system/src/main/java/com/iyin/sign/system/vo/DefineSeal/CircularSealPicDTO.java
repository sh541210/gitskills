package com.iyin.sign.system.vo.DefineSeal;

import java.io.Serializable;

/**
 * @author:liushuqiao
 * @title: CircularSealPicDTO
 * @description:圆形印章图片的传输类，用于生成圆形印章时传输类,继承自印模图片父类
 * @date: 14:50 2017/9/28
 * @version: v1.0.0
 */
public class CircularSealPicDTO extends SealPicDTO implements Serializable {
    /**
     * 头部大小
     */
    private Integer sizeTop;
    /**
     * 中间大小
     */
    private Integer sizeCenter;
    /**
     * 底部大小
     */
    private Integer sizeBottom;

    /**
     * 头部字体样式文件
     */
    private String fontTop;
    /**
     * 尾部字体样式文件
     */
    private String fontBottom;
    /**
     * 板层的宽度大小
     */
    private Float plyCircle;

    /**
     * 板层与字之间的距离
     */
    private Float spaceBetweenPlyAndWord;

    /**
     * 板层与图片之间的距离
     */
    private Float spaceBetweenPicAndPly;
    /**
     * 图片顶上的文字
     */
    private String topCircle;
    /**
     * 图片中间的文字
     */
    private String centerCircle;
    /**
     * 图片底部的文字
     */
    private String bottomCircle;
    /**
     * 绝对路径:这里指本地的绝对路径D:/esign/seal/...
     */
    private String absPath;
    /**
     * 保存文件名
     */
    private String fileName;

    /**
     * 保存到FastDfs的存储URL
     */
    private String dfsUrl;

    public String getDfsUrl() {
        return dfsUrl;
    }

    public void setDfsUrl(String dfsUrl) {
        this.dfsUrl = dfsUrl;
    }

    public String getAbsPath() {
        return absPath;
    }

    public void setAbsPath(String absPath) {
        this.absPath = absPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTopCircle() {
        return topCircle;
    }

    public void setTopCircle(String topCircle) {
        this.topCircle = topCircle;
    }

    public String getCenterCircle() {
        return centerCircle;
    }

    public void setCenterCircle(String centerCircle) {
        this.centerCircle = centerCircle;
    }

    public String getBottomCircle() {
        return bottomCircle;
    }

    public void setBottomCircle(String bottomCircle) {
        this.bottomCircle = bottomCircle;
    }

    public Integer getSizeTop() {
        return sizeTop;
    }

    public void setSizeTop(Integer sizeTop) {
        this.sizeTop = sizeTop;
    }

    public Integer getSizeCenter() {
        return sizeCenter;
    }

    public void setSizeCenter(Integer sizeCenter) {
        this.sizeCenter = sizeCenter;
    }

    public Integer getSizeBottom() {
        return sizeBottom;
    }

    public void setSizeBottom(Integer sizeBottom) {
        this.sizeBottom = sizeBottom;
    }

    public String getFontTop() {
        return fontTop;
    }

    public void setFontTop(String fontTop) {
        this.fontTop = fontTop;
    }

    public String getFontBottom() {
        return fontBottom;
    }

    public void setFontBottom(String fontBottom) {
        this.fontBottom = fontBottom;
    }

    public Float getPlyCircle() {
        return plyCircle;
    }

    public void setPlyCircle(Float plyCircle) {
        this.plyCircle = plyCircle;
    }

    public Float getSpaceBetweenPlyAndWord() {
        return spaceBetweenPlyAndWord;
    }

    public void setSpaceBetweenPlyAndWord(Float spaceBetweenPlyAndWord) {
        this.spaceBetweenPlyAndWord = spaceBetweenPlyAndWord;
    }

    public Float getSpaceBetweenPicAndPly() {
        return spaceBetweenPicAndPly;
    }

    public void setSpaceBetweenPicAndPly(Float spaceBetweenPicAndPly) {
        this.spaceBetweenPicAndPly = spaceBetweenPicAndPly;
    }

    @Override
    public String toString() {
        return "CircularSealPicDTO{" +
                "sizeTop=" + sizeTop +
                ", sizeCenter=" + sizeCenter +
                ", sizeBottom=" + sizeBottom +
                ", fontTop='" + fontTop + '\'' +
                ", fontBottom='" + fontBottom + '\'' +
                ", plyCircle=" + plyCircle +
                ", spaceBetweenPlyAndWord=" + spaceBetweenPlyAndWord +
                ", spaceBetweenPicAndPly=" + spaceBetweenPicAndPly +
                ", topCircle='" + topCircle + '\'' +
                ", centerCircle='" + centerCircle + '\'' +
                ", bottomCircle='" + bottomCircle + '\'' +
                ", absPath='" + absPath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", dfsUrl='" + dfsUrl + '\'' +
                '}';
    }
}
