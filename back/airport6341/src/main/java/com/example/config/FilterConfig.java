package com.example.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.jwt.JwtFilter;

// 필터를 통과할 url설정하기
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtFilter> 
        filterRegistrationBean(JwtFilter jwtFilter){

        FilterRegistrationBean<JwtFilter> bean 
            = new FilterRegistrationBean<>();            
        bean.setFilter(jwtFilter);    

        // 아래 명시된 페이지는 jwtFilter동작됨
        // jwtFilter동작 = 토큰 검증
        bean.addUrlPatterns(
            "/api/member/delete.json", //회원 탈퇴시  
            "/api/member/update.json", //회원정보 수정시
            "/api/member/updatepw.json",  // 회원암호 변경시
            "/api/admin/*",  // /api/admin/으로 시작하는 하위페이지 전부 
            "/api/cart/*", // /api/cart/로 시작하는 하위페이지 전부
            "/api/customer/*", // /api/customer/로 시작하는 하위페이지 전부
            "/api/order/*", // 주문페이지 전부
            "/api/board/postlike.json", // 좋아요 누를시 로그인 한 사용자만 가능하게 해야함
            "/api/board/insertboardone.json", // 글 쓸때에 로그인 한 사용자만 가능하게 해야함
            // "/api/board/boardinsertbatchimage.json", // 글 쓸때 이미지 첨부시에도 토큰 갖고있어야함
            "/api/board/updateoneboard.json", // 글 수정때 로그인 한 사용자만 가능하게 해야함
            "/api/board/deleteoneboard.json",   // 글 삭제할때 로그인 한 사용자만 가능하게 해야함
            "/api/board/insertboardreply.json", // 댓글 등록시 로그인한 사용자 필터 
            "/api/board/insertreply.json", // 댓글 등록시 로그인한 사용자 필터 
            "/api/board/hashtagmappingdelete.json", //해시태그 삭제 하기
            "/api/board/updatereply.json", // 댓글 수정하기
            "/api/board/deletereply.json", // 댓글 삭제하기
            "/api/board/compareid.json" // 로그인한 사용자 정보 확인하기
            );

        return bean;
    }


}
