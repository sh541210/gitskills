package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.entity.SysCertInfo;
import com.iyin.sign.system.mapper.ISysCertInfoMapper;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.ICertInfoService;
import com.iyin.sign.system.service.IFileService;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.vo.file.FileUploadRespDto;
import com.iyin.sign.system.vo.resp.CertInfoRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: CertInfoServiceImpl.java
 * @Description: 证书
 * @Author: yml
 * @CreateDate: 2019/6/26
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/26
 * @Version: 1.0.0
 */
@Service
@Slf4j
public class CertInfoServiceImpl implements ICertInfoService {

    @Autowired private IFileService fileService;

    @Resource
    private ISysCertInfoMapper sysCertInfoMapper;

    /**
     * 异常匹配
     */
    private Pattern compile = Pattern.compile("Cannot add or update a child row");
    /**
     * 名称匹配
     */
    private Pattern pattern = Pattern.compile("O=\\S+");
    /**
     * 截取字符开始位置
     */
    private static final int BEGIN_INDEX = 2;

    /**
     * 导入证书
     *
     * @Author: yml
     * @CreateDate: 2019/6/26
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/26
     * @Version: 0.0.1
     * @param file
     * @param certPassword
     * @param userId
     * @param orgId
     * @return : com.iyin.sign.system.vo.resp.CertInfoRespDTO
     */
    @Override
    public CertInfoRespDTO importFile(MultipartFile file, String certPassword, String userId, String orgId) {
        SysCertInfo sysCertInfo = new SysCertInfo();
        try {
            addCertInfo(file, certPassword, sysCertInfo,userId,orgId);
            CertInfoRespDTO certInfoRespDTO = new CertInfoRespDTO();
            BeanUtils.copyProperties(sysCertInfo,certInfoRespDTO);
            return certInfoRespDTO;
        }
        catch (Exception e) {
            log.error("com.iyin.sign.system.service.impl.CertInfoServiceImpl.importFile异常，{}",e.getLocalizedMessage());
            Matcher matcher = compile.matcher(e.getLocalizedMessage());
            if(matcher.find()){
                throw new BusinessException(ErrorCode.REQUEST_40202.getCode(),"找不到该组织ID");
            }else{
                throw new BusinessException(ErrorCode.REQUEST_40202.getCode(),e.getLocalizedMessage().replaceAll("keystore password was incorrect","证书密码错误，请重试"));
            }
        }
    }

    /**
     * 根据组织ID查找证书
     *
     * @param orgId
     * @param pageNum
     * @param pageSize
     * @return : com.iyin.sign.system.service.ICertInfoService
     * @Author: yml
     * @CreateDate: 2019/6/27
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/27
     * @Version: 0.0.1
     */
    @Override
    public IyinPage<CertInfoRespDTO> listByOrgId(String orgId, Integer pageNum, Integer pageSize) {
        IyinPage<CertInfoRespDTO> sysCertInfoIyinPage = new IyinPage<>(pageNum,pageSize);
        QueryWrapper<SysCertInfo> queryWrapper = new QueryWrapper<SysCertInfo>().eq("is_deleted",0);
        queryWrapper.eq("org_id",orgId);
        IPage<SysCertInfo> sysCertInfos = sysCertInfoMapper.selectPage(new IyinPage<>(pageNum,pageSize), queryWrapper);
        BeanUtils.copyProperties(sysCertInfos,sysCertInfoIyinPage);
        List<CertInfoRespDTO> certinforespdtos = Lists.newArrayList();
        for (SysCertInfo sysCertInfo : sysCertInfos.getRecords()) {
            CertInfoRespDTO certInfoRespDTO = new CertInfoRespDTO();
            BeanUtils.copyProperties(sysCertInfo,certInfoRespDTO);
            certinforespdtos.add(certInfoRespDTO);
        }
        sysCertInfoIyinPage.setRecords(certinforespdtos);
        return sysCertInfoIyinPage;
    }

    /**
     * 删除证书
     *
     * @param certId
     * @return : boolean
     * @Author: yml
     * @CreateDate: 2019/6/27
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/27
     * @Version: 0.0.1
     */
    @Override
    public boolean deleteById(String certId) {
        QueryWrapper<SysCertInfo> queryWrapper = new QueryWrapper<SysCertInfo>().eq("cert_id", certId);
        SysCertInfo sysCertInfo = sysCertInfoMapper.selectOne(queryWrapper);
        sysCertInfo.setIsDeleted((byte) 1);
        return sysCertInfoMapper.update(sysCertInfo,queryWrapper) != 0;
    }

    private void addCertInfo(MultipartFile file, String certPassword, SysCertInfo sysCertInfo, String userId, String orgId) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        InputStream fis = file.getInputStream();
        // If the keystore password is empty(""), then we have to set
        // to null, otherwise it won't work!!!
        char[] nPassword;
        if ((certPassword == null) || "".equals(certPassword.trim())) {
            nPassword = null;
        } else {
            nPassword = certPassword.toCharArray();
        }
        ks.load(fis, nPassword);
        fis.close();
        // Now we loop all the aliases, we need the alias to get keys.
        // It seems that this value is the "Friendly name" field in the
        // detals tab <-- Certificate window <-- view <-- Certificate
        // Button <-- Content tab <-- Internet Options <-- Tools menu
        // In MS IE 6.
        Enumeration enumas = ks.aliases();
        String keyAlias = null;
        // we are readin just one certificate.
        if (enumas.hasMoreElements()) {
            keyAlias = (String) enumas.nextElement();
            log.info("alias=[" + keyAlias + "]");
        }
        Certificate cert = ks.getCertificate(keyAlias);
        X509Certificate x509Certificate = (X509Certificate) cert;
        sysCertInfo.setCertName(file.getOriginalFilename());
        sysCertInfo.setCertPassword(certPassword);
        sysCertInfo.setCreateUser(userId);
        sysCertInfo.setUpdateUser(userId);
        sysCertInfo.setIssuer(getName(x509Certificate.getIssuerDN().getName()));
        sysCertInfo.setSubject(getName(x509Certificate.getSubjectDN().getName()));
        sysCertInfo.setSerialNumber(x509Certificate.getSerialNumber().toString());
        sysCertInfo.setOrgId(orgId);
        Date notBefore = x509Certificate.getNotBefore();
        Date notAfter = x509Certificate.getNotAfter();
        Date now = new Date();
        if(now.before(notBefore) || now.after(notAfter)){
            throw new BusinessException(ErrorCode.SERVER_50302);
        }
        sysCertInfo.setValidityFrom(notBefore);
        sysCertInfo.setValidityExp(notAfter);
        FileUploadRespDto fileUploadRespDto = fileService.upload(file, userId);
        sysCertInfo.setCertCode(fileUploadRespDto.getFile().getFileCode());
        sysCertInfo.setCertId(SnowflakeIdWorker.getIdToStr());
        sysCertInfoMapper.insert(sysCertInfo);
    }

    private String getName(String name) {
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()){
            String group = matcher.group();
            return group.substring(BEGIN_INDEX,group.length()-1);
        }
        return "Certificate does not have this information";
    }


}
