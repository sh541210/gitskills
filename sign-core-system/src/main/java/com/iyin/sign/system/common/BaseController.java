package com.iyin.sign.system.common;

import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.common.exception.ServiceException;
import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.model.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: BaseController
 * @Description: 基础Controller
 * @Author: 唐德繁
 * @CreateDate: 2018/9/12 0012 下午 12:16
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/9/12 0012 下午 12:16
 * @Version: 0.0.1
 */
@ControllerAdvice
public class BaseController {

    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected EsealResult result;

    /**
     * 处理异常
     *
     * @param e
     */
    protected void handleError(Exception e) {
        result = new EsealResult();
        logger.error("服务器出错，出错原因为：{}", e.getMessage());
        if (e instanceof ServiceException && (((ServiceException) e).getErrorCode() != null)) {
            result.setFailCode(((ServiceException) e).getErrorCode());
        } else if(e instanceof BusinessException){
            result.setCode(((BusinessException) e).getMessageCode());
            result.setMsg(((BusinessException) e).getDetailMessage());
        } else {
            //其他ErrorCode未定义的异常，都统一称为服务器异常，不把详尽信息报给接口调用者，错误信息写日志
            result.setFailCode(ErrorCode.SERVER_50001);
        }
    }

    /**
     * 处理异常,可以指定errorCode
     *
     * @param errorCode
     */
    protected void handleError(ErrorCode errorCode) {
        result = new EsealResult();
        logger.error("服务器出错，出错原因为：{}", errorCode);
        result.setFailCode(errorCode);
    }

    /**
     * 处理正常返回数据
     *
     * @param object
     */
    protected void handleSuc(Object object) {
        result = new EsealResult();
        result.setData(object);
    }

    /**
     * 数据绑定重新定义：String格式日期自动转Date格式
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}

