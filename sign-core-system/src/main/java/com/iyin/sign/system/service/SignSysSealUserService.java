package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysSealUser;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.BindUserSealReqVO;
import com.iyin.sign.system.vo.req.UnBindUserSealReqVO;
import com.iyin.sign.system.vo.resp.UserBaseInfoRespVO;

import java.util.List;

/**
 * @ClassName: SignSysSealUserService
 * @Description: 设置印章权限相关接口
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 11:18
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 11:18
 * @Version: 0.0.1
 */
public interface SignSysSealUserService extends IService<SignSysSealUser> {
    IyinResult<Boolean> settingSealUserLink(BindUserSealReqVO reqVO);

    IyinResult<List<UserBaseInfoRespVO>> queryListUserOfSeal(String sealId);

    IyinResult<Boolean> unBindSealUserLink(UnBindUserSealReqVO reqVO);
}
