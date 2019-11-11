package com.checkcode.common;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author gwx556610
 * @Date: 20:29 2019/7/18
 * @function:
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    private static final int executeTimeMill_50 = 50;

    /**
     * 需要拦截的class 以及 method
     */
    @Pointcut("execution(public * com.checkcode.controller.*.*(..)) " +
            "&& !execution(public * com.checkcode.controller.UploadFileController.*(..)) " +
            "& !execution(public * com.checkcode.controller.PageController.*(..))")
    public void logPointCut() {
    }


    /**
     * 环绕通知 @Around
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object target = point.getTarget();
        Signature sig = point.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("annotation use error");
        }
        msig = (MethodSignature) sig;
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        Object[] args = point.getArgs();
        String paramJson = "";
        try {
            if (args.length > 0) {
                paramJson = JSONObject.toJSONString(args[0]);
            }
        } catch (Exception ex) {
            log.info("analysis args has errors!", ex);
        }
        log.info("[METHOD]:{}, [TIME]:{}, [PARAM]:{}", currentMethod.getName(), beginTime, paramJson);
        Object result = point.proceed();
        try {
            JSONObject resultJson = JSONObject.parseObject(JSONObject.toJSONString(result));
            if (resultJson.getIntValue("code") != 200) {
                log.info("[METHOD]:{}, [TIME]:{}, [RESULT]:{}, [COSTTIME] : {}", currentMethod.getName(), beginTime, JSONObject.toJSONString(result), System.currentTimeMillis() - beginTime);
            } else {
                long costTime = System.currentTimeMillis() - beginTime;
                if (costTime > executeTimeMill_50) {
                    log.info("[METHOD]:{}, [TIME]:{}, [COSTTIME] : {}", currentMethod.getName(), beginTime, costTime);
                    log.debug("[METHOD]:{}, [TIME]:{}, [RESULT]:{}, [COSTTIME] : {}", currentMethod.getName(), beginTime, JSONObject.toJSONString(result), costTime);
                }
            }
        } catch (Exception ex) {
            log.warn("the result isn't json data.");
        }
        return result;
    }

}