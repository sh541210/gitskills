package com.iyin.sign.system.service;

import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.vo.sign.req.TemplateSignReqVO;
import com.iyin.sign.system.vo.sign.resp.SignRespDTO;

import javax.servlet.http.HttpServletRequest;

/**
	* 模板签
	* @Author: wdf
    * @CreateDate: 2019/9/26 18:40
	* @UpdateUser: wdf
    * @UpdateDate: 2019/9/26 18:40
	* @Version: 0.0.1
    * @param
    * @return
    */
public interface ITemplateSignService {

    /**
    	* 模板签章
    	* @Author: wdf 
        * @CreateDate: 2019/9/26 16:35
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/9/26 16:35
    	* @Version: 0.0.1
        * @param templateSignReqVO, request
        * @return com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
        */
    EsealResult<SignRespDTO> templateSign(TemplateSignReqVO templateSignReqVO, HttpServletRequest request);
}
