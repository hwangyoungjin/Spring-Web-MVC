package com.example.springbootsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // HttpSecurity를 통해 HTTP 요청에 대한 웹 기반 보안을 구성
        http.authorizeRequests()  // 모든 요청에 대해 접근 설정
                .antMatchers("/", "/hello").permitAll() // antMatchers() 메서드로 url은 승인(permitAll())
                .anyRequest().authenticated() // 이외 나머지 요청은 권한필요
                .and()
                .formLogin()
                //security가 제공하는 로그인 폼 사용, form 기반으로 인증
                //로그인 정보는 기본적으로 HttpSession을 이용
                //login 경로로 접근하면, Spring Security에서 제공하는 로그인 form을 사용가능
                //기본 제공되는 form 말고, 커스텀 로그인 폼을 사용하고 싶으면 loginPage() 메서드를 사용합니다.
                //이 때 커스텀 로그인 form의 action 경로와 loginPage()의 파라미터 경로가 일치해야 인증을 처리할 수 있습니다. ( login.html에서 확인 )
                .and()
                .httpBasic(); //http basic 로그인 권한 (authenticated) 사용
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
