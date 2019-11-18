package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.entity.SignSysSealInfo;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.vo.req.SealInfoListReqVO;
import com.iyin.sign.system.vo.req.UserSealInfoListReqVO;
import com.iyin.sign.system.vo.resp.SealInfoRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: SignSysSealInfoMapper
 * @Description: 签章系统印章信息
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 15:29
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 15:29
 * @Version: 0.0.1
 */
@Mapper
public interface SignSysSealInfoMapper extends BaseMapper<SignSysSealInfo> {

    /**
     * 查询用户分配的印章
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/30 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/30 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    List<SealInfoRespVO> selectPageUserSealInfoList(IyinPage<SealInfoRespVO> iyinPage, @Param(value = "reqVO") UserSealInfoListReqVO reqVO);

    /**
     * 查询单位全部印章
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/30 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/30 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    List<SealInfoRespVO> pageListSealInfo(IyinPage<SealInfoRespVO> iyinPage,@Param(value = "reqVO") SealInfoListReqVO reqVO);
}
