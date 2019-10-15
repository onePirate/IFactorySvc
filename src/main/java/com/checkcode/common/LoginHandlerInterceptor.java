package com.checkcode.common;

import com.checkcode.common.entity.TokenModel;
import com.checkcode.common.tools.TokenTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Value("${token.timeout}")
    long timeout;

    @Value("${token.switch}")
    boolean tokenSwitch;

    //目标方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        if (!tokenSwitch) {
            return true;
        }
        if (request.getRequestURI().equals("/")
            || StringUtils.isEmpty(request.getRequestURI())){
            return false;
        }
        String token = request.getHeader("token");
        if (token == null) {
            throw new CustomerException(StateEnum.USER_NOT_LOGIN);
        }
        TokenModel tokenModel = TokenTool.getTokenInfo(token);
        if (tokenModel == null) {
            throw new CustomerException(StateEnum.USER_NOT_LOGIN);
        }
        long curTime = System.currentTimeMillis();
        //如果超过指定token有限期，则退出登录
        if ((curTime-tokenModel.getLoginTime())>timeout){
            TokenTool.removeToken(token);
            throw new CustomerException(StateEnum.USER_LOGIN_TIMEOUT);
        }
        TokenTool.updateTokenInfo(token);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{

    }
}