package com.iyin.sign.system.controller;

import com.iyin.sign.system.common.enums.UseSealSourceEnum;
import com.iyin.sign.system.entity.SysSignApplication;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.IAuthTokenService;
import com.iyin.sign.system.service.ICompactInfoService;
import com.iyin.sign.system.util.JWTUtils;
import com.iyin.sign.system.vo.file.req.*;
import com.iyin.sign.system.vo.file.resp.*;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: CompactInfoController2
 * @Description:
 * @Author: yml
 * @CreateDate: 2019/8/13
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/13
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("contract")
@Api(tags = "v1.2.0_合同管理")
public class CompactInfoController {

    @Autowired
    private ICompactInfoService compactInfoService;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private IAuthTokenService authTokenService;


    /**
     * 文件上传
     *
     * @param file
     * @param request
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO>
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     */
    @PostMapping("upload")
    @ApiOperation("v1.2.0_文件上传")
    public IyinResult<CompactFileUploadRespDTO> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return compactInfoService.upload(file, request);
    }

    /**
     * 文件重新上传
     *
     * @param file
     * @param request
     * @param fileCode
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO>
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     */
    @PostMapping("reUpload")
    @ApiOperation("v1.2.0_文件重新上传")
    public IyinResult<CompactFileUploadRespDTO> reUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileCode") String fileCode, HttpServletRequest request) {
        return compactInfoService.reUpload(file,fileCode,request);
    }

    /**
     * 文件删除
     *
     * @param fileCode
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     */
    @PostMapping("delete")
    @ApiOperation("v1.2.0_文件删除")
    public IyinResult<Boolean> delete(@RequestParam("fileCode") String fileCode, HttpServletRequest request) {
        return compactInfoService.delete(fileCode,request);
    }

    /**
     * 合同查询
     *
     * @param contractQueryReqVO
     * @param req
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<ContractQueryRespDTO>>
     * @Author: yml
     * @CreateDate: 2019/8/13
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/13
     * @Version: 0.0.1
     */
    @PostMapping("queryContract")
    @ApiOperation("v1.2.0_合同查询")
    public IyinResult<IyinPage<ContractQueryRespDTO>> queryCompact(
            @RequestBody @Valid ContractQueryReqVO contractQueryReqVO, HttpServletRequest req) {
        String userId = getUserId(req);
        contractQueryReqVO.setUserId(userId);
        return compactInfoService.queryCompact(contractQueryReqVO, userId);
    }

    /**
     * 存草稿
     *
     * @param initiateContractReqVO
     * @param req
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/13
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/13
     * @Version: 0.0.1
     */
    @PostMapping("draftContract")
    @ApiOperation("v1.2.0_存草稿")
    public IyinResult<String> draftContract(
            @RequestBody @Valid InitiateContractReqVO initiateContractReqVO, HttpServletRequest req) {
        String userId = getUserId(req);
        initiateContractReqVO.setUserId(userId);
        return compactInfoService.draftContract(initiateContractReqVO);
    }

    /**
     * 立即发送
     *
     * @param initiateContractReqVO
     * @param req
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/13
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/13
     * @Version: 0.0.1
     */
    @PostMapping("initiateContract")
    @ApiOperation("v1.2.0_立即发送")
    public IyinResult<String> initiateContract(
            @RequestBody @Valid InitiateContractReqVO initiateContractReqVO, HttpServletRequest req) {
        String userId = getUserId(req);
        initiateContractReqVO.setUserId(userId);
        return compactInfoService.initiateContract(initiateContractReqVO);
    }

    /**
     * 发起并签署
     *
     * @param initiateSignContractReqVO
     * @param req
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/13
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/13
     * @Version: 0.0.1
     */
    @PostMapping("initiateSignContract")
    @ApiOperation("v1.2.0_发起并签署")
    public IyinResult<String> initiateSignContract(
            @RequestBody @Valid InitiateSignContractReqVO initiateSignContractReqVO, HttpServletRequest req) {
        String userId = getUserId(req);
        initiateSignContractReqVO.setUserId(userId);
        return compactInfoService.initiateSignContract(initiateSignContractReqVO, req);
    }

    /**
     * 签名域
     *
     * @param req
     * @return : com.iyin.sign.system.model.IyinResult<java.util.List<com.iyin.sign.system.vo.file.resp.CompactFieldInfoRespDTO>>
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("signDomain")
    @ApiOperation("v1.2.0_签名域")
    public IyinResult<List<CompactFieldInfoRespDTO>> signContractDomain(
            @RequestParam("contractId") @ApiParam("合同ID") String contractId, HttpServletRequest req) {
        String userId = getUserId(req);
        return compactInfoService.signContractDomain(contractId, userId);
    }

    /**
     * 签署合同信息
     *
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("signContractFile")
    @ApiOperation("v1.2.0_获取签署合同信息")
    public IyinResult<ContractFileRespDTO> signContractFile(
            @RequestParam("contractId") @ApiParam("合同ID") String contractId) {
        return compactInfoService.signContractFile(contractId);
    }

    /**
     * 签署合同
     *
     * @param contractReqVO
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("signContract")
    @ApiOperation("v1.2.0_签署合同")
    public IyinResult<Boolean> signContract(
            @RequestBody @Valid SignContractReqVO contractReqVO, HttpServletRequest req) {
        String userId = getUserId(req);
        contractReqVO.setUserId(userId);
        return compactInfoService.signContract(contractReqVO, req);
    }

    /**
     * 拒签合同
     *
     * @param contractId
     * @param rejectContent
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("rejectContract")
    @ApiOperation("v1.2.0_拒签合同")
    public IyinResult<Boolean> rejectContract(
            @RequestParam("contractId") @ApiParam("合同ID") String contractId,
            @RequestParam(value = "rejectContent", required = false) @ApiParam("拒签原因")
                    String rejectContent, HttpServletRequest req) {
        String userId = getUserId(req);
        return compactInfoService.rejectContract(contractId, rejectContent, userId);
    }

    /**
     * 撤销合同
     *
     * @param contractId
     * @param revokeContent
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("revokeContract")
    @ApiOperation("v1.2.0_撤销合同")
    public IyinResult<Boolean> revokeContract(
            @RequestParam("contractId") @ApiParam("合同ID") String contractId,
            @RequestParam(value = "revokeContent", required = false) @ApiParam("撤销原因")
                    String revokeContent, HttpServletRequest req) {
        String userId = getUserId(req);
        return compactInfoService.revokeContract(contractId, revokeContent, userId);
    }

    /**
     * 继续/重新发起合同
     *
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("restartContract")
    @ApiOperation("v1.2.0_继续/重新发起合同")
    public IyinResult<InitiateContractReqVO> restartContract(
            @RequestParam("contractId") @ApiParam("合同ID") String contractId, HttpServletRequest req) {
        String userId = getUserId(req);
        return compactInfoService.restartContract(contractId, userId);
    }

    /**
     * 打印合同
     *
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("printContract")
    @ApiOperation("v1.2.0_打印合同")
    public IyinResult<Boolean> printContract(
            @RequestParam("contractId") @ApiParam("合同ID") String contractId, HttpServletRequest req) {
        String userId = getUserId(req);
        return compactInfoService.printContract(contractId, userId);
    }

    /**
     * 合同相关人
     *
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("allUserContract")
    @ApiOperation("v1.2.0_合同相关人")
    public IyinResult<Set<ContractUserRespDTO>> allUserContract(
            @RequestParam("contractId") @ApiParam("合同ID") String contractId, HttpServletRequest req) {
        String userId = getUserId(req);
        return compactInfoService.allUserContract(contractId, userId);
    }

    /**
     * 下载合同
     *
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("downloadContract")
    @ApiOperation("v1.2.0_下载合同")
    public IyinResult<Boolean> downloadContract(@RequestParam("contractId") @ApiParam("合同ID")
                                                        String contractId, HttpServletRequest req, HttpServletResponse response) {
        String userId = getUserId(req);
        return compactInfoService.downloadContract(contractId, userId, response);
    }

    /**
     * 查看合同
     *
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("viewContract")
    @ApiOperation("v1.2.0_查看合同")
    public IyinResult<ContractDetailRespDTO> viewContract(
            @RequestParam("contractId") @ApiParam("合同ID") String contractId, HttpServletRequest req) {
        String userId = getUserId(req);
        return compactInfoService.viewContract(contractId, userId);
    }

    /**
     * 批量导入
     *
     * @param multipartFile
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("batchImport")
    @ApiOperation("v1.2.0_批量导入")
    public IyinResult<BatchImportRespDTO> batchImport(@RequestParam("file") MultipartFile multipartFile) {
        return compactInfoService.batchImport(multipartFile);
    }

    /**
     * 异常数据下载
     *
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @GetMapping("exceptionD")
    @ApiOperation("v1.2.0_异常数据下载")
    public IyinResult<Boolean> exceptionData(HttpServletResponse response) {
        return compactInfoService.exceptionD(response);
    }

    /**
     * 下载批量导入模板
     *
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @GetMapping("downloadBatchImport")
    @ApiOperation("v1.2.0_下载批量导入模板")
    public IyinResult<Boolean> downloadBatchImport(HttpServletResponse response) {
        return compactInfoService.downloadBatchImport(response);
    }

    /**
     * 删除合同
     *
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("delContract")
    @ApiOperation("v1.2.0_删除合同")
    public IyinResult<Boolean> delContract(
            @RequestParam("contractId") @ApiParam("合同ID") String contractId, HttpServletRequest req) {
        String userId = getUserId(req);
        return compactInfoService.delContract(contractId, userId);
    }

    /**
     * 催签合同
     *
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("urgeContract")
    @ApiOperation("v1.2.0_催签合同")
    public IyinResult<Boolean> urgeContract(@RequestParam("contractId") @ApiParam("合同ID") String contractId) {
        return compactInfoService.urgeContract(contractId);
    }

    /**
     * 单独催签
     *
     * @param contact
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("urgePerson")
    @ApiOperation("v1.2.0_单独催签")
    public IyinResult<Boolean> urgePerson(@RequestParam("contact") @ApiParam("联系方式") String contact,
                                          @RequestParam("contractId") @ApiParam("合同ID") String contractId) {
        return compactInfoService.urgePerson(contact, contractId);
    }

    /**
     * 合同签署日志
     *
     * @param contractLogReqVO
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("contractSignLog")
    @ApiOperation("v1.2.1_合同签署日志")
    public IyinResult<IyinPage<CompactLogRespDto>> contractSignLog(
            @RequestBody @Valid ContractLogReqVO contractLogReqVO, HttpServletRequest request) {
        String userId = getUserId(request);
        contractLogReqVO.setUserId(userId);
        return compactInfoService.contractSignLog(contractLogReqVO);
    }

    /**
     * 合同打印日志
     *
     * @param contractLogReqVO
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("contractPrintLog")
    @ApiOperation("v1.2.1_合同打印日志")
    public IyinResult<IyinPage<CompactLogRespDto>> contractPrintLog(
            @RequestBody @Valid ContractLogReqVO contractLogReqVO, HttpServletRequest request) {
        String userId = getUserId(request);
        contractLogReqVO.setUserId(userId);
        return compactInfoService.contractPrintLog(contractLogReqVO);
    }

    /**
     * 用印申请
     *
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     * @param useSealReqVO
     * @param request
     * @return : com.iyin.sign.system.model.IyinResult
     */
    @PostMapping("contractUseSeal")
    @ApiOperation("v1.2.1_用印申请")
    public IyinResult<Boolean> contractUseSeal(
            @RequestBody @Valid ContractUseSealReqVO useSealReqVO, HttpServletRequest request) {
        String userId = getUserId(request);
        useSealReqVO.setUserId(userId);
        useSealReqVO.setSource(UseSealSourceEnum.CONTRACT.getCode());
        return compactInfoService.contractUseSeal(useSealReqVO,request);
    }

    /**
     * 根据session_token获取登录用户ID
     *
     * @param req
     * @return : java.lang.String
     * @Author: yml
     * @CreateDate: 2019/8/14
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/14
     * @Version: 0.0.1
     */
    private String getUserId(HttpServletRequest req) {
        String userId;
        String sessionToken = req.getHeader("session_token");
        if(!StringUtils.isEmpty(sessionToken)){
            Claims claims = jwtUtils.parseJWT(sessionToken);
            userId = String.valueOf(claims.get("userId"));
        }else{
            String apiToken = req.getHeader("api_token");
            SysSignApplication sysSignApplication = authTokenService.verifyAuthToken(apiToken);
            userId = sysSignApplication.getUserId();
        }

        return userId;
    }
}
