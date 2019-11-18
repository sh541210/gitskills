package com.iyin.sign.system.common.fastdfs.client;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import com.iyin.sign.system.common.fastdfs.FastdfsClientConfig;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectState;
import org.apache.commons.pool2.impl.DefaultPooledObject;

@Slf4j
public class StorageClientFactory implements KeyedPooledObjectFactory<String, StorageClient> {
	
	private Integer connectTimeout = FastdfsClientConfig.DEFAULT_CONNECT_TIMEOUT * 1000;
	private Integer networkTimeout = FastdfsClientConfig.DEFAULT_NETWORK_TIMEOUT * 1000;

	public StorageClientFactory() {
		super();
	}

	public StorageClientFactory(Integer connectTimeout, Integer networkTimeout) {
		super();
		this.connectTimeout = connectTimeout;
		this.networkTimeout = networkTimeout;
	}

	@Override
	public PooledObject<StorageClient> makeObject(String key) {
		StorageClientImpl storageClient = new StorageClientImpl(key,connectTimeout,networkTimeout);
		log.info("StorageClientFactory makeObject :{}",key);
		return new DefaultPooledObject<>(storageClient);
	}

	@Override
	public void destroyObject(String key, PooledObject<StorageClient> pooledStorageClient) throws IOException {
		log.info("StorageClientFactory destroyObject state:{}",pooledStorageClient.getState().name());
		StorageClient storageClient = pooledStorageClient.getObject();
		storageClient.close();
	}

	@Override
	public boolean validateObject(String key, PooledObject<StorageClient> p) {
		log.info("StorageClientFactory validateObject state:{}",p.getState().name());
		if(PooledObjectState.INVALID.name().equals(p.getState().name())||PooledObjectState.ABANDONED.name().equals(p.getState().name())){
			try {
				p.getObject().close();
			} catch (IOException e) {
				log.info("StorageClientFactory validateObject e:{}",e.getMessage());
				throw new RuntimeException("StorageClientFactory validateObject fail!");
			}
		}
		return true;
	}

	@Override
	public void activateObject(String key, PooledObject<StorageClient> p)
			throws Exception {
		log.info("StorageClientFactory activateObject state:{}",p.getState().name());
	}

	@Override
	public void passivateObject(String key, PooledObject<StorageClient> p)
			throws Exception {
		log.info("StorageClientFactory passivateObject state:{}",p.getState().name());
		
	}



}
