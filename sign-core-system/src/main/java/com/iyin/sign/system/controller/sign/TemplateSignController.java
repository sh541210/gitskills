package com.iyin.sign.system.controller.sign;

import com.iyin.sign.system.common.SwaggerConstant;
import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.service.ITemplateSignService;
import com.iyin.sign.system.vo.sign.req.TemplateSignReqVO;
import com.iyin.sign.system.vo.sign.resp.SignRespDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @ClassName TemplateSignController
 * TemplateSignController
 * @Author wdf
 * @Date 2019/9/25 12:12
 * @throws
 * @Version 1.0
 **/
@Api(value="模板签章", tags= {SwaggerConstant.TEMP_SIGN})
@Slf4j
@RestController
@RequestMapping("/templateSign")
public class TemplateSignController {

    @Autowired
    private ITemplateSignService templateSignService;

    /**
    	* 模板签章
    	* @Author: wdf
        * @CreateDate: 2019/9/27 11:13
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/9/27 11:13
    	* @Version: 0.0.1
        * @param templateSignReqVO, request
        * @return com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
        */
    @PostMapping(value = "/sign", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "模板签章", tags = {SwaggerConstant.TEMP_SIGN}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public EsealResult<SignRespDTO> templateSign(@RequestBody @Valid TemplateSignReqVO templateSignReqVO, HttpServletRequest request) {
        return templateSignService.templateSign(templateSignReqVO,request);
    }


}
