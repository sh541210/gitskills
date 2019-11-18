package com.iyin.sign.system.common.fastdfs.client;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

import com.iyin.sign.system.common.fastdfs.FastdfsClientConfig;
import com.iyin.sign.system.common.fastdfs.command.CloseCmd;
import com.iyin.sign.system.common.fastdfs.command.Command;
import com.iyin.sign.system.common.fastdfs.command.DeleteCmd;
import com.iyin.sign.system.common.fastdfs.command.GetMetaDataCmd;
import com.iyin.sign.system.common.fastdfs.command.SetMetaDataCmd;
import com.iyin.sign.system.common.fastdfs.command.SimpleDownloadCmd;
import com.iyin.sign.system.common.fastdfs.command.SimpleUploadCmd;
import com.iyin.sign.system.common.fastdfs.command.UploadCmd;
import com.iyin.sign.system.common.fastdfs.data.Result;

public class StorageClientImpl implements StorageClient{
	
	private Socket socket;
	private String host;
	private Integer port;
	private Integer connectTimeout = FastdfsClientConfig.DEFAULT_CONNECT_TIMEOUT * 1000;
	private Integer networkTimeout = FastdfsClientConfig.DEFAULT_NETWORK_TIMEOUT * 1000;
	
	private Socket getSocket() throws IOException{
		if(socket==null){
			socket = new Socket();
			socket.setSoTimeout(networkTimeout);
			socket.connect(new InetSocketAddress(host, port),connectTimeout);
		}
		return socket;
	}
	
	public StorageClientImpl(String address){
		super();
		String[] hostport = address.split(":");
		this.host = hostport[0];
		this.port = Integer.valueOf(hostport[1]);
	}
	
	public StorageClientImpl(String address,Integer connectTimeout, Integer networkTimeout){
		this(address);
		this.connectTimeout = connectTimeout;
		this.networkTimeout = networkTimeout;
	}

	@Override
	public void close() throws IOException{
		Socket socket = getSocket();
		Command<Boolean> command = new CloseCmd();
		command.exec(socket);
		socket.close();
		socket = null;
	}

	@Override
	public Result<String> upload(File file,String fileName,byte storePathIndex) throws IOException{
		Socket socket = getSocket();
		UploadCmd uploadCmd = new UploadCmd(file, fileName,storePathIndex);
		return uploadCmd.exec(socket);
	}

	@Override
	public Result<Boolean> delete(String group,String fileName) throws IOException{
		Socket socket = getSocket();
		DeleteCmd deleteCmd = new DeleteCmd(group,fileName);
		return deleteCmd.exec(socket);
	}

	@Override
	public Result<Boolean> setMeta(String group, String fileName,
			Map<String, String> meta) throws IOException {
		Socket socket = getSocket();
		SetMetaDataCmd setMetaDataCmd = new SetMetaDataCmd(group, fileName, meta);
		return setMetaDataCmd.exec(socket);
	}

	@Override
	public Result<Map<String, String>> getMeta(String group, String fileName)
			throws IOException {
		Socket socket = getSocket();
		GetMetaDataCmd getMetaDataCmd = new GetMetaDataCmd(group, fileName);
		return getMetaDataCmd.exec(socket);
	}

	@Override
	public Result<String> upload(byte[] bytes, String fileName, byte storePathIndex) throws IOException {
		if(bytes==null||bytes.length<1){
			throw new IOException("upload bytes of "+fileName+" bytes null");
		}
		Socket socket = getSocket();
		SimpleUploadCmd uploadCmd = new SimpleUploadCmd(bytes, fileName, storePathIndex);
		return uploadCmd.exec(socket);
	}

	@Override
	public Result<byte[]> download(String group, String remoteFileName)  throws IOException{
		Socket socket = getSocket();
		SimpleDownloadCmd downloadCmd = new SimpleDownloadCmd(group, remoteFileName);
		return downloadCmd.exec(socket);
	}
}
