package com.iyin.sign.system.common.utils;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName: SpringContextUtil
 * @Description: 添加getActiveProfile方法, 获取当前环境
 * @Author: yml
 * @CreateDate: 2019/6/20
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/20
 * @Version: 1.0.0
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context = null;

    /**
     * spring获取bean工具类
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 获取当前环境参数  exp: dev,prod,test
     * @return
     */
    public static String getActiveProfile() {
        String []profiles = context.getEnvironment().getActiveProfiles();
        if( ! ArrayUtils.isEmpty(profiles)){
            return profiles[0];
        }
        return "";
    }
}