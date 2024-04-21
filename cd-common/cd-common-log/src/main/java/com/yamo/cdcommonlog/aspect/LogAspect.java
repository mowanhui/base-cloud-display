package com.yamo.cdcommonlog.aspect;

import cn.hutool.json.JSONUtil;
import com.yamo.cdcommoncore.domain.pojo.LoginUser;
import com.yamo.cdcommoncore.utils.RequestUtils;
import com.yamo.cdcommonlog.annotation.OptLog;
import com.yamo.cdcommonlog.annotation.OptLogTag;
import com.yamo.cdcommonlog.enums.LogCategoryEnums;
import com.yamo.cdcommonlog.enums.LogTypeEnum;
import com.yamo.cdcommonlog.mq.LogMqSender;
import com.yamo.cdcommonlog.pojo.LogInfo;
import com.yamo.cdcommonlog.utils.LogUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志切面
 *
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/28 19:17
 */
@Aspect
//@Component
@Order(2)
public class LogAspect {
    @Autowired
    private LogMqSender logMqSender;

    @Pointcut("@annotation(com.yamo.cdcommonlog.annotation.OptLog)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        LogInfo logInfo = null;
        long startTime = System.currentTimeMillis();
        //执行目标方法
        try {
            //前置通知
            logInfo = getSysLog(joinPoint);
            result = joinPoint.proceed();
            //返回通知
            long endTime = System.currentTimeMillis();
            logInfo.setTime(endTime - startTime);
            logInfo.setResponse(JSONUtil.toJsonStr(result));
            logInfo.setLogType(LogTypeEnum.NORMAL);
            logMqSender.sendLogMsg(logInfo);
            return result;
        } catch (Exception e) {
            //异常通知
            if (logInfo != null && logInfo.getLogType() != null && logInfo.getLogType() == LogTypeEnum.ERROR) {
                long endTime = System.currentTimeMillis();
                logInfo.setTime(endTime - startTime);
                logInfo.setException(getErrorInfo(e));
                logMqSender.sendLogMsg(logInfo);
            }
            throw e;
        }
    }

    public LogInfo getSysLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OptLog optLog = method.getAnnotation(OptLog.class);
        LogInfo logInfo = null;
        Map<String, String> param = null;
        logInfo = LogUtils.getSysLog(RequestUtils.getRequest());
        LoginUser loginUser = RequestUtils.getLoginUserV2();
        if(loginUser!=null){
            logInfo.setCreateBy(loginUser.getUserId());
            logInfo.setUpdateBy(loginUser.getUserId());
        }
        //获取操作项
        OptLogTag optLogTag = joinPoint.getTarget().getClass().getAnnotation(OptLogTag.class);
        if (optLogTag != null) {
            logInfo.setLogTag(optLogTag.value());
            logInfo.setLogSource(optLogTag.logSource());
        }
        if (optLog.error()) {
            logInfo.setLogType(LogTypeEnum.ERROR);
        }
        logInfo.setTitle(optLog.value());
        logInfo.setLogCategory(LogCategoryEnums.SERVICE);
        logInfo.setOptAction(optLog.optAction());
        return logInfo;
    }

    public Map<String, String> convertMap(Map<String, String[]> paramMap) {
        Map<String, String> map = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            map.put(key, paramMap.get(key)[0]);
        }
        return map;
    }

    public String getErrorInfo(Throwable e) {
        return stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace());
    }

    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stet : elements) {
            sb.append(stet).append("\n");
        }
        return exceptionName + ":" + exceptionMessage + "\n\t" + sb.toString();
    }
}
