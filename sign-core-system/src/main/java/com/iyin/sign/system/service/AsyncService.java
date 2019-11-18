package com.iyin.sign.system.service;

import com.iyin.sign.system.common.interfaces.CacheConstants;
import com.iyin.sign.system.common.interfaces.CacheRemove;
import com.iyin.sign.system.entity.SysSignatureLog;
import com.iyin.sign.system.mapper.ISysSignatureLogMapper;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.vo.sign.req.SignatureLogReqVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName AsyncService
 * @Desscription: 异步处理
 * @Author wdf
 * @Date 2019/1/21 11:17
 * @Version 1.0
 **/
@Component
@Slf4j
public class AsyncService {

    @Resource
    private ISysSignatureLogMapper signatureLogMapper;

    /**
     * 添加签章记录/保存雾化、脱密文件
     *
     * @Author: yml
     * @CreateDate: 2019/6/28
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/28
     * @Version: 0.0.1
     * @param signatureLogReqVO
     * @param multiParameter
     * @return : void
     */
    @CacheRemove(value = CacheConstants.PDF_SCAN,key = "#signatureLogReqVO.signFileCode + '_*'")
    public void addLogSaveFile(SignatureLogReqVO signatureLogReqVO, String multiParameter) {
        /*添加签章记录*/
        log.info("com.iyin.sign.system.service.AsyncService.addLogSaveFile,param:{}|{}", signatureLogReqVO,
                multiParameter);
        SysSignatureLog signatureLog = new SysSignatureLog();
        BeanUtils.copyProperties(signatureLogReqVO, signatureLog);
        signatureLog.setPage(Long.valueOf(signatureLogReqVO.getPage()));
        if (StringUtils.isNotBlank(multiParameter)) {
            signatureLog.setMultiParam(multiParameter);
        }
        signatureLog.setId(SnowflakeIdWorker.getIdToStr());
        signatureLogMapper.insert(signatureLog);
    }

}