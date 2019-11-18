package com.iyin.sign.system.service;

import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.vo.sign.req.FastFilePerforationSignHalfReqVO;
import com.iyin.sign.system.vo.sign.req.FastFilePerforationSignReqVO;
import com.iyin.sign.system.vo.sign.req.SingleFileFastSignMoreReqVO;
import com.iyin.sign.system.vo.sign.req.SingleFileFastSignReqVO;
import com.iyin.sign.system.vo.sign.resp.FileSignRespDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @ClassName: IFastSignService
 * @Description: 快捷签
 * @Author: wdf
 * @CreateDate: 2019/6/24
 * @UpdateUser: wdf
 * @UpdateDate: 2019/6/24
 * @Version: 1.0.0
 */
public interface IFileFastSignService {

    /**
     * 快捷单页签章
     *
     * @Author: wdf
     * @CreateDate: 2019/6/24
     * @UpdateUser: wdf
     * @UpdateDate: 2019/6/24
     * @Version: 0.0.1
     * @param signFile
     * @param sealFile
     * @param certFile
     * @param singleFileFastSignReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.FileSignRespDTO>
     */
    EsealResult<FileSignRespDTO> singleSign(MultipartFile signFile, MultipartFile sealFile, MultipartFile certFile,@Valid SingleFileFastSignReqVO singleFileFastSignReqVO, HttpServletRequest request);

    /**
     * 快捷单文件多页坐标签章
     *
     * @Author: wdf
     * @CreateDate: 2019/6/24
     * @UpdateUser: wdf
     * @UpdateDate: 2019/6/24
     * @Version: 0.0.1
     * @param signFile
     * @param sealFile
     * @param certFile
     * @param singleFileFastSignMoreReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.FileSignRespDTO>
     */
    EsealResult<FileSignRespDTO> singleCoordBatchSign(MultipartFile signFile, MultipartFile sealFile, MultipartFile certFile, @Valid SingleFileFastSignMoreReqVO singleFileFastSignMoreReqVO, HttpServletRequest request);

    /**
     * 快捷单文件骑缝坐标签章
     *
     * @Author: wdf
     * @CreateDate: 2019/6/24
     * @UpdateUser: wdf
     * @UpdateDate: 2019/6/24
     * @Version: 0.0.1
     * @param signFile
     * @param sealFile
     * @param certFile
     * @param fastFilePerforationSignReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.FileSignRespDTO>
     */
    EsealResult<FileSignRespDTO> singlePerforationCoordSign(MultipartFile signFile, MultipartFile sealFile, MultipartFile certFile, @Valid FastFilePerforationSignReqVO fastFilePerforationSignReqVO, HttpServletRequest request);



    /**
    	* @Description 个人/企业快捷签-单文件连页坐标签章接口
    	* @Author: wdf
        * @CreateDate: 2019/9/4 18:21
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/9/4 18:21
    	* @Version: 0.0.1
        * @param signFile, sealFile, certFile, fastFilePerforationSignHalfReqVO, request
        * @return com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.FileSignRespDTO>
        */
    EsealResult<FileSignRespDTO> singlePerforationCoordHalfSign(MultipartFile signFile, MultipartFile sealFile, MultipartFile certFile, @Valid FastFilePerforationSignHalfReqVO fastFilePerforationSignHalfReqVO, HttpServletRequest request);


}
