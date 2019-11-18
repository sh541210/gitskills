package com.iyin.sign.system.controller.sign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iyin.sign.system.common.BaseController;
import com.iyin.sign.system.common.SwaggerConstant;
import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.service.IFastSignService;
import com.iyin.sign.system.vo.sign.req.*;
import com.iyin.sign.system.vo.sign.resp.SignLogRespDTO;
import com.iyin.sign.system.vo.sign.resp.SignRespDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @ClassName: FastSignController
 * @Description: 快捷签署
 * @Author: yml
 * @CreateDate: 2019/6/21
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/21
 * @Version: 1.0.0
 */
@RestController
@Api(tags = "快捷签署")
@RequestMapping("/fast")
public class FastSignController extends BaseController {

  @Autowired private IFastSignService fastSignService;

  /**
   * 单文件单页单签章
   *
   * @Author: yml
   * @CreateDate: 2019/6/24
   * @UpdateUser: yml
   * @UpdateDate: 2019/6/24
   * @Version: 0.0.1
   * @param singleFastSignReqVO
   * @param request
   * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
   */
  @PostMapping(value = "/singleSign", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "个人/企业快捷签-单文件单页单签章接口", tags = {SwaggerConstant.API_FAST_SIGN}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public EsealResult<SignRespDTO> singleSign(@RequestBody @Valid SingleFastSignReqVO singleFastSignReqVO, HttpServletRequest request) {
    return fastSignService.singleSign(singleFastSignReqVO, request);
  }

  /**
   * 单文件多页坐标签章
   *
   * @Author: yml
   * @CreateDate: 2019/6/24
   * @UpdateUser: yml
   * @UpdateDate: 2019/6/24
   * @Version: 0.0.1
   * @param singleFastSignMoreReqVO
 * @param request
   * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
   */
  @PostMapping(value = "/singleCoordBatchSign", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "个人/企业快捷签-单文件多页坐标签章接口", tags = {SwaggerConstant.API_FAST_SIGN}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public EsealResult<SignRespDTO> singleCoordBatchSign(@RequestBody @Valid SingleFastSignMoreReqVO singleFastSignMoreReqVO, HttpServletRequest request) {
    return fastSignService.singleCoordBatchSign(singleFastSignMoreReqVO, request);
  }

  /**
   * 单文件骑缝坐标签章
   *
   * @Author: yml
   * @CreateDate: 2019/6/25
   * @UpdateUser: yml
   * @UpdateDate: 2019/6/25
   * @Version: 0.0.1
   * @param fastPerforationSignReqVO
   * @param request
   * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
   */
  @PostMapping(value = "/singlePerforationCoordSign", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "个人/企业快捷签-单文件骑缝坐标签章接口", tags = {SwaggerConstant.API_FAST_SIGN}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public EsealResult<SignRespDTO> singlePerforationCoordSign(@RequestBody @Valid FastPerforationSignReqVO fastPerforationSignReqVO, HttpServletRequest request) {
    return fastSignService.singlePerforationCoordSign(fastPerforationSignReqVO, request);
  }

  /**
   * 单文件坐标连页签章
   *
   * @Author: yml
   * @CreateDate: 2019/6/25
   * @UpdateUser: yml
   * @UpdateDate: 2019/6/25
   * @Version: 0.0.1
   * @param fastPerforationHalfSignReqVO
   * @param request
   * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
   */
  @PostMapping(value = "/singlePerforationCoordHalfSign", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "个人/企业快捷签-单文件坐标连页签章接口 V1.2.0", tags = {SwaggerConstant.API_FAST_SIGN}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public EsealResult<SignRespDTO> singlePerforationCoordHalfSign(@RequestBody @Valid FastPerforationHalfSignReqVO fastPerforationHalfSignReqVO, HttpServletRequest request) {
    return fastSignService.singlePerforationCoordHalfSign(fastPerforationHalfSignReqVO, request);
  }

  /**
   * C++ ukey签章接入系统
   *
   * @Author: yml
   * @CreateDate: 2019/7/31
   * @UpdateUser: yml
   * @UpdateDate: 2019/7/31
   * @Version: 0.0.1
   * @param uKeySignReqVO
   * @param request
   * @return : com.iyin.sign.system.common.json.EsealResult<java.lang.Boolean>
   */
  @PostMapping(value = "/uKeySign")
  @ApiOperation(value = "v1.1.0_C++ uKey签章接入系统", tags = {SwaggerConstant.API_U_KEY_SIGN}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public EsealResult<Boolean> uKeySign(@RequestBody @Valid UKeySignReqVO uKeySignReqVO, HttpServletRequest request) {
    return fastSignService.uKeySign(uKeySignReqVO, request);
  }

  /**
   * 签章日志
   *
   * @Author: yml
   * @CreateDate: 2019/7/31
   * @UpdateUser: yml
   * @UpdateDate: 2019/7/31
   * @Version: 0.0.1
   * @param signLogReqVO
   * @param request
   * @return : com.iyin.sign.system.common.json.EsealResult
   */
  @PostMapping(value = "/signLog")
  @ApiOperation(value = "v1.2.1_sprint4 签章日志", tags = {SwaggerConstant.API_SIGN_LOG}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public EsealResult<IPage<SignLogRespDTO>> signLog(@RequestBody @Valid SignLogReqVO signLogReqVO, HttpServletRequest request) {
    return fastSignService.signLog(signLogReqVO, request);
  }
}
