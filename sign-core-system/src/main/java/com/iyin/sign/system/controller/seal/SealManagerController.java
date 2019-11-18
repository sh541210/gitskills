package com.iyin.sign.system.controller.seal;

import com.iyin.sign.system.dto.req.SignSysUseSealDTO;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.FileResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.ISealManagerService;
import com.iyin.sign.system.vo.req.*;
import com.iyin.sign.system.vo.resp.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName SealManagerController
 * SealManagerController
 * @Author wdf
 * @Date 2019/10/9 17:04
 * @throws
 * @Version 1.0
 **/
@RestController
@Api(tags = "白鹤印章管理")
@RequestMapping("/sealManager")
@Slf4j
public class SealManagerController {
    @Autowired
    private ISealManagerService sealManagerService;

    private static final String OUT_FAIL="file output fail: ";
    private static final String CONTENT_DIS ="Content-Disposition";
    private static final String INLINE = "inline;filename=%s";

    /**
    	* 获取文件打印列表
    	* @Author: wdf 
        * @CreateDate: 2019/10/10 16:52
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/10 16:52
    	* @Version: 0.0.1
        * @param
        * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.resp.SealListRespVO>
        */
    @GetMapping("/getFilePrintInfoList")
    @ApiOperation("v1.2.2_白鹤_打印文件列表")
    @ApiImplicitParam(name = "applyId",paramType = "query",value ="applyId 申请单据id" ,required = true,dataType = "String")
    public IyinResult<List<SealFileInfoListRespVO>> getFilePrintInfoList(@RequestParam String applyId) {
        // 获取印章列表
        return IyinResult.success(sealManagerService.getFilePrintInfoList(applyId));
    }

    /**
    	* 用印申请添加接口
    	* @Author: wdf
        * @CreateDate: 2019/10/10 19:10
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/10 19:10
    	* @Version: 0.0.1
        * @param addUseSealReqVO, request
        * @return com.iyin.sign.system.model.IyinResult<java.lang.Integer>
        */
    @ApiOperation("v1.2.2_白鹤_用印申请添加接口")
    @PostMapping("/addUseSeal")
    public IyinResult<Integer> addUseSeal(@RequestBody @Valid AddUseSealReqVO addUseSealReqVO, HttpServletRequest request) {
        return IyinResult.success(sealManagerService.addUseSeal(addUseSealReqVO,request));
    }

    /**
     * 获取印章列表
     * @Author: wdf
     * @CreateDate: 2019/10/9 17:44
     * @UpdateUser: wdf
     * @UpdateDate: 2019/10/9 17:44
     * @Version: 0.0.1
     * @param
     * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.resp.SealListRespVO>
     */
    @GetMapping("/getSealList")
    @ApiOperation("v1.2.2_白鹤_印章列表")
    public IyinResult<SealListRespVO> getSealList(@ApiParam(value = "是否过期，true表示过滤过期数据")
                                                      @RequestParam(value = "hasExpired",required = false,defaultValue = "true") Boolean hasExpired,
                                                  HttpServletRequest request) {
        // 获取印章列表
        return IyinResult.success(sealManagerService.sealList(hasExpired,true,request));
    }

    /**
    	* 白鹤文件打印
        *
    	* @Author: wdf 
        * @CreateDate: 2019/10/10 14:48
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/10 14:48
    	* @Version: 0.0.1
        * @param fileCodes, request, response
        * @return void
        */
    @ApiOperation(value="v1.2.2_白鹤 文件打印")
    @PostMapping(value="/fetch")
    public void fetch(@RequestParam("fileCodes") String fileCodes, HttpServletRequest request, HttpServletResponse response) {
        InMemoryMultipartFile file = sealManagerService.addFilePrint(fileCodes,request);
        response.setContentType(file.getContentType());
        response.setHeader(CONTENT_DIS, String.format(INLINE, file.getOriginalFilename()));
        try {
            response.getOutputStream().write(file.getBytes());
        } catch (IOException e) {
            log.error(OUT_FAIL, e.getMessage());
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    /**
    	* 申请用印记录
    	* @Author: wdf 
        * @CreateDate: 2019/10/10 15:18
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/10 15:18
    	* @Version: 0.0.1
        * @param currPage, pageSize, sealId, request
        * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.dto.req.SignSysUseSealDTO>>
        */
    @ApiOperation("v1.2.2_白鹤 申请用印记录")
    @GetMapping("/getUseSealList")
    public IyinResult<IyinPage<SignSysUseSealDTO>> getUseSealList(@RequestParam Integer currPage, @RequestParam Integer pageSize,
                                                                  @RequestParam(required = false) String sealId,
                                                                  @RequestParam(required = false) String applyCause, HttpServletRequest request) {
        return sealManagerService.getUseSealList(currPage,pageSize,sealId,applyCause,request);
    }

    /**
    	* 删除申请用印记录
    	* @Author: wdf
        * @CreateDate: 2019/10/10 15:33
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/10 15:33
    	* @Version: 0.0.1
        * @param applyId, request
        * @return com.iyin.sign.system.model.IyinResult
        */
    @ApiOperation("v1.2.2_白鹤 删除申请用印记录")
    @DeleteMapping("/deleteUseSeal")
    @ApiImplicitParam(name = "applyId",paramType = "query",value ="applyId 申请单据id" ,required = true,dataType = "String")
    public IyinResult<Integer> deleteUseSeal(@RequestParam String applyId, HttpServletRequest request) {
        return IyinResult.success(sealManagerService.deleteUseSeal(applyId,request));
    }

    /**
    	* 同步用户记录
    	* @Author: wdf 
        * @CreateDate: 2019/10/16 11:31
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/16 11:31
    	* @Version: 0.0.1
        * @param sycnUserInfoListReqVO, request
        * @return com.iyin.sign.system.model.IyinResult<java.lang.Integer>
        */
    @ApiOperation("v1.2.2_白鹤 同步用户记录")
    @PostMapping("/syncUserList")
    public IyinResult<Integer> syncUserList(@RequestBody @Valid SycnUserInfoListReqVO sycnUserInfoListReqVO, HttpServletRequest request) {
        return sealManagerService.syncUserList(sycnUserInfoListReqVO);
    }


    /**
    	* 白鹤文件下载
    	* @Author: wdf 
        * @CreateDate: 2019/10/10 16:17
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/10 16:17
    	* @Version: 0.0.1
        * @param applyId, request, response
        * @return void
        */
    @ApiOperation(value="v1.2.2_白鹤 文件下载")
    @PostMapping(value="/down")
    @ApiImplicitParam(name = "applyId",paramType = "query",value ="applyId 申请单据id" ,required = true,dataType = "String")
    public void down(@RequestParam String applyId, HttpServletRequest request, HttpServletResponse response) {
        InMemoryMultipartFile file = sealManagerService.addFileDownZip(applyId,request);
        response.setContentType(file.getContentType());
        response.setHeader(CONTENT_DIS, String.format(INLINE, file.getOriginalFilename()));
        try {
            response.getOutputStream().write(file.getBytes());
        } catch (IOException e) {
            log.error(OUT_FAIL, e.getMessage());
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    /**
    	* 获取图片文件
    	* @Author: wdf 
        * @CreateDate: 2019/10/17 15:10
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/17 15:10
    	* @Version: 0.0.1
        * @param category,fileName, request, response
        * @return void
        */
    @ApiOperation(value="v1.2.2_白鹤 获取图片文件")
    @PostMapping(value="/fetchImage")
    @ApiImplicitParam(name = "category", paramType = "query", dataType = "Integer",required = true,value = "category 值含义：1 头像\n" + "2 签名\n" +
            "3 印模\n" + "4 申请文件，申请图片，盖章后文件\n" + "5 盖章拍照图片")
    public void fetchImage(@RequestParam Integer category,@RequestParam String fileName, HttpServletRequest request, HttpServletResponse response) {
        InMemoryMultipartFile file = sealManagerService.fetchImage(category,fileName,request);
        response.setContentType("image/jpeg");
        response.setHeader(CONTENT_DIS, String.format(INLINE, file.getOriginalFilename()));
        try {
            response.getOutputStream().write(file.getBytes());
        } catch (IOException e) {
            log.error(OUT_FAIL, e.getMessage());
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
    }

    /**
    	* 盖章记录
    	* @Author: wdf 
        * @CreateDate: 2019/10/24 14:50
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/24 14:50
    	* @Version: 0.0.1
        * @param useSealStampRecordList, request
        * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.resp.StampRecordResultRespVO>
        */
    @ApiOperation(value="v1.2.2_白鹤 盖章记录")
    @PostMapping(value="/stampRecordList")
    @ApiIgnore
    public IyinResult<StampRecordResultRespVO> stampRecordList(@RequestBody @Valid UseSealStampRecordList useSealStampRecordList, HttpServletRequest request){
        return IyinResult.getIyinResult(sealManagerService.stampRecordList(useSealStampRecordList,request));
    }

    /**
    	* 白鹤申请单据列表
    	* @Author: wdf 
        * @CreateDate: 2019/10/25 10:25
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/25 10:25
    	* @Version: 0.0.1
        * @param stampRecordApplyListReqVO, request
        * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.resp.StampRecordApplyResultRespVO>
        */
    @ApiOperation(value="v1.2.2_白鹤 申请单据列表")
    @PostMapping(value="/stampRecordApplyList")
    public IyinResult<StampRecordApplyResultRespVO> stampRecordApplyList(@RequestBody @Valid StampRecordApplyListReqVO stampRecordApplyListReqVO, HttpServletRequest request){
        return IyinResult.getIyinResult(sealManagerService.stampRecordApplyList(stampRecordApplyListReqVO,request));
    }

    /**
    	* 获取单据盖章记录
    	* @Author: wdf 
        * @CreateDate: 2019/10/25 14:23
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/25 14:23
    	* @Version: 0.0.1
        * @param useSealStampRecordApplyListReqVO, request
        * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.resp.SealRecordListRespVO>
        */
    @ApiOperation(value="v1.2.2_白鹤 获取单据盖章记录")
    @PostMapping(value="/applyStampRecordList")
    public IyinResult<SealRecordListRespVO> applyStampRecordList(@RequestBody @Valid UseSealStampRecordApplyListReqVO useSealStampRecordApplyListReqVO, HttpServletRequest request){
        return IyinResult.getIyinResult(sealManagerService.applyStampRecordList(useSealStampRecordApplyListReqVO,request));
    }
}
