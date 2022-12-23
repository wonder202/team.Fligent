package com.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AirportStoreDTO { // 김포,김해,제주,인천 합친 view
    Long airportstoreno;    // 상업시설번호
    String airportCode; 	// 공항코드
    String airportName; 	// 공항 이름
    String leaseLocation; 	// 임대 위치
    String storeName; 	    // 업체명
    String typeOfBusiness;	// 업종(카테고리)
    String imageurl;        // 이미지 url
}
