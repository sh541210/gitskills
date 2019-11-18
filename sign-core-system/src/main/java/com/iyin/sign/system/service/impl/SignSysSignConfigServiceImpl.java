package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.entity.SignSysSignConfig;
import com.iyin.sign.system.mapper.SignSysSignConfigMapper;
import com.iyin.sign.system.service.ISignSysSignConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 签章配置设置表 服务实现类
 * </p>
 *
 * @author wdf
 * @since 2019-07-22
 */
@Service
public class SignSysSignConfigServiceImpl extends ServiceImpl<SignSysSignConfigMapper, SignSysSignConfig> implements ISignSysSignConfigService {

    @Resource
    private SignSysSignConfigMapper signSysSignConfigMapper;

    @Override
    public SignSysSignConfig getSignConfig() {
        return signSysSignConfigMapper.getSignConfig();
    }
}
