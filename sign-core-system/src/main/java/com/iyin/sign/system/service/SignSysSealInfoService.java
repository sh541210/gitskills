package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysSealInfo;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.*;
import com.iyin.sign.system.vo.resp.SealDefinedUploadRespVO;
import com.iyin.sign.system.vo.resp.SealInfoRespVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: SignSysSealInfoService
 * @Description: 签章系统印章信息服务
 * @Author: wdf
 * @CreateDate: 2019/8/7
 * @UpdateUser: wdf
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
public interface SignSysSealInfoService extends IService<SignSysSealInfo>  {

    /**
     * 章莫处理 0:章模，1:手绘签名
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/5
     * @UpdateUser:
     * @UpdateDate:  2019/8/5
     * @Version:     0.0.1
     * @return
     * @throws
     */
    IyinResult<SealDefinedUploadRespVO> uploadSealDefinedFile(SealPictureUploadReqVO reqVO);

    IyinResult<Boolean> saveSealInfo(SaveCloudSealVO saveCloudSealVO);

    IyinResult<Boolean> updateSealInfo(UpdateCloudSealVO updateCloudSealVO);

    IyinResult<SealInfoRespVO> querySealInfo(String pictureCode);

    IyinResult<Boolean> deleteSealInfo(DeleteEnterpriseSealInfoReqVO reqVO);

    IyinResult<IyinPage<SealInfoRespVO>> pageUserListSealInfo(UserSealInfoListReqVO reqVO);

    IyinResult<Boolean> saveUkSealInfo(SaveUkSealReqVO reqVO, HttpServletRequest request);

    IyinResult<Boolean> updateUkSealInfo(UpdateUkSealReqVO reqVO);

    IyinResult<IyinPage<SealInfoRespVO>> pageListSealInfo(SealInfoListReqVO reqVO);

    IyinResult<SealDefinedUploadRespVO> definedCreateSeal(GeneratePicReqVO reqVO);
}
