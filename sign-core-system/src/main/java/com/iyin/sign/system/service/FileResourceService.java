package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.FileResource;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.file.FileResourceDto;

/**
 * <p>
 * 文件微服务中文件资源表 服务类
 * </p>
 *
 * @author xiaofanzhou
 * @since 2018-09-14
 */
public interface FileResourceService extends IService<FileResource> {
	/**
		* @Description 通过文件编号获取文件信息
		* @Author: wdf
	    * @CreateDate: 2019/1/23 19:46
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/1/23 19:46
		* @Version: 0.0.1
	    * @param fileCode
	    * @return cn.com.iyin.contract.file.entity.FileResource
	    */
	FileResource findByFileCode(String fileCode);

	/**
		* 文档列表
		* @Author: wdf
	    * @CreateDate: 2019/7/6 9:44
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/7/6 9:44
		* @Version: 0.0.1
	    * @param currPage, pageSize, fileName
	    * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.vo.file.FileResourceDto>>
	    */
	IyinResult<IyinPage<FileResourceDto>> getDocList(Integer currPage, Integer pageSize, String fileName);

}
