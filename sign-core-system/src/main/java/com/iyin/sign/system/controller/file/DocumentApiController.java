package com.iyin.sign.system.controller.file;

import com.iyin.sign.system.common.BaseController;
import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.entity.FileResource;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.service.IDocumentApiService;
import com.iyin.sign.system.vo.file.FileResourceDto;
import com.iyin.sign.system.vo.req.QuerySignLogReqVO;
import com.iyin.sign.system.vo.resp.DocumentSignLogRespDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @ClassName: DocumentApiController
 * @Description: 文档API
 * @Author: yml
 * @CreateDate: 2019/7/1
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/1
 * @Version: 1.0.0
 */
@RestController
@Api(tags = "v1.1.0_文档API")
@RequestMapping("/document/api")
public class DocumentApiController extends BaseController {

    @Autowired
    private IDocumentApiService documentApiService;

    /**
     * 根据文档编码查询签署日志
     *
     * @Author: yml
     * @CreateDate: 2019/7/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/8
     * @Version: 0.0.1
     * @param querySignLogReqVO
     * @return : com.iyin.sign.system.common.json.EsealResult
     */
    @ApiOperation("根据文档编码查询签署日志")
    @PostMapping("querySignLog")
    public EsealResult<IyinPage<DocumentSignLogRespDTO>> querySignLog(@RequestBody @Valid QuerySignLogReqVO querySignLogReqVO, HttpServletRequest request){
        try {
            IyinPage<DocumentSignLogRespDTO> signLogs =documentApiService.querySignLog(querySignLogReqVO,request);
            this.handleSuc(signLogs);
        } catch (Exception e) {
            logger.error("com.iyin.sign.system.controller.file.DocumentApiController.querySignLog异常，{}",e.getLocalizedMessage());
            this.handleError(e);
        }
        return this.result;
    }

    /**
     * 上传文档并转换
     *
     * @Author: yml
     * @CreateDate: 2019/7/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/8
     * @Version: 0.0.1
     * @param multipartFile
     * @return : com.iyin.sign.system.common.json.EsealResult
     */
    @ApiOperation("上传文档并转换")
    @PostMapping("conversionDocument")
    public EsealResult<String> conversionDocument(@RequestParam("file")@ApiParam("文档") MultipartFile multipartFile,
                                          @RequestParam("userId")@ApiParam("用户ID") String userId){
        try {
            String fileCode = documentApiService.conversionDocument(multipartFile,userId);
            this.handleSuc(fileCode);
        } catch (Exception e) {
            logger.error("com.iyin.sign.system.controller.file.DocumentApiController.conversionDocument异常，{}",e.getLocalizedMessage());
            this.handleError(e);
        }
        return this.result;
    }

    /**
     * 查询文档详情
     *
     * @Author: yml
     * @CreateDate: 2019/7/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/8
     * @Version: 0.0.1
     * @param fileCode
     * @return : com.iyin.sign.system.common.json.EsealResult
     */
    @ApiOperation("查询文档详情")
    @PostMapping("queryDocumentDetail")
    public EsealResult<FileResource> queryDocumentDetail(@RequestParam("fileCode")@ApiParam("文档编码") String fileCode){
        try {
            FileResource fileResource = documentApiService.queryDocumentDetail(fileCode);
            this.handleSuc(fileResource);
        } catch (Exception e) {
            logger.error("com.iyin.sign.system.controller.file.DocumentApiController.queryDocumentDetail异常，{}",e.getLocalizedMessage());
            this.handleError(e);
        }
        return this.result;
    }

    /**
     * 根据文档编码下载文档
     *
     * @Author: yml
     * @CreateDate: 2019/7/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/8
     * @Version: 0.0.1
     * @param filecode
     * @return : com.iyin.sign.system.common.json.EsealResult
     */
    @ApiOperation(value = "根据文档编码下载文档")
    @PostMapping("downloadDocument")
    public EsealResult<String> downloadDocument(@RequestParam("fileCode")@ApiParam("文档编码") String filecode, HttpServletResponse response){
        try {
            InMemoryMultipartFile in = documentApiService.downloadDocument(filecode, response);
            this.handleSuc(in.getOriginalFilename());
        } catch (Exception e) {
            logger.error("com.iyin.sign.system.controller.file.DocumentApiController.downloadDocument异常，{}",e.getLocalizedMessage());
            this.handleError(e);
        }
        return this.result;
    }

    /**
     * 查询文档列表
     *
     * @Author: yml
     * @CreateDate: 2019/7/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/8
     * @Version: 0.0.1
     * @param fileName
     * @return : com.iyin.sign.system.common.json.EsealResult
     */
    @ApiOperation("查询文档列表")
    @PostMapping("queryDocumentList")
    public EsealResult<IyinPage<FileResourceDto>> queryDocumentList(@RequestParam(value = "fileName",required = false)@ApiParam("文档名称") String fileName,
                                         @RequestParam("pageSize")@ApiParam("页数") Integer pageSize,
                                         @RequestParam("pageNum")@ApiParam("页码") Integer pageNum,HttpServletRequest request){
        try {
            IyinPage<FileResourceDto> documentList= documentApiService.queryDocumentList(fileName,pageNum,pageSize,request);
            this.handleSuc(documentList);
        } catch (Exception e) {
            logger.error("com.iyin.sign.system.controller.file.DocumentApiController.queryDocumentList异常，{}",e.getLocalizedMessage());
            this.handleError(e);
        }
        return this.result;
    }

    /**
     * 验证码验证签章
     *
     * @Author: yml
     * @CreateDate: 2019/7/25
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/25
     * @Version: 0.0.1
     * @param verificationCode
     * @param httpServletRequest
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.entity.FileResource>
     */
    @ApiOperation("v1.1.0_验证码验证签章")
    @GetMapping("queryDocumetnByValityCode")
    public EsealResult<FileResource> queryDocumetnByValityCode(@RequestParam("verificationCode") String verificationCode, HttpServletRequest httpServletRequest){
        try {
            FileResource fileResource = documentApiService.queryDocumetnByValityCode(verificationCode,httpServletRequest);
            this.handleSuc(fileResource);
        } catch (Exception e) {
            logger.error("com.iyin.sign.system.controller.file.DocumentApiController.queryDocumetnByValityCode异常，{}",e.getLocalizedMessage());
            this.handleError(e);
        }
        return this.result;
    }
}