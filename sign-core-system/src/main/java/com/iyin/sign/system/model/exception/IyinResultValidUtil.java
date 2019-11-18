package com.iyin.sign.system.model.exception;

import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.BaseResponseCode;

/**
 * @ClassName: IyinResultValidUtil
 * @Description: IyinResult结果校验工具类
 * @Author: 唐德繁
 * @CreateDate: 2018/9/18 0018 下午 3:15
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/9/18 0018 下午 3:15
 * @Version: 0.0.1
 */
public class IyinResultValidUtil {

    private IyinResultValidUtil() {

    }

    /**
     * 校验IyinResult<T> 返回的code 是否为成功，当不为成功时抛出异常BusinessException
     * @Author: 唐德繁
     * @CreateDate: 2018/09/18 下午 3:16
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/09/18 下午 3:16
     * @Version: 0.0.1
     * @param result
     * @return
     */
    public static final <T> void validateSuccess(IyinResult<T> result){
        if (null == result) {
            throw new BusinessException(BaseResponseCode.SYSTEM_BUSY.getCode(),BaseResponseCode.SYSTEM_BUSY.getMsg());
        }
        if (BaseResponseCode.SUCCESS.getCode() != result.getCode()){
            throw new BusinessException(result.getCode(),result.getMsg());
        }
    }

    /**
     * 校验IyinResult<T> 返回的code 是否为成功，当不为成功时抛出异常BusinessException
     * @Author: 唐德繁
     * @CreateDate: 2018/09/18 下午 3:16
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/09/18 下午 3:16
     * @Version: 0.0.1
     * @param result
     * @return
     */
    public static final <T> void validateSuccess(EsealResult<T> result){
        if (null == result) {
            throw new BusinessException(BaseResponseCode.SYSTEM_BUSY.getCode(),BaseResponseCode.SYSTEM_BUSY.getMsg());
        }
        if (BaseResponseCode.SUCCESS.getCode() != result.getCode()){
            throw new BusinessException(result.getCode(),result.getMsg());
        }
    }
}
