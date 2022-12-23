package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.BoardOneReplyDto;
import com.example.entity.Member;
import com.example.entity.Reply;
import com.example.mapper.BoardMapper;
import com.example.mapper.BoardReplyMapper;
import com.example.mapper.CustomerMapper;

@RestController
@RequestMapping("/api/board")
public class BoardReplyRestController {

    @Autowired BoardReplyMapper bReplyMapper;

    @Autowired BoardMapper bmapper;

    @Autowired
    CustomerMapper cMapper;


    // 완료 - 댓글 등록하기
    // 127.0.0.1:8080/ROOT/api/board/insertreply.json
    // {"content":"11111" ,"board":{"bno": 57}}
    @PostMapping(value = "/insertreply.json")
    public Map<String, Object> insertBoardReplyOnePOST(
            @RequestBody Reply reply,
            HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            // reply member에 넣어줄  username가져오기
            String username = (String) request.getAttribute("username");

            // reply에 넣어줄 member 생성
            Member member = new Member();
            member.setUserid(username);

            reply.setMember(member);

            int ret = bReplyMapper.insertBoardReplyOne(reply);
            if (ret == 1) {
                map.put("status", 200);
            } else {
                map.put("status", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // 완료 - 작성한 댓글 수정하기(사용자 일치여부 확인)
    // 127.0.0.1:8080/ROOT/api/board/updatereply.json
    // {"content":"11111" ,"board":{"bno": 57}, "rno":124}
    @PostMapping(value = "/updatereply.json")
    public Map<String,Object> updatereplyonePUT(
        @RequestBody Reply reply,
        HttpServletRequest httpServletRequest){
        Map<String, Object> map = new HashMap<>();
        try{
            // 댓글 작성자와 수정하려는 사용자가 일치하는지 확인
            // 1. 댓글 번호에 해당하는 댓글 정보 불러오기 => 댓글 작성자 확인
            BoardOneReplyDto selectReply = bReplyMapper.selectReplyDTOOne(reply.getRno());

            // 2. 토큰에 사용자 userid확인하기
            String userid = (String) httpServletRequest.getAttribute("username");

            // 일치하는지 확인
            if(selectReply.getUserid().equals(userid)){
                int ret = bReplyMapper.updateoneReply(reply);
                if (ret == 1) {
                    map.put("status", 200);
                    map.put("result", "댓글수정 성공");
                } else {
                    map.put("status", 1);
                    map.put("result", "댓글수정 실패");
                }
            }
            else { //게시글 작성자와 현재 수정하려 하는 사용자가 다른경우
                map.put("status", 0);
                map.put("result", "사용자 불일치");
            }
        }
            catch (Exception e){
                map.put("status", -1);
                map.put("result", e.getMessage());
            }
            return map;
    }

    // 완료 - 작성한 댓글 삭제(사용자 일치여부 확인)
    // 127.0.0.1:8080/ROOT/api/board/deletereply.json?rno=
    @PostMapping(value = "/deletereply.json")
    public Map<String, Object> deleteoneimage( 
        @RequestParam Long rno, 
        HttpServletRequest httpServletRequest){
        Map<String, Object> map = new HashMap<>();
        try {
            
            // 내가 작성한 댓글만 삭제 가능
            // 삭제하려는 댓글의 번호로 해당댓글의 작성자 조회
            BoardOneReplyDto selectReply = bReplyMapper.selectReplyDTOOne(rno);

            // 2. 토큰에 사용자 userid확인하기
            String userid = (String) httpServletRequest.getAttribute("username");

            // 일치하는지 확인
            if(selectReply.getUserid().equals(userid)){
                int ret = cMapper.deleteoneReply(rno);
                if (ret == 1) {
                    System.out.println("댓글삭제완료!");
                    map.put("status", 200);
                    // map.put("result", ret);
                } else {
                    map.put("status", 1);
                    map.put("result", "댓글삭제 실패");
                }
            }
            else { //게시글 작성자와 현재 삭제하려 하는 사용자가 다른경우
                map.put("status", 0);
                map.put("result", "사용자 불일치");
            }            
        } 
        catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // 댓글 1개 조회
    // 127.0.0.1:8080/ROOT/api/board/selectreply.json?rno=
    @GetMapping(value = "/selectreply.json")
    public Map<String, Object> selectonereply( 
        @RequestParam Long rno){
        Map<String, Object> map = new HashMap<>();
        try {
            // 삭제하려는 댓글의 번호로 해당댓글의 작성자 조회
            BoardOneReplyDto selectReply = bReplyMapper.boardSelectOneReply(rno);
                if (selectReply != null) {
                    map.put("status", 200);
                    map.put("result", selectReply);
                } else {
                    map.put("status", 1);
                    map.put("result", "댓글조회실패");
                }
        } 
        catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

}
