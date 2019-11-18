package com.iyin.sign.system.config;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @ClassName: FastDfsConfiguration
 * @Description: 文件服务器配置类
 * @Author: 唐德繁
 * @CreateDate: 2018/9/12 0012 下午 12:16
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/9/12 0012 下午 12:16
 * @Version: 0.0.1
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@PropertySource(ignoreResourceNotFound=true, value = {
		"file:config/fastdfs.properties",
		"file:fastdfs.properties",
		"classpath:config/fastdfs.properties",
		"classpath:fastdfs.properties" })
@Configuration
@ComponentScan("com.iyin.sign.system")
public class FastDfsConfiguration {
	@Value("${fastdfs.connect_timeout_in_seconds}")
	private int connectTimeout;

	@Value("${fastdfs.network_timeout_in_seconds}")
	private int networkTimeout;

	@Value("${fastdfs.charset}")
	private String charsetf;

	@Value("${fastdfs.http_secret_key}")
	private String key;

	@Value("${fastdfs.http_tracker_http_port}")
	private int trackerHttpPort;

	@Value("${fastdfs.tracker_servers}")
	private String trackerServer;

	@Data
	@NoArgsConstructor
	public class FastDfsProperties {
		private int connectTimeoutInSeconds = connectTimeout;
		private int networkTimeoutInSeconds = networkTimeout;
		private String charset = charsetf;
		private boolean httpAntiStealToken = false;
		private String httpSecretKey = key;
		private int httpTrackerHttpPort = trackerHttpPort;
		private String trackerServers = trackerServer;
	}

	@Bean
	public FastDfsProperties fastDfsProperties() {
		FastDfsProperties fastDfsProperties=new FastDfsProperties();
		log.info("FastDfsProperties :{}",fastDfsProperties);
		return fastDfsProperties;
	}
}
