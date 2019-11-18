package com.iyin.sign.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.dto.req.SignSysTemplateDTO;
import com.iyin.sign.system.entity.SignSysTemplate;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO;
import com.iyin.sign.system.vo.req.AddTemplateReqVo;
import com.iyin.sign.system.vo.req.SignSysTemplateGenVO;
import com.iyin.sign.system.vo.req.SignSysTemplateVO;
import com.iyin.sign.system.vo.resp.TemplateRespDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 模板表 服务类
 * </p>
 *
 * @author wdf
 * @since 2019-07-15
 */
public interface ISignSysTemplateService extends IService<SignSysTemplate> {

    /**
    	* 获取模板列表
    	* @Author: wdf 
        * @CreateDate: 2019/7/19 10:49
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/7/29 10:49
    	* @Version: 0.0.1
        * @param currPage, pageSize, applicationName
        * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.dto.req.SignSysTemplateDTO>>
        */
    IyinResult<IyinPage<SignSysTemplateDTO>> getTemplateList(Integer currPage, Integer pageSize, String applicationName,HttpServletRequest request);
    /**
    	* 修改模板
    	* @Author: wdf 
        * @CreateDate: 2019/7/19 10:49
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/7/29 10:49
    	* @Version: 0.0.1
        * @param signSysTemplate
        * @return int
        */
    int updateTemplate(SignSysTemplate signSysTemplate, HttpServletRequest request);
    /**
    	* 模板生成
    	* @Author: wdf 
        * @CreateDate: 2019/7/19 10:49
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/7/29 10:49
    	* @Version: 0.0.1
        * @param signSysTemplateGenVO
        * @return int
        */
    int genTemplate(SignSysTemplateGenVO signSysTemplateGenVO, HttpServletRequest request);
    /**
    	* 添加模板
    	* @Author: wdf 
        * @CreateDate: 2019/7/19 10:49
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/7/29 10:49
    	* @Version: 0.0.1
        * @param signSysTemplateVO
        * @return int
        */
    int addTemplate(AddTemplateReqVo signSysTemplateVO, HttpServletRequest request);
    /**
    	* 添加模板并返回模板信息
    	* @Author: wdf 
        * @CreateDate: 2019/7/19 10:49
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/7/29 10:49
    	* @Version: 0.0.1
        * @param signSysTemplateVO
        * @return com.iyin.sign.system.entity.SignSysTemplate
        */
    SignSysTemplate addAppBackInfo(SignSysTemplateVO signSysTemplateVO, HttpServletRequest request);
    /**
    	* 获取模板信息
    	* @Author: wdf 
        * @CreateDate: 2019/7/19 10:49
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/7/29 10:49
    	* @Version: 0.0.1
        * @param signSysTemplateDTO
        * @return com.iyin.sign.system.entity.SignSysTemplate
        */
    SignSysTemplate getTemplateById(SignSysTemplateDTO signSysTemplateDTO);

    /**
    	* 导入word
    	* @Author: wdf 
        * @CreateDate: 2019/8/14 9:26
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/14 9:26
    	* @Version: 0.0.1
        * @param file, request
        * @return com.iyin.sign.system.entity.SignSysTemplate
        */
    SignSysTemplate importWord(MultipartFile file,HttpServletRequest request);

    /**
     * 模板详情接口-返回控件
     *
     * @Author: yml
     * @CreateDate: 2019/9/26
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/26
     * @Version: 0.0.1
     * @param id
     * @return : com.iyin.sign.system.vo.resp.TemplateRespDTO
     */
    TemplateRespDTO getTemplateM(String id);

	/**
	 * 从模板导入
	 *
	 * @Author: yml
	 * @CreateDate: 2019/9/26
	 * @UpdateUser: yml
	 * @UpdateDate: 2019/9/26
	 * @Version: 0.0.1
	 * @param templateId
	 * @param request
	 * @return : com.iyin.sign.system.vo.file.resp.CompactFileUploadRespDTO
	 */
	CompactFileUploadRespDTO fromTemplate(String templateId, HttpServletRequest request);
}
