package com.iyin.sign.system.controller;

import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.SignSysSealInfoService;
import com.iyin.sign.system.vo.req.*;
import com.iyin.sign.system.vo.resp.SealDefinedUploadRespVO;
import com.iyin.sign.system.vo.resp.SealInfoRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @ClassName: SignSysSealManagerController
 * @Description: 签章系统印章管理
 * @Author: luwenxiong
 * @CreateDate: 2019/6/22 16:23
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/22 16:23
 * @Version: 0.0.1
 */
@RestController
@RequestMapping("/seal")
@Api(tags = "签章系统印章管理")
public class SignSysSealManagerController {

    @Autowired
    SignSysSealInfoService signSysSealInfoService;

    /**
     * 上传云印章,保持临时章模待提交
     * @return EsealResult
     * @Author: luwenxiong
     * @CreateDate: 2019/6/22 下午 2:14
     * @UpdateUser: luwenxiong
     * @UpdateDate: 2019/6/22 下午 2:14
     * @Version: 0.0.1
     * @param  reqVO
     */
    @PostMapping(value = "/defined/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("上传云印章,保持临时章模待提交")
    public IyinResult<SealDefinedUploadRespVO> uploadSealDefinedFile(@Valid SealPictureUploadReqVO reqVO) {
        return signSysSealInfoService.uploadSealDefinedFile(reqVO);
    }

    @PostMapping(value = "/defined/create")
    @ApiOperation("预览时生成自定义的云印章")
    public IyinResult<SealDefinedUploadRespVO> definedCreateSeal(@RequestBody @Valid GeneratePicReqVO reqVO) {
        return signSysSealInfoService.definedCreateSeal(reqVO);
    }

    @PostMapping("/save")
    @ApiOperation("保存云印章")
    public IyinResult<Boolean> saveSealInfo(@RequestBody @Valid SaveCloudSealVO saveCloudSealVO) {
        return signSysSealInfoService.saveSealInfo(saveCloudSealVO);
    }

    @GetMapping("/query/{pictureCode}")
    @ApiOperation("查看印章详情")
    public IyinResult<SealInfoRespVO> querySealInfo(@PathVariable(value = "pictureCode") String pictureCode) {
        return signSysSealInfoService.querySealInfo(pictureCode);
    }


    @PostMapping("/update")
    @ApiOperation("编辑云印章")
    public IyinResult<Boolean> updateSealInfo(@RequestBody @Valid UpdateCloudSealVO updateCloudSealVO) {
        return signSysSealInfoService.updateSealInfo(updateCloudSealVO);
    }

    @ApiOperation("保存UK印章")
    @PostMapping("/uk/save")
    public IyinResult<Boolean> saveUkSealInfo(@RequestBody @Valid SaveUkSealReqVO reqVO, HttpServletRequest request) {
        return signSysSealInfoService.saveUkSealInfo(reqVO,request);
    }

    @ApiOperation("编辑ukey印章")
    @PostMapping("/uk/update")
    public IyinResult<Boolean> updateUkSealInfo(@RequestBody @Valid UpdateUkSealReqVO reqVO) {
        return signSysSealInfoService.updateUkSealInfo(reqVO);
    }

    @ApiOperation("删除单位下的印章")
    @PostMapping("/delete")
    public IyinResult<Boolean> deleteSealInfo(@RequestBody @Valid DeleteEnterpriseSealInfoReqVO reqVO) {
        return signSysSealInfoService.deleteSealInfo(reqVO);
    }

    @ApiOperation("文件签署时查看印章列表(根据数据权限显示)")
    @PostMapping("/pageUserListSealInfo")
    public IyinResult<IyinPage<SealInfoRespVO>> pageUserListSealInfo(@RequestBody @Valid UserSealInfoListReqVO reqVO) {
        return signSysSealInfoService.pageUserListSealInfo(reqVO);
    }

    @ApiOperation("查询单位下的全部印章")
    @PostMapping("/pageListSealInfo")
    public IyinResult<IyinPage<SealInfoRespVO>> pageListSealInfo(@RequestBody @Valid SealInfoListReqVO reqVO) {
        return signSysSealInfoService.pageListSealInfo(reqVO);
    }


}
