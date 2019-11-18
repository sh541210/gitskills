package com.iyin.sign.system.common.fastdfs;

import com.iyin.sign.system.common.fastdfs.client.StorageClient;
import com.iyin.sign.system.common.fastdfs.client.StorageClientFactory;
import com.iyin.sign.system.common.fastdfs.client.TrackerClient;
import com.iyin.sign.system.common.fastdfs.client.TrackerClientFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.util.List;

public class FastdfsClientFactory {
	
	private static volatile FastdfsClient fastdfsClient;

	public static FastdfsClient getFastdfsClient(FastdfsClientConfig config){
		if (fastdfsClient == null) {
			synchronized (FastdfsClient.class) {
				if (fastdfsClient == null) {
					int connectTimeout = config.getConnectTimeout();
					int networkTimeout = config.getNetworkTimeout();
					TrackerClientFactory trackerClientFactory = new TrackerClientFactory(connectTimeout, networkTimeout);
					StorageClientFactory storageClientFactory = new StorageClientFactory(connectTimeout, networkTimeout);
					GenericKeyedObjectPoolConfig trackerClientPoolConfig = config.getTrackerClientPoolConfig();
					GenericKeyedObjectPoolConfig storageClientPoolConfig = config.getStorageClientPoolConfig();
					GenericKeyedObjectPool<String,TrackerClient> trackerClientPool = new GenericKeyedObjectPool<>(trackerClientFactory, trackerClientPoolConfig);
					GenericKeyedObjectPool<String,StorageClient> storageClientPool = new GenericKeyedObjectPool<>(storageClientFactory, storageClientPoolConfig);
					List<String> trackerAddrs = config.getTrackerAddrs();
					fastdfsClient = new FastdfsClientImpl(trackerAddrs, trackerClientPool, storageClientPool);
				}
			}
		}
		return fastdfsClient;
	}
}
