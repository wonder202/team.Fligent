package com.example.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.BoardSelectOneDto;
import com.example.dto.CustomerUpdatePw;
import com.example.dto.LikeDTO;
import com.example.entity.Board;
import com.example.entity.Member;
import com.example.entity.Reply;
import com.example.mapper.BoardMapper;
import com.example.mapper.CustomerMapper;
import com.example.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/customer/mypage")
@RequiredArgsConstructor
public class CustomerMypageRestController {

    final PasswordEncoder passwordEncoder;
    final MemberRepository mRepository;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    CustomerMapper cMapper;

    @Autowired
    BoardMapper bMapper;

    // 127.0.0.1:8080/ROOT/api/customer/mypage/passwordcheck.json
    // 기존 비번 체크
    @PostMapping(value = "/passwordcheck.json")
    public Map<String, Object> passwordcheckGET(
            HttpServletRequest httpServletRequest,
            @RequestBody Map<String,String> member1
            // RequestBody로 birth , phone , userid를 받아옴
    ){
        Map<String, Object> map = new HashMap<>();
        try {
            String userid = (String) httpServletRequest.getAttribute("username");
        
            // db에 회원 정보가 있는지 조회
            Member member = cMapper.selectMemberOne(userid);

            map.put("status", 1);
            map.put("result", "정보가 일치 하지 않습니다.");

            if (member != null) {       // db정보에 회원정보가 있다면
                if (passwordEncoder.matches(member1.get("userpw"), member.getUserpw())) {
                    map.put("status", 200);
                    map.put("result", "정보가 일치 합니다.");
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }


    // 사용자가 로그인 한 상태에서 비밀번호 찾은 후 비밀번호 변경
    // 현재 암호, 바꿀 암호 입력
    // 127.0.0.1:8080/ROOT/api/customer/mypage/mypageupdatepassword.json
    // {"userpw": "a", "userpw1":"b"} 
    @PutMapping(value = "mypageupdatepassword.json")
    public Map<String, Object> findpasswordGET(
            HttpServletRequest httpServletRequest,
            @RequestBody Map<String,String> member1
            // RequestBody로 birth , phone , userid를 받아옴
    ){
        Map<String, Object> map = new HashMap<>();
        try {
            String userid = (String) httpServletRequest.getAttribute("username");
        
            // db에 회원 정보가 있는지 조회
            Member member = cMapper.selectMemberOne(userid);
            if (member != null) {       // db정보에 회원정보가 있다면
                if (passwordEncoder.matches(member1.get("userpw"), member.getUserpw())) {
                    String encpass = passwordEncoder.encode(member1.get("userpw1"));
                    member.setUserpw(encpass);
                    int ret = cMapper.updatePassword(member);
                    if (ret == 1) {
                        map.put("status", 200);
                    } else {
                        map.put("status", 1);
                        map.put("result", "수정 오류");
                    }
                }
                else{
                    map.put("status", 1);
                    map.put("result", "정보가 일치 하지 않습니다.");
                }
            } else {
                map.put("status", 0);
                map.put("result", "정보가 일치 하지 않습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }





    // 회원탈퇴(회원 이미지 삭제 보류중)
    // vue에서 비번 받아오기, security로 아이디 받아오기,
    // 아이디로 db에서 비번 조회, vue비번과 db비번 매칭
    // Member block값 [ 활동중 = 0, 탈퇴함 = 1, 관리자가 회원차단 = 2 ]
    // 127.0.0.1:8080/ROOT/api/customer/mypage/delete.json
    @PostMapping(value = "/delete.json")
    public Map<String, Object> deletePost(
            @RequestBody Member member,
            HttpServletRequest httpServletRequest) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userid = (String) httpServletRequest.getAttribute("username");

            // mapper + xml로 db 조회 (id로 정보 들고오기)
            String dbpw = cMapper.selectMemberOne(userid).getUserpw();
            // member.getUserpw() 뷰 비번과 db로 온 pw 일치하나 확인하기

            // if문으로 match 성공시 회원탈퇴(쿼리문), 실패시 오류 메세지
            if (passwordEncoder.matches(member.getUserpw(), dbpw)) {
                member.setUserid(userid);
                member.setAddress(null);
                member.setAirportname(null);
                member.setBirth(null);
                member.setBlock(1);
                member.setNickname(null);
                member.setPhone(null);
                member.setRegdate(null);
                member.setRole(null);
                member.setUserpw(null);
                
                // 이미지 파일도 삭제해야함 (인섭 = 이미지restcontroller에 삭제 구현 완료)
                int ret = cMapper.deleteMember(member);
                if (ret == 1) {
                    map.put("status", 200);
                } else {
                    map.put("status", 1);
                    map.put("result", "탈퇴오류");
                }
            } else {
                map.put("status", 0);
                map.put("result", "암호매칭 실패");
            }
            // 현재 해야할 것 
            // 1. 아이디로 회원정보 들고오는 쿼리 = 완료(2022-10-26) 
            // 2. 회원탈퇴 쿼리(아이디 제외 null) = 완료(2022-10-26) 
            // 3. 회원이미지 삭제 쿼리 = 완료(2022-10-26)
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 회원정보 수정시 기존 회원정보 받아오기 (GET)
    // 프로필 이미지는 CutomerMypageImageRestController에 있음
    // 127.0.0.1:8080/ROOT/api/customer/mypage/update.json
    @GetMapping(value = "/update.json")
    public Map<String, Object> updateGet(
            HttpServletRequest httpServletRequest) {

        String userid = (String) httpServletRequest.getAttribute("username");
        Map<String, Object> map = new HashMap<>();
        try {
            // mapper + xml로 db 조회 (id로 정보 들고오기)
            Member member = cMapper.selectMemberOne(userid);
            member.setUserpw(null);
            member.setRegdate(null);
            member.setRole(null);

            map.put("status", 200);
            map.put("result", member);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 회원정보 수정시 닉네임 중복확인
    // param으로 nickname 받아옴
    // 127.0.0.1:8080/ROOT/api/customer/mypage/nicknamecheck.json
    @PostMapping(value = "/nicknamecheck.json")
    public Map<String, Object> idcheckPost(
            @RequestParam(name = "nickname") String nickname) {
        Map<String, Object> map = new HashMap<>();
        try {
            int ret = cMapper.nicknamecheck(nickname);
            if (ret < 1) {
                map.put("status", 200);
            } else {
                map.put("status", 1);
                map.put("result", "중복");
            }
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 회원정보 수정하기 (POST)
    // vue에서 비밀번호 받아오기(userpw, userpw1), db비번과 매치, 맞으면 변경 틀리면 오류메세지
    // 127.0.0.1:8080/ROOT/api/customer/mypage/update.json 
    @PostMapping(value = "/update.json")
    public Map<String, Object> updatePost(
            @RequestBody Member member1,
            HttpServletRequest httpServletRequest
    // @AuthenticationPrincipal CustomUser user
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userid = (String) httpServletRequest.getAttribute("username");
            // String userid = "hoon2"; // 시큐리티에서 받아오기(보류)

            // mapper + xml로 db 조회 (id로 정보 들고오기)
            Member member = cMapper.selectMemberOne(userid);
            member.setNickname(member1.getNickname());
            member.setPhone(member1.getPhone());
            member.setBirth(member1.getBirth());

            int ret = cMapper.updateMember(member);

            if (ret == 1) {
                map.put("status", 200);
            } else {
                map.put("status", 1);
                map.put("result", "수정오류");
            }
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 비밀번호 수정
    // 비밀 번호를 db와 매치후 맞으면 변경
    // vue에서 비밀번호 받아오기(userpw, userpw1), db비번과 매치, 맞으면 변경 틀리면 오류 메세지
    // 127.0.0.1:8080/ROOT/api/customer/mypage/updatepw.json
    @PostMapping(value = "/updatepw.json")
    public Map<String, Object> updatepwPost(
            @RequestBody CustomerUpdatePw updatepw,
            @RequestBody Member member2,
            HttpServletRequest httpServletRequest
    // @AuthenticationPrincipal CustomUser user
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userid = (String) httpServletRequest.getAttribute("username");
            // String userid = "hoon1"; // 시큐리티에서 받아오기(보류)

            // mapper + xml로 db 조회 (id로 정보 들고오기)
            String dbpw = cMapper.selectMemberOne(userid).getUserpw();

            // member.getUserpw() 뷰 비번과 db로 온 pw 일치하나 확인하기
            // if문으로 match 성공시 비번 변경(쿼리문), 실패시 오류 메세지
            if (passwordEncoder.matches(updatepw.getUserpw(), dbpw)) {
                Member member = new Member();
                member.setUserid(userid); // 시큐리티 id보류
                String encpass = passwordEncoder.encode(updatepw.getUserpw1());
                member.setUserpw(encpass);
                
                int ret = cMapper.updatePassword(member);
                if (ret == 1) {
                    map.put("status", 200);
                } else {
                    map.put("status", 1);
                    map.put("result", "수정오류");
                }
            } else {
                map.put("status", 0);
                map.put("result", "암호매칭 실패");
            }
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

    


    // 내가 작성한 글 GET (목록만! 사진 제외 => 인섭 완료)
    // 127.0.0.1:8080/ROOT/api/customer/mypage/boardlist.json
    @GetMapping(value = "/boardlist.json")
    public Map<String, Object> myboardlistGet(
        @RequestParam(name = "start") int start,
        // @AuthenticationPrincipal CustomUser user
        HttpServletRequest httpServletRequest
        ) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        try {
            String userid = (String) httpServletRequest.getAttribute("username");
  

            map1.put("userid", userid);
            map1.put("start", start * 10 - 9);
            List<Board> list = cMapper.myboardlist(map1);
            Long count = cMapper.myboardlistcount(userid);

            map.put("status", 200);
            map.put("result", list);
            map.put("count", count);
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

// ----------------------------------------------- 댓글 기능 -----------------------------------------------------
   


    // 내가 작성한 댓글목록 조회 GET
    // 127.0.0.1:8080/ROOT/api/customer/mypage/boardreplylist.json
    @GetMapping(value="/boardreplylist.json")
    public Map<String, Object> myboardreplylistGet(
        @RequestParam(name = "start") int start,
        HttpServletRequest httpServletRequest
        ){
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        try{
        String userid = (String) httpServletRequest.getAttribute("username");

        map1.put("userid", userid);
        map1.put("start", start*10-9);
        List<Reply> list =  cMapper.myboardreplylist(map1);
        Long count = cMapper.myboardreplycount(userid);

        // map.put("result", member);
        map.put("status", 200);
        map.put("result", list);
        map.put("count", count);

        }
        catch (Exception e){
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }


    // 내가 작성한 댓글 수정하기
    // 127.0.0.1:8080/ROOT/api/customer/mypage/updatereplyone.json
    @PutMapping(value = "/updatereplyone.json")
    public Map<String,Object> updatereplyonePUT(
        @RequestBody Reply reply,
        HttpServletRequest httpServletRequest){
        Map<String, Object> map = new HashMap<>();
        try{
            String userid = (String) httpServletRequest.getAttribute("username");
            Member member = new Member();
            member.setUserid(userid);
            reply.setMember(member);

            System.out.println(userid.toString());
            System.out.println(reply.toString());

            int ret = cMapper.updateoneReply(reply);
            if (ret == 1) {
                map.put("status", 200);
            } else {
                map.put("status", 0);
                map.put("result", "댓글수정 실패");
            }
        }

        catch (Exception e){
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 내가 작성한 댓글 삭제
    // 127.0.0.1:8080/ROOT/api/customer/mypage/deleteoneimage.json?rno=
    @PostMapping(value = "/deleteoneimage.json")
    public Map<String, Object> deleteoneimage( @RequestParam Long rno 
    , HttpServletRequest httpServletRequest){

        Map<String, Object> map = new HashMap<>();

        try {
            String userid = (String) httpServletRequest.getAttribute("username");
            Member member = new Member();
            member.setUserid(userid);

            int ret = cMapper.deleteoneReply(rno);
            map.put("status", 200);
            map.put("result", ret);
        } 
        catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // @GetMapping(value = "/boardreplyone.json")
    // public Map<String, Object> boardreplyoneGET(
    //     @RequestParam(name = "bno") Long bno,
    //     HttpServletRequest httpServletRequest
    // ){
    //     Map<String, Object> map = new HashMap<>();
    //     try {
    //         Map<String,Object> map1 = new HashMap<>();
    //         String userid = (String) httpServletRequest.getAttribute("username");

    //         map1.put("userid", userid);
    //         map1.put("bno",bno);

    //         Reply reply = cMapper.boardreplyone(map1);
    //         if (reply != null) {
    //             int ret = cMapper.updateoneReply(reply);
    //             map.put("ret", ret);
    //             map.put("status", 200);
    //         }
    //         else{
    //             map.put("status", 0);
    //         }
    //     } catch (Exception e) {
    //         map.put("status", -1);
    //         map.put("result", e.getMessage());
    //     }
    //     return map;
    // }


    // 내가 좋아요 누른 글목록 GET
    // 127.0.0.1:8080/ROOT/api/customer/mypage/boardlikelist.json
    @GetMapping(value="/boardlikelist.json")
    public Map<String, Object> myboardlikelistGet(
        @RequestParam(name = "start") int start,
        HttpServletRequest httpServletRequest
        ){
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        try{
        String userid = (String) httpServletRequest.getAttribute("username");

        map1.put("userid", userid);
        map1.put("start", start*10-9);
        List<LikeDTO> list =  cMapper.myboardlikelist(map1);
        Long count =cMapper.myboardlikecount(userid);
        
        List<BoardSelectOneDto> list2 = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            BoardSelectOneDto ret = bMapper.selectBoardOne1(list.get(i).getBno());

            list2.add(ret);
        }

        map.put("status", 200);
        map.put("result", list2);
        map.put("count", count);

        }
        catch (Exception e){
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 좋아요 해제
    // 127.0.0.1:8080/ROOT/api/customer/mypage/deletelike.json
    @PostMapping(value = "/deletelike.json")
    public Map<String, Object> postlikePOST(
            HttpServletRequest httpServletRequest, 
            @RequestParam Long bno) {
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, Object> map1 = new HashMap<>();
            String username = (String) httpServletRequest.getAttribute("username");

            // Member member = new Member();
            // member.setUserid(username);
            // likes.setMember(member);

            map1.put("userid", username);
            map1.put("bno", bno);
            int ret = bMapper.DeleteLike1(map1);

            if(ret == 1){ //삭제된 경우
                map.put("status", 200);
            } else {
                map.put("status", 0);
            }

        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }


    // 127.0.0.1:8080/ROOT/api/customer/mypage/myairfavorite.json?airportname=
    // 공항 즐겨 찾기(관심 공항 설정 1개) 변경
    @PutMapping(value = "/myairfavorite.json")
    public Map<String,Object> myairfavoriteGET(
        @RequestParam(name = "airportname") String airportname,
        HttpServletRequest httpServletRequest ){
        Map<String,Object> map = new HashMap<>();
        try {
            Map<String,Object> map1 = new HashMap<>();
            String userid = (String) httpServletRequest.getAttribute("username");
            Member member = new Member();
            member.setUserid(userid);

            map1.put("userid", userid);
            map1.put("airportname", airportname);

            int ret = cMapper.updateAirportname(map1);
            if(ret == 1 ){
                map.put("status", 200);
                map.put("airportname", airportname);
            }
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 127.0.0.1:8080/ROOT/api/customer/mypage/myairfavorite.json?airportname=
    // 공항 즐겨 찾기(관심 공항 설정 1개) 해제
    @PutMapping(value = "/myairfavoritenull.json")
    public Map<String,Object> myairfavoritenullGET(
        @RequestParam(name = "airportname") String airportname,
        HttpServletRequest httpServletRequest ){
        Map<String,Object> map = new HashMap<>();
        try {
            Map<String,Object> map1 = new HashMap<>();
            String userid = (String) httpServletRequest.getAttribute("username");
            Member member = new Member();
            member.setUserid(userid);

            map1.put("userid", userid);
            map1.put("airportname", airportname);

            int ret = cMapper.updateAirportname(map1);
            if(ret == 1 ){
                map.put("status", 200);
                map.put("result" , "변경된듯");
            }
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 임시 비밀번호 생성
    public String randomPassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                                       .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                                       .limit(targetStringLength)
                                       .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                       .toString();
        System.out.println(generatedString);
        return generatedString;
      }

}
