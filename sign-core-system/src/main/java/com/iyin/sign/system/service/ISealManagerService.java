package com.iyin.sign.system.service;

import com.iyin.sign.system.dto.req.SignSysUseSealDTO;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.*;
import com.iyin.sign.system.vo.resp.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName 白鹤印章管理
 * ISealManagerService
 * @Author wdf
 * @Date 2019/10/8 17:50
 * @throws
 * @Version 1.0
 **/
public interface ISealManagerService {

    /**
    	* 获取印章列表
    	* @Author: wdf 
        * @CreateDate: 2019/10/9 17:01
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/9 17:01
    	* @Version: 0.0.1
        * @param enableFlag, mac, userId
        * @return com.iyin.sign.system.vo.resp.SealListRespVO
        */
    SealListRespVO sealList(Boolean hasExpired,Boolean enableFlag, HttpServletRequest request);

    /**
    	* 获取文件打印列表
    	* @Author: wdf 
        * @CreateDate: 2019/10/10 17:27
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/10 17:27
    	* @Version: 0.0.1
        * @param applyId
        * @return java.util.List<com.iyin.sign.system.vo.resp.SealFileInfoListRespVO>
        */
	List<SealFileInfoListRespVO> getFilePrintInfoList(String applyId);

	/**
		* 用印申请添加接口
		* @Author: wdf
	    * @CreateDate: 2019/10/10 19:10
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/10/10 19:10
		* @Version: 0.0.1
	    * @param addUseSealReqVO, request
	    * @return java.lang.Integer
	    */
	Integer addUseSeal(AddUseSealReqVO addUseSealReqVO, HttpServletRequest request);

    /**
    	* 白鹤文件打印
    	* @Author: wdf 
        * @CreateDate: 2019/10/10 14:54
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/10/10 14:54
    	* @Version: 0.0.1
        * @param fileCodes, request
        * @return com.iyin.sign.system.model.InMemoryMultipartFile
        */
	InMemoryMultipartFile addFilePrint(String fileCodes, HttpServletRequest request);

	/**
		* 申请用印记录
		* @Author: wdf
	    * @CreateDate: 2019/10/10 15:16
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/10/10 15:16
		* @Version: 0.0.1
	    * @param currPage, pageSize, sealId,applyCause, request
	    * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.dto.req.SignSysUseSealDTO>>
	    */
	IyinResult<IyinPage<SignSysUseSealDTO>> getUseSealList(Integer currPage, Integer pageSize, String sealId,String applyCause, HttpServletRequest request);


	/**
		* 删除申请用印 记录
		* @Author: wdf
	    * @CreateDate: 2019/10/10 15:32
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/10/10 15:32
		* @Version: 0.0.1
	    * @param applyId, request
	    * @return Integer
	    */
	Integer deleteUseSeal(String applyId, HttpServletRequest request);

	/**
		* 下载文件
		* @Author: wdf
	    * @CreateDate: 2019/10/10 16:15
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/10/10 16:15
		* @Version: 0.0.1
	    * @param applyId, request
	    * @return com.iyin.sign.system.model.InMemoryMultipartFile
	    */
	InMemoryMultipartFile addFileDownZip(String applyId, HttpServletRequest request);

	/**
		* 同步用户数据-白鹤
		* @Author: wdf
	    * @CreateDate: 2019/10/16 10:42
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/10/16 10:42
		* @Version: 0.0.1
	    * @param sycnUserInfoListReqVO
	    * @return com.iyin.sign.system.model.IyinResult<java.lang.Integer>
	    */
	IyinResult<Integer> syncUserList(SycnUserInfoListReqVO sycnUserInfoListReqVO);

	/**
		* 获取图片文件
		* @Author: wdf 
	    * @CreateDate: 2019/10/17 15:07
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/10/17 15:07
		* @Version: 0.0.1
	    * @param category,fileName, request
	    * @return com.iyin.sign.system.model.InMemoryMultipartFile
	    */
	InMemoryMultipartFile fetchImage(Integer category,String fileName, HttpServletRequest request);


	/**
		* 盖章记录
		* @Author: wdf 
	    * @CreateDate: 2019/10/23 18:14
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/10/23 18:14
		* @Version: 0.0.1
	    * @param useSealStampRecordList, request
	    * @return com.iyin.sign.system.vo.resp.StampRecordResultRespVO
	    */
	StampRecordResultRespVO stampRecordList(UseSealStampRecordList useSealStampRecordList, HttpServletRequest request);


	/**
		* 盖章单据列表
		* @Author: wdf 
	    * @CreateDate: 2019/10/25 10:25
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/10/25 10:25
		* @Version: 0.0.1
	    * @param stampRecordApplyListReqVO, request
	    * @return com.iyin.sign.system.vo.resp.StampRecordApplyResultRespVO
	    */
	StampRecordApplyResultRespVO stampRecordApplyList(StampRecordApplyListReqVO stampRecordApplyListReqVO, HttpServletRequest request);

	/**
		* 获取单据盖章记录
		* @Author: wdf
	    * @CreateDate: 2019/10/25 14:22
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/10/25 14:22
		* @Version: 0.0.1
	    * @param useSealStampRecordApplyListReqVO, request
	    * @return com.iyin.sign.system.vo.resp.SealRecordListRespVO
	    */
	SealRecordListRespVO applyStampRecordList(UseSealStampRecordApplyListReqVO useSealStampRecordApplyListReqVO , HttpServletRequest request);

}
