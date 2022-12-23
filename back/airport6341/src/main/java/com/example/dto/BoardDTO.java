package com.example.dto;

import java.sql.Timestamp;

import lombok.Data;

// 인섭님이 만든거
@Data
public class BoardDTO {
    Long BNO ;
    String TITLE ;
    String CONTENT ;
    Long HIT ;
    Timestamp REGDATE ;
    Long USERLIKECOUNT; // 사용자의 좋아요 개수
    String USERID;
}
