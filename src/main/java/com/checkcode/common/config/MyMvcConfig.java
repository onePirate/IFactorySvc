package com.checkcode.common.config;

import com.checkcode.common.LoginHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Autowired
    LoginHandlerInterceptor loginHandlerInterceptor;

    @Bean
    public WebMvcConfigurer webMvcConfigurerAdapter(){
        return new WebMvcConfigurer(){

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                // /**  表示拦截所有路径下的所有请求
                registry.addInterceptor(loginHandlerInterceptor)
                            .excludePathPatterns("/style/**","/layui/**","/signup","/login","/error","/logout","/","/**/**/upload","/**/upload")
                        .addPathPatterns("/**");
            }
        };
    }

}