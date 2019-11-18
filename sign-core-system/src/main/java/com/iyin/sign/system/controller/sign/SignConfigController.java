package com.iyin.sign.system.controller.sign;

import com.iyin.sign.system.common.SwaggerConstant;
import com.iyin.sign.system.dto.req.SignSysSignConfigDTO;
import com.iyin.sign.system.entity.SignSysSignConfig;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.ISignSysSignConfigService;
import com.iyin.sign.system.vo.req.SignSysSignConfigReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @ClassName SignConfigController
 * SignConfigController
 * @Author wdf
 * @Date 2019/7/22 19:10
 * @throws
 * @Version 1.0
 **/
@Api(value="签章配置", tags= {SwaggerConstant.SIGN_CONFIG})
@Slf4j
@RestController
@RequestMapping("/signConfig")
public class SignConfigController {

    @Autowired
    private ISignSysSignConfigService signSysSignConfigService;

    /**
    	* 获取签章配置详情接口
    	* @Author: wdf
        * @CreateDate: 2019/8/12 12:22
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/12 12:22
    	* @Version: 0.0.1
        * @param
        * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.dto.req.SignSysSignConfigDTO>
        */
    @ApiOperation("获取签章配置详情接口 V1.1.0")
    @GetMapping("/getSignConfig")
    public IyinResult<SignSysSignConfigDTO> getSignConfig() {
        SignSysSignConfig data=signSysSignConfigService.getSignConfig();
        SignSysSignConfigDTO signSysSignConfigDTO=new SignSysSignConfigDTO();
        BeanUtils.copyProperties(data,signSysSignConfigDTO);
        return IyinResult.success(signSysSignConfigDTO);
    }

    /**
    	* 签章配置修改
    	* @Author: wdf
        * @CreateDate: 2019/7/23 17:17
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/7/23 17:17
    	* @Version: 0.0.1
        * @param signSysSignConfigReqVO
        * @return com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
        */
    @ApiOperation("签章配置更新接口 V1.1.0")
    @PatchMapping("/updateSignConfig")
    public IyinResult<Boolean> updateSignConfig(@RequestBody @Valid SignSysSignConfigReqVO signSysSignConfigReqVO) {
        SignSysSignConfig signSysSignConfig=new SignSysSignConfig();
        BeanUtils.copyProperties(signSysSignConfigReqVO,signSysSignConfig);
        signSysSignConfig.setGmtModified(new Date());
        return IyinResult.success(signSysSignConfigService.updateById(signSysSignConfig));
    }

}
