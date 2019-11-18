package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iyin.sign.system.common.enums.SignatureEnum;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.common.exception.ServiceException;
import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.entity.SignSysTemplateField;
import com.iyin.sign.system.entity.SysSignApplication;
import com.iyin.sign.system.mapper.ISignSysTemplateFieldMapper;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.model.exception.IyinResultValidUtil;
import com.iyin.sign.system.service.IAuthTokenService;
import com.iyin.sign.system.service.IFastSignService;
import com.iyin.sign.system.service.IFileService;
import com.iyin.sign.system.service.ITemplateSignService;
import com.iyin.sign.system.util.Base64Util;
import com.iyin.sign.system.vo.sign.req.*;
import com.iyin.sign.system.vo.sign.resp.SignRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TemplateSignServiceImpl
 * TemplateSignServiceImpl
 * @Author wdf
 * @Date 2019/9/26 18:41
 * @throws
 * @Version 1.0
 **/
@Service
@Slf4j
public class TemplateSignServiceImpl implements ITemplateSignService {

    private static final String API_TOKEN = "api_token";
    private static final String SESSION_TOKEN = "session_token";
    private static final String SINGLE_API_TIME = "签章API总时间";
    private static final String SIGN_EXCEPTION = "签章异常{}";

    @Autowired
    private IFileService fileService;

    @Autowired
    private IAuthTokenService authTokenService;

    @Autowired
    private IFastSignService fastSignService;

    @Resource
    private ISignSysTemplateFieldMapper signSysTemplateFieldMapper;

    private SysSignApplication getAppInfo(HttpServletRequest request) throws ServiceException {
        log.debug("TemplateSignServiceImpl getAppInfo");
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

    @Override
    public EsealResult<SignRespDTO> templateSign(TemplateSignReqVO templateSignReqVO, HttpServletRequest request) {
        try {
            long time = System.currentTimeMillis();
            String appId = "WEB";
            SysSignApplication appInfo = getAppInfo(request);
            boolean flag = null != appInfo;
            appId = flag && null != appInfo.getUserAppId() ? appInfo.getUserAppId() : appId;
            if (!flag) {
                throw new ServiceException(ErrorCode.REQUEST_40307);
            }
            // 获取pdf签章文件
            if (null == templateSignReqVO.getPdfBase64Str()) {
                InMemoryMultipartFile inMemoryMultipartFile = fileService.fetch(templateSignReqVO.getFileCode());
                if (StringUtils.isBlank(templateSignReqVO.getOriginalFileName())) {
                    templateSignReqVO.setOriginalFileName(inMemoryMultipartFile.getOriginalFilename());
                }
                templateSignReqVO.setPdfBase64Str(Base64Util.encode(inMemoryMultipartFile.getBytes()));
                String[] sealIds=templateSignReqVO.getSealCodes().split(",");
                List<SignSysTemplateField> tempFieldList=signSysTemplateFieldMapper.selectList(new QueryWrapper<SignSysTemplateField>().eq("template_id",templateSignReqVO.getTemplateId()).eq("is_deleted",0));
                if(tempFieldList.isEmpty()){
                    throw new BusinessException(ErrorCode.REQUEST_40481);
                }
                if(sealIds.length!=tempFieldList.size()){
                    throw new BusinessException(ErrorCode.REQUEST_40482);
                }
                SignSysTemplateField signSysTemplateField=tempFieldList.get(0);
                EsealResult<SignRespDTO> respResult = null;

                respResult = getSignRespEsealResult(templateSignReqVO, request, sealIds, tempFieldList, signSysTemplateField);
                log.info(SINGLE_API_TIME + (System.currentTimeMillis() - time));
                return respResult;
            }
            log.info(SINGLE_API_TIME + (System.currentTimeMillis() - time));
            return null;
        } catch (Exception e) {
            log.error(SIGN_EXCEPTION, e.getMessage());
            throw new BusinessException(ErrorCode.REQUEST_40472.getCode(), e.getMessage());
        }
    }

    /**
    	* 模板签章参数组装
    	* @Author: wdf 
        * @CreateDate: 2019/9/27 11:06
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/9/27 11:06
    	* @Version: 0.0.1
        * @param templateSignReqVO, request, sealIds, tempFieldList, signSysTemplateField
        * @return com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
        */
    private EsealResult<SignRespDTO> getSignRespEsealResult(TemplateSignReqVO templateSignReqVO, HttpServletRequest request, String[] sealIds, List<SignSysTemplateField> tempFieldList, SignSysTemplateField signSysTemplateField) {
        EsealResult<SignRespDTO> respResult;
        if(SignatureEnum.SIGNATURE_SINGLE.getCode().equals(signSysTemplateField.getSignatureMethod())){
            //单页签章参数组装
            SingleFastSignReqVO singleFastSignReqVO =new SingleFastSignReqVO();
            singleFastSignReqVO.setPdfBase64Str(templateSignReqVO.getPdfBase64Str());
            singleFastSignReqVO.setOriginalFileName(templateSignReqVO.getOriginalFileName());
            singleFastSignReqVO.setSignatureMethod(SignatureEnum.SIGNATURE_COORDINATE.getCode());
            singleFastSignReqVO.setUserId(templateSignReqVO.getUserId());
            singleFastSignReqVO.setFileCode(templateSignReqVO.getFileCode());
            List<SingleFastParamSignVO> list=new ArrayList<>();
            for(int i=0;i<tempFieldList.size();i++){
                SignSysTemplateField stf=tempFieldList.get(i);
                SingleFastParamSignVO singleFastParamSignVO=new SingleFastParamSignVO();
                singleFastParamSignVO.setSealCode(sealIds[i]);
                singleFastParamSignVO.setFoggy(false);
                singleFastParamSignVO.setGrey(false);
                singleFastParamSignVO.setCoordinateX(String.valueOf(stf.getSignatureCoordinateX()));
                singleFastParamSignVO.setCoordinateY(String.valueOf(stf.getSignatureCoordinateY()));
                singleFastParamSignVO.setPageNo(String.valueOf(stf.getSignatureStartPage()));
                list.add(singleFastParamSignVO);
            }
            singleFastSignReqVO.setList(list);

            respResult=fastSignService.singleSign(singleFastSignReqVO,request);
            IyinResultValidUtil.validateSuccess(respResult);

        }else if(SignatureEnum.SIGNATURE_MORE.getCode().equals(signSysTemplateField.getSignatureMethod())){
            //多页签章参数组装
            SingleFastSignMoreReqVO singleFastSignMoreReqVO=new SingleFastSignMoreReqVO();
            singleFastSignMoreReqVO.setPdfbase64Str(templateSignReqVO.getPdfBase64Str());
            singleFastSignMoreReqVO.setOriginalFileName(templateSignReqVO.getOriginalFileName());
            singleFastSignMoreReqVO.setUserId(templateSignReqVO.getUserId());
            singleFastSignMoreReqVO.setFileCode(templateSignReqVO.getFileCode());
            List<SingleFastCoordBatchSignVO> list=new ArrayList<>();
            for(int i=0;i<tempFieldList.size();i++){
                SignSysTemplateField stf=tempFieldList.get(i);
                SingleFastCoordBatchSignVO singleFastCoordBatchSignVO=new SingleFastCoordBatchSignVO();
                singleFastCoordBatchSignVO.setSealCode(sealIds[i]);
                singleFastCoordBatchSignVO.setFoggy(false);
                singleFastCoordBatchSignVO.setGrey(false);
                singleFastCoordBatchSignVO.setCoordinateX(String.valueOf(stf.getSignatureCoordinateX()));
                singleFastCoordBatchSignVO.setCoordinateY(String.valueOf(stf.getSignatureCoordinateY()));
                singleFastCoordBatchSignVO.setStartPageNo(String.valueOf(stf.getSignatureStartPage()));
                singleFastCoordBatchSignVO.setEndPageNo(String.valueOf(stf.getSignatureEndPage()));
                list.add(singleFastCoordBatchSignVO);
            }
            singleFastSignMoreReqVO.setList(list);

            respResult=fastSignService.singleCoordBatchSign(singleFastSignMoreReqVO,request);
            IyinResultValidUtil.validateSuccess(respResult);

        }else if(SignatureEnum.SIGNATURE_PERFORATION.getCode().equals(signSysTemplateField.getSignatureMethod())){
            //骑缝签章参数组装
            FastPerforationSignReqVO fastPerforationSignReqVO=new FastPerforationSignReqVO();
            fastPerforationSignReqVO.setOriginalFileName(templateSignReqVO.getOriginalFileName());
            fastPerforationSignReqVO.setPdfbase64Str(templateSignReqVO.getPdfBase64Str());
            fastPerforationSignReqVO.setUserId(templateSignReqVO.getUserId());
            fastPerforationSignReqVO.setFileCode(templateSignReqVO.getFileCode());
            List<FastPerforationSignVO> list=new ArrayList<>();
            for(int i=0;i<tempFieldList.size();i++){
                SignSysTemplateField stf=tempFieldList.get(i);
                FastPerforationSignVO fastPerforationSignVO=new FastPerforationSignVO();
                fastPerforationSignVO.setSealCode(sealIds[i]);
                fastPerforationSignVO.setFoggy(false);
                fastPerforationSignVO.setGrey(false);
                fastPerforationSignVO.setCoordinateY(String.valueOf(stf.getSignatureCoordinateY()));
                fastPerforationSignVO.setStartPageNo(String.valueOf(stf.getSignatureStartPage()));
                fastPerforationSignVO.setEndPageNo(String.valueOf(stf.getSignatureEndPage()));
                fastPerforationSignVO.setPageSize(String.valueOf(stf.getCoverPageNum()));
                list.add(fastPerforationSignVO);
            }

            fastPerforationSignReqVO.setList(list);
            respResult=fastSignService.singlePerforationCoordSign(fastPerforationSignReqVO,request);
            IyinResultValidUtil.validateSuccess(respResult);

        }else if(SignatureEnum.SIGNATURE_CONTINUOUS.getCode().equals(signSysTemplateField.getSignatureMethod())){
            //连页签章参数组装
            FastPerforationHalfSignReqVO fastPerforationHalfSignReqVO=new FastPerforationHalfSignReqVO();
            fastPerforationHalfSignReqVO.setOriginalFileName(templateSignReqVO.getOriginalFileName());
            fastPerforationHalfSignReqVO.setPdfbase64Str(templateSignReqVO.getPdfBase64Str());
            fastPerforationHalfSignReqVO.setUserId(templateSignReqVO.getUserId());
            fastPerforationHalfSignReqVO.setFileCode(templateSignReqVO.getFileCode());
            List<FastPerforationHalfSignVO> list=new ArrayList<>();
            for(int i=0;i<tempFieldList.size();i++){
                SignSysTemplateField stf=tempFieldList.get(i);
                FastPerforationHalfSignVO fastPerforationHalfSignVO=new FastPerforationHalfSignVO();
                fastPerforationHalfSignVO.setSealCode(sealIds[i]);
                fastPerforationHalfSignVO.setFoggy(false);
                fastPerforationHalfSignVO.setGrey(false);
                fastPerforationHalfSignVO.setCoordinateY(String.valueOf(stf.getSignatureCoordinateY()));
                fastPerforationHalfSignVO.setStartPageNo(String.valueOf(stf.getSignatureStartPage()));
                fastPerforationHalfSignVO.setEndPageNo(String.valueOf(stf.getSignatureEndPage()));
                fastPerforationHalfSignVO.setSignatureDirection(stf.getSignatureDirection());
                list.add(fastPerforationHalfSignVO);
            }
            fastPerforationHalfSignReqVO.setList(list);

            respResult=fastSignService.singlePerforationCoordHalfSign(fastPerforationHalfSignReqVO,request);
            IyinResultValidUtil.validateSuccess(respResult);

        }else{
            throw new BusinessException(ErrorCode.REQUEST_40483);
        }
        return respResult;
    }
}
