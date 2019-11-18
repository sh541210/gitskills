package com.iyin.sign.system.common.fastdfs.client;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import com.iyin.sign.system.common.fastdfs.FastdfsClientConfig;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectState;
import org.apache.commons.pool2.impl.DefaultPooledObject;

@Slf4j
public class TrackerClientFactory implements KeyedPooledObjectFactory<String,TrackerClient> {
	
	private Integer connectTimeout = FastdfsClientConfig.DEFAULT_CONNECT_TIMEOUT * 1000;
	private Integer networkTimeout = FastdfsClientConfig.DEFAULT_NETWORK_TIMEOUT * 1000;

	public TrackerClientFactory() {
		super();
	}
	
	public TrackerClientFactory(Integer connectTimeout, Integer networkTimeout) {
		super();
		this.connectTimeout = connectTimeout;
		this.networkTimeout = networkTimeout;
	}

	@Override
	public PooledObject<TrackerClient> makeObject(String key){
		log.info("TrackerClientFactory makeObject :{}",key);
		TrackerClient trackerClient = new TrackerClientImpl(key,connectTimeout,networkTimeout);
		PooledObject<TrackerClient> pooledTrackerClient = new DefaultPooledObject<>(trackerClient);
		return pooledTrackerClient;
	}

	@Override
	public void destroyObject(String key, PooledObject<TrackerClient> pooledTrackerClient) throws IOException{
		log.info("TrackerClientFactory destroyObject state:{}",pooledTrackerClient.getState().name());
		TrackerClient trackerClient = pooledTrackerClient.getObject();
		trackerClient.close();
	}

	@Override
	public boolean validateObject(String key, PooledObject<TrackerClient> p) {
		log.info("TrackerClientFactory validateObject state:{}",p.getState().name());
		if(PooledObjectState.INVALID.name().equals(p.getState().name())||PooledObjectState.ABANDONED.name().equals(p.getState().name())){
			try {
				p.getObject().close();
			} catch (IOException e) {
				log.info("TrackerClientFactory validateObject e:{}",e.getMessage());
				throw new RuntimeException("TrackerClientFactory validateObject fail!");
			}
		}
		return true;
	}

	@Override
	public void activateObject(String key, PooledObject<TrackerClient> p)
			throws Exception {
        log.info("TrackerClientFactory activateObject state:{}",p.getState().name());
	}

	@Override
	public void passivateObject(String key, PooledObject<TrackerClient> p)
			throws Exception {
        log.info("TrackerClientFactory passivateObject state:{}",p.getState().name());
	}

	
	

}
