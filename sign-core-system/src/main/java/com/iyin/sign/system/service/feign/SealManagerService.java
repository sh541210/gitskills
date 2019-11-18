package com.iyin.sign.system.service.feign;

import com.iyin.sign.system.config.FeignMultipartSupportConfig;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.vo.req.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
	* 白鹤印章管理对接
     * x-user-id：同步用户的时候会返回这个用户的id字段
     * x-token：拿到用户的id调用获取登录令牌接口得到token
     * x-company-id：调用获取用户公司列表接口/company/usercompany，用获取到的userid和token设置请求头，得到公司id
 *
 * 所有需要文件的地方，先调用文件上传接口将文件上传一下，得到fileName
 *
	* @Author: wdf 
    * @CreateDate: 2019/10/9 17:56
	* @UpdateUser: wdf
    * @UpdateDate: 2019/10/9 17:56
	* @Version: 0.0.1
    * @param 
    * @return 
    */
@FeignClient(name="baihe-service", url = "${private-baihe.url}",configuration = FeignMultipartSupportConfig.class)
public interface SealManagerService {

    /**
    	* 获取公司印章列表
     *
     * applyPdf需要上传申请盖的文件，接口会返回fileName
     * applyUser就是当前登录的用户id
     * fileType就是文件类型，可以是合同、标书，内部、其他，fileType就直接填合同
     * imgList:就是要盖章的文件拍照图片
        *
    	* @Author: wdf 
        * @CreateDate: 2019/10/10 9:11
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/10 9:11
    	* @Version: 0.0.1
        * @param
        * @return java.util.Map<java.lang.String,java.lang.Object>
        */
    @ApiOperation("获取公司印章列表")
    @GetMapping("/seal/companyseallist")
    Map<String,Object> sealList(@RequestHeader(value = "x-token") String token,
                                @RequestHeader(value = "x-company-id") String companyId,
                                @RequestHeader(value = "x-user-id") String userId,
                                @RequestParam(value = "enableFlag",required = false) Boolean enableFlag
                                );

    /**
    	*  获取印章详情
     *  type=1，代表是按照印章id查询
     * type!=1，代表是安卓印章mac地址查询
     *
     * applyPdf需要上传申请盖的文件，接口会返回fileName
     * applyUser就是当前登录的用户id
     * fileType就是文件类型，可以是合同、标书，内部、其他
     * imgList:就是要盖章的文件拍照图片
    	* @Author: wdf 
        * @CreateDate: 2019/10/23 16:14
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/23 16:14
    	* @Version: 0.0.1
        * @param token, companyId, userId, sealIdOrMac, type
        * @return java.util.Map<java.lang.String,java.lang.Object>
        */
    @ApiOperation("获取印章详情")
    @GetMapping("/seal/sealinfo")
    Map<String,Object> sealInfo(@RequestHeader(value = "x-token") String token,
                                @RequestHeader(value = "x-company-id") String companyId,
                                @RequestHeader(value = "x-user-id") String userId,
                                @RequestParam(value = "sealIdOrMac") String sealIdOrMac,
                                @RequestParam(value = "type") String type

                                );

    /**
    	* 获取登录令牌
    	* @Author: wdf
        * @CreateDate: 2019/10/22 18:58
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/22 18:58
    	* @Version: 0.0.1
        * @param appKey, secret, userId
        * @return java.util.Map<java.lang.String,java.lang.Object>
        */
    @ApiOperation("获取登录令牌")
    @GetMapping("/integrate/accesstoken")
    Map<String,Object> accessToken(@RequestHeader(value = "x-app-key") String appKey,
                                @RequestHeader(value = "x-secret") String secret,
                                @RequestParam(value = "userId") String userId);

    /**
    	* 保存印章
    	* @Author: wdf
        * @CreateDate: 2019/10/10 9:10
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/10 9:10
    	* @Version: 0.0.1
        * @param sealInfoReqVO
        * @return java.util.Map<java.lang.String,java.lang.Object>
        */
    @ApiOperation("保存印章")
    @PostMapping(value = "/seal/save")
    Map<String,Object> sealSave(@RequestHeader(value = "x-app-key") String appKey,
                                @RequestHeader(value = "x-secret") String secret,
                                @RequestBody @Valid SealInfoReqVO sealInfoReqVO);

    /**
     * 用印申请
     *
     * @Author: yml
     * @CreateDate: 2019/10/9
     * @UpdateUser: yml
     * @UpdateDate: 2019/10/9
     * @Version: 0.0.1
     * @param userId, companyId, token,uploadReqVO
     * @return : java.util.Map<java.lang.String,java.lang.Object>
     */
    @ApiOperation("上传申请单据")
    @PostMapping(value = "/apply/adduseseal")
    Map<String,Object> upload(@RequestHeader(value = "x-user-id") String userId,
                              @RequestHeader(value = "x-company-id") String companyId,
                              @RequestHeader(value = "x-token") String token,
                              @RequestBody @Valid UploadReqVO uploadReqVO);

    /**
     * 上传申请单拍照图片或文件
     *
     * @param userId
     * @param companyId
     * @param token
     * @param file
     * @param category
     * @return : java.util.Map<java.lang.String,java.lang.Object>
     * @Author: yml
     * @CreateDate: 2019/10/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/10/23
     * @Version: 0.0.1
     */
    @ApiOperation("用印申请文件上传")
    @PostMapping(value = "/file/uploadimage")
    @ApiImplicitParam(name = "category", paramType = "query", dataType = "Integer", value = "category 值含义：1 头像\n" + "2 签名\n" +
            "3 印模\n" + "4 申请文件，申请图片，盖章后文件\n" + "5 盖章拍照图片")
    Map<String, Object> uploadimage(
            @RequestHeader(value = "x-user-id") String userId,
            @RequestHeader(value = "x-company-id") String companyId,
            @RequestHeader(value = "x-token") String token,
            @RequestPart("file") MultipartFile file, @RequestParam("category") Integer category);

    /**
    	* 盖章记录查询
        *
    	* @Author: wdf 
        * @CreateDate: 2019/10/17 10:51
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/17 10:51
    	* @Version: 0.0.1
        * @param userId, companyId,token, useSealStampRecordList
        * @return java.util.Map<java.lang.String,java.lang.Object>
        */
    @ApiOperation("盖章记录查询")
    @GetMapping("/stamp/stamprecordlist")
    Map<String,Object> stampRecordList(@RequestHeader(value = "x-user-id") String userId,
                                  @RequestHeader(value = "x-company-id") String companyId,
                                  @RequestHeader(value = "x-token") String token,
                                  @RequestBody @Valid UseSealStampRecordList useSealStampRecordList);


    /**
    	* 盖章单据列表
    	* @Author: wdf 
        * @CreateDate: 2019/10/25 10:17
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/25 10:17
    	* @Version: 0.0.1
        * @param userId, companyId, token, stampRecordApplyListReqVO
        * @return java.util.Map<java.lang.String,java.lang.Object>
        */
    @ApiOperation("盖章单据列表")
    @GetMapping("/apply/stamprecordapplylist")
    Map<String,Object> stampRecordApplyList(@RequestHeader(value = "x-user-id") String userId,
                                  @RequestHeader(value = "x-company-id") String companyId,
                                  @RequestHeader(value = "x-token") String token,
                                  @RequestBody @Valid StampRecordApplyListReqVO stampRecordApplyListReqVO);


    /**
    	* 获取单据盖章记录
    	* @Author: wdf 
        * @CreateDate: 2019/10/25 12:13
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/25 12:13
    	* @Version: 0.0.1
        * @param userId, companyId, token, useSealStampRecordApplyListReqVO
        * @return java.util.Map<java.lang.String,java.lang.Object>
        */
    @ApiOperation("获取单据盖章记录")
    @GetMapping("/stamp/applystamprecordlist")
    Map<String,Object> applyStampRecordList(@RequestHeader(value = "x-user-id") String userId,
                                       @RequestHeader(value = "x-company-id") String companyId,
                                       @RequestHeader(value = "x-token") String token,
                                       @RequestBody @Valid UseSealStampRecordApplyListReqVO useSealStampRecordApplyListReqVO);

    /**
    	* 同步用户列表数据
    	* @Author: wdf
        * @CreateDate: 2019/10/16 10:25
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/16 10:25
    	* @Version: 0.0.1
        * @param sycnUserReqVOS
        * @return java.util.Map<java.lang.String,java.lang.Object>
        */
    @ApiOperation("同步用户信息")
    @PostMapping(value = "/integrate/syncuserlist")
    Map<String,Object> syncUserList(@RequestHeader(value = "x-app-key") String appKey,
                                    @RequestHeader(value = "x-secret") String secret,
                                    @RequestParam(value = "mobilePhone") String mobilePhone,
                                    @RequestBody @Valid List<SycnUserReqVO> sycnUserReqVOS);

    /**
    	* 下载申请单图片
    	* @Author: wdf 
        * @CreateDate: 2019/10/17 14:20
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/17 14:20
    	* @Version: 0.0.1
        * @param category, fileName, companyId
        * @return com.iyin.sign.system.model.InMemoryMultipartFile
        */
    @ApiOperation("下载申请单图片")
    @GetMapping("/file/downloadimage")
    @ApiImplicitParam(name = "category", paramType = "query", dataType = "Integer", value = "category 值含义：1 头像\n" + "2 签名\n" +
            "3 印模\n" + "4 申请文件，申请图片，盖章后文件\n" + "5 盖章拍照图片")
    InMemoryMultipartFile fetchImage(@RequestParam(value = "category") Integer category,
                           @RequestParam(value = "fileName") String fileName,
                           @RequestParam(value = "companyId") String companyId
    );

}
