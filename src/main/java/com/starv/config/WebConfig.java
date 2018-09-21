package com.starv.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Created by m1590 on 2018/8/17.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Override
    public  void  addInterceptors(InterceptorRegistry interceptorRegistry){
        interceptorRegistry.addInterceptor(new MyInterceptor())
                .excludePathPatterns("/user/login","/config/get","/user/add","/log/delete");
    }
}
