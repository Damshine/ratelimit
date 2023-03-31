package com.hxut.mrs.annotation;


import com.hxut.mrs.enums.LimitType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    /**
     * 限流key前缀
     * @return
     */
    String key() default "rate_limit:";

    /**
     * 限流时间(单位秒)
     * @return
     */
    int time() default 60;

    /**
     * 限流次数，默认100次
     * @return
     */
    int count() default 100;

    /**
     * 限流类型，默认全局限流
     * @return
     */
    LimitType limitType() default LimitType.DEFAULT;



}
