package com.hxut.mrs.exception;

/**
 * description: BizException
 * date: 2023/3/31 10:20
 * author: MR.孙
 */
public class BizException extends RuntimeException{

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int status) {
        this.code = code;
    }

    public BizException(String message){
        super(message);
    }

    public BizException(String message, int code){
        super(message);
        this.code = code;
    }


}
