package com.iyin.sign.system.model.exception.handler;

import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.BaseResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName: RestExceptionHandler
 * @Description: 统一异常处理 异常增强，以JSON的形式返回给客服端
 * @Author: 肖泛舟
 * @CreateDate: 2018/7/28 16:41
 * @UpdateUser: 肖泛舟
 * @UpdateDate: 2018/7/28 16:41
 * @Version: 0.0.1
 */
@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * 空指针异常
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public <T> IyinResult<T> nullPointerExceptionHandler(NullPointerException e) {
        logger.warn("nullPointerExceptionHandler,exception:{}", e);

        return IyinResult.getIyinResult((BaseResponseCode.NULLPOINTEREXCEPTION));
    }

    /**
     * 类型转换异常
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(ClassCastException.class)
    public <T> IyinResult<T> classCastExceptionHandler(ClassCastException e) {
        logger.warn("classCastExceptionHandler,exception:{}", e);

        return IyinResult.getIyinResult(BaseResponseCode.CLASSCASTEXCEPTION);
    }

    /**
     * IO异常
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(IOException.class)
    public <T> IyinResult<T> iOExceptionHandler(IOException e) {
        logger.warn("iOExceptionHandler,exception:{}", e);

        return IyinResult.getIyinResult(BaseResponseCode.IOEXCEPTION);
    }

    /**
     * 未知方法异常
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(NoSuchMethodException.class)
    public <T> IyinResult<T> noSuchMethodExceptionHandler(NoSuchMethodException e) {
        logger.warn("noSuchMethodExceptionHandler,exception:{}", e);

        return IyinResult.getIyinResult(BaseResponseCode.NOSUCHMETHODEXCEPTION);
    }

    /**
     * 非法数据
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public <T> IyinResult<T> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        logger.warn("IllegalArgumentExceptionHandler,exception:{}", e);

        return IyinResult.getIyinResult(BaseResponseCode.ILLEGAL_ARGUMENT_EXCEPTION.getCode(), e.getLocalizedMessage());
    }

    /**
     * 数组越界异常
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public <T> IyinResult<T> indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException e) {
        logger.warn("indexOutOfBoundsExceptionHandler,exception:{}", e);

        return IyinResult.getIyinResult(BaseResponseCode.INDEXOUTOFBOUNDSEXCEPTION.getCode(), BaseResponseCode.INDEXOUTOFBOUNDSEXCEPTION.getMsg());
    }


    /**
     * 处理所有接口数据验证 - 数据绑定认证
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(BindException.class)
    <T> IyinResult<T> bindValidExceptionHandler(BindException e) {
        logger.warn("bindValidExceptionHandler BindException bindingResult.allErrors():{},exception:{}", e.getBindingResult().getAllErrors(), e);

        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        return createValidExceptionResp(errors);
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    <T> IyinResult<T> bindValidExceptionHandler(javax.validation.ConstraintViolationException e) {
        logger.warn("bindValidExceptionHandler ConstraintViolationException bindingResult.allErrors():{},exception:{}", e.getMessage(), e.getStackTrace());
        String flagOne=".arg3.";
        String flagTwo=":";
        String flagThree=",";
        String errorMessage=e.getMessage();
        if(StringUtils.isNotBlank(errorMessage)){
            errorMessage = getErrorMsg(flagOne, flagTwo, flagThree, errorMessage);
        }
        return IyinResult.getIyinResult(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), errorMessage);
    }

    private String getErrorMsg(String flagOne, String flagTwo, String flagThree, String errorMessage) {
        if(errorMessage.contains(flagOne)&&errorMessage.contains(flagTwo)){
            String[] str=errorMessage.split(flagOne);
            //默认长度为2
            int index=2;
            if(str.length>index) {
                errorMessage = errorMessage.split(flagOne)[1];
                errorMessage = errorMessage.substring(errorMessage.indexOf(flagTwo) + 1, errorMessage.lastIndexOf(flagThree)).trim();
            }else{
                errorMessage = errorMessage.substring(errorMessage.indexOf(flagTwo) + 1).trim();
            }
        }
        return errorMessage;
    }


    /**
     * 处理所有接口数据验证异常
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    <T> IyinResult<T> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        logger.warn("methodArgumentNotValidExceptionHandler MethodArgumentNotValidException bindingResult.allErrors():{},exception:{}", e.getBindingResult().getAllErrors(), e);

        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        return createValidExceptionResp(errors);
    }


    /**
     * 全局业务异常捕捉处理
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    <T> IyinResult<T> businessExceptionHandler(BusinessException e) {
        logger.warn("businessExceptionHandler exception:{}", e);
        logger.warn("businessExceptionHandler messageCode:{}, message:{}, localizedMessage:{}", e.getMessageCode(),e.getDetailMessage(), e.getLocalizedMessage());

        return IyinResult.getIyinResult(e.getMessageCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public <T> IyinResult<T> runtimeExceptionHandler(RuntimeException e) {
        logger.warn("runtimeExceptionHandler exception:{}", e);

        return IyinResult.getIyinResult(BaseResponseCode.OTHEREXCEPTION);
    }

    private <T> IyinResult<T> createValidExceptionResp(List<ObjectError> errors) {
        String[] msgs = new String[errors.size()];
        int i = 0;

        for (ObjectError error : errors) {
            msgs[i] = error.getDefaultMessage();
            i++;
        }
        return IyinResult.getIyinResult(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), msgs[0]);
    }

}
