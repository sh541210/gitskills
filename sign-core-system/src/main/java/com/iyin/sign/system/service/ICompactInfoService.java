package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysCompactInfo;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.file.req.*;
import com.iyin.sign.system.vo.file.resp.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: ICompactInfoService
 * @Description: 文件管理服务
 * @Author: yml
 * @CreateDate: 2019/8/7
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
public interface ICompactInfoService extends IService<SignSysCompactInfo> {

    /**
     * 文件上传
     *
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     * @param file
     * @param request
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO>
     */
    IyinResult<CompactFileUploadRespDTO> upload(MultipartFile file,HttpServletRequest request);

    /**
     * 文件重新上传
     *
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     * @param file
     * @param request
     * @param fileCode
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO>
     */
    IyinResult<CompactFileUploadRespDTO> reUpload(MultipartFile file,String fileCode,HttpServletRequest request);

    /**
     * 文件删除
     *
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     * @param fileCode
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    IyinResult<Boolean> delete(String fileCode,HttpServletRequest request);

    /**
     * 签署
     *
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     * @param fileManageSignInfoReqVO
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    IyinResult<String> initiateSign(FileManageSignInfoReqVO fileManageSignInfoReqVO);

    /**
     * 查询
     *
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     * @param content
     * @param pageNum
     * @param pageSize
     * @param orgId
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.vo.file.resp.FileManageQueryRespDTO>>
     */
    IyinResult<IyinPage<FileManageQueryRespDTO>> queryByContent(String content, Integer pageNum, Integer pageSize, String orgId, HttpServletRequest request);

    /**
     * 签名域
     *
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     * @param fileCode
     * @param request
     * @return : com.iyin.sign.system.model.IyinResult<java.util.List<com.iyin.sign.system.vo.file.resp.CompactFieldInfoRespDTO>>
     */
    IyinResult<List<CompactFieldInfoRespDTO>> signDomain(String fileCode, HttpServletRequest request);

    /**
     * 查看文件
     *
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     * @param fileCode
     * @param request
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.resp.FileDetailRespDTO>
     */
    IyinResult<FileDetailRespDTO> viewFile(String fileCode, HttpServletRequest request);

    /**
     * 签署
     *
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     * @param fileManageSignReqVO
     * @param userId
     * @param req
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.String>
     */
    IyinResult<Boolean> sign(FileManageSignReqVO fileManageSignReqVO, String userId, HttpServletRequest req);

    /**
     * 删除
     *
     * @Author: yml
     * @CreateDate: 2019/8/9
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/9
     * @Version: 0.0.1
     * @param compactId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.String>
     */
    IyinResult<Boolean> delStatus(String compactId);

    /**
     * 合同查询
     *
     * @Author: yml
     * @CreateDate: 2019/8/13
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/13
     * @Version: 0.0.1
     * @param contractQueryReqVO
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.vo.file.resp.ContractQueryRespDTO>>
     */
    IyinResult<IyinPage<ContractQueryRespDTO>> queryCompact(ContractQueryReqVO contractQueryReqVO, String userId);

    /**
     * 存草稿
     *
     * @Author: yml
     * @CreateDate: 2019/8/14
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/14
     * @Version: 0.0.1
     * @param initiateContractReqVO
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.String>
     */
    IyinResult<String> draftContract(InitiateContractReqVO initiateContractReqVO);

    /**
     * 发起并签署
     *
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     * @param initiateSignContractReqVO
     * @param req
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.String>
     */
    IyinResult<String> initiateSignContract(InitiateSignContractReqVO initiateSignContractReqVO, HttpServletRequest req);

    /**
     * 立即发送
     *
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     * @param initiateContractReqVO
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.String>
     */
    IyinResult<String> initiateContract(InitiateContractReqVO initiateContractReqVO);

    /**
     * 签名域
     *
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     * @param contractId
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<java.util.List<com.iyin.sign.system.vo.file.resp.CompactFieldInfoRespDTO>>
     */
    IyinResult<List<CompactFieldInfoRespDTO>> signContractDomain(String contractId, String userId);

    /**
     * 签署合同
     *
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     * @param contractReqVO
     * @param req
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    IyinResult<Boolean> signContract(SignContractReqVO contractReqVO, HttpServletRequest req);

    /**
     * 查看合同
     *
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     * @param contractId
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult
     */
    IyinResult<ContractDetailRespDTO> viewContract(String contractId, String userId);

    /**
     * 签署合同信息
     *
     * @Author: yml
     * @CreateDate: 2019/8/16
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/16
     * @Version: 0.0.1
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.resp.ContractFileRespDTO>
     */
    IyinResult<ContractFileRespDTO> signContractFile(String contractId);

    /**
     * 删除合同
     *
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     * @param contractId
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    IyinResult<Boolean> delContract(String contractId, String userId);

    /**
     * 拒签合同
     *
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     * @param contractId
     * @param rejectContent
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    IyinResult<Boolean> rejectContract(String contractId, String rejectContent, String userId);

    /**
     * 撤销合同
     *
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     * @param contractId
     * @param revokeContent
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    IyinResult<Boolean> revokeContract(String contractId, String revokeContent, String userId);

    /**
     * 打印合同
     *
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     * @param contractId
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    IyinResult<Boolean> printContract(String contractId, String userId);

    /**
     * 下载合同
     *
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     * @param contractId
     * @param userId
     * @param response
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    IyinResult<Boolean> downloadContract(String contractId, String userId, HttpServletResponse response);

    /**
     * 重新发起合同
     *
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     * @param contractId
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.req.InitiateContractReqVO>
     */
    IyinResult<InitiateContractReqVO> restartContract(String contractId, String userId);

    /**
     * 合同相关人
     *
     * @Author: yml
     * @CreateDate: 2019/8/20
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/20
     * @Version: 0.0.1
     * @param contractId
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<java.util.Set<com.iyin.sign.system.vo.file.resp.ContractUserRespDTO>>
     */
    IyinResult<Set<ContractUserRespDTO>> allUserContract(String contractId, String userId);

    /**
     * 批量导入
     *
     * @Author: yml
     * @CreateDate: 2019/8/21
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/21
     * @Version: 0.0.1
     * @param multipartFile
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.req.InitiateContractReqVO.SignInfo>
     */
    IyinResult<BatchImportRespDTO> batchImport(MultipartFile multipartFile);

    /**
     * 下载批量导入模板
     *
     * @Author: yml
     * @CreateDate: 2019/8/21
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/21
     * @Version: 0.0.1
     * @param response
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    IyinResult<Boolean> downloadBatchImport(HttpServletResponse response);

    /**
     * 异常数据下载
     *
     * @Author: yml
     * @CreateDate: 2019/8/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/23
     * @Version: 0.0.1
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     * @param response
     */
    IyinResult<Boolean> exceptionD(HttpServletResponse response);

    /**
     * 催签合同
     *
     * @Author: yml
     * @CreateDate: 2019/8/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/23
     * @Version: 0.0.1
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    IyinResult<Boolean> urgeContract(String contractId);

    /**
     * 合同签署日志
     *
     * @Author: yml
     * @CreateDate: 2019/8/30
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/30
     * @Version: 0.0.1
     * @param contractLogReqVO
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.vo.file.resp.CompactLogRespDto>>
     */
    IyinResult<IyinPage<CompactLogRespDto>> contractSignLog(ContractLogReqVO contractLogReqVO);

    /**
     * 合同打印日志
     *
     * @Author: yml
     * @CreateDate: 2019/8/30
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/30
     * @Version: 0.0.1
     * @param contractLogReqVO
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.vo.file.resp.CompactLogRespDto>>
     */
    IyinResult<IyinPage<CompactLogRespDto>> contractPrintLog(ContractLogReqVO contractLogReqVO);

    /**
     * 单独催签
     *
     * @Author: yml
     * @CreateDate: 2019/9/4
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/4
     * @Version: 0.0.1
     * @param contact
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    IyinResult<Boolean> urgePerson(String contact, String contractId);

    /**
     * 用印申请
     *
     * @Author: yml
     * @CreateDate: 2019/10/10
     * @UpdateUser: yml
     * @UpdateDate: 2019/10/10
     * @Version: 0.0.1
     * @param useSealReqVO
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    IyinResult<Boolean> contractUseSeal(ContractUseSealReqVO useSealReqVO, HttpServletRequest request);
}
