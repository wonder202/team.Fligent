package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.example.dto.ItemImgListDTO;
import com.example.entity.Item;
import com.example.entity.ItemImg;
import com.example.mapper.AdminItemImageMapper;
import com.example.repository.ItemImgRepository;
import com.example.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/admin/item/image")
@RequiredArgsConstructor
public class AdminItemImageRestController {

    final ItemRepository ItemRepository;
    final ItemImgRepository ItemImgRepository;
    final AdminItemImageMapper itemImageMapper;
    final ResourceLoader resourceLoader;


    // 물품 이미지 등록하기 (1개)
    // 127.0.0.1:8080/fligent/api/admin/item/image/insertone.json
    @PostMapping(value = "/insertone.json")
    public Map<String, Object> insertPOST(
            @ModelAttribute ItemImg image,
            @RequestParam(name = "file") MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        try {
            image.setImagedata(file.getBytes());
            image.setImagename(file.getOriginalFilename());
            image.setImagesize(file.getSize());
            image.setImagetype(file.getContentType());

            ItemImgRepository.save(image);
            map.put("status", 200);

        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }

        return map;
    }

    // 물품 이미지 여러개 등록(N개)
    // MultipartFile[] => 배열 형태로 받기
    // 127.0.0.1:8080/ROOT/api/admin/item/image/insertbatch.json
    @PostMapping(value = "/insertbatch.json")
    public Map<String, Object> insertbatchPOST(
            @ModelAttribute ItemImgListDTO list,
            @RequestParam(name = "file") MultipartFile[] file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        try {
            for(int i=0; i<list.getList().size(); i++){
                ItemImg image = list.getList().get(i);
                image.setImagedata(file[i].getBytes());
                image.setImagename(file[i].getOriginalFilename());
                image.setImagesize(file[i].getSize());
                image.setImagetype(file[i].getContentType());
            }
            ItemImgRepository.saveAll(list.getList());
            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 이미지 url 조회
    // 관리자 - 물품에 해당하는 이미지 조회 url 생성
    // = 수정페이지로 이동하기 + 기존의 정보를 가져간다
    // 이미지 번호를 전달하면 해당하는 이미지의 URL을 전송
    // 127.0.0.1:8080/ROOT/api/admin/item/image/url?no=이미지번호 + TOKEN 필요
    @GetMapping(value = "/url")
    public ResponseEntity<byte[]> imageGET(
            @RequestParam(name = "ino") Long ino) throws IOException {
        // 아이템 이미지 번호가 존재하는 경우
        if (ino > 0L) {
            ItemImg image = ItemImgRepository.findById(ino).orElse(null);
            // 조회된 image가 null이 아닌경우(예외처리)
            if (image != null) {
                // ItemImageDTO item = imageMapper.selectImageOne(no);

                if (image.getImagesize() > 0L) { // 이미지 파일이 존재하는 경우
                    // 타입설정 png인지 jpg인지 gif인지
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(
                            MediaType.parseMediaType(image.getImagetype()));
                    // 실제이미지데이터, 타입이포함된 header, status 200
                    ResponseEntity<byte[]> response = new ResponseEntity<>(
                            image.getImagedata(), headers, HttpStatus.OK);
                    return response;
                } else { // 이미지 파일이 존재하지 않는경우 = default이미지 설정
                    InputStream is = resourceLoader.getResource("classpath:/static/image/default.png")
                            .getInputStream();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_PNG);
                    // 실제이미지데이터, 타입이포함된 header, status 200
                    ResponseEntity<byte[]> response = new ResponseEntity<>(
                            is.readAllBytes(), headers, HttpStatus.OK);
                    return response;
                }
            } else { // 아이템 이미지 조회결과가 null인경우 = default이미지 설정
                InputStream is = resourceLoader.getResource("classpath:/static/image/default.png")
                        .getInputStream();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                // 실제이미지데이터, 타입이포함된 header, status 200
                ResponseEntity<byte[]> response = new ResponseEntity<>(
                        is.readAllBytes(), headers, HttpStatus.OK);
                return response;
            }
        } else { // 아이템 이미지 번호가 존재하지 않는경우 = default이미지 설정
            InputStream is = resourceLoader.getResource("classpath:/static/image/default.png")
                    .getInputStream();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            // 실제이미지데이터, 타입이포함된 header, status 200
            ResponseEntity<byte[]> response = new ResponseEntity<>(
                    is.readAllBytes(), headers, HttpStatus.OK);
            return response;
        }
    }


    // // 수정페이지로 이동하기 + 기존의 정보를 가져간다
    // // 127.0.0.1:8080/ROOT/api/admin/item/updateget.json
    // @GetMapping(value = "updateget.json")
    // public Map<String, Object> updateGET(@RequestParam(name = "no") Long no){
    //     Map<String, Object> map = new HashMap<>();
    //     try { 
    //         Item item = ItemRepository.findById(no).orElse(null);

    //         map.put("status", 200);
    //         map.put("item", item);
    //     } catch (Exception e) {
    //         map.put("status", -1);
    //         map.put("result", e.getMessage());
    //     }
    //     return map;
    // }

    //작업중XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx
    // 물품 이미지 수정하기
    // 127.0.0.1:8080/ROOT/api/admin/item/image/update.json
    @PostMapping(value="/update.json")
    public Map<String, Object> updateItem(
        @RequestBody Item item) {
            Map<String, Object> map = new HashMap<>();
            try {
            Item item1 = ItemRepository.findById(item.getIno()).orElse(null);

            item1.setName(item.getName());
            item1.setContent(item.getContent());
            item1.setPrice(item.getPrice());
            item1.setQuantity(item.getQuantity());
            item1.setItemimg(item.getItemimg());

            ItemRepository.save(item1);

            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
        
    // 1개 물품 이미지 삭제하기
    // 127.0.0.1:8080/ROOT/api/admin/item/image/delete.json
    @PostMapping(value="/delete.json")
    public Map<String, Object> deleteItem(
        @RequestParam(name = "ino") Long ino ) {
            Map<String, Object> map = new HashMap<>();
            try {
            ItemImgRepository.deleteById(ino);

            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }


    

    // 관리자 아이템 이미지 등록
    // 127.0.0.1:8080/fligent/api/admin/item/image/iteminsertbatchimage.json
    @PostMapping(value = "/iteminsertbatchimage.json")
    public Map<String, Object> iteminsertimagePOST(
            @RequestParam(name = "ino") Long ino,
            @RequestParam(name = "file") MultipartFile[] file)
            throws IOException {

        Map<String, Object> map = new HashMap<>();
        try {
            List<ItemImg> list = new ArrayList<>();
            for (int i = 0; i < file.length; i++) {
                ItemImg itemImg = new ItemImg();
                itemImg.setImagedata(file[i].getBytes());
                itemImg.setImagename(file[i].getOriginalFilename());
                itemImg.setImagetype(file[i].getContentType());
                itemImg.setImagesize(file[i].getSize());

                Item item = new Item();
                item.setIno(ino);
                itemImg.setItem(item);

                list.add(itemImg);
            }
            System.out.println("등록완료");

            int ret = itemImageMapper.insertItembatchImage(list);
            
            if (ret > 0) {
                map.put("status", 200);
            }

            else {
                map.put("status", 0);
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
    // 관리자 물품 수정 이미지 삭제
    // 127.0.0.1:8080/fligent/api/admin/item/image/deleteimage.json?ino=&iimageno=
    @PostMapping(value = "/deleteimage.json")
    public Map<String, Object> deleteimagePOST( 
        @RequestParam(name = "ino") Long ino,
        @RequestParam(name = "iimageno") Long iimageno
    ){
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("ino", ino);
            map1.put("iimageno", iimageno);

            int ret = itemImageMapper.deleteImage(map1);
            if (ret == 1) {
                map.put("status", 200);
                map.put("result", "삭제 완료");
            } else {
                map.put("status", 0);
            }
            
        } 
        catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }
}
