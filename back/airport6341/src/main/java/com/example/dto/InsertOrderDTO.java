package com.example.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InsertOrderDTO {
    Long ono;       // 주문 번호
    Long cnt;       // 주문 수량
    Date regdate;   // 주문 날짜
    Long ino;       // 아이템 번호
    String userid;  // 유저 아이디
    Long ordercode; // 주문 그룹핑 코드(수동 시퀀스)
    Long cno;       // 장바구니 번호
}
