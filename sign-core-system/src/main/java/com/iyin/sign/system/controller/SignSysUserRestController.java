package com.iyin.sign.system.controller;

import com.iyin.sign.system.entity.SignSysUserInfo;
import com.iyin.sign.system.mapper.SignSysUserRoleMapper;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.MenuTree;
import com.iyin.sign.system.model.RedisKeyConstant;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.ISignSysMenuService;
import com.iyin.sign.system.service.RedisService;
import com.iyin.sign.system.service.SignSysUserInfoService;
import com.iyin.sign.system.util.CaptchaUtil;
import com.iyin.sign.system.vo.req.UpdateUserInfoReqVO;
import com.iyin.sign.system.vo.req.UserNameLoginReqVO;
import com.iyin.sign.system.vo.resp.SignSysDefaultConfigInfoResp;
import com.iyin.sign.system.vo.resp.SignSysUserInfoRespVO;
import com.iyin.sign.system.vo.resp.UserLoginRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @ClassName: SignSysUserRestController
 * @Description: 签章系统前台用户相关服务
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 17:57
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 17:57
 * @Version: 0.0.1
 */
@Api(tags = "前台用户相关服务")
@RestController
@Slf4j
@RequestMapping("/user")
public class SignSysUserRestController {

    @Autowired
    SignSysUserInfoService signSysUserInfoService;

    @Autowired
    RedisService redisService;

    @Autowired
    ISignSysMenuService sysMenuService;

    @Autowired
    SignSysUserRoleMapper signSysUserRoleMapper;

    @ApiOperation("获取登录的验证码")
    @GetMapping("/getValicode")
    public IyinResult<Void> getValicode(HttpServletRequest request, HttpServletResponse response){
        String sessionId = request.getSession().getId();
        // 利用图片工具生成图片
        // 第一个参数是生成的验证码，第二个参数是生成的图片
        response.setHeader("Cache-Control","no-cache");
        Object[] objs = CaptchaUtil.createImage();
        //将验证码放入到redis
        String key= RedisKeyConstant.USER_LOGIN_VALICODE_KEY_PRE+sessionId;
        log.info("key={}， 验证码={}",key,objs[0].toString().toLowerCase());
        redisService.set(key,objs[0].toString().toLowerCase());
        //将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        response.setContentType("image/png");

        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
           throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1052);
        }
        return IyinResult.getIyinResult();
    }


    @PostMapping("/loginByUserNameAndPwd")
    @ApiOperation("用户名密码登录")
    public IyinResult<UserLoginRespVO> loginByUserNameAndPwd(@RequestBody @Valid UserNameLoginReqVO reqVO,HttpServletRequest request){
        return  signSysUserInfoService.loginByUserNameAndPwd(reqVO,request);
    }

    /**
     * 用户退出登录
     * @Author:      luwenxiong
     * @CreateDate:  2019/6/20
     * @UpdateUser:
     * @UpdateDate:  2019/6/20
     * @Version:     0.0.1
     * @return
     * @throws
     */
    @GetMapping("/loginOut/{id}")
    @ApiOperation("退出登录")
    public IyinResult<Boolean> loginOut(@PathVariable(value = "id") String id){
        return signSysUserInfoService.loginOut(id);
    }


    /**
     * 用户登录后获取菜单树结构
     *
     * @Author: luwenxiong
     * @CreateDate: 2019/8/20
     * @UpdateUser: luwenxiong
     * @UpdateDate: 2019/8/20
     * @Version: 0.0.1
     * @return : com.iyin.sign.system.model.IyinResult<java.util.List<com.iyin.sign.system.model.MenuTree>>
     */
    @GetMapping(value = "/getUserMenuTree/{userId}")
    @ApiOperation("用户登录后获取菜单树结构")
    public IyinResult<List<MenuTree>> getUserMenuTree(@PathVariable(value = "userId")String userId) {
        return  signSysUserInfoService.getUserMenuTree(userId);
    }

    @PostMapping("/updatePassword")
    @ApiOperation("前台我的账号-安全设置")
    public IyinResult<Boolean> updatePassword(@RequestBody @Valid UpdateUserInfoReqVO reqVO){

        String oldPassword = reqVO.getOldPassword();
        String userId = reqVO.getUserId();
        String newPassword = reqVO.getNewPassword();

        SignSysUserInfo signSysUserInfo = signSysUserInfoService.getById(userId);
        if(signSysUserInfo ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }

        if(!BCrypt.checkpw(oldPassword,signSysUserInfo.getPassword())){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1017);
        }

        int passwordMinLength = 6;
        if(newPassword.length()< passwordMinLength){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1016);
        }

        String encodePassword = BCrypt.hashpw(newPassword,BCrypt.gensalt());
        signSysUserInfo.setPassword(encodePassword);

        if(!signSysUserInfoService.updateById(signSysUserInfo)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1018);
        }
        return IyinResult.getIyinResult(true);

    }

    @ApiOperation("前台我的账号-基本设置")
    @GetMapping("/account/info/{id}")
    public IyinResult<SignSysUserInfoRespVO> queryAccountInfoById(@PathVariable(value = "id")String id){
        return  signSysUserInfoService.queryAccountInfoById(id);
    }

    @ApiOperation("获取后台配置的系统默认信息")
    @GetMapping("/getSysDefaultConfig")
    public IyinResult<SignSysDefaultConfigInfoResp> getSysDefaultConfig(){
        return  signSysUserInfoService.getSysDefaultConfig();
    }

    @ApiOperation("邮箱帐号绑定手机号")
    @GetMapping("/binding/phone")
    @ApiImplicitParam(name = "phone",paramType = "query",format = "^13")
    public IyinResult<Boolean> bindingPhone(@RequestParam("phone") @Length(max = 11) @ApiParam("手机号")
                                                    String phone, HttpServletRequest request){
        return  signSysUserInfoService.bindingPhone(phone,request);
    }
}
