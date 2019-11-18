package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iyin.sign.system.entity.SysSignatureLog;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.vo.req.SignLogListReqVO;
import com.iyin.sign.system.vo.req.UserSignLogListReqVO;
import com.iyin.sign.system.vo.resp.DocumentSignLogRespDTO;
import com.iyin.sign.system.vo.resp.EnterpriseSignLogRespVO;
import com.iyin.sign.system.vo.sign.req.SignLogReqVO;
import com.iyin.sign.system.vo.sign.resp.SignLogRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: ISysSignatureLogMapper
 * @Description:
 * @Author: yml
 * @CreateDate: 2019/6/24
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/24
 * @Version: 1.0.0
 */
@Mapper
public interface ISysSignatureLogMapper extends BaseMapper<SysSignatureLog> {

    /**
     * 根据文件编码查询签署日志
     *
     * @Author: yml
     * @CreateDate: 2019/7/5
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/5
     * @Version: 0.0.1
     * @param iyinPage
     * @param fileCode
     * @return : java.util.List<com.iyin.sign.system.vo.resp.DocumentSignLogRespDTO>
     */
    List<DocumentSignLogRespDTO> selectByFileCode(IyinPage<DocumentSignLogRespDTO> iyinPage,@Param("fileCode") String fileCode);

    /**
    　　* @description: 企业签章日志集合
    　　* @param iyinPage
        * @param reqVO
    　　* @return  List<EnterpriseSignLogRespVO>
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 16:39
    　　*/
    List<EnterpriseSignLogRespVO> pageListSignLog(IyinPage<EnterpriseSignLogRespVO> iyinPage, @Param(value = "reqVO") SignLogListReqVO reqVO);

    /**
    　　* @description: 企业用户日志集合
    　　* @param iyinPage
        * @param reqVO
    　　* @return List<EnterpriseSignLogRespVO>
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 16:40 
    　　*/
    List<EnterpriseSignLogRespVO> userPageListSignLog(IyinPage<EnterpriseSignLogRespVO> iyinPage, @Param(value = "reqVO")UserSignLogListReqVO reqVO);

    /**
     * 获取添加时间
     * @Author: wdf
     * @CreateDate: 2019/2/28 17:15
     * @UpdateUser: wdf
     * @UpdateDate: 2019/2/28 17:15
     * @Version: 0.0.1
     * @param
     * @return object
     */
    Object getAddDate();

    /**
     *  获取有效签章次数
     * @Author: wdf
     * @CreateDate: 2019/2/26 10:51
     * @UpdateUser: wdf
     * @UpdateDate: 2019/2/26 10:51
     * @Version: 0.0.1
     * @param
     * @return int
     */
    int getSignCount();

    /**
     * 签章日志
     *
     * @Author: yml
     * @CreateDate: 2019/9/16
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/16
     * @Version: 0.0.1
     * @param signLogRespDTOPage
     * @param signLogReqVO
     * @return : java.util.List<com.iyin.sign.system.vo.sign.resp.SignLogRespDTO>
     */
    List<SignLogRespDTO> signLog(IPage<SignLogRespDTO> signLogRespDTOPage,@Param("reqVo") SignLogReqVO signLogReqVO);
}
