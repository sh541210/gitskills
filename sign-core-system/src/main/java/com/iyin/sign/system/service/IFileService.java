package com.iyin.sign.system.service;

import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.sign.ImgInfoDTO;
import com.iyin.sign.system.model.sign.ImgInfoListDTO;
import com.iyin.sign.system.vo.file.FileUploadRespDto;
import com.iyin.sign.system.vo.req.DocPackageReqDto;
import com.iyin.sign.system.vo.req.FileResourceInfoVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
	* @Description 文件处理
	* @Author: wdf
    * @CreateDate: 2019/3/5 17:10
	* @UpdateUser: wdf
    * @UpdateDate: 2019/3/5 17:10
	* @Version: 0.0.1
    * @param
    * @return
    */
public interface IFileService {

	/**
		* @Description 文件上传
		* @Author: wdf
	    * @CreateDate: 2019/1/23 19:45
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/1/23 19:45
		* @Version: 0.0.1
	    * @param file, userId
	    * @return cn.com.iyin.contract.model.dto.file.FileUploadRespDto
	    */
	FileUploadRespDto upload(MultipartFile file, String userId);

	/**
		* @Description 签章后初始化文件上传接口
		* @Author: wdf
	    * @CreateDate: 2019/1/23 19:45
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/1/23 19:45
		* @Version: 0.0.1
	    * @param file, userId
	    * @return cn.com.iyin.contract.model.dto.file.FileUploadRespDto
	    */
	FileUploadRespDto uploadSignInit(MultipartFile file, String userId);

	/**
		* 文件更新
		* @Author: wdf 
	    * @CreateDate: 2019/7/5 18:01
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/7/5 18:01
		* @Version: 0.0.1
	    * @param file, userId, fileCode
	    * @return com.iyin.sign.system.vo.file.FileUploadRespDto
	    */
	FileUploadRespDto updateFile(MultipartFile file, String userId,String fileCode);

	/**
		* 文件更新
		* @Author: wdf
	    * @CreateDate: 2019/7/5 18:01
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/7/5 18:01
		* @Version: 0.0.1
	    * @param file, fileResourceInfoVO
	    * @return com.iyin.sign.system.vo.file.FileUploadRespDto
	    */
	FileUploadRespDto updateFileInfo(MultipartFile file, FileResourceInfoVO fileResourceInfoVO);

	/**
		* @Description base64 pdf 文件上传
		* @Author: wdf
	    * @CreateDate: 2019/1/25 18:54
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/1/25 18:54
		* @Version: 0.0.1
	    * @param file, userId
	    * @return cn.com.iyin.contract.model.dto.file.FileUploadRespDto
	    */
	FileUploadRespDto baseUpload(MultipartFile file, String userId);

	/**
		* @Description 通过文件编号获取文件InMemoryMultipartFile
		* @Author: wdf
	    * @CreateDate: 2019/1/23 19:44
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/1/23 19:44
		* @Version: 0.0.1
	    * @param fileCode
	    * @return cn.com.iyin.contract.model.InMemoryMultipartFile
	    */
	InMemoryMultipartFile fetch(String fileCode);

	/**
		* @Description 签署文件下载
		* @Author: wdf
	    * @CreateDate: 2019/1/23 19:44
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/1/23 19:44
		* @Version: 0.0.1
	    * @param fileCode
	    * @return cn.com.iyin.contract.model.InMemoryMultipartFile
	    */
	InMemoryMultipartFile downloadSignDoc(String fileCode);

	/**
	 * @Description 打包下载文件(不需要合同ID)
	 * @Author: wdf
	 * @CreateDate: 2019/1/23 19:46
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/1/23 19:46
	 * @Version: 0.0.1
	 * @param reqDto
	 * @return cn.com.iyin.contract.model.InMemoryMultipartFile
	 */
	InMemoryMultipartFile downloadDocZip(DocPackageReqDto reqDto);

	/**
	 * 文件转图片
	 * @Author: wdf
	 * @CreateDate: 2019/3/5 17:17
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/3/5 17:17
	 * @Version: 0.0.1
	 * @param file, userId
	 * @return cn.com.iyin.contract.model.dto.file.ImgInfoListDTO
	 */
	ImgInfoListDTO uploadCovertPic(MultipartFile file, String userId);

	/**
	 * 文件转图片
	 * @Author: wdf
	 * @CreateDate: 2019/3/5 17:17
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/3/5 17:17
	 * @Version: 0.0.1
	 * @param fileCode
	 * @return cn.com.iyin.contract.model.dto.file.ImgInfoListDTO
	 */
	ImgInfoListDTO uploadCovertPic(String fileCode,HttpServletRequest request);
	/**
	 * 文件转图片
	 * @Author: wdf
	 * @CreateDate: 2019/3/5 17:17
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/3/5 17:17
	 * @Version: 0.0.1
	 * @param fileCode
	 * @return cn.com.iyin.contract.model.dto.file.ImgInfoDTO
	 */
	ImgInfoDTO uploadCovertPicSize(String fileCode);

	/**
	 * 文件转pdf
	 * @Author: wdf
	 * @CreateDate: 2019/3/20 17:37
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/3/20 17:37
	 * @Version: 0.0.1
	 * @param file
	 * @return java.lang.String
	 */
	String uploadCovertPdf(MultipartFile file);

	/**
	 * 文件转pdf
	 * @Author: wdf
	 * @CreateDate: 2019/3/20 17:37
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/3/20 17:37
	 * @Version: 0.0.1
	 * @param file
	 * @return java.lang.String
	 */
	InMemoryMultipartFile downloadCovertPdf(MultipartFile file);

	/**
		* 文件转pdf
		* @Author: wdf
	    * @CreateDate: 2019/7/6 18:42
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/7/6 18:42
		* @Version: 0.0.1
	    * @param file
	    * @return com.iyin.sign.system.vo.file.FileUploadRespDto
	    */
	FileUploadRespDto uploadCovertPdfData(MultipartFile file, HttpServletRequest request);

	/**
		* 删除文件
		* @Author: wdf
	    * @CreateDate: 2019/7/11 15:30
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/7/11 15:30
		* @Version: 0.0.1
	    * @param fileCode
	    * @return java.lang.Boolean
	    */
	Boolean del(String fileCode);

	/**
	 * @Description 通过文件编号和页码 获取文件流对象
	 * @Author: wdf
	 * @CreateDate: 2019/1/23 19:48
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/1/23 19:48
	 * @Version: 0.0.1
	 * @param fileCode, pageNo
	 * @return cn.com.iyin.contract.model.InMemoryMultipartFile
	 */
	InMemoryMultipartFile fetchByPage(String fileCode, int pageNo);

	/**
	 * @Description 通过文件编号获取文件总页数
	 * @Author: wdf
	 * @CreateDate: 2019/1/23 19:49
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/1/23 19:49
	 * @Version: 0.0.1
	 * @param fileCode
	 * @return int
	 */
	int pageTotal(String fileCode);

	/**
	 * 获取雾化文件
	 *
	 * @Author: yml
	 * @CreateDate: 2019/7/30
	 * @UpdateUser: yml
	 * @UpdateDate: 2019/7/30
	 * @Version: 0.0.1
	 * @param fileCode
	 * @return : com.iyin.sign.system.model.InMemoryMultipartFile
	 */
	InMemoryMultipartFile fetch2(String fileCode);

	/**
	 * 按页浏览pdf，以图片的方式返回jpeg
	 *
	 * @Author: yml
	 * @CreateDate: 2019/7/31
	 * @UpdateUser: yml
	 * @UpdateDate: 2019/7/31
	 * @Version: 0.0.1
	 * @param fileCode
	 * @param pageNo
	 * @return : com.iyin.sign.system.model.InMemoryMultipartFile
	 */
	InMemoryMultipartFile fetchByPage2(String fileCode, int pageNo, HttpServletRequest request);

	String upload(File imageLocalFile);

	String getFullUrlByFileId(String fileId) throws IOException;
}
