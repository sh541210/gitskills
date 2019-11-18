package com.iyin.sign.system.controller;

import com.iyin.sign.system.common.enums.UseSealSourceEnum;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.ICompactInfoService;
import com.iyin.sign.system.util.JWTUtils;
import com.iyin.sign.system.vo.file.req.ContractUseSealReqVO;
import com.iyin.sign.system.vo.file.req.FileManageSignInfoReqVO;
import com.iyin.sign.system.vo.file.req.FileManageSignReqVO;
import com.iyin.sign.system.vo.file.resp.CompactFieldInfoRespDTO;
import com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO;
import com.iyin.sign.system.vo.file.resp.FileDetailRespDTO;
import com.iyin.sign.system.vo.file.resp.FileManageQueryRespDTO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: CompactInfoController
 * @Description: 文件管理
 * @Author: yml
 * @CreateDate: 2019/8/7
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("file")
@Api(tags = "v1.1.0_文件管理")
public class FileManagerController {

    @Autowired
    private ICompactInfoService compactInfoService;
    @Autowired
    private JWTUtils jwtUtils;

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
    @ApiOperation("v1.1.0_文件上传")
    public IyinResult<CompactFileUploadRespDTO> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return compactInfoService.upload(file, request);
    }

    /**
     * 文件重新上传
     *
     * @param file
     * @param fileCode
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO>
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     */
    @PostMapping("reUpload")
    @ApiOperation("v1.1.0_文件重新上传")
    public IyinResult<CompactFileUploadRespDTO> reUpload(@RequestParam("file") MultipartFile file, @RequestParam("fileCode") String fileCode,HttpServletRequest request) {
        return compactInfoService.reUpload(file, fileCode, request);
    }

    /**
     * 文件删除
     *
     * @param fileCode
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO>
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     */
    @PostMapping("delete")
    @ApiOperation("v1.1.0_文件删除")
    public IyinResult<Boolean> delete(@RequestParam("fileCode") String fileCode,HttpServletRequest request) {
        return compactInfoService.delete(fileCode,request);
    }

    /**
     * 发起签署
     *
     * @param fileManageSignInfoReqVO
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     */
    @PostMapping("initiateSign")
    @ApiOperation("v1.1.0_发起签署")
    public IyinResult<String> initiateSign(@RequestBody @Valid FileManageSignInfoReqVO fileManageSignInfoReqVO) {
        return compactInfoService.initiateSign(fileManageSignInfoReqVO);
    }

    /**
     * 查询
     *
     * @param content
     * @param pageNum
     * @param pageSize
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.vo.file.resp.FileManageQueryRespDTO>>
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("query")
    @ApiOperation("v1.1.0_查询")
    public IyinResult<IyinPage<FileManageQueryRespDTO>> queryByContent(@RequestParam(value = "content", required = false) @ApiParam("搜索词") String content,
                                                                       @RequestParam("pageNum") @ApiParam("当前页") Integer pageNum,
                                                                       @RequestParam("pageSize") @ApiParam("页数") Integer pageSize,
                                                                       @RequestParam("orgId") @ApiParam("所属组织ID") String orgId, HttpServletRequest request) {
        return compactInfoService.queryByContent(content, pageNum, pageSize, orgId,request);
    }

    /**
     * 签名域
     *
     * @param fileCode
     * @param request
     * @return : com.iyin.sign.system.model.IyinResult<java.util.List<com.iyin.sign.system.vo.file.resp.CompactFieldInfoRespDTO>>
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @PostMapping("signDomain")
    @ApiOperation("v1.1.0_签名域")
    public IyinResult<List<CompactFieldInfoRespDTO>> signDomain(@RequestParam("fileCode") String fileCode, HttpServletRequest request) {
        return compactInfoService.signDomain(fileCode, request);
    }

    /**
     * 查看文件
     *
     * @param fileCode
     * @param request
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     */
    @PostMapping("viewFile")
    @ApiOperation("v1.1.0_查看文件")
    public IyinResult<FileDetailRespDTO> viewFile(@RequestParam("fileCode") @NotBlank(message = "文件编号不能为空") String fileCode, HttpServletRequest request) {
        return compactInfoService.viewFile(fileCode, request);
    }

    /**
     * 签署
     *
     * @param fileManageSignReqVO
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     */
    @PostMapping("sign")
    @ApiOperation("v1.1.0_签署")
    public IyinResult<Boolean> sign(@RequestBody @Valid FileManageSignReqVO fileManageSignReqVO, HttpServletRequest req) {
        String userId = getUserId(req);
        return compactInfoService.sign(fileManageSignReqVO, userId, req);
    }

    /**
     * 删除
     *
     * @param compactId
     * @return : com.iyin.sign.system.model.IyinResult
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     */
    @PostMapping("delStatus")
    @ApiOperation("v1.1.0_删除")
    public IyinResult<Boolean> delStatus(@RequestParam("compactId")@Param("文件ID")String compactId) {
        return compactInfoService.delStatus(compactId);
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
    @PostMapping("fileUseSeal")
    @ApiOperation("v1.2.1_用印申请")
    public IyinResult<Boolean> fileUseSeal(
            @RequestBody @Valid ContractUseSealReqVO useSealReqVO, HttpServletRequest request) {
        String userId = getUserId(request);
        useSealReqVO.setUserId(userId);
        useSealReqVO.setSource(UseSealSourceEnum.FILE.getCode());
        return compactInfoService.contractUseSeal(useSealReqVO,request);
    }

    private String getUserId(HttpServletRequest req) {
        String sessionToken = req.getHeader("session_token");
        Claims claims = jwtUtils.parseJWT(sessionToken);
        return String.valueOf(claims.get("userId"));
    }

}