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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Board;
import com.example.entity.BoardImg;
import com.example.mapper.BoardImageMapper;
import com.example.mapper.BoardMapper;
import com.example.repository.BoardImageRepository;

@RestController
@RequestMapping(value = "/api/board")
public class BoardImageRestController {
    
    @Autowired BoardImageMapper bImageMapper;
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired BoardMapper boardMapper;

    @Autowired BoardImageRepository imageRepository;
    // DTO를 받을 때 @ModelAttribute => multipart/formdata
    // DTO를 받을 때 @RequestBody => application/json



    // 게시글 이미지 1개 등록
    // 127.0.0.1:8080/ROOT/api/board/boardinsertimageOne.json
    @PostMapping(value = "/boardinsertimageOne.json")
    public Map<String, Object> boardinsertimageOnePOST(
            @ModelAttribute BoardImg boardImage,
            @RequestParam(name = "bno") Long bno,
        @RequestParam(name = "file") MultipartFile file){
    Map<String, Object> retMap = new HashMap<>();
    try {
        boardImage.setImagedata(file.getBytes());
        boardImage.setImagesize(file.getSize());
        boardImage.setImagename(file.getOriginalFilename());
        boardImage.setImagetype(file.getContentType());

                Board board = new Board();
                board.setBno(bno);
                boardImage.setBoard(board);


        int ret = bImageMapper.insertBoardImage(boardImage);
        retMap.put("status", 200);
        retMap.put("result", ret);
    }
    catch(Exception e) {
        e.printStackTrace();
        retMap.put("status", -1);
    }
    return retMap;
}


    // 게시글 이미지 일괄 등록
    // 외래키에 해당하는 게시글번호, 파일전송(4가지 정보)
    // 127.0.0.1:8080/ROOT/api/board/boardinsertbatchimage.json
    @PostMapping(value = "/boardinsertbatchimage.json")
    public Map<String, Object> boardinsertimagePOST(
            @RequestParam(name = "bno") Long bno,
            @RequestParam(name = "file") MultipartFile[] file)
            throws IOException {

        Map<String, Object> map = new HashMap<>();
        try {
            List<BoardImg> list = new ArrayList<>();
            for (int i = 0; i < file.length; i++) {
                BoardImg boardImg = new BoardImg();
                boardImg.setImagedata(file[i].getBytes());
                boardImg.setImagename(file[i].getOriginalFilename());
                boardImg.setImagetype(file[i].getContentType());
                boardImg.setImagesize(file[i].getSize());

                Board board = new Board();
                board.setBno(bno);
                boardImg.setBoard(board);

                list.add(boardImg);
            }
            System.out.println("등록완료");

            int ret = bImageMapper.insertBoardbatchImage(list);
            
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


// -------------------------------------------한 게시물 이미지 n개 조회---------------------------------------------

    // 127.0.0.1:8080/ROOT/api/board/image?imgno=53
    @GetMapping(value="/image")
    public ResponseEntity<byte[]> imageGET( @RequestParam(name="imgno") Long imgno) throws IOException{
        
        // 아이템 이미지 번호가 존재하는 경우
        if(imgno > 0L) {           
            BoardImg board = bImageMapper.selectboardimage(imgno);
            
            // 조회된 image가 null이 아닌경우(예외처리)
            if(board != null) {
                
                // 이미지 파일이 존재하는 경우
                if(board.getImagesize() > 0L) {
                    // 타입설정 png인지 jpg인지 gif인지
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType( MediaType.parseMediaType( board.getImagetype() ) );
                    // 실제이미지데이터, 타입이포함된 header, status 200    
                    return new ResponseEntity<byte[]>(board.getImagedata(), headers, HttpStatus.OK);
                }
                
                // 이미지 파일이 존재하지 않는경우 = default이미지 설정
                else {
                InputStream is = resourceLoader.getResource("classpath:/static/image/default.png")
                    .getInputStream();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                // 실제이미지데이터, 타입이포함된 header, status 200    
                return new ResponseEntity<byte[]>(is.readAllBytes(), headers, HttpStatus.OK);
                }
            }
            // 아이템 이미지 조회결과가 null인경우 = default이미지 설정
            else {
                InputStream is = resourceLoader.getResource("classpath:/static/image/default.png")
                    .getInputStream();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                // 실제이미지데이터, 타입이포함된 header, status 200    
                return new ResponseEntity<byte[]>(is.readAllBytes(), headers, HttpStatus.OK);
            }
        }
        // 아이템 이미지 번호가 존재하지 않는경우 = default이미지 설정
        else {
            InputStream is = resourceLoader.getResource("classpath:/static/image/default.png").getInputStream();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            // 실제이미지데이터, 타입이포함된 header, status 200    
            return new ResponseEntity<byte[]>(is.readAllBytes(), headers, HttpStatus.OK);
        }
    }

    
    
    // 게시판 메인페이지 이미지 url 받아오기
     // 127.0.0.1:8080/ROOT/api/board/selectimglist.json?bno=42
    @GetMapping(value="/selectimglist.json")
    public Map<String, Object> selectOne(
         HttpServletRequest request,
         @RequestParam(name = "bno") Long bno){
         Map<String, Object> retMap = new HashMap<>();
         try {
             // 게시판 정보
             List<BoardImg> boardImg = bImageMapper.selectboardimageList(bno);

             for (BoardImg obj : boardImg ) {
                 // 대표이미지 번호를 이용하여 이미지 URL생성
                obj.setImageurl( request.getContextPath() 
             + "/api/board/image?bimageno=" + obj.getBimageno());
             }
 
             retMap.put("status", 200);
             retMap.put("result", boardImg);
         }
         catch(Exception e) {
             e.printStackTrace();
             retMap.put("status", -1);
             retMap.put("result", e.getMessage());
         }
         return retMap;
     }
// ------------------------------------------------------------------------------------------

    // 수정
    // 127.0.0.1:8080/ROOT/api/board/updateimage.json
    @PutMapping(value = "/updateimage.json")
    public Map<String, Object> updateimagePUT( 
        @ModelAttribute BoardImg boardImage,
        @RequestParam(name = "file") MultipartFile file
    ){
        Map<String, Object> map = new HashMap<>();
        try {
            boardImage.setImagedata(file.getBytes());
            boardImage.setImagename(file.getOriginalFilename());
            boardImage.setImagesize(file.getSize());
            boardImage.setImagetype(file.getContentType());

            int ret = bImageMapper.updateImage(boardImage);
            map.put("status", 200);
            map.put("result", ret);
        } 
        catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

        // 이미지 삭제
        // 127.0.0.1:8080/ROOT/api/board/deleteimage.json
        @PostMapping(value = "/deleteimage.json")
        public Map<String, Object> deleteimagePOST( 
            @RequestParam(name = "bno") Long bno,
            @RequestParam(name = "bimageno") Long bimageno
        ){
            Map<String, Object> map = new HashMap<>();
            try {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("bno", bno);
                map1.put("bimageno", bimageno);


                bImageMapper.deleteBoardOneImage(map1);
                map.put("status", 200);
            } 
            catch (Exception e) {
                e.printStackTrace();
                map.put("status", -1);
            }
            return map;
        }
    


}
