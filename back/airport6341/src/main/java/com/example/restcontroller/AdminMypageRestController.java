package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.BoardProject;
import com.example.entity.Member;
import com.example.entity.MemberProject;
import com.example.repository.BoardRepository;
import com.example.repository.MemberRepository;
import com.example.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/admin/mypage")
@RequiredArgsConstructor
public class AdminMypageRestController {

    final MemberRepository mRepository;
    final BoardRepository bRepository;
    final OrderRepository oRepository;


    // 관리자 - 회원 목록 조회(페이지네이션) *회원프로필은 조회하지 않음
    // 127.0.0.1:8080/ROOT/api/admin/mypage/selectmember.json
    @GetMapping(value = "/selectmember.json")
    public Map<String, Object> selectMemberGET(
            @RequestParam(name = "page", defaultValue = "1") int page) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 페이지네이션 설정(0부터, 1페이지에 출력될 개수)
            PageRequest pageRequest = PageRequest.of(page - 1, 10);

            List<MemberProject> list = mRepository.findByOrderByRegdateDesc(pageRequest);
            long count = mRepository.count();

            map.put("list", list);
            map.put("count", count);
            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 관리자 - 회원 차단/차단 해제
    // Member block값 [ 활동중 = 0, 탈퇴함 = 1, 관리자가 회원차단 = 2 ]
    // 127.0.0.1:8080/ROOT/api/admin/mypage/blockmember.json
    @PostMapping(value = "/blockmember.json")
    public Map<String, Object> blockMemberPOST(@RequestParam(name = "userid") String userid) {
        Map<String, Object> map = new HashMap<>();
        try {
            Member member = mRepository.findById(userid).orElse(null);
            // block 값 1은 백으로 넘어오지 않게 프론트에서 조치!
            if (member.getBlock() == 0) { //회원의 block 값이 0인 경우 = 활동중
                member.setBlock(2); // 회원차단 상태로 변경 block = 2
            } else if(member.getBlock() == 2){ //회원의 block값이 2인 경우 = 관리자가 회원차단
                member.setBlock(0); // 활동중 상태로 변경 block = 0
            } //회원의 block값이 2인 경우 변동없음
            Member member1 = mRepository.save(member);
            System.out.println(member1);
            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 관리자 - 회원 게시글 조회(페이지네이션)
    // 127.0.0.1:8080/ROOT/api/admin/mypage/selectboard.json
    @GetMapping(value = "/selectboard.json")
    public Map<String, Object> selectBoardGET(
            @RequestParam(name = "page", defaultValue = "1") int page) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 페이지네이션 설정(0부터, 1페이지에 출력될 개수)
            PageRequest pageRequest = PageRequest.of(page - 1, 10);

            // 게시글 목록
            List<BoardProject> list = bRepository.findByOrderByBnoDesc(pageRequest);

            // 전체 게시글 갯수
            long count = bRepository.count();

            map.put("list", list);
            map.put("count", count);
            map.put("pages", (count - 1) / 10 + 1);
            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 관리자 - 회원 게시글 삭제
    // 127.0.0.1:8080/ROOT/api/admin/mypage/deleteboard.json
    @PostMapping(value = "/deleteboard.json")
    public Map<String, Object> deleteBoardrPOST(
            @RequestParam(name = "bno") Long bno) {
        Map<String, Object> map = new HashMap<>();
        try {
            bRepository.deleteById(bno);

            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 관리자 - 회원 주문 목록 확인
    // 127.0.0.1:8080/ROOT/api/admin/mypage/selectorder.json
    @GetMapping(value = "/selectorder.json")
    public Map<String, Object> selectOrderPOST(
        @RequestParam(name = "page", defaultValue = "1") int page) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 페이지네이션 설정(0부터, 1페이지에 출력될 개수)
            PageRequest pageRequest = PageRequest.of(page - 1, 10);

            oRepository.findByOrderByOnoDesc(pageRequest);

            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

}
