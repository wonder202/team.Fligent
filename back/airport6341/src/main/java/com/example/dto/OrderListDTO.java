package com.example.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class OrderListDTO {
    Long ono;       // 주문 번호
    Long cnt;       // 주문 수량
    Date regdate;   // 주문 날짜
    Long ino;       // 아이템 번호
    String userid;  // 유저 아이디
    String name;    // 아이템 이름
    Long price;     // 아이템 가격
    Long ordercode; // 주문 그룹핑 번호
}
