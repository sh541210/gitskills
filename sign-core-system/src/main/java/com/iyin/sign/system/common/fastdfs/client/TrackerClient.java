package com.iyin.sign.system.common.fastdfs.client;

import java.io.IOException;
import java.util.List;

import com.iyin.sign.system.common.fastdfs.data.GroupInfo;
import com.iyin.sign.system.common.fastdfs.data.Result;
import com.iyin.sign.system.common.fastdfs.data.StorageInfo;
import com.iyin.sign.system.common.fastdfs.data.UploadStorage;

public interface TrackerClient {

	public Result<UploadStorage> getUploadStorage() throws IOException;
	public Result<String> getUpdateStorageAddr(String group,String fileName) throws IOException;
	public Result<String> getDownloadStorageAddr(String group,String fileName) throws IOException;
	public Result<List<GroupInfo>> getGroupInfos() throws IOException;
	public Result<List<StorageInfo>> getStorageInfos(String group) throws IOException;
	public void close() throws IOException;
	
}
