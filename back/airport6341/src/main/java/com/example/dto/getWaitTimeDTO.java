package com.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class getWaitTimeDTO {
    String IATA_APCD; // 공항코드
    String OPR_STS_CD; // 운영상태 코드
    String PRC_HR; // 조회 기준 시간
    long STY_TCT_AVG_ALL; //전체 구간 소요시간 평균
    long STY_TCT_AVG_A; // 탑승권자동발권-수하물위탁-신분확인
    long STY_TCT_AVG_B; //신분확인-보안검색-탑승시간
    long STY_TCT_AVG_C; //보안검색-탑승시간
    long STY_TCT_AVG_D; //탑승시간-출발
} 
