package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.entity.SignSysCompactInfo;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.vo.file.req.ContractQueryReqVO;
import com.iyin.sign.system.vo.file.resp.ContractQueryRespDTO;
import com.iyin.sign.system.vo.file.resp.FileDetailRespDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: ISignSysCompactInfoMapper.java
 * @Description: 合同
 * @Author: yml
 * @CreateDate: 2019/8/7
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
public interface ISignSysCompactInfoMapper extends BaseMapper<SignSysCompactInfo> {

    /**
     * 更新签署状态
     *
     * @param compactId 合同ID
     * @param code 合同状态
     * @param date 合同完成时间
     * @return : int
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    int updateStatus(@Param("id") String compactId, @Param("status") String code, @Param("date") Date date);

    /**
     * 获取文件信息
     *
     * @param fileCode
     * @param userId
     * @return : com.iyin.sign.system.vo.file.resp.FileDetailRespDTO
     * @Author: yml
     * @CreateDate: 2019/8/8
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/8
     * @Version: 0.0.1
     */
    List<FileDetailRespDTO> getDetail(@Param("fileCode") String fileCode, @Param("userId") String userId);

    /**
     * 合同查询
     *
     *
     * @param iyinPage
     * @param contractQueryReqVO
     * @return : java.util.List<com.iyin.sign.system.vo.file.resp.ContractQueryRespDTO>
     * @Author: yml
     * @CreateDate: 2019/8/13
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/13
     * @Version: 0.0.1
     */
    List<ContractQueryRespDTO> queryCompact(IyinPage<ContractQueryRespDTO> iyinPage,
                                            @Param("contractQueryReqVO") ContractQueryReqVO contractQueryReqVO);

    /**
     * 获取添加时间
     *
     * @param
     * @return object
     * @Author: wdf
     * @CreateDate: 2019/2/28 17:15
     * @UpdateUser: wdf
     * @UpdateDate: 2019/2/28 17:15
     * @Version: 0.0.1
     */
    Object getAddDate();

    /**
     * 拒签合同
     *
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     * @param contractId
     * @param rejectContent
     * @return : int
     */
    int rejectContract(@Param("contractId") String contractId,@Param("rejectContent") String rejectContent);

    /**
     * 撤销合同
     *
     * @Author: yml
     * @CreateDate: 2019/8/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/19
     * @Version: 0.0.1
     * @param contractId
     * @param revokeContent
     * @return : int
     */
    int revokeContract(@Param("contractId") String contractId,@Param("revokeContent") String revokeContent);

    /**
     * 即将签约截止
     *
     * @Author: yml
     * @CreateDate: 2019/8/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/23
     * @Version: 0.0.1

     * @return : java.util.List<com.iyin.sign.system.entity.SignSysCompactInfo>
     * @param threshold
     */
    List<SignSysCompactInfo> comingToSign(@Param("threshold") int threshold);

    /**
     * 合同即将到期
     *
     * @Author: yml
     * @CreateDate: 2019/8/27
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/27
     * @Version: 0.0.1
     * @param threshold
     * @return : java.util.List<com.iyin.sign.system.entity.SignSysCompactInfo>
     */
    List<SignSysCompactInfo> comingToValidityPeriod(@Param("threshold") int threshold);
}