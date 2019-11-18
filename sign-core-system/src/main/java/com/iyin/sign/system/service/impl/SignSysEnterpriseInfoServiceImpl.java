package com.iyin.sign.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.common.enums.UserSourceEnum;
import com.iyin.sign.system.common.enums.UserTypeEnum;
import com.iyin.sign.system.entity.*;
import com.iyin.sign.system.mapper.*;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.RedisKeyConstant;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.model.exception.TokenCheckException;
import com.iyin.sign.system.service.RedisService;
import com.iyin.sign.system.service.SignSysAdminUserService;
import com.iyin.sign.system.service.SignSysEnterpriseInfoService;
import com.iyin.sign.system.service.SignSysNodeInfoService;
import com.iyin.sign.system.util.JWTUtils;
import com.iyin.sign.system.util.ParamValidateLegalUtil;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.vo.req.*;
import com.iyin.sign.system.vo.resp.BatchImportResultRespVO;
import com.iyin.sign.system.vo.resp.EnterprsieBatchImportRespVO;
import com.iyin.sign.system.vo.resp.EnterprsieDetailRespVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: SignSysEnterpriseInfoServiceImpl
 * @Description: 签章系统企业服务
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 14:59
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 14:59
 * @Version: 0.0.1
 */
@Service
@Slf4j
public class SignSysEnterpriseInfoServiceImpl extends ServiceImpl<SignSysEnterpriseInfoMapper,SignSysEnterpriseInfo> implements SignSysEnterpriseInfoService {

    @Autowired
    SignSysEnterpriseInfoMapper signSysEnterpriseInfoMapper;

    @Autowired
    SignSysUserInfoMapper signSysUserInfoMapper;

    @Autowired
    SignSysNodeInfoMapper signSysNodeInfoMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    SignSysNodeInfoService signSysNodeInfoService;

    @Autowired
    SignSysAdminUserMapper signSysAdminUserMapper;

    @Autowired
    SignSysAdminUserService signSysAdminUserService;

    @Autowired
    SignSysUserRoleMapper signSysUserRoleMapper;

    @Autowired
    SignSysUserDataLimitMapper signSysUserDataLimitMapper;

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;




    @Override
    public IyinResult<IyinPage<EnterprsieDetailRespVO>> pageListEnterprise(EnterprsieListReqVO reqVO, HttpServletRequest request) {

        String userId=null;
        try {
            String sessionToken = request.getHeader("session_token");
            Claims claims = jwtUtils.parseJWT(sessionToken);
            userId =String.valueOf(claims.get("userId"));
        } catch (TokenCheckException e) {
            e.printStackTrace();
        }

        /**
         * 单位管理员只显示自己单位的那条数据
         * 超级管理员显示全部单位数据
         */
        if(!StringUtils.isEmpty(userId)){
            boolean superAdmin = signSysAdminUserService.checkSuperAdmin(userId);
            if(!superAdmin){
                //单位管理员
                SignSysAdminUser signSysAdminUser = signSysAdminUserMapper.selectById(userId);
                if(signSysAdminUser!=null){
                   String enterpriseId =  signSysAdminUser.getEnterpriseId();
                   SignSysEnterpriseInfo enterpriseInfo = signSysEnterpriseInfoMapper.selectById(enterpriseId);
                    reqVO.setChineseName(enterpriseInfo.getChineseName());
                }

            }
        }
        IyinPage<EnterprsieDetailRespVO> iyinPage =new IyinPage<EnterprsieDetailRespVO>(reqVO.getCurrentPage(),reqVO.getPageSize());
        List<EnterprsieDetailRespVO> enterpriseRespVOList = signSysEnterpriseInfoMapper.pageListEnterprise(iyinPage,reqVO);
        iyinPage.setRecords(enterpriseRespVOList);
        return IyinResult.getIyinResult(iyinPage);
    }

    @Override
    public IyinResult<EnterprsieDetailRespVO> getEnterpriseDetail(String id) {
        SignSysEnterpriseInfo  signSysEnterpriseInfo =  signSysEnterpriseInfoMapper.selectById(id);
        if( signSysEnterpriseInfo ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1003);
        }
        EnterprsieDetailRespVO respVO = new EnterprsieDetailRespVO();
        BeanUtils.copyProperties(signSysEnterpriseInfo,respVO);
        return IyinResult.getIyinResult(respVO);
    }

    @Override
    public IyinResult<String> createEnterpriseStepOne(CreateEnterpriseStepOneReqVO reqVO) {

        /**
         * 验证单位名称和统一社会信用代码是否被占用
         */
        String chineseName = reqVO.getChineseName();
        String creditCode = reqVO.getCreditCode();

        QueryWrapper<SignSysEnterpriseInfo> queryWrapper = new QueryWrapper<SignSysEnterpriseInfo>().eq("chinese_name",chineseName);

        SignSysEnterpriseInfo dbSignSysEnterpriseInfo = signSysEnterpriseInfoMapper.selectOne(queryWrapper);
        if(dbSignSysEnterpriseInfo !=null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1019);
        }
        queryWrapper = new QueryWrapper<SignSysEnterpriseInfo>().eq("credit_code",creditCode);
        dbSignSysEnterpriseInfo = signSysEnterpriseInfoMapper.selectOne(queryWrapper);
        if(dbSignSysEnterpriseInfo !=null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1020);
        }
        String enterpriseId = SnowflakeIdWorker.getIdToStr();
        redisService.setEn(RedisKeyConstant.CREATE_ENTERPRISE_STEP_ONE_PRE+enterpriseId,reqVO);
        return IyinResult.getIyinResult(enterpriseId);
    }

    @Override
    @Transactional
    public IyinResult<Boolean> createEnterpriseStepTwo(CreateEnterpriseStepTwoReqVO reqVO) {

        String enterpriseId = reqVO.getEnterpriseId();
        String password = reqVO.getPassword();
        String userNameType = reqVO.getUserNameType();
        String userName = reqVO.getUserName();

        CreateEnterpriseStepOneReqVO enterpriseStepOneReqVO = redisService.getEn(RedisKeyConstant.CREATE_ENTERPRISE_STEP_ONE_PRE+enterpriseId);
        if(enterpriseStepOneReqVO ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }

        if("01".equals(userNameType)){
            //验证手机号的合法性
            if(!ParamValidateLegalUtil.isMobile(userName)){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1023);
            }
        }else if("02".equals(userNameType)){
            //验证邮箱
            if(!ParamValidateLegalUtil.isEmail(userName)){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1024);
            }
        }else {
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }

        //判断账户名是否被占用了
        QueryWrapper<SignSysAdminUser> queryWrapper = new QueryWrapper<SignSysAdminUser>().eq("user_name",userName);
        SignSysAdminUser dbSignSysUser = signSysAdminUserMapper.selectOne(queryWrapper);
        if(dbSignSysUser!=null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1025);
        }


        //保存组织结构数据
        SignSysNodeInfo signSysNodeInfo = new SignSysNodeInfo();
        String nodeId = SnowflakeIdWorker.getIdToStr();
        signSysNodeInfo.setId(nodeId);
        signSysNodeInfo.setParentNodeId("0");
        signSysNodeInfo.setNodeName(enterpriseStepOneReqVO.getChineseName());
        signSysNodeInfo.setGmtCreate(new Date());

        int count = signSysNodeInfoMapper.insert(signSysNodeInfo);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1027);
        }

        //保存企业数据
        SignSysEnterpriseInfo signSysEnterpriseInfo = new SignSysEnterpriseInfo();
        BeanUtils.copyProperties(enterpriseStepOneReqVO,signSysEnterpriseInfo);
        signSysEnterpriseInfo.setId(enterpriseId);
        signSysEnterpriseInfo.setNodeId(nodeId);
        signSysEnterpriseInfo.setGmtCreate(new Date());
        count = signSysEnterpriseInfoMapper.insert(signSysEnterpriseInfo);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1026);
        }

        //保存账户数据-(单位管理员，用于后台登录)
        SignSysAdminUser signSysAdminUserInfo = new SignSysAdminUser();
        signSysAdminUserInfo.setId(SnowflakeIdWorker.getIdToStr());
        signSysAdminUserInfo.setEnterpriseId(enterpriseId);
        signSysAdminUserInfo.setUserName(userName);
        signSysAdminUserInfo.setUserNickName(userName);
        signSysAdminUserInfo.setRemark(userName);
        signSysAdminUserInfo.setPowerLevel("01");
        signSysAdminUserInfo.setCreateUser("创建单位时生成");
        signSysAdminUserInfo.setPassword(BCrypt.hashpw(password,BCrypt.gensalt()));
        signSysAdminUserInfo.setCreateDate(new Date());
        signSysAdminUserInfo.setUpdateDate(new Date());
        signSysAdminUserInfo.setUpdateUser("admin");
        signSysAdminUserInfo.setIsDeleted(0);
        count =signSysAdminUserMapper.insert(signSysAdminUserInfo);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1028);
        }
        //删除缓存数据
        redisService.delete(RedisKeyConstant.CREATE_ENTERPRISE_STEP_ONE_PRE+enterpriseId);

        //邮箱注册成功后发送邮件提醒
        if("02".equals(userNameType)){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email",userName);
                log.info("注册成功发送邮件提醒:{}",jsonObject.toJSONString());
                stringRedisTemplate.convertAndSend("register",jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
                log.error(userName+"注册成功，发送邮件失败,原因：{}",e.getMessage());
            }
        }
        return IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<Boolean> updateEnterprise(UpdateEnterpriseReqVO reqVO) {
        String enterpriseId = reqVO.getEnterpriseId();
        String chineseName =reqVO.getChineseName();
        String creditCode = reqVO.getCreditCode();
        QueryWrapper<SignSysEnterpriseInfo> queryWrapper = new QueryWrapper<SignSysEnterpriseInfo>().eq("chinese_name",chineseName);
        SignSysEnterpriseInfo dbSignSysEnterpriseInfo =signSysEnterpriseInfoMapper.selectOne(queryWrapper);
        if(dbSignSysEnterpriseInfo!=null && !dbSignSysEnterpriseInfo.getId().equals(enterpriseId)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1019);
        }
        queryWrapper = new QueryWrapper<SignSysEnterpriseInfo>().eq("credit_code",creditCode);
        dbSignSysEnterpriseInfo =signSysEnterpriseInfoMapper.selectOne(queryWrapper);
        if(dbSignSysEnterpriseInfo!=null && !dbSignSysEnterpriseInfo.getId().equals(enterpriseId)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1020);
        }

        SignSysEnterpriseInfo signSysEnterpriseInfo =signSysEnterpriseInfoMapper.selectById(enterpriseId);
        if(signSysEnterpriseInfo ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        BeanUtils.copyProperties(reqVO,signSysEnterpriseInfo);
        signSysEnterpriseInfo.setGmtModified(new Date());
        int count =signSysEnterpriseInfoMapper.updateById(signSysEnterpriseInfo);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1029);
        }

        //如果单位名称变了，顶级节点也要变化
        String nodeId = signSysEnterpriseInfo.getNodeId();
        SignSysNodeInfo signSysNodeInfo = new SignSysNodeInfo();
        signSysNodeInfo.setId(nodeId);
        signSysNodeInfo.setNodeName(chineseName);
        signSysNodeInfo.setGmtModified(new Date());
        signSysNodeInfoMapper.updateById(signSysNodeInfo);
        return IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<EnterprsieBatchImportRespVO> enterprsieBatchImport(MultipartFile enterpriseFile, HttpServletResponse response){

        String fileName = enterpriseFile.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        if(index ==-1){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1039);
        }

        String format = fileName.substring(index+1,fileName.length());
        if(!"xlsx".equals(format) &&  !"xls".equals(format)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1039);
        }

        InputStream input=null;
        try {
            input = enterpriseFile.getInputStream();
        }catch (IOException e){
            throw new BusinessException("500","文件IO异常");
        }
        List<ExportEnterpriseReqVO> enterpriseData = readEnterpriseFile(input,format);

        int successNum = 0;
        int failNum = 0;
        String errMsg = "";
        for(ExportEnterpriseReqVO importEnterprise:enterpriseData){
            try {
                insertEnterpriseAndAccount(importEnterprise);
                successNum++;
            }catch (BusinessException e){
                failNum++;
                errMsg += "添加第"+importEnterprise.getLineNum()+"行时提示:" +e.getDetailMessage()+"\r\n";
            }
        }
        //封装导入的结构，放入redis方便后面下载
        BatchImportResultRespVO batchImportResult = new BatchImportResultRespVO();
        String importId = SnowflakeIdWorker.getIdToStr();
        batchImportResult.setImportId(importId);
        batchImportResult.setErr_msg(errMsg);
        batchImportResult.setSuccess_num(successNum);
        batchImportResult.setFail_num(failNum);
        redisService.set(importId, JSON.toJSONString(batchImportResult),60);

        StringBuffer desStr =new StringBuffer("已提交"+(successNum+failNum)+"条数据");
        if(successNum>0){
            desStr.append(",成功导入"+successNum+"条");
        }
        if(failNum>0){
            desStr.append("，其中"+failNum+"条数据异常无法导入");
        }
        String desc =desStr.toString();
        EnterprsieBatchImportRespVO respVO = new EnterprsieBatchImportRespVO();
        respVO.setImportId(importId);
        respVO.setDesc(desc);
        return IyinResult.getIyinResult(respVO);
    }

    @Override
    public IyinResult<Boolean> downloadBatchImportResult(String importId, HttpServletResponse response) {

        String json = redisService.get(importId);
        if(StringUtils.isBlank(json)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        BatchImportResultRespVO batchImportResultRespVO = JSON.parseObject(json,BatchImportResultRespVO.class);

        OutputStream responOut=null;
        try{
            StringBuilder builder = new StringBuilder("");
            builder.append("成功条数:"+batchImportResultRespVO.getSuccess_num()+"\r\n");
            builder.append("失败条数:"+batchImportResultRespVO.getFail_num()+"\r\n");
            builder.append("详情:\r\n"+batchImportResultRespVO.getErr_msg());
            byte[] resByte = builder.toString().getBytes("UTF-8");
            //返回文件
            String fileName ="importEnterprise.txt";
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition","attachmen;filename="+fileName);
            responOut = response.getOutputStream();
            responOut.write(resByte,0,resByte.length);
            response.flushBuffer();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(responOut!=null){
                    responOut.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<Boolean> downloadUserExeclSamples(HttpServletResponse response) {
        //声明一个工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = hssfWorkbook.createSheet();
        //设置表格宽度为18
        sheet.setDefaultColumnWidth(18);
        HSSFRow row1 = sheet.createRow(0);

        //表头
        String[] headers = {  "用户名","账号(手机或邮箱)","密码"};
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row1.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        String fileName ="import.xls";
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition","attachmen;filename="+fileName);
        try {
            response.flushBuffer();
            hssfWorkbook.write(response.getOutputStream());
        }catch (IOException e){
            throw new BusinessException("500","下载模板失败");
        }
        return IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<EnterprsieBatchImportRespVO> userBatchImport(MultipartFile userFile, String nodeId, HttpServletResponse response) {
        String fileName = userFile.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        if(index ==-1){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1039);
        }

        String format = fileName.substring(index+1,fileName.length());
        if(!"xlsx".equals(format) &&  !"xls".equals(format)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1039);
        }

        InputStream input=null;
        try {
            input = userFile.getInputStream();
        }catch (IOException e){
            throw new BusinessException("500","文件IO异常");
        }
        List<ExportUserReqVO> userDatas = readUserFile(input,format);

        int successNum = 0;
        int failNum = 0;
        String errMsg = "";

        //获取根节点
        String rootNodeId = signSysNodeInfoService.getRootNodeId(nodeId);

        QueryWrapper<SignSysEnterpriseInfo> queryWrapper = new QueryWrapper<SignSysEnterpriseInfo>()
                .eq("node_id",rootNodeId);
        SignSysEnterpriseInfo signSysEnterpriseInfo = signSysEnterpriseInfoMapper.selectOne(queryWrapper);
        if(signSysEnterpriseInfo==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1032);
        }
        String enterpriseId = signSysEnterpriseInfo.getId();

        for(ExportUserReqVO importUser:userDatas){
            try {
                insertAccount(importUser,nodeId,enterpriseId);
                successNum++;
            }catch (BusinessException e){
                failNum++;
                errMsg += "添加第"+importUser.getLineNum()+"行时提示:" +e.getDetailMessage()+"\r\n";
            }
        }
        //封装导入的结构，放入redis方便后面下载
        BatchImportResultRespVO batchImportResult = new BatchImportResultRespVO();
        String importId = SnowflakeIdWorker.getIdToStr();
        batchImportResult.setImportId(importId);
        batchImportResult.setErr_msg(errMsg);
        batchImportResult.setSuccess_num(successNum);
        batchImportResult.setFail_num(failNum);
        redisService.set(importId,JSON.toJSONString(batchImportResult),60);

        StringBuffer desStr =new StringBuffer("已提交"+(successNum+failNum)+"条数据");
        if(successNum>0){
            desStr.append(",成功导入"+successNum+"条");
        }
        if(failNum>0){
            desStr.append("，其中"+failNum+"条数据异常无法导入");
        }
        String desc =desStr.toString();
        EnterprsieBatchImportRespVO respVO = new EnterprsieBatchImportRespVO();
        respVO.setImportId(importId);
        respVO.setDesc(desc);
        return IyinResult.getIyinResult(respVO);
    }

    @Override
    public IyinResult<Boolean> downloadUserBatchImportResult(String importId, HttpServletResponse response) {
        String json = redisService.get(importId);
        if(StringUtils.isBlank(json)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        BatchImportResultRespVO batchImportResultRespVO = JSON.parseObject(json,BatchImportResultRespVO.class);

        OutputStream responOut=null;
        try{
            StringBuilder builder = new StringBuilder("");
            builder.append("成功条数:"+batchImportResultRespVO.getSuccess_num()+"\r\n");
            builder.append("失败条数:"+batchImportResultRespVO.getFail_num()+"\r\n");
            builder.append("详情:\r\n"+batchImportResultRespVO.getErr_msg());
            byte[] resByte = builder.toString().getBytes("UTF-8");
            //返回文件
            String fileName ="importEnterprise.txt";
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition","attachmen;filename="+fileName);
            responOut = response.getOutputStream();
            responOut.write(resByte,0,resByte.length);
            response.flushBuffer();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(responOut!=null){
                    responOut.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return IyinResult.getIyinResult(true);
    }


    @Override
    public IyinResult<Boolean> downloadExeclSamples(HttpServletResponse response) {

        //声明一个工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = hssfWorkbook.createSheet();
        //设置表格宽度为18
        sheet.setDefaultColumnWidth(18);
        HSSFRow row1 = sheet.createRow(0);

        //表头
        String[] headers = { "单位名称", "统一社会信用代码", "法人名称","用户名(手机或邮箱)","密码"};
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row1.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        String fileName ="import.xls";
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition","attachmen;filename="+fileName);
        try {
            response.flushBuffer();
            hssfWorkbook.write(response.getOutputStream());
        }catch (IOException e){
            throw new BusinessException("500","下载模板失败");
        }
        return IyinResult.getIyinResult(true);
    }


    private void insertEnterpriseAndAccount(ExportEnterpriseReqVO importEnterprise) {
        String enterpriseName = importEnterprise.getEnterpriseName();
        String creditcode = importEnterprise.getCreditCode();

        QueryWrapper<SignSysEnterpriseInfo> queryWrapper = new QueryWrapper<SignSysEnterpriseInfo>()
                .eq("chinese_name",enterpriseName);
        if(signSysEnterpriseInfoMapper.selectOne(queryWrapper)!=null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1019);
        }

        queryWrapper = new QueryWrapper<SignSysEnterpriseInfo>()
                .eq("credit_code",creditcode);
        if(signSysEnterpriseInfoMapper.selectOne(queryWrapper)!=null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1020);
        }

        String userName = importEnterprise.getUserName();
        boolean isMobile =ParamValidateLegalUtil.isMobile(userName);
        boolean isEmail = ParamValidateLegalUtil.isEmail(userName);
        if(!isMobile && !isEmail){
            throw new BusinessException("1023","手机号或邮箱格式不正确");
        }
        //判断账户名是否被占用了
        QueryWrapper<SignSysUserInfo> queryWrapperUser = new QueryWrapper<SignSysUserInfo>().eq("user_name",userName);
        SignSysUserInfo dbSignSysUser = signSysUserInfoMapper.selectOne(queryWrapperUser);
        if(dbSignSysUser!=null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1025);
        }

        //保存组织结构数据
        SignSysNodeInfo signSysNodeInfo = new SignSysNodeInfo();
        String nodeId = SnowflakeIdWorker.getIdToStr();
        signSysNodeInfo.setId(nodeId);
        signSysNodeInfo.setParentNodeId("0");
        signSysNodeInfo.setNodeName(enterpriseName);
        signSysNodeInfo.setGmtCreate(new Date());
        int count = signSysNodeInfoMapper.insert(signSysNodeInfo);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1027);
        }

        //保存企业数据
        SignSysEnterpriseInfo signSysEnterpriseInfo = new SignSysEnterpriseInfo();
        String enterpriseId =SnowflakeIdWorker.getIdToStr();
        BeanUtils.copyProperties(importEnterprise,signSysEnterpriseInfo);
        signSysEnterpriseInfo.setId(enterpriseId);
        signSysEnterpriseInfo.setChineseName(importEnterprise.getEnterpriseName());
        signSysEnterpriseInfo.setNodeId(nodeId);
        signSysEnterpriseInfo.setGmtCreate(new Date());
        count = signSysEnterpriseInfoMapper.insert(signSysEnterpriseInfo);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1026);
        }

        //保存账户数据
        SignSysUserInfo signSysUserInfo = new SignSysUserInfo();
        signSysUserInfo.setId(SnowflakeIdWorker.getIdToStr());
        signSysUserInfo.setEnterpriseOrPersonalId(enterpriseId);
        signSysUserInfo.setUserType(UserTypeEnum.ENTERPRISE_USER.getCode());
        signSysUserInfo.setFirstCreate("01");
        signSysUserInfo.setUserName(userName);
        signSysUserInfo.setUserNickName(userName);
        signSysUserInfo.setNodeId(nodeId);
        signSysUserInfo.setLoginType(isMobile?"01":"02");
        signSysUserInfo.setPassword(BCrypt.hashpw(importEnterprise.getPassword(),BCrypt.gensalt()));
        signSysUserInfo.setGmtCreate(new Date());
        signSysUserInfo.setSource(UserSourceEnum.ADMIN_CREATE.getCode());
        count =signSysUserInfoMapper.insert(signSysUserInfo);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1028);
        }
    }

    private void insertAccount(ExportUserReqVO importUser, String nodeId, String enterpriseId) {
        String userName = importUser.getUserName();
        String userNickName = importUser.getUserNickName();
        boolean isMobile =ParamValidateLegalUtil.isMobile(userName);
        boolean isEmail = ParamValidateLegalUtil.isEmail(userName);
        if(!isMobile && !isEmail){
            throw new BusinessException("1023","手机号或邮箱格式不正确");
        }
        //判断账户名是否被占用了
        QueryWrapper<SignSysUserInfo> queryWrapperUser = new QueryWrapper<SignSysUserInfo>().eq("user_name",userName);
        SignSysUserInfo dbSignSysUser = signSysUserInfoMapper.selectOne(queryWrapperUser);
        if(dbSignSysUser!=null && dbSignSysUser.getIsDeleted() ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1025);
        }

        //保存账户数据
        if(dbSignSysUser==null){
            SignSysUserInfo signSysUserInfo = new SignSysUserInfo();
            String userId =SnowflakeIdWorker.getIdToStr();
            signSysUserInfo.setId(userId);
            signSysUserInfo.setEnterpriseOrPersonalId(enterpriseId);
            signSysUserInfo.setUserType(UserTypeEnum.ENTERPRISE_USER.getCode());
            signSysUserInfo.setFirstCreate("02");
            signSysUserInfo.setUserName(userName);
            signSysUserInfo.setUserNickName(userNickName);
            signSysUserInfo.setNodeId(nodeId);
            signSysUserInfo.setLoginType(isMobile?"01":"02");
            signSysUserInfo.setPassword(BCrypt.hashpw(importUser.getPassword(),BCrypt.gensalt()));
            signSysUserInfo.setGmtCreate(new Date());
            signSysUserInfo.setSource(UserSourceEnum.ADMIN_CREATE.getCode());
            int  count =signSysUserInfoMapper.insert(signSysUserInfo);
            if(count ==0){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1028);
            }

            //给与用户初始化的角色
            SignSysUserRole signSysUserRole  = new SignSysUserRole();
            signSysUserRole.setId(SnowflakeIdWorker.getIdToStr());
            signSysUserRole.setRoleId("10000");
            signSysUserRole.setUserId(userId);
            signSysUserRole.setGmtCreate(new Date());
            signSysUserRole.setIsDeleted(0);
            signSysUserRoleMapper.insert(signSysUserRole);

            //初始化用户数据权限
            SignSysUserDataLimit signSysUserDataLimit = new SignSysUserDataLimit();
            signSysUserDataLimit.setId(SnowflakeIdWorker.getIdToStr());
            signSysUserDataLimit.setUserId(userId);
            signSysUserDataLimit.setGmtCreate(new Date());
            signSysUserDataLimit.setIsDeleted(0);
            signSysUserDataLimit.setLimitType("1");
            signSysUserDataLimitMapper.insert(signSysUserDataLimit);

        }else {
            dbSignSysUser.setEnterpriseOrPersonalId(enterpriseId);
            dbSignSysUser.setUserNickName(userNickName);
            dbSignSysUser.setNodeId(nodeId);
            dbSignSysUser.setPassword(BCrypt.hashpw(importUser.getPassword(),BCrypt.gensalt()));
            dbSignSysUser.setIsDeleted(0);
            dbSignSysUser.setGmtModified(new Date());
            int  count =signSysUserInfoMapper.updateById(dbSignSysUser);
            if(count ==0){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1028);
            }
        }

    }


    public  List<ExportEnterpriseReqVO> readEnterpriseFile(InputStream is,String format) {
        Workbook wb =null;

        try {
            //支持 xlsx
            if("xlsx".equals(format)){
                wb = new XSSFWorkbook(is);
            }else if("xls".equals(format)){
                wb = new HSSFWorkbook(is);
            }else {
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1039);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("500","无法解析文件");
        }finally {
            if(is!=null){
                try {
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        Sheet sheet =  wb.getSheetAt(0);

        // 得到总行数
        int rowNum = sheet.getLastRowNum();

        //可能由于格式问题导致的读取不准确
        rowNum+=5;
        Row row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();

        //导入单位的表头有5列表
        colNum = colNum>=5?5:colNum;

        //总行数有可能由于格式原因导致不准确，这里进行纠正
        int realRowNum=1;
        for (int i = 1; i < rowNum; i++) {
            row = sheet.getRow(i);
            if(isEmpltyRow(row,colNum)){
               break;
            }
            realRowNum+=1;
        }

        //用真正的行数进行处理
        for(int i=1;i<realRowNum;i++){
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum) {
                Cell cell = row.getCell(j);
                if((cell ==null || StringUtils.isEmpty(cell.toString()))  && !isEmpltyRow(row,colNum)){
                    log.error("第{}行的第{}列为空",i+1,j+1);
                    throw new BusinessException("500","第"+(i+1)+"行的第"+(j+1)+"列为空");
                }
                j++;
            }
        }


        //封装数据 正文内容应该从第二行开始,第一行为表头的标题
        List<ExportEnterpriseReqVO> exportEnterpriseReqVOS = new ArrayList<ExportEnterpriseReqVO>();
        for(int i=1;i<realRowNum;i++){
            row =sheet.getRow(i);
            if(isEmpltyRow(row,colNum)){
                continue;
            }
            int k=0;
            ExportEnterpriseReqVO exportEnterpriseReqVO =new ExportEnterpriseReqVO();
            while (k<colNum){
                row.getCell(k).setCellType(Cell.CELL_TYPE_STRING);
                String value = row.getCell(k).getStringCellValue();
                exportEnterpriseReqVO.setLineNum(i+1);
                if(k==0){
                    exportEnterpriseReqVO.setEnterpriseName(value);
                }else if(k==1){
                    exportEnterpriseReqVO.setCreditCode(value);
                }else if(k==2){
                    exportEnterpriseReqVO.setLegalName(value);
                } else if(k==3){
                    exportEnterpriseReqVO.setUserName(value);
                } else if(k==4){
                    exportEnterpriseReqVO.setPassword(value);
                    exportEnterpriseReqVOS.add(exportEnterpriseReqVO);
                }
                k++;
            }
        }

        return exportEnterpriseReqVOS;
    }

    public  List<ExportUserReqVO> readUserFile(InputStream is,String format) {
        Workbook wb =null;

        try {
            //支持 xlsx
            if("xlsx".equals(format)){
                wb = new XSSFWorkbook(is);
            }else if("xls".equals(format)){
                wb = new HSSFWorkbook(is);
            }else {
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1039);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("500","无法解析文件");
        }finally {
            if(is!=null){
                try {
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        Sheet sheet =  wb.getSheetAt(0);

        // 最后一行行标，比行数小1
        int rowNum = sheet.getLastRowNum();
        //可能由于格式问题导致的读取不准确
        rowNum+=5;

        Row row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        //导入用户的表头有3列表
        colNum = colNum>3?3:colNum;

        //总行数有可能由于格式原因导致不准确，这里进行纠正
        int realRowNum=1;
        for (int i = 1; i < rowNum; i++) {
            row = sheet.getRow(i);
            if(isEmpltyRow(row,colNum)){
                break;
            }
            realRowNum+=1;
        }

        for(int i=1;i<realRowNum;i++){
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum) {
                Cell cell = row.getCell(j);
                if((cell ==null || StringUtils.isEmpty(cell.toString()))  && !isEmpltyRow(row,colNum)){
                    log.error("第{}行的第{}列为空",i+1,j+1);
                    throw new BusinessException("500","第"+(i+1)+"行的第"+(j+1)+"列为空");
                }
                j++;
            }
        }

        //封装数据 正文内容应该从第二行开始,第一行为表头的标题
        List<ExportUserReqVO> exportUserReqVOs = new ArrayList<ExportUserReqVO>();
        for(int i=1;i<realRowNum;i++){
            row =sheet.getRow(i);
            if(isEmpltyRow(row,colNum)){
                continue;
            }
            int k=0;
            ExportUserReqVO exportUserReqVO =new ExportUserReqVO();
            while (k<colNum){
                row.getCell(k).setCellType(Cell.CELL_TYPE_STRING);
                String value = row.getCell(k).getStringCellValue();
                exportUserReqVO.setLineNum(i+1);
                if(k==0){
                    exportUserReqVO.setUserNickName(value);
                }else if(k==1){
                    exportUserReqVO.setUserName(value);
                }else if(k==2){
                    exportUserReqVO.setPassword(value);
                    exportUserReqVOs.add(exportUserReqVO);
                }
                k++;
            }
        }

        return exportUserReqVOs;
    }


    /**
     * 判断是否为空行
     * @param row
     * @param line
     * @return
     */
    public boolean isEmpltyRow(Row row,int line){
        boolean flag=true;
        if(row ==null){
            return true;
        }
        for(int i=0;i<line;i++){
            Cell cell = row.getCell(i);
            if(cell!=null && !StringUtils.isEmpty(cell.toString())){
                flag =false;
                break;
            }
        }
        return flag;
    }
}
