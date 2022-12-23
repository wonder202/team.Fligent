package com.example.restcontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.AirportYoutubeUrlDTO;
import com.example.mapper.AirportYoutubeUrlMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value = "/api/airportyoutube")
public class AirportYoutubeUrlRestController {
    

    @Autowired AirportYoutubeUrlMapper aUrlMapper;

    // youtube url DB 저장
    // 127.0.0.1:8080/ROOT/api/airportyoutube/insertyoutubeurl.json
    @PostMapping(value="insertyoutubeurl.json")
    public Map<String,Object> InsertYoutubeUrl(@RequestBody AirportYoutubeUrlDTO aUrlDTO) {
        Map<String,Object> map = new HashMap<>();
        try {
            int ret = aUrlMapper.insertYoutubeUrl(aUrlDTO);
            if (ret == 1) {
                map.put("status", 200);
                map.put("result", "등록성공");
            } else {
                map.put("status", 0);
                map.put("result", "등록실패");
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // youtube url 공항 별 일괄 조회
    // 127.0.0.1:8080/ROOT/api/airportyoutube/selectyoutubeurl.json?airportCode=GMP
    @GetMapping(value = "selectyoutubeurl.json")
    public Map<String,Object> SelectYoutubeUrl(
        @RequestParam(name = "airportCode") String airportCode
    ) {
        Map<String,Object> map = new HashMap<>();
        try {
            List<AirportYoutubeUrlDTO> list = aUrlMapper.selectYoutubeUrl(airportCode);
            if (list != null) {
                map.put("status", 200);
                map.put("list", list);  
            } else {
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    
}
