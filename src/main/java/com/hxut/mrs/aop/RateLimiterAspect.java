package com.hxut.mrs.aop;

import com.hxut.mrs.annotation.RateLimiter;
import com.hxut.mrs.enums.LimitType;
import com.hxut.mrs.exception.BizException;
import com.hxut.mrs.util.IpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * description: RateLimiterAspect
 * date: 2023/3/31 9:42
 * author: MR.孙
 */
@Component
@Aspect
public class RateLimiterAspect {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterAspect.class);

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private RedisScript<Long> limitScript;

    @Before("@annotation(rateLimiter)")
    public void before(JoinPoint joinPoint, RateLimiter rateLimiter) {

        int time = rateLimiter.time();//限流时间
        int count = rateLimiter.count();//限流次数
        String combineKey = getCombineKey(rateLimiter, joinPoint);
        List<Object> keys = Collections.singletonList(combineKey);

        //返回的是限流的次数
       try {

           Long number = redisTemplate.execute(limitScript, keys, time, count);
           if (number == null || number.intValue() > count) {

               throw new BizException("请求过于频繁，请稍后重试", 500);

           }
           logger.info("当前请求次数'{}', 限定次数'{}'", number.intValue(), count);

       } catch (BizException e) {
           throw e;
       } catch (Exception e) {
           throw new RuntimeException("服务器限流异常，请稍后重试");
       }


    }


    private String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {

        StringBuffer sb = new StringBuffer(rateLimiter.key());
        //如果为IP限制
        //rate_limit:IP地址-注解所添加方法所在的类的名称-注解所添加方法的名称
        //rate_limit:192.168.30.10-com.melody.limitredis.controller.RateLimiterController-test
        if (rateLimiter.limitType() == LimitType.IP) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            sb.append(IpUtils.getRequestIp(request)).append("-");
        }

        //获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        //获取方法
        Method method = signature.getMethod();
        //获取类
        Class<?> targetClass = method.getDeclaringClass();

        sb.append(targetClass.getName()).append("-").append(method.getName());

        logger.info("key name is ==>{}", sb.toString());

        return sb.toString();
    }



}
