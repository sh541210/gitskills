package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.iyin.sign.system.common.enums.AppEnum;
import com.iyin.sign.system.common.enums.LoginTypeEnum;
import com.iyin.sign.system.common.enums.UseSealSourceEnum;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.common.utils.BeanAdaptUtil;
import com.iyin.sign.system.dto.req.SignSysUseSealDTO;
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
import com.iyin.sign.system.util.Base64Util;
import com.iyin.sign.system.util.JWTUtils;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.vo.req.*;
import com.iyin.sign.system.vo.resp.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName SealManagerServiceImpl
 * SealManagerServiceImpl
 * @Author wdf
 * @Date 2019/10/8 17:51
 * @throws
 * @Version 1.0
 **/
@Service
@Slf4j
public class SealManagerServiceImpl implements ISealManagerService {

    private static final String API_TOKEN = "api_token";
    private static final String SESSION_TOKEN = "session_token";
    private static final String PDF = ".pdf";
    private static final String DATA = "data";
    private static final String CODE = "code";
    private static final String USER_VO_LIST = "userVoList";
    private static final String COUNT = "count";

    @Value("${fileTempPath}")
    private String fileTempPath;
    @Value("${file-num}")
    private Integer fileNum;
    /**
     * 白鹤 配置
     */
    @Value("${private-baihe.appKey}")
    private String appKey;

    /**
     * 白鹤 配置
     */
    @Value("${private-baihe.secret}")
    private String secret;

    /**
     * 白鹤 配置
     */
    @Value("${private-baihe.companyId}")
    private String companyId;

    /**
     * 白鹤 配置
     */
    @Value("${private-baihe.managerPhone}")
    private String managerPhone;

    @Autowired
    private SealManagerService sealManagerService;
    @Autowired
    private JWTUtils jwtUtils;
    @Resource
    private SignSysUserInfoMapper signSysUserInfoMapper;
    @Resource
    private SignSysFileDownMapper signSysFileDownMapper;
    @Resource
    private SignSysUseSealMapper signSysUseSealMapper;
    @Resource
    private FileResourceMapper fileResourceMapper;
    @Autowired
    private IFileService fileService;
    @Resource
    private SignSysFilePrintMapper signSysFilePrintMapper;
    @Autowired
    private IAuthTokenService authTokenService;
    @Autowired
    private FileResourceService fileResourceService;
    @Autowired
    private SignSysUserInfoService signSysUserInfoService;

    @Override
    public SealListRespVO sealList(Boolean hasExpired,Boolean enableFlag, HttpServletRequest request){
        String userId=getUser(request);

        IyinResult<SignSysUserInfoRespVO> signSysUserInfoRespVOIyinResult=signSysUserInfoService.queryAccountInfoById(userId);
        IyinResultValidUtil.validateSuccess(signSysUserInfoRespVOIyinResult);
        SignSysUserInfoRespVO signSysUserInfoRespVO=signSysUserInfoRespVOIyinResult.getData();
        if(null==signSysUserInfoRespVO){
            throw new BusinessException(ErrorCode.REQUEST_10010);
        }
        String data = getBaiHeToken(signSysUserInfoRespVO);

        Map<String,Object> map=sealManagerService.sealList(data,companyId,signSysUserInfoRespVO.getOtherId(),enableFlag);

        SealListRespVO sealListRespVO=new SealListRespVO();
        if(null==map){
            return sealListRespVO;
        }
        String code=map.get(CODE)+"";
        if(AppEnum.SUCCESS.getCode()==Integer.parseInt(code)){
            List<SealPrivateRespVO> list;
            list= (List<SealPrivateRespVO>) map.get(DATA);
            String ck="true";
            if(ck.equals(hasExpired)){
                for (SealPrivateRespVO sealPrivateRespVO:list){
                    if(sealPrivateRespVO.getServiceTime()<System.currentTimeMillis()){
                        list.remove(sealPrivateRespVO);
                    }
                }
            }
            sealListRespVO.setSealList(list);
            if(null== map.get(COUNT)){
                throw new BusinessException(ErrorCode.REQUEST_10012);
            }
            sealListRespVO.setCount(Integer.parseInt(map.get(COUNT)+""));
        }else{
            sealListRespVO.setCount(0);
        }
        return sealListRespVO;
    }

    @Override
    public List<SealFileInfoListRespVO> getFilePrintInfoList(String applyId) {
        SignSysUseSeal signSysUseSeal=signSysUseSealMapper.selectOne(Wrappers.<SignSysUseSeal>query().lambda().eq(SignSysUseSeal::getApplyId,applyId));
        if(null==signSysUseSeal){
            throw new BusinessException(ErrorCode.REQUEST_40311);
        }
        if(StringUtils.isBlank(signSysUseSeal.getFileCode())){
            throw new BusinessException(ErrorCode.REQUEST_40311);
        }
        String[] fileCodes=signSysUseSeal.getFileCode().split(",");
        List<FileResource> resList=fileResourceMapper.selectList(Wrappers.<FileResource>query().lambda().in(FileResource::getFileCode,fileCodes));
        List<SealFileInfoListRespVO> list= new ArrayList<>();
        for (int i=0;i<resList.size();i++){
            SealFileInfoListRespVO sealFileInfoListRespVO=new SealFileInfoListRespVO();
            FileResource fileResource=resList.get(i);
            sealFileInfoListRespVO.setFileCode(fileResource.getFileCode());
            sealFileInfoListRespVO.setFileName(fileResource.getFileName());
            sealFileInfoListRespVO.setPageTotal(fileResource.getPageTotal());
            list.add(sealFileInfoListRespVO);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addUseSeal(AddUseSealReqVO addUseSealReqVO, HttpServletRequest request) {
        String[] file=addUseSealReqVO.getFileCode().split(",");
        if(file.length>fileNum){
            throw new BusinessException(ErrorCode.REQUEST_40312);
        }
        SignSysUseSeal signSysUseSeal=new SignSysUseSeal();
        signSysUseSeal.setApplyId(SnowflakeIdWorker.getIdToStr());
        signSysUseSeal.setFileCode(addUseSealReqVO.getFileCode());
        signSysUseSeal.setSealId(addUseSealReqVO.getSealId());
        signSysUseSeal.setApplyCount(addUseSealReqVO.getApplyCount());
        signSysUseSeal.setExpireTime(addUseSealReqVO.getExpireTime());
        signSysUseSeal.setSource(3);

        String userId=getUser(request);

        IyinResult<SignSysUserInfoRespVO> signSysUserInfoRespVOIyinResult=signSysUserInfoService.queryAccountInfoById(userId);
        IyinResultValidUtil.validateSuccess(signSysUserInfoRespVOIyinResult);
        SignSysUserInfoRespVO signSysUserInfoRespVO=signSysUserInfoRespVOIyinResult.getData();
        if(null==signSysUserInfoRespVO){
            throw new BusinessException(ErrorCode.REQUEST_10010);
        }
        String data = getBaiHeToken(signSysUserInfoRespVO);

        Map<String,Object> mapSeal=sealManagerService.sealInfo(data,companyId,signSysUserInfoRespVO.getOtherId(),addUseSealReqVO.getSealId(),"1");
        if(null==mapSeal){
            throw new BusinessException(ErrorCode.REQUEST_10009);
        }
        String code=mapSeal.get(CODE)+"";
        SealPrivateRespVO sealInfo;
        if(AppEnum.SUCCESS.getCode()==Integer.parseInt(code)){
            sealInfo= (SealPrivateRespVO) BeanAdaptUtil.map2Obj2((Map)mapSeal.get(DATA),SealPrivateRespVO.class);
        }else{
            throw new BusinessException(ErrorCode.REQUEST_10009);
        }
        if(null==sealInfo){
            throw new BusinessException(ErrorCode.REQUEST_10009);
        }

        signSysUseSeal.setSealName(sealInfo.getName());
        signSysUseSeal.setApplyCause(addUseSealReqVO.getApplyCause());
        signSysUseSeal.setIsDeleted(0);
        signSysUseSeal.setGmtCreate(new Date());
        signSysUseSeal.setCreatUser(userId);

        UploadReqVO uploadReqVO=new UploadReqVO();
        BeanUtils.copyProperties(addUseSealReqVO,uploadReqVO);

        uploadReqVO.setApplyPdf(UseSealSourceEnum.CONTRACT.getName());
        uploadReqVO.setApplyUser(signSysUserInfoRespVO.getOtherId());
        uploadReqVO.setExpireTime(addUseSealReqVO.getExpireTime().getTime());
        uploadReqVO.setFileNumber(file.length);
        uploadReqVO.setFileType(UseSealSourceEnum.CONTRACT.getName());
        uploadReqVO.setImgList(null);
        uploadReqVO.setSealId(addUseSealReqVO.getSealId());

        Map<String,Object> addUseSealMap=sealManagerService.upload(signSysUserInfoRespVO.getOtherId(),companyId,data,uploadReqVO);
        log.info("addUseSeal addUseSealMap:{}",addUseSealMap);
        getData(addUseSealMap);
        return signSysUseSealMapper.insert(signSysUseSeal);
    }

    private String getBaiHeToken(SignSysUserInfoRespVO signSysUserInfoRespVO) {
        Map<String,Object> tokenMap=sealManagerService.accessToken(appKey,secret,signSysUserInfoRespVO.getOtherId());
        return getData(tokenMap);
    }

    private String getData(Map<String, Object> tokenMap) {
        log.debug("SealManagerServiceImpl");
        log.info("tokenMap:{}",tokenMap);
        if(tokenMap.isEmpty()){
            throw new BusinessException(ErrorCode.REQUEST_10012);
        }
        //校验tokenMap
        String tokenCode=tokenMap.get(CODE)+"";
        if(AppEnum.SUCCESS.getCode()!=Integer.parseInt(tokenCode)){
            throw new BusinessException(ErrorCode.REQUEST_10012);
        }
        String data=tokenMap.get(DATA)+"";
        if(StringUtils.isBlank(data)){
            throw new BusinessException(ErrorCode.REQUEST_10012);
        }
        return data;
    }

    @Override
    public InMemoryMultipartFile addFilePrint(String fileCodes, HttpServletRequest request) {
        String id=getUser(request);
        String[] files=fileCodes.split(",");
        String folder = fileTempPath + SnowflakeIdWorker.getIdToStr() + File.separator;
        int num=0;
        String destPath = folder + "all.pdf";
        try {
            for (int i=0;i<files.length;i++){
                SignSysUserInfo signSysUserInfo=signSysUserInfoMapper.selectById(id);
                SignSysFilePrint signSysFilePrint=new SignSysFilePrint();
                signSysFilePrint.setFileCode(files[i]);
                signSysFilePrint.setId(String.valueOf(SnowflakeIdWorker.getId()));
                //用户相关
                signSysFilePrint.setUserChannel(AppEnum.USERAPI.getCode());
                signSysFilePrint.setPrintUser(id);
                signSysFilePrint.setUserEnterprise(signSysUserInfo.getEnterpriseOrPersonalId());
                signSysFilePrint.setUserType("01".equals(signSysUserInfo.getUserType())?1:2);
                signSysFilePrint.setGmtCreate(new Date());
                InMemoryMultipartFile in=fileService.fetch(fileCodes);
                if(null!=in){
                    signSysFilePrintMapper.insert(signSysFilePrint);
                    Base64Util.byteArrayToFile(in.getBytes(), folder + ++num + PDF);
                }else{
                    throw new BusinessException(FileResponseCode.DATA_NOT_FOUND);
                }
            }
            merge(folder, destPath);
            log.info("com.iyin.sign.system.service.impl.SealManagerServiceImpl.addFilePrint,merge");
            byte[] bytes = Base64Util.fileToByte(destPath);
            log.info("文件转 byte");
            return new InMemoryMultipartFile("file", "summary.pdf", "application/pdf", bytes);
        } catch (IOException e) {
            throw new BusinessException(FileResponseCode.DATA_FILE_COVERT_EXCEPTION);
        } finally {
            log.info("删除文件和目录");
            try {
                FileUtils.deleteDirectory(new File(folder));
            } catch (IOException e) {
                throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
            }
        }
    }

    private static void merge(String folderName, String destPath) throws IOException {
        log.debug("merge pdf");
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

    @Override
    public IyinResult<IyinPage<SignSysUseSealDTO>> getUseSealList(Integer currPage, Integer pageSize, String sealId,String applyCause, HttpServletRequest request) {
        IyinPage<SignSysUseSealDTO> page = new IyinPage<>(currPage, pageSize);
        SignSysUseSealDTO signSysUseSealDTO=new SignSysUseSealDTO();
        if(StringUtils.isNotBlank(sealId)){
            signSysUseSealDTO.setSealId(sealId);
        }
        if(StringUtils.isNotBlank(applyCause)){
            signSysUseSealDTO.setApplyCause(applyCause);
        }
        signSysUseSealDTO.setCreatUser(getUser(request));
        List<SignSysUseSealDTO> signSysUseSealDTOS=signSysUseSealMapper.getPageList(page,signSysUseSealDTO);
        page.setRecords(signSysUseSealDTOS);
        //返回请求结果
        IyinResult<IyinPage<SignSysUseSealDTO>> result = IyinResult.success();
        result.setData(page);
        return result;
    }

    @Override
    public Integer deleteUseSeal(String applyId, HttpServletRequest request) {
        return signSysUseSealMapper.delete(Wrappers.<SignSysUseSeal>lambdaQuery().eq(SignSysUseSeal::getApplyId,applyId));
    }

    @Override
    public InMemoryMultipartFile addFileDownZip(String applyId, HttpServletRequest request) {
        String id=getUser(request);
        SignSysUseSeal signSysUseSeal=signSysUseSealMapper.selectOne(Wrappers.<SignSysUseSeal>query().lambda().eq(SignSysUseSeal::getApplyId,applyId));
        if(null==signSysUseSeal){
            throw new BusinessException(ErrorCode.REQUEST_40311);
        }
        if(StringUtils.isBlank(signSysUseSeal.getFileCode())){
            throw new BusinessException(ErrorCode.REQUEST_40311);
        }

        String[] fileCodes=signSysUseSeal.getFileCode().split(",");
        DocPackageReqDto reqDto=new DocPackageReqDto();
        reqDto.setUserId(id);
        reqDto.setResourceIds(Arrays.asList(fileCodes));
        InMemoryMultipartFile in=fileService.downloadDocZip(reqDto);
        if(null!=in){
            for (int i=0;i<reqDto.getResourceIds().size();i++){
                SignSysFileDown signSysFileDown=new SignSysFileDown();
                signSysFileDown.setId(String.valueOf(SnowflakeIdWorker.getId()));
                signSysFileDown.setFileCode(reqDto.getResourceIds().get(i));
                SignSysUserInfo signSysUserInfo=signSysUserInfoMapper.selectById(id);
                signSysFileDown.setDownUser(id);
                signSysFileDown.setGmtCreate(new Date());
                signSysFileDown.setUserChannel(AppEnum.USERAPI.getCode());
                signSysFileDown.setUserType("01".equals(signSysUserInfo.getUserType())?1:2);
                signSysFileDown.setUserEnterprise(signSysUserInfo.getEnterpriseOrPersonalId());
                signSysFileDownMapper.insert(signSysFileDown);

                FileResource res = fileResourceService.findByFileCode(reqDto.getResourceIds().get(i));
                FileResource resource = new FileResource();
                resource.setFileCode(reqDto.getResourceIds().get(i));
                resource.setDownCount(res.getDownCount() + 1);
                fileResourceMapper.updateFile(resource);
            }

        }
        return in;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IyinResult<Integer> syncUserList(SycnUserInfoListReqVO sycnUserInfoListReqVO) {
        List<SignSysUserInfo> list=signSysUserInfoMapper.selectList(Wrappers.<SignSysUserInfo>lambdaQuery()
                .in(SignSysUserInfo::getId,sycnUserInfoListReqVO.getUserList()));

        List<SycnUserReqVO> userVoList=new ArrayList<>();
        for (SignSysUserInfo userInfo:list){
            SycnUserReqVO sycnUserReqVO=new SycnUserReqVO();
            if(LoginTypeEnum.PHONE_LOGIN.getCode().equals(userInfo.getLoginType())){
                sycnUserReqVO.setMobilePhone(userInfo.getUserName());
            }
            if(LoginTypeEnum.EMAIL_LOGIN.getCode().equals(userInfo.getLoginType())){
                sycnUserReqVO.setEmail(userInfo.getUserName());
            }
            sycnUserReqVO.setPassword("12345678");
            sycnUserReqVO.setUserName(userInfo.getUserNickName());
            userVoList.add(sycnUserReqVO);
        }
        if(userVoList.isEmpty()){
            throw new BusinessException(ErrorCode.REQUEST_10007);
        }
        Map<String ,Object> map=sealManagerService.syncUserList(appKey,secret,managerPhone,userVoList);
        log.info("syncUserList map:{}",map);
        if(null==map){
            throw new BusinessException(ErrorCode.REQUEST_10008);
        }
        String code=map.get(CODE)+"";
        boolean diffRes=AppEnum.SUCCESS.getCode()==Integer.parseInt(code);
        if(!diffRes){
            throw new BusinessException(ErrorCode.REQUEST_10008);
        }
        String flag=map.get(DATA)+"";
        int count=0;
        if(StringUtils.isBlank(flag)){
            throw new BusinessException(ErrorCode.REQUEST_10008);
        }else{
            //获取id 回填用户数据
            Map<String ,Object> dataMap = (Map<String, Object>) map.get(DATA);
            if(dataMap.isEmpty()){
                throw new BusinessException(ErrorCode.REQUEST_10008);
            }
            if(null==dataMap.get(USER_VO_LIST)){
                throw new BusinessException(ErrorCode.REQUEST_10008);
            }
            List userList= (List) dataMap.get(USER_VO_LIST);
            for(int i=0;i<userList.size();i++){
                Map userMap= (Map) userList.get(i);
                SycnUserReqVO sycnUserReqVO= (SycnUserReqVO) BeanAdaptUtil.map2Obj2(userMap,SycnUserReqVO.class);

                SignSysUserInfo signSysUserInfo=new SignSysUserInfo();
                signSysUserInfo.setOtherId(sycnUserReqVO.getId());

                for (SignSysUserInfo userInfo:list){
                    if(userInfo.getUserName().equalsIgnoreCase(sycnUserReqVO.getMobilePhone())||userInfo.getUserName().equalsIgnoreCase(sycnUserReqVO.getEmail())){
                        //需要修改的数据
                        QueryWrapper<SignSysUserInfo> queryWrapper = new QueryWrapper<SignSysUserInfo>().eq("id", userInfo.getId());
                        count=count+signSysUserInfoMapper.update(signSysUserInfo,queryWrapper);
                        break;
                    }
                }
            }
        }
        return IyinResult.success(count);
    }

    @Override
    public InMemoryMultipartFile fetchImage(Integer category,String fileName, HttpServletRequest request) {
        return sealManagerService.fetchImage(category,fileName,companyId);
    }

    @Override
    public StampRecordResultRespVO stampRecordList(UseSealStampRecordList useSealStampRecordList, HttpServletRequest request) {
        String userId=getUser(request);

        IyinResult<SignSysUserInfoRespVO> signSysUserInfoRespVOIyinResult=signSysUserInfoService.queryAccountInfoById(userId);
        IyinResultValidUtil.validateSuccess(signSysUserInfoRespVOIyinResult);
        SignSysUserInfoRespVO signSysUserInfoRespVO=signSysUserInfoRespVOIyinResult.getData();
        if(null==signSysUserInfoRespVO){
            throw new BusinessException(ErrorCode.REQUEST_10010);
        }
        String data = getBaiHeToken(signSysUserInfoRespVO);

        Map<String , Object> stampRecordListMap=sealManagerService.stampRecordList(signSysUserInfoRespVO.getOtherId(),companyId,data,useSealStampRecordList);
        log.info("stampRecordList stampRecordListMap:{}",stampRecordListMap);
        getData(stampRecordListMap);
        List<StampRecordRespVO> stampRecordRespVOList= (List<StampRecordRespVO>) stampRecordListMap.get(DATA);
        StampRecordResultRespVO stampRecordResultRespVO=new StampRecordResultRespVO();
        stampRecordResultRespVO.setStampRecordRespVOList(stampRecordRespVOList);

        if(null== stampRecordListMap.get(DATA)){
            throw new BusinessException(ErrorCode.REQUEST_10012);
        }
        if(null== stampRecordListMap.get(COUNT)){
            throw new BusinessException(ErrorCode.REQUEST_10012);
        }
        Integer count=Integer.parseInt(stampRecordListMap.get(COUNT)+"");
        stampRecordResultRespVO.setCount(count);
        return stampRecordResultRespVO;
    }

    @Override
    public StampRecordApplyResultRespVO stampRecordApplyList(StampRecordApplyListReqVO stampRecordApplyListReqVO, HttpServletRequest request) {
        String userId=getUser(request);

        IyinResult<SignSysUserInfoRespVO> signSysUserInfoRespVOIyinResult=signSysUserInfoService.queryAccountInfoById(userId);
        IyinResultValidUtil.validateSuccess(signSysUserInfoRespVOIyinResult);
        SignSysUserInfoRespVO signSysUserInfoRespVO=signSysUserInfoRespVOIyinResult.getData();
        if(null==signSysUserInfoRespVO){
            throw new BusinessException(ErrorCode.REQUEST_10010);
        }
        String data = getBaiHeToken(signSysUserInfoRespVO);

        Map<String , Object> stampRecordApplyListMap=sealManagerService.stampRecordApplyList(signSysUserInfoRespVO.getOtherId(),companyId,data,stampRecordApplyListReqVO);
        getData(stampRecordApplyListMap);
        List<StampRecordApplyVO> recordApplyVOList= (List<StampRecordApplyVO>) stampRecordApplyListMap.get(DATA);
        StampRecordApplyResultRespVO stampRecordApplyResultRespVO=new StampRecordApplyResultRespVO();
        stampRecordApplyResultRespVO.setStampRecordApplyVOList(recordApplyVOList);

        if(null== stampRecordApplyListMap.get(DATA)){
            throw new BusinessException(ErrorCode.REQUEST_10012);
        }
        if(null== stampRecordApplyListMap.get(COUNT)){
            throw new BusinessException(ErrorCode.REQUEST_10012);
        }
        Integer count=Integer.parseInt(stampRecordApplyListMap.get(COUNT)+"");
        stampRecordApplyResultRespVO.setCount(count);
        return stampRecordApplyResultRespVO;
    }

    @Override
    public SealRecordListRespVO applyStampRecordList(UseSealStampRecordApplyListReqVO useSealStampRecordApplyListReqVO, HttpServletRequest request) {
        String userId=getUser(request);

        IyinResult<SignSysUserInfoRespVO> signSysUserInfoRespVOIyinResult=signSysUserInfoService.queryAccountInfoById(userId);
        IyinResultValidUtil.validateSuccess(signSysUserInfoRespVOIyinResult);
        SignSysUserInfoRespVO signSysUserInfoRespVO=signSysUserInfoRespVOIyinResult.getData();
        if(null==signSysUserInfoRespVO){
            throw new BusinessException(ErrorCode.REQUEST_10010);
        }
        String data = getBaiHeToken(signSysUserInfoRespVO);

        Map<String , Object> applyStampRecordList=sealManagerService.applyStampRecordList(signSysUserInfoRespVO.getOtherId(),companyId,data,useSealStampRecordApplyListReqVO);
        log.info("applyStampRecordList applyStampRecordList:{}",applyStampRecordList);
        getData(applyStampRecordList);
        List<SealRecordVO> sealRecordVOList= (List<SealRecordVO>) applyStampRecordList.get(DATA);
        SealRecordListRespVO sealRecordListRespVO=new SealRecordListRespVO();
        sealRecordListRespVO.setSealRecordVOList(sealRecordVOList);

        if(null== applyStampRecordList.get(DATA)){
            throw new BusinessException(ErrorCode.REQUEST_10012);
        }
        if(null== applyStampRecordList.get(COUNT)){
            throw new BusinessException(ErrorCode.REQUEST_10012);
        }
        Integer count=Integer.parseInt(applyStampRecordList.get(COUNT)+"");
        sealRecordListRespVO.setCount(count);
        return sealRecordListRespVO;
    }

    private String getUser(HttpServletRequest request) {
        log.info("SealManagerServiceImpl");
        String userId="";
        String token = request.getHeader(API_TOKEN);
        String sessionToken = request.getHeader(SESSION_TOKEN);
        if (StringUtils.isBlank(token) && StringUtils.isBlank(sessionToken)) {
            throw new BusinessException(ErrorCode.REQUEST_40434);
        }
        if(StringUtils.isNotBlank(sessionToken)){
            log.info("SealManagerServiceImpl ck sessionToken");
            Claims claims = jwtUtils.parseJWT(sessionToken);
            userId = String.valueOf(claims.get("userId"));
        }else{
            SysSignApplication sysSignApplication = authTokenService.verifyAuthToken(token);
            userId = sysSignApplication.getUserId();
        }
        return userId;
    }
}
