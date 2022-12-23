package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.AirportStoreDTO;
import com.example.dto.AirportStoreImageDTO;
import com.example.mapper.AirportStoreMapper;


@RestController
@RequestMapping(value = "/api/airportstore")
public class AirportStoreRestController {

    @Autowired AirportStoreMapper airportStoreMapper;

    ResourceLoader resourceLoader;

    // 페이지에 들어갔을때 조회 하면 됨
    // 127.0.0.1:8080/ROOT/api/airportstore/selectairportcode.json?airportCode=GMP
    @GetMapping(value = "/selectairportcode.json")
    public Map<String, Object> selectairportCodeGET(
        @RequestParam(name = "airportCode") String airportCode,
        @RequestParam(name = "start") int start,
        HttpServletRequest request
    ){
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, Object> map1 = new HashMap<>();

            int page = start*10 - 9 ;
            map1.put("airportCode", airportCode);
            map1.put("start", page);

            List<AirportStoreDTO> list = airportStoreMapper.selectairportCode(map1);
            Long count = airportStoreMapper.selectairportCodeCount(map1);

            for(int i=0; i<list.size(); i++){

                Long airportstoreno = list.get(i).getAirportstoreno();
                Long simageno = airportStoreMapper.airportStoreImageNoSelect(airportstoreno);

                // 가져온 simgno로 imgurl 생성
                String imgurl = request.getContextPath() +  
                "/api/airportstore/image?simageno=" + simageno;

                list.get(i).setImageurl(imgurl);
            }
            if (list != null) {
                map.put("status", 200);
                map.put("airportlist", list);
                map.put("count", count);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // (김포,김해,제주)상업시설 이미지 일괄등록
    // 127.0.0.1:8080/ROOT/api/airportstore/insertbatchairportimage.json?airportstoreno=1
    @PostMapping(value = "/insertbatchairportimage.json" )
    public Map<String, Object> insertBatchAirportImageGET( 
        @RequestParam(name = "airportstoreno") Long airportstoreno,
        @RequestParam(name = "file") MultipartFile[] file)
    throws IOException {

    Map<String, Object> map = new HashMap<>();
    try {
        List<AirportStoreImageDTO> list = new ArrayList<>();
        for (int i = 0; i < file.length; i++) {
            AirportStoreImageDTO airportStoreImage = new AirportStoreImageDTO(); 
            airportStoreImage.setImagedata(file[i].getBytes());
            airportStoreImage.setImagename(file[i].getOriginalFilename());
            airportStoreImage.setImagetype(file[i].getContentType());
            airportStoreImage.setImagesize(file[i].getSize());    
            airportStoreImage.setAirportstoreno(airportstoreno);

            list.add(airportStoreImage);
        }
        
        int list1 = airportStoreMapper.insertbatchAirportImage(list);
        if (list1 == 1) {
            map.put("status", 200);
            System.out.println("등록완료");
        }

        else {
            map.put("status", 0);
        }
        }
        catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage()); 
        } 
        return map;  
    }



    // (인천)상업시설 이미지 일괄등록
    // 127.0.0.1:8080/ROOT/api/airportstore/insertbatchicnairportimage.json?airportstoreno=1
    @PostMapping(value = "/insertbatchicnairportimage.json" )
    public Map<String, Object> insertBatchIcnAirportimage( 
        @RequestParam(name = "airportstoreno") Long airportstoreno,
        @RequestParam(name = "file") MultipartFile[] file)
    throws IOException {

    Map<String, Object> map = new HashMap<>();
    try {
        List<AirportStoreImageDTO> list = new ArrayList<>();
        for (int i = 0; i < file.length; i++) {
            AirportStoreImageDTO airportStoreImage = new AirportStoreImageDTO(); 
            airportStoreImage.setImagedata(file[i].getBytes());
            airportStoreImage.setImagename(file[i].getOriginalFilename());
            airportStoreImage.setImagetype(file[i].getContentType());
            airportStoreImage.setImagesize(file[i].getSize());    
            airportStoreImage.setAirportstoreno(airportstoreno);

            list.add(airportStoreImage);
        }
        
        int list1 = airportStoreMapper.insertBatchIcnAirportimage(list);
        if (list1 == 1) {
            map.put("status", 200);
            System.out.println("등록완료");
        }

        else {
            map.put("status", 0);
        }
        }
        catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage()); 
        } 
        return map;  
    }



    // 이미지 url 에 담을 이미지 정보
    @GetMapping(value="/image")
    public ResponseEntity<byte[]> imageGET( @RequestParam(name="simageno") Long simageno) throws IOException{
        if(simageno > 0L) {           
            AirportStoreImageDTO aImageDTO = airportStoreMapper.selectAirportStoreimage(simageno);  
            if(aImageDTO.getImagesize() > 0L) {
                // 타입설정 png인지 jpg인지 gif인지
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType( MediaType.parseMediaType( aImageDTO.getImagetype() ) );
                // 실제이미지데이터, 타입이포함된 header, status 200    
                return new ResponseEntity<>(aImageDTO.getImagedata(), headers, HttpStatus.OK);
            }
            else {
                InputStream is = resourceLoader.getResource("classpath:/static/image/default.jpg")
                    .getInputStream();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                // 실제이미지데이터, 타입이포함된 header, status 200    
                return new ResponseEntity<>(is.readAllBytes(), headers, HttpStatus.OK);
            }
        }
        else {
            InputStream is = resourceLoader.getResource("classpath:/static/image/default.jpg").getInputStream();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            // 실제이미지데이터, 타입이포함된 header, status 200    
            return new ResponseEntity<>(is.readAllBytes(), headers, HttpStatus.OK);
        }
    }

    // 페이지 들어가서 카테고리로 상업시설 select할때
    // 127.0.0.1:8080/ROOT/api/airportstore/selecttypeofbussiness.json?airportCode=GMP&typeOfBusiness=식음료업&start=1
    @GetMapping(value = "/selecttypeofbussiness.json")
    public Map<String, Object> selectairportCodeGET(
        @RequestParam(name = "airportCode") String airportCode,
        @RequestParam(name = "typeOfBusiness") String typeOfBusiness,
        @RequestParam(name = "start") int start,
        HttpServletRequest request
    ){
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, Object> map1 = new HashMap<>();
            int page = start*11 - 9 ;

            map1.put("typeOfBusiness", typeOfBusiness);
            map1.put("airportCode",airportCode);
            map1.put("start", page);
            Long count = airportStoreMapper.selectTypeofBusinessCount(map1);
            List<AirportStoreDTO> catelist = airportStoreMapper.selectTypeOfBusiness(map1);            
            for(int i=0; i<catelist.size(); i++){

                Long airportstoreno = catelist.get(i).getAirportstoreno();
                Long simageno = airportStoreMapper.airportStoreImageNoSelect(airportstoreno);

                // 가져온 simgno로 imgurl 생성
                String imgurl = request.getContextPath() +  
                "/api/airportstore/image?simageno=" + simageno;

                catelist.get(i).setImageurl(imgurl);
            }


            map.put("status", 200);
            map.put("catelist", catelist);
            map.put("count", count);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }



    // 공항 페이지에서 해시태그를 눌렀을때(해시태그를 이용해서 게시판으로 이동한 다음 해시태그 번호 조건 하에 검색)
    // 127.0.0.1:8080/ROOT/api/airportstore/searchhashtagairport.json
    // @GetMapping(value = "/searchhashtagairport.json")
    // public Map<String, Object> searchhashtagairportGET(
    //     @RequestParam(name = "hno") Long hno,
    //     @RequestParam(name = "start") int start,
    //     HttpServletRequest request
    // ){
    //     Map<String, Object> map = new HashMap<>();
    //     try {
    //         Map<String, Object> map1 = new HashMap<>();
    //         int page = start*11 - 9 ;

    //         map1.put("hno", hno);
    //         map1.put("start", page);
    //         Long count = airportStoreMapper.searchHashtagAirportCount(map1);
    //         List<BoardMainDTO> list = airportStoreMapper.searchHashtagAirport(map1);
    //         if (list != null) {
    //             map.put("status", 200);
    //             map.put("list", list);
    //             map.put("count", count);
    //         } else {
                
    //         }
           
            
    //     }
    //     catch (Exception e) {
    //         map.put("status", -1);
    //         map.put("result", e.getMessage());
    //     }
    //     return map;
    // }
}
