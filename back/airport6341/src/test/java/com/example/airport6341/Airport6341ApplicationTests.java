package com.example.airport6341;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Airport6341ApplicationTests {


	// @Autowired
    // ApiMapper aMapper;

	// @Test
	// void contextLoads() {
	// 	//한국공항공사_항공기 운항정보 디비 저장
	// 	try{
			// 조건(중요중요중요중요중요중요중요중요중요중요)
			// 시퀀스와 테이블이 있는 상태여야 함.
			// 시퀀스 생성
			// CREATE SEQUENCE SEQ_FLY_NO START WITH 1 INCREMENT BY 1 NOMAXVALUE NOCACHE;
			// 테이블 생성
			// CREATE TABLE FLIGHT(
			// FLY_NO VARCHAR2(200) PRIMARY KEY,
			// AIRFLN VARCHAR2(200),
			// AIRLINEKOREAN VARCHAR2(200),
			// BOARDINGKOR VARCHAR2(200),
			// ARRIVEDKOR VARCHAR2(200),
			// STD VARCHAR2(20),
			// ETD VARCHAR2(20)
			// );

			// 참고용
			// SELECT * FROM FLIGHT;
			// DELETE FROM FLIGHT;
			// DROP SEQUENCE SEQ_FLY_NO;

			// INSERT INTO FLIGHT(FLY_NO , AIRFLN , AIRLINEKOREAN , BOARDINGKOR , ARRIVEDKOR, STD, ETD) 
			// VALUES(SEQ_FLY_NO.NEXTVAL, 'AAA', 'AAA', 'AAA', 'AAA', 'AAA', 'AAA');

			// 에러가 뜰 수도 있는데, 인천 데이터가 없어서 뜨는 것임. 무시해도 됨.
	// 		aMapper.deletedata();
	// 		aMapper.dropseq();
	// 		aMapper.createseq();
	// 		String airportname = "";
	// 		for(int j = 0; j<4; j++){
	// 			if(j == 0){airportname = "GMP";}
	// 			if(j == 1){airportname = "PUS";}
	// 			if(j == 2){airportname = "CJU";}
	// 			if(j == 3){airportname = "ICN";}
	// 			String serviceKey = "v%2BvconA4SSH0RNTzvLDNIAA2gItiGZVEA0ekclBmwQPbP4Us%2B%2BtPgEDNI%2B4oB1KuZZ1tfvre4l%2BAGcxLMFtlSg%3D%3D";
	// 			String url = "http://openapi.airport.co.kr/service/rest/FlightStatusList/getFlightStatusList?ServiceKey="+ serviceKey +"&schStTime=0000&schEdTime=2400&schLineType=D&schIOType="+"O"+"&schAirCode="+airportname+"&numOfRows=1000000";
	// 			JSONArray parseItem = new JSONArray();
	// 			RestTemplate restTemplate = new RestTemplate();
	// 			URI uri = new URI(url);
	// 			String response = restTemplate.getForObject(uri, String.class);
	// 			JSONParser jsonParse = new JSONParser();
	// 			JSONObject obj =  (JSONObject)jsonParse.parse(response);
	// 			JSONObject parseResponse = (JSONObject) obj.get("response");
	// 			JSONObject parseBody = (JSONObject) parseResponse.get("body");
	// 			JSONObject parseItems = (JSONObject) parseBody.get("items");
	// 			parseItem = (JSONArray) parseItems.get("item");
	// 			List<ApiFlyDTO> list = new ArrayList<>();
	// 			for (int i = 0; i < parseItem.size(); i++) {
	// 				ApiFlyDTO fly = new ApiFlyDTO();
	// 				JSONObject jobj = (JSONObject) parseItem.get(i);
	// 				fly.setAirFln(jobj.get("airFln").toString());
	// 				fly.setAirlineKorean(jobj.get("airlineKorean").toString());
	// 				fly.setArrivedKor(jobj.get("arrivedKor").toString());
	// 				fly.setBoardingKor(jobj.get("boardingKor").toString());
	// 				fly.setEtd(jobj.get("etd").toString());
	// 				fly.setStd(jobj.get("std").toString());
	// 				System.out.println(fly.toString());
	// 				list.add(fly);
	// 			}
	// 			aMapper.apisave(list);
	// 		}
	// 	}
	// 	catch (Exception e) {
	// 		e.printStackTrace();
	// 		System.out.println(e);
	// 	}
	// }

}
