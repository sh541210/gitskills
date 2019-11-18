package com.iyin.sign.system.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @ClassName:   InspectRunningEnvConfig
 * @Description: 检视应用运行时配置环境
 * @Author:      陆文雄
 * @CreateDate:  2018/12/25 19:33
 * @UpdateUser:  陆文雄
 * @UpdateDate:  2018/12/25 19:33
 * @Version:     0.0.1
 */
@Component
@Slf4j
public class InspectRunningEnvConfig implements CommandLineRunner {

    @Value("${env.msg}")
    private String envMsg;

    @Override
    public void run(String... strings) {
        // 观察启动配置读取情况
        log.info("[ econtract-admin-server ] {}" , envMsg);
    }
}
