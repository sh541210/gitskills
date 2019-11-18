package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.entity.SignSysDefaultConfig;
import com.iyin.sign.system.mapper.SignSysDefaultConfigMapper;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.IFileService;
import com.iyin.sign.system.service.SignSysDefaultConfigService;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.vo.file.FileUploadRespDto;
import com.iyin.sign.system.vo.req.SignSysDefaultConfigReqVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @ClassName: SignSysDefaultConfigServiceImpl
 * @Description: SignSysDefaultConfigServiceImpl
 * @Author: luwenxiong
 * @CreateDate: 2019/8/5 18:00
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/5 18:00
 * @Version: 0.0.1
 */
@Slf4j
@Service
public class SignSysDefaultConfigServiceImpl extends ServiceImpl<SignSysDefaultConfigMapper, SignSysDefaultConfig> implements SignSysDefaultConfigService {

    @Resource
    private SignSysDefaultConfigMapper signSysDefaultConfigMapper;

    private final IFileService fileService;

    @Autowired
    public SignSysDefaultConfigServiceImpl(IFileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public IyinResult<Boolean> defaultConfig(SignSysDefaultConfigReqVO reqVO) {
        SignSysDefaultConfig signSysDefaultConfig = new SignSysDefaultConfig();
        BeanUtils.copyProperties(reqVO, signSysDefaultConfig);
        signSysDefaultConfig.setId(SnowflakeIdWorker.getIdToStr());
        int count = signSysDefaultConfigMapper.insert(signSysDefaultConfig);
        if (count == 0) {
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1055);
        }
        return IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<String> logoUpload(MultipartFile logo, String userId) {

        int fileSize = 1024 * 1024 * 5;
        if (logo.getSize() > fileSize) {
            throw new BusinessException(5012, "上传文件超过5M，请检查后重试");
        }

        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        FileUploadRespDto respDto = fileService.upload(logo, userId);
        String fileId = respDto.getFile().getFilePath();
        String logoUrl = null;
        try {
            logoUrl = fileService.getFullUrlByFileId(fileId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return IyinResult.getIyinResult(logoUrl);
    }
}
