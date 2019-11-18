package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SysSignatureLog;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.SignLogListReqVO;
import com.iyin.sign.system.vo.req.UserSignLogListReqVO;
import com.iyin.sign.system.vo.resp.EnterpriseSignLogRespVO;

/**
 * @ClassName: SignSysSignatureLogService
 * @Description: 签章系统签章日志服务
 * @Author: wdf
 * @CreateDate: 2019/8/7
 * @UpdateUser: wdf
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
public interface SignSysSignatureLogService extends IService<SysSignatureLog> {
    IyinResult<IyinPage<EnterpriseSignLogRespVO>> pageListSignLog(SignLogListReqVO reqVO);

    IyinResult<IyinPage<EnterpriseSignLogRespVO>> userPageListSignLog(UserSignLogListReqVO reqVO);
}
