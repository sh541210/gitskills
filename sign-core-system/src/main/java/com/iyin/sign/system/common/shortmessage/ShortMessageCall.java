package com.iyin.sign.system.common.shortmessage;


import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * 短信接口调用切面注解
 * @Author:      luwenxiong
 * @CreateDate:  2019/9/3
 * @UpdateUser:
 * @UpdateDate:  2019/9/3
 * @Version:     0.0.1
 * @return
 * @throws
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ShortMessageCall {
}
