package com.iyin.sign.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.common.enums.AppEnum;
import com.iyin.sign.system.common.enums.TemplateEnum;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.dto.req.SignSysTemplateDTO;
import com.iyin.sign.system.entity.SignSysTemplate;
import com.iyin.sign.system.entity.SignSysTemplateField;
import com.iyin.sign.system.mapper.ISignSysTemplateFieldMapper;
import com.iyin.sign.system.mapper.SignSysTemplateMapper;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.FileResponseCode;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.IFileService;
import com.iyin.sign.system.service.ISignSysTemplateService;
import com.iyin.sign.system.service.SignSysUserDataLimitService;
import com.iyin.sign.system.util.FileUtil;
import com.iyin.sign.system.util.JWTUtils;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.vo.file.FileResourceDto;
import com.iyin.sign.system.vo.file.FileUploadRespDto;
import com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO;
import com.iyin.sign.system.vo.req.AddTemplateReqVo;
import com.iyin.sign.system.vo.req.SignSysTemplateGenVO;
import com.iyin.sign.system.vo.req.SignSysTemplateListVO;
import com.iyin.sign.system.vo.req.SignSysTemplateVO;
import com.iyin.sign.system.vo.resp.TemplateRespDTO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 模板表 服务实现类
 * </p>
 *
 * @author wdf
 * @since 2019-07-15
 */
@Service
@Slf4j
public class SignSysTemplateServiceImpl extends ServiceImpl<SignSysTemplateMapper, SignSysTemplate> implements ISignSysTemplateService {

    @Value("${fileTempPath}")
    private String fileTempPath;
    private static final String HTML = "HTML";
    private static final String PDF = "PDF";
    private static final String HTML_SUFFIX = ".html";

    @Resource
    private SignSysTemplateMapper signSysTemplateMapper;
    private final IFileService fileService;
    private final JWTUtils jwtUtils;
    private final SignSysUserDataLimitService signSysUserDataLimitService;
    @Resource
    private ISignSysTemplateFieldMapper signSysTemplateFieldMapper;

    @Autowired
    public SignSysTemplateServiceImpl(IFileService fileService, JWTUtils jwtUtils,SignSysUserDataLimitService signSysUserDataLimitService) {
        this.fileService = fileService;
        this.jwtUtils = jwtUtils;
        this.signSysUserDataLimitService = signSysUserDataLimitService;
    }

    @Override
    public IyinResult<IyinPage<SignSysTemplateDTO>> getTemplateList(Integer currPage, Integer pageSize, String applicationName,HttpServletRequest request) {
        IyinPage<SignSysTemplateDTO> page = new IyinPage<>(currPage, pageSize);
        SignSysTemplateListVO signSysTemplateListVO=new SignSysTemplateListVO();
        if(StringUtils.isNotBlank(applicationName)){
            signSysTemplateListVO.setTempName(applicationName);
        }
        List<String> userList=signSysUserDataLimitService.getPowerScopeUserIds(getUser(request));
        signSysTemplateListVO.setUserIds(userList);
        page.setRecords(signSysTemplateMapper.getPageList(page,signSysTemplateListVO));
        //返回请求结果
        IyinResult<IyinPage<SignSysTemplateDTO>> result = IyinResult.success();
        result.setData(page);
        return result;
    }

    @Override
    public int updateTemplate(SignSysTemplate signSysTemplate, HttpServletRequest request) {
        if(StringUtils.isNotBlank(signSysTemplate.getTempHtml())
                ||StringUtils.isNotBlank(signSysTemplate.getTempName())
                ||StringUtils.isNotBlank(signSysTemplate.getTempPurpose())) {
            signSysTemplate.setUpdateUser(getUser(request));
            return signSysTemplateMapper.updateTemplate(signSysTemplate);
        }
        return 0;
    }

    @Override
    public int genTemplate(SignSysTemplateGenVO signSysTemplateGenVO, HttpServletRequest request) {
        SignSysTemplate sysSignTemplate=new SignSysTemplate();
        BeanUtils.copyProperties(signSysTemplateGenVO,sysSignTemplate);
        if(StringUtils.isNotBlank(signSysTemplateGenVO.getTempHtml())
                ||StringUtils.isNotBlank(signSysTemplateGenVO.getTempName())
                ||StringUtils.isNotBlank(signSysTemplateGenVO.getTempPurpose())){
            updateTemplate(sysSignTemplate,request);
        }
        SignSysTemplate signSysTemplate=signSysTemplateMapper.selectById(signSysTemplateGenVO.getId());
        if(null!=signSysTemplate){
            String uuidFilePath = UUID.randomUUID().toString() + HTML_SUFFIX;
            File filePath=new File(fileTempPath+uuidFilePath);
            try (OutputStream os = new FileOutputStream(filePath);
                 PrintStream ps = new PrintStream(os)) {
                ps.println(signSysTemplate.getTempHtml());
                fileService.uploadCovertPdfData(new InMemoryMultipartFile(filePath,signSysTemplate.getTempName()+HTML_SUFFIX),request);
            } catch (IOException e) {
                log.error("genTemplate IOException :", e);
                throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
            }
            return 0;
        }
        return 1;
    }

    @Override
    public int addTemplate(AddTemplateReqVo signSysTemplateVO, HttpServletRequest request) {
        SignSysTemplate signSysTemplate = new SignSysTemplate();
        String id;
        String templateId = signSysTemplateVO.getTemplateId();
        int relt;
        if (StringUtils.isNotBlank(templateId)) {
            id = signSysTemplateVO.getTemplateId();
            SignSysTemplate signSysTemplate1 = signSysTemplateMapper.selectById(id);
            signSysTemplate1.setTempName(signSysTemplateVO.getTempName());
            signSysTemplate1.setTempPurpose(signSysTemplateVO.getTempPurpose());
            signSysTemplate1.setGmtModified(new Date());
            signSysTemplate1.setUpdateUser(getUser(request));
            relt = signSysTemplateMapper.updateById(signSysTemplate1);
        } else {
            id = String.valueOf(SnowflakeIdWorker.getId());
            signSysTemplate.setTempName(signSysTemplateVO.getTempName());
            signSysTemplate.setTempType(TemplateEnum.TEMP_HTML.getName());
            signSysTemplate.setTempPurpose(signSysTemplateVO.getTempPurpose());
            signSysTemplate.setTempHtml(signSysTemplateVO.getTempHtml());
            signSysTemplate.setTempStatus(AppEnum.UNSUSPEND.getCode());
            signSysTemplate.setIsDeleted(AppEnum.UNDELETE.getCode());
            signSysTemplate.setGmtCreate(new Date());
            signSysTemplate.setCreateUser(getUser(request));
            signSysTemplate.setId(id);
            relt = signSysTemplateMapper.insert(signSysTemplate);
        }
        @Valid List<AddTemplateReqVo.TemplateFileds> templateFiledsList = signSysTemplateVO.getTemplateFiledsList();
        if (!CollectionUtils.isEmpty(templateFiledsList)) {
            for (AddTemplateReqVo.TemplateFileds templateFileds : templateFiledsList) {
                SignSysTemplateField signSysTemplateField = new SignSysTemplateField();
                BeanUtils.copyProperties(templateFileds, signSysTemplateField);
                signSysTemplateField.setId(SnowflakeIdWorker.getIdToStr());
                signSysTemplateField.setTemplateId(id);
                signSysTemplateFieldMapper.insert(signSysTemplateField);
            }
        }
        return relt;
    }

    @Override
    public SignSysTemplate addAppBackInfo(SignSysTemplateVO signSysTemplateVO, HttpServletRequest request) {
        SignSysTemplate signSysTemplate = new SignSysTemplate();
        signSysTemplate.setId(String.valueOf(SnowflakeIdWorker.getId()));
        signSysTemplate.setTempName(signSysTemplateVO.getTempName());
        signSysTemplate.setTempType(TemplateEnum.TEMP_HTML.getName());
        signSysTemplate.setTempPurpose(signSysTemplateVO.getTempPurpose());
        signSysTemplate.setTempHtml(signSysTemplateVO.getTempHtml());
        signSysTemplate.setTempStatus(AppEnum.UNDELETE.getCode());
        signSysTemplate.setIsDeleted(AppEnum.UNDELETE.getCode());
        signSysTemplate.setGmtCreate(new Date());
        signSysTemplate.setCreateUser(getUser(request));
        int count=signSysTemplateMapper.insert(signSysTemplate);
        SignSysTemplateDTO signSysTemplateDTO=new SignSysTemplateDTO();
        SignSysTemplate temp=null;
        if(count>0){
            signSysTemplateDTO.setId(signSysTemplate.getId());
            temp=getTemplateById(signSysTemplateDTO);
        }
        return temp;
    }

    @Override
    public SignSysTemplate getTemplateById(SignSysTemplateDTO signSysTemplateDTO) {
        return signSysTemplateMapper.getTemplateById(signSysTemplateDTO);
    }

    @Override
    public SignSysTemplate importWord(MultipartFile file, HttpServletRequest request) {
        SignSysTemplate signSysTemplate = new SignSysTemplate();
        signSysTemplate.setId(String.valueOf(SnowflakeIdWorker.getId()));
        signSysTemplate.setTempName(FileUtil.getFielNameNoExt(file.getOriginalFilename()));
        signSysTemplate.setTempType(TemplateEnum.TEMP_WORD.getName());
        if(file.getContentType().toUpperCase().contains(PDF)){
            signSysTemplate.setTempType(TemplateEnum.TEMP_PDF.getName());
        }
        FileUploadRespDto fileUploadRespDto=fileService.uploadCovertPdfData(file,request);
        signSysTemplate.setTempHtml(fileUploadRespDto.getFile().getFileCode());
        signSysTemplate.setTempStatus(AppEnum.UNDELETE.getCode());
        signSysTemplate.setIsDeleted(AppEnum.UNDELETE.getCode());
        signSysTemplate.setGmtCreate(new Date());
        signSysTemplate.setCreateUser(getUser(request));
        int count=signSysTemplateMapper.insert(signSysTemplate);
        SignSysTemplateDTO signSysTemplateDTO=new SignSysTemplateDTO();
        SignSysTemplate temp=null;
        if(count>0){
            signSysTemplateDTO.setId(signSysTemplate.getId());
            temp=getTemplateById(signSysTemplateDTO);
        }
        return temp;
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
     * @return : com.iyin.sign.system.vo.resp.TemplateRespDTO
     */
    @Override
    public TemplateRespDTO getTemplateM(String id) {
        SignSysTemplate signSysTemplate = signSysTemplateMapper.selectById(id);
        TemplateRespDTO templateRespDTO = new TemplateRespDTO();
        BeanUtils.copyProperties(signSysTemplate,templateRespDTO);
        List<SignSysTemplateField> signSysTemplateFields = signSysTemplateFieldMapper
                .selectList(Wrappers.<SignSysTemplateField>lambdaQuery().eq(SignSysTemplateField::getTemplateId, id));
        String jsonString = JSON.toJSONString(signSysTemplateFields);
        List<AddTemplateReqVo.TemplateFileds> templateFileds = JSON
                .parseArray(jsonString, AddTemplateReqVo.TemplateFileds.class);
        templateRespDTO.setTemplateFiledsList(templateFileds);
        return templateRespDTO;
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
     * @param request
     * @return : com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO
     */
    @Override
    public CompactFileUploadRespDTO fromTemplate(String templateId, HttpServletRequest request) {
        SignSysTemplate signSysTemplate = signSysTemplateMapper.selectById(templateId);
        if(null == signSysTemplate){
            throw new BusinessException(ErrorCode.REQUEST_40456);
        }
        FileUploadRespDto fileUploadRespDto;
        if (HTML.equalsIgnoreCase(signSysTemplate.getTempType())) {
            String uuidFilePath = SnowflakeIdWorker.getIdToStr() + HTML_SUFFIX;
            File filePath = new File(fileTempPath + uuidFilePath);
            try (OutputStream os = new FileOutputStream(filePath); PrintStream ps = new PrintStream(os)) {
                ps.println(signSysTemplate.getTempHtml());
                fileUploadRespDto = fileService.uploadCovertPdfData(
                        new InMemoryMultipartFile(filePath, signSysTemplate.getTempName() + HTML_SUFFIX), request);
            } catch (IOException e) {
                log.error("genTemplate IOException :", e);
                throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
            }
        }else{
            fileUploadRespDto = new FileUploadRespDto();
            FileResourceDto fileResourceDto = new FileResourceDto();
            fileResourceDto.setFileCode(signSysTemplate.getTempHtml());
            fileResourceDto.setFileName(signSysTemplate.getTempName());
            fileUploadRespDto.setFile(fileResourceDto);
        }
        if (null != fileUploadRespDto && null != fileUploadRespDto.getFile()) {
            FileResourceDto uploadRespDtoFile = fileUploadRespDto.getFile();
            String fileCode = uploadRespDtoFile.getFileCode();
            InMemoryMultipartFile inFile = fileService.fetch(fileCode);
            try (PDDocument pdfDocument = PDDocument
                    .load(inFile.getInputStream(), MemoryUsageSetting.setupTempFileOnly())) {
                CompactFileUploadRespDTO compactFileUploadRespDTO = new CompactFileUploadRespDTO();
                compactFileUploadRespDTO.setFileCode(fileCode);
                compactFileUploadRespDTO.setFileName(uploadRespDtoFile.getFileName());
                compactFileUploadRespDTO.setFilePage(pdfDocument.getNumberOfPages());
                compactFileUploadRespDTO.setHomePageImagePath("/document/scan/page/" + fileCode + "/1");
                return compactFileUploadRespDTO;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
            }
        }
        return null;
    }

    private String getUser(HttpServletRequest request) {
        log.info("SignSysTemplateServiceImpl");
        String sessionToken = request.getHeader("session_token");
        log.info("=======sessionToken:{}=========", sessionToken);
        String user = "";
        if (!StringUtils.isEmpty(sessionToken)) {
            Claims claims = jwtUtils.parseJWT(sessionToken);
            user = String.valueOf(claims.get("userId"));
        }
        if(StringUtils.isBlank(user)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1011);
        }
        return user;
    }
}
