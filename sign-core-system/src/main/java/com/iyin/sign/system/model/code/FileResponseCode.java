package com.iyin.sign.system.model.code;


/**
 * @ClassName:   FileResponseCode
 * @Description: 文件微服务错误码 code值范围：3000-3999
 * @Author:      肖泛舟
 * @CreateDate:  2018/8/1 11:17
 * @UpdateUser:  肖泛舟
 * @UpdateDate:  2018/8/1 11:17
 * @Version:     0.0.1
 */
public enum FileResponseCode implements ResponseCodeInterface {

    /**
     * 请求的内容不存在
     */
    DATA_IS_NULL(3000, "文件服务异常"),
    DATA_IO_EXCEPTION(3001, "文件IO异常"),
    DATA_NOT_FOUND(3404, "文件资源不存在"),
    DATA_PNG_EXCEPTION(3405, "PNG文件处理异常"),
    DATA_FILE_COVERT_EXCEPTION(3406, "文档转换异常"),
    DATA_PIC_COVERT_EXCEPTION(3407, "图片转换异常"),
    DATA_INVALID_DOC_EXCEPTION(3408, "invalid document type"),
    DATA_NOT_COVERT_EXCEPTION(3409, "不支持该类型"),
    DATA_FILE_CODE_NULL(3410, "文件CODE非法"),
    DATA_NOT_FOUND_BY_CODE(3411, "暂无数据，请确认验证码是否正确"),
    REQUEST_LIMIT_ENOUGH(3412, "今日查询次数（10次）已用完，请明天再试"),
    WRITE_FASTDFS_ERROR(3413, "文件数据写入中断"),
    ;

    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误消息
     */
    private final String msg;

    /**
     * 错误消息（英文）
     */
    private final String enMsg;

    FileResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.enMsg = "";
    }

    @Override
    public int getCode() {
        return code;
    }
    @Override
    public String getMsg() {
        return msg;
    }
    @Override
    public String getEnMsg() {
        return enMsg;
    }
}
