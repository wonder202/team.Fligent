package com.example.entity;

import org.springframework.beans.factory.annotation.Value;

public interface BoardProject {
    // 글번호
    Long getBno();

    //제목
    String getTitle();
    
    // 내용
    String getContent();

    // 작성자(Member에서 userid꺼내기)
    @Value("#{target.member?.userid}")
    String getMemberUserid(); // member => getMember() 판매자 객체

    // 작성자(Member에서 nickname꺼내기)
    @Value("#{target.member?.nickname}")
    String getMemberNickname(); // member => getMember() 판매자 객체    

    // // 작성자(Member에서 member꺼내기)
    // @Value("#{target.member}")
    // Member getMember(); // member => getMember() 판매자 객체    

    
    // 조회수
    String getHit();

}
