package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iyin.sign.system.common.enums.CertificateEnum;
import com.iyin.sign.system.common.enums.PictureBusinessTypeEnum;
import com.iyin.sign.system.common.enums.PictureTypeEnum;
import com.iyin.sign.system.common.enums.SignatureEnum;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.common.exception.ServiceException;
import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.entity.*;
import com.iyin.sign.system.mapper.*;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.code.BaseResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.model.exception.IyinResultValidUtil;
import com.iyin.sign.system.model.sign.MultiParam;
import com.iyin.sign.system.service.AsyncService;
import com.iyin.sign.system.service.IAuthTokenService;
import com.iyin.sign.system.service.IFastSignService;
import com.iyin.sign.system.util.*;
import com.iyin.sign.system.vo.file.FileResourceDto;
import com.iyin.sign.system.vo.file.FileUploadRespDto;
import com.iyin.sign.system.vo.req.FileResourceInfoVO;
import com.iyin.sign.system.vo.sign.req.*;
import com.iyin.sign.system.vo.sign.resp.SignLogRespDTO;
import com.iyin.sign.system.vo.sign.resp.SignRespDTO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * @ClassName: FastSignServiceImpl
 * @Description: 快捷签
 * @Author: yml
 * @CreateDate: 2019/6/24
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/24
 * @Version: 1.0.0
 */
@Service
@Slf4j
public class FastSignServiceImpl implements IFastSignService {

    /**
     * 后缀格式分割字符串
     */
    private static final char CHAR = '.';
    private final IAuthTokenService authTokenService;

    private final SignServiceImpl signService;
    private final FileServiceImpl fileService;

    @Resource
    private SignSysSealInfoMapper sysSealInfoMapper;
    @Resource
    private ISysCertInfoMapper sysCertInfoMapper;
    @Resource
    private SignSysSignConfigMapper signSysSignConfigMapper;
    @Resource
    private ISysSignatureLogMapper signatureLogMapper;
    @Resource
    private SignSysUserInfoMapper signSysUserInfoMapper;
    @Autowired
    private JWTUtils jwtUtils;
    @Value("${fileTempPath}")
    private String fileTempPath;

    private final AsyncService asyncService;

    private static final String KEY_WORD_TIME = "关键字时间：";
    private static final String SIGN_TIME = "调用签署微服务时间：";
    private static final String SEAL_CERT_TIME = "获取章模证书时间：";
    private static final String SINGLE_API_TIME = "签章API总时间";
    private static final String SIGN_EXCEPTION = "签章异常{}";
    private static final String API_TOKEN = "api_token";
    private static final String SESSION_TOKEN = "session_token";
    private static final String CERT_ID = "cert_id";

    @Autowired
    public FastSignServiceImpl(IAuthTokenService authTokenService, SignServiceImpl signService, FileServiceImpl fileService, AsyncService asyncService) {
        this.authTokenService = authTokenService;
        this.signService = signService;
        this.fileService = fileService;
        this.asyncService = asyncService;
    }

    /**
     * 快捷单页签章
     *
     * @param singleFastSignReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
     * @Author: yml
     * @CreateDate: 2019/6/24
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/24
     * @Version: 0.0.1
     */
    @Override
    public EsealResult<SignRespDTO> singleSign(SingleFastSignReqVO singleFastSignReqVO, HttpServletRequest request) {
        try {
            long time = System.currentTimeMillis();
            String appId = "WEB";
            SysSignApplication appInfo = getAppInfo(request);
            boolean flag = null != appInfo;
            appId = flag && null != appInfo.getUserAppId() ? appInfo.getUserAppId() : appId;
            if (flag) {
                long recordReqLogTime = System.currentTimeMillis();
                // 获取pdf签章文件
                if (null == singleFastSignReqVO.getPdfBase64Str()) {
                    InMemoryMultipartFile inMemoryMultipartFile = fileService.fetch(singleFastSignReqVO.getFileCode());
                    if (StringUtils.isBlank(singleFastSignReqVO.getOriginalFileName())) {
                        singleFastSignReqVO.setOriginalFileName(inMemoryMultipartFile.getOriginalFilename());
                    }
                    singleFastSignReqVO.setPdfBase64Str(Base64Util.encode(inMemoryMultipartFile.getBytes()));
                }
                SingleSignReqVO singleSignReqVO = new SingleSignReqVO();
                singleSignReqVO.setSignOriginalFileStr(singleFastSignReqVO.getPdfBase64Str());

                long getKeyWordTime = System.currentTimeMillis();
                log.info(KEY_WORD_TIME + (getKeyWordTime - recordReqLogTime));

                // 根据章模编码获取章模路径、证书路径
                SignRespDTO signRespDTO = null;
                EsealResult<SignRespDTO> respResult = null;
                LogCommonParam logCommonParam = null;
                for(int i=0;i<singleFastSignReqVO.getList().size();i++){
                    SingleFastParamSignVO singleFastParamSignVO=singleFastSignReqVO.getList().get(i);
                    doOperateData(singleFastSignReqVO,singleFastParamSignVO, singleSignReqVO);
                    singleSignReqVO.setFoggy(singleFastParamSignVO.isFoggy());
                    singleSignReqVO.setGrey(singleFastParamSignVO.isGrey());
                    SignSysSealInfo signSysSealInfo = getSignSysSealInfo(singleFastParamSignVO.getSealCode());
                    SysCertInfo sysCertInfo = sysCertInfoMapper.selectOne(new QueryWrapper<SysCertInfo>().eq(CERT_ID, signSysSealInfo.getCertificateId()));
                    if (null == sysCertInfo) {
                        throw new ServiceException(ErrorCode.REQUEST_40255);
                    }
                    singleSignReqVO.setCertPassword(sysCertInfo.getCertPassword());
                    InMemoryMultipartFile inMemoryMultipartFile = fileService.fetch(sysCertInfo.getCertCode());
                    singleSignReqVO.setCertStr(Base64Util.inputStreamToBase64(inMemoryMultipartFile.getInputStream()));
                    singleSignReqVO.setSealCode(signSysSealInfo.getPictureCode());
                    singleSignReqVO.setSealStr(signSysSealInfo.getPictureData64());
                    long certTime = System.currentTimeMillis();
                    log.info(SEAL_CERT_TIME + (certTime - getKeyWordTime));
                    respResult = signService.setSingleSign(singleSignReqVO);
                    IyinResultValidUtil.validateSuccess(respResult);
                    long getSignTime = System.currentTimeMillis();
                    log.info(SIGN_TIME + (getSignTime - certTime));
                    signRespDTO = respResult.getData();
                    singleSignReqVO.setSignOriginalFileStr(signRespDTO.getSignCompleteFileStr());

                    String fileBase64 = signRespDTO.getSignCompleteFileStr();
                    logCommonParam = getLogCommonParam(singleFastSignReqVO.getUserId(), fileBase64, singleFastSignReqVO.getFileCode(), singleFastSignReqVO.getOriginalFileName());
                    signRespDTO.setFileCode(logCommonParam.getSignFileCode());
                    signRespDTO.setSignDate(logCommonParam.getSignDate());
                    logCommonParam.setSignatureModel(SignatureEnum.SIGNATURE_KEYWORD.getCode());
                    logCommonParam.setSignatureName(SignatureEnum.SIGNATURE_SINGLE.getCode());
                    logCommonSetter(appId, signSysSealInfo, logCommonParam, singleFastSignReqVO.getFileCode(), singleFastSignReqVO.getOriginalFileName());
                    String ipAddr = SystemUtils.getIpAddr(request);
                    logCommonParam.setIpAddress(ipAddr);
                    logCommonParam.setMacAddress(SystemUtils.getMacAddress(ipAddr));
                    logCommonParam.setDeviceName(SystemUtils.getHostName(ipAddr));
                    signLog(singleFastSignReqVO.getUserId(), logCommonParam, signRespDTO.getMultiParam());
                }
                log.info(SINGLE_API_TIME + (System.currentTimeMillis() - time));
                return respResult;
            } else {
                throw new ServiceException(ErrorCode.REQUEST_40307);
            }
        } catch (Exception e) {
            log.error(SIGN_EXCEPTION, e.getMessage());
            throw new BusinessException(ErrorCode.REQUEST_40472.getCode(), e.getMessage());
        }
    }

    private void logCommonSetter(String appId, SignSysSealInfo signSysSealInfo, LogCommonParam logCommonParam, String fileCode, String originalFileName) {
        logCommonParam.setSealName(signSysSealInfo.getPictureName());
        logCommonParam.setSealCode(signSysSealInfo.getPictureCode());
        logCommonParam.setSealType(signSysSealInfo.getPictureBusinessType());
        logCommonParam.setMediumType(signSysSealInfo.getMediumType());
        logCommonParam.setFileCode(fileCode);
        logCommonParam.setFileName(originalFileName);
        logCommonParam.setAppId(appId);
    }

    private LogCommonParam getLogCommonParam(String userId, String fileBase64, String fileCode, String originalFileName) throws IOException {
        LogCommonParam logCommonParam = new LogCommonParam();
        byte[] bytes = Base64Util.decode(fileBase64);
        if (StringUtils.isNotBlank(fileBase64)) {
            // 文件上传到服务器
            String fileName;
            //            分隔符
            if (originalFileName.lastIndexOf(CHAR) < 0) {
                fileName = originalFileName + ".pdf";
            } else {
                fileName = FileUtil.getFielNameNoExt(originalFileName) + ".pdf";
            }
            InMemoryMultipartFile in = new InMemoryMultipartFile(null, fileName, MediaType.APPLICATION_PDF_VALUE, bytes);
            updateSignFile(userId, fileCode, logCommonParam, in, LocalDateTime.now(), LocalDateTime.now());
            logCommonParam.setSignFileCode(fileCode);
            logCommonParam.setSignDate(LocalDateTime.now());
        }
        int pageNum = PdfUtil.getPdfNumberOfPages(bytes);
        logCommonParam.setPage(pageNum);
        return logCommonParam;
    }

    @Async("taskExecutor")
    public void updateSignFile(String userId, String fileCode, LogCommonParam logCommonParam, InMemoryMultipartFile in, LocalDateTime now, LocalDateTime localTime) throws IOException {
        SignSysSignConfig signSysSignConfig = signSysSignConfigMapper.getSignConfig();
        int gmtVerification = signSysSignConfig.getGmtVerification();
        FileResourceInfoVO fileResourceInfoVO = new FileResourceInfoVO();
        fileResourceInfoVO.setFileCode(fileCode);
        fileResourceInfoVO.setUserId(userId);
        fileResourceInfoVO.setQrCode(signSysSignConfig.getQrCode());
        if(1 == signSysSignConfig.getVerificationCode()){
            fileResourceInfoVO.setVerificationCode(MD5Util.md5HashCode32(in.getInputStream()));
        }else{
            fileResourceInfoVO.setVerificationCode("0");
        }
        fileResourceInfoVO.setGmtVerification(0 == gmtVerification ? "0" : LocalDate.now().plusDays(gmtVerification - 1L).toString());
        FileUploadRespDto fileUploadRespDto = fileService.updateFileInfo(in, fileResourceInfoVO);
        if (null != fileUploadRespDto && null != fileUploadRespDto.getFile()) {
            FileResourceDto uploadRespDtoFile = fileUploadRespDto.getFile();
            logCommonParam.setSignFileCode(uploadRespDtoFile.getFileCode());
            logCommonParam.setSignFileHash(uploadRespDtoFile.getFileHash());
            logCommonParam.setSignDate(uploadRespDtoFile.getGmtModified());
            log.info("签章后文件上传更新成功！文件编码：{}；用户ID：{}", fileCode, userId);
        } else {
            if (Duration.between(localTime, now).toMinutes() < 1L) {
                updateSignFile(userId, fileCode, logCommonParam, in, LocalDateTime.now(), localTime);
                log.error("签章后文件上传更新暂未成功！文件编码：{}；用户ID：{}", fileCode, userId);
            } else {
                log.error("签章后文件上传更新1分钟内未成功！文件编码：{}；用户ID：{}", fileCode, userId);
            }
        }
    }

    private void doOperateData(SingleFastSignReqVO singleFastSignReqVO,SingleFastParamSignVO singleFastParamSignVO, SingleSignReqVO singleSignReqVO) throws IOException {
        // 增加逻辑判断 关键字或者坐标 校验参数后处理数据
        if (SignatureEnum.SIGNATURE_KEYWORD.getCode().equals(singleFastSignReqVO.getSignatureMethod())) {
            // 处理数据
            ckKeyWord(singleSignReqVO, singleFastSignReqVO.getPdfBase64Str(), singleFastParamSignVO.getKeyword(), singleFastParamSignVO.getKeywordIndex(), singleFastParamSignVO.getKeywordOffsetX(), singleFastParamSignVO.getKeywordOffsetY());
        }
        if (SignatureEnum.SIGNATURE_COORDINATE.getCode().equals(singleFastSignReqVO.getSignatureMethod())) {
            // 处理数据
            ckCoordData(singleSignReqVO, singleFastParamSignVO.getCoordinateX(), singleFastParamSignVO.getCoordinateY(), singleFastParamSignVO.getPageNo());
        }
    }

    /**
     * 单文件多页坐标签章
     *
     * @param singleFastSignMoreReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
     * @Author: yml
     * @CreateDate: 2019/6/24
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/24
     * @Version: 0.0.1
     */
    @Override
    public EsealResult<SignRespDTO> singleCoordBatchSign(SingleFastSignMoreReqVO singleFastSignMoreReqVO, HttpServletRequest request) {
        try {
            long time = System.currentTimeMillis();
            for(int i=0;i<singleFastSignMoreReqVO.getList().size();i++){
                SingleFastCoordBatchSignVO singleFastCoordBatchSignVO=singleFastSignMoreReqVO.getList().get(i);
                signPageCompare(singleFastCoordBatchSignVO.getEndPageNo(), singleFastCoordBatchSignVO.getStartPageNo());
                //页码
                int startPageNumber = Integer.parseInt(singleFastCoordBatchSignVO.getStartPageNo());
                int endPageNumber = Integer.parseInt(singleFastCoordBatchSignVO.getEndPageNo());
                if (startPageNumber > endPageNumber) {
                    throw new BusinessException(ErrorCode.REQUEST_20004);
                }
            }
            String appId = "WEB";
            SysSignApplication appInfo = getAppInfo(request);
            boolean flag = null != appInfo;
            appId = flag && null != appInfo.getUserAppId() ? appInfo.getUserAppId() : appId;
            if (flag) {
                // 获取pdf签章文件
                if (null == singleFastSignMoreReqVO.getPdfbase64Str()) {
                    InMemoryMultipartFile inMemoryMultipartFile = fileService.fetch(singleFastSignMoreReqVO.getFileCode());
                    singleFastSignMoreReqVO.setPdfbase64Str(Base64Util.encode(inMemoryMultipartFile.getBytes()));
                    if (StringUtils.isBlank(singleFastSignMoreReqVO.getOriginalFileName())) {
                        singleFastSignMoreReqVO.setOriginalFileName(inMemoryMultipartFile.getOriginalFilename());
                    }
                }
                BatchSignReqVO batchSignReqVO = new BatchSignReqVO();
                batchSignReqVO.setSignOriginalFileStr(singleFastSignMoreReqVO.getPdfbase64Str());

                EsealResult<SignRespDTO> respResult = null;
                SignRespDTO signRespDTO = null;
                LogCommonParam logCommonParam = null;

                for(int i=0;i<singleFastSignMoreReqVO.getList().size();i++){
                    SingleFastCoordBatchSignVO singleFastCoordBatchSignVO=singleFastSignMoreReqVO.getList().get(i);
                    batchSignReqVO.setFoggy(singleFastCoordBatchSignVO.isFoggy());
                    batchSignReqVO.setGrey(singleFastCoordBatchSignVO.isGrey());
                    batchSignReqVO.setCoordinateX(Float.parseFloat(singleFastCoordBatchSignVO.getCoordinateX()));
                    batchSignReqVO.setCoordinateY(Float.parseFloat(singleFastCoordBatchSignVO.getCoordinateY()));

                    batchSignReqVO.setStartPageNumber(Integer.parseInt(singleFastCoordBatchSignVO.getStartPageNo()));
                    batchSignReqVO.setEndPageNumber(Integer.parseInt(singleFastCoordBatchSignVO.getEndPageNo()));
                    long getCertTime = System.currentTimeMillis();
                    SignSysSealInfo signSysSealInfo = getSignSysSealInfo(singleFastCoordBatchSignVO.getSealCode());
                    SysCertInfo sysCertInfo = sysCertInfoMapper.selectOne(new QueryWrapper<SysCertInfo>().eq(CERT_ID, signSysSealInfo.getCertificateId()));
                    if (null == sysCertInfo) {
                        throw new ServiceException(ErrorCode.REQUEST_40255);
                    }
                    InMemoryMultipartFile inMemoryMultipartFile = fileService.fetch(sysCertInfo.getCertCode());
                    String certBase64 = Base64Util.inputStreamToBase64(inMemoryMultipartFile.getInputStream());
                    batchSignReqVO.setCertStr(certBase64);
                    batchSignReqVO.setCertPassword(sysCertInfo.getCertPassword());
                    batchSignReqVO.setSealCode(signSysSealInfo.getPictureCode());
                    batchSignReqVO.setSealStr(signSysSealInfo.getPictureData64());
                    long certTime = System.currentTimeMillis();
                    log.info(SEAL_CERT_TIME + (certTime - getCertTime));
                    respResult = signService.setBatchSign(batchSignReqVO);
                    IyinResultValidUtil.validateSuccess(respResult);
                    long getSignTime = System.currentTimeMillis();
                    log.info(SIGN_TIME + (getSignTime - certTime));
                    signRespDTO = respResult.getData();
                    String fileBase64 = signRespDTO.getSignCompleteFileStr();
                    logCommonParam = getLogCommonParam(singleFastSignMoreReqVO.getUserId(), fileBase64, singleFastSignMoreReqVO.getFileCode(), singleFastSignMoreReqVO.getOriginalFileName());
                    signRespDTO.setFileCode(logCommonParam.getSignFileCode());
                    signRespDTO.setSignDate(logCommonParam.getSignDate());
                    logCommonParam.setSignatureModel(SignatureEnum.SIGNATURE_COORDINATE.getCode());
                    logCommonParam.setSignatureName(SignatureEnum.SIGNATURE_MORE.getCode());
                    logCommonSetter(appId, signSysSealInfo, logCommonParam, singleFastSignMoreReqVO.getFileCode(), singleFastSignMoreReqVO.getOriginalFileName());
                    batchSignReqVO.setSignOriginalFileStr(signRespDTO.getSignCompleteFileStr());
                    String ipAddr = SystemUtils.getIpAddr(request);
                    logCommonParam.setIpAddress(ipAddr);
                    logCommonParam.setMacAddress(SystemUtils.getMacAddress(ipAddr));
                    logCommonParam.setDeviceName(SystemUtils.getHostName(ipAddr));
                    signLog(singleFastSignMoreReqVO.getUserId(), logCommonParam, signRespDTO.getMultiParam());
                }
                log.info(SINGLE_API_TIME + (System.currentTimeMillis() - time));
                return respResult;
            } else {
                throw new ServiceException(ErrorCode.REQUEST_40307);
            }
        } catch (Exception e) {
            log.error(SIGN_EXCEPTION, e.getMessage());
            throw new BusinessException(ErrorCode.REQUEST_40472.getCode(), e.getMessage());
        }
    }

    private void signPageCompare(String endPageNo, String startPageNo) throws ServiceException {
        if (Integer.parseInt(endPageNo) <= Integer.parseInt(startPageNo)) {
            throw new ServiceException(ErrorCode.REQUEST_40101);
        }
    }

    private SysSignApplication getAppInfo(HttpServletRequest request) throws ServiceException {
        SysSignApplication app = new SysSignApplication();
        String token = request.getHeader(API_TOKEN);
        String sessionToken = request.getHeader(SESSION_TOKEN);
        if (StringUtils.isBlank(token) && StringUtils.isBlank(sessionToken)) {
            throw new ServiceException(ErrorCode.REQUEST_40434);
        }
        if (StringUtils.isNotBlank(token)) {
            app = authTokenService.verifyAuthToken(token);
        }
        return app;
    }

    /**
     * 根据request获取登录用户ID
     *
     * @param req
     * @return : java.lang.String
     * @Author: yml
     * @CreateDate: 2019/8/14
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/14
     * @Version: 0.0.1
     */
    private String getUserId(HttpServletRequest req) {
        String userId;
        String sessionToken = req.getHeader("session_token");
        if(!StringUtils.isEmpty(sessionToken)){
            Claims claims = jwtUtils.parseJWT(sessionToken);
            userId = String.valueOf(claims.get("userId"));
        }else{
            String apiToken = req.getHeader("api_token");
            SysSignApplication sysSignApplication = authTokenService.verifyAuthToken(apiToken);
            userId = sysSignApplication.getUserId();
        }

        return userId;
    }

    private SignSysSealInfo getSignSysSealInfo(String sealCode) {
        // 根据章模编码获取章模路径、证书路径
        QueryWrapper<SignSysSealInfo> queryWrapper = new QueryWrapper<SignSysSealInfo>().eq("picture_code", sealCode);
        SignSysSealInfo signSysSealInfo = sysSealInfoMapper.selectOne(queryWrapper);
        if (null == signSysSealInfo || null == signSysSealInfo.getPictureData64()) {
            throw new BusinessException(ErrorCode.REQUEST_40254);
        }
        if (CertificateEnum.ORG_CERT.getCode().equalsIgnoreCase(signSysSealInfo.getCertificateSource()) && StringUtils.isBlank(signSysSealInfo.getCertificateId())) {
            throw new BusinessException(ErrorCode.REQUEST_40201);
        } else if (CertificateEnum.ONLINE_CERT.getCode().equalsIgnoreCase(signSysSealInfo.getCertificateSource()) || CertificateEnum.LOCAL_CERT.getCode().equalsIgnoreCase(signSysSealInfo.getCertificateSource())) {
            throw new BusinessException(ErrorCode.SERVER_50203);
        }
        return signSysSealInfo;
    }

    /**
     * 单文件骑缝坐标签章
     *
     * @param fastPerforationSignReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
     * @Author: yml
     * @CreateDate: 2019/6/25
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/25
     * @Version: 0.0.1
     */
    @Override
    public EsealResult<SignRespDTO> singlePerforationCoordSign(FastPerforationSignReqVO fastPerforationSignReqVO, HttpServletRequest request) {
        try {
            long time = System.currentTimeMillis();
            for(int i=0;i<fastPerforationSignReqVO.getList().size();i++){
                FastPerforationSignVO fastPerforationSignVO=fastPerforationSignReqVO.getList().get(i);
                signPageCompare(fastPerforationSignVO.getEndPageNo(), fastPerforationSignVO.getStartPageNo());
                //页码
                int startPageNumber = Integer.parseInt(fastPerforationSignVO.getStartPageNo());
                int endPageNumber = Integer.parseInt(fastPerforationSignVO.getEndPageNo());
                if (startPageNumber > endPageNumber) {
                    throw new BusinessException(ErrorCode.REQUEST_20004);
                }
            }
            String appId = "WEB";
            SysSignApplication appInfo = getAppInfo(request);
            boolean flag = null != appInfo;
            appId = flag && null != appInfo.getUserAppId() ? appInfo.getUserAppId() : appId;
            if (flag) {
                // 获取pdf签章文件
                if (null == fastPerforationSignReqVO.getPdfbase64Str()) {
                    InMemoryMultipartFile inMemoryMultipartFile = fileService.fetch(fastPerforationSignReqVO.getFileCode());
                    fastPerforationSignReqVO.setPdfbase64Str(Base64Util.encode(inMemoryMultipartFile.getBytes()));
                    if (StringUtils.isBlank(fastPerforationSignReqVO.getOriginalFileName())) {
                        fastPerforationSignReqVO.setOriginalFileName(inMemoryMultipartFile.getOriginalFilename());
                    }
                }
                PerforationSignReqVO perforationSignReqVO = new PerforationSignReqVO();
                perforationSignReqVO.setSignOriginalFileStr(fastPerforationSignReqVO.getPdfbase64Str());
                long getCertTime = System.currentTimeMillis();
                // 根据章模编码获取章模路径、证书路径

                EsealResult<SignRespDTO> respResult = null;
                SignRespDTO signRespDTO = null;
                LogCommonParam logCommonParam = null;
                for(int i=0;i<fastPerforationSignReqVO.getList().size();i++){
                    FastPerforationSignVO fastPerforationSignVO=fastPerforationSignReqVO.getList().get(i);
                    signPageCompare(fastPerforationSignVO.getEndPageNo(), fastPerforationSignVO.getStartPageNo());
                    perforationSignReqVO.setCoordinateY(Float.parseFloat(fastPerforationSignVO.getCoordinateY()));
                    perforationSignReqVO.setFoggy(fastPerforationSignVO.isFoggy());
                    perforationSignReqVO.setGrey(fastPerforationSignVO.isGrey());
                    //页码
                    perforationSignReqVO.setStartPageNumber(Integer.parseInt(fastPerforationSignVO.getStartPageNo()));
                    perforationSignReqVO.setEndPageNumber(Integer.parseInt(fastPerforationSignVO.getEndPageNo()));
                    perforationSignReqVO.setSignatureDirection(fastPerforationSignVO.getSignatureDirection());

                    SignSysSealInfo signSysSealInfo = getSignSysSealInfo(fastPerforationSignVO.getSealCode());
                    SysCertInfo sysCertInfo = sysCertInfoMapper.selectOne(new QueryWrapper<SysCertInfo>().eq(CERT_ID, signSysSealInfo.getCertificateId()));
                    if (null == sysCertInfo) {
                        throw new ServiceException(ErrorCode.REQUEST_40255);
                    }
                    InMemoryMultipartFile inMemoryMultipartFile = fileService.fetch(sysCertInfo.getCertCode());
                    String certBase64 = Base64Util.inputStreamToBase64(inMemoryMultipartFile.getInputStream());
                    perforationSignReqVO.setCertStr(certBase64);
                    perforationSignReqVO.setCertPassword(sysCertInfo.getCertPassword());
                    perforationSignReqVO.setSealCode(signSysSealInfo.getPictureCode());
                    perforationSignReqVO.setSealStr(signSysSealInfo.getPictureData64());
                    perforationSignReqVO.setPageSize(fastPerforationSignVO.getPageSize());
                    long certTime = System.currentTimeMillis();
                    log.info(SEAL_CERT_TIME + (certTime - getCertTime));

                    respResult = signService.setPagePerforationSign(perforationSignReqVO);
                    long getSignTime = System.currentTimeMillis();
                    log.info(SIGN_TIME + (getSignTime - getCertTime));
                    IyinResultValidUtil.validateSuccess(respResult);
                    signRespDTO = respResult.getData();

                    perforationSignReqVO.setSignOriginalFileStr(signRespDTO.getSignCompleteFileStr());
                    String fileBase64 = signRespDTO.getSignCompleteFileStr();
                    logCommonParam = getLogCommonParam(fastPerforationSignReqVO.getUserId(), fileBase64, fastPerforationSignReqVO.getFileCode(), fastPerforationSignReqVO.getOriginalFileName());
                    signRespDTO.setFileCode(logCommonParam.getSignFileCode());
                    signRespDTO.setSignDate(logCommonParam.getSignDate());
                    logCommonParam.setSignatureModel(SignatureEnum.SIGNATURE_PERFORATION.getCode());
                    logCommonParam.setSignatureName(SignatureEnum.SIGNATURE_SINGLE.getCode());
                    logCommonSetter(appId, signSysSealInfo, logCommonParam, fastPerforationSignReqVO.getFileCode(), fastPerforationSignReqVO.getOriginalFileName());
                    String ipAddr = SystemUtils.getIpAddr(request);
                    logCommonParam.setIpAddress(ipAddr);
                    logCommonParam.setMacAddress(SystemUtils.getMacAddress(ipAddr));
                    logCommonParam.setDeviceName(SystemUtils.getHostName(ipAddr));
                    signLog(fastPerforationSignReqVO.getUserId(), logCommonParam, respResult.getData().getMultiParam());
                }
                log.info("快捷签-单文件骑缝坐标签章接口API总时间：" + (System.currentTimeMillis() - time));
                return respResult;
            } else {
                throw new ServiceException(ErrorCode.REQUEST_40307);
            }
        } catch (Exception e) {
            log.error(SIGN_EXCEPTION, e.getMessage());
            throw new BusinessException(ErrorCode.REQUEST_40472.getCode(), e.getMessage());
        }
    }

    @Override
    public EsealResult<SignRespDTO> singlePerforationCoordHalfSign(FastPerforationHalfSignReqVO fastPerforationHalfSignReqVO, HttpServletRequest request) {
        try {
            long time = System.currentTimeMillis();
            for(int i=0;i<fastPerforationHalfSignReqVO.getList().size();i++){
                FastPerforationHalfSignVO fastPerforationHalfSignVO=fastPerforationHalfSignReqVO.getList().get(i);
                signPageCompare(fastPerforationHalfSignVO.getEndPageNo(), fastPerforationHalfSignVO.getStartPageNo());
                //页码
                int startPageNumber = Integer.parseInt(fastPerforationHalfSignVO.getStartPageNo());
                int endPageNumber = Integer.parseInt(fastPerforationHalfSignVO.getEndPageNo());
                if (startPageNumber > endPageNumber) {
                    throw new BusinessException(ErrorCode.REQUEST_20004);
                }
            }
            String appId = "WEB";
            SysSignApplication appInfo = getAppInfo(request);
            boolean flag = null != appInfo;
            appId = flag && null != appInfo.getUserAppId() ? appInfo.getUserAppId() : appId;
            if (flag) {
                // 获取pdf签章文件
                if (null == fastPerforationHalfSignReqVO.getPdfbase64Str()) {
                    InMemoryMultipartFile inMemoryMultipartFile = fileService.fetch(fastPerforationHalfSignReqVO.getFileCode());
                    fastPerforationHalfSignReqVO.setPdfbase64Str(Base64Util.encode(inMemoryMultipartFile.getBytes()));
                    if (StringUtils.isBlank(fastPerforationHalfSignReqVO.getOriginalFileName())) {
                        fastPerforationHalfSignReqVO.setOriginalFileName(inMemoryMultipartFile.getOriginalFilename());
                    }
                }
                PerforationHalfSignReqVO perforationSignReqVO = new PerforationHalfSignReqVO();
                perforationSignReqVO.setSignOriginalFileStr(fastPerforationHalfSignReqVO.getPdfbase64Str());
                long getCertTime = System.currentTimeMillis();
                // 根据章模编码获取章模路径、证书路径

                EsealResult<SignRespDTO> respResult = null;
                SignRespDTO signRespDTO = null;
                LogCommonParam logCommonParam = null;
                for(int i=0;i<fastPerforationHalfSignReqVO.getList().size();i++){
                    FastPerforationHalfSignVO fastPerforationHalfSignVO=fastPerforationHalfSignReqVO.getList().get(i);
                    signPageCompare(fastPerforationHalfSignVO.getEndPageNo(), fastPerforationHalfSignVO.getStartPageNo());
                    perforationSignReqVO.setFoggy(fastPerforationHalfSignVO.isFoggy());
                    perforationSignReqVO.setGrey(fastPerforationHalfSignVO.isGrey());
                    perforationSignReqVO.setCoordinateY(Float.parseFloat(fastPerforationHalfSignVO.getCoordinateY()));
                    //页码
                    perforationSignReqVO.setStartPageNumber(Integer.parseInt(fastPerforationHalfSignVO.getStartPageNo()));
                    perforationSignReqVO.setEndPageNumber(Integer.parseInt(fastPerforationHalfSignVO.getEndPageNo()));
                    perforationSignReqVO.setSignatureDirection(fastPerforationHalfSignVO.getSignatureDirection());

                    SignSysSealInfo signSysSealInfo = getSignSysSealInfo(fastPerforationHalfSignVO.getSealCode());
                    SysCertInfo sysCertInfo = sysCertInfoMapper.selectOne(new QueryWrapper<SysCertInfo>().eq(CERT_ID, signSysSealInfo.getCertificateId()));
                    if (null == sysCertInfo) {
                        throw new ServiceException(ErrorCode.REQUEST_40255);
                    }
                    InMemoryMultipartFile inMemoryMultipartFile = fileService.fetch(sysCertInfo.getCertCode());
                    String certBase64 = Base64Util.inputStreamToBase64(inMemoryMultipartFile.getInputStream());
                    perforationSignReqVO.setCertStr(certBase64);
                    perforationSignReqVO.setCertPassword(sysCertInfo.getCertPassword());
                    perforationSignReqVO.setSealCode(signSysSealInfo.getPictureCode());
                    perforationSignReqVO.setSealStr(signSysSealInfo.getPictureData64());
                    long certTime = System.currentTimeMillis();
                    log.info(SEAL_CERT_TIME + (certTime - getCertTime));

                    respResult = signService.setPerforationHalfSign(perforationSignReqVO);
                    long getSignTime = System.currentTimeMillis();
                    log.info(SIGN_TIME + (getSignTime - getCertTime));
                    IyinResultValidUtil.validateSuccess(respResult);
                    signRespDTO = respResult.getData();

                    perforationSignReqVO.setSignOriginalFileStr(signRespDTO.getSignCompleteFileStr());
                    String fileBase64 = signRespDTO.getSignCompleteFileStr();
                    logCommonParam = getLogCommonParam(fastPerforationHalfSignReqVO.getUserId(), fileBase64, fastPerforationHalfSignReqVO.getFileCode(), fastPerforationHalfSignReqVO.getOriginalFileName());
                    signRespDTO.setFileCode(logCommonParam.getSignFileCode());
                    signRespDTO.setSignDate(logCommonParam.getSignDate());
                    logCommonParam.setSignatureModel(SignatureEnum.SIGNATURE_PERFORATION.getCode());
                    logCommonParam.setSignatureName(SignatureEnum.SIGNATURE_SINGLE.getCode());
                    logCommonSetter(appId, signSysSealInfo, logCommonParam, fastPerforationHalfSignReqVO.getFileCode(), fastPerforationHalfSignReqVO.getOriginalFileName());
                    String ipAddr = SystemUtils.getIpAddr(request);
                    logCommonParam.setIpAddress(ipAddr);
                    logCommonParam.setMacAddress(SystemUtils.getMacAddress(ipAddr));
                    logCommonParam.setDeviceName(SystemUtils.getHostName(ipAddr));
                    String[] multi=respResult.getData().getMultiParam().split("XXX");
                    for(int x=0;x<multi.length;x++){
                        signLog(fastPerforationHalfSignReqVO.getUserId(), logCommonParam, multi[x]);
                    }
                }
                log.info("快捷签-单文件骑缝坐标签章接口API总时间：" + (System.currentTimeMillis() - time));
                return respResult;
            } else {
                throw new ServiceException(ErrorCode.REQUEST_40307);
            }
        } catch (Exception e) {
            log.error(SIGN_EXCEPTION, e.getMessage());
            throw new BusinessException(ErrorCode.REQUEST_40472.getCode(), e.getMessage());
        }
    }

    /**
     * C++ uKey签章接入系统
     *
     * @param uKeySignReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<java.lang.Boolean>
     * @Author: yml
     * @CreateDate: 2019/7/31
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/31
     * @Version: 0.0.1
     */
    @Override
    public EsealResult<Boolean> uKeySign(UKeySignReqVO uKeySignReqVO, HttpServletRequest request) {
        boolean result = false;
        try {
            for (int i=0;i<uKeySignReqVO.getList().size();i++){
                UKeySignBaseVO uKeySignBaseVO=uKeySignReqVO.getList().get(i);
                LogCommonParam logCommonParam = getLogCommonParam(uKeySignReqVO.getUserId(), uKeySignReqVO.getUKeySignPdfBase64(), uKeySignReqVO.getFileCode(), uKeySignReqVO.getFileName());
                logCommonParam.setSignatureModel(SignatureEnum.SIGNATURE_U_KEY.getCode());
                logCommonParam.setSignatureName(SignatureEnum.SIGNATURE_U_KEY.getCode());
                SignSysSealInfo signSysSealInfo = new SignSysSealInfo();
                signSysSealInfo.setMediumType(PictureTypeEnum.UKEY_SEAL.getCode());
                signSysSealInfo.setPictureBusinessType(PictureBusinessTypeEnum.ESEAL_PRIVATE.getCode());
                signSysSealInfo.setPictureCode(uKeySignBaseVO.getSealCode());
                signSysSealInfo.setPictureName(uKeySignBaseVO.getSealName());
                logCommonSetter("UKey", signSysSealInfo, logCommonParam, uKeySignReqVO.getFileCode(), uKeySignReqVO.getFileName());
                MultiParam multiParam = new MultiParam();
                BeanUtils.copyProperties(uKeySignBaseVO,multiParam);
                String multiParamStr = toJSONString(multiParam);
                String ipAddr = SystemUtils.getIpAddr(request);
                logCommonParam.setIpAddress(ipAddr);
                logCommonParam.setMacAddress(SystemUtils.getMacAddress(ipAddr));
                logCommonParam.setDeviceName(SystemUtils.getHostName(ipAddr));
                signLog(uKeySignReqVO.getUserId(), logCommonParam, multiParamStr);
            }
            result = true;
        } catch (IOException e) {
            log.error("com.iyin.sign.system.service.impl.FastSignServiceImpl.uKeySign:exception:{}",e.getLocalizedMessage());
        }
        return new EsealResult<>(result);
    }

    /**
     * 签章日志
     *
     * @Author: yml
     * @CreateDate: 2019/9/16
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/16
     * @Version: 0.0.1
     * @param signLogReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignLogRespDTO>
     */
    @Override
    public EsealResult<IPage<SignLogRespDTO>> signLog(SignLogReqVO signLogReqVO, HttpServletRequest request) {
        IPage<SignLogRespDTO> signLogRespDTOIPage = new IyinPage<>(signLogReqVO.getCurrentPage(),signLogReqVO.getPageSize());
        String userId = getUserId(request);
        SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(userId);
        signLogReqVO.setOrgId(signSysUserInfo.getEnterpriseOrPersonalId());
        List<SignLogRespDTO> signLogRespDTOS = signatureLogMapper.signLog(signLogRespDTOIPage,signLogReqVO);
        signLogRespDTOIPage.setRecords(signLogRespDTOS);
        return new EsealResult<>(signLogRespDTOIPage);
    }

    /**
     * 签章
     *
     * @Author: yml
     * @CreateDate: 2019/10/29
     * @UpdateUser: yml
     * @UpdateDate: 2019/10/29
     * @Version: 0.0.1
     * @param signReqVO
     * @return : java.lang.String
     */
    @Override
    public String sign(SignReqVO signReqVO) throws IOException {
        String reqType = signReqVO.getReqType();
        String result = null;
        byte[] pdfBytes = fileService.fetchFileid(signReqVO.getPdfFileID());
        String pdfStr = Base64Util.encode(pdfBytes);
        byte[] sealBytes = fileService.fetchFileid(signReqVO.getSealFileID());
        String sealStr = Base64Util.encode(sealBytes);
        byte[] pfxBytes = fileService.fetchFileid(signReqVO.getPfxFileID());
        String pfxStr = Base64Util.encode(pfxBytes);
        String pfxPassword = signReqVO.getPfxPassword();
        String pageNum = signReqVO.getPage();
        String pageEnd = signReqVO.getPageEnd();
        String x = signReqVO.getX();
        String y = signReqVO.getY();
        byte[] bytes = new byte[0];
        if(SignatureEnum.SINGLE.getCode().equals(reqType)) {
            SingleSignReqVO reqVO = new SingleSignReqVO();
            reqVO.setSignOriginalFileStr(pdfStr);
            reqVO.setSealStr(sealStr);
            reqVO.setCertStr(pfxStr);
            reqVO.setCertPassword(pfxPassword);
            reqVO.setCoordinatex(Float.parseFloat(x));
            reqVO.setCoordinatey(Float.parseFloat(y));
            reqVO.setPageNumber(Integer.parseInt(pageNum));
            reqVO.setSealCode("1");
            bytes = signService.singleSign(reqVO);
        }else if(SignatureEnum.BATCH.getCode().equals(reqType)) {
            BatchSignReqVO reqVO = new BatchSignReqVO();
            reqVO.setSignOriginalFileStr(pdfStr);
            reqVO.setSealStr(sealStr);
            reqVO.setCertStr(pfxStr);
            reqVO.setCertPassword(pfxPassword);
            reqVO.setCoordinateX(Float.parseFloat(x));
            reqVO.setCoordinateY(Float.parseFloat(y));
            reqVO.setStartPageNumber(Integer.parseInt(pageNum));
            reqVO.setEndPageNumber(Integer.parseInt(pageEnd));
            reqVO.setSealCode("1");
            bytes = signService.batch(reqVO);
        }else if(SignatureEnum.PERFORATION.getCode().equals(reqType)) {
            PerforationSignReqVO reqVO = new PerforationSignReqVO();
            reqVO.setSignOriginalFileStr(pdfStr);
            reqVO.setSealStr(sealStr);
            reqVO.setCertStr(pfxStr);
            reqVO.setCertPassword(pfxPassword);
            reqVO.setCoordinateY(Float.parseFloat(y));
            reqVO.setStartPageNumber(Integer.parseInt(pageNum));
            reqVO.setEndPageNumber(Integer.parseInt(pageEnd));
            reqVO.setSealCode("1");
            reqVO.setPageSize(signReqVO.getPageSize());
            reqVO.setSignatureDirection(signReqVO.getSignatureDirection());
            bytes = signService.perforationSign(reqVO);
        }
        File tempFile = new File(fileTempPath, "temp.pdf");
        Base64Util.byteArrayToFile(bytes,tempFile.getCanonicalPath());
        result = fileService.upload(tempFile);
        Files.delete(tempFile.toPath());
        return result;
    }


    /**
     * @param accountId, logCommonParam, signatureResult
     * @param multiParameter
     * @return void
     * @Description 保存签章记录
     * @Author: wdf
     * @CreateDate: 2019/1/23 11:48
     * @UpdateUser: wdf
     * @UpdateDate: 2019/1/23 11:48
     * @Version: 0.0.1
     */
    private void signLog(String accountId, LogCommonParam logCommonParam, String multiParameter) {
        //       保存签章记录
        SignatureLogReqVO signatureLogReqVO = new SignatureLogReqVO();
        BeanUtils.copyProperties(logCommonParam, signatureLogReqVO);

        Date date = new Date();
        signatureLogReqVO.setCreateDate(date);
        signatureLogReqVO.setCreateUser(accountId);
        signatureLogReqVO.setUpdateDate(date);
        signatureLogReqVO.setUpdateUser(accountId);
        signatureLogReqVO.setSignatureResult(SignatureEnum.SIGNATURE_SUCCESS.getCode());
        signatureLogReqVO.setSignatureType(SignatureEnum.SIGNATURE_FAST.getCode());
        signatureLogReqVO.setUserId(accountId);
        if (StringUtils.isEmpty(signatureLogReqVO.getSignFileHash())) {
            signatureLogReqVO.setSignatureResult(SignatureEnum.SIGNATURE_FAILURE.getCode());
        }
        asyncService.addLogSaveFile(signatureLogReqVO, multiParameter);
    }

    /**
     * @param singleSignReqVO, pdfbase64Str, keyword, keywordIndex, keywordOffsetX, keywordOffsetY
     * @return void
     * @Description 关键字坐标处理
     * @Author: wdf
     * @CreateDate: 2018/12/28 16:06
     * @UpdateUser: wdf
     * @UpdateDate: 2018/12/28 16:06
     * @Version: 0.0.1
     */
    private void ckKeyWord(SingleSignReqVO singleSignReqVO, String pdfbase64Str, String keyword, String keywordIndex, String keywordOffsetX, String keywordOffsetY) throws IOException {
        if (StringUtils.isBlank(keyword)) {
            throw new BusinessException(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), "关键字不能为空");
        }
        if (StringUtils.isBlank(keywordIndex)) {
            throw new BusinessException(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), "关键字索引号不能为空");
        }
        if (StringUtils.isBlank(keywordOffsetX)) {
            throw new BusinessException(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), "关键字x轴偏移量不能为空");
        }
        if (StringUtils.isBlank(keywordOffsetY)) {
            throw new BusinessException(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), "关键字y轴偏移量不能为空");
        }
        //获取关键字坐标
        List<float[]> keyWordList = new BoxKeyPosition(keyword,Base64Util.decode(pdfbase64Str)).getPosition();
        if (keyWordList.isEmpty()) {
            throw new BusinessException(ErrorCode.REQUEST_40424);
        }
        int keyWordIndex = Integer.parseInt(keywordIndex);
        if (keyWordIndex > keyWordList.size()) {
            throw new BusinessException(ErrorCode.REQUEST_40413);
        }
        float[] objects = keyWordList.get(keyWordIndex - 1);
        float pointx = objects[0];
        float pointy = objects[1];
        singleSignReqVO.setCoordinatex(pointx + Float.parseFloat(keywordOffsetX));
        singleSignReqVO.setCoordinatey(pointy + Float.parseFloat(keywordOffsetY));
        //页码
        singleSignReqVO.setPageNumber((int)objects[2]);
    }

    /**
     * @param singleSignReqDTO, coordinatex, coordinatey, pageNo
     * @return void
     * @Description 坐标签参数校验
     * @Author: wdf
     * @CreateDate: 2019/3/7 17:10
     * @UpdateUser: wdf
     * @UpdateDate: 2019/3/7 17:10
     * @Version: 0.0.1
     */
    private void ckCoordData(SingleSignReqVO singleSignReqDTO, String coordinatex, String coordinatey, String pageNo) {
        if (StringUtils.isBlank(coordinatex)) {
            throw new BusinessException(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), "签章X坐标轴不能为空");
        }
        if (StringUtils.isBlank(coordinatey)) {
            throw new BusinessException(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), "签章Y坐标轴不能为空");
        }
        if (StringUtils.isBlank(pageNo)) {
            throw new BusinessException(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), "签章页数不能为空");
        }
        singleSignReqDTO.setCoordinatex(Float.parseFloat(coordinatex));
        singleSignReqDTO.setCoordinatey(Float.parseFloat(coordinatey));
        //页码
        singleSignReqDTO.setPageNumber(Integer.parseInt(pageNo));
    }

    @Data
    private static class LogCommonParam {
        @ApiModelProperty("应用ID")
        private String appId;
        @ApiModelProperty("印章名称")
        private String sealName;
        @ApiModelProperty("印章编码")
        private String sealCode;
        @ApiModelProperty("章模类型：行政章，人事章")
        private String sealType;
        @ApiModelProperty("介质类型：01 云印章，02云签名，03 ukey印章，04ukey签名")
        private String mediumType;
        @ApiModelProperty("签章模式(01:关键字签章，02:坐标签章)")
        private String signatureModel;
        @ApiModelProperty(value = "签章方式(01:单页签章，02:多页签章，03:骑缝签章)")
        private String signatureName;
        @ApiModelProperty("文件名称")
        private String fileName;
        @ApiModelProperty("文件编码")
        private String fileCode;
        @ApiModelProperty("签后文档HASH")
        private String signFileHash;
        @ApiModelProperty("签后文档ID")
        private String signFileCode;
        @ApiModelProperty("文档页数")
        private Integer page;
        @ApiModelProperty("签署时间")
        private LocalDateTime signDate;
        @ApiModelProperty("签章ip")
        private String ipAddress;
        @ApiModelProperty("签章mac")
        private String macAddress;
        @ApiModelProperty("签署设备")
        private String deviceName;
    }

}