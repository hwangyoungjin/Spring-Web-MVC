package com.example.demowebmvc;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

public class VisitTimeInterceptor implements HandlerInterceptor { //어떤 웹사이트의 접속한 시간을 기록하는 Interceptor

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if(session.getAttribute("visitTime") == null){
            session.setAttribute("visitTime", LocalDateTime.now());
        }
        return true; // 다음 Handler 까지 가도록 true
    }
}
