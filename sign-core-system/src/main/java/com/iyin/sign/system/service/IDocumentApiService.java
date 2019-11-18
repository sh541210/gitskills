package com.iyin.sign.system.service;

import com.iyin.sign.system.entity.FileResource;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.vo.file.FileResourceDto;
import com.iyin.sign.system.vo.req.QuerySignLogReqVO;
import com.iyin.sign.system.vo.resp.DocumentSignLogRespDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: IDocumentApiService
 * @Description: 文档API
 * @Author: yml
 * @CreateDate: 2019/7/5
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/5
 * @Version: 1.0.0
 */
public interface IDocumentApiService {

    /**
     * 根据文档编码查询签署日志
     *
     * @Author: yml
     * @CreateDate: 2019/7/5
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/5
     * @Version: 0.0.1
     * @param signLogReqVO
     * @return : java.util.List<com.iyin.sign.system.vo.resp.DocumentSignLogRespDTO>
     */
    IyinPage<DocumentSignLogRespDTO> querySignLog(QuerySignLogReqVO signLogReqVO, HttpServletRequest request);

    /**
     * 上传文档并转换
     *
     * @Author: yml
     * @CreateDate: 2019/7/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/8
     * @Version: 0.0.1
     * @param multipartFile
     * @param userId
     * @return : java.lang.String
     */
    String conversionDocument(MultipartFile multipartFile, String userId);

    /**
     * 查询文档详情
     *
     * @Author: yml
     * @CreateDate: 2019/7/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/8
     * @Version: 0.0.1
     * @param fileCode
     * @return : void
     */
    FileResource queryDocumentDetail(String fileCode);

    /**
     * 根据文档编码下载文
     *
     * @Author: yml
     * @CreateDate: 2019/7/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/8
     * @Version: 0.0.1
     * @param fileCode
     * @param response
     * @return : com.iyin.sign.system.model.InMemoryMultipartFile
     */
    InMemoryMultipartFile downloadDocument(String fileCode, HttpServletResponse response);

    /**
     * 查询文档列表
     *
     * @Author: yml
     * @CreateDate: 2019/7/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/8
     * @Version: 0.0.1
     * @param fileName
     * @param pageNum
     * @param pageSize
     * @return : com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.entity.FileResource>
     */
    IyinPage<FileResourceDto> queryDocumentList(String fileName, Integer pageNum, Integer pageSize,HttpServletRequest request);

    /**
     * 验证码验证签章
     *
     * @Author: yml
     * @CreateDate: 2019/7/25
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/25
     * @Version: 0.0.1
     * @param verificationCode
     * @param httpServletRequest
     * @return : com.iyin.sign.system.entity.FileResource
     */
    FileResource queryDocumetnByValityCode(String verificationCode, HttpServletRequest httpServletRequest);
}
