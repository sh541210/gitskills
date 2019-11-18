package com.iyin.sign.system.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * @ClassName: FilterConfig
 * @Description: 过滤器配置类
 * @Author: luwenxiong
 * @CreateDate: 2019/6/22 16:23
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/22 16:23
 * @Version: 0.0.1
 */
@Component
@ConfigurationProperties(prefix = "filter")
public class FilterConfig {

    private String ignores;

    public String getIgnores() {
        return ignores;
    }

    public void setIgnores(String ignores) {
        this.ignores = ignores;

    }


}
