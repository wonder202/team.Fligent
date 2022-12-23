package com.example.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AirportStoreListDTO {
    List<AirportStoreDTO> item;

    Long numOfRows;     // 한 페이지 결과수
    Long pageNo;	    // 페이지수 
    Long totalCount;    // 데이터 총 개수
}
