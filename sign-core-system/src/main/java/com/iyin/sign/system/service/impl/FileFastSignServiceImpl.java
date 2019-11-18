package com.iyin.sign.system.service.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.iyin.sign.system.common.enums.AppEnum;
import com.iyin.sign.system.common.enums.SignatureEnum;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.common.exception.ServiceException;
import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.common.utils.Base64MultipartFile;
import com.iyin.sign.system.common.utils.GetKeyWordPositionUtil;
import com.iyin.sign.system.entity.SysSignApplication;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.code.BaseResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.model.exception.IyinResultValidUtil;
import com.iyin.sign.system.service.*;
import com.iyin.sign.system.util.Base64Util;
import com.iyin.sign.system.util.FileUtil;
import com.iyin.sign.system.util.PdfUtil;
import com.iyin.sign.system.vo.file.FileResourceDto;
import com.iyin.sign.system.vo.file.FileUploadRespDto;
import com.iyin.sign.system.vo.sign.req.*;
import com.iyin.sign.system.vo.sign.resp.FileSignRespDTO;
import com.iyin.sign.system.vo.sign.resp.SignRespDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: FastSignServiceImpl
 * @Description: 快捷签
 * @Author: wdf
 * @CreateDate: 2019/6/24
 * @UpdateUser: wdf
 * @UpdateDate: 2019/6/24
 * @Version: 1.0.0
 */
@Service
@Slf4j
@Validated
public class FileFastSignServiceImpl implements IFileFastSignService {

    private final IAuthTokenService authTokenService;
    private final ISignService signService;
    private final IFileService fileService;

    private final AsyncService asyncService;

    private static final String KEY_WORD_TIME = "关键字时间：";
    private static final String SIGN_TIME = "调用签署微服务时间：";
    private static final String SINGLE_API_TIME = "签章API总时间";
    private static final String SIGN_EXCEPTION = "签章异常{}";
    private static final String PDF_STR = "入参pdf文件名称pdfName=:{},文件类型pdfFileType=:{}";
    private static final String IMAGE = "image";
    private static final String API_TOKEN = "api_token";

    @Autowired
    public FileFastSignServiceImpl(IAuthTokenService authTokenService, ISignService signService, IFileService fileService, AsyncService asyncService) {
        this.authTokenService = authTokenService;
        this.signService = signService;
        this.fileService = fileService;
        this.asyncService = asyncService;
    }

    /**
     * 快捷单页签章
     *
     * @Author: wdf
     * @CreateDate: 2019/6/24
     * @UpdateUser: wdf
     * @UpdateDate: 2019/6/24
     * @Version: 0.0.1
     * @param signFile
     * @param sealFile
     * @param certFile
     * @param singleFileFastSignReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.FileSignRespDTO>
     */
    @Override
    public EsealResult<FileSignRespDTO> singleSign(MultipartFile signFile, MultipartFile sealFile, MultipartFile certFile, @Valid SingleFileFastSignReqVO singleFileFastSignReqVO
            , HttpServletRequest request) {
        try {
            //apiToken
            long time = System.currentTimeMillis();
            String apiToken=request.getHeader(API_TOKEN);
            if (StringUtils.isBlank(apiToken)) {
                throw new ServiceException(ErrorCode.REQUEST_40434);
            }
            SysSignApplication app = authTokenService.verifyAuthToken(apiToken);
            if(null==app){
                throw new ServiceException(ErrorCode.REQUEST_40307);
            }
            if(StringUtils.isBlank(app.getUserAppId())){
                throw new ServiceException(ErrorCode.REQUEST_40307);
            }
            //文件流
            ckFileParam(signFile, sealFile, certFile, singleFileFastSignReqVO);
            String appId = app.getUserAppId();
            SingleSignReqVO singleSignReqVO = new SingleSignReqVO();
            getSingleSignParam(singleFileFastSignReqVO, singleSignReqVO);

            long recordReqLogTime = System.currentTimeMillis();
            doOperateData(singleFileFastSignReqVO, singleSignReqVO);
            long getKeyWordTime = System.currentTimeMillis();
            log.info(KEY_WORD_TIME + (getKeyWordTime - recordReqLogTime));

            EsealResult<SignRespDTO> respResult = signService.setSingleSign(singleSignReqVO);
            IyinResultValidUtil.validateSuccess(respResult);
            long getSignTime = System.currentTimeMillis();
            log.info(SIGN_TIME + (getSignTime - getKeyWordTime));

            SignRespDTO signRespDTO = respResult.getData();
            String fileBase64 = signRespDTO.getSignCompleteFileStr();
            LogCommonParam logCommonParam = getLogCommonParam(singleFileFastSignReqVO.getUserId(), fileBase64,singleFileFastSignReqVO.getOriginalFileName());
            signRespDTO.setFileCode(logCommonParam.getSignFileCode());

            //组装日志参数
            getSignCommonLogParam(appId, logCommonParam, singleFileFastSignReqVO.getSealFileName(), singleFileFastSignReqVO.getSealType(),
                    singleFileFastSignReqVO.getMediumType(), singleFileFastSignReqVO.getOriginalFileName());
            signLog(singleFileFastSignReqVO.getUserId(), logCommonParam, signRespDTO);

            //处理返回数据
            EsealResult<FileSignRespDTO> respNewResult=getBackData(respResult, singleFileFastSignReqVO.getOutType());
            log.info(SINGLE_API_TIME + (System.currentTimeMillis() - time));
            return respNewResult;
        } catch (Exception e) {
            log.error(SIGN_EXCEPTION, e.getMessage());
          throw new BusinessException(ErrorCode.REQUEST_40472.getCode(),e.getMessage());
        }
    }

    /**
     * 组装单页签章参数
     */
    private void getSingleSignParam(SingleFileFastSignReqVO singleFileFastSignReqVO, SingleSignReqVO singleSignReqVO) {
        singleSignReqVO.setSignOriginalFileStr(singleFileFastSignReqVO.getSignatureFileStr());
        singleSignReqVO.setFoggy(singleFileFastSignReqVO.isFoggy());
        singleSignReqVO.setGrey(singleFileFastSignReqVO.isGrey());
        //章模、证书
        singleSignReqVO.setCertPassword(singleFileFastSignReqVO.getCertPassword());
        singleSignReqVO.setCertStr(singleFileFastSignReqVO.getCertBase64Str());
        singleSignReqVO.setSealStr(singleFileFastSignReqVO.getSealBase64Str());
    }

    /**
     * 处理返回数据
     */
    private EsealResult<FileSignRespDTO> getBackData(EsealResult<SignRespDTO> respResult, int outType) {
        EsealResult<FileSignRespDTO> respNewResult=new EsealResult<>();
        FileSignRespDTO fsr=new FileSignRespDTO();
        if (AppEnum.TYPEFILE.getCode() == outType) {
            fsr.setFileCode(respResult.getData().getFileCode());
            fsr.setMultiParam(respResult.getData().getMultiParam());
            respNewResult.setData(fsr);
        } else {
            fsr.setSignCompleteFileStr(respResult.getData().getSignCompleteFileStr());
            fsr.setMultiParam(respResult.getData().getMultiParam());
            respNewResult.setData(fsr);
        }
        return respNewResult;
    }

    @Override
    public EsealResult<FileSignRespDTO> singleCoordBatchSign(MultipartFile signFile, MultipartFile sealFile, MultipartFile certFile, @Valid SingleFileFastSignMoreReqVO singleFileFastSignMoreReqVO, HttpServletRequest request) {
        try {
            //apiToken
            long time = System.currentTimeMillis();
            String apiToken=request.getHeader(API_TOKEN);
            if (StringUtils.isBlank(apiToken)) {
                throw new ServiceException(ErrorCode.REQUEST_40434);
            }
            SysSignApplication app = authTokenService.verifyAuthToken(apiToken);
            if(null==app){
                throw new ServiceException(ErrorCode.REQUEST_40307);
            }
            if(StringUtils.isBlank(app.getUserAppId())){
                throw new ServiceException(ErrorCode.REQUEST_40307);
            }
            //文件流
            ckMoreFileParam(signFile, sealFile, certFile, singleFileFastSignMoreReqVO);
            String appId = app.getUserAppId();
            BatchSignReqVO batchSignReqVO = new BatchSignReqVO();
            //组装签章参数
            getMoreSignParam(singleFileFastSignMoreReqVO, batchSignReqVO);
            long recordReqLogTime = System.currentTimeMillis();
            EsealResult<SignRespDTO> respResult = signService.setBatchSign(batchSignReqVO);
            IyinResultValidUtil.validateSuccess(respResult);
            long getSignTime = System.currentTimeMillis();
            log.info(SIGN_TIME + (getSignTime - recordReqLogTime));

            SignRespDTO signRespDTO = respResult.getData();
            String fileBase64 = signRespDTO.getSignCompleteFileStr();
            LogCommonParam logCommonParam = getLogCommonParam(singleFileFastSignMoreReqVO.getUserId(), fileBase64,singleFileFastSignMoreReqVO.getOriginalFileName());
            signRespDTO.setFileCode(logCommonParam.getSignFileCode());
            //组装日志参数
            getSignCommonLogParam(appId, logCommonParam, singleFileFastSignMoreReqVO.getSealFileName(), singleFileFastSignMoreReqVO.getSealType(),
                    singleFileFastSignMoreReqVO.getMediumType(), singleFileFastSignMoreReqVO.getOriginalFileName());
            signLog(singleFileFastSignMoreReqVO.getUserId(), logCommonParam, signRespDTO);

            //处理返回数据
            EsealResult<FileSignRespDTO> respNewResult=getBackData(respResult, singleFileFastSignMoreReqVO.getOutType());
            log.info(SINGLE_API_TIME + (System.currentTimeMillis() - time));
            return respNewResult;
        } catch (Exception e) {
            log.error(SIGN_EXCEPTION, e.getMessage());
            throw new BusinessException(ErrorCode.REQUEST_40472.getCode(),e.getMessage());
        }
    }

    /**
     * 组装日志参数
     */
    private void getSignCommonLogParam(String appId, LogCommonParam logCommonParam, String sealFileName, String sealType, String mediumType, String originalFileName) {
        logCommonParam.setSignatureModel(SignatureEnum.SIGNATURE_KEYWORD.getCode());
        logCommonParam.setSignatureName(SignatureEnum.SIGNATURE_SINGLE.getCode());
        logCommonParam.setSealName(sealFileName);
        logCommonParam.setSealType(sealType);
        logCommonParam.setMediumType(mediumType);
        logCommonParam.setFileName(originalFileName);
        logCommonParam.setAppId(appId);
    }

    /**
     * 组装多页签章参数
     */
    private void getMoreSignParam(SingleFileFastSignMoreReqVO singleFileFastSignMoreReqVO, BatchSignReqVO batchSignReqVO) {
        batchSignReqVO.setSignOriginalFileStr(singleFileFastSignMoreReqVO.getSignatureFileStr());
        batchSignReqVO.setFoggy(singleFileFastSignMoreReqVO.isFoggy());
        batchSignReqVO.setGrey(singleFileFastSignMoreReqVO.isGrey());
        batchSignReqVO.setCoordinateX(Float.parseFloat(singleFileFastSignMoreReqVO.getCoordinateX()));
        batchSignReqVO.setCoordinateY(Float.parseFloat(singleFileFastSignMoreReqVO.getCoordinateY()));
        //页码
        int startPageNumber = Integer.parseInt(singleFileFastSignMoreReqVO.getStartPageNo());
        int endPageNumber = Integer.parseInt(singleFileFastSignMoreReqVO.getEndPageNo());
        if(startPageNumber > endPageNumber){
            throw new BusinessException(ErrorCode.REQUEST_20004);
        }
        batchSignReqVO.setStartPageNumber(startPageNumber);
        batchSignReqVO.setEndPageNumber(endPageNumber);

        //根据章模编码获取章模路径、证书路径
        batchSignReqVO.setCertPassword(singleFileFastSignMoreReqVO.getCertPassword());
        batchSignReqVO.setCertStr(singleFileFastSignMoreReqVO.getCertBase64Str());
        batchSignReqVO.setSealStr(singleFileFastSignMoreReqVO.getSealBase64Str());
    }

    @Override
    public EsealResult<FileSignRespDTO> singlePerforationCoordSign(MultipartFile signFile, MultipartFile sealFile, MultipartFile certFile, @Valid FastFilePerforationSignReqVO fastFilePerforationSignReqVO, HttpServletRequest request) {
        try {
            //apiToken
            long time = System.currentTimeMillis();
            String apiToken=request.getHeader(API_TOKEN);
            if (StringUtils.isBlank(apiToken)) {
                throw new ServiceException(ErrorCode.REQUEST_40434);
            }
            SysSignApplication app = authTokenService.verifyAuthToken(apiToken);
            if(null==app){
                throw new ServiceException(ErrorCode.REQUEST_40307);
            }
            if(StringUtils.isBlank(app.getUserAppId())){
                throw new ServiceException(ErrorCode.REQUEST_40307);
            }
            //文件流
            ckPerforationFileParam(signFile, sealFile, certFile, fastFilePerforationSignReqVO);

            String appId = app.getUserAppId();
            long recordReqLogTime = System.currentTimeMillis();
            PerforationSignReqVO perforationSignReqVO = new PerforationSignReqVO();
            //组装签章参数
            getPerforationSignParam(fastFilePerforationSignReqVO, perforationSignReqVO);

            EsealResult<SignRespDTO> respResult = signService.setPerforationSign(perforationSignReqVO);
            IyinResultValidUtil.validateSuccess(respResult);
            long getSignTime = System.currentTimeMillis();
            log.info(SIGN_TIME + (getSignTime - recordReqLogTime));

            SignRespDTO signRespDTO = respResult.getData();
            String fileBase64 = signRespDTO.getSignCompleteFileStr();
            LogCommonParam logCommonParam = getLogCommonParam(fastFilePerforationSignReqVO.getUserId(), fileBase64,fastFilePerforationSignReqVO.getOriginalFileName());
            signRespDTO.setFileCode(logCommonParam.getSignFileCode());
            //组装日志参数
            getSignCommonLogParam(appId, logCommonParam, fastFilePerforationSignReqVO.getSealFileName(), fastFilePerforationSignReqVO.getSealType(), fastFilePerforationSignReqVO.getMediumType(), fastFilePerforationSignReqVO.getOriginalFileName());
            signLog(fastFilePerforationSignReqVO.getUserId(), logCommonParam, signRespDTO);

            //处理返回数据
            EsealResult<FileSignRespDTO> respNewResult=getBackData(respResult, fastFilePerforationSignReqVO.getOutType());
            log.info(SINGLE_API_TIME + (System.currentTimeMillis() - time));
            return respNewResult;
        } catch (Exception e) {
            log.error(SIGN_EXCEPTION, e.getMessage());
            throw new BusinessException(ErrorCode.REQUEST_40472.getCode(),e.getMessage());
        }
    }

    @Override
    public EsealResult<FileSignRespDTO> singlePerforationCoordHalfSign(MultipartFile signFile, MultipartFile sealFile, MultipartFile certFile, @Valid FastFilePerforationSignHalfReqVO fastFilePerforationSignHalfReqVO, HttpServletRequest request) {
        try {
            //apiToken
            long time = System.currentTimeMillis();
            String apiToken=request.getHeader(API_TOKEN);
            if (StringUtils.isBlank(apiToken)) {
                throw new ServiceException(ErrorCode.REQUEST_40434);
            }
            SysSignApplication app = authTokenService.verifyAuthToken(apiToken);
            if(null==app){
                throw new ServiceException(ErrorCode.REQUEST_40307);
            }
            if(StringUtils.isBlank(app.getUserAppId())){
                throw new ServiceException(ErrorCode.REQUEST_40307);
            }
            //文件流
            ckPerforationFileHalfParam(signFile, sealFile, certFile, fastFilePerforationSignHalfReqVO);

            String appId = app.getUserAppId();
            long recordReqLogTime = System.currentTimeMillis();
            PerforationHalfSignReqVO perforationHalfSignReqVO = new PerforationHalfSignReqVO();
            //组装签章参数
            getPerforationSignHalfParam(fastFilePerforationSignHalfReqVO, perforationHalfSignReqVO);

            EsealResult<SignRespDTO> respResult = signService.setPerforationHalfSign(perforationHalfSignReqVO);
            IyinResultValidUtil.validateSuccess(respResult);
            long getSignTime = System.currentTimeMillis();
            log.info(SIGN_TIME + (getSignTime - recordReqLogTime));

            SignRespDTO signRespDTO = respResult.getData();
            String fileBase64 = signRespDTO.getSignCompleteFileStr();
            LogCommonParam logCommonParam = getLogCommonParam(fastFilePerforationSignHalfReqVO.getUserId(), fileBase64,fastFilePerforationSignHalfReqVO.getOriginalFileName());
            signRespDTO.setFileCode(logCommonParam.getSignFileCode());
            //组装日志参数
            getSignCommonLogParam(appId, logCommonParam, fastFilePerforationSignHalfReqVO.getSealFileName(), fastFilePerforationSignHalfReqVO.getSealType(), fastFilePerforationSignHalfReqVO.getMediumType(), fastFilePerforationSignHalfReqVO.getOriginalFileName());

            String[] multi=respResult.getData().getMultiParam().split("XXX");
            for(int x=0;x<multi.length;x++){
                signRespDTO.setMultiParam(multi[x]);
                signLog(fastFilePerforationSignHalfReqVO.getUserId(), logCommonParam, signRespDTO);
            }
            //处理返回数据
            EsealResult<FileSignRespDTO> respNewResult=getBackData(respResult, fastFilePerforationSignHalfReqVO.getOutType());
            log.info(SINGLE_API_TIME + (System.currentTimeMillis() - time));
            return respNewResult;
        } catch (Exception e) {
            log.error(SIGN_EXCEPTION, e.getMessage());
            throw new BusinessException(ErrorCode.REQUEST_40472.getCode(),e.getMessage());
        }
    }

    /**
     * 处理骑缝连页签章数据
     */
    private void getPerforationSignHalfParam(FastFilePerforationSignHalfReqVO fastFilePerforationSignHalfReqVO, PerforationHalfSignReqVO perforationHalfSignReqVO) {
        log.info("连页骑缝参数处理 getPerforationSignHalfParam");
        perforationHalfSignReqVO.setSignOriginalFileStr(fastFilePerforationSignHalfReqVO.getSignatureFileStr());
        perforationHalfSignReqVO.setFoggy(fastFilePerforationSignHalfReqVO.isFoggy());
        perforationHalfSignReqVO.setGrey(fastFilePerforationSignHalfReqVO.isGrey());
        perforationHalfSignReqVO.setCoordinateY(Float.parseFloat(fastFilePerforationSignHalfReqVO.getCoordinateY()));
        //页码
        perforationHalfSignReqVO.setStartPageNumber(Integer.parseInt(fastFilePerforationSignHalfReqVO.getStartPageNo()));
        perforationHalfSignReqVO.setEndPageNumber(Integer.parseInt(fastFilePerforationSignHalfReqVO.getEndPageNo()));
        perforationHalfSignReqVO.setSignatureDirection(fastFilePerforationSignHalfReqVO.getSignatureDirection());
        //章模、证书
        perforationHalfSignReqVO.setCertPassword(fastFilePerforationSignHalfReqVO.getCertPassword());
        perforationHalfSignReqVO.setCertStr(fastFilePerforationSignHalfReqVO.getCertBase64Str());
        perforationHalfSignReqVO.setSealStr(fastFilePerforationSignHalfReqVO.getSealBase64Str());
    }

    /**
     * 处理骑缝签章数据
     */
    private void getPerforationSignParam(FastFilePerforationSignReqVO fastFilePerforationSignReqVO, PerforationSignReqVO perforationSignReqVO) {
        perforationSignReqVO.setSignOriginalFileStr(fastFilePerforationSignReqVO.getSignatureFileStr());
        perforationSignReqVO.setFoggy(fastFilePerforationSignReqVO.isFoggy());
        perforationSignReqVO.setGrey(fastFilePerforationSignReqVO.isGrey());
        perforationSignReqVO.setCoordinateY(Float.parseFloat(fastFilePerforationSignReqVO.getCoordinateY()));
        //页码
        perforationSignReqVO.setStartPageNumber(Integer.parseInt(fastFilePerforationSignReqVO.getStartPageNo()));
        perforationSignReqVO.setEndPageNumber(Integer.parseInt(fastFilePerforationSignReqVO.getEndPageNo()));
        perforationSignReqVO.setSignatureDirection(fastFilePerforationSignReqVO.getSignatureDirection());
        //章模、证书
        perforationSignReqVO.setCertPassword(fastFilePerforationSignReqVO.getCertPassword());
        perforationSignReqVO.setCertStr(fastFilePerforationSignReqVO.getCertBase64Str());
        perforationSignReqVO.setSealStr(fastFilePerforationSignReqVO.getSealBase64Str());
    }

    /**
     * 处理骑缝连页签章文件参数
     */
    private void ckPerforationFileHalfParam(MultipartFile signFile, MultipartFile sealFile, MultipartFile certFile, FastFilePerforationSignHalfReqVO fastFilePerforationSignHalfReqVO) throws ServiceException, IOException {
        if(AppEnum.TYPEFILE.getCode()==fastFilePerforationSignHalfReqVO.getInType()) {
            log.info("骑缝连页签章 文件流");
            String pdfFileType = ckFileNull(signFile, sealFile, certFile);
            if (!(MediaType.APPLICATION_PDF_VALUE.equals(pdfFileType))) {
                fastFilePerforationSignHalfReqVO.setSignatureFileStr(fileService.uploadCovertPdf(signFile));
            }else{
                fastFilePerforationSignHalfReqVO.setSignatureFileStr(Base64Util.encode(signFile.getBytes()));
            }
            fastFilePerforationSignHalfReqVO.setOriginalFileName(signFile.getOriginalFilename());
            if (org.springframework.util.StringUtils.hasText(sealFile.getContentType()) && sealFile.getContentType().toLowerCase().startsWith(IMAGE)) {
                fastFilePerforationSignHalfReqVO.setSealBase64Str(Base64Util.encode(sealFile.getBytes()));
                fastFilePerforationSignHalfReqVO.setSealFileName(sealFile.getOriginalFilename());
            }else {
                throw new ServiceException(ErrorCode.SEAL_FILE_TYPE_NAME_ERROR);
            }
            //暂时不做类型校验
            fastFilePerforationSignHalfReqVO.setCertBase64Str(Base64Util.encode(certFile.getBytes()));
            fastFilePerforationSignHalfReqVO.setCertFileName(certFile.getOriginalFilename());
        }else{
            log.info("骑缝连页签章 base64");
            if(StringUtils.isBlank(fastFilePerforationSignHalfReqVO.getSignatureFileStr()) ||
                    StringUtils.isBlank(fastFilePerforationSignHalfReqVO.getSealBase64Str()) ||
                    StringUtils.isBlank(fastFilePerforationSignHalfReqVO.getCertBase64Str())){
                throw new ServiceException(ErrorCode.REQUEST_40427);
            }
            if(StringUtils.isBlank(fastFilePerforationSignHalfReqVO.getOriginalFileName())){
                throw new ServiceException(ErrorCode.PDF_FILE_NAME_ERROR);
            }
            if(StringUtils.isBlank(fastFilePerforationSignHalfReqVO.getCertFileName())){
                throw new ServiceException(ErrorCode.CERT_FILE_NAME_ERROR);
            }
            if(StringUtils.isBlank(fastFilePerforationSignHalfReqVO.getSealFileName())){
                throw new ServiceException(ErrorCode.SEAL_FILE_NAME_ERROR);
            }
        }
    }

    /**
     * 处理骑缝签章文件参数
     */
    private void ckPerforationFileParam(MultipartFile signFile, MultipartFile sealFile, MultipartFile certFile, FastFilePerforationSignReqVO fastFilePerforationSignReqVO) throws ServiceException, IOException {
        if(AppEnum.TYPEFILE.getCode()==fastFilePerforationSignReqVO.getInType()) {
            log.info("骑缝签章 文件流");
            String pdfFileType = ckFileNull(signFile, sealFile, certFile);
            if (!(MediaType.APPLICATION_PDF_VALUE.equals(pdfFileType))) {
                fastFilePerforationSignReqVO.setSignatureFileStr(fileService.uploadCovertPdf(signFile));
            }else{
                fastFilePerforationSignReqVO.setSignatureFileStr(Base64Util.encode(signFile.getBytes()));
            }
            fastFilePerforationSignReqVO.setOriginalFileName(signFile.getOriginalFilename());
            if (org.springframework.util.StringUtils.hasText(sealFile.getContentType()) && sealFile.getContentType().toLowerCase().startsWith(IMAGE)) {
                fastFilePerforationSignReqVO.setSealBase64Str(Base64Util.encode(sealFile.getBytes()));
                fastFilePerforationSignReqVO.setSealFileName(sealFile.getOriginalFilename());
            }else {
                throw new ServiceException(ErrorCode.SEAL_FILE_TYPE_NAME_ERROR);
            }
            //暂时不做类型校验
            fastFilePerforationSignReqVO.setCertBase64Str(Base64Util.encode(certFile.getBytes()));
            fastFilePerforationSignReqVO.setCertFileName(certFile.getOriginalFilename());
        }else{
            log.info("骑缝签章 base64");
            if(StringUtils.isBlank(fastFilePerforationSignReqVO.getSignatureFileStr()) ||
                    StringUtils.isBlank(fastFilePerforationSignReqVO.getSealBase64Str()) ||
                    StringUtils.isBlank(fastFilePerforationSignReqVO.getCertBase64Str())){
                throw new ServiceException(ErrorCode.REQUEST_40427);
            }
            if(StringUtils.isBlank(fastFilePerforationSignReqVO.getOriginalFileName())){
                throw new ServiceException(ErrorCode.PDF_FILE_NAME_ERROR);
            }
            if(StringUtils.isBlank(fastFilePerforationSignReqVO.getCertFileName())){
                throw new ServiceException(ErrorCode.CERT_FILE_NAME_ERROR);
            }
            if(StringUtils.isBlank(fastFilePerforationSignReqVO.getSealFileName())){
                throw new ServiceException(ErrorCode.SEAL_FILE_NAME_ERROR);
            }
        }
    }

    /**
     * 待签章文件相关校验
     */
    private String ckFileNull(MultipartFile signFile, MultipartFile sealFile, MultipartFile certFile) throws ServiceException {
        if (null == signFile || null == sealFile || null == certFile) {
            throw new ServiceException(ErrorCode.REQUEST_40427);
        }
        // 校验签章文件格式
        String pdfFileName = signFile.getOriginalFilename();
        String pdfFileType = signFile.getContentType();
        log.info(PDF_STR, pdfFileName, pdfFileType);
        return pdfFileType;
    }

    /**
     * 处理多页签章文件参数
     */
    private void ckMoreFileParam(MultipartFile signFile, MultipartFile sealFile, MultipartFile certFile,
                                 SingleFileFastSignMoreReqVO singleFileFastSignMoreReqVO) throws ServiceException, IOException {
        if(AppEnum.TYPEFILE.getCode()==singleFileFastSignMoreReqVO.getInType()) {
            log.info("多页签章 文件流");
            String pdfFileType = ckFileNull(signFile, sealFile, certFile);
            if (!(MediaType.APPLICATION_PDF_VALUE.equals(pdfFileType))) {
                singleFileFastSignMoreReqVO.setSignatureFileStr(fileService.uploadCovertPdf(signFile));
            }else{
                singleFileFastSignMoreReqVO.setSignatureFileStr(Base64Util.encode(signFile.getBytes()));
            }
            singleFileFastSignMoreReqVO.setOriginalFileName(signFile.getOriginalFilename());
            if (org.springframework.util.StringUtils.hasText(sealFile.getContentType()) && sealFile.getContentType().toLowerCase().startsWith(IMAGE)) {
                singleFileFastSignMoreReqVO.setSealBase64Str(Base64Util.encode(sealFile.getBytes()));
                singleFileFastSignMoreReqVO.setSealFileName(sealFile.getOriginalFilename());
            }else {
                throw new ServiceException(ErrorCode.SEAL_FILE_TYPE_NAME_ERROR);
            }
            //暂时不做类型校验
            singleFileFastSignMoreReqVO.setCertBase64Str(Base64Util.encode(certFile.getBytes()));
            singleFileFastSignMoreReqVO.setCertFileName(certFile.getOriginalFilename());
        }else{
            log.info("多页签章 base64");
            if(StringUtils.isBlank(singleFileFastSignMoreReqVO.getSignatureFileStr()) ||
                    StringUtils.isBlank(singleFileFastSignMoreReqVO.getSealBase64Str()) ||
                    StringUtils.isBlank(singleFileFastSignMoreReqVO.getCertBase64Str())){
                throw new ServiceException(ErrorCode.REQUEST_40427);
            }
            if(StringUtils.isBlank(singleFileFastSignMoreReqVO.getOriginalFileName())){
                throw new ServiceException(ErrorCode.PDF_FILE_NAME_ERROR);
            }
            if(StringUtils.isBlank(singleFileFastSignMoreReqVO.getCertFileName())){
                throw new ServiceException(ErrorCode.CERT_FILE_NAME_ERROR);
            }
            if(StringUtils.isBlank(singleFileFastSignMoreReqVO.getSealFileName())){
                throw new ServiceException(ErrorCode.SEAL_FILE_NAME_ERROR);
            }
        }
    }

    /**
     * 处理单页签章文件参数
     */
    private void ckFileParam(MultipartFile signFile, MultipartFile sealFile, MultipartFile certFile, SingleFileFastSignReqVO singleFileFastSignReqVO) throws ServiceException, IOException {
        if(AppEnum.TYPEFILE.getCode()==singleFileFastSignReqVO.getInType()) {
            log.info("单页签章 文件流");
            String pdfFileType = ckFileNull(signFile, sealFile, certFile);
            if (!(MediaType.APPLICATION_PDF_VALUE.equals(pdfFileType))) {
                singleFileFastSignReqVO.setSignatureFileStr(fileService.uploadCovertPdf(signFile));
            }else{
                singleFileFastSignReqVO.setSignatureFileStr(Base64Util.encode(signFile.getBytes()));
            }
            singleFileFastSignReqVO.setOriginalFileName(signFile.getOriginalFilename());
            if (org.springframework.util.StringUtils.hasText(sealFile.getContentType()) && sealFile.getContentType().toLowerCase().startsWith(IMAGE)) {
                singleFileFastSignReqVO.setSealBase64Str(Base64Util.encode(sealFile.getBytes()));
                singleFileFastSignReqVO.setSealFileName(sealFile.getOriginalFilename());
            }else {
                throw new ServiceException(ErrorCode.SEAL_FILE_TYPE_NAME_ERROR);
            }
            //暂时不做类型校验
            singleFileFastSignReqVO.setCertBase64Str(Base64Util.encode(certFile.getBytes()));
            singleFileFastSignReqVO.setCertFileName(certFile.getOriginalFilename());
        }else{
            log.info("单页签章 base64");
            if(StringUtils.isBlank(singleFileFastSignReqVO.getSignatureFileStr()) ||
                StringUtils.isBlank(singleFileFastSignReqVO.getSealBase64Str()) ||
                StringUtils.isBlank(singleFileFastSignReqVO.getCertBase64Str())){
                throw new ServiceException(ErrorCode.REQUEST_40427);
            }
            if(StringUtils.isBlank(singleFileFastSignReqVO.getOriginalFileName())){
                throw new ServiceException(ErrorCode.PDF_FILE_NAME_ERROR);
            }
            if(StringUtils.isBlank(singleFileFastSignReqVO.getCertFileName())){
                throw new ServiceException(ErrorCode.CERT_FILE_NAME_ERROR);
            }
            if(StringUtils.isBlank(singleFileFastSignReqVO.getSealFileName())){
                throw new ServiceException(ErrorCode.SEAL_FILE_NAME_ERROR);
            }
        }
    }

    /**
     * 处理签章后文件日志信息
     */
    private LogCommonParam getLogCommonParam(String userId, String fileBase64, String originalFileName) throws IOException {
        LogCommonParam logCommonParam = new LogCommonParam();
        log.info(" start getLogCommonParam: file:{}",fileBase64);
        int pageNum = 0;
        if(StringUtils.isNotBlank(fileBase64)){
            // 文件上传到服务器
            MultipartFile multipartFile = Base64MultipartFile.base64ToMultipart2(fileBase64);
            String fileName = FileUtil.getFielNameNoExt(originalFileName) + ".pdf";
            InMemoryMultipartFile in = new InMemoryMultipartFile(null, fileName, MediaType.APPLICATION_PDF_VALUE,multipartFile.getBytes());
            pageNum = PdfUtil.getPdfNumberOfPages(in.getBytes());
            log.info("getLogCommonParam 文件上传到服务器");
            FileUploadRespDto fileUploadRespDto = fileService.uploadSignInit(in, userId);
            if (null != fileUploadRespDto) {
                FileResourceDto uploadRespDtoFile = fileUploadRespDto.getFile();
                if (null != uploadRespDtoFile) {
                    logCommonParam.setSignFileCode(uploadRespDtoFile.getFileCode());
                    logCommonParam.setSignFileHash(uploadRespDtoFile.getFileHash());
                    logCommonParam.setFileCode(uploadRespDtoFile.getFileCode());
                }
            }
        }
        logCommonParam.setPage(pageNum);
        log.info(" end getLogCommonParam pageNum:",pageNum);
        return logCommonParam;
    }

    private void doOperateData(SingleFileFastSignReqVO singleFileFastSignReqVO, SingleSignReqVO singleSignReqVO) throws IOException {
        // 增加逻辑判断 关键字或者坐标 校验参数后处理数据
        if (SignatureEnum.SIGNATURE_KEYWORD
                .getCode()
                .equals(singleFileFastSignReqVO.getSignatureMethod())) {
            // 处理数据
            ckKeyWord(
                    singleSignReqVO,
                    singleFileFastSignReqVO.getSignatureFileStr(),
                    singleFileFastSignReqVO.getKeyword(),
                    singleFileFastSignReqVO.getKeywordIndex(),
                    singleFileFastSignReqVO.getKeywordOffsetX(),
                    singleFileFastSignReqVO.getKeywordOffsetY());
        }
        if (SignatureEnum.SIGNATURE_COORDINATE
                .getCode()
                .equals(singleFileFastSignReqVO.getSignatureMethod())) {
            // 处理数据
            ckCoordData(
                    singleSignReqVO,
                    singleFileFastSignReqVO.getCoordinateX(),
                    singleFileFastSignReqVO.getCoordinateY(),
                    singleFileFastSignReqVO.getPageNo());
        }
    }

    /**
     * @param accountId, logCommonParam, signatureResult
     * @param signRespDTO
     * @return void
     * @Description 保存签章记录
     * @Author: wdf
     * @CreateDate: 2019/1/23 11:48
     * @UpdateUser: wdf
     * @UpdateDate: 2019/1/23 11:48
     * @Version: 0.0.1
     */
    private void signLog(String accountId, LogCommonParam logCommonParam, SignRespDTO signRespDTO) {
        //保存签章记录
        log.info("signLog start 异步");
        SignatureLogReqVO signatureLogReqVO = new SignatureLogReqVO();
        BeanUtils.copyProperties(logCommonParam,signatureLogReqVO);

        Date date = new Date();
        signatureLogReqVO.setCreateDate(date);
        signatureLogReqVO.setCreateUser(accountId);
        signatureLogReqVO.setUpdateDate(date);
        signatureLogReqVO.setUpdateUser(accountId);
        signatureLogReqVO.setSignatureResult(SignatureEnum.SIGNATURE_SUCCESS.getCode());
        signatureLogReqVO.setSignatureType(SignatureEnum.SIGNATURE_FAST.getCode());
        signatureLogReqVO.setUserId(accountId);
        asyncService.addLogSaveFile(signatureLogReqVO, signRespDTO.getMultiParam());
        log.info("signLog end 异步");
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
    private void ckKeyWord(SingleSignReqVO singleSignReqVO, String pdfbase64Str, String keyword,
                           String keywordIndex, String keywordOffsetX, String keywordOffsetY) throws IOException {
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
        log.info("FileFastSignServiceImpl ckKeyWord");
        //获取关键字坐标
        PdfReader pdfReader = new PdfReader(Base64Util.baseToInputStream(pdfbase64Str));
        List<float[]> keyWordList = GetKeyWordPositionUtil.findKeywordPostions(pdfReader, keyword);
        if (keyWordList.isEmpty()) {
            throw new BusinessException(ErrorCode.REQUEST_40424);
        }
        int keyWordIndex = Integer.parseInt(keywordIndex);
        if (keyWordIndex > keyWordList.size()) {
            throw new BusinessException(ErrorCode.REQUEST_40413);
        }
        float[] objects = keyWordList.get(keyWordIndex - 1);
        float pointx = objects[1];
        float pointy = objects[2];
        singleSignReqVO.setCoordinatex(pointx + Float.parseFloat(keywordOffsetX));
        singleSignReqVO.setCoordinatey(pointy + Float.parseFloat(keywordOffsetY));
        //页码
        singleSignReqVO.setPageNumber((int)objects[0]);
    }

    /**
     * @param singleSignReqDTO, coordinateX, coordinateY, pageNo
     * @return void
     * @Description 坐标签参数校验
     * @Author: wdf
     * @CreateDate: 2019/3/7 17:10
     * @UpdateUser: wdf
     * @UpdateDate: 2019/3/7 17:10
     * @Version: 0.0.1
     */
    private void ckCoordData(SingleSignReqVO singleSignReqDTO, String coordinateX, String coordinateY, String pageNo) {
        if (StringUtils.isBlank(coordinateX)) {
            throw new BusinessException(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), "签章X坐标轴不能为空");
        }
        if (StringUtils.isBlank(coordinateY)) {
            throw new BusinessException(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), "签章Y坐标轴不能为空");
        }
        if (StringUtils.isBlank(pageNo)) {
            throw new BusinessException(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), "签章页数不能为空");
        }
        singleSignReqDTO.setCoordinatex(Float.parseFloat(coordinateX));
        singleSignReqDTO.setCoordinatey(Float.parseFloat(coordinateY));
        //页码
        singleSignReqDTO.setPageNumber(Integer.parseInt(pageNo));
    }

    @Data
    private class LogCommonParam {
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
    }

}