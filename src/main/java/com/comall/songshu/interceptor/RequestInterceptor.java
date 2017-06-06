package com.comall.songshu.interceptor;

import com.comall.songshu.service.RequestCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 请求拦截器
 *
 * @author liushengling
 * @create 2017-06-06-10:31
 **/
public class RequestInterceptor extends HandlerInterceptorAdapter {

//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private RequestCacheService requestCacheService;
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
//        Enumeration<String> names = request.getParameterNames();
//        String name = null;
//        while(names.hasMoreElements()) {
//            name = names.nextElement();
//        }
//        String result = requestCacheService.getRequestCache(request,name);
//        if(result != null){
//            response.getWriter().print(result);
//            return false;
//        }
//        return true;
//    }
}
