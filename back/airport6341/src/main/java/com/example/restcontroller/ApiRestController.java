package com.example.restcontroller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.AirportStoreDTO;
import com.example.dto.AirportStoreListDTO;
import com.example.dto.WeatherDTO;
import com.example.dto.getWaitTimeDTO;
import com.example.mapper.ApiMapper;
import com.example.service.AirportService;

@RestController
@RequestMapping(value = "/api")
public class ApiRestController {

    @Autowired
    AirportService aService;

    @Autowired
    ApiMapper aMapper;

    // 단기예보조회
    // 127.0.0.1:8080/ROOT/api/weatherinfo.json
    @GetMapping(value = "/weatherinfo.json")
    public List<WeatherDTO> flightInfoGET(@RequestParam(name = "schAirCode") String schAirCode) {
        try {
            // Map<String, Object> map = new HashMap<>();
            List<WeatherDTO> result = new ArrayList<>();

            String nx = ""; // 예보지점의 X 좌표값
            String ny = ""; // 예보지점의 Y 좌표값
            if (schAirCode.equals("GMP")) {
                nx = "57";
                ny = "127";
            } else if (schAirCode.equals("PUS")) {
                nx = "96";
                ny = "76";
            } else if (schAirCode.equals("CJU")) {
                nx = "52";
                ny = "38";
            } else if (schAirCode.equals("ICN")) {
                nx = "51";
                ny = "125";
            }

            JSONArray weatherinfo = aService.weatherGET(nx, ny);

            WeatherDTO weather = new WeatherDTO();
            for (int i = 0; i < weatherinfo.size(); i++) {
                JSONObject jobj = (JSONObject) weatherinfo.get(i);

                // 예보 발표일자
                weather.setBaseDate(jobj.get("baseDate").toString());
                
                // 예보 발표시각
                weather.setBaseTime(jobj.get("baseTime").toString());

                // 카테고리 값 변환하여 넣기
                if (jobj.get("category").equals("PTY")) { // PTY = 강수형태
                    weather.setPTYcategory("강수형태");

                    // 강수형태(PTY) 코드
                    // (초단기) 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4), 빗방울(5), 빗방울눈날림(6), 눈날림(7)
                    if (jobj.get("fcstValue").equals("0")) {
                        weather.setPTYValue("없음");
                        // map.put("fcstValue", "없음");
                    } else if (jobj.get("fcstValue").equals("1")) {
                        weather.setPTYValue("비");
                        // map.put("fcstValue", "비");
                    } else if (jobj.get("fcstValue").equals("2")) {
                        weather.setPTYValue("비/눈");
                        // map.put("fcstValue", "비/눈");
                    } else if (jobj.get("fcstValue").equals("3")) {
                        weather.setPTYValue("눈");
                        // map.put("fcstValue", "눈");
                    } else if (jobj.get("fcstValue").equals("4")) {
                        weather.setPTYValue("소나기");
                        // map.put("fcstValue", "소나기");
                    } else if (jobj.get("fcstValue").equals("5")) {
                        weather.setPTYValue("빗방울");
                        // map.put("fcstValue", "빗방울");
                    } else if (jobj.get("fcstValue").equals("6")) {
                        weather.setPTYValue("빗방울/눈날림");
                        // map.put("fcstValue", "빗방울/눈날림");
                    } else if (jobj.get("fcstValue").equals("7")) {
                        weather.setPTYValue("눈날림");
                        // map.put("fcstValue", "눈날림");
                    } else {
                        weather.setPTYValue("정보없음");
                        // weather.setFcstValue("정보없음");
                    }
                }
                // REH = 습도
                if (jobj.get("category").equals("REH")) {
                    weather.setREHcategory("습도(%)");
                    weather.setREHValue(jobj.get("fcstValue").toString());
                }
                // SKY = 하늘상태
                if (jobj.get("category").equals("SKY")) { 
                    weather.setSKYcategory("하늘상태");
                    // 맑음(1), 구름많음(3), 흐림(4)
                    if (jobj.get("fcstValue").equals("1")) {
                        weather.setSKYValue("맑음");
                    } else if (jobj.get("fcstValue").equals("3")) {
                        weather.setSKYValue("구름많음");
                        // weather.setFcstValue("구름많음");
                    } else if (jobj.get("fcstValue").equals("4")) {
                        weather.setSKYValue("흐림");
                        // weather.setFcstValue("흐림");
                    } else {
                        weather.setSKYValue("정보없음");
                        // weather.setFcstValue("정보없음");
                    }
                }
                // TMP = 1시간 기온
                if (jobj.get("category").equals("TMP")) { 
                    weather.setTMPcategory("1시간 기온(℃)");
                    weather.setTMPValue(jobj.get("fcstValue").toString());
                }

            }
            result.add(weather);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 한국공항공사_항공기 운항정보
    // 127.0.0.1:8080/ROOT/api/flightstatuslist.json
    @GetMapping(value = "/flightstatuslist.json")
    public String flightInfoGET(
        @RequestParam(name = "pageNo") int pageNo, 
        @RequestParam(name = "schAirCode") String schAirCode,
        @RequestParam(name = "type") String type,
        @RequestParam(name = "schStTime") String schStTime,
        @RequestParam(name = "schEdTime") String schEdTime
    ) {
        try {
            String flightstatuslist = aService.flightstatuslist(pageNo, schAirCode, type, schStTime, schEdTime);
            return flightstatuslist;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // 한국공항공사_공항 소요시간 정보(김포, 제주만 나옴)
    // 127.0.0.1:8080/ROOT/api/getwaittime.json
    @GetMapping(value = "/getwaittime.json")
    public getWaitTimeDTO getwaittimeGET(
        @RequestParam(name = "cate") String cate
    ) {
        try {
            // System.out.println("cate  ==> " + cate);
            JSONObject flightstatuslist = aService.getwaittime(cate);
            
            if(flightstatuslist != null){ // AirportService 에서 반환받은 값이 null이 아닌 경우
                getWaitTimeDTO getwaittime = new getWaitTimeDTO();
            
                // 초-> 분 시간 변환을 위해 Double 형태로 parse 
                double all = Double.parseDouble(flightstatuslist.get("STY_TCT_AVG_ALL").toString());
                double a =  Double.parseDouble(flightstatuslist.get("STY_TCT_AVG_A").toString());
                double b =  Double.parseDouble(flightstatuslist.get("STY_TCT_AVG_B").toString());
                double c =  Double.parseDouble(flightstatuslist.get("STY_TCT_AVG_C").toString());
                double d =  Double.parseDouble(flightstatuslist.get("STY_TCT_AVG_D").toString());
                
                
                getwaittime.setIATA_APCD(flightstatuslist.get("IATA_APCD").toString()); // 공항코드
                getwaittime.setOPR_STS_CD(flightstatuslist.get("OPR_STS_CD").toString()); // 운영상태 코드
                getwaittime.setPRC_HR(flightstatuslist.get("PRC_HR").toString()); // 조회 기준 시간
                getwaittime.setSTY_TCT_AVG_ALL(Math.round(all/60)); //전체 구간 소요시간 평균
                getwaittime.setSTY_TCT_AVG_A(Math.round(a/60)); // 탑승권자동발권-수하물위탁-신분확인
                getwaittime.setSTY_TCT_AVG_B(Math.round(b/60)); //신분확인-보안검색-탑승시간
                getwaittime.setSTY_TCT_AVG_C(Math.round(c/60)); //탑승시간-출발
                getwaittime.setSTY_TCT_AVG_D(Math.round(d/60)); //전체 구간 소요시간 평균
                
                return getwaittime;
            } else { // AirportService 에서 반환받은 값이 null인 경우
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    
    
    // 한국공항공사_전국공항 주차장 혼잡도(김포, 제주만 나옴)
    // 127.0.0.1:8080/ROOT/api/parkinghoninfo.json
    @GetMapping(value = "/parkinghoninfo.json")
    public String ParkinghonInfoGET(
        @RequestParam(name = "cate") String cate
        ) {
            try {
                String parkinginfo = aService.ParkinghonInfo(cate);
                return parkinginfo;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // 한국공항공사_전국공항 실시간 주차정보
    // 127.0.0.1:8080/ROOT/api/parkinginfo.json
    @GetMapping(value = "/parkinginfo.json")
    public String ParkingInfoGET(
        @RequestParam(name = "cate") String cate
    ) {
        try {
            String parkinginfo = aService.ParkingInfo(cate);
            return parkinginfo;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // 인천국제공항공사_주차 정보
    // 127.0.0.1:8080/ROOT/api/incheonparkinginfo.json
    @GetMapping(value = "/incheonparkinginfo.json")
    public String incheonParkingInfoGET(
        // @RequestParam(name = "cate") String cate
    ) {
        try {
            String incheonparkinginfo = aService.IncheonParkingInfo();
            return incheonparkinginfo;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    // 전국 공항 상업 시설 정보 불러온 후 DB 저장
    // 127.0.0.1:8080/ROOT/api/airportleasecontractinfo.json
    @PostMapping(value = "/airportleasecontractinfo.json")
    public Map<String,Object> AirportLeaseContractInfo( 
        @RequestParam(name = "airportCode") String airportCode,
        @RequestParam(name = "pageNo") Long pageNo
    ){
        Map<String,Object> map = new HashMap<>();
        try {
            String airportleasecontractinfo = aService.AirportLeaseContractInfo(airportCode,pageNo); // body 까지 꺼낸거 정보 불러오기(service)
            
            // Map에서 String type을 DTO로 변환하고 페이지네이션도 꺼내야 하기에(페이지네이션 데이터가 item 밖에 있어서 수동으로 꺼내야 함) 
            // 여기서 정보 꺼내기
            AirportStoreListDTO airportStoreListDTO1 = new AirportStoreListDTO();
            org.json.JSONObject json = new org.json.JSONObject(airportleasecontractinfo);
            
            // 위에 변수로 지정한 json 오브젝트에 페이지 네이션 정보 집어 넣기
            airportStoreListDTO1.setPageNo(json.getLong("pageNo"));
            airportStoreListDTO1.setTotalCount(json.getLong("totalCount"));
            airportStoreListDTO1.setNumOfRows(json.getLong("numOfRows"));
            
            // items 정보
            org.json.JSONObject items = json.getJSONObject("items");
            org.json.JSONArray item  = items.getJSONArray("item");

            // string 타입을 list<dto> 타입으로 바꾸기
            List<AirportStoreDTO> list = new ArrayList<>();

            for(int i=0;i<item.length();i++){
                AirportStoreDTO airportStoreDTO = new AirportStoreDTO();

                String airportcode = item.getJSONObject(i).getString("airportCode");
                airportStoreDTO.setAirportCode(airportCode);
                System.out.println(airportcode);

                String airportName = item.getJSONObject(i).getString("airportName");
                airportStoreDTO.setAirportName(airportName);
                System.out.println(airportName);

                String leaseLocation = item.getJSONObject(i).getString("leaseLocation");
                airportStoreDTO.setLeaseLocation(leaseLocation);
                System.out.println(leaseLocation);

                String storeName = item.getJSONObject(i).getString("storeName");
                airportStoreDTO.setStoreName(storeName);
                System.out.println(storeName);

                String typeOfBusiness = item.getJSONObject(i).getString("typeOfBusiness");
                airportStoreDTO.setTypeOfBusiness(typeOfBusiness);
                System.out.println(typeOfBusiness);

                list.add(airportStoreDTO);
            }
            airportStoreListDTO1.setItem(list);
            
            int ret = aMapper.airportleasecontractinfo(airportStoreListDTO1.getItem());
            System.out.println(ret);
            if(ret == airportStoreListDTO1.getItem().size()){
            map.put("airportStoreList", airportStoreListDTO1);
            map.put("status", 200);
            // DB에 추가
            }
        }            
        catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }
}