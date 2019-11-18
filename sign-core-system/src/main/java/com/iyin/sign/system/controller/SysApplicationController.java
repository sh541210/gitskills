package com.iyin.sign.system.controller;

import com.iyin.sign.system.dto.req.SysApplicationDto;
import com.iyin.sign.system.dto.req.SysApplicationReq;
import com.iyin.sign.system.dto.req.SysSignApplicationDTO;
import com.iyin.sign.system.entity.SysSignApplication;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.ISysApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @ClassName: SysApplicationController
 * @Description: 应用管理模块
 * @Author: wdf
 * @CreateDate: 2019/7/1
 * @UpdateUser: wdf
 * @UpdateDate: 2019/7/1
 * @Version: 1.0.0
 */
@Api(tags = "应用管理模块")
@RestController
@Slf4j
@RequestMapping("/sysApplication")
public class SysApplicationController {
    @Autowired
    ISysApplicationService sysApplicationService;

    /**
    	* 应用管理列表接口
    	* @Author: wdf
        * @CreateDate: 2019/8/12 12:26
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/12 12:26
    	* @Version: 0.0.1
        * @param currPage, pageSize, applicationName
        * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.dto.req.SysSignApplicationDTO>>
        */
    @ApiOperation("应用管理列表接口")
    @GetMapping("/getApplicationList")
    public IyinResult<IyinPage<SysSignApplicationDTO>> getApplicationList(@RequestParam Integer currPage, @RequestParam Integer pageSize,@RequestParam(required = false) String applicationName,HttpServletRequest request) {
        return sysApplicationService.getApplicationList(currPage,pageSize,applicationName,request);
    }

    /**
    	* 应用详情接口
    	* @Author: wdf
        * @CreateDate: 2019/8/12 12:26
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/12 12:26
    	* @Version: 0.0.1
        * @param sysApplicationDto
        * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.dto.req.SysApplicationDto>
        */
    @ApiOperation("应用详情接口")
    @GetMapping("/getApplication")
    public IyinResult<SysApplicationDto> getApplication(@RequestBody SysApplicationDto sysApplicationDto) {
        SysSignApplication data=sysApplicationService.getApplicationById(sysApplicationDto);
        SysApplicationDto sysApplicationDto1=new SysApplicationDto();
        BeanUtils.copyProperties(data,sysApplicationDto1);
        return IyinResult.success(sysApplicationDto1);
    }

    /**
    	* 应用添加接口
    	* @Author: wdf
        * @CreateDate: 2019/8/12 12:26
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/12 12:26
    	* @Version: 0.0.1
        * @param sysApplicationReq
        * @return com.iyin.sign.system.model.IyinResult<java.lang.Integer>
        */
    @ApiOperation("应用添加接口")
    @PostMapping("/addApplication")
    @ApiIgnore
    public IyinResult<Integer> addApplication(@RequestBody @Valid SysApplicationReq sysApplicationReq, HttpServletRequest request) {
        return IyinResult.success(sysApplicationService.addApplication(sysApplicationReq,request));
    }

    /**
    	* 应用添加并返回应用信息接口
    	* @Author: wdf
        * @CreateDate: 2019/8/12 12:27
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/12 12:27
    	* @Version: 0.0.1
        * @param sysApplicationReq
        * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.entity.SysSignApplication>
        */
    @ApiOperation("应用添加并返回应用信息接口")
    @PostMapping("/addAppBackInfo")
    public IyinResult<SysSignApplication> addAppBackInfo(@RequestBody SysApplicationReq sysApplicationReq, HttpServletRequest request) {
        return IyinResult.success(sysApplicationService.addAppBackInfo(sysApplicationReq,request));
    }

    /**
    	* 应用更新接口
    	* @Author: wdf
        * @CreateDate: 2019/8/12 12:27
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/12 12:27
    	* @Version: 0.0.1
        * @param sysApplicationReq
        * @return com.iyin.sign.system.model.IyinResult
        */
    @ApiOperation("应用更新接口")
    @PatchMapping("/updateApplication")
    public IyinResult updateApplication(@RequestBody SysApplicationReq sysApplicationReq) {
        SysSignApplication sysSignApplication=new SysSignApplication();
        BeanUtils.copyProperties(sysApplicationReq,sysSignApplication);
        sysApplicationService.updateApplication(sysSignApplication);
        return IyinResult.success();
    }

    /**
    	* 应用删除接口
    	* @Author: wdf 
        * @CreateDate: 2019/8/12 12:27
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/12 12:27
    	* @Version: 0.0.1
        * @param id
        * @return com.iyin.sign.system.model.IyinResult
        */
    @ApiOperation("应用删除接口")
    @DeleteMapping("/deleteApplication")
    public IyinResult deleteApplication(@RequestParam String id) {
        sysApplicationService.deleteApplication(id);
        return IyinResult.success();
    }

    /**
    	* 应用启用/禁用接口
    	* @Author: wdf
        * @CreateDate: 2019/8/12 12:27
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/12 12:27
    	* @Version: 0.0.1
        * @param id, type
        * @return com.iyin.sign.system.model.IyinResult
        */
    @ApiOperation("应用启用/禁用接口")
    @PatchMapping("/abledApp")
    public IyinResult abledApp(@ApiParam("应用ID")@RequestParam(name = "id") String id,@ApiParam("0启用1禁用") @RequestParam(name = "type")@Range(min = 0,max = 1) Integer type) {
        sysApplicationService.abledApp(id,type);
        return IyinResult.success();
    }

}
