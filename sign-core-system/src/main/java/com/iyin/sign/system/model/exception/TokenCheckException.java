package com.iyin.sign.system.model.exception;

import com.iyin.sign.system.model.code.ResponseCodeInterface;

/**
 * @Description: token检查异常
 * @Author: 肖泛舟
 * @CreateDate: 2018/9/13 12:01
 * @UpdateUser: 肖泛舟
 * @UpdateDate: 2018/9/13 12:01
 * @Version: 0.0.1
 */
public class TokenCheckException extends BaseException {

	private static final long serialVersionUID = 4617282801552709280L;

	public TokenCheckException(int messageCode) {
		super(messageCode);
	}

	public TokenCheckException(int messageCode, String message) {
		super(messageCode, message);
	}

	/**
	 * 构造函数
	 * @param code 异常码
	 */
	public TokenCheckException(ResponseCodeInterface code) {
		super(code.getCode(), code.getMsg());
	}

	@Override
	public String toString() {
		return "TokenCheckException [getMessageCode()=" + getMessageCode() + "]";
	}
 
	
	
}
