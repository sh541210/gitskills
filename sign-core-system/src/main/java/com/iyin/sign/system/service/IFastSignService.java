package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.vo.sign.req.*;
import com.iyin.sign.system.vo.sign.resp.SignLogRespDTO;
import com.iyin.sign.system.vo.sign.resp.SignRespDTO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName: IFastSignService
 * @Description: 快捷签
 * @Author: yml
 * @CreateDate: 2019/6/24
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/24
 * @Version: 1.0.0
 */
public interface IFastSignService {

    /**
     * 快捷单页签章
     *
     * @Author: yml
     * @CreateDate: 2019/6/24
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/24
     * @Version: 0.0.1
     * @param singleFastKeySignReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
     */
    EsealResult<SignRespDTO> singleSign(SingleFastSignReqVO singleFastKeySignReqVO, HttpServletRequest request);

    /**
     * 单文件多页坐标签章
     *
     * @Author: yml
     * @CreateDate: 2019/6/24
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/24
     * @Version: 0.0.1
     * @param singleFastSignMoreReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
     */
    EsealResult<SignRespDTO> singleCoordBatchSign(SingleFastSignMoreReqVO singleFastSignMoreReqVO, HttpServletRequest request);

    /**
     * 单文件骑缝坐标签章
     *
     * @Author: yml
     * @CreateDate: 2019/6/25
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/25
     * @Version: 0.0.1
     * @param fastPerforationSignReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
     */
    EsealResult<SignRespDTO> singlePerforationCoordSign(FastPerforationSignReqVO fastPerforationSignReqVO, HttpServletRequest request);
    /**
     * 单文件连页坐标签章
     *
     * @Author: yml
     * @CreateDate: 2019/6/25
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/25
     * @Version: 0.0.1
     * @param fastPerforationHalfSignReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
     */
    EsealResult<SignRespDTO> singlePerforationCoordHalfSign(FastPerforationHalfSignReqVO fastPerforationHalfSignReqVO, HttpServletRequest request);

    /**
     * C++ uKey签章接入系统
     *
     * @Author: yml
     * @CreateDate: 2019/7/31
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/31
     * @Version: 0.0.1
     * @param uKeySignReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult<java.lang.Boolean>
     */
    EsealResult<Boolean> uKeySign(UKeySignReqVO uKeySignReqVO, HttpServletRequest request);

    /**
     * 签章日志
     *
     * @Author: yml
     * @CreateDate: 2019/9/16
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/16
     * @Version: 0.0.1
     * @param signLogReqVO
     * @param request
     * @return : com.iyin.sign.system.common.json.EsealResult
     */
    EsealResult<IPage<SignLogRespDTO>> signLog(SignLogReqVO signLogReqVO, HttpServletRequest request);

    /**
     * 签章
     *
     * @Author: yml
     * @CreateDate: 2019/10/29
     * @UpdateUser: yml
     * @UpdateDate: 2019/10/29
     * @Version: 0.0.1
     * @param signReqVO
     * @return : java.lang.String
     */
    String sign(SignReqVO signReqVO) throws IOException;
}
