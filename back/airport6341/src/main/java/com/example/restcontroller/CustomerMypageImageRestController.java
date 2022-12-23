package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.CustomerImageDTO;
import com.example.entity.MemberImg;
import com.example.mapper.CustomerImageMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/customer")
@RequiredArgsConstructor
public class CustomerMypageImageRestController {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    CustomerImageMapper ciMapper;

    // 프로필 이미지 url로 변환
    // 127.0.0.1:8080/ROOT/api/customer/image?mimageno=300
    @GetMapping(value="/image")
    public ResponseEntity<byte[]> imageGET( @RequestParam(name="mimageno") Long mimageno) throws IOException{
        if(mimageno > 0L) {           
            MemberImg memberImg = ciMapper.selectmemberimage(mimageno);
            if(memberImg.getImagesize() > 0L) {
                // 타입설정 png인지 jpg인지 gif인지
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType( MediaType.parseMediaType( memberImg.getImagetype() ) );
                // 실제이미지데이터, 타입이포함된 header, status 200
                return new ResponseEntity<>(memberImg.getImagedata(), headers, HttpStatus.OK);
            }
            else {
                InputStream is = resourceLoader.getResource("classpath:/static/image/defaultprofile.png")
                    .getInputStream();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                // 실제이미지데이터, 타입이포함된 header, status 200    
                return new ResponseEntity<>(is.readAllBytes(), headers, HttpStatus.OK);
            }
        }
            else {
                InputStream is = resourceLoader.getResource("classpath:/static/image/defaultprofile.png").getInputStream();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                // 실제이미지데이터, 타입이포함된 header, status 200    
                return new ResponseEntity<>(is.readAllBytes(), headers, HttpStatus.OK);
            }
    }

    // 프로필 이미지 불러오기
    // 회원 프로필 조회 할때 이거 쓰면 됌
    // 127.0.0.1:8080/ROOT/api/customer/selectmemberimglist.json
    @GetMapping(value="/selectmemberimglist.json")
    public Map<String, Object> selectOne(
         HttpServletRequest request){
         Map<String, Object> retMap = new HashMap<>();
         try {
            String userid = (String) request.getAttribute("username");
            // 이미지테이블 회원 정보
            CustomerImageDTO memberImg = ciMapper.selectMemberImageNo(userid);
            System.out.println("memberImg => " + memberImg);
            
            if(memberImg == null){  // 조회된 memberImg가 null 인경우
                String memberImgURL = request.getContextPath() + "/api/memberimage/image?mimageno=" + 0;
                retMap.put("result", memberImgURL);
            } 
            else { // 조회된 memberImg가 null이 아닌 경우
                if(memberImg.getApiimageurl() != null){ // Apiimageurl가 있는경우
                    String memberImgURL = memberImg.getApiimageurl();
                    System.out.println("memberImgURL => " + memberImg.getApiimageurl());
                    retMap.put("result", memberImgURL);
                } else { // Apiimageurl가 없는경우
                    String memberImgURL = request.getContextPath() + "/api/memberimage/image?mimageno=" + memberImg.getMimageno();
                    System.out.println("memberImgURL => " + memberImgURL);
                    retMap.put("result", memberImgURL);
                }
        }
            retMap.put("status", 200);
         }
         catch(Exception e) {
             e.printStackTrace();
             retMap.put("status", -1);
             retMap.put("result", e.getMessage());
         }
         return retMap;
    }

    // 프로필 이미지 수정
    // 127.0.0.1:8080/ROOT/api/customer/selectmemberimgupdate.json
    // form-data 
    // mimageno, file
    @PutMapping(value = "/selectmemberimgupdate.json")
    public Map<String, Object> selectmemberimgupdatePUT(
        @RequestParam(name = "file") MultipartFile file,
        @ModelAttribute MemberImg memberImage
        ){
        Map<String, Object> map = new HashMap<>();
        try {
            memberImage.setImagedata(file.getBytes());
            memberImage.setImagename(file.getOriginalFilename());
            memberImage.setImagesize(file.getSize());
            memberImage.setImagetype(file.getContentType());

            int ret = ciMapper.updatememberimage(memberImage);
            map.put("status", 200);
            map.put("result", ret);
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 프로필 이미지 삭제
    // 127.0.0.1:8080/ROOT/api/customer/deleteoneimage.json?mimageno=
    @PutMapping(value = "/deleteoneimage.json")
    public Map<String, Object> deleteoneimage( @RequestParam Long mimageno,
    @ModelAttribute MemberImg memberImg ){
        Map<String, Object> map = new HashMap<>();
        try{
            memberImg.setImagedata(null);
            memberImg.setImagename(null);
            memberImg.setImagesize(null);
            memberImg.setImagetype(null);
            memberImg.setImageurl(null);
            memberImg.setApiimageurl(null);
            int ret = ciMapper.deletememberImage(mimageno);
            if(ret == 1){
                map.put("status", 200);
                map.put("result", "프로필 이미지 초기화");
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }
}
