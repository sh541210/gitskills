package com.iyin.sign.system.common.fastdfs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix="fastdfs")
@Configuration
public class FastdfsClientConfig {
	private int connectTimeoutInSeconds = 5;
	private int networkTimeoutInSeconds = 30;
	private String charset = "UTF-8";
	private List<String> trackerServers = Arrays.asList();
	private GenericKeyedObjectPoolConfig pool = new GenericKeyedObjectPoolConfig();

	// second
	public static final int DEFAULT_CONNECT_TIMEOUT = 5;
	// second
	public static final int DEFAULT_NETWORK_TIMEOUT = 30;

	public static final String FAST_DEV_STORAGEADDR="192.168.51.212:23000";
	public static final String FAST_DEV_STORAGEADDR_SAFE="192.168.51.212:23000";


	public GenericKeyedObjectPoolConfig getTrackerClientPoolConfig(){
		return new GenericKeyedObjectPoolConfig();
	}
	

	public GenericKeyedObjectPoolConfig getStorageClientPoolConfig(){
		return new GenericKeyedObjectPoolConfig();
	}

	public int getConnectTimeout() {
		return connectTimeoutInSeconds * 1000;
	}
	public int getNetworkTimeout() {
		return networkTimeoutInSeconds * 1000;
	}
	public List<String> getTrackerAddrs() {
		log.info("trackerServers: {}", trackerServers);
		return trackerServers;
	}
}
