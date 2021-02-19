package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @description：TODO
 */
@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     *
     * @param
     * @return pre 前置过滤器
     *          route 请求时调用
     *          post路由请求和error过滤器之后调用
     *          error处理请求时发生错误时调用
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器级别，越小越先执行
     * @param
     * @return int
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否开启过滤器
     * @param
     * @return boolean
     */

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 具体逻辑,安全验证，返回null就是放行，requestContext.setSendZuulResponse(false); 就是终止运行
     * @param
     * @return java.lang.Object
     */

    @Override
    public Object run() throws ZuulException {
        //得到request上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //得到request域
        HttpServletRequest request = requestContext.getRequest();

        //我也不懂。。。
        if (request.getMethod().equals("OPTIONS")) {
            return null;
        }

        //登陆的时候不做拦截
        //查找改请求路径是否有login字段，有的话就放行
        if (request.getRequestURI().indexOf("login")>0) {
            return null;
        }

        //得到头消息
        String header = request.getHeader("Authorization");
        //判断是否有该头消息且Bearer 开头
        if (header != null && !"".equals(header)&&header.startsWith("Bearer ")) {
            //从第7个开始
            String token = header.substring(7);
            System.out.println(token);
            try {
                //转换
                Claims claims = jwtUtil.parseJWT(token);
                //如果不是空
                if (claims!=null){
                    //得到角色
                    if ("admin".equals(claims.get("roles"))){
                        //转发
                        requestContext.addZuulRequestHeader("Authorization",header);
                        return null;//null放行
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                requestContext.setSendZuulResponse(false);//终止运行
            }

        }

        //没有admin权限的话
        requestContext.setSendZuulResponse(false);//终止运行
        requestContext.setResponseStatusCode(401);//http状态码
        requestContext.setResponseBody("无权访问");
        requestContext.getResponse().setContentType("text/html;charset=UTF-8");

        return null;
    }
}
