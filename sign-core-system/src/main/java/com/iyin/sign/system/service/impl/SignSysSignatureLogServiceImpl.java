package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.entity.SysSignatureLog;
import com.iyin.sign.system.mapper.ISysSignatureLogMapper;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.SignSysSignatureLogService;
import com.iyin.sign.system.vo.req.SignLogListReqVO;
import com.iyin.sign.system.vo.req.UserSignLogListReqVO;
import com.iyin.sign.system.vo.resp.EnterpriseSignLogRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: SignSysSignatureLogServiceImpl
 * @Description: 签章日志service
 * @Author: luwenxiong
 * @CreateDate: 2019/7/1 11:33
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/1 11:33
 * @Version: 0.0.1
 */
@Service
@Slf4j
public class SignSysSignatureLogServiceImpl extends ServiceImpl<ISysSignatureLogMapper,SysSignatureLog> implements SignSysSignatureLogService {

    @Resource
    ISysSignatureLogMapper iSysSignatureLogMapper;

    @Override
    public IyinResult<IyinPage<EnterpriseSignLogRespVO>> pageListSignLog(SignLogListReqVO reqVO) {

        IyinPage<EnterpriseSignLogRespVO> iyinPage = new IyinPage<>(reqVO.getCurrentPage(), reqVO.getPageSize());
        List<EnterpriseSignLogRespVO> logList =  iSysSignatureLogMapper.pageListSignLog(iyinPage,reqVO);
        iyinPage.setRecords(logList);
        return IyinResult.getIyinResult(iyinPage);
    }

    @Override
    public IyinResult<IyinPage<EnterpriseSignLogRespVO>> userPageListSignLog(UserSignLogListReqVO reqVO) {
        IyinPage<EnterpriseSignLogRespVO> iyinPage = new IyinPage<>(reqVO.getCurrentPage(), reqVO.getPageSize());
        List<EnterpriseSignLogRespVO> logList =  iSysSignatureLogMapper.userPageListSignLog(iyinPage,reqVO);
        iyinPage.setRecords(logList);
        return IyinResult.getIyinResult(iyinPage);
    }
}
