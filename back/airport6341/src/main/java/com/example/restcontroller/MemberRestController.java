package com.example.restcontroller;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Member;
import com.example.entity.MemberImg;
import com.example.entity.Token;
import com.example.jwt.JwtUtil;
import com.example.mapper.CustomerMapper;
import com.example.repository.MemberImgRepository;
import com.example.repository.MemberRepository;
import com.example.repository.TokenRepository;
import com.example.service.RegisterMail;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/member")
@RequiredArgsConstructor
public class MemberRestController {

    final PasswordEncoder passwordEncoder;
    final AuthenticationManager authenticationManager;
    final MemberRepository mRepository;
    final MemberImgRepository imageRepository;
    final TokenRepository tRepository;
    final JwtUtil jwtUtil;

    @Autowired
    CustomerMapper cMapper;

    @Autowired
    RegisterMail registerMail;

    // 비밀번호 찾기
    // 2022-10-28 추가요구사항 : 비밀번호 찾기시 비밀번호 변경 전 이메일, 생일 , 연락처 DB정보와 같으면 비밀번호 변경
    // 1. vue에서 던지는 userid를 받아와서 db에 정보 조회
    // 2. 정보가 있다면 birth, phone을 vue에서 받아와서 db에 정보 조회
    // 3. 정보가 맞으면 body로 비밀번호 변경 하기

    // 아이디 찾기
    // 생일, 연락처 정보가 DB 정보와 일치 할때
    // 127.0.0.1:8080/ROOT/api/member/findidgetmember.json
    // {"birth":20221004,"phone":"123-123-123"}
    @GetMapping(value = "findidgetmember.json")
    public Map<String, Object> findIdGET(
            @RequestBody Member member1) {
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("birth", member1.getBirth()); // 생일
            map1.put("phone", member1.getPhone()); // 연락처
            Member member = cMapper.selectMemberidbirthandphone(map1); // 생일 , 연락처 정보 DB 조회
            if (member != null) {
                map.put("status", 200);
                map.put("result", member);
            } else {
                map.put("status", 0);
                map.put("result", "정보가 일치 하지 않습니다");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }

        return map;
    }

    // 로그인 안한 상태에서 비밀번호 변경시 메일 발송
    // 127.0.0.1:8080/ROOT/api/member/sendmail.json
    // {"userid": "a" ,"phone":"123-123-123", "birth":20221004}
    @PostMapping(value = "sendmail.json")
    public Map<String, Object> findpasswordGET(
            @RequestBody Member member1
    // RequestBody로 birth , phone , userid를 받아옴
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, Object> map1 = new HashMap<>();

            // 데이터 map에다가 압축
            map1.put("birth", member1.getBirth()); // 생일
            map1.put("phone", member1.getPhone()); // 전화 번호
            map1.put("userid", member1.getUserid()); // 유저 아이디

            // db에 회원 정보가 있는지 조회
            Member member = cMapper.selectPasswordMemberOne(map1);
            if (member != null) { // db정보에 회원정보가 있다면
                // 인증메일 발송
                String code = registerMail.sendSimpleMessage(member.getUserid());
                if (code != null) {
                    map.put("status", 200);
                    map.put("code", code);
                } else {
                    map.put("status", 1);
                    map.put("result", "사용자 메일주소가 유효하지 않음");
                }
            } else {
                map.put("status", 0);
                map.put("result", "사용자 정보 불일치");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // 비밀번호 찾아서 이메일, 생일 연락처 DB정보에 매칭시켜서 같으면 비밀번호 변경
    // 127.0.0.1:8080/ROOT/api/member/setnewpw.json
    @PutMapping(value = "setnewpw.json")
    public Map<String, Object> setNewPwGET(
            @RequestBody Map<String, String> setpw
    // setpw => {"userid": "아이디", "userpw": "바꿀암호"}
    ) {
        Map<String, Object> map = new HashMap<>();
        try {

            // Map<String, Object> map1 = new HashMap<>();

            // 데이터 map에다가 압축
            // map1.put("birth", member1.getBirth()); // 생일
            // map1.put("phone", member1.getPhone()); // 전화 번호
            // map1.put("userid", member1.getUserid()); // 유저 아이디

            // db에 회원 정보가 있는지 조회
            Member member = cMapper.selectMemberOne(setpw.get("userid"));
            if (member != null) { // db정보에 회원정보가 있다면
                String encpass = passwordEncoder.encode(setpw.get("userpw"));
                member.setUserpw(encpass);
                int ret = cMapper.updatePassword(member);
                if (ret == 1) {
                    map.put("status", 200);
                } else {
                    map.put("status", 1);
                    map.put("result", "수정 오류");
                }
            } else {
                map.put("status", 0);
                map.put("result", "사용자 정보 불일치");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // 완료 - 회원가입
    // 127.0.0.1:8080/ROOT/api/member/join.json
    @PostMapping(value = "/join.json")
    public Map<String, Object> joinPost(@RequestBody Member member) {
        Map<String, Object> map = new HashMap<>();
        try {
            member.setUserpw(passwordEncoder.encode(member.getUserpw()));
            mRepository.save(member);
            // Member member1 = mRepository.save(member);
            map.put("status", 200);
            // map.put("result", member1);
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 완료 - 회원가입시 아이디 중복확인
    // 127.0.0.1:8080/ROOT/api/member/idcheck.json?userid=user1
    @GetMapping(value = "/idcheck.json")
    public Map<String, Object> idcheckGET(
            @RequestParam(name = "userid") String userid) {
        Map<String, Object> map = new HashMap<>();
        try {
            boolean ret = mRepository.existsById(userid);
            map.put("status", 200);
            map.put("result", ret); // 있으면 참(TRUE), 없으면 거짓(FALSE)
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 완료 - 회원가입시 닉네임 중복확인
    // param으로 nickname 받아옴
    // 127.0.0.1:8080/ROOT/api/member/nicknamecheck.json?nickname=user1
    @PostMapping(value = "/nicknamecheck.json")
    public Map<String, Object> idcheckPost(
            @RequestParam(name = "nickname") String nickname
    // @AuthenticationPrincipal CustomUser user
    ) {
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

    // 완료 - 로그인
    // 아이디, 암호, 권한이 와야함
    // {"userid" : "아이디", "userpw" :"암호", "role" : "CUSTOMER(권한)"}
    // 127.0.0.1:8080/ROOT/api/member/login.json
    @PostMapping(value = "/login.json")
    public Map<String, Object> loginPOST(@RequestBody Member member) {
        Map<String, Object> map = new HashMap<>();
        try {
            // System.out.println("member ====> " + member);
            
            // 권한정보 변경을 위한 문자배열
            String[] strRole = { member.getRole() };
            
            // 문자배열을 Collection타입으로 변환
            Collection<GrantedAuthority> role = AuthorityUtils.createAuthorityList(strRole);
            
            // 매니저를 이용한 인증, userdetailservice와 같은 기능
            UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken(member.getUserid(),
            member.getUserpw(), role);
            
            // userdetailservice를 사용하지 못하니 같은 기능의 authenticationManager 사용
            // = DB에 저장된 회원정보와 비교
            authenticationManager.authenticate(token1);
            // System.out.println("111111111111111111111111");


            // 로그인 한 사용자의 토큰 생성
            String token = JwtUtil.generateToken(member.getUserid(), member.getRole());
            // System.out.println("로그인 한 사람 새로 만든 token  => " + token);
            
            // System.out.println("222222222222222222222222222");
            
            
            // TOKEN UPSERT를 위한 조회
            Token searchToken = tRepository.findTokenByMember_userid(member.getUserid());
            // System.out.println("기존 token 있는지 확인  => " + searchToken);

            // System.out.println("조회된 token 저장전!!!! ========> " + searchToken.getToken());
            // System.out.println("3333333333333333333333333333");


            // default null이 아닐때 디폴트로 많이 만든다
            // if - else 사용하지 말고 if만 사용한다
            if (searchToken != null) { // 토큰이 있는경우
                // 기존토큰 삭제
                tRepository.delete(searchToken);
                // System.out.println("기존토큰 삭제됨");
            // System.out.println("44444444444444444444444444");

            }
            // 토큰 생성 후 DB 저장
            Token obj = new Token();
            obj.setToken(token);
            obj.setMember(member);
            obj.setRole(member.getRole());
            // System.out.println("555555555555555555555555555");
            // System.out.println(obj.getToken());
            
            
            tRepository.save(obj);
            // System.out.println("666666666666666666666");
            
            map.put("result", token);
            map.put("status", 200);

        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 완료-회원 프로필 사진 등록
    // 127.0.0.1:8080/ROOT/api/member/imginsert.json
    @PostMapping(value = "/imginsert.json")
    public Map<String, Object> insertPOST(
            @ModelAttribute MemberImg image,
            @RequestParam(name = "file") MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        try {

            image.setImagename(file.getOriginalFilename());
            image.setImagesize(file.getSize());
            image.setImagetype(file.getContentType());
            image.setImagedata(file.getBytes());

            imageRepository.save(image);

            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 로그아웃
    // LogoutHandler를 이용한 로그아웃 처리
    // 127.0.0.1:8080/ROOT/api/member/logout.json
    @PostMapping(value = "/logout.json")
    public Map<String, Object> logout1GET(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication auth,
            @RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // LogoutHandler 실행
            new SecurityContextLogoutHandler().logout(request, response, auth);
            // DB에서 토큰 삭제
            tRepository.deleteById(token);
            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", -1);
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
        // System.out.println(generatedString);
        return generatedString;
    }
}