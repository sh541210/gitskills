package com.iyin.sign.system.common.fastdfs.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import com.iyin.sign.system.common.fastdfs.FastdfsClientConfig;
import com.iyin.sign.system.common.fastdfs.command.CloseCmd;
import com.iyin.sign.system.common.fastdfs.command.Command;
import com.iyin.sign.system.common.fastdfs.command.GroupInfoCmd;
import com.iyin.sign.system.common.fastdfs.command.QueryDownloadCmd;
import com.iyin.sign.system.common.fastdfs.command.QueryUpdateCmd;
import com.iyin.sign.system.common.fastdfs.command.QueryUploadCmd;
import com.iyin.sign.system.common.fastdfs.command.StorageInfoCmd;
import com.iyin.sign.system.common.fastdfs.data.GroupInfo;
import com.iyin.sign.system.common.fastdfs.data.Result;
import com.iyin.sign.system.common.fastdfs.data.StorageInfo;
import com.iyin.sign.system.common.fastdfs.data.UploadStorage;

public class TrackerClientImpl implements TrackerClient{
	
	private Socket socket;
	private String host;
	private Integer port;
	private Integer connectTimeout = FastdfsClientConfig.DEFAULT_CONNECT_TIMEOUT * 1000;
	private Integer networkTimeout = FastdfsClientConfig.DEFAULT_NETWORK_TIMEOUT * 1000;
	
	public TrackerClientImpl(String address){
		super();
		String[] hostport = address.split(":");
		this.host = hostport[0];
		this.port = Integer.valueOf(hostport[1]);
	}
	
	public TrackerClientImpl(String address,Integer connectTimeout, Integer networkTimeout){
		this(address);
		this.connectTimeout = connectTimeout;
		this.networkTimeout = networkTimeout;
	}
	
	private Socket getSocket() throws IOException{
		if(socket==null){
			socket = new Socket();
			socket.setSoTimeout(networkTimeout);
			socket.connect(new InetSocketAddress(host, port),connectTimeout);
		}
		return socket;
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
	public Result<UploadStorage> getUploadStorage() throws IOException{
		Socket socket = getSocket();
		Command<UploadStorage> command = new QueryUploadCmd();
		return command.exec(socket);
	}

	@Override
	public Result<String> getUpdateStorageAddr(String group,String fileName) throws IOException{
		Socket socket = getSocket();
		Command<String> cmd = new QueryUpdateCmd(group,fileName);
		return cmd.exec(socket);
	}

	@Override
	public Result<String> getDownloadStorageAddr(String group,String fileName) throws IOException{
		Socket socket = getSocket();
		Command<String> cmd = new QueryDownloadCmd(group,fileName);
		return cmd.exec(socket);
	}

	@Override
	public Result<List<GroupInfo>> getGroupInfos() throws IOException{
		Socket socket = getSocket();
		Command<List<GroupInfo>> cmd = new GroupInfoCmd();
		return cmd.exec(socket);
	}

	@Override
	public Result<List<StorageInfo>> getStorageInfos(String group) throws IOException{
		Socket socket = getSocket();
		Command<List<StorageInfo>> cmd = new StorageInfoCmd(group);
		return cmd.exec(socket);
	}
	
}
