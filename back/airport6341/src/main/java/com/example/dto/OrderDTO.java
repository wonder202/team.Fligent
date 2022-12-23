package com.example.dto;

import java.util.Date;

import lombok.Data;

@Data
public class OrderDTO {
    Long cno;       // 장바구니 번호
    Long cnt;       // 주문 수량
    Long ino;       // 아이템 번호
    Date regdate;   // 주문 날짜
    String userid;  // 유저아이디
    Long ordercode; // 주문 그룹핑 번호

}
