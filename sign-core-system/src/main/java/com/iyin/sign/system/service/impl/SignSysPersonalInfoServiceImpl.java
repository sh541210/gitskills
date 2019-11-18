package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.entity.SignSysPersonalInfo;
import com.iyin.sign.system.mapper.SignSysPersonalInfoMapper;
import com.iyin.sign.system.service.SignSysPersonalInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SignSysPersonalInfoServiceImpl
 * @Description: 个人信息service
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 18:42
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 18:42
 * @Version: 0.0.1
 */
@Service
@Slf4j
public class SignSysPersonalInfoServiceImpl extends ServiceImpl<SignSysPersonalInfoMapper,SignSysPersonalInfo> implements SignSysPersonalInfoService {
}
