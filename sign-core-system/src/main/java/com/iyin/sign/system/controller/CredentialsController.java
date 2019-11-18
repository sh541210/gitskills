package com.iyin.sign.system.controller;

import com.iyin.sign.system.common.BaseController;
import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.service.ICertInfoService;
import com.iyin.sign.system.vo.resp.CertInfoRespDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: FileController.java
 * @Description: 证书管理
 * @Author: yml
 * @CreateDate: 2019/6/20
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/20
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/credentials")
@Api(tags = "证书管理")
@AllArgsConstructor
public class CredentialsController extends BaseController {

    private ICertInfoService certInfoService;

    /**
     * 导入证书.
     *
     * @Author: yml
     * @CreateDate: 2019/7/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/23
     * @Version: 0.0.1
     * @param file
     * @param certPassword
     * @param orgId
     * @param userId
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.resp.CertInfoRespDTO>
     */
    @PostMapping(
            value = "/fileUpload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("导入证书")
    public EsealResult<CertInfoRespDTO> fileUpload(
            @RequestParam("file") @ApiParam("证书文件") MultipartFile file,
            @RequestParam(value = "certPassword") @ApiParam("证书密码") String certPassword,
            @RequestParam("orgId") @ApiParam("组织ID") String orgId,
            @RequestParam("userId") @ApiParam("用户ID") String userId) {
        try {
            CertInfoRespDTO certInfoDto =
                    certInfoService.importFile(file, certPassword, userId, orgId);
            this.handleSuc(certInfoDto);
        } catch (Exception e) {
            logger.error("this CredentialsController.fileUpload() error:{}", e.getLocalizedMessage());
            this.handleError(e);
        }
        return this.result;
    }

    /**
     * 根据组织ID查找证书
     *
     * @Author: yml
     * @CreateDate: 2019/6/27
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/27
     * @Version: 0.0.1
     * @param orgId
     * @return : com.iyin.sign.system.common.json.EsealResult
     */
    @ApiOperation("根据组织ID查找证书")
    @PostMapping(value = "listByOrgId")
    public EsealResult<IyinPage<CertInfoRespDTO>> listByOrgId(@RequestParam("orgId")  @ApiParam("组织ID") String orgId,
                                                              @RequestParam("pageNum")@ApiParam("页码") Integer pageNum,
                                                              @RequestParam("pageSize")@ApiParam("页数") Integer pageSize){
        try {
            IyinPage<CertInfoRespDTO> certinforespdtos = certInfoService.listByOrgId(orgId,pageNum,pageSize);
            this.handleSuc(certinforespdtos);
        } catch (Exception e) {
            logger.error("this CredentialsController.listByOrgId() error:{}", e.getLocalizedMessage());
            this.handleError(e);
        }
        return this.result;
    }

    /**
     * 删除证书
     *
     * @Author: yml
     * @CreateDate: 2019/6/27
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/27
     * @Version: 0.0.1
     * @param certId
     * @return : com.iyin.sign.system.common.json.EsealResult
     */
    @PostMapping(value = "deleteById")
    @ApiOperation("删除证书")
    public EsealResult<Boolean> deleteById(@RequestParam("certId")  @ApiParam("证书ID") String certId){
        try {
            boolean flag = certInfoService.deleteById(certId);
            this.handleSuc(flag);
        } catch (Exception e) {
            logger.error("this CredentialsController.listByOrgId() error:{}", e.getLocalizedMessage());
            this.handleError(e);
        }
        return this.result;
    }
}
