package com.iyin.sign.system.controller;

import com.iyin.sign.system.common.BaseController;
import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.common.utils.MacTools;
import com.iyin.sign.system.service.VerifyLicense;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**O
 * @ClassName AuthLicenceController
 * @Desscription: auth licence
 * @Author wdf
 * @Date 2019/2/23 18:24
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/verify")
@Api(tags = "License")
public class AuthLicenceController extends BaseController {

    private String coentType = "content-type";

    @Autowired
    private VerifyLicense verifyAuthLicence;

    /**
     * @Description 注册Licence
     * @Author: wdf
     * @CreateDate: 2019/2/25 10:47
     * @UpdateUser: wdf
     * @UpdateDate: 2019/2/25 10:47
     * @Version: 0.0.1
     * @param file, response
     * @return com.cdf.eseal.common.json.EsealResult
     */
    @PostMapping(value = "/licence")
    @ApiOperation("注册 或更新 Licence V1.2.0")
    public EsealResult verifyAuthLicence(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        try {
            response.setHeader(coentType, "text/html");
            response.setContentType("text/html;charset=utf-8");
            this.handleSuc(verifyAuthLicence.verifyAuthLicence(file));
        } catch (Exception e) {
            logger.error("this AuthLicenceController.verifyAuthLicence() error:{}", e.getLocalizedMessage());
            this.handleError(e);
        }
        return this.result;
    }

    /**
    	* @Description 获取机器码
    	* @Author: wdf
        * @CreateDate: 2019/2/25 19:03
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/2/25 19:03
    	* @Version: 0.0.1
        * @param
        * @return com.cdf.eseal.common.json.EsealResult
        */
    @PostMapping(value = "/getMachineCode")
    @ApiOperation("获取机器码 V1.2.0")
    public EsealResult getMachineCode() {
        try {
            this.handleSuc(verifyAuthLicence.getMachineCode());
        } catch (Exception e) {
            logger.error("this AuthLicenceController.getMachineCode() error:{}", e.getLocalizedMessage());
            this.handleError(e);
        }
        return this.result;
    }

    /**
    	* @Description 获取Mac
    	* @Author: wdf 
        * @CreateDate: 2019/8/20 17:47
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/20 17:47
    	* @Version: 0.0.1
        * @return com.iyin.sign.system.common.json.EsealResult
        */
    @GetMapping(value = "/getMac")
    @ApiOperation("获取MAC V1.2.0")
    public EsealResult getMac() {
        try {
            this.handleSuc(MacTools.getMacList());
        } catch (Exception e) {
            logger.error("this AuthLicenceController.getMac() error:{}", e.getLocalizedMessage());
            this.handleError(e);
        }
        return this.result;
    }

}
