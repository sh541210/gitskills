package com.iyin.sign.system.controller.file;

import com.iyin.sign.system.common.SwaggerConstant;
import com.iyin.sign.system.common.interfaces.CacheConstants;
import com.iyin.sign.system.common.utils.Base64MultipartFile;
import com.iyin.sign.system.dto.req.Constant;
import com.iyin.sign.system.dto.req.FileBaseDTO;
import com.iyin.sign.system.entity.FileResource;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.FileResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.model.sign.ImgInfoDTO;
import com.iyin.sign.system.model.sign.ImgInfoListDTO;
import com.iyin.sign.system.service.FileResourceService;
import com.iyin.sign.system.service.IFileService;
import com.iyin.sign.system.service.SignSysSignatureLogService;
import com.iyin.sign.system.vo.file.FileResourceDto;
import com.iyin.sign.system.vo.file.FileUploadRespDto;
import com.iyin.sign.system.vo.req.Base642FileReqVO;
import com.iyin.sign.system.vo.req.DocPackageReqDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
	* @Description 文件上传相关
	* @Author: wdf
    * @CreateDate: 2019/1/28 17:29
	* @UpdateUser: wdf
    * @UpdateDate: 2019/1/28 17:29
	* @Version: 0.0.1
    * @param
    * @return
    */
@Api(value="文件上传", tags= {SwaggerConstant.DOC_CENTER_UPLOAD})
@Slf4j
@RestController
@RequestMapping("/document")
public class FileUploadController {
	@Autowired
	FileResourceService fileResourceService;
	@Autowired
	IFileService fileService;
	@Autowired
	private SignSysSignatureLogService signatureLogService;

	private static final String OUT_FAIL="file output fail: ";
	private static final String CONTENT_DIS ="Content-Disposition";
	private static final String CHROME = "Chrome";
	private static final String MSIE = "MSIE";
	private static final String TRIDENT = "Trident";
	private static final String USERAGENT = "User-Agent";
	private static final String FILENAME = "attachment;filename=%s";
	private static final String INLINE = "inline;filename=%s";


	/**
		* 文件列表接口
		* @Author: wdf
	    * @CreateDate: 2019/8/12 12:21
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/8/12 12:21
		* @Version: 0.0.1
	    * @param currPage, pageSize, fileName
	    * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.vo.file.FileResourceDto>>
	    */
	@ApiOperation(value = "文件列表接口",tags = {SwaggerConstant.DOC_CENTER_FILE})
	@GetMapping("/getDocList")
	@ApiIgnore
	public IyinResult<IyinPage<FileResourceDto>> getDocList(@RequestParam Integer currPage, @RequestParam Integer pageSize, @RequestParam(required = false) String fileName) {
		return fileResourceService.getDocList(currPage,pageSize,fileName);
	}

	/**
		* @Description 获取文件记录
		* @Author: wdf
	    * @CreateDate: 2018/12/3 15:51
		* @UpdateUser: wdf
	    * @UpdateDate: 2018/12/3 15:51
		* @Version: 0.0.1
	    * @param fileCode
	    * @return IyinResult<cn.com.iyin.contract.file.entity.FileResource>
	    */
	@ApiOperation(value = "获取文件记录",tags = {SwaggerConstant.DOC_CENTER_UPLOAD})
	@GetMapping("/record/{fileCode}")
	@ResponseBody
	public IyinResult<FileResource> findByFileCode(@PathVariable(name = "fileCode") String fileCode){
		FileResource byFileCode = fileResourceService.findByFileCode(fileCode);
		return IyinResult.getIyinResult(byFileCode);
	}

	/**
		* @Description 文件 上传
		* @Author: wdf
	    * @CreateDate: 2019/1/14 14:21
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/1/14 14:21
		* @Version: 0.0.1
	    * @param file, userId
	    * @return IyinResult<cn.com.iyin.contract.model.dto.file.FileUploadRespDto>
	    */
	@ApiOperation(value="文件上传", tags= {SwaggerConstant.DOC_CENTER_UPLOAD})
	@ResponseBody
	@PostMapping(path="/file/upload", consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
	public IyinResult<FileUploadRespDto> upload(@RequestParam(name="file", required=true) MultipartFile file, @RequestParam(name="userId", required=true) String userId) {
		FileUploadRespDto resp = fileService.upload(file, userId);
		return IyinResult.success(resp);
	}

	/**
		* @Description 文件更新
		* @Author: wdf
	    * @CreateDate: 2019/1/14 14:21
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/1/14 14:21
		* @Version: 0.0.1
	    * @param file, userId,fileCode
	    * @return IyinResult<cn.com.iyin.contract.model.dto.file.FileUploadRespDto>
	    */
	@ApiOperation(value="文件更新", tags= {SwaggerConstant.DOC_CENTER_UPLOAD})
	@ResponseBody
	@PostMapping(path="/file/updateFile", consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
	public IyinResult<FileUploadRespDto> updateFile(@RequestParam(name="file", required=true) MultipartFile file, @RequestParam(name="userId", required=true) String userId,@RequestParam(name = "fileCode") String fileCode) {
		FileUploadRespDto resp = fileService.updateFile(file, userId,fileCode);
		return IyinResult.success(resp);
	}

	/**
		* @Description base64文件上传
		* @Author: wdf
	    * @CreateDate: 2019/1/14 16:32
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/1/14 16:32
		* @Version: 0.0.1
	    * @param fileBaseDTO
	    * @return IyinResult<cn.com.iyin.contract.model.dto.file.FileUploadRespDto>
	    */
	@ApiOperation(value="base64文件上传", tags= {SwaggerConstant.DOC_CENTER_UPLOAD})
	@ResponseBody
	@PostMapping(path="/upload/base64", consumes={MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ApiIgnore
	public IyinResult<FileUploadRespDto> uploadBase64(@RequestBody @Valid FileBaseDTO fileBaseDTO) {
		MultipartFile fileCov= Base64MultipartFile.base64ToMultipart2(fileBaseDTO.getBaseFile());
		FileUploadRespDto resp = fileService.baseUpload(fileCov, fileBaseDTO.getUserId());
		return IyinResult.success(resp);
	}


	/**
		* @Description 文件下载
		* @Author: wdf 
	    * @CreateDate: 2018/12/3 19:15
		* @UpdateUser: wdf
	    * @UpdateDate: 2018/12/3 19:15
		* @Version: 0.0.1
	    * @param fileCode, response
	    * @return void
	    */
	@ApiOperation(value="文件下载", tags= {SwaggerConstant.DOC_CENTER_UPLOAD})
	@GetMapping(path="download/{fileCode}")
	public void download(@PathVariable(value = "fileCode") String fileCode, HttpServletRequest request, HttpServletResponse response) {
		log.info("fetch resource id: {}", fileCode);
		InMemoryMultipartFile file = fileService.fetch(fileCode);
		response.setContentType(file.getContentType());
		String userAgent = request.getHeader(USERAGENT);
		String oraFileName = file.getOriginalFilename();
		try {
			log.info("download userAgent: {}", userAgent);
			// 针对IE或者以IE为内核的浏览器：
			if (userAgent.contains(MSIE) || userAgent.contains(TRIDENT) || userAgent.contains(CHROME)) {
				oraFileName  = java.net.URLEncoder.encode(oraFileName , Constant.UTF8);
			} else {
				// 非IE浏览器的处理：
				oraFileName  = new String(oraFileName.getBytes(Constant.UTF8), Constant.ISO88591);
			}
			response.setCharacterEncoding(Constant.UTF8);
			response.setHeader(CONTENT_DIS, String.format(FILENAME, oraFileName));
			response.getOutputStream().write(file.getBytes());
		} catch (IOException e) {
			log.error(OUT_FAIL, e.getMessage());
			throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
		}
	}

	/**
		* @Description 签署文件下载
		* @Author: wdf
	    * @CreateDate: 2018/12/3 19:15
		* @UpdateUser: wdf
	    * @UpdateDate: 2018/12/3 19:15
		* @Version: 0.0.1
	    * @param fileCode, response
	    * @return void
	    */
	@ApiOperation(value="签署文件下载", tags= {SwaggerConstant.DOC_CENTER_UPLOAD})
	@GetMapping(path="downloadSignDoc/{fileCode}")
	public void downloadSignDoc(@PathVariable(value = "fileCode") String fileCode,HttpServletRequest request, HttpServletResponse response) {
		log.info("downloadSignDoc resource id: {}", fileCode);
		InMemoryMultipartFile file = fileService.downloadSignDoc(fileCode);
		response.setContentType(file.getContentType());
		String userAgent = request.getHeader(USERAGENT);
		String oraFileName = file.getOriginalFilename();
		try {
			log.info("签署文件下载 downloadSignDoc userAgent: {}", userAgent);
			// 针对IE或者以IE为内核的浏览器
			if (userAgent.contains(MSIE) || userAgent.contains(TRIDENT) || userAgent.contains(CHROME)) {
				oraFileName  = java.net.URLEncoder.encode(oraFileName , Constant.UTF8);
			} else {
				// 非IE浏览器的处理
				oraFileName  = new String(oraFileName.getBytes(Constant.UTF8), Constant.ISO88591);
			}
			response.setCharacterEncoding(Constant.UTF8);
			response.setHeader(CONTENT_DIS, String.format(FILENAME, oraFileName));
			response.getOutputStream().write(file.getBytes());
		} catch (IOException e) {
			log.error(OUT_FAIL, e.getMessage());
			throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
		}
	}

	/**
		* @Description 文件删除
		* @Author: wdf
	    * @CreateDate: 2018/12/3 19:15
		* @UpdateUser: wdf
	    * @UpdateDate: 2018/12/3 19:15
		* @Version: 0.0.1
	    * @param fileCode
	    * @return void
	    */
	@ApiOperation(value="文件删除", tags= {SwaggerConstant.DOC_CENTER_FILE})
	@DeleteMapping(path="del/{fileCode}")
	public IyinResult<Boolean> del(@PathVariable(value = "fileCode", required=true) String fileCode) {
		log.info("fetch resource id: {}", fileCode);
		Boolean flag= fileService.del(fileCode);
        return IyinResult.success(flag);
	}

	/**
		* @Description 文件在线浏览
		* @Author: wdf 
	    * @CreateDate: 2018/12/3 19:15
		* @UpdateUser: wdf
	    * @UpdateDate: 2018/12/3 19:15
		* @Version: 0.0.1
	    * @param fileCode, response
	    * @return void
	    */
	@ApiOperation(value="文件在线浏览", tags= {SwaggerConstant.DOC_CENTER_FILE})
	@GetMapping(path="fetch/{fileCode}")
	public void fetch(@PathVariable(value = "fileCode") String fileCode, HttpServletResponse response) {
		InMemoryMultipartFile file = fileService.fetch2(fileCode);
		response.setContentType(file.getContentType());
		response.setHeader(CONTENT_DIS, String.format(INLINE, file.getOriginalFilename()));
		try {
			response.getOutputStream().write(file.getBytes());
		} catch (IOException e) {
			log.error(OUT_FAIL, e.getMessage());
			throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
		}
	}

	/**
		* @Description 文件合并下载
		* @Author: wdf
	    * @CreateDate: 2019/1/15 11:06
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/1/15 11:06
		* @Version: 0.0.1
	    * @param reqDto, response
	    * @return void
	    */
	@ApiOperation(value="文件合并下载", tags= {SwaggerConstant.DOC_CENTER_FILE})
	@PostMapping(path="/package/download",
			consumes={MediaType.APPLICATION_JSON_VALUE},
			produces= {MediaType.ALL_VALUE})
	public void downloadDocZip(@RequestBody @Valid DocPackageReqDto reqDto, HttpServletResponse response) {
		log.info("download package: userId: {}", reqDto.getUserId());
		InMemoryMultipartFile file = fileService.downloadDocZip(reqDto);
		response.setContentType(file.getContentType());
		response.setHeader(CONTENT_DIS, String.format(FILENAME, file.getOriginalFilename()));
		try {
			byte[] bytes = file.getBytes();
			response.getOutputStream().write(bytes);
		} catch (IOException e) {
			log.error(OUT_FAIL, e.getMessage());
			throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
		}
	}

	/**
		* @Description 文件转图片
		* @Author: wdf
	    * @CreateDate: 2019/3/5 17:24
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/3/5 17:24
		* @Version: 0.0.1
	    * @param file, userId
	    * @return cn.com.iyin.contract.model.common.IyinResult<cn.com.iyin.contract.model.dto.file.ImgInfoListDTO>
	    */
	@ResponseBody
	@PostMapping(path="/upload/covertPic",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@ApiOperation(value = "文件转图片",tags = {SwaggerConstant.DOC_CENTER_FILE})
	public IyinResult<ImgInfoListDTO> uploadCovertPic(@RequestParam(name="file") MultipartFile file, @RequestParam(name="userId")String userId) {
		ImgInfoListDTO imgInfoListDTO=fileService.uploadCovertPic(file,userId);
		return IyinResult.success(imgInfoListDTO);
	}

	/**
		* @Description 文件CODE转图片，输出图片CODE列表
		* @Author: wdf
	    * @CreateDate: 2019/3/5 17:24
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/3/5 17:24
		* @Version: 0.0.1
	    * @param fileCode
	    * @return cn.com.iyin.contract.model.common.IyinResult<cn.com.iyin.contract.model.dto.file.ImgInfoListDTO>
	    */
	@ResponseBody
	@GetMapping(path="/fetch/getPic/{fileCode}")
	@ApiOperation(value = "文件CODE转图片，输出图片CODE列表",tags = {SwaggerConstant.DOC_CENTER_FILE})
	public IyinResult<ImgInfoListDTO> getPic(@PathVariable(name = "fileCode") String fileCode,HttpServletRequest request) {
		ImgInfoListDTO imgInfoListDTO=fileService.uploadCovertPic(fileCode,request);
		return IyinResult.success(imgInfoListDTO);
	}

	/**
	 * @Description 输出文件宽高和页码
	 * @Author: wdf
	 * @CreateDate: 2019/3/5 17:24
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/3/5 17:24
	 * @Version: 0.0.1
	 * @param fileCode
	 * @return cn.com.iyin.contract.model.common.IyinResult<cn.com.iyin.contract.model.dto.file.ImgInfoListDTO>
	 */
	@ResponseBody
	@GetMapping(path="/fetch/getPicSize/{fileCode}")
	@ApiOperation(value = "输出文件宽高和页码",tags = {SwaggerConstant.DOC_CENTER_FILE})
	@Cacheable(value = CacheConstants.PDF_SIZE,key = "#fileCode")
	public IyinResult<ImgInfoDTO> getPicSize(@PathVariable(name = "fileCode") String fileCode) {
		ImgInfoDTO imgInfoDTO=fileService.uploadCovertPicSize(fileCode);
		return IyinResult.success(imgInfoDTO);
	}

	/**
		* @Description 文件转pdf
		* @Author: wdf 
	    * @CreateDate: 2019/3/20 11:36
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/3/20 11:36
		* @Version: 0.0.1
	    * @param file
	    * @return cn.com.iyin.contract.model.common.IyinResult<java.lang.String>
	    */
	@ResponseBody
	@PostMapping(path="/upload/uploadCovertPdf",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@ApiOperation(value = "文件转pdf",tags = {SwaggerConstant.DOC_CENTER_FILE})
	public IyinResult<String> uploadCovertPdf(@RequestPart(name="file") MultipartFile file) {
		return IyinResult.success(fileService.uploadCovertPdf(file));
	}

	/**
	 * @Description 文件转换并下载
	 * @Author: wdf
	 * @CreateDate: 2018/12/3 19:15
	 * @UpdateUser: wdf
	 * @UpdateDate: 2018/12/3 19:15
	 * @Version: 0.0.1
	 * @param file, request, response
	 * @return void
	 */
	@ApiOperation(value="文件转换并下载", tags= {SwaggerConstant.DOC_CENTER_UPLOAD})
	@PostMapping(path="download/uploadCovertPdf",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public void downloadCovertPdf(@RequestPart(name="file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		InMemoryMultipartFile in = fileService.downloadCovertPdf(file);
		response.setContentType(in.getContentType());
		String userAgent = request.getHeader(USERAGENT);
		String oraFileName = in.getOriginalFilename();
		try {
			log.info("downloadCovertPdf userAgent: {}", userAgent);
			// 针对IE或者以IE为内核的浏览器：
			if (userAgent.contains(MSIE) || userAgent.contains(TRIDENT) || userAgent.contains(CHROME)) {
				oraFileName  = java.net.URLEncoder.encode(oraFileName , Constant.UTF8);
			} else {
				// 非IE浏览器的处理：
				oraFileName  = new String(oraFileName.getBytes(Constant.UTF8), Constant.ISO88591);
			}
			response.setCharacterEncoding(Constant.UTF8);
			response.setHeader(CONTENT_DIS, String.format(FILENAME, oraFileName));
			response.getOutputStream().write(in.getBytes());
		} catch (IOException e) {
			log.error(OUT_FAIL, e.getMessage());
			throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
		}
	}

	/**
		* 文件转pdf并上传已经转换后的文件
		* @Author: wdf 
	    * @CreateDate: 2019/7/6 18:44
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/7/6 18:44
		* @Version: 0.0.1
	    * @param file
	    * @return com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.vo.file.FileUploadRespDto>
	    */
	@ResponseBody
	@PostMapping(path="/upload/uploadCovertPdfData",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@ApiOperation(value = "文件转pdf并上传已经转换后的文件",tags = {SwaggerConstant.DOC_CENTER_FILE})
	public IyinResult<FileUploadRespDto> uploadCovertPdfData(@RequestPart(name="file") MultipartFile file,HttpServletRequest request) {
		return IyinResult.success(fileService.uploadCovertPdfData(file,request));
	}

	/**
	 * Base64文件转pdf
	 *
	 * @Author: yml
	 * @CreateDate: 2019/3/29
	 * @UpdateUser: yml
	 * @UpdateDate: 2019/3/29
	 * @Version: 0.0.1
	 * @param base64ToFileReqVO
	 * @return : cn.com.iyin.contract.model.common.IyinResult<java.lang.String>
	 */
	@ResponseBody
	@PostMapping(path="/upload/uploadCovertPdf2")
	@ApiOperation(value = "Base64文件转pdf",tags = {SwaggerConstant.DOC_CENTER_FILE})
	@ApiIgnore
	public IyinResult<String> uploadCovertPdf2(@RequestBody Base642FileReqVO base64ToFileReqVO) {
		MultipartFile file = Base64MultipartFile.base64ToMultipart2(base64ToFileReqVO.getFileBase64());
		return IyinResult.success(fileService.uploadCovertPdf(file));
	}

	/**
		* 按页取pdf，以图片的方式返回jpeg
		* @Author: wdf
	    * @CreateDate: 2019/8/12 12:21
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/8/12 12:21
		* @Version: 0.0.1
	    * @param fileCode, pageNo, response
	    * @return void
	    */
	@ApiOperation(value="文件在线浏览，按页图片", tags= {SwaggerConstant.DOC_CENTER_CONTRACT})
	@GetMapping(path="fetch/page/{fileCode}/{pageNo}")
	public void fetchByPage(
			@PathVariable(value = "fileCode") String fileCode,
			@PathVariable(value = "pageNo") int pageNo,
			HttpServletResponse response) {
		InMemoryMultipartFile file = fileService.fetchByPage(fileCode, pageNo);
		response.setContentType(file.getContentType());
		response.setHeader(CONTENT_DIS, String.format(INLINE, file.getOriginalFilename()));
		try {
			response.getOutputStream().write(file.getBytes());
		} catch (IOException e) {
			log.error(OUT_FAIL, e.getMessage());
			throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
		}
	}

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
	 * @param response
	 * @return : void
	 */
	@ApiOperation(value="文件在线浏览，按页图片", tags= {SwaggerConstant.DOC_CENTER_CONTRACT})
	@GetMapping(path="scan/page/{fileCode}/{pageNo}")
	public void scanByPage(
			@PathVariable(value = "fileCode") String fileCode,
			@PathVariable(value = "pageNo") int pageNo, HttpServletRequest request,
			HttpServletResponse response) {
		InMemoryMultipartFile file = fileService.fetchByPage2(fileCode, pageNo,request);
		response.setContentType(file.getContentType());
		response.setHeader(CONTENT_DIS, String.format(INLINE, file.getOriginalFilename()));
		try {
			response.getOutputStream().write(file.getBytes());
		} catch (IOException e) {
			log.error(OUT_FAIL, e.getMessage());
			throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
		}
	}

	/**
		* 按页取pdf，以图片的方式返回jpeg
		* @Author: wdf
	    * @CreateDate: 2019/8/12 12:21
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/8/12 12:21
		* @Version: 0.0.1
	    * @param fileCode
	    * @return com.iyin.sign.system.model.IyinResult<java.lang.Integer>
	    */
	@ApiOperation(value="合同文件总页数", tags= {SwaggerConstant.DOC_CENTER_CONTRACT})
	@GetMapping(path="{fileCode}/page/total")
	public IyinResult<Integer> pageNums(@PathVariable(value = "fileCode") String fileCode) {
		log.info("get pdf page total: {}", fileCode);
		int total = fileService.pageTotal(fileCode);
		return IyinResult.success(total);
	}

}
