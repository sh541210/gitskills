package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.common.enums.CertificateEnum;
import com.iyin.sign.system.common.enums.PictureFormatEnum;
import com.iyin.sign.system.common.utils.Base64MultipartFile;
import com.iyin.sign.system.dto.req.TwoPressureCodeReqDTO;
import com.iyin.sign.system.entity.SignSysSealInfo;
import com.iyin.sign.system.entity.SignSysSealPictureDataTmp;
import com.iyin.sign.system.entity.SignSysSealUser;
import com.iyin.sign.system.entity.SignSysUkCertInfo;
import com.iyin.sign.system.mapper.SignSysSealInfoMapper;
import com.iyin.sign.system.mapper.SignSysSealUserMapper;
import com.iyin.sign.system.mapper.SignSysUkCertInfoMapper;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.IFileService;
import com.iyin.sign.system.service.SignSysSealInfoService;
import com.iyin.sign.system.service.SignSysSealPictureDataTmpService;
import com.iyin.sign.system.util.*;
import com.iyin.sign.system.util.picUtils.GenerateSealPicGraphics2DUtil;
import com.iyin.sign.system.vo.DefineSeal.CircularSealPicDTO;
import com.iyin.sign.system.vo.file.FileUploadRespDto;
import com.iyin.sign.system.vo.req.*;
import com.iyin.sign.system.vo.resp.SealDefinedUploadRespVO;
import com.iyin.sign.system.vo.resp.SealInfoRespVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @ClassName: SignSysSealInfoServiceImpl
 * @Description: 印章信息service
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 11:21
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 11:21
 * @Version: 0.0.1
 */
@Service
@Slf4j
public class SignSysSealInfoServiceImpl extends ServiceImpl<SignSysSealInfoMapper,SignSysSealInfo> implements SignSysSealInfoService {


    @Value("${eseal-picture.app-new-path}")
    String newExecFullPathName;

    @Value("${eseal-picture.temp-file-path}")
    String filePath;

    String pictureHeightKey = "pictureHeight";
    String pictureWidthKey = "pictureWidth";

    @Autowired
    private IFileService fileService;

    @Autowired
    private SignSysSealPictureDataTmpService signSysSealPictureDataTmpService;

    @Autowired
    private SignSysSealInfoMapper signSysSealInfoMapper;

    @Autowired
    private SignSysSealUserMapper signSysSealUserMapper;

    @Autowired
    private SignSysUkCertInfoMapper signSysUkCertInfoMapper;

    @Autowired
    JWTUtils jwtUtils;

    @Override
    public IyinResult<SealDefinedUploadRespVO> uploadSealDefinedFile(SealPictureUploadReqVO reqVO) {
        MultipartFile sealFile = reqVO.getFile();
        String mixCode = reqVO.getMixCode();
        String pictureType = reqVO.getPictureType();
        if(StringUtils.isEmpty(pictureType)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1058);
        }
        // 混淆码必须13位以上长度，否则生成二压码报错
        if (StringUtils.isEmpty(mixCode) || mixCode.length() < 13) {
            mixCode = System.currentTimeMillis() + "";
        }
        // 文件大小限制b
        int fileSize = 1024 * 1024 * 5;
        if (sealFile.getSize() > fileSize) {
            throw new BusinessException(5012,"上传文件超过5M，请检查后重试");
        }

        // 获得原始文件名(IE上传会带客户端文件所在全路径，需要截取)
        String sealImageName = sealFile.getOriginalFilename();
        // 文件名斜杠位置
        int imageSlashIndex = sealImageName.lastIndexOf('\\');
        int startIndex;
        // 不存在的标识
        int nonentityCode = -1;
        // 下标移动值
        int indexMoveValue = 1;
        if (imageSlashIndex != nonentityCode) {
            // 截取文件名
            startIndex = imageSlashIndex + indexMoveValue;
            //带后缀的文件名
            sealImageName = sealImageName.substring(startIndex, sealImageName.length());
        }
        // 章摸图片文件名点号的位置
        int imagePointIndex = sealImageName.lastIndexOf('.');
        // 截取文件名(不带后缀)
        startIndex = 0;
        //没后缀的文件名
        String sourceFileName = sealImageName.substring(startIndex, imagePointIndex);
        // 截取文件后缀名
        startIndex = imagePointIndex + indexMoveValue;
        String imageSuffixName = sealImageName.substring(startIndex, sealImageName.length());
        String upperCase = imageSuffixName.toUpperCase();
        // 只支持png,jpg,bmp格式图片
        if (!PictureFormatEnum.BMP.getName().equals(upperCase) && !PictureFormatEnum.JPG.getName().equals(upperCase) && !PictureFormatEnum.PNG.getName().equals(upperCase)) {
            throw new BusinessException(5013,"上传文件格式不支持，请检查后重试");
        }

        // 1.保存本地文件
        // 设置本地文件保存位置
        String basePath = filePath;
        // （注意：dirPath和File对象在后面的逻辑中，会多次改变指向的文件，File在改变指向之前，
        // 会删除当前指向的文件，以保证整个处理流程结束之后，不会留下临时文件）
        // dirPath当前指向上传后的文件全路径(basePath + 原始文件名带后缀)
        String dirPath = basePath + sealImageName;
        File imageLocalFile = new File(dirPath);
        if (!imageLocalFile.getParentFile().exists()) {
            imageLocalFile.getParentFile().mkdirs();
        }
        try {
            // 保存文件到本地
            sealFile.transferTo(imageLocalFile);
        } catch (IOException e) {
            deleteFileIfExists(imageLocalFile);
            log.error("原始图片保存失败：{}", e);
            throw new BusinessException(5039,"临时文件保存失败");
        }

        /**
         * 默认为章莫
         */
        // tip:由于走小程序的手绘签名对Android支持不完善导致上传的jpg图片背景为黑色
        // 现统一要求上传背景透明的png图片，由后端做转换
        // 只对手绘签名做转换
        if ("02".equals(pictureType)) {
            String randomFileName = sourceFileName + "_" + System.currentTimeMillis() + "." + PictureFormatEnum.BMP.getName().toLowerCase();
            String fileFullPath = basePath + randomFileName;
            // 转换成bmp
            try {
                ImageFormatChangeUtil.changeToBmp(dirPath, fileFullPath);
                // 删除原始透明背景png图片
                deleteFileIfExists(imageLocalFile);
                // 指向bmp图片
                dirPath = fileFullPath;
                imageLocalFile = new File(dirPath);
            } catch (IOException e) {
                log.error("图片格式转换bmp失败");
                deleteFileIfExists(imageLocalFile);
                throw new BusinessException(5014,"章模图片格式转换失败");
            }
        }

        /**
         * 只有是正规章模才走c++，普通签名走c++会把签名给截断
         */
//        if("01".equals(pictureType)){
            // 2.C++处理
            //指向可执行文件的全路径
            String execTargetAppName = newExecFullPathName;
            // 新文件名前缀
            String newNamePrefix = "eseal_pic_";
            // 防重名字段
            String timeMark = System.currentTimeMillis() + "_";
            // 不带后缀新文件名
            String newFileOnlyName = newNamePrefix + timeMark + sourceFileName;
            // 目标图片名称,新版C++处理默认返回png
            String targetSealImageNamePNG = newFileOnlyName + "." + PictureFormatEnum.BMP.getName().toLowerCase();
            // 摘取章模后生成的目标图片全路径
            String fullTargetNamePNG = basePath + targetSealImageNamePNG;

            float phyWidth = 0.0f;
            float phyHeight = 0.0f;
//            if (reqVO.getWidth() != null) {
//                phyWidth = reqVO.getWidth();
//            }
//            if (reqVO.getHeight() != null) {
//                phyHeight = reqVO.getHeight();
//            }

            InvokeCMethod.dealPicVersion2(execTargetAppName, dirPath, fullTargetNamePNG, phyWidth, phyHeight, "02".equals(pictureType)?1:0);
            // 删除C++处理前的图片
            deleteFileIfExists(imageLocalFile);
            // 当前指向文件为C++处理后的png图片
            dirPath = fullTargetNamePNG;
            imageLocalFile = new File(dirPath);
            if (!imageLocalFile.exists()) {
                log.error("C++程序无法处理该图片");
                throw new BusinessException(5016,"章模摘取失败");
            }
//        }

        // 生成二压码
        String eyBase64 = fileToEYBase64(imageLocalFile, mixCode);
        if (StringUtils.isEmpty(eyBase64)) {
            deleteFileIfExists(imageLocalFile);
            throw new BusinessException(5019,"章模摘取失败");
        }
        // 生成base64位
        String base64 = null;
        try {
            base64 = Base64Util.encodeFile(dirPath);
        } catch (Exception e) {
            log.info("操作：图片转BASE64异常：{}", e);
        }
        if (StringUtils.isEmpty(base64)) {
            deleteFileIfExists(imageLocalFile);
            throw new BusinessException(5024,"章模转换BASE64失败");
        }

        TwoPressureCodeReqDTO reqDTO = new TwoPressureCodeReqDTO();
        reqDTO.setEyCode(eyBase64);
        reqDTO.setBase64(base64);
        // 上传章模图片途径 保存在 bdcesc_eseal_picture.picture_flag
        reqDTO.setPicType(pictureType);
        // 调底层服务入库
        String tmpId = signSysSealPictureDataTmpService.savePictureDataPart(reqDTO);

        // 3.摘取章模后生成的目标图片计算宽高
        SealDefinedUploadRespVO respVO = new SealDefinedUploadRespVO();
        respVO.setPictureDataTmpId(tmpId);
        Map<String, Integer> map = getPictureWidthAndHeight(dirPath, imageLocalFile);
        respVO.setPictureHeight(map.get(pictureHeightKey));
        respVO.setPictureWidth(map.get(pictureWidthKey));
        // 4.摘取章模后生成的目标图片加水印转化成jpg(方便前端压缩传输)，上传FastDfs
        try {
            //随机文件名前缀
            String prefixion = sourceFileName + "_" + System.currentTimeMillis() + ".";
            // 原始文件名取新的文件名(.jpg)
            String jpgName = prefixion + PictureFormatEnum.JPG.getName().toLowerCase();
            // 转化后的bmp图片路径
            String newJpgPath = basePath + jpgName;
            // 加水印
            log.info("加水印前的源文件：{}", dirPath);
            ImageUtil.addWaterMark(dirPath, newJpgPath, "安印科技", 45);
            log.info("加水印后的目标文件：{}", newJpgPath);
            // 删除未加水印的bmp图片
            deleteFileIfExists(imageLocalFile);
            // 指向jpg图片
            dirPath = newJpgPath;
            imageLocalFile = new File(dirPath);
            // 原始文件名取新的文件名(.png)
            String pngName = prefixion + PictureFormatEnum.PNG.getName().toLowerCase();
            String pngPath = basePath + pngName;
            //抠图，背景透明化
            log.info("抠图前的源文件：{}", dirPath);
            ImageUtil.transferAlpha(dirPath, pngPath);
            log.info("抠图后的目标文件：{}", pngPath);
            // 删除未抠图的jpg图片
            deleteFileIfExists(imageLocalFile);
            // 指向png图片文件
            dirPath = pngPath;
            imageLocalFile = new File(dirPath);
            String fileId = fileService.upload(imageLocalFile);
            String targetUrl = fileService.getFullUrlByFileId(fileId);

            respVO.setPicturePath(targetUrl);
        } catch (IOException e) {
            log.error("上传文件服务器失败:{}", e.getMessage());
            throw new BusinessException(5017,"上传文件服务器失败");
        } catch (Exception e) {
            log.error("上传文件服务器失败:{}", e.getMessage());
            throw new BusinessException(5017,"上传文件服务器失败");
        } finally {
            // 最后删除本地图片
            deleteFileIfExists(imageLocalFile);
        }
        return IyinResult.getIyinResult(respVO);

    }

    @Override
    public IyinResult<Boolean> saveSealInfo(SaveCloudSealVO saveCloudSealVO) {
        String def="40";
        saveCloudSealVO.setPictureHeight(def);
        saveCloudSealVO.setPictureWidth(def);
        String certificateSource = saveCloudSealVO.getCertificateSource();
        if(CertificateEnum.ORG_CERT.getCode().equals(certificateSource)){
            if(StringUtils.isEmpty(saveCloudSealVO.getCertificateId())){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1030);
            }
        }

        SignSysSealInfo signSysSealInfo = new SignSysSealInfo();
        signSysSealInfo.setId(SnowflakeIdWorker.getIdToStr());
        BeanUtils.copyProperties(saveCloudSealVO,signSysSealInfo);
        signSysSealInfo.setPicturePattern("BMP");
        signSysSealInfo.setMediumType("01");
        /**
         * 获取章模二丫码
         */
        SignSysSealPictureDataTmp dataTmp = signSysSealPictureDataTmpService.getById(saveCloudSealVO.getPictureDataTmpId());
        if(dataTmp ==null){
            throw new BusinessException(7020,"查询不到章模信息");
        }
        signSysSealInfo.setPictureData(dataTmp.getPictureData());
        signSysSealInfo.setPictureData64(dataTmp.getPictureData64());
        /**
         * 默认印章状态为 正常
         */
        signSysSealInfo.setPictureStatus("01");

        /**
         * 给深圳默认区域码440301
         */
        String areaCode = "440301";
        String pictureCode = getEsealCodeByAreaCide(areaCode);
        signSysSealInfo.setPictureCode(pictureCode);
        signSysSealInfo.setIsDeleted(0);
        signSysSealInfo.setGmtCreate(new Date());
        signSysSealInfo.setGmtModified(new Date());
        signSysSealInfo.setPictureOrigin("签章系统");



        int count = signSysSealInfoMapper.insert(signSysSealInfo);
        if(count !=1){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1012);
        }
        //同时删除临时表数据
        signSysSealPictureDataTmpService.removeById(saveCloudSealVO.getPictureDataTmpId());

        return  IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<Boolean> updateSealInfo(UpdateCloudSealVO updateCloudSealVO) {
        String def="40";
        updateCloudSealVO.setPictureHeight(def);
        updateCloudSealVO.setPictureWidth(def);
        String pictureCode = updateCloudSealVO.getPictureCode();

        String certificateSource = updateCloudSealVO.getCertificateSource();
        if(CertificateEnum.ORG_CERT.getCode().equals(certificateSource)){
            if(StringUtils.isEmpty(updateCloudSealVO.getCertificateId())){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1030);
            }
        }

        QueryWrapper<SignSysSealInfo> queryWrapper = new QueryWrapper<SignSysSealInfo>().eq("picture_code",pictureCode);
        SignSysSealInfo signSysSealInfo = signSysSealInfoMapper.selectOne(queryWrapper);
        if(signSysSealInfo ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        BeanUtils.copyProperties(updateCloudSealVO,signSysSealInfo);
        signSysSealInfo.setGmtModified(new Date());

        String tmpId = updateCloudSealVO.getPictureDataTmpId();
        if(!StringUtils.isEmpty(tmpId)){
            //重新上传了章莫图片，需要更新章莫数据
            SignSysSealPictureDataTmp dataTmp = signSysSealPictureDataTmpService.getById(updateCloudSealVO.getPictureDataTmpId());
            if(dataTmp ==null){
                throw new BusinessException(7020,"查询不到章模信息");
            }
            signSysSealInfo.setPictureData(dataTmp.getPictureData());
            signSysSealInfo.setPictureData64(dataTmp.getPictureData64());
        }
        int count =signSysSealInfoMapper.updateById(signSysSealInfo);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1013);
        }
        return IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<SealInfoRespVO> querySealInfo(String pictureCode) {
        QueryWrapper<SignSysSealInfo> queryWrapper = new QueryWrapper<SignSysSealInfo>().eq("picture_code",pictureCode);
        SignSysSealInfo signSysSealInfo = signSysSealInfoMapper.selectOne(queryWrapper);
        if(signSysSealInfo ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        SealInfoRespVO sealInfoRespVO = new SealInfoRespVO();
        BeanUtils.copyProperties(signSysSealInfo,sealInfoRespVO);

        //如果是ukey印章，返回ukey印章的证书信息
        String mediumType = signSysSealInfo.getMediumType();
        if("02".equals(mediumType)){
            String cerId = signSysSealInfo.getCertificateId();
            SignSysUkCertInfo signSysUkCertInfo = signSysUkCertInfoMapper.selectById(cerId);
            if(signSysUkCertInfo!=null){
                sealInfoRespVO.setUkCertId(signSysUkCertInfo.getId());
                sealInfoRespVO.setIssuer(signSysUkCertInfo.getIssuer());
                sealInfoRespVO.setOid(signSysUkCertInfo.getOid());
                sealInfoRespVO.setTrustId(signSysUkCertInfo.getTrustId());
                sealInfoRespVO.setValidEnd(signSysUkCertInfo.getValidEnd());
                sealInfoRespVO.setValidStart(signSysUkCertInfo.getValidStart());
            }
        }
        return IyinResult.getIyinResult(sealInfoRespVO);
    }

    @Override
    public IyinResult<Boolean> deleteSealInfo(DeleteEnterpriseSealInfoReqVO reqVO) {
        String enterpriseId =reqVO.getEnterpriseId();
        List<String> pictureCodes = reqVO.getPictureCodes();
        if(pictureCodes ==null ||pictureCodes.isEmpty()){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }

        //如果印章已被分配，则不让删除
        String pictureCode = pictureCodes.get(0);
        QueryWrapper<SignSysSealInfo> sealInfoQueryWrapper = new QueryWrapper<SignSysSealInfo>()
                .eq("picture_code",pictureCode);
        SignSysSealInfo signSysSealInfo = signSysSealInfoMapper.selectOne(sealInfoQueryWrapper);
        if(signSysSealInfo!=null){
            String sealId = signSysSealInfo.getId();
            QueryWrapper<SignSysSealUser> sealUserQueryWrapper = new QueryWrapper<SignSysSealUser>()
                    .eq("seal_id",sealId);
            List<SignSysSealUser> sealUsers = signSysSealUserMapper.selectList(sealUserQueryWrapper);
            if(sealUsers!=null && !sealUsers.isEmpty()){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1063);
            }
        }

        QueryWrapper<SignSysSealInfo> queryWrapper = new QueryWrapper<SignSysSealInfo>().in(true,"picture_code",pictureCode).eq("is_deleted",0);
        int count =  signSysSealInfoMapper.delete(queryWrapper);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1014);
        }

        return IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<IyinPage<SealInfoRespVO>> pageUserListSealInfo(UserSealInfoListReqVO reqVO) {

        IyinPage<SealInfoRespVO> iyinPage = new IyinPage<SealInfoRespVO>(reqVO.getCurrentPage(),reqVO.getPageSize());
        List<SealInfoRespVO> signSysSealInfos = signSysSealInfoMapper.selectPageUserSealInfoList(iyinPage,reqVO);

        for(SealInfoRespVO respVO:signSysSealInfos){
            //如果是ukey印章，返回ukey印章的证书信息
            String mediumType = respVO.getMediumType();
            if("02".equals(mediumType)){
                String cerId = respVO.getCertificateId();
                SignSysUkCertInfo signSysUkCertInfo = signSysUkCertInfoMapper.selectById(cerId);
                if(signSysUkCertInfo!=null){
                    respVO.setUkCertId(signSysUkCertInfo.getId());
                    respVO.setIssuer(signSysUkCertInfo.getIssuer());
                    respVO.setOid(signSysUkCertInfo.getOid());
                    respVO.setTrustId(signSysUkCertInfo.getTrustId());
                    respVO.setValidEnd(signSysUkCertInfo.getValidEnd());
                    respVO.setValidStart(signSysUkCertInfo.getValidStart());
                }
            }
        }
        iyinPage.setRecords(signSysSealInfos);
        return IyinResult.getIyinResult(iyinPage);
    }

    @Override
    @Transactional
    public IyinResult<Boolean> saveUkSealInfo(SaveUkSealReqVO reqVO, HttpServletRequest request) {
        String sealCode = reqVO.getSealCode();
        //先判定这个印章编码对应的ukey印章是否已经入库
        QueryWrapper<SignSysSealInfo> queryWrapper = new QueryWrapper<SignSysSealInfo>()
                .eq("picture_code",sealCode);
        SignSysSealInfo signSysSealInfo = signSysSealInfoMapper.selectOne(queryWrapper);
        if(signSysSealInfo!=null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1047);
        }

        //保存证书信息
        SignSysUkCertInfo signSysUkCertInfo = new SignSysUkCertInfo();
        String certId =SnowflakeIdWorker.getIdToStr();
        BeanUtils.copyProperties(reqVO,signSysUkCertInfo);
        signSysUkCertInfo.setId(certId);
        signSysUkCertInfo.setCurrentStatus("0");
        int count = signSysUkCertInfoMapper.insert(signSysUkCertInfo);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1051);
        }
        signSysSealInfo = new SignSysSealInfo();
        signSysSealInfo.setId(SnowflakeIdWorker.getIdToStr());
        signSysSealInfo.setPictureCode(sealCode);
        signSysSealInfo.setPictureName(reqVO.getPictureName());
        signSysSealInfo.setMediumType("02");
        signSysSealInfo.setCertificateId(certId);
        signSysSealInfo.setPictureUserType(reqVO.getPictureUserType());
        signSysSealInfo.setEnterpriseOrPersonalId(reqVO.getEnterpriseOrPersonalId());
        signSysSealInfo.setPictureBusinessType(reqVO.getPictureBusinessType());
        signSysSealInfo.setPictureStatus("01");
        signSysSealInfo.setGmtCreate(new Date());
        signSysSealInfo.setPictureData64(reqVO.getPictureBase64());

        //将ukey印章base64转换为图片上传到fastdfs
        try {
            String fileName=UUID.randomUUID().toString()+".bmp";
            InMemoryMultipartFile in=new InMemoryMultipartFile(fileName,fileName,"image/bmp",Base64MultipartFile.base64ToMultipart2(reqVO.getPictureBase64()).getBytes());
            FileUploadRespDto fileUploadRespDto = fileService.upload(in,getUser(request));
            String targetUrl = fileService.getFullUrlByFileId(fileUploadRespDto.getFile().getFilePath());
            signSysSealInfo.setPicturePath(targetUrl);
        } catch (IOException e) {
            log.error("上传ukey印章章模失败",e);
        }
        signSysSealInfo.setIsDeleted(0);
        count = signSysSealInfoMapper.insert(signSysSealInfo);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1048);
        }
        return IyinResult.getIyinResult(true);
    }

    private String getUser(HttpServletRequest request) {
        log.info("SignSysSealInfoServiceImpl");
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

    @Override
    public IyinResult<Boolean> updateUkSealInfo(UpdateUkSealReqVO reqVO) {

        String sealId = reqVO.getSealId();
        SignSysSealInfo signSysSealInfo = signSysSealInfoMapper.selectById(sealId);
        if(signSysSealInfo ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        BeanUtils.copyProperties(reqVO,signSysSealInfo);
        int count =signSysSealInfoMapper.updateById(signSysSealInfo);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1049);
        }
        return IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<IyinPage<SealInfoRespVO>> pageListSealInfo(SealInfoListReqVO reqVO) {
        IyinPage<SealInfoRespVO> iyinPage = new IyinPage<SealInfoRespVO>(reqVO.getCurrentPage(),reqVO.getPageSize());
        List<SealInfoRespVO> signSysSealInfos = signSysSealInfoMapper.pageListSealInfo(iyinPage,reqVO);

        for(SealInfoRespVO respVO:signSysSealInfos){
            //如果是ukey印章，返回ukey印章的证书信息
            String mediumType = respVO.getMediumType();
            if("02".equals(mediumType)){
                String cerId = respVO.getCertificateId();
                SignSysUkCertInfo signSysUkCertInfo = signSysUkCertInfoMapper.selectById(cerId);
                if(signSysUkCertInfo!=null){
                    respVO.setUkCertId(signSysUkCertInfo.getId());
                    respVO.setIssuer(signSysUkCertInfo.getIssuer());
                    respVO.setOid(signSysUkCertInfo.getOid());
                    respVO.setTrustId(signSysUkCertInfo.getTrustId());
                    respVO.setValidEnd(signSysUkCertInfo.getValidEnd());
                    respVO.setValidStart(signSysUkCertInfo.getValidStart());
                }
            }
        }
        iyinPage.setRecords(signSysSealInfos);
        return IyinResult.getIyinResult(iyinPage);
    }

    @Override
    public IyinResult<SealDefinedUploadRespVO> definedCreateSeal(GeneratePicReqVO generatePicReqVO) {
        //1、组装生成图片的参数
        CircularSealPicDTO circularSealPicDTO = GenerateSealPicGraphics2DUtil.configurationCircularSealPic(generatePicReqVO);
        //2、绘图，成生bufferedImage
        BufferedImage signPic = GenerateSealPicGraphics2DUtil.generateBufferedImageCircle(circularSealPicDTO);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(signPic, "png", out);
        } catch (IOException e) {
            log.error("BufferedImage转ByteArrayOutputStream异常：{}", e.getMessage());
        }
        MultipartFile multipartFile = new MockMultipartFile("file", circularSealPicDTO.getFileName(), ContentType.IMAGE_PNG.toString(), out.toByteArray());
        SealPictureUploadReqVO reqVO = new SealPictureUploadReqVO();
        reqVO.setFile(multipartFile);
        //自动生成的不走c++,所以这里设置为02，规避C++调用
        reqVO.setPictureType("01");
        return this.uploadSealDefinedFile(reqVO);
    }

    private void deleteFileIfExists(File imageLocalFile) {
        Path path = imageLocalFile.toPath();
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("临时文件删除失败:{}", e);
            throw new BusinessException(5011,"临时文件删除失败");
        }
    }

    private String fileToEYBase64(File file, String creditCode) {
        String sealImageBase64 = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            int[] bt = SealYS.getEYsByte(fis, creditCode);
            byte[] byte1 = SealYS.intToByte(bt);
            sealImageBase64 = Base64Util.encode(byte1);
        } catch (Exception e) {
            log.error("章模压缩失败：{}", e);
            deleteFileIfExists(file);
        }
        return sealImageBase64;
    }

    private Map<String, Integer> getPictureWidthAndHeight(String path, File imageLocalFile) {
        Map<String, Integer> map = new HashMap<>(16);

        try (InputStream inputStream = new FileInputStream(imageLocalFile)) {
            ImageUtil.newAllResizeImage(path, PictureFormatEnum.BMP.getName().toLowerCase());

            //不需要知道文件名信息，传null
            //传inputStream时，处理完文件可删除，直接传file会被占用，无法删除
            ImageInfo imageInfo = Sanselan.getImageInfo(inputStream, null);
            Integer heightDpi = imageInfo.getPhysicalHeightDpi();
            int defaultDpi = 400;
            heightDpi = heightDpi == defaultDpi ? heightDpi : defaultDpi;
            Integer widthDpi = imageInfo.getPhysicalWidthDpi();
            widthDpi = widthDpi == defaultDpi ? widthDpi : defaultDpi;
            Integer heightPx = imageInfo.getHeight();
            Integer widthPx = imageInfo.getWidth();
            //获取物理宽和高
            double standardParam = 25.4;
            int h = (int) (heightPx * standardParam / heightDpi);
            int w = (int) (widthPx * standardParam / widthDpi);

            map.put(pictureHeightKey, h);
            map.put(pictureWidthKey, w);
        } catch (Exception e) {
            //删除图片
            deleteFileIfExists(imageLocalFile);
            log.error("章模图片宽高计算失败：{}", e.getMessage());
            throw new BusinessException(7004,"文件处理异常，请稍后再试");
        }

        return map;
    }

    /**
     * @param areaCode
     * @return
     * @description: 通过区域码（6位）获取电子印章编码
     * ,区域码也是统一社会信用代码中的（3-8）位。
     * @author 麦达剑
     * @updateTime ：2018/10/02
     */
    public String getEsealCodeByAreaCide(String areaCode) {
        String esealCode = null;
        //随机数扩展的最大取值范围，扩大后[0,9)
        int randomExtMaxValue = 9;
        //随机数范围移动值，移动后[1,10)
        int randomMoveValue = 1;
        //幂运算底数
        int baseNum = 10;
        //生成目标位数
        int targetBit = 8;
        //截取开始位置
        int subStartIndex = 1;
        //9位int算式
        int resultNumber = (int) ((Math.random() * randomExtMaxValue + randomMoveValue) * Math.pow(baseNum, targetBit));
        //转成String去掉最高位，剩下8位随机字符串
        String targetRandomStr = String.valueOf(resultNumber).substring(subStartIndex);
        //拼接成印章编码
        esealCode = areaCode + targetRandomStr;
        return esealCode;
    }
}
