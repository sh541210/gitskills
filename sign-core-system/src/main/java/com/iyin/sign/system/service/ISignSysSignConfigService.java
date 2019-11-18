package com.iyin.sign.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysSignConfig;

/**
 * <p>
 * 签章配置设置表 服务类
 * </p>
 *
 * @author wdf
 * @since 2019-07-22
 */
public interface ISignSysSignConfigService extends IService<SignSysSignConfig> {

    /**
    	* 获取签章设置
    	* @Author: wdf 
        * @CreateDate: 2019/7/23 10:43
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/7/23 10:43
    	* @Version: 0.0.1
        * @return com.iyin.sign.system.entity.SignSysSignConfig
        */
    SignSysSignConfig getSignConfig();
}
