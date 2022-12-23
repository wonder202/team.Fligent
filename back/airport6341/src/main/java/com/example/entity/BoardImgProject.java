package com.example.entity;

import org.springframework.beans.factory.annotation.Value;

public interface BoardImgProject {
    Long bimgaeno();

    // 작성자(Member에서 userid꺼내기)
    @Value("#{target.member?.userid}")
    String getMemberUserid(); // member => getMember() 판매자 객체
}
