package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysAdminUser;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.AdminUserLoginReqVO;
import com.iyin.sign.system.vo.resp.SignSysAdminUserBaseInfoRespVO;
import com.iyin.sign.system.vo.resp.SignSysAdminUserLoginRespVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName: SignSysAdminUserService
 * @Description: 后台管理员service
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 15:31
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 15:31
 * @Version: 0.0.1
 */
public interface SignSysAdminUserService extends IService<SignSysAdminUser> {

    IyinResult<SignSysAdminUserLoginRespVO> loginByUserName(AdminUserLoginReqVO reqVO, HttpServletRequest request);

    IyinResult<Boolean> loginOut(String id);

    IyinResult<SignSysAdminUserLoginRespVO> queryAccountInfoById(String id);

    IyinResult<List<SignSysAdminUserBaseInfoRespVO>> queryListAdminUser();

    /**
     * 验证是否为超级管理员
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    Boolean checkSuperAdmin(String userId);
}
