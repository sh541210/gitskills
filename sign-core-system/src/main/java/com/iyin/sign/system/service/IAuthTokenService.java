package com.iyin.sign.system.service;

import com.iyin.sign.system.common.exception.ServiceException;
import com.iyin.sign.system.entity.SysSignApplication;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.TokenReqVO;
import com.iyin.sign.system.vo.resp.TokenRespVO;

/**
 * @ClassName: ICompactInfoService
 * @Description: 权限授权服务
 * @Author: wdf
 * @CreateDate: 2019/8/7
 * @UpdateUser: wdf
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
public interface IAuthTokenService {

    /**
     * @Description 新建应用token
     * @Author: wdf
     * @CreateDate: 2018/12/25 18:54
     * @UpdateUser: wdf
     * @UpdateDate: 2018/12/25 18:54
     * @Version: 0.0.1
     * @param tokenReqVO
     * @return IyinResult<cn.com.iyin.contract.cloud.vo.cloud.resp.TokenRespVO>
     */
    IyinResult<TokenRespVO> createToken(TokenReqVO tokenReqVO);

    /**
    	* @Description 校验token
    	* @Author: wdf 
        * @CreateDate: 2019/7/9 15:03
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/7/9 15:03
    	* @Version: 0.0.1
        * @param token
        * @return com.iyin.sign.system.entity.SysSignApplication
        */
    SysSignApplication verifyAuthToken(String token);

    SysSignApplication getUserId(String appId, String appSecret) throws ServiceException;
}
