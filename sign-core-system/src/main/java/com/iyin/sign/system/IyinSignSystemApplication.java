package com.iyin.sign.system;

import com.iyin.sign.system.common.fastdfs.FastdfsClient;
import com.iyin.sign.system.common.fastdfs.FastdfsClientConfig;
import com.iyin.sign.system.common.fastdfs.FastdfsClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @ClassName: IyinSignSystemApplication
 * @Description: 项目启动入口
 * @Author: luwenxiong
 * @CreateDate: 2019/6/19 16:40
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/19 16:40
 * @Version: 0.0.1
 */
@SpringBootApplication
@Slf4j
@EnableAsync
@EnableCaching
@EnableScheduling
@EnableFeignClients
public class IyinSignSystemApplication {

    private static final int MAX_POOL_SIZE = 10;

    @Bean
    public FastdfsClient fastdfsClient(@Autowired FastdfsClientConfig config) {
        try {
            log.info("FastdfsClientConfig: {}", config);
            return FastdfsClientFactory.getFastdfsClient(config);
        } catch (Exception e) {
            log.error("",e);
        }
        return null;
    }

    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Anno-Executor");
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        return executor;
    }

    public static void main(String[] args){
        SpringApplication.run(IyinSignSystemApplication.class,args);
    }
}
