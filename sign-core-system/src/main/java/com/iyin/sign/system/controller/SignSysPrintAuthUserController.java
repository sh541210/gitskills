package com.iyin.sign.system.controller;


import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.ISignSysPrintAuthUserService;
import com.iyin.sign.system.vo.req.SignSysContractPrintAddReqVO;
import com.iyin.sign.system.vo.req.SignSysPrintAuthUserAddReqVO;
import com.iyin.sign.system.vo.req.SignSysPrintAuthUserDelReqVO;
import com.iyin.sign.system.vo.resp.SignSysPrintAuthUserListRespVO;
import com.iyin.sign.system.vo.resp.SignSysPrintAuthUserRespVO;
import com.iyin.sign.system.vo.resp.SignSysPrintInfoRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 打印分配
 * </p>
 *
 * @author wdf
 * @since 2019-08-06
 */
@RestController
@Slf4j
@RequestMapping("/printAuthUser")
@Api(value = "文件打印分配", tags = "v1.2.0_打印分配")
public class SignSysPrintAuthUserController {

    @Autowired
    private ISignSysPrintAuthUserService signSysPrintAuthUserService;

    /**
    	* 添加打印分配
    	* @Author: wdf
        * @CreateDate: 2019/8/12 12:24
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/12 12:24
    	* @Version: 0.0.1
        * @param signSysPrintAuthAddReqVO, request
        * @return com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
        */
    @ApiOperation("添加打印分配 v1.2.0")
    @PostMapping("/addPrintAuthUser")
    public IyinResult<Boolean> addPrintAuthUser(@RequestBody @Valid SignSysPrintAuthUserAddReqVO signSysPrintAuthAddReqVO, HttpServletRequest request) {
        return IyinResult.success(signSysPrintAuthUserService.addPrintAuthUser(signSysPrintAuthAddReqVO,request));
    }

    /**
     * 合同添加打印分配
     *
     * @Author: yml
     * @CreateDate: 2019/9/4
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/4
     * @Version: 0.0.1
     * @param contractPrintAddReqVO
     * @param request
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    @ApiOperation("添加打印分配 v1.2.0")
    @PostMapping("/addPrintAuthUser2")
    public IyinResult<Boolean> addPrintAuthUser2(
            @RequestBody @Valid SignSysContractPrintAddReqVO contractPrintAddReqVO, HttpServletRequest request) {
        return IyinResult.success(signSysPrintAuthUserService.addPrintAuthUser2(contractPrintAddReqVO, request));
    }

    /**
    	* 获取已分配对象
    	* @Author: wdf
        * @CreateDate: 2019/8/12 12:24
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/12 12:24
    	* @Version: 0.0.1
        * @param fileCode
        * @return com.iyin.sign.system.model.IyinResult<java.util.List<com.iyin.sign.system.vo.resp.SignSysPrintAuthUserRespVO>>
        */
    @ApiOperation("获取已分配对象 v1.2.0")
    @GetMapping("/getPrintAuthList")
    public IyinResult<SignSysPrintAuthUserListRespVO> getPrintAuthList(@RequestParam String fileCode) {
        return IyinResult.success(signSysPrintAuthUserService.getPrintAuthList(fileCode));
    }

    /**
     * 合同获取已分配对象与次数
     *
     * @Author: yml
     * @CreateDate: 2019/9/3
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/3
     * @Version: 0.0.1
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult<java.util.List<com.iyin.sign.system.vo.resp.SignSysPrintInfoRespVO>>
     */
    @ApiOperation("合同获取已分配对象与次数 v1.2.0")
    @GetMapping("/getPrintAuthList2")
    public IyinResult<SignSysPrintInfoRespVO> getPrintAuthList2(@RequestParam @ApiParam("合同ID") String contractId) {
        return IyinResult.success(signSysPrintAuthUserService.getPrintAuthList2(contractId));
    }

    /**
    	* 删除已分配对象
    	* @Author: wdf
        * @CreateDate: 2019/8/12 12:24
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/12 12:24
    	* @Version: 0.0.1
        * @param signSysPrintAuthUserDelReqVO, request
        * @return com.iyin.sign.system.model.IyinResult<java.lang.Integer>
        */
    @ApiOperation("删除已分配对象 v1.2.0")
    @DeleteMapping("/delPrintAuthList")
    public IyinResult<Integer> delPrintAuthList(@RequestBody @Valid SignSysPrintAuthUserDelReqVO signSysPrintAuthUserDelReqVO, HttpServletRequest request) {
        return IyinResult.success(signSysPrintAuthUserService.delPrintAuthUser(signSysPrintAuthUserDelReqVO,request));
    }

}
