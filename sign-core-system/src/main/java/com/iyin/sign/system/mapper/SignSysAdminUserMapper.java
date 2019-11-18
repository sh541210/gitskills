package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.entity.SignSysAdminUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: SignSysAdminUserMapper
 * @Description: SignSysAdminUserMapper
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 15:29
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 15:29
 * @Version: 0.0.1
 */
@Mapper
public interface SignSysAdminUserMapper extends BaseMapper<SignSysAdminUser> {

    Object getAddDate();

}
