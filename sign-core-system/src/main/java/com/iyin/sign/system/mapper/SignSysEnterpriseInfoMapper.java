package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.entity.SignSysEnterpriseInfo;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.vo.req.EnterprsieListReqVO;
import com.iyin.sign.system.vo.resp.EnterprsieDetailRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: SignSysEnterpriseInfoMapper
 * @Description: SignSysEnterpriseInfoMapper
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 18:34
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 18:34
 * @Version: 0.0.1
 */
@Mapper
public interface SignSysEnterpriseInfoMapper extends BaseMapper<SignSysEnterpriseInfo> {

    /**
    　　* @description: 获取企业详情集合
    　　* @param iyinPage
     　　* @param reqVO
     　　* @return List<EnterprsieDetailRespVO>
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 16:44
    　　*/
    List<EnterprsieDetailRespVO> pageListEnterprise(IyinPage<EnterprsieDetailRespVO> iyinPage, @Param("reqVO") EnterprsieListReqVO reqVO);

    /**
    　　* @description: 查询管理员归属的企业
    　　* @return SignSysEnterpriseInfo
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 16:45
    　　*/
    SignSysEnterpriseInfo selectAdminBelongEnterprise();
}
