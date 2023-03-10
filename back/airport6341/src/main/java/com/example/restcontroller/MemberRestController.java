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

    // ???????????? ??????
    // 2022-10-28 ?????????????????? : ???????????? ????????? ???????????? ?????? ??? ?????????, ?????? , ????????? DB????????? ????????? ???????????? ??????
    // 1. vue?????? ????????? userid??? ???????????? db??? ?????? ??????
    // 2. ????????? ????????? birth, phone??? vue?????? ???????????? db??? ?????? ??????
    // 3. ????????? ????????? body??? ???????????? ?????? ??????

    // ????????? ??????
    // ??????, ????????? ????????? DB ????????? ?????? ??????
    // 127.0.0.1:8080/ROOT/api/member/findidgetmember.json
    // {"birth":20221004,"phone":"123-123-123"}
    @GetMapping(value = "findidgetmember.json")
    public Map<String, Object> findIdGET(
            @RequestBody Member member1) {
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("birth", member1.getBirth()); // ??????
            map1.put("phone", member1.getPhone()); // ?????????
            Member member = cMapper.selectMemberidbirthandphone(map1); // ?????? , ????????? ?????? DB ??????
            if (member != null) {
                map.put("status", 200);
                map.put("result", member);
            } else {
                map.put("status", 0);
                map.put("result", "????????? ?????? ?????? ????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }

        return map;
    }

    // ????????? ?????? ???????????? ???????????? ????????? ?????? ??????
    // 127.0.0.1:8080/ROOT/api/member/sendmail.json
    // {"userid": "a" ,"phone":"123-123-123", "birth":20221004}
    @PostMapping(value = "sendmail.json")
    public Map<String, Object> findpasswordGET(
            @RequestBody Member member1
    // RequestBody??? birth , phone , userid??? ?????????
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, Object> map1 = new HashMap<>();

            // ????????? map????????? ??????
            map1.put("birth", member1.getBirth()); // ??????
            map1.put("phone", member1.getPhone()); // ?????? ??????
            map1.put("userid", member1.getUserid()); // ?????? ?????????

            // db??? ?????? ????????? ????????? ??????
            Member member = cMapper.selectPasswordMemberOne(map1);
            if (member != null) { // db????????? ??????????????? ?????????
                // ???????????? ??????
                String code = registerMail.sendSimpleMessage(member.getUserid());
                if (code != null) {
                    map.put("status", 200);
                    map.put("code", code);
                } else {
                    map.put("status", 1);
                    map.put("result", "????????? ??????????????? ???????????? ??????");
                }
            } else {
                map.put("status", 0);
                map.put("result", "????????? ?????? ?????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // ???????????? ????????? ?????????, ?????? ????????? DB????????? ??????????????? ????????? ???????????? ??????
    // 127.0.0.1:8080/ROOT/api/member/setnewpw.json
    @PutMapping(value = "setnewpw.json")
    public Map<String, Object> setNewPwGET(
            @RequestBody Map<String, String> setpw
    // setpw => {"userid": "?????????", "userpw": "????????????"}
    ) {
        Map<String, Object> map = new HashMap<>();
        try {

            // Map<String, Object> map1 = new HashMap<>();

            // ????????? map????????? ??????
            // map1.put("birth", member1.getBirth()); // ??????
            // map1.put("phone", member1.getPhone()); // ?????? ??????
            // map1.put("userid", member1.getUserid()); // ?????? ?????????

            // db??? ?????? ????????? ????????? ??????
            Member member = cMapper.selectMemberOne(setpw.get("userid"));
            if (member != null) { // db????????? ??????????????? ?????????
                String encpass = passwordEncoder.encode(setpw.get("userpw"));
                member.setUserpw(encpass);
                int ret = cMapper.updatePassword(member);
                if (ret == 1) {
                    map.put("status", 200);
                } else {
                    map.put("status", 1);
                    map.put("result", "?????? ??????");
                }
            } else {
                map.put("status", 0);
                map.put("result", "????????? ?????? ?????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // ?????? - ????????????
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

    // ?????? - ??????????????? ????????? ????????????
    // 127.0.0.1:8080/ROOT/api/member/idcheck.json?userid=user1
    @GetMapping(value = "/idcheck.json")
    public Map<String, Object> idcheckGET(
            @RequestParam(name = "userid") String userid) {
        Map<String, Object> map = new HashMap<>();
        try {
            boolean ret = mRepository.existsById(userid);
            map.put("status", 200);
            map.put("result", ret); // ????????? ???(TRUE), ????????? ??????(FALSE)
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ?????? - ??????????????? ????????? ????????????
    // param?????? nickname ?????????
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
                map.put("result", "??????");
            }
        } catch (Exception e) {
            map.put("status", 0);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ?????? - ?????????
    // ?????????, ??????, ????????? ?????????
    // {"userid" : "?????????", "userpw" :"??????", "role" : "CUSTOMER(??????)"}
    // 127.0.0.1:8080/ROOT/api/member/login.json
    @PostMapping(value = "/login.json")
    public Map<String, Object> loginPOST(@RequestBody Member member) {
        Map<String, Object> map = new HashMap<>();
        try {
            // System.out.println("member ====> " + member);
            
            // ???????????? ????????? ?????? ????????????
            String[] strRole = { member.getRole() };
            
            // ??????????????? Collection???????????? ??????
            Collection<GrantedAuthority> role = AuthorityUtils.createAuthorityList(strRole);
            
            // ???????????? ????????? ??????, userdetailservice??? ?????? ??????
            UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken(member.getUserid(),
            member.getUserpw(), role);
            
            // userdetailservice??? ???????????? ????????? ?????? ????????? authenticationManager ??????
            // = DB??? ????????? ??????????????? ??????
            authenticationManager.authenticate(token1);
            // System.out.println("111111111111111111111111");


            // ????????? ??? ???????????? ?????? ??????
            String token = JwtUtil.generateToken(member.getUserid(), member.getRole());
            // System.out.println("????????? ??? ?????? ?????? ?????? token  => " + token);
            
            // System.out.println("222222222222222222222222222");
            
            
            // TOKEN UPSERT??? ?????? ??????
            Token searchToken = tRepository.findTokenByMember_userid(member.getUserid());
            // System.out.println("?????? token ????????? ??????  => " + searchToken);

            // System.out.println("????????? token ?????????!!!! ========> " + searchToken.getToken());
            // System.out.println("3333333333333333333333333333");


            // default null??? ????????? ???????????? ?????? ?????????
            // if - else ???????????? ?????? if??? ????????????
            if (searchToken != null) { // ????????? ????????????
                // ???????????? ??????
                tRepository.delete(searchToken);
                // System.out.println("???????????? ?????????");
            // System.out.println("44444444444444444444444444");

            }
            // ?????? ?????? ??? DB ??????
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

    // ??????-?????? ????????? ?????? ??????
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

    // ????????????
    // LogoutHandler??? ????????? ???????????? ??????
    // 127.0.0.1:8080/ROOT/api/member/logout.json
    @PostMapping(value = "/logout.json")
    public Map<String, Object> logout1GET(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication auth,
            @RequestHeader(name = "TOKEN") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // LogoutHandler ??????
            new SecurityContextLogoutHandler().logout(request, response, auth);
            // DB?????? ?????? ??????
            tRepository.deleteById(token);
            map.put("status", 200);
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // ?????? ???????????? ??????
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