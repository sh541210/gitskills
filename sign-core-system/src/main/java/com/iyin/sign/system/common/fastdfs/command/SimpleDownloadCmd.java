package com.iyin.sign.system.common.fastdfs.command;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import com.iyin.sign.system.common.fastdfs.data.Result;

public class SimpleDownloadCmd extends AbstractCmd<byte[]>{
	
	public SimpleDownloadCmd(String group,String remoteFileName){
		byte[] bsOffset;
		byte[] bsDownBytes;
		byte[] groupBytes;
		byte[] filenameBytes;
		byte[] bs;
		int groupLen;
		long fileOffset = 0;
		long downloadBytes = 0;
		bsOffset = long2buff(fileOffset);
		bsDownBytes = long2buff(downloadBytes);
		groupBytes = new byte[FDFS_GROUP_NAME_MAX_LEN];
		bs = group.getBytes(charset);
		filenameBytes = remoteFileName.getBytes(charset);
		Arrays.fill(groupBytes, (byte) 0);
		if (bs.length <= groupBytes.length) {
			groupLen = bs.length;
		} else {
			groupLen = groupBytes.length;
		}
		System.arraycopy(bs, 0, groupBytes, 0, groupLen);
		
		this.responseCmd = STORAGE_PROTO_CMD_RESP;
		this.responseSize = -1;
		this.requestCmd = STORAGE_PROTO_CMD_DOWNLOAD_FILE;
		this.body1 = new byte[bsOffset.length + bsDownBytes.length + groupBytes.length + filenameBytes.length];
		//����дfileOffset long��
		System.arraycopy(bsOffset, 0, body1, 0, bsOffset.length);
		//����д downloadBytes long��
		System.arraycopy(bsDownBytes, 0, body1, bsOffset.length, bsDownBytes.length);
		//����дgroup��bytes
		System.arraycopy(groupBytes, 0, body1, bsOffset.length+bsDownBytes.length, groupBytes.length);
		//����дfilename��bytes
		System.arraycopy(filenameBytes, 0, body1, bsOffset.length+bsDownBytes.length+groupBytes.length, filenameBytes.length);
	}
	
	@Override
	public Result<byte[]> exec(Socket socket) throws IOException {
		this.request(socket.getOutputStream());
		Response response = response(socket.getInputStream());
		if(response.isSuccess()){
			byte[] data = response.getData();
			return new Result<>(response.getCode(), data);
		}
		else{
			Result<byte[]> result = new Result<>(response.getCode(), "Error");
			return result;
		}
	}

}
