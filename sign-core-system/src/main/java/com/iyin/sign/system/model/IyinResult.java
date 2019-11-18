package com.iyin.sign.system.model;

import com.iyin.sign.system.model.code.BaseResponseCode;
import com.iyin.sign.system.model.code.ResponseCodeInterface;
import com.iyin.sign.system.model.exception.BusinessException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @ClassName: EsealResult
 * @Description: rest请求返回结果类
 * @Author: 肖泛舟
 * @CreateDate: 2018/7/28 14:47
 * @UpdateUser: 肖泛舟
 * @UpdateDate: 2018/7/28 14:47
 * @Version: 0.0.1
 */
@Slf4j
@Data
@ApiModel(value = "EsealResult", description = "通用返回对象")
public class IyinResult<T> implements Serializable {
    public static final int DEFAULT_SUCCEED_CODE = 0;

    /**
     * 请求响应code，0为成功 其他为失败
     */
    @ApiModelProperty(value = "请求响应code，0为成功 其他为失败", name = "code")
    private int code = DEFAULT_SUCCEED_CODE;

    /**
     * 响应异常码详细信息
     */
    @ApiModelProperty(value = "响应异常码详细信息", name = "msg")
    private String msg;

    /**
     * 响应内容 ， code 0 时为 返回 数据
     */
    @ApiModelProperty(value = "需要返回的数据", name = "data")
    private T data;

    public IyinResult(T data) {
        this.data = data;
        this.code = BaseResponseCode.SUCCESS.getCode();
        this.msg = BaseResponseCode.SUCCESS.getMsg();
    }


    public IyinResult() {
        this.code = BaseResponseCode.SUCCESS.getCode();
        this.msg = BaseResponseCode.SUCCESS.getMsg();
        this.data = null;
    }

    public IyinResult(int code) {
        this.data = null;
        this.code = code;
    }

    public IyinResult(int code, String msg) {
        this.data = null;
        this.code = code;
        this.msg = msg;
    }

    public IyinResult(int code, T data) {
        this.data = data;
        this.code = code;
    }

    public IyinResult(int code, String msg, T data) {
        this.data = data;
        this.msg = msg;
        this.code = code;
    }

    public IyinResult(ResponseCodeInterface responseCode) {
        this.data = null;
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
    }

    public IyinResult(ResponseCodeInterface responseCode, T data) {
        this.data = data;
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> IyinResult<T> success() {
        return new IyinResult<>();
    }

    public static <T> IyinResult<T> success(T data) {
        return new IyinResult<>(data);
    }

    /**
     * @return
     * @Description: EsealResult 默认构造函数 返回成功code
     * @Author: 肖泛舟
     * @CreateDate: 2018/7/30 18:42
     * @UpdateUser: 肖泛舟
     * @UpdateDate: 2018/7/30 18:42
     * @Version: 0.0.1
     * @Param:
     */
    public static <T> IyinResult<T> getIyinResult() {
        return new IyinResult<>();
    }


    /**
     * @return
     * @Description:
     * @Author: 肖泛舟
     * @CreateDate: 2018/7/30 18:42
     * @UpdateUser: 肖泛舟
     * @UpdateDate: 2018/7/30 18:42
     * @Version: 0.0.1
     * @Param: * @param code 错误码
     */
    public static <T> IyinResult<T> getIyinResult(int code) {
        return new IyinResult<>(code);
    }

    /**
     * @return
     * @Description:
     * @Author: 肖泛舟
     * @CreateDate: 2018/7/30 18:43
     * @UpdateUser: 肖泛舟
     * @UpdateDate: 2018/7/30 18:43
     * @Version: 0.0.1
     * @Param: @param data: 成功返回的数据
     */
    public static <T> IyinResult<T> getIyinResult(T data) {
        return new IyinResult<>(data);
    }

    /**
     * @return
     * @Description:
     * @Author: 肖泛舟
     * @CreateDate: 2018/7/30 18:43
     * @UpdateUser: 肖泛舟
     * @UpdateDate: 2018/7/30 18:43
     * @Version: 0.0.1
     * @Param: * @param code 错误码
     * @Param: * @param msg 错误信息
     * @Param: @param data: 成功返回的数据
     */
    public static <T> IyinResult<T> getIyinResult(int code, String msg, T data) {
        return new IyinResult<>(code, msg, data);
    }

    /**
     * @return
     * @Description:
     * @Author: 肖泛舟
     * @CreateDate: 2018/7/30 18:44
     * @UpdateUser: 肖泛舟
     * @UpdateDate: 2018/7/30 18:44
     * @Version: 0.0.1
     * @Param: @param code 错误码
     * @Param: * @param msg 错误信息
     */
    public static <T> IyinResult<T> getIyinResult(int code, String msg) {
        return new IyinResult<>(code, msg);
    }

    public static <T> IyinResult<T> getIyinResult(String code, String msg) {
        return new IyinResult<>(Integer.parseInt(code), msg);
    }

    public static <T> IyinResult<T> getIyinResult(ResponseCodeInterface responseCode) {
        return new IyinResult<>(responseCode);
    }

    public static <T> IyinResult<T> getIyinResult(ResponseCodeInterface responseCode, T data) {
        IyinResult<T> result = new IyinResult<>(responseCode);
        result.setData(data);
        return result;
    }

    public static <T> T getSucceedResultDate(IyinResult<T> iyinResult) {
        if (iyinResult != null && IyinResult.DEFAULT_SUCCEED_CODE == iyinResult.getCode()) {
            return iyinResult.getData();
        } else {
            if (iyinResult != null) {
                log.error(iyinResult.getMsg());
                throw new BusinessException(iyinResult.getCode(), iyinResult.getMsg());
            } else {
                log.error("IyinResult is null");
                throw new BusinessException(BaseResponseCode.SERVICECALLEXCEPTION);
            }
        }
    }

    public static <T> T getSucceedResultDataNoException(IyinResult<T> iyinResult) {
        if (iyinResult != null && IyinResult.DEFAULT_SUCCEED_CODE == iyinResult.getCode()) {
            return iyinResult.getData();
        } else {
            log.error("调用二方接口失败：IyinResult={}", iyinResult);
            return null;
        }
    }


	public static <T> T getData(IyinResult<T> result) {
		if (result != null && IyinResult.DEFAULT_SUCCEED_CODE == result.getCode()) {
			return result.getData();
		} else if (result != null) {
			log.error(result.getMsg());
            throw new BusinessException(result.getCode(), result.getMsg());
		} else {
			log.error("IyinResult is null");
            throw new BusinessException(BaseResponseCode.SERVICECALLEXCEPTION);
		}
	}

}
