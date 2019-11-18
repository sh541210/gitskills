package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.entity.SignSysCompactLog;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.vo.file.resp.CompactLogRespDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: ISignSysCompactLogMapper.java
 * @Description: 合同操作记录
 * @Author: yml
 * @CreateDate: 2019/8/16
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/16
 * @Version: 1.0.0
 */
public interface ISignSysCompactLogMapper extends BaseMapper<SignSysCompactLog> {

    /**
     * 查询操作日志
     *
     * @Author: yml
     * @CreateDate: 2019/8/20
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/20
     * @Version: 0.0.1
     * @param contractId
     * @return : java.util.List<com.iyin.sign.system.vo.file.resp.CompactLogRespDto>
     */
    List<CompactLogRespDto> queryLog(@Param("contractId") String contractId);

    /**
     * 查询指定操作日志
     *
     * @Author: yml
     * @CreateDate: 2019/8/30
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/30
     * @Version: 0.0.1
     * @param page
     * @param contractId
     * @param type
     * @return : java.util.List<com.iyin.sign.system.vo.file.resp.CompactLogRespDto>
     */
    List<CompactLogRespDto> queryDesignationLog(IyinPage page, @Param("contractId") String contractId, @Param("type") String type);
}