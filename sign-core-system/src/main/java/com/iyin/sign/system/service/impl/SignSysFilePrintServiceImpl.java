package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.iyin.sign.system.common.enums.AppEnum;
import com.iyin.sign.system.common.enums.UserTypeEnum;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.dto.req.SignSysFilePrintDTO;
import com.iyin.sign.system.entity.*;
import com.iyin.sign.system.mapper.*;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.FileResponseCode;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.model.sign.MultiParam;
import com.iyin.sign.system.service.FileResourceService;
import com.iyin.sign.system.service.IAuthTokenService;
import com.iyin.sign.system.service.ISignSysFilePrintService;
import com.iyin.sign.system.util.Base64Util;
import com.iyin.sign.system.util.JWTUtils;
import com.iyin.sign.system.util.PdfReadUtil;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.vo.file.FileUploadRespDto;
import com.iyin.sign.system.vo.req.PrintSummaryReqVO;
import com.iyin.sign.system.vo.req.SignSysContractPrintReq;
import com.iyin.sign.system.vo.req.SignSysFilePrintReq;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.itextpdf.text.Element.ALIGN_CENTER;

/**
 * <p>
 * 文件打印记录表 服务实现类
 * </p>
 *
 * @author wdf
 * @since 2019-07-19
 */
@Service
@Slf4j
public class SignSysFilePrintServiceImpl extends ServiceImpl<SignSysFilePrintMapper, SignSysFilePrint> implements ISignSysFilePrintService {

    private static final String API_TOKEN = "api_token";
    private static final String SESSION_TOKEN = "session_token";
    private static final String PDF = ".pdf";
    public static final int DPI = 145;

    private final FileServiceImpl fileService;
    @Resource
    private FileResourceMapper fileResourceMapper;
    private final FileResourceService fileResourceService;
    @Resource
    private SignSysFilePrintMapper signSysFilePrintMapper;
    @Resource
    private SignSysEnterpriseInfoMapper signSysEnterpriseInfoMapper;
    @Resource
    private SignSysUserInfoMapper signSysUserInfoMapper;
    @Resource
    private SignSysPersonalInfoMapper signSysPersonalInfoMapper;
    private final JWTUtils jwtUtils;
    @Resource
    private SignSysPrintAuthUserMapper signSysPrintAuthUserMapper;
    @Resource
    private ISysSignatureLogMapper signatureLogMapper;
    @Resource
    private ISignSysCompactInfoMapper signSysCompactInfoMapper;
    @Autowired
    private IAuthTokenService authTokenService;
    @Value("${fileTempPath}")
    private String fileTempPath;

    @Autowired
    public SignSysFilePrintServiceImpl(FileServiceImpl fileService, FileResourceService fileResourceService, JWTUtils jwtUtils) {
        this.fileService = fileService;
        this.fileResourceService = fileResourceService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public InMemoryMultipartFile addFilePrint(SignSysFilePrintReq signSysFilePrintReq,HttpServletRequest request) {
        //校验有没有打印次数
        String id=getUser(request);
        SignSysPrintAuthUser signSysPrintAuthUser=new SignSysPrintAuthUser();
        signSysPrintAuthUser.setFileCode(signSysFilePrintReq.getFileCode());
        signSysPrintAuthUser.setUserId(id);
        SignSysPrintAuthUser printAuth=signSysPrintAuthUserMapper.selectOne(new QueryWrapper<>(signSysPrintAuthUser));
        if(null==printAuth){
            throw new BusinessException(ErrorCode.REQUEST_20001);
        }else{
            if(printAuth.getPrintNum()<1){
                throw new BusinessException(ErrorCode.REQUEST_20000);
            }
        }

        log.info("addFilePrint printAuth:{}",printAuth.toString());
        SignSysUserInfo signSysUserInfo=signSysUserInfoMapper.selectById(id);
        SignSysFilePrint signSysFilePrint=new SignSysFilePrint();
        BeanUtils.copyProperties(signSysFilePrintReq,signSysFilePrint);
        signSysFilePrint.setId(String.valueOf(SnowflakeIdWorker.getId()));
        //用户相关
        signSysFilePrint.setUserChannel(AppEnum.USERAPI.getCode());
        signSysFilePrint.setPrintUser(id);
        signSysFilePrint.setUserEnterprise(signSysUserInfo.getEnterpriseOrPersonalId());
        signSysFilePrint.setUserType("01".equals(signSysUserInfo.getUserType())?1:2);
        signSysFilePrint.setGmtCreate(new Date());
        InMemoryMultipartFile in=fileService.fetch(signSysFilePrintReq.getFileCode());
        if(null!=in){
            signSysFilePrintMapper.insert(signSysFilePrint);
            FileResource res = fileResourceService.findByFileCode(signSysFilePrintReq.getFileCode());
            FileResource resource = new FileResource();
            resource.setFileCode(signSysFilePrintReq.getFileCode());
            resource.setPrintNum(res.getPrintNum() + 1);
            fileResourceMapper.updateFile(resource);
            SignSysPrintAuthUser pa=new SignSysPrintAuthUser();
            pa.setPrintNum(printAuth.getPrintNum()-1);
            pa.setId(printAuth.getId());
            signSysPrintAuthUserMapper.updateById(pa);
            List<SysSignatureLog> signatureLogs = signatureLogMapper.selectList(Wrappers.<SysSignatureLog>lambdaQuery().eq(SysSignatureLog::getFileCode, signSysFilePrintReq.getFileCode()).orderByDesc(SysSignatureLog::getCreateDate));
            if(0==printAuth.getIsFoggy()&&0==printAuth.getIsGrey()){
                return in;
            }
            byte[] bytes= new byte[0];
            try {
                bytes = in.getBytes();
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.DATA_IO_EXCEPTION);
            }
            for (SysSignatureLog sysSignatureLog : signatureLogs) {
                String multiParamStr = sysSignatureLog.getMultiParam();
                if(org.apache.commons.lang.StringUtils.isNotBlank(multiParamStr)){
                    MultiParam multiParam = parseObject(multiParamStr,MultiParam.class);
                    log.info("addFilePrint multiParam old:{}",multiParam);
                    try {
                        multiParam.setFoggy(0!=printAuth.getIsFoggy());
                        multiParam.setGrey(0!=printAuth.getIsGrey());
                        log.info("addFilePrint multiParam new:{}",multiParam);
                        bytes=PdfReadUtil.manipulatePdfNew(bytes, multiParam);
                    } catch (Exception e) {
                        log.error("打印失败 雾化 脱密 {}",e.getMessage());
                        throw new BusinessException(ErrorCode.REQUEST_20002);
                    }
                }
            }
            in = new InMemoryMultipartFile("file", in.getOriginalFilename(), in.getContentType(), bytes);
        }
        return in;
    }

    @Override
    public IyinResult<IyinPage<SignSysFilePrintDTO>> getFilePrintList(Integer currPage, Integer pageSize, String fileCode, HttpServletRequest request) {
        ckToken(request);
        IyinPage<SignSysFilePrintDTO> page=new IyinPage<>(currPage,pageSize);
        SignSysFilePrintDTO signSysFilePrintDTO=new SignSysFilePrintDTO();
        if(StringUtils.isNotBlank(fileCode)){
            signSysFilePrintDTO.setFileCode(fileCode);
        }
        List<SignSysFilePrintDTO> list=signSysFilePrintMapper.getPageList(page,signSysFilePrintDTO);

        //后台用户企业默认用第一个
        SignSysEnterpriseInfo belongEnterprise = signSysEnterpriseInfoMapper.selectAdminBelongEnterprise();
        for (SignSysFilePrintDTO print : list){
            if(AppEnum.USERMANAGER.getCode()==(print.getUserChannel())){
                //已跟产品确认，后台用户名默认为System
                print.setUserName("System");
                print.setEnterpriseName(belongEnterprise.getChineseName());
            }else{
                SignSysUserInfo signSysUserInfo=signSysUserInfoMapper.selectById(print.getPrintUser());
                if(null==signSysUserInfo){
                    continue;
                }
                //前台用户获取信息
                getApiUser(print, signSysUserInfo);
            }
        }
        page.setRecords(list);
        IyinResult<IyinPage<SignSysFilePrintDTO>> result=IyinResult.success();
        result.setData(page);
        return result;
    }

    private String ckToken(HttpServletRequest request) {
        log.info("SignSysFilePrintServiceImpl ckToken");
        String userId="";
        String token = request.getHeader(API_TOKEN);
        String sessionToken = request.getHeader(SESSION_TOKEN);
        if (org.apache.commons.lang.StringUtils.isBlank(token) && org.apache.commons.lang.StringUtils.isBlank(sessionToken)) {
            throw new BusinessException(ErrorCode.REQUEST_40434);
        }
        if(org.apache.commons.lang.StringUtils.isNotBlank(sessionToken)){
            Claims claims = jwtUtils.parseJWT(sessionToken);
            userId = String.valueOf(claims.get("userId"));
        }else{
            log.info("SignSysFilePrintServiceImpl apiToken");
            SysSignApplication sysSignApplication = authTokenService.verifyAuthToken(token);
            userId = sysSignApplication.getUserId();
        }
        return userId;
    }

    /**
     * 合同打印日志 并输出文件流
     *
     * @Author: yml
     * @CreateDate: 2019/9/3
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/3
     * @Version: 0.0.1
     * @param sysContractPrintReq
     * @param request
     * @return : com.iyin.sign.system.model.InMemoryMultipartFile
     */
    @Override
    public InMemoryMultipartFile addFilePrint2(SignSysContractPrintReq sysContractPrintReq, HttpServletRequest request) {
        //校验有没有打印次数
        String id=getUser(request);
        SignSysPrintAuthUser signSysPrintAuthUser=new SignSysPrintAuthUser();
        signSysPrintAuthUser.setFileCode(sysContractPrintReq.getContractId());
        signSysPrintAuthUser.setUserId(id);
        SignSysPrintAuthUser printAuth=signSysPrintAuthUserMapper.selectOne(new QueryWrapper<>(signSysPrintAuthUser));
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper
                .selectById(sysContractPrintReq.getContractId());
        if(null==printAuth && !signSysCompactInfo.getUserId().equals(id)){
            throw new BusinessException(ErrorCode.REQUEST_20001);
        }

        log.info("addFilePrint2 printAuth:{}",printAuth);
        SignSysUserInfo signSysUserInfo=signSysUserInfoMapper.selectById(id);
        SignSysFilePrint signSysFilePrint=new SignSysFilePrint();
        BeanUtils.copyProperties(sysContractPrintReq,signSysFilePrint);
        signSysFilePrint.setId(String.valueOf(SnowflakeIdWorker.getId()));
        //用户相关
        signSysFilePrint.setUserChannel(AppEnum.USERAPI.getCode());
        signSysFilePrint.setPrintUser(id);
        signSysFilePrint.setUserEnterprise(signSysUserInfo.getEnterpriseOrPersonalId());
        signSysFilePrint.setUserType("01".equals(signSysUserInfo.getUserType())?1:2);
        signSysFilePrint.setGmtCreate(new Date());
        InMemoryMultipartFile in=fileService.fetch(sysContractPrintReq.getFileCode());
        if(null!=in){
            signSysFilePrintMapper.insert(signSysFilePrint);
            FileResource res = fileResourceService.findByFileCode(sysContractPrintReq.getFileCode());
            FileResource resource = new FileResource();
            resource.setFileCode(sysContractPrintReq.getFileCode());
            resource.setPrintNum(res.getPrintNum() + 1);
            fileResourceMapper.updateFile(resource);
            List<SysSignatureLog> signatureLogs = signatureLogMapper.selectList(Wrappers.<SysSignatureLog>lambdaQuery().eq(SysSignatureLog::getFileCode, sysContractPrintReq.getFileCode()).orderByDesc(SysSignatureLog::getCreateDate));
            if(null == printAuth || (0==printAuth.getIsFoggy()&&0==printAuth.getIsGrey())){
                return in;
            }
            byte[] bytes= new byte[0];
            try {
                bytes = in.getBytes();
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
            }
            for (SysSignatureLog sysSignatureLog : signatureLogs) {
                String multiParamStr = sysSignatureLog.getMultiParam();
                if(org.apache.commons.lang.StringUtils.isNotBlank(multiParamStr)){
                    MultiParam multiParam = parseObject(multiParamStr,MultiParam.class);
                    log.info("addFilePrint2 multiParam old:{}",multiParam);
                    try {
                        multiParam.setFoggy(0!=printAuth.getIsFoggy());
                        multiParam.setGrey(0!=printAuth.getIsGrey());
                        log.info("addFilePrint2 multiParam new:{}",multiParam);
                        bytes=PdfReadUtil.manipulatePdfNew(bytes, multiParam);
                    } catch (Exception e) {
                        log.error("addFilePrint2 打印失败 雾化 脱密 {}",e.getMessage());
                        throw new BusinessException(ErrorCode.REQUEST_20002);
                    }
                }
            }
            in = new InMemoryMultipartFile("file", in.getOriginalFilename(), in.getContentType(), bytes);
        }
        return in;
    }

    /**
     * 打印摘要
     *
     * @Author: yml
     * @CreateDate: 2019/9/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/23
     * @Version: 0.0.1
     * @param printSummaryReqVO
     * @param request
     * @return : java.lang.Integer
     */
    @Override
    public InMemoryMultipartFile printSummary(PrintSummaryReqVO printSummaryReqVO, HttpServletRequest request) throws IOException {
        printSummaryReqVO.setOrigin(null == printSummaryReqVO.getOrigin() ? Boolean.FALSE : printSummaryReqVO.getOrigin());
        String uuidFilePath = UUID.randomUUID().toString() + ".html";
        File filePath = new File(fileTempPath + uuidFilePath);
        String fileCode = null;
        int file = 0;
        String folder = fileTempPath + SnowflakeIdWorker.getIdToStr() + File.separator;
        try (OutputStream os = new FileOutputStream(filePath); PrintStream ps = new PrintStream(os)) {
            String context = printSummaryReqVO.getContext();
            if (!StringUtils.isBlank(context)) {
                ps.println(context);
                FileUploadRespDto fileUploadRespDto = fileService
                        .uploadCovertPdfData(new InMemoryMultipartFile(filePath, uuidFilePath), request);
                if (null == fileUploadRespDto) {
                    throw new BusinessException(ErrorCode.SERVER_50401);
                }
                fileCode = fileUploadRespDto.getFile().getFileCode();
                byte[] bytes1 = getImage(fileCode, printSummaryReqVO);
                Base64Util.byteArrayToFile(bytes1, folder + file + PDF);
            }
            List<String> fileCodes = printSummaryReqVO.getFileCodes();
            for (String fileCode0 : fileCodes) {
                byte[] bytes2 = getImage(fileCode0, printSummaryReqVO);
                Base64Util.byteArrayToFile(bytes2, folder + ++file + PDF);
            }
            String destPath = folder + "all.pdf";
            log.info("com.iyin.sign.system.service.impl.SignSysFilePrintServiceImpl.printSummary,destPath,{}",
                    destPath);
            merge(folder, destPath);
            log.info("com.iyin.sign.system.service.impl.SignSysFilePrintServiceImpl.printSummary,merge");
            byte[] bytes = Base64Util.fileToByte(destPath);
            return new InMemoryMultipartFile("file", "summary.pdf", "application/pdf", bytes);
        } catch (IOException e) {
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        } finally {
            Files.delete(filePath.toPath());
            if (null != fileCode) {
                fileService.del(fileCode);
            }
            FileUtils.deleteDirectory(new File(folder));
        }
    }

    /**
     * merge
     *
     * @Author: yml
     * @CreateDate: 2019/9/25
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/25
     * @Version: 0.0.1
     * @param folderName
     * @param destPath
     * @return : void
     */
    public static void merge(String folderName, String destPath) throws IOException {
        PDFMergerUtility mergePdf = new PDFMergerUtility();
        String[] filesInFolder = getFiles(folderName);
        Arrays.sort(filesInFolder, String::compareTo);
        for (int i = 0; i < filesInFolder.length; i++) {
            mergePdf.addSource(folderName + File.separator + filesInFolder[i]);
        }
        mergePdf.setDestinationFileName(destPath);
        mergePdf.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
    }

    /**
     * 获取文件夹下文件集
     *
     * @Author: yml
     * @CreateDate: 2019/9/25
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/25
     * @Version: 0.0.1
     * @param folder
     * @return : java.lang.String[]
     */
    private static String[] getFiles(String folder) throws IOException {
        File file = new File(folder);
        String[] filesInFolder;

        if (file.isDirectory()) {
            filesInFolder = file.list((dir, name) -> name.endsWith(".pdf"));
            return filesInFolder;
        } else {
            throw new IOException("Path is not a directory");
        }
    }

    /**
     * 处理
     *
     * @Author: yml
     * @CreateDate: 2019/9/25
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/25
     * @Version: 0.0.1
     * @param fileCode
     * @param printSummaryReqVO
     * @return : byte[]
     */
    private byte[] getImage(String fileCode, PrintSummaryReqVO printSummaryReqVO) {
        List<SysSignatureLog> signatureLogs = Lists.newArrayList();
        boolean b = printSummaryReqVO.getAtomization() || printSummaryReqVO.getGraying();
        if (Boolean.FALSE.equals(printSummaryReqVO.getOrigin()) || b) {
            signatureLogs = signatureLogMapper.selectList(
                    Wrappers.<SysSignatureLog>lambdaQuery().eq(SysSignatureLog::getFileCode, fileCode)
                            .orderByDesc(SysSignatureLog::getCreateDate));
        }
        byte[] bytes = new byte[0];
        InMemoryMultipartFile in = fileService
                .fetchWithC(fileCode, printSummaryReqVO.getGrating(), printSummaryReqVO.getWatermark(),
                        Boolean.TRUE.equals(printSummaryReqVO.getOrigin()));
        try {
            bytes = in.getBytes();
        } catch (IOException e) {
            log.error("com.iyin.sign.system.service.impl.SignSysFilePrintServiceImpl.getImage,html,{}" +
                    e.getLocalizedMessage());
        }
        for (SysSignatureLog sysSignatureLog : signatureLogs) {
            String multiParamStr = sysSignatureLog.getMultiParam();
            if (org.apache.commons.lang.StringUtils.isNotBlank(multiParamStr)) {
                MultiParam multiParam = parseObject(multiParamStr, MultiParam.class);
                log.info("addFilePrint2 multiParam old:{}", multiParam);
                try {
                    multiParam.setFoggy(Boolean.TRUE.equals(printSummaryReqVO.getAtomization()));
                    multiParam.setGrey(Boolean.TRUE.equals(printSummaryReqVO.getGraying()));
                    log.info("addFilePrint2 multiParam new:{}", multiParam);
                    if(multiParam.isFoggy() || multiParam.isGrey()){
                        bytes = PdfReadUtil.manipulatePdfNew(bytes, multiParam);
                    }
                } catch (Exception e) {
                    log.error("addFilePrint2 打印失败 雾化 脱密 {}", e.getMessage());
                    throw new BusinessException(ErrorCode.REQUEST_20002);
                }
            }
        }
        return bytes;
    }

    private void getApiUser(SignSysFilePrintDTO print, SignSysUserInfo signSysUserInfo) {
        if(UserTypeEnum.PERSONAL_USER.getCode().equals(signSysUserInfo.getUserType())){
            SignSysPersonalInfo signSysPersonalInfo = signSysPersonalInfoMapper.selectById(signSysUserInfo.getEnterpriseOrPersonalId());
            if(null!=signSysPersonalInfo){
                print.setUserName(signSysUserInfo.getUserNickName());
                //个人企业名称填写个人名字
                print.setEnterpriseName(signSysPersonalInfo.getPersonalName());
            }
        }else{
            SignSysEnterpriseInfo signSysEnterpriseInfo = signSysEnterpriseInfoMapper.selectById(signSysUserInfo.getEnterpriseOrPersonalId());
            if(null!=signSysEnterpriseInfo) {
                print.setUserName(signSysUserInfo.getUserNickName());
                print.setEnterpriseName(signSysEnterpriseInfo.getChineseName());
            }
        }
    }

    private String getUser(HttpServletRequest request) {
        log.info("SignSysFilePrintServiceImpl");
        String sessionToken = request.getHeader(SESSION_TOKEN);
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
