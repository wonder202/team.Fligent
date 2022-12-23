package com.example.dto;

import lombok.Data;

@Data
public class AirportYoutubeUrlDTO {
    Long yno;           // 유튜브 url 번호
    String youtubeUrl;  // 유튜브 영상 주소
    String airportCode; // 공항 코드
}
