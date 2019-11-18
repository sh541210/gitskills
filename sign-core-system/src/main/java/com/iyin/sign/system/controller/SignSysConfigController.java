package com.iyin.sign.system.controller;

import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.SignSysDefaultConfigService;
import com.iyin.sign.system.vo.req.SignSysDefaultConfigReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @ClassName: SignSysConfigController
 * @Description: 系统设置服务
 * @Author: luwenxiong
 * @CreateDate: 2019/8/5 17:45
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/5 17:45
 * @Version: 0.0.1
 */
@RequestMapping("config")
@Api(tags = "系统设置")
@RestController
@Slf4j
public class SignSysConfigController {

    @Autowired
    SignSysDefaultConfigService signSysDefaultConfigService;

    @PostMapping("/defaultConfig")
    @ApiOperation("默认信息设置")
    public IyinResult<Boolean> defaultConfig(@RequestBody @Valid SignSysDefaultConfigReqVO reqV){
             return signSysDefaultConfigService.defaultConfig(reqV);
    }

    @PostMapping("/logoUpload")
    @ApiOperation("上传logo")
    public IyinResult<String> logoUpload(@RequestParam(value = "file") MultipartFile logo,@RequestParam(value = "userId")String userId){
          return signSysDefaultConfigService.logoUpload(logo,userId);
    }
}
