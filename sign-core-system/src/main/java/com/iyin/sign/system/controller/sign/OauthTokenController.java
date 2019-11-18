package com.iyin.sign.system.controller.sign;

import com.iyin.sign.system.common.BaseController;
import com.iyin.sign.system.common.SwaggerConstant;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.IAuthTokenService;
import com.iyin.sign.system.vo.req.TokenReqVO;
import com.iyin.sign.system.vo.resp.TokenRespVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * @ClassName: OauthTokenController
 * @Description: 签章服务获取签章token.
 * @Author: wdf
 * @CreateDate: 2019/7/1
 * @UpdateUser: wdf
 * @UpdateDate: 2019/7/1
 * @Version: 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class OauthTokenController extends BaseController {

    @Autowired
    IAuthTokenService authTokenService;

    /**
     * @Description 获取token
     * @Author: wdf
     * @CreateDate: 2018/12/24 15:23
     * @UpdateUser: wdf
     * @UpdateDate: 2018/12/24 15:23
     * @Version: 0.0.1
     * @param tokenReqVO, request
     * @return IyinResult<cn.com.iyin.contract.cloud.vo.cloud.resp.TokenRespVO>
     */
    @PostMapping(value = "/createToken", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "createToken", tags = {SwaggerConstant.API_INTE_TOKEN}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public IyinResult<TokenRespVO> token(@RequestBody @Valid TokenReqVO tokenReqVO) {
        return authTokenService.createToken(tokenReqVO);
    }

}
