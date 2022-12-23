package com.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.AirportStoreDTO;
import com.example.dto.ApiFlyDTO;

@Mapper
public interface ApiMapper {

    public int airportleasecontractinfo(List<AirportStoreDTO> list);

    public int deletedata();

    public int dropseq();

    public int createseq();

    public int apisave(List<ApiFlyDTO> info);

    public long apiselectcount(Map<String,Object> map);

    public List<ApiFlyDTO> apiselectlist(Map<String,Object> map);
}
