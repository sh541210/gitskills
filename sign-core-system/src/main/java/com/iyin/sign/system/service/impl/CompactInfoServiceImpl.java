package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.iyin.sign.system.common.enums.AppEnum;
import com.iyin.sign.system.common.enums.FileManageEnum;
import com.iyin.sign.system.common.enums.SignatureEnum;
import com.iyin.sign.system.common.enums.UseSealSourceEnum;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.common.utils.Slf4jUtil;
import com.iyin.sign.system.common.utils.msg.SendMessageUtil;
import com.iyin.sign.system.entity.*;
import com.iyin.sign.system.mapper.*;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.FileResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.model.exception.IyinResultValidUtil;
import com.iyin.sign.system.service.*;
import com.iyin.sign.system.service.feign.SealManagerService;
import com.iyin.sign.system.util.*;
import com.iyin.sign.system.vo.file.FileResourceDto;
import com.iyin.sign.system.vo.file.FileUploadRespDto;
import com.iyin.sign.system.vo.file.req.*;
import com.iyin.sign.system.vo.file.resp.*;
import com.iyin.sign.system.vo.req.DocPackageReqDto;
import com.iyin.sign.system.vo.req.UploadReqVO;
import com.iyin.sign.system.vo.resp.SignSysUserInfoRespVO;
import com.iyin.sign.system.vo.sign.req.*;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.JSON.parseArray;
import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * @ClassName: CompactInfoServiceImpl
 * @Description:
 * @Author: yml
 * @CreateDate: 2019/8/7
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
@Slf4j
@Service
public class CompactInfoServiceImpl extends ServiceImpl<ISignSysCompactInfoMapper, SignSysCompactInfo> implements ICompactInfoService {

    /**
     * 待签人数
     */
    private static final int TO_DO_SIGN = 2;
    /**
     * 半小时
     */
    private static final int HALF_HOUR = 30;
    /**
     * excel
     */
    private static final String SUFFIX_2003 = ".xls";
    private static final String SUFFIX_2007 = ".xlsx";
    private static final int ZERO = 0;
    private static final int FIRST = 1;
    private static final int SECOND = 2;
    private static final int WIDTH = 18;
    private List<InitiateContractReqVO.SignInfo> exceptionList;
    private static final int MORE_THAN_TWO = 2;
    private static final String API_TOKEN = "api_token";
    private static final String SESSION_TOKEN = "session_token";
    private static final String CONTRACT_NO_EXIST = "合同不存在";
    private static final String QR_CODE = "/sign/verify/qrCode/";

    @Value("${contract.start}")
    private String start;
    @Value("${contract.urge}")
    private String urge;
    @Value("${contract.refuse}")
    private String refuse;
    @Value("${contract.revoke}")
    private String revoke;
    @Value("${contract.complete}")
    private String complete;
    @Value("${contract.h5_link}")
    private String h5Link;
    @Value("${email.picture-url}")
    private String emailPictureUrl;
    @Value("${email.head}")
    private String emailHead;
    @Value("${email.dear}")
    private String emailDear;
    @Value("${email.before}")
    private String emailBefore;
    @Value("${email.body}")
    private String emailBody;
    @Value("${email.foot}")
    private String emailFoot;
    @Value("${email.qq}")
    private String emailQQ;
    @Value("${email.phone}")
    private String emailPhone;
    /**
     * 白鹤 配置
     */
    @Value("${private-baihe.appKey}")
    public String appKey;

    /**
     * 白鹤 配置
     */
    @Value("${private-baihe.secret}")
    public String secret;
    /**
     * 白鹤 配置
     */
    @Value("${private-baihe.companyId}")
    private String companyId;

    private final IFileService fileService;
    private final DocumentApiServiceImpl documentApiService;
    @Resource
    private FileResourceMapper fileResourceMapper;
    @Resource
    private ISignSysCompactFileMapper signSysCompactFileMapper;
    @Resource
    private ISignSysCompactInfoMapper signSysCompactInfoMapper;
    @Resource
    private ISignSysCompactCopyMapper signSysCompactCopyMapper;
    @Resource
    private ISignSysCompactFieldMapper signSysCompactFieldMapper;
    @Resource
    private ISignSysCompactSignatoryMapper signSysCompactSignatoryMapper;
    @Resource
    private ISignSysCompactLogMapper signSysCompactLogMapper;
    @Resource
    private SignSysUserInfoMapper signSysUserInfoMapper;
    @Resource
    private SignSysPrintAuthUserMapper signSysPrintAuthUserMapper;
    @Resource
    private SignSysSignConfigMapper signSysSignConfigMapper;
    @Resource
    private ISysSignatureLogMapper sysSignatureLogMapper;
    @Resource
    private SignSysUseSealMapper sysUseSealMapper;

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private ShortLinkService shortLinkService;
    @Autowired
    private SealManagerService sealManagerService;
    @Autowired
    private SignSysUserInfoService signSysUserInfoService;

    private final IFastSignService fastSignService;
    private final RedisService redisService;
    private final SendMessageUtil sendMessageUtil;
    private final IAuthTokenService authTokenService;

    @Autowired
    public CompactInfoServiceImpl(IFileService fileService, DocumentApiServiceImpl documentApiService, IFastSignService fastSignService, RedisService redisService, SendMessageUtil sendMessageUtil, IAuthTokenService authTokenService) {
        this.fileService = fileService;
        this.documentApiService = documentApiService;
        this.fastSignService = fastSignService;
        this.redisService = redisService;
        this.sendMessageUtil = sendMessageUtil;
        this.authTokenService = authTokenService;
    }

    /**
     * 文件上传
     *
     * @param file
     * @param request
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO>
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<CompactFileUploadRespDTO> upload(MultipartFile file,HttpServletRequest request) {
        String userId=ckToken(request);
        FileUploadRespDto fileUploadRespDto = documentApiService.getFileUploadRespDto(file, userId);
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
                return IyinResult.success(compactFileUploadRespDTO);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
            }
        }
        return IyinResult.getIyinResult(ErrorCode.REQUEST_80001);
    }

    /**
     * 文件重新上传
     *
     * @param file
     * @param request
     * @param fileCode
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO>
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<CompactFileUploadRespDTO> reUpload(MultipartFile file, String fileCode,HttpServletRequest request) {
        ckToken(request);
        Boolean del = fileService.del(fileCode);
        if (Boolean.TRUE.equals(del)) {
            return this.upload(file, request);
        }
        return IyinResult.getIyinResult(ErrorCode.REQUEST_80002);
    }

    /**
     * 文件删除
     *
     * @param fileCode
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     * @Author: yml
     * @CreateDate: 2019/8/7
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/7
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<Boolean> delete(String fileCode,HttpServletRequest request) {
        ckToken(request);
        return IyinResult.success(fileService.del(fileCode));
    }

    /**
     * 发起页面
     *
     * @param fileManageInfoReqVO
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.String>
     * @Author: yml
     * @CreateDate: 2019/8/14
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/14
     * @Version: 0.0.1
     */
    @Transactional(rollbackFor = Exception.class)
    public IyinResult<String> initiate(FileManageInfoReqVO fileManageInfoReqVO) {
        SignSysCompactInfo signSysCompactInfo = new SignSysCompactInfo();
        String id = SnowflakeIdWorker.getIdToStr();
        signSysCompactInfo.setId(id);
        signSysCompactInfo.setCompactTheme(fileManageInfoReqVO.getCompactTheme());
        signSysCompactInfo.setSignatoryWay(fileManageInfoReqVO.getSignatoryWay());
        String signWay = fileManageInfoReqVO.getSignWay();
        signSysCompactInfo.setSignWay(signWay);
        signSysCompactInfo.setOrgId(fileManageInfoReqVO.getOrgId());
        String userId = fileManageInfoReqVO.getUserId();
        signSysCompactInfo.setUserId(userId);
        signSysCompactInfo.setCompactStatus(FileManageEnum.DRAFT.getCode());
        signSysCompactInfo.setType(fileManageInfoReqVO.getType());
        Date gmtCreate = new Date();
        signSysCompactInfo.setGmtCreate(gmtCreate);
        signSysCompactInfo.setCreatUser(userId);
        signSysCompactInfo.setModifyUser(userId);
        boolean save = save(signSysCompactInfo);
        if (save) {
            int relt = getRelt(
                    new SaveFileReqVo(fileManageInfoReqVO.getFileCode(), fileManageInfoReqVO.getFilePage(), "01", id,
                            userId, gmtCreate, null,null));
            if (0 < relt) {
                String nextSignatoryId;
                boolean first = true;
                List<FileManageInfoReqVO.SignInfo> signInfos = fileManageInfoReqVO.getSignInfos();
                for (int i = 0; i < signInfos.size(); i++) {
                    String status = FileManageEnum.SOMEONE_TODO.getCode();
                    FileManageInfoReqVO.SignInfo signInfo = signInfos.get(i);
                    if (FileManageEnum.SIGN.getCode().equals(signInfo.getType()) && first) {
                        status = FileManageEnum.TODO.getCode();
                        first = false;
                    }
                    FileManageInfoReqVO.SignInfo signInfo1;
                    if (signInfos.size() == (i + 1)) {
                        nextSignatoryId = null;
                    } else {
                        int j = i + 1;
                        do {
                            signInfo1 = signInfos.get(j);
                            j++;
                        } while (signInfo1.getType().equals(FileManageEnum.CARBON_COPY.getCode()) &&
                                j != signInfos.size());
                        nextSignatoryId = signInfo1.getSignatoryId();
                    }
                    Signer signer = new Signer();
                    signer.setCompactId(id);
                    signer.setUserId(userId);
                    signer.setGmtCreate(gmtCreate);
                    signer.setSignWay(signWay);
                    signer.setSignStatus(status);
                    signer.setNextSignatoryId(nextSignatoryId);
                    signer.setLogicalNumber(i);
                    InitiateContractReqVO.SignInfo signInfo2 = new InitiateContractReqVO.SignInfo();
                    BeanUtils.copyProperties(signInfo,signInfo2);
                    doOperateSignInfo(signer, signInfo2);
                }
                return IyinResult.success(id);
            }
        }
        return IyinResult.getIyinResult(ErrorCode.REQUEST_80003);
    }

    /**
     * 保存文件、附件
     *
     *
     * @param saveFileReqVo@return : int
     * @Author: yml
     * @CreateDate: 2019/8/14
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/14
     * @Version: 0.0.1
     */
    private int getRelt(SaveFileReqVo saveFileReqVo) {
        SignSysCompactFile signSysCompactFile = new SignSysCompactFile();
        signSysCompactFile.setId(SnowflakeIdWorker.getIdToStr());
        signSysCompactFile.setCompactId(saveFileReqVo.getContractId());
        signSysCompactFile.setFileCode(saveFileReqVo.getFileCode());
        signSysCompactFile.setFileCodeOrigin(saveFileReqVo.getFileCodeOrigin());
        signSysCompactFile.setFileName(saveFileReqVo.getFileName());
        signSysCompactFile.setFileType(saveFileReqVo.getFileType());
        signSysCompactFile.setPageTotal(saveFileReqVo.getPageTotal());
        signSysCompactFile.setGmtCreate(saveFileReqVo.getGmtCreate());
        signSysCompactFile.setCreatUser(saveFileReqVo.getUserId());
        signSysCompactFile.setModifyUser(saveFileReqVo.getUserId());
        return signSysCompactFileMapper.insert(signSysCompactFile);
    }

    /**
     * 保存签署信息
     *
     * @param signer
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/14
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/14
     * @Version: 0.0.1
     */
    private void insertSigner(Signer signer) {
        SignSysCompactSignatory signSysCompactSignatory = new SignSysCompactSignatory();
        signSysCompactSignatory.setId(SnowflakeIdWorker.getIdToStr());
        signSysCompactSignatory.setCompactId(signer.getCompactId());
        signSysCompactSignatory.setSignatoryId(signer.getSignatoryId());
        signSysCompactSignatory.setNextSignatoryId(signer.getNextSignatoryId());
        signSysCompactSignatory.setSignName(signer.getSignName());
        signSysCompactSignatory.setSignContact(signer.getSignContact());
        signSysCompactSignatory.setSignStatus(signer.getSignStatus());
        signSysCompactSignatory.setSerialNumber(signer.getLogicalNumber());
        signSysCompactSignatory.setGmtCreate(signer.getGmtCreate());
        signSysCompactSignatory.setCreatUser(signer.getUserId());
        signSysCompactSignatory.setModifyUser(signer.getUserId());
        signSysCompactSignatoryMapper.insert(signSysCompactSignatory);
    }


    /**
     * 签署
     *
     * @param fileManageSignInfoReqVO
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IyinResult<String> initiateSign(FileManageSignInfoReqVO fileManageSignInfoReqVO) {
        FileManageInfoReqVO fileManageInfoReqVO = new FileManageInfoReqVO();
        BeanUtils.copyProperties(fileManageSignInfoReqVO, fileManageInfoReqVO);
        fileManageInfoReqVO.setSignInfos(fileManageSignInfoReqVO.getSignInfos());
        IyinResult<String> initiate = this.initiate(fileManageInfoReqVO);
        IyinResultValidUtil.validateSuccess(initiate);
        String compactId = initiate.getData();
        List<CompactFieldInfoReqVO> compactFieldInfoReqVOList = fileManageSignInfoReqVO.getCompactFieldInfoReqVOList();
        Date gmtCreate = new Date();
        String userId = fileManageSignInfoReqVO.getUserId();
        for (CompactFieldInfoReqVO compactFieldInfoReqVO : compactFieldInfoReqVOList) {
            saveCompactField(compactId, userId, gmtCreate, compactFieldInfoReqVO, FileManageEnum.FIELD_SEAL.getCode());
        }
        int relt = signSysCompactInfoMapper.updateStatus(compactId, FileManageEnum.SIGNING.getCode(), null);
        return IyinResult.success(relt > 0 ? compactId : null);
    }

    /**
     * 查询
     *
     * @param content
     * @param pageNum
     * @param pageSize
     * @param orgId
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.vo.file.resp.FileManageQueryRespDTO>>
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<IyinPage<FileManageQueryRespDTO>> queryByContent(String content, Integer pageNum, Integer pageSize, String orgId, HttpServletRequest request) {
        IyinPage<FileManageQueryRespDTO> page = new IyinPage<>(pageNum, pageSize);
        String userId=ckToken(request);
        List<FileManageQueryRespDTO> fileManageQueryRespDTOS = fileResourceMapper
                .queryFileManageByName(page, content, userId, FileManageEnum.FILE_MANAGE.getCode(), orgId);
        page.setRecords(fileManageQueryRespDTOS);
        return IyinResult.success(page);
    }

    /**
     * 签名域
     *
     * @param fileCode
     * @param request
     * @return : com.iyin.sign.system.model.IyinResult<java.util.List<com.iyin.sign.system.vo.file.resp.CompactFieldInfoRespDTO>>
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<List<CompactFieldInfoRespDTO>> signDomain(String fileCode, HttpServletRequest request) {
        String userId=ckToken(request);
        List<SignSysCompactField> signSysCompactFields = signSysCompactFieldMapper.selectList(
                Wrappers.<SignSysCompactField>query().lambda().eq(SignSysCompactField::getCompactFileCode, fileCode)
                        .eq(SignSysCompactField::getSignatoryId, userId));
        String s = toJSONString(signSysCompactFields);
        List<CompactFieldInfoRespDTO> compactFieldInfoRespDTO = parseArray(s, CompactFieldInfoRespDTO.class);
        return IyinResult.success(compactFieldInfoRespDTO);
    }

    /**
     * 查看文件
     *
     * @param fileCode
     * @param request
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.resp.FileDetailRespDTO>
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<FileDetailRespDTO> viewFile(String fileCode, HttpServletRequest request) {
        String userId=ckToken(request);
        List<FileDetailRespDTO> detailRespDTOS = signSysCompactInfoMapper.getDetail(fileCode, userId);
        FileDetailRespDTO fileDetailRespDTO = null;
        FileDetailRespDTO fileDetailResp = new FileDetailRespDTO();
        for (FileDetailRespDTO detailRespDTO : detailRespDTOS) {
            if (null == fileDetailResp.getId()) {
                fileDetailResp = detailRespDTO;
            }
            if (userId.equals(detailRespDTO.getCarbonCopy()) || userId.equals(detailRespDTO.getSignatoryId())) {
                fileDetailRespDTO = detailRespDTO;
            }
            if (null != fileDetailRespDTO) {
                break;
            }
        }
        if (null == fileDetailRespDTO) {
            fileDetailRespDTO = fileDetailResp;
            fileDetailRespDTO.setSignStatus(null);
        }
        return IyinResult.success(fileDetailRespDTO);
    }

    /**
     * 签署
     *
     * @param fileManageSignReqVO
     * @param userId
     * @param req
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.String>
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IyinResult<Boolean> sign(FileManageSignReqVO fileManageSignReqVO, String userId, HttpServletRequest req) {
        String compactId = fileManageSignReqVO.getCompactId();
        SignSysCompactSignatory signSysCompactSignatory = signSysCompactSignatoryMapper.selectOne(
                Wrappers.<SignSysCompactSignatory>query().lambda()
                        .eq(SignSysCompactSignatory::getCompactId, compactId)
                        .eq(SignSysCompactSignatory::getSignatoryId, userId)
                        .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.TODO.getCode()));
        if (null != signSysCompactSignatory) {
            List<SignSysCompactSignatory> signSysCompactSignatories = signSysCompactSignatoryMapper.selectList(
                    Wrappers.<SignSysCompactSignatory>query().lambda()
                            .eq(SignSysCompactSignatory::getCompactId, compactId).and(wrapper -> wrapper
                            .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.TODO.getCode()).or()
                            .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.SOMEONE_TODO.getCode()))
                            .isNull(SignSysCompactSignatory::getSignDate));
            if ((null != signSysCompactSignatories) && (TO_DO_SIGN > signSysCompactSignatories.size())) {
                signSysCompactSignatoryMapper
                        .updateByCompactId(compactId, FileManageEnum.PASS.getCode());
                signSysCompactInfoMapper
                        .updateStatus(compactId, FileManageEnum.COMPLETE.getCode(),
                                new Date());
            } else {
                signSysCompactSignatory.setSignStatus(FileManageEnum.PASS.getCode());
                signSysCompactSignatory.setSignDate(new Date());
                signSysCompactSignatoryMapper.updateById(signSysCompactSignatory);
                SignSysCompactSignatory signSysCompactSignatory1 = signSysCompactSignatoryMapper.selectOne(
                        Wrappers.<SignSysCompactSignatory>lambdaQuery()
                                .eq(SignSysCompactSignatory::getCompactId, compactId)
                                .eq(SignSysCompactSignatory::getSignatoryId,
                                        signSysCompactSignatory.getNextSignatoryId()));
                if(null != signSysCompactSignatory1){
                    signSysCompactSignatory1.setSignStatus(FileManageEnum.TODO.getCode());
                    signSysCompactSignatoryMapper.updateById(signSysCompactSignatory1);
                }
            }
            SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper
                    .selectById(compactId);
            SignSysSignConfig signSysSignConfig = signSysSignConfigMapper.getSignConfig();
            signSysCompactInfo.setQrCode(
                    1 == signSysSignConfig.getQrCode() ? QR_CODE + compactId : null);
            signSysCompactInfo.setVerificationCode(
                    1 == signSysSignConfig.getVerificationCode() ? MD5Util.getStringMD5(signSysCompactInfo.toString()) :
                            null);
            int gmtVerification = signSysSignConfig.getGmtVerification();
            signSysCompactInfo.setVerificationDate(
                    0 == gmtVerification ? "0" : LocalDate.now().plusDays(gmtVerification - 1L).toString());
            signSysCompactInfoMapper.updateById(signSysCompactInfo);
            saveOperateLog(compactId, FileManageEnum.LOG_SIGN.getCode(), userId);
            signatureFile(fileManageSignReqVO, userId, req);
            return IyinResult.success(true);
        } else {
            return IyinResult.getIyinResult(ErrorCode.REQUEST_80004);
        }
    }

    /**
     * 签章
     *
     * @param fileManageSignReqVO
     * @param userId
     * @param req
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     */
    private void signatureFile(FileManageSignReqVO fileManageSignReqVO, String userId, HttpServletRequest req) {
        log.info("com.iyin.sign.system.service.impl.CompactInfoServiceImpl.signatureFile,fileManageSignReqVO:{}",
                fileManageSignReqVO);
        String signatureMethod = fileManageSignReqVO.getSignatureMethod();
        if (SignatureEnum.SIGNATURE_SINGLE.getCode().equals(signatureMethod)) {
            SingleFastSignReqVO singleFastSignReqVO = new SingleFastSignReqVO();
            singleFastSignReqVO.setFileCode(fileManageSignReqVO.getCompactFileCode());

            singleFastSignReqVO.setUserId(userId);
            @Valid List<SingleFastParamSignVO> list = Lists.newArrayList();
            SingleFastParamSignVO singleFastParamSignVO = new SingleFastParamSignVO();
            singleFastParamSignVO.setCoordinateX(fileManageSignReqVO.getSignatureCoordinateX().toString());
            singleFastParamSignVO.setCoordinateY(fileManageSignReqVO.getSignatureCoordinateY().toString());
            singleFastParamSignVO.setPageNo(fileManageSignReqVO.getSignatureStartPage().toString());
            singleFastParamSignVO.setSealCode(fileManageSignReqVO.getSealCode());
            singleFastParamSignVO.setFoggy(fileManageSignReqVO.isFoggy());
            singleFastParamSignVO.setGrey(fileManageSignReqVO.isGrey());
            list.add(singleFastParamSignVO);
            singleFastSignReqVO.setList(list);
            singleFastSignReqVO.setOriginalFileName(fileManageSignReqVO.getOriginalFileName());
            singleFastSignReqVO.setSignatureMethod(SignatureEnum.SIGNATURE_COORDINATE.getCode());
            fastSignService.singleSign(singleFastSignReqVO, req);
        } else if (SignatureEnum.SIGNATURE_MORE.getCode().equals(signatureMethod)) {
            SingleFastSignMoreReqVO singleFastSignMoreReqVO = new SingleFastSignMoreReqVO();
            singleFastSignMoreReqVO.setUserId(userId);
            singleFastSignMoreReqVO.setOriginalFileName(fileManageSignReqVO.getOriginalFileName());
            singleFastSignMoreReqVO.setFileCode(fileManageSignReqVO.getCompactFileCode());

            @Valid List<SingleFastCoordBatchSignVO> singleFastCoordBatchSignVOS = Lists.newArrayList();
            SingleFastCoordBatchSignVO singleFastCoordBatchSignVO = new SingleFastCoordBatchSignVO();
            singleFastCoordBatchSignVO.setSealCode(fileManageSignReqVO.getSealCode());
            singleFastCoordBatchSignVO.setCoordinateX(fileManageSignReqVO.getSignatureCoordinateX().toString());
            singleFastCoordBatchSignVO.setCoordinateY(fileManageSignReqVO.getSignatureCoordinateY().toString());
            singleFastCoordBatchSignVO.setStartPageNo(fileManageSignReqVO.getSignatureStartPage().toString());
            singleFastCoordBatchSignVO.setEndPageNo(fileManageSignReqVO.getSignatureEndPage().toString());
            singleFastCoordBatchSignVO.setFoggy(fileManageSignReqVO.isFoggy());
            singleFastCoordBatchSignVO.setGrey(fileManageSignReqVO.isGrey());
            singleFastCoordBatchSignVOS.add(singleFastCoordBatchSignVO);
            singleFastSignMoreReqVO.setList(singleFastCoordBatchSignVOS);
            fastSignService.singleCoordBatchSign(singleFastSignMoreReqVO, req);
        } else if (SignatureEnum.SIGNATURE_PERFORATION.getCode().equals(signatureMethod)) {
            FastPerforationSignReqVO perforationSignReqVO = new FastPerforationSignReqVO();
            perforationSignReqVO.setUserId(userId);
            perforationSignReqVO.setOriginalFileName(fileManageSignReqVO.getOriginalFileName());
            perforationSignReqVO.setFileCode(fileManageSignReqVO.getCompactFileCode());
            FastPerforationSignVO fastPerforationSignVO = new FastPerforationSignVO();
            fastPerforationSignVO.setFoggy(fileManageSignReqVO.isFoggy());
            fastPerforationSignVO.setGrey(fileManageSignReqVO.isGrey());
            fastPerforationSignVO.setSealCode(fileManageSignReqVO.getSealCode());
            fastPerforationSignVO.setCoordinateY(fileManageSignReqVO.getSignatureCoordinateY().toString());
            fastPerforationSignVO.setStartPageNo(fileManageSignReqVO.getSignatureStartPage().toString());
            fastPerforationSignVO.setEndPageNo(fileManageSignReqVO.getSignatureEndPage().toString());
            fastPerforationSignVO.setPageSize(fileManageSignReqVO.getPageSize());
            fastPerforationSignVO.setSignatureDirection(
                    fileManageSignReqVO.getSignatureCoordinateX().compareTo(BigDecimal.ZERO) == 0 ?
                            SignatureEnum.SIGNATURE_PERFORATION_LEFT.getCode() :
                            SignatureEnum.SIGNATURE_PERFORATION_RIGHT.getCode());
            @Valid List<FastPerforationSignVO> fastPerforationSignVOS = Lists.newArrayList();
            fastPerforationSignVOS.add(fastPerforationSignVO);
            perforationSignReqVO.setList(fastPerforationSignVOS);
            fastSignService.singlePerforationCoordSign(perforationSignReqVO, req);
        } else if (SignatureEnum.SIGNATURE_CONTINUOUS.getCode().equals(signatureMethod)) {
            FastPerforationHalfSignReqVO perforationHalfSignReqVO = new FastPerforationHalfSignReqVO();
            perforationHalfSignReqVO.setFileCode(fileManageSignReqVO.getCompactFileCode());
            perforationHalfSignReqVO.setUserId(userId);
            perforationHalfSignReqVO.setOriginalFileName(fileManageSignReqVO.getOriginalFileName());
            @Valid List<FastPerforationHalfSignVO> list = Lists.newArrayList();
            FastPerforationHalfSignVO fastPerforationHalfSignVO = new FastPerforationHalfSignVO();
            fastPerforationHalfSignVO.setFoggy(fileManageSignReqVO.isFoggy());
            fastPerforationHalfSignVO.setGrey(fileManageSignReqVO.isGrey());
            fastPerforationHalfSignVO.setSealCode(fileManageSignReqVO.getSealCode());
            fastPerforationHalfSignVO.setCoordinateY(fileManageSignReqVO.getSignatureCoordinateY().toString());
            fastPerforationHalfSignVO.setStartPageNo(fileManageSignReqVO.getSignatureStartPage().toString());
            fastPerforationHalfSignVO.setEndPageNo(fileManageSignReqVO.getSignatureEndPage().toString());
            fastPerforationHalfSignVO.setSignatureDirection(
                    fileManageSignReqVO.getSignatureCoordinateX().compareTo(BigDecimal.ZERO) == 0 ?
                            SignatureEnum.SIGNATURE_PERFORATION_LEFT.getCode() :
                            SignatureEnum.SIGNATURE_PERFORATION_RIGHT.getCode());
            list.add(fastPerforationHalfSignVO);
            perforationHalfSignReqVO.setList(list);
            fastSignService.singlePerforationCoordHalfSign(perforationHalfSignReqVO, req);
        }
    }

    /**
     * 删除
     *
     * @param compactId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.String>
     * @Author: yml
     * @CreateDate: 2019/8/9
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/9
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<Boolean> delStatus(String compactId) {
        int i = signSysCompactInfoMapper.deleteById(compactId);
        return IyinResult.success(0 < i);
    }

    /**
     * 合同查询
     *
     * @param contractQueryReqVO
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.vo.file.resp.ContractQueryRespDTO>>
     * @Author: yml
     * @CreateDate: 2019/8/13
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/13
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<IyinPage<ContractQueryRespDTO>> queryCompact(ContractQueryReqVO contractQueryReqVO, String userId) {
        IyinPage<ContractQueryRespDTO> iyinPage = new IyinPage<>(contractQueryReqVO.getPageNum(),
                contractQueryReqVO.getPageSize());
        List<ContractQueryRespDTO> contractQueryRespDTOS = signSysCompactInfoMapper
                .queryCompact(iyinPage, contractQueryReqVO);
        contractQueryRespDTOS.forEach(contractQueryRespDTO -> {
            String contractId = contractQueryRespDTO.getCompactId();
            List<SignSysCompactSignatory> signSysCompactSignatories = signSysCompactSignatoryMapper.selectList(
                    Wrappers.<SignSysCompactSignatory>query().lambda()
                            .eq(SignSysCompactSignatory::getCompactId, contractId));
            AtomicReference<String> signer = new AtomicReference<>("");
            signSysCompactSignatories.forEach(signSysCompactSignatory -> {
                SignSysUserInfo signSysUserInfo = signSysUserInfoMapper
                        .selectById(signSysCompactSignatory.getSignatoryId());
                String name;
                if (null != signSysUserInfo) {
                    name = StringUtils.isEmpty(signSysUserInfo.getUserNickName()) ? signSysUserInfo.getUserName() :
                            signSysUserInfo.getUserNickName();
                } else {
                    name = signSysCompactSignatory.getSignName();
                }
                signer.set(signer.get() + "," + name);
            });
            if (!StringUtils.isBlank(signer.get())) {
                contractQueryRespDTO.setSigner(signer.get().substring(FIRST));
            }
        });
        iyinPage.setRecords(contractQueryRespDTOS);
        return IyinResult.success(iyinPage);
    }

    /**
     * 存草稿
     *
     * @param initiateContractReqVO
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.String>
     * @Author: yml
     * @CreateDate: 2019/8/14
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/14
     * @Version: 0.0.1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IyinResult<String> draftContract(InitiateContractReqVO initiateContractReqVO) {
        String no = "no";
        if(!no.equals(initiateContractReqVO.getDeleteFlag())){
            deleteRelationData(initiateContractReqVO.getContractId());
        }
        String signWay = initiateContractReqVO.getSignWay();
        if (FileManageEnum.INDIVIDUALLY_SIGN.getCode().equals(signWay)) {
            @Valid List<InitiateContractReqVO.SignInfo> signInfos = initiateContractReqVO.getSignInfos();
            InitiateContractReqVO.SignInfo signInfoSelf = signInfos.get(0);
            IyinResult<String> draft = null;
            for (int i = 1; i < signInfos.size(); i++) {
                InitiateContractReqVO.SignInfo signInfo = signInfos.get(i);
                List<InitiateContractReqVO.SignInfo> list = Lists.newLinkedList();
                list.add(signInfoSelf);
                list.add(signInfo);
                initiateContractReqVO.setSignInfos(list);
                boolean isIndividuallyResult = false;
                if (null == initiateContractReqVO.getIsIndividually() || !initiateContractReqVO.getIsIndividually()) {
                    isIndividuallyResult = true;
                }
                if (i == 1 && Boolean.TRUE.equals(isIndividuallyResult)) {
                    draft = draft(initiateContractReqVO);
                } else {
                    doDraft(initiateContractReqVO,
                            null != initiateContractReqVO.getIsStart() && initiateContractReqVO.getIsStart());
                }
            }
            return draft;
        } else {
            return draft(initiateContractReqVO);
        }
    }

    /**
     * 每人单独签署转为顺序签署
     *
     * @param initiateContractReqVO
     * @param isStart 是否发起
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     */
    @Async("taskExecutor")
    @Transactional(rollbackFor = Exception.class)
    public void doDraft(InitiateContractReqVO initiateContractReqVO, boolean isStart) {
        //        复制一份合同
        @Valid List<InitiateContractReqVO.ContractFile> contractFileList = initiateContractReqVO.getContractFileList();
        List<InitiateContractReqVO.ContractFile> contractFiles = Lists.newLinkedList();
        String userId = initiateContractReqVO.getUserId();
        boolean selfSign = FileManageEnum.SELF_SIGN.getCode().equals(initiateContractReqVO.getSignatoryWay());
        for (InitiateContractReqVO.ContractFile contractFile : contractFileList) {
            String fileCode = contractFile.getFileCode();
            log.info("com.iyin.sign.system.service.impl.CompactInfoServiceImpl.doDraft,fileCode:{}", fileCode);
            InMemoryMultipartFile inMemoryMultipartFile = fileService.fetch(fileCode);
            FileUploadRespDto fileUploadRespDto = fileService.baseUpload(inMemoryMultipartFile, userId);
            FileResourceDto file = fileUploadRespDto.getFile();
            String fileCode1 = file.getFileCode();
            contractFile.setFileCode(fileCode1);
            SignSysCompactFile signSysCompactFile = signSysCompactFileMapper.selectOne(
                    Wrappers.<SignSysCompactFile>lambdaQuery()
                            .eq(SignSysCompactFile::getFileCode, fileCode)
                            .isNotNull(SignSysCompactFile::getFileCodeOrigin));
            if( null != signSysCompactFile) {
                contractFile.setFileCodeOrigin(signSysCompactFile.getFileCodeOrigin());
            }
            contractFiles.add(contractFile);
            doCopySignLog(fileCode, file);
        }
        initiateContractReqVO.setContractFileList(contractFiles);
        IyinResult<String> result = draft(initiateContractReqVO);
        IyinResultValidUtil.validateSuccess(result);
        String contractId = result.getData();
        if (isStart) {
            List<SignSysCompactLog> signSysCompactLogs = signSysCompactLogMapper
                    .selectList(Wrappers.<SignSysCompactLog>lambdaQuery().eq(SignSysCompactLog::getCompactId, initiateContractReqVO.getContractId()));
            for (SignSysCompactLog signSysCompactLog : signSysCompactLogs) {
                signSysCompactLog.setId(SnowflakeIdWorker.getIdToStr());
                signSysCompactLog.setCompactId(contractId);
                signSysCompactLogMapper.insert(signSysCompactLog);
            }
            SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
            signSysCompactInfo.setCompactStartDate(new Date());
            signSysCompactInfo.setCompactStatus(FileManageEnum.SIGNING.getCode());
            signSysCompactInfoMapper.updateById(signSysCompactInfo);
            if (selfSign) {
                log.info(
                        "com.iyin.sign.system.service.impl.CompactInfoServiceImpl.doDraft:单独签署：contractId-{}，userId-{}",
                        contractId, userId);
                SignSysCompactSignatory signSysCompactSignatory = signSysCompactSignatoryMapper.selectOne(
                        Wrappers.<SignSysCompactSignatory>query().lambda()
                                .eq(SignSysCompactSignatory::getCompactId, contractId)
                                .eq(SignSysCompactSignatory::getSignatoryId, userId)
                                .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.TODO.getCode()));
                signSysCompactSignatory.setSignStatus(FileManageEnum.PASS.getCode());
                signSysCompactSignatory.setSignDate(new Date());
                signSysCompactSignatoryMapper.updateById(signSysCompactSignatory);
                doNextSign(contractId,signSysCompactSignatory.getNextSignatoryId());
            }else{
                initiateContractReqVO.setContractId(contractId);
                doStartNotice(initiateContractReqVO);
            }
            List<SignSysCompactSignatory> signSysCompactSignatories = signSysCompactSignatoryMapper.selectList(
                    Wrappers.<SignSysCompactSignatory>lambdaQuery().eq(SignSysCompactSignatory::getCompactId, contractId)
                            .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.TODO.getCode()));
            if(CollectionUtils.isEmpty(signSysCompactSignatories)){
                signSysCompactInfo.setCompactEndDate(new Date());
                signSysCompactInfo.setCompactStatus(FileManageEnum.COMPLETE.getCode());
                signSysCompactInfoMapper.updateById(signSysCompactInfo);
            }
            SignSysSignConfig signSysSignConfig = signSysSignConfigMapper.getSignConfig();
            signSysCompactInfo.setQrCode(
                    1 == signSysSignConfig.getQrCode() ? QR_CODE + contractId : null);
            signSysCompactInfo.setVerificationCode(
                    1 == signSysSignConfig.getVerificationCode() ? MD5Util.getStringMD5(signSysCompactInfo.toString()) :
                            null);
            int gmtVerification = signSysSignConfig.getGmtVerification();
            signSysCompactInfo.setVerificationDate(
                    0 == gmtVerification ? "0" : LocalDate.now().plusDays(gmtVerification - 1L).toString());
            signSysCompactInfoMapper.updateById(signSysCompactInfo);
        }
    }

    /**
     * 处理签章日志
     *
     * @Author: yml
     * @CreateDate: 2019/9/9
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/9
     * @Version: 0.0.1
     * @param fileCode
     * @param file
     * @return : void
     */
    @Async("taskExecutor")
    void doCopySignLog(String fileCode, FileResourceDto file) {
        List<SysSignatureLog> sysSignatureLogs;
        sysSignatureLogs = sysSignatureLogMapper
                .selectList(Wrappers.<SysSignatureLog>lambdaQuery().eq(SysSignatureLog::getFileCode, fileCode));
        String fileCode1 = file.getFileCode();
        log.info("com.iyin.sign.system.service.impl.CompactInfoServiceImpl.doDraft,log:{}，fileCode:{},toFileCode:{}",
                sysSignatureLogs, fileCode, fileCode1);
        for (SysSignatureLog sysSignatureLog : sysSignatureLogs) {
            sysSignatureLog.setId(SnowflakeIdWorker.getIdToStr());
            sysSignatureLog.setFileCode(fileCode1);
            sysSignatureLog.setSignFileCode(fileCode1);
            sysSignatureLog.setSignFileHash(file.getFileHash());
            sysSignatureLogMapper.insert(sysSignatureLog);
        }
    }

    /**
     * 无序和顺序签
     *
     * @param initiateContractReqVO
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.String>
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     */
    @Transactional(rollbackFor = Exception.class)
    public IyinResult<String> draft(InitiateContractReqVO initiateContractReqVO) {
        SignSysCompactInfo signSysCompactInfo = new SignSysCompactInfo();
        BeanUtils.copyProperties(initiateContractReqVO, signSysCompactInfo);
        String id = SnowflakeIdWorker.getIdToStr();
        signSysCompactInfo.setId(id);
        String userId = initiateContractReqVO.getUserId();
        signSysCompactInfo.setUserId(userId);
        signSysCompactInfo.setCompactStatus(FileManageEnum.DRAFT.getCode());
        Date gmtCreate = new Date();
        signSysCompactInfo.setGmtCreate(gmtCreate);
        signSysCompactInfo.setCreatUser(userId);
        signSysCompactInfo.setModifyUser(userId);
        boolean save = save(signSysCompactInfo);
        if (save) {
            @Valid List<InitiateContractReqVO.ContractFile> contractFileList = initiateContractReqVO
                    .getContractFileList();
            int relt = 0;
            for (InitiateContractReqVO.ContractFile contractFile : contractFileList) {
                relt = getRelt(new SaveFileReqVo(contractFile.getFileCode(), contractFile.getPageTotal(),
                        contractFile.getFileType(), id, userId, gmtCreate, contractFile.getFileName(),contractFile.getFileCodeOrigin()));
                if (0 >= relt) {
                    break;
                }
            }
            if (compactSignInfo(initiateContractReqVO, id, userId, gmtCreate, relt)) {
                return IyinResult.success(id);
            }
        }
        return IyinResult.getIyinResult(ErrorCode.REQUEST_80003);
    }

    /**
     * 发起并签署
     *
     * @param initiateSignContractReqVO
     * @param req
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.String>
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IyinResult<String> initiateSignContract(InitiateSignContractReqVO initiateSignContractReqVO, HttpServletRequest req) {
        deleteRelationData(initiateSignContractReqVO.getContractId());
        InitiateContractReqVO initiateContractReqVO = new InitiateContractReqVO();
        BeanUtils.copyProperties(initiateSignContractReqVO, initiateContractReqVO);
        initiateContractReqVO.setIsStart(true);
        List<InitiateContractReqVO.SignInfo> signInfosOrign = initiateContractReqVO.getSignInfos();
        String signWay = initiateContractReqVO.getSignWay();
        if (FileManageEnum.INDIVIDUALLY_SIGN.getCode().equals(signWay) && signInfosOrign.size() > MORE_THAN_TWO) {
            List<InitiateContractReqVO.SignInfo> signInfos = Lists.newArrayList();
            signInfos.add(0, signInfosOrign.get(0));if(signInfosOrign.size() > SECOND){

                signInfos.add(1, signInfosOrign.get(1));

    }        initiateContractReqVO.setSignInfos(signInfos);
        }
        IyinResult<String> result = this.draftContract(initiateContractReqVO);
        IyinResultValidUtil.validateSuccess(result);
        String contractId = result.getData();
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
        signSysCompactInfo.setCompactStartDate(new Date());
        signSysCompactInfo.setCompactStatus(FileManageEnum.SIGNING.getCode());
        List<UKeySignReqVO> uKeySignReqVOS = initiateSignContractReqVO.getUKeySignReqVOS();
        if (!CollectionUtils.isEmpty(uKeySignReqVOS)) {
            uKeySignReqVOS.forEach(uKeySignReqVO -> {
                SignSysCompactFile signSysCompactFile = signSysCompactFileMapper.selectOne(
                        Wrappers.<SignSysCompactFile>lambdaQuery()
                                .eq(SignSysCompactFile::getCompactId, contractId)
                                .eq(SignSysCompactFile::getFileCode, uKeySignReqVO.getFileCode())
                                .isNull(SignSysCompactFile::getFileCodeOrigin));
                if( null != signSysCompactFile){
                    InMemoryMultipartFile fetch = fileService.fetch(uKeySignReqVO.getFileCode());
                    FileUploadRespDto fileUploadRespDto = fileService.baseUpload(fetch, initiateSignContractReqVO.getUserId());
                    signSysCompactFile.setFileCodeOrigin(fileUploadRespDto.getFile().getFileCode());
                    signSysCompactFileMapper.updateById(signSysCompactFile);
                }
                fastSignService.uKeySign(uKeySignReqVO, req);
            });
        }
        if (signContractAction(req, contractId, initiateSignContractReqVO.getUserId(),
                initiateSignContractReqVO.getFileManageSignReqVOS())) {
            return IyinResult.getIyinResult(ErrorCode.REQUEST_80004);
        }
        List<SignSysCompactSignatory> signSysCompactSignatories = signSysCompactSignatoryMapper.selectList(
                Wrappers.<SignSysCompactSignatory>lambdaQuery().eq(SignSysCompactSignatory::getCompactId, contractId).and(
                        wrapper -> wrapper.eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.TODO.getCode()).or()
                                .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.SOMEONE_TODO.getCode())));
        log.info("com.iyin.sign.system.service.impl.CompactInfoServiceImpl.initiateSignContract,签署人：{}",signSysCompactSignatories);
        if(CollectionUtils.isEmpty(signSysCompactSignatories)){
            signSysCompactInfo.setCompactEndDate(new Date());
            signSysCompactInfo.setCompactStatus(FileManageEnum.COMPLETE.getCode());
        }
        signSysCompactInfoMapper.updateById(signSysCompactInfo);
        saveOperateLog(contractId, FileManageEnum.LOG_SIGN.getCode(), initiateSignContractReqVO.getUserId());
        initiateContractReqVO.setContractId(contractId);
        if (FileManageEnum.INDIVIDUALLY_SIGN.getCode().equals(signWay) && signInfosOrign.size() > MORE_THAN_TWO) {
            signInfosOrign.remove(signInfosOrign.get(1));
            initiateContractReqVO.setSignInfos(signInfosOrign);
            initiateContractReqVO.setIsIndividually(true);
            initiateContractReqVO.setDeleteFlag("no");
            this.draftContract(initiateContractReqVO);
        }
        SignSysSignConfig signSysSignConfig = signSysSignConfigMapper.getSignConfig();
        signSysCompactInfo.setQrCode(
                1 == signSysSignConfig.getQrCode() ? QR_CODE + contractId : null);
        signSysCompactInfo.setVerificationCode(
                1 == signSysSignConfig.getVerificationCode() ? MD5Util.getStringMD5(signSysCompactInfo.toString()) :
                        null);
        int gmtVerification = signSysSignConfig.getGmtVerification();
        signSysCompactInfo.setVerificationDate(
                0 == gmtVerification ? "0" : LocalDate.now().plusDays(gmtVerification - 1L).toString());
        signSysCompactInfoMapper.updateById(signSysCompactInfo);
        return doStartNotice(initiateContractReqVO);
    }

    /**
     * 合同发起通知
     *
     * @param initiateContractReqVO
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.String>
     * @Author: yml
     * @CreateDate: 2019/8/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/23
     * @Version: 0.0.1
     */
    private IyinResult<String> doStartNotice(InitiateContractReqVO initiateContractReqVO) {
        String contractId = initiateContractReqVO.getContractId();
        List<SignSysCompactSignatory> signSysCompactSignatories = signSysCompactSignatoryMapper.selectList(
                Wrappers.<SignSysCompactSignatory>lambdaQuery().eq(SignSysCompactSignatory::getCompactId, contractId)
                        .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.TODO.getCode()));
        String signDeadlineStr = "";
        Date signDeadline = initiateContractReqVO.getSignDeadline();
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy年MM月dd日");
        if (null != signDeadline) {
            signDeadlineStr = "签约截止至" + myFmt.format(signDeadline) + ",";
        }
        for (SignSysCompactSignatory signSysCompactSignatory : signSysCompactSignatories) {
            SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(initiateContractReqVO.getUserId());
            SignSysUserInfo signSysUserInfoSign = signSysUserInfoMapper
                    .selectById(signSysCompactSignatory.getSignatoryId());
            String userName = null;
            if (null != signSysUserInfoSign) {
                userName = signSysUserInfoSign.getUserName();
            }
            String h5LinkStr = Slf4jUtil.format(h5Link,contractId,initiateContractReqVO.getCompactTheme());
            String shortUrl = shortLinkService.getShortUrl(h5LinkStr);
            String content = Slf4jUtil
                    .format(start, signSysUserInfo.getUserNickName(), initiateContractReqVO.getCompactTheme(),
                            signDeadlineStr,shortUrl);
            doSendNotice(StringUtils.isEmpty(userName) ? signSysCompactSignatory.getSignContact() : userName, content);
        }
        return IyinResult.success(contractId);
    }

    /**
     * 签署
     *
     * @param req
     * @param contractId
     * @param userId2
     * @param fileManageSignReqVOS2
     * @return : boolean
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     */
    private boolean signContractAction(HttpServletRequest req, String contractId, String userId2, List<FileManageSignReqVO> fileManageSignReqVOS2) {
        log.info("com.iyin.sign.system.service.impl.CompactInfoServiceImpl.signContractAction,fileManageSignReqVOS2:{}",
                fileManageSignReqVOS2);
        SignSysCompactSignatory signSysCompactSignatory = signSysCompactSignatoryMapper.selectOne(
                Wrappers.<SignSysCompactSignatory>query().lambda().eq(SignSysCompactSignatory::getCompactId, contractId)
                        .eq(SignSysCompactSignatory::getSignatoryId, userId2)
                        .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.TODO.getCode()));
        if (null != signSysCompactSignatory) {
            List<SignSysCompactSignatory> signSysCompactSignatories = signSysCompactSignatoryMapper.selectList(
                    Wrappers.<SignSysCompactSignatory>query().lambda()
                            .eq(SignSysCompactSignatory::getCompactId, contractId).and(wrapper -> wrapper
                            .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.TODO.getCode()).or()
                            .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.SOMEONE_TODO.getCode()))
                            .isNull(SignSysCompactSignatory::getSignDate));
            if ((null != signSysCompactSignatories) && (TO_DO_SIGN > signSysCompactSignatories.size())) {
                signSysCompactSignatoryMapper.updateByCompactId(contractId, FileManageEnum.PASS.getCode());
                SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
                signSysCompactInfo.setCompactEndDate(new Date());
                signSysCompactInfo.setCompactStatus(FileManageEnum.COMPLETE.getCode());
                signSysCompactInfoMapper.updateById(signSysCompactInfo);
                String userId = signSysCompactInfo.getUserId();
                SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(userId);
                String content = Slf4jUtil.format(complete, signSysCompactInfo.getCompactTheme());
                doSendNotice(signSysUserInfo.getUserName(), content);
            } else {
                signSysCompactSignatory.setSignStatus(FileManageEnum.PASS.getCode());
                signSysCompactSignatory.setSignDate(new Date());
                signSysCompactSignatoryMapper.updateById(signSysCompactSignatory);
                String nextSignatoryId = signSysCompactSignatory.getNextSignatoryId();
                if (null != nextSignatoryId) {
                    doNextSign(contractId, nextSignatoryId);
                }
            }
        } else {
            return true;
        }
        @NotEmpty(message = "签章信息不能为空") @Valid List<FileManageSignReqVO> fileManageSignReqVOS = fileManageSignReqVOS2;
        for (FileManageSignReqVO fileManageSignReqVO : fileManageSignReqVOS) {
            SignSysCompactFile signSysCompactFile = signSysCompactFileMapper.selectOne(
                    Wrappers.<SignSysCompactFile>lambdaQuery()
                            .eq(SignSysCompactFile::getCompactId, contractId)
                            .eq(SignSysCompactFile::getFileCode, fileManageSignReqVO.getCompactFileCode())
                            .isNull(SignSysCompactFile::getFileCodeOrigin));
            if( null != signSysCompactFile){
                InMemoryMultipartFile fetch = fileService.fetch(fileManageSignReqVO.getCompactFileCode());
                FileUploadRespDto fileUploadRespDto = fileService.baseUpload(fetch, userId2);
                signSysCompactFile.setFileCodeOrigin(fileUploadRespDto.getFile().getFileCode());
                signSysCompactFileMapper.updateById(signSysCompactFile);
            }
            signatureFile(fileManageSignReqVO, userId2, req);
        }
        return false;
    }

    private void doNextSign(String contractId, String nextSignatoryId) {
        log.info("com.iyin.sign.system.service.impl.CompactInfoServiceImpl.signContractAction:修改下一签署人{}状态",
                nextSignatoryId);
        SignSysCompactSignatory signSysCompactSignatory = signSysCompactSignatoryMapper.selectOne(
                Wrappers.<SignSysCompactSignatory>query().lambda()
                        .eq(SignSysCompactSignatory::getSignatoryId, nextSignatoryId)
                        .eq(SignSysCompactSignatory::getCompactId, contractId)
                        .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.SOMEONE_TODO.getCode()));
        if(null != signSysCompactSignatory){
            signSysCompactSignatory.setSignStatus(FileManageEnum.TODO.getCode());
            signSysCompactSignatoryMapper.updateById(signSysCompactSignatory);
            SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
            String signDeadlineStr = "";
            Date signDeadline = signSysCompactInfo.getSignDeadline();
            SimpleDateFormat myFmt = new SimpleDateFormat("yyyy年MM月dd日");
            if (null != signDeadline) {
                signDeadlineStr = "签约截止至" + myFmt.format(signDeadline) + ",";
            }
            SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(signSysCompactInfo.getUserId());
            SignSysUserInfo signSysUserInfoSign = signSysUserInfoMapper
                    .selectById(signSysCompactSignatory.getSignatoryId());
            String userName = null;
            if (null != signSysUserInfoSign) {
                userName = signSysUserInfoSign.getUserName();
            }
            String h5LinkStr = Slf4jUtil.format(h5Link,contractId,signSysCompactInfo.getCompactTheme());
            String shortUrl = shortLinkService.getShortUrl(h5LinkStr);
            String content = Slf4jUtil
                    .format(start, signSysUserInfo.getUserNickName(), signSysCompactInfo.getCompactTheme(),
                            signDeadlineStr,shortUrl);
            doSendNotice(StringUtils.isEmpty(userName) ? signSysCompactSignatory.getSignContact() : userName, content);
        }
    }

    /**
     * 立即发送
     *
     * @param initiateContractReqVO
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.String>
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IyinResult<String> initiateContract(InitiateContractReqVO initiateContractReqVO) {
        deleteRelationData(initiateContractReqVO.getContractId());
        initiateContractReqVO.setIsStart(true);
        IyinResult<String> result = draftContract(initiateContractReqVO);
        IyinResultValidUtil.validateSuccess(result);
        String contractId = result.getData();
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
        signSysCompactInfo.setCompactStartDate(new Date());
        signSysCompactInfo.setCompactStatus(FileManageEnum.SIGNING.getCode());
        signSysCompactInfoMapper.updateById(signSysCompactInfo);
        initiateContractReqVO.setContractId(contractId);
        return doStartNotice(initiateContractReqVO);
    }

    /**
     * 删除合同相关数据
     *
     * @param contractIdOrign
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/20
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/20
     * @Version: 0.0.1
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRelationData(String contractIdOrign) {
        if (StringUtils.isNotBlank(contractIdOrign)) {
            signSysCompactInfoMapper.deleteById(contractIdOrign);
            signSysCompactSignatoryMapper.delete(Wrappers.<SignSysCompactSignatory>lambdaQuery()
                    .eq(SignSysCompactSignatory::getCompactId, contractIdOrign));
            signSysCompactCopyMapper.delete(Wrappers.<SignSysCompactCopy>lambdaQuery()
                    .eq(SignSysCompactCopy::getCompactId, contractIdOrign));
            signSysCompactFileMapper.delete(Wrappers.<SignSysCompactFile>lambdaQuery()
                    .eq(SignSysCompactFile::getCompactId, contractIdOrign));
            signSysCompactFieldMapper.delete(Wrappers.<SignSysCompactField>lambdaQuery()
                    .eq(SignSysCompactField::getCompactId, contractIdOrign));
        }
    }

    /**
     * 签名域
     *
     * @param contractId
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<java.util.List<com.iyin.sign.system.vo.file.resp.CompactFieldInfoRespDTO>>
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<List<CompactFieldInfoRespDTO>> signContractDomain(String contractId, String userId) {
        List<SignSysCompactField> signSysCompactFields = signSysCompactFieldMapper.selectList(
                Wrappers.<SignSysCompactField>query().lambda().eq(SignSysCompactField::getCompactId, contractId)
                        .eq(SignSysCompactField::getSignatoryId, userId));
        String s = toJSONString(signSysCompactFields);
        List<CompactFieldInfoRespDTO> compactFieldInfoRespDTO = parseArray(s, CompactFieldInfoRespDTO.class);
        return IyinResult.success(compactFieldInfoRespDTO);
    }

    /**
     * 签署合同
     *
     * @param contractReqVO
     * @param req
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IyinResult<Boolean> signContract(SignContractReqVO contractReqVO, HttpServletRequest req) {
        String contractId = contractReqVO.getContractId();
        String userId = contractReqVO.getUserId();
        saveOperateLog(contractId, FileManageEnum.LOG_SIGN.getCode(), userId);
        List<UKeySignReqVO> uKeySignReqVOS = contractReqVO.getUKeySignReqVOS();
        AtomicReference<Boolean> result = new AtomicReference<>(true);
        if (!CollectionUtils.isEmpty(uKeySignReqVOS)) {
            uKeySignReqVOS.forEach(uKeySignReqVO -> {
                EsealResult<Boolean> booleanEsealResult = fastSignService.uKeySign(uKeySignReqVO, req);
                IyinResultValidUtil.validateSuccess(booleanEsealResult);
                result.set(booleanEsealResult.getData());
            });
        }
        if (signContractAction(req, contractId, contractReqVO.getUserId(), contractReqVO.getSignReqVOS())) {
            return IyinResult.getIyinResult(ErrorCode.REQUEST_80004);
        }
        SignSysSignConfig signSysSignConfig = signSysSignConfigMapper.getSignConfig();
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
        signSysCompactInfo.setQrCode(
                1 == signSysSignConfig.getQrCode() ? QR_CODE + contractId : null);
        signSysCompactInfo.setVerificationCode(
                1 == signSysSignConfig.getVerificationCode() ? MD5Util.getStringMD5(signSysCompactInfo.toString()) :
                        null);
        int gmtVerification = signSysSignConfig.getGmtVerification();
        signSysCompactInfo.setVerificationDate(
                0 == gmtVerification ? "0" : LocalDate.now().plusDays(gmtVerification - 1L).toString());
        signSysCompactInfoMapper.updateById(signSysCompactInfo);
        return IyinResult.success(result.get());
    }

    /**
     * 查看合同
     *
     * @param contractId
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.resp.ContractDetailRespDTO>
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<ContractDetailRespDTO> viewContract(String contractId, String userId) {
        String redisView = contractId.concat(userId).concat(FileManageEnum.LOG_VIEW.getName());
        if (!redisService.exists(redisView)) {
            saveOperateLog(contractId, FileManageEnum.LOG_VIEW.getCode(), userId);
            redisService.set(redisView, redisView, HALF_HOUR);
        }
        ContractDetailRespDTO contractDetailRespDTO = new ContractDetailRespDTO();
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
        if (null == signSysCompactInfo || FileManageEnum.REVOKED.getCode().equals(signSysCompactInfo.getCompactStatus())){
            contractDetailRespDTO.setDeleteOrRevoke(true);
            return IyinResult.success(contractDetailRespDTO);
        }
        contractDetailRespDTO.setContractId(contractId);
        contractDetailRespDTO.setQrCode(signSysCompactInfo.getQrCode());
        contractDetailRespDTO.setVerificationCode(signSysCompactInfo.getVerificationCode());
        contractDetailRespDTO.setVerificationDate(signSysCompactInfo.getVerificationDate());
        contractDetailRespDTO.setContractTheme(signSysCompactInfo.getCompactTheme());
        String compactStatus = signSysCompactInfo.getCompactStatus();
        //        签署中需再次判断是否待签
        if (FileManageEnum.SIGNING.getCode().equals(compactStatus)) {
            if (null != signSysCompactInfo.getSignDeadline() &&
                    new Date().compareTo(signSysCompactInfo.getSignDeadline()) > 0) {
                compactStatus = FileManageEnum.EXPIRED.getCode();
            } else {
                SignSysCompactSignatory signSysCompactSignatory = signSysCompactSignatoryMapper.selectOne(
                        Wrappers.<SignSysCompactSignatory>query().lambda()
                                .eq(SignSysCompactSignatory::getCompactId, contractId)
                                .eq(SignSysCompactSignatory::getSignatoryId, userId));
                compactStatus = compactStatus
                        .concat(null == signSysCompactSignatory ? FileManageEnum.SOMEONE_TODO.getCode() :
                                signSysCompactSignatory.getSignStatus());
            }
        }
        contractDetailRespDTO.setContractStatus(compactStatus);
        contractDetailRespDTO.setCompactStartDate(signSysCompactInfo.getCompactStartDate());
        contractDetailRespDTO.setRemark(signSysCompactInfo.getRemark());
        contractDetailRespDTO.setSignDeadline(signSysCompactInfo.getSignDeadline());
        contractDetailRespDTO.setRefuseSignRemake(signSysCompactInfo.getRefuseSignRemake());
        contractDetailRespDTO.setRevocationRemake(signSysCompactInfo.getRevocationRemake());
        getSignFile(contractId, contractDetailRespDTO);
        List<SignSysCompactSignatory> signSysCompactSignatories = signSysCompactSignatoryMapper.selectList(
                Wrappers.<SignSysCompactSignatory>query().lambda()
                        .eq(SignSysCompactSignatory::getCompactId, contractId));
        //        合同发起人
        String sponsor = signSysCompactInfo.getUserId();
        contractDetailRespDTO.setSponsorId(sponsor);
        if (CollectionUtils.isEmpty(signSysCompactSignatories) || !signSysCompactSignatories.get(0).getSignatoryId().equals(sponsor)) {
            SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(sponsor);
            SignSysCompactSignatory signSysCompactSignatory = new SignSysCompactSignatory();
            signSysCompactSignatory.setSignName(signSysUserInfo.getUserNickName());
            signSysCompactSignatory.setSignContact(signSysUserInfo.getUserName());
            signSysCompactSignatory.setSignatoryId(sponsor);
            signSysCompactSignatory.setSignDate(signSysCompactInfo.getCompactStartDate());
            signSysCompactSignatories.add(0, signSysCompactSignatory);
        }
        contractDetailRespDTO.setSignStatusInfo(signSysCompactSignatories);
        List<CompactLogRespDto> signSysCompactLogs = signSysCompactLogMapper.queryLog(contractId);
        contractDetailRespDTO.setSignSysCompactLogs(signSysCompactLogs);
        return IyinResult.success(contractDetailRespDTO);
    }

    /**
     * 保存操作日志
     *
     * @param contractId
     * @param type
     * @param userId
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     */
    @Async("taskExecutor")
    public int saveOperateLog(String contractId, String type, String userId) {
        SignSysCompactLog compactLog = new SignSysCompactLog();
        compactLog.setId(SnowflakeIdWorker.getIdToStr());
        compactLog.setCompactId(contractId);
        compactLog.setCreateTime(new Date());
        compactLog.setCreateUser(userId);
        compactLog.setType(type);
        compactLog.setUserId(userId);
        return signSysCompactLogMapper.insert(compactLog);
    }

    /**
     * 签署合同信息
     *
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.resp.ContractFileRespDTO>
     * @Author: yml
     * @CreateDate: 2019/8/16
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/16
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<ContractFileRespDTO> signContractFile(String contractId) {
        ContractFileRespDTO contractFileRespDTO = new ContractFileRespDTO();
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
        contractFileRespDTO.setContractId(contractId);
        contractFileRespDTO.setContractTheme(signSysCompactInfo.getCompactTheme());
        getSignFile(contractId, contractFileRespDTO);
        return IyinResult.success(contractFileRespDTO);
    }

    /**
     * 获取签署文件
     *
     * @param contractId
     * @param contractFileRespDTO
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     */
    private void getSignFile(String contractId, ContractFileRespDTO contractFileRespDTO) {
        List<SignSysCompactFile> signSysCompactFiles = signSysCompactFileMapper.selectList(
                Wrappers.<SignSysCompactFile>query().lambda().eq(SignSysCompactFile::getCompactId, contractId)
                        .eq(SignSysCompactFile::getFileType, FileManageEnum.CONTRACT_FILE.getCode()));
        contractFileRespDTO.setCompactFiles(signSysCompactFiles);
        List<SignSysCompactFile> compactFileList = signSysCompactFileMapper.selectList(
                Wrappers.<SignSysCompactFile>query().lambda().eq(SignSysCompactFile::getCompactId, contractId)
                        .eq(SignSysCompactFile::getFileType, FileManageEnum.CONTRACT_ATTACHMENT.getCode()));
        contractFileRespDTO.setCompactAttachments(compactFileList);
    }

    /**
     * 删除合同
     *
     * @param contractId
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IyinResult<Boolean> delContract(String contractId, String userId) {
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
        signSysCompactInfo.setModifyUser(userId);
        int result = signSysCompactInfoMapper.updateById(signSysCompactInfo);
        deleteRelationData(contractId);
        return IyinResult.success(result > 0);
    }

    /**
     * 拒签合同
     *
     * @param contractId
     * @param rejectContent
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IyinResult<Boolean> rejectContract(String contractId, String rejectContent, String userId) {
        saveOperateLog(contractId, FileManageEnum.LOG_REFUSE.getCode(), userId);
        SignSysCompactSignatory signSysCompactSignatory = signSysCompactSignatoryMapper.selectOne(
                Wrappers.<SignSysCompactSignatory>query().lambda().eq(SignSysCompactSignatory::getCompactId, contractId)
                        .eq(SignSysCompactSignatory::getSignatoryId, userId)
                        .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.TODO.getCode()));
        if (null != signSysCompactSignatory) {
            signSysCompactSignatory.setSignDate(new Date());
            signSysCompactSignatory.setSignStatus(FileManageEnum.NOT_PASSED.getCode());
            int result = signSysCompactSignatoryMapper.updateById(signSysCompactSignatory);
            result = result + signSysCompactInfoMapper.rejectContract(contractId, rejectContent);
            if (result > 1) {
                SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
                Assert.notNull(signSysCompactInfo, CONTRACT_NO_EXIST);
                SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(userId);
                SignSysUserInfo sysUserInfo = signSysUserInfoMapper.selectById(signSysCompactInfo.getUserId());
                String content = Slf4jUtil
                        .format(refuse, signSysUserInfo.getUserNickName(), signSysCompactInfo.getCompactTheme());
                doSendNotice(sysUserInfo.getUserName(), content);
            }
            return IyinResult.success(result > 1);
        }
        return IyinResult.getIyinResult(ErrorCode.REQUEST_80004);
    }

    /**
     * 撤销合同
     *
     * @param contractId
     * @param revokeContent
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<Boolean> revokeContract(String contractId, String revokeContent, String userId) {
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectOne(
                Wrappers.<SignSysCompactInfo>query().lambda().eq(SignSysCompactInfo::getId, contractId)
                        .eq(SignSysCompactInfo::getUserId, userId));
        if (null != signSysCompactInfo) {
            int result = signSysCompactInfoMapper.revokeContract(contractId, revokeContent);
            if (result > 0) {
                IyinResult<Set<ContractUserRespDTO>> setIyinResult = allUserContract(contractId, userId);
                if (null != setIyinResult && !CollectionUtils.isEmpty(setIyinResult.getData())) {
                    Set<ContractUserRespDTO> data = setIyinResult.getData();
                    SignSysUserInfo sysUserInfo = signSysUserInfoMapper.selectById(userId);
                    Assert.notNull(sysUserInfo, "无该发起用户信息");
                    for (ContractUserRespDTO datum : data) {
                        String content = Slf4jUtil
                                .format(revoke, sysUserInfo.getUserNickName(), signSysCompactInfo.getCompactTheme());
                        doSendNotice(datum.getUserName(), content);
                    }
                }
            }
            return IyinResult.success(result > 0);
        }
        return IyinResult.getIyinResult(ErrorCode.REQUEST_80005);
    }

    /**
     * 打印合同
     *
     * @param contractId
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<Boolean> printContract(String contractId, String userId) {
        SignSysPrintAuthUser signSysPrintAuthUser = signSysPrintAuthUserMapper.selectOne(
                Wrappers.<SignSysPrintAuthUser>lambdaQuery().eq(SignSysPrintAuthUser::getFileCode, contractId)
                        .eq(SignSysPrintAuthUser::getUserId, userId));
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
        boolean self = !signSysCompactInfo.getUserId().equals(userId);
        if (null == signSysPrintAuthUser && self) {
            return IyinResult.getIyinResult(ErrorCode.REQUEST_80007);
        } else if (self && 0 >= signSysPrintAuthUser.getPrintNum()) {
            return IyinResult.getIyinResult(ErrorCode.REQUEST_80008);
        }
        if (null != signSysPrintAuthUser) {
            signSysPrintAuthUser.setPrintNum(signSysPrintAuthUser.getPrintNum() - 1);
            signSysPrintAuthUserMapper.updateById(signSysPrintAuthUser);
        }
        int insert = saveOperateLog(contractId, FileManageEnum.LOG_PRINT.getCode(), userId);
        return IyinResult.success(insert > 0);
    }

    /**
     * 下载合同
     *
     * @param contractId
     * @param userId
     * @param response
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<Boolean> downloadContract(String contractId, String userId, HttpServletResponse response) {
        int insert = saveOperateLog(contractId, FileManageEnum.LOG_DOWNLOAD.getCode(), userId);
        List<SignSysCompactFile> compactFileList = signSysCompactFileMapper.selectList(
                Wrappers.<SignSysCompactFile>query().lambda().eq(SignSysCompactFile::getCompactId, contractId));
        DocPackageReqDto docPackageReqDto = new DocPackageReqDto();
        docPackageReqDto.setUserId(userId);
        List<String> list = Lists.newArrayList();
        for (SignSysCompactFile signSysCompactFile : compactFileList) {
            list.add(signSysCompactFile.getFileCode());
        }
        docPackageReqDto.setResourceIds(list);
        InMemoryMultipartFile file = fileService.downloadDocZip(docPackageReqDto);
        response.setContentType(file.getContentType());
        response.setHeader("Content-Disposition", String.format("attachment;filename=%s", file.getOriginalFilename()));
        try {
            byte[] bytes = file.getBytes();
            response.getOutputStream().write(bytes);
        } catch (IOException e) {
            log.error(
                    "com.iyin.sign.system.service.impl.CompactInfoServiceImpl.downloadContract:[file output fail: {}]",
                    e.getMessage());
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
        return IyinResult.success(insert > 0);
    }

    /**
     * 重新发起合同
     *
     * @param contractId
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.req.InitiateContractReqVO>
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<InitiateContractReqVO> restartContract(String contractId, String userId) {
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
        if (null != signSysCompactInfo && userId.equals(signSysCompactInfo.getUserId())) {
            InitiateContractReqVO initiateContractReqVO = new InitiateContractReqVO();
            BeanUtils.copyProperties(signSysCompactInfo, initiateContractReqVO);
            List<SignSysCompactFile> signSysCompactFiles = signSysCompactFileMapper.selectList(
                    Wrappers.<SignSysCompactFile>query().lambda().eq(SignSysCompactFile::getCompactId, contractId));
            List<InitiateContractReqVO.ContractFile> contractFiles = Lists.newLinkedList();
            signSysCompactFiles.forEach(signSysCompactFile -> {
                InitiateContractReqVO.ContractFile contractFile = new InitiateContractReqVO.ContractFile();
                BeanUtils.copyProperties(signSysCompactFile, contractFile);
                if (null != signSysCompactFile.getFileCodeOrigin()) {
                    contractFile.setFileCode(signSysCompactFile.getFileCodeOrigin());
                }
                contractFiles.add(contractFile);
            });
            initiateContractReqVO.setContractFileList(contractFiles);
            List<SignSysCompactField> signSysCompactFields = signSysCompactFieldMapper.selectList(
                    Wrappers.<SignSysCompactField>query().lambda().eq(SignSysCompactField::getCompactId, contractId));
            List<CompactFieldInfoReqVO> compactFieldInfoReqVOS = Lists.newLinkedList();
            signSysCompactFields.forEach(signSysCompactField -> {
                CompactFieldInfoReqVO compactFieldInfoReqVO = new CompactFieldInfoReqVO();
                BeanUtils.copyProperties(signSysCompactField, compactFieldInfoReqVO);
                compactFieldInfoReqVOS.add(compactFieldInfoReqVO);
            });
            initiateContractReqVO.setCompactFieldInfoReqVOList(compactFieldInfoReqVOS);
            List<SignSysCompactSignatory> signSysCompactSignatories = signSysCompactSignatoryMapper.selectList(
                    Wrappers.<SignSysCompactSignatory>query().lambda()
                            .eq(SignSysCompactSignatory::getCompactId, contractId));
            List<InitiateContractReqVO.SignInfo> signInfos = Lists.newLinkedList();
            signSysCompactSignatories.forEach(signSysCompactSignatory -> {
                InitiateContractReqVO.SignInfo signInfo = new InitiateContractReqVO.SignInfo();
                BeanUtils.copyProperties(signSysCompactSignatory, signInfo);
                signInfo.setType(FileManageEnum.SIGN.getCode());
                signInfos.add(signInfo);
            });
            List<SignSysCompactCopy> signSysCompactCopies = signSysCompactCopyMapper.selectList(
                    Wrappers.<SignSysCompactCopy>query().lambda().eq(SignSysCompactCopy::getCompactId, contractId));
            signSysCompactCopies.forEach(signSysCompactCopy -> {
                InitiateContractReqVO.SignInfo signInfo = new InitiateContractReqVO.SignInfo();
                BeanUtils.copyProperties(signSysCompactCopy, signInfo);
                signInfo.setType(FileManageEnum.CARBON_COPY.getCode());
                signInfo.setSignatoryId(signSysCompactCopy.getUserId());
                if (signInfo.getSignatoryId().equals(userId)) {
                    signInfos.add(0, signInfo);
                    return;
                }
                signInfos.add(signInfo);
            });
            initiateContractReqVO.setSignInfos(signInfos);
            return IyinResult.success(initiateContractReqVO);
        }
        return IyinResult.getIyinResult(ErrorCode.REQUEST_80004);
    }

    /**
     * 合同相关人
     *
     * @param contractId
     * @param userId
     * @return : com.iyin.sign.system.model.IyinResult<java.util.Set<com.iyin.sign.system.vo.file.resp.ContractUserRespDTO>>
     * @Author: yml
     * @CreateDate: 2019/8/20
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/20
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<Set<ContractUserRespDTO>> allUserContract(String contractId, String userId) {
        Set<ContractUserRespDTO> contractUserRespDTOS = Sets.newConcurrentHashSet();
        List<SignSysCompactSignatory> signSysCompactSignatories = signSysCompactSignatoryMapper.selectList(
                Wrappers.<SignSysCompactSignatory>lambdaQuery().eq(SignSysCompactSignatory::getCompactId, contractId));
        Set<ContractUserRespDTO> finalContractUserRespDTOS = contractUserRespDTOS;
        signSysCompactSignatories.forEach(signSysCompactSignatory -> {
            SignSysUserInfo signSysUserInfo = signSysUserInfoMapper
                    .selectById(signSysCompactSignatory.getSignatoryId());
            userInfoCollect(finalContractUserRespDTOS, signSysUserInfo);
        });
        List<SignSysCompactCopy> signSysCompactCopies = signSysCompactCopyMapper.selectList(
                Wrappers.<SignSysCompactCopy>lambdaQuery().eq(SignSysCompactCopy::getCompactId, contractId));
        signSysCompactCopies.forEach(signSysCompactCopy -> {
            SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(signSysCompactCopy.getUserId());
            userInfoCollect(finalContractUserRespDTOS, signSysUserInfo);
        });
        contractUserRespDTOS = finalContractUserRespDTOS.stream()
                .filter(contractUserRespDTO -> !contractUserRespDTO.getUserId().equals(userId))
                .collect(Collectors.toSet());
        return IyinResult.success(contractUserRespDTOS);
    }

    /**
     * 用户数据组装
     *
     * @param finalContractUserRespDTOS
     * @param signSysUserInfo
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/23
     * @Version: 0.0.1
     */
    private void userInfoCollect(Set<ContractUserRespDTO> finalContractUserRespDTOS, SignSysUserInfo signSysUserInfo) {
        String id = null;
        String userName = null;
        String nickName = null;
        if (null != signSysUserInfo) {
            id = signSysUserInfo.getId();
            userName = signSysUserInfo.getUserName();
            nickName = signSysUserInfo.getUserNickName();
        }
        ContractUserRespDTO contractUserRespDTO = new ContractUserRespDTO();
        contractUserRespDTO.setUserId(id);
        contractUserRespDTO.setUserName(userName);
        contractUserRespDTO.setUserNickName(nickName);
        finalContractUserRespDTOS.add(contractUserRespDTO);
    }

    /**
     * 批量导入
     *
     * @param file
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.req.InitiateContractReqVO.SignInfo>
     * @Author: yml
     * @CreateDate: 2019/8/21
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/21
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<BatchImportRespDTO> batchImport(MultipartFile file) {
        BatchImportRespDTO batchImportRespDTO = new BatchImportRespDTO();
        List<InitiateContractReqVO.SignInfo> list = Lists.newLinkedList();
        //获取文件的名字
        String originalFilename = file.getOriginalFilename();
        Workbook workbook = null;
        try {
            if (originalFilename.endsWith(SUFFIX_2003)) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if (originalFilename.endsWith(SUFFIX_2007)) {
                workbook = new XSSFWorkbook(file.getInputStream());
            }
        } catch (Exception e) {
            log.error("com.iyin.sign.system.service.impl.CompactInfoServiceImpl_{}:exception-{}", originalFilename,
                    e.getLocalizedMessage());
            throw new BusinessException(ErrorCode.SYS_WRONG_FORMAT);
        }
        if (null == workbook) {
            throw new BusinessException(ErrorCode.SYS_WRONG_FORMAT);
        }
        //获取所有的工作表的的数量
        int numOfSheet = workbook.getNumberOfSheets();
        int[] data = new int[SECOND];
        getContent(list, workbook, numOfSheet, data);
        batchImportRespDTO.setTotal(data[0]);
        batchImportRespDTO.setException(data[1]);
        batchImportRespDTO.setNormal(data[0] - data[1]);
        batchImportRespDTO.setSignInfos(list);
        batchImportRespDTO.setExceptionDownload("/exceptionD");
        return IyinResult.success(batchImportRespDTO);
    }

    /**
     * 下载批量导入模板
     *
     * @param response
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     * @Author: yml
     * @CreateDate: 2019/8/21
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/21
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<Boolean> downloadBatchImport(HttpServletResponse response) {
        ClassPathResource resource = new ClassPathResource("template" + File.separator + "批量导入用户模板.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = resource.getFilename();
        try {
            fileName = new String(fileName.getBytes("gb2312"), StandardCharsets.ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            log.error("com.iyin.sign.system.service.impl.CompactInfoServiceImpl.downloadBatchImport.exception:{}",
                    e.getLocalizedMessage());
        }
        String format = String.format("attachment;filename=%s", fileName);
        response.setHeader("Content-Disposition", format);
        try {
            byte[] bytes = FileUtil.toBytesArray(resource.getInputStream());
            response.getOutputStream().write(bytes);
        } catch (IOException e) {
            log.error(
                    "com.iyin.sign.system.service.impl.CompactInfoServiceImpl.downloadBatchImport:[file output fail: {}]",
                    e.getLocalizedMessage());
            throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
        }
        return IyinResult.success(true);
    }

    /**
     * 异常数据下载
     *
     * @param response
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     * @Author: yml
     * @CreateDate: 2019/8/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/23
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<Boolean> exceptionD(HttpServletResponse response) {
        Assert.notEmpty(exceptionList, "无数据导出");
        //声明一个工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = hssfWorkbook.createSheet();
        //设置表格宽度为18
        sheet.setDefaultColumnWidth(WIDTH);
        HSSFRow row = sheet.createRow(ZERO);
        //表头
        String[] headers = {"名称", "联系方式", "签署方式"};
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        for (InitiateContractReqVO.SignInfo signInfo : exceptionList) {
            HSSFRow sheetRow = sheet.createRow(sheet.getLastRowNum() + 1);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = sheetRow.createCell(i);
                HSSFRichTextString text;
                if (i == 0) {
                    text = new HSSFRichTextString(signInfo.getSignName());
                    cell.setCellValue(text);
                } else if (i == 1) {
                    text = new HSSFRichTextString(signInfo.getSignContact());
                    cell.setCellValue(text);
                } else {
                    text = new HSSFRichTextString(
                            signInfo.getType().equals(FileManageEnum.CARBON_COPY.getCode()) ? "抄送" : "签署");
                    cell.setCellValue(text);
                }
            }
        }
        String fileName = "异常数据.xls";
        try {
            fileName = new String(fileName.getBytes("gb2312"), StandardCharsets.ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            log.error("com.iyin.sign.system.service.impl.CompactInfoServiceImpl.downloadBatchImport.exception:{}",
                    e.getLocalizedMessage());
        }
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachmen;filename=" + fileName);
        try {
            exceptionList = null;
            response.flushBuffer();
            hssfWorkbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.DATA_IO_EXCEPTION);
        }
        return IyinResult.success(true);
    }

    /**
     * 催签合同
     *
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     * @Author: yml
     * @CreateDate: 2019/8/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/23
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<Boolean> urgeContract(String contractId) {
        List<SignSysCompactSignatory> signSysCompactSignatories = signSysCompactSignatoryMapper.selectList(
                Wrappers.<SignSysCompactSignatory>lambdaQuery().eq(SignSysCompactSignatory::getCompactId, contractId)
                        .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.TODO.getCode()));
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
        Assert.notNull(signSysCompactInfo, CONTRACT_NO_EXIST);
        for (SignSysCompactSignatory signSysCompactSignatory : signSysCompactSignatories) {
            SignSysUserInfo signSysUserInfoSign = signSysUserInfoMapper
                    .selectById(signSysCompactSignatory.getSignatoryId());
            String userName = null;
            if (null != signSysUserInfoSign) {
                userName = signSysUserInfoSign.getUserName();
            }
            String h5LinkStr = Slf4jUtil.format(h5Link,contractId,signSysCompactInfo.getCompactTheme());
            String shortUrl = shortLinkService.getShortUrl(h5LinkStr);
            String content = Slf4jUtil.format(urge, signSysCompactInfo.getCompactTheme(), shortUrl);
            doSendNotice(StringUtils.isEmpty(userName) ? signSysCompactSignatory.getSignContact() : userName, content);
        }
        return IyinResult.success(true);
    }

    /**
     * 合同签署日志
     *
     * @param contractLogReqVO
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.vo.file.resp.CompactLogRespDto>>
     * @Author: yml
     * @CreateDate: 2019/8/30
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/30
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<IyinPage<CompactLogRespDto>> contractSignLog(ContractLogReqVO contractLogReqVO) {
        IyinPage<CompactLogRespDto> iyinPage = new IyinPage<>(contractLogReqVO.getPageNum(),
                contractLogReqVO.getPageSize());
        List<CompactLogRespDto> compactLogRespDtos = signSysCompactLogMapper
                .queryDesignationLog(iyinPage, contractLogReqVO.getContractId(), FileManageEnum.LOG_SIGN.getCode());
        iyinPage.setRecords(compactLogRespDtos);
        return IyinResult.success(iyinPage);
    }

    /**
     * 合同打印日志
     *
     * @param contractLogReqVO
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.vo.file.resp.CompactLogRespDto>>
     * @Author: yml
     * @CreateDate: 2019/8/30
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/30
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<IyinPage<CompactLogRespDto>> contractPrintLog(ContractLogReqVO contractLogReqVO) {
        IyinPage<CompactLogRespDto> iyinPage = new IyinPage<>(contractLogReqVO.getPageNum(),
                contractLogReqVO.getPageSize());
        List<CompactLogRespDto> compactLogRespDtos = signSysCompactLogMapper
                .queryDesignationLog(iyinPage, contractLogReqVO.getContractId(), FileManageEnum.LOG_PRINT.getCode());
        iyinPage.setRecords(compactLogRespDtos);
        return IyinResult.success(iyinPage);
    }

    /**
     * 单独催签
     *
     * @Author: yml
     * @CreateDate: 2019/9/4
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/4
     * @Version: 0.0.1
     * @param contact
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    @Override
    public IyinResult<Boolean> urgePerson(String contact, String contractId) {
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(contractId);
        Assert.notNull(signSysCompactInfo, CONTRACT_NO_EXIST);
        String h5LinkStr = Slf4jUtil.format(h5Link,contractId,signSysCompactInfo.getCompactTheme());
        String shortUrl = shortLinkService.getShortUrl(h5LinkStr);
        String content = Slf4jUtil.format(urge, signSysCompactInfo.getCompactTheme(), shortUrl);
        doSendNotice(contact, content);
        return IyinResult.success(true);
    }

    /**
     * 用印申请
     *
     * @Author: yml
     * @CreateDate: 2019/10/10
     * @UpdateUser: yml
     * @UpdateDate: 2019/10/10
     * @Version: 0.0.1
     * @param useSealReqVO
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    @Override
    public IyinResult<Boolean> contractUseSeal(ContractUseSealReqVO useSealReqVO, HttpServletRequest request) {
        SignSysUseSeal signSysUseSeal = new SignSysUseSeal();
        String contractId = useSealReqVO.getContractId();
        BeanUtils.copyProperties(useSealReqVO,signSysUseSeal);
        signSysUseSeal.setApplyId(SnowflakeIdWorker.getIdToStr());
        signSysUseSeal.setExpireTime(DateUtil.formatDate(useSealReqVO.getExpireTime()));
        signSysUseSeal.setCreatUser(useSealReqVO.getUserId());
        Integer source = useSealReqVO.getSource();
        signSysUseSeal.setSource(source);
        String userId = useSealReqVO.getUserId();
        String xUserId = getXUserId(userId);
        signSysUseSeal.setApplyUser(xUserId);
        IyinResult<ContractFileRespDTO> contractFileRespDTOIyinResult = this.signContractFile(contractId);
        IyinResultValidUtil.validateSuccess(contractFileRespDTOIyinResult);
        ContractFileRespDTO data = contractFileRespDTOIyinResult.getData();
        List<SignSysCompactFile> compactFiles = data.getCompactFiles();
        List<SignSysCompactFile> compactAttachments = data.getCompactAttachments();
        signSysUseSeal.setFileNumber(compactFiles.size() + compactAttachments.size());
        signSysUseSeal.setFileType(UseSealSourceEnum.getName(source));
        signSysUseSeal.setBusinessId(contractId);
        int insert = sysUseSealMapper.insert(signSysUseSeal);
        if(insert > 0){
            UploadReqVO uploadReqVO = new UploadReqVO();
            BeanUtils.copyProperties(useSealReqVO,uploadReqVO);
            uploadReqVO.setApplyPdf(UseSealSourceEnum.CONTRACT.getName());
            uploadReqVO.setImgList(null);
            uploadReqVO.setApplyUser(xUserId);
            uploadReqVO.setFileNumber(signSysUseSeal.getFileNumber());
            uploadReqVO.setFileType(signSysUseSeal.getFileType());
            Map<String, Object> upload = sealManagerService
                    .upload(xUserId, companyId, getBaiHeToken(userId), uploadReqVO);
            log.info("白鹤申请结果,{}", upload);
            saveOperateLog(useSealReqVO.getContractId(), FileManageEnum.LOG_USE_SEAL.getCode(), useSealReqVO.getUserId());
        }
        return IyinResult.success(insert > 0);
    }

    /**
     * 获取白鹤业务ID
     *
     * @Author: yml
     * @CreateDate: 2019/10/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/10/23
     * @Version: 0.0.1
     * @param userId
     * @return : java.lang.String
     */
    private String getXUserId(String userId) {
        IyinResult<SignSysUserInfoRespVO> signSysUserInfoRespVOIyinResult=signSysUserInfoService.queryAccountInfoById(userId);
        IyinResultValidUtil.validateSuccess(signSysUserInfoRespVOIyinResult);
        SignSysUserInfoRespVO signSysUserInfoRespVO=signSysUserInfoRespVOIyinResult.getData();
        if(null==signSysUserInfoRespVO){
            throw new BusinessException(ErrorCode.REQUEST_10010);
        }
        return signSysUserInfoRespVO.getOtherId();
    }

    /**
     * 获取白鹤token
     *
     * @Author: yml
     * @CreateDate: 2019/10/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/10/23
     * @Version: 0.0.1
     * @param userId
     * @return : java.lang.String
     */
    private String getBaiHeToken(String userId) {
        IyinResult<SignSysUserInfoRespVO> signSysUserInfoRespVOIyinResult=signSysUserInfoService.queryAccountInfoById(userId);
        IyinResultValidUtil.validateSuccess(signSysUserInfoRespVOIyinResult);
        SignSysUserInfoRespVO signSysUserInfoRespVO=signSysUserInfoRespVOIyinResult.getData();
        if(null==signSysUserInfoRespVO){
            throw new BusinessException(ErrorCode.REQUEST_10010);
        }
        Map<String,Object> tokenMap=sealManagerService.accessToken(appKey,secret,signSysUserInfoRespVO.getOtherId());
        return getData(tokenMap);
    }

    private String getData(Map<String, Object> tokenMap) {
        if(tokenMap.isEmpty()){
            throw new BusinessException(ErrorCode.REQUEST_10012);
        }
        //校验tokenMap
        String tokenCode=tokenMap.get("code")+"";
        if(AppEnum.SUCCESS.getCode()!=Integer.parseInt(tokenCode)){
            throw new BusinessException(ErrorCode.REQUEST_10012);
        }
        String data=tokenMap.get("data")+"";
        if(StringUtils.isBlank(data)){
            throw new BusinessException(ErrorCode.REQUEST_10012);
        }
        return data;
    }

    private String getUser(HttpServletRequest request) {
        log.info("CompactInfoServiceImpl");
        String userId="";
        String token = request.getHeader(API_TOKEN);
        String sessionToken = request.getHeader(SESSION_TOKEN);
        if (StringUtils.isBlank(token) && StringUtils.isBlank(sessionToken)) {
            throw new BusinessException(ErrorCode.REQUEST_40434);
        }
        if(StringUtils.isNotBlank(sessionToken)){
            log.info("CompactInfoServiceImpl ck sessionToken");
            Claims claims = jwtUtils.parseJWT(sessionToken);
            userId = String.valueOf(claims.get("userId"));
        }else{
            SysSignApplication sysSignApplication = authTokenService.verifyAuthToken(token);
            userId = sysSignApplication.getUserId();
        }
        return userId;
    }

    /**
     * 发送通知
     *
     * @Author: yml
     * @CreateDate: 2019/10/10
     * @UpdateUser: yml
     * @UpdateDate: 2019/10/10
     * @Version: 0.0.1
     * @param userName
     * @param content
     * @return : void
     */
    @Async("taskExecutor")
    public void doSendNotice(String userName, String content) {
        if (userName.matches(SendMessageUtil.MOBILE_PHONE_REGEX)) {
            try {
                sendMessageUtil.doSendSMS(userName, content);
            } catch (Exception e) {
                log.error("com.iyin.sign.system.service.impl.CompactInfoServiceImpl.doSendNotice,exception:{}",
                        e.getLocalizedMessage());
            }
        } else {
            String cId = "headImage";
            String dearUser = Slf4jUtil.format(emailDear, userName);
            String br = "<br/>";
            String sb = "<img src='cid:" + cId + "'/>" + br + br + emailHead + br + br + dearUser + br + br +
                    "&emsp;&emsp;" + content + br + br + br + br + emailFoot + br + emailQQ + br + emailPhone;
            sendMessageUtil.doSendEmailWithPicture(userName, sb, emailPictureUrl, cId);
        }
    }

    /**
     * 获取表格数据内容
     *
     * @param list
     * @param workbook
     * @param numOfSheet
     * @param data
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/23
     * @Version: 0.0.1
     */
    private void getContent(List<InitiateContractReqVO.SignInfo> list, Workbook workbook, int numOfSheet, int[] data) {
        //遍历这个这些表
        int total = 0;
        int exception = 0;
        exceptionList = Lists.newLinkedList();
        for (int i = 0; i < numOfSheet; i++) {
            //获取一个sheet也就是一个工作簿
            Sheet sheet = workbook.getSheetAt(i);
            int lastRowNum = sheet.getLastRowNum();
            //从第一行开始第一行一般是标题
            for (int j = 1; j <= lastRowNum; j++) {
                total++;
                Row row = sheet.getRow(j);
                InitiateContractReqVO.SignInfo signInfo = new InitiateContractReqVO.SignInfo();
                String name = null;
                String type = null;
                //获取姓名单元格
                if (row.getCell(ZERO) != null) {
                    row.getCell(ZERO).setCellType(Cell.CELL_TYPE_STRING);
                    name = row.getCell(ZERO).getStringCellValue();
                    signInfo.setSignName(name);
                }
                //类型
                if (row.getCell(SECOND) != null) {
                    row.getCell(SECOND).setCellType(Cell.CELL_TYPE_STRING);
                    type = row.getCell(SECOND).getStringCellValue();
                    signInfo.setType(type);
                }
                //联系方式
                if (row.getCell(FIRST) != null) {
                    row.getCell(FIRST).setCellType(Cell.CELL_TYPE_STRING);
                    String contact = row.getCell(FIRST).getStringCellValue();
                    if (!(contact.matches(SendMessageUtil.MOBILE_PHONE_REGEX) ||
                            contact.matches(SendMessageUtil.EMAIL_REGEX))) {
                        exception++;
                        InitiateContractReqVO.SignInfo exceptionInfo = new InitiateContractReqVO.SignInfo();
                        exceptionInfo.setSignName(name);
                        exceptionInfo.setSignContact(contact);
                        exceptionInfo.setType(type);
                        exceptionList.add(exceptionInfo);
                        continue;
                    }
                    signInfo.setSignContact(contact);
                }
                List<SignSysUserInfo> signSysUserInfos = signSysUserInfoMapper.selectList(
                        Wrappers.<SignSysUserInfo>lambdaQuery()
                                .eq(SignSysUserInfo::getUserName, signInfo.getSignContact())
                                .eq(SignSysUserInfo::getIsDeleted, 0));
                if (!CollectionUtils.isEmpty(signSysUserInfos)) {
                    signInfo.setSignatoryId(signSysUserInfos.get(0).getId());
                }
                list.add(signInfo);
            }
        }
        data[0] = total;
        data[1] = exception;
    }

    /**
     * 签署信息
     *
     * @param initiateContractReqVO
     * @param id
     * @param userId
     * @param gmtCreate
     * @param relt
     * @return : boolean
     * @Author: yml
     * @CreateDate: 2019/8/14
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/14
     * @Version: 0.0.1
     */
    private boolean compactSignInfo(InitiateContractReqVO initiateContractReqVO, String id, String userId, Date gmtCreate, int relt) {
        if (0 < relt) {
            String signWay = initiateContractReqVO.getSignWay();
            String nextSignatoryId;
            boolean first = true;
            @Valid List<InitiateContractReqVO.SignInfo> signInfos = initiateContractReqVO.getSignInfos();
            for (int i = 0; i < signInfos.size(); i++) {
                String status = FileManageEnum.SOMEONE_TODO.getCode();
                InitiateContractReqVO.SignInfo signInfo = signInfos.get(i);
                if (FileManageEnum.SIGN.getCode().equals(signInfo.getType()) && first) {
                    status = FileManageEnum.TODO.getCode();
                    first = false;
                }
                InitiateContractReqVO.SignInfo signInfo1;
                if (signInfos.size() == (i + 1)) {
                    nextSignatoryId = null;
                }else {
                    int j = i + 1;
                    do {
                        signInfo1 = signInfos.get(j);
                        j++;
                    } while (signInfo1.getType().equals(FileManageEnum.CARBON_COPY.getCode()) && j != signInfos.size());
                    nextSignatoryId = signInfo1.getType().equals(FileManageEnum.CARBON_COPY.getCode()) ? null :
                            signInfo1.getSignatoryId();
                }
                Signer signer = new Signer();
                signer.setCompactId(id);
                signer.setUserId(userId);
                signer.setGmtCreate(gmtCreate);
                signer.setSignWay(signWay);
                signer.setSignStatus(status);
                signer.setNextSignatoryId(nextSignatoryId);
                signer.setLogicalNumber(i);
                doOperateSignInfo(signer, signInfo);
            }
            List<CompactFieldInfoReqVO> compactFieldInfoReqVOList = initiateContractReqVO
                    .getCompactFieldInfoReqVOList();
            for (CompactFieldInfoReqVO compactFieldInfoReqVO : compactFieldInfoReqVOList) {
                saveCompactField(id, userId, gmtCreate, compactFieldInfoReqVO, compactFieldInfoReqVO.getSignType());
            }
            return true;
        }
        return false;
    }

    /**
     * 组装签署信息
     *
     * @param signerOrigin
     * @param signInfo
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     */
    private void doOperateSignInfo(Signer signerOrigin, InitiateContractReqVO.SignInfo signInfo) {
        if (FileManageEnum.SIGN.getCode().equals(signInfo.getType())) {
            Signer signer = new Signer();
            signer.setCompactId(signerOrigin.getCompactId());
            signer.setUserId(signerOrigin.getUserId());
            signer.setGmtCreate(signerOrigin.getGmtCreate());
            signer.setSignatoryId(signInfo.getSignatoryId());
            signer.setNextSignatoryId(null);
            signer.setSignName(signInfo.getSignName());
            signer.setSignContact(signInfo.getSignContact());
            signer.setSignStatus(FileManageEnum.TODO.getCode());
            saveSigner(signerOrigin.getSignWay(), signerOrigin.getSignStatus(), signerOrigin.getNextSignatoryId(),
                    signerOrigin.getLogicalNumber(), signer);
        } else if (FileManageEnum.CARBON_COPY.getCode().equals(signInfo.getType())) {
            saveCarbonCopy(signerOrigin.getCompactId(), signerOrigin.getUserId(), signerOrigin.getGmtCreate(),
                    signInfo.getSignatoryId(), signInfo.getSignName(), signInfo.getSignContact());
        }
    }

    /**
     * 组装签署信息
     *
     * @param signWay
     * @param status
     * @param nextSignatoryId
     * @param i
     * @param signer
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/15
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/15
     * @Version: 0.0.1
     */
    private void saveSigner(String signWay, String status, String nextSignatoryId, int i, Signer signer) {
        if (FileManageEnum.UNORDERED_SIGN.getCode().equals(signWay)) {
            insertSigner(signer);
        } else {
            signer.setNextSignatoryId(nextSignatoryId);
            signer.setSignStatus(status);
            signer.setLogicalNumber(i);
            insertSigner(signer);
        }
    }

    /**
     * 保存签名位置
     *
     * @param id
     * @param userId
     * @param gmtCreate
     * @param compactFieldInfoReqVO
     * @param signType
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/14
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/14
     * @Version: 0.0.1
     */
    private void saveCompactField(String id, String userId, Date gmtCreate, CompactFieldInfoReqVO compactFieldInfoReqVO, String signType) {
        SignSysCompactField signSysCompactField = new SignSysCompactField();
        signSysCompactField.setId(SnowflakeIdWorker.getIdToStr());
        signSysCompactField.setCompactId(id);
        signSysCompactField.setCompactFileCode(compactFieldInfoReqVO.getCompactFileCode());
        signSysCompactField.setSignatoryId(compactFieldInfoReqVO.getSignatoryId());
        signSysCompactField.setSignatureCoordinateX(compactFieldInfoReqVO.getSignatureCoordinateX());
        signSysCompactField.setSignatureCoordinateY(compactFieldInfoReqVO.getSignatureCoordinateY());
        signSysCompactField.setSignatureMethod(compactFieldInfoReqVO.getSignatureMethod());
        signSysCompactField.setSignatureStartPage(compactFieldInfoReqVO.getSignatureStartPage());
        signSysCompactField.setSignatureEndPage(compactFieldInfoReqVO.getSignatureEndPage());
        signSysCompactField.setSignName(compactFieldInfoReqVO.getSignName());
        signSysCompactField.setSignType(signType);
        signSysCompactField.setGmtCreate(gmtCreate);
        signSysCompactField.setCreatUser(userId);
        signSysCompactField.setModifyUser(userId);
        signSysCompactFieldMapper.insert(signSysCompactField);
    }

    /**
     * 保存抄送人
     *
     * @param id
     * @param userId
     * @param gmtCreate
     * @param signatoryId
     * @param signName
     * @param signContact
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/14
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/14
     * @Version: 0.0.1
     */
    private void saveCarbonCopy(String id, String userId, Date gmtCreate, String signatoryId, String signName, String signContact) {
        SignSysCompactCopy signSysCompactCopy = new SignSysCompactCopy();
        signSysCompactCopy.setId(SnowflakeIdWorker.getIdToStr());
        signSysCompactCopy.setCompactId(id);
        signSysCompactCopy.setUserId(signatoryId);
        signSysCompactCopy.setSignName(signName);
        signSysCompactCopy.setSignContact(signContact);
        signSysCompactCopy.setGmtCreate(gmtCreate);
        signSysCompactCopy.setCreatUser(userId);
        signSysCompactCopy.setModifyUser(userId);
        signSysCompactCopyMapper.insert(signSysCompactCopy);
    }

    private String ckToken(HttpServletRequest request) {
        log.info("CompactInfoServiceImpl ckToken");
        String userId="";
        String token = request.getHeader(API_TOKEN);
        String sessionToken = request.getHeader(SESSION_TOKEN);
        if (StringUtils.isBlank(token) && StringUtils.isBlank(sessionToken)) {
            throw new BusinessException(ErrorCode.REQUEST_40434);
        }
        if(StringUtils.isNotBlank(sessionToken)){
            Claims claims = jwtUtils.parseJWT(sessionToken);
            userId = String.valueOf(claims.get("userId"));
        }else{
            SysSignApplication sysSignApplication = authTokenService.verifyAuthToken(token);
            userId = sysSignApplication.getUserId();
        }
        return userId;
    }


    /**
     * 签署人
     */
    @Data
    private static class Signer {
        private String compactId;
        private String userId;
        private Date gmtCreate;
        private String signatoryId;
        private String signName;
        private String signContact;
        private String nextSignatoryId;
        private String signStatus;
        private int logicalNumber;
        private String signWay;
    }

    /**
     * @ClassName: CompactInfoServiceImpl.java
     * @Description: 保存文件请求封装类
     * @Author: yml
     * @CreateDate: 2019/10/17
     * @UpdateUser: yml
     * @UpdateDate: 2019/10/17
     * @Version: 1.0.0
     */
    private static class SaveFileReqVo {
        private final String fileCode;
        private final Integer pageTotal;
        private final String fileType;
        private final String contractId;
        private final String userId;
        private final Date gmtCreate;
        private final String fileName;
        private final String fileCodeOrigin;

        /**
         * @param fileCode
         * @param pageTotal
         * @param id
         * @param userId
         * @param gmtCreate
         */
        private SaveFileReqVo(String fileCode, Integer pageTotal, String fileType, String id, String userId, Date gmtCreate, String fileName, String fileCodeOrigin) {
            this.fileCode = fileCode;
            this.pageTotal = pageTotal;
            this.fileType = fileType;
            contractId = id;
            this.userId = userId;
            this.gmtCreate = gmtCreate;
            this.fileName = fileName;
            this.fileCodeOrigin = fileCodeOrigin;
        }

        public String getFileCode() {
            return fileCode;
        }

        public Integer getPageTotal() {
            return pageTotal;
        }

        public String getFileType() {
            return fileType;
        }

        public String getContractId() {
            return contractId;
        }

        public String getUserId() {
            return userId;
        }

        public Date getGmtCreate() {
            return gmtCreate;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFileCodeOrigin() {
            return fileCodeOrigin;
        }
    }
}
