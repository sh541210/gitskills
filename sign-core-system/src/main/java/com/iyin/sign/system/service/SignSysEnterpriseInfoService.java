package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysEnterpriseInfo;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.CreateEnterpriseStepOneReqVO;
import com.iyin.sign.system.vo.req.CreateEnterpriseStepTwoReqVO;
import com.iyin.sign.system.vo.req.EnterprsieListReqVO;
import com.iyin.sign.system.vo.req.UpdateEnterpriseReqVO;
import com.iyin.sign.system.vo.resp.EnterprsieBatchImportRespVO;
import com.iyin.sign.system.vo.resp.EnterprsieDetailRespVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: SignSysEnterpriseInfoService
 * @Description: 签章系统单位管理service
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 14:58
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 14:58
 * @Version: 0.0.1
 */
public interface SignSysEnterpriseInfoService extends IService<SignSysEnterpriseInfo> {

    /**
     * 分页查询单位信息
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<IyinPage<EnterprsieDetailRespVO>> pageListEnterprise(EnterprsieListReqVO reqVO, HttpServletRequest request);

    /**
     * 根据ID查询单位信息
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<EnterprsieDetailRespVO> getEnterpriseDetail(String id);

    /**
     * 创建单位信息第一步
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<String> createEnterpriseStepOne(CreateEnterpriseStepOneReqVO reqVO);

    /**
     * 创建单位第二步
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<Boolean> createEnterpriseStepTwo(CreateEnterpriseStepTwoReqVO reqVO);

    /**
     * 修改单位信息
     * @Author:      luwenxiong
     * @CreateDate:  2019/7/2
     * @UpdateUser:
     * @UpdateDate:  2019/7/2
     * @Version:     0.0.1
     * @return
     * @throws
     */
    IyinResult<Boolean> updateEnterprise(UpdateEnterpriseReqVO reqVO);

    /**
     * 批量导入单位信息
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<EnterprsieBatchImportRespVO> enterprsieBatchImport(MultipartFile enterpriseFile, HttpServletResponse response);

    /**
     * 下载批量导入单位信息模板
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<Boolean> downloadExeclSamples(HttpServletResponse response);

    /**
     * 下载批量导入单位信息结果
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<Boolean> downloadBatchImportResult(String importId, HttpServletResponse response);

    /**
     * 下载批量导入用户信息模板
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<Boolean> downloadUserExeclSamples(HttpServletResponse response);

    /**
     * 用户信息批量导入
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<EnterprsieBatchImportRespVO> userBatchImport(MultipartFile userFile, String nodeId, HttpServletResponse response);

    /**
     * 下载批量导入用户信息结果
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<Boolean> downloadUserBatchImportResult(String importId, HttpServletResponse response);
}
