package com.example.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ItemImgDTO {
    Long iimageno;          // 아이템 이미지 번호
    byte[] imagedata;       // 아이템 이미지 데이터
    String imagetype;       // 아이템 이미지 타입
    Long imagesize;         // 아이템 이미지 크기
    String imagename;       // 아이템 이미지 이름
    Date regdate;           // 등록날짜
    Long ino;               // 아이템 번호
}
