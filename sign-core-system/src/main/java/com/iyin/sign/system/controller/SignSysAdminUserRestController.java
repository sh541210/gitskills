package com.iyin.sign.system.controller;

import com.iyin.sign.system.entity.SignSysAdminUser;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.SignSysAdminUserService;
import com.iyin.sign.system.vo.req.AdminUserLoginReqVO;
import com.iyin.sign.system.vo.req.UpdateAdminInfoReqVO;
import com.iyin.sign.system.vo.resp.SignSysAdminUserBaseInfoRespVO;
import com.iyin.sign.system.vo.resp.SignSysAdminUserLoginRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: AdminUseRestController
 * @Description: 后台管理员相关服务
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 15:34
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 15:34
 * @Version: 0.0.1
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "后台管理员相关服务")
@Slf4j
public class SignSysAdminUserRestController {

    @Autowired
    SignSysAdminUserService signSysAdminUserService;

    /**
     * 后台管理员登录
     * @Author:      luwenxiong
     * @CreateDate:  2019/6/20
     * @UpdateUser:
     * @UpdateDate:  2019/6/20
     * @Version:     0.0.1
     * @return
     * @throws
     */
    @PostMapping("/login")
    @ApiOperation("签章系统后台管理员登录")
    public IyinResult<SignSysAdminUserLoginRespVO> loginByUserName(@RequestBody @Valid AdminUserLoginReqVO reqVO, HttpServletRequest request){
        return signSysAdminUserService.loginByUserName(reqVO,request);
    }

    /**
     * 后台管理员退出登录
     * @Author:      luwenxiong
     * @CreateDate:  2019/6/20
     * @UpdateUser:
     * @UpdateDate:  2019/6/20
     * @Version:     0.0.1
     * @return
     * @throws
     */
    @GetMapping("/loginOut/{id}")
    @ApiOperation("签章系统后台管理员退出")
    public IyinResult<Boolean> loginOut(@PathVariable(value = "id") String id){
        return signSysAdminUserService.loginOut(id);
    }

    @PostMapping("/updatePassword")
    @ApiOperation("后台我的账号-安全设置")
    public IyinResult<Boolean> updatePassword(@RequestBody @Valid UpdateAdminInfoReqVO reqVO){

        String oldPassword = reqVO.getOldPassword();
        String adminId = reqVO.getAdminId();
        String newPassword = reqVO.getNewPassword();

        SignSysAdminUser signSysAdminUser = signSysAdminUserService.getById(adminId);
        if(signSysAdminUser ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }

        if(!BCrypt.checkpw(oldPassword,signSysAdminUser.getPassword())){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1017);
        }

        int passwordMinLength = 6;
        if(newPassword.length()< passwordMinLength){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1016);
        }

        String encodePassword = BCrypt.hashpw(newPassword,BCrypt.gensalt());
        signSysAdminUser.setPassword(encodePassword);

       if(!signSysAdminUserService.updateById(signSysAdminUser)){
           throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1018);
       }
       return IyinResult.getIyinResult(true);

    }

    @ApiOperation("后台我的账号-基本设置")
    @GetMapping("/account/info/{id}")
    public IyinResult<SignSysAdminUserLoginRespVO> queryAccountInfoById(@PathVariable(value = "id")String id){
        return signSysAdminUserService.queryAccountInfoById(id);
    }

    @ApiOperation("查询所有的管理员列表(临时作为阳光保险查询签收人列表接口)")
    @GetMapping("/queryListAdminUser")
    public IyinResult<List<SignSysAdminUserBaseInfoRespVO>>  queryListAdminUser(){
        return signSysAdminUserService.queryListAdminUser();
    }

}
