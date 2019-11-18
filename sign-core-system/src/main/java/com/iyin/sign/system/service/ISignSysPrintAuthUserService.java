package com.iyin.sign.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysPrintAuthUser;
import com.iyin.sign.system.vo.req.SignSysContractPrintAddReqVO;
import com.iyin.sign.system.vo.req.SignSysPrintAuthUserAddReqVO;
import com.iyin.sign.system.vo.req.SignSysPrintAuthUserDelReqVO;
import com.iyin.sign.system.vo.resp.SignSysPrintAuthUserListRespVO;
import com.iyin.sign.system.vo.resp.SignSysPrintAuthUserRespVO;
import com.iyin.sign.system.vo.resp.SignSysPrintInfoRespVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 打印分配表 服务类
 * </p>
 *
 * @author wdf
 * @since 2019-08-06
 */
public interface ISignSysPrintAuthUserService extends IService<SignSysPrintAuthUser> {

    /**
    	* 批量新增
    	* @Author: wdf 
        * @CreateDate: 2019/8/6 14:05
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/6 14:05
    	* @Version: 0.0.1
        * @param signSysPrintAuthUserAddReqVO, request
        * @return java.lang.Boolean
        */
    Boolean addPrintAuthUser(SignSysPrintAuthUserAddReqVO signSysPrintAuthUserAddReqVO, HttpServletRequest request);

    /**
    	* 获取已分配对象
    	* @Author: wdf 
        * @CreateDate: 2019/8/6 14:39
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/8/6 14:39
    	* @Version: 0.0.1
        * @param fileCode
        * @return java.util.List<com.iyin.sign.system.vo.resp.SignSysPrintAuthUserRespVO>
        */
	SignSysPrintAuthUserListRespVO getPrintAuthList(String fileCode);

	int delPrintAuthUser(SignSysPrintAuthUserDelReqVO signSysPrintAuthUserDelReqVO, HttpServletRequest request);

	/**
	 * 获取已分配对象与次数
	 *
	 * @Author: yml
	 * @CreateDate: 2019/9/3
	 * @UpdateUser: yml
	 * @UpdateDate: 2019/9/3
	 * @Version: 0.0.1
	 * @param fileCode
	 * @return :
	 */
	SignSysPrintInfoRespVO getPrintAuthList2(String fileCode);

	/**
	 * 合同添加打印分配
	 *
	 * @Author: yml
	 * @CreateDate: 2019/9/4
	 * @UpdateUser: yml
	 * @UpdateDate: 2019/9/4
	 * @Version: 0.0.1
	 * @param contractPrintAddReqVO
	 * @param request
	 * @return : java.lang.Object
	 */
	Boolean addPrintAuthUser2(SignSysContractPrintAddReqVO contractPrintAddReqVO, HttpServletRequest request);
}
