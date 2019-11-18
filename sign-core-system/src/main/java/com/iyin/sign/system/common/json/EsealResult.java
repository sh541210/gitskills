package com.iyin.sign.system.common.json;


import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.model.code.BaseResponseCode;
import lombok.Data;

/**
 *
 * @title: EsealResutl
 * @description: 安印电子签章平台的统一返回类
 * @date: 16:26 2017/6/30
 * @author tdf
 * @version: v1.0.0
 */
@Data
public class EsealResult<T> {
    /**
     * 请求的标识符，0为成功1为失败
     */
    private int code = 0;
    /**
     * 请求的消息
     */
    private String msg;
    /**
     * 需要返回的数据
     */
    private T data;

    /**
     * 默认无参构造器
     */
    public EsealResult() {
        this.setDefSuc();
    }

    public EsealResult(T data) {
        this.data=data;
        this.code = BaseResponseCode.SUCCESS.getCode();
        this.msg = BaseResponseCode.SUCCESS.getMsg();
    }

    /**
     * 不带数据构造器
     * @param code
     * @param msg
     */
    public EsealResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 带数据的构造器
     * @param code
     * @param msg
     * @param data
     */
    public EsealResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 带ErrorCode的构造器
     * @param errorCode
     */
    public EsealResult(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    /**
     * 设置默认请求成功
     */
    public void setDefSuc() {
        this.code = 0;
        this.msg = "请求成功";
    }

    /**
     * 设置默认请求失败
     * @param msg
     */
    public void setDefFail(String msg) {
        this.code = 1;
        this.msg = msg!=null?msg: "请求失败";
    }

    /**
     * 设置ErroCode的请求失败
     * @param errorCode
     */
    public void setFailCode(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> EsealResult<T> esealResult() {
        return new EsealResult<>();
    }

    @Override
    public String toString() {
        return "EsealResult{" +
                "'code'=" + code +
                ", 'msg'='" + msg + '\'' +
                ", 'data'='" + data +
                "'}";
    }
}
