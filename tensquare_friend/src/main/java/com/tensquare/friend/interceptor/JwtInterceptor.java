package com.tensquare.friend.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//JWT令牌拦截器，对所有进行判断，但都会放行。如果有令牌就记录在头消息，没有就不记录
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");//获取头信息

        //如果真的有这个头消息,就进行判定。如果有这个令牌，并且通过了一些列判断，再设置一个消息头键值claims_xxx
        // 然后service那边就getAttribute(claims_xxx)进行判断，而不是还是用Authorization判断
        if (header!=null && !"".equals(header)){
            //如果头消息以Bearer 开头的话
            if (header.startsWith("Bearer ")){
                String token = header.substring(7);
                try {
                    //将token转义
                    Claims claims = jwtUtil.parseJWT(token);

                    //得到角色
                    String roles = (String) claims.get("roles");
                    //判断角色是啥
                    if (roles!=null && roles.equals("admin")){
                        request.setAttribute("claims_admin",token);
                    }
                    if (roles!=null && roles.equals("user")){
                        request.setAttribute("claims_user",token);
                    }
                }catch (Exception e){
                    throw new RuntimeException("令牌有误");
                }
            }
        }

        return true;//都放行
    }
}
