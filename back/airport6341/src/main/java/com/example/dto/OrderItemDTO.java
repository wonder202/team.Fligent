package com.example.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    Long cno;       // 장바구니 번호
    Long cnt;       // 주문 수량
    Long ino;       // 아이템 번호
    String name;    // 아이템 이름
    Long price;     // 아이템 가격
    String userid;  // 유저아이디
    Long tot;       // 아이템 총가격 (수량x아이템 개당 가격)

}
