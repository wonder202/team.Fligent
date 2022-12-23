package com.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.AirportStoreDTO;
import com.example.dto.AirportStoreImageDTO;

@Mapper
public interface AirportStoreMapper {
    
    // 페이지 들어가서 상업시설 조회
    public List<AirportStoreDTO> selectairportCode(Map<String,Object> map);

    // 페이지 들어가서 상업시설 개수 구하기
    public Long selectairportCodeCount(Map<String,Object> map);

    // 페이지 들어가서 판매업종 카테고리 조회
    public List<AirportStoreDTO> selectTypeOfBusiness(Map<String,Object> map);

    // 판매업종 카테고리 조회중 상업시설 개수 구하기  
    public Long selectTypeofBusinessCount(Map<String,Object> map);

    // (김포,김해,제주)이미지 등록 하기
    public int insertbatchAirportImage(List<AirportStoreImageDTO> list);

    // (인천)이미지 등록 하기
    public int insertBatchIcnAirportimage(List<AirportStoreImageDTO> list);

    // 이미지 조회 하기
    public AirportStoreImageDTO selectAirportStoreimage( Long simageno );
    
    // 이미지 url 번호 가져오기
    public Long airportStoreImageNoSelect(Long airportstoreno);

    // 공항 페이지 별 해시태그 검색
    // public List<BoardMainDTO> searchHashtagAirport(Map<String,Object> map);

    // 공항 페이지 별 해시태그 검색한 결과 글 개수
    // public Long searchHashtagAirportCount(Map<String,Object> map);

}
