package com.example.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AirportStoreImageDTO {
    Long simageno;          // 상업시설 이미지 번호
    byte[] imagedata;       // 상업시설 이미지 데이터
    String imagetype;       // 상업시설 이미지 타입
    Long imagesize;         // 상업시설 이미지 크기
    String imagename;       // 상업시설 이미지 이름
    Date regdate;           // 등록날짜
    Long airportstoreno;    // 상업시설 번호
}
