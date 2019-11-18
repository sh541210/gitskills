package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.entity.SignSysRole;
import com.iyin.sign.system.entity.SignSysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: SignSysRoleMapper
 * @Description: SignSysUserRoleMapper
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 15:12
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 15:12
 * @Version: 0.0.1
 */
@Mapper
public interface SignSysUserRoleMapper extends BaseMapper<SignSysUserRole> {
}
