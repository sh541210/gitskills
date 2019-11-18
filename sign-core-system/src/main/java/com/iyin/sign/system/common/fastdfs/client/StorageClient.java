package com.iyin.sign.system.common.fastdfs.client;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.iyin.sign.system.common.fastdfs.data.Result;

public interface StorageClient {
	
	public Result<byte[]> download(String group, String fileId) throws IOException;
	
	public Result<String> upload(File file,String fileName,byte storePathIndex) throws IOException;
	
	public Result<Boolean> delete(String group,String fileName) throws IOException;
	public Result<Boolean> setMeta(String group,String fileName,Map<String,String> meta) throws IOException;
	public Result<Map<String,String>> getMeta(String group,String fileName) throws IOException;
	public void close() throws IOException;

	Result<String> upload(byte[] bytes, String fileName, byte storePathIndex) throws IOException;
	
}
