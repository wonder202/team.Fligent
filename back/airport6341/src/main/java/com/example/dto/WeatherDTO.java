package com.example.dto;

import lombok.Data;

@Data
public class WeatherDTO {
    String baseDate ; // 발표일자
    String baseTime ; // 발표시각
    String PTYcategory; // 카테고리(강수형태)
    String PTYValue ; // 강수형태 값
    String REHcategory; // 카테고리(습도)
    String REHValue ; // 습도 값
    String SKYcategory; // 카테고리(하늘상태)
    String SKYValue ; // 하늘상태 값
    String TMPcategory; // 카테고리(1시간 기온)
    String TMPValue ; // 1시간 기온 값
}
