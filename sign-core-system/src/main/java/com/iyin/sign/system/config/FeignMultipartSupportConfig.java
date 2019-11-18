package com.iyin.sign.system.config;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @ClassName: FeignMultipartSupportConfig
 * @Description: 配置类
 * @Author: 唐德繁
 * @CreateDate: 2018/9/18 0018 下午 9:03
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/9/18 0018 下午 9:03
 * @Version: 0.0.1
 */
@Configuration
public class FeignMultipartSupportConfig {
    @Autowired
    ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * 编码方法
     * @Author: 唐德繁
     * @CreateDate: 2018/09/18 下午 9:04
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/09/18 下午 9:04
     * @Version: 0.0.1
     * @return
     */
    @Bean
    @Scope("prototype")
    public Encoder multipartFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
	@Bean
    @Scope("prototype")
    public Decoder feignDownloadDecoder() {
		return new FeignDownloadDecoder();
    }
}
