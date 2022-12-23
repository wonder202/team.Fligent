package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.AirportYoutubeUrlDTO;

@Mapper
public interface AirportYoutubeUrlMapper {

    // youtube url DB 저장
    public int insertYoutubeUrl(AirportYoutubeUrlDTO aUrlDTO);

    // youtube url 공항 별 일괄 조회
    public List<AirportYoutubeUrlDTO> selectYoutubeUrl(String airportCode);
}
