package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.entity.SignSysRole;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.vo.req.SignSysRolePageListReqVO;
import com.iyin.sign.system.vo.resp.SignSysRoleRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: SignSysRoleMapper
 * @Description: signSysmapper
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 15:12
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 15:12
 * @Version: 0.0.1
 */
@Mapper
public interface SignSysRoleMapper extends BaseMapper<SignSysRole> {

    /**
    　　* @description: 获取签章系统角色集合
    　　* @param iyinPage
     　 * @param reqVO
    　　* @return List<SignSysRoleRespVO>
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 16:54
    　　*/
    List<SignSysRoleRespVO> pageListRole(IyinPage<SignSysRoleRespVO> iyinPage,@Param(value = "reqVO")SignSysRolePageListReqVO reqVO);
}
