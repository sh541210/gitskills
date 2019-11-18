package com.iyin.sign.system.common.fastdfs;

import com.iyin.sign.system.common.fastdfs.client.StorageClient;
import com.iyin.sign.system.common.fastdfs.client.StorageClientFactory;
import com.iyin.sign.system.common.fastdfs.client.TrackerClient;
import com.iyin.sign.system.common.fastdfs.client.TrackerClientFactory;
import com.iyin.sign.system.common.fastdfs.data.GroupInfo;
import com.iyin.sign.system.common.fastdfs.data.Result;
import com.iyin.sign.system.common.fastdfs.data.StorageInfo;
import com.iyin.sign.system.common.fastdfs.data.UploadStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 */
@Slf4j
public class FastdfsClientImpl implements FastdfsClient{

	private static Logger logger = LoggerFactory.getLogger(FastdfsClientImpl.class);
	private GenericKeyedObjectPool<String, TrackerClient> trackerClientPool;
	private GenericKeyedObjectPool<String, StorageClient> storageClientPool;
	private List<String> trackerAddrs;
	private Map<String,String> storageIpMap = new ConcurrentHashMap<>();
	private static Random rand;

	private String getAddr(String addr){
		log.info("FastdfsClientImpl getAddr addr:{}",addr);
		if(FastdfsClientConfig.FAST_DEV_STORAGEADDR.equals(addr)){
			return FastdfsClientConfig.FAST_DEV_STORAGEADDR_SAFE;
		}
		return addr;
	}

	static {
		rand=new Random();
	}

	public FastdfsClientImpl(List<String> trackerAddrs) throws Exception {
		super();
		this.trackerAddrs = trackerAddrs;
		trackerClientPool = new GenericKeyedObjectPool<>(new TrackerClientFactory());
		storageClientPool = new GenericKeyedObjectPool<>(new StorageClientFactory());
		updateStorageIpMap();
	}

	public FastdfsClientImpl(List<String> trackerAddrs,
							 GenericKeyedObjectPool<String, TrackerClient> trackerClientPool,
							 GenericKeyedObjectPool<String, StorageClient> storageClientPool){
		super();
		this.trackerAddrs = trackerAddrs;
		this.trackerClientPool = trackerClientPool;
		this.storageClientPool = storageClientPool;
	}

	@Override
	public void close() {
		this.trackerClientPool.close();
		this.storageClientPool.close();
	}

	private void updateStorageIpMap() throws Exception{
		String trackerAddr = getTrackerAddr();
		TrackerClient trackerClient = null;
		try {
			trackerClient = trackerClientPool.borrowObject(trackerAddr);
			Result<List<GroupInfo>> result = trackerClient.getGroupInfos();
			if(result.getCode()==0){
				List<GroupInfo> groupInfos = result.getData();
				doDealData(trackerClient, groupInfos);
			}else{
				throw new Exception("Get getGroupInfos Error");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			if(trackerClient!=null){
				trackerClientPool.returnObject(trackerAddr, trackerClient);
			}
		}
	}

	private void doDealData(TrackerClient trackerClient, List<GroupInfo> groupInfos) throws IOException {
		for(GroupInfo groupInfo:groupInfos){
			Result<List<StorageInfo>> result2= trackerClient.getStorageInfos(groupInfo.getGroupName());
			if(result2.getCode()==0){
				List<StorageInfo> storageInfos = result2.getData();
				for(StorageInfo storageInfo:storageInfos){
					String hostPort = storageInfo.getDomainName();
					if(storageInfo.getStorageHttpPort()!=80){
						hostPort = hostPort + ":" + storageInfo.getStorageHttpPort();
					}
					storageIpMap.put(storageInfo.getIpAddr()+":"+storageInfo.getStoragePort(), hostPort);
				}
			}
		}
	}

	private String getDownloadHostPort(String storageAddr) throws Exception{
		String downloadHostPort = storageIpMap.get(storageAddr);
		if(downloadHostPort==null){
			updateStorageIpMap();
			downloadHostPort = storageIpMap.get(storageAddr);
		}
		return downloadHostPort;
	}

	@Override
	public Boolean setMeta(String fileId, Map<String, String> meta)
			throws Exception {
		String trackerAddr = getTrackerAddr();
		TrackerClient trackerClient = null;
		StorageClient storageClient = null;
		Boolean result = null;
		String storageAddr=null;
		try{
			FastDfsFile fastDfsFile = new FastDfsFile(fileId);
			trackerClient = trackerClientPool.borrowObject(trackerAddr);
			Result<String> result2 = trackerClient.getUpdateStorageAddr(fastDfsFile.group, fastDfsFile.fileName);
			if(result2.getCode()==0){
				storageAddr = getAddr(result2.getData());
				storageClient = storageClientPool.borrowObject(storageAddr);
				Result<Boolean> result3 = storageClient.setMeta(fastDfsFile.group, fastDfsFile.fileName,meta);
				if(result3.getCode()==0 ){
					result = result3.getData();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}finally{
			if(storageClient!=null){
				storageClientPool.returnObject(storageAddr, storageClient);
			}
			if(trackerClient!=null){
				trackerClientPool.returnObject(trackerAddr, trackerClient);
			}
		}
		return result;
	}

	@Override
	public Map<String, String> getMeta(String fileId) throws Exception {
		String trackerAddr = getTrackerAddr();
		TrackerClient trackerClient = null;
		StorageClient storageClient = null;
		Map<String, String> meta = null;
		String storageAddr=null;
		try{
			FastDfsFile fastDfsFile = new FastDfsFile(fileId);
			trackerClient = trackerClientPool.borrowObject(trackerAddr);
			Result<String> result2 = trackerClient.getUpdateStorageAddr(fastDfsFile.group, fastDfsFile.fileName);
			if(result2.getCode()==0){
				storageAddr = getAddr(result2.getData());
				storageClient = storageClientPool.borrowObject(storageAddr);
				Result<Map<String,String>> result3 = storageClient.getMeta(fastDfsFile.group, fastDfsFile.fileName);
				if(result3.getCode()==0 ){
					meta = result3.getData();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}finally{
			if(storageClient!=null){
				storageClientPool.returnObject(storageAddr, storageClient);
			}
			if(trackerClient!=null){
				trackerClientPool.returnObject(trackerAddr, trackerClient);
			}
		}
		return meta;
	}

	@Override
	public String getUrl(String fileId) throws Exception{
		String trackerAddr = getTrackerAddr();
		TrackerClient trackerClient = null;
		String url = null;
		try {
			FastDfsFile fastDfsFile = new FastDfsFile(fileId);
			trackerClient = trackerClientPool.borrowObject(trackerAddr);
			Result<String> result = trackerClient.getDownloadStorageAddr(fastDfsFile.group,fastDfsFile.fileName);
			if(result.getCode()==0){
				String hostPort = getDownloadHostPort(result.getData());
				url = "http://"+hostPort+"/"+fastDfsFile.fileName;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			if(trackerClient!=null){
				trackerClientPool.returnObject(trackerAddr, trackerClient);
			}
		}
		return url;
	}

	@Override
	public String upload(File file) throws Exception{
		String fileName = file.getName();
		return upload(file, fileName);
	}

	@Override
	public String upload(File file,String fileName) throws Exception{
		String trackerAddr = getTrackerAddr();
		TrackerClient trackerClient = null;
		StorageClient storageClient = null;
		String storageAddr = null;
		String fileId = null;
		try {
			trackerClient = trackerClientPool.borrowObject(trackerAddr);
			Result<UploadStorage> result = trackerClient.getUploadStorage();
			if(result.getCode()==0){
				storageAddr = getAddr(result.getData().getAddress());
				storageClient = storageClientPool.borrowObject(storageAddr);
				Result<String> result2 = storageClient.upload(file, fileName, result.getData().getPathIndex());
				if(result2.getCode()==0){
					fileId = result2.getData();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			if(storageClient!=null){
				storageClientPool.returnObject(storageAddr, storageClient);
			}
			if(trackerClient!=null){
				trackerClientPool.returnObject(trackerAddr, trackerClient);
			}
		}
		return fileId;
	}

	@Override
	public Boolean delete(String fileId) throws Exception{
		String trackerAddr = getTrackerAddr();
		TrackerClient trackerClient = null;
		StorageClient storageClient = null;
		Boolean result=false;
		String storageAddr=null;
		try{
			FastDfsFile fastDfsFile = new FastDfsFile(fileId);
			trackerClient = trackerClientPool.borrowObject(trackerAddr);
			Result<String> result2 = trackerClient.getUpdateStorageAddr(fastDfsFile.group, fastDfsFile.fileName);
			if(result2.getCode()==0){
				storageAddr = getAddr(result2.getData());
				storageClient = storageClientPool.borrowObject(storageAddr);
				Result<Boolean> result3 = storageClient.delete(fastDfsFile.group, fastDfsFile.fileName);
				if(result3.getCode()==0){
					result = true;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}finally{
			if(storageClient!=null){
				storageClientPool.returnObject(storageAddr, storageClient);
			}
			if(trackerClient!=null){
				trackerClientPool.returnObject(trackerAddr, trackerClient);
			}
		}
		return result;
	}

	public String getTrackerAddr(){
		log.info("getTrackerAddr start FastdfsClientImpl 278 rand：{}",rand.nextInt(trackerAddrs.size()));
		int i = rand.nextInt(trackerAddrs.size());
		log.info("getTrackerAddr end FastdfsClientImpl 280 i：{}",i);
		return trackerAddrs.get(i);
	}

	private class FastDfsFile{
		private String group;
		private String fileName;

		public FastDfsFile(String fileId) {
			int pos = fileId.indexOf('/');
			this.group = fileId.substring(0, pos);
			this.fileName = fileId.substring(pos+1);
		}
		public FastDfsFile(String group, String fileName) {
			this.group = group;
			this.fileName = fileName;
		}

	}

	@Override
	public Map<String, String> getMetadata(String fileId) throws Exception {
		return getMeta(fileId);
	}

	@Override
	public Boolean setMetadata(String fileId, Map<String, String> meta) throws Exception {
		return setMeta(fileId, meta);
	}

	@Override
	public String upload(byte[] bytes, String fileName) throws Exception {
		log.info("upload start FastdfsClientImpl 316");
		String trackerAddr = getTrackerAddr();
		log.info("upload FastdfsClientImpl trackerAddr:{}",trackerAddr);
		TrackerClient trackerClient = null;
		StorageClient storageClient = null;
		String storageAddr = null;
		String fileId = null;
		try {
			log.info("upload start FastdfsClientImpl 324");
			trackerClient = trackerClientPool.borrowObject(trackerAddr);
			log.info("trackerClient:{}",trackerClient);
			Result<UploadStorage> result = trackerClient.getUploadStorage();
			log.info("result:{}",result);
			if(result.getCode()==0){
				storageAddr = getAddr(result.getData().getAddress());
				storageClient = storageClientPool.borrowObject(storageAddr);
				Result<String> result2 = storageClient.upload(bytes, fileName, result.getData().getPathIndex());
				log.info("result2:{}",result2);
				if(result2.getCode()==0){
					fileId = result2.getData();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			if(storageClient!=null){
				storageClientPool.returnObject(storageAddr, storageClient);
			}
			if(trackerClient!=null){
				trackerClientPool.returnObject(trackerAddr, trackerClient);
			}
		}
		log.info("upload end FastdfsClientImpl 349");
		return fileId;
	}

	@Override
	public String upload(byte[] bytes, String fileName, String group) throws Exception {
		String trackerAddr = getTrackerAddr();
		TrackerClient trackerClient = null;
		StorageClient storageClient = null;
		String result = null;
		String storageAddr = null;
		try{
			trackerClient = trackerClientPool.borrowObject(trackerAddr);
			Result<UploadStorage> result2 = trackerClient.getUploadStorage();
			if(result2.getCode()==0){
				storageAddr = getAddr(result2.getData().getAddress());
				storageClient = storageClientPool.borrowObject(storageAddr);
				Result<String> result3 = storageClient.upload(bytes, fileName, result2.getData().getPathIndex());
				if(result3.getCode()==0){
					result = result3.getData();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}finally{
			if(storageClient!=null){
				storageClientPool.returnObject(storageAddr, storageClient);
			}
			if(trackerClient!=null){
				trackerClientPool.returnObject(trackerAddr, trackerClient);
			}
		}
		return result;
	}

	@Override
	public byte[] download(String fileId) throws Exception {
		String trackerAddr = getTrackerAddr();
		TrackerClient trackerClient = null;
		StorageClient storageClient = null;
		byte[] result = null;
		String storageAddr = null;
		try{
			FastDfsFile fastDfsFile = new FastDfsFile(fileId);
			trackerClient = trackerClientPool.borrowObject(trackerAddr);
			Result<String> result2 = trackerClient.getDownloadStorageAddr(fastDfsFile.group, fastDfsFile.fileName);
			if(result2.getCode()==0){
				storageAddr = getAddr(result2.getData());
				storageClient = storageClientPool.borrowObject(storageAddr);
				Result<byte[]> result3 = storageClient.download(fastDfsFile.group, fastDfsFile.fileName);
				if(result3.getCode()==0){
					result = result3.getData();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}finally{
			if(storageClient!=null){
				storageClientPool.returnObject(storageAddr, storageClient);
			}
			if(trackerClient!=null){
				trackerClientPool.returnObject(trackerAddr, trackerClient);
			}
		}
		return result;
	}

	@Override
	public byte[] download(String fileId, String group) throws Exception {
		String trackerAddr = getTrackerAddr();
		TrackerClient trackerClient = null;
		StorageClient storageClient = null;
		byte[] result = null;
		String storageAddr = null;
		try{
			FastDfsFile fastDfsFile = new FastDfsFile(group, fileId);
			trackerClient = trackerClientPool.borrowObject(trackerAddr);
			Result<String> result2 = trackerClient.getDownloadStorageAddr(fastDfsFile.group, fastDfsFile.fileName);
			if(result2.getCode()==0){
				storageAddr = getAddr(result2.getData());
				storageClient = storageClientPool.borrowObject(storageAddr);
				Result<byte[]> result3 = storageClient.download(fastDfsFile.group, fastDfsFile.fileName);
				if(result3.getCode()==0){
					result = result3.getData();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}finally{
			if(storageClient!=null){
				storageClientPool.returnObject(storageAddr, storageClient);
			}
			if(trackerClient!=null){
				trackerClientPool.returnObject(trackerAddr, trackerClient);
			}
		}
		return result;
	}

	@Override
	public String upload(byte[] bytes, String fileName, Map<String, String> meta) throws Exception {
		log.info("FastdfsClientImpl upload start");
		String fileId = upload(bytes, fileName);
		setMeta(fileId, meta);
		log.info("FastdfsClientImpl upload  setMeta end");
		return fileId;
	}

}