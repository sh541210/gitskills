package com.iyin.sign.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.dto.req.SignSysFilePrintDTO;
import com.iyin.sign.system.entity.SignSysFilePrint;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.PrintSummaryReqVO;
import com.iyin.sign.system.vo.req.SignSysContractPrintReq;
import com.iyin.sign.system.vo.req.SignSysFilePrintReq;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 * 文件打印记录表 服务类
 * </p>
 *
 * @author wdf
 * @since 2019-07-19
 */
public interface ISignSysFilePrintService extends IService<SignSysFilePrint> {

    /**
    	*  后台用户添加打印日志 并输出文件流
    	* @Author: wdf 
        * @CreateDate: 2019/7/19 19:49
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/7/19 19:49
    	* @Version: 0.0.1
        * @param signSysFilePrintReq
        * @return com.iyin.sign.system.model.InMemoryMultipartFile
        */
    InMemoryMultipartFile addFilePrint(SignSysFilePrintReq signSysFilePrintReq, HttpServletRequest request);

    /**
    	* 打印日志列表
    	* @Author: wdf
        * @CreateDate: 2019/7/22 11:40
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/7/22 11:40
    	* @Version: 0.0.1
        * @param currPage, pageSize, fileCode
        * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.dto.req.SignSysFilePrintDTO>>
        */
	IyinResult<IyinPage<SignSysFilePrintDTO>> getFilePrintList(Integer currPage, Integer pageSize, String fileCode, HttpServletRequest request);

	/**
	 * 合同打印日志 并输出文件流
	 *
	 * @Author: yml
	 * @CreateDate: 2019/9/3
	 * @UpdateUser: yml
	 * @UpdateDate: 2019/9/3
	 * @Version: 0.0.1
	 * @param sysContractPrintReq
	 * @param request
	 * @return : com.iyin.sign.system.model.InMemoryMultipartFile
	 */
    InMemoryMultipartFile addFilePrint2(SignSysContractPrintReq sysContractPrintReq, HttpServletRequest request);

	/**
	 * 打印摘要
	 *
	 * @Author: yml
	 * @CreateDate: 2019/9/23
	 * @UpdateUser: yml
	 * @UpdateDate: 2019/9/23
	 * @Version: 0.0.1
	 * @param printSummaryReqVO
	 * @param request
	 * @return : java.lang.Object
	 */
	InMemoryMultipartFile printSummary(PrintSummaryReqVO printSummaryReqVO, HttpServletRequest request) throws IOException;
}
