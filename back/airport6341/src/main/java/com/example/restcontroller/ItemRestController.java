package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.ItemDTO;
import com.example.dto.ItemSelectOneDTO;
import com.example.entity.Item;
import com.example.entity.ItemImg;
import com.example.entity.ItemImgProject;
import com.example.repository.ItemImgRepository;
import com.example.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/item")
@RequiredArgsConstructor
public class ItemRestController {

    final ItemRepository ItemRepository;
    final ItemImgRepository ItemImgRepository;
    final ResourceLoader resourceLoader;

    // 고객 - 물품 메인페이지 조회 (TOKEN필요 없음)
    // 선택된 카테고리 조회 결과 (페이지네이션12개 + 물품12개목록 + 물품의 이미지 오름차순 0번째 목록만 조회)
    // 127.0.0.1:8080/ROOT/api/item/selectitem.json?page=1&icateno=1
    @GetMapping(value = "/selectitem.json")
    public Map<String, Object> selectItemGET(
            HttpServletRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "icateno", defaultValue = "1") Long icateno) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 페이지네이션 설정(0부터, 1페이지에 출력될 개수)
            PageRequest pageRequest = PageRequest.of(page - 1, 12);

            // 선택한 카테고리의 물품 갯수 구하기
            long count = ItemRepository.countByItemcate_icateno(icateno);

            // 카테고리에 해당하는 아이템 번호 + 아이템 이미지 번호 가져오기 => 최신 등록순 정렬 + 페이지네이션
            List<Item> itemList = ItemRepository.findByItemcateIcatenoOrderByRegdateDesc(icateno, pageRequest);

                List<ItemDTO> itemDtolist = new ArrayList<>();
                for (int i = 0; i < itemList.size(); i++) {
                    
                    // ItemImgProject이용하여 오름차순 정렬된 iimageno 한개만 가져오기
                    ItemImgProject ItemImgProject = ItemImgRepository.findTopByItem_inoOrderByIimagenoAsc(itemList.get(i).getIno());


                    //DTO 만들어서 넣어주기
                    ItemDTO itemDTO = new ItemDTO();
                    itemDTO.setIno(itemList.get(i).getIno());
                    itemDTO.setName(itemList.get(i).getName());
                    itemDTO.setPrice(itemList.get(i).getPrice());
                    itemDTO.setQuantity(itemList.get(i).getQuantity());
                    itemDTO.setRegdate(itemList.get(i).getRegdate());

                    // System.out.println("********************기본이미지url 0으로설정*********************");
                    String Imageurl = request.getContextPath() + "/api/item/image?ino=0";
                    itemDTO.setImageurl(Imageurl);
                    
                    
                    if (ItemImgProject != null) {// ItemImgProject이 있는경우
                    // 프로젝션은 터미널창에 바로 출력되지 않는다 ! get~으로 받아와 출력
                    // ItemImgProject가 null인경우가 있을수 있으니 출력도 if문 안에서 진행되어야 한다 => 안 그럼 nullpointexception 에 걸려 result = -1이 된다
                        if (ItemImgProject.getIimageno() > 0L) { //이미지 번호가 0보다 큰경우           
                            
                            // Imageurl 생성하여 itemDTO에 추가 
                            String Imageurl1 = request.getContextPath()
                            + "/api/item/image?ino=" + ItemImgProject.getIimageno();
                            
                            itemDTO.setImageurl(Imageurl1);
                        }
                    } 
                    itemDtolist.add(itemDTO);
            }
            map.put("status", 200);
            map.put("count", count);
            map.put("itemlist", itemDtolist);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 고객 - 이미지 1개 조회
    // 이미지 번호를 전달하면 해당하는 이미지의 URL을 전송
    // 127.0.0.1:8080/ROOT/api/item/image?ino=이미지번호
    @GetMapping(value = "/image")
    public ResponseEntity<byte[]> imageGET(
            @RequestParam(name = "ino") Long ino) throws IOException {
        // 아이템 이미지 번호가 존재하는 경우(0보다 큰경우)
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
        } else { // 아이템 이미지 번호가 존재하지 않는경우(0보다 작은경우) = default이미지 설정
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

    // 고객 - 물품 1개 상세페이지(+ 해당 하위 물품 이미지 url 전부)
    // 고객 - 물품 수정시 기존정보 불러오기(+ 해당 물품 이미지)
    // 127.0.0.1:8080/ROOT/api/item/selectoneitem.json?ino=6
    @GetMapping(value = "/selectoneitem.json")
    public Map<String, Object> selectOne(
            HttpServletRequest request,
            @RequestParam(name = "ino") Long ino) {
        Map<String, Object> map = new HashMap<>();
        try {
            Item item = ItemRepository.findById(ino).orElse(null);
            ItemSelectOneDTO ItemSelectOne = new ItemSelectOneDTO();
            
            // if(item != null){ //예외처리

            // item => ItemSelectOneDTO에 담기
            ItemSelectOne.setIno(item.getIno());
            ItemSelectOne.setName(item.getName());
            ItemSelectOne.setContent(item.getContent());
            ItemSelectOne.setPrice(item.getPrice());
            ItemSelectOne.setQuantity(item.getQuantity());
            ItemSelectOne.setRegdate(item.getRegdate());
            

            // no에 해당하는 물품 하위의 이미지 번호를 오름차순으로 가져오기(projection)
            List<ItemImgProject> itemImgnolist = ItemImgRepository.findByItem_inoOrderByIimagenoAsc(ino);

            List<String> imgurlList = new ArrayList<>();
            for(int i=0; i<itemImgnolist.size(); i++){

                // Imageurl 생성하여 itemDTO에 추가 
                String Imageurl = request.getContextPath()
                + "/api/item/image?ino=" + itemImgnolist.get(i).getIimageno();
                
                // System.out.println("Imageurl => " + Imageurl);
                imgurlList.add(Imageurl);

            ItemSelectOne.setImageurl(imgurlList);
        } 
            map.put("status", 200);
            map.put("item", ItemSelectOne);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 고객 - 물품 수정하기
    // 127.0.0.1:8080/ROOT/api/item/updatepost.json
    @PostMapping(value="/updatepost.json")
    public Map<String, Object> updateItem(
        @RequestBody Item item) {
            Map<String, Object> map = new HashMap<>();
            try {

            Item item1 = ItemRepository.findById(item.getIno()).orElse(null);
            System.out.println(item1);

            item1.setName(item.getName());
            item1.setContent(item.getContent());
            item1.setPrice(item.getPrice());
            item1.setQuantity(item.getQuantity());

            ItemRepository.save(item1);

            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
}
