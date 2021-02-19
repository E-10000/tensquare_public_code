package com.tensquare.user.config;

import com.tensquare.user.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//拦截器配置类，进行拦截器注册
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Autowired
    JwtInterceptor jwtInterceptor;

    //添加拦截器
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)//注册拦截器
                .addPathPatterns("/**")//对所有路径拦截
                .excludePathPatterns("/**/login/**")//但对登陆不拦截
                .excludePathPatterns("/**/sendsms/**")//对发短信不拦截
                .excludePathPatterns("/**/register/**");//对注册不拦截

    }
}
