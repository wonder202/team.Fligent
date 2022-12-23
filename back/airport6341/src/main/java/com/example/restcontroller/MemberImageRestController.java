package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.MemberImg;
import com.example.mapper.CustomerImageMapper;
import com.example.repository.MemberImgRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = { "/api/memberimage" })
@RequiredArgsConstructor
public class MemberImageRestController {

    final MemberImgRepository imageRepository;
    final CustomerImageMapper ciMapper;
    final ResourceLoader resourceLoader;

    // 회원 프로필 사진 등록
    // 127.0.0.1:8080/ROOT/api/memberimage/insert.json
    @PostMapping(value = "/insert.json")
    public Map<String, Object> insertPOST(
            @ModelAttribute MemberImg image,
            @RequestParam(name = "file") MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();


        image.setImagename(file.getOriginalFilename());
        image.setImagesize(file.getSize());
        image.setImagetype(file.getContentType());
        image.setImagedata(file.getBytes());

        imageRepository.save(image);

        try {
            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 네이버 API 로그인 프로필 사진 등록
    // 127.0.0.1:8080/ROOT/api/memberimage/apiinsert.json
    @PostMapping(value = "/apiinsert.json")
    public Map<String, Object> apiInsertPOST(
            @RequestBody MemberImg image) {
        Map<String, Object> map = new HashMap<>();
        try {
            MemberImg image1 = new MemberImg();
            image1.setMember(image.getMember());
            image1.setApiimageurl(image.getApiimageurl().toString());

            MemberImg result = imageRepository.save(image1);

            if (result != null) {

                map.put("status", 200);
            }
            map.put("status", 0);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 프로필 이미지 url로 변환
    // 127.0.0.1:8080/ROOT/api/memberimage/image?mimageno=300
    @GetMapping(value = "/image")
    public ResponseEntity<byte[]> imageGET(@RequestParam(name = "mimageno") Long mimageno) throws IOException {

        if (mimageno > 0L) {
            MemberImg memberImg = ciMapper.selectmemberimage(mimageno);

            // 조회된 image가 null이 아닌경우(예외처리)
            if (memberImg != null) {

                // // API로그인의 경우 = APIIMAGEURL이 있는경우 (APIIMAGEURL != null)
                // if (memberImg.getApiimageurl() != null) {

                // } else {

                    // 이미지 파일이 존재하는 경우
                    if (memberImg.getImagesize() > 0L) {
                        // 타입설정 png인지 jpg인지 gif인지
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.parseMediaType(memberImg.getImagetype()));
                        // 실제이미지데이터, 타입이포함된 header, status 200
                        return new ResponseEntity<>(memberImg.getImagedata(), headers, HttpStatus.OK);
                    }
                    // 이미지 파일이 존재하지 않는경우 = default이미지 설정
                    else {
                        InputStream is = resourceLoader.getResource("classpath:/static/image/defaultprofile.png")
                                .getInputStream();
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.IMAGE_PNG);
                        // 실제이미지데이터, 타입이포함된 header, status 200
                        return new ResponseEntity<>(is.readAllBytes(), headers, HttpStatus.OK);
                    }
            } else {
                // 이미지 조회결과가 null인경우 = default이미지 설정
                InputStream is = resourceLoader.getResource("classpath:/static/image/defaultprofile.png")
                        .getInputStream();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                // 실제이미지데이터, 타입이포함된 header, status 200
                return new ResponseEntity<>(is.readAllBytes(), headers, HttpStatus.OK);
            }
            // 이미지 번호가 존재하지 않는경우 = default이미지 설정
        } else {
            // defaultprofile.png
            InputStream is = resourceLoader.getResource("classpath:/static/image/defaultprofile.png").getInputStream();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            // 실제이미지데이터, 타입이포함된 header, status 200
            return new ResponseEntity<>(is.readAllBytes(), headers, HttpStatus.OK);
        }
    }
}
