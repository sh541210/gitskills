package com.iyin.sign.system.service;

import com.iyin.sign.system.common.json.EsealResult;
import com.iyin.sign.system.vo.sign.req.*;
import com.iyin.sign.system.vo.sign.resp.SignRespDTO;

import java.io.IOException;

/**
 * @ClassName: ISignService
 * @Description: 签章
 * @Author: yml
 * @CreateDate: 2019/6/21
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/21
 * @Version: 1.0.0
 */
public interface ISignService {

    /**
     * 单页签章
     *
     * @Author: yml
     * @CreateDate: 2019/6/22
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/22
     * @Version: 0.0.1
     * @param singleSignReqDTO
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
     * @throws IOException IO异常
     */
    EsealResult<SignRespDTO> setSingleSign(SingleSignReqVO singleSignReqDTO) throws IOException;

    /**
     * 多页签章
     *
     * @Author: yml
     * @CreateDate: 2019/6/22
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/22
     * @Version: 0.0.1
     * @param batchSignReqVO
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
     * @throws IOException IO异常
     */
    EsealResult<SignRespDTO> setBatchSign(BatchSignReqVO batchSignReqVO) throws IOException;

    /**
     * 骑缝签章
     *
     * @Author: yml
     * @CreateDate: 2019/6/22
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/22
     * @Version: 0.0.1
     * @param perforationSignReqVO
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
     * @throws IOException IO异常
     */
    EsealResult<SignRespDTO> setPerforationSign(PerforationSignReqVO perforationSignReqVO) throws IOException;

    /**
     * 骑缝签章
     *
     * @Author: yml
     * @CreateDate: 2019/6/22
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/22
     * @Version: 0.0.1
     * @param sameKeyWordSignReqVO
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
     * @throws IOException IO异常
     */
    EsealResult<SignRespDTO> setKeyWordSign(SameKeyWordSignReqVO sameKeyWordSignReqVO) throws IOException;

    /**
     * 骑缝签章
     *
     * @Author: yml
     * @CreateDate: 2019/6/22
     * @UpdateUser: yml
     * @UpdateDate: 2019/6/22
     * @Version: 0.0.1
     * @param perforationSignReqVO
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
     * @throws IOException IO异常
     */
    EsealResult<SignRespDTO> setPagePerforationSign(PerforationSignReqVO perforationSignReqVO) throws IOException;

    /**
     * 连页签章
     *
     * @Author: wdf
     * @CreateDate: 2019/8/8
     * @UpdateUser: wdf
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     * @param perforationHalfSignReqVO
     * @return : com.iyin.sign.system.common.json.EsealResult<com.iyin.sign.system.vo.sign.resp.SignRespDTO>
     * @throws IOException IO异常
     */
    EsealResult<SignRespDTO> setPerforationHalfSign(PerforationHalfSignReqVO perforationHalfSignReqVO) throws IOException;
}
