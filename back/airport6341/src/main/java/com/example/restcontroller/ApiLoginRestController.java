package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;


@RestController
@RequestMapping(value = "/api/login")
public class ApiLoginRestController {

	// LoginNCallback.vue 에서 호출
	// 127.0.0.1:8080/fligent/api/login/oauth2.0?query=&clientid=&secret=
	@GetMapping(value = "/oauth2.0")
	public Map<String, Object> oauth20(
		@RequestParam(name="clientid") String clientid,
		@RequestParam(name="code") String code,
		@RequestParam(name="state") String state,
		@RequestParam(name="secret") String secret){
		Map<String, Object> retMap = new HashMap<>();
		try {

			// System.out.println("받아온 state 값 => " + state);
			// System.out.println("받아온 code 값 => " + code);
			// System.out.println("받아온 clientid 값 => " + clientid);
			// System.out.println("받아온 secret 값 => " + secret);

			// https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=jyvqXeaVOVmV&client_secret=527300A0_COq1_XV33cf&code=EIc5bFrl4RibFls1&state=9kgsGTfH4j7IyAkg
	        String url = "https://nid.naver.com/oauth2.0/token?" 
	        		+ "grant_type=authorization_code&client_id=" + clientid
	        		+ "&client_secret=" + secret
					+ "&code=" + code
					+ "&state=" + state;
	        
			// System.out.println("토큰 가져올 url ====> "+ url);

	        // OkHttp 클라이언트 객체 생성
	        OkHttpClient client = new OkHttpClient();
	 
	        // GET 요청 객체 생성
	        Request.Builder builder = new Request.Builder().url(url).get();
	        builder.addHeader("X-Naver-Client-Id", clientid);
	        builder.addHeader("X-Naver-Client-Secret", secret);
	        
	        Request request = builder.build();
	 
	        Response response = client.newCall(request).execute();
	        if (response.isSuccessful()) {
	            // 응답 받아서 처리
	            ResponseBody body = response.body();
	            // body.close();
	            if (body != null) {
	            	String str = body.string();
	            	retMap.put("status", 200);
	            	// retMap.put("result", body.string());
	                // System.out.println("oauth20 의 Response  ==> " + str);
	                
	                JSONObject jobj = new JSONObject(str);
	                retMap.put("access_token" , jobj.getString("access_token"));
	                retMap.put("refresh_token" , jobj.getString("refresh_token"));
	                retMap.put("token_type" , jobj.getString("token_type"));
	                retMap.put("expires_in" , jobj.getString("expires_in"));
	                
	                body.close();
	            }
	        }
	        else {
	            System.err.println("Error Occurred");
	        }

	    } catch(Exception e) {
	    	retMap.put("status", -1);
	        e.printStackTrace();
	    }
        return retMap;
	}
	
	// 127.0.0.1:8080/fligent/api/login/v1?token=&
	@GetMapping(value = "/v1")
	public Map<String, Object> v1(@RequestParam(name="token") String token) {
		Map<String, Object> retMap = new HashMap<>();
		Map<String, Object> responseMap = new HashMap<>();
		try {
			// System.out.println("param으로 받아온 access_token => " + token);
	        String url = "https://openapi.naver.com/v1/nid/me";
	          
	        // OkHttp 클라이언트 객체 생성
	        OkHttpClient client = new OkHttpClient();
	 
	        // GET 요청 객체 생성
	        Request.Builder builder = new Request.Builder().url(url).get();
	        builder.addHeader("Authorization", "Bearer " + token);
	        
	        Request request = builder.build();
	 
	        Response response = client.newCall(request).execute();
	        if (response.isSuccessful()) {
	            // 응답 받아서 처리
	            ResponseBody body = response.body();
	            if (body != null) {
	            	String str = body.string();
	            	body.close();

	                // System.out.println("Response ==> " + str);

					JSONObject jobj = new JSONObject(str);
					JSONObject responseObj = jobj.getJSONObject("response");
	                responseMap.put("birthday" , responseObj.getString("birthday"));
	                responseMap.put("profile_image" , responseObj.getString("profile_image"));
	                responseMap.put("birthyear" , responseObj.getString("birthyear"));
	                responseMap.put("nickname" , responseObj.getString("nickname"));
	                responseMap.put("mobile" , responseObj.getString("mobile"));
	                responseMap.put("email" , responseObj.getString("email"));

					retMap.put("status", 200);
	                retMap.put("response" , responseMap);

					// System.out.println("result ==> "+ responseMap.toString());
	            }
	        }
	        else {
	            System.err.println("Error Occurred");
	        }

	    } catch(Exception e) {
	        e.printStackTrace();
	    }
        return retMap;
	}
}
