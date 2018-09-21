package com.starv.util;

/**
 * Created by m1590 on 2018/8/15.
 */
public class ResultUtil {
    public static Result SUCCESS(Object data){
        Result result=new Result();
        result.setMessage("SUCCESS");
        result.setData(data);
        return result;
    }

    public static Result ERROR(Object data){
        Result result=new Result();
        result.setMessage("ERROR");
        result.setData(data);
        return result;
    }
}
