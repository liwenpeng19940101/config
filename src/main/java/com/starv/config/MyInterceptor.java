package com.starv.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.invoke.MethodHandle;

/**
 * Created by m1590 on 2018/8/17.
 */
public class MyInterceptor implements HandlerInterceptor {
    public static final Logger logger = LoggerFactory.getLogger(MyInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            logger.info("----------拦截接口-------------");
            HttpSession session = request.getSession();
            logger.info("user_name== "+session.getAttribute("user_name"));
            if (session.getAttribute("user_name") == null) {
                logger.info("----------未登录-------------");
                logger.info("" + request.getContextPath() + "/index.html");
                response.sendRedirect(request.getContextPath() + "/index.html");

                return false;
            }
        }

        return true;
    }
}
