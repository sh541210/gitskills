package com.iyin.sign.system.controller;

import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.SignSysSealUserService;
import com.iyin.sign.system.vo.req.BindUserSealReqVO;
import com.iyin.sign.system.vo.req.UnBindUserSealReqVO;
import com.iyin.sign.system.vo.resp.UserBaseInfoRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: SignSysSealUserManagerController
 * @Description: 印章权限管理相关
 * @Author: luwenxiong
 * @CreateDate: 2019/6/24 17:42
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/24 17:42
 * @Version: 0.0.1
 */
@RestController
@RequestMapping("/userSeal")
@Api(tags = "印章权限分配管理")
public class SignSysSealUserManagerController {

    @Autowired
    SignSysSealUserService signSysSealUserService;

    @ApiOperation("印章用户权限配置")
    @PostMapping("/bind")
    public IyinResult<Boolean> bindSealUserLink(@RequestBody @Valid BindUserSealReqVO reqVO) {
        return signSysSealUserService.settingSealUserLink(reqVO);
    }

    @ApiOperation("成员印章权限回收")
    @PostMapping("/unBind")
    public IyinResult<Boolean> unBindSealUserLink(@RequestBody @Valid UnBindUserSealReqVO reqVO) {
        return signSysSealUserService.unBindSealUserLink(reqVO);
    }

    @ApiOperation("查询拥有当前印章的所有用户")
    @GetMapping("/query/{sealId}")
    public IyinResult<List<UserBaseInfoRespVO>> queryListUserOfSeal(@PathVariable(value = "sealId") String sealId) {
        return signSysSealUserService.queryListUserOfSeal(sealId);
    }


}
