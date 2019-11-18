package com.iyin.sign.system.common.interfaces;

import java.lang.annotation.*;

/**
 * @ClassName: CacheRemove
 * @Description: 功能描述:需要清除的当前类型--当前类
 * @Author: yml
 * @CreateDate: 2019/8/2
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/2
 * @Version: 1.0.0
 */
@Target({ElementType.METHOD,ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheRemove {

    /**
     * 需要清除的大类
     */
    String value() default "";


    /**
     * 需要清除的具体的额类型
     */
    String key() default "";

}