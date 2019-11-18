package com.iyin.sign.system.model.exception;

/**
 * @ClassName InternalRuntimeException
 * @Author wdf
 * @Date 2019/7/19 14:16
 * @throws
 * @Version 1.0
 **/
public class InternalRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 4347697398264229440L;
	
	public InternalRuntimeException(String msg) {
		super(msg);
	}
}
