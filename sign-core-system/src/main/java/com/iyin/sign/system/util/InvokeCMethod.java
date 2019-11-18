package com.iyin.sign.system.util;

import com.iyin.sign.system.model.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class InvokeCMethod {

    private InvokeCMethod(){}

    /**
     *
     * @param realAppPath   可执行文件名称（跟完整路径）
     * @param picType       待处理图片的图片格式（目前只至此BMP格式）
     * @param realInPath    待处理图片的图片路径
     * @param fileInName    待处理图片的图片名称
     * @param realOutPath   处理完成后图片的保存路径
     * @param fileOutName   处理完成后图片的名称
     */
    public static Boolean dealPic(String realAppPath , String picType , String realInPath , String fileInName , String realOutPath , String fileOutName){
        Boolean result = false;
        log.info("Deal with the customer's picture beginning ...");
        Long processStart = System.currentTimeMillis();
        String[] cmd = {realAppPath , picType , realInPath , fileInName , realOutPath , fileOutName};
        Process process = null;
        StreamGobbler errorGobbler = null;
        StreamGobbler outputGobbler = null;
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            process = pb.start();
            errorGobbler = new StreamGobbler(process.getErrorStream(), "error");
            outputGobbler = new StreamGobbler(process.getInputStream(), "output");
            errorGobbler.start();
            outputGobbler.start();
            try {
                int exitCode = process.waitFor();
                log.info("线程退出码：{}", exitCode);
            } catch (InterruptedException e) {
                log.error("Deal with the customer's picture error , the detail message is : {}" , e.getMessage());
                throw new BusinessException(7006,"章模摘取失败");
            }
        } catch (IOException e) {
            log.error("Deal with the customer's picture error , the detail message is : {}" , e.getMessage());
            throw new BusinessException(7006,"章模摘取失败");
        } finally {
            if (process != null){
                if (null != process.getInputStream() && null != outputGobbler){
                    outputGobbler.interrupt();
                }
                if (null != process.getErrorStream() && null != errorGobbler){
                    errorGobbler.interrupt();
                }
            }
        }
        log.info("Deal with the customer's picture finished ,consume time = 【 " + (System.currentTimeMillis() - processStart) + " 】ms");
        result = true;
        return result;
    }

    public static void main(String[] args) {
        String realAppPath = "C:\\cutPicMode\\GetPicMode.exe";
        String inPath = "C:\\cutPicMode\\1.bmp";
        String outPath = "C:\\cutPicMode\\";
        float phyWidth = 0.0f;
        float phyHeight = 0.0f;
        int type = 0;

        dealPicVersion2(realAppPath , inPath , outPath , phyWidth , phyHeight , type);

    }
    /**

     * 通过cmd命令调用新版C++程序处理章模
     * @Author: 刘志
     * @CreateDate: 2018/11/8 17:35
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/11/8 17:35
     * @Version: 0.0.1
     * @param realAppPath 可执行文件名称（跟完整路径）
     * @param inPath 输入图片的完整路径
     * @param outPath 输出图片的完整路径
     * @param phyWidth 图片物理宽度(传0.0f表示智能处理)
     * @param phyHeight 图片物理高度(传0.0f表示智能处理)
     * @param type (处理类型, 0:处理章模, 1:处理手绘签名)
     * @return java.lang.Boolean
     */
    public static Boolean dealPicVersion2(String realAppPath , String inPath , String outPath , float phyWidth , float phyHeight , int type){

        Boolean result = false;
        log.info("Deal with the customer's picture beginning ...");
        Long processStart = System.currentTimeMillis();
        String[] cmd = {realAppPath , inPath , outPath , String.valueOf(phyWidth) , String.valueOf(phyHeight) , String.valueOf(type)};
        Process process = null;
        StreamGobbler errorGobbler = null;
        StreamGobbler outputGobbler = null;
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            process = pb.start();
            errorGobbler = new StreamGobbler(process.getErrorStream(), "error");
            outputGobbler = new StreamGobbler(process.getInputStream(), "output");
            errorGobbler.start();
            outputGobbler.start();
            try {
                int exitCode = process.waitFor();
                log.info("线程退出码：{}", exitCode);
            } catch (InterruptedException e) {
                log.error("Deal with the customer's picture error , the detail message is : {}" , e.getMessage());
                throw new BusinessException(7006,"章模摘取失败");
            }
        } catch (IOException e) {
            log.error("Deal with the customer's picture error , the detail message is : {}" , e.getMessage());
            throw new BusinessException(7006,"章模摘取失败");
        } finally {
            if (process != null){
                if (null != process.getInputStream() && null != outputGobbler){
                    outputGobbler.interrupt();
                }
                if (null != process.getErrorStream() && null != errorGobbler){
                    errorGobbler.interrupt();
                }
            }
        }
        log.info("Deal with the customer's picture finished ,consume time = 【 " + (System.currentTimeMillis() - processStart) + " 】ms");
        result = true;
        return result;
    }


}
