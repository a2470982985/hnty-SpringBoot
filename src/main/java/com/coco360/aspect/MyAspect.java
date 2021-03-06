package com.coco360.aspect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coco360.pojo.RespMsg;
import com.coco360.util.HttpClientUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
/*
    此类使用的推送服务为    推送加 （http://pushplus.hxtrip.com/）
    您如需使用此服务请到此网站注册并获得的token
    在下方填入您的token即可获取消息推送服务
*/
@Component("MyAspect")
@Aspect
public class MyAspect {

    @Pointcut("execution(com.coco360.pojo.RespMsg com.coco360.controller..*(String)) ")
    public void pointcut1() {
    }
    @Pointcut("execution(* com.coco360.controller.TodaySchoolController.getPicture(..))")
    public void pointcut2() {
    }

    @AfterReturning(value = "MyAspect.pointcut1()", returning = "val")
    public void AfterReturning(RespMsg val) {

        if (val.getCode() == 200) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", "xxxxx");//请修改此处的xxxx为您的token
            jsonObject.put("title", "今日校园");
            jsonObject.put("content", JSONObject.toJSON(val));
            jsonObject.put("template", "json");
            try {
                HttpClientUtil.sendJson("http://pushplus.hxtrip.com/send", jsonObject, "utf-8", null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @AfterReturning(value = "MyAspect.pointcut2()")
    public void Before(JoinPoint point){
        String methodName = point.getSignature().getName();
        List<Object> args = Arrays.asList(point.getArgs());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", "xxxxx");//请修改此处的xxxx为您的token
        jsonObject.put("title", "今日校园");
        jsonObject.put("content", JSONObject.toJSON(args.get(1)));
        jsonObject.put("template", "json");
        try {
            HttpClientUtil.sendJson("http://pushplus.hxtrip.com/send", jsonObject, "utf-8", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
