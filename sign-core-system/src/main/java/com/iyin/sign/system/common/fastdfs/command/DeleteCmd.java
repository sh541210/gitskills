package com.iyin.sign.system.common.fastdfs.command;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import com.iyin.sign.system.common.fastdfs.data.Result;

public class DeleteCmd extends AbstractCmd<Boolean> {

	@Override
	public Result<Boolean> exec(Socket socket) throws IOException {
		request(socket.getOutputStream());
		Response response = response(socket.getInputStream());
		if(response.isSuccess()){
			return new Result<>(response.getCode(),true);
		}else{
			return new Result<>(response.getCode(),"Delete Error");
		}
	}

	public DeleteCmd(String group,String fileName) {
		super();
		byte[] groupByte = group.getBytes(charset);
		int groupLen = groupByte.length;
		if (groupLen > FDFS_GROUP_NAME_MAX_LEN) {
			groupLen = FDFS_GROUP_NAME_MAX_LEN;
		}
		byte[] fileNameByte = fileName.getBytes(charset);
		body1 = new byte[FDFS_GROUP_NAME_MAX_LEN + fileNameByte.length];
		Arrays.fill(body1, (byte) 0);
		System.arraycopy(groupByte, 0, body1, 0, groupLen);
		System.arraycopy(fileNameByte, 0, body1, FDFS_GROUP_NAME_MAX_LEN, fileNameByte.length);
		this.requestCmd = STORAGE_PROTO_CMD_DELETE_FILE;
		this.responseCmd = STORAGE_PROTO_CMD_RESP;
		this.responseSize = 0;
	}

}
