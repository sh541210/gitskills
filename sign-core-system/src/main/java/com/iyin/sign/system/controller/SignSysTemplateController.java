package com.iyin.sign.system.controller;


import com.iyin.sign.system.dto.req.SignSysTemplateDTO;
import com.iyin.sign.system.entity.SignSysTemplate;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.ISignSysTemplateService;
import com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO;
import com.iyin.sign.system.vo.req.AddTemplateReqVo;
import com.iyin.sign.system.vo.req.SignSysTemplateGenVO;
import com.iyin.sign.system.vo.req.SignSysTemplateUpdVO;
import com.iyin.sign.system.vo.req.SignSysTemplateVO;
import com.iyin.sign.system.vo.resp.TemplateRespDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * 模板表 前端控制器
 * </p>
 *
 * @author wdf
 * @since 2019-07-15
 */
@Api(tags = "模板管理模块")
@Slf4j
@RestController
@RequestMapping("/signSysTemplate")
public class SignSysTemplateController {
    @Autowired
    private ISignSysTemplateService signSysTemplateService;


    /**
     * 生成模板
     *
     * @param signSysTemplateGenVO
     * @return com.iyin.sign.system.model.IyinResult<java.lang.Integer>
     * @Author: wdf
     * @CreateDate: 2019/8/12 12:25
     * @UpdateUser: wdf
     * @UpdateDate: 2019/8/12 12:25
     * @Version: 0.0.1
     */
    @ApiOperation("生成模板接口")
    @PatchMapping("/genTemplate")
    public IyinResult<Integer> genTemplate(
            @RequestBody @Valid SignSysTemplateGenVO signSysTemplateGenVO, HttpServletRequest request) {
        return IyinResult.success(signSysTemplateService.genTemplate(signSysTemplateGenVO, request));
    }


    /**
     * 模板管理列表接口
     *
     * @param currPage, pageSize, tempName
     * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage < com.iyin.sign.system.dto.req.SignSysTemplateDTO>>
     * @Author: wdf
     * @CreateDate: 2019/8/12 12:25
     * @UpdateUser: wdf
     * @UpdateDate: 2019/8/12 12:25
     * @Version: 0.0.1
     */
    @ApiOperation("模板管理列表接口")
    @GetMapping("/getTemplateList")
    public IyinResult<IyinPage<SignSysTemplateDTO>> getTemplateList(
            @RequestParam Integer currPage,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String tempName, HttpServletRequest request) {
        return signSysTemplateService.getTemplateList(currPage, pageSize, tempName, request);
    }

    /**
     * 模板详情接口
     *
     * @param id
     * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.dto.req.SignSysTemplateDTO>
     * @Author: wdf
     * @CreateDate: 2019/8/12 12:25
     * @UpdateUser: wdf
     * @UpdateDate: 2019/8/12 12:25
     * @Version: 0.0.1
     */
    @ApiOperation("模板详情接口")
    @GetMapping("/getTemplate")
    public IyinResult<SignSysTemplateDTO> getTemplate(@RequestParam(value = "id") String id) {
        SignSysTemplateDTO signSysTemplateDTO = new SignSysTemplateDTO();
        signSysTemplateDTO.setId(id);
        SignSysTemplate data = signSysTemplateService.getTemplateById(signSysTemplateDTO);
        SignSysTemplateDTO signSysTemplateDTO1 = new SignSysTemplateDTO();
        BeanUtils.copyProperties(data, signSysTemplateDTO1);
        return IyinResult.success(signSysTemplateDTO1);
    }

    /**
     * 模板添加接口
     *
     * @param addTemplateReqVo
     * @return com.iyin.sign.system.model.IyinResult<java.lang.Integer>
     * @Author: wdf
     * @CreateDate: 2019/8/12 12:25
     * @UpdateUser: wdf
     * @UpdateDate: 2019/8/12 12:25
     * @Version: 0.0.1
     */
    @ApiOperation("模板添加接口")
    @PostMapping("/addTemplate")
    public IyinResult<Integer> addTemplate(
            @RequestBody @Valid AddTemplateReqVo addTemplateReqVo, HttpServletRequest request) {
        return IyinResult.success(signSysTemplateService.addTemplate(addTemplateReqVo, request));
    }

    /**
     * 模板详情接口-返回控件
     *
     * @Author: yml
     * @CreateDate: 2019/9/26
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/26
     * @Version: 0.0.1
     * @param id
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Integer>
     */
    @ApiOperation("模板详情接口-返回控件")
    @PostMapping("/getTemplateM")
    public IyinResult<TemplateRespDTO> getTemplateM(@RequestParam(value = "id") String id) {
        return IyinResult.success(signSysTemplateService.getTemplateM(id));
    }

    /**
     * 模板添加并返回模板信息接口
     *
     * @param signSysTemplateVO
     * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.entity.SignSysTemplate>
     * @Author: wdf
     * @CreateDate: 2019/8/12 12:25
     * @UpdateUser: wdf
     * @UpdateDate: 2019/8/12 12:25
     * @Version: 0.0.1
     */
    @ApiOperation("模板添加并返回模板信息接口")
    @PostMapping("/addAppBackInfo")
    public IyinResult<SignSysTemplate> addAppBackInfo(
            @RequestBody @Valid SignSysTemplateVO signSysTemplateVO, HttpServletRequest request) {
        return IyinResult.success(signSysTemplateService.addAppBackInfo(signSysTemplateVO, request));
    }

    /**
     * 模板更新接口
     *
     * @param signSysTemplateUpdVO
     * @return com.iyin.sign.system.model.IyinResult
     * @Author: wdf
     * @CreateDate: 2019/8/12 12:26
     * @UpdateUser: wdf
     * @UpdateDate: 2019/8/12 12:26
     * @Version: 0.0.1
     */
    @ApiOperation("模板更新接口")
    @PatchMapping("/updateTemplate")
    public IyinResult updateTemplate(
            @RequestBody @Valid SignSysTemplateUpdVO signSysTemplateUpdVO, HttpServletRequest request) {
        SignSysTemplate sysSignTemplate = new SignSysTemplate();
        BeanUtils.copyProperties(signSysTemplateUpdVO, sysSignTemplate);
        return IyinResult.success(signSysTemplateService.updateTemplate(sysSignTemplate, request));
    }

    /**
     * 模板删除接口
     *
     * @param id
     * @return com.iyin.sign.system.model.IyinResult
     * @Author: wdf
     * @CreateDate: 2019/8/12 12:26
     * @UpdateUser: wdf
     * @UpdateDate: 2019/8/12 12:26
     * @Version: 0.0.1
     */
    @ApiOperation("模板删除接口")
    @DeleteMapping("/deleteTemplate")
    public IyinResult deleteTemplate(@RequestParam String id) {
        return IyinResult.success(signSysTemplateService.removeById(id));
    }


    /**
     * 导入word
     *
     * @param file, request
     * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.entity.SignSysTemplate>
     * @Author: wdf
     * @CreateDate: 2019/8/14 9:25
     * @UpdateUser: wdf
     * @UpdateDate: 2019/8/14 9:25
     * @Version: 0.0.1
     */
    @PostMapping(value = "/importWord")
    @ApiOperation("导入word V1.2.0")
    public IyinResult<SignSysTemplate> importWord(
            @RequestParam(value = "signFile") MultipartFile file, HttpServletRequest request) {
        return IyinResult.success(signSysTemplateService.importWord(file, request));
    }

    /**
     * 从模板导入
     *
     * @Author: yml
     * @CreateDate: 2019/9/26
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/26
     * @Version: 0.0.1
     * @param templateId
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.entity.SignSysTemplate>
     */
    @PostMapping(value = "/fromTemplate")
    @ApiOperation("从模板导入")
    public IyinResult<CompactFileUploadRespDTO> fromTemplate(
            @RequestParam(value = "id") @ApiParam("模板ID") String templateId, HttpServletRequest request) {
        return IyinResult.success(signSysTemplateService.fromTemplate(templateId, request));
    }

}
