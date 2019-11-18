package com.iyin.sign.system.controller;

import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.SignSysEnterpriseInfoService;
import com.iyin.sign.system.service.SignSysNodeInfoService;
import com.iyin.sign.system.service.SignSysSignatureLogService;
import com.iyin.sign.system.service.SignSysUserInfoService;
import com.iyin.sign.system.vo.req.EnterprsieListReqVO;
import com.iyin.sign.system.vo.req.SignLogListReqVO;
import com.iyin.sign.system.vo.req.UserListReqVO;
import com.iyin.sign.system.vo.req.*;
import com.iyin.sign.system.vo.resp.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: EnterpriseManagerController
 * @Description: 单位管理服务
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 14:35
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 14:35
 * @Version: 0.0.1
 */
@RestController
@RequestMapping("/enterprise")
@Api(tags = "单位管理")
@Slf4j
public class SignSysEnterpriseManagerController {

    @Autowired
    SignSysEnterpriseInfoService signSysEnterpriseInfoService;

    @Autowired
    SignSysUserInfoService signSysUserInfoService;

    @Autowired
    SignSysSignatureLogService sysSignatureLogService;

    @Autowired
    SignSysNodeInfoService signSysNodeInfoService;


    @ApiOperation("单位管理-新建单位-1")
    @PostMapping("/create/step/one")
    public IyinResult<String> createEnterpriseStepOne(@RequestBody @Valid CreateEnterpriseStepOneReqVO reqVO){
         return  signSysEnterpriseInfoService.createEnterpriseStepOne(reqVO);
    }

    @ApiOperation("单位管理-新建单位-2")
    @PostMapping("/create/step/two")
    public IyinResult<Boolean> createEnterpriseStepTwo(@RequestBody @Valid CreateEnterpriseStepTwoReqVO reqVO){
        return  signSysEnterpriseInfoService.createEnterpriseStepTwo(reqVO);
    }


    @GetMapping(value = "/downloadExeclSamples")
    @ApiOperation("批量导入单位数据表格模板下载")
    public IyinResult<Boolean> downloadExeclSamples(HttpServletResponse response){
        return  signSysEnterpriseInfoService.downloadExeclSamples(response);
    }

    @PostMapping(value = "/enterprsieBatchImport", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("批量导入单位数据")
    public IyinResult<EnterprsieBatchImportRespVO> enterprsieBatchImport(@RequestParam(value = "file") MultipartFile enterpriseFile, HttpServletResponse response){
        return  signSysEnterpriseInfoService.enterprsieBatchImport(enterpriseFile,response);
    }

    @GetMapping(value = "/downloadBatchImportResult/{importId}")
    @ApiOperation("下载批量导入的结果")
    public IyinResult<Boolean> downloadBatchImportResult(@PathVariable(value = "importId")String importId, HttpServletResponse response){
        return  signSysEnterpriseInfoService.downloadBatchImportResult(importId,response);
    }

    @ApiOperation("查询所有的单位列表")
    @PostMapping("/pageListEnterprise")
    public IyinResult<IyinPage<EnterprsieDetailRespVO>> pageListEnterprise(@RequestBody @Valid EnterprsieListReqVO reqVO,HttpServletRequest request){
        return signSysEnterpriseInfoService.pageListEnterprise(reqVO,request);
    }

    @ApiOperation("查看单位详情")
    @GetMapping("/getEnterpriseDetail/{id}")
    public IyinResult<EnterprsieDetailRespVO> getEnterpriseDetail(@PathVariable(value = "id")String id){
        return signSysEnterpriseInfoService.getEnterpriseDetail(id);
    }

    @ApiOperation("修改单位信息")
    @PostMapping("/updateEnterprise")
    public IyinResult<Boolean> updateEnterprise(@RequestBody @Valid UpdateEnterpriseReqVO reqVO){
        return signSysEnterpriseInfoService.updateEnterprise(reqVO);
    }

    @ApiOperation("查询所选节点下的用户列表")
    @PostMapping("/pageListUserInfoByNodeId")
    public IyinResult<IyinPage<UserDetailRespVO>> pageListUserInfoByNodeId(@RequestBody @Valid UserListReqVO reqVO){
       return signSysUserInfoService.pageListUserInfoByNodeId(reqVO);
    }

    /**
     * 查询所选节点下的用户列表
     *
     * @Author: yml
     * @CreateDate: 2019/9/2
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/2
     * @Version: 0.0.1
     * @param reqVO
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.vo.resp.UserDetailRespVO>>
     */
    @ApiOperation("查询所选节点下的用户列表")
    @PostMapping("/pageListUserInfoByNodeId2")
    public IyinResult<IyinPage<UserDetailRespVO>> pageListUserInfoByNodeId2(@RequestBody @Valid UserListReqVO reqVO){
       return signSysUserInfoService.pageListUserInfoByNodeId2(reqVO);
    }

    @ApiOperation("查询单位下的所有成员列表")
    @GetMapping("/getAllUserInfoByEnterpriseId/{enterpriseId}")
    public IyinResult<List<UserDetailRespVO>> getAllUserInfoByEnterpriseId(@PathVariable(value = "enterpriseId")String enterpriseId){
        return signSysUserInfoService.getAllUserInfoByEnterpriseId(enterpriseId);
    }


    @ApiOperation("添加成员")
    @PostMapping("/addUser")
    public IyinResult<Boolean> addUser(@RequestBody @Valid AddUserReqVO reqVO){
        return signSysUserInfoService.aaddUser(reqVO);
    }

    @ApiOperation("查看用户详情")
    @GetMapping("/getUserDetail/{id}")
    public IyinResult<UserDetailRespVO> getUserDetail(@PathVariable(value = "id")String id){
        return signSysUserInfoService.getUserDetail(id);
    }

    @ApiOperation("编辑用户信息")
    @PostMapping("/updateUser")
    public IyinResult<Boolean> updateUser(@RequestBody @Valid UpdateUserReqVO reqVO){
        return signSysUserInfoService.updateUser(reqVO);
    }

    @ApiOperation("禁用/启用用户")
    @PostMapping("/forbidUser")
    public IyinResult<Boolean> forbidUser(@RequestBody @Valid ForbidUserReqVO reqVO){
        return  signSysUserInfoService.forbidUser(reqVO);
    }

    @ApiOperation("删除用户")
    @PostMapping("/delUser")
    public IyinResult<Boolean> delUser(@RequestBody @Valid DelUserReqVO reqVO){
        return  signSysUserInfoService.delUser(reqVO);
    }

    @ApiOperation("禁用/启用单位")
    @PostMapping("/forbidEnterprise")
    public IyinResult<Boolean> forbidEnterprise(@RequestBody @Valid ForbidEnterpriseReqVO reqVO){
        return  signSysUserInfoService.forbidEnterprise(reqVO);
    }

    @ApiOperation("查询单位下的签署日志")
    @PostMapping("/pageListSignLog")
    public IyinResult<IyinPage<EnterpriseSignLogRespVO>> pageListSignLog(@RequestBody @Valid SignLogListReqVO reqVO){
        return sysSignatureLogService.pageListSignLog(reqVO);
    }

    @ApiOperation("查询用户的签署日志")
    @PostMapping("/userPageListSignLog")
    public IyinResult<IyinPage<EnterpriseSignLogRespVO>> userPageListSignLog(@RequestBody @Valid UserSignLogListReqVO reqVO){
        return sysSignatureLogService.userPageListSignLog(reqVO);
    }

    @ApiOperation("获取组织结构树")
    @GetMapping("/getNodeTree/{enterpriseId}")
    public IyinResult<NodeTreeRespVO>  getNodeTree(@PathVariable(value = "enterpriseId") String enterpriseId){
        return signSysNodeInfoService.getNodeTree(enterpriseId);
    }

    @ApiOperation("添加组织节点")
    @PostMapping("/addNode")
    public IyinResult<String> addNode(@RequestBody @Valid SaveNodeReqVO saveNodeReqVO){
        return signSysNodeInfoService.addNode(saveNodeReqVO);
    }

    @ApiOperation("编辑节点")
    @PostMapping("/updateNode")
    public IyinResult<Boolean> updateNode(@RequestBody @Valid UpdateNodeReqVO updateNodeReqVO){
        return signSysNodeInfoService.updateNode(updateNodeReqVO);
    }

    @ApiOperation(("删除节点"))
    @GetMapping("/delNode/{nodeId}")
    public IyinResult<Boolean> delNode(@PathVariable(value = "nodeId")String nodeId){
        return signSysNodeInfoService.delNode(nodeId);
    }

    @GetMapping(value = "/downloadUserExeclSamples")
    @ApiOperation("批量导入用户数据表格模板下载")
    public IyinResult<Boolean> downloadUserExeclSamples(HttpServletResponse response){
        return  signSysEnterpriseInfoService.downloadUserExeclSamples(response);
    }

    @PostMapping(value = "/userBatchImport", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("批量导入用户数据")
    public IyinResult<EnterprsieBatchImportRespVO> userBatchImport(@RequestParam(value = "file") MultipartFile userFile,@RequestParam(value = "nodeId")String nodeId, HttpServletResponse response){
        return  signSysEnterpriseInfoService.userBatchImport(userFile,nodeId,response);
    }

    @GetMapping(value = "/downloadUserBatchImportResult/{importId}")
    @ApiOperation("下载批量导入用户的结果")
    public IyinResult<Boolean> downloadUserBatchImportResult(@PathVariable(value = "importId")String importId, HttpServletResponse response){
        return  signSysEnterpriseInfoService.downloadUserBatchImportResult(importId,response);
    }




}
