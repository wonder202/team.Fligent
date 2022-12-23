package com.example.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.handler.CustomLoginSuccessHandler;
import com.example.handler.CustomLogoutSuccessHandler;
import com.example.service.SecurityLoginService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    final SecurityLoginService securityLoginService;
    final CustomLoginSuccessHandler customLoginsuccessHandler;
    final CustomLogoutSuccessHandler customLogoutSuccessHandler;


    // 필터 설정 하기
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) 
            throws Exception{

        // 권한설정
        // 127.0.0.1:8080/ROOT/admin/****  ADMIN
        // 127.0.0.1:8080/ROOT/customer/***  CUSTOMER
        http.authorizeHttpRequests()
            // .antMatchers("/admin", "/admin/**").hasAuthority("ADMIN")
            // .antMatchers("/customer", "/customer/**").hasAuthority("CUSTOMER")
            .anyRequest().permitAll();

        // 로그인
        http.formLogin()
            // .loginPage("/member/login.json") // GET화면 URL
            // .loginProcessingUrl("/member/login.json") //th:action
            // .usernameParameter("userid")
            // .passwordParameter("userpw")
            // .defaultSuccessUrl("/")
            // .successHandler(customLoginsuccessHandler)
            .permitAll();
        
        // 로그아웃
        http.logout()
            // .logoutUrl("/member/logout.do")
            // .logoutSuccessUrl("/")
            // .logoutSuccessHandler(customLogoutSuccessHandler)
            // .clearAuthentication(true)
            // .invalidateHttpSession(true)
            .permitAll();

        
        // 로그인 시 세션을 서버에 저장하지 않음
        // ALWAYS 항상 세션 생성
        // IF_REQUIRED 기본값
        // NEVER 생성하지만 있으면 기존것 사용
        // STATELESS 생성하지 않고 기존것도 사용하지 않음                
        // http.sessionManagement().sessionCreationPolicy(
        //    SessionCreationPolicy.STATELESS);

        //post호출시 csrf키를 요구하지 않음    
        // http.csrf().disable();    //<= 비권장
        
        http.csrf().ignoringAntMatchers("/api/**", "/member/**", "/css/**", "/js/**", "/img/**");   
        http.headers().frameOptions().sameOrigin(); 

        // 직접 생성한 service 등록                
        http.userDetailsService(securityLoginService);

        // 접근불가 페이지일때 보여지는 url주소
        http.exceptionHandling().accessDeniedPage("/page403.do");
        return http.build();
    }

    // userDetailsService 대체용
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) 
            throws Exception{
            return authenticationConfiguration.getAuthenticationManager();
    }


    // 암호의 hash알고리즘 설정
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
