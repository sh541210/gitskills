package com.iyin.sign.system.service;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Service;

/**
 * @author tangdefan
 * @title: AccessLimitService
 * @projectName iyin-sign-system
 * @description: 限流服务
 * @date 2019/8/21 14:32
 */
@Service
public class AccessLimitService {

    /**
     * 每秒设置10个令牌
     */
    RateLimiter rateLimiter = RateLimiter.create(100.0);

    /**
    　　* @description: 尝试获取令牌
    　　* @return boolean
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/21 14:35
    　　*/
    public boolean tryAcquire() {
        return rateLimiter.tryAcquire();
    }
}
