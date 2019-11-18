package com.iyin.sign.system.vo.DefineSeal;

import java.io.Serializable;

/**
 * @author:liushuqiao
 * @title: OvalSealPicDTO
 * @description:椭圆章的生成包装类，继承自印章图片父类
 * @date: 14:46 2017/12/19
 * @version: v1.0.0
 */
public class OvalSealPicDTO extends SealPicDTO implements Serializable {
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

    /**
     * 字体文件
     */
    private String fontPath;
    /**
     * xdox文件绝对路径
     */
    private String xdocPath;

    /**
     * 顶部文字
     */
    private String topWords;

    /**
     * 底部方字
     */
    private String bottomWords;

    public String getTopWords() {
        return topWords;
    }

    public void setTopWords(String topWords) {
        this.topWords = topWords;
    }

    public String getBottomWords() {
        return bottomWords;
    }

    public void setBottomWords(String bottomWords) {
        this.bottomWords = bottomWords;
    }

    public String getFontPath() {
        return fontPath;
    }

    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }

    public String getXdocPath() {
        return xdocPath;
    }

    public void setXdocPath(String xdocPath) {
        this.xdocPath = xdocPath;
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

    public String getDfsUrl() {
        return dfsUrl;
    }

    public void setDfsUrl(String dfsUrl) {
        this.dfsUrl = dfsUrl;
    }

    @Override
    public String toString() {
        return "OvalSealPicDTO{" +
                "absPath='" + absPath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", dfsUrl='" + dfsUrl + '\'' +
                ", fontPath='" + fontPath + '\'' +
                ", xdocPath='" + xdocPath + '\'' +
                ", topWords='" + topWords + '\'' +
                ", bottomWords='" + bottomWords + '\'' +
                '}';
    }
}
