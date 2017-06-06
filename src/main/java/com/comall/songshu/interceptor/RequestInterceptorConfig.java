package com.comall.songshu.interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 请求拦截器配置
 *
 * @author liushengling
 * @create 2017-06-06-10:31
 **/
//@EnableWebMvc
//@Configuration
public class RequestInterceptorConfig extends WebMvcConfigurerAdapter{


//    @Bean
//    RequestInterceptor loadRequestInterceptor(){
//        return  new RequestInterceptor();
//    }
//
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册拦截器
//        registry.addInterceptor(loadRequestInterceptor()).addPathPatterns("/**");
//    }
}


