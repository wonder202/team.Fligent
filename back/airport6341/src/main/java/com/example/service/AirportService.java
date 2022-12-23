package com.example.service;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.dto.ApiFlyDTO;
import com.example.mapper.ApiMapper;


@Service
public class AirportService {

    @Autowired
    ApiMapper aMapper;

    // 단기예보조회
    // 김포국제공항 (방화2동) 격자X : 57 , 격자Y : 127 
    // 부산김해공항 (대저2동) 격자X : 96, 격자Y : 76
    // 제주국제공항 (용담2동) 격자X : 52, 격자Y : 38
    // 인천국제공항 (운서동) 격자X : 51 , 격자Y : 125 
    // http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=인증키&numOfRows=60&pageNo=1&base_date=20221022&base_time=1450&nx=55&ny=127
    public JSONArray weatherGET(String nx, String ny) {
        try {
            JSONArray parseItem = new JSONArray();

            int time = 120; // 2시간전 시간

            for(int i = 0; i<10; i++){
                LocalDateTime searchTime = LocalDateTime.now().minusMinutes(time); 

                String serviceKey = "JMfgU%2BBFuhNwtwN3uwBqS0G7D9r7Y3%2FShOc4dnGzq2QFXjkuuM%2BiKdtIzBuVDbWd1R0ZmPIj%2BTzepGRDD%2BMJuA%3D%3D";// 서비스키
                String base_date = searchTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));// 발표 날짜
  
                String base_time = searchTime.format(DateTimeFormatter.ofPattern("HHmm"));// 발표 시각
 

                String url = 
                    "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" 
                    + serviceKey 
                    + "&numOfRows=14&pageNo=1" // 한 페이지 결과 수 (10개 카테고리값 * 6시간) + 페이지번호 Default: 1
                    + "&base_date=" 
                    + base_date 
                    + "&base_time="
                    + base_time
                    + "&nx=" 
                    + nx 
                    + "&ny=" 
                    + ny 
                    + "&dataType=JSON";
    
                RestTemplate restTemplate = new RestTemplate();
                URI uri = new URI(url);
                
                String response = restTemplate.getForObject(uri, String.class);
                JSONParser jsonParse = new JSONParser();
                JSONObject obj =  (JSONObject)jsonParse.parse(response);
                JSONObject parseResponse = (JSONObject) obj.get("response");
                JSONObject parseBody = (JSONObject) parseResponse.get("body");
                
                
                if(parseResponse.get("body") != null){ //body 값이 들어있는 경우
                    JSONObject parseItems = (JSONObject) parseBody.get("items");
                    parseItem = (JSONArray) parseItems.get("item");

                    break;
                }
                time = time + 30;
            }
            
            return parseItem;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //한국공항공사_항공기 운항정보
    public String flightstatuslist(int pageNo, String schAirCode, String type, String schStTime, String schEdTime) {
        try {
            String serviceKey = "v%2BvconA4SSH0RNTzvLDNIAA2gItiGZVEA0ekclBmwQPbP4Us%2B%2BtPgEDNI%2B4oB1KuZZ1tfvre4l%2BAGcxLMFtlSg%3D%3D";
            String url = "http://openapi.airport.co.kr/service/rest/FlightStatusList/getFlightStatusList?ServiceKey="+ serviceKey +"&schStTime="+schStTime+"&schEdTime="+schEdTime+"&schLineType=D&schIOType="+type+"&schAirCode="+schAirCode+"&pageNo="+pageNo;
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(url);
            String response = restTemplate.getForObject(uri, String.class);
            JSONParser jsonParse = new JSONParser();
            JSONObject obj =  (JSONObject)jsonParse.parse(response);
            JSONObject parseResponse = (JSONObject) obj.get("response");
		    JSONObject parseBody = (JSONObject) parseResponse.get("body");
            // api에서 정보를 못불러오면 아래껄 씀
            // parseBody = null;
            if(parseBody == null || parseBody.toString() == ""){
                LocalDate now = LocalDate.now();
                if(schAirCode == "GMP"){schAirCode = "서울/김포";}
                else if(schAirCode == "PUS"){schAirCode = "부산/김해";}
                else if(schAirCode == "CJU"){schAirCode = "제주";}
                else if(schAirCode == "ICN"){schAirCode = "인천";}
                else {schAirCode = "서울/김포";}
                Map<String, Object> map = new HashMap<>();
                map.put("start", pageNo*10-9); 
                map.put("aircode", schAirCode); 
                map.put("type", type);
                map.put("schStTime", schStTime);
                map.put("schEdTime", schEdTime);
                long totalCount = aMapper.apiselectcount(map);
                JSONObject jsonObj1 = new JSONObject();
                jsonObj1.put("pageNo", (int)pageNo);
                jsonObj1.put("totalCount", (int)totalCount);
                JSONObject items = new JSONObject();
                JSONArray itemobj = new JSONArray();
                List<ApiFlyDTO> list = aMapper.apiselectlist(map);
                for(int i = 0; i<10; i++){
                    JSONObject item = new JSONObject();
                    item.put("flightDate", now);
                    item.put("airFln", list.get(i).getAirFln());
                    item.put("airlineKorean", list.get(i).getAirlineKorean());
                    item.put("boardingKor", list.get(i).getBoardingKor());
                    item.put("arrivedKor", list.get(i).getArrivedKor());
                    item.put("etd", list.get(i).getEtd());
                    item.put("std", list.get(i).getStd());
                    itemobj.add(item);
                }
                items.put("item", itemobj);
                jsonObj1.put("items", items); 
                jsonObj1.put("numOfRows", 10);
                return jsonObj1.toString();
            }
            return parseBody.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 한국공항공사_공항 소요시간 정보
    public JSONObject getwaittime(String cate) {
        try {
            // System.out.println(cate);
            String serviceKey = "JMfgU%2BBFuhNwtwN3uwBqS0G7D9r7Y3%2FShOc4dnGzq2QFXjkuuM%2BiKdtIzBuVDbWd1R0ZmPIj%2BTzepGRDD%2BMJuA%3D%3D";
            String url = "http://api.odcloud.kr/api/getAPRTWaitTime/v1/aprtWaitTime?page=1&perPage=10&returnType=JSON&cond%5BIATA_APCD%3A%3AEQ%5D="+cate+"&serviceKey="+serviceKey;
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(url);
            String response = restTemplate.getForObject(uri, String.class);
            JSONParser jsonParse = new JSONParser();
            JSONObject obj = (JSONObject) jsonParse.parse(response);
            JSONArray parseResponse = (JSONArray) obj.get("data");

            if(parseResponse.isEmpty() != true){
            // 실시간 데이터는 1개만 오기때문에 데이터는 parseResponse.get(0)으로 꺼내준다
            JSONObject result = (JSONObject) parseResponse.get(0);

            // String result = obj.toString();
            return result;
            } else {  //parseResponse.isEmpty() == true
                return null;
            }
        }  catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 한국공항공사_전국공항 주차장 혼잡도
    public String ParkinghonInfo(String cate) {
        try {
            String serviceKey = "JMfgU%2BBFuhNwtwN3uwBqS0G7D9r7Y3%2FShOc4dnGzq2QFXjkuuM%2BiKdtIzBuVDbWd1R0ZmPIj%2BTzepGRDD%2BMJuA%3D%3D";
            String url = "http://openapi.airport.co.kr/service/rest/AirportParkingCongestion/airportParkingCongestionRT?schAirportCode="+cate+"&serviceKey="
                    + serviceKey
                    + "&numOfRows=10&pageNo=1";
            // Spring boot에서 제공하는 rest템플릿 사용
            RestTemplate restTemplate = new RestTemplate();   
            URI uri = new URI(url);
            String response = restTemplate.getForObject(uri, String.class);
            JSONParser jsonParse = new JSONParser();
            JSONObject obj = (JSONObject) jsonParse.parse(response);
            JSONObject parseResponse = (JSONObject) obj.get("response");
		    JSONObject parseBody = (JSONObject) parseResponse.get("body");
            String result = parseBody.toString();

            return result;

        }  catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

        // 한국공항공사_전국공항 실시간 주차정보
        public String ParkingInfo(String cate) {
            try{
                String serviceKey = "PxUQv09spE8N85pwIwySieXRxlNADWhNtWpxFKk02XAc4KGH1PmJt8NnEil8TFkRIMTk2bBhANMVyNNtWr6SVw==";
                String url = "http://openapi.airport.co.kr/service/rest/AirportParking/airportparkingRT?serviceKey="+serviceKey+"&schAirportCode="+cate+"";
                RestTemplate restTemplate = new RestTemplate();
                URI uri = new URI(url);
                String response = restTemplate.getForObject(uri, String.class);
                JSONParser jsonParse = new JSONParser();
                JSONObject obj = (JSONObject) jsonParse.parse(response);
                JSONObject parseResponse = (JSONObject) obj.get("response");
                JSONObject parseBody = (JSONObject) parseResponse.get("body");
                String result = parseBody.toString();
                return result;
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

    // 인천국제공항공사_주차 정보
    public String IncheonParkingInfo() {
        try{
            String serviceKey = "PxUQv09spE8N85pwIwySieXRxlNADWhNtWpxFKk02XAc4KGH1PmJt8NnEil8TFkRIMTk2bBhANMVyNNtWr6SVw==";
            String url = "http://apis.data.go.kr/B551177/StatusOfParking/getTrackingParking?serviceKey="
            + serviceKey
            + "&numOfRows=10&pageNo=1&type=json";
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(url);
            String response = restTemplate.getForObject(uri, String.class);
            JSONParser jsonParse = new JSONParser();
            JSONObject obj = (JSONObject) jsonParse.parse(response);
            System.out.println(obj.toString());
            JSONObject parseResponse = (JSONObject) obj.get("response");
		    JSONObject parseBody = (JSONObject) parseResponse.get("body");
            String result = parseBody.toString();
    
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // 전국 공항 상업 시설 정보
    public String AirportLeaseContractInfo(String airportCode, Long pageNo) {
        try{
            String serviceKey = "PxUQv09spE8N85pwIwySieXRxlNADWhNtWpxFKk02XAc4KGH1PmJt8NnEil8TFkRIMTk2bBhANMVyNNtWr6SVw==";
            String url = "http://openapi.airport.co.kr/service/rest/AirportLeaseInfo/airportLeaseContract?serviceKey=" + serviceKey +"&pageNo=" + pageNo + "&numOfRows=10&schAirportCode=" + airportCode + "&contractStatus=ON";
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(url);
            String response = restTemplate.getForObject(uri, String.class);

            JSONParser jsonParse = new JSONParser();
            JSONObject obj = (JSONObject) jsonParse.parse(response);
            JSONObject parseResponse = (JSONObject) obj.get("response");
		    JSONObject parseBody = (JSONObject) parseResponse.get("body");
            String result = parseBody.toString();
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
