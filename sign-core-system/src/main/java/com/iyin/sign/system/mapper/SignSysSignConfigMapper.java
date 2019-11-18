package com.iyin.sign.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.entity.SignSysSignConfig;

/**
 * <p>
 * 签章配置设置表 Mapper 接口
 * </p>
 *
 * @author wdf
 * @since 2019-07-22
 */
public interface SignSysSignConfigMapper extends BaseMapper<SignSysSignConfig> {

    /**
    	* 获取签章设置
    	* @Author: wdf 
        * @CreateDate: 2019/7/23 10:42
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/7/23 10:42
    	* @Version: 0.0.1
        * @return com.iyin.sign.system.entity.SignSysSignConfig
        */
    SignSysSignConfig getSignConfig();
}
