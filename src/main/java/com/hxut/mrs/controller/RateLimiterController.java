package com.hxut.mrs.controller;

import com.hxut.mrs.annotation.RateLimiter;
import com.hxut.mrs.enums.LimitType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * description: RateLimiterController
 * date: 2023/3/31 10:26
 * author: MR.孙
 */
@RestController
public class RateLimiterController {

    /**
     * 测试接口， 根据IP 1秒钟之内只能访问1次，
     * @return
     */
    @RateLimiter(time = 1, count = 1, limitType = LimitType.IP)
    @GetMapping("/test")
    public String test() {

        return "test>>>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    }


}
