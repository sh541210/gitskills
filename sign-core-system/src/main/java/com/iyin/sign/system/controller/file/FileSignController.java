package com.iyin.sign.system.controller.file;

import com.alibaba.fastjson.JSONObject;
import com.iyin.sign.system.common.SwaggerConstant;
import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.service.IFileFastSignService;
import com.iyin.sign.system.vo.sign.req.FastFilePerforationSignHalfReqVO;
import com.iyin.sign.system.vo.sign.req.FastFilePerforationSignReqVO;
import com.iyin.sign.system.vo.sign.req.SingleFileFastSignMoreReqVO;
import com.iyin.sign.system.vo.sign.req.SingleFileFastSignReqVO;
import com.iyin.sign.system.vo.sign.resp.FileSignRespDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @ClassName: FileSignController
 * @Description: 文件流输入签章
 * @Author: wdf
 * @CreateDate: 2019/6/26
 * @UpdateUser: wdf
 * @UpdateDate: 2019/6/26
 * @Version: 1.0.0
 */
@RestController
@Api(tags = "文件流快捷签署")
@RequestMapping("file/fast")
@Slf4j
public class FileSignController {

    @Autowired
    private IFileFastSignService fileFastSignService;

    /**
     * 单文件单页单签章
     *
     * @Author: wdf
     * @CreateDate: 2019/6/26
     * @UpdateUser: wdf
     * @UpdateDate: 2019/6/26
     * @Version: 0.0.1
     * @param signFile
     * @param sealFile
     * @param certFile
     * @param jsonStr
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.FileSignRespDTO>
     */
    @PostMapping(value = "/singleSign")
    @ApiOperation(
            value = "个人/企业快捷签-单文件单页单签章接口",
            tags = {SwaggerConstant.API_FAST_SIGN},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EsealResult<FileSignRespDTO> singleSign(@RequestParam(value = "signFile",required = false) MultipartFile signFile,
                                               @RequestParam(value = "sealFile",required = false) MultipartFile sealFile,
                                               @RequestParam(value = "certFile",required = false) MultipartFile certFile,
                                               @RequestParam("jsonStr") String jsonStr,
                                               HttpServletRequest request) {
        @Valid
        SingleFileFastSignReqVO singleFileFastSignReqVO = JSONObject.parseObject(jsonStr, SingleFileFastSignReqVO.class);
        return fileFastSignService.singleSign(signFile,sealFile,certFile,singleFileFastSignReqVO, request);
    }

    /**
     * 个人/企业快捷签-单文件多页坐标签章接口
     *
     * @Author: wdf
     * @CreateDate: 2019/6/26
     * @UpdateUser: wdf
     * @UpdateDate: 2019/6/26
     * @Version: 0.0.1
     * @param signFile
     * @param sealFile
     * @param certFile
     * @param jsonStr
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.FileSignRespDTO>
     */
    @PostMapping(value = "/singleCoordBatchSign")
    @ApiOperation(
            value = "个人/企业快捷签-单文件多页坐标签章接口",
            tags = {SwaggerConstant.API_FAST_SIGN},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EsealResult<FileSignRespDTO> singleCoordBatchSign(@RequestParam(value = "signFile",required = false) MultipartFile signFile,
                                               @RequestParam(value = "sealFile",required = false) MultipartFile sealFile,
                                               @RequestParam(value = "certFile",required = false) MultipartFile certFile,
                                               @RequestParam("jsonStr") String jsonStr,
                                               HttpServletRequest request) {
        @Valid
        SingleFileFastSignMoreReqVO singleFileFastSignMoreReqVO = JSONObject.parseObject(jsonStr, SingleFileFastSignMoreReqVO.class);
        return fileFastSignService.singleCoordBatchSign(signFile,sealFile,certFile,singleFileFastSignMoreReqVO, request);
    }

    /**
     * 个人/企业快捷签-单文件骑缝坐标签章接口
     *
     * @Author: wdf
     * @CreateDate: 2019/6/26
     * @UpdateUser: wdf
     * @UpdateDate: 2019/6/26
     * @Version: 0.0.1
     * @param signFile
     * @param sealFile
     * @param certFile
     * @param jsonStr
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.FileSignRespDTO>
     */
    @PostMapping(value = "/singlePerforationCoordSign")
    @ApiOperation(
            value = "个人/企业快捷签-单文件骑缝坐标签章接口",
            tags = {SwaggerConstant.API_FAST_SIGN},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EsealResult<FileSignRespDTO> singlePerforationCoordSign(@RequestParam(value = "signFile",required = false) MultipartFile signFile,
                                               @RequestParam(value = "sealFile",required = false) MultipartFile sealFile,
                                               @RequestParam(value = "certFile",required = false) MultipartFile certFile,
                                               @RequestParam("jsonStr") String jsonStr,
                                               HttpServletRequest request) {
        @Valid
        FastFilePerforationSignReqVO fastFilePerforationSignReqVO = JSONObject.parseObject(jsonStr, FastFilePerforationSignReqVO.class);
        return fileFastSignService.singlePerforationCoordSign(signFile,sealFile,certFile,fastFilePerforationSignReqVO, request);
    }

    /**
    	* @Description 个人/企业快捷签-单文件连页坐标签章接口
    	* @Author: wdf
        * @CreateDate: 2019/9/4 18:20
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/9/4 18:20
    	* @Version: 0.0.1
        * @param signFile, sealFile, certFile, jsonStr, request
        * @return com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.FileSignRespDTO>
        */
    @PostMapping(value = "/singlePerforationCoordHalfSign", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "个人/企业快捷签-单文件坐标连页签章接口 V1.2.1", tags = {SwaggerConstant.API_FAST_SIGN}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public EsealResult<FileSignRespDTO> singlePerforationCoordHalfSign(@RequestParam(value = "signFile",required = false) MultipartFile signFile,
                                                                   @RequestParam(value = "sealFile",required = false) MultipartFile sealFile,
                                                                   @RequestParam(value = "certFile",required = false) MultipartFile certFile,
                                                                   @RequestParam("jsonStr") String jsonStr,
                                                                   HttpServletRequest request) {
        @Valid
        FastFilePerforationSignHalfReqVO fastFilePerforationSignHalfReqVO = JSONObject.parseObject(jsonStr, FastFilePerforationSignHalfReqVO.class);
        return fileFastSignService.singlePerforationCoordHalfSign(signFile,sealFile,certFile,fastFilePerforationSignHalfReqVO, request);
    }

}
