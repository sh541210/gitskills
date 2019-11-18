package com.iyin.sign.system.common.fastdfs;

import java.io.File;
import java.util.Map;

/**
	* FastdfsClient
	* @Author: wdf 
    * @CreateDate: 2019/9/18 15:01
	* @UpdateUser: wdf
    * @UpdateDate: 2019/9/18 15:01
	* @Version: 0.0.1
    * @param 
    * @return 
    */
public interface FastdfsClient {

	public String upload(File file) throws Exception;
	public String upload(File file, String fileName) throws Exception;
	public String getUrl(String fileId) throws Exception;
	public Boolean setMeta(String fileId, Map<String, String> meta) throws Exception;
	public Map<String,String> getMeta(String fileId) throws Exception;
	public Boolean delete(String fileId) throws Exception;
	public void close();

	public String upload(byte[] bytes, String fileName) throws Exception;
	public String upload(byte[] bytes, String fileName, Map<String, String> meta) throws Exception;
	public String upload(byte[] bytes, String fileName, String group) throws Exception;
	public byte[] download(String fileId, String group) throws Exception;
	public byte[] download(String fileName) throws Exception;
	public Map<String,String> getMetadata(String fileId) throws Exception;
	public Boolean setMetadata(String fileId, Map<String,String> meta) throws Exception;

}
