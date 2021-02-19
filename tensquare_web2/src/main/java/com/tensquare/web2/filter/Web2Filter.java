package com.tensquare.web2.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @description：TODO
 */
@Component
public class Web2Filter extends ZuulFilter {
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
     * 具体逻辑,不要丢失头消息
     * @param
     * @return java.lang.Object
     */

    @Override
    public Object run() throws ZuulException {
        //得到request上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //得到request域
        HttpServletRequest request = requestContext.getRequest();
        //得到头消息
        String header = request.getHeader("Authorization");
        //判断是否有该头消息
        if (header != null && !"".equals(header)) {
            requestContext.addZuulRequestHeader("Authorization",header);
        }

        return null;
    }
}
