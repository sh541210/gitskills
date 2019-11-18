package com.iyin.sign.system.common.exception;

/**
 * @author:liushuqiao
 * @title: ServiceException
 * @description:自定义的Service异常
 * @date: 9:12 2017/9/20
 * @version: v1.0.0
 */
public class ServiceException extends Exception {
    /**
     * serial_number
     */
    private static final long serialVersionUID = 1L;

    /**
     * 错误枚举类
     */
    private  ErrorCode errorCode;

    /**
     * 错误信息
     */
    private String errorMessage = null;

    /**
     * 错误类型，缺省为0,表示正常
     */
    private int errorType = 0;

    /**
     * ServiceException构造函数
     */
    public ServiceException() {
        super();
        errorMessage = "Service layer is Error!!";
    }

    /**
     * ServiceException构造函数根据传递的异常信息
     *
     * @param argMessage
     */
    public ServiceException(String argMessage) {
        super(argMessage);
        errorMessage = argMessage;
    }

    /**
     * TFServiceException构造函数根据传递的异常信息
     *
     * @param argMessage
     */
    public ServiceException(int errorType, String argMessage) {
        super(argMessage);
        this.errorType = errorType;
        this.errorMessage = argMessage;
    }

    public ServiceException(String errorType, String argMessage) {
        super(argMessage);
        this.errorType = Integer.parseInt(errorType);
        this.errorMessage = argMessage;
    }

    /**
     * 根据错误码构造ServiceException
     *
     * @param errorCode
     */
    public ServiceException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getMsg();
        this.errorType = errorCode.getCode();
    }

    /**
     * ServiceException构造函数根据传递的异常信息
     *
     * @param argMessage
     * @param argThr
     */
    public ServiceException(String argMessage, Throwable argThr) {
        super(argMessage, argThr);
    }

    /**
     * ServiceException构造函数根据传递的异常信息
     *
     * @param argMessage
     * @param argThr
     */
    public ServiceException(int errorType, String argMessage, Throwable argThr) {
        super(argMessage, argThr);
        this.errorType = errorType;
    }

    /**
     * ServiceException构造函数通过传递异常对象
     *
     * @param argThr
     */
    public ServiceException(Throwable argThr) {
        super(argThr);
        this.errorMessage = argThr.getLocalizedMessage();
        if (argThr instanceof ServiceException) {
            this.errorCode = ((ServiceException) argThr).getErrorCode();
            this.errorMessage = ((ServiceException) argThr).getErrorMessage();
            this.errorType = ((ServiceException) argThr).getErrorType();
        }
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorType=" + errorType +
                '}';
    }
}