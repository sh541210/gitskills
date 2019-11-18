package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.entity.SignSysDefaultConfig;

/**
 * @ClassName: SignSysDefaultConfigMapper
 * @Description: SignSysDefaultConfigMapper
 * @Author: luwenxiong
 * @CreateDate: 2019/8/5 18:01
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/5 18:01
 * @Version: 0.0.1
 */
public interface SignSysDefaultConfigMapper extends BaseMapper<SignSysDefaultConfig> {

    /**
    　　* @description: 获取最新配置
    　　* @return SignSysDefaultConfig
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 16:42
    　　*/
    SignSysDefaultConfig selectLastConfig();
}
