package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.entity.FileResource;
import com.iyin.sign.system.entity.SignSysEnterpriseInfo;
import com.iyin.sign.system.entity.SignSysUserInfo;
import com.iyin.sign.system.entity.SysSignApplication;
import com.iyin.sign.system.mapper.FileResourceMapper;
import com.iyin.sign.system.mapper.ISysSignatureLogMapper;
import com.iyin.sign.system.mapper.SignSysEnterpriseInfoMapper;
import com.iyin.sign.system.mapper.SignSysUserInfoMapper;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.code.FileResponseCode;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.model.exception.TokenCheckException;
import com.iyin.sign.system.service.IAuthTokenService;
import com.iyin.sign.system.service.IDocumentApiService;
import com.iyin.sign.system.service.RedisService;
import com.iyin.sign.system.service.SignSysUserDataLimitService;
import com.iyin.sign.system.util.Base64Util;
import com.iyin.sign.system.util.FileUtil;
import com.iyin.sign.system.util.JWTUtils;
import com.iyin.sign.system.vo.file.FileResourceDto;
import com.iyin.sign.system.vo.file.FileUploadRespDto;
import com.iyin.sign.system.vo.req.FileResourceListVO;
import com.iyin.sign.system.vo.req.QuerySignLogReqVO;
import com.iyin.sign.system.vo.resp.DocumentSignLogRespDTO;
import com.netflix.discovery.converters.Auto;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: DocumentApiServiceImpl
 * @Description: 文档API
 * @Author: yml
 * @CreateDate: 2019/7/5
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/5
 * @Version: 1.0.0
 */
@Service
@Slf4j
public class DocumentApiServiceImpl implements IDocumentApiService {

    /**
     * 后缀分割字符串
     */
    private static final char CHAR = '.';
    @Value("${request.limit}")
    private int requestLimit;
    @Resource
    private ISysSignatureLogMapper signatureLogMapper;
    @Resource
    private FileResourceMapper fileResourceMapper;
    @Resource
    private SignSysUserInfoMapper signSysUserInfoMapper;
    @Resource
    SignSysEnterpriseInfoMapper sysEnterpriseInfoMapper;

    @Autowired
    SignSysUserDataLimitService signSysUserDataLimitService;

    @Autowired
    private FileServiceImpl fileService;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private RedisService redisService;
    @Autowired
    private IAuthTokenService authTokenService;

    @Value("${fileTempPath}")
    private String fileTempPath;

    private static final String CONTENT_DIS ="Content-Disposition";
    private static final String OUT_FAIL="com.iyin.sign.system.service.impl.DocumentApiServiceImpl，file output fail:{} ";
    private static final String API_TOKEN = "api_token";
    private static final String SESSION_TOKEN = "session_token";
    private static final String USERID = "userId";

    /**
     * 根据文档编码查询签署日志
     *
     * @param signLogReqVO
     * @return : java.util.List<com.iyin.sign.system.vo.resp.DocumentSignLogRespDTO>
     * @Author: yml
     * @CreateDate: 2019/7/5
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/5
     * @Version: 0.0.1
     */
    @Override
    public IyinPage<DocumentSignLogRespDTO> querySignLog(QuerySignLogReqVO signLogReqVO, HttpServletRequest request) {
        ckToken(request);
        IyinPage<DocumentSignLogRespDTO> iyinPage = new IyinPage<>(signLogReqVO.getCurrentPage(), signLogReqVO.getPageSize());
        List<DocumentSignLogRespDTO> documentsignlogrespdtos = signatureLogMapper.selectByFileCode(iyinPage, signLogReqVO.getFileCode());
        iyinPage.setRecords(documentsignlogrespdtos);
        return iyinPage;
    }

    private String ckToken(HttpServletRequest request) {
        log.info("DocumentApiServiceImpl ckToken");
        String userId="";
        String token = request.getHeader(API_TOKEN);
        String sessionToken = request.getHeader(SESSION_TOKEN);
        if (org.apache.commons.lang.StringUtils.isBlank(token) && org.apache.commons.lang.StringUtils.isBlank(sessionToken)) {
            throw new BusinessException(ErrorCode.REQUEST_40434);
        }
        if(org.apache.commons.lang.StringUtils.isNotBlank(sessionToken)){
            Claims claims = jwtUtils.parseJWT(sessionToken);
            userId = String.valueOf(claims.get(USERID));
        }else{
            log.info("DocumentApiServiceImpl apiToken");
            SysSignApplication sysSignApplication = authTokenService.verifyAuthToken(token);
            userId = sysSignApplication.getUserId();
        }
        return userId;
    }

    /**
     * 上传文档并转换
     *
     * @param multipartFile
     * @param userId
     * @return : java.lang.String
     * @Author: yml
     * @CreateDate: 2019/7/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/8
     * @Version: 0.0.1
     */
    @Override
    public String conversionDocument(MultipartFile multipartFile, String userId) {
        FileUploadRespDto fileUploadRespDto = getFileUploadRespDto(multipartFile, userId);
        if(null != fileUploadRespDto && null != fileUploadRespDto.getFile()){
            return fileUploadRespDto.getFile().getFileCode();
        }
        return null;
    }

    FileUploadRespDto getFileUploadRespDto(MultipartFile multipartFile, String userId) {
        FileUploadRespDto fileUploadRespDto;
        try {
            File outFile=fileService.fileToPdf(multipartFile,fileTempPath);
            //upload outFile
            String originalFilename = multipartFile.getOriginalFilename();
            if(originalFilename.lastIndexOf(CHAR) < 0 ){
                originalFilename = originalFilename + ".pdf";
            }else{
                originalFilename = FileUtil.getFielNameNoExt(originalFilename) + ".pdf";
            }
            fileUploadRespDto=fileService.upload(new InMemoryMultipartFile(null, originalFilename, MediaType.APPLICATION_PDF_VALUE, Base64Util.fileToByte(outFile.getPath())),userId);
            Files.deleteIfExists(outFile.toPath());
        } catch (IOException e) {
            log.error("com.iyin.sign.system.service.impl.DocumentApiServiceImpl.conversionDocument IOException :",e);
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        }
        return fileUploadRespDto;
    }

    /**
     * 查询文档详情
     *
     * @param fileCode
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/7/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/8
     * @Version: 0.0.1
     */
    @Override
    public FileResource queryDocumentDetail(String fileCode) {
        return fileResourceMapper.selectOne(new QueryWrapper<FileResource>().eq("file_code", fileCode).eq("is_deleted", 0));
    }

    /**
     * 根据文档编码下载文档
     *
     * @param fileCode
     * @param response
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/7/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/8
     * @Version: 0.0.1
     */
    @Override
    public InMemoryMultipartFile downloadDocument(String fileCode, HttpServletResponse response) {
        try {
            InMemoryMultipartFile file = fileService.downloadSignDoc(fileCode);
            response.setContentType(file.getContentType());
            response.setHeader(CONTENT_DIS, String.format("attachment;filename=%s", file.getOriginalFilename()));
            response.getOutputStream().write(file.getBytes());
            return file;
        } catch (IOException|BusinessException e) {
            log.error(OUT_FAIL, e.getMessage());
            if(e instanceof IOException){
                throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
            }else{
                throw new BusinessException(FileResponseCode.DATA_NOT_FOUND);
            }
        }
    }

    /**
     * 查询文档列表
     *
     * @param fileName
     * @param pageNum
     * @param pageSize
     * @return : com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.entity.FileResource>
     * @Author: yml
     * @CreateDate: 2019/7/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/8
     * @Version: 0.0.1
     */
    @Override
    public IyinPage<FileResourceDto> queryDocumentList(String fileName, Integer pageNum, Integer pageSize,HttpServletRequest request) {
        IyinPage<FileResourceDto> iyinPage = new IyinPage<>(pageNum,pageSize);
        FileResourceListVO fileResourceListVO=new FileResourceListVO();
        if(StringUtils.isNotBlank(fileName)) {
            fileResourceListVO.setFileName(fileName);
        }
        List<String> userList=signSysUserDataLimitService.getPowerScopeUserIds(ckToken(request));
        fileResourceListVO.setUserIds(userList);
        List<FileResourceDto> fileResourceDtoList= fileResourceMapper.queryByName(iyinPage, fileResourceListVO);
        iyinPage.setRecords(fileResourceDtoList);
        return iyinPage;
    }

    private String getUser(HttpServletRequest request) {
        log.info("DocumentApiServiceImpl");
        String sessionToken = request.getHeader("session_token");
        log.info("=======sessionToken:{}=========", sessionToken);
        String user = "";
        if (!StringUtils.isEmpty(sessionToken)) {
            Claims claims = jwtUtils.parseJWT(sessionToken);
            user = String.valueOf(claims.get(USERID));
        }
        if(StringUtils.isBlank(user)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1011);
        }
        return user;
    }

    /**
     * 验证码验证签章
     *
     * @param verificationCode
     * @param httpServletRequest
     * @return : com.iyin.sign.system.entity.FileResource
     * @Author: yml
     * @CreateDate: 2019/7/25
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/25
     * @Version: 0.0.1
     */
    @Override
    public FileResource queryDocumetnByValityCode(String verificationCode, HttpServletRequest httpServletRequest) {
        FileResource fileResource;
        String userId =null;
        try {
            String sessionToken = httpServletRequest.getHeader("session_token");
            Claims claims = jwtUtils.parseJWT(sessionToken);
            userId =String.valueOf(claims.get(USERID));
            String url = httpServletRequest.getRequestURL().toString();
            String key = "req_limit_".concat(url).concat(userId);
            long initDelay  =getRemainMinuteOneDay(new Date());
            if (!redisService.exists(key) || StringUtils.isEmpty(redisService.get(key))) {
                redisService.set(key, String.valueOf(1),initDelay);
            } else {
                Integer getValue = Integer.parseInt(redisService.get(key)) + 1;
                redisService.set(key, String.valueOf(getValue),initDelay);
            }
            int count = Integer.parseInt(redisService.get(key));
            if (count > requestLimit) {
                throw new BusinessException(FileResponseCode.REQUEST_LIMIT_ENOUGH);
            }
            SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(userId);
            String enterpriseOrPersonalId;
            if(null == signSysUserInfo){
                SignSysEnterpriseInfo belongEnterprise = sysEnterpriseInfoMapper.selectAdminBelongEnterprise();
                enterpriseOrPersonalId = belongEnterprise.getId();
            }else{
                enterpriseOrPersonalId = signSysUserInfo.getEnterpriseOrPersonalId();
            }
            fileResource = fileResourceMapper.queryDocumetnByValityCode(verificationCode, enterpriseOrPersonalId);
        } catch (TokenCheckException e) {
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1008);
        }
        if(null == fileResource){
            throw new BusinessException(FileResponseCode.DATA_NOT_FOUND_BY_CODE);
        }


        /**
         *数据权限验证
         * 验证当前操作用户，是否拥有资源的查看权限
         */
       boolean power =  signSysUserDataLimitService.checkResouceDataLimit(fileResource.getUserId(),userId);
       if(!power){
           throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1057);
       }
        return fileResource;
    }

    /**
     * 获取当天剩余的分钟数
     *
     * @Author: yml
     * @CreateDate: 2019/7/26
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/26
     * @Version: 0.0.1

     * @return : long
     */
    private static long getRemainMinuteOneDay(Date currentDate) {
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault());
        return ChronoUnit.MINUTES.between(currentDateTime, midnight);
    }

}
