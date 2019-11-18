package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysUserDataLimit;

import java.util.List;

/**
 * @ClassName: SignSysUserDataLimitService
 * @Description: SignSysUserDataLimitService
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 15:43
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 15:43
 * @Version: 0.0.1
 */
public interface SignSysUserDataLimitService extends IService<SignSysUserDataLimit> {

    /**
     * 判断当前操作用户是否拥有资源创建者所属资源的查看权限
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/21
     * @UpdateUser:
     * @UpdateDate:  2019/8/21
     * @Version:     0.0.1
     * @param ownerUserId 资源创建者userId
     * @param userId 当前操作用户ID
     * @return
     * @throws
     */
    Boolean checkResouceDataLimit(String ownerUserId,String userId);

    /**
     * 根据用户的数据权限类型，查询其能共享资源的其他用户id集合
     * @param userId
     * @return
     */
    List<String> getPowerScopeUserIds(String userId);


}
