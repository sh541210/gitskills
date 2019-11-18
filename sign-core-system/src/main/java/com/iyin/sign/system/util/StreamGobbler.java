package com.iyin.sign.system.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @ClassName: StreamGobbler
 * @Description: 被调用进程的输出截获线程类
 * @Author: 刘志
 * @CreateDate: 2018/9/10 16:39
 * @UpdateUser: 刘志
 * @UpdateDate: 2018/9/10 16:39
 * @Version: 0.0.1
 */
@Slf4j
public class StreamGobbler extends Thread {

    InputStream is;
    String type;

    public StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        try {
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if ("error".equals(type)) {
                    log.info("Error:{}", line);
                } else {
                    log.info("Debug:{}", line);
                }
            }
        } catch (IOException ioe) {
            log.error("被调用进程的输出截获失败:{}", ioe);
        }finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("截获进程输出用流关闭失败：{}", e);
                }
            }
        }
    }
}
