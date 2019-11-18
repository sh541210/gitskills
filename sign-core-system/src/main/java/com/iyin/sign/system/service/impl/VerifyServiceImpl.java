package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.iyin.sign.system.common.enums.FileManageEnum;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.dto.req.CerInfoRespDTO;
import com.iyin.sign.system.dto.req.SignInfoRespDTO;
import com.iyin.sign.system.dto.req.VerifySignRespDTO;
import com.iyin.sign.system.entity.SignSysCompactFile;
import com.iyin.sign.system.entity.SignSysCompactInfo;
import com.iyin.sign.system.entity.SignSysCompactSignatory;
import com.iyin.sign.system.mapper.ISignSysCompactFileMapper;
import com.iyin.sign.system.mapper.ISignSysCompactInfoMapper;
import com.iyin.sign.system.mapper.ISignSysCompactSignatoryMapper;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.IFileService;
import com.iyin.sign.system.service.VerifyService;
import com.iyin.sign.system.util.DateFormatType;
import com.iyin.sign.system.util.DateUtil;
import com.iyin.sign.system.util.SignFileUtil;
import com.iyin.sign.system.vo.req.VerifySignFileReqDTO;
import com.iyin.sign.system.vo.resp.SignFileSignatureRespDTO;
import com.iyin.sign.system.vo.resp.VerifyByCodeRespDTO;
import com.iyin.sign.system.vo.resp.VerifySignFileListRespDTO;
import com.iyin.sign.system.vo.resp.VerifySignFileRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: VerifyServiceImpl
 * @Description: 验真服务实现类
 * @Author: 唐德繁
 * @CreateDate: 2018/12/13 0013 下午 3:11
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/12/13 0013 下午 3:11
 * @Version: 0.0.1
 */
@Service
@Slf4j
public class VerifyServiceImpl implements VerifyService {

    private static final String SEAL_STAMP = "IYIN_STAMP";

    private static final String SEAL_SYMBOL = "@";

    private static final String CN = "CN";

    private final IFileService fileService;

    @Resource
    private ISignSysCompactFileMapper signSysCompactFileMapper;
    @Resource
    private ISignSysCompactInfoMapper signSysCompactInfoMapper;
    @Resource
    private ISignSysCompactSignatoryMapper signSysCompactSignatoryMapper;

    @Autowired
    public VerifyServiceImpl(IFileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 验真加密文件
     *
     * @param verifySignFileReqDTO
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2018/12/13 下午 3:30
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/12/13 下午 3:30
     * @Version: 0.0.1
     */
    @Override
    public IyinResult<VerifySignFileRespDTO> setVerifySignFile(VerifySignFileReqDTO verifySignFileReqDTO) {
        List<SignFileSignatureRespDTO> signFileSignatureList = Collections.emptyList();
        InputStream signFileStream = baseToInputStream(verifySignFileReqDTO.getSignFileStr());
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        try {
            PdfReader reader = new PdfReader(signFileStream);
            AcroFields fields = reader.getAcroFields();
            ArrayList<String> names = fields.getSignatureNames();
            if (!names.isEmpty()) {
                signFileSignatureList = getSignInfo(signFileStream, fields, names);
            }
            reader.close();
        } catch (Exception e) {
            log.error("读取加签文件异常：{}", e.getMessage());
        }
        //响应请求结果
        VerifySignFileRespDTO verifySignFileRespDTO = new VerifySignFileRespDTO();
        verifySignFileRespDTO.setSignFileSignatureList(signFileSignatureList);
        IyinResult<VerifySignFileRespDTO> result = IyinResult.success();
        result.setData(verifySignFileRespDTO);
        return result;
    }

    @Override
    public IyinResult<VerifySignFileListRespDTO> setVerifySignFile(String fileCode) {
        InMemoryMultipartFile in=fileService.fetch(fileCode);
        List<VerifySignRespDTO> signFileSignatureList = Collections.emptyList();
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        try {
            ByteArrayInputStream stream=new ByteArrayInputStream(in.getBytes());
            PdfReader reader = new PdfReader(stream);
            AcroFields fields = reader.getAcroFields();
            ArrayList<String> names = fields.getSignatureNames();
            log.info("names.size：{}",names.size());
            if (!names.isEmpty()) {
                signFileSignatureList = getSignInfoFile(stream, fields, names);
            }
            reader.close();
        } catch (Exception e) {
            log.error("fileCode 读取加签文件异常：{}", e.getMessage());
        }
        //响应请求结果
        VerifySignFileListRespDTO verifySignFileListRespDTO = new VerifySignFileListRespDTO();
        verifySignFileListRespDTO.setSignFileSignatureList(signFileSignatureList);
        IyinResult<VerifySignFileListRespDTO> result = IyinResult.success();
        result.setData(verifySignFileListRespDTO);
        return result;
    }

    @Override
    public IyinResult<VerifySignFileListRespDTO> setVerifySignFile(MultipartFile file) {
        List<VerifySignRespDTO> signFileSignatureList = Collections.emptyList();
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        try {
            ByteArrayInputStream stream=new ByteArrayInputStream(file.getBytes());
            PdfReader reader = new PdfReader(stream);
            AcroFields fields = reader.getAcroFields();
            ArrayList<String> names = fields.getSignatureNames();
            if (!names.isEmpty()) {
                signFileSignatureList = getSignInfoFile(stream, fields, names);
            }
            reader.close();
        } catch (Exception e) {
            log.error("setVerifySignFile 读取加签文件异常：{}", e.getMessage());
        }
        //响应请求结果
        VerifySignFileListRespDTO verifySignFileListRespDTO = new VerifySignFileListRespDTO();
        verifySignFileListRespDTO.setSignFileSignatureList(signFileSignatureList);
        IyinResult<VerifySignFileListRespDTO> result = IyinResult.success();
        result.setData(verifySignFileListRespDTO);
        return result;
    }

    /**
     * 二维码验证签章
     *
     * @Author: yml
     * @CreateDate: 2019/9/3
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/3
     * @Version: 0.0.1
     * @param contractId
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.resp.VerifySignFileListRespDTO>
     */
    @Override
    public IyinResult<VerifySignFileListRespDTO> verifySignByQrCode(String contractId) {
        List<SignSysCompactFile> signSysCompactFiles = signSysCompactFileMapper
                .selectList(Wrappers.<SignSysCompactFile>query().lambda()
                        .eq(SignSysCompactFile::getCompactId, contractId)
                        .eq(SignSysCompactFile::getFileType, FileManageEnum.CONTRACT_FILE.getCode()));
        List<VerifySignRespDTO> signFileSignatureList = Lists.newLinkedList();
        for (SignSysCompactFile signSysCompactFile : signSysCompactFiles) {
            String fileCode = signSysCompactFile.getFileCode();
            InMemoryMultipartFile in=fileService.fetch(fileCode);
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            List<VerifySignRespDTO> signInfoFile = Lists.newLinkedList();
            try (ByteArrayInputStream stream=new ByteArrayInputStream(in.getBytes())){
                PdfReader reader = new PdfReader(stream);
                AcroFields fields = reader.getAcroFields();
                ArrayList<String> names = fields.getSignatureNames();
                log.info("names.size：{}",names.size());
                if (!names.isEmpty()) {
                    signInfoFile = getSignInfoFile(stream, fields, names);
                }
                reader.close();
            } catch (Exception e) {
                log.error("fileCode 读取加签文件异常：{}", e.getLocalizedMessage());
            }
            signFileSignatureList.addAll(signInfoFile);
        }
        VerifySignFileListRespDTO verifySignFileListRespDTO = new VerifySignFileListRespDTO();
        verifySignFileListRespDTO.setSignFileSignatureList(signFileSignatureList);
        return IyinResult.success(verifySignFileListRespDTO);
    }

    /**
     * 验证码验证签章
     *
     * @Author: yml
     * @CreateDate: 2019/9/10
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/10
     * @Version: 0.0.1
     * @param code
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.resp.VerifyByCodeRespDTO>
     */
    @Override
    public IyinResult<VerifyByCodeRespDTO> verifySignByCode(String code) {
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectOne(
                Wrappers.<SignSysCompactInfo>lambdaQuery().eq(SignSysCompactInfo::getVerificationCode, code));
        if (null != signSysCompactInfo) {
            boolean b = null == signSysCompactInfo.getVerificationDate() || signSysCompactInfo.getVerificationDate()
                    .compareTo(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) >= 0;
            if (b) {
                VerifyByCodeRespDTO verifyByCodeRespDTO = new VerifyByCodeRespDTO();
                verifyByCodeRespDTO.setCreateDate(signSysCompactInfo.getGmtCreate());
                verifyByCodeRespDTO.setTheme(signSysCompactInfo.getCompactTheme());
                verifyByCodeRespDTO.setId(signSysCompactInfo.getId());
                SignSysCompactSignatory signSysCompactSignatory = signSysCompactSignatoryMapper.selectOne(
                        Wrappers.<SignSysCompactSignatory>lambdaQuery().isNotNull(SignSysCompactSignatory::getSignDate)
                                .eq(SignSysCompactSignatory::getCompactId, signSysCompactInfo.getId())
                                .orderByDesc(SignSysCompactSignatory::getSignDate).last("limit 1"));
                verifyByCodeRespDTO.setSignDate(null == signSysCompactSignatory ? null : signSysCompactSignatory.getSignDate());
                return IyinResult.success(verifyByCodeRespDTO);
            }
        }
        throw new BusinessException(ErrorCode.DATA_NOT_FOUND);
    }

    /**
     * 获取签章信息
     *
     * @param signFileStream
     * @param fields
     * @param names
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2018/12/13 下午 5:02
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/12/13 下午 5:02
     * @Version: 0.0.1
     */
    private List<SignFileSignatureRespDTO> getSignInfo(InputStream signFileStream, AcroFields fields, ArrayList<String> names) {
        List<SignFileSignatureRespDTO> signFileSignatureList = new ArrayList<>();
        int size = names.size();
        try {
            if (size == 1) {
                SignFileSignatureRespDTO signFileSignatureRespDTO = singleSealVerify(fields, names.get(0));
                signFileSignatureList.add(signFileSignatureRespDTO);
            } else {
                signFileSignatureList = multiSealVerity(signFileStream, fields, names);
            }
        } catch (Exception e) {
            log.error("验真加签文件获取加签信息异常：{}", e.getMessage());
        }
        return signFileSignatureList;
    }

    /**
     * 获取签章信息
     *
     * @param signFileStream
     * @param fields
     * @param names
     * @return
     * @Author: wdf
     * @CreateDate: 2019/07/24 下午 2:27
     * @UpdateUser: wdf
     * @UpdateDate: 2019/07/24 下午 2:27
     * @Version: 0.0.1
     */
    private List<VerifySignRespDTO> getSignInfoFile(InputStream signFileStream, AcroFields fields, ArrayList<String> names) {
        List<VerifySignRespDTO> signFileSignatureList = new ArrayList<>();
        int size = names.size();
        try {
            if (size == 1) {
                VerifySignRespDTO signFileSignatureRespDTO = singleSealVerifyFile(fields, names.get(0));
                signFileSignatureList.add(signFileSignatureRespDTO);
            } else {
                signFileSignatureList = multiSealVerityFile(signFileStream, fields, names);
            }
        } catch (Exception e) {
            log.error("getSignInfoFile 验真加签文件获取加签信息异常：{}", e.getMessage());
        }
        return signFileSignatureList;
    }

    /**
     * 多印章校验
     *
     * @param signFileStream
     * @param fields
     * @param names
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2018/12/17 下午 6:58
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/12/17 下午 6:58
     * @Version: 0.0.1
     */
    private List<SignFileSignatureRespDTO> multiSealVerity(InputStream signFileStream, AcroFields fields, ArrayList<String> names) {
        List<SignFileSignatureRespDTO> signFileSignatureList = new ArrayList<>();
        try {
            int size = names.size();
            for (int i = 0; i < size; i++) {
                SignFileSignatureRespDTO signFileSignatureRespDTO = new SignFileSignatureRespDTO();
                PdfPKCS7 pkcs7 = fields.verifySignature(names.get(i));
                boolean isSign = pkcs7.verify();
                boolean isCovers = fields.signatureCoversWholeDocument(names.get(i));
                //印章之间是否存在被篡改
                int count = SignFileUtil.countKeywords(signFileStream, size);
                int j = size - 1;
                if (i != j && count != j) {
                    isCovers = true;
                }
                boolean flag = false;
                if (isCovers && isSign) {
                    flag = true;
                }
                signFileSignatureRespDTO.setIsSignatureCertificate(isSign);
                String signer = CertificateInfo.getSubjectFields(pkcs7.getSigningCertificate()).getField(CN);
                signFileSignatureRespDTO.setSigner(signer);
                signFileSignatureRespDTO.setSignDate(pkcs7.getSignDate().getTime());
                String certificateAuthority = CertificateInfo.getIssuerFields(pkcs7.getSigningCertificate()).getField("O");
                signFileSignatureRespDTO.setCertificateAuthority(certificateAuthority);
                boolean isTimestampCertificate = pkcs7.verifyTimestampImprint();
                signFileSignatureRespDTO.setIsTimestampCertificate(isTimestampCertificate);
                if (isTimestampCertificate) {
                    signFileSignatureRespDTO.setTimestamp(pkcs7.getTimeStampDate().getTime());
                }
                PdfString pdfString = fields.getSignatureDictionary(names.get(i)).getAsString(new PdfName(SEAL_STAMP));
                if (pdfString != null) {
                    String sealCode = pdfString.toString().split(SEAL_SYMBOL)[0];
                    signFileSignatureRespDTO.setSealCode(sealCode);
                }
                signFileSignatureList.add(signFileSignatureRespDTO);
            }
        } catch (Exception e) {
            log.error("校验多签章异常：{}", e.getMessage());
        }
        return signFileSignatureList;
    }

    /**
     * 多印章校验
     *
     * @param signFileStream
     * @param fields
     * @param names
     * @return
     * @Author: wdf
     * @CreateDate: 2019/07/24 下午 2:27
     * @UpdateUser: wdf
     * @UpdateDate: 2019/07/24 下午 2:27
     * @Version: 0.0.1
     */
    private List<VerifySignRespDTO> multiSealVerityFile(InputStream signFileStream, AcroFields fields, ArrayList<String> names) {
        List<VerifySignRespDTO> signFileSignatureList = new ArrayList<>();
        try {
            int size = names.size();
            for (int i = 0; i < size; i++) {
                VerifySignRespDTO signFileSignatureRespDTO = new VerifySignRespDTO();
                PdfPKCS7 pkcs7 = fields.verifySignature(names.get(i));
                boolean isSign = pkcs7.verify();
                boolean isCovers = fields.signatureCoversWholeDocument(names.get(i));
                boolean isTimestampCertificate = pkcs7.verifyTimestampImprint();
                //印章之间是否存在被篡改
                int count = SignFileUtil.countKeywords(signFileStream, size);
                int j = size - 1;
                if (i != j && count != j) {
                    isCovers = true;
                }
                String effectiveDes="签名无效";
                String updateDes="";
                if (isSign && isCovers ) {
                    effectiveDes="签名有效";
                    updateDes="未被篡改";
                }

                //签名信息
                SignInfoRespDTO signInfoRespDTO = new SignInfoRespDTO();
                signInfoRespDTO.setEffectiveDes(effectiveDes);
                signInfoRespDTO.setUpdateDes(updateDes);
                if(isTimestampCertificate){
                    signInfoRespDTO.setTimestamp(pkcs7.getTimeStampDate().getTime());
                    signInfoRespDTO.setTimesVerify("通过");
                }else{
                    signInfoRespDTO.setTimesVerify("不通过");
                }
                signInfoRespDTO.setSignTime(pkcs7.getSignDate().getTime());
                signFileSignatureRespDTO.setSignInfoRespDTO(signInfoRespDTO);

                //证书信息
                CerInfoRespDTO cerInfoRespDTO = new CerInfoRespDTO();
                X509Certificate x509Certificate = pkcs7.getSigningCertificate();
                //证书拥有者
                String signer = CertificateInfo.getSubjectFields(x509Certificate).getField(CN);
                cerInfoRespDTO.setOwnwer(signer);
                //证书发行者
                String certificateAuthority = CertificateInfo.getIssuerFields(x509Certificate).getField(CN);
                cerInfoRespDTO.setCertificateAuthority(certificateAuthority);
                //签名算法
                String signAlg = x509Certificate.getSigAlgName();
                cerInfoRespDTO.setSignAlgorithm(signAlg);

                //证书有效期
                Date startTime =  x509Certificate.getNotBefore();
                String startTimeStr =  DateUtil.formatDate(startTime,DateFormatType.DATETIME);
                Date endTime = x509Certificate.getNotAfter();
                String endTimeStr =  DateUtil.formatDate(endTime,DateFormatType.DATETIME);
                cerInfoRespDTO.setPeriodOfValidity(startTimeStr+" ~ "+endTimeStr);
                signFileSignatureRespDTO.setCerInfoRespDTO(cerInfoRespDTO);

                signFileSignatureList.add(signFileSignatureRespDTO);
            }
        } catch (Exception e) {
            log.error("校验多签章异常：{}", e.getMessage());
        }
        return signFileSignatureList;
    }

    /**
     * 单个印章信息校验
     *
     * @param fields
     * @param str
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2018/12/14 下午 8:05
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/12/14 下午 8:05
     * @Version: 0.0.1
     */
    private SignFileSignatureRespDTO singleSealVerify(AcroFields fields, String str) {
        SignFileSignatureRespDTO signFileSignatureRespDTO = new SignFileSignatureRespDTO();
        try {
            PdfPKCS7 pkcs7 = fields.verifySignature(str);
            boolean isSignatureCertificate = pkcs7.verify();
            boolean isCovers = fields.signatureCoversWholeDocument(str);
            boolean flag = false;
            if (isSignatureCertificate && isCovers) {
                flag = true;
            }
            signFileSignatureRespDTO.setIsSignatureCertificate(flag);
            String signer = CertificateInfo.getSubjectFields(pkcs7.getSigningCertificate()).getField(CN);
            signFileSignatureRespDTO.setSigner(signer);
            String certificateAuthority = CertificateInfo.getIssuerFields(pkcs7.getSigningCertificate()).getField("O");
            signFileSignatureRespDTO.setCertificateAuthority(certificateAuthority);
            signFileSignatureRespDTO.setSignDate(pkcs7.getSignDate().getTime());
            boolean isTimestampCertificate = pkcs7.verifyTimestampImprint();
            signFileSignatureRespDTO.setIsTimestampCertificate(isTimestampCertificate);
            if (isTimestampCertificate) {
                signFileSignatureRespDTO.setTimestamp(pkcs7.getTimeStampDate().getTime());
            }
            PdfString pdfString = fields.getSignatureDictionary(str).getAsString(new PdfName(SEAL_STAMP));
            if (pdfString != null) {
                String sealCode = pdfString.toString().split(SEAL_SYMBOL)[0];
                signFileSignatureRespDTO.setSealCode(sealCode);
            }
        } catch (Exception e) {
            log.error("验真加签文件获取加签信息异常：{}", e.getMessage());
        }
        return signFileSignatureRespDTO;
    }

    /**
     * 单个印章信息校验
     *
     * @param fields
     * @param str
     * @return
     * @Author: wdf
     * @CreateDate: 2019/07/24 下午 2:27
     * @UpdateUser: wdf
     * @UpdateDate: 2019/07/24 下午 2:27
     * @Version: 0.0.1
     */
    private VerifySignRespDTO singleSealVerifyFile(AcroFields fields, String str) {
        VerifySignRespDTO verifySignRespDTO = new VerifySignRespDTO();
        try {
            PdfPKCS7 pkcs7 = fields.verifySignature(str);
            boolean isSignatureCertificate = pkcs7.verify();
            boolean isCovers = fields.signatureCoversWholeDocument(str);
            String effectiveDes="签名无效";
            String updateDes="";
            if (isSignatureCertificate && isCovers) {
                effectiveDes="签名有效";
                updateDes="未被篡改";
            }

            //签名信息
            SignInfoRespDTO signInfoRespDTO = new SignInfoRespDTO();
            signInfoRespDTO.setEffectiveDes(effectiveDes);
            signInfoRespDTO.setUpdateDes(updateDes);

            boolean isTimestampCertificate = pkcs7.verifyTimestampImprint();
            if(isTimestampCertificate){
                signInfoRespDTO.setTimestamp(pkcs7.getTimeStampDate().getTime());
                signInfoRespDTO.setTimesVerify("通过");
            }else{
                signInfoRespDTO.setTimesVerify("不通过");
            }
            signInfoRespDTO.setSignTime(pkcs7.getSignDate().getTime());
            verifySignRespDTO.setSignInfoRespDTO(signInfoRespDTO);

            //证书信息
            CerInfoRespDTO cerInfoRespDTO = new CerInfoRespDTO();
            X509Certificate x509Certificate = pkcs7.getSigningCertificate();
            //证书拥有者
            String signer = CertificateInfo.getSubjectFields(x509Certificate).getField(CN);
            cerInfoRespDTO.setOwnwer(signer);
            //证书发行者
            String certificateAuthority = CertificateInfo.getIssuerFields(x509Certificate).getField(CN);
            cerInfoRespDTO.setCertificateAuthority(certificateAuthority);
            //签名算法
            String signAlg = x509Certificate.getSigAlgName();
            cerInfoRespDTO.setSignAlgorithm(signAlg);
            //证书有效期
            Date startTime =  x509Certificate.getNotBefore();
            String startTimeStr =  DateUtil.formatDate(startTime, DateFormatType.DATETIME);
            Date endTime = x509Certificate.getNotAfter();
            String endTimeStr =  DateUtil.formatDate(endTime,DateFormatType.DATETIME);
            cerInfoRespDTO.setPeriodOfValidity(startTimeStr+" ~ "+endTimeStr);
            verifySignRespDTO.setCerInfoRespDTO(cerInfoRespDTO);
        } catch (Exception e) {
            log.error("验真加签文件获取加签信息异常：{}", e.getMessage());
        }
        return verifySignRespDTO;
    }

    /**
     * Base64字符串转InputStream流对象
     *
     * @param base64string
     * @return
     * @Author: 唐德繁
     * @CreateDate: 2018/12/13 下午 5:01
     * @UpdateUser: 唐德繁
     * @UpdateDate: 2018/12/13 下午 5:01
     * @Version: 0.0.1
     */
    private InputStream baseToInputStream(String base64string) {
        ByteArrayInputStream stream = null;
        try {
            byte[] bytes = Base64.decodeBase64(base64string);
            stream = new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return stream;
    }
}
