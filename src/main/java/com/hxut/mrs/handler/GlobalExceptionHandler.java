package com.hxut.mrs.handler;

import com.hxut.mrs.exception.BizException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * description: GlobalExceptionHandler
 * date: 2023/3/31 10:21
 * author: MR.孙
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public Map<String,Object> handler(BizException e){
        Map<String,Object> map = new HashMap<>();
        map.put("message",e.getMessage());
        map.put("code",e.getCode());
        return map;
    }

}
