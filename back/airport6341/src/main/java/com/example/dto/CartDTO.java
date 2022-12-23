package com.example.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class CartDTO {
    Long cno;       // 장바구니 번호
    Long cnt;       // 장바구니 수량
    Date regdate;   // 등록일
    Long ino;       // 아이템 번호
    String userid;  // 유저아이디
    String name;    // 아이템 이름
    Long price;     // 아이템 가격
    Long tot;       // 아이템 총가격 (수량x아이템 개당 가격)
    String imageurl;   //  이미지를 담을 url 변수 만들기
}
